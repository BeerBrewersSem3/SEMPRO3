package beerbrewers.batch;

import beerbrewers.brew.Brew;

import jakarta.persistence.*;

@Entity(name = "batch")
@Table
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
    private long batchId;

    @ManyToOne(
            targetEntity = Brew.class,
            optional = false
    )
    @JoinColumn(name = "brew_id")
    private Brew brew;

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

    protected Batch() {

    }

    public Batch(Brew brew, long amount, long speed, boolean isCompleted, long defectiveCount) {
        this.brew = brew;
        this.amount = amount;
        this.speed = speed;
        this.isCompleted = isCompleted;
        this.defectiveCount = defectiveCount;
    }

    public Batch(long batchId, Brew brew, long amount, long speed, boolean isCompleted, long defectiveCount) {
        this.batchId = batchId;
        this.brew = brew;
        this.amount = amount;
        this.speed = speed;
        this.isCompleted = isCompleted;
        this.defectiveCount = defectiveCount;
    }

    public long getBatchId() {
        return batchId;
    }

    public Brew getBrew() {
        return brew;
    }

    public void setBrew(Brew brew) {
        this.brew = brew;
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
}
