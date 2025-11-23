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
