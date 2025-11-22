package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.GeoAreaCircleSearchController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaCircleSearchResult;

import java.util.List;
import java.util.Scanner;

public class GeoAreaCircleSearchUI {

    private final GeoAreaCircleSearchController controller;
    private final Scanner sc;

    // ANSI COLORS
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String BOLD = "\u001B[1m";

    public GeoAreaCircleSearchUI() {
        this.controller = new GeoAreaCircleSearchController();
        this.sc = new Scanner(System.in);
    }

    public void run() {

        printHeader("USEI09 - Circular Geographical Search");

        double centerLat = readDouble("Enter CENTER LATITUDE [-90, 90]: ");
        double centerLon = readDouble("Enter CENTER LONGITUDE [-180, 180]: ");
        double radiusKm = readDouble("Enter RADIUS (km): ");

        printDivider();

        Boolean isCity = readOptionalBoolean("Filter CITY stations? (true/false, blank = no filter): ");
        Boolean isMainStation = readOptionalBoolean("Filter MAIN stations? (true/false, blank = no filter): ");
        String country = readOptionalString("Filter COUNTRY (PT/ES/ALL, blank = no filter): ");

        printDivider();

        try {
            showLoader("Processing KD-tree search…");

            GeoAreaCircleSearchResult result =
                    controller.searchCircle(centerLat, centerLon, radiusKm,
                            isCity, isMainStation, country);

            showResult(result);

        } catch (Exception ex) {
            System.out.println(RED + "\nERROR: " + ex.getMessage() + RESET);
        }

        pause();
    }

    // ==============================================
    // INPUT METHODS
    // ==============================================

    private double readDouble(String msg) {
        while (true) {
            System.out.print(YELLOW + msg + RESET);
            String input = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                // Validate latitude range if message contains "LATITUDE"
                if (msg.toUpperCase().contains("LATITUDE")) {
                    if (value < -90 || value > 90) {
                        System.out.println(RED + "Latitude must be within [-90, 90]. Try again.\n" + RESET);
                        continue;
                    }
                }
                // Validate longitude range if message contains "LONGITUDE"
                if (msg.toUpperCase().contains("LONGITUDE")) {
                    if (value < -180 || value > 180) {
                        System.out.println(RED + "Longitude must be within [-180, 180]. Try again.\n" + RESET);
                        continue;
                    }
                }
                // Validate radius if message contains "RADIUS"
                if (msg.toUpperCase().contains("RADIUS")) {
                    if (value <= 0) {
                        System.out.println(RED + "Radius must be greater than 0. Try again.\n" + RESET);
                        continue;
                    }
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid number. Try again.\n" + RESET);
            }
        }
    }

    private Boolean readOptionalBoolean(String msg) {
        System.out.print(YELLOW + msg + RESET);
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return null;
        if (input.equalsIgnoreCase("true")) return true;
        if (input.equalsIgnoreCase("false")) return false;
        System.out.println(RED + "Invalid boolean → ignoring filter.\n" + RESET);
        return null;
    }

    private String readOptionalString(String msg) {
        System.out.print(YELLOW + msg + RESET);
        String input = sc.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

    // ==============================================
    // DISPLAY HELPERS
    // ==============================================

    private void printHeader(String title) {
        int width = 60;  // largura da caixa
        int padding = (width - title.length()) / 2;

        String top = "╔" + "═".repeat(width) + "╗";
        String bottom = "╚" + "═".repeat(width) + "╝";
        String middle = "║" + " ".repeat(padding) + title + " ".repeat(width - padding - title.length()) + "║";

        System.out.println("\n" + CYAN + BOLD + top + RESET);
        System.out.println(CYAN + BOLD + middle + RESET);
        System.out.println(CYAN + BOLD + bottom + RESET);
    }

    private void printDivider() {
        System.out.println(BLUE +
                "────────────────────────────────────────────────────────────" + RESET);
    }

    private void pause() {
        System.out.print("\nPress ENTER to return... ");
        sc.nextLine();
    }

    private void showLoader(String msg) {
        System.out.print(CYAN + msg + RESET);
        try { Thread.sleep(400); } catch (Exception ignored) {}
        System.out.print(".");
        try { Thread.sleep(400); } catch (Exception ignored) {}
        System.out.print(".");
        try { Thread.sleep(400); } catch (Exception ignored) {}
        System.out.println(".");
    }

    // ==============================================
    // RESULT PRESENTATION
    // ==============================================

    private void showResult(GeoAreaCircleSearchResult result) {

        List<CompleteStation> stations = result.getStations();
        List<Double> distances = result.getDistances();

        if (stations.isEmpty()) {
            System.out.println(RED + "\nNo stations found in the specified circular region." + RESET);
            return;
        }

        System.out.println(GREEN + "\nStations found: " + stations.size() + RESET);
        printTableHeader();

        for (int i = 0; i < stations.size(); i++) {
            CompleteStation s = stations.get(i);
            double d = distances.get(i);

            System.out.printf("│ %-8.2f │ %-9.4f │ %-9.4f │ %-35s │\n",
                    d,
                    s.getLatitude(),
                    s.getLongitude(),
                    s.getStation());
        }

        printTableFooter();

        printDivider();
        System.out.println(BLUE + "Execution time (ns): " + RESET + result.getExecutionTimeNanos());
        System.out.println(BLUE + "Complexity: " + RESET + result.getComplexity());

        printDivider();
        System.out.println(GREEN + "Sample queries:" + RESET);
        for (String q : result.getSampleQueries()) {
            System.out.println(" • " + q);
        }
        printDivider();
    }

    private void printTableHeader() {
        System.out.println(BOLD +
                "\n┌──────────┬───────────┬───────────┬──────────────────────────────────────┐" + RESET);
        System.out.printf(BOLD + "│ %-8s │ %-9s │ %-9s │ %-35s │\n" + RESET,
                "Dist(km)", "Latitude", "Longitude", "Station");
        System.out.println(
                "├──────────┼───────────┼───────────┼──────────────────────────────────────┤");
    }

    private void printTableFooter() {
        System.out.println(
                "└──────────┴───────────┴───────────┴──────────────────────────────────────┘");
    }
}
