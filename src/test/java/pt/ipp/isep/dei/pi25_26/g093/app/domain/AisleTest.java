package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AisleTest {

    @Test
    public void testConstructorAndGetters() {
        List<Bay> bays = new ArrayList<>();
        Aisle aisle = new Aisle(1, bays);

        assertEquals(1, aisle.getId());
        assertEquals(bays, aisle.getBays());
    }

    @Test
    public void testSetBays() {
        List<Bay> bays1 = new ArrayList<>();
        Aisle aisle = new Aisle(1, bays1);

        List<Bay> bays2 = new ArrayList<>();
        bays2.add(new Bay(1, 10));

        aisle.setBays(bays2);
        assertEquals(bays2, aisle.getBays());
    }

    @Test
    public void testGetBaysNotNull() {
        Aisle aisle = new Aisle(1, new ArrayList<Bay>());
        assertNotNull(aisle.getBays());
    }
}
