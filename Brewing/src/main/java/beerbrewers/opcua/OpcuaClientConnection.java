package beerbrewers.opcua;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
@Component
public class OpcuaClientConnection {

    private OpcUaClient client;
    private static final Logger logger = LoggerFactory.getLogger(OpcuaClientConnection.class);

    {
        try {
            boolean useSimulation = true;
            boolean useDocker = true;
            String endpointUrl;
            String hostname;

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
            List<EndpointDescription> endpointDescriptionList = DiscoveryClient.getEndpoints(endpointUrl).get();
            EndpointDescription configPoint = EndpointUtil.updateUrl(endpointDescriptionList.get(0), hostname, 4840);
            OpcUaClientConfigBuilder configBuilder = new OpcUaClientConfigBuilder();
            configBuilder.setEndpoint(configPoint);
            this.client = OpcUaClient.create(configBuilder.build());
            client.connect().get();
            logger.info("Connected to OPC UA Server");
        } catch (UaException | InterruptedException e) {
            logger.error("Connection Error (OPC UA Server)");
        } catch (ExecutionException e) {
            logger.error("Execution Error (OPC UA Server)", e);
        }
    }
    public OpcUaClient getClient() {
        return client;
    }
}
