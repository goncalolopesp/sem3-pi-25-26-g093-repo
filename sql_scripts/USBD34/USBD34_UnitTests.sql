SET SERVEROUTPUT ON;

DECLARE
    v_path_id NUMBER;
    v_op_a_id NUMBER;
    v_op_b_id NUMBER;
    v_train_id NUMBER;
    v_model_id NUMBER;
    v_wagon_a_id NUMBER;
    v_wagon_b_id NUMBER;
    v_freight_id NUMBER;
    v_fac_1 NUMBER;
    v_fac_2 NUMBER;

    PROCEDURE log_test(p_name VARCHAR2, p_status VARCHAR2) IS
BEGIN
        DBMS_OUTPUT.PUT_LINE('Test ' || p_name || ': ' || p_status);
END;

BEGIN
    INSERT INTO Path(pathID) VALUES (DEFAULT) RETURNING pathID INTO v_path_id;
    INSERT INTO Operator(vatNumber, shortName, name) VALUES ('999000111', 'OPA', 'Operator A') RETURNING operatorID INTO v_op_a_id;
    INSERT INTO Operator(vatNumber, shortName, name) VALUES ('999000222', 'OPB', 'Operator B') RETURNING operatorID INTO v_op_b_id;
    INSERT INTO Train(pathID, operatorID, startDate) VALUES (v_path_id, v_op_a_id, CURRENT_DATE) RETURNING trainID INTO v_train_id;

    SELECT MIN(wagonModelID) INTO v_model_id FROM Wagon_Model;
    SELECT MIN(id) INTO v_fac_1 FROM Facility;
    SELECT MAX(id) INTO v_fac_2 FROM Facility;

    INSERT INTO Wagon(operatorID, wagonModelID, identifier, serviceStart)
    VALUES (v_op_a_id, v_model_id, 'W-OPA-34', CURRENT_DATE) RETURNING wagonID INTO v_wagon_a_id;

    INSERT INTO Wagon(operatorID, wagonModelID, identifier, serviceStart)
    VALUES (v_op_b_id, v_model_id, 'W-OPB-34', CURRENT_DATE) RETURNING wagonID INTO v_wagon_b_id;

    INSERT INTO Freight(trainID, startFacilityID, endFacilityID, freightDate)
    VALUES (v_train_id, v_fac_1, v_fac_2, CURRENT_DATE) RETURNING freightID INTO v_freight_id;

    DBMS_OUTPUT.PUT_LINE('--- STARTING USBD34 VALIDATION ---');

    BEGIN
        INSERT INTO Wagon_Freight(wagonID, freightID) VALUES (v_wagon_a_id, v_freight_id);
        log_test('1 (Matching Operators)', 'SUCCESS');
    EXCEPTION WHEN OTHERS THEN
            log_test('1 (Matching Operators)', 'FAILED - ' || SQLERRM);
    END;

    BEGIN
        INSERT INTO Wagon_Freight(wagonID, freightID) VALUES (v_wagon_b_id, v_freight_id);
        log_test('2 (Different Operators)', 'FAILED (Should have been blocked)');
    EXCEPTION WHEN OTHERS THEN
        IF SQLCODE = -20002 THEN
            log_test('2 (Different Operators)', 'SUCCESS (Blocked as expected)');
        ELSE
            log_test('2 (Different Operators)', 'ERROR - ' || SQLERRM);
        END IF;
    END;

    COMMIT;
END;
/