BEGIN
    DELETE FROM Electric_Locomotive_Model WHERE locomotiveModelID IN (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker IN ('SIE', 'ALS'));
    DELETE FROM Diesel_Locomotive_Model WHERE locomotiveModelID IN (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker IN ('SIE', 'ALS'));
    DELETE FROM Locomotive_Model WHERE maker IN ('SIE', 'ALS');

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('USBD43 CleanUp: Done.');
END;
/