CREATE OR REPLACE TRIGGER TRG_USBD34_CHECK_WAGON_OPERATOR
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
            RAISE_APPLICATION_ERROR(-20002, 'Business Rule Violation: The wagon must belong to the same operator as the train.');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20003, 'Data Error: Freight or Wagon association not found.');
END;
/