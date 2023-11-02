package beerbrewers.brewmaster9000.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Worker {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long workerId;
    private String name;

    /**
     * Remember to implement password encryption!!
     */
    private String password;

    /**
     * This Constructor exists solely for JPA and is thus protected.
     */
    protected Worker() {};

    /**
     * This Constructor is used to create instances of Worker to be added to the database.
     * @param name
     * @param password
     */
    public Worker(String name, String password) {
        super();
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
