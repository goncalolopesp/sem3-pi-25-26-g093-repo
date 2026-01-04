SET SERVEROUTPUT ON;

--------------------------------------------------------------------------------
-- USBD45 - Clean Up
-- Removes test railway lines and corresponding segments
--------------------------------------------------------------------------------

BEGIN
    -- Delete segments first (FK dependency)
    DELETE FROM RAILWAY_SEGMENT
    WHERE railwayLineID IN (
        SELECT id
        FROM RAILWAY_LINE
        WHERE name LIKE 'Linha Teste%'
           OR name LIKE 'Linha Invalida%'
           OR name LIKE 'Linha Teste Final%'
    );

    -- Delete test railway lines
    DELETE FROM RAILWAY_LINE
    WHERE name LIKE 'Linha Teste%'
       OR name LIKE 'Linha Invalida%'
       OR name LIKE 'Linha Teste Final%';

    DBMS_OUTPUT.PUT_LINE('USBD45 CleanUp executed successfully.');
END;
/

