package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import java.util.ArrayList;
import java.util.List;

public class BST<T extends Comparable<T>> implements BSTInterface<T> {

    protected BSTNode<T> root;

    public void insert(T element) {
        root = insert(root, element);
    }

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

    public boolean contains(T element) {
        return contains(root, element);
    }

    private boolean contains(BSTNode<T> n, T e) {
        if (n == null) return false;
        int cmp = e.compareTo(n.element);
        if (cmp == 0) return true;
        return (cmp < 0) ? contains(n.left, e) : contains(n.right, e);
    }

    public boolean remove(T element) {
        if (!contains(element)) return false;
        root = remove(root, element);
        return true;
    }

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

    private BSTNode<T> smallest(BSTNode<T> n) {
        while (n.left != null) n = n.left;
        return n;
    }

    public List<T> inOrder() {
        List<T> list = new ArrayList<>();
        inOrder(root, list);
        return list;
    }

    private void inOrder(BSTNode<T> n, List<T> l) {
        if (n == null) return;
        inOrder(n.left, l);
        l.add(n.element);
        inOrder(n.right, l);
    }

    public boolean isEmpty() { return root == null; }
}
