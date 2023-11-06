package beerbrewers.BrewMaster9000.opcua;

/* IMPORTS */
import jakarta.annotation.PostConstruct;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component  // Automatcally detect dependency injections
public class OpcUaClientConnection {

    private static final Logger logger = LoggerFactory.getLogger(OpcUaClientConnection.class);

    private OpcUaClient client;


    // Endpoint URL for the OPC UA Server of the BeerMachine
    @Value("${opcua.client.endpointUrl}")
    private String endPointUrl;

    @PostConstruct
    private void initialize() throws Exception {
        this.client = OpcUaClient.create(endPointUrl);
        connectToServer();
    }

    private void connectToServer(){
        try {
            client.connect().get();
            logger.info("Connected to OPC UA Server");
        } catch (Exception e) {
            logger.error("Error connecting to the OPC UA Server: " + e.getMessage());
        }
    }

    // Disconnects from the OPC UA server
    public void disconnect() throws Exception {
        client.disconnect().get();
    }

    public OpcUaClient getClient() {
        return client;
    }
}