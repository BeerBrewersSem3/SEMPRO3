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
    private Long operationTypeId;

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

    public OperationType(Long operationTypeId, String name) {
        this.operationTypeId = operationTypeId;
        this.name = name;
    }

    public Long getOperationTypeId() {
        return operationTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "OperationType{" +
                "operationTypeId=" + operationTypeId +
                ", name='" + name + '\'' +
                '}';
    }
}
