CREATE TABLE Continent (
  id           SERIAL               PRIMARY KEY,
  name         VARCHAR(200)                                    NOT NULL,
  asciiName    VARCHAR(200)                                    NOT NULL,
  geoId        INTEGER              UNIQUE                     NOT NULL
);

-- Countries
CREATE TABLE Country (
  id           SERIAL               PRIMARY KEY,
  iso2         CHAR(2)              UNIQUE                     NOT NULL,
  iso3         CHAR(3)              UNIQUE                     NOT NULL,
  name         VARCHAR(200)                                    NOT NULL,
  asciiName    VARCHAR(200)                                    NOT NULL,
  continentId  INTEGER              REFERENCES Continents(id)  NOT NULL,
  location     GEOMETRY(Point, 4326)                           NOT NULL,
  population   INTEGER                                         NOT NULL,
  geoId        INTEGER              UNIQUE                     NOT NULL
);

-- Neighbours of a country
CREATE TABLE Country_Neighbour (
  countryId    INTEGER              REFERENCES countries(id),
  neighbourId  INTEGER              REFERENCES countries(id),

  PRIMARY KEY (countryId, neighbourId)
);

-- Time zones
CREATE TABLE Timezone (
  id         SERIAL                 PRIMARY KEY,
  code       VARCHAR(40)            UNIQUE                   NOT NULL,
  gmtOffset  NUMERIC(3, 1)                                   NOT NULL,
  dstOffset  NUMERIC(3, 1)                                   NOT NULL,
  rawOffset  NUMERIC(3, 1)                                   NOT NULL
);

-- Administrative divisions
CREATE TABLE ADM1 (
  id         SERIAL                 PRIMARY KEY,
  name       VARCHAR(200)                                    NOT NULL,
  asciiName  VARCHAR(200)                                    NOT NULL,
  location   GEOMETRY(Point, 4326)                           NOT NULL,
  admCode    INTEGER                                         NOT NULL,
  countryId  INTEGER                REFERENCES countries(id) NOT NULL,
  timezone   INTEGER                REFERENCES timezones(id) NOT NULL,
  population INTEGER                                         NOT NULL,
  geoId      INTEGER                UNIQUE                   NOT NULL
);

CREATE TABLE ADM2 (
  id         SERIAL                 PRIMARY KEY,
  name       VARCHAR(200)                                    NOT NULL,
  asciiName  VARCHAR(200)                                    NOT NULL,
  location   GEOMETRY(Point, 4326)                           NOT NULL,
  admCode    INTEGER                                         NOT NULL,
  countryId  INTEGER                REFERENCES countries(id) NOT NULL,
  timezone   INTEGER                REFERENCES timezones(id) NOT NULL,
  population INTEGER                                         NOT NULL,
  adm1Id     INTEGER                REFERENCES ADM1(id)      NOT NULL,
  geoId      INTEGER                UNIQUE                   NOT NULL
);

CREATE TABLE ADM3 (
  id         SERIAL                 PRIMARY KEY,
  name       VARCHAR(200)                                    NOT NULL,
  asciiName  VARCHAR(200)                                    NOT NULL,
  location   GEOMETRY(Point, 4326)                           NOT NULL,
  countryId  INTEGER                REFERENCES countries(id) NOT NULL,
  timezone   INTEGER                REFERENCES timezones(id) NOT NULL,
  population INTEGER                                         NOT NULL,
  adm1Id     INTEGER                REFERENCES ADM1(id)      NOT NULL,
  adm2Id     INTEGER                REFERENCES ADM2(id)      NOT NULL,
  geoId      INTEGER                UNIQUE                   NOT NULL
);

CREATE TABLE ADM4 (
  id         SERIAL                 PRIMARY KEY,
  name       VARCHAR(200)                                    NOT NULL,
  asciiName  VARCHAR(200)                                    NOT NULL,
  location   GEOMETRY(Point, 4326)                           NOT NULL,
  countryId  INTEGER                REFERENCES countries(id) NOT NULL,
  timezone   INTEGER                REFERENCES timezones(id) NOT NULL,
  population INTEGER                                         NOT NULL,
  adm1Id     INTEGER                REFERENCES ADM1(id)      NOT NULL,
  adm2Id     INTEGER                REFERENCES ADM2(id)      NOT NULL,
  adm3Id     INTEGER                REFERENCES ADM3(id)      NOT NULL,
  geoId      INTEGER                UNIQUE                   NOT NULL
);

-- Populated places
CREATE TABLE Place (
  id         INTEGER                PRIMARY KEY,
  name       VARCHAR(200)                                    NOT NULL,
  asciiName  VARCHAR(200)                                    NOT NULL,
  location   GEOMETRY(Point, 4326)                           NOT NULL,
  countryId  INTEGER                REFERENCES countries(id) NOT NULL,
  timezone   INTEGER                REFERENCES timezones(id) NOT NULL,
  population INTEGER                                         NOT NULL,
  adm1Id     INTEGER                REFERENCES ADM1(id),
  adm2Id     INTEGER                REFERENCES ADM2(id),
  adm3Id     INTEGER                REFERENCES ADM3(id),
  adm4Id     INTEGER                REFERENCES ADM4(id),
  geoId      INTEGER                UNIQUE                   NOT NULL,
  parentId   INTEGER                REFERENCES Place(id),
  level      INTEGER                                         NOT NULL
);