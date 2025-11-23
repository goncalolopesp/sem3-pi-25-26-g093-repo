package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tree<T extends Comparable<T>> {

    // Dimension of the Tree (k-d tree)
    private final int k;

    public static class TreeNode<T extends Comparable<T>> {
        private ArrayList<T> values;
        private TreeNode<T> left;
        private TreeNode<T> right;
        private final List<Object> payloads;

        public TreeNode(ArrayList<T> values) {
            this.values = new ArrayList<>(values);
            this.left = null;
            this.right = null;
            this.payloads = new ArrayList<>();
        }

        public ArrayList<T> getElements() { return values; }
        public T getElement(int e) { return values.get(e); }
        public TreeNode<T> getLeft() { return left; }
        public TreeNode<T> getRight() { return right; }
        public List<Object> getPayloads() { return Collections.unmodifiableList(payloads); }

        public void addPayload(Object payload) {
            if (payload != null) {
                payloads.add(payload);
            }
        }

        public void setElements(ArrayList<T> values) { this.values = new ArrayList<>(values); }
        public void setLeft(TreeNode<T> leftChild) { left = leftChild; }
        public void setRight(TreeNode<T> rightChild) { right = rightChild; }

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

    public Tree(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("Dimension k must be > 0");
        }
        this.k = k;
        this.root = null;
    }

    public Tree(TreeNode<T> root, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("Dimension k must be > 0");
        }
        this.k = k;
        this.root = root;
    }

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

    public boolean isEmpty() {
        return root == null;
    }

    public void Insert(ArrayList<T> values) {
        root = Insert(root, values, null, 0);
    }

    public void Insert(ArrayList<T> values, Object payload) {
        root = Insert(root, values, payload, 0);
    }

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

    public int Size() {
        return Size(root);
    }

    public int Size(TreeNode<T> node) {
        if (node == null)
            return 0;

        return 1 + Size(node.getLeft()) + Size(node.getRight());
    }

    public int Height() {
        return Height(root, 0);
    }

    public int Height(TreeNode<T> node, int depth) {
        if (node == null)
            return 0;

        depth++;
        return Math.max(depth, Math.max(Height(node.getRight(), depth), Height(node.getLeft(), depth)));
    }

    public void ShowNodes() {
        ShowNode(root);
    }

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

    public TreeNode<T> Find(ArrayList<T> values) {
        return Find(root, values, 0);
    }

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

    public List<TreeNode<T>> RangeSearch(ArrayList<T> lower, ArrayList<T> upper) {
        ArrayList<TreeNode<T>> matches = new ArrayList<>();
        RangeSearch(root, lower, upper, 0, matches);
        return matches;
    }

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
