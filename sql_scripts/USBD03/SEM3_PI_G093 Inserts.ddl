INSERT INTO Facility_Type (typeName) VALUES ('Generic');

INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Tamel', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Senhora das Dores', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Lousado', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Porto Campanhã', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Leandro', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Porto São Bento', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Barcelos', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Vila Nova da Cerveira', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Midões', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Valença', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Darque', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Contumil', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Ermesinde', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'São Frutuoso', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'São Pedro da Torre', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Viana do Castelo', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Famalicão', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Barroselas', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Nine', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Caminha', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Carvalha', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Carreço', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Leixões', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'São Mamede de Infesta', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'Leça do Balio', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'São Gemil', 0, 0);
INSERT INTO Facility (facilityTypeID, name, posX, posY) VALUES (1, 'São Romão', 0, 0);

INSERT INTO Owner (vatNumber, name, shortName)
VALUES ('PT503933813', 'Infraestruturas de Portugal, SA', 'IP');

INSERT INTO Operator (vatNumber, shortName, name)
VALUES ('PT507832388', 'Captrain', 'Captrain Portugal S.A.');

INSERT INTO Operator (vatNumber, shortName, name)
VALUES ('PT509017800', 'Medway', 'Medway - Operador Ferroviário de Mercadorias, S.A');


INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
    (SELECT id FROM Facility WHERE name='Porto São Bento'),
    (SELECT id FROM Facility WHERE name='Porto Campanhã'),
    (SELECT ownerID FROM Owner WHERE shortName='IP'),
    'Ramal São Bento - Campanhã',
    1668
);

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
    (SELECT id FROM Facility WHERE name='Porto Campanhã'),
    (SELECT id FROM Facility WHERE name='Contumil'),
    (SELECT ownerID FROM Owner WHERE shortName='IP'),
    'Ramal Campanhã - Contumil',
    1668
);

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
    (SELECT id FROM Facility WHERE name='Contumil'),
    (SELECT id FROM Facility WHERE name='Nine'),
    (SELECT ownerID FROM Owner WHERE shortName='IP'),
    'Ramal Contumil - Nine',
    1668
);

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
    (SELECT id FROM Facility WHERE name='Nine'),
    (SELECT id FROM Facility WHERE name='Barcelos'),
    (SELECT ownerID FROM Owner WHERE shortName='IP'),
    'Ramal Nine - Barcelos',
    1668
);

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
    (SELECT id FROM Facility WHERE name='Barcelos'),
    (SELECT id FROM Facility WHERE name='Darque'),
    (SELECT ownerID FROM Owner WHERE shortName='IP'),
    'Ramal Barcelos - Darque',
    1668
);

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
    (SELECT id FROM Facility WHERE name='Darque'),
    (SELECT id FROM Facility WHERE name='Viana do Castelo'),
    (SELECT ownerID FROM Owner WHERE shortName='IP'),
    'Ramal Darque - Viana',
    1668
);

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
           (SELECT id FROM Facility WHERE name='Viana do Castelo'),
           (SELECT id FROM Facility WHERE name='Caminha'),
           (SELECT ownerID FROM Owner WHERE shortName='IP'),
           'Ramal Viana - Caminha',
           1668
       );

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
           (SELECT id FROM Facility WHERE name='Caminha'),
           (SELECT id FROM Facility WHERE name='São Pedro da Torre'),
           (SELECT ownerID FROM Owner WHERE shortName='IP'),
           'Ramal Caminha - Torre',
           1668
       );

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
           (SELECT id FROM Facility WHERE name='São Pedro da Torre'),
           (SELECT id FROM Facility WHERE name='Valença'),
           (SELECT ownerID FROM Owner WHERE shortName='IP'),
           'Ramal Torre - Valença',
           1668
       );

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
           (SELECT id FROM Facility WHERE name='Contumil'),
           (SELECT id FROM Facility WHERE name='São Gemil'),
           (SELECT ownerID FROM Owner WHERE shortName='IP'),
           'Ramal Contumil - São Gemil',
           1668
       );

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
           (SELECT id FROM Facility WHERE name='São Gemil'),
           (SELECT id FROM Facility WHERE name='São Mamede de Infesta'),
           (SELECT ownerID FROM Owner WHERE shortName='IP'),
           'Ramal São Gemil - São Mamede de Infesta',
           1668
       );

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
           (SELECT id FROM Facility WHERE name='São Mamede de Infesta'),
           (SELECT id FROM Facility WHERE name='Leça do Balio'),
           (SELECT ownerID FROM Owner WHERE shortName='IP'),
           'Ramal São Mamede de Infesta - Leça do Balio',
           1668
       );

INSERT INTO Railway_Line (startFacilityID, endFacilityID, ownerID, name, gauge)
VALUES (
           (SELECT id FROM Facility WHERE name='Leça do Balio'),
           (SELECT id FROM Facility WHERE name='Leixões'),
           (SELECT ownerID FROM Owner WHERE shortName='IP'),
           'Ramal Leça do Balio - Leixões',
           1668
       );



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
VALUES ( (SELECT id FROM Railway_Segment WHERE segmentOrder=2 AND railwayLineID =
          (SELECT id FROM Railway_Line WHERE name='Ramal Viana - Caminha')), 2000, 864);

INSERT INTO Siding (railwaySegmentID, position, length)
VALUES ( (SELECT id FROM Railway_Segment WHERE segmentOrder=3 AND railwayLineID =
          (SELECT id FROM Railway_Line WHERE name='Ramal Viana - Caminha')), 11000, 266);

INSERT INTO Wagon_Model_Type (typeName) VALUES ('Wood wagon');
INSERT INTO Wagon_Model_Type (typeName) VALUES ('Cereal wagon');
INSERT INTO Wagon_Model_Type (typeName) VALUES ('Covered wagon with sliding door');
INSERT INTO Wagon_Model_Type (typeName) VALUES ('Container wagon (max 40'' HC)');
INSERT INTO Wagon_Model_Type (typeName) VALUES ('Biodiesel wagon');

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, gauge, maker, tareWeight, numberOfBogies)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Cereal wagon'),
        56, 75, 17240, 4270, 120, 1668, 'Metalsines', 24, 2);

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, gauge, maker, tareWeight, numberOfBogies)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Cereal wagon'),
        26, 38, 9640, 4166, 100, 1668, 'Equimetal', 14, 2);

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, gauge, maker, tareWeight, numberOfBogies)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Covered wagon with sliding door'),
        50, 110, 21700, 4170, 100, 1668, 'SepsaComet', 30, 2);

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, gauge, maker, tareWeight, numberOfBogies)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Container wagon (max 40'' HC)'),
        61, 76, 14040, 2535, 120, 1668, 'Metalsines', 21, 2);

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, gauge, maker, tareWeight, numberOfBogies)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Container wagon (max 40'' HC)'),
        28, 76, 13860, 1060, 120, 1668, 'Metalsines', 12, 2);

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, gauge, maker, tareWeight, numberOfBogies)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Container wagon (max 40'' HC)'),
        68, 76, 18116, 1030, 120, 1668, 'Emef', 22, 2);

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, gauge, maker, tareWeight, numberOfBogies)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Container wagon (max 40'' HC)'),
        68, 76, 18116, 1030, 120, 1435, 'Emef', 22, 2);

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, gauge, maker, tareWeight, numberOfBogies)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Biodiesel wagon'),
        57, 65, 13800, 4226, 120, 1668, 'Equimetal', 23, 2);

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, gauge, maker, tareWeight, numberOfBogies)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Biodiesel wagon'),
        57, 65, 13800, 4226, 120, 1435, 'Equimetal', 23, 2);

INSERT INTO Wagon_Model (wagonModelTypeID, capacity, volumeCapacity, length, height, maxSpeed, gauge, maker, tareWeight, numberOfBogies)
VALUES ((SELECT wagonModelTypeID FROM Wagon_Model_Type WHERE typeName='Wood wagon'),
        26, 63, 14020, 3300, 100, 1668, 'Simmering', 14, 2);

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 077'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 078'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 079'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 080'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 081'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 082'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 083'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 084'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 085'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 086'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 087'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 088'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 089'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 090'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 091'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=61 AND gauge=1668),
           '356 3 092'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=56 AND gauge=1668),
           '082 3 046'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=56 AND gauge=1668),
           '082 3 047'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Metalsines' AND capacity=56 AND gauge=1668),
           '082 3 048'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Equimetal' AND capacity=26 AND gauge=1668),
           '074 1 001'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Equimetal' AND capacity=26 AND gauge=1668),
           '074 1 002'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Equimetal' AND capacity=26 AND gauge=1668),
           '074 1 003'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Equimetal' AND capacity=26 AND gauge=1668),
           '074 1 004'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Equimetal' AND capacity=26 AND gauge=1668),
           '074 1 005'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Equimetal' AND capacity=26 AND gauge=1668),
           '074 1 006'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Equimetal' AND capacity=26 AND gauge=1668),
           '074 1 007'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='SepsaComet'),
           '181 1 010'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='SepsaComet'),
           '181 1 011'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='SepsaComet'),
           '181 1 012'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='SepsaComet'),
           '181 1 013'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='SepsaComet'),
           '181 1 014'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'),
           '333 0 001'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'),
           '333 0 002'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'),
           '333 0 003'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'),
           '333 0 004'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'),
           '333 0 005'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'),
           '333 0 006'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'),
           '333 0 007'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'),
           '333 0 008'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'),
           '333 0 009'
       );

INSERT INTO Wagon (operatorID, wagonModelID, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT wagonModelID FROM Wagon_Model WHERE maker='Simmering'),
           '333 0 010'
       );


INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 077'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND endFacilityID=(SELECT id FROM Facility WHERE name='Darque')));

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 077'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Valença') AND endFacilityID=(SELECT id FROM Facility WHERE name='Leixões')));

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 078'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND endFacilityID=(SELECT id FROM Facility WHERE name='Darque')));

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 078'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Valença') AND endFacilityID=(SELECT id FROM Facility WHERE name='Leixões')));

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 079'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND endFacilityID=(SELECT id FROM Facility WHERE name='Darque')));

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 079'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Valença') AND endFacilityID=(SELECT id FROM Facility WHERE name='Leixões')));

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 080'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND endFacilityID=(SELECT id FROM Facility WHERE name='Darque')));

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 080'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Valença') AND endFacilityID=(SELECT id FROM Facility WHERE name='Leixões')));

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 081'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND endFacilityID=(SELECT id FROM Facility WHERE name='Darque')));

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 081'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Valença') AND endFacilityID=(SELECT id FROM Facility WHERE name='Leixões')));

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 082'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Leixões') AND endFacilityID=(SELECT id FROM Facility WHERE name='Darque')));

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES ((SELECT wagonID FROM Wagon WHERE identifier='356 3 082'),
        (SELECT freightID FROM Freight WHERE startFacilityID=(SELECT id FROM Facility WHERE name='Valença') AND endFacilityID=(SELECT id FROM Facility WHERE name='Leixões')));

INSERT INTO Locomotive_Model (power, topSpeed, trackType, gauge, tareWeight, maxWeight, numberOfBogies, maker)
VALUES (1623, 100, 1, 1668, 117, 130, 2, 'Alstom');

INSERT INTO Locomotive_Model (power, topSpeed, trackType, gauge, tareWeight, maxWeight, numberOfBogies, maker)
VALUES (3178, 120, 1, 1668, 124, 140, 3, 'Stadler');

INSERT INTO Locomotive_Model (power, topSpeed, trackType, gauge, tareWeight, maxWeight, numberOfBogies, maker)
VALUES (5600, 220, 2, 1668, 87, 120, 2, 'Siemens');

INSERT INTO Diesel_Locomotive_Model (locomotiveModelID, fuelCapacity)
VALUES (
           (SELECT locomotiveModelID FROM Locomotive_Model
            WHERE power = 1623 AND topSpeed = 100 AND trackType = 1 AND maker = 'Alstom'),
           4882
       );

INSERT INTO Diesel_Locomotive_Model (locomotiveModelID, fuelCapacity)
VALUES (
           (SELECT locomotiveModelID FROM Locomotive_Model
            WHERE power = 3178 AND topSpeed = 120 AND trackType = 1 AND maker = 'Stadler'),
           6700
       );

INSERT INTO Electric_Locomotive_Model (locomotiveModelID, voltage, frequence)
VALUES (
           (SELECT locomotiveModelID FROM Locomotive_Model
            WHERE power = 5600 AND topSpeed = 220 AND trackType = 2 AND maker = 'Siemens'),
           25000,
           50
       );

INSERT INTO Train (operatorID, startDate, startTime)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           DATE '2025-10-03',
           '09:45'
       );

INSERT INTO Train (operatorID, startDate, startTime)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           DATE '2025-10-03',
           '18:00'
       );

INSERT INTO Train (operatorID, startDate, startTime)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           DATE '2025-10-06',
           '10:00'
       );


-- Inês – Siemens Eurosprinter (model 5600hp) – Train 22
INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, startYear, model, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Siemens'),
           (SELECT trainID FROM Train WHERE startTime = '10:00'),
           'Inês',
           1993,
           'Eurosprinter',
           '5621'
       );

-- Paz – Siemens Eurosprinter – Train 21
INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, startYear, model, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Siemens'),
           (SELECT trainID FROM Train WHERE startTime = '18:00'),
           'Paz',
           1993,
           'Eurosprinter',
           '5623'
       );

-- Helena – Siemens Eurosprinter – sem trainID
INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, startYear, model, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Siemens'),
           NULL,
           'Helena',
           1994,
           'Eurosprinter',
           '5630'
       );

-- Eva – Alstom CP1900 – sem trainID
INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, startYear, model, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Alstom'),
           NULL,
           'Eva',
           1981,
           'CP1900',
           '1903'
       );

-- Adriana – Stadler E4000 – sem trainID
INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, startYear, model, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Stadler'),
           NULL,
           'Adriana',
           2018,
           'E4000',
           '5034'
       );

-- Marina – Stadler E4000 – Train 22
INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, startYear, model, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Medway'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Stadler'),
           (SELECT trainID FROM Train WHERE startTime = '10:00'),
           'Marina',
           2018,
           'E4000',
           '5036'
       );

-- 335.001 – Captrain – Stadler E4000 – sem trainID
INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, startYear, model, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Captrain'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Stadler'),
           NULL,
           '335.001',
           2019,
           'Eurosprinter',
           '335.001'
       );

-- 335.003 – Captrain – Stadler E4000 – sem trainID
INSERT INTO Locomotive (operatorID, locomotiveModelID, trainID, name, startYear, model, identifier)
VALUES (
           (SELECT operatorID FROM Operator WHERE shortName = 'Captrain'),
           (SELECT locomotiveModelID FROM Locomotive_Model WHERE maker = 'Stadler'),
           NULL,
           '335.003',
           2019,
           'Eurosprinter',
           '335.003'
       );


INSERT INTO Path (trainID)
VALUES ((SELECT trainID FROM Train WHERE startTime = '09:45'));

INSERT INTO Path (trainID)
VALUES ((SELECT trainID FROM Train WHERE startTime = '18:00'));

INSERT INTO Path (trainID)
VALUES ((SELECT trainID FROM Train WHERE startTime = '10:00'));

-- PATH do comboio 09:45
INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Leixões'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '1'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Leça do Balio'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '2'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'São Mamede de Infesta'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '3'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'São Gemil'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '4'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Contumil'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '5'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Nine'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '6'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Barcelos'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '7'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Darque'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '8'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Viana do Castelo'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '9'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Caminha'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '10'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'São Pedro da Torre'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '11'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Valença'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='09:45'),
           '12'
       );

-- PATH do comboio 18:00
INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Valença'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '1'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'São Pedro da Torre'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '2'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Caminha'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '3'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Viana do Castelo'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '4'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Darque'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '5'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Barcelos'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '6'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Nine'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '7'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Contumil'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '8'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'São Gemil'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '9'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'São Mamede de Infesta'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '10'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Leça do Balio'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '11'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Leixões'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='18:00'),
           '12'
       );

-- PATH do comboio 10:00
INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Valença'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '1'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'São Pedro da Torre'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '2'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Caminha'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '3'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Viana do Castelo'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '4'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Darque'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '5'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Barcelos'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '6'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Nine'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '7'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Contumil'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '8'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'São Gemil'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '9'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'São Mamede de Infesta'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '10'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Leça do Balio'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '11'
       );

INSERT INTO Facility_Path
VALUES (
           (SELECT id FROM Facility WHERE name = 'Leixões'),
           (SELECT pathID FROM Path p JOIN Train t ON p.trainID = t.trainID WHERE t.startTime='10:00'),
           '12'
       );

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES (
           (SELECT trainID FROM Train WHERE startTime='09:45'),
           (SELECT id FROM Facility WHERE name='Leixões'),
           (SELECT id FROM Facility WHERE name='Darque'),
           DATE '2025-03-10'
       );

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES (
           (SELECT trainID FROM Train WHERE startTime='09:45'),
           (SELECT id FROM Facility WHERE name='Contumil'),
           (SELECT id FROM Facility WHERE name='Valença'),
           DATE '2025-03-10'
       );

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES (
           (SELECT trainID FROM Train WHERE startTime='09:45'),
           (SELECT id FROM Facility WHERE name='Leixões'),
           (SELECT id FROM Facility WHERE name='Valença'),
           DATE '2025-03-10'
       );

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES (
           (SELECT trainID FROM Train WHERE startTime='09:45'),
           (SELECT id FROM Facility WHERE name='Leixões'),
           (SELECT id FROM Facility WHERE name='Caminha'),
           DATE '2025-03-10'
       );

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES (
           (SELECT trainID FROM Train WHERE startTime='09:45'),
           (SELECT id FROM Facility WHERE name='Nine'),
           (SELECT id FROM Facility WHERE name='Valença'),
           DATE '2025-03-10'
       );

-- FREIGHT 6 → Train 21 (18:00)
INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES (
           (SELECT trainID FROM Train WHERE startTime='18:00'),
           (SELECT id FROM Facility WHERE name='Darque'),
           (SELECT id FROM Facility WHERE name='Leixões'),
           DATE '2025-03-10'
       );

-- FREIGHT 7 → sem trainID
INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES (
           NULL,
           (SELECT id FROM Facility WHERE name='Leixões'),
           (SELECT id FROM Facility WHERE name='Porto Campanhã'),
           DATE '2025-03-10'
       );

-- FREIGHTS para o comboio das 10:00 (Train 22)
INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES (
           (SELECT trainID FROM Train WHERE startTime='10:00'),
           (SELECT id FROM Facility WHERE name='Darque'),
           (SELECT id FROM Facility WHERE name='Leixões'),
           DATE '2025-06-10'
       );

INSERT INTO Freight (trainID, startFacilityID, endFacilityID, freightDate)
VALUES (
           (SELECT trainID FROM Train WHERE startTime='10:00'),
           (SELECT id FROM Facility WHERE name='Valença'),
           (SELECT id FROM Facility WHERE name='Leixões'),
           DATE '2025-06-10'
       );


INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 077'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Nine')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Valença')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 077'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Valença')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-06-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 078'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Nine')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Valença')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 078'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Valença')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-06-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 079'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Nine')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Valença')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 079'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Valença')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-06-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 080'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Nine')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Valença')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 080'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Valença')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-06-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 089'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Contumil')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Valença')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 090'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Leixões')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Porto Campanhã')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 091'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Leixões')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Porto Campanhã')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '356 3 092'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Leixões')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Porto Campanhã')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '181 1 011'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Leixões')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Valença')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '181 1 011'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Valença')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-06-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '181 1 012'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Leixões')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Valença')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '181 1 012'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Valença')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-06-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '181 1 013'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Leixões')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Caminha')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 001'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Leixões')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Darque')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 001'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Darque')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-06-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 002'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Leixões')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Darque')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 002'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Darque')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-06-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 003'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Darque')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 004'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Leixões')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Darque')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 004'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Darque')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-06-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 005'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Leixões')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Darque')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 005'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Darque')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-06-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 006'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Leixões')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Darque')
              AND freightDate     = DATE '2025-03-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 006'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Darque')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-06-10')
       );

INSERT INTO Wagon_Freight (wagonID, freightID)
VALUES (
           (SELECT wagonID FROM Wagon WHERE identifier = '333 0 007'),
           (SELECT freightID FROM Freight
            WHERE startFacilityID = (SELECT id FROM Facility WHERE name='Darque')
              AND endFacilityID   = (SELECT id FROM Facility WHERE name='Leixões')
              AND freightDate     = DATE '2025-03-10')
       );



