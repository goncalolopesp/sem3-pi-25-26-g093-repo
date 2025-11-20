package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory class that holds a collection of Item objects.
 */
public class Inventory {
    private List<Item> items;

    /**
     * Creates an empty Inventory.
     */
    public Inventory() {
        items = new ArrayList<>();
    }

    /**
     * Adds a valid Item to the Inventory.
     * @param i the Item to be added.
     */
    public void add(Item i) {
        if (i != null && i.isValid()) {
            items.add(i);
        }
    }

    /**
     * Provides a string representation of the Inventory.
     * @return a string listing all items in the inventory.
     */
    @Override
    public String toString() {
        String result = "";
        for (Item item : items) {
            result += item.toString();
            result += "-----------------------";
        }
        return result;
    }
}
