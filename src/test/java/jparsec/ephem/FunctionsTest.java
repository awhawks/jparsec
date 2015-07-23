package jparsec.ephem;

import jparsec.observer.LocationElement;

public class FunctionsTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        System.out.println(Functions.roundDownToPlace(1346.4667, 1));
        System.out.println(Functions.roundUpToPlace(1346.4667, 2));
        System.out.println(Functions.roundToPlace(1346.4667, 2));
        System.out.println(Functions.formatValue(48.3, 0, 2, true));
        double val = 2453736.499999995;
        int decimals = 8;
        System.out.println(Functions.formatValue(val, decimals) + '\n');

        LocationElement loc1 = new LocationElement(Functions.parseRightAscension("12 41 8.63"), Functions.parseDeclination("-5 37 54.2"), 1.0);
        LocationElement loc2 = new LocationElement(Functions.parseRightAscension("12 52 5.21"), Functions.parseDeclination("-4 22 26.2"), 1.0);
        LocationElement loc3 = new LocationElement(Functions.parseRightAscension("12 39 28.11"), Functions.parseDeclination("-1 50 3.7"), 1.0);

        LocationElement loc = Functions.getCircleContainingThreeObjects(loc1, loc2, loc3);
        System.out.println(Functions.formatRA(loc.getLongitude()));
        System.out.println(Functions.formatDEC(loc.getLatitude()));
        System.out.println(Functions.formatAngleAsDegrees(loc.getRadius(), 5));

        System.out.println();

        loc1 = new LocationElement(Functions.parseRightAscension("9 05 41.44"), Functions.parseDeclination("18 30 30"), 1.0);
        loc2 = new LocationElement(Functions.parseRightAscension("9 9 29"), Functions.parseDeclination("17 43 56.7"), 1.0);
        loc3 = new LocationElement(Functions.parseRightAscension("8 59 47.14"), Functions.parseDeclination("17 49 36.8"), 1.0);
        loc = Functions.getCircleContainingThreeObjects(loc1, loc2, loc3);
        System.out.println(Functions.formatRA(loc.getLongitude()));
        System.out.println(Functions.formatDEC(loc.getLatitude()));
        System.out.println(Functions.formatAngleAsDegrees(loc.getRadius(), 5));
    }
}
