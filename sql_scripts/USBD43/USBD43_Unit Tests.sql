CREATE OR REPLACE FUNCTION fn_usbd43_add_electric_model (
    p_model_id  IN NUMBER,
    p_voltage   IN NUMBER,
    p_frequence IN NUMBER
) RETURN BOOLEAN IS
BEGIN
    INSERT INTO Electric_Locomotive_Model (
        locomotiveModelID,
        voltage,
        frequence
    )
    VALUES (
        p_model_id,
        p_voltage,
        p_frequence
    );

    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END fn_usbd43_add_electric_model;
/

SET SERVEROUTPUT ON;

DECLARE
    b BOOLEAN;
BEGIN
    DBMS_OUTPUT.PUT_LINE('---------------------------------------------');
    DBMS_OUTPUT.PUT_LINE('USBD43 - Electric Locomotive Model Tests');
    DBMS_OUTPUT.PUT_LINE('---------------------------------------------');

    b := fn_usbd43_add_electric_model(-1, 25000, 50);
    IF b = FALSE THEN
        DBMS_OUTPUT.PUT_LINE('Test #1 (Invalid model ID): PASSED');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test #1 (Invalid model ID): FAILED');
    END IF;

    b := fn_usbd43_add_electric_model(3, NULL, 50);
    IF b = FALSE THEN
        DBMS_OUTPUT.PUT_LINE('Test #2 (Missing voltage): PASSED');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test #2 (Missing voltage): FAILED');
    END IF;

    b := fn_usbd43_add_electric_model(3, 25000, 50);
    IF b = TRUE THEN
        DBMS_OUTPUT.PUT_LINE('Test #3 (Valid electric model): PASSED');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test #3 (Valid electric model): FAILED');
    END IF;

    DBMS_OUTPUT.PUT_LINE('---------------------------------------------');
    DBMS_OUTPUT.PUT_LINE('All USBD43 tests executed.');
    DBMS_OUTPUT.PUT_LINE('---------------------------------------------');
END;
/
