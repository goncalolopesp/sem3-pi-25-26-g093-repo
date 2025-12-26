CREATE OR REPLACE FUNCTION fn_usbd34_associate_freight (
    p_freight_id IN NUMBER
) RETURN BOOLEAN IS
    v_count NUMBER := 0;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM Freight
    WHERE freightID = p_freight_id;

    IF v_count = 0 THEN
        RETURN FALSE;
    END IF;

    FOR r IN (
        SELECT wagonID
        FROM Wagon_Freight
        WHERE freightID = p_freight_id
    ) LOOP
        UPDATE Wagon_Freight
        SET freightID = p_freight_id
        WHERE wagonID = r.wagonID
          AND freightID = p_freight_id;
    END LOOP;

    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END fn_usbd34_associate_freight;
/


SET SERVEROUTPUT ON;

DECLARE
    b BOOLEAN;
BEGIN
    DBMS_OUTPUT.PUT_LINE('---------------------------------------------');
    DBMS_OUTPUT.PUT_LINE('USBD34 - Trigger Validation Unit Tests');
    DBMS_OUTPUT.PUT_LINE('---------------------------------------------');

    b := fn_usbd34_associate_freight(-1);

    IF b = FALSE THEN
        DBMS_OUTPUT.PUT_LINE('Test #1 (Invalid Freight ID): PASSED');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test #1 (Invalid Freight ID): FAILED');
    END IF;

    b := fn_usbd34_associate_freight(1);

    IF b = TRUE THEN
        DBMS_OUTPUT.PUT_LINE('Test #2 (Valid freight trigger check): PASSED');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test #2 (Valid freight trigger check): FAILED');
    END IF;

    b := fn_usbd34_associate_freight(1);

    IF b = TRUE THEN
        DBMS_OUTPUT.PUT_LINE('Test #3 (Idempotent re-validation): PASSED');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test #3 (Idempotent re-validation): FAILED');
    END IF;

    b := fn_usbd34_associate_freight(2);

    IF b = TRUE THEN
        DBMS_OUTPUT.PUT_LINE('Test #4 (Second freight validation): PASSED');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test #4 (Second freight validation): FAILED');
    END IF;

    DBMS_OUTPUT.PUT_LINE('---------------------------------------------');
    DBMS_OUTPUT.PUT_LINE('All USBD34 tests executed.');
    DBMS_OUTPUT.PUT_LINE('---------------------------------------------');
END;
/

