package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.GeoAreaCircleSearchController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaCircleSearchResult;

import java.util.List;
import java.util.Scanner;

public class GeoAreaCircleSearchUI {

    private final GeoAreaCircleSearchController controller;
    private final Scanner sc;

    public GeoAreaCircleSearchUI() {
        this.controller = new GeoAreaCircleSearchController();
        this.sc = new Scanner(System.in);
    }

    public void run() {

        System.out.println("\n=== USEI09 - Search Stations by Circular Region ===");

        double centerLat = readDouble("Center latitude [-90, 90]: ");
        double centerLon = readDouble("Center longitude [-180, 180]: ");
        double radiusKm = readDouble("Radius (km): ");

        Boolean isCity = readOptionalBoolean("Only city stations? (true/false, blank = ignore): ");
        Boolean isMainStation = readOptionalBoolean("Only main stations? (true/false, blank = ignore): ");
        String country = readOptionalString("Country (PT/ES/ALL, blank = ignore): ");

        try {
            GeoAreaCircleSearchResult result =
                    controller.searchCircle(centerLat, centerLon, radiusKm,
                            isCity, isMainStation, country);

            // Por agora, só chamar um método vazio:
            showResult(result);

        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        pause();
    }

    private double readDouble(String msg) {
        while (true) {
            System.out.print(msg);
            String input = sc.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private Boolean readOptionalBoolean(String msg) {
        System.out.print(msg);
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return null;
        if (input.equalsIgnoreCase("true")) return true;
        if (input.equalsIgnoreCase("false")) return false;
        System.out.println("Invalid boolean. Ignoring filter.");
        return null;
    }

    private String readOptionalString(String msg) {
        System.out.print(msg);
        String input = sc.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

    private void pause() {
        System.out.print("\nPress ENTER to return... ");
        sc.nextLine();
    }

    private void showResult(GeoAreaCircleSearchResult result) {
        List<CompleteStation> stations = result.getStations();
        List<Double> distances = result.getDistances();

        if (stations.isEmpty()) {
            System.out.println("\nNo stations found within the given radius and filters.");
        } else {
            System.out.println("\n--- Stations found: " + stations.size() + " ---");
            
            for (int i = 0; i < stations.size(); i++) {
                CompleteStation station = stations.get(i);
                Double distance = distances.get(i);
                System.out.println("[dist=" + String.format("%.2f", distance) + " km] " + station.toString());
            }
        }

        System.out.println("\nExecution time (ns): " + result.getExecutionTimeNanos());
        System.out.println("Complexity: " + result.getComplexity());
        System.out.println("Sample queries:");
        for (String q : result.getSampleQueries()) {
            System.out.println(" - " + q);
        }
    }
}

