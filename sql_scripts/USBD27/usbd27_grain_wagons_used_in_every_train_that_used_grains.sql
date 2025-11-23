SET SERVEROUTPUT ON;

--------------------------------------------------------------------------------
-- USBD27 - Vagões de grãos usados em todos os comboios com grãos no período
--------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION fn_usbd27 (
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
WHERE
    WM.WAGONMODELTYPEID = 7
  AND (
          SELECT COUNT(DISTINCT F1.TRAINID)
          FROM FREIGHT F1
                   JOIN WAGON_FREIGHT WF1 ON WF1.FREIGHTID = F1.FREIGHTID
                   JOIN WAGON W1 ON W1.WAGONID = WF1.WAGONID
                   JOIN WAGON_MODEL WM1 ON WM1.WAGONMODELID = W1.WAGONMODELID
          WHERE
              WM1.WAGONMODELTYPEID = 7
            AND F1.FREIGHTDATE BETWEEN p_start_date AND p_end_date
      ) =
      (
          SELECT COUNT(DISTINCT F2.TRAINID)
          FROM FREIGHT F2
                   JOIN WAGON_FREIGHT WF2 ON WF2.FREIGHTID = F2.FREIGHTID
          WHERE
              WF2.WAGONID = W.WAGONID
            AND F2.FREIGHTDATE BETWEEN p_start_date AND p_end_date
      );

RETURN l_rc;
END fn_usbd27;
/
--------------------------------------------------------------------------------
-- Teste USBD27
--------------------------------------------------------------------------------

DECLARE
rc      SYS_REFCURSOR;
    v_id    NUMBER;
    v_ident VARCHAR2(50);
    v_cap   NUMBER;
    v_vol   NUMBER;
    v_bog   NUMBER;
BEGIN
    rc := fn_usbd27(DATE '2025-10-03', DATE '2025-10-06');

    LOOP
FETCH rc INTO v_id, v_ident, v_cap, v_vol, v_bog;
        EXIT WHEN rc%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('WAGON: ' || v_id || ' - ' || v_ident);
END LOOP;

CLOSE rc;
END;
/
