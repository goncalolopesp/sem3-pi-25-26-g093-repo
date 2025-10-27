# USBD018 – Count segments in a given line by track type

## 1. Requirements Engineering

### 1.1. User Story Description
As a planner, I want to know the number of segments in a given line, grouped by track type.

### 1.2. Customer Specifications and Clarifications

**From specifications document:**
* A line consists of one or multiple line segments.
* Track types include at least single or double track.

**From the client clarifications:**
* Must return only segments of the selected line.
* Result grouped by track type.

### 1.3. Acceptance Criteria

* **AC1:** Results must include TrackType and SegmentCount.
* **AC2:** Only segments belonging to the selected line are counted.
* **AC3:** Track types with zero segments are not shown.
* **AC4:** If the line does not exist, result returns empty and clear message must be displayed.
* **AC5:** Query must use the database filled in USBD004.

### 1.4. Found out Dependencies
* USBD002
* USBD003
* USBD004

### 1.5 Input and Output Data

**Input Data:**
* Line identifier (name or ID)

**Output Data:**
* Aggregated results per track type, e.g.:

| TrackType | SegmentCount |
|-----------|--------------|
| Single    | 8            |
| Double    | 3            |

**_Other alternatives might exist._**

### 1.7 Other Relevant Remarks
* Consider creating a reusable View or Stored Procedure.
* Query performance should be considered for future scale.
