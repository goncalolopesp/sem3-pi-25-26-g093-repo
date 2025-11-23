package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.WagonController;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.WarehouseController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Aisle;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Bay;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Wagon;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class WagonCargoUI {

    public void showMenu(){
        Scanner sc = new Scanner(System.in);
        int option;

        do{
            System.out.println("\n=== CARGO STORING MENU===");
            System.out.println("1. Add Cargo From Wagons to Warehouses");
            System.out.println("2. Dispatch All Cargo from the Warehouses");
            System.out.println("3. Dispatch quantity of Cargo from the Warehouses");
            System.out.println("4. Relocation");
            System.out.println("5. Show Cargo in a specific Bay");
            System.out.println("0. Exit");
            System.out.print("Choose an Option: ");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> addCargoToWarehouses();
                case 2 -> dispatchOperation();
                case 3 -> dispatchOperation1();
                case 4 -> relocationOperation();
                case 5 -> showCargoFromBay();
                case 0 -> System.out.println("Returning to the main menu...");
                default -> System.out.println("Invalid Option (Does not exist).");
            }
        } while (option != 0);
    }

    private void relocationOperation() {
        Scanner sc = new Scanner(System.in);
        WagonController wc = new WagonController();
        WarehouseController whc = new WarehouseController();

        System.out.println("\n=== Relocation Operation ===");

        System.out.print("Enter SKU of box to relocate: ");
        int sku = sc.nextInt();

        System.out.print("Enter Box ID to relocate: ");
        int boxId = sc.nextInt();

        System.out.println("\nSelect Destination Warehouse:");
        List<Warehouse> warehouses = whc.getWarehouses();
        for (Warehouse w : warehouses) {
            System.out.printf("Warehouse %d\n", w.getId());
        }
        System.out.print("Warehouse ID: ");
        int warehouseId = sc.nextInt();
        Warehouse targetWarehouse = whc.getWarehouseById(warehouseId);
        if (targetWarehouse == null) {
            System.out.println("Invalid warehouse.");
            return;
        }

        System.out.println("\nSelect Destination Aisle:");
        for (Aisle a : targetWarehouse.getAisles()) {
            System.out.printf("Aisle %d\n", a.getId());
        }
        System.out.print("Aisle ID: ");
        int aisleId = sc.nextInt();
        Aisle targetAisle = whc.getAisleById(targetWarehouse, aisleId);
        if (targetAisle == null) {
            System.out.println("Invalid aisle.");
            return;
        }

        System.out.println("\nSelect Destination Bay:");
        for (Bay b : targetAisle.getBays()) {
            System.out.printf("Bay %d\n", b.getId());
        }
        System.out.print("Bay ID: ");
        int bayId = sc.nextInt();
        Bay targetBay = whc.getBayById(targetAisle, bayId);
        if (targetBay == null) {
            System.out.println("Invalid bay.");
            return;
        }

        boolean success = wc.relocateBox(sku, boxId, whc, targetBay);

        if (success) {
            System.out.println("Cargo successfully relocated!");
        } else {
            System.out.println("Cargo not found. No relocation performed.");
        }
    }


    private void dispatchOperation() {
        System.out.println("\n=== Dispatch All Cargo from the Warehouses ===");
        try{
            WagonUI wagonUI = new WagonUI();
            wagonUI.dispatchAllCargoFromWagon();
        } catch (Exception e){
            System.out.println("Error executing WagonUI: " + e.getMessage());
        }

    }

    private void dispatchOperation1() {
        System.out.println("\n=== Dispatch quantity of Cargo from the Warehouses ===");
        try{
            WagonUI wagonUI = new WagonUI();
            System.out.print("Enter the quantity to dispatch: ");
            Scanner sc = new Scanner(System.in);
            int quantity = sc.nextInt();
            wagonUI.dispatchCargoFromWagon(quantity);
        } catch (Exception e){
            System.out.println("Error executing WagonUI: " + e.getMessage());
        }

    }

    private void addCargoToWarehouses(){
        System.out.println("\n=== Add Cargo to Warehouses ===");
        try {
            WagonUI wagonUI = new WagonUI();
            wagonUI.run();
        } catch (Exception e) {
            System.out.println("Error executing WagonUI: " + e.getMessage());
        }
    }

    private void showCargoFromBay(){
        System.out.println("\n=== Show Cargo from a Bay ===");
        try{
            WagonUI wagonUI = new WagonUI();
            wagonUI.displayCargoInBay();
        } catch (Exception e){
            System.out.println("Error executing WagonUI: " + e.getMessage());
        }
    }

}
