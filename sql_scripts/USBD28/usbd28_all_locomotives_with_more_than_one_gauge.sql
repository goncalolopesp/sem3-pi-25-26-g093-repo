--------------------------------------------------------------------------------
-- Project: Projeto Integrador 2025/26
-- Fase: BDDAD
-- User Story: USBD28 - Locomotivas de um operador com mais de uma bitola
-- Descrição:
--   Esta query filtra as locomotivas de um operador específico que possuem mais de uma bitola associada ao seu modelo.
--------------------------------------------------------------------------------

SELECT
    L.LOCOMOTIVEID,
    L.IDENTIFIER,
    L.NAME,
    O.NAME AS OPERATOR_NAME
FROM
    LOCOMOTIVE L
        JOIN LOCOMOTIVE_MODEL LM
             ON LM.LOCOMOTIVEMODELID = L.LOCOMOTIVEMODELID
        JOIN OPERATOR O
             ON O.OPERATORID = L.OPERATORID
WHERE
    O.OPERATORID = 5
  AND
    (SELECT COUNT(*)
     FROM LOCOMOTIVE_MODEL LMG
     WHERE LMG.LOCOMOTIVEMODELID = LM.LOCOMOTIVEMODELID
    ) > 1

--------------------------------------------------------------------------------
-- Fim da Query
--------------------------------------------------------------------------------