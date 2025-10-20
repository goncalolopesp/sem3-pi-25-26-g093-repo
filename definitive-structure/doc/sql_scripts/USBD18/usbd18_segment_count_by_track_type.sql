--------------------------------------------------------------------------------
-- Project: Projeto Integrador 2025/26
-- Fase: BDDAD
-- User Story: USBD18 - Número de segmentos por tipo de via numa linha ferroviária
-- Descrição:
--   Esta query conta o número de segmentos existentes por tipo de via
--   (número de linhas/trilhos) em cada linha ferroviária registada.
--------------------------------------------------------------------------------

SELECT 
    rl.name AS railway_line_name,
    rs.numbertracks AS track_type,
    COUNT(rs.id) AS total_segments
FROM RAILWAY_SEGMENT rs
JOIN RAILWAY_LINE rl 
    ON rs.railwaylineid = rl.id
GROUP BY rl.name, rs.numbertracks
ORDER BY rl.name, rs.numbertracks;

--------------------------------------------------------------------------------
-- Fim da Query
--------------------------------------------------------------------------------
