package beerbrewers.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchService {
    private final BatchRepository batchRepository;

    @Autowired
    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public List<Batch> getBatches() {
        return batchRepository.findAll();
    }

    public void addNewBatch(Batch batch) {
        batchRepository.save(batch);
    }
<<<<<<< Updated upstream
=======

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

    @Transactional
    public long getNextBatchId() {
        return batchRepository.findTopByOrderByBatchIdDesc().getBatchId()+1;
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
                        saveBatch(currentBatch);
                    }
                }
                case PROD_DEFECTIVE_COUNT -> {
                    currentBatch.setDefectiveCount(Integer.parseInt(newState));
                }

            }
        }
    }

    @Transactional
    public Batch saveBatch(Batch batch){
        if(batchRepository.existsById(batch.getBatchId())){
            currentBatch.setCompletedCount(currentBatch.getCompletedCount()+batch.getCompletedCount());
            currentBatch.setDefectiveCount(currentBatch.getDefectiveCount()+batch.getDefectiveCount());
            batchRepository.save(currentBatch);
            return currentBatch;
        } else {
            batchRepository.save(batch);
            return batch;
        }
    }
    public Batch getCurrentBatch(){
        return currentBatch;
    }

    @Override
    public List<OpcuaNodes> getSubscribedNodes() {
        return subscribesNodes;
    }
    public void setCurrentBatch(Batch batch){
        this.currentBatch = batch;
    }
>>>>>>> Stashed changes
}
