package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.EligibilityAllocationController;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.EligibilityAllocationController.Allocation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyItem;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service class responsible for generating picking plans (USEI03).
 * <p>
 * This class retrieves order allocations from USEI02 and distributes them
 * across trolleys based on a chosen heuristic algorithm:
 * <ul>
 *     <li>FF - First Fit</li>
 *     <li>FFD - First Fit Decreasing</li>
 *     <li>BFD - Best Fit Decreasing</li>
 * </ul>
 * Each trolley plan generated respects the specified maximum weight capacity.
 */
public class PickingPlanner {

    /**
     * Generates a picking plan according to the specified heuristic.
     *
     * @param trolleyCapacity maximum capacity (in kilograms) of each trolley
     * @param mode             heuristic algorithm ("FF", "FFD", or "BFD")
     * @return a list of {@link TrolleyPlan} objects representing each loaded trolley
     * @throws FileNotFoundException if required CSV files cannot be found
     */
    public List<TrolleyPlan> generatePickingPlan(double trolleyCapacity, String mode) throws FileNotFoundException {

        // Initialize the allocation controller (USEI02)
        EligibilityAllocationController controller = new EligibilityAllocationController();

        // Temporarily mute USEI02 console output
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
            @Override
            public void write(int b) {}
        }));

        // Execute the allocation logic (USEI02)
        controller.Allocate("partial");

        // Restore normal output
        System.setOut(originalOut);

        // Retrieve allocations from controller
        List<Allocation> allocations = controller.getAllocations();

        // Convert allocations into trolley items
        List<TrolleyItem> items = new ArrayList<>();
        for (Allocation alloc : allocations) {

            // Ignore unallocated (UNDISPATCHABLE) entries
            if (alloc.qty <= 0) continue;

            double unitWeight = 2.0; // Temporary placeholder value
            double totalWeight = alloc.qty * unitWeight;

            TrolleyItem item = new TrolleyItem(
                    alloc.orderId,
                    alloc.sku,
                    alloc.qty,
                    totalWeight,
                    alloc.aisle,
                    alloc.bay
            );

            items.add(item);
        }

        // Sort items if using decreasing heuristics
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

            // Create a new trolley if none could fit the item
            if (!placed) {
                TrolleyPlan newTrolley = new TrolleyPlan(trolleys.size() + 1, trolleyCapacity);
                newTrolley.tryAdd(item);
                trolleys.add(newTrolley);
            }
        }

        // Remove empty trolleys (just in case)
        trolleys.removeIf(t -> t.getUsedWeight() <= 0);

        return trolleys;
    }
}

