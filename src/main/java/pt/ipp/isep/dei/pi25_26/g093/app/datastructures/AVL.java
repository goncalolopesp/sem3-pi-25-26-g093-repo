package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * AVL Tree implementation.
 * @param <T> the type of elements stored in the tree
 */
public class AVL<T extends Comparable<T>> {

    /**
     * Node class representing each node in the AVL tree.
     */
    private class Node {
        T key;
        Node left, right;
        int height;

        /**
         * Creates a new Node with the given key.
         * @param key the key of the node
         */
        Node(T key) {
            this.key = key;
            this.height = 1;
        }
    }

    private Node root;

    /**
     * Creates an empty AVL tree.
     */
    public AVL() {
        this.root = null;
    }

    /**
     * Inserts a key into the AVL tree.
     * @param key the key to be inserted
     */
    public void insert(T key) {
        root = insertRec(root, key);
    }

    /**
     * Helper method to insert a key into the AVL tree.
     * @param node current node
     * @param key key to be inserted
     * @return the updated node
     */
    private Node insertRec(Node node, T key) {
        if (node == null) return new Node(key);

        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            node.left = insertRec(node.left, key);
        else
            node.right = insertRec(node.right, key);

        updateHeight(node);
        return balance(node);
    }

    /**
     * Calculates the height of a node.
     * @param n the node
     * @return the height of the node
     */
    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    /**
     * Updates the height of a node.
     * @param n the node
     */
    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    /**
     * Calculates the balance factor of a node.
     * @param n the node
     * @return the balance factor
     */
    private int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
    }

    /**
     * Balances the AVL tree at the given node.
     * @param n the node
     * @return the balanced node
     */
    private Node balance(Node n) {
        int bf = balanceFactor(n);

        if (bf > 1) {
            if (balanceFactor(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        if (bf < -1) {
            if (balanceFactor(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }

    /**
     * Performs a right rotation on the given node.
     * @param y the node to be rotated
     * @return the new root after rotation
     */
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    /**
     * Performs a left rotation on the given node.
     * @param x the node to be rotated
     * @return the new root after rotation
     */
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    /**
     * Checks if the AVL tree is empty.
     * @return true if the tree is empty, false otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Performs an in-order traversal of the AVL tree.
     * @return a list of keys in in-order sequence
     */
    public List<T> inOrder() {
        List<T> list = new ArrayList<>();
        inOrder(root, list);
        return list;
    }

    /**
     * Helper method for in-order traversal.
     * @param n current node
     * @param l list to store keys
     */
    private void inOrder(Node n, List<T> l) {
        if (n == null) return;
        inOrder(n.left, l);
        l.add(n.key);
        inOrder(n.right, l);
    }

}
