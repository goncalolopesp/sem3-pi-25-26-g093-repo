# USBD001 - Create data dictionary / glossary.

## 1. Requirements Engineering

### 1.1. User Story Description

As a Product Owner, I want a data dictionary/glossary to be created.

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

**From the client clarifications:**

### 1.3. Acceptance Criteria

* **AC1:** The glossary must list all key entities, attributes, and relationships referenced in the railway infrastructure and rolling stock sections of the project description.
* **AC2:** Each term must include a clear, concise, and unambiguous definition.
* **AC3:** The glossary must be stored and shared in a structured format (e.g., table, Markdown, or spreadsheet), easily maintainable and version-controlled in the GitHub repository.
* **AC4:** All terms must use consistent naming conventions (e.g., CamelCase, no duplicates).
* **AC5:** The glossary must serve as the foundation for the conceptual and relational data models (USBD02).

### 1.4. Found out Dependencies

* None.

### 1.5 Input and Output Data

**Input Data:**

* Railway infrastructure and rolling stock documentation (Sections 2.2.1 and 2.2.2).
* Naming conventions and data modelling standards.

**Output Data:**

**A Data Dictionary / Glossary document containing:**
* Entity name
* Attribute name and type
* Description / definition
* Example or reference section (optional)

**_Other alternatives might exist._**

### 1.7 Other Relevant Remarks

* The glossary will serve as the base vocabulary for all subsequent models (conceptual, logical, and physical).
* It should be maintained as a living document, updated whenever new entities or attributes are introduced.
* Recommended format: Markdown table or Visual Paradigm glossary export.