#!/bin/bash

echo "dropping the custom table"
sudo -u postgres psql -d falcon -c "DROP TABLE IF EXISTS custom;"

echo "adding custom table to db"
shp2pgsql -c -I -W “LATIN1” -s 4326 ../world-borders-data/custom/custom.shp public.custom > ../world-borders-data/custom/custom.sql
sudo -u postgres psql -d falcon -f ../world-borders-data/custom/custom.sql

echo "clearing custom layer cache"
rm -r ../tile-cache/public/images/tiles/custom/

echo "starting OGC server"
./bin/ogcserver-local.py demo/map.xml
