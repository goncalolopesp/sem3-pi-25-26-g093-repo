package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public void add(Item i) {
        if (i != null && i.isValid()) {
            items.add(i);
        }
    }

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
