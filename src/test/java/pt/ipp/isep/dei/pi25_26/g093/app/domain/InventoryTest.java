package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {

    @Test
    public void testAddValidItem() {
        Inventory inv = new Inventory();
        Item i = new Item(1, "Soap", "Cleaning", "Unit", 1.0, 0.5);

        inv.add(i);
        String text = inv.toString();
        assertTrue(text.contains("Soap"));
    }

    @Test
    public void testAddNullItemIsIgnored() {
        Inventory inv = new Inventory();
        inv.add(null);
        assertEquals("", inv.toString());
    }

    @Test
    public void testAddInvalidItemIsIgnored() {
        Inventory inv = new Inventory();
        Item i = new Item(2, "Weird", "InvalidCat", "Unit", 1.0, 0.1);
        inv.add(i);

        assertEquals("", inv.toString());
    }

    @Test
    public void testToStringMultipleItems() {
        Inventory inv = new Inventory();
        inv.add(new Item(1, "Soap", "Cleaning", "Unit", 1.0, 0.5));
        inv.add(new Item(2, "TV", "Electronics", "Unit", 10.0, 5.0));

        String text = inv.toString();
        assertTrue(text.contains("Soap"));
        assertTrue(text.contains("TV"));
        assertTrue(text.contains("-----------------------"));
    }
}
