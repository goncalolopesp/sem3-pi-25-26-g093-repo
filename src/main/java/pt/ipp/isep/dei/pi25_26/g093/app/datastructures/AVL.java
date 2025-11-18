package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

public class AVL<T extends Comparable<T>> {

    private class Node {
        T key;
        Node left, right;
        int height;

        Node(T key) {
            this.key = key;
            this.height = 1;
        }
    }

    private Node root;

    public AVL() {
        this.root = null;
    }

    public void insert(T key) {
        root = insertRec(root, key);
    }

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

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
    }

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

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    public boolean isEmpty() {
        return root == null;
    }
}
