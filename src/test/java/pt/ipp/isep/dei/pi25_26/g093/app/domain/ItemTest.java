package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

    @Test
    public void testValidItemConstruction() {
        Item item = new Item(10, "Soap", "Cleaning", "Pack", 0.3, 0.2);
        assertNotNull(item);
        assertTrue("Expected toString to include item name for valid item",
                item.toString().contains("Soap"));
    }

    @Test
    public void testDifferentValidCategory() {
        Item item = new Item(15, "TV", "Electronics", "Unit", 5.0, 10.0);
        assertTrue(item.toString().contains("TV"));
    }

    @Test
    public void testDifferentValidUnit() {
        Item item = new Item(22, "Rice", "Grocery", "Bag", 2.5, 2.0);
        assertTrue(item.toString().contains("Rice"));
    }

    @Test
    public void testInvalidCategory() {
        Item item = new Item(50, "Invalid Cat", "BadCategory", "Pack", 1.0, 1.0);

        assertNotNull(item);
        assertFalse("Invalid category should not result in valid item data",
                item.toString().contains("Invalid Cat"));
    }

    @Test
    public void testInvalidUnit() {
        Item item = new Item(51, "Invalid Unit", "Cleaning", "WrongUnit", 1.0, 1.0);

        assertNotNull(item);
        assertFalse("Invalid unit should not result in valid item data",
                item.toString().contains("Invalid Unit"));
    }

    @Test
    public void testConstructorDoesNotCrashWithNullStrings() {
        try {
            new Item(99, null, "Electronics", "Unit", 1.0, 1.0);
        } catch (Exception e) {
            fail("Constructor should not throw an exception when name is null");
        }
    }
}
