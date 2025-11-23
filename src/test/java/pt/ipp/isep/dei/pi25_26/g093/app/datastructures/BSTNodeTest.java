package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import org.junit.Test;

import static org.junit.Assert.*;

public class BSTNodeTest {

    @Test
    public void testConstructorStoresElement() throws Exception {
        BSTNode<Integer> node = new BSTNode<>(10);
        assertNotNull(node);
        assertEquals(Integer.valueOf(10), node.element);
        assertNull(node.left);
        assertNull(node.right);
    }

    @Test
    public void testLeftAndRightAssignment() {
        BSTNode<Integer> root = new BSTNode<>(10);
        BSTNode<Integer> left = new BSTNode<>(5);
        BSTNode<Integer> right = new BSTNode<>(15);

        root.left = left;
        root.right = right;

        assertEquals(left, root.left);
        assertEquals(right, root.right);
    }
}
