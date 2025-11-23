package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import java.util.List;

/**
 * Binary Search Tree Interface.
 * @param <T> the type of elements stored in the tree
 */
public interface BSTInterface<T extends Comparable<T>> {
    /**
     * Inserts an element into the BST.
     * @param element the element to be inserted
     */
    void insert(T element);

    /**
     * Removes an element from the BST.
     * @param element the element to be removed
     * @return true if the element was removed, false otherwise
     */
    boolean remove(T element);

    /**
     * Checks if the BST contains a specific element.
     * @param element the element to be searched
     * @return true if the element is found, false otherwise
     */
    boolean contains(T element);

    /**
     * Performs an in-order traversal of the BST.
     * @return a List of elements in in-order sequence
     */
    List<T> inOrder();

    /**
     * Checks if the BST is empty.
     * @return true if the BST is empty, false otherwise
     */
    boolean isEmpty();
}
