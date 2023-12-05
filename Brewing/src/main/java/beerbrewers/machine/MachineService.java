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
    private CountDownLatch batchAttributeLatch;
    private CountDownLatch commandLatch;
    private OpcuaNodes awaitingNode;
    private String awaitingState;
    private String currentState;
    private static final Logger logger = LoggerFactory.getLogger(MachineService.class);
    private List<OpcuaNodes> subscribedNodes = List.of(
            OpcuaNodes.CMD_CHANGE_REQUEST,
            OpcuaNodes.CNTRL_CMD,
            OpcuaNodes.STATE_CURRENT,
            OpcuaNodes.MACH_SPEED_WRITE,
            OpcuaNodes.NEXT_BATCH_ID,
            OpcuaNodes.NEXT_PRODUCT_ID,
            OpcuaNodes.NEXT_BATCH_AMOUNT
    );
    private Map<Number, String> commandToStateMap = new ConcurrentHashMap<>();

    private Map<OpcuaNodes, Number> awaitingNodes = new ConcurrentHashMap<>();

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

    public void addBatchAttributeCommandToQueue(OpcuaNodes node, Number command){
        awaitingNodes.put(node,command);
        this.batchAttributeLatch =  new CountDownLatch(awaitingNodes.size());
    }
    public void sendBatchCommands(){
        for(Map.Entry<OpcuaNodes, Number> entry : awaitingNodes.entrySet()){
            this.opcUaCommander.sendCommand(entry.getKey(),entry.getValue());
        }
    }

    public void setBatchAttributesToMachine(Batch batch){
        websocketService.sendConsoleInfo("Sending batch information to the machine");
        addBatchAttributeCommandToQueue(OpcuaNodes.MACH_SPEED_WRITE,(float)batch.getSpeed());
        addBatchAttributeCommandToQueue(OpcuaNodes.NEXT_BATCH_ID,batch.getBatchId().floatValue());
        addBatchAttributeCommandToQueue(OpcuaNodes.NEXT_BATCH_AMOUNT,(float)batch.getAmount());
        if(batch.getBrewName().getBrewId() != 0) {
            addBatchAttributeCommandToQueue(OpcuaNodes.NEXT_PRODUCT_ID,(float)batch.getBrewName().getBrewId());
        }
        sendBatchCommands();
        waitForLatch(batchAttributeLatch);
        websocketService.sendConsoleInfo("Batch information has been sent to the machine");
    }
    public void setBatchAttributesToMachine(Batch batch, long amount){
        websocketService.sendConsoleInfo("Sending batch information to the machine");
        addBatchAttributeCommandToQueue(OpcuaNodes.MACH_SPEED_WRITE,(float)batch.getSpeed());
        addBatchAttributeCommandToQueue(OpcuaNodes.NEXT_BATCH_ID,batch.getBatchId().floatValue());
        addBatchAttributeCommandToQueue(OpcuaNodes.NEXT_BATCH_AMOUNT,(float)amount);
        if(batch.getBrewName().getBrewId() != 0) {
            addBatchAttributeCommandToQueue(OpcuaNodes.NEXT_PRODUCT_ID,(float)batch.getBrewName().getBrewId());
        }
        sendBatchCommands();
        waitForLatch(batchAttributeLatch);
        websocketService.sendConsoleInfo("Batch information has been sent to the machine");
    }


    public void startMachine(){
        websocketService.sendConsoleInfo("Starting brewing process");
        if(!Objects.equals(currentState, "2")) {
            resetLatchAndSendCntrlCmd(OpcuaNodes.CNTRL_CMD, 3);
        }
        resetLatchAndSendCntrlCmd(OpcuaNodes.CNTRL_CMD,1);
        resetLatchAndSendCntrlCmd(OpcuaNodes.CNTRL_CMD,2);
        websocketService.sendConsoleInfo("Brewing process stated");
    }

    public void resetMachineState(){
        resetLatchAndSendCntrlCmd(OpcuaNodes.CNTRL_CMD,3);
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


    private void resetLatchAndSendCommand(OpcuaNodes node, Number command) {
        this.awaitingNode = node;
        this.batchAttributeLatch =  new CountDownLatch(1);
        this.opcUaCommander.sendCommand(node, command);
        waitForLatch(batchAttributeLatch);
    }

    private void resetLatchAndSendCntrlCmd(OpcuaNodes node, Number command) {
        this.awaitingNode = OpcuaNodes.STATE_CURRENT;
        this.awaitingState = commandToStateMap.get(command);
        this.commandLatch = new CountDownLatch(1);
        this.opcUaCommander.sendCommand(node, command);
        waitForLatch(commandLatch);
    }

    private void waitForLatch(CountDownLatch latch) {
        try{
            latch.await(10,TimeUnit.SECONDS);
            logger.info("Latch released");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onNodeUpdate(OpcuaNodes node, String newState) {
        logger.debug("Node: " + node.getName() + " has new value of: " + newState);
        logger.info(node + " new value " + newState);
        if(node.getName().equals(OpcuaNodes.STATE_CURRENT.getName())
                & awaitingNode == OpcuaNodes.STATE_CURRENT){
            if(Objects.equals(newState, awaitingState)) {
                commandLatch.countDown();
            }
        }
        if(awaitingNode != null && node.getName().equals(awaitingNode.getName())) {
            logger.debug("INSIDE: Node: " + node.getName() + " has new value of: " + newState);
            batchAttributeLatch.countDown();
        }
        if(awaitingNodes.containsKey(node)){
            awaitingNodes.remove(node);
            logger.info("Node: " + node.getName() + " has new value of: " + newState);
            batchAttributeLatch.countDown();
        }
        if(currentState == null & node.getName().equals("currentState")) {
            currentState = newState;
        }

    }

    public List<OpcuaNodes> getSubscribedNodes() {
        return subscribedNodes;
    }
}
