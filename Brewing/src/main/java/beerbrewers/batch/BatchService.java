package beerbrewers.batch;

import beerbrewers.opcua.OpcUaNodeObserver;
import beerbrewers.opcua.OpcUaNodeUpdateManager;
import beerbrewers.opcua.OpcuaNodes;
import beerbrewers.operation.OperationService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BatchService implements OpcUaNodeObserver {
    private final BatchRepository batchRepository;
    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;
    private final OperationService operationService;
    private Batch currentBatch;
    private BrewEnum[] brewEnums = {BrewEnum.PILSNER, BrewEnum.WHEAT, BrewEnum.IPA, BrewEnum.STOUT, BrewEnum.ALE, BrewEnum.ALCOHOL_FREE};


    @Autowired
    public BatchService(BatchRepository batchRepository,
                        OpcUaNodeUpdateManager opcUaNodeUpdateManager,
                        OperationService operationService) {
        this.batchRepository = batchRepository;
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
        this.operationService = operationService;
    }

    @PostConstruct
    public void initializeSubscription(){
        List<OpcuaNodes> nodesToSubscribe = List.of(
                OpcuaNodes.PROD_PRODUCED,
                OpcuaNodes.PROD_DEFECTIVE_COUNT
        );
        nodesToSubscribe.forEach(node -> {
            opcUaNodeUpdateManager.addObserver(node, this);
        });
    }

    public List<Batch> getBatches() {
        return batchRepository.findAll();
    }

    public void addNewBatch(Batch batch) {
        batchRepository.save(batch);
    }

    @Transactional
    public Batch saveAndGetBatch(int brewId, long batchAmount, long batchSpeed) {
        // Create batch instance
        Batch batch = new Batch(operationService.getCurrentRunningOperation(),brewEnums[brewId],batchAmount,batchSpeed);
        // Save the entity to the database
        batchRepository.save(batch);
        currentBatch = batch;
        // Return the batch with ID
        return batch;
    }

    @Transactional
    public Batch getLatestBatch(){
        return batchRepository.findTopByOrderByBatchIdDesc();
    }

    @Override
    public void onNodeUpdate(OpcuaNodes node, String newState) {
        if(currentBatch != null) {
            switch(node){
                case PROD_PRODUCED -> {
                    currentBatch.setCompletedCount(Integer.parseInt(newState));
                    if(currentBatch.getAmount() == Integer.parseInt(newState)) {

                        currentBatch.setCompleted(true);
                        currentBatch.setEndTime(new Timestamp(System.currentTimeMillis()));
                        batchRepository.save(currentBatch);
                    }
                }
                case PROD_DEFECTIVE_COUNT -> {
                    currentBatch.setDefectiveCount(Integer.parseInt(newState));
                }

            }
        }
    }
}
