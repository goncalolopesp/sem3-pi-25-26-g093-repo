package pt.ipp.isep.dei.pi25_26.g093.app;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.WagonController;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.WarehouseController;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.AVL;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.io.File;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Date;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.*;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser1;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Stores the data from the CSV files into the repositories for later use.
 */
public class Bootstrap {

    Repositories repo;
    WagonRepository wagonRepo;
    WarehouseRepository warehouseRepo;
    ReturnItemRepository returnItemRepo;
    String baysCsv;
    String itemsCsv;
    String order_linesCsv;
    String orderCsv;
    String returnsCsv;
    String wagonsCsv;
    String europeStationsCsv;

    String path = "train_station_dataset";

    private static Bootstrap instance;
    /**
     * Creates a new Bootstrap instance and gets the repositories.
     */
    public Bootstrap() {
        repo = Repositories.getInstance();
        wagonRepo = repo.getWagonRepository();
        warehouseRepo = repo.getWarehouseRepository();
        returnItemRepo = repo.getReturnItemRepository();
    }

    public static Bootstrap getInstance() {
        if (instance == null) {
            synchronized (Bootstrap.class) {
                instance = new Bootstrap();
            }
        }
        return instance;
    }

    /**
     * Returns the path to the bays csv file.
     */
    public String getBaysCsv() {return path + "/" + baysCsv;}
    /**
     * Returns the path to the items csv file.
     */
    public String getItemsCsv() {return path + "/" + itemsCsv;}
    /**
     * Returns the path to the order lines csv file.
     */
    public String getOrder_linesCsv() {return path + "/" + order_linesCsv;}
    /**
     * Returns the path to the orders csv file.
     */
    public String getOrderCsv() {return path + "/" + orderCsv;}
    /**
     * Returns the path to the returns csv file.
     */
    public String getReturnsCsv() {return path + "/" + returnsCsv;}
    /**
     * Returns the path to the wagons csv file.
     */
    public String getWagonsCsv() {return path + "/" + wagonsCsv;}
    /**
     * Returns the path to the Europe train stations csv file.
     */
    public String getEuropeStationsCsv() { return path + "/" + europeStationsCsv; }

    /**
     * Prompts the user to enter the path to the CSV files.
     */
    public void completeCSVFiles(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Path to the bays csv file: ");
        baysCsv = scanner.nextLine();
        System.out.print("Path to the items csv file: ");
        itemsCsv = scanner.nextLine();
        System.out.print("Path to the order csv file: ");
        orderCsv = scanner.nextLine();
        System.out.print("Path to the order_lines csv file: ");
        order_linesCsv = scanner.nextLine();
        System.out.print("Path to the returns csv file: ");
        returnsCsv = scanner.nextLine();
        System.out.print("Path to the wagons csv file: ");
        wagonsCsv = scanner.nextLine();
        System.out.print("Path to the europe stations csv file: ");
        europeStationsCsv = scanner.nextLine();
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
        addReturnItemsToRepository();
        addOrdersToRepository();
        addCompleteStations();
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

    public void addReturnItemsToRepository() throws FileNotFoundException {

        try {

            CSVParser csvParser = new CSVParser(path + "/returns.csv");
            List<Map<String, String>> csvData = csvParser.getCSVData();
            List<ReturnItem> returnItems = new ArrayList<>();
            DateTimeFormatter dateTimeFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for(Map<String, String> data : csvData) {

                String id = data.get("returnId");
                if (id == null) id = data.get("\uFEFFreturnId");

                String sku = data.get("sku");
                int quantity = Integer.parseInt(data.get("qty"));
                String reason = data.get("reason");
                LocalDateTime timestamp = LocalDateTime.parse(data.get("timestamp"), dateTimeFmt);

                LocalDate expiryDate = null;
                String expiryRaw = data.get("expiryDate");
                if (expiryRaw != null && !expiryRaw.isBlank()) {
                    expiryDate = LocalDate.parse(expiryRaw, dateFmt);
                }
                ReturnItem returnItem = new ReturnItem(id, sku, quantity, reason, timestamp, expiryDate);

                returnItems.add(returnItem);

            }
            returnItemRepo.addReturnItems(returnItems);
        }catch (FileNotFoundException e){

            System.out.println("File returns.csv not found");
            throw e;
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
     * Adds the orders from the .csv file to the order repository.
     * @throws FileNotFoundException if there is no file with the name "orders.csv" in the directory "train_station_dataset"
     */
    public void addOrdersToRepository() throws FileNotFoundException{
        try{
            CSVParser csvOrderParser = new CSVParser(path + "/" + orderCsv);
            CSVParser csvOrderLineParser = new CSVParser(path + "/" + order_linesCsv);
            List<Map<String, String>> csvOrderData = csvOrderParser.getCSVData();
            List<Map<String, String>> csvOrderLineData = csvOrderLineParser.getCSVData();
            OrderRepository orderRepository = Repositories.getInstance().getOrderRepository();

            for(Map<String, String> orderData : csvOrderData)
            {
                String orderId = orderData.get("orderId");
                String dueDate = orderData.get("dueDate");
                int priority = Integer.parseInt(orderData.get("priority"));

                Order order = new Order(orderId, dueDate, priority);

                for(Map<String, String> orderLineData : csvOrderLineData)
                {
                    String lineOrderId = orderLineData.get("orderId");
                    if(lineOrderId.equalsIgnoreCase(orderId))
                    {
                        int lineNo = Integer.parseInt(orderLineData.get("lineNo"));
                        String sku = orderLineData.get("sku");
                        int qty = Integer.parseInt(orderLineData.get("qty"));

                        OrderLine line = new OrderLine(lineNo, sku, qty);
                        order.addNewLine(line);
                    }
                }

                orderRepository.add(order);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
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

    public void addCompleteStations() throws FileNotFoundException {

        CompleteStationRepository repo = Repositories.getInstance().getCompleteStationRepository();

        CSVParser1 parser = new CSVParser1(path + "/" + europeStationsCsv);
        List<Map<String, String>> rows = parser.getCSVData();

        int total = rows.size();
        int validCount = 0;
        int invalidCount = 0;
        int processed = 0;
        int lastPercent = -1;

        for (Map<String, String> r : rows) {
            processed++;

            try {
                String country = safeTrim(r.get("country"));
                String tzg = safeTrim(r.get("time_zone_group"));
                String name = safeTrim(r.get("station"));

                String rawTZ = safeTrim(r.get("time_zone"));
                String tz = normalizeTimeZone(rawTZ);

                Double lat = parseNullableDouble(r.get("latitude"));
                Double lon = parseNullableDouble(r.get("longitude"));

                boolean isCity = Boolean.parseBoolean(safeTrim(r.get("is_city")));
                boolean isMain = Boolean.parseBoolean(safeTrim(r.get("is_main_station")));
                boolean isAirport = Boolean.parseBoolean(safeTrim(r.get("is_airport")));

                CompleteStation cs = new CompleteStation(
                        country, tz, tzg, name,
                        lat, lon,
                        isCity, isMain, isAirport
                );

                if (country.isEmpty() || tzg.isEmpty() || name.isEmpty()) {
                    invalidCount++;
                }else{
                    if (lat == null || (lat < -90 || lat > 90))
                        invalidCount++;
                    else{
                        if (lon == null || (lon < -180 || lon > 180))
                            invalidCount++;
                        else{
                            repo.addStation(cs);
                            addToTrees(cs);
                            validCount++;
                        }
                    }
                }

            } catch (Exception e) {
                invalidCount++;
            }

        }

        System.out.println("\nLoaded stations: " + validCount + " (skipped " + invalidCount + ")");
    }

    public void addToTrees(CompleteStation cs) {
        CompleteStationRepository repo = Repositories.getInstance().getCompleteStationRepository();
        if (cs.getLatitude() != null)
            repo.addLatKey(new LatKey(cs));

        if (cs.getLongitude() != null)
            repo.addLonKey(new LonKey(cs));

        repo.addTzgKey(new TzgKey(cs));
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    private Double parseNullableDouble(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty() || s.equalsIgnoreCase("none") || s.equalsIgnoreCase("null")) {
            return null;
        }
        return Double.parseDouble(s);
    }

    // Converts "('Europe/Paris',)" -> "Europe/Paris"
    private String normalizeTimeZone(String rawTZ) {
        if (rawTZ == null) return "";
        return rawTZ
                .replaceAll("[()'\\[\\]{}]", "")
                .replace(",", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

}