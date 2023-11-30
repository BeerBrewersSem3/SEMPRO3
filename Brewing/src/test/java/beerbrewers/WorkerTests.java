package beerbrewers;

import beerbrewers.worker.Worker;
import beerbrewers.worker.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class WorkerTests {

    
    private final WorkerRepository workerRepository;



    @Autowired
    WorkerTests(WorkerRepository workerRepository1){
        this.workerRepository = workerRepository1;
    }
    @Test
    public void testAddNewWorker() {
        Worker worker = new Worker("Unit test", "Test Unit");

    }
//    @Test
//    public void testDataSourceInjection() {
//        assertTrue(supabaseConnection instanceof SupabaseConnection);
//        assertNotNull(supabaseConnection);
//        assertNotNull(supabaseConnection.testQueryForDatabase());
//    }
}