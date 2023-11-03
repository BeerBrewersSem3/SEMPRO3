package beerbrewers.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
