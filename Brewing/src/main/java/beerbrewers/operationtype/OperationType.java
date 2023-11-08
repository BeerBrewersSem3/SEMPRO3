package beerbrewers.operationtype;

import jakarta.persistence.*;

@Entity(name = "OperationType")
@Table(name = "operation_type")
public class OperationType {

    @Id
    @SequenceGenerator(
            name = "operation_type_sequence",
            sequenceName = "operation_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "operation_type_sequence"
    )
    @Column(
            name = "batch_id",
            updatable = false
    )
    private long operationTypeId;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    protected OperationType() {

    }
    public OperationType(String name) {
        this.name = name;
    }

    public OperationType(long operationTypeId, String name) {
        this.operationTypeId = operationTypeId;
        this.name = name;
    }

    public long getOperationTypeId() {
        return operationTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
