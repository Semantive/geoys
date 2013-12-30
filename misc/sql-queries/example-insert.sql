INSERT INTO places (name, asciiName, location, countryId,
                    adm1Id, adm2Id, adm3Id, adm4Id)
VALUES ('Warszawa', 'Warszawa',
        ST_GeomFromText('POINT(21.01178 52.22977)', 4326), 10, 101, 102, 103, 104);

INSERT INTO places (name, asciiName, location, countryId,
                    adm1Id, adm2Id, adm3Id, adm4Id)
VALUES ('Kielce', 'Kielce',
        ST_GeomFromText('POINT(20.62752 50.87033)', 4326), 10, 101, 102, 103, 104);