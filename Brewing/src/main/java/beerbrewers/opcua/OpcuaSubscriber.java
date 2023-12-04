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
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    public OpcuaSubscriber(OpcuaClientConnection opcuaClientConnection, OpcUaNodeUpdateManager opcUaNodeUpdateManager){
        this.connection = opcuaClientConnection;
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
    }
    @PostConstruct
    public void intializeSubscription() {
        try {
            subscribe(OpcuaNodes.STATE_CURRENT);
            subscribe(OpcuaNodes.TEMPERATURE);
            subscribe(OpcuaNodes.REL_HUMIDITY);
            subscribe(OpcuaNodes.VIBRATION);
            subscribe(OpcuaNodes.CURRENT_BATCH_ID);
            subscribe(OpcuaNodes.CURRENT_BATCH_AMOUNT);
            subscribe(OpcuaNodes.CUR_MACH_SPEED);
            subscribe(OpcuaNodes.PROD_PRODUCED);
            subscribe(OpcuaNodes.PROD_PROCESSED_COUNT);
            subscribe(OpcuaNodes.PROD_DEFECTIVE_COUNT);
            subscribe(OpcuaNodes.MAINTENANCE_COUNTER);
            subscribe(OpcuaNodes.MAINTENANCE_TRIGGER);
            subscribe(OpcuaNodes.MACH_SPEED_WRITE);
            subscribe(OpcuaNodes.NEXT_BATCH_ID);
            subscribe(OpcuaNodes.NEXT_PRODUCT_ID);
            subscribe(OpcuaNodes.NEXT_BATCH_AMOUNT);
            subscribe(OpcuaNodes.CMD_CHANGE_REQUEST);
            subscribe(OpcuaNodes.CNTRL_CMD);
            subscribe(OpcuaNodes.BARLEY);
            subscribe(OpcuaNodes.WHEAT);
            subscribe(OpcuaNodes.HOPS);
            subscribe(OpcuaNodes.MALT);
            subscribe(OpcuaNodes.YEAST);
            subscribe(OpcuaNodes.FILLING_INVENTORY);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(OpcuaNodes node) throws ExecutionException, InterruptedException {
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
