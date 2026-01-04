package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.Test;
import org.junit.Assert;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.MapGraph;

public class RailwayNetworkServiceTest {

    @Test
    public void testCalculateMaxFlow() {
        // 1. Preparar: Criar um grafo simples para teste
        MapGraph<String, Double> graph = new MapGraph<>(true);

        // Cenário:
        // A -> B (Capacidade 10)
        // B -> C (Capacidade 5)  <-- Gargalo
        // A -> C (Capacidade 2)  <-- Caminho extra
        graph.addEdge("A", "B", 10.0);
        graph.addEdge("B", "C", 5.0);
        graph.addEdge("A", "C", 2.0);

        RailwayNetworkService service = new RailwayNetworkService();

        // 2. Executar: Calcular fluxo de A para C
        // Caminho 1 (A->B->C): Máximo 5.0 (limitado por B->C)
        // Caminho 2 (A->C): Máximo 2.0
        // Total esperado: 5.0 + 2.0 = 7.0
        double result = service.calculateMaxFlow(graph, "A", "C");
        Assert.assertEquals("O fluxo máximo deve ser 7.0", 7.0, result, 0.01);
    }

    @Test
    public void testCalculateMaxFlowSemCaminho() {
        MapGraph<String, Double> graph = new MapGraph<>(true);
        graph.addEdge("A", "B", 10.0);
        graph.addEdge("C", "D", 10.0);
        // Não há ligação entre (A,B) e (C,D)

        RailwayNetworkService service = new RailwayNetworkService();
        double result = service.calculateMaxFlow(graph, "A", "D");

        Assert.assertEquals("Se não há caminho, o fluxo deve ser 0", 0.0, result, 0.01);
    }
}