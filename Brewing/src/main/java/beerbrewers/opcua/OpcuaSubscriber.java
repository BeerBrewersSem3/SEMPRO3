package beerbrewers.opcua;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@Service
public class OpcuaSubscriber {

    private String state = "";
    private OpcuaClientConnection connection;
    @Autowired
    private OpcUaNodeUpdateManager opcUaNodeUpdateManager;
    @Autowired
    public OpcuaSubscriber(OpcuaClientConnection opcuaClientConnection, OpcUaNodeUpdateManager opcUaNodeUpdateManager){
        this.connection = opcuaClientConnection;
        //this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
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
                    //System.out.println(dataValue.getValue().getValue());
                    state = dataValue.getValue() + "";
                    opcUaNodeUpdateManager.notifyObservers(node, state);
                });
            }
        });
    }
}
