SET SERVEROUTPUT ON;

--------------------------------------------------------------------------------
-- USBD26 - Vagões não utilizados num período temporal
--------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION fn_usbd26 (
    p_start_date IN DATE,
    p_end_date   IN DATE
) RETURN SYS_REFCURSOR
IS
    l_rc SYS_REFCURSOR;
BEGIN
OPEN l_rc FOR
SELECT
    W.WAGONID,
    W.IDENTIFIER,
    WM.CAPACITY,
    WM.VOLUMECAPACITY,
    WM.NUMBEROFBOGIES
FROM
    WAGON W
        JOIN WAGON_MODEL WM
             ON WM.WAGONMODELID = W.WAGONMODELID
        LEFT JOIN WAGON_FREIGHT WF
                  ON WF.WAGONID = W.WAGONID
        LEFT JOIN FREIGHT F
                  ON F.FREIGHTID = WF.FREIGHTID
                      AND F.FREIGHTDATE BETWEEN p_start_date AND p_end_date
WHERE
    F.FREIGHTID IS NULL;

RETURN l_rc;
END fn_usbd26;
/
--------------------------------------------------------------------------------
-- Teste USBD26
--------------------------------------------------------------------------------

DECLARE
rc      SYS_REFCURSOR;
    v_id    NUMBER;
    v_ident VARCHAR2(50);
    v_cap   NUMBER;
    v_vol   NUMBER;
    v_bog   NUMBER;
BEGIN
    rc := fn_usbd26(DATE '2025-10-03', DATE '2025-10-06');

    LOOP
FETCH rc INTO v_id, v_ident, v_cap, v_vol, v_bog;
        EXIT WHEN rc%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('WAGON: ' || v_id || ' - ' || v_ident);
END LOOP;

CLOSE rc;
END;
/
