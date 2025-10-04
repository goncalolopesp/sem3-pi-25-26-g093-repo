# Supplementary Specification (FURPS+)

## Functionality

- All images, diagrams, and figures (models, domain diagrams, reports) must be stored in **SVG format**.
- The system must validate business rules when importing and handling data (e.g., SKU verification, dates, quantities, and entity integrity).
- The system must allow reading and writing of CSV files (e.g., `wagons.csv`, `bays.csv`, `orders.csv`) robustly, reporting errors clearly and deterministically.
- The system must ensure **data persistence** using a **relational database (Oracle)** and **in-memory data structures**.
- The system must support **user authentication** (operators, planners, managers) to access main functionalities.
- The system must **log critical operations and audits** (e.g., quarantine processing and return inspections).

_This section defines functionalities that are transversal or not tied to a specific use case, including integrity, security, and logging._

---

## Usability

- The application interface (LAPR3 component) must be developed using **JavaFX 11**.
- Error and success messages must be **clear, deterministic, and informative**, specifying the exact file, line, and field involved.
- The interface must follow **consistent visual and terminological standards**, using coherent logistics and railway terminology.
- The system must provide **basic user assistance and documentation** (tooltips, Javadoc comments, README).

_Evaluates the user interface in terms of design, error prevention, consistency, and documentation._

---

## Reliability

- All business rules (e.g., FEFO/FIFO logic, expiry control, quarantine validation, CSV validation) must be strictly enforced.
- Data integrity between sessions must be maintained using **Oracle DB persistence** and/or **object serialization**.
- The system must include **password authentication** with the following requirements:
    - Minimum 7 characters
    - At least 3 uppercase letters
    - At least 2 digits
- The system must handle data import errors gracefully, loading valid records even when some are invalid.
- Quarantine and audit logs must be written to external files for accountability.

_Refers to data integrity, fault tolerance, recovery, and security mechanisms._

---

## Performance

- CSV files with up to **10,000 records** must be processed and validated within **10 seconds**.
- Picking plan generation (USEI03/USEI04) must execute in **under 2 seconds** for up to **5,000 boxes**.
- The system must handle multiple sequential imports (batch mode) without significant performance degradation.

_Assesses response time, processing speed, and scalability._

---

## Supportability

- The system must be modular and maintainable, following **Object-Oriented Programming (OOP)** best practices and **SOLID principles**.
- Recognized **coding standards** (e.g., CamelCase, indentation, naming conventions) must be used.
- All Java code must include **Javadoc comments**, and documentation must be generated automatically.
- **Unit tests** must be implemented for all non-I/O methods using **JUnit 5**, with coverage reports generated via **JaCoCo**.
- The application must be developed in **Java**, using **IntelliJ IDEA** or **NetBeans** as the IDE.
- The project must use a **clear package structure** (e.g., `domain`, `controller`, `ui`, `utils`, `repository`).
- Version control must follow the department’s guidelines (private **GitHub repository**, structured board, and Scrum process).
- The architecture should allow easy integration of future modules (e.g., new optimization heuristics or simulation features).

_Gathers characteristics like testability, maintainability, adaptability, and scalability._

---

## Others +

### Design Constraints

- The system must follow a modular architecture compatible with the diagram shown in **Figure 4** of the assignment document.
- UML models (domain, class, and use case diagrams) must be created using **Visual Paradigm** and exported in **SVG format**.
- Design patterns such as **MVC** and **DAO** must be applied to ensure separation of concerns.

---

### Implementation Constraints

- The application must be implemented in **Java**.
- The graphical user interface must use **JavaFX 11**.
- The database must be implemented in **Oracle DB**, with scripts generated in **PL/SQL**.
- C/Assembly modules may be used for hardware-level or low-level operations (ARQCP component).

---

### Interface Constraints

- The system must exchange data between components (PL/SQL, Java, C) using **well-defined file interfaces** (CSV or JSON).
- Import/export formats must strictly follow defined schemas and field names (`items.csv`, `returns.csv`, etc.).

---

### Physical Constraints

- The system must run on **Windows**, **macOS**, or **Linux**, requiring **JDK 17+**.
- Recommended system specifications: at least **8 GB RAM** and **2 GB free storage**.

---

**End of Document**
