package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.UpgradePlanController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.service.UpgradePlanResult;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.RailwayNetworkCsvLoader;

import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Graph;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.MapGraph;        // <- ajusta se o teu nome for diferente
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Algorithms;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Edge;

import java.util.*;
import java.util.stream.Collectors;

public class UpgradePlanUI {

    private final UpgradePlanController controller;
    private final Scanner scanner;

    public UpgradePlanUI() {
        this.controller = new UpgradePlanController();
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        int option;

        do {
            System.out.println("\n=== USEI11 - Directed Line Upgrade Plan ===");
            System.out.println("1. Calculate Upgrade Plan (using CSV data)");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            while (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.print("Select an option: ");
            }

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> calculateUpgradePlanFromCsv();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option.");
            }

        } while (option != 0);
    }

    private void calculateUpgradePlanFromCsv() {
        try {
            String stationsPath = "train_station_dataset/stations.csv";
            String linesPath = "train_station_dataset/lines.csv";

            RailwayNetworkCsvLoader loader =
                    new RailwayNetworkCsvLoader(stationsPath, linesPath);

            List<NetworkNode> nodes = loader.loadNodes();
            List<NetworkEdge> edges = loader.loadEdges(nodes);

            // Build graph locally to allow cycle printing as paths
            Graph<NetworkNode, NetworkEdge> g = buildGraph(nodes, edges);

            UpgradePlanResult result =
                    controller.calculateUpgradePlan(nodes, edges);

            displayResults(result, g);

        } catch (Exception e) {
            System.out.println("Error processing CSV data: " + e.getMessage());
        }
    }

    private Graph<NetworkNode, NetworkEdge> buildGraph(List<NetworkNode> nodes, List<NetworkEdge> edges) {
        Graph<NetworkNode, NetworkEdge> g = new MapGraph<>(true); // directed

        for (NetworkNode n : nodes) {
            g.addVertex(n);
        }

        // Need a quick map id->node for adding edges using node objects
        Map<String, NetworkNode> byId = nodes.stream()
                .collect(Collectors.toMap(NetworkNode::getNodeId, x -> x));

        for (NetworkEdge e : edges) {
            NetworkNode from = byId.get(e.getFromNodeId());
            NetworkNode to = byId.get(e.getToNodeId());
            if (from != null && to != null) {
                g.addEdge(from, to, e);
            }
        }

        return g;
    }

    private void displayResults(UpgradePlanResult result,
                                Graph<NetworkNode, NetworkEdge> g) {

        System.out.println("\n══════════════════════════════════════════════════════════════");
        System.out.println(" USEI11 – DIRECTED LINE UPGRADE PLAN");
        System.out.println("══════════════════════════════════════════════════════════════");

        System.out.println("\n⏳ Computing upgrade plan...");
        System.out.println("   • Cycle detection using Colored DFS");
        System.out.println("   • Topological ordering if acyclic");

        System.out.println("\n──────────────────────────────────────────────────────────────");
        System.out.println(" RESULTS");
        System.out.println("──────────────────────────────────────────────────────────────");

        if (result.hasCycle()) {

            System.out.println("\n⚠ GRAPH CONTAINS CYCLES");
            System.out.println("The railway network has circular dependencies.");
            System.out.println("Upgrade cannot proceed until these cycles are resolved.");

            List<List<NetworkNode>> cycles = Algorithms.findDirectedCycles(g);

            System.out.println("\nSummary:");
            System.out.println("  • Total cycles detected: " + cycles.size());
            System.out.println("  • Unique stations in cycles: " + result.getCycleNodes().size());

            int maxCycles = 10;
            int shown = Math.min(maxCycles, cycles.size());

            System.out.println("\n──────────────────────────────────────────────────────────────");
            System.out.println(" CYCLES (showing " + shown + " of " + cycles.size() + ")");
            System.out.println("──────────────────────────────────────────────────────────────");

            for (int i = 0; i < shown; i++) {
                List<NetworkNode> cycle = cycles.get(i);

                System.out.println("\nCycle " + (i + 1));
                System.out.println("  Stations:");
                System.out.print("    ");

                for (int j = 0; j < cycle.size(); j++) {
                    System.out.print(cycle.get(j).getName());
                    if (j < cycle.size() - 1) System.out.print(" → ");
                }
                System.out.println();

                System.out.println("  Links:");
                for (int j = 0; j < cycle.size() - 1; j++) {
                    NetworkNode a = cycle.get(j);
                    NetworkNode b = cycle.get(j + 1);

                    Edge<NetworkNode, NetworkEdge> edge = g.edge(a, b);
                    if (edge != null) {
                        NetworkEdge e = edge.getWeight();
                        System.out.printf(
                                "    - %s → %s | dist=%6.2f km | cap=%2d | cost=%6.2f%n",
                                e.getFromNodeId(),
                                e.getToNodeId(),
                                e.getDistanceKm(),
                                e.getCapacity(),
                                e.getCost()
                        );
                    }
                }
            }

            if (cycles.size() > maxCycles) {
                System.out.println("\n    ... and " + (cycles.size() - maxCycles)
                        + " additional cycles not shown");
            }

            System.out.println("\n──────────────────────────────────────────────────────────────");
            System.out.println(" STATIONS INVOLVED IN CYCLES (" + result.getCycleNodes().size() + ")");
            System.out.println("──────────────────────────────────────────────────────────────");

            result.getCycleNodes().stream()
                    .sorted(Comparator.comparing(NetworkNode::getName,
                            String.CASE_INSENSITIVE_ORDER))
                    .forEach(n ->
                            System.out.println("  • " + n.getName()
                                    + " (" + n.getNodeId() + ")"));

            System.out.println("\n──────────────────────────────────────────────────────────────");
            System.out.println(" LINKS INVOLVED IN CYCLES (" + result.getCycleEdges().size() + ")");
            System.out.println("──────────────────────────────────────────────────────────────");

            result.getCycleEdges().stream()
                    .sorted(Comparator.comparing(NetworkEdge::getFromNodeId)
                            .thenComparing(NetworkEdge::getToNodeId))
                    .forEach(e ->
                            System.out.printf(
                                    "  • %s → %s | dist=%6.2f km | cap=%2d | cost=%6.2f%n",
                                    e.getFromNodeId(),
                                    e.getToNodeId(),
                                    e.getDistanceKm(),
                                    e.getCapacity(),
                                    e.getCost()
                            ));

        } else {

            System.out.println("\n✔ GRAPH IS ACYCLIC");
            System.out.println("A valid upgrade order exists.");

            System.out.println("\nUpgrade order:");
            int i = 1;
            for (NetworkNode n : result.getTopologicalOrder()) {
                System.out.println("  " + (i++) + ". " + n.getName()
                        + " (" + n.getNodeId() + ")");
            }
        }

        System.out.println("\n──────────────────────────────────────────────────────────────");
        System.out.println(" COMPLEXITY ANALYSIS");
        System.out.println("──────────────────────────────────────────────────────────────");
        System.out.println(result.getComplexityAnalysis());

        System.out.println("══════════════════════════════════════════════════════════════");
    }

}
