package jparsec.observer;

import jparsec.ephem.Functions;
import jparsec.math.Constant;
import jparsec.util.JPARSECException;

public class ReferenceEllipsoidTest {
    /**
     * For unit testing only
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("ReferenceEllipsoid Test");

        try {
            ObservatoryElement observ = Observatory.findObservatorybyName("Teide");
            ObserverElement obs = ObserverElement.parseObservatory(observ);

            double lon = 33.33 * Constant.DEG_TO_RAD, lat = 45.1921 * Constant.DEG_TO_RAD;
            obs.setLongitudeRad(lon);
            obs.setLatitudeRad(lat);
            obs.setHeight(1000, true);

            System.out.println(obs.getName());
            System.out.println("geodetic lon " + Functions.formatAngle(obs.getLongitudeRad(), 3));
            System.out.println("geodetic lat " + Functions.formatAngle(obs.getLatitudeRad(), 3));
            System.out.println(obs.getHeight());

            System.out.println(obs.getName());
            System.out.println("geocentric lon " + Functions.formatAngle(obs.getGeoLon(), 3));
            System.out.println("geocentric lat " + Functions.formatAngle(obs.getGeoLat(), 3));
            System.out.println(obs.getHeight());

            ObserverElement obs1 = new ObserverElement("Paris",
                    Functions.parseDeclination("-2 20 14"),
                    Functions.parseDeclination("48 50 11"), 0, 0);
            ObserverElement obs2 = new ObserverElement("USNO",
                    Functions.parseDeclination("77 03 56"),
                    Functions.parseDeclination("38 55 17"), 0, 0);
            System.out.println("Distance obs. Paris to USNO (km): " +
                    ObserverElement.getDistance(obs1, obs2));
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
