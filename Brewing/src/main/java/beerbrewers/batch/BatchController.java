package beerbrewers.batch;

import beerbrewers.worker.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/batch")
public class BatchController {
    private final BatchService batchService;

    @Autowired
    private BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping
    public BatchResponse getBatches(
            @RequestParam(value = "pageNo",defaultValue = "0",required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize
    ) {
        return batchService.getBatches(pageNo,pageSize);
    }

    @GetMapping(path = "{batchId}")
    public Batch getBatch(@PathVariable("batchId") Long batchId) {
        return batchService.getBatch(batchId);
    }

    @GetMapping(path = "/current")
    public Batch getCurrentBatch() {
        return batchService.getCurrentBatch();
    }

    @PostMapping
    public void saveBatch(@RequestBody Batch batch) {
        batchService.addNewBatch(batch);
    }
}
