package beerbrewers.BrewMaster9000.opcua;

import jakarta.annotation.PostConstruct;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Component
@DependsOn("dashboardService")
public class OpcUaClientSubscriber {
    private static final Logger logger = LoggerFactory.getLogger(OpcUaClientSubscriber.class);
    @Autowired
    private OpcUaClientConnection opcUaClientConnection;
    @Autowired
    private OpcUaNodeUpdateManager opcUaNodeUpdateManager;
   /* @PostConstruct
    public void initializeSubscriptions(){
        List<OpcUaNodes> nodesToSubscribe = List.of(
                OpcUaNodes.STATE_CURRENT,
                OpcUaNodes.MACH_SPEED_READ,
                OpcUaNodes.PROD_DEFECTIVE_COUNT,
                OpcUaNodes.PROD_PROCESSED_COUNT,
                //OpcUaNodes.STOP_REASON,
                OpcUaNodes.CNTRL_CMD,
                OpcUaNodes.CURRENT_BATCH_ID,
                OpcUaNodes.BATCH_QTY,
                OpcUaNodes.REL_HUMIDITY,
                OpcUaNodes.TEMPERATURE,
                OpcUaNodes.VIBRATION,
                OpcUaNodes.CUR_MACH_SPEED
        );
        nodesToSubscribe.forEach(node -> {
            subscribe(node);
            logger.debug("Subscribed from OpcUaClientSubscriber to node: " + node.getName());
        });
    }

    */

    // Will change to the currentState of the BeerMachine, when connection is made.
    private Map<OpcUaNodes, String> nodeStates = new ConcurrentHashMap<>();

    /** Subscribes to the specified node
     *  on the OPC UA Server.
     * @param node The node (from the OpcUaNodes enum) to subscribe to.
     */

    public void subscribe(OpcUaNodes node){
        UaSubscription subscription = null;
        // Tries to create a subscription to the OPC UA Server
        try {
            subscription = opcUaClientConnection.getClient().getSubscriptionManager().createSubscription(10.0).get();
        } catch (InterruptedException | ExecutionException e) {
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
                            String stateValue = dataValue.getValue().getValue().toString();
                            // Prints the value for dev.
                            logger.debug("New value from OPC UA Server for: " + node.getName() + " = " + stateValue);
                            nodeStates.put(node, dataValue.getValue().getValue().toString());
                            // Send the new value to the appropriate websocket destination
                            opcUaNodeUpdateManager.notifyObservers(node, stateValue);

                        });
                    }
                });
        logger.debug("Subscribed to node: " + node.getName());
    }
}
