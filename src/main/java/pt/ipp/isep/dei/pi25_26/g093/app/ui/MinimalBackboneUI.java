package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.MinimalBackboneController;
import java.util.Scanner;

public class MinimalBackboneUI implements Runnable {
    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== USEI12: Geração de Backbone Ferroviário ===");
        System.out.print("Introduza o nome base para os ficheiros (ex: rede_belgica): ");
        String folderPath = "generated-graphs/";
        String name = folderPath + sc.nextLine();
        //String name = sc.nextLine();

        MinimalBackboneController ctrl = new MinimalBackboneController();
        System.out.println("A processar MST e a gerar imagem SVG...");

        if (ctrl.generateBackbone(name)) {
            System.out.println("Sucesso!");
            System.out.println("1. Ficheiro DOT: " + name + ".dot");
            System.out.println("2. Ficheiro SVG: " + name + ".svg (Abrir no browser)");
        } else {
            System.out.println("Erro: Falha na geração. Certifica-te que tens o Graphviz instalado e o comando 'neato' acessível.");
        }
    }
}