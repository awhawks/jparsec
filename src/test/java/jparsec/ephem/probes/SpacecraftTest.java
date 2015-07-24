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
        System.out.println("Spacecraft test");

        try {
            // Main objects
            AstroDate astro = new AstroDate(1996, AstroDate.JANUARY, 1);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
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
            String horizons[];
            /* = new String[] { // Spirit
                "2452921.500000000 = A.D. 2003-Oct-09 00:00:00.0000 (CT)",
                "EC= 1.950613534257460E-01 QR= 1.015241182486214E+00 IN= 2.568563256177712E-01",
                "OM= 2.582741114396523E+02 W = 3.576251119367114E+02 Tp=  2452797.843160490971",
                "N = 6.958159876484857E-01 MA= 8.604240591231755E+01 TA= 1.081599605263792E+02",
                "A = 1.261265298674612E+00 AD= 1.507289414863009E+00 PR= 5.173781666279647E+02"
            };

            horizons = new String[] {
                "2456018.500000000 = A.D. 2012-Apr-01 00:00:00.0000 (CT)",
                 "EC= 2.186288185825909E-01 QR= 9.849980637718811E-01 IN= 1.666450728710398E+00",
                 "OM= 2.427043236295177E+02 W = 1.711208010196681E+02 Tp=  2455883.068369616754",
                 "N = 6.963652534091054E-01 MA= 9.430988161155695E+01 TA= 1.180906444526340E+02",
                 "A = 1.260601986862495E+00 AD= 1.536205909953109E+00 PR= 5.169700789026945E+02"
            };
            */

            horizons = new String[] {
                "2457221.500000000 = A.D. 2015-Jul-18 00:00:00.0000 (CT)",
                "EC= 1.396502418164295E+00 QR= 2.239569958804241E+00 IN= 2.423181841903402E+00",
                "OM= 2.320104143427997E+02 W = 2.852782283712027E+02 Tp=  2453774.600156403612",
                "N = 7.342194488175884E-02 MA= 2.530780903294607E+02 TA= 1.268263097078230E+02",
                "A =-5.648313493705464E+00 AD= 6.684586453809735E+91 PR= 1.157407291666667E+95"
            };

            System.out.println(Spacecraft.horizons2JPARSEC("New Horizons", horizons, 0.0, 0.0));
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
