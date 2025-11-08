package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.ReturnItem;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;


import java.util.ArrayList;
import java.util.List;
public class ReturnItemRepository {

    private List<ReturnItem> ReturnItems;


    public ReturnItemRepository() {

        ReturnItems = new ArrayList<>();
    }

    public List<ReturnItem> getReturnItems() {

        return ReturnItems;
    }

    public void addReturnItems(List<ReturnItem> items) {
        ReturnItems.addAll(items);
    }
}
