--------------------------------------------------------------------------------
-- USBD45 - Add a new line (a line must have at least one segment)
--------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION fn_usbd45(
    p_name               IN VARCHAR2,
    p_owner_id           IN NUMBER,
    p_start_facility_id  IN NUMBER,
    p_end_facility_id    IN NUMBER,
    p_gauge_id           IN NUMBER,
    -- mandatory first segment
    p_segment_order      IN NUMBER,
    p_is_electrified     IN NUMBER,
    p_length             IN NUMBER,
    p_number_tracks      IN NUMBER,
    p_weight_limit       IN NUMBER
) RETURN BOOLEAN IS
    o_count INT := 0;
    s_count INT := 0;
    e_count INT := 0;
    g_count INT := 0;
    v_line_id NUMBER;
BEGIN
    -- Validate foreign keys (PK names corrected)
    SELECT COUNT(*) INTO o_count
    FROM OWNER
    WHERE OWNERID = p_owner_id;

    SELECT COUNT(*) INTO s_count
    FROM FACILITY
    WHERE ID = p_start_facility_id;

    SELECT COUNT(*) INTO e_count
    FROM FACILITY
    WHERE ID = p_end_facility_id;

    SELECT COUNT(*) INTO g_count
    FROM GAUGE
    WHERE GAUGEID = p_gauge_id;

    IF o_count != 0
       AND s_count != 0
       AND e_count != 0
       AND g_count != 0 THEN

        -- Insert railway line
        INSERT INTO RAILWAY_LINE (
            ownerID,
            startFacilityID,
            endFacilityID,
            gaugeID,
            name
        ) VALUES (
            p_owner_id,
            p_start_facility_id,
            p_end_facility_id,
            p_gauge_id,
            p_name
        )
        RETURNING ID INTO v_line_id;

        -- Insert mandatory first segment
        INSERT INTO RAILWAY_SEGMENT (
            railwayLineID,
            segmentOrder,
            isElectrified,
            length,
            numberTracks,
            weightLimit
        ) VALUES (
            v_line_id,
            p_segment_order,
            p_is_electrified,
            p_length,
            p_number_tracks,
            p_weight_limit
        );

        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END fn_usbd45;
/

