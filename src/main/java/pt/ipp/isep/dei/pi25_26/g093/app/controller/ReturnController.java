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

/**
 * Controller responsible for managing return items in the terminal.
 * Handles the quarantine process, classification, inspection,
 * restocking, and discard operations, including audit logging.
 *
 * This functionality corresponds to USEI05 - Returns & Quarantine.
 */
public class ReturnController {

    /** File path for the audit logs. */
    private static final String LOG_PATH = "logs/audit_log.txt";

    private final WagonController wagonController;
    private final WarehouseController warehouseController;

    private List<ReturnItem> restocked = new ArrayList<>();
    private PriorityQueue<ReturnItem> quarantine;
    private List<ReturnItem> discarded = new ArrayList<>();

    /**
     * Creates a new ReturnController with dependencies on warehouse
     * and wagon controllers.
     *
     * @param wagonController controller responsible for wagon operations
     * @param warehouseController controller responsible for warehouse operations
     */
    public ReturnController(WagonController wagonController, WarehouseController warehouseController) {
        this.wagonController = wagonController;
        this.warehouseController = warehouseController;
        this.quarantine = new PriorityQueue<>();
    }

    /**
     * Loads return items from a predefined CSV file.
     * Invalid records are ignored with warning output.
     *
     * @return list of valid ReturnItem objects
     * @throws FileNotFoundException if the CSV file cannot be found
     */
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

    /**
     * Processes a list of return items by classifying them into restockable,
     * quarantined, or discarded groups based on return reason.
     *
     * @param returns list of returns to process
     */
    public void processReturns(List<ReturnItem> returns) {
        restocked.clear();
        quarantine.clear();
        discarded.clear();

        for (ReturnItem item : returns) {
            classifyReturn(item);
        }
    }

    /**
     * Classifies an item into discarded or quarantined based on its return reason.
     * Expired and damaged items are discarded immediately.
     * Customer-remorse and cycle-count are quarantined.
     *
     * @param item return item to classify
     */
    private void classifyReturn(ReturnItem item) {
        String reason = normalizeReason(item.getReason());

        if (reason.equals("expired") || reason.equals("damaged")) {
            discarded.add(item);
            writeAuditLog(item, "Discarded", 0, item.getQuantity());
            return;
        }

        if (reason.equals("customer-remorse") || reason.equals("cycle-count")) {
            quarantine.add(item);
        } else {
            discarded.add(item);
            writeAuditLog(item, "Discarded", 0, item.getQuantity());
        }
    }

    /**
     * Inspects all items in quarantine, evaluating expiration and SKU existence,
     * and deciding whether to restock or discard each item.
     */
    public void inspectQuarantine() {
        while (!quarantine.isEmpty()) {
            ReturnItem item = quarantine.poll();
            restockOrDiscard(item);
        }
    }

    /**
     * Decides whether an item should be restocked or discarded, and performs
     * warehouse insertion using FEFO rules if possible.
     *
     * @param item item to inspect
     */
    private void restockOrDiscard(ReturnItem item) {
        int qty = item.getQuantity();

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
            qtyRestock = 0;
            qtyDiscard = qty;
            discarded.add(item);
        }

        writeAuditLog(item,
                stored ? "Restocked" : "Discarded",
                qtyRestock,
                qtyDiscard
        );
    }

    /**
     * Normalizes return reasons for comparison.
     *
     * @param r reason input
     * @return normalized string representing the reason
     */
    private String normalizeReason(String r) {
        if (r == null) return "";
        return r.toLowerCase().trim().replace(" ", "-").replace("_", "-");
    }

    /**
     * Verifies if the returned product has expired.
     *
     * @param item return item to check
     * @return true if expired, false otherwise
     */
    private boolean isExpired(ReturnItem item) {
        if (item.getExpiryDate() == null) return false;
        return item.getExpiryDate().isBefore(LocalDate.now());
    }

    /**
     * Writes the result of the inspection to an audit log file.
     * Includes return ID, SKU, quantities processed, and timestamp.
     *
     * @param item processed return item
     * @param action type of outcome: Restocked or Discarded
     * @param qtyRestocked quantity successfully reinserted into stock
     * @param qtyDiscarded quantity discarded due to expired or invalid conditions
     */
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

    /** @return warehouse controller used by this component */
    public WarehouseController getWarehouseController() {return warehouseController;}

    /** @return list of items successfully restocked */
    public List<ReturnItem> getRestocked() { return restocked; }

    /** @return priority queue of quarantined items pending inspection */
    public PriorityQueue<ReturnItem> getQuarantine() { return quarantine; }

    /** @return list of items discarded during processing */
    public List<ReturnItem> getDiscarded() { return discarded; }
}
