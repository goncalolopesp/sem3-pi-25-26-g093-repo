package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.GeoRadiusSearchController;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.AVL;
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

        try {
            // Dica: Podes usar valores default para testar rápido se não quiseres escrever sempre
            System.out.print("Center latitude: ");
            double lat = Double.parseDouble(sc.nextLine());

            System.out.print("Center longitude: ");
            double lon = Double.parseDouble(sc.nextLine());

            System.out.print("Radius (km): ");
            double radiusKm = Double.parseDouble(sc.nextLine());

            // Chamada ao Controller
            GeoRadiusSearchService.Result result = controller.execute(lat, lon, radiusKm);

            // CORREÇÃO AQUI: A AVL recebe apenas 1 argumento genérico (o tipo do elemento)
            AVL<StationDistance> stationsTree = result.getStationsTree();

            if (stationsTree == null || stationsTree.isEmpty()) {
                System.out.println("\nNo stations found inside this radius.");
                return;
            }

            System.out.println("\nStations inside radius (sorted):");

            // Itera sobre os elementos da árvore.
            // O método inOrder() deve devolver um Iterable<StationDistance>
            for (StationDistance sd : stationsTree.inOrder()) {
                System.out.println(sd); // Usa o toString() que criaste: "2.5 km - StationName"
            }

            System.out.println("\n=== Summary by country ===");
            for (Map.Entry<String, Integer> e : result.getCountryCounts().entrySet()) {
                System.out.printf("%s: %d\n", e.getKey(), e.getValue());
            }

            System.out.println("\n=== City summary ===");
            System.out.printf("City stations: %d\n", result.getCityCount());
            System.out.printf("Non-city stations: %d\n", result.getNonCityCount());

        } catch (NumberFormatException e) {
            System.out.println("\nError: Invalid number format.");
        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }
}