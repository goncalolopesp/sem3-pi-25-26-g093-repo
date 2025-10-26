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
    private LocalDate expiryDate;     // YYYY-MM-DD
    private LocalDateTime receivedAt; // YYYY-MM-DDTHH:MM:SS

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

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        if (expiryDate == null || expiryDate.isBlank()) {
            this.expiryDate = LocalDate.parse("9999-12-31", dateFormatter);
        } else {
            this.expiryDate = LocalDate.parse(expiryDate, dateFormatter);
        }

        this.receivedAt = LocalDateTime.parse(receivedAt, dateTimeFormatter);
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

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        if (expiryDate == null || expiryDate.isBlank()) {
            this.expiryDate = LocalDate.parse("9999-12-31", dateFormatter);
        } else {
            this.expiryDate = LocalDate.parse(expiryDate, dateFormatter);
        }

        this.receivedAt = LocalDateTime.parse(receivedAt, dateTimeFormatter);
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
}
