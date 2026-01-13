SET SERVEROUTPUT ON;

CREATE OR REPLACE FUNCTION fn_usbd37(
    p_operator_id   IN NUMBER,
    p_identifier    IN VARCHAR2,
    p_maker         IN VARCHAR2,
    p_payload       IN NUMBER
) RETURN BOOLEAN IS
    v_model_id NUMBER;

BEGIN
    -- 1. Criar o Modelo
INSERT INTO Wagon_Model (wagonModelTypeID, maker, payload, capacity, volumeCapacity, length, height, maxSpeed, tareWeight, numberOfBogies)
VALUES (1, p_maker, p_payload, 50, 100, 15, 4, 120, 20, 2)
    RETURNING wagonModelID INTO v_model_id;

-- 2. Associar a Bitola
INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID)
VALUES (v_model_id, 1);

-- 3. Criar o Vagão
INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart, currentFacilityID, status)
VALUES (p_operator_id, v_model_id, p_identifier, SYSDATE, 1, 'Active');

RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
RETURN FALSE;
END fn_usbd37;
/