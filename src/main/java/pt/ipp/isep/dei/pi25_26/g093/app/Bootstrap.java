package pt.ipp.isep.dei.pi25_26.g093.app;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.WagonController;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.WarehouseController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.io.File;

import pt.ipp.isep.dei.pi25_26.g093.app.repositories.*;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
        System.out.print("Path to the order csv file: ");
        orderCsv = scanner.nextLine();
        System.out.print("Path to the order_lines csv file: ");
        order_linesCsv = scanner.nextLine();
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
        addReturnItemsToRepository();
        addOrdersToRepository();
        addFacilities();
        addRailwayLines();
        addRailwayLineSegments();
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

    public void addFacilities() {

        FacilityRepository repo = Repositories.getInstance().getFacilityRepository();

        repo.add(new Facility(1,  "São Romão", FacilityType.STATION));
        repo.add(new Facility(2,  "Tamel", FacilityType.STATION));
        repo.add(new Facility(3,  "Senhora das Dores", FacilityType.STATION));
        repo.add(new Facility(4,  "Lousado", FacilityType.STATION));
        repo.add(new Facility(5,  "Porto Campanhã", FacilityType.STATION));
        repo.add(new Facility(6,  "Leandro", FacilityType.STATION));
        repo.add(new Facility(7,  "Porto São Bento", FacilityType.STATION));
        repo.add(new Facility(8,  "Barcelos", FacilityType.STATION));
        repo.add(new Facility(9,  "Vila Nova da Cerveira", FacilityType.STATION));
        repo.add(new Facility(10, "Midões", FacilityType.STATION));
        repo.add(new Facility(11, "Valença", FacilityType.STATION));
        repo.add(new Facility(12, "Darque", FacilityType.STATION));
        repo.add(new Facility(13, "Contumil", FacilityType.STATION));
        repo.add(new Facility(14, "Ermesinde", FacilityType.STATION));
        repo.add(new Facility(15, "São Frutuoso", FacilityType.STATION));
        repo.add(new Facility(16, "São Pedro da Torre", FacilityType.STATION));
        repo.add(new Facility(17, "Viana do Castelo", FacilityType.STATION));
        repo.add(new Facility(18, "Famalicão", FacilityType.STATION));
        repo.add(new Facility(19, "Barroselas", FacilityType.STATION));
        repo.add(new Facility(20, "Nine", FacilityType.STATION));
        repo.add(new Facility(21, "Caminha", FacilityType.STATION));
        repo.add(new Facility(22, "Carvalha", FacilityType.STATION));
        repo.add(new Facility(23, "Carreço", FacilityType.STATION));
        repo.add(new Facility(50, "Leixões", FacilityType.STATION));
        repo.add(new Facility(45, "São Mamede de Infesta", FacilityType.STATION));
        repo.add(new Facility(48, "Leça do Balio", FacilityType.STATION));
        repo.add(new Facility(43, "São Gemil", FacilityType.STATION));
    }




    public void addRailwayLines() {
        FacilityRepository facRepo = Repositories.getInstance().getFacilityRepository();
        RailwayLineRepository lineRepo = Repositories.getInstance().getRailwayLineRepository();

        lineRepo.add(new RailwayLine(1, "Ramal São Bento - Campanhã", "IP", 1668, facRepo.getById(7), facRepo.getById(5)));
        lineRepo.add(new RailwayLine(2, "Ramal Campanhã - Contumil", "IP", 1668, facRepo.getById(5), facRepo.getById(13)));
        lineRepo.add(new RailwayLine(3, "Ramal Contumil - Nine", "IP", 1668, facRepo.getById(13), facRepo.getById(20)));
        lineRepo.add(new RailwayLine(4, "Ramal Nine - Barcelos", "IP", 1668, facRepo.getById(20), facRepo.getById(8)));
        lineRepo.add(new RailwayLine(5, "Ramal Barcelos - Darque", "IP", 1668, facRepo.getById(8), facRepo.getById(12)));
        lineRepo.add(new RailwayLine(6, "Ramal Darque - Viana do Castelo", "IP", 1668, facRepo.getById(12), facRepo.getById(17)));
        lineRepo.add(new RailwayLine(7, "Ramal Viana do Castelo - Caminha", "IP", 1668, facRepo.getById(17), facRepo.getById(21)));
        lineRepo.add(new RailwayLine(8, "Ramal Caminha - São Pedro da Torre", "IP", 1668, facRepo.getById(21), facRepo.getById(16)));
        lineRepo.add(new RailwayLine(9, "Ramal São Pedro da Torre - Valença", "IP", 1668, facRepo.getById(16), facRepo.getById(11)));
        lineRepo.add(new RailwayLine(23, "São Gemil - São Mamede de Infesta", "IP", 1668, facRepo.getById(43), facRepo.getById(45)));
        lineRepo.add(new RailwayLine(24, "São Mamede de Infesta - Leça do Balio", "IP", 1668, facRepo.getById(45), facRepo.getById(48)));
        lineRepo.add(new RailwayLine(21, "Leça do Balio - Leixões", "IP", 1668, facRepo.getById(48), facRepo.getById(50)));
    }

    public void addRailwayLineSegments() {

        RailwayLineRepository lineRepo = Repositories.getInstance().getRailwayLineRepository();
        RailwayLineSegmentRepository segRepo = Repositories.getInstance().getRailwayLineSegmentRepository();

        RailwayLine l1 = lineRepo.getById(1);
        l1.addSegment(new RailwayLineSegment(1, l1, 1, true, 8000, 2618, 4, null, null, 120));

        RailwayLine l2 = lineRepo.getById(2);
        l2.addSegment(new RailwayLineSegment(3, l2, 1, true, 8000, 2443, 4, null, null, 120));

        RailwayLine l3 = lineRepo.getById(3);
        l3.addSegment(new RailwayLineSegment(10, l3, 1, true, 8000, 26560, 2, null, null, 120));
        l3.addSegment(new RailwayLineSegment(11, l3, 2, true, 8000, 10000, 2, null, null, 120));

        RailwayLine l4 = lineRepo.getById(4);
        l4.addSegment(new RailwayLineSegment(15, l4, 1, true, 8000, 5286, 2, null, null, 120));
        l4.addSegment(new RailwayLineSegment(16, l4, 2, true, 8000, 6000, 2, null, null, 120));

        RailwayLine l5 = lineRepo.getById(5);
        l5.addSegment(new RailwayLineSegment(14, l5, 1, true, 8000, 10387, 2, null, null, 120));
        l5.addSegment(new RailwayLineSegment(12, l5, 2, true, 8000, 12000, 2, null, null, 120));
        l5.addSegment(new RailwayLineSegment(13, l5, 3, true, 8000, 3100, 2, null, null, 120));

        RailwayLine l6 = lineRepo.getById(6);
        l6.addSegment(new RailwayLineSegment(20, l6, 1, true, 6400, 4890, 2, null, null, 120));

        RailwayLine l7 = lineRepo.getById(7);
        l7.addSegment(new RailwayLineSegment(18, l7, 1, true, 8000, 6000, 1, null, null, 120));
        l7.addSegment(new RailwayLineSegment(21, l7, 2, true, 8000, 5000, 1, 2000.0, 864.0, 120));
        l7.addSegment(new RailwayLineSegment(22, l7, 3, true, 8000, 12000, 2, null, null, 120));

        RailwayLine l8 = lineRepo.getById(8);
        l8.addSegment(new RailwayLineSegment(25, l8, 1, true, 8000, 20829, 1, 11000.0, 266.0, 120));

        RailwayLine l9 = lineRepo.getById(9);
        l9.addSegment(new RailwayLineSegment(26, l9, 1, true, 8000, 4264, 1, null, null, 120));

        RailwayLine l21 = lineRepo.getById(21);
        l21.addSegment(new RailwayLineSegment(30, l21, 1, true, 8000, 3883, 2, null, null, 120));

        RailwayLine l22 = lineRepo.getById(22);
        l22.addSegment(new RailwayLineSegment(31, l22, 1, true, 8400, 1174, 2, null, null, 120));
        l22.addSegment(new RailwayLineSegment(32, l22, 2, true, 8000, 2534, 2, null, null, 120));

        RailwayLine l23 = lineRepo.getById(23);
        l23.addSegment(new RailwayLineSegment(33, l23, 1, true, 8000, 1566, 2, null, null, 120));
        l23.addSegment(new RailwayLineSegment(34, l23, 2, true, 8000, 1453, 2, null, null, 120));

        RailwayLine l24 = lineRepo.getById(24);
        l24.addSegment(new RailwayLineSegment(35, l24, 1, true, 8100, 3597, 2, null, null, 120));
        l24.addSegment(new RailwayLineSegment(36, l24, 2, true, 8000, 4334, 2, null, null, 120));

        for (RailwayLine line : lineRepo.getAll()) {
            line.sortSegmentsByOrder();
        }
    }

}