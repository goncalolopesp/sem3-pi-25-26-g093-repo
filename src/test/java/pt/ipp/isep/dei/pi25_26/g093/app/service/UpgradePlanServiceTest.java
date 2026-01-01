package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Unit tests for the business logic of USEI11.
 *
 * <p>These tests validate the decisions made by UpgradePlanService:
 * detection of cycles and generation of a valid upgrade order.
 */
public class UpgradePlanServiceTest {

    private UpgradePlanService service;

    private NetworkNode nodeA;
    private NetworkNode nodeB;
    private NetworkNode nodeC;

    @Before
    public void setUp() {
        service = new UpgradePlanService();

        nodeA = new NetworkNode("A", "Node A", 10, 10, 0, 0);
        nodeB = new NetworkNode("B", "Node B", 20, 20, 0, 0);
        nodeC = new NetworkNode("C", "Node C", 30, 30, 0, 0);
    }

    // =========================================================
    // BUSINESS CASE 1: GRAPH WITHOUT CYCLES
    // =========================================================

    @Test
    public void testCalculateUpgradePlan_AcyclicGraph() {

        List<NetworkNode> nodes = List.of(nodeA, nodeB, nodeC);

        List<NetworkEdge> edges = List.of(
                new NetworkEdge("A", "B", 10, 50, 100),
                new NetworkEdge("B", "C", 15, 60, 120)
        );

        UpgradePlanResult result =
                service.calculateUpgradePlan(nodes, edges);

        assertFalse(result.hasCycle());

        List<NetworkNode> order = result.getTopologicalOrder();
        assertEquals(3, order.size());

        int indexA = indexOf(order, "A");
        int indexB = indexOf(order, "B");
        int indexC = indexOf(order, "C");

        assertTrue(indexA < indexB);
        assertTrue(indexB < indexC);
    }

    // =========================================================
    // BUSINESS CASE 2: GRAPH WITH CYCLES
    // =========================================================

    @Test
    public void testCalculateUpgradePlan_CyclicGraph() {

        List<NetworkNode> nodes = List.of(nodeA, nodeB);

        List<NetworkEdge> edges = List.of(
                new NetworkEdge("A", "B", 10, 50, 100),
                new NetworkEdge("B", "A", 10, 50, 100)
        );

        UpgradePlanResult result =
                service.calculateUpgradePlan(nodes, edges);

        assertTrue(result.hasCycle());

        Set<NetworkNode> cycleNodes = result.getCycleNodes();
        Set<NetworkEdge> cycleEdges = result.getCycleEdges();

        assertEquals(2, cycleNodes.size());
        assertEquals(2, cycleEdges.size());
    }

    // =========================================================
    // BUSINESS CASE 3: INVALID INPUT
    // =========================================================

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateUpgradePlan_NullNodes() {
        service.calculateUpgradePlan(null, List.of());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateUpgradePlan_EmptyNodes() {
        service.calculateUpgradePlan(List.of(), List.of());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateUpgradePlan_NullEdges() {
        service.calculateUpgradePlan(List.of(nodeA), null);
    }

    // =========================================================
    // HELPER
    // =========================================================

    private int indexOf(List<NetworkNode> list, String nodeId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNodeId().equals(nodeId)) {
                return i;
            }
        }
        return -1;
    }
}
