# --- !Ups
CREATE TABLE continent (
  geoname_id    INTEGER               PRIMARY KEY,
  code          CHAR(2)               UNIQUE                            NOT NULL
);

CREATE TABLE country (
  geoname_id    INTEGER               PRIMARY KEY,
  iso2_code     CHAR(2)               UNIQUE                            NOT NULL,
  iso3_code     CHAR(3)               UNIQUE                            NOT NULL,
  iso_numeric   CHAR(3)               UNIQUE                            NOT NULL,
  fips_code     CHAR(2)               UNIQUE                            NOT NULL,
  population    INTEGER                                                 NOT NULL,
  continent_id  INTEGER               REFERENCES continent(geoname_id)  NOT NULL,
  tld           VARCHAR(8)                                              NOT NULL,
  currency_code CHAR(3)                                                 NOT NULL
);

CREATE TABLE timezone (
  id            SERIAL                PRIMARY KEY,
  country_id    INTEGER               REFERENCES country(geoname_id)    NOT NULL,
  name          VARCHAR(40)           UNIQUE                            NOT NULL,
  gmt_offset    NUMERIC(3, 1)                                           NOT NULL,
  dst_offset    NUMERIC(3, 1)                                           NOT NULL,
  raw_offset    NUMERIC(3, 1)                                           NOT NULL
);

CREATE TABLE feature (
  geoname_id    INTEGER               PRIMARY KEY,
  feature_class CHAR(1)                                                 NOT NULL,
  feature_code  VARCHAR(10)                                             NOT NULL,
  adm_code      VARCHAR(40),
  country_id    INTEGER               REFERENCES feature(geoname_id),
  adm1_id       INTEGER               REFERENCES feature(geoname_id),
  adm2_id       INTEGER               REFERENCES feature(geoname_id),
  adm3_id       INTEGER               REFERENCES feature(geoname_id),
  adm4_id       INTEGER               REFERENCES feature(geoname_id),
  parent_id     INTEGER               REFERENCES feature(geoname_id),
  timezone_id   INTEGER               REFERENCES timezone(id),
  population    BIGINT,
  wiki_link     VARCHAR(255)
);

CREATE TABLE name_translation (
  id            SERIAL                PRIMARY KEY,
  geoname_id    INTEGER               REFERENCES feature(geoname_id)    NOT NULL,
  language      CHAR(8)               NOT NULL,
  name          VARCHAR(255)          NOT NULL
);

-- todo: partycje

# --- !Downs

DROP TABLE name_translation;
DROP TABLE feature;
DROP TABLE timezone;
DROP TABLE country;
DROP TABLE continent;