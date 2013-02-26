import mapnik

##############################
## Constants
##############################
MIN_LAT = -35
MAX_LAT = 35
MIN_LONG = -12
MAX_LONG = 12

MAP_WIDTH = 1200
MAP_HEIGHT = 800

##############################
## Program
##############################
polygonStyle = mapnik.Style()

## polygon rules for countries
rule = mapnik.Rule()
rule.filter = mapnik.Filter("[NAME] = 'Angola'")

symbol = mapnik.PolygonSymbolizer(mapnik.Color("#604040"))
rule.symbols.append(symbol)
polygonStyle.rules.append(rule)

rule = mapnik.Rule()
rule.filter = mapnik.Filter("[NAME] != 'Angola'")

symbol = mapnik.PolygonSymbolizer(mapnik.Color("#406040"))
rule.symbols.append(symbol)

polygonStyle.rules.append(rule)

rule = mapnik.Rule()
symbol = mapnik.LineSymbolizer(mapnik.Color("#000000"), 0.1)
rule.symbols.append(symbol)

polygonStyle.rules.append(rule)

## label style for text
labelStyle = mapnik.Style()
rule = mapnik.Rule()
symbol = mapnik.TextSymbolizer(mapnik.Expression('[NAME]'), "DejaVu Sans Book", 12, mapnik.Color("#000000"))
rule.symbols.append(symbol)
labelStyle.rules.append(rule)

## set data source
datasource = mapnik.Shapefile(file="TM_WORLD_BORDERS-0.3.shp")

## define layers
polygonLayer = mapnik.Layer("Polygons")
polygonLayer.datasource = datasource
polygonLayer.styles.append("PolygonStyle")

labelLayer = mapnik.Layer("Labels")
labelLayer.datasource = datasource
labelLayer.styles.append("LabelStyle")

## create map
map = mapnik.Map(MAP_WIDTH, MAP_HEIGHT, "+proj=longlat +datum=WGS84")
map.background = mapnik.Color("#8080a0")
map.append_style("PolygonStyle", polygonStyle)
map.append_style("LabelStyle", labelStyle)
map.layers.append(polygonLayer)
map.layers.append(labelLayer)

## zoom and render
map.zoom_to_box(mapnik.Envelope(MIN_LONG, MIN_LAT, MAX_LONG, MAX_LAT))
mapnik.render_to_file(map, "../public/images/tiles/map.png")
print "rendering map..."


