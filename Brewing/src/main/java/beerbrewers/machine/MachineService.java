package beerbrewers.machine;

import beerbrewers.batch.Batch;
import beerbrewers.batch.BatchService;
import beerbrewers.opcua.*;
import beerbrewers.websocket.WebsocketService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class MachineService implements OpcUaNodeObserver {


    private final BatchService batchService;
    private final OpcuaCommander opcUaCommander;
    private final WebsocketService websocketService;
    private CountDownLatch batchAttributeLatch = new CountDownLatch(4);
    private CountDownLatch commandLatch;
    private OpcUaNode awaitingNode;
    private String awaitingState;
    private String currentState;
    private static final Logger logger = LoggerFactory.getLogger(MachineService.class);
    private List<OpcUaNode> subscribedNodes = List.of(
            OpcUaNode.CMD_CHANGE_REQUEST,
            OpcUaNode.CNTRL_CMD,
            OpcUaNode.STATE_CURRENT,
            OpcUaNode.MACH_SPEED_WRITE,
            OpcUaNode.NEXT_BATCH_ID,
            OpcUaNode.NEXT_PRODUCT_ID,
            OpcUaNode.NEXT_BATCH_AMOUNT
    );
    private Map<Number, String> commandToStateMap = new ConcurrentHashMap<>();

    private Map<OpcUaNode, Number> awaitingNodes = new ConcurrentHashMap<>();

    @Autowired
    public MachineService(BatchService      batchService,
                          OpcuaCommander    opcuaCommander,
                          WebsocketService  websocketService) {
        this.batchService = batchService;
        this.opcUaCommander = opcuaCommander;
        this.websocketService = websocketService;
    }

    @PostConstruct
    public void initialize(){
        commandToStateMap.put(1,"4");
        commandToStateMap.put(2,"6");
        commandToStateMap.put(3,"2");
        commandToStateMap.put(4,"8");
        commandToStateMap.put(5,"2");
    }


    public void startNewBatch(int brewId, long batchAmount, long batchSpeed){
        Batch batch = batchService.saveAndGetBatch(brewId, batchAmount, batchSpeed);
        setBatchAttributesToMachine(batch);
        startMachine();
        websocketService.send("batchStart","");
    }

    public void addBatchAttributeCommandToQueue(OpcUaNode node, Number command){
        awaitingNodes.put(node,command);
        this.batchAttributeLatch =  new CountDownLatch(awaitingNodes.size());
    }
    public void sendBatchCommands(){
        for(Map.Entry<OpcUaNode, Number> entry : awaitingNodes.entrySet()){
            this.opcUaCommander.sendCommand(entry.getKey(),entry.getValue());
        }
    }

    public void setBatchAttributesToMachine(Batch batch){
        websocketService.sendConsoleInfo("Sending batch information to the machine");
        addBatchAttributeCommandToQueue(OpcUaNode.MACH_SPEED_WRITE,(float)batch.getSpeed());
        addBatchAttributeCommandToQueue(OpcUaNode.NEXT_BATCH_ID,batch.getBatchId().floatValue());
        addBatchAttributeCommandToQueue(OpcUaNode.NEXT_BATCH_AMOUNT,(float)batch.getAmount());
        if(batch.getBrewName().getBrewId() != 0) {
            addBatchAttributeCommandToQueue(OpcUaNode.NEXT_PRODUCT_ID,(float)batch.getBrewName().getBrewId());
        }
        sendBatchCommands();
        waitForLatch(batchAttributeLatch);
        websocketService.sendConsoleInfo("Batch information has been sent to the machine");
    }
    public void setBatchAttributesToMachine(Batch batch, long amount){
        websocketService.sendConsoleInfo("Sending batch information to the machine");
        addBatchAttributeCommandToQueue(OpcUaNode.MACH_SPEED_WRITE,(float)batch.getSpeed());
        addBatchAttributeCommandToQueue(OpcUaNode.NEXT_BATCH_ID,batch.getBatchId().floatValue());
        addBatchAttributeCommandToQueue(OpcUaNode.NEXT_BATCH_AMOUNT,(float)amount);
        if(batch.getBrewName().getBrewId() != 0) {
            addBatchAttributeCommandToQueue(OpcUaNode.NEXT_PRODUCT_ID,(float)batch.getBrewName().getBrewId());
        }
        sendBatchCommands();
        waitForLatch(batchAttributeLatch);
        websocketService.sendConsoleInfo("Batch information has been sent to the machine");
    }


    public void startMachine(){
        websocketService.sendConsoleInfo("Starting brewing process");
        if(!Objects.equals(currentState, "2")) {
            resetLatchAndSendCntrlCmd(OpcUaNode.CNTRL_CMD, 3);
        }
        resetLatchAndSendCntrlCmd(OpcUaNode.CNTRL_CMD,1);
        resetLatchAndSendCntrlCmd(OpcUaNode.CNTRL_CMD,2);
        websocketService.sendConsoleInfo("Brewing process stated");
    }

    public void resetMachineState(){
        resetLatchAndSendCntrlCmd(OpcUaNode.CNTRL_CMD,3);
    }

    public void stopMachine(){
        websocketService.sendConsoleInfo("Stopping machine");
        resetMachineState();
        if(batchService.getCurrentBatch() != null){
            batchService.saveBatch(batchService.getCurrentBatch());
        }
        websocketService.sendConsoleInfo("Machine stopped");
        websocketService.send("batchStart","");
    }

    public void continueBatch(){
        Batch batch = batchService.getCurrentBatch();
        setBatchAttributesToMachine(batch,batch.getAmount()-batch.getCompletedCount());
        startMachine();
        websocketService.send("batchStart","");
    }

    private void resetLatchAndSendCntrlCmd(OpcUaNode node, Number command) {
        this.awaitingNode = OpcUaNode.STATE_CURRENT;
        this.awaitingState = commandToStateMap.get(command);
        this.commandLatch = new CountDownLatch(1);
        this.opcUaCommander.sendCommand(node, command);
        waitForLatch(commandLatch);
    }

    private void waitForLatch(CountDownLatch latch) {
        try{
            latch.await(10,TimeUnit.SECONDS);
            logger.debug("Latch released");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onNodeUpdate(OpcUaNode node, String newState) {
        logger.debug(node + " new value " + newState);
        if(node.getName().equals(OpcUaNode.STATE_CURRENT.getName())
                & awaitingNode == OpcUaNode.STATE_CURRENT
                & Objects.equals(newState, awaitingState)) {
                commandLatch.countDown();
        } else if(awaitingNodes.containsKey(node)){
            awaitingNodes.remove(node);
            logger.debug("Node: " + node.getName() + " has new value of: " + newState);
            batchAttributeLatch.countDown();
        } else if(currentState == null & node.getName().equals("currentState")) {
            currentState = newState;
        }
    }

    public List<OpcUaNode> getSubscribedNodes() {
        return subscribedNodes;
    }
}
