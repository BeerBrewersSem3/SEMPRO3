package beerbrewers.BrewMaster9000.opcua;

/* IMPORTS */
import jakarta.annotation.PostConstruct;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.cert.X509Certificate;
import java.util.List;

@Component  // Automatcally detect dependency injections
public class OpcUaClientConnection {

    private static final Logger logger = LoggerFactory.getLogger(OpcUaClientConnection.class);

    private OpcUaClient client;


    // Endpoint URL for the OPC UA Server of the BeerMachine
    @Value("${opcua.client.endpointUrl}")
    private String endPointUrl;

    @PostConstruct
    private void initialize() throws Exception {
        connectToServer();
    }

    private void connectToServer(){
        try {
            List<EndpointDescription> endpointDescriptionList = DiscoveryClient.getEndpoints("opc.tcp://192.168.0.122:4840").get();
            EndpointDescription configPoint = EndpointUtil.updateUrl(endpointDescriptionList.get(0),"192.168.0.122",4840);

            OpcUaClientConfigBuilder configBuilder = new OpcUaClientConfigBuilder();
            configBuilder.setEndpoint(configPoint);
            this.client = OpcUaClient.create(configBuilder.build());
            client.connect().get();
            logger.info("Connected to OPC UA Server");

        } catch (Exception e) {
            logger.error("Error connecting to the OPC UA Server: " + e.getMessage());
        }
    }

    // Disconnects from the OPC UA server
    public void disconnect() throws Exception {
        client.disconnect().get();
    }

    public OpcUaClient getClient() {
        return client;
    }
}