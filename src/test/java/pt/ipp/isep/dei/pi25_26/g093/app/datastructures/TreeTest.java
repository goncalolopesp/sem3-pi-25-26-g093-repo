package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TreeTest {

    Integer[][] values = {{8, 4}, {5, 1}, {9, 3}, {7, 2}, {6, 10}};
    Tree<Integer> tree;

    @Before
    public void SetUp()
    {
        tree = new Tree<>(2);

        for(Integer[] i : values)
        {
            ArrayList<Integer> listValues = new ArrayList<>(Arrays.asList(i));
            tree.Insert(listValues);
        }
    }

    @Test
    public void ensureSize()
    {
        int expectedSize = values.length;
        int actualSize = tree.Size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void ensureHeight()
    {
        int expectedHeight = 4;
        int actualHeight = tree.Height();

        assertEquals(expectedHeight, actualHeight);
    }
}
