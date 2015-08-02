package jparsec.observer;

import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.ephem.planets.EphemElement;
import jparsec.ephem.planets.PlanetEphem;
import jparsec.io.ConsoleReport;
import jparsec.math.Constant;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;

public class ExtraterrestrialObserverElementTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("ExtraterrestrialObserverElement test");

        AstroDate astro = new AstroDate(2012, 1, 1, 0, 0, 0);
        TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
        EphemerisElement eph = new EphemerisElement(
            Target.TARGET.JUPITER,
            EphemerisElement.COORDINATES_TYPE.APPARENT,
            EphemerisElement.EQUINOX_OF_DATE,
            EphemerisElement.TOPOCENTRIC,
            EphemerisElement.REDUCTION_METHOD.IAU_2006,
            EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000);

        CityElement city = City.findCity("Madrid");
        ObserverElement observer = ObserverElement.parseCity(city);

        // Move observer towards Jupiter, at 0.1 AU of distance
        double pos[] = Ephem.eclipticToEquatorial(
                PlanetEphem.getHeliocentricEclipticPositionJ2000(TimeScale.getJD(time, observer, eph,
                        TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), Target.TARGET.JUPITER), Constant.J2000, eph);
        LocationElement loc = LocationElement.parseRectangularCoordinates(pos);
        loc.setRadius(loc.getRadius() - 0.1);
        pos = loc.getRectangularCoordinates();
        pos = new double[] { pos[0], pos[1], pos[2], 0, 0, 0 };
        observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("Close to Jupiter", pos));

        EphemElement ephem = Ephem.getEphemeris(time, observer, eph, false);
        ConsoleReport.basicEphemReportToConsole(ephem);


        // Mars, loc: lon = 184.702,  lat = -14.64;
        // Az/El Sun                Horizons             JPARSEC ('simple mode')Mars24
        // 2010-Jan-06 00:00 UT1    284.5225   1.1738    284.5379    1.2618     284.6525    1.1303
        // 2004-Jan-03 13:46:31 UT1 179.9952 -62.0741    180.0387  -62.1659     179.9890  -61.9392
        // (lon=lat=0)2000-Jan-06 00:00 UT1    191.1564 -64.5049    191.1364  -64.5079     191.0398  -64.2616
        astro = new AstroDate(2010, 1, 6, 0, 0, 0);
        //astro = new AstroDate(2004, 1, 3, 13, 46, 31);
        time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
        eph = new EphemerisElement(
            Target.TARGET.SUN,
            EphemerisElement.COORDINATES_TYPE.APPARENT,
            EphemerisElement.EQUINOX_OF_DATE,
            EphemerisElement.TOPOCENTRIC,
            EphemerisElement.REDUCTION_METHOD.IAU_2006,
            EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000);

        observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("Marte", Target.TARGET.MARS,
                new LocationElement(184.702 * Constant.DEG_TO_RAD, -14.64 * Constant.DEG_TO_RAD, 1.0)));

        ephem = Ephem.getEphemeris(time, observer, eph, false);
        ConsoleReport.basicEphemReportToConsole(ephem);

        /*
        // Check TSL Earth
        eph.targetBody = TARGET.EARTH;
        observer.forceObserverOnEarth();
        System.out.println(SiderealTime.apparentSiderealTime(time, observer, eph)*Constant.RAD_TO_DEG);
        double JD_TDB = TimeScale.getJD(time, observer, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
        System.out.println((PhysicalParameters.getBodySiderealTimeAt0Lon(JD_TDB, eph)+observer.getLongitudeRad())*Constant.RAD_TO_DEG);
        */
        /*
        // Check coord. rotation body <-> Earth
        LocationElement loc1 = new LocationElement(184.702 * Constant.DEG_TO_RAD, 89*Constant.DEG_TO_RAD, 1.0);
        LocationElement loc2 = Ephem.getPositionFromBody(loc1, time, observer, eph);
        LocationElement loc3 = Ephem.getPositionFromEarth(loc2, time, observer, eph);
        System.out.println(loc1.toStringAsEquatorialLocation());
        System.out.println(loc2.toStringAsEquatorialLocation());
        System.out.println(loc3.toStringAsEquatorialLocation());
        */
    }
}
