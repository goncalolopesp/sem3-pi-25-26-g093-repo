package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.WarehouseController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Cargo;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.ReturnItem;
import pt.ipp.isep.dei.pi25_26.g093.app.ui.WagonUI;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReturnService {

    private static final String LOG_PATH = "logs/audit_log.txt";
    private List<ReturnItem> restocked = new ArrayList<>();
    private List<ReturnItem> quarantine = new ArrayList<>();
    private List<ReturnItem> discarded = new ArrayList<>();

    public void processReturns(List<ReturnItem> returns) {

        restocked.clear();

        quarantine.clear();

        discarded.clear();

        Collections.sort(returns);

        for (ReturnItem item : returns) {

            processItem(item);
        }
    }

    private void processItem(ReturnItem item) {
        String action;
        switch (item.getReason().toLowerCase()) {
            case "damaged":
            case "expired":
                action = "Discarded";
                discarded.add(item);
                break;
            case "customer-remorse":
                action = "Restocked";
                restocked.add(item);

                int boxId = Integer.parseInt(item.getReturnId().replaceAll("\\D+", ""));

                int sku = Integer.parseInt(item.getSku().replaceAll("\\D+", ""));

                String expiryStr;
                if (item.getExpiryDate() == null) {
                    expiryStr = "9999-12-31";
                } else {
                    expiryStr = item.getExpiryDate().toString();
                }
                String receivedAtStr = item.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

                Cargo cargo = new Cargo(boxId, sku, item.getQuantity(), expiryStr, receivedAtStr);

                break;
            case "cycle-count":
                action = "Quarantined";
                quarantine.add(item);
                break;
            default:
                action = "Unknown";
        }

        writeAuditLog(item, action);
    }

    private void writeAuditLog(ReturnItem item, String action) {
        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            writer.write(String.format(
                    "%s | returnId=%s | sku=%s | action=%s | qty=%d%n",
                    timestamp,
                    item.getReturnId(),
                    item.getSku(),
                    action,
                    item.getQuantity()
            ));
        } catch (IOException e) {
            System.err.println("ERROR writing in log: " + e.getMessage());
        }
    }

    public List<ReturnItem> getRestocked() { return restocked; }
    public List<ReturnItem> getQuarantine() { return quarantine; }
    public List<ReturnItem> getDiscarded() { return discarded; }
}
