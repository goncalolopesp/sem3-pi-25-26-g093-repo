package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.TrolleyPlanRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.service.PickPathService;
import pt.ipp.isep.dei.pi25_26.g093.app.service.PickingPlanner;

import java.util.List;
import java.util.Scanner;
import pt.ipp.isep.dei.pi25_26.g093.app.ui.PickingPlansUI;

public class PickPathUI {

    private PickPathService pickPathService;

    public void run() {

        pickPathService = new PickPathService();
        TrolleyPlanRepository repo = Repositories.getInstance().getTrolleyPlanRepository();
        List<TrolleyPlan> lastGeneratedPlans = repo.getPlans();

        if (lastGeneratedPlans == null || lastGeneratedPlans.isEmpty()) {
            System.out.println("No picking plans available. Run USEI03 first.");
            return;
        }

        try {
            for (TrolleyPlan plan : lastGeneratedPlans) {
                System.out.println("\n--- Sequencing for " + plan + " ---");
                String report = pickPathService.generateDetailedReport(plan);
                System.out.println(report);
            }
        } catch (Exception e) {

            System.out.println("Error executing USEI04: " + e.getMessage());

        }
    }
}
