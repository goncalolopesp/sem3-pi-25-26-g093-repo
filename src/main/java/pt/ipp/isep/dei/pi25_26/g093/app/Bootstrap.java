package pt.ipp.isep.dei.pi25_26.g093.app;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.WagonController;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.WarehouseController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Aisle;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Bay;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Wagon;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WagonRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WarehouseRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Bootstrap {

    Repositories repo;
    WagonRepository wagonRepo;
    WarehouseRepository warehouseRepo;
    // Read information from the csv files found in train_station_dataset
    // and add it to the respective Repositories.

    public Bootstrap() {
        repo = Repositories.getInstance();
        wagonRepo = repo.getWagonRepository();
        warehouseRepo = repo.getWarehouseRepository();
    }

    public void run() throws FileNotFoundException {
        addWarehousesToRepository();
        addWagonsToRepository();
    }

    public void addWarehousesToRepository() throws FileNotFoundException {
        try {
            CSVParser csvParser = new CSVParser("train_station_dataset/bays.csv");
            List<Map<String, String>> csvData = csvParser.getCSVData();
            WarehouseController warehouseController = new WarehouseController();

            // Get unique warehouse IDs
            List<Integer> warehouseIds = new ArrayList<>();
            for (Map<String, String> csvLine : csvData) {
                String wareHouseIdStr1 = csvLine.get("warehouseId");
                String wareHouseIdStr = wareHouseIdStr1.substring(1);
                int warehouseId = Integer.parseInt(wareHouseIdStr);
                if (!warehouseIds.contains(warehouseId)) {
                    warehouseIds.add(warehouseId);
                }
            }

            // Create warehouses with aisles
            for (Integer warehouseId : warehouseIds) {
                List<Aisle> aisles = new ArrayList<>();
                for (int i = 1; i <= getMaxAisleId(); i++) {
                    Aisle newAisle = new Aisle(i, getBaysByAisle(i));
                    aisles.add(newAisle);
                }
                Warehouse warehouse = new Warehouse(warehouseId, aisles);
                warehouseController.addWarehouse(warehouse);
                warehouseRepo.add(warehouse);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading warehouse data: " + e.getMessage());
            throw e;
        }
    }

    public List<Map<String, String>> getCSVData(String csvDirectory) throws FileNotFoundException {
        CSVParser csvParser = new CSVParser(csvDirectory);
        List<Map<String, String>> csvData = csvParser.getCSVData();
        return csvData;
    }

    public List<Bay> getBaysByAisle(int aisleId) throws FileNotFoundException {
        List<Map<String, String>> csvData = this.getCSVData("train_station_dataset/bays.csv");
        List<Bay> bays = new java.util.ArrayList<>();
        for (Map<String, String> csvLine : csvData) {
            int aisleId1 = Integer.parseInt(csvLine.get("aisle"));
            if (aisleId1 == aisleId){
                String bayIdStr = csvLine.get("bay");
                int bayId = Integer.parseInt(bayIdStr);
                String capacityBoxesStr = csvLine.get("capacityBoxes");
                int capacityBoxes = Integer.parseInt(capacityBoxesStr);
                bays.add(new Bay(bayId, capacityBoxes));
            }
        }
        return bays;
    }

    public int getMaxAisleId() throws FileNotFoundException {

        List<Map<String, String>> csvData = this.getCSVData("train_station_dataset/bays.csv");

        int max = -1;

        for (Map<String, String> csvLine : csvData) {
            int aisleId = Integer.parseInt(csvLine.get("aisle"));
            if (aisleId > max){
                max = aisleId;
            }
        }
        return max;
    }

    public void addWagonsToRepository() throws FileNotFoundException {
        try {
            CSVParser csvParser = new CSVParser("train_station_dataset/wagons.csv");
            List<Map<String, String>> csvData = csvParser.getCSVData();
            WagonController wagonController = new WagonController();
            List<Wagon> wagons = new ArrayList<>();

            // Ler todas as linhas e criar Wagons
            for (Map<String, String> csvLine : csvData) {
                int qty = Integer.parseInt(csvLine.get("qty").trim());
                int wagonId = Integer.parseInt(csvLine.get("wagonId").substring(3).trim());
                int boxId = Integer.parseInt(csvLine.get("boxId").substring(3).trim());
                int sku = Integer.parseInt(csvLine.get("sku").substring(3).trim());
                String expiryDate = csvLine.get("expiryDate").trim();
                String receivedAt = csvLine.get("receivedAt").trim();

                for (int i = 0; i < qty; i++) {
                    wagons.add(new Wagon(wagonId, boxId, sku, 1, expiryDate, receivedAt));
                }
            }

            // Ordenar manualmente por data (ascendente)
            wagons.sort((w1, w2) -> {
                String d1 = w1.getCargo().getExpiryDate();
                String d2 = w2.getCargo().getExpiryDate();
                return compareDates(d1, d2);
            });

            // Adicionar já ordenados
            for (Wagon wagon : wagons) {
                wagonController.addWagon(wagon);
            }


        } catch (Exception e) {
            System.err.println("Error reading wagon data: " + e.getMessage());
            throw new FileNotFoundException("Could not process wagon data file");
        }
    }

    /**
     * Compara duas strings de data (ex: "5-1-2026", "31-12-9999") manualmente.
     * Retorna:
     *   -1 → d1 é mais antiga
     *    0 → iguais
     *    1 → d1 é mais recente
     * Datas inválidas ou vazias vão para o fim da ordenação.
     */
    private int compareDates(String d1, String d2) {
        // Tratar nulos ou vazios: vão para o fim
        if (d1 == null || d1.isBlank()) return 1;
        if (d2 == null || d2.isBlank()) return -1;

        // Se forem exatamente iguais
        if (d1.equals(d2)) return 0;

        // Tratar datas com formato "31-12-9999" como "máximas" (fim da lista)
        if (d1.equals("31-12-9999") && !d2.equals("31-12-9999")) return 1;
        if (!d1.equals("31-12-9999") && d2.equals("31-12-9999")) return -1;

        try {
            String[] p1 = d1.split("-");
            String[] p2 = d2.split("-");

            if (p1.length != 3 || p2.length != 3) return 0;

            int day1 = Integer.parseInt(p1[0].trim());
            int month1 = Integer.parseInt(p1[1].trim());
            int year1 = Integer.parseInt(p1[2].trim());

            int day2 = Integer.parseInt(p2[0].trim());
            int month2 = Integer.parseInt(p2[1].trim());
            int year2 = Integer.parseInt(p2[2].trim());

            // Comparar ano → mês → dia
            if (year1 != year2) return Integer.compare(year1, year2);
            if (month1 != month2) return Integer.compare(month1, month2);
            return Integer.compare(day1, day2);

        } catch (NumberFormatException e) {
            System.err.println("Invalid date format while comparing: " + d1 + " vs " + d2);
            return 0;
        }
    }




}