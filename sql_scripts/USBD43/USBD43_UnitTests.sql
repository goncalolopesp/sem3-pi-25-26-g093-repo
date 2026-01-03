SET SERVEROUTPUT ON;

DECLARE
    v_model_elec_id NUMBER;
    v_model_diesel_id NUMBER;

    PROCEDURE log_test(p_name VARCHAR2, p_status VARCHAR2) IS
BEGIN
        DBMS_OUTPUT.PUT_LINE('Test ' || p_name || ': ' || p_status);
END;

BEGIN
    DBMS_OUTPUT.PUT_LINE('--- STARTING USBD43 TESTS ---');

    INSERT INTO Locomotive_Model(power, topSpeed, trackType, tareWeight, maxWeight, numberOfBogies, maker, length, weight, width)
    VALUES (3000, 160, 1, 80, 90, 4, 'SIE', 20, 85, 3) RETURNING locomotiveModelID INTO v_model_elec_id;

    INSERT INTO Locomotive_Model(power, topSpeed, trackType, tareWeight, maxWeight, numberOfBogies, maker, length, weight, width)
    VALUES (2000, 120, 1, 70, 75, 4, 'ALS', 18, 72, 3) RETURNING locomotiveModelID INTO v_model_diesel_id;

    INSERT INTO Diesel_Locomotive_Model(locomotiveModelID, fuelCapacity) VALUES (v_model_diesel_id, 500);

    BEGIN
        INSERT INTO Electric_Locomotive_Model(locomotiveModelID, voltage, frequence)
        VALUES (v_model_elec_id, 25000, 50);
        log_test('1 (Valid Electric Model)', 'SUCCESS');
    EXCEPTION WHEN OTHERS THEN
        log_test('1 (Valid Electric Model)', 'FAILED - ' || SQLERRM);
    END;

    BEGIN
        INSERT INTO Electric_Locomotive_Model(locomotiveModelID, voltage, frequence)
        VALUES (v_model_diesel_id, 1500, 0);
        log_test('2 (Electric-Diesel Conflict)', 'FAILED (Should have been blocked)');
    EXCEPTION WHEN OTHERS THEN
        IF SQLCODE = -20010 THEN
            log_test('2 (Electric-Diesel Conflict)', 'SUCCESS (Blocked as expected)');
        ELSE
            log_test('2 (Electric-Diesel Conflict)', 'ERROR - ' || SQLERRM);
        END IF;
    END;

    COMMIT;
END;
/