package beerbrewers.opcua;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
@Component
public class OpcuaClientConnection {

    private OpcUaClient client;
    {
        try {
            client = OpcUaClient.create(
                    "opc.tcp://localhost:4840"
            );

            client.connect().get();
            System.out.println("Connected to OPC UA Sever");

        } catch (UaException | InterruptedException e) {
            System.out.println("Connection Error (OPC UA Server)");
        } catch (ExecutionException e) {
            System.out.println("Execution Error (OPC UA Server)");
        }
    }
}
