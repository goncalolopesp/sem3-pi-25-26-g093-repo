package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.ReturnController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.ReturnItem;
import pt.ipp.isep.dei.pi25_26.g093.app.service.ReturnService;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class ReturnUI {

    private final ReturnController controller;
    private final ReturnService service;

    public ReturnUI() {
        this.controller = new ReturnController();
        this.service = new ReturnService();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== RETURNS & QUARANTINE MENU ===");
            System.out.println("1. Process returns from file");
            System.out.println("2. Show summary of last processing");
            System.out.println("0. Exit");
            System.out.print("Option: ");

            String option = sc.nextLine();

            switch (option) {
                case "1":
                    processReturns();
                    break;
                case "2":
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

            service.processReturns(returns);
            System.out.println("\nReturns processed successfully!");
            System.out.println("Audit log written to logs/audit_log.txt.");

        } catch (FileNotFoundException e) {
            System.out.println("Error: returns.csv file not found.");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void showSummary() {
        System.out.println("\n---- RETURN PROCESSING SUMMARY ----");
        System.out.println("Restocked items:   " + service.getRestocked().size());
        System.out.println("Quarantined items: " + service.getQuarantine().size());
        System.out.println("Discarded items:   " + service.getDiscarded().size());
        System.out.println("-----------------------------------");
    }

    static void main(String[] args) {
        new ReturnUI().run();
    }
}
