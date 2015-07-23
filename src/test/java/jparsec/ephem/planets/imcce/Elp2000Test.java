package jparsec.ephem.planets.imcce;

import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.ephem.planets.EphemElement;
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
        System.out.println("Elp2000 Test");

        try {
            AstroDate astro = new AstroDate(2435109);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);
            CityElement city = City.findCity("Madrid");
            EphemerisElement eph = new EphemerisElement(Target.TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.ICRF);
            ObserverElement observer = ObserverElement.parseCity(city);

            String name = eph.targetBody.getName();
            eph.algorithm = EphemerisElement.ALGORITHM.VSOP87_ELP2000ForMoon;

            EphemElement ephem = Ephem.getEphemeris(time, observer, eph, false);

            System.out.println("jd " + astro.jd() + " / dst " + TimeScale.getDST(astro.jd(), observer));
            System.out.println("ELP2000");
            System.out.println("" + name + " RA: " + Functions.formatRA(ephem.rightAscension, 5));
            System.out.println("" + name + " DEC: " + Functions.formatDEC(ephem.declination, 4));
            System.out.println("" + name + " dist: " + ephem.distance);
            System.out.println("" + name + " elong: " + Functions.formatAngleAsDegrees(ephem.elongation, 8));
            System.out.println("" + name + " phaseAng: " + Functions.formatAngleAsDegrees(ephem.phaseAngle, 8));
            System.out.println("" + name + " hel. ecl. lon: " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLongitude, 8));
            System.out.println("" + name + " hel. ecl. lat: " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLatitude, 8));
            System.out.println("" + name + " hel. ecl. dist: " + ephem.distanceFromSun);

            System.out.println("DE200");
            eph.algorithm = EphemerisElement.ALGORITHM.JPL_DE200;
            ephem = Ephem.getEphemeris(time, observer, eph, false);
            System.out.println("" + name + " RA: " + Functions.formatRA(ephem.rightAscension, 5));
            System.out.println("" + name + " DEC: " + Functions.formatDEC(ephem.declination, 4));
            System.out.println("" + name + " dist: " + ephem.distance);
            System.out.println("" + name + " elong: " + Functions.formatAngleAsDegrees(ephem.elongation, 8));
            System.out.println("" + name + " phaseAng: " + Functions.formatAngleAsDegrees(ephem.phaseAngle, 8));
            System.out.println("" + name + " hel. ecl. lon: " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLongitude, 8));
            System.out.println("" + name + " hel. ecl. lat: " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLatitude, 8));
            System.out.println("" + name + " hel. ecl. dist: " + ephem.distanceFromSun);
            //ConsoleReport.fullEphemReportToConsole(ephem);
            JPARSECException.showWarnings();
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
