package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;
import pt.ipp.isep.dei.pi25_26.g093.app.service.UpgradePlanService;
import pt.ipp.isep.dei.pi25_26.g093.app.service.UpgradePlanResult;

import java.util.List;

/**
 * Controller for USEI11 - Directed Line Upgrade Plan.
 * 
 * <p>This controller mediates between the UI layer and the service layer,
 * handling upgrade plan calculations for railway network infrastructure.
 * 
 * <p>The controller coordinates:
 * <ul>
 *   <li>Topological sorting of nodes for upgrade order</li>
 *   <li>Cycle detection in the dependency graph</li>
 *   <li>Complexity analysis</li>
 * </ul>
 * 
 * @author Logistics on Rails Team
 * @see UpgradePlanService
 */
public class UpgradePlanController {
    
    private final UpgradePlanService service;
    
    /**
     * Constructs a new controller with the default service instance.
     */
    public UpgradePlanController() {
        this.service = new UpgradePlanService();
    }
    
    /**
     * Constructs a new controller with the specified service.
     * 
     * @param service the UpgradePlanService to use. Must not be null.
     */
    public UpgradePlanController(UpgradePlanService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.service = service;
    }
    
    /**
     * Calculates the upgrade plan for the given network.
     * 
     * <p>This method delegates to the service layer to:
     * <ul>
     *   <li>Detect cycles in the dependency graph</li>
     *   <li>Perform topological sort if no cycles exist</li>
     *   <li>Generate complexity analysis</li>
     * </ul>
     * 
     * @param nodes list of network nodes (stations, terminals, freight yards)
     * @param edges list of directed edges representing dependencies
     * @return UpgradePlanResult containing either:
     *         <ul>
     *           <li>Topological ordering of nodes (if acyclic)</li>
     *           <li>Cycle information (if cycles detected)</li>
     *         </ul>
     * @throws IllegalArgumentException if nodes or edges are invalid
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


