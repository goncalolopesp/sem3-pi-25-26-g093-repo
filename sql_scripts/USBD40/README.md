# USBD40 - Freight Registration with Wagons

## Description
This User Story allows the system to register a freight, associate it with a train, and allocate a specified quantity of available wagons from a start facility to the freight. It ensures that sufficient free wagons are available before registration.

## Implementation Details
- **Function**: `register_freight`
- **Parameters**:
    - `p_train_id`: The ID of the train associated with the freight.
    - `p_start_facility`: The ID of the start facility where the wagons are located.
    - `p_end_facility`: The ID of the end facility where the freight will be delivered.
    - `p_freight_date`: The date the freight will be transported.
    - `p_wagon_model_id`: The model ID of the wagons being allocated to the freight.
    - `p_quantity`: The number of wagons to be allocated to the freight.

- **Validation**:
    - Ensures that the quantity of wagons requested is greater than zero.
    - Verifies that enough free wagons are available at the start facility before proceeding.

- **Outcome**:
    - Registers a new freight in the `Freight` table.
    - Associates the specified wagons with the freight in the `Wagon_Freight` table.
    - Updates the status of the allocated wagons to `IN_FREIGHT`.

## Unit Tests

The following tests are performed to ensure the correctness of the function:

### Test 1: Valid Insertion
- **Goal**: Test registering a valid freight with a specified quantity of available wagons.
- **Expected Outcome**: The freight is successfully registered, and the wagons are associated with the freight.

### Test 2: Invalid Quantity (Zero)
- **Goal**: Ensure that a freight registration is rejected if the quantity of wagons is zero.
- **Expected Outcome**: An error is raised indicating that the quantity must be greater than zero.

### Test 3: Insufficient Wagons
- **Goal**: Ensure that the function rejects a freight registration if there are not enough free wagons available at the start facility.
- **Expected Outcome**: An error is raised indicating that there are not enough free wagons at the start facility.
