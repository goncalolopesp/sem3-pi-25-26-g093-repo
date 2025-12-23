SET SERVEROUTPUT ON;

--------------------------------------------------------------------------------
-- USBD36 - Add a building to an existing facility
--------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION fn_usbd36(
    p_facility_id IN NUMBER,
    p_building_type_id IN NUMBER,
    p_name in VARCHAR
) RETURN SYS_REFCURSOR
IS
    l_rc SYS_REFCURSOR;
BEGIN
OPEN l_rc FOR
INSERT INTO BUILDING (facilityID, buildingTypeID, name)
VALUES (p_facility_id, p_building_type_id, p_name);
RETURN l_rc;
END fn_usbd36;
/

DECLARE
rc SYS_REFCURSOR;
    v_facility_id NUMBER;
    v_building_type_id NUMBER;
    v_name VARCHAR(100);
BEGIN
    rc := fn_usbd36(v_facility_id, v_building_type_id, v_name);

FETCH rc INTO v_facility_id, v_building_type_id, v_name;
DBMS_OUTPUT.PUT_LINE('FacilityID = ' || v_facility_id || '%');
DBMS_OUTPUT.PUT_LINE('BuildingTypeID = ' || v_building_type_id || '%');
DBMS_OUTPUT.PUT_LINE('Name = ' || v_name || '%');

CLOSE rc;
END;
/