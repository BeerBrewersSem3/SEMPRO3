package beerbrewers.brewing;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class BrewingApplication
{
    @Autowired
    private OpcuaClientConnection connection;

    @PostConstruct
    public void test() throws ExecutionException, InterruptedException {
        connection.subscribe(OpcuaNodes.STATE_CURRENT);
    }
    public static void main(String[] args)
    {
        SpringApplication.run(BrewingApplication.class, args);

    }
}