package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.LatKey;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.LonKey;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.ArrayList;
import java.util.List;

public class BalancedTreeController {
    private final CompleteStationRepository repo;

    public BalancedTreeController() {
        this.repo = Repositories.getInstance().getCompleteStationRepository();
    }

    /**
     * Builds a 2D-tree (k=2) from the stations currently stored in the repository.
     * NOTE: the build strategy is incremental insertion; it does not guarantee
     * a perfectly balanced KD-tree, but is sufficient for USEI08 queries.
     */
    public String buildTree() {
        long startTime = System.nanoTime();
        Tree<Double> tree = new Tree<>(2);

        if (repo.getStations().isEmpty())
            return "There are no stations to build a tree with!";

        int inserted = 0;
        int skipped = 0;

        List<LatKey> lats = repo.getLatTree().inOrder();

        for (LatKey latKey : lats) {
            Double lat = latKey.getLatitude();
            CompleteStation station = latKey.getStation();
            Double lon = station.getLongitude();

            ArrayList<Double> values = new ArrayList<>(2);
            values.add(lat);
            values.add(lon);
            tree.Insert(values, station);
        }

        repo.setTree(tree);
        int height = tree.Height();
        int size = tree.Size();

        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1e6;

        StringBuilder sb = new StringBuilder();
        sb.append("The tree has been successfully made!\n")
                .append("Height: ").append(height).append("\n")
                .append("Size (nodes): ").append(size).append("\n")
                .append("Stations inserted: ").append(inserted).append("\n")
                .append("Stations skipped (invalid coordinates): ").append(skipped).append("\n")
                .append("The operation took ").append(executionTime).append("ms to be completed!");

        return sb.toString();
    }

    public boolean treeExists() {
        return repo.getTree() != null && !repo.getTree().isEmpty();
    }
}
