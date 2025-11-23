# USEI08 — Efficiency Analysis (Search by Geographical Area)

## 1. Introduction

This document presents the **worst-case time complexity analysis** of the algorithms implemented in **USEI08 – Search by Geographical Area**.

The implemented solution uses:

- A **2D KD-Tree** (`Tree<Double>`)
- A **rectangular range query** (KD-Tree RangeSearch)
- Additional **post-processing filters** (`StationAreaFilter`)
- A **sorting step** on the final list
- Construction of an immutable **GeoAreaSearchResult**

Definitions:

- **n** — number of stations in the KD-Tree  
- **k** — number of KD-Tree nodes visited during the rectangular search  
- **m** — number of stations that pass filters and reach final output  

Only algorithms implemented manually by the team are included.  
UI/printing logic is excluded.

---

## 2. Program Flow Overview

USEI08 performs:

1. `validateRanges` — constant-time checks  
2. Build lower/upper bounds vectors  
3. Execute `KDTree.RangeSearch(lower, upper)`  
4. Extract payloads  
5. Apply filtering (`StationAreaFilter`)  
6. Sorting (`m log m`)  
7. Build `GeoAreaSearchResult`

Total complexity:  
**Θ(√n + m log m)** in typical KD-Tree performance.

---

## 3. Time Complexity Tables (per class)

### 3.1 `Tree<T>` (KD-Tree)

| **Method**                                   | **Description**                                | **Time Complexity**                           |
|----------------------------------------------|------------------------------------------------|-----------------------------------------------|
| `Tree(int k)`                                | Constructor                                    | Θ(1)                                           |
| `Tree(root, k)`                              | Constructor with root                          | Θ(1)                                           |
| `isEmpty()`                                  | Checks if tree is empty                        | Θ(1)                                           |
| `Insert(values)`                             | Inserts a point into the KD-tree               | Θ(h) ≈ Θ(log n) avg, Θ(n) worst                |
| `Insert(values, payload)`                    | Inserts point with payload                     | Θ(h)                                           |
| `Insert(node, values, payload, depth)`       | Recursive insert                               | Θ(h)                                           |
| `Size()`                                     | Computes number of nodes                       | Θ(n)                                           |
| `Size(node)`                                 | Recursive version                              | Θ(n)                                           |
| `Height()`                                   | Computes tree height                           | Θ(n)                                           |
| `Height(node, depth)`                        | Recursive                                       | Θ(n)                                           |
| `ShowNodes()`                                | Prints all nodes                                | Θ(n)                                           |
| `ShowNode(node)`                             | Recursive                                       | Θ(n)                                           |
| `Find(values)`                               | Searches by point                               | Θ(h) ≈ Θ(log n) avg, Θ(n) worst                |
| `Find(node, values, depth)`                  | Recursive                                       | Θ(h)                                           |
| `RangeSearch(lower, upper)`                  | Rectangular search                              | **Θ(√n + m)** expected, Θ(n) worst             |
| `RangeSearch(node, lower, upper, depth, matches)` | Recursive version                         | **Θ(√n + m)** expected                         |

---

### 3.2 `BalancedTreeController`

| **Method**             | **Description**                                | **Time Complexity**                                   |
|------------------------|------------------------------------------------|-------------------------------------------------------|
| `BalancedTreeController()` | Constructor                              | Θ(1)                                                  |
| `buildTree()`          | Builds KD-tree from repo stations              | Θ(S·h) ≈ Θ(S log S) avg, Θ(S²) worst                  |
| `treeExists()`         | Checks if KD-tree exists                       | Θ(1)                                                  |

---

### 3.3 `CompleteStationRepository`

| **Method**                        | **Description**                                | **Time Complexity** |
|----------------------------------|------------------------------------------------|---------------------|
| `addStation(cs)`                 | Adds station to internal list                  | Θ(1)                |
| `getStations()`                  | Getter                                         | Θ(1)                |
| `getTree()`                      | Returns KD-tree                                | Θ(1)                |
| `setTree(tree)`                  | Sets KD-tree                                   | Θ(1)                |
| `getLatTree()/getLonTree()/getTzgTree()` | Get BSTs                              | Θ(1)                |
| `setLatTree()/setLongTree()/setTzgTree()` | Set BSTs                              | Θ(1)                |
| `addLatKey()/addLonKey()/addTzgKey()`     | Insert into BST                          | Θ(log S) avg        |
 
---

### 3.4 `GeoAreaSearchController`

| **Method**                           | **Description**                                | **Time Complexity**            |
|--------------------------------------|------------------------------------------------|--------------------------------|
| `GeoAreaSearchController()`          | Constructor                                    | Θ(1)                           |
| `GeoAreaSearchController(service)`   | Constructor with DI                            | Θ(1)                           |
| `searchArea(...)`                    | Delegates to service.searchByArea              | **Θ(√n + m log m)**            |

---

### 3.5 `GeoAreaSearch` (USEI08 Core)

| **Method**             | **Description**                                        | **Time Complexity**               |
|------------------------|--------------------------------------------------------|----------------------------------|
| `GeoAreaSearch(repo)`  | Constructor                                            | Θ(1)                             |
| `searchByArea(...)`    | KD-range search + filters + sort + result              | **Θ(√n + m log m)**              |
| `withinBounds(...)`    | Checks lat/lon bounds                                  | Θ(1)                             |
| `validateRanges(...)`  | Validates rectangular inputs                           | Θ(1)                             |

---

### 3.6 `GeoAreaSearchResult`

| **Method**                   | **Description**                                | **Time Complexity** |
|------------------------------|------------------------------------------------|----------------------|
| `GeoAreaSearchResult(...)`   | Copies lists + metadata                        | Θ(m)                 |
| `getStations()`              | Getter                                         | Θ(1)                 |
| `getExecutionTimeNanos()`    | Getter                                         | Θ(1)                 |
| `getTemporalComplexity()`    | Getter                                         | Θ(1)                 |
| `getSampleQueries()`         | Getter                                         | Θ(1)                 |

---

### 3.7 `StationAreaFilter`

| **Method**                      | **Description**                                | **Time Complexity** |
|--------------------------------|------------------------------------------------|----------------------|
| `StationAreaFilter(...)`       | Constructor + normalization                    | Θ(1)                 |
| `normalizeCountry(...)`        | Normalize country (PT/ES/ALL)                  | Θ(1)                 |
| `getIsCity()/getIsMainStation()/getCountry()` | Getters               | Θ(1)                 |
| `apply(list)`                  | Filter list using stream                       | Θ(L)                 |
| `matches(station)`             | Boolean checks for station                     | Θ(1)                 |
| `toString()`                   | Build string                                   | Θ(1)                 |

---

### 3.8 `GeoAreaSearchUI`

| **Method**                 | **Description**                            | **Time Complexity**           |
|----------------------------|--------------------------------------------|-------------------------------|
| `GeoAreaSearchUI()`        | Constructor                                | Θ(1)                          |
| `run()`                    | UI + calls searchArea                      | **Θ(√n + m log m)**           |
| `printHeader()`            | Print text                                 | Θ(1)                          |
| `printlnSeparator()`       | Print text                                 | Θ(1)                          |
| `pause()`                  | Wait for enter                             | Θ(1)                          |
| `readDouble()`             | Read user input                            | Θ(1)                          |
| `readOptionalBoolean()`    | Read boolean                               | Θ(1)                          |
| `readOptionalString()`     | Read string                                | Θ(1)                          |
| `printResult(result)`      | Loop m stations and print                  | Θ(m)                          |

---

## 4. Summary Table of USEI08

| **Component**         | **Time Complexity**       |
|-----------------------|---------------------------|
| KD-Tree RangeSearch   | Θ(√n + m)                 |
| Filtering             | Θ(m)                      |
| Sorting               | Θ(m log m)                |
| **TOTAL**             | **Θ(√n + m log m)**       |

---

## 5. Final Conclusion

The implementation of USEI08 is asymptotically efficient:

### **Final Complexity: Θ(√n + m log m)**

Significantly outperforming a naïve **Θ(n)** linear scan over all 64k stations.
