package beerbrewers.BrewMaster9000.opcua;

/* IMPORTS */
import jakarta.annotation.PostConstruct;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component  // Automatcally detect dependency injections
public class OpcUaClientConnection {

    private OpcUaClient client;


    // Endpoint URL for the OPC UA Server of the BeerMachine
    private String endPointUrl = "opc.tcp://127.0.0.1:4840";

    // Constructor initializes the OPC UA Client with the OPC UA Server endpoint
    public OpcUaClientConnection() throws Exception {
        this.client = OpcUaClient.create(endPointUrl);
    }
    @PostConstruct
    private void connectToServer(){
        try {
            client.connect().get();
            System.out.println("Connected to OPC UA Server");
        } catch (Exception e) {
            System.out.println("Error connecting to the OPC UA Server: " + e.getMessage());
            System.out.println(e.getMessage());
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