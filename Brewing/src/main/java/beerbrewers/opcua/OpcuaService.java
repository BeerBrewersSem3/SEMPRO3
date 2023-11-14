package beerbrewers.opcua;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpcuaService {

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
   }
}


