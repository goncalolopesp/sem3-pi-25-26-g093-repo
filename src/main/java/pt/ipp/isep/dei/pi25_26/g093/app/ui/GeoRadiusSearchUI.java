package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.GeoRadiusSearchController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationDistance;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoRadiusSearchService;

import java.util.Map;
import java.util.Scanner;

public class GeoRadiusSearchUI implements Runnable {

    private final GeoRadiusSearchController controller = new GeoRadiusSearchController();

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

