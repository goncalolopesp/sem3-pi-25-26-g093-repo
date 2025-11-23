package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Bay in an Aisle.
 */
public class Bay {
    private int id;
    private int capacity;
    private List<Cargo> cargo;

    /**
     * Creates a new Bay with the given ID and capacity.
     * @param id ID of the bay to be created.
     * @param capacity the capacity of the bay.
     */
    public Bay(int id, int capacity){
        this.id = id;
        this.capacity = capacity;
        this.cargo = new ArrayList<>();
    }

    /**
     * Gets the capacity of the bay.
     * @return the capacity of the bay.
     */
    public int getCapacity(){
        return capacity;
    }

    /**
     * Sets the capacity of the bay.
     * @param capacity new Capacity of the Bay
     */
    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    /**
     * Sets the cargo of the bay.
     * @param cargo List of the cargo to be set in the bay
     */
    public void setCargo(List<Cargo> cargo){
        this.cargo = cargo;
    }

    /**
     * Adds Cargo to the bay.
     * @param c the cargo to be added to the bay
     */
    public void addCargo(Cargo c){
        this.cargo.add(c);
    }

    /**
     * Gets the cargo of the bay.
     * @return a List of the cargo in the bay
     */
    public List<Cargo> getCargo(){
        return cargo;
    }

    /**
     * Gets the ID of the bay.
     * @return the ID of the bay.
     */
    public int getId() {
        return id;
    }

    public boolean removeCargo(Cargo c){
        return this.cargo.remove(c);
    }
}
