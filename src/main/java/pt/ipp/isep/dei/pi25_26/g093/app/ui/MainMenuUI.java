package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main Menu of the application.
 * Provides access to the different modules:
 *  - USEI01: WagonUI (Show Wagons)
 *  - USEI02: AllocationUI (Order Allocation)
 *  - USEI03–05 via CargoHandlingUI (USPL02)
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
                case 1 -> runWagonUI();
                case 2 -> runAllocationUI();
                case 3 -> showStationStorageManagerInfo();
                case 4 -> runCargoHandlingUI();
                case 0 -> System.out.println("A sair do programa...");
                default -> System.out.println("❌ Opção inválida. Tente novamente.");
            }

        } while (option != 0);
    }

    /**
     * Prints the menu header and options.
     */
    private void displayHeader() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Show Wagons (USEI01)");
        System.out.println("2. Order Eligibility & Allocation (USEI02)");
        System.out.println("3. As a Station Storage Manager");
        System.out.println("4. Cargo Handling Operations (USPL02)");
        System.out.println("0. Exit");
        System.out.println("-----------------------");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Reads the user's input safely.
     */
    private int readOption(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Por favor insira um número válido.");
            scanner.nextLine();
            System.out.print("Escolha uma opção: ");
        }
        int option = scanner.nextInt();
        scanner.nextLine(); // limpar newline
        return option;
    }

    /**
     * Executes USEI01 – Show Wagons.
     */
    private void runWagonUI() {
        System.out.println("\n=== USEI01 - Show Wagons ===");
        try {
            WagonUI wagonUI = new WagonUI();
            wagonUI.run();
        } catch (Exception e) {
            System.out.println("❌ Erro ao executar WagonUI: " + e.getMessage());
        }
    }

    /**
     * Executes USEI02 – Order Allocation.
     */
    private void runAllocationUI() {
        System.out.println("\n=== USEI02 - Order Allocation ===");
        try {
            AllocationUI allocationUI = new AllocationUI();
            allocationUI.run();
        } catch (Exception e) {
            System.out.println("❌ Erro ao executar AllocationUI: " + e.getMessage());
        }
    }

    /**
     * Placeholder for Station Storage Manager functionality.
     */
    private void showStationStorageManagerInfo() {
        System.out.println("\n=== Station Storage Manager ===");
        System.out.println("Esta opção será usada para funcionalidades futuras.");
    }

    /**
     * Executes USPL02 – Cargo Handling Operations (USEI01→USEI05).
     */
    private void runCargoHandlingUI() {
        System.out.println("\n=== USPL02 - Cargo Handling Operations ===");
        try {
            CargoHandlingUI cargoUI = new CargoHandlingUI();
            cargoUI.showMenu();
        } catch (Exception e) {
            System.out.println("❌ Erro ao executar CargoHandlingUI: " + e.getMessage());
        }
    }
}
