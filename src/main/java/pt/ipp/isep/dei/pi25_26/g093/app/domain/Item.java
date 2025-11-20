package pt.ipp.isep.dei.pi25_26.g093.app.domain;

/**
 * Item class representing a product with various attributes.
 */
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
    private boolean valid = true;

    /**
     * Constructor for Item.
     * @param sku the identifier for the item
     * @param name the name of the item
     * @param category the category of the item
     * @param unit the unit of the item
     * @param volume the volume of the item
     * @param unitWeight the weight per unit of the item
     */
    public Item(int sku, String name, String category, String unit, double volume, double unitWeight) {
        this.sku = sku;
        this.name = name;
        this.volume = volume;
        this.unitWeight = unitWeight;

        try {
            this.category = Category.valueOf(category);
        } catch (Exception e) {
            System.err.println("SKU " + sku + " has an invalid category: " + category);
            valid = false;
            return;
        }

        try {
            this.unit = Unit.valueOf(unit);
        } catch (Exception e) {
            System.err.println("SKU " + sku + " has an invalid unit: " + unit);
            valid = false;
            return;
        }
    }

    /**
     * Checks if the item is valid.
     * @return true if the item is valid, false otherwise
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Returns the string representation of the item.
     * @return the name of the item if valid, otherwise an empty string
     */
    @Override
    public String toString() {
        if (!valid) return "";
        return name;
    }
}
