package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Allocation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyItem;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.EligibilityAllocationController;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service class responsible for generating picking plans (USEI03).
 * <p>
 * This class receives order allocations from USEI02 and distributes them
 * into trolleys according to a chosen heuristic algorithm:
 * <ul>
 *     <li>FF - First Fit</li>
 *     <li>FFD - First Fit Decreasing</li>
 *     <li>BFD - Best Fit Decreasing</li>
 * </ul>
 * Each generated trolley plan respects the defined weight capacity.
 */
public class PickingPlanner {

    /**
     * Generates a picking plan using the selected heuristic mode.
     *
     * @param trolleyCapacity the maximum capacity (in kilograms) of each trolley
     * @param mode the algorithm to apply: "FF", "FFD" or "BFD"
     * @return a list of {@link TrolleyPlan} objects representing each loaded trolley
     * @throws FileNotFoundException if any required CSV file cannot be found
     */
    public List<TrolleyPlan> generatePickingPlan(double trolleyCapacity, String mode) throws FileNotFoundException {
        // Create controller and execute order allocation (USEI02)
        EligibilityAllocationController controller = new EligibilityAllocationController();
        controller.Allocate("partial");

        // Retrieve generated allocations
        List<Allocation> allocations = controller.getAllocations();

        // Convert allocations into TrolleyItems
        List<TrolleyItem> items = new ArrayList<>();
        for (Allocation alloc : allocations) {
            double unitWeight = 2.0; // Temporary example value until real data is available
            double totalWeight = alloc.getQty() * unitWeight;

            TrolleyItem item = new TrolleyItem(
                    alloc.getOrderId(),
                    alloc.getSku(),
                    alloc.getQty(),
                    totalWeight,
                    alloc.getAisle(),
                    alloc.getBay()
            );

            items.add(item);
        }

        // Apply heuristic sorting if necessary
        List<TrolleyItem> itemsCopy = new ArrayList<>(items);
        if (mode.equalsIgnoreCase("FFD") || mode.equalsIgnoreCase("BFD")) {
            itemsCopy.sort(Comparator.comparingDouble(TrolleyItem::getWeight).reversed());
        }

        List<TrolleyPlan> trolleys = new ArrayList<>();

        // Distribute items into trolleys
        for (TrolleyItem item : itemsCopy) {
            boolean placed = false;

            if (mode.equalsIgnoreCase("FF") || mode.equalsIgnoreCase("FFD")) {
                for (TrolleyPlan trolley : trolleys) {
                    if (trolley.tryAdd(item)) {
                        placed = true;
                        break;
                    }
                }
            } else if (mode.equalsIgnoreCase("BFD")) {
                TrolleyPlan bestFit = null;
                double minRemaining = Double.MAX_VALUE;

                for (TrolleyPlan trolley : trolleys) {
                    double remaining = trolley.getCapacityKg() - trolley.getUsedWeight();
                    if (item.getWeight() <= remaining && remaining - item.getWeight() < minRemaining) {
                        bestFit = trolley;
                        minRemaining = remaining - item.getWeight();
                    }
                }

                if (bestFit != null) {
                    bestFit.tryAdd(item);
                    placed = true;
                }
            }

            // Create a new trolley if the item did not fit anywhere
            if (!placed) {
                TrolleyPlan newTrolley = new TrolleyPlan(trolleys.size() + 1, trolleyCapacity);
                newTrolley.tryAdd(item);
                trolleys.add(newTrolley);
            }
        }

        return trolleys;
    }

    /**
     * Displays a summary of the generated picking plans in the console.
     *
     * @param trolleys the list of trolley plans to display
     */
    public void displayPlan(List<TrolleyPlan> trolleys) {
        System.out.println("=== Picking Plan Summary ===");
        for (TrolleyPlan trolley : trolleys) {
            System.out.println(trolley);
            for (TrolleyItem item : trolley.getItems()) {
                System.out.println("   -> " + item);
            }
        }
    }
}




