package pt.ipp.isep.dei.pi25_26.g093.app.graphs;

import pt.ipp.isep.dei.pi25_26.g093.app.graphs.MatrixGraph;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.Comparator;

/**
 *
 * @author DEI-ISEP
 *
 */
public class Algorithms {

    /* ============================
       BREADTH-FIRST SEARCH (BFS)
       ============================ */
    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {

        if (g == null || !g.validVertex(vert))
            return null;

        LinkedList<V> queue = new LinkedList<>();
        LinkedList<V> result = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];

        int startKey = g.key(vert);
        visited[startKey] = true;
        queue.add(vert);

        while (!queue.isEmpty()) {
            V v = queue.removeFirst();
            result.add(v);

            for (V adj : g.adjVertices(v)) {
                int k = g.key(adj);
                if (!visited[k]) {
                    visited[k] = true;
                    queue.add(adj);
                }
            }
        }
        return result;
    }


    /* ============================
       DEPTH-FIRST SEARCH (recursive)
       ============================ */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig,
                                                boolean[] visited, LinkedList<V> qdfs) {

        int vKey = g.key(vOrig);
        visited[vKey] = true;
        qdfs.add(vOrig);

        for (V adj : g.adjVertices(vOrig)) {
            int adjKey = g.key(adj);
            if (!visited[adjKey])
                DepthFirstSearch(g, adj, visited, qdfs);
        }
    }

    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {

        if (g == null || !g.validVertex(vert))
            return null;

        LinkedList<V> qdfs = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];

        DepthFirstSearch(g, vert, visited, qdfs);

        return qdfs;
    }


    /* ============================
       ALL PATHS (backtracking)
       ============================ */

    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest,
                                        boolean[] visited, LinkedList<V> path,
                                        ArrayList<LinkedList<V>> paths) {

        int vKey = g.key(vOrig);
        visited[vKey] = true;
        path.addFirst(vOrig); // store reversed

        if (vOrig.equals(vDest)) {
            LinkedList<V> newPath = new LinkedList<>(path);
            Collections.reverse(newPath);
            paths.add(newPath);
        } else {
            for (V adj : g.adjVertices(vOrig)) {
                int adjKey = g.key(adj);
                if (!visited[adjKey])
                    allPaths(g, adj, vDest, visited, path, paths);
            }
        }

        // Backtrack
        path.removeFirst();
        visited[vKey] = false;
    }

    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {

        if (g == null || !g.validVertex(vOrig) || !g.validVertex(vDest))
            return null;

        ArrayList<LinkedList<V>> paths = new ArrayList<>();
        LinkedList<V> current = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];

        allPaths(g, vOrig, vDest, visited, current, paths);

        return paths;
    }


    /* ============================
       DIJKSTRA SHORTEST PATH
       ============================ */

    private static <V, E> void shortestPathDijkstra(Graph<V, E> g, V vOrig,
                                                    Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                                    boolean[] visited, V[] pathKeys, E[] dist) {

        int n = g.numVertices();

        for (int i = 0; i < n; i++) {
            visited[i] = false;
            pathKeys[i] = null;
            dist[i] = null;
        }

        int origKey = g.key(vOrig);
        if (origKey == -1)
            return;

        dist[origKey] = zero;

        for (int i = 0; i < n; i++) {

            // Find min-distance unvisited vertex
            int vKey = -1;
            E minDist = null;

            for (int j = 0; j < n; j++) {
                if (!visited[j] && dist[j] != null &&
                        (minDist == null || ce.compare(dist[j], minDist) < 0)) {
                    minDist = dist[j];
                    vKey = j;
                }
            }

            if (vKey == -1)
                break; // no more reachable vertices

            visited[vKey] = true;

            V v = g.vertex(vKey);

            for (V adj : g.adjVertices(v)) {
                int adjKey = g.key(adj);
                if (visited[adjKey])
                    continue;

                Edge<V, E> e = g.edge(v, adj);
                if (e == null)
                    continue;

                E newDist = sum.apply(dist[vKey], e.getWeight());

                if (dist[adjKey] == null || ce.compare(newDist, dist[adjKey]) < 0) {
                    dist[adjKey] = newDist;
                    pathKeys[adjKey] = v;
                }
            }
        }
    }


    /* ============================
       SHORTEST PATH vOrig → vDest
       ============================ */

    public static <V, E> E shortestPath(Graph<V, E> g, V vOrig, V vDest,
                                        Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                        LinkedList<V> shortPath) {

        if (g == null || !g.validVertex(vOrig) || !g.validVertex(vDest))
            return null;

        int n = g.numVertices();

        @SuppressWarnings("unchecked")
        V[] pathKeys = (V[]) new Object[n];
        @SuppressWarnings("unchecked")
        E[] dist = (E[]) new Object[n];
        boolean[] visited = new boolean[n];

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist);

        int destKey = g.key(vDest);
        if (dist[destKey] == null)
            return null; // no path

        shortPath.clear();
        getPath(g, vOrig, vDest, pathKeys, shortPath);

        return dist[destKey];
    }


    /* ============================
       SHORTEST PATHS from vOrig → all
       ============================ */

    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig,
                                               Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                               ArrayList<LinkedList<V>> paths, ArrayList<E> dists) {

        if (g == null || !g.validVertex(vOrig))
            return false;

        int n = g.numVertices();

        @SuppressWarnings("unchecked")
        V[] pathKeys = (V[]) new Object[n];
        @SuppressWarnings("unchecked")
        E[] dist = (E[]) new Object[n];
        boolean[] visited = new boolean[n];

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist);

        paths.clear();
        dists.clear();

        for (int i = 0; i < n; i++) {
            V v = g.vertex(i);
            LinkedList<V> p = new LinkedList<>();

            if (dist[i] != null)
                getPath(g, vOrig, v, pathKeys, p);

            paths.add(p);
            dists.add(dist[i]);
        }

        return true;
    }


    /* ============================
       PATH RECONSTRUCTION
       ============================ */

    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest,
                                       V[] pathKeys, LinkedList<V> path) {

        path.clear();

        int vKey = g.key(vDest);
        if (vKey == -1)
            return;

        V v = vDest;
        path.addFirst(v);

        while (!v.equals(vOrig)) {
            v = pathKeys[g.key(v)];
            if (v == null) {
                path.clear();
                return;
            }
            path.addFirst(v);
        }
    }


    /* ============================
       FLOYD–WARSHALL (minDistGraph)
       ============================ */

    public static <V, E> MatrixGraph<V, E> minDistGraph(Graph<V, E> g,
                                                        Comparator<E> ce,
                                                        BinaryOperator<E> sum) {

        int n = g.numVertices();
        ArrayList<V> vs = g.vertices();

        @SuppressWarnings("unchecked")
        E[][] m = (E[][]) new Object[n][n];

        // Initialize matrix with direct distances
        for (int i = 0; i < n; i++) {
            V v1 = vs.get(i);
            for (int j = 0; j < n; j++) {
                V v2 = vs.get(j);

                if (i == j)
                    m[i][j] = null;
                else {
                    Edge<V, E> e = g.edge(v1, v2);
                    m[i][j] = (e != null) ? e.getWeight() : null;
                }
            }
        }

        // Floyd-Warshall update
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (m[i][k] == null) continue;

                for (int j = 0; j < n; j++) {
                    if (m[k][j] == null) continue;

                    E viaK = sum.apply(m[i][k], m[k][j]);

                    if (m[i][j] == null || ce.compare(viaK, m[i][j]) < 0)
                        m[i][j] = viaK;
                }
            }
        }

        return new MatrixGraph<>(g.isDirected(), vs, m);
    }
}
