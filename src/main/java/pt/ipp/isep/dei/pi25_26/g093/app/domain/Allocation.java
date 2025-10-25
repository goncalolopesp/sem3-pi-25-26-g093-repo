package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Allocation {

    private String orderId;
    private int lineNo;
    private String sku;
    private int qty;
    private String boxId;
    private int aisle;
    private int bay;

    public Allocation(String orderId, int lineNo, String sku, int qty, String boxId, int aisle, int bay) {
        this.orderId = orderId;
        this.lineNo = lineNo;
        this.sku = sku;
        this.qty = qty;
        this.boxId = boxId;
        this.aisle = aisle;
        this.bay = bay;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public int getAisle() {
        return aisle;
    }

    public void setAisle(int aisle) {
        this.aisle = aisle;
    }

    public int getBay() {
        return bay;
    }

    public void setBay(int bay) {
        this.bay = bay;
    }

    @Override
    public String toString() {
        return String.format("Allocation: orderId=%s | lineNo=%s | sku=%s | qty=%d | boxId=%s | aisle=%s | bay=%s}",
                orderId, lineNo, sku, qty, boxId, aisle, bay);
    }
}
