# USEI15 — Efficiency Analysis (Risk-Aware Shortest Path Calculation)

## 1. Introduction

This document presents the **worst-case time complexity analysis** of the algorithms implemented in **USEI15 – Risk-Aware Shortest Path Calculation**.

The implemented solution uses:

- **Bellman-Ford algorithm** for shortest path computation
- **Risk penalties** applied to edges during the traversal
- **Backtracking** for path reconstruction
- **Pruning** applied to cut off suboptimal solutions during traversal

Definitions:

- **n** — number of stations (vertices) in the graph
- **m** — number of edges in the graph
- **k** — number of nodes actually visited during shortest path calculation
- **N** — number of nearest neighbors requested (not used in the path calculation)

For **Bellman-Ford**, the time complexity is typically **O(n * m)**, where **n** is the number of vertices and **m** is the number of edges.

---

## 2. Program Flow Overview

**USEI15** performs:

1. **validateParameters** — constant-time validation — **Θ(1)**
2. **initializeDataStructures** — initializes maps for distances and paths — **Θ(n)**
3. **Bellman-Ford Algorithm** — relaxes edges and computes the shortest paths — **Θ(n * m)**
4. **Negative Cycle Detection** — checks for negative cycles — **Θ(m)**
5. **Backtracking** — reconstructs the shortest path — **Θ(n)**

**Total expected complexity:**  
**Θ(n * m)**  
**Worst-case complexity:**  
**Θ(n * m)** (in the worst case where no pruning is applied)

---

## 3. Time Complexity Tables (per class)

### 3.1 `RiskAwareShortestPathController`

| **Method**                     | **Description**                                             | **Time Complexity**                             |
|---------------------------------|-------------------------------------------------------------|-------------------------------------------------|
| **Constructor**                 | Initializes the controller                                  | Θ(1)                                            |
| `computeShortestPathWithRisk()` | Executes the shortest path calculation with risk penalties  | **Θ(n * m)** (time complexity of Bellman-Ford)  |
| `getEdgeCost()`                 | Retrieves the cost of an edge between two stations          | Θ(m)                                            |
| `getRiskPenalty()`             | Applies risk penalty to an edge                             | Θ(1)                                            |
| `findStationByName()`          | Finds a station by its name                                 | Θ(n)                                            |

---

### 3.2 `PathResult`

| **Method**                | **Description**                                | **Time Complexity**   |
|---------------------------|-----------------------------------------------|-----------------------|
| **Constructor**            | Creates the result object                     | Θ(n)                  |
| `getPath()`               | Returns the path of stations                  | Θ(1)                  |
| `getTotalCost()`          | Returns the total cost of the path            | Θ(1)                  |

---

### 3.3 `Graph` (Graph representing the stations and edges)

| **Method**               | **Description**                                | **Time Complexity**   |
|--------------------------|------------------------------------------------|-----------------------|
| `vertices()`             | Returns all vertices (stations)                | Θ(n)                  |
| `edges()`                | Returns all edges (connections between stations) | Θ(m)                  |
| `outgoingEdges(station)` | Returns all outgoing edges for a given station | Θ(d) (d = degree of vertex) |

---

## 4. General Complexity Analysis

The **Bellman-Ford algorithm** is the main driver of time complexity. It works as follows:

- **n-1 iterations** over all edges where each edge is relaxed, which results in **Θ(n * m)** complexity.
- **Negative cycle detection** after the **n-1 iterations** is done by a final check over all edges, which has complexity **Θ(m)**.

Therefore, the total time complexity for calculating the shortest path with risk penalties is **Θ(n * m)**.

### **Expected Complexity:**
**Θ(n * m)**, where **n** is the number of stations (vertices) and **m** is the number of edges in the graph.

### **Worst Case:**
**Θ(n * m)**, which occurs when the graph is unbalanced and no pruning is applied.

---

## 5. Summary Table of USEI15

| **Component**                   | **Expected Case Complexity** | **Worst Case Complexity** |
|----------------------------------|------------------------------|---------------------------|
| **Bellman-Ford Algorithm**      | Θ(n * m)                     | Θ(n * m)                  |
| **Backtracking**                | Θ(n)                         | Θ(n)                      |
| **Edge Relaxation**             | Θ(m)                         | Θ(m)                      |
| **Negative Cycle Detection**    | Θ(m)                         | Θ(m)                      |
| **Final Path Reconstruction**   | Θ(n)                         | Θ(n)                      |
| **Sorting Final Results**       | Θ(N log N)                   | Θ(N log N)                |
| **TOTAL**                        | **Θ(n * m)**                 | **Θ(n * m)**              |

---

## 6. Final Conclusion

The implementation of **USEI15** (Risk-Aware Shortest Path Calculation) is efficient and follows the classic **Bellman-Ford** algorithm with modifications to handle risk penalties.

- **Expected Complexity:**  
  **Θ(n * m)**, where **n** is the number of stations and **m** is the number of edges in the graph.

- **Worst Case:**  
  **Θ(n * m)**, which happens when the graph is unbalanced, and no pruning is applied to cut off suboptimal solutions.

This complexity is quite efficient for typical graphs used in pathfinding, and ensures that the solution handles large graphs with up to thousands of stations and edges efficiently.

---

**End of document.**
