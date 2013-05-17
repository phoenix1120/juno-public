SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;
BEGIN;
CREATE TABLE "public"."custom" (gid serial PRIMARY KEY,
"id" numeric(10,0));
SELECT AddGeometryColumn('public','custom','the_geom','4326','MULTIPOLYGON',2);
INSERT INTO "public"."custom" ("id",the_geom) VALUES (NULL,'0106000020E6100000010000000103000020E6100000010000000A00000092F3B2884FB050C0B8A00618B7373D404ECBCD765B5833C002537308A5DC4F40E2DB89C046CD41404C59FD8F83763A40D020EA63BD614240AE538F67D7EC40C07891828C304323C0EE4460A376F442C0485B51AD1AA720C0A08B4DCC77051440EC28017126983AC000A19447D01F0A401BED6169643240C07A922C1B7C9B43C05B8A150E13F551C0513CF4CEE7E142C092F3B2884FB050C0B8A00618B7373D40');
CREATE INDEX "custom_the_geom_gist" ON "public"."custom" using gist ("the_geom" gist_geometry_ops);
COMMIT;
