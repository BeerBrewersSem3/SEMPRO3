package beerbrewers.brew;

import jakarta.persistence.*;

@Entity(name = "Brew")
@Table(name = "brew")
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
    @Column(
            name = "brew_id",
            updatable = false
    )
    private Long brewId;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "machine_commands",
            nullable = false,
            columnDefinition = "TEXT"
    )
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

    public Brew(Long brewId, String name, String description, String machineCommands) {
        this.brewId = brewId;
        this.name = name;
        this.description = description;
        this.machineCommands = machineCommands;
    }

    public Long getBrewId() {
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
