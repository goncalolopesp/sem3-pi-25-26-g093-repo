SET SERVEROUTPUT ON
DECLARE
v_freight_id      NUMBER;
    v_count           NUMBER;

    v_train_id        Train.trainID%TYPE;
    v_start_facility  Facility.id%TYPE;
    v_end_facility    Facility.id%TYPE;
    v_wagon_model_id  Wagon.wagonModelID%TYPE;
BEGIN
    DBMS_OUTPUT.PUT_LINE('==============================');
    DBMS_OUTPUT.PUT_LINE('USBD40 - Unit Tests START');
    DBMS_OUTPUT.PUT_LINE('==============================');

    ------------------------------------------------------------------
    -- Preparação de dados comuns
    ------------------------------------------------------------------
SELECT MIN(trainID)
INTO v_train_id
FROM Train;

SELECT id
INTO v_start_facility
FROM Facility
WHERE name = 'Leixões';

SELECT id
INTO v_end_facility
FROM Facility
WHERE name = 'Darque';

SELECT wagonModelID
INTO v_wagon_model_id
FROM Wagon
WHERE status = 'FREE'
  AND currentFacilityID = v_start_facility
  AND ROWNUM = 1;

------------------------------------------------------------------
-- TEST 1: Inserção válida
------------------------------------------------------------------
BEGIN
        v_freight_id := register_freight(
            p_train_id       => v_train_id,
            p_start_facility => v_start_facility,
            p_end_facility   => v_end_facility,
            p_freight_date   => DATE '2025-10-03',
            p_wagon_model_id => v_wagon_model_id,
            p_quantity       => 1
        );

SELECT COUNT(*)
INTO v_count
FROM Wagon_Freight
WHERE freightID = v_freight_id;

IF v_count = 1 THEN
            DBMS_OUTPUT.PUT_LINE(
                'TEST 1 PASSED: Freight registered with wagon.'
            );
ELSE
            DBMS_OUTPUT.PUT_LINE(
                'TEST 1 FAILED: Wagon not associated.'
            );
END IF;
EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'TEST 1 FAILED (EXCEPTION): ' || SQLERRM
            );
END;

    ------------------------------------------------------------------
    -- TEST 2: Quantidade inválida (0)
    ------------------------------------------------------------------
BEGIN
        v_freight_id := register_freight(
            v_train_id,
            v_start_facility,
            v_end_facility,
            DATE '2025-10-03',
            v_wagon_model_id,
            0
        );

        DBMS_OUTPUT.PUT_LINE(
            'TEST 2 FAILED: Quantity zero accepted.'
        );
EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'TEST 2 PASSED: Invalid quantity rejected.'
            );
END;

    ------------------------------------------------------------------
    -- TEST 3: Wagons insuficientes
    ------------------------------------------------------------------
BEGIN
        v_freight_id := register_freight(
            v_train_id,
            v_start_facility,
            v_end_facility,
            DATE '2025-10-03',
            v_wagon_model_id,
            9999
        );

        DBMS_OUTPUT.PUT_LINE(
            'TEST 3 FAILED: Insufficient wagons accepted.'
        );
EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'TEST 3 PASSED: Insufficient wagons rejected.'
            );
END;

    DBMS_OUTPUT.PUT_LINE('==============================');
    DBMS_OUTPUT.PUT_LINE('USBD40 - Unit Tests END');
    DBMS_OUTPUT.PUT_LINE('==============================');
END;
/
