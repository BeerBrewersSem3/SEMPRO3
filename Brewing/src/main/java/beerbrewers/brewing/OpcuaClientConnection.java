package beerbrewers.brewing;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@Component
public class OpcuaClientConnection {
    private String state = "";

    private OpcUaClient client;
    {
        try {
            client = OpcUaClient.create(
                    "opc.tcp://localhost:4840"
            );

            client.connect().get();
            System.out.println("Connected");

        } catch (UaException e) {
            System.out.println("Connection Error");
        } catch (ExecutionException e) {
            System.out.println("Execution Error");
        } catch (InterruptedException e) {
            System.out.println("Connection Error");
        }
    }

    public void subscribe(OpcuaNodes node) throws ExecutionException, InterruptedException {
        NodeId nodeId = new NodeId(node.getNamespaceIndex(),node.getIdentifier());
        UaSubscription uaSubscription = client.getSubscriptionManager().createSubscription(10).get();
        ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);
        MonitoringParameters monitoringParameters = new MonitoringParameters(Unsigned.uint(1), 10.0, null, Unsigned.uint(10), true);
        MonitoredItemCreateRequest monitoredItemCreateRequest = new MonitoredItemCreateRequest(readValueId, MonitoringMode.Reporting, monitoringParameters);
        CompletableFuture<List<UaMonitoredItem>> completableFuture = uaSubscription.createMonitoredItems(TimestampsToReturn.Both, List.of(monitoredItemCreateRequest));
        completableFuture.thenAccept(items -> {
            for (UaMonitoredItem item : items) {
                item.setValueConsumer((moniteredItem, dataValue) -> {
                    System.out.println(dataValue.getValue().getValue());
                    state = dataValue.getValue() + "";
                });
            }
        });
    }

    public String getState() {
        return state;
    }
}
