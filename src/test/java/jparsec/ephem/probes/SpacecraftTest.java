package jparsec.ephem.probes;

import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.ephem.planets.EphemElement;
import jparsec.ephem.planets.OrbitEphem;
import jparsec.ephem.planets.OrbitalElement;
import jparsec.io.ConsoleReport;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;

public class SpacecraftTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Spacecraft Test");

        try {
            // Main objects
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            EphemerisElement eph = new EphemerisElement(
                Target.TARGET.JUPITER,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.TOPOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.WILLIAMS_1994,
                EphemerisElement.FRAME.ICRF);

            // Show ephemeris for Jupiter
            AstroDate astro = new AstroDate(1996, AstroDate.JANUARY, 1);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            EphemElement ephem = Ephem.getEphemeris(time, observer, eph, true);
            ConsoleReport.basicEphemReportToConsole(ephem);

            // Now for the Galileo spacecraft when it reached Jupiter
            int probe = Spacecraft.searchProbe("Galileo-10");
            eph.targetBody = Target.TARGET.NOT_A_PLANET;
            eph.algorithm = EphemerisElement.ALGORITHM.PROBE;
            eph.orbit = Spacecraft.getProbeElement(probe);
            ephem = Spacecraft.orbitEphemeris(time, observer, eph);
            ConsoleReport.basicEphemReportToConsole(ephem);

            double JD = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            String name = Spacecraft.getName(probe) + " / " + Spacecraft.getPhase(probe) + " / " +
                    Spacecraft.searchProbe(Spacecraft.getFullName(probe));
            System.out.println("JD " + JD + " / " + name);

            System.out.println("Horizons to JPARSEC:");
            String horizons[] = {
                    "2456018.500000000 = A.D. 2012-Apr-01 00:00:00.0000 (CT)",
                    "EC= 2.186288185825909E-01 QR= 9.849980637718811E-01 IN= 1.666450728710398E+00",
                    "OM= 2.427043236295177E+02 W = 1.711208010196681E+02 Tp=  2455883.068369616754",
                    "N = 6.963652534091054E-01 MA= 9.430988161155695E+01 TA= 1.180906444526340E+02",
                    "A = 1.260601986862495E+00 AD= 1.536205909953109E+00 PR= 5.169700789026945E+02"
            };

            System.out.println(Spacecraft.horizons2JPARSEC("MSL", horizons, 0.0, 0.0));
            System.out.println("Orbital elements to JPARSEC:");
            astro = new AstroDate(2008, AstroDate.JANUARY, 1);
            OrbitalElement orbit = OrbitEphem.getOrbitalElements(Target.TARGET.VENUS, astro.jd());
            System.out.println(Spacecraft.orbitalElement2JPARSEC(orbit));
            JPARSECException.showWarnings();
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
