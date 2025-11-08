package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;

public class Repositories {
    private static Repositories instance;
    private WagonRepository wagonRepository;
    private WarehouseRepository warehouseRepository;
    private ReturnItemRepository returnItemRepository;

    /**
     * Creates a new Repositories object with WagonRepository and WarehouseRepository.
     */
    public Repositories(){
        wagonRepository = new WagonRepository();
        warehouseRepository = new WarehouseRepository();
        returnItemRepository = new ReturnItemRepository();
    }

    /**
     * Gets the WagonRepository.
     * @return the WagonRepository.
     */
    public WagonRepository getWagonRepository(){
        return wagonRepository;
    }

    /**
     * Gets the WarehouseRepository.
     * @return the WarehouseRepository.
     */
    public WarehouseRepository getWarehouseRepository(){
        return warehouseRepository;
    }


    public ReturnItemRepository getReturnItemRepository(){

        return returnItemRepository;

    }


    /**
     * Gets the instance of the Repositories class.
     * @return the instance of the Repositories class.
     */
    public static Repositories getInstance() {
        if (instance == null) {
            synchronized (Repositories.class) {
                instance = new Repositories();
            }
        }
        return instance;
    }

}
