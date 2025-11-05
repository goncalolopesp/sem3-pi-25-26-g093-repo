--------------------------------------------------------------------------------
-- Project: Projeto Integrador 2025/26
-- Fase: BDDAD
-- User Story: USBD12 - Consultar vagões por tipo, bitola e operador
-- Descrição:
--   Esta query apresenta a lista de modelos de vagões, com o tipo de vagão,
--   a bitola (gauge) suportada e o operador ferroviário responsável.
--   Inclui também a capacidade de carga e volume de cada modelo.
--------------------------------------------------------------------------------

SELECT 
    wm.rollingstockmodelid AS wagon_id,
    wt.typename AS wagon_type,
    g.width_mm AS gauge_width_mm,
    g.description AS gauge_description,
    o.name AS operator_name,
    wm.capacity AS load_capacity_tons,
    wm.volumecapacity AS volume_capacity_m3
FROM WAGON_MODEL wm
JOIN WAGON_TYPE wt 
    ON wm.wagontypeid = wt.id
JOIN ROLLING_STOCK_MODEL rsm 
    ON wm.rollingstockmodelid = rsm.id
JOIN OPERATOR o 
    ON rsm.operatorenterpriseid = o.enterpriseid
JOIN ROLLINGSTOCK_SUPPORTED_GAUGE rsg 
    ON rsm.id = rsg.rollingstockmodelid
JOIN GAUGE g 
    ON rsg.gaugeid = g.id
ORDER BY o.name, wt.typename, wm.rollingstockmodelid;

--------------------------------------------------------------------------------
-- Fim da Query
--------------------------------------------------------------------------------
