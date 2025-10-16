package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Item {
    private enum Category{
        Cleaning,
        Electronics,
        Hardware,
        Beverage,
        PersonalCare,
        Grocery
    }

    private enum Unit{
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

    public Item(int sku, String name, String category, String unit, double volume, double unitWeight){
        this.sku = sku;
        this.name = name;
        switch (category){
            case "Cleaning":
                this.category = Category.Cleaning;
                break;
            case "Electronics":
                this.category = Category.Electronics;
                break;
            case "Hardware":
                this.category = Category.Hardware;
                break;
            case "Beverage":
                this.category = Category.Beverage;
                break;
            case "PersonalCare":
                this.category = Category.PersonalCare;
                break;
            case "Grocery":
                this.category = Category.Grocery;
                break;
            default:
               System.err.println("SKU " + sku + " has an invalid category: " + category);
               return;
        }

        switch(unit){
            case "Pack":
                this.unit = Unit.Pack;
                break;
            case "Bottle":
                this.unit = Unit.Bottle;
                break;
            case "Unit":
                this.unit = Unit.Unit;
                break;
            case "Bag":
                this.unit = Unit.Bag;
                break;
            case "Box":
                this.unit = Unit.Box;
                break;
            default:
                System.err.println("SKU " + sku + " has an invalid unit: " + unit);
                return;
        }

        this.volume = volume;
        this.unitWeight = unitWeight;
    }

}
