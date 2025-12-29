package pt.ipp.isep.dei.pi25_26.g093.app.ui.LP;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Freight;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.PlannedRoute;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.WagonReal;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.database.FacilityRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.database.FreightRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.database.RailwayNetworkRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.service.RouteService;

import java.util.*;

public class PendingFreightUI {

    private final Scanner scanner = new Scanner(System.in);

    private final FreightRepository freightRepository = new FreightRepository();
    private final FacilityRepository facilityRepository = new FacilityRepository();
    private final RailwayNetworkRepository railwayNetworkRepository = new RailwayNetworkRepository();
    private final RouteService routeService = new RouteService(railwayNetworkRepository);
    private final DispatcherUI dispatcherUI = new DispatcherUI();

    // Planned routes (in memory – used later by USLP10)
    private final Map<Integer, PlannedRoute> plannedRoutes = new HashMap<>();
    private int nextRouteId = 1;

    // =========================================================
    // MENU
    // =========================================================
    public void showMenu() {

        int option;

        do {
            System.out.println("\n=== USLP08 - Pending Freights Planning ===");
            System.out.println("1) List all pending freights");
            System.out.println("2) Plan route for pending freights");
            System.out.println("3) Exit");
            System.out.print("Choose an option: ");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> listPendingFreights();
                case 2 -> planPendingFreights();
                case 3 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid option.");
            }

        } while (option != 3);
    }

    // =========================================================
    // LIST PENDING FREIGHTS
    // =========================================================
    private void listPendingFreights() {

        Collection<Freight> freights = freightRepository.getAll();

        if (freights.isEmpty()) {
            System.out.println("\nNo pending freights.");
            return;
        }

        System.out.println("\n=== PENDING FREIGHTS ===");

        for (Freight f : freights) {

            Optional<PlannedRoute> routeOpt = plannedRoutes.values().stream()
                    .filter(r -> r.getFreightIDs().contains(f.getId()))
                    .findFirst();

            boolean hasRoute = routeOpt.isPresent();

            System.out.printf(
                    "Freight %d | %s → %s | %d wagons | %s%n",
                    f.getId(),
                    f.getOrigin().getName(),
                    f.getDestination().getName(),
                    f.getWagons().size(),
                    hasRoute ? "ROUTE PLANNED" : "NO ROUTE ASSIGNED"
            );

            // Wagons + cargo type
            for (WagonReal w : f.getWagons()) {
                System.out.printf(
                        " - %s | Model: %s | Max load: %.1f t | Cargo Type: %s | Max speed: %.0f km/h%n",
                        w.getIdentifier(),
                        w.getModel().getMaker(),
                        w.getMaxLoadTon(),
                        w.getModel().getType().getTypeName(),
                        w.getMaxSpeedKmh()
                );
            }

            // Route info
            if (hasRoute) {
                PlannedRoute pr = routeOpt.get();

                System.out.println("Route type: " + (pr.isComplex() ? "COMPLEX" : "SIMPLE"));

                System.out.print("Route: ");
                for (int i = 0; i < pr.getFacilityPath().size(); i++) {
                    if (i > 0) System.out.print(" → ");
                    System.out.print(
                            facilityRepository
                                    .findById(pr.getFacilityPath().get(i))
                                    .getName()
                    );
                }
                System.out.println();

                System.out.println("Associated freights in this route: " + pr.getFreightIDs());
            }

            System.out.println();
        }
    }

    // =========================================================
    // PLAN ROUTE (MULTI-FREIGHT)
    // =========================================================
    private void planPendingFreights() {

        List<Freight> freights = new ArrayList<>(freightRepository.getAll());

        if (freights.isEmpty()) {
            System.out.println("\nNo pending freights available.");
            return;
        }

        System.out.println("\n=== PLAN ROUTE FOR PENDING FREIGHTS ===");

        for (int i = 0; i < freights.size(); i++) {
            Freight f = freights.get(i);
            System.out.printf(
                    "%d) Freight %d | %s → %s | %d wagons%n",
                    i + 1,
                    f.getId(),
                    f.getOrigin().getName(),
                    f.getDestination().getName(),
                    f.getWagons().size()
            );
        }

        System.out.println("0) Finish selection");

        // -------------------------------
        // Select multiple freights
        // -------------------------------
        List<Freight> selectedFreights = new ArrayList<>();
        System.out.println("\nSelect freights for this route (0 to finish):");

        while (true) {
            int opt = scanner.nextInt();
            if (opt == 0) break;

            if (opt < 1 || opt > freights.size()) {
                System.out.println("Invalid option.");
                continue;
            }

            Freight f = freights.get(opt - 1);

            if (!selectedFreights.contains(f)) {
                selectedFreights.add(f);
                System.out.println("Added freight " + f.getId());
            }
        }

        if (selectedFreights.isEmpty()) {
            System.out.println("No freights selected.");
            return;
        }

        // -------------------------------
        // Validation: same origin
        // -------------------------------
        int originId = selectedFreights.get(0).getOrigin().getId();

        boolean sameOrigin = selectedFreights.stream()
                .allMatch(f -> f.getOrigin().getId() == originId);

        if (!sameOrigin) {
            System.out.println("All selected freights must start at the same facility.");
            return;
        }

        // -------------------------------
        // Route builder (reuse USLP07 logic)
        // -------------------------------
        List<Integer> facilityPath = dispatcherUI.askUserForFacilityPath(
                originId,
                selectedFreights
        );

        if (facilityPath == null) {
            System.out.println("Route planning cancelled.");
            return;
        }

        PlannedRoute route = new PlannedRoute(
                nextRouteId++,
                selectedFreights.stream().map(Freight::getId).toList(),
                facilityPath
        );

        plannedRoutes.put(route.getId(), route);

        System.out.println("\nRoute planned successfully!");
        System.out.println("Route type: " + (route.isComplex() ? "COMPLEX" : "SIMPLE"));
        System.out.println("Associated freights: " + route.getFreightIDs());
    }
}
