package beerbrewers.opcua;
public enum OpcuaNodes {

    /* ADMIN */
    PROD_DEFECTIVE_COUNT(6, "::Program:Cube.Admin.ProdDefectiveCount", "prodDefectiveCount"), // [R] Amount of defective products.
    PROD_PROCESSED_COUNT(6, "::Program:Cube.Admin.ProdProcessedCount", "prodProcessedCount"), // [R] Amount of produced products.
    STOP_REASON(6, "::Program:Cube.Admin.StopReason", "stopReason"),                          // [R] 10: Empty inventory 11: Maintenance needed 12: Manual stop 13: Motor power loss 14: Manual abort
    //STOP_REASON_VALUE ?? THERE IS A STOP REASON ID AND A VALUE. UPPER ENUM IS A STOP REASON ID.
    //Parameter[0] - Id of product in batch

    /* PRODUCT */
    PROD_PRODUCED(6, "::Program:product.produced","prodProduced"),


    /* STATUS */
    STATE_CURRENT(6, "::Program:Cube.Status.StateCurrent", "currentState"),                   // [R] Current PackML state
    MACH_SPEED_READ(6, "::Program:Cube.Command.MachSpeed", "machSpeed"),                      // [R] Machine speed in primary products per minute for next batch.
    CUR_MACH_SPEED(6,"::Program:Cube.Status.CurMachSpeed","currentMachineSpeed"),             // [R] Current machine speed measured from 0-100
    CURRENT_BATCH_ID(6,"::Program:Cube.Status.Parameter[0].Value","currentBatchId"),          // [R] Current batch ID - Batch quantity number - Parameter[0]
    CURRENT_BATCH_AMOUNT(6,"::Program:Cube.Status.Parameter[1].Value","currentBatchAmount"),           // [R] Amount of products in current batch. Parameter [1]
    REL_HUMIDITY(6,"::Program:Cube.Status.Parameter[2].Value","relativeHumidity", 3L),            // [R] Relative Humidity - Parameter[2]
    TEMPERATURE(6,"::Program:Cube.Status.Parameter[3].Value","temperature", 2L),                  // [R] Temperature - Parameter [3]
    VIBRATION(6,"::Program:Cube.Status.Parameter[4].Value","vibration", 1L),                      // [R] Vibration - Parameter [4]

    /* COMMAND */
    CNTRL_CMD(6, "::Program:Cube.Command.CntrlCmd", "cntrlCmd"),                              // [R/W] PackML command = 1: Reset 2: Start 3: Stop 4: Abort 5: Clear
    CMD_CHANGE_REQUEST(6, "::Program:Cube.Command.CmdChangeRequest", "cmdChangeRequest"),     // [R/W] When true, executes commando in Control Command
    //MACH_SPEED_WRITE()

    /* INVENTORY */
    BARLEY(6, "::Program:Inventory.Barley", "barley"),
    HOPS(6,"::Program:Inventory.Hops", "hops"),
    MALT(6,"::Program:Inventory.Malt", "malt"),
    WHEAT(6,"::Program:Inventory.Wheat","wheat"),
    YEAST(6,"::Program:Inventory.Yeast","yeast"),

    /* MAINTENANCE */
    MAINTENANCE_COUNTER(6, "::Program:Maintenance.Counter", "maintenanceCounter"),
    MAINTENANCE_TRIGGER(6, "::Program:Maintenance.Trigger", "maintenanceTrigger");

    private final int namespaceIndex;
    private final String identifier;
    private final String name;

    private Long databaseId;

    OpcuaNodes(int namespaceIndex, String identifier, String name) {
        this.namespaceIndex = namespaceIndex;
        this.identifier = identifier;
        this.name = name;
    }

    OpcuaNodes(int namespaceIndex, String identifier, String name, Long databaseId) {
        this.namespaceIndex = namespaceIndex;
        this.identifier = identifier;
        this.name = name;
        this.databaseId = databaseId;
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

    public Long getDatabaseId() {
        return databaseId;
    }
}
