# USEI05 - Returns & Quarantine

## 1. Requirements Engineering

### 1.1. User Story Description

As a quality operator, I want returned goods to be placed in quarantine so that I can inspect them in the reverse order they arrived (latest first), process them by discarding or restocking, and create an audit file detailing the processing done to each product.
### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

>	Returned items cannot immediately re-enter inventory; they must go first go through a quarantine inspection phase.

>	Items are inspected in reverse order of arrival (LIFO) - the most recent returns are processed first.

>   Returns can have four types of reasons:
> Customer remorse → can be restocked.
> Damaged → must be discarded.
> Expired → must be discarded.  
> Cycle count → restocked after verification.

**From the client clarifications:**

### 1.3. Acceptance Criteria

* **AC1:** All returned items are loaded into a quarantine structure (stack) ordered by descending timestamp (LIFO).
* **AC2:** Each return is processed in reverse order of arrival.
* **AC3:** Items with “Customer remorse” or “Cycle count” are restocked; “Damaged” and “Expired” are discarded.
* **AC4:** Restocked items are reinserted into inventory according to the FEFO/FIFO rules from USEI01.
* **AC5:** For each processed return, the system writes an entry to an external log file containing:
  timestamp | returnId | sku | action=Restocked/Discarded | qty | [qtyRestocked, qtyDiscarded]
* **AC6:** The system handles partial restocks (some items discarded, others restocked).
* **AC7:** The quarantine is emptied once all items are processed.

### 1.4. Found out Dependencies

* **Depends on:**
    - `USEI01` (Warehouse Inventory initialization and FEFO/FIFO handling logic).
    - Access to data from `returns.csv`.

### 1.5 Input and Output Data

**Input Data:**
* `returns.csv` containing:
    - `returnId, SKU, qty, reason, timestamp, expiryDate`
* Current warehouse inventory data structure (from USEI01).

**Output Data:**
* Updated warehouse inventory with reinserted items.
* Log file (e.g. `returns_audit_log.txt`) with all actions performed:


### 1.6. System Sequence Diagram (SSD)

**This user story does not directly involve system interaction (no actor ↔ system messages).
The activity is conceptual and performed by the development team to produce documentation, not executable functionality..**

### 1.7 Other Relevant Remarks

* The **stack structure** ensures “latest first” processing.
* Logging is critical for **auditing and traceability** of warehouse operations.
* Use of **Java I/O (BufferedWriter or Files.write)** recommended for log creation.
* **Timestamps** must be generated dynamically using system time.
* Potential future enhancement: allow automatic report generation (summarizing restocks vs. discards).  
