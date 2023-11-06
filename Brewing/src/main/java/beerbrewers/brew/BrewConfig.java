package beerbrewers.brew;


import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BrewConfig {

    /**
     * The following method could be refactored to read the brews from a txt file or JSON document.
     * @param repository
     * @return
     */

    @Bean
    CommandLineRunner brewCommandLineRunner(BrewRepository repository) {
        JsonObject brew1Commands = Json.createObjectBuilder()
                        .add("state commands", "placeholder1")
                        .add("actuator commands", "placeholder1")
                        .add("OPCUA id", "placeholder1")
                .build();

        JsonObject brew2Commands = Json.createObjectBuilder()
                .add("state commands", "placeholder2")
                .add("actuator commands", "placeholder2")
                .add("OPCUA id", "placeholder2")
                .build();

        JsonObject brew3Commands = Json.createObjectBuilder()
                .add("state commands", "placeholder3")
                .add("actuator commands", "placeholder3")
                .add("OPCUA id", "placeholder3")
                .build();

        return args -> {
            Brew brew1 = new Brew(1, "Dark Sensation", "Dark Ale 5,8%.", brew1Commands.toString());
            Brew brew2 = new Brew(2, "Fruity Palms", "Wheat Beer 4,8%.", brew2Commands.toString());
            Brew brew3 = new Brew(3, "Black Attack", "Imperial Stout 8,8%.", brew3Commands.toString());

            repository.saveAll(
                    List.of(
                            brew1,
                            brew2,
                            brew3
                    )
            );
        };
    }
}
