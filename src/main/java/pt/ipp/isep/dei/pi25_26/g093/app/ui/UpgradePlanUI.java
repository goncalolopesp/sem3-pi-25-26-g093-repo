package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.UpgradePlanController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;
import pt.ipp.isep.dei.pi25_26.g093.app.service.UpgradePlanResult;

import java.util.*;

/**
 * User Interface for USEI11 - Directed Line Upgrade Plan.
 *
 * <p>This UI allows infrastructure planners to input a directed railway
 * dependency network and compute a valid upgrade order or detect cycles.
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
     * @throws IllegalArgumentException if controller is null
     */
    public UpgradePlanUI(UpgradePlanController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("Controller cannot be null");
        }
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the main menu and handles user interaction.
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

    private void displayHeader() {
        System.out.println("\n=== USEI11 - Directed Line Upgrade Plan ===");
        System.out.println("1. Calculate Upgrade Plan");
        System.out.println("2. Show Example");
        System.out.println("0. Back to Main Menu");
        System.out.println("-------------------------------------------");
        System.out.print("Select an option: ");
    }

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

    private void calculateUpgradePlan() {
        System.out.println("\n--- Calculate Upgrade Plan ---");

        try {
            List<NetworkNode> nodes = inputNodes();
            if (nodes.isEmpty()) {
                System.out.println("No nodes entered. Returning to menu.");
                return;
            }

            List<NetworkEdge> edges = inputEdges(nodes);

            System.out.println("\nCalculating upgrade plan...");
            UpgradePlanResult result =
                    controller.calculateUpgradePlan(nodes, edges);

            displayResults(result);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error occurred. Please try again.");
        }
    }

    private List<NetworkNode> inputNodes() {
        List<NetworkNode> nodes = new ArrayList<>();
        Set<String> usedIds = new HashSet<>();

        System.out.println("\nEnter network nodes:");
        System.out.println("Format: nodeId|name|latitude|longitude|svgX|svgY");
        System.out.println("Enter 'done' when finished");

        while (true) {
            System.out.print("Node: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) break;
            if (input.isEmpty()) continue;

            try {
                NetworkNode node = parseNode(input);

                if (usedIds.contains(node.getNodeId())) {
                    throw new IllegalArgumentException("Duplicate nodeId");
                }

                nodes.add(node);
                usedIds.add(node.getNodeId());
                System.out.println("  Added: " + node);

            } catch (Exception e) {
                System.out.println("  Error: " + e.getMessage());
            }
        }
        return nodes;
    }

    private NetworkNode parseNode(String input) {
        String[] parts = input.split("\\|");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Expected 6 fields separated by |");
        }

        return new NetworkNode(
                parts[0].trim(),
                parts[1].trim(),
                Double.parseDouble(parts[2].trim()),
                Double.parseDouble(parts[3].trim()),
                Double.parseDouble(parts[4].trim()),
                Double.parseDouble(parts[5].trim())
        );
    }

    private List<NetworkEdge> inputEdges(List<NetworkNode> nodes) {
        List<NetworkEdge> edges = new ArrayList<>();
        Set<String> nodeIds = new HashSet<>();

        for (NetworkNode node : nodes) {
            nodeIds.add(node.getNodeId());
        }

        System.out.println("\nEnter directed edges:");
        System.out.println("Format: fromNodeId|toNodeId|distanceKm|capacity|cost");
        System.out.println("Enter 'done' when finished");

        while (true) {
            System.out.print("Edge: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) break;
            if (input.isEmpty()) continue;

            try {
                NetworkEdge edge = parseEdge(input, nodeIds);
                edges.add(edge);
                System.out.println("  Added: " + edge);

            } catch (Exception e) {
                System.out.println("  Error: " + e.getMessage());
            }
        }
        return edges;
    }

    private NetworkEdge parseEdge(String input, Set<String> nodeIds) {
        String[] parts = input.split("\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Expected 5 fields separated by |");
        }

        if (!nodeIds.contains(parts[0].trim()) ||
                !nodeIds.contains(parts[1].trim())) {
            throw new IllegalArgumentException("Invalid nodeId reference");
        }

        return new NetworkEdge(
                parts[0].trim(),
                parts[1].trim(),
                Double.parseDouble(parts[2].trim()),
                Integer.parseInt(parts[3].trim()),
                Double.parseDouble(parts[4].trim())
        );
    }

    private void displayResults(UpgradePlanResult result) {
        System.out.println("\n=== Upgrade Plan Results ===");

        if (result.hasCycle()) {
            System.out.println("❌ Cycle detected");
            System.out.println("Cycle nodes:");
            result.getCycleNodes().forEach(
                    n -> System.out.println(" - " + n.getName())
            );
        } else {
            System.out.println("✅ Valid upgrade order:");
            int i = 1;
            for (NetworkNode node : result.getTopologicalOrder()) {
                System.out.println(i++ + ". " + node.getName());
            }
        }

        System.out.println("\n" + result.getComplexityAnalysis());
    }

    private void showExample() {
        System.out.println("\nExample:");
        System.out.println("A -> B -> C");
        System.out.println("Valid order: A, B, C");
        System.out.println("If C -> A is added, a cycle is detected.");
    }
}
