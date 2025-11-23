--------------------------------------------------------------------------------
-- INPUT DATA FOR USBD28 (auto PKs)
-- Creates:
--   • Two LOCOMOTIVE_MODEL rows for the same MODEL ('TestModel-X')
--     but with different GAUGE values (multi-gauge).
--   • One LOCOMOTIVE using that model.
--------------------------------------------------------------------------------

-- Create LOCOMOTIVE_MODEL with gauge 1435 for model 'TestModel-X'
DECLARE
v_model_id NUMBER;
BEGIN
INSERT INTO LOCOMOTIVE_MODEL (
    power, topSpeed, trackType, gauge, tareWeight,
    maxWeight, numberOfBogies, maker, model
)
VALUES (
           5000, 120, 1,      -- power, topSpeed, trackType
           1435,              -- gauge
           50, 100, 2,        -- tareWeight, maxWeight, numberOfBogies
           'MakerX',          -- maker
           'TestModel-X'      -- model name
       )
    RETURNING locomotiveModelID INTO v_model_id;
END;
/
--------------------------------------------------------------------------------

-- Create LOCOMOTIVE_MODEL with gauge 1668 for the same model 'TestModel-X'
DECLARE
v_model_id NUMBER;
BEGIN
INSERT INTO LOCOMOTIVE_MODEL (
    power, topSpeed, trackType, gauge, tareWeight,
    maxWeight, numberOfBogies, maker, model
)
VALUES (
           5000, 120, 1,
           1668,
           50, 100, 2,
           'MakerX',
           'TestModel-X'
       )
    RETURNING locomotiveModelID INTO v_model_id;
END;
/
--------------------------------------------------------------------------------

-- Create LOCOMOTIVE that uses model 'TestModel-X'
DECLARE
v_loco_id NUMBER;
BEGIN
INSERT INTO LOCOMOTIVE (
    operatorID, locomotiveModelID, trainID,
    name, startYear, identifier
)
VALUES (
           5,
           (SELECT MIN(locomotiveModelID)
            FROM LOCOMOTIVE_MODEL
            WHERE model = 'TestModel-X'),
           NULL,
           'TestLoco',
           2020,
           'TX01'
       )
    RETURNING locomotiveID INTO v_loco_id;
END;
/
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- USBD28 - Locomotives that support multiple gauges (filtered by name)
-- A locomotive is considered multi-gauge if its MODEL (in LOCOMOTIVE_MODEL.MODEL)
-- appears in more than one LOCOMOTIVE_MODEL row with different GAUGE values.
--------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION fn_usbd28 (
    p_locomotive_name IN LOCOMOTIVE.NAME%TYPE
) RETURN SYS_REFCURSOR
IS
    l_rc SYS_REFCURSOR;
BEGIN
OPEN l_rc FOR
SELECT
    L.LOCOMOTIVEID,   -- LOCOMOTIVEID
    L.NAME,           -- NAME
    L.IDENTIFIER,     -- IDENTIFIER
    L.OPERATORID,     -- OPERATOR
    LM.MODEL         -- MODEL
FROM
    LOCOMOTIVE L
        JOIN LOCOMOTIVE_MODEL LM
             ON LM.LOCOMOTIVEMODELID = L.LOCOMOTIVEMODELID
WHERE
  -- Filter by locomotive name
    L.NAME = p_locomotive_name
  -- Only models that exist with more than one distinct gauge
  AND LM.MODEL IN (
    SELECT MODEL
    FROM LOCOMOTIVE_MODEL
    GROUP BY MODEL
    HAVING COUNT(DISTINCT GAUGE) > 1
);

RETURN l_rc;
END fn_usbd28;
/

-------------------------------------------------
-- OUTPUT THE RESULTS TO THE CONSOLE
-------------------------------------------------

SET SERVEROUTPUT ON;

DECLARE
rc SYS_REFCURSOR;
    v_id NUMBER;
    v_name VARCHAR2(50);
    v_ident VARCHAR2(50);
    v_operator NUMBER;
    v_model VARCHAR2(50);
BEGIN
    rc := fn_usbd28('TestLoco');

    DBMS_OUTPUT.PUT_LINE('LOCOMOTIVEID | NAME | IDENTIFIER | OPERATOR | MODEL');
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------');

    LOOP
FETCH rc INTO v_id, v_name, v_ident, v_operator, v_model;
        EXIT WHEN rc%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE(
            v_id || ' | ' ||
            v_name || ' | ' ||
            v_ident || ' | ' ||
            v_operator || ' | ' ||
            v_model
        );
END LOOP;

CLOSE rc;
END;
/
--------------------------------------------------------------------------------