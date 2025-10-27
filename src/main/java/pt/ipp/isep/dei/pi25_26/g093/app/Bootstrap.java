package pt.ipp.isep.dei.pi25_26.g093.app;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.WagonController;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.WarehouseController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.io.File;

import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WagonRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WarehouseRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Bootstrap {

    Repositories repo;
    WagonRepository wagonRepo;
    WarehouseRepository warehouseRepo;
    String baysCsv;
    String itemsCsv;
    String order_linesCsv;
    String orderCsv;
    String returnsCsv;
    String wagonsCsv;

    String path = "train_station_dataset";

    private static Bootstrap instance;
    /**
     * Creates a new Bootstrap instance and gets the repositories.
     */
    public Bootstrap() {
        repo = Repositories.getInstance();
        wagonRepo = repo.getWagonRepository();
        warehouseRepo = repo.getWarehouseRepository();
    }

    public static Bootstrap getInstance() {
        if (instance == null) {
            synchronized (Bootstrap.class) {
                instance = new Bootstrap();
            }
        }
        return instance;
    }

    public String getBaysCsv() {return path + "/" + baysCsv;}
    public String getItemsCsv() {return path + "/" + itemsCsv;}
    public String getOrder_linesCsv() {return path + "/" + order_linesCsv;}
    public String getOrderCsv() {return path + "/" + orderCsv;}
    public String getReturnsCsv() {return path + "/" + returnsCsv;}
    public String getWagonsCsv() {return path + "/" + wagonsCsv;}

    /**
     * Prompts the user to enter the path to the CSV files.
     */
    public void completeCSVFiles(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Path to the bays csv file: ");
        baysCsv = scanner.nextLine();
        System.out.print("Path to the items csv file: ");
        itemsCsv = scanner.nextLine();
        System.out.print("Path to the order_lines csv file: ");
        order_linesCsv = scanner.nextLine();
        System.out.print("Path to the order csv file: ");
        orderCsv = scanner.nextLine();
        System.out.print("Path to the returns csv file: ");
        returnsCsv = scanner.nextLine();
        System.out.print("Path to the wagons csv file: ");
        wagonsCsv = scanner.nextLine();
    }

    /**
     * Runs the bootstrap process.
     * @throws FileNotFoundException if there are no files in the directory "train_station_dataset"
     */
    public void run() throws FileNotFoundException {
        printAllCSVFilesOfDirectory(path);
        completeCSVFiles();
        addWarehousesToRepository();
        addWagonsToRepository();
    }

    /**
     * Gets all .csv content from the directory "dir" and prints it to the console.
     * @param dir folder that contains all csv files
     */
    public void printAllCSVFilesOfDirectory(String dir){
        File fileDir = new File(dir);
        File[] files = fileDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (file.getName().endsWith(".csv")) {
                        System.out.println(file.getName());
                    }
                }
            }
        }
    }

    /**
     * Adds the warehouses from the .csv file to the warehouse repository.
     * @throws FileNotFoundException if there is no file with the name "bays.csv" in the directory "train_station_dataset"
     */
    public void addWarehousesToRepository() throws FileNotFoundException {
        try {
            CSVParser csvParser = new CSVParser(path + "/" + baysCsv);
            List<Map<String, String>> csvData = csvParser.getCSVData();
            WarehouseController warehouseController = new WarehouseController();

            List<Integer> warehouseIds = new ArrayList<>();
            for (Map<String, String> csvLine : csvData) {
                String wareHouseIdStr1 = csvLine.get("warehouseId");
                String wareHouseIdStr = wareHouseIdStr1.substring(1);
                int warehouseId = Integer.parseInt(wareHouseIdStr);
                if (!warehouseIds.contains(warehouseId)) {
                    warehouseIds.add(warehouseId);
                }
            }
            for (Integer warehouseId : warehouseIds) {
                List<Aisle> aisles = new ArrayList<>();
                for (int i = 1; i <= getMaxAisleId(); i++) {
                    Aisle newAisle = new Aisle(i, getBaysByAisle(i));
                    aisles.add(newAisle);
                }
                Warehouse warehouse = new Warehouse(warehouseId, aisles);
                warehouseController.addWarehouse(warehouse);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading warehouse data: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Gets the data from a csvFile and returns it as a List of Items identified by their header
     * @param csvDirectory the path to the csv file
     * @return List of map with header identifying the value
     * @throws FileNotFoundException if there is no file with the name passed as "csvDirectory"
     */
    public List<Map<String, String>> getCSVData(String csvDirectory) throws FileNotFoundException {
        CSVParser csvParser = new CSVParser(csvDirectory);
        List<Map<String, String>> csvData = csvParser.getCSVData();
        return csvData;
    }

    /**
     * Gets all the bays in an Aisle
     * @param aisleId the id of the Aisle to get the bays from.
     * @return List of Bays in the Aisle with the id "aisleId"
     * @throws FileNotFoundException if there is no file with the name "bays.csv" in the directory "train_station_dataset"
     */
    public List<Bay> getBaysByAisle(int aisleId) throws FileNotFoundException {
        List<Map<String, String>> csvData = this.getCSVData(path + "/" + baysCsv);
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

    /**
     * Gets the max id from the aisles
     * @return the max id from the aisles
     * @throws FileNotFoundException if there is no file with the name "bays.csv" in the directory "train_station_dataset"
     */
    public int getMaxAisleId() throws FileNotFoundException {

        List<Map<String, String>> csvData = this.getCSVData(path + "/" + baysCsv);

        int max = -1;

        for (Map<String, String> csvLine : csvData) {
            int aisleId = Integer.parseInt(csvLine.get("aisle"));
            if (aisleId > max){
                max = aisleId;
            }
        }
        return max;
    }

    /**
     * Adds Cargo to the WagonRepository item-by-item.
     * @throws FileNotFoundException if there is no file with the name "wagons.csv" in the directory "train_station_dataset"
     */
    public void addWagonsToRepository1() throws FileNotFoundException {
        try {
            CSVParser csvParser = new CSVParser(path + "/" + wagonsCsv);
            List<Map<String, String>> csvData = csvParser.getCSVData();
            WagonController wagonController = new WagonController();
            List<Wagon> wagons = new ArrayList<>();

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

            for (int i = 0; i < wagons.size() - 1; i++) {
                for (int j = 0; j < wagons.size() - i - 1; j++) {
                    String d1 = wagons.get(j).getCargo().getExpiryDate();
                    String d2 = wagons.get(j + 1).getCargo().getExpiryDate();

                    Date date1 = new Date(d1);
                    Date date2 = new Date(d2);

                    if (date1.compareTo(date2) > 0) {
                        Wagon temp = wagons.get(j);
                        wagons.set(j, wagons.get(j + 1));
                        wagons.set(j + 1, temp);
                    }
                }
            }

            for (Wagon wagon : wagons) {
                wagonController.addWagon(wagon);
            }


        } catch (Exception e) {
            System.err.println("Error reading wagon data: " + e.getMessage());
            throw new FileNotFoundException("Could not process wagon data file");
        }
    }

    /**
     * Adds Cargo to the WagonRepository box-by-box.
     * @throws FileNotFoundException if there is no file with the name "wagons.csv" in the directory "train_station_dataset"
     */

    public void addWagonsToRepository() throws FileNotFoundException {
        try {
            CSVParser csvParser = new CSVParser(path + "/" + wagonsCsv);
            List<Map<String, String>> csvData = csvParser.getCSVData();
            WagonController wagonController = new WagonController();
            List<Wagon> wagons = new ArrayList<>();

            for (Map<String, String> csvLine : csvData) {
                int qty = Integer.parseInt(csvLine.get("qty").trim());
                int wagonId = Integer.parseInt(csvLine.get("wagonId").substring(3).trim());
                int boxId = Integer.parseInt(csvLine.get("boxId").substring(3).trim());
                int sku = Integer.parseInt(csvLine.get("sku").substring(3).trim());
                String expiryDate = csvLine.get("expiryDate").trim();
                String receivedAt = csvLine.get("receivedAt").trim();

                wagons.add(new Wagon(wagonId, boxId, sku, qty, expiryDate, receivedAt));
            }

            for (int i = 0; i < wagons.size() - 1; i++) {
                for (int j = 0; j < wagons.size() - i - 1; j++) {
                    String s1 = wagons.get(j).getCargo().getExpiryDate();
                    String s2 = wagons.get(j + 1).getCargo().getExpiryDate();

                    Date d1 = new Date(s1);
                    Date d2 = new Date(s2);

                    if (d1.compareTo(d2) > 0) {
                        Wagon temp = wagons.get(j);
                        wagons.set(j, wagons.get(j + 1));
                        wagons.set(j + 1, temp);
                    }
                }
            }

            for (Wagon wagon : wagons) {
                wagonController.addWagon(wagon);
            }

        } catch (Exception e) {
            System.err.println("Error reading wagon data: " + e.getMessage());
            throw new FileNotFoundException("Could not process wagon data file");
        }
    }

}