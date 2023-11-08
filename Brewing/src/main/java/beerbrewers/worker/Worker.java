package beerbrewers.worker;

import jakarta.persistence.*;

@Entity(name = "Worker")
@Table(name = "worker")
public class Worker {

    @Id
    @SequenceGenerator(
            name = "worker_sequence",
            sequenceName = "worker_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "worker_sequence"
    )
    @Column(
            name = "worker_id",
            updatable = false
    )
    private long workerId;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    /**
     * Remember to implement password encryption!!
     */

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String password;

    /**
     * This Constructor exists solely for JPA and is thus protected.
     */
    protected Worker() {}

    /**
     * This Constructor is used to create instances of Worker to be added to the database.
     * @param name
     * @param password
     */
    public Worker(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Worker(long workerId, String name, String password) {
        this.workerId = workerId;
        this.name = name;
        this.password = password;
    }

    public long getWorkerId() {
        return workerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "workerId=" + workerId +
                ", name='" + name + '\'' +
                '}';
    }
}
