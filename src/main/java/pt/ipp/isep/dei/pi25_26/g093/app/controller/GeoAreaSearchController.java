package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationAreaFilter;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaSearch;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaSearchResult;

/**
 * Controller for USEI08 - Search by Geographical Area (Rectangular Query).
 * 
 * <p>This controller mediates between the UI layer and the service layer,
 * handling the creation of StationAreaFilter objects and delegating
 * search operations to GeoAreaSearch.
 * 
 * @author ESINF Sprint 2 Team
 * @see GeoAreaSearch
 * @see GeoAreaSearchUI
 */
public class GeoAreaSearchController {
    private final GeoAreaSearch service;

    /**
     * Constructs a new controller using the default repository instance.
     */
    public GeoAreaSearchController() {
        this(new GeoAreaSearch(
                Repositories.getInstance()
                        .getCompleteStationRepository()));
    }

    /**
     * Constructs a new controller with the specified service.
     * 
     * @param service the GeoAreaSearch service to use. Must not be null.
     */
    public GeoAreaSearchController(GeoAreaSearch service) {
        this.service = service;
    }

    /**
     * Executes a rectangular geographical area search with optional filters.
     * 
     * <p>This method creates a StationAreaFilter from the provided parameters
     * and delegates to the service layer.
     * 
     * @param latMin minimum latitude of the search rectangle (inclusive, range [-90, 90])
     * @param latMax maximum latitude of the search rectangle (inclusive, range [-90, 90])
     * @param lonMin minimum longitude of the search rectangle (inclusive, range [-180, 180])
     * @param lonMax maximum longitude of the search rectangle (inclusive, range [-180, 180])
     * @param isCity filter for city stations (true = only cities, false = only non-cities, null = no filter)
     * @param isMainStation filter for main stations (true = only main, false = only non-main, null = no filter)
     * @param country filter for country ("PT", "ES", "ALL", or null = all countries)
     * @return GeoAreaSearchResult containing matching stations and metadata. Never null.
     * @throws IllegalArgumentException if input ranges are invalid
     * @throws IllegalStateException if the KD-tree is not built
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
