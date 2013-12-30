-- Insert
INSERT INTO countries(
  id, iso2, iso3, name, asciiName
) VALUES (
  798544, 'PL', 'POL', 'Poland', 'Poland'
)

INSERT INTO places(
  id, name, asciiName, location, countryId, adm1Id, adm2Id, adm3Id, adm4Id
) VALUES (
  756135, 'Warsaw', 'Warsaw', ST_GeomFromText('POINT(21.01178 52.22977)', 4326), 798544,
)

, 10, 101, 102, 103, 104);

id        INTEGER                PRIMARY KEY,
  name      VARCHAR(200)                                    NOT NULL,
  asciiName VARCHAR(200)                                    NOT NULL,
  location  GEOMETRY(Point, 4326)                           NOT NULL,
  countryId INTEGER                REFERENCES countries(id) NOT NULL,
  admLvl

  CREATE TABLE places (
  id        INTEGER               PRIMARY KEY,
  name      VARCHAR(200)                                   NOT NULL,
  asciiName VARCHAR(200)                                   NOT NULL,
  location  GEOMETRY(Point, 4326)                          NOT NULL,
  countryId INTEGER               REFERENCES countries(id) NOT NULL,
  adm1Id    INTEGER               REFERENCES adms(id),
  adm2Id    INTEGER               REFERENCES adms(id),
  adm3Id    INTEGER               REFERENCES adms(id),
  adm4Id    INTEGER               REFERENCES adms(id)
);