package beerbrewers.operation;

import beerbrewers.batch.Batch;
import beerbrewers.worker.Worker;

import jakarta.persistence.*;
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
            fetch = FetchType.LAZY,
            mappedBy = "batch",
            cascade = CascadeType.ALL)
    private List<Batch> batchList;

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
}
