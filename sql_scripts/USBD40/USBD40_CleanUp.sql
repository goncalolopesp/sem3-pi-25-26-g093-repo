BEGIN
EXECUTE IMMEDIATE 'DROP PROCEDURE register_freight';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -4043 THEN
            RAISE;
END IF;
END;
/

-- Remover associações wagon ↔ freight criadas nos testes
DELETE FROM Wagon_Freight
WHERE freightID IN (
    SELECT freightID
    FROM Freight
    WHERE freightDate >= TRUNC(SYSDATE)
);

-- Remover freights criados nos testes
DELETE FROM Freight
WHERE freightDate >= TRUNC(SYSDATE);

-- Repor estado dos wagons
UPDATE Wagon
SET status = 'FREE';

COMMIT;
