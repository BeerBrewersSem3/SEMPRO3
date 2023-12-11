package beerbrewers.batch;

import beerbrewers.opcua.OpcUaNodeObserver;
import beerbrewers.opcua.OpcUaNode;
import beerbrewers.operation.OperationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BatchService implements OpcUaNodeObserver {
    private final BatchRepository batchRepository;
    private final OperationService operationService;
    private Batch currentBatch;
    private Brew[] brews = {Brew.PILSNER, Brew.WHEAT, Brew.IPA, Brew.STOUT, Brew.ALE, Brew.ALCOHOL_FREE};
    private List<OpcUaNode> subscribesNodes = List.of(
            OpcUaNode.PROD_PRODUCED,
            OpcUaNode.PROD_DEFECTIVE_COUNT
    );

    @Autowired
    public BatchService(BatchRepository batchRepository,
                        OperationService operationService) {
        this.batchRepository = batchRepository;
        this.operationService = operationService;
    }

    public BatchResponse getBatches(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<Batch> batches = batchRepository.findAll(pageable);
        List<Batch> listOfBatches = batches.getContent();
        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setData(listOfBatches);
        batchResponse.setPageNo(batches.getNumber());
        batchResponse.setPageSize(batches.getSize());
        batchResponse.setTotalElements(batches.getTotalElements());
        batchResponse.setTotalPages(batches.getTotalPages());
        batchResponse.setLast(batches.isLast());

        return batchResponse;
    }

    public Batch getBatch(Long batchId) {
        return batchRepository.findByBatchId(batchId);
    }

    public void addNewBatch(Batch batch) {
        batchRepository.save(batch);
    }

    @Transactional
    public Batch saveAndGetBatch(int brewId, long batchAmount, long batchSpeed) {
        // Create batch instance

        Batch batch = new Batch(operationService.getCurrentRunningOperation(), brews[brewId],batchAmount,batchSpeed);
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
    public void onNodeUpdate(OpcUaNode node, String newState) {
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

    public void saveBatch(Batch batch){
        batchRepository.save(batch);
    }
    public Batch getCurrentBatch(){
        return currentBatch;
    }

    @Override
    public List<OpcUaNode> getSubscribedNodes() {
        return subscribesNodes;
    }
}
