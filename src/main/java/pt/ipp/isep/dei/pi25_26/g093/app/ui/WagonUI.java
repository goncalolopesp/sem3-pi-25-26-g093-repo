package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.WagonController;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.WarehouseController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Date;

import java.util.*;

public class WagonUI {
    private static final String MAX_DATE_STRING = "31-12-9999";

    /**
     * Runs the code
     */
    public void run() {
        System.out.println("WagonUI");
        addWagonsToWarehouses();
    }

    /**
     * Adds wagons to Bays in Aisles in Warehouses and outputs the success or failure of the operation.
     */
    private void addWagonsToWarehouses(){
        WagonController wc = new WagonController();
        boolean addedAllWagonsToWarehouses = wc.storeAllCargoInWarehouses();
        if (addedAllWagonsToWarehouses){
            System.out.println("All wagons were added to the warehouses successfully.");
        } else {
            System.out.println("Some wagons were not added to the warehouses.");
        }
    }
    
}
