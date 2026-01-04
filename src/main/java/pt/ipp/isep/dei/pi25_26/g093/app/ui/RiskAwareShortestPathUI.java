package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.RiskAwareShortestPathController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.PathResult;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Edge;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.GraphRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.List;
import java.util.Scanner;

public class RiskAwareShortestPathUI {

    private final RiskAwareShortestPathController controller;
    private final GraphRepository repo;

    public RiskAwareShortestPathUI() {
        this.controller = new RiskAwareShortestPathController();
        this.repo = Repositories.getInstance().getGraphRepository();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    calculatePath(scanner);
                    break;
                case "0":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n--- RISK-AWARE PATH CALCULATION MENU ---");
        System.out.println("1 - Calculate Risk-Optimized Path");
        System.out.println("0 - Exit");
        System.out.print("Choose an option: ");
    }

    private void calculatePath(Scanner scanner) {
        System.out.println("\n--- Calculate Risk-Optimized Path ---");
        System.out.println("This algorithm considers potential negative cost optimizations.");

        listStations();

        CompleteStation start = readStation(scanner, "Enter the name of the origin station: ");
        if (start == null) return;

        CompleteStation end = readStation(scanner, "Enter the name of the destination station: ");
        if (end == null) return;

        System.out.println("Calculating optimized path from " + start.getStation() + " to " + end.getStation() + "...");

        PathResult result = controller.computeShortestPathWithRisk(start, end);

        if (!result.isSuccess()) {
            System.out.println("Error: " + result.getErrorMessage());

            if (result.getNegativeCycleStations() != null) {
                System.out.println("Stations involved in negative cycle:");
                for (CompleteStation s : result.getNegativeCycleStations()) {
                    System.out.println(" - " + s.getStation());
                }

                System.out.println("Edges involved in negative cycle:");
                for (Edge<CompleteStation, Double> e : result.getNegativeCycleEdges()) {
                    System.out.println(" - " + e.getVOrig().getStation() +
                            " -> " + e.getVDest().getStation());
                }
            }

        } else {
            printAnalysisResults(result);
            generateShortestPathFiles(start, end, result);
        }
    }

    private CompleteStation readStation(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String stationName = scanner.nextLine().trim();
            if (stationName.equalsIgnoreCase("0") || stationName.equalsIgnoreCase("exit")) {
                return null;
            }
            CompleteStation station = controller.findStationByName(stationName);
            if (station != null) {
                return station;
            } else {
                System.out.println("Station not found. Please try again.");
                System.out.println("Write '0' or 'exit' to comeback to menu.");
            }
        }
    }

    private void printAnalysisResults(PathResult result) {
        System.out.println("\n[*] Optimized Path Calculated:");
        System.out.println("Path Sequence:");

        double totalCost = result.getTotalCost();

        List<CompleteStation> path = result.getPath();

        for (int i = 0; i < path.size(); i++) {
            CompleteStation station = path.get(i);
            System.out.print(station.getStation());

            if (i < path.size() - 1) {
                CompleteStation nextStation = path.get(i + 1);
                double edgeCost = controller.getEdgeCost(station, nextStation);
                if (edgeCost >= 0) {
                    System.out.printf(" (Cost: %.2f) -> ", edgeCost);
                } else {
                    System.out.println("Error: No edge found between " + station.getStation() + " and " + nextStation.getStation());
                    return;
                }
            }

            if (i == path.size() - 1) {
                System.out.println();
            }
        }

        System.out.printf("\nTotal Accumulated Cost/Risk: %.2f\n", totalCost);
    }


    private void listStations() {

        System.out.println("\n--- Available Stations ---");
        List<CompleteStation> stations = repo.getGraph().vertices();

        if (stations.isEmpty()) {
            System.out.println("No stations found in the network.");
        } else {
            System.out.println("List of available stations:");

            int columns = 5;
            int counter = 0;

            for (CompleteStation station : stations) {
                System.out.printf("%-20s", station.getStation());
                counter++;
                if (counter % columns == 0) {
                    System.out.println();
                }
            }

            if (counter % columns != 0) {
                System.out.println();
            }
        }
    }

    private void generateShortestPathFiles(CompleteStation start, CompleteStation end, PathResult result) {
        String filename = start.getStation() + "_to_" + end.getStation();  // Nome baseado nas estações

        String filePath = "generated-graphs/" + filename;
        controller.generateDotFileForPath(result.getPath(), filePath);

        boolean imageGenerated = controller.generatePathImage(filePath);

        if (imageGenerated) {
            System.out.println("Successfully generated " + filename + ".dot and " + filename + ".svg");
        } else {
            System.out.println("Failed to generate SVG image. Make sure 'neato' is installed.");
        }
    }

}
