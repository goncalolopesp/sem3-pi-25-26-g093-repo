package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.UpgradePlanController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.service.UpgradePlanResult;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.RailwayNetworkCsvLoader;

import java.util.List;
import java.util.Scanner;

/**
 * User Interface for USEI11 - Directed Line Upgrade Plan.
 *
 * <p>This UI loads the railway network from CSV files and computes
 * a valid upgrade order or detects dependency cycles.
 */
public class UpgradePlanUI {

    private final UpgradePlanController controller;
    private final Scanner scanner;

    public UpgradePlanUI() {
        this.controller = new UpgradePlanController();
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        int option;

        do {
            System.out.println("\n=== USEI11 - Directed Line Upgrade Plan ===");
            System.out.println("1. Calculate Upgrade Plan (using CSV data)");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            while (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.print("Select an option: ");
            }

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> calculateUpgradePlanFromCsv();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option.");
            }

        } while (option != 0);
    }

    /**
     * Loads data from CSV files and calculates the upgrade plan.
     */
    private void calculateUpgradePlanFromCsv() {
        try {
            String stationsPath = "train_station_dataset/stations.csv";
            String linesPath = "train_station_dataset/lines.csv";

            RailwayNetworkCsvLoader loader =
                    new RailwayNetworkCsvLoader(stationsPath, linesPath);

            List<NetworkNode> nodes = loader.loadNodes();
            List<NetworkEdge> edges = loader.loadEdges(nodes);

            UpgradePlanResult result =
                    controller.calculateUpgradePlan(nodes, edges);

            displayResults(result);

        } catch (Exception e) {
            System.out.println("Error processing CSV data: " + e.getMessage());
        }
    }

    private void displayResults(UpgradePlanResult result) {
        System.out.println("\n=== Upgrade Plan Results ===");

        if (result.hasCycle()) {
            System.out.println("❌ Cycle detected. Upgrade order is impossible.");
            System.out.println("Nodes involved in cycles:");
            result.getCycleNodes()
                    .forEach(n -> System.out.println(" - " + n.getName()));
        } else {
            System.out.println("✅ Valid upgrade order:");
            int i = 1;
            for (NetworkNode node : result.getTopologicalOrder()) {
                System.out.println(i++ + ". " + node.getName());
            }
        }

        System.out.println("\n" + result.getComplexityAnalysis());
    }
}
