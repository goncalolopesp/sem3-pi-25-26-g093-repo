# Supplementary Specification (FURPS+)

## Functionality

* All the images/figures produced should be recorded in SVG format

_Specifies functionalities that:  
&nbsp; &nbsp; (i) are common across several US/UC;  
&nbsp; &nbsp; (ii) are not related to US/UC, namely: Audit, Reporting and Security._

## Usability

* The application interface must be developed in JavaFX 11.

_Evaluates the user interface. It has several subcategories,
among them: error prevention; interface aesthetics and design; help and
documentation; consistency and standards._


## Reliability

* Business rules validation must be respected when recording and updating data. (Data Integrity)
* The application should use object serialization to ensure data persistence between two runs of the application.
* The application must use password authentication with specific complexity requirements (minimum 7 characters, including 3 uppercase letters and 2 digits). (Security)

_Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures._


## Performance

N/A

_Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._


## Supportability

* The class structure must be designed for easy maintenance and future feature additions following best Object-Oriented (OO) practices(adopt best practices for identify requirements, software analysis and design) (adopt recognized coding standards) (use Javadoc to generate useful documentation for Java code). (Maintainability)
* Javadoc comments must be used for code documentation. (Documentation)
* Unit tests must be implemented for all methods (excluding Input/Output methods) using the JUnit 5 framework. Test coverage reports should be generated using the JaCoCo plugin. (Testability)
* The application must be developed in Java using IntelliJ IDE or NetBeans. (Development Environment)

_The supportability requirements gathers several characteristics, such as:
testability, adaptability, maintainability, compatibility,
configurability, installability, scalability and more._

## Others +

### Design Constraints

N/A

_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._


### Implementation Constraints

* The application must be developed in Java language.
* The application's graphical interface is to be developed in JavaFX 11.

_Specifies or constraints the code or construction of a system such
such as: mandatory standards/patterns, implementation languages,
database integrity, resource limits, operating system._

### Interface Constraints

N/A

_Specifies or constraints the features inherent to the interaction of the
system being developed with other external systems._

### Physical Constraints

N/A

_Specifies a limitation or physical requirement regarding the hardware used to house the system, as for example: material, shape, size or weight._