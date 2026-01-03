# USBD34 - Freight and Train Association

## Description
As a Freight Manager, I want to associate a freight with a train. This User Story ensures that all wagons included in a specific freight are associated with the train, while maintaining strict business logic: **every wagon in the freight must belong to the same operator that owns/operates the train.**

## Implementation
The solution uses a PL/SQL Trigger to enforce data integrity at the database level.

### Trigger: `TRG_USBD34_CHECK_WAGON_OPERATOR`
- **Table**: `Wagon_Freight`
- **Event**: `BEFORE INSERT OR UPDATE`
- **Logic**:
    1. It retrieves the `operatorID` of the `Train` linked to the `Freight`.
    2. It retrieves the `operatorID` of the `Wagon` being added.
    3. It compares both IDs. If they are different, it raises an application error (`-20002`), preventing the association.

## Unit Testing
The validation was performed using an anonymous PL/SQL block that simulated two scenarios:
1.  **Positive Test**: Successfully associated a wagon and a train from the same operator.
2.  **Negative Test**: Attempted to associate a wagon from "Operator B" to a train from "Operator A". The trigger correctly blocked the operation.

## Data Cleanup
A dedicated cleanup script is provided to remove all mock data (VATs '999000111' and '999000222' and identifiers 'W-%-34') to ensure the environment remains consistent after testing.