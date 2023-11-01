package beerbrewers.BrewMaster9000;

/* IMPORTS */
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Component  // Automatcally detect dependency injections
public class OpcUaClientConnection {
    @Autowired  // Automatically provide a singleton class
    private SimpMessagingTemplate simpMessagingTemplate;

    private OpcUaClient client;
    // Will change to the currentState of the BeerMachine, when connection is made.
    private Map<OpcUaNodes, String> nodeStates = new ConcurrentHashMap<>();

    // Endpoint URL for the OPC UA Server of the BeerMachine
    private String endPointUrl = "opc.tcp://127.0.0.1:4840";

    // Constructor initializes the OPC UA Client with the OPC UA Server endpoint
    public OpcUaClientConnection() throws Exception {
        this.client = OpcUaClient.create(endPointUrl);
    }

    /** Connects to the OPC UA Server
     *
     * @return
     * @throws Exception
     */
    public boolean connect() throws Exception {
        try {
            client.connect().get();
            return true;
        } catch (Exception e) {
            System.out.println("Error connecting to OPC UA server: " + e.getMessage());
            throw e;
        }
    }

    /** Subscribes to the specified node
     *  on the OPC UA Server.
     * @param node The node (from the OpcUaNodes enum) to subscribe to.
     * @throws Exception
     */
    public void subscribe(OpcUaNodes node){
        UaSubscription subscription = null;
        // Tries to create a subscription to the OPC UA Server
        try {
            subscription = client.getSubscriptionManager().createSubscription(10.0).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        // Creating a node ID of the node provided
        NodeId nodeId = new NodeId(node.getNamespaceIndex(), node.getIdentifier());

        // Creating a ReadValueId for the node provided in nodeId
        ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);

        // Defining parameters for the monitoring // subscription
        MonitoringParameters parameters = new MonitoringParameters(
                Unsigned.uint(1), //
                1000.0, // Sampling interval (ms)
                null,   // Filter
                Unsigned.uint(10), // Queue size
                true // Discard oldest value when queue is full
        );
        // Creating a request to monitor the node
        MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(
                readValueId, MonitoringMode.Reporting, parameters
        );
        // Send the request to create the monitoring which returns a CompleteAbleFurture<List<MonitoredItems>>
        CompletableFuture<List<UaMonitoredItem>> future = subscription.createMonitoredItems(
                org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn.Both,
                List.of(request)
        );

        // For every update of the monitored item
        future.thenAccept(items -> {
            for (UaMonitoredItem item : items) {
                // For each monitoredItem, we will use the dataValue
                item.setValueConsumer((monitoredItem, dataValue) -> {
                    // String for webapplication
                    String stateValue = "Current State of " + node.getName() + ": " + dataValue.getValue();
                    // Map of all nodes' value
                    nodeStates.put(node, stateValue);
                    // Prints the value
                    System.out.println(stateValue);
                    // Send the new value to the appropriate websocket destination
                    simpMessagingTemplate.convertAndSend("/sensor/data/" + node.getName(), stateValue);
                });
            }
        });
    }
    public void sendCommand(int number) throws Exception {
        try {
            // Creating NodeId of the CntrlCmd node
            NodeId nodeId = new NodeId(6, "::Program:Cube.Command.CntrlCmd");
            // Writing the int as the value of the node on the OPC UA Server
            client.writeValue(nodeId, DataValue.valueOnly(new Variant(number))).get();
        } catch (Exception e) {
            System.out.println("Error writing number to OPC UA server: " + e.getMessage());
            throw e;
        }
    }

    /**
     * When executed with parameter true,
     * the BeerMachine will execute the command that is currently set in CntrlCmd.
     * @param value The boolean value to write to the OPC UA server.
     * @throws Exception
     */
    public void executeCommand(boolean value) throws Exception {
        try {
            // Creating NodeID of the CmdChangeRequest node
            NodeId nodeId = new NodeId(6, "::Program:Cube.Command.CmdChangeRequest");
            // Writing the value on the OPC UA server to the boolean provided
            client.writeValue(nodeId, DataValue.valueOnly(new Variant(value))).get();
        } catch (Exception e) {
            System.out.println("Error writing boolean value to OPC UA server: " + e.getMessage());
            throw e;
        }
    }
    // Returns the current state of the BeerMachine
    public String getState(OpcUaNodes node) {
        return nodeStates.getOrDefault(node, "Not connected");
    }

    // Disconnects from the OPC UA server
    public void disconnect() throws Exception {
        client.disconnect().get();
    }
}