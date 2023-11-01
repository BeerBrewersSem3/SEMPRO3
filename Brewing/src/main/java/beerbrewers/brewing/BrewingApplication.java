package beerbrewers.brewing;

/* IMPORTS */
import beerbrewers.BrewMaster9000.OpcUaClientConnection;
import beerbrewers.BrewMaster9000.OpcUaNodes;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "beerbrewers")
public class BrewingApplication {

    @Autowired
    private OpcUaClientConnection opcUaClientConnection;

    public static void main(String[] args) {
        // Initializes the Spring Application
        SpringApplication.run(BrewingApplication.class, args);

    }

    @PostConstruct // Done after
    public void initialize() {
        try {
            // Connects to the OPC UA Server
            opcUaClientConnection.connect();
            // Subscribes tot the StateCurrent node
            opcUaClientConnection.subscribe(OpcUaNodes.STATE_CURRENT);
            // Subscribes to the CntrlCmd node
            opcUaClientConnection.subscribe(OpcUaNodes.CNTRL_CMD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

