package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

    @Test
    public void testValidItem() {
        Item i = new Item(1, "Soap", "Cleaning", "Unit", 1.0, 0.5);
        assertTrue(i.isValid());
        assertEquals("Soap", i.toString());
    }

    @Test
    public void testInvalidCategory() {
        Item i = new Item(2, "BadCat", "INVALID", "Unit", 1.0, 0.5);
        assertFalse(i.isValid());
        assertEquals("", i.toString());
    }

    @Test
    public void testInvalidUnit() {
        Item i = new Item(3, "BadUnit", "Cleaning", "INVALID", 1.0, 0.5);
        assertFalse(i.isValid());
        assertEquals("", i.toString());
    }

    @Test
    public void testValidDifferentCategoryAndUnit() {
        Item i = new Item(4, "Coke", "Beverage", "Bottle", 0.5, 0.5);
        assertTrue(i.isValid());
        assertEquals("Coke", i.toString());
    }
}
