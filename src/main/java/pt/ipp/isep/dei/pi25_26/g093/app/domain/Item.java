package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Item {
    private enum Category {
        Cleaning,
        Electronics,
        Hardware,
        Beverage,
        PersonalCare,
        Grocery
    }

    private enum Unit {
        Pack,
        Bottle,
        Unit,
        Bag,
        Box
    }

    private int sku;
    private String name;
    private Category category;
    private Unit unit;
    private double volume;
    private double unitWeight;

    // Indica se o item é válido
    private boolean valid = true;

    public Item(int sku, String name, String category, String unit, double volume, double unitWeight) {
        this.sku = sku;
        this.name = name;
        this.volume = volume;
        this.unitWeight = unitWeight;

        // Validar categoria
        try {
            this.category = Category.valueOf(category);
        } catch (Exception e) {
            System.err.println("SKU " + sku + " has an invalid category: " + category);
            valid = false;
            return;
        }

        // Validar unidade
        try {
            this.unit = Unit.valueOf(unit);
        } catch (Exception e) {
            System.err.println("SKU " + sku + " has an invalid unit: " + unit);
            valid = false;
            return;
        }
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public String toString() {
        if (!valid) return "";
        return name;
    }
}
