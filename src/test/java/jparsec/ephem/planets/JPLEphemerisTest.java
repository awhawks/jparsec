package jparsec.ephem.planets;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.graph.DataSet;
import jparsec.io.ConsoleReport;
import jparsec.math.Constant;
import jparsec.math.matrix.Matrix;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;

public class JPLEphemerisTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("JPLEphemeris test");
        JPLEphemeris jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE430);

        // Full calculations
        AstroDate astro = new AstroDate(1992, AstroDate.APRIL, 12, 0, 0, 0);
        TimeElement time = new TimeElement(astro.jd(), TimeElement.SCALE.TERRESTRIAL_TIME);
        CityElement city = City.findCity("Madrid");
        EphemerisElement eph = new EphemerisElement(
            Target.TARGET.JUPITER,
            EphemerisElement.COORDINATES_TYPE.APPARENT,
            EphemerisElement.EQUINOX_OF_DATE,
            EphemerisElement.TOPOCENTRIC,
            EphemerisElement.REDUCTION_METHOD.IAU_1976,
            EphemerisElement.FRAME.ICRF);
        eph.algorithm = jpl.getJPLVersionID();
        ObserverElement observer = ObserverElement.parseCity(city);
        EphemElement ephem = jpl.getJPLEphemeris(time, observer, eph);
        System.out.println("JPL DE430");
        ConsoleReport.basicEphemReportToConsole(ephem);

        jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE405);
        ephem = jpl.getJPLEphemeris(time, observer, eph);

        System.out.println("JPL DE405");
        ConsoleReport.fullEphemReportToConsole(ephem);

        System.out.println("Series96");
        ephem = jparsec.ephem.planets.imcce.Series96.series96Ephemeris(time, observer, eph);
        jparsec.io.ConsoleReport.fullEphemReportToConsole(ephem);

        double lib[] = jpl.getPositionAndVelocity(2455713.5, Target.TARGET.Libration);
        double nut[] = jpl.getPositionAndVelocity(2455713.5, Target.TARGET.Nutation);
        System.out.println("Librations");
        ConsoleReport.stringArrayReport(DataSet.toStringValues(lib));
        // Should be
        // 0,067141829176838490   0,412413988874723900 3522,780878808184800000
        // -0,000121984430648748  -0,000007337186520484   0,230087432221497220

        System.out.println("Nutations");
        ConsoleReport.stringArrayReport(DataSet.toStringValues(nut));
        // Should be
        // 0,000078496970210652  -0,000006384222943097
        // 0,000000404335979328  -0,000000247269507293

        double ang1 = -2 * 0.001 * Constant.ARCSEC_TO_RAD;
        double ang2 = -12 * 0.001 * Constant.ARCSEC_TO_RAD;
        double ang3 = -2 * 0.001 * Constant.ARCSEC_TO_RAD;
        Matrix m = Matrix.getR1(ang1).times(Matrix.getR2(ang2).times(Matrix.getR3(ang3)));
        m.print(19, 16);
    }
}
