# USBD002 – Create the Relational Model (logical level)

## 1. Requirements Engineering

### 1.1. User Story Description
As a Product Owner, I want the relational model to be created (logical level).

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**
* The relational model must support the requirements of:
  * Railway Infrastructure (tracks, lines, segments, owners)
  * Rolling stock (locomotives, wagons, operators)
* Must allow automatic generation of physical DDL script using Visual Paradigm.

**From the client clarifications:**
* All key entities must be uniquely identified.
* Referential integrity and cardinalities must be enforced via constraints.

### 1.3. Acceptance Criteria

* **AC1:** All necessary entities and relationships from Sections 2.2.1 and 2.2.2 must be included.
* **AC2:** Primary Keys (PK) and Foreign Keys (FK) must be defined.
* **AC3:** Constraints and business rules must be visible in Visual Paradigm.
* **AC4:** DDL must be auto-generated without manual corrections.
* **AC5:** Naming conventions must be applied consistently.
* **AC6:** Must support USBD03 and future queries (USBD05–USBD20).

### 1.4. Found out Dependencies
* USBD001

### 1.5 Input and Output Data

**Input Data:**
* Glossary data (USBD001)
* Infrastructure and rolling stock requirements

**Output Data:**
* Relational model/ERD in Visual Paradigm

**_Other alternatives might exist._**

### 1.7 Other Relevant Remarks
* Ensure scalability for electrification, gauges, operators, and train composition.
* Multi-locomotive and multi-wagon relationships should be representable.
