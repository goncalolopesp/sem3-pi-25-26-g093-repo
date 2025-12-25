SET SERVEROUTPUT ON;

--------------------------------------------------------------------------------
-- USBD41 - Remove a freight from a planned train
--------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION fn_usbd41(
    p_train_id IN NUMBER
) RETURN SYS_REFCURSOR
IS
    l_rc SYS_REFCURSOR;
BEGIN
DELETE FROM FREIGHT WHERE TRAINID = p_train_id;
RETURN l_rc;
END fn_usbd41;
/

DECLARE
rc SYS_REFCURSOR;
    v_train_id NUMBER;
BEGIN
    rc := fn_usbd41(v_train_id);

FETCH rc INTO v_train_id;
DBMS_OUTPUT.PUT_LINE('TrainID = ' || v_train_id || '%');

CLOSE rc;
END;
/
