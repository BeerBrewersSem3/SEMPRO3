package beerbrewers.worker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class WorkerConfig {

    @Bean
    CommandLineRunner testWorkerCommandLineRunner(WorkerRepository repository) {
        return args -> {
            Worker john = new Worker(
                    1L,"John Smith", "hutlihut"
            );
            Worker jane = new Worker(
                    2L,"Jane Doe", "hunter123"
            );
            repository.saveAll(
                    List.of(john, jane)
            );
        };
    }
}
