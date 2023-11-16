package beerbrewers;

import beerbrewers.opcua.OpcuaClientConnection;
import beerbrewers.persistence.SupabaseConnection;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private SupabaseConnection supabaseConnection;
    @PostConstruct
    public void initialize(){
        supabaseConnection.testQueryForDatabase();
    }
    public static void main(String[] args)
    {
        SpringApplication.run(BrewingApplication.class, args);
    }

}