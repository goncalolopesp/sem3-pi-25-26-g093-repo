BEGIN
    DELETE FROM Locomotive
    WHERE identifier LIKE 'T-%';

    DELETE FROM Train
    WHERE operatorID IN (SELECT operatorID FROM Operator WHERE vatNumber = 'PT123');

    DELETE FROM Operator
    WHERE vatNumber = 'PT123';

    DELETE FROM Locomotive_Model
    WHERE maker = 'Test';

    DELETE FROM Path
    WHERE pathID NOT IN (SELECT pathID FROM Train);

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Cleanup: All test data related to USBD33 has been deleted.');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Cleanup Error: ' || SQLERRM);
END;
/