BEGIN TRANSACTION;

-- Inserts the Earth and continents.
--  Those entries are not provided in features dump.
INSERT INTO feature
  (geoname_id, default_name, feature_class, feature_code, parent_id, location)
VALUES
  (6295630, 'Earth',          'L', 'AREA', NULL,    ST_GeomFromText('POINT(0                  0)',  4326)),
  (6255146, 'Africa',         'L', 'CONT', 6295630, ST_GeomFromText('POINT(  21.0937      7.1881)', 4326)),
  (6255152, 'Antarctica',     'L', 'CONT', 6295630, ST_GeomFromText('POINT(  16.40626  -78.15856)', 4326)),
  (6255147, 'Asia',           'L', 'CONT', 6295630, ST_GeomFromText('POINT(  89.29688   29.84064)', 4326)),
  (6255148, 'Europe',         'L', 'CONT', 6295630, ST_GeomFromText('POINT(   9.14062   48.69096)', 4326)),
  (6255149, 'North America',  'L', 'CONT', 6295630, ST_GeomFromText('POINT(-100.54688   46.07323)', 4326)),
  (6255151, 'Oceania',        'L', 'CONT', 6295630, ST_GeomFromText('POINT( 138.51562  -18.31281)', 4326)),
  (6255150, 'South America',  'L', 'CONT', 6295630, ST_GeomFromText('POINT( -14.60485  -57.65625)', 4326));

INSERT INTO continent
  (geoname_id, code)
VALUES
  (6255146, 'AF'),
  (6255152, 'AN'),
  (6255147, 'AS'),
  (6255148, 'EU'),
  (6255149, 'NA'),
  (6255151, 'OC'),
  (6255150, 'SA');

COMMIT;