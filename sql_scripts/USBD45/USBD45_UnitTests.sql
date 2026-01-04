DECLARE
    b BOOLEAN;
BEGIN
    b := fn_usbd45(
        'Linha Invalida',
        -1,   -- owner
        -1,   -- start facility
        -1,   -- end facility
        -1,   -- gauge
        1,    -- segment order
        1,    -- electrified
        100,  -- length
        1,    -- number of tracks
        2000  -- weight limit
    );

    IF b = FALSE THEN
        DBMS_OUTPUT.PUT_LINE('USBD45 Test #1 Successful!');
    ELSE
        DBMS_OUTPUT.PUT_LINE('USBD45 Test #1 Failed!');
    END IF;
END;
/

