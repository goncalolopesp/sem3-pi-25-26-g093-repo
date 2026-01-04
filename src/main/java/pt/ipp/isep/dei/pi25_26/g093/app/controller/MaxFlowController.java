package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Graph;
import pt.ipp.isep.dei.pi25_26.g093.app.service.RailwayNetworkService;

public class MaxFlowController {

    private final Graph<String, Double> railwayNetwork;
    private final RailwayNetworkService service;

    // O Controller recebe o grafo (a rede ferroviária carregada no Bootstrap)
    public MaxFlowController(Graph<String, Double> railwayNetwork) {
        this.railwayNetwork = railwayNetwork;
        this.service = new RailwayNetworkService();
    }

    public double getMaxFlow(String source, String sink) {
        // Validações básicas antes de chamar o serviço
        if (source == null || sink == null || source.trim().isEmpty() || sink.trim().isEmpty()) {
            throw new IllegalArgumentException("Os nomes das estações não podem estar vazios.");
        }

        // Chama o serviço que criaste no passo anterior
        return service.calculateMaxFlow(railwayNetwork, source, sink);
    }
}
