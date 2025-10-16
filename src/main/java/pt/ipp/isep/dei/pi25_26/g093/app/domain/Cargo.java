package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Cargo {
    private int boxID;
    private int sku;
    private int quantity;
    private Date expiryDate; //Format : YYYY-MM-DD
    private DateTime receivedAt; //Format : YYYY-MM-DDTHH:MM:SS

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

    public int getBoxID(){
        return boxID;
    }

    public int getSku(){
        return sku;
    }

    public int getQuantity(){
        return quantity;
    }

    public String getExpiryDate(){
        return expiryDate.toString();
    }

    public String getReceivedAt(){
        return receivedAt.toString();
    }
}
