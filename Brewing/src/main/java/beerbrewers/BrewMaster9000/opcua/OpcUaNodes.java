package beerbrewers.BrewMaster9000.opcua;

public enum OpcUaNodes {
    STATE_CURRENT(6, "::Program:Cube.Status.StateCurrent", "currentState"),
    CNTRL_CMD(6, "::Program:Cube.Command.CntrlCmd", "cntrlCmd"),
    CMD_CHANGE_REQUEST(6, "::Program:Cube.Command.CmdChangeRequest", "cmdChangeRequest"),
    MACH_SPEED(6, "::Program:Cube.Command.MachSpeed", "machSpeed"),
    PROD_DEFECTIVE_COUNT(6, "::Program:Cube.Admin.ProdDefectiveCount", "prodDefectiveCount"),
    PROD_PROCESSED_COUNT(6, "::Program:Cube.Admin.ProdProcessedCount", "prodProcessedCount"),
    STOP_REASON(6, "::Program:Cube.Admin.StopReason", "stopReason");

    private final int namespaceIndex;
    private final String identifier;
    private final String name;

    OpcUaNodes(int namespaceIndex, String identifier, String name) {
        this.namespaceIndex = namespaceIndex;
        this.identifier = identifier;
        this.name = name;
    }

    public int getNamespaceIndex() {
        return namespaceIndex;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }
}