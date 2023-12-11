package beerbrewers.opcua;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class OpcuaCommander {

    private OpcuaClientConnection connection;
    private Logger logger = LoggerFactory.getLogger(OpcuaCommander.class);
    @Autowired
    public OpcuaCommander(OpcuaClientConnection opcuaClientConnection){
        this.connection = opcuaClientConnection;
    }

    public boolean sendCommand(OpcUaNode node, Number command) {
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
            logger.info("Command sent to node: " + node.getIdentifier() + " with value: " + command.toString());
            if(node.equals(OpcUaNode.CNTRL_CMD)){
                commandChangeRequest(true);
            }
            return true;
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public boolean commandChangeRequest(boolean command){
        NodeId nodeId = new NodeId(OpcUaNode.CMD_CHANGE_REQUEST.getNamespaceIndex(), OpcUaNode.CMD_CHANGE_REQUEST.getIdentifier());
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
