package beerbrewers.brewing;

/* IMPORTS */
import beerbrewers.BrewMaster9000.OpcUaClientConnection;
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

    @PostConstruct
    public void initialize() {
        try {
            // Connects to the OPC UA server

            // Subscribes to the OPC UA server
            opcUaClientConnection.connectAndSubscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** int value between 0-19.
     *  See BeerMachineSpecification for more information
     * @return currentState
     */
    public String getCurrentState() {
        return opcUaClientConnection.getCurrentState();
    }

    /** Sends an int/command to the BeerMachine CntrlCmd (0-5)
     * @param number
     * @throws Exception
     */
    public void sendCommand(int number) throws Exception {
        opcUaClientConnection.sendCommand(number);
    }

    /** Sends a boolean to the BeerMachine CmdChangeRequest.
     *  If (true) the machine will execute command from CntrlCmd
     * @param value
     * @throws Exception
     */
    public void executeCommand(boolean value) throws Exception {
        opcUaClientConnection.executeCommand(value);
    }
}

