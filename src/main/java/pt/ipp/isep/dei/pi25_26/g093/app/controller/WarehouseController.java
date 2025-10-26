package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Aisle;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Bay;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Cargo;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;
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

    public WarehouseController() {
    }

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
    private final Comparator<Cargo> fefoComparator = (c1, c2) -> {
        LocalDate e1 = (c1.getExpiryDate() == null) ? LocalDate.MAX : LocalDate.parse(c1.getExpiryDate());
        LocalDate e2 = (c2.getExpiryDate() == null) ? LocalDate.MAX : LocalDate.parse(c2.getExpiryDate());

        int cmp = e1.compareTo(e2);
        if (cmp != 0) return cmp;

        cmp = c1.getReceivedAt().compareTo(c2.getReceivedAt());
        if (cmp != 0) return cmp;

        return Integer.compare(c1.getBoxID(), c2.getBoxID());
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
                bay.getCargo().sort(fefoComparator);
                return true;
            }
        }

        return false;
    }
}
