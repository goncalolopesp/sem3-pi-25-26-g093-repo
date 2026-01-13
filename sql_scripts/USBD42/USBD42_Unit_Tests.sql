SET SERVEROUTPUT ON;

DECLARE
b BOOLEAN;
BEGIN
    -- Test: Register movement with both arrival and departure times
    b := fn_usbd42(1, 1, SYSTIMESTAMP, SYSTIMESTAMP + INTERVAL '1' HOUR);

    IF b THEN
        DBMS_OUTPUT.PUT_LINE('Test Successful: Train Movement Registered!');
COMMIT;
ELSE
        DBMS_OUTPUT.PUT_LINE('Test Failed!');
END IF;
END;
/

 -- SELECT * FROM Train_Movement ORDER BY movementID DESC;