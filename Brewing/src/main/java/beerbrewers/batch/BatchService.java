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
}
