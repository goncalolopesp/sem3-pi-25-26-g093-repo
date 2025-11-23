package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.CompleteStationIndexController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CompleteStationUI {

    private final Scanner sc = new Scanner(System.in);
    private final CompleteStationIndexController controller =
            new CompleteStationIndexController();

    public void run(){
        showMenu();
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== USEI06 - Time-Zone Index and Windowed Queries ===");
            System.out.println("1 - Search by latitude range");
            System.out.println("2 - Search by longitude range");
            System.out.println("3 - Search by time zone group");
            System.out.println("4 - Search by time zone group window");
            System.out.println("5 - Search by time zone group AND country");
            System.out.println("0 - Back");
            System.out.print("Option: ");

            String op = sc.nextLine().trim();
            switch (op) {
                case "1": menuLatRange(); break;
                case "2": menuLonRange(); break;
                case "3": menuTZG(); break;
                case "4": menuTZGWindow(); break;
                case "5": menuTZGAndCountry(); break;
                case "0": return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void menuLatRange() {
        try {
            System.out.print("Min latitude: ");
            double min = readDouble();
            if (min < -90 || min > 90) {
                System.out.println("Invalid latitude value (must be in [-90, 90]).");
                return;
            }

            System.out.print("Max latitude: ");
            double max = readDouble();
            if (max < -90 || max > 90) {
                System.out.println("Invalid latitude value (must be in [-90, 90]).");
                return;
            }

            Map<Double, List<CompleteStation>> res = controller.getByLatitudeRange(min, max);
            Map.Entry<Double, List<CompleteStation>> entry = res.entrySet().iterator().next();
            printStations(res.get(entry.getKey()));

        } catch (IllegalStateException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Returning to menu.");
        }
    }

    private void menuLonRange() {
        try {
            System.out.print("Min longitude: ");
            double min = readDouble();
            if (min < -180 || min > 180) {
                System.out.println("Invalid longitude value (must be in [-180, 180]).");
                return;
            }

            System.out.print("Max longitude: ");
            double max = readDouble();
            if (max < -180 || max > 180) {
                System.out.println("Invalid longitude value (must be in [-180, 180]).");
                return;
            }

            Map<Double, List<CompleteStation>> res = controller.getByLongitudeRange(min, max);
            Map.Entry<Double, List<CompleteStation>> entry = res.entrySet().iterator().next();
            printStations(res.get(entry.getKey()));

        } catch (IllegalStateException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Returning to menu.");
        }
    }

    private void menuTZG() {
        try {
            System.out.print("Time zone group (e.g. CET, WET/GMT): ");
            String tzg = sc.nextLine().trim();
            if (tzg.isEmpty()) {
                System.out.println("Time zone group cannot be empty.");
                return;
            }

            Map<Double, List<CompleteStation>> res = controller.getByTimeZoneGroup(tzg);
            Map.Entry<Double, List<CompleteStation>> entry = res.entrySet().iterator().next();
            printStations(res.get(entry.getKey()));

        } catch (IllegalStateException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void menuTZGWindow() {
        try {
            System.out.print("Start time zone group: ");
            String start = sc.nextLine().trim();
            if (start.isEmpty()) {
                System.out.println("Time zone group cannot be empty.");
                return;
            }

            System.out.print("End time zone group: ");
            String end = sc.nextLine().trim();
            if (end.isEmpty()) {
                System.out.println("Time zone group cannot be empty.");
                return;
            }

            Map<Double, List<CompleteStation>> res = controller.getByTimeZoneGroupWindow(start, end);
            Map.Entry<Double, List<CompleteStation>> entry = res.entrySet().iterator().next();
            printStations(res.get(entry.getKey()));

        } catch (IllegalStateException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void menuTZGAndCountry() {
        try {
            System.out.print("Time zone group: ");
            String tzg = sc.nextLine().trim();
            if (tzg.isEmpty()) {
                System.out.println("Time zone group cannot be empty.");
                return;
            }

            System.out.print("Country code (e.g. PT, FR): ");
            String country = sc.nextLine().trim();
            if (country.isEmpty()) {
                System.out.println("Country cannot be empty.");
                return;
            }

            Map<Double, List<CompleteStation>> res = controller.getByTimeZoneGroupAndCountry(tzg, country);
            Map.Entry<Double, List<CompleteStation>> entry = res.entrySet().iterator().next();
            printStations(res.get(entry.getKey()));

        } catch (IllegalStateException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private double readDouble() {
        String line = sc.nextLine().trim();
        return Double.parseDouble(line);
    }

    private void printStations(List<CompleteStation> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No results.");
            return;
        }
        System.out.println("\n--- Results (" + list.size() + ") ---");
        for (CompleteStation cs : list) {
            System.out.println(cs);
        }
    }
}
