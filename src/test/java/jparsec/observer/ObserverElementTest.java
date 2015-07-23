package jparsec.observer;

import jparsec.ephem.Functions;

public class ObserverElementTest {
    /**
     * For unit testing only
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("ObserverElement test");
        CityElement city = City.findCity("Paris");
        System.out.println("Paris is " + city.name);

        ObserverElement obs = ObserverElement.parseCity(city);

        System.out.println("Paris is " + obs.getName());
        System.out.println("Paris is at " + obs.getHeight() + "m ASL");
        System.out.println("Paris is on " + obs.getMotherBody());

        String ip = "";
        obs = new ObserverElement(ip);
        System.out.println("Current user data:");
        System.out.println("Location: " + obs.getName());
        System.out.println("Longitude: " + Functions.formatAngle(obs.getLongitudeDeg(), 1));
        System.out.println("Latitude: " + Functions.formatAngle(obs.getLatitudeDeg(), 1));
        System.out.println("Height: " + obs.getHeight());
        System.out.println("Time Zone: " + obs.getTimeZone());
        System.out.println("DST Code: " + obs.getDSTCode());
        System.out.println("Tests launched from " + obs.getName() + ", at " +
                Functions.formatAngleAsDegrees(obs.getLongitudeDeg(), 3) + ", " +
                Functions.formatAngleAsDegrees(obs.getLatitudeDeg(), 3));
    }
}
