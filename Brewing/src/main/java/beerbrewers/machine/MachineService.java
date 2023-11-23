package beerbrewers.machine;

import beerbrewers.batch.Batch;
import beerbrewers.batch.BrewEnum;
import beerbrewers.opcua.*;
import beerbrewers.operation.Operation;
import beerbrewers.worker.Worker;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class MachineService implements OpcUaNodeObserver {

    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;
    private final OpcuaCommander opcUaCommander;
    private CountDownLatch latch;
    private OpcuaNodes awaitingNode;
    private static final Logger logger = LoggerFactory.getLogger(MachineService.class);

    @Autowired
    public MachineService(OpcUaNodeUpdateManager    opcUaNodeUpdateManager,
                          OpcuaCommander            opcuaCommander) {
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
        this.opcUaCommander = opcuaCommander;
    }

    @PostConstruct
    public void initializeSubscription(){
        List<OpcuaNodes> nodesToSubscribe = List.of(
                OpcuaNodes.CMD_CHANGE_REQUEST,
                OpcuaNodes.CNTRL_CMD,
                OpcuaNodes.STATE_CURRENT,
                OpcuaNodes.MACH_SPEED_WRITE,
                OpcuaNodes.NEXT_BATCH_ID,
                OpcuaNodes.NEXT_PRODUCT_ID,
                OpcuaNodes.NEXT_BATCH_AMOUNT
        );
        nodesToSubscribe.forEach(node -> {
            opcUaNodeUpdateManager.addObserver(node, this);
            logger.info("MachineService subscribed to node: " + node.getName());
        });
        Batch batch = new Batch(1L,new Operation(new Worker("Henrik","1234")),BrewEnum.ALCOHOL_FREE,100L,200L,false,0L,0L, new Timestamp(2023,11,16,20,15,20,0));
        startBatch(batch);
    }
    public boolean startBatch(Batch batch){

        resetLatchAndSendCommand(OpcuaNodes.MACH_SPEED_WRITE,(float)batch.getSpeed());
        resetLatchAndSendCommand(OpcuaNodes.NEXT_BATCH_ID,batch.getBatchId().floatValue());
        resetLatchAndSendCommand(OpcuaNodes.NEXT_PRODUCT_ID,(float)batch.getBrewName().getId());
        resetLatchAndSendCommand(OpcuaNodes.NEXT_BATCH_AMOUNT,(float)batch.getAmount());
        // TODO: Wait for all latch
        resetLatchAndSendCommand(OpcuaNodes.CNTRL_CMD,1);
        waitForLatch();
        resetLatchAndSendCommand(OpcuaNodes.CNTRL_CMD,2);
        waitForLatch();
        return true;
    }

    public void resetLatchAndSendCommand(OpcuaNodes node, Number command) {
        this.awaitingNode = node;
        this.latch =  new CountDownLatch(1);
        this.opcUaCommander.sendCommand(node, command);
    }

    private void waitForLatch() {
        try{
            latch.await(1,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onNodeUpdate(OpcuaNodes node, String newState) {
        System.out.println("Node: " + node.getName() + " has new value of: " + newState);
        if (node.getName().equals(awaitingNode.getName())) {
            System.out.println("INSIDE: Node: " + node.getName() + " has new value of: " + newState);
            latch.countDown();
        }
    }
}
