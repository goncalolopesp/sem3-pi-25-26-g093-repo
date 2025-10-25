package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Cargo;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.ReturnItem;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReturnController {

    private static final String LOG_PATH = "logs/audit_log.txt";

    private final WagonController wagonController;
    private final WarehouseController warehouseController;

    private List<ReturnItem> restocked = new ArrayList<>();
    private PriorityQueue<ReturnItem> quarantine;
    private List<ReturnItem> discarded = new ArrayList<>();

    public ReturnController(WagonController wagonController, WarehouseController warehouseController) {
        this.wagonController = wagonController;
        this.warehouseController = warehouseController;
        this.quarantine = new PriorityQueue<>();
    }

    public List<ReturnItem> loadReturnItems() throws FileNotFoundException {
        String path = "train_station_dataset/returns.csv";
        List<ReturnItem> returnItems = new ArrayList<>();

        CSVParser parser = new CSVParser(path);
        List<Map<String, String>> data = parser.getCSVData();

        for (Map<String, String> row : data) {
            try {
                ReturnItem itemCSV = ReturnItem.fromCSV(row);
                returnItems.add(itemCSV);
            } catch (Exception e) {
                System.err.println("Invalid CSV record ignored: " + row);
            }
        }
        return returnItems;
    }

    public void processReturns(List<ReturnItem> returns) {
        restocked.clear();
        quarantine.clear();
        discarded.clear();

        for (ReturnItem item : returns) {
            classifyReturn(item);
        }
    }

    private void classifyReturn(ReturnItem item) {
        String reason = normalizeReason(item.getReason());

        // Expired or damaged items go directly to quarantine? No! Directly to discard!
        if (reason.equals("expired") || reason.equals("damaged")) {
            discarded.add(item);
            writeAuditLog(item, "Discarded", 0, item.getQuantity());
            return;
        }

        // Customer remorse and cycle count go into quarantine
        if (reason.equals("customer-remorse") || reason.equals("cycle-count")) {
            quarantine.add(item);
        } else {
            // Unknown reasons -> discard
            discarded.add(item);
            writeAuditLog(item, "Discarded", 0, item.getQuantity());
        }
    }

    public void inspectQuarantine() {
        while (!quarantine.isEmpty()) {
            ReturnItem item = quarantine.poll();
            restockOrDiscard(item);
        }
    }

    private void restockOrDiscard(ReturnItem item) {
        int qty = item.getQuantity();

        // If SKU does not exist -> discard all
        if (!warehouseController.skuExists(item.getSku())) {
            discarded.add(item);
            writeAuditLog(item, "Discarded", 0, qty);
            return;
        }

        boolean expired = isExpired(item);

        int qtyRestock = expired ? 0 : qty;
        int qtyDiscard = expired ? qty : 0;

        boolean stored = false;

        if (qtyRestock > 0) {
            Cargo cargo = new Cargo(
                    "RET-" + item.getReturnId(),
                    item.getSku(),
                    qtyRestock,
                    item.getExpiryDate() == null ? null : item.getExpiryDate().toString(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
            );

            stored = warehouseController.storeCargoFEFO(cargo);
        }

        if (stored) {
            restocked.add(item);
        } else {
            qtyDiscard = qty;
            qtyRestock = 0;
            discarded.add(item);
        }

        writeAuditLog(item,
                stored ? "Restocked" : "Discarded",
                qtyRestock,
                qtyDiscard
        );
    }

    private String normalizeReason(String r) {
        if (r == null) return "";
        return r.toLowerCase().trim().replace(" ", "-").replace("_", "-");
    }

    private boolean isExpired(ReturnItem item) {
        if (item.getExpiryDate() == null) return false;
        return item.getExpiryDate().isBefore(LocalDate.now());
    }

    private void writeAuditLog(ReturnItem item, String action, int qtyRestocked, int qtyDiscarded) {
        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            if (action.equals("Restocked")) {
                writer.write(String.format(
                        "%s | returnId=%s | sku=%s | action=Restocked | qtyRestocked=%d | qtyDiscarded=%d%n",
                        ts, item.getReturnId(), item.getSku(), qtyRestocked, qtyDiscarded
                ));
            } else {
                writer.write(String.format(
                        "%s | returnId=%s | sku=%s | action=Discarded | qtyDiscarded=%d%n",
                        ts, item.getReturnId(), item.getSku(), qtyDiscarded
                ));
            }

        } catch (IOException e) {
            System.err.println("ERROR writing in log: " + e.getMessage());
        }
    }

    public WarehouseController getWarehouseController() {return warehouseController;}

    public List<ReturnItem> getRestocked() { return restocked; }
    public PriorityQueue<ReturnItem> getQuarantine() { return quarantine; }
    public List<ReturnItem> getDiscarded() { return discarded; }
}
