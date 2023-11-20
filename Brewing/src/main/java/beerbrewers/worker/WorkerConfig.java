package beerbrewers.worker;

import beerbrewers.operation.OperationConfig;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration("workerConfig")
public class WorkerConfig {

    @Bean
    @Transactional
    CommandLineRunner testWorkerCommandLineRunner(WorkerRepository repository) {
        return args -> {
            Worker john = new Worker(
                    "John Smith", "hutlihut"
            );
            Worker jane = new Worker(
                    "Jane Doe", "hunter123"
            );
            repository.saveAll(
                    List.of(john, jane)
            );
        };
    }
}
