package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReturnItem implements Comparable<ReturnItem>{

    private String returnID;
    private String sku;
    private int quantity;
    private String reason;
    private LocalDateTime timeStamp;
    private LocalDate expiryDate;


    public ReturnItem(String returnID, String sku, int quantity, String reason, LocalDateTime timeStamp, LocalDate expiryDate) {

        this.returnID = returnID;
        this.sku = sku;
        this.quantity = quantity;
        this.reason = reason;
        this.timeStamp = timeStamp;
        this.expiryDate = expiryDate;

    }

    public static ReturnItem fromCSV(Map<String, String> data){


        DateTimeFormatter dateTimeFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expiry;

        String id = data.get("returnId");
        if (id == null) {
            id = data.get("\uFEFFreturnId");
        }
        String sku = data.get("sku");
        int quantity = Integer.parseInt(data.get("qty"));
        String reason = data.get("reason");
        LocalDateTime timestamp = LocalDateTime.parse(data.get("timestamp"), dateTimeFmt);

        String expiryRaw = data.get("expiryDate");

         if (expiryRaw == null || expiryRaw.isBlank()) {
            expiry = null;
        } else {
            expiry = LocalDate.parse(expiryRaw, dateFmt);
        }

        return new ReturnItem(id, sku, quantity, reason, timestamp, expiry);
    }

    public String getReturnId() {
        return returnID;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getTimestamp() {
        return timeStamp;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    @Override
    public int compareTo(ReturnItem other) {
        int cmp = other.timeStamp.compareTo(this.timeStamp);
        return (cmp != 0) ? cmp : this.returnID.compareTo(other.returnID);
    }

    @Override
    public String toString() {
        return String.format("ReturnItem{id=%s, sku=%s, qty=%d, reason=%s, timestamp=%s, expiry=%s}",
                returnID, sku, quantity, reason, timeStamp, expiryDate);
    }
}

