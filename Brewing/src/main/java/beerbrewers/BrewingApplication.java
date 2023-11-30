package beerbrewers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("beerbrewers")
@ComponentScan({"beerbrewers","beerbrewers.*"})
@EntityScan("beerbrewers")
public class BrewingApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(BrewingApplication.class, args);
    }

}