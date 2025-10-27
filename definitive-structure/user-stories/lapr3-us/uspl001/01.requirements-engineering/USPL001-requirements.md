# USPL02 – Cargo Handling Text-Based Interface

## 1. Requirements Engineering

### 1.1. User Story Description
As a **Station Storage Manager**, I intend to access the **Cargo Handling** functionality at a Railway Terminal and the results through a **user-friendly text-based interface**.

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> The LAPR3 component must provide the integration layer between the user and the functionalities developed in the ESINF component.
>
> The interface must allow the user to execute and view results of functionalities such as loading data from CSV files, managing warehouse operations, and performing calculations (e.g., from USEI01–USEI04).
>
> The interface must be **text-based** (menu-driven), not graphical, and must provide clear, formatted outputs.

**From client clarifications:**
> - The user interface should provide access to the functionalities implemented in ESINF (USEI01–USEI04).
> - The goal is to **simulate a console application** (text UI) that enables user interaction through numbered options.
> - The UI must **read user input**, validate it, and display clear feedback and results.
> - Error messages and prompts must be explicit and user-friendly.

### 1.3. Acceptance Criteria

* **AC1:** The system displays a main menu with options to access all functionalities (e.g., load data, execute pick path, generate reports).
* **AC2:** Each option corresponds to a functionality from the ESINF module (USEI01–USEI04).
* **AC3:** The interface validates user input (e.g., correct file paths, valid numeric options).
* **AC4:** All outputs are displayed in a formatted and readable text layout.
* **AC5:** The interface shows clear success/error messages for each operation.
* **AC6:** After each operation, the user is returned to the main menu until choosing “Exit.”
* **AC7:** No graphical components (GUI frameworks) are used; only Java console I/O.

### 1.4. Found out Dependencies

* **Depends on:**
    - `USEI01` to `USEI04` functionalities for execution logic.
    - `CSVParser` utility for data import.
    - `WarehouseService` or similar backend service class.

* **Used by:**
    - The Product Owner or Station Manager to operate and test the system.

### 1.5. Input and Output Data

**Input Data:**
* User inputs through console (numeric or textual).
* CSV file paths for loading data.
* Parameter inputs for test scenarios (e.g., choose picking strategy).

**Output Data:**
* Text-based results printed in the console:
    - Lists of processed data.
    - Picking path distances.
    - Error or confirmation messages.

### 1.6. System Sequence Diagram (SSD)

**Actors:** Station Storage Manager (User)  
**System:** Cargo Handling Text-Based Interface

**Main Flow:**
1. User launches the application.
2. System displays menu options.
3. User selects desired functionality (e.g., “Execute Pick Path Sequencing”).
4. System calls corresponding service (e.g., USEI04 algorithm).
5. System displays formatted results or error messages.
6. User returns to main menu or exits.

### 1.7. Other Relevant Remarks
* The **UI must separate logic from presentation** (Model-View-Controller concept).
* **Scanner** (for input) and **System.out.printf/println** (for output) are recommended.
* **Exception handling** (FileNotFoundException, InputMismatchException) must be applied.
* The interface acts as the **entry point** for integration and demonstration of ESINF functionalities.  
