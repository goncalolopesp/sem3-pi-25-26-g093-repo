--------------------------------------------------------------------------------
-- USBD35 - Register a new facility
--------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION fn_usbd35(
    p_facility_type_id IN NUMBER,
    p_name             IN VARCHAR2,
    p_pos_x            IN NUMBER,
    p_pos_y            IN NUMBER
) RETURN BOOLEAN IS
    t_count INT := 0;
BEGIN
    -- Validate facility type
    SELECT COUNT(*) INTO t_count
    FROM FACILITY_TYPE
    WHERE id = p_facility_type_id;

    IF t_count != 0 THEN
        INSERT INTO FACILITY (facilityTypeID, name, posX, posY)
        VALUES (p_facility_type_id, p_name, p_pos_x, p_pos_y);

        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END fn_usbd35;
/

