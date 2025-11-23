package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * Binary Search Tree implementation.
 * @param <T> the type of elements stored in the tree
 */
public class BST<T extends Comparable<T>> implements BSTInterface<T> {

    protected BSTNode<T> root;

    /**
     * Inserts an element into the BST.
     * @param element the element to be inserted
     */
    public void insert(T element) {
        root = insert(root, element);
    }

    /**
     * Helper method to insert an element into the BST.
     * @param node current node
     * @param element element to be inserted
     * @return the updated node
     */
    private BSTNode<T> insert(BSTNode<T> node, T element) {
        if (node == null) {
            return new BSTNode<>(element);
        }
        int cmp = element.compareTo(node.element);
        if (cmp < 0) {
            node.left = insert(node.left, element);
        } else {
            node.right = insert(node.right, element);
        }

        return node;
    }

    /**
     * Checks if the BST contains a specific element.
     * @param element the element to be searched
     * @return true if the element is found, false otherwise
     */
    public boolean contains(T element) {
        return contains(root, element);
    }

    /**
     * Helper method to check if the BST contains a specific element.
     * @param n current node
     * @param e element to be searched
     * @return true if the element is found, false otherwise
     */
    private boolean contains(BSTNode<T> n, T e) {
        if (n == null) return false;
        int cmp = e.compareTo(n.element);
        if (cmp == 0) return true;
        return (cmp < 0) ? contains(n.left, e) : contains(n.right, e);
    }

    /**
     * Removes an element from the BST.
     * @param element the element to be removed
     * @return true if the element was removed, false if it was not found
     */
    public boolean remove(T element) {
        if (!contains(element)) return false;
        root = remove(root, element);
        return true;
    }

    /**
     * Helper method to remove an element from the BST.
     * @param n current node
     * @param e element to be removed
     * @return the updated node
     */
    private BSTNode<T> remove(BSTNode<T> n, T e) {
        if (n == null) return null;

        int cmp = e.compareTo(n.element);
        if (cmp < 0) n.left = remove(n.left, e);
        else if (cmp > 0) n.right = remove(n.right, e);
        else {
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;

            BSTNode<T> min = smallest(n.right);
            n.element = min.element;
            n.right = remove(n.right, min.element);
        }
        return n;
    }

    /**
     * Finds the smallest node in a subtree.
     * @param n the root of the subtree
     * @return the node with the smallest element
     */
    private BSTNode<T> smallest(BSTNode<T> n) {
        while (n.left != null) n = n.left;
        return n;
    }

    /**
     * Performs an in-order traversal of the BST.
     * @return a list of elements in in-order sequence
     */
    public List<T> inOrder() {
        List<T> list = new ArrayList<>();
        inOrder(root, list);
        return list;
    }

    /**
     * Helper method for in-order traversal.
     * @param n current node
     * @param l list to store elements
     */
    private void inOrder(BSTNode<T> n, List<T> l) {
        if (n == null) return;
        inOrder(n.left, l);
        l.add(n.element);
        inOrder(n.right, l);
    }

    /**
     * Checks if the BST is empty.
     * @return true if the BST is empty, false otherwise
     */
    public boolean isEmpty() { return root == null; }
}
