package jparsec.observer;

import jparsec.ephem.Functions;
import jparsec.math.Constant;
import jparsec.vo.SimbadQuery;

public class LocationElementTest {
    /**
     * For unit testing only.
     *
     *@param args Not used.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("LocationElement test");

        double lon1 = Math.random() * Constant.TWO_PI;
        double lon2 = Math.random() * Constant.TWO_PI;
        double lat1 = Math.random() * Math.PI - Constant.PI_OVER_TWO;
        double lat2 = Math.random() * Math.PI - Constant.PI_OVER_TWO;

        LocationElement loc1 = new LocationElement(lon1, lat1, 1.0);
        LocationElement loc2 = new LocationElement(lon2, lat2, 1.0);

        double PA = LocationElement.getPositionAngle(loc1, loc2) * Constant.RAD_TO_DEG;
        double PA2 = LocationElement.getPositionAngle(loc1, loc2) * Constant.RAD_TO_DEG;

        System.out.println(Functions.formatAngle(loc1.getLongitude(), 1) + " / " + Functions.formatAngle(loc1.getLatitude(), 1));
        System.out.println(Functions.formatAngle(loc2.getLongitude(), 1) + " / " + Functions.formatAngle(loc2.getLatitude(), 1));
        System.out.println("PA: " + PA + " / " + PA2);

        String obj = "M31";
        loc1 = new LocationElement(obj, false);
        System.out.println(obj + ": " + loc1.toStringAsEquatorialLocation());
        loc1 = SimbadQuery.query(obj).getLocation();
        System.out.println(obj + ": " + loc1.toStringAsEquatorialLocation());
    }
}
