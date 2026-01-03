package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.MinimalBackboneController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Edge;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Graph;

import java.util.Scanner;

public class MinimalBackboneUI implements Runnable {
    private final MinimalBackboneController controller = new MinimalBackboneController();
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        int option;
        do {
            System.out.println("\n===== Minimal Backbone Network (USEI12) =====");
            System.out.println("1. Compute and Show Minimal Backbone Network Graph");
            System.out.println("2. Generate DOT and SVG Files");
            System.out.println("0. Return to Main Menu");
            System.out.print("Select an option: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    showMinimalBackbone();
                    break;
                case 2:
                    generateFiles();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (option != 0);
    }

    private void showMinimalBackbone() {
        // We call the logic via the controller
        Graph<CompleteStation, Double> mst = controller.getMinimalBackboneGraph();
        if (mst == null || mst.numVertices() == 0) {
            System.out.println("No network data available.");
            return;
        }

        System.out.println("\n--- Minimal Backbone Network ---");
        double totalLength = 0;
        for (Edge<CompleteStation, Double> edge : mst.edges()) {
            // To avoid printing undirected edges twice
            if (edge.getVOrig().getStation().compareTo(edge.getVDest().getStation()) < 0) {
                System.out.printf("%s <-> %s | Length: %.2f km\n",
                        edge.getVOrig().getStation(), edge.getVDest().getStation(), edge.getWeight());
                totalLength += edge.getWeight();
            }
        }
        System.out.printf("Total Backbone length: %.2f km\n", totalLength);
    }

    private void generateFiles() {
        System.out.print("Enter the base name for the files (e.g., 'backbone'): ");
        String filename = scanner.next();

        String file = "generated-graphs/" + filename;
        boolean success = controller.generateBackbone(file);

        if (success) {
            System.out.println("Successfully generated " + filename + ".dot and " + filename + ".svg");
        } else {
            System.out.println("Failed to generate files. Check if 'neato' is installed and data is loaded.");
        }
    }
}