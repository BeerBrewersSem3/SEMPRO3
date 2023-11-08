package beerbrewers.brew;

import jakarta.persistence.*;

@Entity(name = "brew")
@Table
public class Brew {

    @Id
    @SequenceGenerator(
            name = "brew_sequence",
            sequenceName = "brew_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "brew_sequence"
    )
    private long brewId;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private String machineCommands;

    /**
     * This Constructor exists solely for JPA and is thus protected.
     */
    protected Brew() {}

    public Brew(String name, String description, String machineCommands) {
        this.name = name;
        this.description = description;
        this.machineCommands = machineCommands;
    }

    public Brew(long brewId, String name, String description, String machineCommands) {
        this.brewId = brewId;
        this.name = name;
        this.description = description;
        this.machineCommands = machineCommands;
    }

    public long getBrewId() {
        return brewId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMachineCommands() {
        return machineCommands;
    }

    public void setMachineCommands(String machineCommands) {
        this.machineCommands = machineCommands;
    }

    @Override
    public String toString() {
        return "Brew{" +
                "brewId=" + brewId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", machineCommands=" + machineCommands +
                '}';
    }
}
