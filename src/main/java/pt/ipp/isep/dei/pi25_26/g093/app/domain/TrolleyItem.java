package pt.ipp.isep.dei.pi25_26.g093.app.domain;

/**
 * Representa um item individual (produto) colocado num trolley.
 * Cada item vem de uma alocação (AllocationRow) recebida da USEI02.
 */
public class TrolleyItem {
    private final String orderId;
    private final String sku;
    private final int qty;
    private final double weight; // peso total (qty * unitWeight)
    private final int aisle;
    private final int bay;

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
/**
 * Representa um item individual (produto) colocado num trolley.
 * Cada item vem de uma alocação (AllocationRow) recebida da USEI02.
 * Esta classe é utilizada exclusivamente pela USEI03 (Picking Planner).
 */

