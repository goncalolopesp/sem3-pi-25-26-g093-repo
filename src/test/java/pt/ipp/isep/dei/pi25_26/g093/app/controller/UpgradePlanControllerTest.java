package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.service.UpgradePlanResult;
import pt.ipp.isep.dei.pi25_26.g093.app.service.UpgradePlanService;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class UpgradePlanControllerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullService_ThrowsException() {
        new UpgradePlanController(null);
    }

    @Test
    public void testCalculateUpgradePlan_DelegatesToService() {

        // Fake service (stub)
        UpgradePlanService fakeService = new UpgradePlanService() {
            @Override
            public UpgradePlanResult calculateUpgradePlan(
                    List<NetworkNode> nodes,
                    List<NetworkEdge> edges) {

                return new UpgradePlanResult(
                        Set.of(nodes.get(0)),
                        Set.of(edges.get(0)),
                        "fake complexity"
                );
            }
        };

        UpgradePlanController controller =
                new UpgradePlanController(fakeService);

        NetworkNode nodeA =
                new NetworkNode("A", "Node A", 10, 10, 0, 0);

        NetworkEdge edge =
                new NetworkEdge("A", "A", 1, 10, 5);

        List<NetworkNode> nodes = List.of(nodeA);
        List<NetworkEdge> edges = List.of(edge);

        UpgradePlanResult result =
                controller.calculateUpgradePlan(nodes, edges);

        assertTrue(result.hasCycle());
        assertEquals(1, result.getCycleNodes().size());
        assertEquals("fake complexity", result.getComplexityAnalysis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateUpgradePlan_NullNodes() {
        UpgradePlanController controller =
                new UpgradePlanController(new UpgradePlanService());

        controller.calculateUpgradePlan(null, List.of());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateUpgradePlan_EmptyNodes() {
        UpgradePlanController controller =
                new UpgradePlanController(new UpgradePlanService());

        controller.calculateUpgradePlan(List.of(), List.of());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateUpgradePlan_NullEdges() {
        UpgradePlanController controller =
                new UpgradePlanController(new UpgradePlanService());

        NetworkNode node =
                new NetworkNode("A", "Node A", 10, 10, 0, 0);

        controller.calculateUpgradePlan(List.of(node), null);
    }
}
