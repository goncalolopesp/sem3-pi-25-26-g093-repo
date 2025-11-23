package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic Tree data structure (k-d tree).
 * @param <T> the type of elements stored in the tree nodes
 */
public class Tree<T extends Comparable<T>> {

    // Dimension of the Tree (k-d tree)
    private final int k;

    /**
     * Tree Node class representing each node in the tree.
     * @param <T> the type of elements stored in the tree nodes
     */
    public static class TreeNode<T extends Comparable<T>> {
        private ArrayList<T> values;
        private TreeNode<T> left;
        private TreeNode<T> right;
        private final List<Object> payloads;

        /**
         * Constructor for TreeNode.
         * @param values the list of values for the node
         */
        public TreeNode(ArrayList<T> values) {
            this.values = new ArrayList<>(values);
            this.left = null;
            this.right = null;
            this.payloads = new ArrayList<>();
        }

        /**
         * Gets the elements of the node.
         * @return the list of elements
         */
        public ArrayList<T> getElements() { return values; }

        /**
         * Gets the element at the specified index.
         * @param e the index of the element
         * @return the element at index e
         */
        public T getElement(int e) { return values.get(e); }

        /**
         * Retrieves the left child of this tree node.
         *
         * @return the left child node of this tree node, or null if no left child exists
         */
        public TreeNode<T> getLeft() { return left; }

        /**
         * Retrieves the right child of this tree node.
         * @return the right child node of this tree node, or null if no right child exists
         */
        public TreeNode<T> getRight() { return right; }

        /**
         * Gets the payloads associated with this node.
         * @return an unmodifiable list of payloads
         */
        public List<Object> getPayloads() { return Collections.unmodifiableList(payloads); }

        /**
         * Adds a payload to the node.
         * @param payload the payload to add
         */
        public void addPayload(Object payload) {
            if (payload != null) {
                payloads.add(payload);
            }
        }

        /**
         * Sets the elements of the node.
         * @param values the new list of elements
         */
        public void setElements(ArrayList<T> values) { this.values = new ArrayList<>(values); }

        /**
         * Sets the left child of the node.
         * @param leftChild the new left child node
         */
        public void setLeft(TreeNode<T> leftChild) { left = leftChild; }

        /**
         * Sets the right child of the node.
         * @param rightChild the new right child node
         */
        public void setRight(TreeNode<T> rightChild) { right = rightChild; }

        /**
         * Generates a string representation of the node.
         * @return a string representing the node's values
         */
        @Override
        public String toString() {
            StringBuilder text = new StringBuilder("(");
            for (int i = 0; i < values.size() - 1; i++) {
                text.append(values.get(i)).append(", ");
            }
            text.append(values.get(values.size() - 1)).append(")");
            return text.toString();
        }
    }

    private TreeNode<T> root;

    /**
     * Constructor for Tree.
     * @param k the dimension of the tree
     */
    public Tree(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("Dimension k must be > 0");
        }
        this.k = k;
        this.root = null;
    }

    /**
     * Constructor for Tree with a root node.
     * @param root the root node of the tree
     * @param k the dimension of the tree
     */
    public Tree(TreeNode<T> root, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("Dimension k must be > 0");
        }
        this.k = k;
        this.root = root;
    }

    /**
     * Returns the root node of the tree.
     * @return the root node
     */
    protected TreeNode<T> root() {
        return root;
    }

    /**
     * Returns the root node of the tree.
     * 
     * @return the root node, or null if the tree is empty
     */
    public TreeNode<T> getRoot() {
        return root;
    }

    /**
     * Checks if the tree is empty.
     * @return true if the tree is empty, false otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Inserts a list of values into the tree.
     * @param values the list of values to insert
     */
    public void Insert(ArrayList<T> values) {
        root = Insert(root, values, null, 0);
    }

    /**
     * Inserts a list of values with an associated payload into the tree.
     * @param values the list of values to insert
     * @param payload the payload associated with the values
     */
    public void Insert(ArrayList<T> values, Object payload) {
        root = Insert(root, values, payload, 0);
    }

    /**
     * Inserts a list of values with an associated payload into the tree starting from a given node and depth.
     * @param node the starting node for insertion
     * @param values the list of values to insert
     * @param payload the payload associated with the values
     * @param depth the current depth in the tree
     * @return
     */
    public TreeNode<T> Insert(TreeNode<T> node, ArrayList<T> values, Object payload, int depth) {
        if (node == null) {
            TreeNode<T> newNode = new TreeNode<>(values);
            if (payload != null) {
                newNode.addPayload(payload);
            }
            return newNode;
        }

        if (node.getElements().equals(values)) {
            if (payload != null) {
                node.addPayload(payload);
            }
            return node;
        }

        int dimension = depth % k;
        int compare = node.getElement(dimension).compareTo(values.get(dimension));

        if (compare > 0) {
            node.setLeft(Insert(node.getLeft(), values, payload, depth + 1));
        } else {
            node.setRight(Insert(node.getRight(), values, payload, depth + 1));
        }

        return node;
    }

    /**
     * Calculates the size of the tree.
     * @return the number of nodes in the tree
     */
    public int Size() {
        return Size(root);
    }

    /**
     * Calculates the size of the subtree rooted at the given node.
     * @param node the root node of the subtree
     * @return the number of nodes in the subtree
     */
    public int Size(TreeNode<T> node) {
        if (node == null)
            return 0;

        return 1 + Size(node.getLeft()) + Size(node.getRight());
    }

    /**
     * Calculates the height of the tree.
     * @return the height of the tree
     */
    public int Height() {
        return Height(root, 0);
    }

    /**
     * Calculates the height of the subtree rooted at the given node.
     * @param node the root node of the subtree
     * @param depth the current depth in the tree
     * @return the height of the subtree
     */
    public int Height(TreeNode<T> node, int depth) {
        if (node == null)
            return 0;

        depth++;
        return Math.max(depth, Math.max(Height(node.getRight(), depth), Height(node.getLeft(), depth)));
    }

    /**
     * Displays the nodes of the tree.
     */
    public void ShowNodes() {
        ShowNode(root);
    }

    /**
     * Displays the given node and its children.
     * @param node the node to display
     */
    public void ShowNode(TreeNode<T> node) {
        if (node != null) {
            System.out.println("Node: " + node);

            if (node.getLeft() != null)
                System.out.println("Node left: " + node.getLeft());
            if (node.getRight() != null)
                System.out.println("Node right: " + node.getRight());

            ShowNode(node.getLeft());
            ShowNode(node.getRight());
        }
    }

    /**
     * Finds a node with the specified values in the tree.
     * @param values the list of values to find
     * @return the node with the specified values, or null if not found
     */
    public TreeNode<T> Find(ArrayList<T> values) {
        return Find(root, values, 0);
    }

    /**
     * Finds a node with the specified values starting from a given node and depth.
     * @param node the starting node for the search
     * @param values the list of values to find
     * @param depth the current depth in the tree
     * @return the node with the specified values, or null if not found
     */
    public TreeNode<T> Find(TreeNode<T> node, ArrayList<T> values, int depth) {
        if (node != null) {
            if (node.values.equals(values))
                return node;

            int dimension = depth % k;
            int compare = node.getElement(dimension).compareTo(values.get(dimension));

            if (compare > 0)
                return Find(node.getLeft(), values, depth + 1);

            return Find(node.getRight(), values, depth + 1);
        }

        return null;
    }

    /**
     * Performs a range search in the tree.
     * @param lower the lower bounds of the range
     * @param upper the upper bounds of the range
     * @return a list of nodes that fall within the specified range
     */
    public List<TreeNode<T>> RangeSearch(ArrayList<T> lower, ArrayList<T> upper) {
        ArrayList<TreeNode<T>> matches = new ArrayList<>();
        RangeSearch(root, lower, upper, 0, matches);
        return matches;
    }

    /**
     * Helper method for performing a range search starting from a given node and depth.
     * @param node the starting node for the search
     * @param lower the lower bounds of the range
     * @param upper the upper bounds of the range
     * @param depth the current depth in the tree
     * @param matches the list to store matching nodes
     */
    private void RangeSearch(TreeNode<T> node, ArrayList<T> lower, ArrayList<T> upper, int depth, List<TreeNode<T>> matches) {
        if (node != null) {
            // Check if the node's point is completely inside the range
            boolean inside = true;
            for (int i = 0; i < k; i++) {
                int lowCompare = node.getElement(i).compareTo(lower.get(i));
                int upCompare = node.getElement(i).compareTo(upper.get(i));

                if (lowCompare < 0 || upCompare > 0) {
                    inside = false;
                    break;
                }
            }

            if (inside)
                matches.add(node);

            // Determine which subtrees to search based on current dimension
            int dimension = depth % k;
            T nodeValue = node.getElement(dimension);
            T lowerBound = lower.get(dimension);
            T upperBound = upper.get(dimension);

            int lowCompare = nodeValue.compareTo(lowerBound);
            int upCompare = nodeValue.compareTo(upperBound);

            // If node's value is less than lower bound, only search right subtree
            // If node's value is greater than upper bound, only search left subtree
            // If node's value is between bounds, search both subtrees
            if (lowCompare < 0) {
                // Node value < lower bound: only search right subtree
                RangeSearch(node.getRight(), lower, upper, depth + 1, matches);
            } else if (upCompare > 0) {
                // Node value > upper bound: only search left subtree
                RangeSearch(node.getLeft(), lower, upper, depth + 1, matches);
            } else {
                // Node value is within bounds: search both subtrees
                RangeSearch(node.getLeft(), lower, upper, depth + 1, matches);
                RangeSearch(node.getRight(), lower, upper, depth + 1, matches);
            }
        }
    }
}
