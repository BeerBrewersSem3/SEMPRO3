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
    @Autowired
    private SupabaseConnection supabaseConnection;

    @Autowired
    DatabaseTests() {

    }

    @Test
    public void testDataSourceInjection() {
        assertNotNull(supabaseConnection);
        assertTrue(supabaseConnection instanceof SupabaseConnection);
        assertNotNull(supabaseConnection.testQueryForDatabase());
    }
}



