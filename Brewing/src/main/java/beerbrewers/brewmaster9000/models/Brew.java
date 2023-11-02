package beerbrewers.brewmaster9000.models;

import org.postgresql.util.PGobject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Brew {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long brewId;
    private String name;
    private String description;
    private PGobject machineCommands;

    public Brew(String name, String description, PGobject machineCommands) {
        super();
        this.name = name;
        this.description = description;
        this.machineCommands = machineCommands;
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

    public PGobject getMachineCommands() {
        return machineCommands;
    }

    public void setMachineCommands(PGobject machineCommands) {
        this.machineCommands = machineCommands;
    }
}
