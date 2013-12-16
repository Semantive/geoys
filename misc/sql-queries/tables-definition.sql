CREATE TABLE Continent (
  id            SERIAL               PRIMARY KEY,
  code          CHAR(2)              UNIQUE                     NOT NULL,
  name          VARCHAR(200)                                    NOT NULL,
  ascii_name    VARCHAR(200)                                    NOT NULL,
  geo_id        INTEGER              UNIQUE                     NOT NULL
);

-- Countries
CREATE TABLE Country (
  id            SERIAL               PRIMARY KEY,
  iso2          CHAR(2)              UNIQUE                     NOT NULL,
  iso3          CHAR(3)              UNIQUE                     NOT NULL,
  fips          CHAR(2),
  name          VARCHAR(200)                                    NOT NULL,
  ascii_name    VARCHAR(200)                                    NOT NULL,
  continent_id  INTEGER              REFERENCES Continent(id)   NOT NULL,
  location      GEOMETRY(Point, 4326),
  population    INTEGER                                         NOT NULL,
  geo_id        INTEGER              UNIQUE                     NOT NULL
);

-- Neighbours of a country
CREATE TABLE Country_Neighbour (
  country_id    INTEGER              REFERENCES Country(id),
  neighbour_id  INTEGER              REFERENCES Country(id),

  PRIMARY KEY (country_id, neighbour_id)
);

-- Time zones
CREATE TABLE Timezone (
  id          SERIAL                 PRIMARY KEY,
  country_id  INTEGER                REFERENCES Country(id)   NOT NULL,
  name        VARCHAR(40)            UNIQUE                   NOT NULL,
  gmt_offset  NUMERIC(3, 1)                                   NOT NULL,
  dst_offset  NUMERIC(3, 1)                                   NOT NULL,
  raw_offset  NUMERIC(3, 1)                                   NOT NULL
);

-- Administrative divisions
CREATE TABLE ADM1 (
  id          SERIAL                 PRIMARY KEY,
  name        VARCHAR(200)                                    NOT NULL,
  ascii_name  VARCHAR(200)                                    NOT NULL,
  location    GEOMETRY(Point, 4326),
  adm_code    VARCHAR(40)                                     NOT NULL,
  country_id  INTEGER                REFERENCES Country(id)   NOT NULL,
  timezone_id INTEGER                REFERENCES Timezone(id),
  population  INTEGER,
  geo_id      INTEGER                UNIQUE                   NOT NULL
);

-- CREATE TABLE ADM2 (
--   id         SERIAL                 PRIMARY KEY,
--   name       VARCHAR(200)                                    NOT NULL,
--   ascii_name VARCHAR(200)                                    NOT NULL,
--   location   GEOMETRY(Point, 4326)                           NOT NULL,
--   adm_code   INTEGER                                         NOT NULL,
--   country_id INTEGER                REFERENCES Country(id)   NOT NULL,
--   timezone_id INTEGER                REFERENCES Timezone(id)  NOT NULL,
--   population INTEGER                                         NOT NULL,
--   adm1_id    INTEGER                REFERENCES ADM1(id)      NOT NULL,
--   geo_id     INTEGER                UNIQUE                   NOT NULL
-- );
--
-- CREATE TABLE ADM3 (
--   id         SERIAL                 PRIMARY KEY,
--   name       VARCHAR(200)                                    NOT NULL,
--   ascii_name VARCHAR(200)                                    NOT NULL,
--   location   GEOMETRY(Point, 4326)                           NOT NULL,
--   country_id INTEGER                REFERENCES Country(id)   NOT NULL,
--   timezone   INTEGER                REFERENCES Timezone(id)  NOT NULL,
--   population INTEGER                                         NOT NULL,
--   adm1_id    INTEGER                REFERENCES ADM1(id)      NOT NULL,
--   adm2_id    INTEGER                REFERENCES ADM2(id)      NOT NULL,
--   geo_id     INTEGER                UNIQUE                   NOT NULL
-- );
--
-- CREATE TABLE ADM4 (
--   id         SERIAL                 PRIMARY KEY,
--   name       VARCHAR(200)                                    NOT NULL,
--   ascii_name VARCHAR(200)                                    NOT NULL,
--   location   GEOMETRY(Point, 4326)                           NOT NULL,
--   country_id INTEGER                REFERENCES Country(id)   NOT NULL,
--   timezone_id INTEGER                REFERENCES Timezone(id)  NOT NULL,
--   population INTEGER                                         NOT NULL,
--   adm1_id    INTEGER                REFERENCES ADM1(id)      NOT NULL,
--   adm2_id    INTEGER                REFERENCES ADM2(id)      NOT NULL,
--   adm3_id    INTEGER                REFERENCES ADM3(id)      NOT NULL,
--   geo_id     INTEGER                UNIQUE                   NOT NULL
-- );
--
-- -- Populated places
-- CREATE TABLE Place (
--   id         INTEGER                PRIMARY KEY,
--   name       VARCHAR(200)                                    NOT NULL,
--   ascii_name VARCHAR(200)                                    NOT NULL,
--   location   GEOMETRY(Point, 4326)                           NOT NULL,
--   country_id INTEGER                REFERENCES Country(id)   NOT NULL,
--   timezone_id INTEGER                REFERENCES Timezone(id)  NOT NULL,
--   population INTEGER                                         NOT NULL,
--   adm1_id    INTEGER                REFERENCES ADM1(id),
--   adm2_id    INTEGER                REFERENCES ADM2(id),
--   adm3_id    INTEGER                REFERENCES ADM3(id),
--   adm4_id    INTEGER                REFERENCES ADM4(id),
--   geo_id     INTEGER                UNIQUE                   NOT NULL,
--   parent_id  INTEGER                REFERENCES Place(id),
--   level      INTEGER                                         NOT NULL
-- );