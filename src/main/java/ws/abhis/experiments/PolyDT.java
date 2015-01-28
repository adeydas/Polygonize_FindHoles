package ws.abhis.experiments;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Created by Abhishek on 1/27/2015.
 */
public class PolyDT {
    private Geometry polygon;
    private boolean isHole;

    public PolyDT(Geometry polygon, boolean isHole) {
        this.polygon = polygon;
        this.isHole = isHole;
    }

    public boolean isHole() {
        return isHole;
    }

    public void setHole(boolean isHole) {
        this.isHole = isHole;
    }

    public Geometry getPolygon() {
        return polygon;
    }

    public void setPolygon(Geometry polygon) {
        this.polygon = polygon;
    }
}
