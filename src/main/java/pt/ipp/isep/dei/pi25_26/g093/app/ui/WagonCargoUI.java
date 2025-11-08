package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Wagon;

import java.util.Scanner;

public class WagonCargoUI {

    public void showMenu(){
        Scanner sc = new Scanner(System.in);
        int option;

        do{
            System.out.println("\n=== CARGO STORING MENU===");
            System.out.println("1. Add Cargo From Wagons to Warehouses");
            System.out.println("2. Show Cargo in a specific Bay");
            System.out.println("0. Exit");
            System.out.print("Choose an Option: ");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> addCargoToWarehouses();
                case 2 -> showCargoFromBay();
                case 0 -> System.out.println("Returning to the main menu...");
                default -> System.out.println("Invalid Option (Does not exist).");
            }
        } while (option != 0);
    }

    private void addCargoToWarehouses(){
        System.out.println("\n=== Add Cargo to Warehouses ===");
        try {
            WagonUI wagonUI = new WagonUI();
            wagonUI.run();
        } catch (Exception e) {
            System.out.println("Error executing WagonUI: " + e.getMessage());
        }
    }

    private void showCargoFromBay(){
        System.out.println("\n=== Show Cargo from a Bay ===");
        try{
            WagonUI wagonUI = new WagonUI();
            wagonUI.displayCargoInBay();
        } catch (Exception e){
            System.out.println("Error executing WagonUI: " + e.getMessage());
        }
    }

}
