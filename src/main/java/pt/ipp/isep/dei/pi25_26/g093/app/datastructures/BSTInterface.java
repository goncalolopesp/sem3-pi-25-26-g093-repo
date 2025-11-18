package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import java.util.List;

public interface BSTInterface<T extends Comparable<T>> {
    void insert(T element);
    boolean remove(T element);
    boolean contains(T element);
    List<T> inOrder();
    boolean isEmpty();
}
