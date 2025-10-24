package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.service.PickingPlanner;
import pt.ipp.isep.dei.pi25_26.g093.app.service.PickPathService;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;

import java.util.List;
import java.util.Scanner;

/**
 * USPL02 — Cargo Handling Operations Interface
 * Allows a Station Storage Manager to access all USEI01–USEI05 functionalities
 * through a text-based interface.
 */
public class CargoHandlingUI {

    // Guarda os planos de picking gerados na USEI03 para serem usados na USEI04
    private List<TrolleyPlan> lastGeneratedPlans;

    /**
     * Shows the menu for Cargo Handling operations.
     */
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n=== CARGO HANDLING MENU ===");
            System.out.println("1. USEI01 - Unloading Wagons");
            System.out.println("2. USEI02 - Order Allocation");
            System.out.println("3. USEI03 - Picking Plans");
            System.out.println("4. USEI04 - Pick Path Sequencing");
            System.out.println("5. USEI05 - Returns & Quarantine");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            option = sc.nextInt();
            sc.nextLine(); // limpa newline

            switch (option) {
                case 1 -> executeUSEI01();
                case 2 -> executeUSEI02();
                case 3 -> executeUSEI03(sc);
                case 4 -> executeUSEI04();
                case 5 -> executeUSEI05();
                case 0 -> System.out.println("A regressar ao menu principal...");
                default -> System.out.println("Opção inválida.");
            }

        } while (option != 0);
    }

    /**
     * USEI01 - Unloading Wagons
     */
    private void executeUSEI01() {
        System.out.println("\n=== USEI01 - Unloading Wagons ===");
        try {
            WagonUI wagonUI = new WagonUI();
            wagonUI.run();
        } catch (Exception e) {
            System.out.println("❌ Erro ao executar USEI01: " + e.getMessage());
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
            System.out.println("❌ Erro ao executar USEI02: " + e.getMessage());
        }
    }

    /**
     * USEI03 - Picking Plans
     */
    private void executeUSEI03(Scanner sc) {
        System.out.println("\n=== USEI03 - Picking Plans ===");
        System.out.print("Capacidade do trolley (kg): ");
        double capacity = sc.nextDouble();
        sc.nextLine();
        System.out.print("Modo (FF, FFD, BFD): ");
        String mode = sc.nextLine();

        try {
            PickingPlanner planner = new PickingPlanner();
            lastGeneratedPlans = planner.generatePickingPlan(capacity, mode);
            planner.displayPlan(lastGeneratedPlans);
            System.out.println("✔ Planos de picking gerados e guardados para o sequenciamento (USEI04).");
        } catch (Exception e) {
            System.out.println("❌ Erro ao gerar planos de picking: " + e.getMessage());
        }
    }

    /**
     * USEI04 - Pick Path Sequencing
     */
    private void executeUSEI04() {
        System.out.println("\n=== USEI04 - Pick Path Sequencing ===");

        if (lastGeneratedPlans == null || lastGeneratedPlans.isEmpty()) {
            System.out.println("⚠️  Nenhum plano de picking disponível. Execute primeiro a USEI03.");
            return;
        }

        try {
            PickPathService pickPathService = new PickPathService();
            for (TrolleyPlan plan : lastGeneratedPlans) {
                System.out.println("\n--- Sequenciamento para " + plan + " ---");
                String report = pickPathService.generateDetailedReport(plan);
                System.out.println(report);
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao gerar sequenciamento: " + e.getMessage());
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
            System.out.println("❌ Erro ao executar USEI05: " + e.getMessage());
        }
    }
}
