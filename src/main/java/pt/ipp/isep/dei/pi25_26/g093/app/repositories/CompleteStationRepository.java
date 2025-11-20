package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.AVL;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.BST;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.util.ArrayList;
import java.util.List;

public class CompleteStationRepository {

    private final List<CompleteStation> stations = new ArrayList<>();

    private BST<LatKey> latTree = new BST<>();
    private BST<LonKey> lonTree = new BST<>();
    private BST<TzgKey> tzgTree = new BST<>();

    private Tree<Double> tree = new Tree<>(2);

    public void addStation(CompleteStation cs) {
        stations.add(cs);
    }

    public List<CompleteStation> getStations() {
        return stations;
    }

    public BST<LatKey> getLatTree() { return latTree; }
    public BST<LonKey> getLonTree() { return lonTree; }
    public BST<TzgKey> getTzgTree() { return tzgTree; }
    public Tree<Double> getTree() { return tree; }

    public void setLatTree(BST<LatKey> latTree) { this.latTree = latTree; }
    public void setLongTree(BST<LonKey> lonTree) { this.lonTree = lonTree; }
    public void setTzgTree(BST<TzgKey> tzgTree) { this.tzgTree = tzgTree; }
    public void setTree(Tree<Double> tree) { this.tree = tree; }

    public void addLatKey(LatKey key) { latTree.insert(key); }
    public void addLonKey(LonKey key) { lonTree.insert(key); }
    public void addTzgKey(TzgKey key) { tzgTree.insert(key); }

    public void setTrees(BST<LatKey> lat, BST<LonKey> lon, BST<TzgKey> tzg, Tree<Double> tree) {
        this.latTree = lat;
        this.lonTree = lon;
        this.tzgTree = tzg;
        this.tree = tree;
    }
}
