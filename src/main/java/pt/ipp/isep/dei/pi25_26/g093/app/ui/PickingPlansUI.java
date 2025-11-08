package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.ReturnController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyItem;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.ReturnItemRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.service.PickingPlanner;

import java.util.List;
import java.util.Scanner;

public class PickingPlansUI {

    Scanner sc = new Scanner(System.in);

    private static List<TrolleyPlan> lastGeneratedPlans;

    public PickingPlansUI() {
    }

    public void run() {
        System.out.print("Trolley capacity (KG): ");
        double capacity = sc.nextDouble();
        sc.nextLine();
        System.out.print("Mode (FF, FFD, BFD): ");
        String mode = sc.nextLine();

        try {
            PickingPlanner planner = new PickingPlanner();
            System.out.println("=== Picking Plan Summary ===");
            lastGeneratedPlans = planner.generatePickingPlan(capacity, mode);
            if (lastGeneratedPlans.isEmpty()) {
                System.out.println("No picking plans found");
                return;
            }
            for (TrolleyPlan trolley : lastGeneratedPlans) {
                System.out.println(trolley);
                for (TrolleyItem item : trolley.getItems()) {
                    System.out.println("   -> " + item);
                }
            }
            Repositories.getInstance().getTrolleyPlanRepository().setPlans(lastGeneratedPlans);
            System.out.println("Picking plans generated and saved for sequencing (USEI04).");
        } catch (Exception e) {
            System.out.println("Error generating picking plans: " + e.getMessage());
        }
    }

}
