package beerbrewers;

import beerbrewers.worker.Worker;
import beerbrewers.worker.WorkerRepository;
import beerbrewers.worker.WorkerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WorkerTests {

    
    private final WorkerRepository workerRepository;
    private final WorkerService workerService;
    private Worker worker;
    private Long workerID;



    @Autowired
    WorkerTests(WorkerRepository workerRepository, WorkerService workerService){
        this.worker = new Worker("Unit test", "Test Unit");
        this.workerRepository = workerRepository;
        this.workerService = workerService;
    }
    @Test
    public void addAndDeleteWorkers() {
        Worker returnedWorker = workerService.addNewWorker(this.worker);
        assertTrue(returnedWorker instanceof Worker);
        assertEquals(returnedWorker.getName(), "Unit test");
        assertTrue(returnedWorker.getWorkerId() instanceof Long);
        assertNotNull(returnedWorker);
        boolean deletedWorker = workerService.deleteWorker(returnedWorker.getWorkerId());
        assertTrue(deletedWorker);
    }

    @Test
    public void getAllWorkers() {
        List<Worker> allWorkers = workerService.getWorkers();
        assertNotNull(allWorkers);
    }

}