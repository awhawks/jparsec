package jparsec.ephem.planets.imcce;

import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.ephem.planets.EphemElement;
import jparsec.io.ConsoleReport;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;

public class Elp2000Test {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Elp2000 test");

        try {
            AstroDate astro = new AstroDate(2435109);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);
            CityElement city = City.findCity("Madrid");
            EphemerisElement eph = new EphemerisElement(
                Target.TARGET.Moon,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.GEOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.IAU_2006,
                EphemerisElement.FRAME.ICRF);
            ObserverElement observer = ObserverElement.parseCity(city);
            eph.algorithm = EphemerisElement.ALGORITHM.VSOP87_ELP2000ForMoon;
            EphemElement ephem = Ephem.getEphemeris(time, observer, eph, false);
            System.out.println("jd " + astro.jd() + " / dst " + TimeScale.getDST(astro.jd(), observer));
            ephem.name = eph.targetBody.getName() + " (ELP2000)";
            ConsoleReport.basicEphemReportToConsole(ephem);
            eph.algorithm = EphemerisElement.ALGORITHM.JPL_DE200;
            ephem = Ephem.getEphemeris(time, observer, eph, false);
            ephem.name = eph.targetBody.getName() + " (DE200)";
            ConsoleReport.basicEphemReportToConsole(ephem);
            JPARSECException.showWarnings();
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
