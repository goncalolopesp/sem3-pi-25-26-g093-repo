package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;

import java.util.List;

public class WarehouseRepository {

    private List<Warehouse> warehouseList;

    public WarehouseRepository(){
        this.warehouseList = new java.util.ArrayList<>();
    }

    public List<Warehouse> getWarehouseList(){
        return this.warehouseList;
    }

    public void add(Warehouse w){
        this.warehouseList.add(w);
    }

}
