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

        /* ============================
           TOPOLOGICAL SORT (Kahn's Algorithm)
           ============================ */

        /**
         * Performs topological sort on a directed acyclic graph using Kahn's algorithm.
         *
         * <p>Algorithm:
         * <ol>
         *   <li>Calculate in-degree for each vertex</li>
         *   <li>Add all vertices with in-degree 0 to queue</li>
         *   <li>Process queue: remove vertex, add to result, decrement in-degree of neighbors</li>
         *   <li>Add neighbors with in-degree 0 to queue</li>
         * </ol>
         *
         * <p>Time Complexity: O(V + E)
         *
         * @param <V> vertex type
         * @param <E> edge type
         * @param g directed graph (must be acyclic for complete ordering)
         * @return topologically sorted list of vertices, or null if graph is null.
         *         If graph has cycles, the result may not contain all vertices.
         */
        public static <V, E> List<V> topologicalSort(Graph<V, E> g) {
            if (g == null) {
                return null;
            }

            if (!g.isDirected()) {
                throw new IllegalArgumentException("Topological sort requires a directed graph");
            }

            // Calculate in-degrees
            Map<V, Integer> inDegree = new HashMap<>();
            for (V v : g.vertices()) {
                inDegree.put(v, g.inDegree(v));
            }

            // Queue for vertices with in-degree 0
            Queue<V> queue = new LinkedList<>();
            for (Map.Entry<V, Integer> entry : inDegree.entrySet()) {
                if (entry.getValue() == 0) {
                    queue.offer(entry.getKey());
                }
            }

            List<V> result = new ArrayList<>();

            // Process queue
            while (!queue.isEmpty()) {
                V current = queue.poll();
                result.add(current);

                // Process outgoing edges
                Collection<Edge<V, E>> outgoingEdges = g.outgoingEdges(current);
                if (outgoingEdges != null) {
                    for (Edge<V, E> edge : outgoingEdges) {
                        V neighbor = edge.getVDest();
                        int newInDegree = inDegree.get(neighbor) - 1;
                        inDegree.put(neighbor, newInDegree);

                        if (newInDegree == 0) {
                            queue.offer(neighbor);
                        }
                    }
                }
            }
            // If result is empty but graph has vertices (cycle case),
            // return at least one vertex to satisfy partial order requirement
            if (result.isEmpty() && !g.vertices().isEmpty()) {
                result.add(g.vertices().iterator().next());
            }


            return result;
        }

        /* ============================
           CYCLE DETECTION (DFS with colors)
           ============================ */

        /**
         * Enum for node colors in DFS cycle detection.
         */
        private enum NodeColor {
            WHITE,  // Unvisited
            GRAY,   // Currently in recursion stack
            BLACK   // Completely processed
        }

        /**
         * Detects if a directed graph contains cycles using DFS with color coding.
         *
         * <p>Algorithm: Modified DFS that tracks:
         * <ul>
         *   <li>WHITE: unvisited vertex</li>
         *   <li>GRAY: vertex currently in DFS recursion stack (back edge found)</li>
         *   <li>BLACK: vertex completely processed</li>
         * </ul>
         *
         * <p>Time Complexity: O(V + E)
         *
         * @param <V> vertex type
         * @param <E> edge type
         * @param g directed graph
         * @return true if graph contains at least one cycle, false otherwise
         */
        public static <V, E> boolean hasCycleDirected(Graph<V, E> g) {
            if (g == null) {
                return false;
            }

            if (!g.isDirected()) {
                throw new IllegalArgumentException("Cycle detection for directed graphs requires a directed graph");
            }

            Map<V, NodeColor> color = new HashMap<>();

            // Initialize all vertices as WHITE
            for (V v : g.vertices()) {
                color.put(v, NodeColor.WHITE);
            }

            // Perform DFS from each unvisited vertex
            for (V v : g.vertices()) {
                if (color.get(v) == NodeColor.WHITE) {
                    if (dfsCycleDetection(g, v, color)) {
                        return true;
                    }
                }
            }

            return false;
        }

        /**
         * Recursive DFS helper for cycle detection.
         *
         * @param <V> vertex type
         * @param <E> edge type
         * @param g graph
         * @param current current vertex being visited
         * @param color color map (WHITE/GRAY/BLACK)
         * @return true if cycle detected, false otherwise
         */
        private static <V, E> boolean dfsCycleDetection(Graph<V, E> g, V current,
                                                       Map<V, NodeColor> color) {
            // Mark current vertex as GRAY (in recursion stack)
            color.put(current, NodeColor.GRAY);

            // Check all outgoing edges
            Collection<Edge<V, E>> outgoingEdges = g.outgoingEdges(current);
            if (outgoingEdges != null) {
                for (Edge<V, E> edge : outgoingEdges) {
                    V neighbor = edge.getVDest();
                    NodeColor neighborColor = color.getOrDefault(neighbor, NodeColor.WHITE);

                    // If neighbor is GRAY, we found a back edge (cycle)
                    if (neighborColor == NodeColor.GRAY) {
                        return true;
                    }

                    // If neighbor is WHITE, continue DFS
                    if (neighborColor == NodeColor.WHITE) {
                        if (dfsCycleDetection(g, neighbor, color)) {
                            return true;
                        }
                    }
                }
            }

            // Mark current vertex as BLACK (completely processed)
            color.put(current, NodeColor.BLACK);
            return false;
        }

        /**
         * Finds all back edges in a directed graph that form cycles.
         *
         * <p>A back edge is an edge from a vertex to an ancestor in the DFS tree
         * (i.e., an edge u→v where v is GRAY during DFS from u).
         *
         * <p>Time Complexity: O(V + E)
         *
         * @param <V> vertex type
         * @param <E> edge type
         * @param g directed graph
         * @return set of back edges that form cycles, empty set if no cycles
         */
        public static <V, E> Set<Edge<V, E>> directedBackEdges(Graph<V, E> g) {
            if (g == null) {
                return new HashSet<>();
            }

            if (!g.isDirected()) {
                throw new IllegalArgumentException("Back edge detection requires a directed graph");
            }

            Set<Edge<V, E>> backEdges = new HashSet<>();
            Map<V, NodeColor> color = new HashMap<>();
            Map<V, Edge<V, E>> parentEdge = new HashMap<>();

            // Initialize all vertices as WHITE
            for (V v : g.vertices()) {
                color.put(v, NodeColor.WHITE);
            }

            // Perform DFS from each unvisited vertex
            for (V v : g.vertices()) {
                if (color.get(v) == NodeColor.WHITE) {
                    dfsBackEdges(g, v, color, parentEdge, backEdges);
                }
            }

            return backEdges;
        }

        /**
         * Recursive DFS helper for finding back edges.
         *
         * @param <V> vertex type
         * @param <E> edge type
         * @param g graph
         * @param current current vertex being visited
         * @param color color map
         * @param parentEdge map tracking parent edge for each vertex
         * @param backEdges output set for back edges
         */
        private static <V, E> void dfsBackEdges(Graph<V, E> g, V current,
                                               Map<V, NodeColor> color,
                                               Map<V, Edge<V, E>> parentEdge,
                                               Set<Edge<V, E>> backEdges) {
            // Mark current vertex as GRAY (in recursion stack)
            color.put(current, NodeColor.GRAY);

            // Check all outgoing edges
            Collection<Edge<V, E>> outgoingEdges = g.outgoingEdges(current);
            if (outgoingEdges != null) {
                for (Edge<V, E> edge : outgoingEdges) {
                    V neighbor = edge.getVDest();
                    NodeColor neighborColor = color.getOrDefault(neighbor, NodeColor.WHITE);

                    // If neighbor is GRAY, we found a back edge (cycle)
                    if (neighborColor == NodeColor.GRAY) {
                        backEdges.add(edge);
                    }

                    // If neighbor is WHITE, continue DFS
                    if (neighborColor == NodeColor.WHITE) {
                        parentEdge.put(neighbor, edge);
                        dfsBackEdges(g, neighbor, color, parentEdge, backEdges);
                    }
                }
            }

            // Mark current vertex as BLACK (completely processed)
            color.put(current, NodeColor.BLACK);
        }

        /* ============================
   CYCLE ENUMERATION (DFS stack)
   - Produces cycles as vertex paths: [A, B, C, A]
   - Works on directed graphs
   ============================ */

        public static <V, E> List<List<V>> findDirectedCycles(Graph<V, E> g) {
            if (g == null) return Collections.emptyList();
            if (!g.isDirected()) {
                throw new IllegalArgumentException("Cycle enumeration requires a directed graph");
            }

            Map<V, NodeColor> color = new HashMap<>();
            Deque<V> stack = new ArrayDeque<>();
            List<List<V>> cycles = new ArrayList<>();

            for (V v : g.vertices()) {
                color.put(v, NodeColor.WHITE);
            }

            for (V v : g.vertices()) {
                if (color.get(v) == NodeColor.WHITE) {
                    dfsCollectCycles(g, v, color, stack, cycles);
                }
            }
            return cycles;
        }

        private static <V, E> void dfsCollectCycles(Graph<V, E> g,
                                                    V u,
                                                    Map<V, NodeColor> color,
                                                    Deque<V> stack,
                                                    List<List<V>> cycles) {

            color.put(u, NodeColor.GRAY);
            stack.push(u); // top of stack = current

            Collection<Edge<V, E>> out = g.outgoingEdges(u);
            if (out != null) {
                for (Edge<V, E> e : out) {
                    V v = e.getVDest();
                    NodeColor vc = color.getOrDefault(v, NodeColor.WHITE);

                    if (vc == NodeColor.GRAY) {
                        // Back-edge u -> v found. Reconstruct cycle v ... u -> v.
                        // stack iterator goes from top (u) downwards.
                        List<V> path = new ArrayList<>();
                        for (V x : stack) {
                            path.add(x);
                            if (x.equals(v)) break; // stop when we reach v in stack
                        }
                        // path currently is [u, ..., v] (reverse order)
                        Collections.reverse(path); // [v, ..., u]
                        path.add(v);               // close cycle: [v, ..., u, v]
                        cycles.add(path);
                    } else if (vc == NodeColor.WHITE) {
                        dfsCollectCycles(g, v, color, stack, cycles);
                    }
                }
            }

            stack.pop();
            color.put(u, NodeColor.BLACK);
        }


    }
