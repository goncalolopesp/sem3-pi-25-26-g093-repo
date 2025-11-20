--------------------------------------------------------------------------------
-- Project: Projeto Integrador 2025/26
-- Fase: BDDAD
-- User Story: USBD26 - Vagões não utilizados em comboios num período temporal
-- Descrição:
--   Esta query filtra os vagões que não foram utilizados em nenhum comboio num certo período temporal, verificando a data e o id do frete.
--------------------------------------------------------------------------------

SELECT
    WAGON.WAGONID,
    WAGON.IDENTIFIER,
    WAGON_MODEL.CAPACITY,
    WAGON_MODEL.VOLUMECAPACITY,
    WAGON_MODEL.NUMBEROFBOGIES
FROM
    WAGON
        INNER JOIN WAGON_MODEL
                   ON WAGON_MODEL.WAGONMODELID = WAGON.WAGONMODELID

        LEFT JOIN WAGON_FREIGHT
                  ON WAGON_FREIGHT.WAGONID = WAGON.WAGONID

        LEFT JOIN FREIGHT
                  ON FREIGHT.FREIGHTID = WAGON_FREIGHT.FREIGHTID
                      AND FREIGHT.FREIGHTDATE BETWEEN DATE '2025-10-03' AND DATE '2025-10-06'

WHERE
    FREIGHT.FREIGHTID IS NULL

--------------------------------------------------------------------------------
-- Fim da Query
--------------------------------------------------------------------------------