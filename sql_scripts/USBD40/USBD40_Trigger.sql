CREATE OR REPLACE FUNCTION register_freight (
    p_train_id          IN Freight.trainID%TYPE,
    p_start_facility    IN Freight.startFacilityID%TYPE,
    p_end_facility      IN Freight.endFacilityID%TYPE,
    p_freight_date      IN Freight.freightDate%TYPE,
    p_wagon_model_id    IN Wagon.wagonModelID%TYPE,
    p_quantity          IN NUMBER
) RETURN NUMBER
IS
    v_available  NUMBER;
    v_freight_id Freight.freightID%TYPE;
BEGIN
    -- Validação da quantidade
    IF p_quantity IS NULL OR p_quantity <= 0 THEN
        RAISE_APPLICATION_ERROR(
            -20100,
            'Validation Error: Quantity must be greater than zero.'
        );
END IF;

    -- Contar wagons disponíveis
SELECT COUNT(*)
INTO v_available
FROM Wagon
WHERE wagonModelID = p_wagon_model_id
  AND status = 'FREE'
  AND currentFacilityID = p_start_facility;

IF v_available < p_quantity THEN
        RAISE_APPLICATION_ERROR(
            -20101,
            'Business Rule Violation: Not enough free wagons at the departure facility.'
        );
END IF;

    -- Criar Freight
INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES (p_train_id, p_start_facility, p_end_facility, p_freight_date)
    RETURNING freightID INTO v_freight_id;

-- Associar wagons
INSERT INTO Wagon_Freight (wagonID, freightID)
SELECT wagonID, v_freight_id
FROM Wagon
WHERE wagonModelID = p_wagon_model_id
  AND status = 'FREE'
  AND currentFacilityID = p_start_facility
  AND ROWNUM <= p_quantity;

-- Atualizar estado dos wagons
UPDATE Wagon
SET status = 'IN_FREIGHT'
WHERE wagonID IN (
    SELECT wagonID
    FROM Wagon_Freight
    WHERE freightID = v_freight_id
);

RETURN v_freight_id;
END;
/
