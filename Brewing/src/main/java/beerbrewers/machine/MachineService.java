package beerbrewers.machine;

import beerbrewers.batch.Batch;
import beerbrewers.batch.BatchService;
import beerbrewers.opcua.*;
import beerbrewers.operation.OperationService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class MachineService implements OpcUaNodeObserver {


    private final BatchService batchService;
    private final OpcuaCommander opcUaCommander;
    private CountDownLatch latch;
    private OpcuaNodes awaitingNode;
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

    @Autowired
    public MachineService(BatchService              batchService,
                          OpcuaCommander            opcuaCommander) {
        this.batchService = batchService;
        this.opcUaCommander = opcuaCommander;
    }

    public void startBatch(int brewId, long batchAmount, long batchSpeed){
        Batch batch = batchService.saveAndGetBatch(brewId, batchAmount, batchSpeed);
        resetLatchAndSendCommand(OpcuaNodes.MACH_SPEED_WRITE,(float)batch.getSpeed());
        resetLatchAndSendCommand(OpcuaNodes.NEXT_BATCH_ID,batch.getBatchId().floatValue());
        resetLatchAndSendCommand(OpcuaNodes.NEXT_PRODUCT_ID,(float)batch.getBrewName().getBrewId());
        resetLatchAndSendCommand(OpcuaNodes.NEXT_BATCH_AMOUNT,(float)batch.getAmount());
        stopMachine();
        resetLatchAndSendCommand(OpcuaNodes.CNTRL_CMD,1);
        resetLatchAndSendCommand(OpcuaNodes.CNTRL_CMD,2);
    }

    public void stopMachine(){
        resetLatchAndSendCommand(OpcuaNodes.CNTRL_CMD,3);
    }

    private void resetLatchAndSendCommand(OpcuaNodes node, Number command) {
        this.awaitingNode = node;
        this.latch =  new CountDownLatch(1);
        this.opcUaCommander.sendCommand(node, command);
        waitForLatch();
    }

    private void waitForLatch() {
        try{
            latch.await(3,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onNodeUpdate(OpcuaNodes node, String newState) {
        logger.debug("Node: " + node.getName() + " has new value of: " + newState);
        if(awaitingNode != null && node.getName().equals(awaitingNode.getName())) {
            logger.debug("INSIDE: Node: " + node.getName() + " has new value of: " + newState);
            latch.countDown();
        }

    }

    public List<OpcuaNodes> getSubscribedNodes() {
        return subscribedNodes;
    }
}
