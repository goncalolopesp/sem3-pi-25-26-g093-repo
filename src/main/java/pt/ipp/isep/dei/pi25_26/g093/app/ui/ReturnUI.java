package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.ReturnController;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.WagonController;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.WarehouseController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.ReturnItem;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class ReturnUI {

    private final ReturnController controller;

    public ReturnUI() {
        WagonController wagonController = new WagonController();
        WarehouseController warehouseController = new WarehouseController();

        try {
            CSVParser baysParser = new CSVParser("train_station_dataset/bays.csv");
            warehouseController.loadWarehouses(baysParser.getCSVData());
            System.out.println("Warehouses loaded successfully");
        } catch (Exception e) {
            System.out.println("\n" + "Error loading warehouses: " + e.getMessage());
        }

        this.controller = new ReturnController(wagonController, warehouseController);
    }


    public void run() {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== RETURNS & QUARANTINE MENU ===");
            System.out.println("1. Process returns from file");
            System.out.println("2. Inspect quarantine");
            System.out.println("3. Show summary of last processing");
            System.out.println("0. Exit");
            System.out.print("Option: ");

            String option = sc.nextLine();

            switch (option) {
                case "1":
                    processReturns();
                    break;
                case "2":
                    inspectQuarantine();
                    break;
                case "3":
                    showSummary();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        sc.close();
        System.out.println("Exiting Returns & Quarantine module.");
    }

    private void processReturns() {
        try {
            List<ReturnItem> returns = controller.loadReturnItems();

            if (returns.isEmpty()) {
                System.out.println("No return items found in CSV file.");
                return;
            }

            controller.processReturns(returns);
            System.out.println("\nReturns processed successfully!");
            System.out.println("Audit log updated.");

        } catch (FileNotFoundException e) {
            System.out.println("Error: returns.csv file not found.");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void inspectQuarantine() {
        controller.inspectQuarantine();
        System.out.println("Quarantine inspected. Restock/Discard results logged.");
    }

    private void showSummary() {
        System.out.println("\n---- RETURN PROCESSING SUMMARY ----");
        System.out.println("Restocked items:   " + controller.getRestocked().size());
        System.out.println("Quarantined items: " + controller.getQuarantine().size());
        System.out.println("Discarded items:   " + controller.getDiscarded().size());
        System.out.println("-----------------------------------");
    }

}
