package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.UpgradePlanController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;
import pt.ipp.isep.dei.pi25_26.g093.app.service.UpgradePlanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * User Interface for USEI11 - Directed Line Upgrade Plan.
 * 
 * <p>This UI allows infrastructure planners to:
 * <ul>
 *   <li>Input network nodes (stations, terminals, freight yards)</li>
 *   <li>Input directed edges with dependencies</li>
 *   <li>Calculate upgrade order using topological sort</li>
 *   <li>Detect cycles in the dependency graph</li>
 *   <li>View complexity analysis</li>
 * </ul>
 * 
 * @author Logistics on Rails Team
 */
public class UpgradePlanUI {
    
    private final UpgradePlanController controller;
    private final Scanner scanner;
    
    /**
     * Constructs a new UI with the default controller.
     */
    public UpgradePlanUI() {
        this.controller = new UpgradePlanController();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Constructs a new UI with the specified controller.
     * 
     * @param controller the UpgradePlanController to use
     */
    public UpgradePlanUI(UpgradePlanController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Displays the main menu and handles user interactions.
     */
    public void showMenu() {
        int option;
        
        do {
            displayHeader();
            option = readOption();
            
            switch (option) {
                case 1 -> calculateUpgradePlan();
                case 2 -> showExample();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Please try again.");
            }
            
            if (option != 0) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
            
        } while (option != 0);
    }
    
    /**
     * Displays the menu header.
     */
    private void displayHeader() {
        System.out.println("\n=== USEI11 - Directed Line Upgrade Plan ===");
        System.out.println("1. Calculate Upgrade Plan");
        System.out.println("2. Show Example");
        System.out.println("0. Back to Main Menu");
        System.out.println("-------------------------------------------");
        System.out.print("Select an option: ");
    }
    
    /**
     * Reads and validates user option.
     * 
     * @return selected option number
     */
    private int readOption() {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.nextLine();
            System.out.print("Select an option: ");
        }
        int option = scanner.nextInt();
        scanner.nextLine();
        return option;
    }
    
    /**
     * Main workflow for calculating upgrade plan.
     */
    private void calculateUpgradePlan() {
        System.out.println("\n--- Calculate Upgrade Plan ---");
        
        try {
            // Input nodes
            List<NetworkNode> nodes = inputNodes();
            if (nodes.isEmpty()) {
                System.out.println("No nodes entered. Returning to menu.");
                return;
            }
            
            // Input edges
            List<NetworkEdge> edges = inputEdges(nodes);
            
            // Calculate upgrade plan
            System.out.println("\nCalculating upgrade plan...");
            UpgradePlanResult result = controller.calculateUpgradePlan(nodes, edges);
            
            // Display results
            displayResults(result);
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Inputs network nodes from the user.
     * 
     * @return list of NetworkNode objects
     */
    private List<NetworkNode> inputNodes() {
        List<NetworkNode> nodes = new ArrayList<>();
        
        System.out.println("\nEnter network nodes (stations, terminals, freight yards):");
        System.out.println("Format: nodeId|name|latitude|longitude|svgX|svgY");
        System.out.println("Enter 'done' when finished");
        System.out.println("Example: S1|Station1|50.5|4.35|100|200");
        
        while (true) {
            System.out.print("Node: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("done")) {
                break;
            }
            
            if (input.isEmpty()) {
                continue;
            }
            
            try {
                NetworkNode node = parseNode(input);
                nodes.add(node);
                System.out.println("  Added: " + node);
            } catch (Exception e) {
                System.out.println("  Error parsing node: " + e.getMessage());
                System.out.println("  Format: nodeId|name|latitude|longitude|svgX|svgY");
            }
        }
        
        return nodes;
    }
    
    /**
     * Parses a node string into a NetworkNode object.
     * 
     * @param input input string in format: nodeId|name|latitude|longitude|svgX|svgY
     * @return NetworkNode object
     */
    private NetworkNode parseNode(String input) {
        String[] parts = input.split("\\|");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid format. Expected 6 fields separated by |");
        }
        
        String nodeId = parts[0].trim();
        String name = parts[1].trim();
        double latitude = Double.parseDouble(parts[2].trim());
        double longitude = Double.parseDouble(parts[3].trim());
        double svgX = Double.parseDouble(parts[4].trim());
        double svgY = Double.parseDouble(parts[5].trim());
        
        return new NetworkNode(nodeId, name, latitude, longitude, svgX, svgY);
    }
    
    /**
     * Inputs network edges from the user.
     * 
     * @param nodes list of valid nodes (for validation)
     * @return list of NetworkEdge objects
     */
    private List<NetworkEdge> inputEdges(List<NetworkNode> nodes) {
        List<NetworkEdge> edges = new ArrayList<>();
        
        // Build node ID set for validation
        java.util.Set<String> nodeIds = new java.util.HashSet<>();
        for (NetworkNode node : nodes) {
            nodeIds.add(node.getNodeId());
        }
        
        System.out.println("\nEnter directed edges (dependencies):");
        System.out.println("Format: fromNodeId|toNodeId|distanceKm|capacity|cost");
        System.out.println("Enter 'done' when finished");
        System.out.println("Example: S1|S2|15.5|50|-100.0");
        System.out.println("Note: A -> B means A must be upgraded before B");
        
        while (true) {
            System.out.print("Edge: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("done")) {
                break;
            }
            
            if (input.isEmpty()) {
                continue;
            }
            
            try {
                NetworkEdge edge = parseEdge(input, nodeIds);
                edges.add(edge);
                System.out.println("  Added: " + edge);
            } catch (Exception e) {
                System.out.println("  Error parsing edge: " + e.getMessage());
                System.out.println("  Format: fromNodeId|toNodeId|distanceKm|capacity|cost");
            }
        }
        
        return edges;
    }
    
    /**
     * Parses an edge string into a NetworkEdge object.
     * 
     * @param input input string in format: fromNodeId|toNodeId|distanceKm|capacity|cost
     * @param nodeIds set of valid node IDs for validation
     * @return NetworkEdge object
     */
    private NetworkEdge parseEdge(String input, java.util.Set<String> nodeIds) {
        String[] parts = input.split("\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid format. Expected 5 fields separated by |");
        }
        
        String fromNodeId = parts[0].trim();
        String toNodeId = parts[1].trim();
        double distanceKm = Double.parseDouble(parts[2].trim());
        int capacity = Integer.parseInt(parts[3].trim());
        double cost = Double.parseDouble(parts[4].trim());
        
        if (!nodeIds.contains(fromNodeId)) {
            throw new IllegalArgumentException("From node ID '" + fromNodeId + "' not found in nodes");
        }
        if (!nodeIds.contains(toNodeId)) {
            throw new IllegalArgumentException("To node ID '" + toNodeId + "' not found in nodes");
        }
        
        return new NetworkEdge(fromNodeId, toNodeId, distanceKm, capacity, cost);
    }
    
    /**
     * Displays the upgrade plan results.
     * 
     * @param result UpgradePlanResult to display
     */
    private void displayResults(UpgradePlanResult result) {
        System.out.println("\n=== Upgrade Plan Results ===");
        
        if (result.hasCycle()) {
            System.out.println("\n❌ CYCLE DETECTED - Topological ordering is impossible");
            System.out.println("\nCycle Nodes:");
            List<NetworkNode> cycleNodes = result.getCycleNodes();
            for (int i = 0; i < cycleNodes.size(); i++) {
                NetworkNode node = cycleNodes.get(i);
                System.out.printf("  %d. %s (%s)%n", i + 1, node.getName(), node.getNodeId());
            }
            
            System.out.println("\nCycle Edges:");
            List<NetworkEdge> cycleEdges = result.getCycleEdges();
            for (int i = 0; i < cycleEdges.size(); i++) {
                NetworkEdge edge = cycleEdges.get(i);
                System.out.printf("  %d. %s -> %s (%.2f km, capacity: %d, cost: %.2f)%n",
                    i + 1, edge.getFromNodeId(), edge.getToNodeId(),
                    edge.getDistanceKm(), edge.getCapacity(), edge.getCost());
            }
            
            System.out.println("\nCycle Path:");
            for (int i = 0; i < cycleNodes.size(); i++) {
                System.out.print(cycleNodes.get(i).getNodeId());
                if (i < cycleNodes.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println(" -> " + cycleNodes.get(0).getNodeId());
            
        } else {
            System.out.println("\n✅ VALID TOPOLOGICAL ORDERING");
            System.out.println("\nUpgrade Order (respecting all dependencies):");
            List<NetworkNode> order = result.getTopologicalOrder();
            for (int i = 0; i < order.size(); i++) {
                NetworkNode node = order.get(i);
                System.out.printf("  %d. %s (%s)%n", i + 1, node.getName(), node.getNodeId());
            }
        }
        
        System.out.println("\n" + result.getComplexityAnalysis());
    }
    
    /**
     * Shows an example usage scenario.
     */
    private void showExample() {
        System.out.println("\n--- Example: Belgian Railway Network ---");
        System.out.println("\nExample Network:");
        System.out.println("  Nodes:");
        System.out.println("    S1 - Brussels Central");
        System.out.println("    S2 - Antwerp Central");
        System.out.println("    S3 - Ghent");
        System.out.println("    S4 - Bruges");
        System.out.println("\n  Edges (Dependencies):");
        System.out.println("    S1 -> S2 (Brussels must be upgraded before Antwerp)");
        System.out.println("    S2 -> S3 (Antwerp must be upgraded before Ghent)");
        System.out.println("    S3 -> S4 (Ghent must be upgraded before Bruges)");
        System.out.println("\n  Expected Result: [S1, S2, S3, S4]");
        System.out.println("\n  If we add S4 -> S1, a cycle is created:");
        System.out.println("    Cycle: S1 -> S2 -> S3 -> S4 -> S1");
        
        System.out.println("\n--- Example with Cycle ---");
        System.out.println("\nExample Network:");
        System.out.println("  Nodes: S1, S2, S5");
        System.out.println("  Edges:");
        System.out.println("    S2 -> S5");
        System.out.println("    S5 -> S2");
        System.out.println("\n  Result: Cycle detected: S2 -> S5 -> S2");
    }
}


