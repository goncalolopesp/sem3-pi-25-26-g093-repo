# USBD45 – Add a new line

## Description
This user story allows the creation of a new railway line.
A line must always have at least one segment.

## Files
- USBD45_Function.sql – PL/SQL function implementation
- USBD45_UnitTests.sql – Unit tests
- USBD45_CleanUp.sql – Clean up script

## Notes
The function validates all foreign keys before insertion and returns
TRUE if the line and segment are created successfully.

