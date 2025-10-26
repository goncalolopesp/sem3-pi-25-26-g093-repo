package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;
import pt.ipp.isep.dei.pi25_26.g093.app.service.PickingPlanner;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * User interface for USEI03 - Picking Planner.
 * <p>
 * Allows the user to generate picking plans using the selected algorithm
 * (First Fit, First Fit Decreasing, or Best Fit Decreasing) and trolley capacity.
 */

public class PickingPlannerUI {

    /**
     * Runs the Picking Planner process.
     * <p>
     * Asks the user for the trolley capacity and algorithm mode,
     * generates the picking plans, and displays them.
     */

    public void run() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("\n=== USEI03 - Picking Planner ===");

            System.out.print("Enter trolley capacity (kg): ");
            double capacity = sc.nextDouble();
            sc.nextLine();

            System.out.print("Select algorithm (FF, FFD, BFD): ");
            String mode = sc.nextLine().trim();

            PickingPlanner planner = new PickingPlanner();
            List<TrolleyPlan> plans = planner.generatePickingPlan(capacity, mode);

            System.out.println("\n--- Picking Plan generated successfully ---");
            planner.displayPlan(plans);

        } catch (FileNotFoundException e) {
            System.out.println("Error: CSV file not found - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error while running Picking Planner: " + e.getMessage());
        }
    }
}

