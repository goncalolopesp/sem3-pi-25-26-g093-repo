--------------------------------------------------------------------------------
-- Project: Projeto Integrador 2025/26
-- Fase: BDDAD
-- User Story: USBD29 - Percentagem de locomotivas elétricas de um operador
-- Descrição:
--   Esta query calcula a percentagem de locomotivas elétricas pertencentes a um operador específico em relação ao total de locomotivas desse operador.
--------------------------------------------------------------------------------

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
    L.OPERATORID = 5;

--------------------------------------------------------------------------------
-- Fim da Query
--------------------------------------------------------------------------------