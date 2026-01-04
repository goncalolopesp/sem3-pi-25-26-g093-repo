SET SERVEROUTPUT ON;

--------------------------------------------------------------------------------
-- USBD35 - Clean Up
-- Removes facilities created for testing purposes
--------------------------------------------------------------------------------

BEGIN
    DELETE FROM FACILITY
    WHERE name LIKE 'Nova Facility%'
       OR name LIKE 'Invalid Facility%'
       OR name LIKE 'Facility Teste%';

    DBMS_OUTPUT.PUT_LINE('USBD35 CleanUp executed successfully.');
END;
/

