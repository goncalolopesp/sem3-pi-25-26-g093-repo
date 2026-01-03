# USBD38 - Add a New Gauge

## Description
This functionality allows a Planner to add a new gauge (rail width) to the system. The gauge determines the track width used by trains, and it must be unique based on both its name and width. The `add_gauge` function ensures that these rules are followed, including validations for name uniqueness, width validity, and appropriate formatting.

## Acceptance Criteria

### Gauge Name:
- **Cannot be NULL or just whitespace.**
- The name is normalized by trimming spaces and converting it to uppercase.
- **Duplicates (case-insensitive and with extra spaces) are not allowed.**

### Gauge Width:
- **Must be a positive number.**
- Duplicate widths are not allowed.

## Functionality

### Validation of Name:
- Checks that the name is not NULL, empty, or consisting only of spaces.
- The name is normalized by removing extra spaces and converting it to uppercase.

### Validation of Width:
- Ensures that the width is a positive number greater than zero.

### Uniqueness Check:
- Ensures that no other gauge with the same name or width already exists in the database.

Once all validations pass, the gauge is inserted into the `Gauge` table with the normalized name and width.

## Unit Tests

The following tests are performed to ensure the correctness of the function:

### Test 1: Valid Insertion
- **Goal**: Test inserting a valid gauge with a unique name and width.
- **Expected Outcome**: The gauge is successfully inserted, and its ID is returned.

### Test 2: Duplicate Name (Case Insensitive)
- **Goal**: Ensure that a gauge with a name differing only by case is rejected.
- **Expected Outcome**: An error is raised indicating that the name already exists.

### Test 3: Duplicate Name (With Extra Spaces)
- **Goal**: Ensure that names differing only by spaces are treated as duplicates.
- **Expected Outcome**: An error is raised indicating that the name already exists.

### Test 4: NULL Name
- **Goal**: Ensure that NULL names are rejected.
- **Expected Outcome**: An error is raised indicating that the name cannot be NULL.

### Test 5: Blank Name (Only Spaces)
- **Goal**: Ensure that blank names are rejected.
- **Expected Outcome**: An error is raised indicating that the name cannot be blank.

### Test 6: Duplicate Width
- **Goal**: Ensure that a gauge with a duplicate width is rejected.
- **Expected Outcome**: An error is raised indicating that the width already exists.

### Test 7: Invalid Width (Zero or Negative)
- **Goal**: Ensure that widths of zero or negative values are rejected.
- **Expected Outcome**: An error is raised indicating that the width must be greater than zero.

## Conclusion
This functionality ensures the integrity of the gauge data by validating and enforcing business rules for both the gauge name and width. It guarantees that the system does not accept duplicates or invalid data, and provides clear error messages when these rules are violated.
