package beerbrewers.brewing;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrewingApplication
{
    @Autowired
    private OpcuaClientConnection opcuaClientConnection;
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