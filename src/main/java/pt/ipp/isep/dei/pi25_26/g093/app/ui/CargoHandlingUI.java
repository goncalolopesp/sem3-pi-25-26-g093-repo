package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.BalancedTreeController;

/**
 * USPL02 — Cargo Handling Operations Interface
 * Allows a Station Storage Manager to access all USEI01–USEI09 functionalities
 * through a text-based interface.
 */
public class CargoHandlingUI {

    static java.util.Scanner sc = new java.util.Scanner(System.in);

    /**
     * Shows the menu for Cargo Handling operations.
     */
    public void showMenu() {
        int option;

        do {
            System.out.println("\n=== CARGO HANDLING MENU ===");
            System.out.println("1. USEI01 - Unloading Wagons");
            System.out.println("2. USEI02 - Order Allocation");
            System.out.println("3. USEI03 - Picking Plans");
            System.out.println("4. USEI04 - Pick Path Sequencing");
            System.out.println("5. USEI05 - Returns & Quarantine");
            System.out.println("6. USEI06 - Time-Zone Index and Windowed Queries");
            System.out.println("7. USEI07 - Build a balanced 2D-Tree on Latitude/Longitude");
            System.out.println("8. USEI08 - Search by Geographical Area");
            System.out.println("9. USEI09 - Search by Circular Region (radius)");
            System.out.println("0. Back to the main menu");
            System.out.print("Choose a option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> executeUSEI01();
                case 2 -> executeUSEI02();
                case 3 -> executeUSEI03();
                case 4 -> executeUSEI04();
                case 5 -> executeUSEI05();
                case 6 -> executeUSEI06();
                case 7 -> executeUSEI07();
                case 8 -> executeUSEI08();
                case 9 -> executeUSEI09();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option.");
            }

        } while (option != 0);
    }

    /**
     * USEI01 - Unloading Wagons
     */
    private void executeUSEI01() {
        System.out.println("\n=== USEI01 - Unloading Wagons ===");
        try {
            WagonCargoUI wagonUI = new WagonCargoUI();
            wagonUI.showMenu();
        } catch (Exception e) {
            System.out.println("Error executing USEI01: " + e.getMessage());
        }
    }

    /**
     * USEI02 - Order Allocation
     */
    private void executeUSEI02() {
        System.out.println("\n=== USEI02 - Order Allocation ===");
        try {
            AllocationUI allocationUI = new AllocationUI();
            allocationUI.run();
        } catch (Exception e) {
            System.out.println("Error executing USEI02: " + e.getMessage());
        }
    }

    /**
     * USEI03 - Picking Plans
     */
    private void executeUSEI03() {
        System.out.println("\n=== USEI03 - Picking Plans ===");
        try {
            PickingPlansUI pickingPlansUI = new PickingPlansUI();
            pickingPlansUI.run();
        } catch (Exception e) {
            System.out.println("Error executing USEI03: " + e.getMessage());
        }
    }

    /**
     * USEI04 - Pick Path Sequencing
     */
    private void executeUSEI04() {

        System.out.println("\n=== USEI04 - Pick Path Sequencing ===");

        try{
            PickPathUI pickPathUI = new PickPathUI();
            pickPathUI.run();

        } catch (Exception e) {

            System.out.println("Error executing USEI04: " + e.getMessage());

        }

    }

    /**
     * USEI05 - Returns & Quarantine
     */
    private void executeUSEI05() {
        System.out.println("\n=== USEI05 - Returns & Quarantine ===");
        try {
            ReturnUI returnUI = new ReturnUI();
            returnUI.run();
        } catch (Exception e) {
            System.out.println("Error executing USEI05 - Returns & Quarantine: " + e.getMessage());
        }
    }

    /**
     * USEI06 - Time-Zone Index and Windowed Queries
     */
    private void executeUSEI06(){
        System.out.println("\n=== USEI06 - Time-Zone Index and Windowed Queries ===");
        try {
            CompleteStationUI stationUI = new CompleteStationUI();
            stationUI.run();
        } catch (Exception e) {
            System.out.println("Error executing USEI06: " + e.getMessage());
        }
    }

    /**
     * USEI07 - Build a balanced 2D-Tree on Latitude/Longitude
     */
    private void executeUSEI07(){
        System.out.println("\n=== USEI07 - Build a balanced 2D-Tree on Latitude/Longitude ===");
        try {
            BalancedTreeUI treeUI = new BalancedTreeUI();
            treeUI.run();
        } catch (Exception e) {
            System.out.println("Error executing USEI07: " + e.getMessage());
        }
    }

    /**
     * USEI08 - Search by Geographical Area
     */
    private void executeUSEI08() {
        System.out.println("\n=== USEI08 - Search by Geographical Area ===");
        try {
            BalancedTreeController controller = new BalancedTreeController();
            if (!controller.treeExists()) {
                System.out.println("No KD-tree available. Please build it first via USEI07.");
                return;
            }

            GeoAreaSearchUI searchUI = new GeoAreaSearchUI();
            searchUI.run();
        } catch (Exception e) {
            System.out.println("Error executing USEI08: " + e.getMessage());
        }
    }

    /**
     * USEI09 - Search by Circular Region (radius)
     */
    private void executeUSEI09() {
        System.out.println("\n=== USEI09 - Search by Circular Region (radius) ===");
        try {
            BalancedTreeController controller = new BalancedTreeController();
            if (!controller.treeExists()) {
                System.out.println("No KD-tree available. Please build it first via USEI07.");
                return;
            }

            GeoAreaCircleSearchUI ui = new GeoAreaCircleSearchUI();
            ui.run();
        } catch (Exception e) {
            System.out.println("Error executing USEI09: " + e.getMessage());
        }
    }
}
