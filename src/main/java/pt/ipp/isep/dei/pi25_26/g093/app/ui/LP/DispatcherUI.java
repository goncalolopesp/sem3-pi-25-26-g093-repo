package pt.ipp.isep.dei.pi25_26.g093.app.ui.LP;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.database.*;
import pt.ipp.isep.dei.pi25_26.g093.app.service.RouteService;
import pt.ipp.isep.dei.pi25_26.g093.app.service.SchedulerService;
import pt.ipp.isep.dei.pi25_26.g093.app.service.TrainBuilderService;
import pt.ipp.isep.dei.pi25_26.g093.app.simulation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DispatcherUI {

    private final Scanner scanner = new Scanner(System.in);

    private LocalDateTime currentTime = LocalDateTime.now();

    private static final DateTimeFormatter DATE_TIME_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm");

    private final LocomotiveRepository locomotiveRepo = new LocomotiveRepository();
    private final FreightRepository freightRepo = new FreightRepository();
    private final RailwayNetworkRepository networkRepo = new RailwayNetworkRepository();
    private final RouteService routeService = new RouteService(networkRepo);
    private final FacilityRepository facilityRepository = new FacilityRepository();

    private final TrainBuilderService trainBuilderService = new TrainBuilderService();
    private final SimulationEngine simulationEngine = new SimulationEngine();
    private final SchedulerService schedulerService =
            new SchedulerService(trainBuilderService, simulationEngine);


    public void showMenu() {

        int option;

        do {
            System.out.println("==============================");
            System.out.println("         DISPATCHER MENU      ");
            System.out.println("==============================");
            System.out.println("1) Create & Schedule a Train (USPL07)");
            System.out.println("2) List all locomotives");
            System.out.println("3) List all freights");
            System.out.println("4) Advance scheduler time");
            System.out.println("5) List scheduled trains");
            System.out.println("6) Move locomotives between facilities");
            System.out.println("0) Exit");
            System.out.println("==============================");

            option = readInt("Choose an option: ");

            switch (option) {
                case 1 -> createAndScheduleTrain();
                case 2 -> listLocomotives();
                case 3 -> listFreights();
                case 4 -> advanceTime();
                case 5 -> listSchedules();
                case 6 -> moveLocomotives();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Try again.");
            }

        } while (option != 0);
    }


    private void createAndScheduleTrain() {

        System.out.println("\n====================");
        System.out.println("   DISPATCHER UI");
        System.out.println("====================");

        List<Locomotive> locomotives = chooseLocomotivesForTrain();
        if (locomotives.isEmpty()) {
            System.out.println("No locomotive selected. Aborting.");
            return;
        }

        int locoFacilityId = locomotives.get(0).getCurrentFacilityId();

        List<Freight> freights = chooseFreightsForTrain(locoFacilityId);

        if (freights.isEmpty()) {
            System.out.println("No freights selected. Aborting operation.");
            return;
        }

        boolean allSameOrigin = freights.stream()
                .allMatch(f -> f.getOrigin().getId() == locoFacilityId);

        if (!allSameOrigin) {
            System.out.println("Warning: some freights do not start at the locomotive's facility.");
            System.out.println("Currently, best supported scenario is all freights sharing the loco origin.");
        }

        List<RailwaySegment> route = null;

        while (true) {
            List<Integer> facilityPath = askUserForFacilityPath(locoFacilityId, freights);
            if (facilityPath == null) {
                System.out.println("Route definition aborted. Train will not be scheduled.");
                return;
            }

            try {
                route = routeService.buildManualRoute(facilityPath);
            } catch (IllegalStateException e) {
                System.out.println("Error building route: " + e.getMessage());
                return;
            }

            System.out.println("\nRoute found for all stops:");
            for (RailwaySegment seg : route) {
                RailwayLine line = seg.getRailwayLine();
                String lineName = (line != null ? line.getName() : "Unknown line");
                System.out.printf("%s | SEG %d (order %d) | %.0f m%n",
                        lineName,
                        seg.getId(),
                        seg.getOrderInLine(),
                        seg.getLengthMeters());
            }

            int reviewOption = reviewRoute(locomotives, freights, route);

            if (reviewOption == 1) {
                break;
            } else if (reviewOption == 2) {
                continue;
            } else if (reviewOption == 3) {
                System.out.println("Operation cancelled. Train will not be scheduled.");
                return;
            } else {
                System.out.println("Invalid option on route review. Cancelling.");
                return;
            }
        }

        LocalDateTime departure = askPlannedDeparture();

        TrainSchedule schedule =
                schedulerService.scheduleTrain(locomotives, freights, route, departure);

        for (Freight f : freights) {
            freightRepo.remove(f.getId());
        }

        Train train = schedule.getTrain();

        System.out.println("\nTrain scheduled successfully!");
        System.out.println("Train " + train.getNumber() + " includes:");
        System.out.println(" - Locomotives:");
        for (Locomotive loco : locomotives) {
            System.out.println("    * " + loco.getIdentifier());
        }

        System.out.println(" - Freights:");
        for (Freight f : freights) {
            System.out.println("    * " + f.getId() + " (" +
                    f.getOrigin().getName() + " → " + f.getDestination().getName() + ")");
        }
        System.out.println(" - Planned departure: " + departure.format(DATE_TIME_FMT));
        System.out.println("\nTrain will be simulated when scheduler time reaches departure time (option 4).");
    }

    private void listLocomotives() {

        System.out.println("\n=== LOCOMOTIVES ===");

        List<Locomotive> list = new ArrayList<>(locomotiveRepo.getAll());

        for (Locomotive loco : list) {

            Facility locFacility = networkRepo.findFacility(loco.getCurrentFacilityId());
            String status = loco.isAvailable() ? "AVAILABLE" : "IN USE";

            System.out.printf(
                    "ID: %d | Number: %s | Model: %s | Max speed: %.0f km/h | Location: %s | %s%n",
                    loco.getId(),
                    loco.getIdentifier(),
                    loco.getModelName(),
                    loco.getMaxSpeedKmh(),
                    (locFacility != null ? locFacility.getName() : "Unknown"),
                    status
            );
        }
    }


    private void listFreights() {

        System.out.println("\n=== FREIGHTS ===");

        for (Freight f : freightRepo.getAll()) {
            System.out.printf(
                    "Freight %d | %s → %s | %d wagons%n",
                    f.getId(),
                    f.getOrigin().getName(),
                    f.getDestination().getName(),
                    f.getWagons().size()
            );
        }
    }


    private void advanceTime() {

        System.out.println("\nCurrent scheduler time: " + currentTime.format(DATE_TIME_FMT));

        int minutes = 0;

        do {

            minutes = readInt("Advance how many minutes?\n");

            if(minutes < 0){

                System.out.println("Please insert a positive value.");
            }

        }while(minutes < 0);

        currentTime = currentTime.plusMinutes(minutes);
        System.out.println("New scheduler time: " + currentTime.format(DATE_TIME_FMT));

        schedulerService.processScheduler(currentTime);

        for (TrainSchedule s : schedulerService.listSchedules()) {
            if (s.getStatus() == TrainSchedule.Status.FINISHED &&
                    s.getResultTimeline() != null) {

                System.out.println("\n=== Train " + s.getTrain().getNumber() + " finished ===");
                printSimulation(s);


                List<Freight> freights = s.getFreights();
                if (!freights.isEmpty()) {
                    Freight last = freights.get(freights.size() - 1);
                    for (Locomotive loco : s.getTrain().getLocomotives()) {
                        loco.setCurrentFacilityId(last.getDestination().getId());
                    }
                    System.out.println("Locomotives now at: " + last.getDestination().getName());
                }
            }
        }
    }


    private void listSchedules() {

        System.out.println("\n=== SCHEDULED TRAINS ===");

        for (TrainSchedule s : schedulerService.listSchedules()) {
            LocalDateTime dep = s.getDepartureTime();
            System.out.printf(
                    "Train %s | Departure: %s | Status: %s%n",
                    s.getTrain().getNumber(),
                    dep.format(DATE_TIME_FMT),
                    s.getStatus()
            );
        }
    }

    public List<Locomotive> chooseLocomotivesForTrain() {

        System.out.println("\nAvailable locomotives / trains:");

        List<Locomotive> available = new ArrayList<>();
        for (Locomotive loco : locomotiveRepo.getAll()) {
            if (loco.isAvailable()) {
                available.add(loco);
            }
        }

        if (available.isEmpty()) {
            System.out.println("No available locomotives.");
            return List.of();
        }

        for (int i = 0; i < available.size(); i++) {
            Locomotive loco = available.get(i);
            Facility locFacility = networkRepo.findFacility(loco.getCurrentFacilityId());

            System.out.printf(
                    "%d) %s (%s, %.0f km/h) – at %s%n",
                    i + 1,
                    loco.getIdentifier(),
                    loco.getModelName(),
                    loco.getMaxSpeedKmh(),
                    (locFacility != null ? locFacility.getName() : "Unknown")
            );
        }

        int choice;
        do {
            choice = readInt("Choose first locomotive: ");
        } while (choice < 1 || choice > available.size());

        Locomotive first = available.get(choice - 1);
        int facilityId = first.getCurrentFacilityId();

        List<Locomotive> sameFac = new ArrayList<>();
        for (Locomotive loco : available) {
            if (loco.getId() != first.getId()
                    && loco.getCurrentFacilityId() == facilityId) {
                sameFac.add(loco);
            }
        }

        List<Locomotive> result = new ArrayList<>();
        result.add(first);

        if (!sameFac.isEmpty()) {
            System.out.println("\nOptional: choose a second locomotive at the same facility (or 0 to skip):");
            for (int i = 0; i < sameFac.size(); i++) {
                Locomotive loco = sameFac.get(i);
                System.out.printf(
                        "%d) %s (%s, %.0f km/h)%n",
                        i + 1,
                        loco.getIdentifier(),
                        loco.getModelName(),
                        loco.getMaxSpeedKmh()
                );
            }
            System.out.println("0) No second locomotive");

            int opt;
            do {
                opt = readInt("Choose option: ");
            } while (opt < 0 || opt > sameFac.size());

            if (opt > 0) {
                result.add(sameFac.get(opt - 1));
            }
        }

        return result;
    }


    public List<Freight> chooseFreightsForTrain(int facilityId) {

        List<Freight> selected = new ArrayList<>();

        Facility locFac = facilityRepository.findById(facilityId);
        String facName = (locFac != null ? locFac.getName() : ("ID " + facilityId));

        // lista local de freights disponíveis nesta facility
        List<Freight> availableAtFacility = new ArrayList<>();
        for (Freight f : freightRepo.getAll()) {
            if (f.getOrigin().getId() == facilityId) {
                availableAtFacility.add(f);
            }
        }

        while (true) {

            System.out.println("\nCurrent locomotive location: " + facName);

            if (availableAtFacility.isEmpty()) {
                System.out.println("No freights available at this facility.");
                break;
            }

            System.out.println("\nAvailable freights:");
            for (Freight f : availableAtFacility) {
                System.out.printf("%d) %s → %s (%d wagons)%n",
                        f.getId(),
                        f.getOrigin().getName(),
                        f.getDestination().getName(),
                        f.getWagons().size());
            }
            System.out.println("0) Finish selecting freights");

            int id = readInt("Choose freight ID: ");
            if (id == 0) break;

            Freight chosen = null;
            for (Freight f : availableAtFacility) {
                if (f.getId() == id) {
                    chosen = f;
                    break;
                }
            }

            if (chosen == null) {
                System.out.println("Invalid ID. Try again.");
                continue;
            }

            selected.add(chosen);
            availableAtFacility.remove(chosen);

            System.out.println("Freight added.");
        }

        return selected;
    }


    public List<Integer> askUserForFacilityPath(int originId, List<Freight> freights) {

        Facility origin = networkRepo.findFacility(originId);

        Set<Integer> mandatory = new LinkedHashSet<>();
        for (Freight f : freights) {
            mandatory.add(f.getDestination().getId());
        }

        System.out.println("\n=== ROUTE BUILDER ===");
        System.out.println("Starting facility: "
                + (origin != null ? origin.getName() : ("ID " + originId)));

        System.out.println("\nMandatory stops:");
        for (Integer id : mandatory) {
            Facility f = networkRepo.findFacility(id);
            System.out.println(" - " + (f != null ? f.getName() + " (ID " + id + ")" : ("ID " + id)));
        }

        Map<Integer, Set<Integer>> adjacency = buildAdjacencyMap();

        List<Integer> path = new ArrayList<>();
        path.add(originId);

        while (true) {

            System.out.print("\nCurrent route: ");
            for (int i = 0; i < path.size(); i++) {
                int fid = path.get(i);
                Facility f = networkRepo.findFacility(fid);
                String name = (f != null ? f.getName() : ("ID " + fid));
                if (i == 0) System.out.print(name);
                else System.out.print(" -> " + name);
            }
            System.out.println();

            Set<Integer> missing = new LinkedHashSet<>(mandatory);
            missing.removeAll(path);
            if (!missing.isEmpty()) {
                System.out.println("\nMandatory destinations still required:");
                for (Integer mid : missing) {
                    Facility f = networkRepo.findFacility(mid);
                    System.out.println(" - " + (f != null ? f.getName() + " (ID " + mid + ")" : ("ID " + mid)));
                }
            } else {
                System.out.println("\nAll mandatory destinations already included in route.");
            }

            int current = path.get(path.size() - 1);
            Set<Integer> neighbors = adjacency.getOrDefault(current, Set.of());

            System.out.println("\nChoose next stop (direct connections only):");
            List<Integer> options = new ArrayList<>();
            int idx = 1;
            for (Integer n : neighbors) {
                Facility f = networkRepo.findFacility(n);
                System.out.printf("%d) %s (ID %d)%n",
                        idx,
                        (f != null ? f.getName() : "Unknown"),
                        n);
                options.add(n);
                idx++;
            }
            System.out.println("0) Finish route");

            int opt = readInt("Choose option: ");
            if (opt == 0) {
                if (path.size() < 2) {
                    System.out.println("Route must have at least origin and one next facility.");
                    return null;
                }

                Set<Integer> visited = new HashSet<>(path);
                List<Integer> missingList = new ArrayList<>();
                for (Integer mid : mandatory) {
                    if (!visited.contains(mid)) {
                        missingList.add(mid);
                    }
                }

                if (!missingList.isEmpty()) {
                    System.out.println("\nRoute is missing mandatory destinations:");
                    for (Integer mid : missingList) {
                        Facility f = networkRepo.findFacility(mid);
                        System.out.println(" - " + (f != null ? f.getName() + " (ID " + mid + ")" : ("ID " + mid)));
                    }
                    System.out.println("Please redefine the route.");
                    return null;
                }

                return path;
            }

            if (opt < 0 || opt > options.size()) {
                System.out.println("Invalid option. Try again.");
                continue;
            }

            int nextId = options.get(opt - 1);
            path.add(nextId);
        }
    }

    private Map<Integer, Set<Integer>> buildAdjacencyMap() {
        Map<Integer, Set<Integer>> adjacency = new HashMap<>();

        for (RailwayLine line : networkRepo.getAllLines()) {
            int a = line.getStart().getId();
            int b = line.getEnd().getId();

            adjacency.computeIfAbsent(a, k -> new LinkedHashSet<>()).add(b);
            adjacency.computeIfAbsent(b, k -> new LinkedHashSet<>()).add(a);
        }

        return adjacency;
    }

    private int reviewRoute(List<Locomotive> locomotives,
                            List<Freight> freights,
                            List<RailwaySegment> route) {

        System.out.println("\n===============================");
        System.out.println("          ROUTE REVIEW");
        System.out.println("===============================");

        System.out.println("\nLocomotives:");
        for (Locomotive loco : locomotives) {
            System.out.printf(" - %s (%s, %.0f km/h, %.0f kW)%n",
                    loco.getIdentifier(),
                    loco.getModelName(),
                    loco.getMaxSpeedKmh(),
                    loco.getPowerKw());
        }

        System.out.println("\nFreights:");
        for (Freight f : freights) {
            System.out.printf(" - %d: %s → %s (%d wagons)%n",
                    f.getId(),
                    f.getOrigin().getName(),
                    f.getDestination().getName(),
                    f.getWagons().size());
        }

        double totalDistance = route.stream()
                .mapToDouble(RailwaySegment::getLengthMeters)
                .sum();

        Train previewTrain = trainBuilderService.buildTrain(
                locomotives,
                freights,
                currentTime.toLocalDate(),
                currentTime.toLocalTime()
        );

        SimulationState previewState = new SimulationState(
                previewTrain,
                route,
                currentTime
        );

        SimulationTimeline previewTimeline = simulationEngine.simulate(previewState);
        long totalSeconds = computeTotalSeconds(previewTimeline);

        double avgSpeed = (totalSeconds > 0)
                ? (totalDistance / 1000.0) / (totalSeconds / 3600.0)
                : 0.0;

        System.out.println("\nRoute summary:");
        System.out.printf(" - Total distance: %.1f km%n", totalDistance / 1000.0);
        System.out.printf(" - Estimated time: %02dm %02ds%n", totalSeconds / 60, totalSeconds % 60);
        System.out.printf(" - Average speed: %.1f km/h%n", avgSpeed);

        System.out.println("\nSegments by line:");
        Map<RailwayLine, List<RailwaySegment>> grouped = groupSegmentsByLine(route);

        for (Map.Entry<RailwayLine, List<RailwaySegment>> entry : grouped.entrySet()) {
            RailwayLine line = entry.getKey();
            String lineName = (line != null ? line.getName() : "Unknown line");
            System.out.println(lineName + ":");
            for (RailwaySegment seg : entry.getValue()) {
                System.out.printf("   * SEG %d (order %d) | %.0f m%n",
                        seg.getId(),
                        seg.getOrderInLine(),
                        seg.getLengthMeters());
            }
        }

        System.out.println("\n==========================================");
        System.out.println("1) Confirm and schedule train");
        System.out.println("2) Redefine route");
        System.out.println("3) Cancel operation");
        System.out.println("==========================================");

        return readInt("Choose an option: ");
    }

    private Map<RailwayLine, List<RailwaySegment>> groupSegmentsByLine(List<RailwaySegment> route) {
        Map<RailwayLine, List<RailwaySegment>> map = new LinkedHashMap<>();
        for (RailwaySegment seg : route) {
            RailwayLine line = seg.getRailwayLine();
            map.computeIfAbsent(line, k -> new ArrayList<>()).add(seg);
        }
        return map;
    }

    private long computeTotalSeconds(SimulationTimeline timeline) {
        timeline.sort();
        List<SimulationEvent> events = timeline.getEvents();

        if (events.isEmpty()) return 0;

        class SegmentInfo {
            LocalDateTime enter;
            LocalDateTime exit;
        }

        Map<Integer, SegmentInfo> infoMap = new HashMap<>();

        for (SimulationEvent e : events) {
            int segId = e.getSegment().getId();
            infoMap.putIfAbsent(segId, new SegmentInfo());
            SegmentInfo info = infoMap.get(segId);

            switch (e.getType()) {
                case ENTER_SEGMENT -> info.enter = e.getTime();
                case EXIT_SEGMENT -> info.exit = e.getTime();
                default -> {
                }
            }
        }

        long totalSeconds = 0;
        for (SegmentInfo s : infoMap.values()) {
            if (s.enter != null && s.exit != null) {
                totalSeconds += Duration.between(s.enter, s.exit).getSeconds();
            }
        }

        return totalSeconds;
    }

    private LocalDateTime askPlannedDeparture() {

        System.out.println("\nPlanned departure:");
        System.out.println("Current scheduler date: " + currentTime.toLocalDate());

        LocalDate date = null;
        while (date == null) {
            System.out.print("  Date (YYYY-MM-DD) [>= " + currentTime.toLocalDate() + "]: ");
            String input = scanner.nextLine().trim();
            try {
                LocalDate parsed = LocalDate.parse(input);
                if (parsed.isBefore(currentTime.toLocalDate())) {
                    System.out.println("Date cannot be in the past relative to scheduler clock.");
                } else {
                    date = parsed;
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Try again.");
            }
        }

        LocalTime time = null;
        while (time == null) {
            System.out.print("  Time (HH:MM): ");
            String input = scanner.nextLine().trim();
            try {
                time = LocalTime.parse(input, TIME_FMT);
            } catch (Exception e) {
                System.out.println("Invalid format. Try again (e.g. 07:30).");
            }
        }

        return LocalDateTime.of(date, time);
    }


    private void printSimulation(TrainSchedule schedule) {

        SimulationTimeline timeline = schedule.getResultTimeline();

        System.out.println("\n==============================");
        System.out.println("          TRAIN TIMETABLE");
        System.out.println("==============================");

        timeline.sort();
        List<SimulationEvent> events = timeline.getEvents();

        if (events.isEmpty()) {
            System.out.println("No simulation events recorded.");
            return;
        }

        List<RailwaySegment> route = schedule.getRoute();
        if (route.isEmpty()) {
            System.out.println("Empty route.");
            return;
        }

        List<Facility> facilityOrder = new ArrayList<>();

        RailwaySegment firstSeg = route.get(0);
        RailwayLine firstLine = firstSeg.getRailwayLine();

        Facility startA = networkRepo.findFacility(firstLine.getStart().getId());
        Facility startB = networkRepo.findFacility(firstLine.getEnd().getId());

        LocalDateTime firstEnter = findEnterTimeForSegment(events, firstSeg);
        Facility realStart = startA; // fallback
        if (startA != null && startB != null && firstEnter != null) {
            realStart = startA;
        }

        facilityOrder.add(realStart);
        Facility previous = realStart;

        for (RailwaySegment seg : route) {
            RailwayLine line = seg.getRailwayLine();
            Facility A = networkRepo.findFacility(line.getStart().getId());
            Facility B = networkRepo.findFacility(line.getEnd().getId());

            Facility next = (A != null && previous != null && A.getId() == previous.getId())
                    ? B
                    : A;

            facilityOrder.add(next);
            previous = next;
        }

        Map<Integer, Facility> segmentOriginFacility = new HashMap<>();
        for (int i = 0; i < route.size(); i++) {
            RailwaySegment seg = route.get(i);
            Facility originFac = facilityOrder.get(i);
            segmentOriginFacility.put(seg.getId(), originFac);
        }

        List<FacilityStop> stops = new ArrayList<>();

        Facility firstFac = facilityOrder.get(0);
        LocalDateTime firstTime = schedule.getDepartureTime();

        stops.add(new FacilityStop(firstFac, firstTime, firstTime, false, 0));

        for (int i = 1; i < facilityOrder.size(); i++) {
            Facility fac = facilityOrder.get(i);

            RailwaySegment seg = route.get(i - 1);
            LocalDateTime arrival = findExitTimeForSegment(events, seg);
            if (arrival == null) arrival = firstTime;

            stops.add(new FacilityStop(fac, arrival, arrival, false, 0));
        }

        for (Freight f : schedule.getFreights()) {
            Facility dest = f.getDestination();

            for (FacilityStop st : stops) {
                if (st.getFacility() != null && st.getFacility().getId() == dest.getId()) {
                    st.setUnloading(true);
                    st.setUnloadingFreights(st.getUnloadingFreights() + 1);
                    st.setDeparture(st.getArrival().plusMinutes(10));
                }
            }
        }

        Map<Integer, LocalDateTime> waitStartBySeg = new HashMap<>();
        Map<Integer, Long> waitSecondsByFacilityId = new HashMap<>();

        for (SimulationEvent e : events) {
            RailwaySegment seg = e.getSegment();
            if (seg == null) continue;

            int segId = seg.getId();

            if (e.getType() == SimulationEvent.Type.WAITING) {
                waitStartBySeg.put(segId, e.getTime());
            } else if (e.getType() == SimulationEvent.Type.ENTER_SEGMENT) {
                LocalDateTime start = waitStartBySeg.get(segId);
                if (start != null) {
                    long sec = Duration.between(start, e.getTime()).getSeconds();
                    Facility originFac = segmentOriginFacility.get(segId);
                    if (originFac != null) {
                        waitSecondsByFacilityId.merge(originFac.getId(), sec, Long::sum);
                    }
                    waitStartBySeg.remove(segId);
                }
            }
        }

        System.out.println("\nFACILITY TIMETABLE:");
        System.out.println("---------------------------------------------------------------------");
        System.out.printf("%-25s | %-16s | %-16s | %-25s%n",
                "Facility", "Arrival", "Departure", "Operation");
        System.out.println("---------------------------------------------------------------------");

        for (FacilityStop st : stops) {

            long waitSec = 0L;
            if (st.getFacility() != null) {
                waitSec = waitSecondsByFacilityId.getOrDefault(st.getFacility().getId(), 0L);
            }

            String op;

            String waitStr = null;
            if (waitSec > 0) {
                long min = waitSec / 60;
                long sec = waitSec % 60;
                if (sec == 0) {
                    waitStr = "WAIT (" + min + "m)";
                } else if (min == 0) {
                    waitStr = "WAIT (" + sec + "s)";
                } else {
                    waitStr = "WAIT (" + min + "m " + sec + "s)";
                }
            }

            if (st.isUnloading()) {
                if (waitStr != null) {
                    op = "UNLOAD (" + st.getUnloadingFreights() + ") + " + waitStr;
                } else {
                    op = "UNLOAD (" + st.getUnloadingFreights() + ")";
                }
            } else {
                if (waitStr != null) {
                    op = waitStr;
                } else {
                    op = "Pass-through";
                }
            }

            System.out.printf("%-25s | %-16s | %-16s | %-25s%n",
                    st.getFacility() != null ? st.getFacility().getName() : "Unknown",
                    st.getArrival().format(DATE_TIME_FMT),
                    st.getDeparture().format(DATE_TIME_FMT),
                    op
            );
        }

        System.out.println("---------------------------------------------------------------------\n");
    }



    private int lastIndexOfLine(List<RailwaySegment> route, RailwayLine line) {
        for (int i = route.size() - 1; i >= 0; i--) {
            if (route.get(i).getRailwayLine().getId() == line.getId())
                return i;
        }
        return 0;
    }

    private LocalDateTime findEnterTimeForSegment(List<SimulationEvent> events, RailwaySegment seg) {
        for (SimulationEvent e : events) {
            if (e.getSegment().getId() == seg.getId() &&
                    e.getType() == SimulationEvent.Type.ENTER_SEGMENT)
                return e.getTime();
        }
        return null;
    }

    private LocalDateTime findExitTimeForSegment(List<SimulationEvent> events, RailwaySegment seg) {
        for (SimulationEvent e : events) {
            if (e.getSegment().getId() == seg.getId() &&
                    e.getType() == SimulationEvent.Type.EXIT_SEGMENT)
                return e.getTime();
        }
        return null;
    }

    private List<LocalDateTime> allEnterTimes(List<SimulationEvent> events,
                                              RailwayLine line,
                                              Facility fac) {
        List<LocalDateTime> list = new ArrayList<>();

        for (SimulationEvent e : events) {
            if (e.getType() == SimulationEvent.Type.ENTER_SEGMENT &&
                    e.getSegment().getRailwayLine().getId() == line.getId()) {

                RailwaySegment seg = e.getSegment();
                Facility A = facilityRepository.findById(seg.getRailwayLine().getStart().getId());

                if (A != null && A.getId() == fac.getId())
                    list.add(e.getTime());
            }
        }

        return list;
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.nextLine();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return value;
    }

    private void moveLocomotives() {

        System.out.println("\n=== LOCOMOTIVE TRANSFER MODE ===");

        Locomotive first = chooseAvailableLocomotiveForTransfer();
        if (first == null) {
            System.out.println("No locomotive selected. Cancelling.");
            return;
        }

        int originId = first.getCurrentFacilityId();
        Facility origin = networkRepo.findFacility(originId);

        Locomotive second = chooseSecondLocomotiveForTransfer(first);

        List<Locomotive> locos =
                (second == null ?
                        List.of(first) :
                        List.of(first, second));

        Facility destination = chooseDestinationFacility(originId);
        if (destination == null) {
            System.out.println("Transfer cancelled.");
            return;
        }

        List<RailwaySegment> route;
        try {
            route = routeService.buildRouteBetweenFacilities(originId, destination.getId());
        } catch (Exception e) {
            System.out.println("No route found: " + e.getMessage());
            return;
        }

        double speed = locos.stream()
                .mapToDouble(Locomotive::getMaxSpeedKmh)
                .min()
                .orElse(40);

        double distance = route.stream().mapToDouble(RailwaySegment::getLengthMeters).sum();
        int minutes = (int)Math.round((distance/1000.0)/speed * 60);

        System.out.println("\n=== TRANSFER SUMMARY ===");
        System.out.println("From: " + origin.getName());
        System.out.println("To:   " + destination.getName());
        System.out.println("Total distance: " + (distance/1000.0) + " km");
        System.out.println("Estimated travel time: " + minutes + " minutes");

        System.out.print("Confirm transfer (y/n)? ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("Cancelled.");
            return;
        }

        for (Locomotive loco : locos) {
            loco.setCurrentFacilityId(destination.getId());
        }

        System.out.println("Locomotives moved successfully!");
    }

    private Locomotive chooseAvailableLocomotiveForTransfer() {

        List<Locomotive> available = locomotiveRepo.getAll()
                .stream()
                .filter(Locomotive::isAvailable)
                .toList();

        if (available.isEmpty()) {
            System.out.println("No available locomotives.");
            return null;
        }

        System.out.println("\nAvailable locomotives:");
        for (int i = 0; i < available.size(); i++) {
            Locomotive loco = available.get(i);
            Facility fac = networkRepo.findFacility(loco.getCurrentFacilityId());
            System.out.printf("%d) %s (%s) – at %s%n",
                    i+1, loco.getIdentifier(), loco.getModelName(), fac.getName());
        }

        int opt = readInt("Choose locomotive: ");
        if (opt < 1 || opt > available.size()) return null;

        return available.get(opt-1);
    }


    private Locomotive chooseSecondLocomotiveForTransfer(Locomotive first) {

        int facId = first.getCurrentFacilityId();

        List<Locomotive> options = locomotiveRepo.getAll()
                .stream()
                .filter(Locomotive::isAvailable)
                .filter(l -> l.getId() != first.getId())
                .filter(l -> l.getCurrentFacilityId() == facId)
                .toList();

        if (options.isEmpty()) return null;

        System.out.println("\nOptional second locomotive at " +
                networkRepo.findFacility(facId).getName() + ":");
        for (int i = 0; i < options.size(); i++) {
            Locomotive l = options.get(i);
            System.out.printf("%d) %s (%s)%n", i+1, l.getIdentifier(), l.getModelName());
        }
        System.out.println("0) No second locomotive");

        int opt = readInt("Choose: ");
        if (opt == 0) return null;
        if (opt < 1 || opt > options.size()) return null;

        return options.get(opt-1);
    }

    private Facility chooseDestinationFacility(int originId) {

        Collection<Facility> all = networkRepo.getAllFacilities();

        List<Facility> options = all.stream()
                .filter(f -> f.getId() != originId)
                .toList();

        System.out.println("\nAvailable destination facilities:");
        for (int i = 0; i < options.size(); i++) {
            Facility f = options.get(i);
            System.out.printf("%d) %s%n", i+1, f.getName());
        }
        System.out.println("0) Cancel");

        int opt = readInt("Choose facility: ");
        if (opt == 0) return null;
        if (opt < 1 || opt > options.size()) return null;

        return options.get(opt-1);
    }
}
