package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Cargo {
    private int boxID;
    private int sku;
    private int quantity;
    private Date expiryDate; //Format : YYYY-MM-DD
    private DateTime receivedAt; //Format : YYYY-MM-DDTHH:MM:SS

    /**
     * Creates a new Cargo with the given parameters.
     * @param boxID ID of the box
     * @param sku ID of the item
     * @param quantity Quantity of items in the box
     * @param expiryDate Date of expiry (YYYY-MM-DD)
     * @param receivedAt Date of arrival (YYYY-MM-DDTHH:MM:SS)
     */
    public Cargo(int boxID, int sku, int quantity, String expiryDate, String receivedAt){
        this.boxID = boxID;
        this.sku = sku;
        this.quantity = quantity;
        if (expiryDate == null || expiryDate.length() == 0){
            expiryDate = "9999-12-31";
        }
        this.expiryDate = new Date(expiryDate);
        this.receivedAt = new DateTime(receivedAt);
    }

    /**
     * Gets the ID of the box.
     * @return ID of the box
     */
    public int getBoxID(){
        return boxID;
    }

    /**
     * Gets the ID of the item.
     * @return ID of the item.
     */
    public int getSku(){
        return sku;
    }

    /**
     * Gets the quantity of items in the box.
     * @return quantity of items in the box.
     */
    public int getQuantity(){
        return quantity;
    }

    /**
     * Gets the expiry date of the cargo.
     * @return expiry date of the cargo.
     */
    public String getExpiryDate(){
        return expiryDate.toString();
    }

    /**
     * Gets the date and time when the cargo was received.
     * @return date and time when the cargo was received.
     */
    public String getReceivedAt(){
        return receivedAt.toString();
    }
}
