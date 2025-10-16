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

    public WarehouseController(){

    }

    public WarehouseRepository getRepo(){
        return repositories.getWarehouseRepository();
    }

    public List<Warehouse> getWarehouses(){
        return getRepo().getWarehouseList();
    }

    public void addWarehouse(Warehouse w){
        getRepo().add(w);
    }


}
