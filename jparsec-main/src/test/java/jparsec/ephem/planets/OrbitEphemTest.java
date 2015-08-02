package jparsec.ephem.planets;

import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.io.ConsoleReport;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;
import jparsec.util.Configuration;
import jparsec.util.JPARSECException;

public class OrbitEphemTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("OrbitEphem Test");

        try {
            AstroDate astro = new AstroDate(); //2006, AstroDate.JANUARY, 1);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            EphemerisElement eph = new EphemerisElement(
                Target.TARGET.MARS,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.TOPOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.IAU_2009,
                EphemerisElement.FRAME.ICRF);
            eph.algorithm = EphemerisElement.ALGORITHM.VSOP87_ELP2000ForMoon;

            EphemElement ephem = Ephem.getEphemeris(time, observer, eph, true);
            String name = eph.targetBody.getName();
            System.out.println("" + name + " RA: " + Functions.formatRA(ephem.rightAscension));
            System.out.println("" + name + " DEC: " + Functions.formatDEC(ephem.declination));
            System.out.println("" + name + " dist: " + ephem.distance);

            // Calculate ephemeris
            double JD = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            OrbitalElement orbit = OrbitEphem.getOrbitalElements(eph.targetBody, JD);
            //orbit.meanMotion = OrbitEphem.obtainMeanMotion(eph.targetBody, orbit.semimajorAxis);
            eph.orbit = orbit;
            ephem = OrbitEphem.orbitEphemeris(time, observer, eph);

            System.out.println("ORBIT");
            System.out.println("JD " + JD);
            System.out.println("" + name + " RA: " + Functions.formatRA(ephem.rightAscension));
            System.out.println("" + name + " DEC: " + Functions.formatDEC(ephem.declination));
            System.out.println("" + name + " dist: " + ephem.distance);

            // Calculate orbit of a comet
            AstroDate new_astro = new AstroDate(); //2451545.0);
            TimeElement new_time = new TimeElement(new_astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            if (!Configuration.isAcceptableDateForComets(new_time.astroDate, false))
                Configuration.updateCometsInTempDir(new_time.astroDate);
            int index = OrbitEphem.getIndexOfComet("2015 G2");
            //observer = ObserverElement.parseObservatory(Observatory.findObservatorybyName("Yebes"));
            OrbitalElement new_orbit = OrbitEphem.getOrbitalElementsOfComet(index);

            EphemerisElement another_eph = new EphemerisElement(Target.TARGET.Comet, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_J2000, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2009,
                    EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.ORBIT, new_orbit);
            EphemElement another_ephem = Ephem.getEphemeris(new_time, observer, another_eph, true);
            EphemElement another_ephem2 = OrbitEphem.orbitEphemeris(new_time, observer, another_eph);
            name = new_orbit.name;
            JD = new_astro.jd();
            System.out.println("JD " + JD + " / " + name);
            ConsoleReport.fullEphemReportToConsole(another_ephem);
            ConsoleReport.fullEphemReportToConsole(another_ephem2);

            // Calculate orbit of Pluto
            double pos[] = new double[] { -26.06710, -11.92126, 8.80594 };
            double vel[] = new double[] { 0.001633041, -0.003103617, -0.000152622 };
            double mass = 0.0;
            AstroDate date = new AstroDate(1982, 1, 31);
            double jd = date.jd();
            OrbitalElement PlutoOrbit = OrbitEphem.obtainOrbitalElementsFromPositionAndVelocity(pos, vel, jd, mass);

            System.out.println("Osculating elemens for Pluto in 1982-1-31 obtained from position and velocity vectors:");
            System.out.println("i = " + PlutoOrbit.inclination * Constant.RAD_TO_DEG);
            System.out.println("o = " + PlutoOrbit.ascendingNodeLongitude * Constant.RAD_TO_DEG);
            System.out.println("a = " + PlutoOrbit.semimajorAxis);
            System.out.println("e = " + PlutoOrbit.eccentricity);
            System.out.println("w = " + PlutoOrbit.argumentOfPerihelion * Constant.RAD_TO_DEG);
            System.out.println("M = " + PlutoOrbit.meanAnomaly * Constant.RAD_TO_DEG);
            System.out.println("n = " + PlutoOrbit.meanMotion * Constant.RAD_TO_DEG);

            PlutoOrbit = OrbitEphem.getOrbitalElements(Target.TARGET.Pluto, jd, EphemerisElement.ALGORITHM.MOSHIER);

            System.out.println("Same data obtained from Moshier ephemeris (DE404, i, o, and w refered to J2000):");
            System.out.println("i = " + PlutoOrbit.inclination * Constant.RAD_TO_DEG);
            System.out.println("o = " + PlutoOrbit.ascendingNodeLongitude * Constant.RAD_TO_DEG);
            System.out.println("a = " + PlutoOrbit.semimajorAxis);
            System.out.println("e = " + PlutoOrbit.eccentricity);
            System.out.println("w = " + PlutoOrbit.argumentOfPerihelion * Constant.RAD_TO_DEG);
            System.out.println("M = " + PlutoOrbit.meanAnomaly * Constant.RAD_TO_DEG);
            System.out.println("n = " + PlutoOrbit.meanMotion * Constant.RAD_TO_DEG);


            System.out.println("Orbit determination example:");
            int n = 3;
            TimeElement times[] = new TimeElement[n];
            ObserverElement observers[] = new ObserverElement[n];
            EphemerisElement ephs[] = new EphemerisElement[n];
            LocationElement locations[] = new LocationElement[n];
            CityElement citym = City.findCity("Madrid");
            TimeElement timem = new TimeElement();
            EphemerisElement ephm = new EphemerisElement(
                Target.TARGET.MARS,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.TOPOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.IAU_2009,
                EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000);
            ephm.algorithm = EphemerisElement.ALGORITHM.MOSHIER;

            for (int i = 0; i < n; i++) {
                observers[i] = ObserverElement.parseCity(citym);
                times[i] = timem.clone();
                timem.add(50);
                ephs[i] = ephm;
            }

            for (int i = 0; i < times.length; i++) {
                ephem = Ephem.getEphemeris(times[i], observers[i], ephs[i], false);
                locations[i] = ephem.getEquatorialLocation();
                //System.out.println(ephem.distance);
            }

            OrbitalElement myOrbit = OrbitEphem.solveOrbit(locations, times, observers, ephs);
            System.out.println(myOrbit.toString());
            System.out.println();
            System.out.println("Correct values are:");
            System.out.println();
            JD = TimeScale.getJD(times[0], observers[0], ephs[0], TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            myOrbit = OrbitEphem.getOrbitalElements(ephm.targetBody, JD, EphemerisElement.ALGORITHM.MOSHIER);
            System.out.println(myOrbit.toString());

            JPARSECException.showWarnings();
        } catch (JPARSECException ve) {
            ve.showException();
        }
    }
}
