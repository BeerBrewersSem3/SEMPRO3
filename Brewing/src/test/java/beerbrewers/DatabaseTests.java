package beerbrewers;

import beerbrewers.batch.Batch;
import beerbrewers.batch.BrewEnum;
import beerbrewers.machine.MachineService;
import beerbrewers.operation.Operation;
import beerbrewers.persistence.SupabaseConnection;
import beerbrewers.sensorreading.SensorReadingRepository;
import beerbrewers.sensorreading.SensorReadingService;
import beerbrewers.worker.Worker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DatabaseTests {

    private Worker worker;
    private Operation operation;
    private Batch batch;
    @Autowired
    private SupabaseConnection supabaseConnection;
    @Autowired
    private MachineService machineService;

    @Autowired
    DatabaseTests() {
       this.worker = new Worker(1L,"John John", "MyPassword");
       this.operation= new Operation(1L, worker);
       this.batch = new Batch(1L, operation, BrewEnum.IPA, 100L, 100L);
    }

    @Test
    public void testDataSourceInjection() {
        assertNotNull(supabaseConnection);
        assertTrue(supabaseConnection instanceof SupabaseConnection);
        assertNotNull(supabaseConnection.testQueryForDatabase());
    }

    @Test
    public void testStartMachine() {
        // Long batchId, Operation operation, BrewEnum brewName, long amount, long speed, boolean isCompleted,
        long startTime = System.currentTimeMillis();
        machineService.setBatchAttributesToMachine(this.batch);
        machineService.startMachine();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);
    }
}



