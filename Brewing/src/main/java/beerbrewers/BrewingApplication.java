package beerbrewers;

/* IMPORTS */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class BrewingApplication {

    public static void main(String[] args) {
        // Initializes the Spring Application
        SpringApplication.run(BrewingApplication.class, args);

    }
}

