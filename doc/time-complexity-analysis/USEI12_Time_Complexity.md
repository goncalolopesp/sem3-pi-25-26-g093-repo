# USEI12 — Efficiency Analysis (Minimal Backbone Network)

## 1. Introduction

This document presents the **worst-case time complexity analysis** of the algorithms implemented in **USEI12 – Minimal Backbone Network**.

The implemented solution uses:
- **Prim's Algorithm** to compute the Minimum Spanning Tree (MST) over an undirected rail network.
- **Graphviz (Neato)** for coordinate-based SVG visualization.

Definitions:
- **V** — total number of stations (vertices).
- **E** — total number of tracks/connections (edges).

---

## 2. Program Flow Overview

USEI12 performs:

1. `computePrim` — Iterates through the graph to find the minimal subset of tracks that connects all stations.
2. `generateBackbone` — Traverses the resulting MST to generate a DOT file format with XY positions.
3. `executeNeato` — Invokes the external Graphviz engine to render the SVG.

Total expected complexity:  
**$O(E \log V)$**

---

## 3. Time Complexity Tables

### 3.1 `MinimalBackboneController` (MST Logic)

| **Method** | **Description** | **Time Complexity** |
| :--- | :--- | :--- |
| `computePrim()` | Main MST logic using a Priority Queue | $O(E \log V)$ |
| `pq.poll()` | Removing the minimum weight edge from the heap | $O(\log E)$ |
| `pq.add()` | Adding reachable edges to the heap | $O(\log E)$ |
| `mst.addVertex()` | Adding a station to the result graph | $O(1)$ |



### 3.2 `Visualization` (File Exports)

| **Method** | **Description** | **Time Complexity** |
| :--- | :--- | :--- |
| `writer.println` | Writing vertex and edge data to DOT file | $O(V)$ |
| `executeNeato()` | Rendering the graph via external process | $O(V^2)$ (avg. for neato) |

---

## 4. Summary Table of USEI12

| Component | Complexity (Expected Case) | Worst Case |
| :--- | :--- | :--- |
| Minimal Backbone Logic (Prim) | $O(E \log V)$ | $O(E \log V)$ |
| DOT File Generation | $O(V)$ | $O(V)$ |
| **TOTAL** | **$O(E \log V)$** | **$O(E \log V)$** |

---

## 5. Final Conclusion

The implementation of USEI12 is highly efficient for the Belgian rail network. By using a **Priority Queue** with Prim's algorithm, we ensure that the computation time grows logarithmically in relation to the number of stations, even as the network scale increases. The use of XY coordinates in the DOT file ensures that the visualization is not only accurate but also consistent with the geographic reality of the stations.