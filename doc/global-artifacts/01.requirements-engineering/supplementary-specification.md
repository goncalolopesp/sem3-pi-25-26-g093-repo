# Supplementary Specification (FURPS+)

This document describes the non-functional requirements and constraints of the “Logistics on Rails” system. Requirements are structured according to the FURPS+ model.

---

## F — Functionality

- All images, diagrams, and figures (models, domain diagrams, reports) must be stored in **SVG format**.
- The system must validate business rules when importing and handling data (e.g., SKU verification, dates, quantities, and entity integrity).
- The system must allow reading and writing of **CSV files** (e.g., `wagons.csv`, `bays.csv`, `orders.csv`) robustly, reporting errors clearly and deterministically.
- The system must ensure **data persistence** using a **relational database (Oracle)** and **in-memory data structures**.
- The system must **log critical operations and audits** (e.g., quarantine processing and return inspections).

_These functionalities are transversal to the system and ensure correctness, data integrity, and traceability._

---

## U — Usability

- The primary interface (Sprint 1, LAPR3) must be a **text-based interface**, as required in the assignment.
- Error and success messages must be **clear, deterministic, and informative**, specifying the exact file, line, and field involved.
- The interface must follow **consistent visual and terminological standards**, using coherent logistics and railway terminology.
- The system must provide **basic user assistance and documentation** (tooltips if applicable, Javadoc comments, README).

_Ensures efficient usability, consistent terminology, and adequate user guidance._

---

## R — Reliability

- All business rules (e.g., FEFO/FIFO logic, expiry control, quarantine validation, CSV validation) must be **strictly enforced**.
- Data integrity between sessions must be maintained using **Oracle DB persistence**.
- The system must handle import errors gracefully, **loading valid records even when some records are invalid**.
- Quarantine and audit logs must be **written to external files** for accountability.

_Focus on data integrity, fault tolerance, and operational reliability._

---

## P — Performance

- CSV files with up to **10,000 records** must be processed and validated within **10 seconds**.
- Picking plan generation (USEI03/USEI04) must run in **under 2 seconds** for up to **5,000 boxes**.
- The system must handle **multiple sequential imports** without significant performance degradation.

_Addresses execution time, resource usage, and scalability._

---

## S — Supportability

- The system must be modular and maintainable, following **Object-Oriented Programming** best practices and **SOLID principles**.
- **Coding standards** (e.g., CamelCase, indentation, naming conventions) must be applied consistently.
- All Java code must include **Javadoc comments**, and documentation must be automatically generated.
- **Unit tests** must be implemented for non-I/O methods using **JUnit 5**, with coverage reports using **JaCoCo**.
- The application must be developed in **Java**, using **IntelliJ IDEA** or **NetBeans** as IDE.
- The project must use a **clear package structure** (e.g., `domain`, `controller`, `ui`, `utils`, `repository`).
- Version control must follow the department guidelines (**private GitHub repo**, Scrum Board, structured sprints).
- The architecture should allow **easy integration of future modules** (e.g., new heuristics or simulation).

_Defines testability, maintainability, documentation requirements and extensibility._

---

## + Additional Constraints

### Design Constraints

- The system must follow a modular architecture compatible with the components diagram from the assignment.
- UML models (domain, class, and use case diagrams) must be created using **Visual Paradigm** and exported in SVG.
- Software architecture must apply **MVC** and **DAO** patterns.

---

### Implementation Constraints

- Application backend must be implemented in **Java**.
- Database must be implemented in **Oracle DB**, with scripts generated in **PL/SQL**.
- C/Assembly modules may be used for low-level station communication (ARQCP component).

---

### Interface Constraints

- Integration between PL/SQL, Java, and C must be performed via **CSV file interfaces**.
- Import/export formats must strictly follow the predefined schemas (`items.csv`, `returns.csv`, etc.).

---

### Physical Constraints

- The system must run on **Windows**, **macOS**, or **Linux**, requiring **JDK 17+**.
- Recommended minimum system specs: **8 GB RAM** and **2 GB free storage**.

---

**End of Document**
