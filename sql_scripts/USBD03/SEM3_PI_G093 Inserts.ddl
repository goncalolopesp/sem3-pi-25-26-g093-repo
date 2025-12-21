INSERT INTO Owner (vatNumber, name, shortName)
VALUES ('PT503933813', 'Infraestruturas de Portugal, SA', 'IP');

INSERT INTO Operator (vatNumber, shortName, name)
VALUES ('PT509017800', 'Medway', 'Medway - Operador Ferroviário de Mercadorias, S.A');

INSERT INTO Operator (vatNumber, shortName, name)
VALUES ('PT507832388', 'Captrain', 'Captrain Portugal S.A.');

INSERT INTO Gauge (name, width_mm) VALUES ('Iberian Gauge', 1668);

INSERT INTO Gauge (name, width_mm) VALUES ('Standard Gauge', 1435);

INSERT INTO Facility_Type (typeName) VALUES ('Generic');

INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Tamel', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Senhora das Dores', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Lousado', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Porto Campanhã', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Leandro', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Porto São Bento', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Barcelos', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Vila Nova da Cerveira', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Midões', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Valença', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Darque', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Contumil', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Ermesinde', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'São Frutuoso', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'São Pedro da Torre', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Viana do Castelo', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Famalicão', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Barroselas', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Nine', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Caminha', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Carvalha', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Carreço', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Leixões', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'São Mamede de Infesta', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'Leça do Balio', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'São Gemil', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES ((SELECT id FROM Facility_Type WHERE typeName='Generic'), 'São Romão', 0, 0);

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='Porto São Bento'), (SELECT id FROM Facility WHERE name='Porto Campanhã'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal São Bento - Campanhã', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='Porto Campanhã'), (SELECT id FROM Facility WHERE name='Contumil'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal Campanhã - Contumil', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='Contumil'), (SELECT id FROM Facility WHERE name='Nine'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal Contumil - Nine', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='Nine'), (SELECT id FROM Facility WHERE name='Barcelos'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal Nine - Barcelos', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='Barcelos'), (SELECT id FROM Facility WHERE name='Darque'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal Barcelos - Darque', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='Darque'), (SELECT id FROM Facility WHERE name='Viana do Castelo'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal Darque - Viana', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='Viana do Castelo'), (SELECT id FROM Facility WHERE name='Caminha'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal Viana - Caminha', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='Caminha'), (SELECT id FROM Facility WHERE name='São Pedro da Torre'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal Caminha - Torre', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='São Pedro da Torre'), (SELECT id FROM Facility WHERE name='Valença'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal Torre - Valença', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='Contumil'), (SELECT id FROM Facility WHERE name='São Gemil'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal Contumil - São Gemil', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='São Gemil'), (SELECT id FROM Facility WHERE name='São Mamede de Infesta'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal São Gemil - São Mamede de Infesta', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='São Mamede de Infesta'), (SELECT id FROM Facility WHERE name='Leça do Balio'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal São Mamede de Infesta - Leça do Balio', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gaugeID)
VALUES ((SELECT id FROM Facility WHERE name='Leça do Balio'), (SELECT id FROM Facility WHERE name='Leixões'), (SELECT ownerID FROM Owner WHERE shortName='IP'), 'Ramal Leça do Balio - Leixões', (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal São Bento - Campanhã'), 1, 'Y', 2618, 4, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Campanhã - Contumil'), 1, 'Y', 2443, 4, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Contumil - Nine'), 1, 'Y', 26560, 2, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Contumil - Nine'), 2, 'Y', 10000, 2, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Nine - Barcelos'), 1, 'Y', 5286, 2, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Nine - Barcelos'), 2, 'Y', 6000, 2, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Barcelos - Darque'), 1, 'Y', 10387, 2, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Barcelos - Darque'), 2, 'Y', 12000, 2, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Barcelos - Darque'), 3, 'Y', 3100, 2, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Darque - Viana'), 1, 'Y', 4890, 2, 6400);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Viana - Caminha'), 1, 'Y', 6000, 1, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Viana - Caminha'), 2, 'Y', 5000, 1, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Viana - Caminha'), 3, 'Y', 12000, 1, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Caminha - Torre'), 1, 'Y', 20829, 1, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Torre - Valença'), 1, 'Y', 4264, 1, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Contumil - São Gemil'), 1, 'Y', 3883, 2, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal São Gemil - São Mamede de Infesta'), 1, 'Y', 1174, 2, 8400);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal São Gemil - São Mamede de Infesta'), 2, 'Y', 2534, 2, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal São Mamede de Infesta - Leça do Balio'), 1, 'Y', 1566, 2, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal São Mamede de Infesta - Leça do Balio'), 2, 'Y', 1453, 2, 8000);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Leça do Balio - Leixões'), 1, 'Y', 3597, 2, 8100);

INSERT INTO Railway_Segment (railwayLineID, segmentOrder, isElectrified, length, numberTracks, weightLimit)
VALUES ((SELECT id FROM Railway_Line WHERE name='Ramal Leça do Balio - Leixões'), 2, 'Y', 4334, 2, 8000);

INSERT INTO Siding (railwaySegmentID, position, length)
VALUES ( (SELECT id FROM Railway_Segment WHERE segmentOrder=2 AND railwayLineID = (SELECT id FROM Railway_Line WHERE name='Ramal Viana - Caminha')), 2000, 864);

INSERT INTO Siding (railwaySegmentID, position, length)
VALUES ( (SELECT id FROM Railway_Segment WHERE segmentOrder=3 AND railwayLineID = (SELECT id FROM Railway_Line WHERE name='Ramal Viana - Caminha')), 11000, 266);

INSERT INTO Wagon_Model_Type (typeName) VALUES ('Wood wagon');
INSERT INTO Wagon_Model_Type (typeName) VALUES ('Cereal wagon');
INSERT INTO Wagon_Model_Type (typeName) VALUES ('Covered wagon with sliding door');
INSERT INTO Wagon_Model_Type (typeName) VALUES ('Container wagon (max 40'' HC)');
INSERT INTO Wagon_Model_Type (typeName) VALUES ('Biodiesel wagon');

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, maker, tareWeight, numberOfBogies, payload)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Cereal wagon'),
        56, 75, 17.24, 4.27, 120, 'Metalsines', 24, 2, 56);

INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID)
VALUES ((SELECT MAX(wagonModelID) FROM Wagon_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, maker, tareWeight, numberOfBogies, payload)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Cereal wagon'),
        26, 38, 9.64, 4.16, 100, 'Equimetal', 14, 2, 26);
INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID)
VALUES ((SELECT MAX(wagonModelID) FROM Wagon_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, maker, tareWeight, numberOfBogies, payload)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Covered wagon with sliding door'),
        50, 110, 21.70, 4.17, 100, 'SepsaComet', 30, 2, 50);
INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID)
VALUES ((SELECT MAX(wagonModelID) FROM Wagon_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, maker, tareWeight, numberOfBogies, payload)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Container wagon (max 40'' HC)'),
        61, 76, 14.04, 2.53, 120, 'Metalsines', 21, 2, 61);
INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID)
VALUES ((SELECT MAX(wagonModelID) FROM Wagon_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, maker, tareWeight, numberOfBogies, payload)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Container wagon (max 40'' HC)'),
        28, 76, 13.86, 1.06, 120, 'Metalsines', 12, 2, 28);
INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID)
VALUES ((SELECT MAX(wagonModelID) FROM Wagon_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, maker, tareWeight, numberOfBogies, payload)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Container wagon (max 40'' HC)'),
        68, 76, 18.11, 1.03, 120, 'Emef', 22, 2, 68);
INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID)
VALUES ((SELECT MAX(wagonModelID) FROM Wagon_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, maker, tareWeight, numberOfBogies, payload)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Container wagon (max 40'' HC)'),
        68, 76, 18.11, 1.03, 120, 'Emef', 22, 2, 68);
INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID)
VALUES ((SELECT MAX(wagonModelID) FROM Wagon_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1435));

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, maker, tareWeight, numberOfBogies, payload)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Biodiesel wagon'),
        57, 65, 13.80, 4.22, 120, 'Equimetal', 23, 2, 57);
INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID)
VALUES ((SELECT MAX(wagonModelID) FROM Wagon_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, maker, tareWeight, numberOfBogies, payload)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Biodiesel wagon'),
        57, 65, 13.80, 4.22, 120, 'Equimetal', 23, 2, 57);
INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID)
VALUES ((SELECT MAX(wagonModelID) FROM Wagon_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1435));

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, maker, tareWeight, numberOfBogies, payload)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Wood wagon'),
        26, 63, 14.02, 3.30, 100, 'Simmering', 14, 2, 26);
INSERT INTO Wagon_Model_Gauge (wagonModelID, gaugeID)
VALUES ((SELECT MAX(wagonModelID) FROM Wagon_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Locomotive_Model (power, topSpeed, trackType, tareWeight, maxWeight, numberOfBogies, maker, length, weight, width)
VALUES (5600, 220, 2, 87, 87, 2, 'Siemens', 19.2, 87, 3);
INSERT INTO Electric_Locomotive_Model (locomotiveModelID, voltage, frequence)
VALUES ((SELECT MAX(locomotiveModelID) FROM Locomotive_Model), 25000, 50);
INSERT INTO Locomotive_Model_Gauge (locomotiveModelID, gaugeID)
VALUES ((SELECT MAX(locomotiveModelID) FROM Locomotive_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Locomotive_Model (power, topSpeed, trackType, tareWeight, maxWeight, numberOfBogies, maker, length, weight, width)
VALUES (1623, 100, 1, 117, 117, 2, 'Alstom', 19.08, 117, 3);
INSERT INTO Diesel_Locomotive_Model (locomotiveModelID, fuelCapacity)
VALUES ((SELECT MAX(locomotiveModelID) FROM Locomotive_Model), 4882);
INSERT INTO Locomotive_Model_Gauge (locomotiveModelID, gaugeID)
VALUES ((SELECT MAX(locomotiveModelID) FROM Locomotive_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1668));

INSERT INTO Locomotive_Model (power, topSpeed, trackType, tareWeight, maxWeight, numberOfBogies, maker, length, weight, width)
VALUES (3178, 120, 1, 124, 124, 3, 'Stadler', 23.02, 124, 3);
INSERT INTO Diesel_Locomotive_Model (locomotiveModelID, fuelCapacity)
VALUES ((SELECT MAX(locomotiveModelID) FROM Locomotive_Model), 6700);
INSERT INTO Locomotive_Model_Gauge (locomotiveModelID, gaugeID)
VALUES ((SELECT MAX(locomotiveModelID) FROM Locomotive_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1668));
INSERT INTO Locomotive_Model_Gauge (locomotiveModelID, gaugeID)
VALUES ((SELECT MAX(locomotiveModelID) FROM Locomotive_Model), (SELECT gaugeID FROM Gauge WHERE width_mm=1435));

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
SELECT
    (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
    (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND payload=61),
    identifier,
    TO_DATE('02/05/1987', 'DD/MM/YYYY')
FROM (
         SELECT '356 3 077' as identifier FROM DUAL UNION ALL
         SELECT '356 3 078' FROM DUAL UNION ALL
         SELECT '356 3 079' FROM DUAL UNION ALL
         SELECT '356 3 080' FROM DUAL UNION ALL
         SELECT '356 3 081' FROM DUAL UNION ALL
         SELECT '356 3 082' FROM DUAL UNION ALL
         SELECT '356 3 083' FROM DUAL UNION ALL
         SELECT '356 3 084' FROM DUAL UNION ALL
         SELECT '356 3 085' FROM DUAL UNION ALL
         SELECT '356 3 086' FROM DUAL UNION ALL
         SELECT '356 3 087' FROM DUAL UNION ALL
         SELECT '356 3 088' FROM DUAL UNION ALL
         SELECT '356 3 089' FROM DUAL UNION ALL
         SELECT '356 3 090' FROM DUAL UNION ALL
         SELECT '356 3 091' FROM DUAL UNION ALL
         SELECT '356 3 092' FROM DUAL
     );

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
SELECT
    (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
    (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND payload=56),
    identifier,
    TO_DATE('12/04/1990', 'DD/MM/YYYY')
FROM (
         SELECT '082 3 045' as identifier FROM DUAL UNION ALL
         SELECT '082 3 046' FROM DUAL UNION ALL
         SELECT '082 3 047' FROM DUAL UNION ALL
         SELECT '082 3 048' FROM DUAL
     );

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
SELECT
    (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
    (SELECT wagonModelID FROM Wagon_Model WHERE maker='Equimetal' AND payload=26),
    identifier,
    TO_DATE('03/02/1977', 'DD/MM/YYYY')
FROM (
         SELECT '074 1 001' as identifier FROM DUAL UNION ALL
         SELECT '074 1 002' FROM DUAL UNION ALL
         SELECT '074 1 003' FROM DUAL UNION ALL
         SELECT '074 1 004' FROM DUAL UNION ALL
         SELECT '074 1 005' FROM DUAL UNION ALL
         SELECT '074 1 006' FROM DUAL UNION ALL
         SELECT '074 1 007' FROM DUAL
     );

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
SELECT
    (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
    (SELECT wagonModelID FROM Wagon_Model WHERE maker='SepsaComet'),
    identifier,
    TO_DATE('03/02/1977', 'DD/MM/YYYY')
FROM (
         SELECT '181 1 010' as identifier FROM DUAL UNION ALL
         SELECT '181 1 011' FROM DUAL UNION ALL
         SELECT '181 1 012' FROM DUAL UNION ALL
         SELECT '181 1 013' FROM DUAL UNION ALL
         SELECT '181 1 014' FROM DUAL
     );

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
VALUES ((SELECT operatorID FROM Operator WHERE shortName = 'Medway'), (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'), '333 0 001', TO_DATE('01/09/2005', 'DD/MM/YYYY'));

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
VALUES ((SELECT operatorID FROM Operator WHERE shortName = 'Medway'), (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'), '333 0 002', TO_DATE('01/09/2005', 'DD/MM/YYYY'));

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
VALUES ((SELECT operatorID FROM Operator WHERE shortName = 'Medway'), (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'), '333 0 003', TO_DATE('01/09/2005', 'DD/MM/YYYY'));

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
VALUES ((SELECT operatorID FROM Operator WHERE shortName = 'Medway'), (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'), '333 0 004', TO_DATE('01/09/2005', 'DD/MM/YYYY'));

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
VALUES ((SELECT operatorID FROM Operator WHERE shortName = 'Medway'), (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'), '333 0 005', TO_DATE('01/09/2005', 'DD/MM/YYYY'));

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
VALUES ((SELECT operatorID FROM Operator WHERE shortName = 'Medway'), (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'), '333 0 006', TO_DATE('01/09/2005', 'DD/MM/YYYY'));

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
VALUES ((SELECT operatorID FROM Operator WHERE shortName = 'Medway'), (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'), '333 0 007', TO_DATE('01/09/2005', 'DD/MM/YYYY'));

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
VALUES ((SELECT operatorID FROM Operator WHERE shortName = 'Medway'), (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'), '333 0 008', TO_DATE('01/09/2005', 'DD/MM/YYYY'));

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
VALUES ((SELECT operatorID FROM Operator WHERE shortName = 'Medway'), (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'), '333 0 009', TO_DATE('01/09/2005', 'DD/MM/YYYY'));

INSERT INTO Wagon (operatorID, wagonModelID, identifier, serviceStart)
VALUES ((SELECT operatorID FROM Operator WHERE shortName = 'Medway'), (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'), '333 0 010', TO_DATE('01/09/2005', 'DD/MM/YYYY'));

INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, serviceStart, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Siemens' AND rownum=1),
           NULL, 'Inês', TO_DATE('01/01/1995', 'DD/MM/YYYY'), '5621'
       );

INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, serviceStart, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Siemens' AND rownum=1),
           NULL, 'Paz', TO_DATE('01/04/1995', 'DD/MM/YYYY'), '5623'
       );

INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, serviceStart, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Siemens' AND rownum=1),
           NULL, 'Helena', TO_DATE('02/01/1996', 'DD/MM/YYYY'), '5630'
       );

INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, serviceStart, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Alstom' AND rownum=1),
           NULL, 'Eva', TO_DATE('07/04/1981', 'DD/MM/YYYY'), '1903'
       );

INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, serviceStart, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Stadler' AND rownum=1),
           NULL, 'Adriana', TO_DATE('15/02/2017', 'DD/MM/YYYY'), '5034'
       );

INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, serviceStart, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Stadler' AND rownum=1),
           NULL, 'Marina', TO_DATE('01/02/2017', 'DD/MM/YYYY'), '5036'
       );

INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, serviceStart, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Captrain'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Stadler' AND rownum=1),
           NULL, '335.001', TO_DATE('07/05/2019', 'DD/MM/YYYY'), '335.001'
       );

INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, serviceStart, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Captrain'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Stadler' AND rownum=1),
           NULL, '335.003', TO_DATE('03/06/2019', 'DD/MM/YYYY'), '335.003'
       );


INSERT INTO Path (pathID) VALUES (DEFAULT);

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Leixões'), (SELECT MAX(pathID) FROM Path), 1, TO_TIMESTAMP('2025-10-03 09:45:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 09:50:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Leça do Balio'), (SELECT MAX(pathID) FROM Path), 2, TO_TIMESTAMP('2025-10-03 10:05:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 10:10:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='São Mamede de Infesta'), (SELECT MAX(pathID) FROM Path), 3, TO_TIMESTAMP('2025-10-03 10:25:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 10:30:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='São Gemil'), (SELECT MAX(pathID) FROM Path), 4, TO_TIMESTAMP('2025-10-03 10:45:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 10:50:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Contumil'), (SELECT MAX(pathID) FROM Path), 5, TO_TIMESTAMP('2025-10-03 11:05:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 11:15:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Nine'), (SELECT MAX(pathID) FROM Path), 6, TO_TIMESTAMP('2025-10-03 11:45:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 11:55:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Barcelos'), (SELECT MAX(pathID) FROM Path), 7, TO_TIMESTAMP('2025-10-03 12:15:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 12:20:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Darque'), (SELECT MAX(pathID) FROM Path), 8, TO_TIMESTAMP('2025-10-03 12:40:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 12:50:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Viana do Castelo'), (SELECT MAX(pathID) FROM Path), 9, TO_TIMESTAMP('2025-10-03 13:05:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 13:15:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Caminha'), (SELECT MAX(pathID) FROM Path), 10, TO_TIMESTAMP('2025-10-03 13:40:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 13:45:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='São Pedro da Torre'), (SELECT MAX(pathID) FROM Path), 11, TO_TIMESTAMP('2025-10-03 14:05:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 14:10:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Valença'), (SELECT MAX(pathID) FROM Path), 12, TO_TIMESTAMP('2025-10-03 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 14:30:00', 'YYYY-MM-DD HH24:MI:SS'));


INSERT INTO Path (pathID) VALUES (DEFAULT);

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Valença'), (SELECT MAX(pathID) FROM Path), 1, TO_TIMESTAMP('2025-10-03 18:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 18:10:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Darque'), (SELECT MAX(pathID) FROM Path), 2, TO_TIMESTAMP('2025-10-03 19:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 19:40:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Leixões'), (SELECT MAX(pathID) FROM Path), 3, TO_TIMESTAMP('2025-10-03 22:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-03 22:00:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Path (pathID) VALUES (DEFAULT);

INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Valença'), (SELECT MAX(pathID) FROM Path), 1, TO_TIMESTAMP('2025-10-06 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-06 10:10:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO Facility_Path (Facilityid, PathpathID, "order", arrivalTime, departureTime)
VALUES ((SELECT id FROM Facility WHERE name='Leixões'), (SELECT MAX(pathID) FROM Path), 2, TO_TIMESTAMP('2025-10-06 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-10-06 14:00:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO Train (pathID, operatorID, startDate)
VALUES ((SELECT MIN(pathID) FROM Path), (SELECT operatorID FROM Operator WHERE shortName='Medway'), TO_DATE('2025-10-03', 'YYYY-MM-DD'));

INSERT INTO Train (pathID, operatorID, startDate)
VALUES ((SELECT pathID FROM Path ORDER BY pathID ASC OFFSET 1 ROWS FETCH NEXT 1 ROWS ONLY), (SELECT operatorID FROM Operator WHERE shortName='Medway'), TO_DATE('2025-10-03', 'YYYY-MM-DD'));

INSERT INTO Train (pathID, operatorID, startDate)
VALUES ((SELECT MAX(pathID) FROM Path), (SELECT operatorID FROM Operator WHERE shortName='Medway'), TO_DATE('2025-10-06', 'YYYY-MM-DD'));

UPDATE Locomotive SET trainID = (SELECT trainID FROM Train WHERE startDate=TO_DATE('2025-10-03', 'YYYY-MM-DD') AND pathID=(SELECT MIN(pathID) FROM Path))
WHERE identifier IN ('5621', '5623');

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES ((SELECT trainID FROM Train WHERE startDate=TO_DATE('2025-10-03', 'YYYY-MM-DD') AND pathID=(SELECT MIN(pathID) FROM Path)),
        (SELECT id FROM Facility WHERE name='Leixões'),
        (SELECT id FROM Facility WHERE name='Darque'),
        TO_DATE('2025-10-03', 'YYYY-MM-DD'));

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES ((SELECT trainID FROM Train WHERE startDate=TO_DATE('2025-10-03', 'YYYY-MM-DD') AND pathID=(SELECT MIN(pathID) FROM Path)),
        (SELECT id FROM Facility WHERE name='Contumil'),
        (SELECT id FROM Facility WHERE name='Valença'),
        TO_DATE('2025-10-03', 'YYYY-MM-DD'));

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES ((SELECT trainID FROM Train WHERE startDate=TO_DATE('2025-10-03', 'YYYY-MM-DD') AND pathID=(SELECT MIN(pathID) FROM Path)),
        (SELECT id FROM Facility WHERE name='Leixões'),
        (SELECT id FROM Facility WHERE name='Valença'),
        TO_DATE('2025-10-03', 'YYYY-MM-DD'));

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES ((SELECT trainID FROM Train WHERE startDate=TO_DATE('2025-10-03', 'YYYY-MM-DD') AND pathID=(SELECT MIN(pathID) FROM Path)),
        (SELECT id FROM Facility WHERE name='Leixões'),
        (SELECT id FROM Facility WHERE name='Caminha'),
        TO_DATE('2025-10-03', 'YYYY-MM-DD'));

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES ((SELECT trainID FROM Train WHERE startDate=TO_DATE('2025-10-03', 'YYYY-MM-DD') AND pathID=(SELECT MIN(pathID) FROM Path)),
        (SELECT id FROM Facility WHERE name='Nine'),
        (SELECT id FROM Facility WHERE name='Valença'),
        TO_DATE('2025-10-03', 'YYYY-MM-DD'));

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES ((SELECT trainID FROM Train WHERE startDate=TO_DATE('2025-10-03', 'YYYY-MM-DD') AND pathID != (SELECT MIN(pathID) FROM Path)),
       (SELECT id FROM Facility WHERE name='Darque'),
       (SELECT id FROM Facility WHERE name='Leixões'),
    TO_DATE('2025-10-03', 'YYYY-MM-DD'));

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES ((SELECT trainID FROM Train WHERE startDate=TO_DATE('2025-10-06', 'YYYY-MM-DD')),
        (SELECT id FROM Facility WHERE name='Darque'),
        (SELECT id FROM Facility WHERE name='Leixões'),
        TO_DATE('2025-10-06', 'YYYY-MM-DD'));

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES ((SELECT trainID FROM Train WHERE startDate=TO_DATE('2025-10-06', 'YYYY-MM-DD')),
        (SELECT id FROM Facility WHERE name='Valença'),
        (SELECT id FROM Facility WHERE name='Leixões'),
        TO_DATE('2025-10-06', 'YYYY-MM-DD'));

INSERT INTO Wagon_Freight (wagonID, freightID)
SELECT w.wagonID, f.freightID FROM Wagon w, Freight f
WHERE w.identifier IN ('333 0 001', '333 0 002', '333 0 004', '333 0 005', '333 0 006')
  AND f.startFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND f.endFacilityID=(SELECT id FROM Facility WHERE name='Darque') AND f.freightDate=TO_DATE('2025-10-03', 'YYYY-MM-DD');

INSERT INTO Wagon_Freight (wagonID, freightID)
SELECT w.wagonID, f.freightID FROM Wagon w, Freight f
WHERE w.identifier = '356 3 089'
  AND f.startFacilityID=(SELECT id FROM Facility WHERE name='Contumil') AND f.endFacilityID=(SELECT id FROM Facility WHERE name='Valença');

INSERT INTO Wagon_Freight (wagonID, freightID)
SELECT w.wagonID, f.freightID FROM Wagon w, Freight f
WHERE w.identifier IN ('181 1 011', '181 1 012')
  AND f.startFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND f.endFacilityID=(SELECT id FROM Facility WHERE name='Valença');

INSERT INTO Wagon_Freight (wagonID, freightID)
SELECT w.wagonID, f.freightID FROM Wagon w, Freight f
WHERE w.identifier = '181 1 013'
  AND f.startFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND f.endFacilityID=(SELECT id FROM Facility WHERE name='Caminha');

INSERT INTO Wagon_Freight (wagonID, freightID)
SELECT w.wagonID, f.freightID FROM Wagon w, Freight f
WHERE w.identifier IN ('356 3 077', '356 3 078', '356 3 079', '356 3 080')
  AND f.startFacilityID=(SELECT id FROM Facility WHERE name='Nine') AND f.endFacilityID=(SELECT id FROM Facility WHERE name='Valença');

INSERT INTO Wagon_Freight (wagonID, freightID)
SELECT w.wagonID, f.freightID FROM Wagon w, Freight f
WHERE w.identifier IN ('333 0 003', '333 0 007')
  AND f.startFacilityID=(SELECT id FROM Facility WHERE name='Darque') AND f.endFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND f.freightDate=TO_DATE('2025-10-03', 'YYYY-MM-DD');

INSERT INTO Wagon_Freight (wagonID, freightID)
SELECT w.wagonID, f.freightID FROM Wagon w, Freight f
WHERE w.identifier IN ('333 0 001', '333 0 002', '333 0 004', '333 0 005', '333 0 006')
  AND f.startFacilityID=(SELECT id FROM Facility WHERE name='Darque') AND f.endFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND f.freightDate=TO_DATE('2025-10-06', 'YYYY-MM-DD');

INSERT INTO Wagon_Freight (wagonID, freightID)
SELECT w.wagonID, f.freightID FROM Wagon w, Freight f
WHERE w.identifier IN ('181 1 011', '181 1 012', '356 3 077', '356 3 078', '356 3 079', '356 3 080')
  AND f.startFacilityID=(SELECT id FROM Facility WHERE name='Valença') AND f.endFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND f.freightDate=TO_DATE('2025-10-06', 'YYYY-MM-DD');