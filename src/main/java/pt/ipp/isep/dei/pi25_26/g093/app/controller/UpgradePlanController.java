package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;
import pt.ipp.isep.dei.pi25_26.g093.app.service.UpgradePlanService;
import pt.ipp.isep.dei.pi25_26.g093.app.service.UpgradePlanResult;

import java.util.List;

/**
 * Controller for USEI11 – Directed Line Upgrade Plan.
 *
 * <p>This controller acts as an intermediary between the user interface
 * and the service layer. It is responsible for validating input data
 * and delegating the upgrade plan calculation to the business logic.
 *
 * <p>The controller does not implement graph algorithms or business rules;
 * these responsibilities are fully delegated to {@link UpgradePlanService}.
 *
 * @author Logistics on Rails Team
 * @see UpgradePlanService
 */
public class UpgradePlanController {

    private final UpgradePlanService service;

    /**
     * Constructs a new controller with a default {@link UpgradePlanService}.
     */
    public UpgradePlanController() {
        this.service = new UpgradePlanService();
    }

    /**
     * Constructs a new controller with the specified service.
     *
     * @param service the {@link UpgradePlanService} to use
     * @throws IllegalArgumentException if the service is {@code null}
     */
    public UpgradePlanController(UpgradePlanService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.service = service;
    }

    /**
     * Calculates the upgrade plan for a directed railway network.
     *
     * <p>This method validates the input data and delegates the calculation
     * to the service layer, which performs cycle detection and, when possible,
     * computes a valid topological upgrade order.
     *
     * @param nodes list of network nodes (stations, terminals, freight yards)
     * @param edges list of directed dependency edges
     * @return an {@link UpgradePlanResult} containing either:
     *         <ul>
     *           <li>a valid topological ordering of nodes, or</li>
     *           <li>information about detected cycles</li>
     *         </ul>
     * @throws IllegalArgumentException if the nodes list is null or empty,
     *         or if the edges list is null
     */
    public UpgradePlanResult calculateUpgradePlan(List<NetworkNode> nodes,
                                                  List<NetworkEdge> edges) {

        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("Nodes list cannot be null or empty");
        }
        if (edges == null) {
            throw new IllegalArgumentException("Edges list cannot be null");
        }

        return service.calculateUpgradePlan(nodes, edges);
    }
}
