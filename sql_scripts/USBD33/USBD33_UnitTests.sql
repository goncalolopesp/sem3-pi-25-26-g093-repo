SET SERVEROUTPUT ON;

DECLARE
    v_path_id NUMBER;
    v_op_id NUMBER;
    v_train_id NUMBER;
    v_model_id NUMBER;
    PROCEDURE Attempt_Insert(p_train_id NUMBER, p_model_id NUMBER, p_op_id NUMBER, p_id VARCHAR2) IS
    BEGIN
        INSERT INTO Locomotive(operatorID, locomotiveModelID, trainID, name, serviceStart, identifier)
        VALUES (p_op_id, p_model_id, p_train_id, 'Loco ' || p_id, CURRENT_DATE, 'T-' || p_id);
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Input ' || p_id || ': Success.');
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            IF SQLCODE = -20001 THEN
                DBMS_OUTPUT.PUT_LINE('Input ' || p_id || ': Blocked (Limit Exceeded).');
            ELSE
                DBMS_OUTPUT.PUT_LINE('Input ' || p_id || ': Error - ' || SQLERRM);
            END IF;
        END;
BEGIN
    INSERT INTO Path(pathID) VALUES (DEFAULT) RETURNING pathID INTO v_path_id;
    INSERT INTO Operator(vatNumber, shortName, name) VALUES ('PT123', 'TST', 'Test') RETURNING operatorID INTO v_op_id;
    INSERT INTO Train(pathID, operatorID, startDate) VALUES (v_path_id, v_op_id, CURRENT_DATE) RETURNING trainID INTO v_train_id;

    SELECT MIN(locomotiveModelID) INTO v_model_id FROM Locomotive_Model;
    IF v_model_id IS NULL THEN
        INSERT INTO Locomotive_Model(power, topSpeed, trackType, tareWeight, maxWeight, numberOfBogies, maker, length, weight, width)
        VALUES (1000, 100, 1, 50, 60, 4, 'Test', 10, 50, 3) RETURNING locomotiveModelID INTO v_model_id;
    END IF;
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('--- STARTING VALIDATION ---');
    Attempt_Insert(v_train_id, v_model_id, v_op_id, '1');
    FOR i IN 2..10 LOOP
            Attempt_Insert(v_train_id, v_model_id, v_op_id, TO_CHAR(i));
    END LOOP;
    Attempt_Insert(v_train_id, v_model_id, v_op_id, '11_OVER');
END;
/