package beerbrewers.brewing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class BrewingApplication
{
    @Autowired
    private OpcuaClientConnection opcuaClientConnection;
    public static void main(String[] args)
    {
        SpringApplication.run(BrewingApplication.class, args);
        SupabaseConnection supabaseConnection = SupabaseConnection.getInstance();
        supabaseConnection.testQueryForDatabase();
    }

}