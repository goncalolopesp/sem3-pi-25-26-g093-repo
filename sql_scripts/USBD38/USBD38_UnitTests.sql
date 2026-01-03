SET SERVEROUTPUT ON
DECLARE
v_id NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('==============================');
    DBMS_OUTPUT.PUT_LINE('USBD38 - Unit Tests START');
    DBMS_OUTPUT.PUT_LINE('==============================');

DELETE FROM Gauge
WHERE UPPER(
              REGEXP_REPLACE(TRIM(name), '\s+', ' ')
      ) = 'BITOLA TESTE';

COMMIT;

------------------------------------------------------------------
-- TEST 1: Inserção válida
------------------------------------------------------------------
BEGIN
        v_id := add_gauge('Bitola Teste', 1000);
        DBMS_OUTPUT.PUT_LINE(
            'TEST 1 PASSED: Valid gauge inserted (ID=' || v_id || ')'
        );
EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'TEST 1 FAILED: ' || SQLERRM
            );
END;

    ------------------------------------------------------------------
    -- TEST 2: Nome duplicado (maiúsculas)
    ------------------------------------------------------------------
BEGIN
        v_id := add_gauge('BITOLA TESTE', 1001);
        DBMS_OUTPUT.PUT_LINE(
            'TEST 2 FAILED: Duplicate name accepted.'
        );
EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'TEST 2 PASSED: Duplicate name rejected.'
            );
END;

    ------------------------------------------------------------------
    -- TEST 3: Nome duplicado (espaços)
    ------------------------------------------------------------------
BEGIN
        v_id := add_gauge('   bitola   teste   ', 1002);
        DBMS_OUTPUT.PUT_LINE(
            'TEST 3 FAILED: Name with spaces accepted.'
        );
EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'TEST 3 PASSED: Name with spaces rejected.'
            );
END;

    ------------------------------------------------------------------
    -- TEST 4: Nome NULL
    ------------------------------------------------------------------
BEGIN
        v_id := add_gauge(NULL, 1003);
        DBMS_OUTPUT.PUT_LINE(
            'TEST 4 FAILED: NULL name accepted.'
        );
EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'TEST 4 PASSED: NULL name rejected.'
            );
END;

    ------------------------------------------------------------------
    -- TEST 5: Nome só com espaços
    ------------------------------------------------------------------
BEGIN
        v_id := add_gauge('     ', 1004);
        DBMS_OUTPUT.PUT_LINE(
            'TEST 5 FAILED: Blank name accepted.'
        );
EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'TEST 5 PASSED: Blank name rejected.'
            );
END;

    ------------------------------------------------------------------
    -- TEST 6: Largura duplicada
    ------------------------------------------------------------------
BEGIN
        v_id := add_gauge('Outra Bitola Teste', 1000);
        DBMS_OUTPUT.PUT_LINE(
            'TEST 6 FAILED: Duplicate width accepted.'
        );
EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'TEST 6 PASSED: Duplicate width rejected.'
            );
END;

    ------------------------------------------------------------------
    -- TEST 7: Largura inválida
    ------------------------------------------------------------------
BEGIN
        v_id := add_gauge('Bitola Inválida', 0);
        DBMS_OUTPUT.PUT_LINE(
            'TEST 7 FAILED: Invalid width accepted.'
        );
EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'TEST 7 PASSED: Invalid width rejected.'
            );
END;

    DBMS_OUTPUT.PUT_LINE('==============================');
    DBMS_OUTPUT.PUT_LINE('USBD38 - Unit Tests END');
    DBMS_OUTPUT.PUT_LINE('==============================');
END;
/
