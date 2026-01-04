package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.MaxFlowController;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Graph;

import java.util.Scanner;

public class MaxFlowUI implements Runnable {

    private final MaxFlowController controller;

    // Recebe o grafo da MainUI para passar ao Controller
    public MaxFlowUI(Graph<String, Double> railwayNetwork) {
        this.controller = new MaxFlowController(railwayNetwork);
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- USEI14: Calcular Fluxo Máximo (Max Flow) ---");

        try {
            // 1. Pedir Estação Origem
            System.out.print("Introduza o nome da Estação de Origem: ");
            String source = sc.nextLine();

            // 2. Pedir Estação Destino
            System.out.print("Introduza o nome da Estação de Destino: ");
            String sink = sc.nextLine();

            // 3. Chamar Controller
            double maxFlow = controller.getMaxFlow(source, sink);

            // 4. Mostrar Resultado
            System.out.println("------------------------------------------------");
            System.out.printf("Fluxo Máximo entre '%s' e '%s': %.2f comboios/dia\n", source, sink, maxFlow);
            System.out.println("------------------------------------------------");

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }
}
