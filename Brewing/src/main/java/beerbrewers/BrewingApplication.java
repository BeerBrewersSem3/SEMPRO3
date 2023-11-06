package beerbrewers;

/* IMPORTS */
import beerbrewers.BrewMaster9000.opcua.OpcUaClientSubscriber;
import beerbrewers.BrewMaster9000.opcua.OpcUaNodes;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrewingApplication {

    @Autowired
    private OpcUaClientSubscriber opcUaClientSubscriber;

    public static void main(String[] args) {
        // Initializes the Spring Application
        SpringApplication.run(BrewingApplication.class, args);

    }

    @PostConstruct // Done after
    public void initialize() {
        try {

            // Subscribes tot the StateCurrent node
            opcUaClientSubscriber.subscribe(OpcUaNodes.STATE_CURRENT);
            // Subscribes to the CntrlCmd node
            opcUaClientSubscriber.subscribe(OpcUaNodes.CNTRL_CMD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

