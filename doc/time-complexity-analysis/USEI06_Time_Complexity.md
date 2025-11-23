
# USEI06 — Time Complexity Analysis (Latitude, Longitude, and Time‑Zone Group Index Search)

## 1. Introduction

This document presents the time‑complexity analysis of the **actual implementation** of **USEI06 — Time‑Zone Index and Windowed Queries**, based strictly on the code provided.

Although the User Story requires accelerated lookups using BST/AVL trees, the current controller **does not use any tree structure** for searching.  
Queries instead perform:

- A **full linear scan** of the station list, followed by
- **Sorting** of the filtered results.

Therefore, all query operations have worst‑case and expected‑case time complexity:

## **Θ(n log n)**

During bootstrap, the system fills multiple BSTs, but these indexes are **not used** by the controller.

---

## 2. Program Flow Overview

USEI06 consists of two distinct phases:

### **A. Bootstrap Phase (CSV Loading)**

For each row in the CSV:

1. Validate row fields — Θ(1)
2. Create a `CompleteStation` — Θ(1)
3. Append station to repository list — Θ(1)
4. Insert keys into three BSTs:
    - Latitude BST
    - Longitude BST
    - Time‑Zone Group BST

Assuming the BST is **balanced**, insertions cost Θ(log n).  
If **not balanced**, worst‑case Θ(n).

Thus bootstrap complexity is:

- **Θ(n log n)** if BSTs remain balanced
- **Θ(n²)** in worst‑case degenerated BSTs

### **B. Query Phase (Controller Methods)**

Each controller method follows this pattern:

1. Parameter validation — Θ(1)
2. Iterate over all stations — **Θ(n)**
3. Apply simple comparisons/filters — Θ(1) each
4. Sort the resulting subset — **Θ(m log m)** (m can be n)
5. Return a Map with execution time and result — Θ(1)

Since m = n in the worst case:

## **Total per query = Θ(n log n)**

---

## 3. Time‑Complexity by Class

---

# 3.1 `CompleteStationRepository`

### Internal structures

| Structure | Use in implementation | Notes |
|----------|------------------------|-------|
| `List<CompleteStation>` | **Used by all queries** | Linear storage |
| `BST<LatKey>` | Filled at bootstrap | **Never queried** |
| `BST<LonKey>` | Filled at bootstrap | **Never queried** |
| `BST<TzgKey>` | Filled at bootstrap | **Never queried** |
| `Tree<Double>` | Allocated but unused | No impact |

### Relevant Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `addStation(cs)` | Append to list | Θ(1) |
| `addLatKey(key)` | Insert into BST | Θ(h) → balanced: Θ(log n), worst: Θ(n) |
| `addLonKey(key)` | Insert into BST | Θ(h) |
| `addTzgKey(key)` | Insert into BST | Θ(h) |
| `getStations()` | Return list reference | Θ(1) |

### Bootstrap Total

- Balanced BSTs: **Θ(n log n)**
- Worst‑case BSTs: **Θ(n²)**

---

# 3.2 `CompleteStation`

All getters and comparisons are simple field accesses:

- `getLatitude()`, `getLongitude()`, `getCountry()`, etc. → **Θ(1)**
- `equals()` → compares a few fields → **Θ(1)**

No hidden overhead.

---

# 3.3 `CompleteStationIndexController` (Core of USEI06)

All search methods:

- `getByLatitudeRange`
- `getByLongitudeRange`
- `getByTimeZoneGroup`
- `getByTimeZoneGroupWindow`
- `getByTimeZoneGroupAndCountry`

share the same structure:

### Step‑by‑Step Complexity

| Step | Description | Complexity |
|------|-------------|------------|
| Validation | null/empty checks | Θ(1) |
| Access station list | `repo.getStations()` | Θ(1) |
| Linear scan | Iterate over all stations | **Θ(n)** |
| Attribute comparisons | Simple numeric/string checks | Θ(1) |
| Sorting | Multi‑attribute comparator | **Θ(m log m)** (m ≤ n) |
| Build Map | Store timing + results | Θ(1) |

Since result size may be the whole dataset:

## **Total query complexity = Θ(n log n)**

---

## 4. Summary Table of USEI06 (Actual Implementation)

| Component | Complexity | Notes |
|-----------|------------|-------|
| Bootstrap Loading | Θ(n log n) (balanced) / Θ(n²) (worst BST shape) | BST insertions dominate |
| Latitude Query | **Θ(n log n)** | Full scan + sort |
| Longitude Query | **Θ(n log n)** | Full scan + sort |
| Time‑Zone Group Query | **Θ(n log n)** | Full scan + sort |
| TZG Window Query | **Θ(n log n)** | Full scan + sort |
| TZG + Country Query | **Θ(n log n)** | Full scan + sort |

### **Overall complexity of USEI06:**
# **Θ(n log n)**

---

## 5. Final Conclusion

The current implementation of USEI06:

- **Builds BST indexes during bootstrap**
- **Does NOT use these indexes for any query**

As a result:

- All queries behave as **linear scans followed by sorting**
- Providing **Θ(n log n)** complexity per request
- Falling short of the User Story requirement for “fast, reproducible indexed lookups”

Despite this, the implementation is internally consistent and predictable in performance.

---