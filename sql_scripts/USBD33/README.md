# USBD33 - Train Length Control

## Description
This User Story implements a business rule where a train cannot exceed a maximum of 10 units.
A "unit" is defined as any Locomotive or Wagon assigned to the train.

## Implementation Details
- **Object**: Row-level Trigger on `Locomotive`.
- **Constraint**: Counts the total sum of Locomotives and Wagons (via Freight) associated with the `trainID`.
- **Logic**: If `Count(Loco) + Count(Wagon) + 1 > 10`, the system prevents the insertion.

## How to Test
Execute the provided anonymous block. It performs:
1. Two successful sequences (Insertion 1 and sequence up to 10).
2. One validation check (Insertion 11) which is correctly blocked by the database trigger, ensuring data integrity.