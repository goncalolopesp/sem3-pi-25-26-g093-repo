CREATE OR REPLACE FUNCTION add_gauge (
    p_name     IN Gauge.name%TYPE,
    p_width_mm IN Gauge.width_mm%TYPE
) RETURN NUMBER
IS
    v_count      NUMBER;
    v_norm_name  Gauge.name%TYPE;
    v_gauge_id   Gauge.gaugeID%TYPE;
BEGIN
    -- Validação do nome
    IF p_name IS NULL THEN
        RAISE_APPLICATION_ERROR(-20001, 'Gauge name cannot be NULL.');
END IF;

    v_norm_name := UPPER(
        REGEXP_REPLACE(TRIM(p_name), '\s+', ' ')
    );

    IF v_norm_name IS NULL OR v_norm_name = '' THEN
        RAISE_APPLICATION_ERROR(
            -20002,
            'Gauge name cannot be empty or blank.'
        );
END IF;

    -- Validação da largura
    IF p_width_mm IS NULL OR p_width_mm <= 0 THEN
        RAISE_APPLICATION_ERROR(
            -20003,
            'Gauge width must be greater than zero.'
        );
END IF;

    -- Verificar duplicação de nome (ignorando espaços e maiúsculas)
SELECT COUNT(*)
INTO v_count
FROM Gauge
WHERE UPPER(
              REGEXP_REPLACE(TRIM(name), '\s+', ' ')
      ) = v_norm_name;

IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(
            -20004,
            'Business Rule Violation: A gauge with this name already exists.'
        );
END IF;

    -- Verificar duplicação de largura
SELECT COUNT(*)
INTO v_count
FROM Gauge
WHERE width_mm = p_width_mm;

IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(
            -20005,
            'Business Rule Violation: A gauge with this width already exists.'
        );
END IF;

    -- Inserção
INSERT INTO Gauge (name, width_mm)
VALUES (v_norm_name, p_width_mm)
    RETURNING gaugeID INTO v_gauge_id;

RETURN v_gauge_id;
END;
/
