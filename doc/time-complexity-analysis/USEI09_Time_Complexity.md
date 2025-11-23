# USEI09 — Efficiency Analysis (Nearest-N Proximity Search)

## 1. Introduction

This document presents the **worst-case time complexity analysis** of the algorithms implemented in **USEI09 – Proximity Search (Nearest‑N with Filters)**.

The implemented solution uses:

- A **2D KD‑Tree** (`Tree<Double>`)
- A **nearest‑N search algorithm with backtracking and pruning**
- A **max‑heap** of size `N` to maintain the closest candidates
- A **Haversine distance computation**
- Optional **early‑pruning filters**
- Construction of an immutable **NearestNSearchResult**

Definitions:

- **n** — number of stations stored in the KD‑Tree  
- **h** — height of the KD‑Tree (`~log n` on average, `n` in worst case)  
- **k** — number of nodes actually visited during nearest‑N search  
- **N** — number of nearest neighbours requested  

For balanced KD‑trees, typical nearest‑neighbour performance is **O(log n + k)** where `k << n`.

---

## 2. Program Flow Overview

USEI09 performs:

1. `validateNearestNParameters` — constant‑time validation  
2. `getRoot()` — obtain KD‑tree root  
3. Execute recursive **KD‑Tree nearest‑N search**  
4. Maintain **max‑heap (size N)** with best candidates  
5. **Prune subtrees** based on minimum possible distance  
6. Apply **filters early** during traversal  
7. Sort final `N` candidates  
8. Build `NearestNSearchResult`

Total expected complexity:  
**Θ(log n + k + N log N)**  
Worst‑case complexity:  
**Θ(n + N log N)** (degenerated KD‑Tree, no pruning)

---

## 3. Time Complexity Tables (per class)

### 3.1 `Tree<T>` (KD‑Tree — used only for root access)

| **Method**    | **Description**                    | **Time Complexity** |
|---------------|------------------------------------|---------------------|
| `getRoot()`   | Returns tree root                  | Θ(1)                |
| `isEmpty()`   | Check if tree empty                | Θ(1)                |

`NearestNSearchService` does **not** use insert, size, height, or range search.

---

### 3.2 `NearestNSearchService` (Core of USEI09)

| **Method**                  | **Description**                                             | **Time Complexity**                           |
|-----------------------------|-------------------------------------------------------------|-----------------------------------------------|
| Constructor                 | Store repo reference                                       | Θ(1)                                          |
| `searchNearestN(...)`       | Main Nearest‑N workflow                                    | **Θ(log n + k + N log N)** expected           |
| `nearestNSearch(...)`       | Recursive KD‑Tree NN search with pruning                   | **Θ(log n + k)** expected / Θ(n) worst        |
| `haversineDistance(...)`    | Distance computation                                        | Θ(1)                                          |
| `validateNearestNParameters`| Parameter checks                                            | Θ(1)                                          |

---

### 3.3 `NearestNSearchController`

| **Method**                | **Description**                      | **Time Complexity** |
|---------------------------|--------------------------------------|---------------------|
| Constructor(s)            | Setup                                | Θ(1)                |
| `searchNearestN(...)`     | Delegates to service                 | Θ(log n + k + N log N) |

---

### 3.4 `NearestNSearchResult`

| **Method**                | **Description**                             | **Time Complexity** |
|---------------------------|---------------------------------------------|---------------------|
| Constructor               | Copies lists, sets metadata                 | Θ(N)                |
| Getters                  | Retrieve stored lists and properties         | Θ(1)                |
| `getComplexity()`         | Formatted string                            | Θ(1)                |

---

### 3.5 `StationAreaFilter` (Used during traversal)

| **Method**             | **Description**                              | **Time Complexity** |
|------------------------|----------------------------------------------|---------------------|
| Constructors           | Normalize fields                             | Θ(1)                |
| `matches(station)`     | Evaluate all filters                         | Θ(1)                |
| `apply(list)`          | Stream-filter list (not used in NN search)   | Θ(N)                |

During nearest‑N search, **only `matches()` is used**, not `apply()`.

---

### 3.6 `StationDistance`

| **Method**             | **Description**      | **Time Complexity** |
|------------------------|----------------------|---------------------|
| Constructor            | Create record        | Θ(1)                |
| Getters                | Access fields        | Θ(1)                |

---

## 4. Summary Table of USEI09

| Component                     | Complexity (Expected Case) | Worst Case |
|------------------------------|-----------------------------|------------|
| KD‑Tree traversal            | Θ(log n + k)               | Θ(n)       |
| Max‑heap maintenance         | Θ(log N) per insertion     | same       |
| Filters (per candidate)      | Θ(1)                       | Θ(1)       |
| Sorting final N results      | Θ(N log N)                 | Θ(N log N) |
| **TOTAL**                    | **Θ(log n + k + N log N)** | **Θ(n + N log N)** |

---

## 5. Final Conclusion

The implementation of USEI09 (Nearest‑N KD‑Tree search) is asymptotically efficient and follows classic nearest‑neighbour KD‑Tree behaviour:

### **Expected Complexity:**  
# **Θ(log n + k + N log N)**  
(where `k` is the number of nodes actually visited)

### **Worst Case:**  
# **Θ(n + N log N)**  
(happens only if the KD‑Tree is degenerated or pruning is ineffective)

This matches the theoretical performance of nearest‑neighbour search in balanced KD‑Trees with bounded branching.

---

**End of document.**
