package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoRadiusSearchService;

/**
 * Controller for USEI10 – Radius Search & Density Summary.
 * <p>
 * This controller acts as an intermediate layer between the UI and the
 * {@link GeoRadiusSearchService}. It receives the user inputs (latitude,
 * longitude and search radius), forwards them to the service, and returns the
 * processed result to the UI.
 * </p>
 *
 * <p>
 * The controller performs no business logic of its own; all computation is
 * delegated to the service layer to maintain clean separation of concerns.
 * </p>
 */
public class GeoRadiusSearchController {

    /** Service responsible for performing radius-based geographical searches. */
    private final GeoRadiusSearchService service;

    /**
     * Creates a new controller and initializes the underlying service.
     */
    public GeoRadiusSearchController() {
        this.service = new GeoRadiusSearchService();
    }

    // NOVO CONSTRUTOR para testes (injeção de mocks)
    public GeoRadiusSearchController(GeoRadiusSearchService service) {
        this.service = service;
    }

    /**
     * Executes a radius search around a geographical point.
     *
     * @param lat      center latitude in decimal degrees
     * @param lon      center longitude in decimal degrees
     * @param radiusKm radius of the search area in kilometers
     *
     * @return a {@link GeoRadiusSearchService.Result} object containing:
     *         <ul>
     *             <li>Sorted list of stations inside the radius</li>
     *             <li>Summary by country</li>
     *             <li>City vs non-city station summary</li>
     *         </ul>
     */
    public GeoRadiusSearchService.Result execute(double lat, double lon, double radiusKm) {
        return service.search(lat, lon, radiusKm);
    }
}


