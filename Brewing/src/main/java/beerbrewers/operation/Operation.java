package beerbrewers.operation;

import beerbrewers.worker.Worker;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table
public class Operation {

    @Id
    @SequenceGenerator(
            name = "operation_sequence",
            sequenceName = "operation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "operation_sequence"
    )
    private long operationId;

    @ManyToOne(targetEntity = Worker.class, optional = false)
    private long workerId;
    @ManyToOne(targetEntity = OperationType.class, optional = false)
    private long operationTypeId;
    @Column(nullable = false)
    private Date date;

    protected Operation() {

    }
    public Operation(long workerId, long operationTypeId, Date date) {
        this.workerId = workerId;
        this.operationTypeId = operationTypeId;
        this.date = date;
    }

    public Operation(long operationId, long workerId, long operationTypeId, Date date) {
        this.operationId = operationId;
        this.workerId = workerId;
        this.operationTypeId = operationTypeId;
        this.date = date;
    }

    public long getOperationId() {
        return operationId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(long operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
