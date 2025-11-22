package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaCircleSearch;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaCircleSearchResult;

public class GeoAreaCircleSearchController {

    private final GeoAreaCircleSearch service;

    public GeoAreaCircleSearchController() {
        this(new GeoAreaCircleSearch(
                Repositories.getInstance().getCompleteStationRepository()
        ));
    }

    public GeoAreaCircleSearchController(GeoAreaCircleSearch service) {
        this.service = service;
    }

    public GeoAreaCircleSearchResult searchCircle(
            double centerLat,
            double centerLon,
            double radiusKm,
            Boolean isCity,
            Boolean isMainStation,
            String country
    ) {
        return service.searchCircle(centerLat, centerLon, radiusKm,
                isCity, isMainStation, country);
    }
}

