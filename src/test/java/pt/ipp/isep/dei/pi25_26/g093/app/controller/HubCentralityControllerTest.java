package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.HubScore;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Edge;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Graph;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.MapGraph;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;

public class HubCentralityControllerTest {
    private Graph<CompleteStation, Double> graph = new MapGraph<>(false);
    ArrayList<CompleteStation> graphVertices;
    List<HubScore> scores;

    @Before
    public void SetUp()
    {
        CompleteStation station1 = new CompleteStation(
                "FR","('Europe/Paris',)","CET","Chateau-Arnoux-St-Auban",
                44.08179,6.001625,true,false,false);
        CompleteStation station2 = new CompleteStation(
                "FR","('Europe/Paris',)","CET","Chateau-Arnoux-St-Auban",
                44.0615651,5.9973734,false,true,false);
        CompleteStation station3 = new CompleteStation(
                "FR","('Europe/Paris',)","CET","Chateau-Arnoux Mairie",
                44.063863,6.011248,false,false,false);
        CompleteStation station4 = new CompleteStation(
                "FR","('Europe/Paris',)","CET","Digne-les-Bains",
                44.35,6.35,true,false,false);
        CompleteStation station5 = new CompleteStation(
                "FR","('Europe/Paris',)","CET","Digne-les-Bains",
                44.088710133980605,6.222982406616211,false,true,false);

        CompleteStation[] stations = {station1, station2, station3, station4, station5};

        for (CompleteStation station : stations) graph.addVertex(station);

        graph.addEdge(stations[0], stations[1], 4.0);
        graph.addEdge(stations[1], stations[2], 3.0);
        graph.addEdge(stations[2], stations[3], 6.0);
        graph.addEdge(stations[3], stations[0], 1.0);
        graph.addEdge(stations[4], stations[0], 5.0);
        graph.addEdge(stations[4], stations[2], 3.0);

        graphVertices = graph.vertices();
        HubCentralityController controller = new HubCentralityController();
        scores = controller.HubCentrality(graph);
    }

    @Test
    public void TestDegree()
    {
        int[] expected = {3, 2, 3, 2, 2};

        for(int i = 0; i < graphVertices.size(); i++)
        {
            assertEquals(expected[i], scores.get(i).getDegree());
        }
    }

    @Test
    public void TestStrength()
    {
        // Get total weight (for normalization)
        Collection<Edge<CompleteStation, Double>> allEdges = graph.edges();
        double totalWeight = 0;
        for(Edge<CompleteStation, Double> edge : allEdges)
            totalWeight += edge.getWeight();

        double[] expected = {10.0, 7.0, 12.0, 7.0, 8.0};

        for(int i = 0; i < graphVertices.size(); i++)
        {
            assertEquals(expected[i] / totalWeight, scores.get(i).getStrength(), 0.0);
        }
    }

    @Test
    public void TestBetweenness()
    {
        double[] expected = {0.333,	0.0, 0.166, 0.166, 0.0};
        for(int i = 0; i < graphVertices.size(); i++)
        {
            assertEquals(expected[i], scores.get(i).getBetweenness(), 0.001);
        }
    }

    @Test
    public void TestCloseness()
    {
        double[] expected = {0.398,	0.237, 0.244, 0.383, 0.216};
        for(int i = 0; i < graphVertices.size(); i++)
        {
            assertEquals(expected[i], scores.get(i).getCloseness(), 0.001);
        }
    }

    @Test
    public void TestHubScore()
    {
        List<Double> expected = new ArrayList<>();
        for(HubScore score : scores)
            expected.add(score.getBetweenness() * 0.35 + score.getCloseness() * 0.35 + score.getStrength() * 0.3);

        for(int i = 0; i < graphVertices.size(); i++)
        {
            assertEquals(expected.get(i), scores.get(i).getHubScore(), 0.0);
        }
    }
}
