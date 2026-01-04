# USEI14 — Efficiency Analysis (Maximum Throughput / Max Flow)

## 1. Introduction

This document presents the **worst-case time complexity analysis** of the algorithms implemented in **USEI14 – Maximum throughput between two Hubs**.

The implemented solution uses:

- **Edmonds-Karp algorithm** for maximum flow computation
- **Breadth-First Search (BFS)** for finding the shortest augmenting path in the residual graph
- **Adjacency Lists (MapGraph)** for efficient graph traversal

Definitions:

- **n** — number of stations (vertices) in the graph ($V$)
- **m** — number of tracks/edges in the graph ($E$)
- **f** — value of the maximum flow (used in Ford-Fulkerson, but Edmonds-Karp depends on $n$ and $m$)

For **Edmonds-Karp**, the time complexity is typically **$\Theta(n \cdot m^2)$**, where **n** is the number of vertices and **m** is the number of edges.

---

## 2. Program Flow Overview

**USEI14** performs:

1. **Graph Cloning** — Creates the residual graph (copy of vertices and edges) — **$\Theta(n + m)$**
2. **Edmonds-Karp Loop** — Repeats until no path exists from source to sink:
    - **BFS (bfsResidual)** — Finds the shortest path in the residual graph — **$\Theta(m)$**
    - **Bottleneck Calculation** — Traverses the path back to find min capacity — **$\Theta(n)$**
    - **Edge Updates** — Updates forward and backward edge weights — **$\Theta(n)$**

**Total expected complexity:**
**$\Theta(n \cdot m^2)$**

**Worst-case complexity:**
**$\Theta(n \cdot m^2)$**

---

## 3. Time Complexity Tables (per class)

### 3.1 `RailwayNetworkService`

| **Method** | **Description** | **Time Complexity** |
|---------------------------------|-------------------------------------------------------------|-------------------------------------------------|
| `calculateMaxFlow()`            | Executes the Edmonds-Karp algorithm                         | **$\Theta(n \cdot m^2)$** |
| `bfsResidual()`                 | Finds the shortest path using BFS in the residual graph     | $\Theta(m)$                                     |
| **Residual Graph Creation** | Copies the original graph structure                         | $\Theta(n + m)$                                 |

---

### 3.2 `MapGraph` (Graph representing the network)

| **Method** | **Description** | **Time Complexity** |
|--------------------------|------------------------------------------------|-----------------------|
| `vertices()`             | Returns all vertices (stations)                | $\Theta(n)$           |
| `edges()`                | Returns all edges (tracks)                     | $\Theta(m)$           |
| `outgoingEdges(station)` | Returns all outgoing edges for a given station | $\Theta(d)$ (d = degree of vertex) |
| `edge(u, v)`             | Retrieves a specific edge                      | $\Theta(1)$ (Average case for Hash Map) |

---

## 4. General Complexity Analysis

The **Edmonds-Karp algorithm** is the main driver of time complexity. It works as follows:

- It uses **BFS** to find an augmenting path. In a graph implemented with adjacency lists (like `MapGraph`), BFS takes **$\Theta(m)$** time.
- In the worst-case scenario, the algorithm performs at most **$\Theta(n \cdot m)$** augmentations (iterations of the loop).
- Each augmentation involves a BFS search.

Therefore, the total time complexity is the number of augmentations multiplied by the cost of BFS:
$$\Theta(n \cdot m) \times \Theta(m) = \Theta(n \cdot m^2)$$

### **Expected Complexity:**
**$\Theta(n \cdot m^2)$**, where **n** is the number of stations and **m** is the number of tracks.

### **Worst Case:**
**$\Theta(n \cdot m^2)$**, this bound is tight for the Edmonds-Karp implementation regardless of edge capacities.

---

## 5. Summary Table of USEI14

| **Component** | **Expected Case Complexity** | **Worst Case Complexity** |
|----------------------------------|------------------------------|---------------------------|
| **Residual Graph Initialization**| $\Theta(n + m)$              | $\Theta(n + m)$           |
| **BFS (Single Search)** | $\Theta(m)$                  | $\Theta(m)$               |
| **Path Reconstruction** | $\Theta(n)$                  | $\Theta(n)$               |
| **Max Number of Augmentations** | $\Theta(n \cdot m)$          | $\Theta(n \cdot m)$       |
| **TOTAL (Edmonds-Karp)** | **$\Theta(n \cdot m^2)$** | **$\Theta(n \cdot m^2)$** |

---

## 6. Final Conclusion

The implementation of **USEI14** (Maximum Throughput) uses the **Edmonds-Karp** algorithm, which is a specific implementation of the Ford-Fulkerson method that uses BFS to find augmenting paths.

- **Expected Complexity:**
  **$\Theta(n \cdot m^2)$**, where **n** is the number of stations and **m** is the number of edges.

- **Efficiency:**
  Since the railway network is a **sparse graph** (where $m$ is typically much smaller than $n^2$, often $m \approx k \cdot n$), this complexity is very efficient for the given problem size compared to other max-flow algorithms like generic Ford-Fulkerson (which depends on flow value $f$) or Dinic's Algorithm for very dense graphs.

---

**End of document.**