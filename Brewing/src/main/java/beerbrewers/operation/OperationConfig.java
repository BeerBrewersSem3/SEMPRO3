package beerbrewers.operation;

import beerbrewers.worker.Worker;
import beerbrewers.worker.WorkerService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;

@Configuration
public class OperationConfig {

    private static final Logger logger = LoggerFactory.getLogger(OperationConfig.class);

    @Autowired
    private final WorkerService workerService;

    public OperationConfig(WorkerService workerService) {
        this.workerService = workerService;
    }

    @Bean
    @Transactional
    CommandLineRunner operationCommandLineRunner(OperationRepository repository) {
        return args -> {
                Worker worker = workerService.findWorker(1L);
                Operation testOperation = new Operation(worker);
                repository.save(testOperation);

        };
    }

}
