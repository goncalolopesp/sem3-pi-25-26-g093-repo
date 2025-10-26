package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Eligibility {

    private String orderId;
    private int lineNo;
    private String sku;
    private int requestedQty;
    private int allocatedQty;
    private String status;

    public Eligibility(String orderId, int lineNo, String sku, int requestedQty, int allocatedQty) {
        this.orderId = orderId;
        this.lineNo = lineNo;
        this.sku = sku;
        this.requestedQty = requestedQty;
        this.allocatedQty = allocatedQty;
        this.status = "UNDISPATCHABLE";
    }

    // Getters and Setters
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

    public int getRequestedQty() {
        return requestedQty;
    }

    public void setRequestedQty(int requestedQty) {
        this.requestedQty = requestedQty;
    }

    public int getAllocatedQty() {
        return allocatedQty;
    }

    public void setAllocatedQty(int allocatedQty) {
        this.allocatedQty = allocatedQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Eligibility: orderId=%s | lineNo=%s | sku=%s | requestedQty=%d | allocatedQty=%d | status=%s",
                orderId, lineNo, sku, requestedQty, allocatedQty, status);
    }
}