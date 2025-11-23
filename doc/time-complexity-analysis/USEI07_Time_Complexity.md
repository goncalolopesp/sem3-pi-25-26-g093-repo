# USEI07 — Efficiency Analysis (Build Balanced 2D-Tree)

## 1. Introduction

This document presents the **worst-case time complexity analysis** of the algorithms implemented in **USEI07 – Build a balanced 2D-Tree on Latitude/Longitude**.

The implemented solution uses:

- Creation of a **2D KD‑Tree** (`Tree<Double>`)

Definitions:

- **n** — number of stations stored in the KD‑Tree  
- **h** — height of the KD‑Tree (`~log n` on average, `n` in worst case)

---

## 2. Program Flow Overview

USEI07 performs:

1. `buildTree` — Runs a List of all LatKeys
2. `Insert` — Inserts node to the tree

Total expected complexity:  
**Θ(n log n)**

Worst case complexity:
**Θ($n^2$)**

---

## 3. Time Complexity Tables (per class)

### 3.1 `Tree<T>` (KD‑Tree)

| **Method**  | **Description**         | **Time Complexity** |
|-------------|-------------------------|---------------------|
| `getRoot()` | Returns tree root       | Θ(1)                |
| `isEmpty()` | Check if tree empty     | Θ(1)                |
| `Insert()`  | Insert node to the tree | Θ(log n)            |

---

## 4. Summary Table of USEI07

| Component               | Complexity (Expected Case) | Worst Case             |
|-------------------------|----------------------------|------------------------|
| LatKey list traversal   | Θ(n)                       | Θ(n)                   |
| Insert                  | Θ(log n) per insertion     | Θ(log n) per insertion |
| **TOTAL**               | **Θ(n log n)**             | **Θ($n^2$)**           |

---

## 5. Final Conclusion

The implementation of USEI07 (Build Balanced 2D Tree) follows classic KD‑Tree insertion behaviour:

### **Expected Complexity:**  
# **Θ(n log n)**  

### **Worst Case:**  
# **Θ($n^2$)**  
(happens only if the KD‑Tree isn't balanced)

This matches the theoretical performance of insertion in 2D Trees.

---

**End of document.**
