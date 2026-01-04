package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Edge;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Graph;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.MapGraph;

import java.util.*;

public class RailwayNetworkService {

    /**
     * USEI14 - Calcula o Fluxo Máximo entre duas estações.
     * * @param railwayNetwork O grafo original (Estações e Capacidades).
     * @param source A estação de origem.
     * @param sink A estação de destino.
     * @param <V> O tipo do vértice (pode ser String, Station, etc).
     * @return O número máximo de comboios (capacidade total).
     */
    public <V> double calculateMaxFlow(Graph<V, Double> railwayNetwork, V source, V sink) {

        // 1. Criar o Grafo Residual
        // Usamos o construtor de cópia do MapGraph para não alterar o original
        MapGraph<V, Double> residualGraph = new MapGraph<>(railwayNetwork);

        // O construtor copia as arestas, mas precisamos de garantir que existem as inversas (com peso 0)
        for (Edge<V, Double> edge : railwayNetwork.edges()) {
            V u = edge.getVOrig();
            V v = edge.getVDest();

            // Verifica se existe aresta inversa (v -> u)
            if (residualGraph.edge(v, u) == null) {
                residualGraph.addEdge(v, u, 0.0); // Cria aresta de retorno vazia
            }
        }

        double maxFlow = 0.0;
        Map<V, V> parentMap = new HashMap<>(); // Para guardar o caminho (Filho -> Pai)

        // 2. Loop Principal (Edmonds-Karp)
        // Enquanto o BFS encontrar um caminho no grafo residual...
        while (bfsResidual(residualGraph, source, sink, parentMap)) {

            // a. Descobrir o gargalo (menor capacidade no caminho encontrado)
            double pathFlow = Double.MAX_VALUE;
            V current = sink;

            while (!current.equals(source)) {
                V parent = parentMap.get(current);
                double cap = residualGraph.edge(parent, current).getWeight(); //
                pathFlow = Math.min(pathFlow, cap);
                current = parent;
            }

            // b. Atualizar as arestas no grafo residual
            current = sink;
            while (!current.equals(source)) {
                V parent = parentMap.get(current);

                // Subtrair capacidade na ida (usando setWeight do Edge)
                Edge<V, Double> fwdEdge = residualGraph.edge(parent, current);
                fwdEdge.setWeight(fwdEdge.getWeight() - pathFlow);

                // Somar capacidade na volta (para permitir cancelar fluxo depois)
                Edge<V, Double> bwdEdge = residualGraph.edge(current, parent);
                bwdEdge.setWeight(bwdEdge.getWeight() + pathFlow);

                current = parent;
            }

            // c. Somar ao total
            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    /**
     * BFS Privado e Específico para o Max Flow.
     * Não usamos o do Algorithms.java porque precisamos do mapa de pais (parentMap)
     * e precisamos de validar capacidades > 0.
     */
    private <V> boolean bfsResidual(Graph<V, Double> g, V start, V end, Map<V, V> parent) {
        parent.clear();
        Queue<V> q = new LinkedList<>();
        Set<V> visited = new HashSet<>();

        q.add(start);
        visited.add(start);
        parent.put(start, null);

        while (!q.isEmpty()) {
            V u = q.poll();

            // Usa o método outgoingEdges da biblioteca Graph
            for (Edge<V, Double> e : g.outgoingEdges(u)) {
                V v = e.getVDest();

                // Só visitamos se não foi visitado E se a aresta tem capacidade útil
                if (!visited.contains(v) && e.getWeight() > 0) {
                    parent.put(v, u); // Guardamos de onde viemos
                    visited.add(v);
                    q.add(v);

                    if (v.equals(end)) return true; // Encontrámos o destino!
                }
            }
        }
        return false;
    }
}
