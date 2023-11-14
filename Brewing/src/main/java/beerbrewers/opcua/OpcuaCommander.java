package beerbrewers.opcua;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class OpcuaCommander {

    private OpcuaClientConnection connection;
    @Autowired
    public OpcuaCommander(OpcuaClientConnection opcuaClientConnection){
        this.connection = opcuaClientConnection;
    }

    public boolean sendCommand(OpcuaNodes node, int command){
        NodeId nodeId = new NodeId(node.getNamespaceIndex(), node.getIdentifier());
        try {
            connection.getClient().writeValue(nodeId, DataValue.valueOnly(new Variant(command))).get();
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean commandChangeRequest(boolean command){
        NodeId nodeId = new NodeId(OpcuaNodes.CMD_CHANGE_REQUEST.getNamespaceIndex(),OpcuaNodes.CMD_CHANGE_REQUEST.getIdentifier());
        try {
            connection.getClient().writeValue(nodeId, DataValue.valueOnly(new Variant(command))).get();
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
