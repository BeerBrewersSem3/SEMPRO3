package beerbrewers;

import beerbrewers.batch.Batch;
import beerbrewers.batch.BatchRepository;
import beerbrewers.batch.BatchService;
import beerbrewers.batch.BrewEnum;
import beerbrewers.opcua.OpcuaNodes;
import beerbrewers.operation.Operation;
import beerbrewers.sensor.Sensor;
import beerbrewers.sensor.SensorRepository;
import beerbrewers.sensor.SensorService;
import beerbrewers.worker.Worker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BatchTests {


    private final BatchRepository batchRepository;
    private final BatchService batchService;
    private Batch batch;



    @Autowired
    BatchTests(BatchRepository batchRepository, BatchService batchService){
        this.batchRepository = batchRepository;
        this.batchService = batchService;
        //this.batch = new Batch();
    }

    @Test
    public void getBatches() {
        List<Batch> allBatches = batchService.getBatches();
        assertNotNull(allBatches);
    }

    @Test
    public void addNewBatch() {
        Worker worker = new Worker("Unit test", "Test Unit");
        Operation operation = new Operation(worker);
        BrewEnum brewName = BrewEnum.PILSNER;
        this.batch = new Batch(operation, brewName, 100L, 100L);
    }


    @Test
    public void nodesListeningToBatch() {
        List<OpcuaNodes> subscribedNodes = this.batchService.getSubscribedNodes();
        assertNotNull(subscribedNodes);
        assertTrue(subscribedNodes.size() > 0);
    }

}