package jparsec.ephem;

import jparsec.io.ConsoleReport;
import jparsec.math.Constant;
import jparsec.observer.LocationElement;

public class PrecessionTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Precession test");

        EphemerisElement eph = new EphemerisElement();
        eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_2006;

        String ra = "05h 32m 59.061s";
        String dec = "-05d 11' 52.28''";
        LocationElement loc0 = new LocationElement(Functions.parseRightAscension(ra), Functions.parseDeclination(dec), 1.0);
        LocationElement loc = loc0.clone();
        System.out.println("ra: " + Functions.formatRA(loc.getLongitude()));
        System.out.println("dec: " + Functions.formatDEC(loc.getLatitude()));

        double from = Constant.J2000, to = Constant.J1900;

        System.out.println("");
        System.out.println("IAU 2006");
        double q[] = LocationElement.parseLocationElement(loc);
        double eq[] = Precession.precess(from, to, q, eph);
        loc = LocationElement.parseRectangularCoordinates(eq);
        System.out.println("ra: " + Functions.formatRA(loc.getLongitude()));
        System.out.println("dec: " + Functions.formatDEC(loc.getLatitude()));
        eq = Precession.precess(to, from, eq, eph);
        loc = LocationElement.parseRectangularCoordinates(eq);
        System.out.println("ra: " + Functions.formatRA(loc.getLongitude()));
        System.out.println("dec: " + Functions.formatDEC(loc.getLatitude()));
        ConsoleReport.doubleArrayReport(Precession.getAngles(false, to, eph), "f3.8");

        System.out.println("");
        System.out.println("IAU 2000");
        eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_2000;
        q = LocationElement.parseLocationElement(loc0);
        eq = Precession.precess(from, to, q, eph);
        loc = LocationElement.parseRectangularCoordinates(eq);
        System.out.println("ra: " + Functions.formatRA(loc.getLongitude()));
        System.out.println("dec: " + Functions.formatDEC(loc.getLatitude()));
        eq = Precession.precess(to, from, eq, eph);
        loc = LocationElement.parseRectangularCoordinates(eq);
        System.out.println("ra: " + Functions.formatRA(loc.getLongitude()));
        System.out.println("dec: " + Functions.formatDEC(loc.getLatitude()));
        ConsoleReport.doubleArrayReport(Precession.getAngles(false, to, eph), "f3.8");
        // Note the inconsistency of the IAU 2000 precession model, 0.04" per century in DEC !

        System.out.println("");
        System.out.println("Laskar 1986");
        eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.LASKAR_1986;
        q = LocationElement.parseLocationElement(loc0);
        eq = Precession.precess(from, to, q, eph);
        loc = LocationElement.parseRectangularCoordinates(eq);
        System.out.println("ra: " + Functions.formatRA(loc.getLongitude()));
        System.out.println("dec: " + Functions.formatDEC(loc.getLatitude()));
        eq = Precession.precess(to, from, eq, eph);
        loc = LocationElement.parseRectangularCoordinates(eq);
        System.out.println("ra: " + Functions.formatRA(loc.getLongitude()));
        System.out.println("dec: " + Functions.formatDEC(loc.getLatitude()));
        ConsoleReport.doubleArrayReport(Precession.getAngles(false, to, eph), "f3.8");

        System.out.println("");
        System.out.println("Newcomb");
        q = LocationElement.parseLocationElement(loc0);
        eq = Precession.precessionNewcomb(from, to, q);
        loc = LocationElement.parseRectangularCoordinates(eq);
        System.out.println("ra: " + Functions.formatRA(loc.getLongitude()));
        System.out.println("dec: " + Functions.formatDEC(loc.getLatitude()));
        eq = Precession.precessionNewcomb(to, from, eq);
        loc = LocationElement.parseRectangularCoordinates(eq);
        System.out.println("ra: " + Functions.formatRA(loc.getLongitude()));
        System.out.println("dec: " + Functions.formatDEC(loc.getLatitude()));

        double jd0 = Constant.J2000, jd = 1219339.078;
        double q1[] = Precession.precessionIAU2006(jd0, jd, q.clone());
        double q2[] = Precession.precessionVondrak2011(jd0, jd, q.clone());
        System.out.println(q1[0] + " / " + q2[0]);
        System.out.println(q1[1] + " / " + q2[1]);
        System.out.println(q1[2] + " / " + q2[2]);
    }
}
