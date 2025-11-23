package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.service.NearestNSearchService;
import pt.ipp.isep.dei.pi25_26.g093.app.service.NearestNSearchResult;

/**
 * Controller for USEI09 - Proximity Search (Nearest-N with Filters).
 * 
 * <p>This controller mediates between the UI layer and the service layer,
 * delegating search operations to NearestNSearchService.
 * 
 * @author ESINF Sprint 2 Team
 * @see NearestNSearchService
 * @see NearestNSearchUI
 */
public class NearestNSearchController {

    private final NearestNSearchService service;

    /**
     * Constructs a new controller using the default repository instance.
     */
    public NearestNSearchController() {
        this(new NearestNSearchService(
                Repositories.getInstance().getCompleteStationRepository()
        ));
    }

    /**
     * Constructs a new controller with the specified service.
     * 
     * @param service the NearestNSearchService service to use. Must not be null.
     */
    public NearestNSearchController(NearestNSearchService service) {
        this.service = service;
    }

    /**
     * Executes a nearest-N proximity search with optional filters.
     * 
     * <p>This method delegates to the service layer to perform the search
     * using KD-tree nearest-N algorithm with backtracking, pruning, and Haversine distance calculation.
     * 
     * @param centerLat target point latitude (range [-90, 90])
     * @param centerLon target point longitude (range [-180, 180])
     * @param n number of nearest stations to find (must be &gt; 0)
     * @param isCity filter for city stations (true = only cities, false = only non-cities, null = no filter)
     * @param isMainStation filter for main stations (true = only main, false = only non-main, null = no filter)
     * @param country filter for country ("PT", "ES", "ALL", or null = all countries)
     * @param timeZone filter for time zone (exact match, case-insensitive), or null = no filter
     * @param timeZoneGroup filter for time zone group (exact match, case-insensitive), or null = no filter
     * @return NearestNSearchResult containing the N nearest stations with distances
     *         and metadata. Never null.
     * @throws IllegalArgumentException if input parameters are invalid
     * @throws IllegalStateException if the KD-tree is not built
     */
    public NearestNSearchResult searchNearestN(
            double centerLat,
            double centerLon,
            int n,
            Boolean isCity,
            Boolean isMainStation,
            String country,
            String timeZone,
            String timeZoneGroup
    ) {
        return service.searchNearestN(centerLat, centerLon, n,
                isCity, isMainStation, country, timeZone, timeZoneGroup);
    }
}

