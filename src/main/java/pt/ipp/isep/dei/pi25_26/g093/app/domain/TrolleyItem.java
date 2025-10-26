package pt.ipp.isep.dei.pi25_26.g093.app.domain;

/**
 * Represents a product item allocated to a trolley.
 * Each item comes from an allocation generated in USEI02.
 */
public class TrolleyItem {

    private final String orderId;
    private final String sku;
    private final int qty;
    private final double weight;
    private final int aisle;
    private final int bay;

    /**
     * Creates a new trolley item.
     *
     * @param orderId order identifier
     * @param sku product SKU code
     * @param qty quantity of the product
     * @param weight total weight (kg)
     * @param aisle warehouse aisle number
     * @param bay warehouse bay number
     */
    public TrolleyItem(String orderId, String sku, int qty, double weight, int aisle, int bay) {
        this.orderId = orderId;
        this.sku = sku;
        this.qty = qty;
        this.weight = weight;
        this.aisle = aisle;
        this.bay = bay;
    }

    public String getOrderId() { return orderId; }
    public String getSku() { return sku; }
    public int getQty() { return qty; }
    public double getWeight() { return weight; }
    public int getAisle() { return aisle; }
    public int getBay() { return bay; }

    @Override
    public String toString() {
        return String.format("Order %s | SKU %s | Qty %d | %.2f kg | (Aisle %d, Bay %d)",
                orderId, sku, qty, weight, aisle, bay);
    }
}



