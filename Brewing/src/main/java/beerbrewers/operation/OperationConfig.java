package beerbrewers.operation;

import beerbrewers.worker.WorkerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;

@Configuration("operationConfig")
@DependsOn("workerConfig")
public class OperationConfig {

    private final WorkerRepository workerRepository;

    public OperationConfig(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    @Bean
    CommandLineRunner operationCommandLineRunner(OperationRepository repository) {
        return args -> {
                Operation testOperation = new Operation();

                testOperation.setWorker(workerRepository.findByWorkerId(1L));
                repository.save(testOperation);

        };
    }

}
