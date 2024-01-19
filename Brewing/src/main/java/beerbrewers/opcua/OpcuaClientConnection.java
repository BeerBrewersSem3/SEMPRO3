package beerbrewers.opcua;
import beerbrewers.websocket.WebsocketService;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
@Component
public class OpcuaClientConnection {

    private final WebsocketService websocketService;
    private static final Logger logger = LoggerFactory.getLogger(OpcuaClientConnection.class);

    private OpcUaClient client;
    private boolean isConnected = false;

    private final boolean useSimulation = true;
    private final boolean useDocker = false;
    private String endpointUrl;
    private String hostname;

    @Autowired
    public OpcuaClientConnection(WebsocketService websocketService) {
        this.websocketService = websocketService;
        initializeConnection();
        connectToServer();
    }

    public void initializeConnection() {
        if(useSimulation) {
            if(useDocker) {
                endpointUrl = "opc.tcp://host.docker.internal:4840";
                hostname = "host.docker.internal";
            } else {
                endpointUrl = "opc.tcp://localhost:4840";
                hostname = "localhost";
            }
        } else {
            endpointUrl = "opc.tcp://192.168.0.122:4840";
            hostname = "192.168.0.122";
        }
    }

    public void connectToServer() {
        try{
            List<EndpointDescription> endpointDescriptionList = DiscoveryClient.getEndpoints(endpointUrl).get();
            EndpointDescription configPoint = EndpointUtil.updateUrl(endpointDescriptionList.get(0), hostname, 4840);
            OpcUaClientConfigBuilder configBuilder = new OpcUaClientConfigBuilder();
            configBuilder.setEndpoint(configPoint);
            this.client = OpcUaClient.create(configBuilder.build());
            client.connect().get();
            isConnected = true;
            logger.info("Connected to OPC UA Server");
            websocketService.sendConsoleInfo("Connected to the Beer Machine");
        } catch (UaException | InterruptedException e) {
            logger.error("Connection Error (OPC UA Server)");
            isConnected = false;
        } catch (ExecutionException e) {
            logger.error("Execution Error (OPC UA Server)", e);
            isConnected = false;
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void checkServerStatusAndReconnect() {
        try {
            if(this.client != null &&  isConnected) {
                NodeId nodeId = new NodeId( OpcUaNode.STATE_CURRENT.getNamespaceIndex(),
                        OpcUaNode.STATE_CURRENT.getIdentifier());
                client.readValue(0, TimestampsToReturn.Both, nodeId).get();
                logger.info("Connection to OPC UA Server: OK");
            } else {
                logger.info("Reconnecting to the OPC UA Server");
                websocketService.sendConsoleInfo("[ERROR] Lost connection to the Beer Machine, reconnecting...");
                connectToServer();
            }
        } catch (Exception e) {
            logger.error("[ERROR] Lost connection to the Beer Machine, reconnecting...");
            isConnected = false;
            connectToServer();
        }
    }

    public OpcUaClient getClient() {
        return this.client;
    }
}
