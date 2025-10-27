package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AisleTest {

    @Test
    public void testConstructorAndGetters() {
        List<Bay> bays = new ArrayList<>();
        bays.add(new Bay(1, 10));
        bays.add(new Bay(2, 8));

        Aisle aisle = new Aisle(5, bays);

        assertEquals(5, aisle.getId());
        assertEquals(bays, aisle.getBays());
    }

    @Test
    public void testSetBays() {
        List<Bay> initialBays = new ArrayList<>();
        initialBays.add(new Bay(1, 5));

        Aisle aisle = new Aisle(2, initialBays);

        List<Bay> newBays = new ArrayList<>();
        newBays.add(new Bay(3, 12));
        newBays.add(new Bay(4, 6));

        aisle.setBays(newBays);

        assertEquals(newBays, aisle.getBays());
    }

    @Test
    public void testGetId() {
        Aisle aisle = new Aisle(99, new ArrayList<>());
        assertEquals(99, aisle.getId());
    }
}
