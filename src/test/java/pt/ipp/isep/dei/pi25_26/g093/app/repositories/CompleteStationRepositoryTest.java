package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.BST;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.util.List;

import static org.junit.Assert.*;

public class CompleteStationRepositoryTest {

    private CompleteStationRepository repo;

    @Before
    public void setUp() {
        repo = new CompleteStationRepository();
    }

    private CompleteStation cs(String name, String country, Double lat, Double lon, String tzg) {
        return new CompleteStation(country, "TZ", tzg, name, lat, lon, false, false, false);
    }

    @Test
    public void testAddAndGetStations() {
        CompleteStation s1 = cs("Lisboa", "PT", 10.0, -9.0, "G1");
        repo.addStation(s1);

        List<CompleteStation> list = repo.getStations();
        assertEquals(1, list.size());
        assertEquals(s1, list.get(0));
    }

    @Test
    public void testLatLonTzgTreesAreNotNull() {
        assertNotNull(repo.getLatTree());
        assertNotNull(repo.getLonTree());
        assertNotNull(repo.getTzgTree());
        assertNotNull(repo.getTree());
    }

    @Test
    public void testAddLatKeyInsertsIntoLatTree() {
        CompleteStation s = cs("Lisboa", "PT", 10.0, -9.0, "G1");
        LatKey key = new LatKey(s);
        repo.addLatKey(key);

        BST<LatKey> bst = repo.getLatTree();
        assertTrue(bst.contains(key));
    }

    @Test
    public void testAddLonKeyInsertsIntoLonTree() {
        CompleteStation s = cs("Lisboa", "PT", 10.0, -9.0, "G1");
        LonKey key = new LonKey(s);
        repo.addLonKey(key);

        BST<LonKey> bst = repo.getLonTree();
        assertTrue(bst.contains(key));
    }

    @Test
    public void testAddTzgKeyInsertsIntoTzgTree() {
        CompleteStation s = cs("Lisboa", "PT", 10.0, -9.0, "G1");
        TzgKey key = new TzgKey(s);
        repo.addTzgKey(key);

        BST<TzgKey> bst = repo.getTzgTree();
        assertTrue(bst.contains(key));
    }
}
