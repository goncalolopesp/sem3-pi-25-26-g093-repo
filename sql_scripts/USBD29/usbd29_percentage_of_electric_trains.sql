SET SERVEROUTPUT ON;

--------------------------------------------------------------------------------
-- USBD29 - Percentagem de locomotivas elétricas de um operador
--------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION fn_usbd29 (
    p_operator_id IN NUMBER
) RETURN SYS_REFCURSOR
IS
    l_rc SYS_REFCURSOR;
BEGIN
OPEN l_rc FOR
SELECT
    ROUND(
            100 * COUNT(ELM.LOCOMOTIVEMODELID) / COUNT(*),
            2
    ) AS PERCENT_ELECTRIC
FROM
    LOCOMOTIVE L
        LEFT JOIN LOCOMOTIVE_MODEL LM
                  ON LM.LOCOMOTIVEMODELID = L.LOCOMOTIVEMODELID
        LEFT JOIN ELECTRIC_LOCOMOTIVE_MODEL ELM
                  ON ELM.LOCOMOTIVEMODELID = LM.LOCOMOTIVEMODELID
WHERE
    L.OPERATORID = p_operator_id;

RETURN l_rc;
END fn_usbd29;
/
--------------------------------------------------------------------------------
-- Teste USBD29
--------------------------------------------------------------------------------

DECLARE
rc      SYS_REFCURSOR;
    v_pct   NUMBER;
BEGIN
    rc := fn_usbd29(5);

FETCH rc INTO v_pct;
DBMS_OUTPUT.PUT_LINE('Percentagem = ' || v_pct || '%');

CLOSE rc;
END;
/
