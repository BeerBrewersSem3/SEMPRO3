package beerbrewers;

import beerbrewers.opcua.OpcuaClientConnection;
import beerbrewers.opcua.OpcuaCommander;
import beerbrewers.opcua.OpcuaNodes;
import beerbrewers.opcua.OpcuaSubscriber;
import beerbrewers.persistence.SupabaseConnection;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
@EnableJpaRepositories("beerbrewers")
@ComponentScan({"beerbrewers","beerbrewers.*"})
@EntityScan("beerbrewers")
public class BrewingApplication
{
    @Autowired
    private OpcuaSubscriber subscriber;
    @Autowired
    private OpcuaCommander commander;
    @Autowired
    private SupabaseConnection supabaseConnection;
    @PostConstruct
    public void initialize(){
        supabaseConnection.testQueryForDatabase();

        try {
            subscriber.subscribe(OpcuaNodes.CNTRL_CMD);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        commander.sendCommand(OpcuaNodes.CNTRL_CMD,2);
        commander.sendCommand(OpcuaNodes.CMD_CHANGE_REQUEST,true);
    }
    public static void main(String[] args)
    {
        SpringApplication.run(BrewingApplication.class, args);
    }

}