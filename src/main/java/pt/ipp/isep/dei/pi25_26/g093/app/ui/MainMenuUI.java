package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.HubCentralityController;
import pt.ipp.isep.dei.pi25_26.g093.app.ui.LP.AssembleTrainUI;
import pt.ipp.isep.dei.pi25_26.g093.app.ui.LP.DispatcherUI;
import pt.ipp.isep.dei.pi25_26.g093.app.ui.LP.PendingFreightUI;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main Menu of the application.
 *
 * <p>Provides access to the different use cases of the system, including
 * planning, simulation and infrastructure analysis modules.
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
                case 3 -> estimateTravelTime();
                case 4 -> freightPlanner();
                case 5 -> assembleTrain();
                case 6 -> runSimulation();
                case 7 -> runUpgradePlan();
                case 8 -> runMinimalBackbone();
                case 9 -> runHubScore();
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
        System.out.println("4. Freight Planner (USPL08)");
        System.out.println("5. Assemble Train (USPL09)");
        System.out.println("6. Freight Dispatcher (USPL10)");
        System.out.println("7. Directed Line Upgrade Plan (USEI11)");
        System.out.println("8. Minimal Backbone (USEI12)");
        System.out.println("9. Rail Hub Score (USEI13)");
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
        try {
            CargoHandlingUI cargoUI = new CargoHandlingUI();
            cargoUI.showMenu();
        } catch (Exception e) {
            System.out.println("Error executing CargoHandlingUI: " + e.getMessage());
        }
    }

    /**
     * Option 3 – Estimates travel time between stations (USPL03).
     */
    private void estimateTravelTime() {
        try {
            TravelTimeUI travelUI = new TravelTimeUI();
            travelUI.showMenu();
        } catch (Exception e) {
            System.out.println("Error executing TravelTimeUI: " + e.getMessage());
        }
    }

    /**
     * Option 4 – Runs the freight planner module (USPL08).
     */
    private void freightPlanner() {
        try {
            PendingFreightUI pendingFreightUI = new PendingFreightUI();
            pendingFreightUI.showMenu();
        } catch (Exception e) {
            System.out.println("Error executing PendingFreightUI: " + e.getMessage());
        }
    }

    /**
     * Option 5 – Runs the assemble train module (USPL09).
     */
    private void assembleTrain() {
        try {
            AssembleTrainUI assembleTrainUI = new AssembleTrainUI();
            assembleTrainUI.showMenu();
        } catch (Exception e) {
            System.out.println("Error executing AssembleTrainUI: " + e.getMessage());
        }
    }

    /**
     * Option 6 – Runs the freight dispatcher module (USPL10).
     */
    private void runSimulation() {
        try {
            DispatcherUI dispatcherUI = new DispatcherUI();
            dispatcherUI.showMenu();
        } catch (Exception e) {
            System.out.println("Error executing DispatcherUI: " + e.getMessage());
        }
    }

    /**
     * Option 7 – Runs the minimal backbone module (USEI10).
     */
    private void runMinimalBackbone() {
        try {
            MinimalBackboneUI minimalBackboneUI = new MinimalBackboneUI();
            minimalBackboneUI.run();
        } catch (Exception e) {
            System.out.println("Error executing MinimalBackboneUI: " + e.getMessage());
        }
    }

    /**
     * Option 8 – Runs the Directed Line Upgrade Plan module (USEI11).
     */
    private void runUpgradePlan() {
        try {
            UpgradePlanUI upgradeUI = new UpgradePlanUI();
            upgradeUI.showMenu();
        } catch (Exception e) {
            System.out.println("Error executing UpgradePlanUI: " + e.getMessage());
        }
    }

    private void runHubScore()
    {
        try{
            HubCentralityUI hubCentralityUI = new HubCentralityUI();
            hubCentralityUI.run();
        }catch (Exception e)
        {
            System.out.println("Error executing HubCentralityUI: " + e.getMessage());
        }
    }
}
