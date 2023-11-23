package beerbrewers.batch;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

    @Transactional
    public Long saveBatchAndGetId(Batch batch) {
        // Save the entity to the database
        batchRepository.save(batch);
        // Return the auto-generated ID
        return batch.getBatchId();
    }

    @Transactional
    public Batch getLatestBatch(){
        return batchRepository.findTopByOrderByBatchIdDesc();
    }
}
