package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BSTTest {

    @Test
    public void testNewTree_IsEmpty() {
        BST<Integer> bst = new BST<>();
        assertTrue(bst.isEmpty());
        assertTrue(bst.inOrder().isEmpty());
    }

    @Test
    public void testInsertAndContains() {
        BST<Integer> bst = new BST<>();
        bst.insert(10);
        bst.insert(5);

        assertTrue(bst.contains(10));
        assertTrue(bst.contains(5));
        assertFalse(bst.contains(7));
    }

    @Test
    public void testInOrderTraversalSorted() {
        BST<Integer> bst = new BST<>();
        bst.insert(30);
        bst.insert(10);
        bst.insert(20);
        bst.insert(40);

        List<Integer> list = bst.inOrder();
        assertArrayEquals(new Integer[]{10, 20, 30, 40}, list.toArray(new Integer[0]));
    }

    @Test
    public void testRemoveLeafNode() {
        BST<Integer> bst = new BST<>();
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);

        assertTrue(bst.remove(5));
        assertFalse(bst.contains(5));
    }

    @Test
    public void testRemoveNonExisting_ReturnsFalse() {
        BST<Integer> bst = new BST<>();
        bst.insert(10);

        assertFalse(bst.remove(99));
        assertTrue(bst.contains(10));
    }

    @Test
    public void testRemoveRootWithTwoChildren() {
        BST<Integer> bst = new BST<>();
        bst.insert(20);
        bst.insert(10);
        bst.insert(30);
        bst.insert(25);

        assertTrue(bst.remove(20));
        assertFalse(bst.contains(20));

        List<Integer> list = bst.inOrder();
        assertArrayEquals(new Integer[]{10, 25, 30}, list.toArray(new Integer[0]));
    }
}
