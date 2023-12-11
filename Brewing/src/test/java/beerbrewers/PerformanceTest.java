package beerbrewers;

import beerbrewers.batch.Batch;
import beerbrewers.batch.BrewEnum;
import beerbrewers.machine.MachineService;
import beerbrewers.operation.Operation;
import beerbrewers.persistence.SupabaseConnection;
import beerbrewers.worker.Worker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PerformanceTest {

    private double allRunTimesSum, allRunTimesAverage;
    private Worker worker;
    private Operation operation;
    private Batch batch;
    @Autowired
    private MachineService machineService;

    @Autowired
    PerformanceTest() {
       this.worker = new Worker(1L,"John John", "MyPassword");
       this.operation= new Operation(1L, worker);
       this.batch = new Batch(1L, operation, BrewEnum.IPA, 100L, 100L);
       this.allRunTimesSum = 0;
    }

    @Test
    public void testStartMachine() {
        ArrayList<Double> allRunTimes = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            long startTime = System.currentTimeMillis();
            machineService.setBatchAttributesToMachine(this.batch);
            machineService.startMachine();
            long endTime = System.currentTimeMillis();
            double timeInSeconds = (double) (endTime-startTime) / 1000;
            System.out.println("Iteration #"+(i)+" just ran at " + timeInSeconds + " seconds");
            allRunTimes.add(timeInSeconds);
        }

        allRunTimes.forEach((e) -> {
            this.allRunTimesSum += e;
        });
        this.allRunTimesAverage = this.allRunTimesSum / allRunTimes.size();
        System.out.println("Dataset: " + allRunTimes);
        System.out.println("Average runtime: " + this.allRunTimesAverage);
        assertTrue(this.allRunTimesAverage < 1.0);
    }
}



