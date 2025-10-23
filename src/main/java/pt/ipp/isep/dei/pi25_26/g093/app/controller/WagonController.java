package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WagonRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WagonController {
    Repositories repositories = Repositories.getInstance();

    /**
     * Gets the WagonRepository
     * @return WagonRepository
     */
    public WagonRepository getRepo(){
        return repositories.getWagonRepository();
    }

    /**
     * Gets all the stored wagons
     * @return Stored Wagons in the WagonRepository
     */
    public List<Wagon> getWagons(){
        return getRepo().getWagons();
    }

    /**
     * Adds a new wagon to the WagonRepository
     * @param w Wagon to be added to the WagonRepository
     */
    public void addWagon(Wagon w){
        getRepo().add(w);
    }

    /**
     * Adds a new wagon to the WagonRepository using the parameters provided.
     * @param wagonID the ID of the Wagon
     * @param boxID the ID of the box
     * @param sku the ID of the item
     * @param quantity The quantity of items in a box
     * @param expiryDate The Expiry Date of the items
     * @param receivedAt The Date the items were received
     */
    public void addWagon(int wagonID, int boxID, int sku, int quantity, String expiryDate, String receivedAt){
        Wagon newWagon = new Wagon(wagonID, boxID, sku, quantity, expiryDate, receivedAt);
        this.addWagon(newWagon);
    }

    /**
     * Groups the wagons by their expiry date.
     * @param wagons the list of wagons to group.
     * @return a map with the key being the expiry date and the value being a list of wagons with the same expiry date.
     */
    private Map<String, List<Wagon>> groupWagonsByExpiry(List<Wagon> wagons) {
        Map<String, List<Wagon>> grouped = new HashMap<>();
        for (Wagon w : wagons) {
            String expiryStr = new Date(w.getCargo().getExpiryDate()).toString();
            grouped.computeIfAbsent(expiryStr, k -> new ArrayList<>()).add(w);
        }
        return grouped;
    }

    /**
     * Sorts the wagons by their received date.
     * @param grouped the map with the key being the expiry date and the value being a list of wagons with the same expiry date.
     */
    private void sortWagonsByReceivedDate(Map<String, List<Wagon>> grouped) {
        for (List<Wagon> list : grouped.values()) {
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = 0; j < list.size() - i - 1; j++) {
                    DateTime r1 = new DateTime(list.get(j).getCargo().getReceivedAt());
                    DateTime r2 = new DateTime(list.get(j + 1).getCargo().getReceivedAt());
                    int cmp = r1.compareTo(r2);
                    if (cmp > 0 || (cmp == 0 &&
                            list.get(j).getCargo().getBoxID() > list.get(j + 1).getCargo().getBoxID())) {
                        Wagon temp = list.get(j);
                        list.set(j, list.get(j + 1));
                        list.set(j + 1, temp);
                    }
                }
            }
        }
    }

    /**
     * Nests the wagons by their received date.
     * @param grouped the map with the key being the expiry date and the value being a list of wagons with the same expiry date.
     * @return a map with the key being the received date and the value being a map with the key being the expiry date and the value being a list of wagons with the same expiry date and received date.
     */
    private Map<String, Map<String, List<Wagon>>> nestWagonsByReceivedDate(Map<String, List<Wagon>> grouped) {
        Map<String, Map<String, List<Wagon>>> nested = new HashMap<>();
        for (Map.Entry<String, List<Wagon>> e : grouped.entrySet()) {
            String expiryStr = e.getKey();
            Map<String, List<Wagon>> byReceived = new HashMap<>();
            for (Wagon w : e.getValue()) {
                String receivedStr = new Date(w.getCargo().getReceivedAt()).toString();
                byReceived.computeIfAbsent(receivedStr, k -> new ArrayList<>()).add(w);
            }
            nested.put(expiryStr, byReceived);
        }
        return nested;
    }

    /**
     * Sorts the nested wagons by their expiry date and received date.
     * @param nested the map with the key being the received date and the value being a map with the key being the expiry date and the value being a list of wagons with the same expiry date and received date.
     */
    private void sortNestedWagons(Map<String, Map<String, List<Wagon>>> nested) {
        List<String> sortedExpiryKeys = sortDateKeys(new ArrayList<>(nested.keySet()));
        for (String expiryStr : sortedExpiryKeys) {
            Map<String, List<Wagon>> receivedMap = nested.get(expiryStr);
            List<String> sortedReceivedKeys = sortDateKeys(new ArrayList<>(receivedMap.keySet()));
            for (String receivedStr : sortedReceivedKeys) {
                List<Wagon> wagonList = receivedMap.get(receivedStr);
                sortWagonsByBoxID(wagonList);
            }
        }
    }

    /**
     * Sorts the keys of a list of strings by their date.
     * @param keys the list of strings to sort
     * @return the sorted list of strings.
     */
    private List<String> sortDateKeys(List<String> keys) {
        for (int i = 0; i < keys.size() - 1; i++) {
            for (int j = 0; j < keys.size() - i - 1; j++) {
                Date d1 = new Date(keys.get(j));
                Date d2 = new Date(keys.get(j + 1));
                if (d1.compareTo(d2) > 0) {
                    String temp = keys.get(j);
                    keys.set(j, keys.get(j + 1));
                    keys.set(j + 1, temp);
                }
            }
        }
        return keys;
    }

    /**
     * Sorts the wagons by their boxID.
     * @param wagons the list of wagons to sort.
     */
    private void sortWagonsByBoxID(List<Wagon> wagons) {
        for (int i = 0; i < wagons.size() - 1; i++) {
            for (int j = 0; j < wagons.size() - i - 1; j++) {
                int id1 = wagons.get(j).getCargo().getBoxID();
                int id2 = wagons.get(j + 1).getCargo().getBoxID();
                if (id1 > id2) {
                    Wagon temp = wagons.get(j);
                    wagons.set(j, wagons.get(j + 1));
                    wagons.set(j + 1, temp);
                }
            }
        }
    }

    /**
     * Stores all the cargo in order.
     * @param nested the map with the key being the received date and the value being a map with the key being the expiry date and the value being a list of wagons with the same expiry date and received date.
     * @param wc the WarehouseController instance.
     * @return true if all cargo was stored, false otherwise.
     */
    private boolean storeAllCargoInOrder(Map<String, Map<String, List<Wagon>>> nested, WarehouseController wc) {
        boolean allStored = true;
        for (Map.Entry<String, Map<String, List<Wagon>>> outer : nested.entrySet()) {
            for (Map.Entry<String, List<Wagon>> inner : outer.getValue().entrySet()) {
                for (Wagon wagon : inner.getValue()) {
                    Cargo cargo = wagon.getCargo();
                    boolean placed = placeCargoInFirstAvailableBay(cargo, wc);
                    if (!placed) {
                        allStored = false;
                        System.out.println("Unable to store Cargo boxID=" + cargo.getBoxID() + " sku=" + cargo.getSku() + " — no free bay space available.");
                    }
                }
            }
        }
        return allStored;
    }

    /**
     * Places a cargo in the first available bay.
     * @param cargo the cargo to place.
     * @param wc the WarehouseController instance.
     * @return true if the cargo was placed, false otherwise.
     */
    private boolean placeCargoInFirstAvailableBay(Cargo cargo, WarehouseController wc) {
        for (Warehouse wh : wc.getWarehouses()) {
            List<Aisle> aisles = wh.getAisles();
            if (aisles == null) continue;
            for (Aisle aisle : aisles) {
                List<Bay> bays = aisle.getBays();
                if (bays == null) continue;
                for (Bay bay : bays) {
                    int used = bay.getCargo() == null ? 0 : bay.getCargo().size();
                    if (used < bay.getCapacity()) {
                        if (bay.getCargo() == null) {
                            bay.setCargo(new java.util.ArrayList<>());
                        }
                        bay.addCargo(cargo);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Acts as a medium between the WagonUI and the WagonController. Will Call methods related to adding the Cargo from the Wagons to the Bays in the Aisles in the Warehouses.
     * @return true if success; false if failure.
     */
    public boolean storeAllCargoInWarehouses(){
        WagonController wg = new WagonController();
        WarehouseController wc = new WarehouseController();
        Map<String, List<Wagon>> wagonsByExpiryStr = groupWagonsByExpiry(wg.getWagons());
        sortWagonsByReceivedDate(wagonsByExpiryStr);
        Map<String, Map<String, List<Wagon>>> nested = nestWagonsByReceivedDate(wagonsByExpiryStr);
        sortNestedWagons(nested);
        return storeAllCargoInOrder(nested, wc);
    }
}
