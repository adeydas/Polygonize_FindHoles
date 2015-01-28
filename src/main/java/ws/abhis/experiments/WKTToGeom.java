package ws.abhis.experiments;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;
import org.geotools.geojson.geom.GeometryJSON;

import java.util.*;

/**
 * Created by Abhishek on 1/26/2015.
 */
public class WKTToGeom {
    /**
     * Vanilla WKT to Geom
     * @param wkt
     * @return Geometry
     */
    public Geometry wktToGeometry(String wkt) throws Exception {
       Geometry g = polygonize(readGeom(wkt));
       return g;
    }

    /**
     * Find holes in a geometry
     * @param geometry
     * @return List
     */
    public List<PolyDT> getHoles(Geometry geometry) {
        temp = new ArrayList<PolyDT>();
        findHoles(geometry);
        return temp;
    }

    /**
     * Read geometry from WKT or GeoJson
     * @param possibleGeometry
     * @return Geometry
     * @throws Exception
     */
    private Geometry readGeom(String possibleGeometry) throws Exception {
        Geometry geometry = null;

        //try for WKT
        try {
            geometry = new WKTReader().read(possibleGeometry);
        } catch (Exception e) {
            geometry = null;
        }

        //try for GeoJson
        if (geometry == null) {
            try {
                geometry = new GeometryJSON().read(possibleGeometry);
            } catch (Exception e) {
                geometry = null;
            }
        }

        if (geometry == null) {
            throw new Exception("Invalid geometry. Only GeoJson and WKT supported.");
        }
        return geometry;
    }

    /**
     * Polygonize all polygons in a multi-polygon or return the geometry as-is
     * @param geometry
     * @return Geometry
     * @throws Exception
     */
    private Geometry polygonize(Geometry geometry) throws Exception {

            //Polygonize geometry
            Polygonizer polygonizer = new Polygonizer();

            //return the received geometry if its not a multi-polygon
            if (geometry.getGeometryType().toUpperCase().equals("LINESTRING") || geometry.getGeometryType().toUpperCase().equals("MULTILINESTRING") || geometry.getGeometryType().toUpperCase().equals("MULTILINESTRING") || geometry.getGeometryType().toUpperCase().equals("MULTIPOINT") || geometry.getGeometryType().toUpperCase().equals("POLYGON")) {
                return geometry;
            }

            //if its a Polygon
            if (geometry.getGeometryType().toUpperCase().equals("MULTIPOLYGON")) {
                int cGeoms = geometry.getNumGeometries();
                if (cGeoms > 1) {
                    for (int i=0; i<cGeoms; i++) {
                        polygonizer.add(geometry.getGeometryN(i));
                    }

                    Collection polygonList = polygonizer.getPolygons();
                    Iterator it = polygonList.iterator();

                    Polygon[] allGeoms = new Polygon[polygonList.size()];

                    int ii = 0;
                    while (it.hasNext()) {
                        Polygon p = (Polygon) it.next();
                        allGeoms[ii++] = p;
                    }

                    MultiPolygon multiPolygon = new MultiPolygon(allGeoms, new GeometryFactory());

                    Geometry g = (Geometry) multiPolygon;

                    return g;
                } else {
                    return geometry;
                }
            }

            return geometry;
        }




    private List<PolyDT> temp;

    /**
     * Find holes in the geometry
     * @param geometry
     */
    private void findHoles(Geometry geometry) {
        String geometryType = geometry.getGeometryType();
        geometryType = geometryType.toUpperCase();

        if (geometryType.equals("MULTIPOLYGON")) {
            MultiPolygon multiPolygon = (MultiPolygon) geometry;
            for (int i=0; i<multiPolygon.getNumGeometries(); i++) {
                findHoles((Geometry) multiPolygon.getGeometryN(i));
            }
        } else if (geometryType.equals("POLYGON")) {
            Polygon polygon = (Polygon) geometry;
            int noOfHoles = polygon.getNumInteriorRing();



            if (noOfHoles > 0) {
                for (int j = 0; j < noOfHoles; j++) {
                    LineString hole = polygon.getInteriorRingN(j);

                    Coordinate[] coordinates = hole.getCoordinates();

                    GeometryFactory factory = new GeometryFactory();

                    //Find co-ordinate sequence from line string
                    CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);

                    LinearRing linearRing = factory.createLinearRing(coordinateSequence);

                    Polygon p = new Polygon(linearRing, null, factory);


                    temp.add(new PolyDT((Geometry)p, true));
                }
            }

            temp.add(new PolyDT((Geometry)polygon, false));

        } else if (geometryType.equals("LINESTRING")) {
            temp.add(new PolyDT(geometry, false));

        } else if (geometryType.equals("MULTILINESTRING")) {
            temp.add(new PolyDT(geometry, false));

        } else if (geometryType.equals("POINT")) {
            temp.add(new PolyDT(geometry, false));

        } else if (geometryType.equals("MULTIPOINT")) {
            temp.add(new PolyDT(geometry, false));

        }




    }


}
