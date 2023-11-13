package beerbrewers.operation;

import beerbrewers.batch.Batch;
import beerbrewers.worker.Worker;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Operation")
@Table(name = "operation")
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
    @Column(
            name = "operation_id",
            updatable = false
    )
    private Long operationId;

    @ManyToOne(
            targetEntity = Worker.class,
            optional = false
    )
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @OneToMany(
            mappedBy = "operation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Batch> batchList = new ArrayList<>();

    protected Operation() {

    }

    /**
     * Constructors with batch
     * @param operationId
     * @param worker
     * @param batchList
     */

    public Operation(Long operationId, Worker worker, List<Batch> batchList) {
        this.operationId = operationId;
        this.worker = worker;
        this.batchList = batchList;
    }

    public Operation(Worker worker, List<Batch> batchList) {
        this.worker = worker;
        this.batchList = batchList;
    }

    /**
     * Constructors without batch
     * @param operationId
     * @param worker
     */

    public Operation(Long operationId, Worker worker) {
        this.operationId = operationId;
        this.worker = worker;
    }

    public Operation(Worker worker) {
        this.worker = worker;
    }


    /**
     * The following utility methods (e.g. addBatch and removeBatch) are used to synchronize both sides of the
     * bidirectional association. These should always be provided, when working with a bidirectional association as,
     * otherwise, we risk very subtle state propagation issues.
     * @param batch
     */
    public void addBatch(Batch batch) {
        batchList.add(batch);
        batch.setOperation(this);
    }

    public void removeBatch(Batch batch) {
        batchList.remove(batch);
        batch.setOperation(null);
    }

    /**
     * getters, setters and toString
     */

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public List<Batch> getBatchList() {
        return batchList;
    }

    public void setBatchList(List<Batch> batchList) {
        this.batchList = batchList;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "operationId=" + operationId +
                ", worker=" + worker +
                ", batchList=" + batchList +
                '}';
    }
}
