package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainMenuUI {

    /**
     * Shows the main menu and asks for an option.
     * Connects the option to the respective UI.
     */
    public void showMainMenu() throws FileNotFoundException {
        displayHeader();
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine(); // consumir newline

        while (option > 0) {
            switch (option) {
                case 1 -> {
                    WagonUI wagonUI = new WagonUI();
                    wagonUI.run();
                }
                case 2 -> {
                    AllocationUI allocationUI = new AllocationUI();
                    allocationUI.run();
                }
                case 3 -> {
                    // Exemplo de outro módulo (se já existir)
                    System.out.println("Station Storage Manager ainda não implementado.");
                }
                case 4 -> {
                    // Aqui chamamos o módulo da USPL02
                    CargoHandlingUI cargoUI = new CargoHandlingUI();
                    cargoUI.showMenu();
                }
                default -> System.out.println("Opção inválida.");
            }

            displayHeader();
            option = scanner.nextInt();
            scanner.nextLine();
        }

        System.out.println("A sair do programa...");
    }

    private void displayHeader() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Show Wagons");
        System.out.println("2. Order Eligibility & Allocation");
        System.out.println("3. As a Station Storage Manager");
        System.out.println("4. Cargo Handling Operations (USPL02)");
        System.out.println("0. Exit");
        System.out.println("-----------------------");
        System.out.print("Escolha uma opção: ");
    }
}
