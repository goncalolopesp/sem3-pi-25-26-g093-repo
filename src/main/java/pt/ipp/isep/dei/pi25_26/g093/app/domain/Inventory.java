package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items;

    /**
     * Creates a new Inventory with an empty list of Items.
     */
    public Inventory(){
        items = new ArrayList<>();
    }

    /**
     * Adds an item to the inventory
     * @param i the item to be added to the inventory.
     */
    public void add(Item i){
        items.add(i);
    }

    /**
     * Gets a representation of the Inventory as a String.
     * @return the representation of the Inventory as a String.
     */
    public String toString(){
        String result = "";
        for (Item item : items){
            result += item.toString();
            result += "-----------------------";
        }
        return result;
    }

}
