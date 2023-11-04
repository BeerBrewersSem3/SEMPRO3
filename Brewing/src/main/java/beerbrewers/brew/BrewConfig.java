package beerbrewers.brew;

import org.json.JSONObject;
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
        JSONObject brew1Commands = new JSONObject();
        brew1Commands.put("state commands", "placeholder1");
        brew1Commands.put("actuator commands", "placeholder1");
        brew1Commands.put("OPCUA id", "placeholder1");

        JSONObject brew2Commands = new JSONObject();
        brew2Commands.put("state commands", "placeholder2");
        brew2Commands.put("actuator commands", "placeholder2");
        brew2Commands.put("OPCUA id", "placeholder2");

        JSONObject brew3Commands = new JSONObject();
        brew3Commands.put("state commands", "placeholder3");
        brew3Commands.put("actuator commands", "placeholder3");
        brew3Commands.put("OPCUA id", "placeholder3");

        return args -> {
            Brew brew1 = new Brew(1, "Dark Sensation", "Dark Ale 5,8%.", brew1Commands);
            Brew brew2 = new Brew(2, "Fruity Palms", "Wheat Beer 4,8%.", brew2Commands);
            Brew brew3 = new Brew(3, "Black Attack", "Imperial Stout 8,8%.", brew3Commands);

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
