# USEI11 — Time Complexity Analysis (Directed Line Upgrade Plan)

## 1. Introduction

This document presents the **time complexity analysis** of the algorithms implemented in **USEI11 – Directed Line Upgrade Plan**.

The functionality USEI11 solves a classic dependency analysis problem, modeled through a directed graph. Each station is represented by a vertex, and each directed link represents a dependency of the type:

> "Station A must be upgraded before Station B"

For this reason, the algorithms used are exactly those studied in the ESINF course unit, namely:
- **DFS (Depth-First Search)**
- **Cycle detection**
- **Topological sorting**

The implemented solution uses:
- **MapGraph** (adjacency list representation) as a directed graph
- **Algorithms.hasCycleDirected()** for cycle detection using DFS
- **Algorithms.topologicalSort()** for topological ordering (standard graph algorithm studied in ESINF)
- **Algorithms.directedBackEdges()** for identifying cycle-forming edges

**Definitions:**
- **V** — total number of stations (vertices)
- **E** — total number of directed connections (edges)

---

## 2. Why is the Complexity O(V + E)?

In graphs represented by **adjacency lists** (like the `MapGraph` used in this project):

- Each vertex (V) is visited **at most once**
- Each edge (E) is analyzed **at most once**

This is a fundamental property of the graph algorithms presented in the ESINF course slides.

---

## 3. Program Flow Overview

USEI11 performs the following operations:

1. **Graph Construction** — Builds a directed graph from stations and edges
2. **Cycle Detection** — Uses DFS to detect if cycles exist
3. **Result Generation**:
   - If cycles exist: Collects cycle nodes and edges
   - If no cycles: Performs topological sort

**Total complexity:**  
**O(V + E)**

---

## 4. Time Complexity Analysis

### 4.1 Graph Construction

The graph construction process traverses:

- **List of stations** → adds all vertices
- **List of lines** → adds all edges

Since each element is processed **exactly once**:

| Operation | Description | Time Complexity |
| :--- | :--- | :--- |
| Add vertices | Iterate through all stations | O(V) |
| Add edges | Iterate through all connections | O(E) |
| **TOTAL** | **Graph Construction** | **O(V + E)** |

---

### 4.2 Cycle Detection

Cycle detection uses **DFS on directed graphs**, identifying back edges.

During this process:

- Each station is visited **exactly once**
- Each connection is analyzed **exactly once**

| Operation | Description | Time Complexity |
| :--- | :--- | :--- |
| DFS traversal | Visit each vertex once | O(V) |
| Edge exploration | Analyze each edge once | O(E) |
| **TOTAL** | **Cycle Detection** | **O(V + E)** |

This behavior is directly aligned with the algorithms presented in the ESINF graph slides.

---

### 4.3 Topological Sorting

When the graph contains **no cycles**, topological sorting is applied using the standard graph algorithm studied in ESINF (DFS-based or Kahn-based).

Just like DFS:

- Each vertex is processed **once**
- Each edge is explored **once**

| Operation | Description | Time Complexity |
| :--- | :--- | :--- |
| Vertex processing | Process each vertex once | O(V) |
| Edge traversal | Explore each edge once | O(E) |
| **TOTAL** | **Topological Sort** | **O(V + E)** |

---

### 4.4 Cycle Node and Edge Collection

When cycles are detected, the algorithm collects:

- All nodes involved in cycles (from back edge endpoints)
- All edges involved in cycles (back edges themselves)

Since cycle collection reuses the DFS traversal performed by `directedBackEdges()`, the overall complexity remains O(V + E).

| Operation | Description | Time Complexity |
| :--- | :--- | :--- |
| `directedBackEdges()` | DFS to find back edges | O(V + E) |
| Collect cycle nodes | Iterate through back edges | O(E) (worst case) |
| Collect cycle edges | Iterate through back edges | O(E) (worst case) |
| **TOTAL** | **Cycle Collection** | **O(V + E)** |

---

## 5. Why Not O(n²)?

There are **no operations** of the type:

- ❌ Nested loops over vertices
- ❌ Comparison between all pairs of stations
- ❌ Adjacency matrices

Everything is done with **adjacency lists**, therefore:

- ❌ There is no V × V
- ❌ There is no n²

For this reason, expressions like **O(n + 7n²)** do not apply to this problem.

---

## 6. Overall Complexity of USEI11

Regardless of the final result:

| Situation | Operations Executed | Complexity |
| :--- | :--- | :--- |
| **Graph with cycles** | Construction + Cycle Detection + Cycle Collection | **O(V + E)** |
| **Graph without cycles** | Construction + Cycle Detection + Topological Sort | **O(V + E)** |

👉 **The total complexity of USEI11 is always linear relative to the size of the graph.**

---

## 7. Summary Table

| Component | Description | Time Complexity |
| :--- | :--- | :--- |
| Graph Construction | Build directed graph from nodes and edges | O(V + E) |
| Cycle Detection | DFS-based cycle detection | O(V + E) |
| Topological Sort | Standard graph algorithm (when acyclic) | O(V + E) |
| Cycle Collection | Collect nodes and edges from back edges | O(V + E) |
| **TOTAL** | **Complete USEI11 execution** | **O(V + E)** |

**Space Complexity:** O(V + E) (adjacency list representation)

---

## 8. Conclusion

USEI11:

- ✅ Uses exclusively algorithms studied in the ESINF course unit
- ✅ Reuses the provided methods from the `Algorithms` class
- ✅ Presents optimal complexity for graph problems
- ✅ Scales correctly for large railway networks

**Final Complexity:**  
# **O(V + E)**

This linear complexity ensures that the solution performs efficiently even for large-scale railway networks with thousands of stations and connections.

---

**End of document.**

