SET SERVEROUTPUT ON;

--------------------------------------------------------------------------------
-- USBD41 - Remove a freight from a planned train
--------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION fn_usbd41(
    p_train_id IN NUMBER
) RETURN BOOLEAN IS
    id_count INT := 0;
BEGIN
    SELECT COUNT(*) INTO id_count FROM FREIGHT WHERE TRAINID = p_train_id;

    IF id_count != 0 THEN
        DELETE FROM WAGON_FREIGHT WHERE FREIGHTID IN (SELECT FREIGHTID FROM FREIGHT WHERE TRAINID = p_train_id);
        DELETE FROM FREIGHT WHERE TRAINID = p_train_id;
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END fn_usbd41;
/

DECLARE
    b BOOLEAN;
BEGIN
    --Test Invalid IDs
    b := fn_usbd41(-1);

    IF b = FALSE THEN
        DBMS_OUTPUT.PUT_LINE('Test #1 Successful!');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test #1 Failed!');
    END IF;
END;
/
