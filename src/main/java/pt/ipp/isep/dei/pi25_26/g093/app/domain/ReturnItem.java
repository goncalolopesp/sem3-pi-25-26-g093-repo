package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Represents an item returned to the warehouse that must be evaluated before
 * being reintroduced into inventory.
 *
 * Implements Comparable to support inspection in descending timestamp order,
 * ensuring the most recent returns are processed first (LIFO), as required in USEI05.
 */
public class ReturnItem implements Comparable<ReturnItem> {

    private String returnID;
    private String sku;
    private int quantity;
    private String reason;
    private LocalDateTime timeStamp;
    private LocalDate expiryDate;

    /**
     * Full constructor.
     *
     * @param returnID unique identifier of the return
     * @param sku SKU of the returned product
     * @param quantity returned quantity
     * @param reason reason for the return
     * @param timeStamp when the product was returned
     * @param expiryDate expiration date (may be null for non-perishables)
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


    public String getReturnId() { return returnID; }
    public String getSku() { return sku; }
    public int getQuantity() { return quantity; }
    public String getReason() { return reason; }
    public LocalDateTime getTimestamp() { return timeStamp; }
    public LocalDate getExpiryDate() { return expiryDate; }

    /**
     * Natural ordering: newest items first.
     *
     * @param other the item to compare against
     * @return comparison value for sorting
     */
    @Override
    public int compareTo(ReturnItem other) {
        int cmp = other.timeStamp.compareTo(this.timeStamp);
         return (cmp != 0) ? cmp : this.returnID.compareTo(other.returnID);
    }

    /**
     * @return formatted string with key return properties
     */
    @Override
    public String toString() {
        return String.format("ReturnItem{id=%s, sku=%s, qty=%d, reason=%s, timestamp=%s, expiry=%s}",
                returnID, sku, quantity, reason, timeStamp, expiryDate);
    }
}
