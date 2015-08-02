package jparsec.ephem.planets.imcce;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.ephem.planets.EphemElement;
import jparsec.ephem.planets.JPLEphemeris;
import jparsec.io.ConsoleReport;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;

public class Series96Test {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Series96 test");

        try {
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);

            AstroDate astro = new AstroDate(2049, AstroDate.JANUARY, 1, 0, 0, 0);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);
            EphemerisElement eph = new EphemerisElement(
                    Target.TARGET.NEPTUNE,
                    EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE,
                    EphemerisElement.GEOCENTRIC,
                    EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.ICRF);

            EphemElement ephem = Series96.series96Ephemeris(time, observer, eph);
            ephem.name = eph.targetBody.getName() + " (Series96)";
            ConsoleReport.basicEphemReportToConsole(ephem);
            eph.algorithm = EphemerisElement.ALGORITHM.JPL_DE403;
            JPLEphemeris jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE403);
            ephem = jpl.getJPLEphemeris(time, observer, eph);
            ephem.name = eph.targetBody.getName() + " (DE403)";
            ConsoleReport.basicEphemReportToConsole(ephem);

            JPARSECException.showWarnings();
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
