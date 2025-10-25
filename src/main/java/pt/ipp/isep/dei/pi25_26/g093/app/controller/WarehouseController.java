package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Aisle;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Bay;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Cargo;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WarehouseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller responsible for managing warehouse operations including inventory storage and
 * warehouse structure loading. This controller interacts with the WarehouseRepository to retrieve
 * and store information.
 *
 * This functionality supports cargo unloading, restocking and validation rules applied in USEI01,
 * USEI02, USEI03 and USEI05 user stories.
 */
public class WarehouseController {

    /** Singleton access to repositories */
    Repositories repositories = Repositories.getInstance();

    /**
     * Creates a new WarehouseController instance.
     */
    public WarehouseController(){ }

    /**
     * Retrieves the WarehouseRepository instance.
     *
     * @return the WarehouseRepository holding all warehouse data
     */
    public WarehouseRepository getRepo(){
        return repositories.getWarehouseRepository();
    }

    /**
     * Retrieves all warehouses currently stored in the system.
     *
     * @return list of warehouses
     */
    public List<Warehouse> getWarehouses(){
        return getRepo().getWarehouseList();
    }

    /**
     * Adds a new warehouse to the repository.
     *
     * @param w warehouse to add
     */
    public void addWarehouse(Warehouse w){
        getRepo().add(w);
    }

    /**
     * Stores cargo according to FEFO (First-Expired-First-Out) rules.
     * Cargo with the same SKU should be inserted in the same bay when possible.
     * Cargo ordering inside the bay is based first on earliest expiry date then by received date.
     *
     * If no bay contains the SKU, the cargo is stored in the first empty bay with available capacity.
     *
     * @param cargo cargo to be stored
     * @return true if successfully stored, false if no capacity available
     */
    public boolean storeCargoFEFO(Cargo cargo) {

        String targetSku = cargo.getSku();
        List<Bay> candidateBays = new ArrayList<>();

        // Search for bays already storing the SKU
        for (Warehouse wh : getWarehouses()) {
            if (wh.getAisles() != null) {
                for (Aisle aisle : wh.getAisles()) {
                    if (aisle.getBays() != null) {
                        for (Bay bay : aisle.getBays()) {
                            List<Cargo> bayCargo = bay.getCargo();

                            if (bayCargo != null && !bayCargo.isEmpty() &&
                                    bayCargo.get(0).getSku().equals(targetSku)) {
                                candidateBays.add(bay);
                            }
                        }
                    }
                }
            }
        }

        // Store in first compatible empty bay if no SKU-matching bay found
        if (candidateBays.isEmpty()) {
            for (Warehouse wh : getWarehouses()) {
                for (Aisle aisle : wh.getAisles()) {
                    for (Bay bay : aisle.getBays()) {
                        if (bay.getCargo().isEmpty() && bay.getCapacity() > 0) {
                            bay.addCargo(cargo);
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        // Insert cargo in FEFO order if SKU-matching bay found
        for (Bay bay : candidateBays) {
            if (bay.getCargo().size() < bay.getCapacity()) {

                bay.addCargo(cargo);

                bay.getCargo().sort((c1, c2) -> {
                    int cmp = c1.getExpiryDate().compareTo(c2.getExpiryDate());
                    if (cmp != 0) return cmp;

                    cmp = c1.getReceivedAt().compareTo(c2.getReceivedAt());
                    if (cmp != 0) return cmp;

                    return c1.getBoxID().compareTo(c2.getBoxID());
                });
                return true;
            }
        }
        return false;
    }

    /**
     * Loads a warehouse structure from CSV dataset containing the following fields:
     * warehouseId, aisle, bay, capacityBoxes.
     *
     * Warehouses are constructed dynamically using the input data.
     *
     * @param csvData list of CSV rows parsed into key-value mappings
     */
    public void loadWarehouses(List<Map<String, String>> csvData) {

        List<Integer> warehouseIds = new ArrayList<>();

        // Identify unique warehouse IDs
        for (Map<String, String> line : csvData) {
            int whId = Integer.parseInt(line.get("warehouseId").replaceAll("[^0-9]", ""));
            if (!warehouseIds.contains(whId)) {
                warehouseIds.add(whId);
            }
        }

        // Create aisles and bays for each warehouse
        for (int whId : warehouseIds) {
            List<Aisle> aisles = new ArrayList<>();

            for (Map<String, String> line : csvData) {
                int whLine = Integer.parseInt(line.get("warehouseId").replaceAll("[^0-9]", ""));
                if (whLine != whId) continue;

                int aisleId = Integer.parseInt(line.get("aisle"));
                int bayId = Integer.parseInt(line.get("bay"));
                int capacity = Integer.parseInt(line.get("capacityBoxes"));

                Aisle aisle = aisles.stream()
                        .filter(a -> a.getId() == aisleId)
                        .findFirst()
                        .orElseGet(() -> {
                            Aisle newAisle = new Aisle(aisleId, new ArrayList<>());
                            aisles.add(newAisle);
                            return newAisle;
                        });

                aisle.getBays().add(new Bay(bayId, capacity));
            }

            Warehouse warehouse = new Warehouse(whId, aisles);
            addWarehouse(warehouse);
        }

        System.out.println("Warehouses loaded: " + getWarehouses().size());
    }

    /**
     * Checks whether a given SKU exists in any bay in the warehouse structure.
     *
     * @param sku SKU code to search for
     * @return true if any bay contains the SKU, false otherwise
     */
    public boolean skuExists(String sku) {
        for (Warehouse wh : getWarehouses()) {
            if (wh.getAisles() != null) {
                for (Aisle aisle : wh.getAisles()) {
                    if (aisle.getBays() != null) {
                        for (Bay bay : aisle.getBays()) {
                            List<Cargo> cargoList = bay.getCargo();
                            if (cargoList != null && !cargoList.isEmpty()) {
                                if (cargoList.get(0).getSku().equals(sku)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
