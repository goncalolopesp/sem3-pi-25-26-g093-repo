package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoRadiusSearchService;

public class GeoRadiusSearchController {

    private final GeoRadiusSearchService service;

    public GeoRadiusSearchController() {
        this.service = new GeoRadiusSearchService();
    }

    public GeoRadiusSearchService.Result execute(double lat, double lon, double radiusKm) {
        return service.search(lat, lon, radiusKm);
    }
}

