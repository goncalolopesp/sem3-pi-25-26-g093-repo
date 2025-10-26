package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a trolley used for transporting allocated items.
 * Each trolley has a defined weight capacity and stores a list of {@link TrolleyItem} objects.
 */
public class TrolleyPlan {

    private final int trolleyNo;
    private final double capacityKg;
    private double usedWeight = 0.0;
    private final List<TrolleyItem> items = new ArrayList<>();

    /**
     * Creates a new trolley with a specific number and capacity.
     *
     * @param trolleyNo unique trolley identifier
     * @param capacityKg maximum weight capacity in kilograms
     */
    public TrolleyPlan(int trolleyNo, double capacityKg) {
        this.trolleyNo = trolleyNo;
        this.capacityKg = capacityKg;
    }

    /**
     * Tries to add an item to the trolley without exceeding its capacity.
     *
     * @param item the item to be added
     * @return true if the item was added successfully, false if not
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
     * Returns the percentage of the trolley's capacity currently used.
     *
     * @return utilization percentage
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
     * Returns a formatted string describing the trolley’s current load and utilization.
     *
     * @return textual description of the trolley
     */
    @Override
    public String toString() {
        return String.format("Trolley #%d [%.1f / %.1f kg = %.1f%% full]",
                trolleyNo, usedWeight, capacityKg, getUtilization());
    }
}
