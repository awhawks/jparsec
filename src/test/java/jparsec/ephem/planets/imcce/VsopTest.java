package jparsec.ephem.planets.imcce;

import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.ephem.planets.EphemElement;
import jparsec.io.ConsoleReport;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;

public class VsopTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Vsop test");

        try {
            AstroDate astro = new AstroDate(2000, AstroDate.JANUARY, 1, 0, 0, 0);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            EphemerisElement eph = new EphemerisElement(
                Target.TARGET.VENUS,
                EphemerisElement.COORDINATES_TYPE.ASTROMETRIC,
                EphemerisElement.EQUINOX_J2000,
                EphemerisElement.GEOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.IAU_1976,
                EphemerisElement.FRAME.ICRF);

            eph.algorithm = EphemerisElement.ALGORITHM.VSOP87_ELP2000ForMoon;

            EphemElement ephem = Ephem.getEphemeris(time, observer, eph, true);
            String name = ephem.name;
            System.out.println("VSOP87");
            ConsoleReport.fullEphemReportToConsole(ephem);
            System.out.println("DE200");
            eph.algorithm = EphemerisElement.ALGORITHM.JPL_DE405;
            ephem = Ephem.getEphemeris(time, observer, eph, false);
            System.out.println("" + name + " RA: " + Functions.formatRA(ephem.rightAscension, 5));
            System.out.println("" + name + " DEC: " + Functions.formatDEC(ephem.declination, 4));
            System.out.println("" + name + " dist: " + ephem.distance);
            System.out.println("" + name + " l-t: " + ephem.lightTime);
            System.out.println("" + name + " h lon: " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLongitude, 8));
            System.out.println("" + name + " h lat: " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLatitude, 8));

            /*
            double p[] = new double[] { 1, 1, 1.0 };
            double p1[] = Vsop.meanEclipticJ2000ToEquatorial(p);
            System.out.println(p1[0] + "/" + p1[1] + "/" + p1[2]);
            double p2[] = Ephem.eclipticToEquatorial(p, Constant.J2000, EphemerisElement.REDUCE_ALGORITHM.APPLY_IAU2000);
            System.out.println(p2[0] + "/" + p2[1] + "/" + p2[2]);
            double p3[] = Elp2000.meanJ2000InertialToEquatorial(p);
            System.out.println(p3[0] + "/" + p3[1] + "/" + p3[2]);
            double p4[] = Ephem.eclipticToEquatorial(p, Constant.J2000, EphemerisElement.REDUCE_ALGORITHM.APPLY_IAU2000);
            p4 = Ephem.J2000toICRSFrame(p4);
            p4 = Ephem.ICRStoFK5Frame(p4);
            System.out.println(p4[0] + "/" + p4[1] + "/" + p4[2]);
            JPARSECException.showWarnings();
            */
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
