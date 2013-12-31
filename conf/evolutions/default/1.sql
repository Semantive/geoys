# --- !Ups
CREATE TABLE continent (
  id            SERIAL                PRIMARY KEY,
  code          CHAR(2)               UNIQUE                      NOT NULL,
  name          VARCHAR(200)                                      NOT NULL,
  ascii_name    VARCHAR(200)                                      NOT NULL,
  geo_id        INTEGER               UNIQUE                      NOT NULL
);

-- Countries
CREATE TABLE country (
  id            SERIAL                PRIMARY KEY,
  iso2          CHAR(2)               UNIQUE                      NOT NULL,
  iso3          CHAR(3)               UNIQUE                      NOT NULL,
  fips          CHAR(2),
  name          VARCHAR(200)                                      NOT NULL,
  ascii_name    VARCHAR(200)                                      NOT NULL,
  continent_id  INTEGER               REFERENCES continent(id)    NOT NULL,
  location      GEOMETRY(Point, 4326),
  population    INTEGER                                           NOT NULL,
  geo_id        INTEGER               UNIQUE                      NOT NULL
);

CREATE TABLE timezone (
  id            SERIAL                PRIMARY KEY,
  country_id    INTEGER               REFERENCES Country(id)      NOT NULL,
  name          VARCHAR(40)           UNIQUE                      NOT NULL,
  gmt_offset    NUMERIC(3, 1)                                     NOT NULL,
  dst_offset    NUMERIC(3, 1)                                     NOT NULL,
  raw_offset    NUMERIC(3, 1)                                     NOT NULL
);

CREATE TABLE feature (
  id            SERIAL,
  name          VARCHAR(200)          NOT NULL,
  ascii_name    VARCHAR(200)          NOT NULL,
  f_class       CHAR(1)               NOT NULL,
  f_code        VARCHAR(10)           NOT NULL,
  adm_code      VARCHAR(40),
  timezone_id   BIGINT,
  location      GEOMETRY(Point,4326),
  country_id    BIGINT                NOT NULL,
  population    BIGINT,
  geo_id        BIGINT                NOT NULL,
  adm1_id       BIGINT,
  adm2_id       BIGINT,
  adm3_id       BIGINT,
  adm4_id       BIGINT,
  parent_id     BIGINT,
  level         INTEGER
);

CREATE TABLE adm1 (
  CHECK(f_class = 'A' AND f_code = 'ADM1')
) INHERITS(feature);

ALTER TABLE adm1 ADD CONSTRAINT fk_adm1_country
  FOREIGN KEY(country_id) REFERENCES country(id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE adm1 ADD CONSTRAINT fk_adm1_timezone
  FOREIGN KEY(timezone_id) REFERENCES timezone(id) ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE adm2 (
  CHECK(f_class = 'A' AND f_code = 'ADM2')
) INHERITS(feature);

ALTER TABLE adm2 ADD CONSTRAINT fk_adm2_country
  FOREIGN KEY(country_id) REFERENCES country(id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE adm2 ADD CONSTRAINT fk_adm2_timezone
  FOREIGN KEY(timezone_id) REFERENCES timezone(id) ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE adm3 (
  CHECK(f_class = 'A' AND f_code = 'ADM3')
) INHERITS(feature);

ALTER TABLE adm3 ADD CONSTRAINT fk_adm3_country
  FOREIGN KEY(country_id) REFERENCES country(id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE adm3 ADD CONSTRAINT fk_adm3_timezone
  FOREIGN KEY(timezone_id) REFERENCES timezone(id) ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE adm4 (
  CHECK(f_class = 'A' AND f_code = 'ADM4')
) INHERITS(feature);

ALTER TABLE adm4 ADD CONSTRAINT fk_adm4_country
  FOREIGN KEY(country_id) REFERENCES country(id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE adm4 ADD CONSTRAINT fk_adm4_timezone
  FOREIGN KEY(timezone_id) REFERENCES timezone(id) ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE ppl (
  CHECK(f_class = 'P')
) INHERITS(feature);

ALTER TABLE ppl ADD CONSTRAINT fk_ppl_country
  FOREIGN KEY(country_id) REFERENCES country(id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ppl ADD CONSTRAINT fk_ppl_timezone
  FOREIGN KEY(timezone_id) REFERENCES timezone(id) ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE OR REPLACE FUNCTION feature_insert_trigger()
  RETURNS TRIGGER AS $$
BEGIN
    IF(NEW.f_class = 'P') THEN
        INSERT INTO ppl VALUES (NEW.*);
    ELSIF(NEW.f_class = 'A') THEN
      IF(NEW.f_code = 'ADM1') THEN
        INSERT INTO adm1 VALUES (NEW.*);
      ELSIF(NEW.f_code = 'ADM2') THEN
        INSERT INTO adm2 VALUES (NEW.*);
      ELSIF(NEW.f_code = 'ADM3') THEN
        INSERT INTO adm3 VALUES (NEW.*);
      ELSIF(NEW.f_code = 'ADM4') THEN
        INSERT INTO adm4 VALUES (NEW.*);
      ELSE
        RAISE EXCEPTION 'Unknown A code. Fix the feature_insert_trigger() function.';
      END IF;
    ELSE
      RAISE EXCEPTION 'Unknown feature class. Fix the feature_insert_trigger() function.';
    END IF;

    RETURN NULL;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER insert_feature_trigger
  BEFORE INSERT ON feature
FOR EACH ROW EXECUTE PROCEDURE feature_insert_trigger();

# --- !Downs

DROP TRIGGER insert_feature_trigger ON feature;
DROP FUNCTION  feature_insert_trigger();

DROP TABLE ppl;
DROP TABLE adm4;
DROP TABLE adm3;
DROP TABLE adm2;
DROP TABLE adm1;
DROP TABLE feature;
DROP TABLE timezone;
DROP TABLE country;
DROP TABLE continent;