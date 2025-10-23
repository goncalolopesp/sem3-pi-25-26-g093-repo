package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainMenuUI {
    /**
     * Shows the main menu and asks for an option. Also, connects the option to the respective UI.
     */
    public void showMainMenu() throws FileNotFoundException {
        DisplayHeader();
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        while (option > 0){
            switch (option){
                case 1:
                    WagonUI wagonUI = new WagonUI();
                    wagonUI.run();
                    break;
                case 2:
                    AllocationUI allocationUI = new AllocationUI();
                    allocationUI.run();
                    break;
            }
            DisplayHeader();
            scanner.nextLine();
            option = scanner.nextInt();
        }
    }

    private void DisplayHeader()
    {
        System.out.println("Main Menu:");
        System.out.println("1. Show Wagons");
        System.out.println("2. Order Eligibility & Allocation");
        System.out.println("0. Exit");
        System.out.println("-----------------------");
    }
}
