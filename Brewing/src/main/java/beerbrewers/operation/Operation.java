package beerbrewers.operation;

import beerbrewers.batch.Batch;
import beerbrewers.operationtype.OperationType;
import beerbrewers.worker.Worker;

import jakarta.persistence.*;
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
    @JoinColumn(name = "worker_id")
    private Worker worker;
    @ManyToOne(targetEntity = OperationType.class, optional = false)
    @JoinColumn(name = "operation_type_id")
    private OperationType operationType;
    @Column(nullable = false)
    private Date date;
    @OneToOne(targetEntity = Batch.class)
    private Batch batch;

    protected Operation() {

    }
    public Operation(Worker worker, OperationType operationType, Date date, Batch batch) {
        this.worker = worker;
        this.operationType = operationType;
        this.date = date;
        this.batch = batch;
    }

    public Operation(long operationId, Worker worker, OperationType operationType, Date date, Batch batch) {
        this.operationId = operationId;
        this.worker = worker;
        this.operationType = operationType;
        this.date = date;
        this.batch = batch;
    }

    /**
     * Constructors without Batch
     * @param operationId
     * @param worker
     * @param operationType
     * @param date
     */
    public Operation(long operationId, Worker worker, OperationType operationType, Date date) {
        this.operationId = operationId;
        this.worker = worker;
        this.operationType = operationType;
        this.date = date;
    }

    public Operation(Worker worker, OperationType operationType, Date date) {
        this.worker = worker;
        this.operationType = operationType;
        this.date = date;
    }

    public long getOperationId() {
        return operationId;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }
}
