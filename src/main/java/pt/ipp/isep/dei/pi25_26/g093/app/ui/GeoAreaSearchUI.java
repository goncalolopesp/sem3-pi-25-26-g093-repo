package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.GeoAreaSearchController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaSearchResult;

import java.util.List;
import java.util.Scanner;

public class GeoAreaSearchUI {

    private final GeoAreaSearchController controller;
    private final Scanner sc;

    // ANSI Colors (optional)
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";

    public GeoAreaSearchUI() {
        this.controller = new GeoAreaSearchController();
        this.sc = new Scanner(System.in);
    }

    public void run() {

        printHeader("USEI08 — Search Stations by Geographical Area");

        try {
            double latMin = readDouble("Latitude MIN [-90,90]");
            double latMax = readDouble("Latitude MAX [-90,90]");
            double lonMin = readDouble("Longitude MIN [-180,180]");
            double lonMax = readDouble("Longitude MAX [-180,180]");

            // Validate ranges before proceeding
            if (latMin > latMax) {
                System.out.println(RED + "ERROR: latMin cannot be greater than latMax." + RESET);
                pause();
                return;
            }
            if (lonMin > lonMax) {
                System.out.println(RED + "ERROR: lonMin cannot be greater than lonMax." + RESET);
                pause();
                return;
            }
            if (latMin < -90 || latMax > 90) {
                System.out.println(RED + "ERROR: Latitude must be within [-90, 90]." + RESET);
                pause();
                return;
            }
            if (lonMin < -180 || lonMax > 180) {
                System.out.println(RED + "ERROR: Longitude must be within [-180, 180]." + RESET);
                pause();
                return;
            }

            printlnSeparator();

            Boolean isCity = readOptionalBoolean("Filter only CITY stations (true/false, blank = no filter)");
            Boolean isMain = readOptionalBoolean("Filter only MAIN stations (true/false, blank = no filter)");
            String country = readOptionalString("Filter COUNTRY (PT/ES/ALL, blank = no filter)");

            printlnSeparator();

            GeoAreaSearchResult result =
                    controller.searchArea(latMin, latMax, lonMin, lonMax, isCity, isMain, country);

            printResult(result);

        } catch (Exception e) {
            System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
        }

        pause();
    }

    // =======================
    //       UI HELPERS
    // =======================

    private void printHeader(String title) {
        System.out.println("\n" + CYAN + BOLD +
                "=============================================\n" +
                "     " + title + "\n" +
                "=============================================" +
                RESET);
    }

    private void printlnSeparator() {
        System.out.println(CYAN + "---------------------------------------------" + RESET);
    }

    private void pause() {
        System.out.print("\nPress ENTER to return... ");
        sc.nextLine();
    }

    // =======================
    //     READ METHODS
    // =======================

    private double readDouble(String msg) {
        while (true) {
            System.out.print(YELLOW + msg + ": " + RESET);
            String input = sc.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid number. Try again." + RESET);
            }
        }
    }

    private Boolean readOptionalBoolean(String msg) {
        System.out.print(YELLOW + msg + ": " + RESET);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) return null;

        if (input.equalsIgnoreCase("true")) return true;
        if (input.equalsIgnoreCase("false")) return false;

        System.out.println(RED + "Invalid boolean. Ignoring filter." + RESET);
        return null;
    }

    private String readOptionalString(String msg) {
        System.out.print(YELLOW + msg + ": " + RESET);
        String input = sc.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

    // =======================
    //     OUTPUT RESULT
    // =======================

    private void printResult(GeoAreaSearchResult result) {

        List<CompleteStation> stations = result.getStations();

        System.out.println(GREEN + "\n=== RESULTS FOUND: " + stations.size() + " station(s) ===" + RESET);

        if (stations.isEmpty()) {
            System.out.println(RED + "No stations matched your criteria." + RESET);
        } else {
            for (CompleteStation cs : stations) {
                System.out.println(" • " + cs);
            }
        }

        printlnSeparator();
        System.out.println(BLUE + "Execution time (ns): " + RESET + result.getExecutionTimeNanos());
        System.out.println(BLUE + "Reported complexity: " + RESET + result.getTemporalComplexity());

        printlnSeparator();
        System.out.println(GREEN + "Sample queries for reference:" + RESET);

        for (String q : result.getSampleQueries()) {
            System.out.println(" - " + q);
        }

        printlnSeparator();
    }
}
