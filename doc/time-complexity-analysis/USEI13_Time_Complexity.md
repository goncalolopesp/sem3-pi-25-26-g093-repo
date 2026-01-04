# USEI13 — Rail Hub Centrality Analysis

## 1. Introduction

This document presents the **worst-case time complexity analysis** of the algorithms implemented in **USEI13 — Rail Hub Centrality Analysis**.

Definitions:

- **n** — number of stations (vertices) in the graph
- **m** — number of edges in the graph
- **e** — number of edges connected to a node

---

## 2. Program Flow Overview

**USEI13** performs:

1. **Calculate Betweenness** — calculates the betweenness of a node — **Θ($n^2$ * (n * m * log(n)))**
2. **Calculate Harmonic Closeness** — calculates the harmonic closeness of a node — **Θ(n-1)**
3. **Calculate Degree** — retrieves the degree — **Θ(1)**
4. **Calculate Strength** — calculates the strength of a node — **Θ(e)**
5. **Calculate Hub Score** — calculates the hub score — **Θ(1)**

**Total expected complexity:**  
**Θ(n * (n$^2$ * (n * m * log(n))) + (n-1) + e)**  
**Worst-case complexity:**  
**Θ(n$^5$ * log(n))**

---

## 3. Time Complexity Tables

### 3.1 `HubCentralityController`

| **Method**            | **Description**                                | **Time Complexity**         |
|-----------------------|------------------------------------------------|-----------------------------|
| **Constructor**       | Initializes the controller                     | Θ(1)                        |
| `HubCentrality()`     | Executes the overall calculation of Hub Score  | Θ(n * e)                    |
| `Betweenness()`       | Calculates the betweenness of a station        | Θ(n$^2$ * (n * m * log(n))) |
| `Closeness()`         | Calculates the harmonic closeness of a station | Θ(n-1)                      |

---


## 4. General Complexity Analysis

### **Expected Complexity:**
**Θ(n * ($n^2$ * (n * m * log(n))) + (n-1) + e)** , where **n** is the number of stations (vertices), **m** is the number of edges in the graph and **e** is the number of edges connected to each vertex

### **Worst Case:**
**Θ($n^5$ * log(n))**, which occurs when the graph is dense (m = n$^2$)

---

**End of document.**