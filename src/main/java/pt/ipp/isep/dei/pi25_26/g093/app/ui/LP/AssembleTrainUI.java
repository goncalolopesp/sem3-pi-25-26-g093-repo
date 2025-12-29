package pt.ipp.isep.dei.pi25_26.g093.app.ui.LP;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.database.*;
import pt.ipp.isep.dei.pi25_26.g093.app.service.RouteService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

public class AssembleTrainUI {

    private final Scanner scanner = new Scanner(System.in);

    private final LocomotiveRepository locomotiveRepo = new LocomotiveRepository();
    private final FreightRepository freightRepo = new FreightRepository();
    private final TrainRepository trainRepo = new TrainRepository();
    private final RailwayNetworkRepository networkRepo = new RailwayNetworkRepository();
    private final FacilityRepository facilityRepo = new FacilityRepository();
    private final RouteService routeService = new RouteService(networkRepo);

    // Trains criados nesta US (em memória)
    private final List<Train> assembledTrains = new ArrayList<>();
    private int nextTrainId = 1000;

    /* ===================================================== */
    /* MENU                                                  */
    /* ===================================================== */

    public void showMenu() {

        int opt;
        do {
            System.out.println("\n=== USLP09 – Train Assembly ===");
            System.out.println("1) Build & assign a new train");
            System.out.println("2) Assign route to a pre-existing train");
            System.out.println("3) List locomotives");
            System.out.println("4) List freights");
            System.out.println("5) List assigned trains");
            System.out.println("0) Exit");

            opt = readInt("Choose option: ");

            switch (opt) {
                case 1 -> buildAndAssignTrain();
                case 2 -> assignRouteToExistingTrain();
                case 3 -> listLocomotives();
                case 4 -> listFreights();
                case 5 -> listAssignedTrains();
                case 0 -> System.out.println("Leaving USLP09...");
                default -> System.out.println("Invalid option.");
            }

        } while (opt != 0);
    }

    /* ===================================================== */
    /* OPTION 1 – BUILD & ASSIGN NEW TRAIN                   */
    /* ===================================================== */

    private void buildAndAssignTrain() {

        List<Locomotive> locos = chooseLocomotivesForTrain();
        if (locos.isEmpty()) return;

        int originId = locos.get(0).getCurrentFacilityId();

        List<Freight> freights = chooseFreightsForTrain(originId);
        if (freights.isEmpty()) return;

        List<Integer> path = askUserForFacilityPath(originId, freights);
        if (path == null) return;

        List<RailwaySegment> route;
        try {
            route = routeService.buildManualRoute(path);
        } catch (Exception e) {
            System.out.println("Error building route: " + e.getMessage());
            return;
        }

        Train train = new Train(
                nextTrainId,
                "T" + nextTrainId,
                locos,
                freights,
                LocalDate.now(),
                LocalTime.now()
        );
        nextTrainId++;

        train.setRoute(route);
        assembledTrains.add(train);

        System.out.println("Train " + train.getNumber() + " assembled and assigned.");
    }

    /* ===================================================== */
    /* OPTION 2 – ASSIGN ROUTE TO EXISTING TRAIN             */
    /* ===================================================== */

    private void assignRouteToExistingTrain() {

        List<Train> trains = new ArrayList<>(trainRepo.getAll());

        if (trains.isEmpty()) {
            System.out.println("No pre-existing trains.");
            return;
        }

        System.out.println("\n=== PRE-EXISTING TRAINS ===");
        System.out.println("\n=== PRE-EXISTING TRAINS ===");

        for (int i = 0; i < trains.size(); i++) {
            Train t = trains.get(i);

            System.out.println(
                    (i + 1) + ") Train " + t.getNumber() + " | " +
                            ((t.getRoute() == null || t.getRoute().isEmpty())
                                    ? "NO ROUTE"
                                    : "ROUTE ASSIGNED")
            );

            // ---------- LOCOMOTIVES ----------
            System.out.println("   Locomotives:");
            for (Locomotive l : t.getLocomotives()) {
                Facility f = networkRepo.findFacility(l.getCurrentFacilityId());
                System.out.printf(
                        "    - %s | %s | %.0f kW | %.0f km/h | at %s%n",
                        l.getIdentifier(),
                        l.getModelName(),
                        l.getPowerKw(),
                        l.getMaxSpeedKmh(),
                        f != null ? f.getName() : "Unknown"
                );
            }

            // ---------- FREIGHTS ----------
            System.out.println("   Freights:");
            for (Freight f : t.getFreights()) {
                System.out.printf(
                        "    - Freight %d | %s → %s | %d wagons%n",
                        f.getId(),
                        f.getOrigin().getName(),
                        f.getDestination().getName(),
                        f.getWagons().size()
                );

                // detalhe de carga (USLP08 style)
                for (WagonReal w : f.getWagons()) {
                    System.out.printf(
                            "       * %s | Cargo: %s | Max load: %.1f t%n",
                            w.getIdentifier(),
                            w.getModel().getType().getTypeName(),
                            w.getMaxLoadTon()
                    );
                }
            }

            System.out.println();
        }


        int opt = readInt("Choose train (0 to cancel): ");
        if (opt == 0 || opt > trains.size()) return;

        Train selectedTrain = trains.get(opt - 1);

// -------- VALIDATIONS --------

        if (selectedTrain.getLocomotives().isEmpty()) {
            System.out.println(
                    "This train has no locomotives assigned. " +
                            "Cannot assign a route."
            );
            return;
        }

        if (selectedTrain.getFreights().isEmpty()) {
            System.out.println(
                    "This train has no freights assigned. " +
                            "Cannot assign a route."
            );
            return;
        }

// -------- SHOW DETAILS --------

        showTrainDetails(selectedTrain);

        System.out.print("Press ENTER to continue...");
        scanner.nextLine();

// -------- SAFE NOW --------

        int originId = selectedTrain.getLocomotives()
                .get(0)
                .getCurrentFacilityId();


        List<Integer> path = askUserForFacilityPath(
                originId,
                selectedTrain.getFreights()
        );
        if (path == null) return;

        List<RailwaySegment> route;
        try {
            route = routeService.buildManualRoute(path);
        } catch (Exception e) {
            System.out.println("Error building route: " + e.getMessage());
            return;
        }

        selectedTrain.setRoute(route);
        System.out.println("Route assigned to train " + selectedTrain.getNumber());
    }

    /* ===================================================== */
    /* TRAIN DETAILS (PRE-EXISTING TRAIN)                    */
    /* ===================================================== */

    private void showTrainDetails(Train train) {

        System.out.println("\n=== TRAIN DETAILS ===");
        System.out.println("Train " + train.getNumber());

        System.out.println("\nLocomotives:");
        for (Locomotive l : train.getLocomotives()) {
            Facility f = networkRepo.findFacility(l.getCurrentFacilityId());
            System.out.printf(
                    " - %s (%s) at %s%n",
                    l.getIdentifier(),
                    l.getModelName(),
                    f != null ? f.getName() : "Unknown"
            );
        }

        System.out.println("\nFreights:");
        for (Freight f : train.getFreights()) {
            System.out.printf(
                    " - Freight %d | %s → %s | %d wagons%n",
                    f.getId(),
                    f.getOrigin().getName(),
                    f.getDestination().getName(),
                    f.getWagons().size()
            );

            for (WagonReal w : f.getWagons()) {
                System.out.printf(
                        "    * %s | Cargo: %s | Max load: %.1f t%n",
                        w.getIdentifier(),
                        w.getModel().getType().getTypeName(),
                        w.getMaxLoadTon()
                );
            }
        }

        System.out.println();
    }

    /* ===================================================== */
    /* LIST LOCOMOTIVES                                     */
    /* ===================================================== */

    private void listLocomotives() {

        System.out.println("\n=== LOCOMOTIVES ===");

        for (Locomotive l : locomotiveRepo.getAll()) {

            Train usingTrain = assignedTrains()
                    .filter(t -> t.getLocomotives().contains(l))
                    .findFirst()
                    .orElse(null);

            Facility f = networkRepo.findFacility(l.getCurrentFacilityId());

            if (usingTrain == null) {
                System.out.printf(
                        "%s | %s | at %s | FREE%n",
                        l.getIdentifier(),
                        l.getModelName(),
                        f != null ? f.getName() : "Unknown"
                );
            } else {
                System.out.printf(
                        "%s | %s | IN USE (Train %s)%n",
                        l.getIdentifier(),
                        l.getModelName(),
                        usingTrain.getNumber()
                );
            }
        }
    }

    /* ===================================================== */
    /* LIST FREIGHTS                                        */
    /* ===================================================== */

    private void listFreights() {

        System.out.println("\n=== FREIGHTS ===");

        for (Freight f : freightRepo.getAll()) {

            Train usingTrain = assignedTrains()
                    .filter(t -> t.getFreights().contains(f))
                    .findFirst()
                    .orElse(null);

            if (usingTrain == null) {
                System.out.printf(
                        "Freight %d | %s → %s | FREE%n",
                        f.getId(),
                        f.getOrigin().getName(),
                        f.getDestination().getName()
                );
            } else {
                System.out.printf(
                        "Freight %d | %s → %s | IN USE (Train %s)%n",
                        f.getId(),
                        f.getOrigin().getName(),
                        f.getDestination().getName(),
                        usingTrain.getNumber()
                );
            }
        }
    }

    /* ===================================================== */
    /* LIST ASSIGNED TRAINS                                  */
    /* ===================================================== */

    private void listAssignedTrains() {

        System.out.println("\n=== ASSIGNED TRAINS ===");

        List<Train> list = assignedTrains().toList();
        if (list.isEmpty()) {
            System.out.println("No assigned trains.");
            return;
        }

        for (Train t : list) {
            System.out.println("Train " + t.getNumber());

            System.out.println(" Locomotives:");
            t.getLocomotives()
                    .forEach(l -> System.out.println("  - " + l.getIdentifier()));

            System.out.println(" Freights:");
            t.getFreights()
                    .forEach(f -> System.out.println("  - Freight " + f.getId()));

            System.out.print(" Route: ");

            List<RailwaySegment> route = t.getRoute();
            if (route == null || route.isEmpty()) {
                System.out.println("No route");
            } else {

                List<String> facilities = new ArrayList<>();
                Set<Integer> visited = new HashSet<>(); // Para evitar repetir facilities

                for (RailwaySegment seg : route) {
                    RailwayLine line = seg.getRailwayLine();
                    Facility startFacility = networkRepo.findFacility(line.getStart().getId());
                    Facility endFacility = networkRepo.findFacility(line.getEnd().getId());

                    if (startFacility != null && !visited.contains(startFacility.getId())) {
                        facilities.add(startFacility.getName());
                        visited.add(startFacility.getId());
                    }

                    if (endFacility != null && !visited.contains(endFacility.getId())) {
                        facilities.add(endFacility.getName());
                        visited.add(endFacility.getId());
                    }
                }

                System.out.println(String.join(" → ", facilities));
            }

            System.out.println();
        }
    }




    /* ===================================================== */
    /* HELPERS                                              */
    /* ===================================================== */

    private Stream<Train> assignedTrains() {
        return Stream.concat(
                trainRepo.getAll().stream(),
                assembledTrains.stream()
        ).filter(t -> t.getRoute() != null && !t.getRoute().isEmpty());
    }


    private List<Integer> askUserForFacilityPath(int originId, List<Freight> freights) {

        Facility origin = networkRepo.findFacility(originId);
        Set<Integer> mandatory = new LinkedHashSet<>();
        for (Freight f : freights) {
            mandatory.add(f.getDestination().getId());
        }

        System.out.println("\n=== ROUTE BUILDER ===");
        System.out.println("Starting facility: " + (origin != null ? origin.getName() : originId));

        System.out.println("Mandatory destinations:");
        for (Integer id : mandatory) {
            Facility f = networkRepo.findFacility(id);
            System.out.println(" - " + (f != null ? f.getName() : id));
        }

        Map<Integer, Set<Integer>> adjacency = buildAdjacencyMap();

        List<Integer> path = new ArrayList<>();
        List<String> selectedFacilities = new ArrayList<>(); // Lista para armazenar as facilities selecionadas

        path.add(originId);
        selectedFacilities.add(origin != null ? origin.getName() : String.valueOf(originId)); // Adiciona o primeiro destino

        while (true) {
            System.out.print("\nCurrent route:");
            for (int i = 0; i < path.size(); i++) {
                Facility f = networkRepo.findFacility(path.get(i));
                if (f != null) System.out.print(" → " + f.getName());
                else System.out.print(" → Unknown");
            }
            System.out.println();

            int current = path.get(path.size() - 1);
            List<Integer> options = new ArrayList<>(adjacency.getOrDefault(current, Set.of()));

            for (int i = 0; i < options.size(); i++) {
                Facility f = networkRepo.findFacility(options.get(i));
                System.out.printf("%d) %s%n", i + 1, f != null ? f.getName() : options.get(i));
            }
            System.out.println("0) Finish route");

            int opt = readInt("Choose option: ");
            if (opt == 0) break;
            if (opt < 1 || opt > options.size()) continue;

            // Adiciona a próxima facility, se não estiver já na lista
            int nextId = options.get(opt - 1);
            path.add(nextId);
            Facility nextFacility = networkRepo.findFacility(nextId);

            if (nextFacility != null && !selectedFacilities.contains(nextFacility.getName())) {
                selectedFacilities.add(nextFacility.getName()); // Evita repetição de facilities
            }
        }

        // Exibe a sequência de facilities selecionadas
        System.out.println("\nSelected Route (Facilities): " + String.join(" → ", selectedFacilities));

        return path;
    }


    private Map<Integer, Set<Integer>> buildAdjacencyMap() {

        Map<Integer, Set<Integer>> map = new HashMap<>();

        for (RailwayLine line : networkRepo.getAllLines()) {
            int a = line.getStart().getId();
            int b = line.getEnd().getId();

            map.computeIfAbsent(a, k -> new LinkedHashSet<>()).add(b);
            map.computeIfAbsent(b, k -> new LinkedHashSet<>()).add(a);
        }
        return map;
    }

    private List<Locomotive> chooseLocomotivesForTrain() {

        List<Locomotive> available =
                new ArrayList<>(locomotiveRepo.getAll());

        if (available.isEmpty()) {
            System.out.println("No locomotives available.");
            return List.of();
        }

        for (int i = 0; i < available.size(); i++) {
            Locomotive l = available.get(i);
            Facility f = networkRepo.findFacility(l.getCurrentFacilityId());
            System.out.printf(
                    "%d) %s (%s) at %s%n",
                    i + 1,
                    l.getIdentifier(),
                    l.getModelName(),
                    f != null ? f.getName() : "Unknown"
            );
        }

        int opt = readInt("Choose first locomotive: ");
        if (opt < 1 || opt > available.size()) return List.of();

        Locomotive first = available.get(opt - 1);
        int facId = first.getCurrentFacilityId();

        List<Locomotive> sameFac = available.stream()
                .filter(l -> l.getId() != first.getId())
                .filter(l -> l.getCurrentFacilityId() == facId)
                .toList();

        List<Locomotive> result = new ArrayList<>();
        result.add(first);

        if (!sameFac.isEmpty()) {
            System.out.println("Optional second locomotive:");
            for (int i = 0; i < sameFac.size(); i++) {
                System.out.printf("%d) %s%n", i + 1, sameFac.get(i).getIdentifier());
            }
            System.out.println("0) No second locomotive");

            int opt2 = readInt("Choose option: ");
            if (opt2 > 0 && opt2 <= sameFac.size()) {
                result.add(sameFac.get(opt2 - 1));
            }
        }

        return result;
    }

    private List<Freight> chooseFreightsForTrain(int facilityId) {

        List<Freight> available = freightRepo.getAll().stream()
                .filter(f -> f.getOrigin().getId() == facilityId)
                .toList();

        List<Freight> selected = new ArrayList<>();

        while (true) {

            if (available.isEmpty()) break;

            for (Freight f : available) {
                System.out.printf("%d) %s → %s%n",
                        f.getId(),
                        f.getOrigin().getName(),
                        f.getDestination().getName());
            }
            System.out.println("0) Finish selecting freights");

            int id = readInt("Choose freight ID: ");
            if (id == 0) break;

            Freight chosen = available.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (chosen == null) continue;

            selected.add(chosen);
            available = available.stream()
                    .filter(f -> f.getId() != id)
                    .toList();
        }

        return selected;
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Enter a number: ");
        }
        int v = scanner.nextInt();
        scanner.nextLine();
        return v;
    }
}
