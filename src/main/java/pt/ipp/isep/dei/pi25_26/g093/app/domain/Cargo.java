package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a cargo box stored or handled in the terminal.
 */
public class Cargo {

    private int boxID;
    private int sku;
    private int quantity;
    private Date expiryDate;     // YYYY-MM-DD
    private DateTime receivedAt; // YYYY-MM-DDTHH:MM:SS

    // Flag indicating whether the cargo has a meaningful expiry date
    private boolean hasExpiry;

    /**
     * Creates a new Cargo with the given parameters.
     *
     * @param boxID       ID of the box
     * @param sku         SKU of the product
     * @param quantity    quantity in the box
     * @param expiryDate  expiry date (YYYY-MM-DD) or null
     * @param receivedAt  received date-time (YYYY-MM-DDTHH:MM:SS)
     */
    public Cargo(int boxID, int sku, int quantity, String expiryDate, String receivedAt) {
        this.boxID = boxID;
        this.sku = sku;
        this.quantity = quantity;

        if (expiryDate == null || expiryDate.isBlank()) {
            this.expiryDate = new Date("9999-12-31");
            this.hasExpiry = false;
        } else {
            this.expiryDate = new Date(expiryDate);
            this.hasExpiry = true;
        }

        this.receivedAt = new DateTime(receivedAt);
    }

    /**
     * Constructor for returned goods support (USEI05).
     * boxID and sku are strings because they may contain prefixes such as "RET-" or alphanumeric codes.
     */
    public Cargo(String boxID, String sku, int quantity, String expiryDate, String receivedAt) {

        try {
            this.boxID = Integer.parseInt(boxID.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            this.boxID = -1;
        }

        try {
            this.sku = Integer.parseInt(sku.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            this.sku = -1;
        }

        this.quantity = quantity;

        if (expiryDate == null || expiryDate.isBlank()) {
            this.expiryDate = new Date("9999-12-31");
            this.hasExpiry = false;
        } else {
            this.expiryDate = new Date(expiryDate);
            this.hasExpiry = true;
        }

        this.receivedAt = new DateTime(receivedAt);
    }

    /**
     * Returns the Date object representing the expiry date (internal object).
     */
    public Date getExpiryDateObj() {
        return expiryDate;
    }

    /**
     * Returns the DateTime object representing when the cargo was received.
     */
    public DateTime getReceivedAtObj() {
        return receivedAt;
    }

    /**
     * Indicates whether this cargo has an explicit expiry date.
     * Cargo created with an empty expiry uses 9999-12-31 as a placeholder.
     */
    public boolean hasExpiry() {
        return hasExpiry;
    }

    public int getBoxID() {
        return boxID;
    }

    public int getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getExpiryDate() {
        return expiryDate.toString();
    }

    public String getReceivedAt() {
        return receivedAt.toString();
    }

    public void setQuantity(int quantity) {this.quantity = quantity;}
}
