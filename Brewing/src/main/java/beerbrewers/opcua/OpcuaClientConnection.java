package beerbrewers.opcua;
import beerbrewers.websocket.WebsocketService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
@Component
public class OpcuaClientConnection {

    private OpcUaClient client;
    private static final Logger logger = LoggerFactory.getLogger(OpcuaClientConnection.class);

    {
        try {
            client = OpcUaClient.create(
                    "opc.tcp://localhost:4840"
            );

            client.connect().get();
            logger.info("Connected to OPC UA Sever");

        } catch (UaException | InterruptedException e) {
            logger.error("Connection Error (OPC UA Server)");
        } catch (ExecutionException e) {
            logger.error("Execution Error (OPC UA Server)");
        }
    }
    public OpcUaClient getClient() {
        return client;
    }
}
