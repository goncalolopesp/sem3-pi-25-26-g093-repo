package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;

public class GraphRepositoryTest {

    private GraphRepository repository;
    private CompleteStation s1;
    private CompleteStation s2;

    @Before
    public void setUp() {
        repository = new GraphRepository();

        s1 = new CompleteStation("Portugal", "GMT", "Europe", "Porto-São Bento",
                41.1456, -8.6103, true, true, false);
        s2 = new CompleteStation("Portugal", "GMT", "Europe", "Campanhã",
                41.1485, -8.5852, true, true, false);
    }

    @Test
    public void testAddStationAndGetById() {
        repository.addStation(10, s1, 100.0, 200.0);

        Assert.assertEquals("The station should be available by ID", s1, repository.getStationById(10));
        Assert.assertArrayEquals("The coordinates should be the same", new double[]{100.0, 200.0}, repository.getCoords(s1), 0.0);
    }

    @Test
    public void testAddTrack() {
        repository.addStation(10, s1, 0, 0);
        repository.addStation(20, s2, 10, 10);
        repository.addTrack(s1, s2, 2.5);

        Assert.assertNotNull("The connection should exist in the Graph", repository.getGraph().edge(s1, s2));
        Assert.assertEquals(2.5, repository.getGraph().edge(s1, s2).getWeight(), 0.01);
    }
}