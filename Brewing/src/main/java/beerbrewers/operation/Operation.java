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
            fetch = FetchType.LAZY,
            targetEntity = Worker.class
//            optional = false
    )
    @JoinColumn(name = "worker_id")
    private Worker worker;

    protected Operation() {

    }

    public Operation(Long operationId, Worker worker) {
        this.operationId = operationId;
        this.worker = worker;
    }

    public Operation(Worker worker) {
        this.worker = worker;
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

    @Override
    public String toString() {
        return "Operation{" +
                "operationId=" + operationId +
                ", worker=" + worker +
                '}';
    }
}
