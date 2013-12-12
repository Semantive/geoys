-- Countries
CREATE TABLE Countries (
  id         SERIAL               PRIMARY KEY,
  iso2       CHAR(2)                            NOT NULL,
  iso3       CHAR(3)                            NOT NULL,
  name       VARCHAR(200)                       NOT NULL,
  asciiName  VARCHAR(200)                       NOT NULL,
  location   GEOMETRY(Point, 4326)              NOT NULL,
  population INTEGER                            NOT NULL,
  geoId      INTEGER                            NOT NULL
);

-- Time zones
CREATE TABLE TIMEZONES (
  id         SERIAL                 PRIMARY KEY,
  code       VARCHAR(40)            NOT NULL,
  gmtOffset  NUMERIC(3, 1)          NOT NULL,
  dstOffset  NUMERIC(3, 1)          NOT NULL,
  rawOffset  NUMERIC(3, 1)          NOT NULL
);

-- Administrative divisions
CREATE TABLE ADM1 (
  id         SERIAL                 PRIMARY KEY,
  name       VARCHAR(200)                                    NOT NULL,
  asciiName  VARCHAR(200)                                    NOT NULL,
  location   GEOMETRY(Point, 4326)                           NOT NULL,
  countryId  INTEGER                REFERENCES countries(id) NOT NULL,
  timezone   INTEGER                REFERENCES timezones(id) NOT NULL,
  population INTEGER                                         NOT NULL,
  geoId      INTEGER                                         NOT NULL
);

CREATE TABLE ADM2 (
  id         SERIAL                 PRIMARY KEY,
  name       VARCHAR(200)                                    NOT NULL,
  asciiName  VARCHAR(200)                                    NOT NULL,
  location   GEOMETRY(Point, 4326)                           NOT NULL,
  countryId  INTEGER                REFERENCES countries(id) NOT NULL,
  timezone   INTEGER                REFERENCES timezones(id) NOT NULL,
  population INTEGER                                         NOT NULL,
  adm1Id     INTEGER                REFERENCES ADM1(id)      NOT NULL,
  geoId      INTEGER                                         NOT NULL
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
  geoId      INTEGER                                         NOT NULL
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
  geoId      INTEGER                                         NOT NULL
);

-- Populated places
CREATE TABLE places (
  id         INTEGER               PRIMARY KEY,
  name       VARCHAR(200)                                    NOT NULL,
  asciiName  VARCHAR(200)                                    NOT NULL,
  location   GEOMETRY(Point, 4326)                           NOT NULL,
  countryId  INTEGER               REFERENCES countries(id)  NOT NULL,
  timezone   INTEGER               REFERENCES timezones(id)  NOT NULL,
  population INTEGER                                         NOT NULL,
  adm1Id     INTEGER               REFERENCES adms(id),
  adm2Id     INTEGER               REFERENCES adms(id),
  adm3Id     INTEGER               REFERENCES adms(id),
  adm4Id     INTEGER               REFERENCES adms(id),
  parentId   INTEGER                                         NOT NULL
);