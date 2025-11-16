--------------------------------------------------------------------------------
-- Project: Projeto Integrador 2025/26
-- Fase: BDDAD
-- User Story: USBD07 - Consultar segmentos de linha por proprietário
-- Descrição:
--   Esta query lista todos os segmentos ferroviários pertencentes às linhas
--   geridas pela empresa "Infraestruturas de Portugal, SA". Para cada segmento,
--   são apresentados o nome da linha, as instalações inicial e final, o
--   comprimento, o limite de velocidade, o número de vias e se é eletrificada.
--------------------------------------------------------------------------------

SELECT 
    rl.name AS railway_line_name,
    rs.id AS segment_id,
    f_start.name AS start_facility,
    f_end.name AS end_facility,
    rs.length AS segment_length_km,
    rs.speedlimit AS speed_limit_kmh,
    rs.numbertracks AS number_of_tracks,
    rs.iselectrified AS electrified,
    e.name AS owner_name
FROM RAILWAY_SEGMENT rs
JOIN RAILWAY_LINE rl 
    ON rs.railwaylineid = rl.id
JOIN FACILITY f_start 
    ON rl.startfacilityid = f_start.id
JOIN FACILITY f_end 
    ON rl.endfacilityid = f_end.id
JOIN ENTERPRISE e 
    ON rl.ownerenterpriseid = e.id
WHERE e.name = 'Infraestruturas de Portugal, SA'
ORDER BY rl.name, rs.segmentorder;

--------------------------------------------------------------------------------
-- Fim da Query
--------------------------------------------------------------------------------
