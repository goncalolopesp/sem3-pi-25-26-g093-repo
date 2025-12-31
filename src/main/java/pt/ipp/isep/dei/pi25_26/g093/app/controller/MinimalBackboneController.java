package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.GraphRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.io.*;
import java.util.*;

public class MinimalBackboneController {
    private final GraphRepository repo = Repositories.getInstance().getGraphRepository();

    public boolean generateBackbone(String filename) {
        Graph<CompleteStation, Double> g = repo.getGraph();
        if (g.numVertices() == 0) return false;

        Graph<CompleteStation, Double> mst = computePrim(g);

        File dotFile = new File(filename + ".dot");
        try (PrintWriter writer = new PrintWriter(dotFile)) {
            writer.println("graph backbone {");
            writer.println("  layout=neato;");
            writer.println("  overlap=false;");
            writer.println("  splines=true;");
            writer.println("  sep=\"+25\";");
            writer.println("  model=\"subset\";");

            writer.println("  node [shape=circle, style=filled, fillcolor=\"#3399FF\", width=0.05, label=\"\"];");
            writer.println("  edge [penwidth=0.3, color=\"#888888\", fontsize=5];");

            for (CompleteStation s : mst.vertices()) {
                double[] xy = repo.getCoords(s);
                double scaleFactor = 1.5;

                writer.printf(Locale.US, "  \"%s\" [pos=\"%.3f,%.3f!\", xlabel=\"%s\"];\n",
                        s.getStation(), (xy[0]/1000) * scaleFactor, (xy[1]/1000) * scaleFactor, s.getStation());
            }

            for (Edge<CompleteStation, Double> e : mst.edges()) {
                if (e.getVOrig().getStation().compareTo(e.getVDest().getStation()) < 0) {
                    writer.printf(Locale.US, "  \"%s\" -- \"%s\" [label=\"%.1f\"];\n",
                            e.getVOrig().getStation(), e.getVDest().getStation(), e.getWeight());
                }
            }
            writer.println("}");
        } catch (IOException e) {
            return false;
        }

        return executeNeato(filename);
    }

    private Graph<CompleteStation, Double> computePrim(Graph<CompleteStation, Double> g) {
        Graph<CompleteStation, Double> mst = new MapGraph<>(false);
        List<CompleteStation> vertices = g.vertices();
        if (vertices.isEmpty()) return mst;

        Map<CompleteStation, Edge<CompleteStation, Double>> minEdgeTo = new HashMap<>();
        PriorityQueue<Edge<CompleteStation, Double>> pq = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));

        CompleteStation start = vertices.get(0);
        mst.addVertex(start);

        for (Edge<CompleteStation, Double> e : g.outgoingEdges(start)) {
            pq.add(e);
        }

        while (!pq.isEmpty() && mst.numVertices() < g.numVertices()) {
            Edge<CompleteStation, Double> edge = pq.poll();
            CompleteStation dest = edge.getVDest();

            if (mst.validVertex(dest)) continue;

            mst.addVertex(dest);
            mst.addEdge(edge.getVOrig(), dest, edge.getWeight());

            for (Edge<CompleteStation, Double> nextEdge : g.outgoingEdges(dest)) {
                if (!mst.validVertex(nextEdge.getVDest())) {
                    pq.add(nextEdge);
                }
            }
        }
        return mst;
    }

    private boolean executeNeato(String filename) {
        try {
            ProcessBuilder pb = new ProcessBuilder("neato", "-Tsvg", filename + ".dot", "-o", filename + ".svg");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            return p.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}