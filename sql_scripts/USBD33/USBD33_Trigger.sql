CREATE OR REPLACE TRIGGER TRG_USBD33_CHECK_TRAIN_COUNT
BEFORE INSERT OR UPDATE OF trainID ON Locomotive
    FOR EACH ROW
DECLARE
v_total_units NUMBER;
BEGIN
    IF :NEW.trainID IS NULL THEN
        RETURN;
END IF;

SELECT (
           (SELECT COUNT(*) FROM Locomotive WHERE trainID = :NEW.trainID) +
           (SELECT COUNT(*) FROM Wagon_Freight wf
                                     JOIN Freight f ON wf.freightID = f.freightID
            WHERE f.trainID = :NEW.trainID) + 1
           ) INTO v_total_units FROM DUAL;

IF v_total_units > 10 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Maximum train capacity exceeded (10 units).');
END IF;
END;
/