CREATE OR REPLACE TRIGGER trg_wagon_freight_check
BEFORE INSERT OR UPDATE ON Wagon_Freight
FOR EACH ROW
DECLARE
    v_train_operator   NUMBER;
    v_wagon_operator   NUMBER;
BEGIN
    SELECT t.operatorID
    INTO v_train_operator
    FROM Freight f
    JOIN Train t ON t.trainID = f.trainID
    WHERE f.freightID = :NEW.freightID;

    SELECT operatorID
    INTO v_wagon_operator
    FROM Wagon
    WHERE wagonID = :NEW.wagonID;

    IF v_train_operator != v_wagon_operator THEN
        RAISE_APPLICATION_ERROR(-20001,
            'The wagon belongs to a different Operator.');
    END IF;
END;
/
