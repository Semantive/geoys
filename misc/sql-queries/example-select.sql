SELECT ST_DISTANCE_SPHERE(
           (SELECT location FROM places WHERE id = 1),
           (SELECT location FROM places WHERE id = 2)
       ) / 1000


SELECT
  continent.name as kontynent,
  country.name as kraj,
  adm1.name AS adm1,
  adm2.name AS adm2,
  adm3.name AS adm3,
  adm4.name AS adm4,
  ppl0.name AS ppl,
  pplx.name AS pplx
FROM ppl PPLX
  left join
  ppl PPL0 on PPLX.parent_id = PPL0.id
  left join
  adm4 on pplx.adm4_id = adm4.id
  left join
  adm3 on pplx.adm3_id = adm3.id
  left join
  adm2 on pplx.adm2_id = adm2.id
  left join
  adm1 on pplx.adm1_id = adm1.id
  left join
  country on country.id = pplx.country_id
  left join
  continent on country.continent_id = continent.id
WHERE
  pplx.code = 'PPLX'
ORDER BY
  ST_DISTANCE_SPHERE(ST_GeomFromText('POINT(21.06155 52.248403)'), pplx.location)
LIMIT 50;


-- Example of hierarchical query (returns all hierarchy from current geoname feature to the root).
WITH RECURSIVE
  parent_feature(geoname_id, parent_id, depth, path) AS (

      SELECT
        f.geoname_id, f.parent_id, 1::INT AS depth, ARRAY[f.geoname_id] AS path
      FROM
        feature AS f
      WHERE f.parent_id IS NULL

    UNION ALL

      SELECT
        f.geoname_id, f.parent_id, pf.depth + 1 AS depth, path || ARRAY[f.geoname_id]
      FROM
        parent_feature AS pf, feature AS f
      WHERE
        f.parent_id = pf.geoname_id
)
SELECT * FROM feature WHERE geoname_id = ANY((SELECT path FROM parent_feature AS f WHERE f.geoname_id = 798544)::integer[])
