--------------------------------------------------------------------------------
-- Project: Projeto Integrador 2025/26
-- Fase: BDDAD
-- User Story: USBD15 - Número de linhas de um determinado endpoint.
-- Descrição:
--   Esta query conta o número de linhas de um endpoint selecionado.
--------------------------------------------------------------------------------

SELECT
    f.name AS endpoint_name,
    COUNT(rl.id) AS total_departing_lines
FROM RAILWAY_LINE rl
         JOIN FACILITY f
              ON rl.startFacilityID = f.id
WHERE UPPER(f.name) = UPPER('Porto Campanhã')
GROUP BY f.name
ORDER BY f.name;

--------------------------------------------------------------------------------
-- Fim da Query
--------------------------------------------------------------------------------
