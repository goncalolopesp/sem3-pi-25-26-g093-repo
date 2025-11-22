package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.database.RailwayNetworkRepository;

import java.util.*;

public class RouteService {

    private final RailwayNetworkRepository network;

    public RouteService(RailwayNetworkRepository network) {
        this.network = network;
    }

    /**
     * Mantém para compatibilidade: rota automática entre origem e destino de um freight.
     */
    public List<RailwaySegment> buildRoute(Freight freight) {
        int origin = freight.getOrigin().getId();
        int destination = freight.getDestination().getId();
        return buildRouteBetweenFacilities(origin, destination);
    }

    /**
     * Multi-stop automático antigo (continua disponível se quiseres usar noutro lado),
     * mas agora reusa buildRouteBetweenFacilities.
     */
    public List<RailwaySegment> buildMultiStopRoute(List<Freight> freights) {

        if (freights == null || freights.isEmpty()) {
            return List.of();
        }

        List<RailwaySegment> fullRoute = new ArrayList<>();

        for (Freight f : freights) {
            List<RailwaySegment> partial = buildRoute(f);
            fullRoute.addAll(partial);
        }

        return fullRoute;
    }

    /**
     * Rota entre duas facilities (origem/destino), via BFS sobre RailwayLine.
     */
    public List<RailwaySegment> buildRouteBetweenFacilities(int originFacilityId,
                                                            int destinationFacilityId) {

        if (originFacilityId == destinationFacilityId) {
            return List.of(); // já lá está
        }

        List<RailwayLine> usedLines = findLinePath(originFacilityId, destinationFacilityId);

        List<RailwaySegment> route = new ArrayList<>();
        for (RailwayLine line : usedLines) {
            route.addAll(line.getSegments()); // os segmentos da linha já vêm ordenados
        }

        return route;
    }

    /**
     * Constrói uma rota completa com base numa lista de facilities (path manual do utilizador).
     *
     * Exemplo: [Leixões, Contumil, Nine, Barcelos, Darque]
     * -> concatena rota(Leixões→Contumil) + rota(Contumil→Nine) + ...
     */
    public List<RailwaySegment> buildManualRoute(List<Integer> facilityPath) {

        if (facilityPath == null || facilityPath.size() < 2) {
            throw new IllegalArgumentException("Route must contain at least origin and destination facility.");
        }

        List<RailwaySegment> fullRoute = new ArrayList<>();

        for (int i = 0; i < facilityPath.size() - 1; i++) {
            int origin = facilityPath.get(i);
            int dest = facilityPath.get(i + 1);

            List<RailwaySegment> partial = buildRouteBetweenFacilities(origin, dest);
            fullRoute.addAll(partial);
        }

        return fullRoute;
    }

    // ================================================================
    // BFS interno: facilities -> lista de RailwayLine que ligam origem a destino
    // ================================================================
    private List<RailwayLine> findLinePath(int origin, int destination) {

        Map<Integer, RailwayLine> parent = new HashMap<>();
        Queue<Integer> queue = new ArrayDeque<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(origin);
        visited.add(origin);

        while (!queue.isEmpty()) {

            int current = queue.poll();

            if (current == destination) {
                break;
            }

            for (RailwayLine line : network.getAllLines()) {

                int a = line.getStart().getId();
                int b = line.getEnd().getId();

                int next = -1;

                if (a == current) next = b;
                else if (b == current) next = a;

                if (next != -1 && !visited.contains(next)) {
                    visited.add(next);
                    parent.put(next, line);
                    queue.add(next);
                }
            }
        }

        if (!visited.contains(destination)) {
            Facility o = network.findFacility(origin);
            Facility d = network.findFacility(destination);

            String oName = (o != null ? o.getName() : String.valueOf(origin));
            String dName = (d != null ? d.getName() : String.valueOf(destination));

            throw new IllegalStateException(
                    "No route found between " + oName + " and " + dName + "."
            );
        }

        List<RailwayLine> usedLines = new ArrayList<>();
        int node = destination;

        while (node != origin) {
            RailwayLine line = parent.get(node);
            usedLines.add(line);

            int a = line.getStart().getId();
            int b = line.getEnd().getId();

            node = (node == a ? b : a);
        }

        Collections.reverse(usedLines);
        return usedLines;
    }
}
