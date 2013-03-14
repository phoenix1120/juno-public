#!/usr/bin/env python


# Example of reading geometry from a PostGIS table
# Add the sample data in quotes below to a database and then modify the default settings as needed

# Create and enter the database
#"""
#createdb -U postgres -T template_postgis mapnik
#psql -U postgres mapnik

#// Create the test geometry
#DROP TABLE IF EXISTS polygon;
#CREATE TABLE polygon (oid serial);
#SELECT AddGeometryColumn( 'polygon', 'geometry', 4326, 'POLYGON', 2 );
#INSERT INTO polygon (geometry)
#SELECT ST_GeomFromEWKT('SRID=4326;POLYGON((100 0,101 0,101 1,100 1,100 0))') AS geometry;

#"""

from mapnik import *

# Database settings
db_params = dict(
dbname = 'postgistemplate',
user = 'wintrfang',
table = 'world',
password = 'Robzombie1',
host = 'localhost'#,
#row_limit = 40
)

m = Map(1280,1024)
m.background = Color('steelblue')

s = Style()
r=Rule()
r.symbols.append(PolygonSymbolizer(Color('green')))
r.symbols.append(LineSymbolizer(Color('darkorange'),2))
s.rules.append(r)
m.append_style('My Style',s)
lyr = Layer('shape')
lyr.datasource = PostGIS(**db_params)
lyr.styles.append('My Style')


# Buffer the same table to create a different background layer
s2 = Style()
r2=Rule()
r2.symbols.append(PolygonSymbolizer(Color('steelblue')))
r2.symbols.append(LineSymbolizer(Color('darkblue'),3))
s2.rules.append(r2)
m.append_style('My Style2',s2)
lyr2 = Layer('shape_buffer')
#db_params['table'] = '(select ST_Buffer(geom, 1) as geometry from %s) polygon' % db_params['table']
lyr2.datasource = PostGIS(**db_params)
lyr2.styles.append('My Style2')

# Append the second, background layer first, since Mapnik uses the painter's model. currently the second layer is exactly the same as the first layer.
#m.layers.append(lyr2)
m.layers.append(lyr)

#m.zoom_to_box(lyr2.envelope())
# We have to manually zoom out since the buffered layer's envelope is not properly read by Mapnik
m.zoom_all()
save_map(m,'postgis.xml')
render_to_file(m,'postgis_geometry.png')
