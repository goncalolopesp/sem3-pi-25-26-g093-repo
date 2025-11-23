package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaCircleSearch;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaCircleSearchResult;

/**
 * Controller for USEI09 - Search by Circular Region (Radius Query).
 * 
 * <p>This controller mediates between the UI layer and the service layer,
 * delegating search operations to GeoAreaCircleSearch.
 * 
 * @author ESINF Sprint 2 Team
 * @see GeoAreaCircleSearch
 * @see GeoAreaCircleSearchUI
 */
public class GeoAreaCircleSearchController {

    private final GeoAreaCircleSearch service;

    /**
     * Constructs a new controller using the default repository instance.
     */
    public GeoAreaCircleSearchController() {
        this(new GeoAreaCircleSearch(
                Repositories.getInstance().getCompleteStationRepository()
        ));
    }

    /**
     * Constructs a new controller with the specified service.
     * 
     * @param service the GeoAreaCircleSearch service to use. Must not be null.
     */
    public GeoAreaCircleSearchController(GeoAreaCircleSearch service) {
        this.service = service;
    }

    /**
     * Executes a circular geographical area search with optional filters.
     * 
     * <p>This method delegates to the service layer to perform the search
     * using KD-tree pruning and Haversine distance calculation.
     * 
     * @param centerLat center point latitude (range [-90, 90])
     * @param centerLon center point longitude (range [-180, 180])
     * @param radiusKm search radius in kilometers (must be &gt; 0)
     * @param isCity filter for city stations (true = only cities, false = only non-cities, null = no filter)
     * @param isMainStation filter for main stations (true = only main, false = only non-main, null = no filter)
     * @param country filter for country ("PT", "ES", "ALL", or null = all countries)
     * @return GeoAreaCircleSearchResult containing matching stations with distances
     *         and metadata. Never null.
     * @throws IllegalArgumentException if input parameters are invalid
     * @throws IllegalStateException if the KD-tree is not built
     */
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

