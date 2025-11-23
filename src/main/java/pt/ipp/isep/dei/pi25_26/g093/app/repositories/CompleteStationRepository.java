package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.BST;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the CompleteStations and their associated data structures.
 */
public class CompleteStationRepository {

    private final List<CompleteStation> stations = new ArrayList<>();

    private BST<LatKey> latTree = new BST<>();
    private BST<LonKey> lonTree = new BST<>();
    private BST<TzgKey> tzgTree = new BST<>();

    private Tree<Double> tree = new Tree<>(2);

    /**
     * Adds a CompleteStation to the repository.
     * @param cs
     */
    public void addStation(CompleteStation cs) {
        stations.add(cs);
    }

    /**
     * Gets the list of CompleteStations in the repository.
     * @return list of CompleteStations
     */
    public List<CompleteStation> getStations() {
        return stations;
    }

    /**
     * Gets the latitude BST.
     * @return a BST with the latitude keys
     */
    public BST<LatKey> getLatTree() { return latTree; }

    /**
     * Gets the longitude BST.
     * @return a BST with the longitude keys
     */
    public BST<LonKey> getLonTree() { return lonTree; }

    /**
     * Gets the TimeZone Group BST.
     * @return a BST with the TimeZone Group keys
     */
    public BST<TzgKey> getTzgTree() { return tzgTree; }

    /**
     * Gets the main Tree structure.
     * @return the main Tree structure
     */
    public Tree<Double> getTree() { return tree; }

    /**
     * Sets the latitude BST.
     * @param latTree the BST with the latitude keys
     */
    public void setLatTree(BST<LatKey> latTree) { this.latTree = latTree; }

    /**
     * Sets the longitude BST.
     * @param lonTree the BST with the longitude keys
     */
    public void setLongTree(BST<LonKey> lonTree) { this.lonTree = lonTree; }

    /**
     * Sets the TimeZone Group BST.
     * @param tzgTree the BST with the TimeZone Group keys
     */
    public void setTzgTree(BST<TzgKey> tzgTree) { this.tzgTree = tzgTree; }

    /**
     * Sets the main Tree structure.
     * @param tree the main Tree structure
     */
    public void setTree(Tree<Double> tree) { this.tree = tree; }

    /**
     * Adds a latitude key to the latitude BST.
     * @param key the latitude key to be added
     */
    public void addLatKey(LatKey key) { latTree.insert(key); }

    /**
     * Adds a longitude key to the longitude BST.
     * @param key the longitude key to be added
     */
    public void addLonKey(LonKey key) { lonTree.insert(key); }

    /**
     * Adds a TimeZone Group key to the TimeZone Group BST.
     * @param key the TimeZone Group key to be added
     */
    public void addTzgKey(TzgKey key) { tzgTree.insert(key); }

    /**
     * Sets all the trees at once.
     * @param lat the LatKey BST
     * @param lon the LonKey BST
     * @param tzg the TzgKey BST
     * @param tree the main Tree structure
     */
    public void setTrees(BST<LatKey> lat, BST<LonKey> lon, BST<TzgKey> tzg, Tree<Double> tree) {
        this.latTree = lat;
        this.lonTree = lon;
        this.tzgTree = tzg;
        this.tree = tree;
    }
}
