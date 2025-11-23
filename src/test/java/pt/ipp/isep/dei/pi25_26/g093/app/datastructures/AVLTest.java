package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AVLTest {

    @Test
    public void testNewTree_IsEmpty() {
        AVL<Integer> avl = new AVL<>();
        assertTrue(avl.isEmpty());
        assertTrue(avl.inOrder().isEmpty());
    }

    @Test
    public void testInsertSingleElement() {
        AVL<Integer> avl = new AVL<>();
        avl.insert(10);

        List<Integer> list = avl.inOrder();
        assertEquals(1, list.size());
        assertEquals(Integer.valueOf(10), list.get(0));
    }

    @Test
    public void testInsertMultipleElements_SortedInOrder() {
        AVL<Integer> avl = new AVL<>();
        avl.insert(30);
        avl.insert(10);
        avl.insert(20);
        avl.insert(40);

        List<Integer> list = avl.inOrder();
        assertArrayEquals(new Integer[]{10, 20, 30, 40}, list.toArray(new Integer[0]));
    }

    @Test
    public void testInsertDuplicateElements_AllPresent() {
        AVL<Integer> avl = new AVL<>();
        avl.insert(10);
        avl.insert(10);
        avl.insert(10);

        List<Integer> list = avl.inOrder();
        assertEquals(3, list.size());
    }

    @Test
    public void testIsEmptyAfterInserts() {
        AVL<Integer> avl = new AVL<>();
        avl.insert(1);
        assertFalse(avl.isEmpty());
    }
}
