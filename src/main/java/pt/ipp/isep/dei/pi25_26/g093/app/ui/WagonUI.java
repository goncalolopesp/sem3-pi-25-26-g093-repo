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
        addWagonsToWarehouses();
    }

    /**
     * Adds wagons to Bays in Aisles in Warehouses and outputs the success or failure of the operation.
     */
    private void addWagonsToWarehouses(){
        WagonController wc = new WagonController();
        List<Bay> allBaysWithCargo = wc.storeAllCargoInWarehouses();
        boolean addedWagonsToWarehouses = !allBaysWithCargo.isEmpty();
        if (addedWagonsToWarehouses) {
            System.out.println("cargo was added to the warehouses successfully.");
        } else {
            System.out.println("No cargo was added to the warehouses.");
        }
    }

    public void displayCargoInBay(){
        Scanner sc = new Scanner(System.in);
        WarehouseController warehouseController = new WarehouseController();

        List<Warehouse> warehouses = warehouseController.getWarehouses();
        int wID = displayWarehouses(warehouses, sc);
        sc.nextLine();

        Warehouse warehouse = warehouseController.getWarehouseById(wID);
        if (warehouse == null) {
            System.out.println("Warehouse not found.");
            return;
        }
        int aID = displayAisles(warehouse, sc);
        sc.nextLine();

        Aisle aisle = warehouseController.getAisleById(warehouse, aID);
        if (aisle == null) {
            System.out.println("Aisle not found.");
            return;
        }
        int bID = displayBays(aisle, sc);
        sc.nextLine();

        Bay bay = warehouseController.getBayById(aisle, bID);
        if (bay == null) {
            System.out.println("Bay not found.");
            return;
        }

        displayCargoFromBay(bay);
    }

    private int displayWarehouses(List<Warehouse> warehouses, Scanner sc){
        System.out.println("\n=== List of Warehouses ===");
        System.out.print("|");
        for (Warehouse warehouse : warehouses){
            System.out.printf(" %d |", warehouse.getId());
        }
        System.out.println();
        System.out.print("Chosen Warehouse: ");
        return sc.nextInt();
    }

    private int displayAisles(Warehouse warehouse, Scanner sc){
        List<Aisle> aisles = warehouse.getAisles();

        System.out.println("\n=== List of Aisles in Warehouse " + warehouse.getId() + " ===");

        System.out.print("|");
        for (Aisle aisle : aisles){
            System.out.printf(" %d |", aisle.getId());
        }
        System.out.println();
        System.out.print("Chosen Aisle: ");
        return sc.nextInt();
    }

    private int displayBays(Aisle aisle, Scanner sc){
        List<Bay> bays = aisle.getBays();

        System.out.println("\n=== List of Bays in Aisle " + aisle.getId() + " ===");

        System.out.print("|");
        for (Bay bay : bays){
            System.out.printf(" %d |", bay.getId());
        }
        System.out.println();
        System.out.print("Chosen Bay: ");
        return sc.nextInt();
    }

    private void displayCargoFromBay(Bay bay){
        List<Cargo> cargoList = bay.getCargo();
        if (cargoList == null || cargoList.isEmpty()) {
            System.out.println("No cargo found in Bay " + bay.getId());
            return;
        }

        System.out.println("\n=== Cargo in Bay " + bay.getId() + " ===");
        for (Cargo cargo : cargoList){
            System.out.println("Box ID: " + cargo.getBoxID() +
                    ", SKU: " + cargo.getSku() +
                    ", Expiry Date: " + cargo.getExpiryDate().toString() +
                    ", Received Date: " + cargo.getReceivedAt().toString());
        }
    }

    public void dispatchAllCargoFromWagon() {
        WagonController wc = new WagonController();
        List<Cargo> dispatched = wc.dispatchCargoFromWagons();
        if (dispatched.isEmpty()) {
            System.out.println("No cargo was dispatched from the wagons.");
        } else {
            System.out.println("\nSuccessfully dispatched cargo from wagons:");
        }
    }

    public void dispatchCargoFromWagon(int quantity) {
        WagonController wc = new WagonController();
        List<Cargo> dispatched = wc.dispatchQuantityCargoFromWagons(quantity);
        if (dispatched.isEmpty()) {
            System.out.println("No cargo was dispatched from the wagons.");
        } else {
            System.out.println("\nSuccessfully dispatched cargo from wagons:");
        }
    }
}
