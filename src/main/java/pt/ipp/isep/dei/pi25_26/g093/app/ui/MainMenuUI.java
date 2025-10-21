package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import java.util.Scanner;

public class MainMenuUI {

    /**
     * Shows the main menu and asks for an option. Also, connects the option to the respective UI.
     */
    public void showMainMenu(){
        System.out.println("Main Menu:");
        System.out.println("1. Show Wagons");
        System.out.println("0. Exit");
        System.out.println("-----------------------");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        while (option > 0){
            switch (option){
                case 1:
                    WagonUI wagonUI = new WagonUI();
                    wagonUI.run();
                    break;
            }
            System.out.println("Main Menu:");
            System.out.println("1. Show Wagons");
            System.out.println("0. Exit");
            System.out.println("-----------------------");
            scanner.nextLine();
            option = scanner.nextInt();
        }

    }
}
