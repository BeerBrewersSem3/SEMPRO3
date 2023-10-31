package beerbrewers.BrewMaster9000;

import beerbrewers.brewing.BrewingApplication;
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
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class OpcUaClientConnection {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private OpcUaClient client;
    // Will change to the currentState of the BeerMachine, when connection is made.
    private String currentState = "Not connected";
    // Endpoint URL for the OPC UA Server of the BeerMachine
    private String endPointUrl = "opc.tcp://127.0.0.1:4840";



    // Constructor that initializes the OPC UA Client
    public OpcUaClientConnection() throws Exception {
        this.client = OpcUaClient.create(endPointUrl);
    }

    /** Connects to the OPC UA server and subscribes to node: StateCurrent
     *
     * @throws Exception
     */
    public void connectAndSubscribe() throws Exception {
        client.connect().get();

        UaSubscription subscription = client.getSubscriptionManager().createSubscription(1000.0).get();
        NodeId nodeId = new NodeId(6, "::Program:Cube.Status.StateCurrent");
        ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);
        MonitoringParameters parameters = new MonitoringParameters(
                Unsigned.uint(1),
                1000.0,
                null,
                Unsigned.uint(10),
                true
        );
        MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(
                readValueId, MonitoringMode.Reporting, parameters
        );
        CompletableFuture<List<UaMonitoredItem>> future = subscription.createMonitoredItems(
                org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn.Both,
                List.of(request)
        );

        future.thenAccept(items -> {
            for (UaMonitoredItem item : items) {
                item.setValueConsumer((monitoredItem, dataValue) -> {
                    currentState = "Current State: " + dataValue.getValue();
                    System.out.println(currentState);
                    simpMessagingTemplate.convertAndSend("/sensor/data/currentState", currentState);
                });
            }
        });
    }
    public void sendCommand(int number) throws Exception {
        try {
            NodeId nodeId = new NodeId(6, "::Program:Cube.Command.CntrlCmd");
            client.writeValue(nodeId, DataValue.valueOnly(new Variant(number))).get();
        } catch (Exception e) {
            System.out.println("Error writing number to OPC UA server: " + e.getMessage());
            throw e;
        }
    }

    public void executeCommand(boolean value) throws Exception {
        try {
            NodeId nodeId = new NodeId(6, "::Program:Cube.Command.CmdChangeRequest");
            client.writeValue(nodeId, DataValue.valueOnly(new Variant(value))).get();
        } catch (Exception e) {
            System.out.println("Error writing boolean value to OPC UA server: " + e.getMessage());
            throw e;
        }
    }

    public String getCurrentState() {
        return currentState;
    }

    public void disconnect() throws Exception {
        client.disconnect().get();
    }
}