package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.NearestNSearchController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.service.NearestNSearchResult;

import java.util.List;
import java.util.Scanner;

/**
 * User interface for USEI09 - Proximity Search (Nearest-N with Filters).
 * 
 * <p>This UI class handles user interaction for nearest-N proximity searches,
 * including input validation, filter selection, and result display.
 * 
 * <p>Features:
 * <ul>
 *   <li>Validates latitude, longitude, and N (number of nearest stations) during input</li>
 *   <li>Supports optional filters (isCity, isMainStation, country, timeZone, timeZoneGroup)</li>
 *   <li>Displays results in a formatted table with distances</li>
 *   <li>Shows execution metrics and complexity analysis</li>
 * </ul>
 * 
 * @author ESINF Sprint 2 Team
 * @see NearestNSearchController
 */
public class NearestNSearchUI {

    private final NearestNSearchController controller;
    private final Scanner sc;

    // ANSI COLORS
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String BOLD = "\u001B[1m";

    /**
     * Constructs a new NearestNSearchUI with default controller.
     */
    public NearestNSearchUI() {
        this.controller = new NearestNSearchController();
        this.sc = new Scanner(System.in);
    }

    /**
     * Runs the USEI09 user interface.
     * 
     * <p>This method:
     * <ol>
     *   <li>Prompts for target point (centerLat, centerLon) and N (number of nearest stations)</li>
     *   <li>Validates input ranges (latitude [-90, 90], longitude [-180, 180], N &gt; 0)</li>
     *   <li>Prompts for optional filters (isCity, isMainStation, country, timeZone, timeZoneGroup)</li>
     *   <li>Executes the search and displays results in a formatted table</li>
     * </ol>
     * 
     * <p>The method handles exceptions gracefully and provides user-friendly
     * error messages.
     */
    public void run() {

        printHeader("USEI09 - Nearest-N Proximity Search");

        double centerLat = readDouble("Enter TARGET LATITUDE [-90, 90]: ");
        double centerLon = readDouble("Enter TARGET LONGITUDE [-180, 180]: ");
        int n = readInt("Enter N (number of nearest stations to find): ");

        printDivider();

        Boolean isCity = readOptionalBoolean("Filter CITY stations? (true/false, blank = no filter): ");
        Boolean isMainStation = readOptionalBoolean("Filter MAIN stations? (true/false, blank = no filter): ");
        String country = readOptionalString("Filter COUNTRY (PT/ES/ALL, blank = no filter): ");
        String timeZone = readOptionalString("Filter TIME ZONE (e.g. Europe/Lisbon, blank = no filter): ");
        String timeZoneGroup = readOptionalString("Filter TIME ZONE GROUP (e.g. CET, WET, blank = no filter): ");

        printDivider();

        try {
            showLoader("Processing nearest-N KD-tree search…");

            NearestNSearchResult result =
                    controller.searchNearestN(centerLat, centerLon, n,
                            isCity, isMainStation, country, timeZone, timeZoneGroup);

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
                return value;
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid number. Try again.\n" + RESET);
            }
        }
    }

    private int readInt(String msg) {
        while (true) {
            System.out.print(YELLOW + msg + RESET);
            String input = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println(RED + "N must be greater than 0. Try again.\n" + RESET);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid integer. Try again.\n" + RESET);
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

    private void showResult(NearestNSearchResult result) {

        List<CompleteStation> stations = result.getStations();
        List<Double> distances = result.getDistances();

        if (stations.isEmpty()) {
            System.out.println(RED + "\nNo stations found matching the criteria." + RESET);
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
        System.out.println(BLUE + "Execution time (ms): " + RESET + 
                String.format("%.3f", result.getExecutionTimeNanos() / 1_000_000.0));

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

