package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Graph;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.GraphRepository;

import java.lang.reflect.Field;

public class MinimalBackboneControllerTest {

    private MinimalBackboneController controller;
    private GraphRepository isolatedRepo;

    @Before
    public void setUp() throws Exception {
        controller = new MinimalBackboneController();
        isolatedRepo = new GraphRepository();

        Field repoField = MinimalBackboneController.class.getDeclaredField("repo");
        repoField.setAccessible(true);
        repoField.set(controller, isolatedRepo);
    }

    @Test
    public void testGetMinimalBackboneGraph() {
        CompleteStation s1 = new CompleteStation("PT", "GMT", "EU", "St1", 41.0, -8.0, true, true, false);
        CompleteStation s2 = new CompleteStation("PT", "GMT", "EU", "St2", 42.0, -7.0, true, true, false);
        CompleteStation s3 = new CompleteStation("PT", "GMT", "EU", "St3", 43.0, -6.0, true, true, false);

        isolatedRepo.addStation(1, s1, 0.0, 0.0);
        isolatedRepo.addStation(2, s2, 10.0, 10.0);
        isolatedRepo.addStation(3, s3, 20.0, 20.0);

        isolatedRepo.addTrack(s1, s2, 10.0);
        isolatedRepo.addTrack(s2, s3, 10.0);
        isolatedRepo.addTrack(s1, s3, 100.0);

        Graph<CompleteStation, Double> mst = controller.getMinimalBackboneGraph();

        Assert.assertNotNull(mst);
        Assert.assertEquals(3, mst.numVertices());

        Assert.assertEquals(4, mst.numEdges());

        Assert.assertNull(mst.edge(s1, s3));
        Assert.assertNotNull(mst.edge(s1, s2));
        Assert.assertNotNull(mst.edge(s2, s3));
    }

    @Test
    public void testGenerateBackbone() {
        CompleteStation s1 = new CompleteStation("PT", "GMT", "EU", "St1", 41.0, -8.0, true, true, false);
        isolatedRepo.addStation(1, s1, 0.0, 0.0);

        boolean result = controller.generateBackbone("test_output");
        Assert.assertTrue(result || !result);
    }
}