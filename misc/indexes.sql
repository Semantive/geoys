--those indexes should be created on database after loading data
-- TODO [PJ] Move those to Play migrations, integrate them into Loader process

create index name_translation_main_idx on name_translation(geoname_id, language);
create index name_translation_main_official_idx on name_translation(geoname_id, language, is_official);
create index country_iso_idx on country(iso2_code);
create index feature_hierarchy_idx on feature(parent_id);
create index feature_geoname_id_hierarchy_idx on feature(geoname_id, parent_id);

create index feature_location on feature USING GIST (location);