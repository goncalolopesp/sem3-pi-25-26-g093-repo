package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Bay;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WagonRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WarehouseRepository;

import javax.swing.*;
import java.util.List;

public class WarehouseController {
    Repositories repositories = Repositories.getInstance();

    /**
     * Constructor of the WarehouseController class
     */
    public WarehouseController(){

    }

    /**
     * Gets the WarehouseRepository
     * @return WarehouseRepository
     */
    public WarehouseRepository getRepo(){
        return repositories.getWarehouseRepository();
    }

    /**
     * Gets all the stored warehouses
     * @return Stored Warehouses in the WarehouseRepository
     */
    public List<Warehouse> getWarehouses(){
        return getRepo().getWarehouseList();
    }

    /**
     * Adds a new warehouse to the WarehouseRepository
     * @param w Warehouse to be added to the WarehouseRepository
     */
    public void addWarehouse(Warehouse w){
        getRepo().add(w);
    }


}
