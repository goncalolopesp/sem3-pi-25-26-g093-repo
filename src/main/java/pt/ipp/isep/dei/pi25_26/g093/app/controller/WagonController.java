package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WagonRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Acts as a medium between the WagonUI, the Bootstrap and the WagonRepository.
 */
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
    private List<Bay> storeAllCargoInOrder(Map<String, Map<String, List<Wagon>>> nested, WarehouseController wc) {
        List<Bay> allBays = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Wagon>>> outer : nested.entrySet()) {
            for (Map.Entry<String, List<Wagon>> inner : outer.getValue().entrySet()) {
                for (Wagon wagon : inner.getValue()) {
                    Cargo cargo = wagon.getCargo();
                    if (placeCargoInFirstAvailableBay(cargo, wc) != null) {
                        allBays.add(placeCargoInFirstAvailableBay(cargo, wc));
                    }
                }
            }
        }
        return allBays;
    }

    /**
     * Places a cargo in the first available bay.
     * @param cargo the cargo to place.
     * @param wc the WarehouseController instance.
     * @return true if the cargo was placed, false otherwise.
     */
    private Bay placeCargoInFirstAvailableBay(Cargo cargo, WarehouseController wc) {
        for (Warehouse wh : wc.getWarehouses()) {
            for (Aisle aisle : wh.getAisles()) {
                for (Bay bay : aisle.getBays()) {
                    if (bay.getCargo() == null) {
                        bay.setCargo(new ArrayList<>());
                    }
                    if (bay.getCargo().size() < bay.getCapacity()) {
                        insertCargoInBayFEFO(bay, cargo);
                        return bay;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Acts as a medium between the WagonUI and the WagonController. Will Call methods related to adding the Cargo from the Wagons to the Bays in the Aisles in the Warehouses.
     * @return true if success; false if failure.
     */
    public List<Bay> storeAllCargoInWarehouses() {
        WarehouseController warehouseController = new WarehouseController();
        List<Wagon> wagons = this.getWagons();
        List<Bay> allBays = new ArrayList<>();

        List<Cargo> sortedCargo = getSortedCargoFEFO_FIFO(wagons);

        for (Cargo cargo : sortedCargo) {
            allBays.add(placeCargoInWarehouseOrder(cargo, warehouseController));
        }

        return allBays;
    }

    /**
     * Places cargo in warehouses in order.
     * @param cargo the cargo to place.
     * @param wc the WarehouseController instance.
     * @return true if the cargo was placed, false otherwise.
     */
    private Bay placeCargoInWarehouseOrder(Cargo cargo, WarehouseController wc) {
        for (Warehouse wh : wc.getWarehouses()) {
            for (Aisle aisle : wh.getAisles()) {
                for (Bay bay : aisle.getBays()) {
                    if (bay.getCargo() == null) {
                        bay.setCargo(new ArrayList<>());
                    }
                    if (bay.getCargo().size() < bay.getCapacity()) {
                        insertCargoInBayFEFO(bay, cargo);
                        return bay;
                    }
                }
            }
        }
        return null;
    }


    /**
     * Stores cargo directly without sorting.
     * @param cargo the cargo to store.
     * @param wc the WarehouseController instance.
     * @return true if the cargo was stored, false otherwise.
     */
    public Bay storeCargoDirectly(Cargo cargo, WarehouseController wc) {
        return placeCargoInFirstAvailableBay(cargo, wc);
    }

    /**
     * Gets sorted cargo from wagons according to FEFO/FIFO rules.
     * @param wagons the list of wagons to get cargo from.
     * @return the sorted list of cargo.
     */
    private List<Cargo> getSortedCargoFEFO_FIFO(List<Wagon> wagons) {
        List<Cargo> cargos = new ArrayList<>();

        for (Wagon w : wagons) {
            Cargo c = w.getCargo();
            if (c != null) {
                cargos.add(c);
            }
        }

        cargos.sort(this::compareCargoFEFO_FIFO);
        return cargos;
    }

    /**
     * Stores sorted cargo in warehouses.
     * @param cargos the list of cargo to store.
     * @param wc the WarehouseController instance.
     * @return true if all cargo was stored, false otherwise.
     */
    private List<Bay> storeSortedCargo(List<Cargo> cargos, WarehouseController wc) {
        List<Bay> allBays = new ArrayList<>();

        for (Cargo cargo : cargos) {
            boolean placed = false;

            for (Warehouse wh : wc.getWarehouses()) {
                for (Aisle aisle : wh.getAisles()) {
                    for (Bay bay : aisle.getBays()) {
                        if (bay.getCargo() == null) bay.setCargo(new ArrayList<>());
                        if (bay.getCargo().size() < bay.getCapacity()) {
                            insertCargoInBayFEFO(bay, cargo);
                            allBays.add(bay);
                            placed = true;
                            break;
                        }
                    }
                    if (placed) break;
                }
                if (placed) break;
            }
        }

        return allBays;
    }


    /**
     * Dispatches all cargo from the warehouse.
     * @return the list of dispatched cargo
     */
    public List<Cargo> dispatchCargoFromWagons() {
        WarehouseController whc = new WarehouseController();
        List<Cargo> dispatchedCargo = new ArrayList<>();

        for (Warehouse wh : whc.getWarehouses()) {
            for (Aisle a : wh.getAisles()) {
                for (Bay b : a.getBays()) {
                    List<Cargo> cargoInBay = b.getCargo();
                    if (cargoInBay != null && !cargoInBay.isEmpty()) {
                        // copy to preserve order
                        List<Cargo> toRemove = new ArrayList<>(cargoInBay);
                        for (Cargo c : toRemove) {
                            dispatchedCargo.add(c);
                            b.removeCargo(c);   // remove box, not bay
                        }
                    }
                }
            }
        }

        return dispatchedCargo;
    }


    /**
     * Dispatches a given number of boxes from the warehouse,
     * @param quantity the number of boxes to dispatch
     * @return the list of dispatched cargo
     */
    public List<Cargo> dispatchQuantityCargoFromWagons(int quantity) {
        WarehouseController whc = new WarehouseController();
        List<Cargo> dispatchedCargo = new ArrayList<>();
        int remaining = quantity;

        for (Warehouse wh : whc.getWarehouses()) {
            for (Aisle a : wh.getAisles()) {
                for (Bay b : a.getBays()) {
                    if (remaining <= 0) break;

                    List<Cargo> cargoInBay = b.getCargo();
                    if (cargoInBay == null || cargoInBay.isEmpty()) continue;

                    List<Cargo> toRemove = new ArrayList<>();

                    for (Cargo c : cargoInBay) {
                        if (remaining <= 0) break;
                        if (c != null) {
                            dispatchedCargo.add(c);
                            toRemove.add(c);
                            remaining--;
                        }
                    }

                    for (Cargo c : toRemove) {
                        b.removeCargo(c);
                    }
                }
                if (remaining <= 0) break;
            }
            if (remaining <= 0) break;
        }

        return dispatchedCargo;
    }

    /**
     * Dispatches a given number of boxes of a specific SKU from the warehouse,
     * @param sku the SKU of the cargo to dispatch
     * @param quantity the number of boxes to dispatch
     * @return the list of dispatched cargo
     */
    public List<Cargo> dispatchQuantityCargoFromWagonsBySku(int sku, int quantity) {
        WarehouseController whc = new WarehouseController();
        List<Cargo> dispatchedCargo = new ArrayList<>();
        int remaining = quantity;

        for (Warehouse wh : whc.getWarehouses()) {
            for (Aisle a : wh.getAisles()) {
                for (Bay b : a.getBays()) {
                    if (remaining <= 0) break;

                    List<Cargo> cargoInBay = b.getCargo();
                    if (cargoInBay == null || cargoInBay.isEmpty()) continue;

                    List<Cargo> toRemove = new ArrayList<>();

                    for (Cargo c : cargoInBay) {
                        if (remaining <= 0) break;
                        if (c != null && c.getSku() == sku) {
                            dispatchedCargo.add(c);
                            toRemove.add(c);
                            remaining--;
                        }
                    }

                    for (Cargo c : toRemove) {
                        b.removeCargo(c);
                    }
                }
                if (remaining <= 0) break;
            }
            if (remaining <= 0) break;
        }

        return dispatchedCargo;
    }

    /**
     * Parses the expiry date string into a Date object.
     * @param expiry the expiry date string
     * @return the Date object, or null if expiry is null, empty, or "9999-12-31"
     */
    private Date parseExpiry(String expiry) {
        if (expiry == null) return null;
        String trimmed = expiry.trim();
        if (trimmed.isEmpty() || "9999-12-31".equals(trimmed)) {
            return null;
        }
        return new Date(trimmed);
    }

    /**
     * Compares two Cargo objects according to FEFO/FIFO rules
     * @param c1 first Cargo
     * @param c2 second Cargo
     * @return negative if c1 < c2, positive if c1 > c2, zero if equal
     */
    private int compareCargoFEFO_FIFO(Cargo c1, Cargo c2) {
        Date expiry1 = parseExpiry(c1.getExpiryDate());
        Date expiry2 = parseExpiry(c2.getExpiryDate());

        boolean hasExp1 = expiry1 != null;
        boolean hasExp2 = expiry2 != null;

        if (hasExp1 && !hasExp2) return -1;
        if (!hasExp1 && hasExp2) return 1;
        if (hasExp1 && hasExp2) {
            int cmp = expiry1.compareTo(expiry2);
            if (cmp != 0) return cmp;
        }

        DateTime r1 = new DateTime(c1.getReceivedAt());
        DateTime r2 = new DateTime(c2.getReceivedAt());
        int cmpReceived = r1.compareTo(r2);
        if (cmpReceived != 0) return cmpReceived;

        return Integer.compare(c1.getBoxID(), c2.getBoxID());
    }

    /**
     * Inserts cargo into a bay's cargo list according to FEFO/FIFO order.
     * @param bay the bay to insert the cargo into
     * @param cargo the cargo to insert
     */
    private void insertCargoInBayFEFO(Bay bay, Cargo cargo) {
        if (bay.getCargo() == null) {
            bay.setCargo(new ArrayList<>());
        }
        List<Cargo> cargoList = bay.getCargo();
        int index = 0;
        while (index < cargoList.size()
                && compareCargoFEFO_FIFO(cargo, cargoList.get(index)) >= 0) {
            index++;
        }
        cargoList.add(index, cargo);
    }

    /**
     * Relocates a box (Cargo) identified by sku and boxId to a new bay.
     * Only updates its warehouse/aisle/bay (by moving the Cargo object) and
     * inserts it into the new bay's FEFO position (expiryDate/receivedAt unchanged).
     *
     * @param sku       SKU of the box to relocate
     * @param boxId     box ID of the box to relocate
     * @param wc        WarehouseController instance
     * @param targetBay target bay where the box should be placed
     * @return true if relocation was successful; false if the box was not found
     */
    public boolean relocateBox(int sku, int boxId, WarehouseController wc, Bay targetBay) {
        Cargo cargoToMove = null;
        Bay originBay = null;

        for (Warehouse wh : wc.getWarehouses()) {
            for (Aisle aisle : wh.getAisles()) {
                for (Bay bay : aisle.getBays()) {
                    List<Cargo> cargoList = bay.getCargo();
                    if (cargoList == null || cargoList.isEmpty()) continue;

                    for (Cargo c : cargoList) {
                        if (c.getSku() == sku && c.getBoxID() == boxId) {
                            cargoToMove = c;
                            originBay = bay;
                            break;
                        }
                    }

                    if (cargoToMove != null) break;
                }
                if (cargoToMove != null) break;
            }
            if (cargoToMove != null) break;
        }

        if (cargoToMove == null) {
            return false;
        }

        originBay.removeCargo(cargoToMove);

        insertCargoInBayFEFO(targetBay, cargoToMove);

        return true;
    }


}
