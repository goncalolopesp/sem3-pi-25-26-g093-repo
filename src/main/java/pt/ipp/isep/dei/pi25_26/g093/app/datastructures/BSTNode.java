package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

/**
 * Node class for Binary Search Tree.
 * @param <T> the type of element stored in the node
 */
class BSTNode<T> {
    T element;
    BSTNode<T> left;
    BSTNode<T> right;

    /**
     * Creates a new BSTNode with the given element.
     * @param e the element to be stored in the node
     */
    BSTNode(T e) { element = e; }
}
