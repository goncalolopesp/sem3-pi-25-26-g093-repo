package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.ReturnController;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.WagonController;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.WarehouseController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.ReturnItem;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * User interface that supports operations related to return processing and quarantine handling.
 *
 * Allows terminal operators to:
 * 1. Load and process returned items into quarantine or discard
 * 2. Inspect the quarantine items in priority order
 * 3. Display summary statistics of processed returns
 *
 * This UI is designed according to USEI05 requirements.
 */
public class ReturnUI {

    /** Controller responsible for quarantine and return logic. */
    private final ReturnController controller;

    /**
     * Creates a ReturnUI instance initializing controllers and loading
     * warehouse structure from CSV dataset.
     */
    public ReturnUI() {
        WagonController wagonController = new WagonController();
        WarehouseController warehouseController = new WarehouseController();

        try {
            CSVParser baysParser = new CSVParser("train_station_dataset/bays.csv");
            warehouseController.loadWarehouses(baysParser.getCSVData());
            System.out.println("Warehouses loaded successfully");
        } catch (Exception e) {
            System.out.println("\nError loading warehouses: " + e.getMessage());
        }

        this.controller = new ReturnController(wagonController, warehouseController);
    }

    /**
     * Displays the Returns & Quarantine menu and handles user interaction
     * until the operator chooses to exit.
     */
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

    /**
     * Loads returned items from CSV and delegates classification and quarantine handling
     * to the ReturnController.
     */
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

    /**
     * Requests the controller to inspect all items currently in quarantine and
     * apply restock or discard decisions.
     */
    private void inspectQuarantine() {
        controller.inspectQuarantine();
        System.out.println("Quarantine inspected. Restock/Discard results logged.");
    }

    /**
     * Shows statistics about the processing of returned items since the last run.
     */
    private void showSummary() {
        System.out.println("\n---- RETURN PROCESSING SUMMARY ----");
        System.out.println("Restocked items:   " + controller.getRestocked().size());
        System.out.println("Quarantined items: " + controller.getQuarantine().size());
        System.out.println("Discarded items:   " + controller.getDiscarded().size());
        System.out.println("-----------------------------------");
    }
}
