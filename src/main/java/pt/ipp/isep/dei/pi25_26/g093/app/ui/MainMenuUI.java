package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main Menu of the application.
 * Simplified to include only:
 *  1. Team Presentation
 *  2. Cargo Handling Operations (USPL02)
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
        System.out.println("1. Apresentação da Equipa");
        System.out.println("2. Cargo Handling Operations (USPL02)");
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
        scanner.nextLine(); // limpa newline
        return option;
    }

    /**
     * Option 1 – Displays team presentation.
     */
    private void showTeamPresentation() {
        TeamPresentationUI teamUI = new TeamPresentationUI();
        teamUI.show();
    }

    /**
     * Option 2 – Executes USPL02 (Cargo Handling Operations menu).
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
