package beerbrewers.BrewMaster9000.opcua;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpcUaClientCommand {

    private final OpcUaClientConnection opcUaClientConnection;

    public OpcUaClientCommand(OpcUaClientConnection opcUaClientConnection) {
        this.opcUaClientConnection = opcUaClientConnection;
    }

    public void sendCommand(int number) throws Exception {
        try {
            // Creating NodeId of the CntrlCmd node
            NodeId nodeId = new NodeId(6, "::Program:Cube.Command.CntrlCmd");
            // Writing the int as the value of the node on the OPC UA Server
            opcUaClientConnection.getClient().writeValue(nodeId, DataValue.valueOnly(new Variant(number))).get();
        } catch (Exception e) {
            System.out.println("Error writing number to OPC UA server: " + e.getMessage());
            throw e;
        }
    }

    /**
     * When executed with parameter true,
     * the BeerMachine will execute the command that is currently set in CntrlCmd.
     * @param value The boolean value to write to the OPC UA server.
     */
    public void executeCommand(boolean value) throws Exception {
        try {
            // Creating NodeID of the CmdChangeRequest node
            NodeId nodeId = new NodeId(6, "::Program:Cube.Command.CmdChangeRequest");
            // Writing the value on the OPC UA server to the boolean provided
            opcUaClientConnection.getClient().writeValue(nodeId, DataValue.valueOnly(new Variant(value))).get();
        } catch (Exception e) {
            System.out.println("Error writing boolean value to OPC UA server: " + e.getMessage());
            throw e;
        }
    }
}
