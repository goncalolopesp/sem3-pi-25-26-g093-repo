package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WarehouseRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Controller responsible for managing warehouses and storing cargo
 * using FEFO (First Expired First Out) and FIFO rules.
 *
 * It supports operations defined in USEI01 and USEI05:
 * <ul>
 *     <li>FEFO sorting for perishable goods</li>
 *     <li>FIFO sorting based on received timestamp</li>
 *     <li>Fallback comparison by box ID</li>
 * </ul>
 */
public class WarehouseController {

    private final Repositories repositories = Repositories.getInstance();

    /**
     * Gets the warehouse repository instance.
     *
     * @return the WarehouseRepository instance
     */
    public WarehouseRepository getRepo() {
        return repositories.getWarehouseRepository();
    }

    /**
     * Retrieves all warehouses currently registered in the system.
     *
     * @return list of Warehouse objects
     */
    public List<Warehouse> getWarehouses() {
        return getRepo().getWarehouseList();
    }

    /**
     * Adds a new warehouse to the warehouse repository.
     *
     * @param warehouse the warehouse object to register
     */
    public void addWarehouse(Warehouse warehouse) {
        getRepo().add(warehouse);
    }

    /**
     * Comparator to order cargo using FEFO as the primary criteria,
     * FIFO as secondary, and box ID as final tie breaker.
     */
    // Comparator that implements: expirable items first (earliest expiry),
    // for expirable items with same expiry -> order by boxID ascending.
    // Non-expirable items are ordered after expirable ones by receivedAt (FIFO),
    // and tie-broken by boxID.
    public final Comparator<Cargo> cargoStorageComparator = (c1, c2) -> {
        boolean h1 = c1.hasExpiry();
        boolean h2 = c2.hasExpiry();

        if (h1 && h2) {
            // Both have expiry: FEFO (earlier expiry first)
            int cmp = c1.getExpiryDateObj().compareTo(c2.getExpiryDateObj());
            if (cmp != 0) return cmp;
            // If same expiry, order by boxID ascending
            return Integer.compare(c1.getBoxID(), c2.getBoxID());
        } else if (h1) {
            // c1 has expiry, c2 doesn't -> c1 comes first
            return -1;
        } else if (h2) {
            // c2 has expiry, c1 doesn't -> c2 comes first
            return 1;
        } else {
            // Neither has expiry: FIFO by receivedAt, then boxID
            int cmp = c1.getReceivedAtObj().compareTo(c2.getReceivedAtObj());
            if (cmp != 0) return cmp;
            return Integer.compare(c1.getBoxID(), c2.getBoxID());
        }
    };


    /**
     * Stores cargo in a compatible bay using FEFO/FIFO rules.
     * Only bays with the same SKU or empty bays are considered valid.
     *
     * @param cargo the cargo to be stored
     * @return true if the cargo was stored successfully, false if no space available
     */
    public boolean storeCargoFEFO(Cargo cargo) {

        List<Bay> possibleBays = new ArrayList<>();

        for (Warehouse wh : getWarehouses()) {
            for (Aisle aisle : wh.getAisles()) {
                for (Bay bay : aisle.getBays()) {

                    List<Cargo> bayCargo = bay.getCargo();

                    if (bayCargo.isEmpty()) {
                        possibleBays.add(bay);
                    } else if (bayCargo.get(0).getSku() == (cargo.getSku())) {
                        possibleBays.add(bay);
                    }
                }
            }
        }

        if (possibleBays.isEmpty()) return false;

        for (Bay bay : possibleBays) {
            if (bay.getCargo().size() < bay.getCapacity()) {
                bay.addCargo(cargo);
                // Keep bay's internal cargo ordered by the same storage comparator
                bay.getCargo().sort(cargoStorageComparator);
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves a warehouse by its unique identifier.
     *
     * @param warehouseId the ID of the warehouse to retrieve
     * @return the Warehouse object if found, null otherwise
     */
    public Warehouse getWarehouseById(int warehouseId) {
        for (Warehouse warehouse : getWarehouses()) {
            if (warehouse.getId() == warehouseId) {
                return warehouse;
            }
        }
        return null;
    }

    /**
     * Retrieves an aisle by its unique identifier within a given warehouse.
     * @param warehouse the warehouse containing the aisle
     * @param aisleId the ID of the aisle to retrieve
     * @return the Aisle object if found, null otherwise
     */
    public Aisle getAisleById(Warehouse warehouse, int aisleId) {
        for (Aisle aisle : warehouse.getAisles()) {
            if (aisle.getId() == aisleId) {
                return aisle;
            }
        }
        return null;
    }

    /**
     * Retrieves a bay by its unique identifier within a given aisle.
     * @param aisle the aisle containing the bay
     * @param bayId the ID of the bay to retrieve
     * @return the Bay object if found, null otherwise
     */
    public Bay getBayById(Aisle aisle, int bayId) {
        for (Bay bay : aisle.getBays()) {
            if (bay.getId() == bayId) {
                return bay;
            }
        }
        return null;
    }
}
