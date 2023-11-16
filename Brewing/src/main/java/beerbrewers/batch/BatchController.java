package beerbrewers.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/batch")
public class BatchController {
    private final BatchService batchService;

    @Autowired
    private BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping
    public List<Batch> getBatches() {
        return batchService.getBatches();
    }

    @PostMapping
    public void saveBatch(@RequestBody Batch batch) {
        batchService.addNewBatch(batch);
    }
}
