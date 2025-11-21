package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationAreaFilter;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaSearch;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaSearchResult;

public class GeoAreaSearchController {
    private final GeoAreaSearch service;

    public GeoAreaSearchController() {
        this(new GeoAreaSearch(
                Repositories.getInstance()
                        .getCompleteStationRepository()));
    }

    public GeoAreaSearchController(GeoAreaSearch service) {
        this.service = service;
    }

    /**
     * Prepares the filter and calls the service to execute the search.
     */
    public GeoAreaSearchResult searchArea(
            double latMin, double latMax,
            double lonMin, double lonMax,
            Boolean isCity, Boolean isMainStation,
            String country) {

        StationAreaFilter filter =
                new StationAreaFilter(isCity, isMainStation, country);

        return service.searchByArea(latMin, latMax, lonMin, lonMax, filter);
    }
}
