package beerbrewers.machine;

import beerbrewers.batch.Batch;
import beerbrewers.batch.BatchService;
import beerbrewers.batch.BrewEnum;
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

    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;
    private final OperationService operationService;
    private final BatchService batchService;
    private final OpcuaCommander opcUaCommander;
    private CountDownLatch latch;
    private OpcuaNodes awaitingNode;
    private static final Logger logger = LoggerFactory.getLogger(MachineService.class);
    private BrewEnum[] brewEnums = {BrewEnum.PILSNER, BrewEnum.WHEAT, BrewEnum.IPA, BrewEnum.STOUT, BrewEnum.ALE, BrewEnum.ALCOHOL_FREE};


    @Autowired
    public MachineService(OpcUaNodeUpdateManager    opcUaNodeUpdateManager,
                          OperationService          operationService,
                          BatchService              batchService,
                          OpcuaCommander            opcuaCommander) {
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
        this.operationService = operationService;
        this.batchService = batchService;
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
        /*Batch batch = new Batch(1L,new Operation(new Worker("Henrik","1234")),BrewEnum.ALCOHOL_FREE,100L,200L,false,0L,0L, new Timestamp(2023,11,16,20,15,20,0));
        startBatch(batch);

         */


    }
    public boolean startBatch(int brewId, long batchAmount, long batchSpeed){
        Batch batch = new Batch(operationService.getCurrentRunningOperation(),brewEnums[brewId],batchAmount,batchSpeed);
        Long batchId = batchService.saveBatchAndGetId(batch);
        batch.setBatchId(batchId);

        startBatch(batch);
        return true;
    }
    public boolean startBatch(Batch batch){


        resetLatchAndSendCommand(OpcuaNodes.MACH_SPEED_WRITE,(float)batch.getSpeed());
        resetLatchAndSendCommand(OpcuaNodes.NEXT_BATCH_ID,batch.getBatchId().floatValue());
        resetLatchAndSendCommand(OpcuaNodes.NEXT_PRODUCT_ID,(float)batch.getBrewName().getBrewId());
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
        logger.debug("Node: " + node.getName() + " has new value of: " + newState);
        if(awaitingNode != null && node.getName().equals(awaitingNode.getName())) {
            logger.debug("INSIDE: Node: " + node.getName() + " has new value of: " + newState);
            latch.countDown();
        }

    }
}
