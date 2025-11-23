package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Cargo;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.ReturnItem;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.ReturnItemRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller that processes customer returns and quarantine operations,
 * implementing the business logic required for USEI05.
 *
 * Each return item is classified as:
 * <ul>
 *   <li>Restocked</li>
 *   <li>Discarded</li>
 *   <li>Quarantined for later inspection</li>
 * </ul>
 *
 * Every action taken is recorded in an audit log file.
 */
public class ReturnController {

    private static final String PATH = "train_station_dataset/returns.csv";
    private static final String LOG_PATH = "logs/audit_log.txt";

    ReturnItemRepository repo = Repositories.getInstance().getReturnItemRepository();
    private List<ReturnItem> returnItems = repo.getReturnItems();
    private final List<ReturnItem> restocked = new ArrayList<>();
    private final List<ReturnItem> quarantine = new ArrayList<>();
    private final List<ReturnItem> discarded = new ArrayList<>();


    /**
     * Sorts and evaluates all return items according to the defined rules.
     * Items are processed in descending timestamp order (most recent first).
     *
     * @param returns list of ReturnItem objects to process
     */
    public void processReturns(List<ReturnItem> returns) {
        restocked.clear();
        quarantine.clear();
        discarded.clear();

        Collections.sort(returns);

        for (ReturnItem item : returns) {
            evaluateReturn(item);
        }
    }

    /**
     * Determines the appropriate action based on the reason of the return.
     *
     * @param item the return item to evaluate
     */
    private void evaluateReturn(ReturnItem item) {
        String reason = item.getReason().toLowerCase();
        int qtyRestocked = 0;
        int qtyDiscarded = 0;
        String action;

        switch (reason) {
            case "damaged":
            case "expired":
                action = "Discarded";
                qtyDiscarded = item.getQuantity();
                discarded.add(item);
                break;

            case "customer-remorse":
                action = "Restocked";
                qtyRestocked = item.getQuantity();
                restockItem(item, qtyRestocked);
                restocked.add(item);
                break;

            case "cycle-count":
                action = "Quarantined";
                quarantine.add(item);
                break;

            default:
                action = "Unknown";
                qtyDiscarded = item.getQuantity();
                discarded.add(item);
        }

        logAction(item, action, qtyRestocked, qtyDiscarded);
    }

    /**
     * Restocks the returned item into inventory following FEFO/FIFO insertion.
     *
     * @param item the return item to restock
     * @param qtyRestocked quantity restocked
     */
    private void restockItem(ReturnItem item, int qtyRestocked) {
        String boxId = item.getReturnId();
        String expiry = (item.getExpiryDate() == null ? "9999-12-31" : item.getExpiryDate().toString());
        String receivedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        Cargo cargo = new Cargo(boxId, item.getSku(), qtyRestocked, expiry, receivedAt);

        WarehouseController wc = new WarehouseController();
        wc.storeCargoFEFO(cargo);
    }

    /**
     * Appends a record of the action taken to an external audit log file.
     *
     * @param item return item being logged
     * @param action action taken (Restocked, Discarded, etc.)
     * @param qtyRestocked quantity restocked
     * @param qtyDiscarded quantity discarded
     */
    private void logAction(ReturnItem item, String action, int qtyRestocked, int qtyDiscarded) {

        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            String log;
            if (action.equals("Restocked")) {
                log = String.format("%s | returnId=%s | sku=%s | action=Restocked | qty=%d%n",
                        timestamp, item.getReturnId(), item.getSku(), qtyRestocked);
            } else if (action.equals("Discarded")) {
                log = String.format("%s | returnId=%s | sku=%s | action=Discarded | qty=%d%n",
                        timestamp, item.getReturnId(), item.getSku(), qtyDiscarded);
            } else {
                log = String.format("%s | returnId=%s | sku=%s | action=%s%n",
                        timestamp, item.getReturnId(), item.getSku(), action);
            }

            writer.write(log);

        } catch (IOException e) {
        }
    }

    /**
     * Removes the last quarantined item (LIFO processing order).
     *
     * @return the most recently quarantined item, or null if quarantine is empty
     */
    public ReturnItem popQuarantineItem() {
        if (quarantine.isEmpty()) return null;
        return quarantine.remove(quarantine.size() - 1);
    }

    /**
     * Inspect a quarantined item and decide whether to restock or discard it.
     *
     * @param item item being inspected
     * @param restock true if the item should be returned to inventory, false if discarded
     * @return action string ("Restocked" or "Discarded")
     */
    public String inspectItem(ReturnItem item, boolean restock) {
        int qtyRestocked = 0;
        int qtyDiscarded = 0;
        String action;

        if (restock) {
            action = "Restocked";
            qtyRestocked = item.getQuantity();
            restockItem(item, qtyRestocked);
            restocked.add(item);
        } else {
            action = "Discarded";
            qtyDiscarded = item.getQuantity();
            discarded.add(item);
        }

        logAction(item, action, qtyRestocked, qtyDiscarded);
        return action;
    }

    /** @return list of successfully restocked items */
    public List<ReturnItem> getRestocked() { return restocked; }

    /** @return list of items currently in quarantine */
    public List<ReturnItem> getQuarantine() { return quarantine; }

    /** @return list of discarded items */
    public List<ReturnItem> getDiscarded() { return discarded; }
}
