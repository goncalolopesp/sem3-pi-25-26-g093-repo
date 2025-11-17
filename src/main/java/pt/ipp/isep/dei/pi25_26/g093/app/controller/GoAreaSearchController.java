package pt.ipp.isep.dei.pi25_26.g093.app.controller;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationAreaFilter;
/*import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaSearch;*/

public class GoAreaSearchController {
    private final GeoAreaSearch service;

    public GeoAreaSearchController(GeoAreaSearch service) {
        this.service = service;
    }

    /**
     * Prepares the filter and calls the service to execute the search.
     *
     * This method does NOT depend on USEI06 or USEI07.
     */
    public Object searchArea(
            double latMin, double latMax,
            double lonMin, double lonMax,
            Boolean isCity, Boolean isMainStation,
            String country) {

        StationAreaFilter filter =
                new StationAreaFilter(isCity, isMainStation, country);

        return service.searchByArea(latMin, latMax, lonMin, lonMax, filter);
    }
}
}
