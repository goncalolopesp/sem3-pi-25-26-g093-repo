package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.database.*;
import pt.ipp.isep.dei.pi25_26.g093.app.service.RouteService;
import pt.ipp.isep.dei.pi25_26.g093.app.service.TrainBuilderService;
import pt.ipp.isep.dei.pi25_26.g093.app.simulation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public abstract class TrainAssemblyService {

    /* ================== SHARED STATE ================== */

    protected final Scanner scanner = new Scanner(System.in);

    protected final LocomotiveRepository locomotiveRepo = new LocomotiveRepository();
    protected final FreightRepository freightRepo = new FreightRepository();
    protected final RailwayNetworkRepository networkRepo = new RailwayNetworkRepository();
    protected final FacilityRepository facilityRepository = new FacilityRepository();
    protected final RouteService routeService = new RouteService(networkRepo);

    /* ================== LOCOMOTIVES ================== */

    protected List<Locomotive> chooseLocomotivesForTrain() {

        System.out.println("\nAvailable locomotives:");

        List<Locomotive> available = locomotiveRepo.getAll()
                .stream()
                .filter(Locomotive::isAvailable)
                .toList();

        if (available.isEmpty()) {
            System.out.println("No available locomotives.");
            return List.of();
        }

        for (int i = 0; i < available.size(); i++) {
            Locomotive loco = available.get(i);
            Facility fac = networkRepo.findFacility(loco.getCurrentFacilityId());

            System.out.printf(
                    "%d) %s (%s, %.0f km/h) – at %s%n",
                    i + 1,
                    loco.getIdentifier(),
                    loco.getModelName(),
                    loco.getMaxSpeedKmh(),
                    fac != null ? fac.getName() : "Unknown"
            );
        }

        int choice;
        do {
            choice = readInt("Choose first locomotive: ");
        } while (choice < 1 || choice > available.size());

        Locomotive first = available.get(choice - 1);
        int facilityId = first.getCurrentFacilityId();

        List<Locomotive> sameFacility = available.stream()
                .filter(l -> l.getId() != first.getId())
                .filter(l -> l.getCurrentFacilityId() == facilityId)
                .toList();

        List<Locomotive> result = new ArrayList<>();
        result.add(first);

        if (!sameFacility.isEmpty()) {
            System.out.println("\nOptional second locomotive:");
            for (int i = 0; i < sameFacility.size(); i++) {
                Locomotive l = sameFacility.get(i);
                System.out.printf("%d) %s (%s)%n",
                        i + 1, l.getIdentifier(), l.getModelName());
            }
            System.out.println("0) No second locomotive");

            int opt = readInt("Choose option: ");
            if (opt > 0 && opt <= sameFacility.size()) {
                result.add(sameFacility.get(opt - 1));
            }
        }

        return result;
    }

    /* ================== FREIGHTS ================== */

    protected List<Freight> chooseFreightsForTrain(int facilityId) {

        List<Freight> selected = new ArrayList<>();

        Facility fac = facilityRepository.findById(facilityId);
        String facName = fac != null ? fac.getName() : ("ID " + facilityId);

        List<Freight> available = freightRepo.getAll()
                .stream()
                .filter(f -> f.getOrigin().getId() == facilityId)
                .toList();

        while (true) {

            System.out.println("\nCurrent locomotive location: " + facName);

            if (available.isEmpty()) {
                System.out.println("No freights available at this facility.");
                break;
            }

            for (Freight f : available) {
                System.out.printf("%d) %s → %s (%d wagons)%n",
                        f.getId(),
                        f.getOrigin().getName(),
                        f.getDestination().getName(),
                        f.getWagons().size());
            }

            System.out.println("0) Finish selecting freights");
            int id = readInt("Choose freight ID: ");
            if (id == 0) break;

            Freight chosen = available.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (chosen == null) {
                System.out.println("Invalid ID.");
                continue;
            }

            selected.add(chosen);
            available = available.stream()
                    .filter(f -> f.getId() != id)
                    .toList();

            System.out.println("Freight added.");
        }

        return selected;
    }

    /* ================== ROUTE BUILDER ================== */

    protected List<Integer> askUserForFacilityPath(
            int originId,
            List<Freight> freights
    ) {

        Facility origin = networkRepo.findFacility(originId);

        Set<Integer> mandatory = new LinkedHashSet<>();
        for (Freight f : freights) {
            mandatory.add(f.getDestination().getId());
        }

        System.out.println("\n=== ROUTE BUILDER ===");
        System.out.println("Starting facility: " +
                (origin != null ? origin.getName() : originId));

        System.out.println("\nMandatory stops:");
        for (Integer id : mandatory) {
            Facility f = networkRepo.findFacility(id);
            System.out.println(" - " + (f != null ? f.getName() : id));
        }

        Map<Integer, Set<Integer>> adjacency = buildAdjacencyMap();
        List<Integer> path = new ArrayList<>();
        path.add(originId);

        while (true) {

            System.out.print("\nCurrent route: ");
            path.forEach(id -> {
                Facility f = networkRepo.findFacility(id);
                System.out.print(" -> " + (f != null ? f.getName() : id));
            });
            System.out.println();

            int current = path.get(path.size() - 1);
            Set<Integer> neighbors = adjacency.getOrDefault(current, Set.of());

            List<Integer> options = new ArrayList<>(neighbors);

            for (int i = 0; i < options.size(); i++) {
                Facility f = networkRepo.findFacility(options.get(i));
                System.out.printf("%d) %s%n", i + 1,
                        f != null ? f.getName() : options.get(i));
            }
            System.out.println("0) Finish route");

            int opt = readInt("Choose option: ");
            if (opt == 0) break;
            if (opt < 1 || opt > options.size()) continue;

            path.add(options.get(opt - 1));
        }

        if (!path.containsAll(mandatory)) {
            System.out.println("Route missing mandatory destinations.");
            return null;
        }

        return path;
    }

    protected Map<Integer, Set<Integer>> buildAdjacencyMap() {

        Map<Integer, Set<Integer>> map = new HashMap<>();

        for (RailwayLine line : networkRepo.getAllLines()) {
            int a = line.getStart().getId();
            int b = line.getEnd().getId();

            map.computeIfAbsent(a, k -> new LinkedHashSet<>()).add(b);
            map.computeIfAbsent(b, k -> new LinkedHashSet<>()).add(a);
        }

        return map;
    }

    /* ================== ROUTE REVIEW ================== */

    protected int reviewRoute(
            List<Locomotive> locomotives,
            List<Freight> freights,
            List<RailwaySegment> route,
            LocalDateTime previewTime,
            TrainBuilderService trainBuilder,
            SimulationEngine simulationEngine
    ) {

        double distance = route.stream()
                .mapToDouble(RailwaySegment::getLengthMeters)
                .sum();

        Train previewTrain = trainBuilder.buildTrain(
                locomotives,
                freights,
                previewTime.toLocalDate(),
                previewTime.toLocalTime()
        );

        SimulationTimeline timeline = simulationEngine.simulate(
                new SimulationState(previewTrain, route, previewTime)
        );

        long seconds = computeTotalSeconds(timeline);

        System.out.printf(
                "Distance: %.1f km | Time: %dm %ds%n",
                distance / 1000,
                seconds / 60,
                seconds % 60
        );

        System.out.println("1) Confirm");
        System.out.println("2) Redefine route");
        System.out.println("3) Cancel");

        return readInt("Choose option: ");
    }

    protected long computeTotalSeconds(SimulationTimeline timeline) {

        timeline.sort();

        Map<Integer, LocalDateTime> enter = new HashMap<>();
        long total = 0;

        for (SimulationEvent e : timeline.getEvents()) {
            int id = e.getSegment().getId();

            if (e.getType() == SimulationEvent.Type.ENTER_SEGMENT) {
                enter.put(id, e.getTime());
            }
            if (e.getType() == SimulationEvent.Type.EXIT_SEGMENT) {
                LocalDateTime start = enter.get(id);
                if (start != null) {
                    total += Duration.between(start, e.getTime()).getSeconds();
                }
            }
        }
        return total;
    }

    /* ================== UTIL ================== */

    protected int readInt(String prompt) {
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
