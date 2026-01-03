SET SERVEROUTPUT ON;

DECLARE
b BOOLEAN;
BEGIN
    b := fn_usbd37(1);

    IF b THEN
        DBMS_OUTPUT.PUT_LINE('Test Successful: Wagon Inserted!');
COMMIT;
ELSE
        DBMS_OUTPUT.PUT_LINE('Test Failed!');
END IF;
END;
/
