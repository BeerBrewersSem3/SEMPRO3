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

    public boolean sendCommand(OpcuaNodes node, Number command) {
        NodeId nodeId = new NodeId(node.getNamespaceIndex(), node.getIdentifier());
        Variant variantCommand;

        if( command instanceof Integer) {
            variantCommand = new Variant(command.intValue());
        } else if (command instanceof Float) {
            variantCommand = new Variant(command.floatValue());
        } else {
            throw new RuntimeException("The command is not a valid type");
        }

        try {
            connection.getClient().writeValue(nodeId, DataValue.valueOnly(variantCommand)).get();
            if(node.equals(OpcuaNodes.CNTRL_CMD)){
                commandChangeRequest(true);
            }
            return true;
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
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
