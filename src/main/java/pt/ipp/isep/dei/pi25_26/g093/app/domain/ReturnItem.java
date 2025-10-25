package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Represents a single returned item that must be processed within the warehouse system.
 * A return item contains product identification, quantity, cause of return,
 * timestamp indicating when it was returned, and optionally its expiry date.
 *
 * Objects of this class are sortable so that quarantine can be processed
 * in reverse chronological order (latest returns first).
 *
 * This class is used by the ReturnController in the context of USEI05 - Returns & Quarantine.
 */
public class ReturnItem implements Comparable<ReturnItem> {

    private String returnID;
    private String sku;
    private int quantity;
    private String reason;
    private LocalDateTime timeStamp;
    private LocalDate expiryDate;

    /**
     * Creates a new return item instance.
     *
     * @param returnID unique identifier of the returned product
     * @param sku product SKU code
     * @param quantity quantity returned
     * @param reason reason for return (e.g., expired, damaged, customer remorse)
     * @param timeStamp timestamp when the item was registered as returned
     * @param expiryDate expiry date (if applicable), may be null
     */
    public ReturnItem(String returnID, String sku, int quantity, String reason,
                      LocalDateTime timeStamp, LocalDate expiryDate) {
        this.returnID = returnID;
        this.sku = sku;
        this.quantity = quantity;
        this.reason = reason;
        this.timeStamp = timeStamp;
        this.expiryDate = expiryDate;
    }

    /**
     * Factory method that constructs a ReturnItem from a CSV data row.
     * Handles timestamp and optional expiry date parsing.
     *
     * @param data map containing columns: returnId, sku, qty, reason, timestamp, expiryDate
     * @return the constructed ReturnItem instance
     * @throws RuntimeException if mandatory fields are invalid
     */
    public static ReturnItem fromCSV(Map<String, String> data) {

        DateTimeFormatter dateTimeFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String id = data.get("returnId");
        if (id == null) {
            id = data.get("\uFEFFreturnId"); // Handles BOM (Byte Order Mark) cases
        }

        String sku = data.get("sku");
        int quantity = Integer.parseInt(data.get("qty"));
        String reason = data.get("reason");
        LocalDateTime timestamp = LocalDateTime.parse(data.get("timestamp"), dateTimeFmt);

        String expiryRaw = data.get("expiryDate");
        LocalDate expiry = (expiryRaw == null || expiryRaw.isBlank())
                ? null
                : LocalDate.parse(expiryRaw, dateFmt);

        return new ReturnItem(id, sku, quantity, reason, timestamp, expiry);
    }

    /** @return unique identifier of the return */
    public String getReturnId() {
        return returnID;
    }

    /** @return SKU of the returned product */
    public String getSku() {
        return sku;
    }

    /** @return quantity returned */
    public int getQuantity() {
        return quantity;
    }

    /** @return reason for product return */
    public String getReason() {
        return reason;
    }

    /** @return timestamp of when the product was returned */
    public LocalDateTime getTimestamp() {
        return timeStamp;
    }

    /** @return expiry date for perishable products (may be null) */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    /**
     * Compares this return item to another using descending timestamp order.
     * If timestamps are equal, alphabetical ordering by return ID is used.
     *
     * @param other the item to compare with
     * @return negative if this item should come before the other
     */
    @Override
    public int compareTo(ReturnItem other) {
        int cmp = other.timeStamp.compareTo(this.timeStamp);
        return (cmp != 0) ? cmp : this.returnID.compareTo(other.returnID);
    }

    /**
     * @return a string representation useful for logging and debugging
     */
    @Override
    public String toString() {
        return String.format("ReturnItem{id=%s, sku=%s, qty=%d, reason=%s, timestamp=%s, expiry=%s}",
                returnID, sku, quantity, reason, timeStamp, expiryDate);
    }
}
