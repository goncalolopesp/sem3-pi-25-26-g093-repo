SET SERVEROUTPUT ON;

CREATE OR REPLACE TYPE t_gauge_list AS TABLE OF NUMBER;
/

CREATE OR REPLACE FUNCTION fn_usbd37(
    p_train_id IN NUMBER
) RETURN BOOLEAN IS
    v_model_id NUMBER;
BEGIN
INSERT INTO Wagon_Model (wagonModelTypeID, maker, payload, capacity, volumeCapacity, length, height, maxSpeed, tareWeight, numberOfBogies)
VALUES (1, 'SimpleMaker', 50, 50, 100, 15, 4, 120, 20, 2)
    RETURNING wagonModelID INTO v_model_id;

INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID) VALUES (v_model_id, 1);

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
VALUES (1, v_model_id, 'WAGON-US37-SIMPLE', SYSDATE);

RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END fn_usbd37;
/