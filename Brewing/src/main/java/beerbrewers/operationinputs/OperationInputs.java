package beerbrewers.operationinputs;

import beerbrewers.operation.Operation;
import jakarta.persistence.*;

@Entity(name = "OperationInputs")
@Table(name = "operation_inputs")
public class OperationInputs {

    @Id
    @SequenceGenerator(
            name = "operation_inputs_sequence",
            sequenceName = "operation_inputs_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "operation_inputs_sequence"
    )
    @Column(
            name = "operation_inputs_id",
            updatable = false
    )
    private Long operationInputsId;

    @ManyToOne(
            targetEntity = Operation.class,
            optional = false
    )
    @JoinColumn(name = "operation_id")
    private Operation operation;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @Enumerated(EnumType.STRING)
    private OperationInputsEnum name;

    @Column(
            name = "timestamp",
            nullable = false,
            columnDefinition = "TIMESTAMP"
    )
    private String timestamp;


    /**
     * This Constructor exists solely for JPA and is thus protected.
     */
    protected OperationInputs() {}

    public OperationInputs(Operation operation, OperationInputsEnum name, String timestamp) {
        this.operation = operation;
        this.name = name;
        this.timestamp = timestamp;
    }

    public OperationInputs(Long operationInputsId, Operation operation, OperationInputsEnum name, String timestamp) {
        this.operationInputsId = operationInputsId;
        this.operation = operation;
        this.name = name;
        this.timestamp = timestamp;
    }

    public Long getOperationInputsId() {
        return operationInputsId;
    }

    public void setOperationInputsId(Long operationInputsId) {
        this.operationInputsId = operationInputsId;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public OperationInputsEnum getName() {
        return name;
    }

    public void setName(OperationInputsEnum name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OperationInputs{" +
                "operationInputsId=" + operationInputsId +
                ", operation=" + operation +
                ", name=" + name +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
