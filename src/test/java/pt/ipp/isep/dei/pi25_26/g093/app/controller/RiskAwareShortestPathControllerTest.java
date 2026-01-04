package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.RiskAwareShortestPathController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.PathResult;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.GraphRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.ArrayList;
import java.util.List;

public class RiskAwareShortestPathControllerTest {

    private RiskAwareShortestPathController controller;
    private GraphRepository repo;
    private List<CompleteStation> stations;

    @Before
    public void setUp() {
        repo = Repositories.getInstance().getGraphRepository();
        controller = new RiskAwareShortestPathController();

        stations = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            String stationName = "Station" + i;
            CompleteStation station = new CompleteStation(
                    "Country" + i,
                    "Timezone" + i,
                    "TimezoneGroup" + i,
                    stationName,
                    (double) i,
                    (double) i,
                    i % 2 == 0,
                    i % 3 == 0,
                    i % 5 == 0
            );
            stations.add(station);
            repo.addStation(i, station, (double) i, (double) i); // Adiciona as estações ao repositório
        }

        // Adicionando as arestas ao grafo
        for (int i = 0; i < 29; i++) {
            repo.addTrack(stations.get(i), stations.get(i + 1), i + 1.0); // Arestas entre Station1 e Station30
        }

        // Adicionando um ciclo negativo para testar
        repo.addTrack(stations.get(29), stations.get(0), -10.0); // Criando um ciclo negativo
        repo.addTrack(stations.get(10), stations.get(15), 3.0);   // Aresta com custo 3
        repo.addTrack(stations.get(20), stations.get(25), -2.0);  // Aresta com custo -2

        // Verificando as configurações do grafo
        System.out.println("Grafo configurado com " + repo.getGraph().numVertices() + " vértices e " + repo.getGraph().numEdges() + " arestas.");
    }


    @Test
    public void testNegativeCycleDetection() {
        PathResult result = controller.computeShortestPathWithRisk(stations.get(0), stations.get(29));

        assertFalse("Deve falhar ao detectar o ciclo negativo", result.isSuccess());
        assertEquals("A mensagem de erro deve ser sobre ciclo negativo", "Negative cycle detected between stations.", result.getErrorMessage());
    }

    @Test
    public void testPathNotExist() {
        PathResult result = controller.computeShortestPathWithRisk(stations.get(10), stations.get(29));

        assertFalse("O cálculo do caminho deve falhar quando não existe caminho", result.isSuccess());

        assertEquals("Negative cycle detected between stations.", result.getErrorMessage());
    }

    @Test
    public void testGetEdgeCost() {
        double cost = controller.getEdgeCost(stations.get(0), stations.get(1));
        assertEquals("O custo da aresta entre Station1 e Station2 deve ser 1.0", 1.0, cost, 0.001);

        cost = controller.getEdgeCost(stations.get(1), stations.get(2));
        assertEquals("O custo da aresta entre Station2 e Station3 deve ser 2.0", 2.0, cost, 0.001);

        cost = controller.getEdgeCost(stations.get(2), stations.get(3));
        assertEquals("O custo da aresta entre Station3 e Station4 deve ser 3.0", 3.0, cost, 0.001);

        cost = controller.getEdgeCost(stations.get(10), stations.get(15));
        assertEquals("O custo da aresta entre Station11 e Station16 deve ser 3.0", 3.0, cost, 0.001);
    }

    @Test
    public void testGetRiskPenalty() {
        double penalty = controller.getRiskPenalty(stations.get(0), stations.get(1));
        assertEquals("A penalidade de risco entre Station1 e Station2 deve ser 0.0", 0.0, penalty, 0.001);


        repo.addTrack(stations.get(5), stations.get(6), 6.0);
        penalty = controller.getRiskPenalty(stations.get(5), stations.get(6));
        assertEquals("A penalidade de risco entre Station6 e Station7 deve ser 1.0", 1.0, penalty, 0.001);
    }

    @Test
    public void testFindStationByName() {
        CompleteStation foundStation = controller.findStationByName("Station5");
        assertEquals("A estação Station5 deve ser encontrada corretamente", stations.get(4), foundStation);

        CompleteStation notFound = controller.findStationByName("Station100");
        assertNull("A estação Station100 não deve ser encontrada", notFound);
    }

}
