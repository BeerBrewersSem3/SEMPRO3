package beerbrewers.operationtype;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OperationTypeConfig {

    @Bean
    CommandLineRunner operationTypeCommandLineRunner(OperationTypeRepository repository) {

        return args -> {
            OperationType operationType1 = new OperationType(1, "Start");
            OperationType operationType2 = new OperationType(2, "Stop");
            OperationType operationType3 = new OperationType(3, "Emergency Stop");
            OperationType operationType4 = new OperationType(4, "Pause");
            OperationType operationType5 = new OperationType(5, "Placeholder");
            OperationType operationType6 = new OperationType(6, "Placeholder");

            repository.saveAll(
                    List.of(
                            operationType1,
                            operationType2,
                            operationType3,
                            operationType4,
                            operationType5,
                            operationType6
                    )
            );
        };
    }
}
