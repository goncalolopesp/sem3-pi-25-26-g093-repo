package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;

import java.util.List;

/**
 * Stores the Warehouses
 */
public class WarehouseRepository {

    private List<Warehouse> warehouseList;

    /**
     * Creates a new instance of the WarehouseRepository and creates an empty List of Warehouses
     */
    public WarehouseRepository(){
        this.warehouseList = new java.util.ArrayList<>();
    }

    /**
     * Gets the Warehouses in the WarehouseRepository
     * @return Warehouses in the WarehouseRepository
     */
    public List<Warehouse> getWarehouseList(){
        return this.warehouseList;
    }

    /**
     * Adds a new Warehouse to the WarehouseRepository
     * @param w Warehouse to be added to the WarehouseRepository
     */
    public void add(Warehouse w){
        this.warehouseList.add(w);
    }

}
