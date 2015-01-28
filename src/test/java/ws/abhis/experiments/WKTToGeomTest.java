package ws.abhis.experiments;

import com.vividsolutions.jts.geom.Geometry;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class WKTToGeomTest extends TestCase {

    public void testWktToGeometry() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/PR")));
        String line = "";
        StringBuffer buffer = new StringBuffer();
        while ((line = br.readLine())!=null) {
            buffer.append(line + "\n");
        }

        WKTToGeom wktToGeom = new WKTToGeom();
        Geometry geometry = wktToGeom.wktToGeometry(buffer.toString());

        assertTrue((geometry.toString().compareTo(buffer.toString())) == -2);
    }

    public void testGetHoles() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/PR")));
        String line = "";
        StringBuffer buffer = new StringBuffer();
        while ((line = br.readLine())!=null) {
            buffer.append(line + "\n");
        }

        WKTToGeom wktToGeom = new WKTToGeom();
        Geometry geometry = wktToGeom.wktToGeometry(buffer.toString());

        List<PolyDT> holes = wktToGeom.getHoles(geometry);

        int holesCount = 0;
        for (PolyDT polyDT : holes) {
            if (polyDT.isHole()) {
                holesCount++;
            }
        }

        assertTrue(holesCount == 6);
    }
}