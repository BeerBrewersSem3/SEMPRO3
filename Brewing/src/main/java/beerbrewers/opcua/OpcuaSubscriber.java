package beerbrewers.opcua;

import jakarta.annotation.PostConstruct;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@Service
public class OpcuaSubscriber {

    private String state = "";
    private final OpcuaClientConnection connection;
    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;
    private static final Logger logger = LoggerFactory.getLogger(OpcuaSubscriber.class);

    @Autowired
    public OpcuaSubscriber(OpcuaClientConnection opcuaClientConnection,
                           OpcUaNodeUpdateManager opcUaNodeUpdateManager){
        this.connection = opcuaClientConnection;
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
    }
    @PostConstruct
    public void intializeSubscription() {
        try {
            subscribe(OpcUaNode.STATE_CURRENT);
            subscribe(OpcUaNode.TEMPERATURE);
            subscribe(OpcUaNode.REL_HUMIDITY);
            subscribe(OpcUaNode.VIBRATION);
            subscribe(OpcUaNode.CURRENT_BATCH_ID);
            subscribe(OpcUaNode.CURRENT_BATCH_AMOUNT);
            subscribe(OpcUaNode.CUR_MACH_SPEED);
            subscribe(OpcUaNode.PROD_PRODUCED);
            subscribe(OpcUaNode.PROD_PROCESSED_COUNT);
            subscribe(OpcUaNode.PROD_DEFECTIVE_COUNT);
            subscribe(OpcUaNode.MAINTENANCE_COUNTER);
            subscribe(OpcUaNode.MAINTENANCE_TRIGGER);
            subscribe(OpcUaNode.MACH_SPEED_WRITE);
            subscribe(OpcUaNode.NEXT_BATCH_ID);
            subscribe(OpcUaNode.NEXT_PRODUCT_ID);
            subscribe(OpcUaNode.NEXT_BATCH_AMOUNT);
            subscribe(OpcUaNode.CMD_CHANGE_REQUEST);
            subscribe(OpcUaNode.CNTRL_CMD);
            subscribe(OpcUaNode.BARLEY);
            subscribe(OpcUaNode.WHEAT);
            subscribe(OpcUaNode.HOPS);
            subscribe(OpcUaNode.MALT);
            subscribe(OpcUaNode.YEAST);
            subscribe(OpcUaNode.FILLING_INVENTORY);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    public void subscribe(OpcUaNode node) throws ExecutionException, InterruptedException {
        NodeId nodeId = new NodeId(node.getNamespaceIndex(), node.getIdentifier());
        UaSubscription uaSubscription = connection.getClient().getSubscriptionManager().createSubscription(10).get();
        ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);
        MonitoringParameters monitoringParameters = new MonitoringParameters(Unsigned.uint(1), 10.0, null, Unsigned.uint(10), true);
        MonitoredItemCreateRequest monitoredItemCreateRequest = new MonitoredItemCreateRequest(readValueId, MonitoringMode.Reporting, monitoringParameters);
        CompletableFuture<List<UaMonitoredItem>> completableFuture = uaSubscription.createMonitoredItems(TimestampsToReturn.Both, List.of(monitoredItemCreateRequest));
        completableFuture.thenAccept(items -> {
            for (UaMonitoredItem item : items) {
                item.setValueConsumer((moniteredItem, dataValue) -> {
                    state = dataValue.getValue().getValue().toString();
                    logger.debug("The node " + node.getName() + "has a new value of " + state);
                    opcUaNodeUpdateManager.notifyObservers(node, state);
                });
            }
        });
    }





}
