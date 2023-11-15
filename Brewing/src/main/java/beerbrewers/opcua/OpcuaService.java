package beerbrewers.opcua;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpcuaService {

    /*
    private final OpcUaClient opcUaClient;

   @Autowired
    public OpcuaService(OpcuaClientConnection opcuaClientConnection) {
       this.opcUaClient = opcuaClientConnection.getClient();
   }

   public float getCurrentMachineSpeed() {
       try {
            NodeId machineSpeedNodeId = new NodeId(6,"::Program:Cube.Status.CurMachSpeed");
            try {
                float machineSpeed = opcUaClient.readValue(0, machineSpeedNodeId.)
            }
       }
   }*/

    private OpcuaSubscriber subscriber;
    private OpcuaCommander commander;
    @Autowired
    public OpcuaService(OpcuaSubscriber subscriber, OpcuaCommander commander){
        this.subscriber = subscriber;
        this.commander = commander;
        initializeSubscriptions();
    }

    private void initializeSubscriptions() {
        try {
            subscriber.subscribe(OpcuaNodes.CUR_MACH_SPEED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public float getCurrentMachineSpeed() {
        String speedAsString = subscriber.getState();
        try {
            return Float.parseFloat(speedAsString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1.0f; // Replace with actual error handling
        }
    }
}


