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
    public List<RailwaySegment> buildRouteBetweenFacilities(int originFacilityId, int destinationFacilityId) {
        if (originFacilityId == destinationFacilityId) {
            return List.of(); // Já estamos na origem
        }

        // Encontra o caminho das linhas
        List<RailwayLine> usedLines = findLinePath(originFacilityId, destinationFacilityId);

        List<RailwaySegment> route = new ArrayList<>();
        // Para cada linha encontrada no caminho, adicionamos seus segmentos na ordem
        for (RailwayLine line : usedLines) {
            route.addAll(line.getSegments());
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
        Set<Integer> visitedFacilities = new LinkedHashSet<>();  // Usamos LinkedHashSet para garantir ordem

        // Construção da rota com repetição permitida
        for (int i = 0; i < facilityPath.size() - 1; i++) {
            int origin = facilityPath.get(i);
            int dest = facilityPath.get(i + 1);

            // Adiciona as facilities visitadas ao conjunto de paradas
            visitedFacilities.add(origin);

            // Não adicionamos a última facility para evitar duplicação
            if (i == facilityPath.size() - 2) {
                visitedFacilities.add(dest);
            }

            List<RailwaySegment> partialRoute = buildRouteBetweenFacilities(origin, dest);
            fullRoute.addAll(partialRoute);
        }

        // Mostrar as facilities em ordem para o usuário
        System.out.println("\nCurrent route:");
        for (Integer facilityId : visitedFacilities) {
            Facility f = network.findFacility(facilityId);
            System.out.println(f != null ? f.getName() : "Unknown Facility");
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
        visited.add(origin);
        queue.add(origin);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (current == destination) {
                break; // Já encontramos o destino
            }

            // Percorre as linhas e encontra os próximos pontos conectados
            for (RailwayLine line : network.getAllLines()) {
                int a = line.getStart().getId();
                int b = line.getEnd().getId();
                int next = -1;

                if (a == current) next = b;
                else if (b == current) next = a;

                // Se a estação ainda não foi visitada, adicionamos ao caminho
                if (next != -1 && !visited.contains(next)) {
                    visited.add(next); // Marca a estação como visitada
                    parent.put(next, line);
                    queue.add(next);
                }
            }
        }

        // Se não encontramos o destino, lançamos uma exceção
        if (!visited.contains(destination)) {
            throw new IllegalStateException("No route found between " +
                    network.findFacility(origin).getName() + " and " +
                    network.findFacility(destination).getName() + ".");
        }

        // Reconstruímos a sequência de linhas percorridas até o destino
        List<RailwayLine> usedLines = new ArrayList<>();
        int node = destination;

        while (node != origin) {
            RailwayLine line = parent.get(node);
            usedLines.add(line);

            // Passa para o próximo nó até chegar à origem
            int a = line.getStart().getId();
            int b = line.getEnd().getId();
            node = (node == a ? b : a);
        }

        // Inverte as linhas para obter a sequência correta
        Collections.reverse(usedLines);
        return usedLines;
    }

}
