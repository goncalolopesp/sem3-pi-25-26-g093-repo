DECLARE
    b BOOLEAN;
BEGIN
    -- Test invalid facility type
    b := fn_usbd35(-1, 'Invalid Facility', 10, 20);

    IF b = FALSE THEN
        DBMS_OUTPUT.PUT_LINE('USBD35 Test #1 Successful!');
    ELSE
        DBMS_OUTPUT.PUT_LINE('USBD35 Test #1 Failed!');
    END IF;
END;
/

