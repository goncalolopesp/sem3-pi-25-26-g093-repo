package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.PlannedRoute;

import java.util.*;

public class PlannedRouteRepository {

    private final Map<Integer, PlannedRoute> routes = new LinkedHashMap<>();
    private int nextID = 1;

    public PlannedRoute save(List<Integer> freightIds, List<Integer> facilityPath) {
        PlannedRoute pr = new PlannedRoute(nextID++, freightIds, facilityPath);
        routes.put(pr.getId(), pr);
        return pr;
    }

    public Collection<PlannedRoute> getAll() {
        return routes.values();
    }

    public Optional<PlannedRoute> findById(int id) {
        return Optional.ofNullable(routes.get(id));
    }

    public Optional<PlannedRoute> findByFreightId(int freightId) {
        return routes.values().stream()
                .filter(r -> r.getFreightIDs().contains(freightId))
                .findFirst();
    }}
