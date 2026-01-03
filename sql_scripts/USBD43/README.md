# USBD43 - Electric Locomotive Model Registration

## Description
This User Story allows the system to register specific technical data for Electric Locomotive Models, ensuring they are distinct from Diesel models.

## Implementation Details
- **Trigger**: `TRG_USBD43_CHECK_ELECTRIC_MODEL` on `Electric_Locomotive_Model`.
- **Constraint**: Prevents a model from existing in both `Electric_Locomotive_Model` and `Diesel_Locomotive_Model` tables simultaneously (Specialization Exclusivity).

## Tests
1. **Valid Entry**: Successfully registers voltage and frequency for a new model.
2. **Exclusivity Check**: Blocks the registration of electric specs if the model is already registered as diesel.