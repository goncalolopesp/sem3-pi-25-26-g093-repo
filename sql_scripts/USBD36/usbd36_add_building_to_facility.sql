SET SERVEROUTPUT ON;

--------------------------------------------------------------------------------
-- USBD36 - Add a building to an existing facility
--------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION fn_usbd36(
    p_facility_id IN NUMBER,
    p_building_type_id IN NUMBER,
    p_name in VARCHAR2
) RETURN BOOLEAN IS
    f_count INT := 0;
    t_count INT := 0;
BEGIN
    SELECT COUNT(*) INTO f_count FROM FACILITY WHERE ID = p_facility_id;
    SELECT COUNT(*) INTO t_count FROM BUILDING_TYPE WHERE ID = p_building_type_id;

    IF f_count != 0 AND t_count != 0 THEN
        INSERT INTO BUILDING (facilityID, buildingTypeID, name)
        VALUES (p_facility_id, p_building_type_id, p_name);

        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END fn_usbd36;
/

DECLARE
    rc BOOLEAN;
BEGIN
    -- Test invalid IDs
    rc := fn_usbd36(-1, -1, 'Nome');

    IF rc = FALSE THEN
        DBMS_OUTPUT.PUT_LINE('Test #1 Successful!');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test #1 Failed!');
    END IF;
END;
/