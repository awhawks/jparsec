package jparsec.observer;

import jparsec.ephem.Functions;
import jparsec.util.JPARSECException;

public class ReferenceEllipsoidTest {
    /**
     * For unit testing only
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("ReferenceEllipsoid test");

        try {
            ObservatoryElement observ = Observatory.findObservatorybyName("Teide");
            ObserverElement obs = ObserverElement.parseObservatory(observ);

            System.out.println(obs.getName());
            System.out.println("geodetic lon " + Functions.formatAngle(obs.getLongitudeRad(), 3));
            System.out.println("geodetic lat " + Functions.formatAngle(obs.getLatitudeRad(), 3));
            System.out.println("geocentric lon " + Functions.formatAngle(obs.getGeoLon(), 3));
            System.out.println("geocentric lat " + Functions.formatAngle(obs.getGeoLat(), 3));
            System.out.println("alt " + obs.getHeight());

            ObserverElement obs1 = new ObserverElement("Paris",
                Functions.parseDeclination("-2 20 14"),
                Functions.parseDeclination("48 50 11"), 0, 0);
            ObserverElement obs2 = new ObserverElement("USNO",
                Functions.parseDeclination("77 03 56"),
                Functions.parseDeclination("38 55 17"), 0, 0);
            System.out.println("Distance obs. Paris to USNO (km): " + ObserverElement.getDistance(obs1, obs2));
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
