SET SERVEROUTPUT ON;
DECLARE;
BEGIN
    -- Tenta criar um vagão: Operador 1, Matrícula 'VAGAO-TESTE-99', Marca 'IsepRail', Carga 80
    resultado := fn_usbd37(1, 'VAGAO-TESTE-99', 'IsepRail', 80);

    IF resultado THEN
        DBMS_OUTPUT.PUT_LINE('Sucesso: O vagão foi criado!');
COMMIT;
ELSE
        DBMS_OUTPUT.PUT_LINE('Falha: Ocorreu um erro.');
END IF;
END;
/