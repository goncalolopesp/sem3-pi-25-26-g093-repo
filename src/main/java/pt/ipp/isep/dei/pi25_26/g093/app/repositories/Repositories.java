package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;

public class Repositories {
    private static Repositories instance;
    private WagonRepository wagonRepository;
    private WarehouseRepository warehouseRepository;

    public Repositories(){
        wagonRepository = new WagonRepository();
        warehouseRepository = new WarehouseRepository();
    }

    public WagonRepository getWagonRepository(){
        return wagonRepository;
    }

    public WarehouseRepository getWarehouseRepository(){
        return warehouseRepository;
    }

    public static Repositories getInstance() {
        if (instance == null) {
            synchronized (Repositories.class) {
                instance = new Repositories();
            }
        }
        return instance;
    }

}
