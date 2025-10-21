package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.List;

public class Aisle {
    private int id;
    private List<Bay> bays;

    /**
     * Creates a new Aisle with the given ID and List of Bays.
     * @param id the ID of the Aisle to be created.
     * @param bays the Bays that are included in this Aisle
     */
    public Aisle(int id, List<Bay> bays){
        this.id = id;
        this.bays = bays;
    }

    /**
     * Gets all the bays of the Aisle
     * @return The list of the bays in the aisle
     */
    public List<Bay> getBays(){
        return bays;
    }

    /**
     * Sets the bays in the aisle to a specific List of bays
     * @param bays the List of bays to be set in the aisle. Must not be null. Must not contain null elements.
     */
    public void setBays(List<Bay> bays){
        this.bays = bays;
    }

    /**
     * Gets the ID of the Aisle.
     * @return the ID of the Aisle.
     */
    public int getId() {
        return id;
    }
}
