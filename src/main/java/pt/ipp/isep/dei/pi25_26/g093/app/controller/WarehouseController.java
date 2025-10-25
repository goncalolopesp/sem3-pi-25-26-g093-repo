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

public class WarehouseController {
    Repositories repositories = Repositories.getInstance();

    /**
     * Constructor of the WarehouseController class
     */
    public WarehouseController(){
    }

    /**
     * Gets the WarehouseRepository
     * @return WarehouseRepository
     */
    public WarehouseRepository getRepo(){
        return repositories.getWarehouseRepository();
    }

    /**
     * Gets all the stored warehouses
     * @return Stored Warehouses in the WarehouseRepository
     */
    public List<Warehouse> getWarehouses(){
        return getRepo().getWarehouseList();
    }

    /**
     * Adds a new warehouse to the WarehouseRepository
     * @param w Warehouse to be added to the WarehouseRepository
     */
    public void addWarehouse(Warehouse w){
        getRepo().add(w);
    }

    public boolean storeCargoFEFO(Cargo cargo) {

        String targetSku = cargo.getSku();
        List<Bay> candidateBays = new ArrayList<>();

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

    public void loadWarehouses(List<Map<String, String>> csvData) {

        List<Integer> warehouseIds = new ArrayList<>();

        for (Map<String, String> line : csvData) {
            int whId = Integer.parseInt(line.get("warehouseId").replaceAll("[^0-9]", ""));
            if (!warehouseIds.contains(whId)) {
                warehouseIds.add(whId);
            }
        }

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

        System.out.println("Warehouses carregados: " + getWarehouses().size());
    }


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
