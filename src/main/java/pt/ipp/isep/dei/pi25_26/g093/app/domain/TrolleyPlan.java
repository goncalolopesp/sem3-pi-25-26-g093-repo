package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a picking trolley used to carry items in the warehouse.
 * <p>
 * Each trolley has a fixed weight capacity and can store multiple {@link TrolleyItem} objects
 * until the maximum load is reached.
 */
public class TrolleyPlan {

    private final int trolleyNo;       // Trolley identifier number
    private final double capacityKg;   // Maximum capacity in kilograms
    private double usedWeight = 0.0;   // Current total weight
    private final List<TrolleyItem> items = new ArrayList<>(); // Items inside this trolley

    /**
     * Constructs a new trolley plan with a given number and weight capacity.
     *
     * @param trolleyNo the trolley identifier
     * @param capacityKg the maximum weight capacity in kilograms
     */
    public TrolleyPlan(int trolleyNo, double capacityKg) {
        this.trolleyNo = trolleyNo;
        this.capacityKg = capacityKg;
    }

    /**
     * Attempts to add an item to the trolley.
     * If the item's weight does not exceed the remaining capacity, it is added successfully.
     *
     * @param item the {@link TrolleyItem} to add
     * @return {@code true} if the item was added successfully, {@code false} otherwise
     */
    public boolean tryAdd(TrolleyItem item) {
        if (usedWeight + item.getWeight() <= capacityKg) {
            items.add(item);
            usedWeight += item.getWeight();
            return true;
        }
        return false;
    }

    /**
     * Calculates the percentage of the trolley’s capacity currently in use.
     *
     * @return the utilization percentage (0–100)
     */
    public double getUtilization() {
        if (capacityKg == 0) return 0;
        return (usedWeight / capacityKg) * 100.0;
    }

    public int getTrolleyNo() { return trolleyNo; }
    public double getCapacityKg() { return capacityKg; }
    public double getUsedWeight() { return usedWeight; }
    public List<TrolleyItem> getItems() { return items; }

    /**
     * Returns a human-readable summary of the trolley’s load status,
     * including total weight and capacity utilization.
     */
    @Override
    public String toString() {
        return String.format("Trolley #%d [%.1f / %.1f kg = %.1f%% full]",
                trolleyNo, usedWeight, capacityKg, getUtilization());
    }
}

