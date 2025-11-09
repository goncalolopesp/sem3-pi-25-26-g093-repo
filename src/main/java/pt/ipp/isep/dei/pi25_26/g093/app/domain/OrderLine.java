package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class OrderLine {
    private int lineNo;
    private String sku;
    private int qty;

    public OrderLine(int lineNo, String sku, int qty)
    {
        this.lineNo = lineNo;
        this.sku = sku;
        this.qty = qty;
    }

    public int getLineNo() {
        return lineNo;
    }

    public String getSku() {
        return sku;
    }

    public int getQty() {
        return qty;
    }
}
