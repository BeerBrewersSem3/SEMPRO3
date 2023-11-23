package beerbrewers.machine;

import beerbrewers.batch.Batch;
import beerbrewers.batch.BatchService;
import beerbrewers.batch.BrewEnum;
import beerbrewers.operation.Operation;
import beerbrewers.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;

@Controller
public class MachineController {
    private Batch batch;
    private final MachineService machineService;

    private static final Logger logger = LoggerFactory.getLogger(MachineController.class);
    @Autowired
    public MachineController(MachineService machineService, BatchService batchService) {
        this.machineService = machineService;

    }

    @MessageMapping("batch/newBatch")
    public void sendBatch(Map<String, String> batchMap) {
        // Testing retrieval of batch data
        int brewId =  Integer.parseInt(batchMap.get("brewType"));
        long batchAmount = Long.parseLong(batchMap.get("batchAmount"));
        long batchSpeed = Long.parseLong(batchMap.get("batchSpeed"));


        machineService.startBatch(brewId, batchAmount, batchSpeed);
        logger.info("\n type: " + brewId +
                "\n amount: " + batchAmount +
                "\n speed: " + batchSpeed);
    }
}
