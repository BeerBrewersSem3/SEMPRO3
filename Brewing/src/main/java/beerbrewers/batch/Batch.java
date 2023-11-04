package beerbrewers.batch;

import beerbrewers.brew.Brew;

import javax.persistence.*;

@Entity
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
    private long batchId;
    @ManyToOne(targetEntity = Brew.class, optional = false)
    private long brewId;
    @Column(nullable = false)
    private long amount;
    @Column(nullable = false)
    private long speed;
    @Column(nullable = false)
    private boolean isCompleted;
    @Column(nullable = false)
    private long defectiveCount;

    protected Batch() {

    }

    public Batch(long brewId, long amount, long speed, boolean isCompleted, long defectiveCount) {
        this.brewId = brewId;
        this.amount = amount;
        this.speed = speed;
        this.isCompleted = isCompleted;
        this.defectiveCount = defectiveCount;
    }

    public Batch(long batchId, long brewId, long amount, long speed, boolean isCompleted, long defectiveCount) {
        this.batchId = batchId;
        this.brewId = brewId;
        this.amount = amount;
        this.speed = speed;
        this.isCompleted = isCompleted;
        this.defectiveCount = defectiveCount;
    }

    public long getBatchId() {
        return batchId;
    }

    public long getBrewId() {
        return brewId;
    }

    public void setBrewId(long brewId) {
        this.brewId = brewId;
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
