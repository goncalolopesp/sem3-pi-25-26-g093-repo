package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoRadiusSearchService;

/**
 * Controller que liga a UI ao Service.
 */
public class GeoRadiusSearchController {

    private final GeoRadiusSearchService service;

    /**
     * CONSTRUTOR 1: Usado pela UI (Modo Real).
     * Vai buscar a árvore carregada no sistema para iniciar o serviço.
     */
    public GeoRadiusSearchController() {

        var stationRepository = Repositories.getInstance().getCompleteStationRepository();
        Tree<Double> globalKDTree = stationRepository.getTree();
        this.service = new GeoRadiusSearchService(globalKDTree);
    }

    /**
     * CONSTRUTOR 2: Usado pelos Testes (Modo Mock).
     * Permite injetar um serviço com dados falsos para testar sem a base de dados.
     */
    public GeoRadiusSearchController(GeoRadiusSearchService service) {
        this.service = service;
    }

    /**
     * Método chamado pela UI para executar a pesquisa.
     *
     * @param lat      Latitude do centro
     * @param lon      Longitude do centro
     * @param radiusKm Raio em quilómetros
     * @return O resultado contendo a árvore ordenada e estatísticas
     */
    public GeoRadiusSearchService.Result execute(double lat, double lon, double radiusKm) {
        return service.search(lat, lon, radiusKm);
    }
}