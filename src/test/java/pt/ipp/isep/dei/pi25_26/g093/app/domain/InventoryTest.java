package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {

    @Test
    public void testInventoryStartsEmpty() {
        Inventory inv = new Inventory();
        String output = inv.toString();
        assertTrue("Inventory should start empty and return empty string", output.isEmpty());
    }

    @Test
    public void testAddSingleItem() {
        Inventory inv = new Inventory();
        Item item = new Item(101, "Soap", "Cleaning", "Pack", 0.3, 0.2);

        inv.add(item);
        String output = inv.toString();

        assertTrue("Expected inventory string to contain item name", output.contains("Soap"));
        assertTrue("Expected separator after item string", output.contains("-----------------------"));
    }

    @Test
    public void testAddMultipleItems() {
        Inventory inv = new Inventory();
        Item i1 = new Item(201, "Laptop", "Electronics", "Unit", 1.0, 2.5);
        Item i2 = new Item(202, "Orange Juice", "Beverage", "Bottle", 1.2, 1.1);

        inv.add(i1);
        inv.add(i2);

        String output = inv.toString();

        assertTrue(output.contains("Laptop"));
        assertTrue(output.contains("Orange Juice"));

        int occurrences = output.split("-----------------------", -1).length - 1;
        assertEquals("Expected one separator line per item", 2, occurrences);
    }

    @Test
    public void testInvalidCategoryResultsInEmptyInventoryAddition() {
        Inventory inv = new Inventory();
        Item invalidItem = new Item(301, "Mystery Box", "InvalidCategory", "Box", 2.0, 1.0);

        inv.add(invalidItem);
        String output = inv.toString();

        assertFalse("Invalid category should not result in valid inventory entry",
                output.contains("Mystery Box"));
    }

    @Test
    public void testInvalidUnitResultsInEmptyInventoryAddition() {
        Inventory inv = new Inventory();
        Item invalidItem = new Item(302, "Unknown Unit Item", "Grocery", "UnknownUnit", 1.0, 0.5);

        inv.add(invalidItem);
        String output = inv.toString();

        assertFalse("Invalid unit should not result in valid inventory entry",
                output.contains("Unknown Unit Item"));
    }
}
