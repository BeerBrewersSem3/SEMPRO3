package beerbrewers.batch;

import beerbrewers.operation.Operation;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity(name = "Batch")
@Table(name = "batch")
public class Batch {

    @Id
    @SequenceGenerator(
            name = "batch_sequence",
            sequenceName = "batch_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "batch_sequence"
    )
    @Column(
            name = "batch_id",
            updatable = false
    )
    private Long batchId;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Operation.class,
            optional = false
    )
    private Operation operation;

    @Column(
            name = "brew_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @Enumerated(EnumType.STRING)
    private BrewEnum brewName;

    @Column(
            name = "amount",
            nullable = false
    )
    private long amount;

    @Column(
            name = "speed",
            nullable = false
    )
    private long speed;

    @Column(
            name = "is_completed",
            nullable = false
    )
    private boolean isCompleted;

    @Column(
            name = "defective_count",
            nullable = false
    )
    private long defectiveCount;

    @Column(
            name = "completed_count",
            nullable = false
    )
    private long completedCount;

    @Column(
            name = "start_time",
            nullable = false
    )
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    /**
     * Constructor for JPA
     */
    protected Batch() {

    }

    /**
     * Constructors without batchId.
     * @param operation
     * @param brewName
     * @param amount
     * @param speed
     * @param isCompleted
     * @param defectiveCount
     * @param completedCount
     * @param startTime
     */
    public Batch(Operation operation, BrewEnum brewName, long amount, long speed, boolean isCompleted,
                 long defectiveCount, long completedCount, Timestamp startTime) {
        this.operation = operation;
        this.brewName = brewName;
        this.amount = amount;
        this.speed = speed;
        this.isCompleted = isCompleted;
        this.defectiveCount = defectiveCount;
        this.completedCount = completedCount;
        this.startTime = startTime;
    }

    public Batch(Operation operation, BrewEnum brewName, long amount, long speed, boolean isCompleted,
                 long defectiveCount, long completedCount, Timestamp startTime, Timestamp endTime) {
        this.operation = operation;
        this.brewName = brewName;
        this.amount = amount;
        this.speed = speed;
        this.isCompleted = isCompleted;
        this.defectiveCount = defectiveCount;
        this.completedCount = completedCount;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Constructors with batchId
     * @param batchId
     * @param operation
     * @param brewName
     * @param amount
     * @param speed
     * @param isCompleted
     * @param defectiveCount
     * @param completedCount
     * @param startTime
     */
    public Batch(Long batchId, Operation operation, BrewEnum brewName, long amount, long speed, boolean isCompleted,
                 long defectiveCount, long completedCount, Timestamp startTime) {
        this.batchId = batchId;
        this.operation = operation;
        this.brewName = brewName;
        this.amount = amount;
        this.speed = speed;
        this.isCompleted = isCompleted;
        this.defectiveCount = defectiveCount;
        this.completedCount = completedCount;
        this.startTime = startTime;
    }

    public Batch(Long batchId, Operation operation, BrewEnum brewName, long amount, long speed, boolean isCompleted,
                 long defectiveCount, long completedCount, Timestamp startTime, Timestamp endTime) {
        this.batchId = batchId;
        this.operation = operation;
        this.brewName = brewName;
        this.amount = amount;
        this.speed = speed;
        this.isCompleted = isCompleted;
        this.defectiveCount = defectiveCount;
        this.completedCount = completedCount;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * The Batch entity implements the equals and hashCode methods. Since we cannot rely on a natural
     * identifier for equality checks, we need to use the entity id instead for the equals() method. However, we
     * need to do son in a way that equality is consistent across all entity state transitions, which is also the
     * reason why the hashCode has to be a constant value. Because we rely on equality for the removeBatch, it’s good
     * practice to override equals and hashCode for the child entity in a bidirectional association.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Batch )) return false;
        return batchId != null && batchId.equals(((Batch) o).getBatchId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Getters, setters and toString
     * @return
     */

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public BrewEnum getBrewName() {
        return brewName;
    }

    public void setBrewName(BrewEnum brewName) {
        this.brewName = brewName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getDefectiveCount() {
        return defectiveCount;
    }

    public void setDefectiveCount(long defectiveCount) {
        this.defectiveCount = defectiveCount;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batchId=" + batchId +
                ", operation=" + operation +
                ", brewName=" + brewName +
                ", amount=" + amount +
                ", speed=" + speed +
                ", isCompleted=" + isCompleted +
                ", defectiveCount=" + defectiveCount +
                ", completedCount=" + completedCount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
