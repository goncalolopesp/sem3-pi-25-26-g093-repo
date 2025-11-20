--------------------------------------------------------------------------------
-- Project: Projeto Integrador 2025/26
-- Fase: BDDAD
-- User Story: USBD27 - Vagões de grãos utilizados em todos os comboios que transportaram grãos num período temporal
-- Descrição:
--   Esta query filtra os vagões de ceriais que foram utilizados em todos os comboios que transportaram grãos num certo período temporal, verificando a data e o tipo do frete.
--------------------------------------------------------------------------------

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
                   JOIN WAGON_FREIGHT WF1
                        ON WF1.FREIGHTID = F1.FREIGHTID
                   JOIN WAGON W1
                        ON W1.WAGONID = WF1.WAGONID
                   JOIN WAGON_MODEL WM1
                        ON WM1.WAGONMODELID = W1.WAGONMODELID
          WHERE
              WM1.WAGONMODELTYPEID = 7
            AND F1.FREIGHTDATE BETWEEN DATE '2025-10-03' AND DATE '2025-10-06'
      ) =
      (
          SELECT COUNT(DISTINCT F2.TRAINID)
          FROM FREIGHT F2
                   JOIN WAGON_FREIGHT WF2
                        ON WF2.FREIGHTID = F2.FREIGHTID
          WHERE
              WF2.WAGONID = W.WAGONID
            AND F2.FREIGHTDATE BETWEEN DATE '2025-10-03' AND DATE '2025-10-06'
      )

--------------------------------------------------------------------------------
-- Fim da Query
--------------------------------------------------------------------------------