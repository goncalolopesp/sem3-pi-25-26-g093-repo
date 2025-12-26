CREATE OR REPLACE TRIGGER trg_electric_locomotive_model_check
BEFORE INSERT OR UPDATE ON Electric_Locomotive_Model
FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_count
    FROM Locomotive_Model
    WHERE locomotiveModelID = :NEW.locomotiveModelID;

    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(
            -20010,
            'Referenced locomotive model does not exist.'
        );
    END IF;

    IF :NEW.voltage IS NULL OR :NEW.frequence IS NULL THEN
        RAISE_APPLICATION_ERROR(
            -20011,
            'Electric locomotive must have voltage and frequence defined.'
        );
    END IF;
END;
/
