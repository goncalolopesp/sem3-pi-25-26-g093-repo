package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.GeoRadiusSearchController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationDistance;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoRadiusSearchService;

import java.util.Map;
import java.util.Scanner;

/**
 * UI for USEI10 – Radius Search & Density Summary.
 * <p>
 * This class provides a text-based user interface that allows a
 * Station Storage Manager to perform a radius search around a geographical
 * point and view:
 * </p>
 *
 * <ul>
 *     <li>All stations located inside the specified radius</li>
 *     <li>Results sorted by distance (ascending)</li>
 *     <li>A summary of stations per country</li>
 *     <li>A city vs non-city station summary</li>
 * </ul>
 *
 * <p>
 * The UI collects the necessary user input, validates it at a basic level,
 * forwards it to the {@link GeoRadiusSearchController}, and displays the
 * results returned by the service layer.
 * </p>
 *
 * <p>
 * This UI does not contain business logic — all calculations, filtering,
 * sorting and summaries are handled by the service.
 * </p>
 */
public class GeoRadiusSearchUI implements Runnable {

    /** Controller responsible for coordinating the radius search operation. */
    private final GeoRadiusSearchController controller = new GeoRadiusSearchController();

    /**
     * Executes the UI flow for USEI10.
     * <p>
     * Prompts the user for:
     * </p>
     * <ul>
     *     <li>Center latitude</li>
     *     <li>Center longitude</li>
     *     <li>Radius in kilometers</li>
     * </ul>
     *
     * <p>
     * After calling the controller, the method prints:
     * </p>
     *
     * <ul>
     *     <li>The sorted list of stations inside the radius</li>
     *     <li>A station count grouped by country</li>
     *     <li>A summary of city vs. non-city stations</li>
     * </ul>
     */
    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== USEI10 - Radius Search ===");

        System.out.print("Center latitude: ");
        double lat = Double.parseDouble(sc.nextLine());

        System.out.print("Center longitude: ");
        double lon = Double.parseDouble(sc.nextLine());

        System.out.print("Radius (km): ");
        double radiusKm = Double.parseDouble(sc.nextLine());

        try {
            GeoRadiusSearchService.Result result =
                    controller.execute(lat, lon, radiusKm);

            if (result.getStations().isEmpty()) {
                System.out.println("\nNo stations found inside this radius.");
                return;
            }

            System.out.println("\nStations inside radius (sorted):");
            for (StationDistance sd : result.getStations()) {
                System.out.println(sd);
            }

            System.out.println("\n=== Summary by country ===");
            for (Map.Entry<String, Integer> e : result.getCountryCounts().entrySet()) {
                System.out.printf("%s: %d\n", e.getKey(), e.getValue());
            }

            System.out.println("\n=== City summary ===");
            System.out.printf("City stations: %d\n", result.getCityCount());
            System.out.printf("Non-city stations: %d\n", result.getNonCityCount());

        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }
}
