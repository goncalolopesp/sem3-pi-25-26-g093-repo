CREATE OR REPLACE TRIGGER TRG_USBD43_CHECK_ELECTRIC_MODEL
BEFORE INSERT ON Electric_Locomotive_Model
FOR EACH ROW
DECLARE
    v_is_diesel NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_is_diesel
    FROM Diesel_Locomotive_Model
    WHERE locomotiveModelID = :NEW.locomotiveModelID;

    IF v_is_diesel > 0 THEN
        RAISE_APPLICATION_ERROR(-20010, 'Business Rule Violation: Model cannot be both Electric and Diesel.');
    END IF;

    IF :NEW.voltage IS NULL OR :NEW.frequence IS NULL THEN
        RAISE_APPLICATION_ERROR(-20011, 'Validation Error: Voltage and Frequency are required.');
    END IF;
END;
/