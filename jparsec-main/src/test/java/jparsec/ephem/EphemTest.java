package jparsec.ephem;

import jparsec.astronomy.CoordinateSystem;
import jparsec.ephem.planets.EphemElement;
import jparsec.ephem.planets.PlanetEphem;
import jparsec.ephem.stars.StarEphem;
import jparsec.io.ConsoleReport;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.Configuration;

public class EphemTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Ephem test");
        AstroDate astro = new AstroDate(2013, AstroDate.JULY, 23, 10, 0, 0); // Test unknown sunset time
        TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
        CityElement city = City.findCity("New York"); // Test: 2 locations with the same name...
        // The same with Tenerife and Auckland
        final ObserverElement observer = ObserverElement.parseCity(city);
        EphemerisElement eph = new EphemerisElement(Target.TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2009,
                EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000, EphemerisElement.ALGORITHM.MOSHIER);
        observer.setTimeZone(0.0);
        observer.setLongitudeDeg(-73.56);
        observer.setLatitudeDeg(40.40);
        observer.setDSTCode(ObserverElement.DST_RULE.NONE);
        // 2013/7/23 0:1:54 UT

        EphemElement ephem = Ephem.getEphemeris(time, observer, eph, true);

        System.out.println("jd " + astro.jd());
        System.out.println("Moshier");
        LocationElement loc = CoordinateSystem.equatorialToEcliptic(
                new LocationElement(ephem.rightAscension, ephem.declination, ephem.distance), time, observer, eph);
        System.out.println("Ecl lon " + Functions.formatAngle(loc.getLongitude(), 4));
        System.out.println("Ecl lat " + Functions.formatAngle(loc.getLatitude(), 4));
        ConsoleReport.fullEphemReportToConsole(ephem);

        ephem.setLocation(StarEphem.transform_FK4_B1950_to_FK5_J2000(ephem.getEquatorialLocation()));
        ephem.rightAscension = ephem.getLocation().getLongitude();
        ephem.declination = ephem.getLocation().getLatitude();
        ConsoleReport.fullEphemReportToConsole(ephem);

        ephem.setLocation(StarEphem.transform_FK5_J2000_to_FK4_B1950(ephem.getEquatorialLocation()));
        ephem.rightAscension = ephem.getLocation().getLongitude();
        ephem.declination = ephem.getLocation().getLatitude();
        ConsoleReport.fullEphemReportToConsole(ephem);

        /*
        Sun Rise: 21-dic-2009 8:33:58
        Sun Transit: 21-dic-2009 13:14:15
        Sun Set: 21-dic-2009 17:54:32

        // FK5 coordinates of Sun calculated directly = FK4 transformed to FK5 => fully consistent
        Sun Right ascension: 17h 56m 15.8827s
        Sun Declination: -23 &ordm; 26' 11.944"
        */

        // Testing synchronization
        final EphemerisElement eph2 = new EphemerisElement(
                Target.TARGET.NEPTUNE,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.GEOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.IAU_1976,
                EphemerisElement.FRAME.ICRF,
                EphemerisElement.ALGORITHM.JPL_DE405);
        final EphemerisElement eph3 = new EphemerisElement(
                Target.TARGET.Oberon,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.GEOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.IAU_1976,
                EphemerisElement.FRAME.ICRF,
                EphemerisElement.ALGORITHM.NATURAL_SATELLITE);
        eph = new EphemerisElement(
                Target.TARGET.SUN,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.GEOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.IAU_1976,
                EphemerisElement.FRAME.ICRF,
                EphemerisElement.ALGORITHM.JPL_DE405);
        astro = new AstroDate(1600, AstroDate.JANUARY, 1, 0, 0, 0);
        final TimeElement timeF = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                EphemElement ephem2;
                try {
                    //ephem2 = PlanetEphem.MoshierEphemeris(timeF, observer, eph2);
                    ephem2 = Ephem.getEphemeris(timeF, observer, eph3, false);
                    System.out.println("RA2:  " + Functions.formatRA(ephem2.rightAscension, 5));
                    System.out.println("DEC2: " + Functions.formatDEC(ephem2.declination, 4));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH = "/home/alonso/eclipse/libreria_jparsec/ephem/test";

        // This call blocks MoshierCalc method in PlanetEphem, so that the thread cannot
        // start executing that method until this ends.
        ephem = Ephem.getEphemeris(timeF, observer, eph2, false);
        System.out.println("RA:  " + Functions.formatRA(ephem.rightAscension, 5));
        System.out.println("DEC: " + Functions.formatDEC(ephem.declination, 4));

        // The thread should block again MoshierCalc method in PlanetEphem, but since it calculates
        // the position of a natural satellite (several calls to MoshierCalc involved), the first time
        // MoshierCalc is locked, but then it becomes free for some ms.
        t.start();

        // Unless we put a big sleep here, next call will end before the previous Thread and will
        // lock the calculation of ephemerides of natural satellites. It is OK, the main concern here
        // is that the result of ephemerides should not change. The stability is not guarranted without
        // using synchronized since I use a static planetocentricPositionOfTargetSatellite array to store
        // the relative position of the satellite.
        //Thread.sleep(5000);
        // ... so next lines will be executed later
        //ephem = Ephem.getEphemeris(timeF, observer, eph3, false);
        ephem = PlanetEphem.MoshierEphemeris(timeF, observer, eph);
        //ephem = Vsop.vsopEphemeris(timeF, observer, eph);
        System.out.println("RA:  " + Functions.formatRA(ephem.rightAscension, 5));
        System.out.println("DEC: " + Functions.formatDEC(ephem.declination, 4));

        /*
RA:  10h 00m 31.59382s
DEC: 12 &ordm; 51' 13.3634"
RA2:  01h 41m 49.40292s
DEC2: 10 &ordm; 01' 47.6673"
RA:  18h 43m 27.72859s
DEC: -23 &ordm; 06' 56.9669"
        */

        // For ALMA
        /*
        observer.temperature = 0;
        observer.humidity = 0;
        observer.pressure = 550;
        observer.height = 5500;
        */

        for (double alt_deg = -5; alt_deg <= 90; alt_deg = alt_deg + 1) {
            double alt = alt_deg * Constant.DEG_TO_RAD;

            eph.wavelength = EphemerisElement.OBSERVING_WAVELENGTH.OPTICAL_BENNET;
            double geomElev = Ephem.getGeometricElevation(eph, observer, alt) * Constant.RAD_TO_DEG;
            double appElev = Ephem.getApparentElevation(eph, observer, geomElev * Constant.DEG_TO_RAD, 50) * Constant.RAD_TO_DEG;
            String s = alt_deg + "   ";
            s += geomElev + "   " + appElev + " (Bennet, optical)   ";

            eph.wavelength = EphemerisElement.OBSERVING_WAVELENGTH.OPTICAL_YAN;
            geomElev = Ephem.getGeometricElevation(eph, observer, alt) * Constant.RAD_TO_DEG;
            appElev = Ephem.getApparentElevation(eph, observer, geomElev * Constant.DEG_TO_RAD, 50) * Constant.RAD_TO_DEG;
            s += geomElev + "   " + appElev + " (Yan, optical)   ";

            eph.wavelength = EphemerisElement.OBSERVING_WAVELENGTH.RADIO_BENNET;
            geomElev = Ephem.getGeometricElevation(eph, observer, alt) * Constant.RAD_TO_DEG;
            appElev = Ephem.getApparentElevation(eph, observer, geomElev * Constant.DEG_TO_RAD, 50) * Constant.RAD_TO_DEG;
            s += geomElev + "   " + appElev + " (Bennet, radio)   ";

            eph.wavelength = EphemerisElement.OBSERVING_WAVELENGTH.RADIO_YAN;
            geomElev = Ephem.getGeometricElevation(eph, observer, alt) * Constant.RAD_TO_DEG;
            appElev = Ephem.getApparentElevation(eph, observer, geomElev * Constant.DEG_TO_RAD, 50) * Constant.RAD_TO_DEG;
            s += geomElev + "   " + appElev + " (Yan, radio)   ";

            eph.wavelength = EphemerisElement.OBSERVING_WAVELENGTH.NUMERICAL_INTEGRATION;
            geomElev = Ephem.getGeometricElevation(eph, observer, alt) * Constant.RAD_TO_DEG;
            appElev = Ephem.getApparentElevation(eph, observer, geomElev * Constant.DEG_TO_RAD, 50) * Constant.RAD_TO_DEG;
            s += geomElev + "   " + appElev + " (Num. integration)";

            System.out.println(s);
        }

        astro = new AstroDate(2012, AstroDate.DECEMBER, 3);
        time = new TimeElement(astro, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
        eph.targetBody = Target.TARGET.JUPITER;
        eph.frame = EphemerisElement.FRAME.ICRF;
        eph.isTopocentric = true;
        double radialV = Ephem.getRadialVelocity(time, observer, eph);
        System.out.println("Radial vel. (km/s) of Jupiter at 2012 opposition: " + radialV); // should be close to 0

        for (int i = 0; i < 130; i++) {
            time.add(3);
            radialV = Ephem.getRadialVelocity(time, observer, eph);
            System.out.println("Radial vel. (km/s) of Jupiter 3 days after: " + radialV);
        }

        System.out.println("J2000 <-> apparent test");
        LocationElement in = ephem.getEquatorialLocation();
        LocationElement out = Ephem.fromJ2000ToApparentGeocentricEquatorial(in, time, observer, eph);
        eph.isTopocentric = false;
        LocationElement out2 = Ephem.toMeanEquatorialJ2000(out, time, observer, eph);
        System.out.println(in.toStringAsEquatorialLocation());
        System.out.println(out.toStringAsEquatorialLocation());
        System.out.println(out2.toStringAsEquatorialLocation());
    }
}
