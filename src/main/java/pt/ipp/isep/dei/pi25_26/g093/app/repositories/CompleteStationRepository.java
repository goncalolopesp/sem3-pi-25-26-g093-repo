package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.AVL;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.util.ArrayList;
import java.util.List;

public class CompleteStationRepository {

    private final List<CompleteStation> stations = new ArrayList<>();

    private AVL<LatKey> latTree = new AVL<>();
    private AVL<LonKey> lonTree = new AVL<>();
    private AVL<TzgKey> tzgTree = new AVL<>();

    private Tree<Double> tree = new Tree<>(2);

    public void addStation(CompleteStation cs) {
        stations.add(cs);
    }

    public List<CompleteStation> getStations() {
        return stations;
    }

    public AVL<LatKey> getLatTree() { return latTree; }
    public AVL<LonKey> getLonTree() { return lonTree; }
    public AVL<TzgKey> getTzgTree() { return tzgTree; }
    public Tree<Double> getTree() { return tree; }

    public void setLatTree(AVL<LatKey> latTree) { this.latTree = latTree; }
    public void setLongTree(AVL<LonKey> lonTree) { this.lonTree = lonTree; }
    public void setTzgTree(AVL<TzgKey> tzgTree) { this.tzgTree = tzgTree; }
    public void setTree(Tree<Double> tree) { this.tree = tree; }

    public void setTrees(AVL<LatKey> lat, AVL<LonKey> lon, AVL<TzgKey> tzg, Tree<Double> tree) {
        this.latTree = lat;
        this.lonTree = lon;
        this.tzgTree = tzg;
        this.tree = tree;
    }
}
