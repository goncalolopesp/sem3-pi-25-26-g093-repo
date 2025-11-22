package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main Menu of the application.
 * Provides access to:
 *  1. Team Presentation
 *  2. Cargo Handling Operations (USPL02)
 *  3. Travel Time Between Stations (USPL03)
 *  0. Exit
 */
public class MainMenuUI {

    /**
     * Displays the main menu and executes the selected option.
     */
    public void showMainMenu() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            displayHeader();
            option = readOption(scanner);

            switch (option) {
                case 1 -> showTeamPresentation();
                case 2 -> runCargoHandlingUI();
                case 4 -> runSimulation();
                case 0 -> System.out.println("Exiting program...");
                default -> System.out.println("Invalid option. Please try again.");
            }

        } while (option != 0);
    }

    /**
     * Prints the menu header and options.
     */
    private void displayHeader() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Team Presentation");
        System.out.println("2. Cargo Handling Operations (USPL02)");
        System.out.println("3. Travel Time Between Stations (USPL03)");
        System.out.println("4. Freight Dispatcher (USPL07)");
        System.out.println("0. Exit");
        System.out.println("-----------------------");
        System.out.print("Select an option: ");
    }

    /**
     * Reads and validates the user's input.
     */
    private int readOption(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.nextLine();
            System.out.print("Select an option: ");
        }
        int option = scanner.nextInt();
        scanner.nextLine();
        return option;
    }

    /**
     * Option 1 – Displays the team presentation.
     */
    private void showTeamPresentation() {
        TeamPresentationUI teamUI = new TeamPresentationUI();
        teamUI.show();
    }

    /**
     * Option 2 – Runs the Cargo Handling Operations module (USPL02).
     */
    private void runCargoHandlingUI() {
        System.out.println("\n=== USPL02 - Cargo Handling Operations ===");
        try {
            CargoHandlingUI cargoUI = new CargoHandlingUI();
            cargoUI.showMenu();
        } catch (Exception e) {
            System.out.println("Error executing CargoHandlingUI: " + e.getMessage());
        }
    }

    private void runSimulation() {
        try {
            DispatcherUI dispatcherUI = new DispatcherUI();
            dispatcherUI.showMenu();
        } catch (Exception e) {
            System.out.println("Error executing DispatcherUI: " + e.getMessage());
        }
    }
}
