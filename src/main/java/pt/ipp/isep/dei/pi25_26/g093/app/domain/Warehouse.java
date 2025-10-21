package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.List;

public class Warehouse {
    private int id;
    private List<Aisle> aisles;

    /**
     * Creates a new Warehouse with the given ID and List of Aisles.
     * @param id the ID of the Warehouse to be created.
     * @param aisles the Aisles that are included in this Warehouse.
     */
    public Warehouse(int id, List<Aisle> aisles){
        this.id = id;
        this.aisles = aisles;
    }

    /**
     * Gets all the Aisles of the Warehouse
     * @return the List of Aisles in the Warehouse
     */
    public List<Aisle> getAisles(){
        return aisles;
    }

    /**
     * Sets the Aisles in the Warehouse to a specific List of Aisles.
     * @param aisles the List of Aisles to be set in the Warehouse
     */
    public void setAisles(List<Aisle> aisles){
        this.aisles = aisles;
    }

    /**
     * Gets the ID of the Warehouse.
     * @return the ID of the Warehouse.
     */
    public int getId() {
        return id;
    }
}
