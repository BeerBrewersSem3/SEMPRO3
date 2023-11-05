package beerbrewers.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller acts as API to the frontend.
 */
@RestController
@RequestMapping(path = "api/v1/worker")
public class WorkerController {

    private final WorkerService workerService;

    @Autowired
    private WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping
    public List<Worker> getWorkers() {
        return workerService.getWorkers();
    }

    @PostMapping
    public void registerNewWorker(@RequestBody Worker worker) {
        workerService.addNewWorker(worker);
    }

    @DeleteMapping(path = "{workerId}")
    public void deleteWorker(@PathVariable("workerId") Long workerId) {
        workerService.deleteWorker(workerId);
    }

}
