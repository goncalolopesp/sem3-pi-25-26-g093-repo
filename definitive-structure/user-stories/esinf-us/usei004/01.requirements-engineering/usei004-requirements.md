
# USEI04 – Pick Path Sequencing

## 1. Requirements Engineering

### 1.1. User Story Description
As a **picker**, I want to plan the task of picking up several boxes, sequenced using a **2D layout (aisles & bays)**, so I can minimize walking distance starting from the entrance at the main corridor, with no return leg.

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> Warehouses are composed of aisles and bays organized as coordinates (aisle, bay).  
> The entrance is at coordinate (0,0).
>
> Two strategies must be implemented for sequencing:
> - **Strategy A:** Deterministic sweep (sorted ascending by aisle).
> - **Strategy B:** Nearest neighbour (greedy) using distance D.
>
> The total distance must be calculated as the sum of all partial distances between consecutive coordinates using the provided distance rules.

**Clarifications from the client:**
> - Duplicate bays must be **merged** before calculating the path.
> - The output must include the **path sequence** and the **total distance** for each strategy.
> - Distance formula:
    >   - Same aisle: D = |b1 − b2|
>   - Different aisle: D = b1 + |a1 − a2| × 3 + b2
> - Start point is always (0,0).
> - Only the **forward leg** of the trip is considered (no return).

### 1.3. Acceptance Criteria

* **AC1:** Input is the list of unique bay coordinates from the picking plan (USEI03).
* **AC2:** Duplicate bays are merged by summing quantities.
* **AC3:** Both Strategy A and Strategy B are executed for the same set of coordinates.
* **AC4:** For each strategy, the full **pick sequence** (ordered list of coordinates) is displayed.
* **AC5:** For each strategy, the **total distance** is calculated using the provided D formula.
* **AC6:** The result includes a comparison showing which strategy yields a shorter total distance.
* **AC7:** Distances are computed correctly from the entrance (0,0) to each coordinate and between consecutive bays.

### 1.4. Found out Dependencies

* **Depends on:**
    - `USEI03` (Picking Plan generation) for input bay list.
    - Warehouse layout and coordinate model (aisle, bay).

* **Used by:**
    - The LAPR3 text interface (USPL02) to display results to the user.

### 1.5. Input and Output Data

**Input Data:**
* Set of coordinates representing bays (aisle, bay).
* Distance rules (given by the system).

**Output Data:**
* For **Strategy A:**
    - Ordered bay sequence (ascending by aisle).
    - Total distance.
* For **Strategy B:**
    - Ordered bay sequence (nearest neighbour).
    - Total distance.
* Comparison result (which strategy is shorter).

### 1.6. System Sequence Diagram (SSD)

**Actors:** Picker (User)  
**System:** Pick Path Sequencing Module

**Main Flow:**
1. Picker provides or loads picking plan (set of bays).
2. System merges duplicate bays.
3. System executes Strategy A (Deterministic sweep).
4. System executes Strategy B (Nearest neighbour).
5. System calculates and compares total distances.
6. System returns results: both paths and distances.

### 1.7. Other Relevant Remarks

* Implementation should use **modular methods** (e.g., `calculateDistance()`, `nearestNeighbourPath()` etc.).
* Output should be **clearly formatted** for console display (for integration with USPL02).
* **O(n²)** complexity is acceptable for nearest-neighbour implementation.
* Future improvement: support for bidirectional paths (with return leg).
* Recommended testing with small datasets to verify both strategies produce correct results.