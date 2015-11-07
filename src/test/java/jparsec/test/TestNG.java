package jparsec.test;

import jparsec.astronomy.AtlasChart;
import jparsec.astronomy.Constellation;
import jparsec.astronomy.CoordinateSystem;
import jparsec.astrophysics.MeasureElement;
import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Nutation;
import jparsec.ephem.Obliquity;
import jparsec.ephem.Target;
import jparsec.ephem.event.LunarEclipse;
import jparsec.ephem.event.LunarEvent;
import jparsec.ephem.event.MainEvents;
import jparsec.ephem.event.MoonEvent;
import jparsec.ephem.event.MoonEventElement;
import jparsec.ephem.event.SimpleEventElement;
import jparsec.ephem.event.SolarEclipse;
import jparsec.ephem.moons.GUST86;
import jparsec.ephem.moons.L1;
import jparsec.ephem.moons.Mars07;
import jparsec.ephem.moons.TASS17;
import jparsec.ephem.planets.EphemElement;
import jparsec.ephem.planets.JPLEphemeris;
import jparsec.ephem.stars.DoubleStarElement;
import jparsec.ephem.stars.StarElement;
import jparsec.ephem.stars.StarEphem;
import jparsec.ephem.stars.StarEphemElement;
import jparsec.ephem.stars.VariableStarElement;
import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.io.ReadFile;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.EarthOrientationParameters;
import jparsec.observer.ExtraterrestrialObserverElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.SiderealTime;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;
import jparsec.time.TimeScale;
import jparsec.time.calendar.BaseCalendar;
import jparsec.time.calendar.Coptic;
import jparsec.time.calendar.Ethiopic;
import jparsec.time.calendar.Gregorian;
import jparsec.time.calendar.Hebrew;
import jparsec.time.calendar.HinduSolar;
import jparsec.time.calendar.Islamic;
import jparsec.time.calendar.Julian;
import jparsec.time.calendar.Persian;
import jparsec.util.Configuration;
import jparsec.util.JPARSECException;
import jparsec.vo.SimbadElement;
import jparsec.vo.SimbadQuery;
import org.math.plot.plotObjects.Base;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static jparsec.ephem.Target.TARGET;
import static jparsec.observer.ReferenceEllipsoid.ELLIPSOID;
import static org.testng.Assert.*;

public class TestNG {

    private final EphemerisElement eph;
    private TimeElement time;
    private ObserverElement observer;
    private AstroDate astro;

    private TestNG() {
        astro = new AstroDate(2000, AstroDate.JANUARY, 1, 12, 0, 0);
        time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);
        observer = new ObserverElement("Madrid", Functions.parseDeclination("-03\u00b0 42' 36.000\""),
                Functions.parseDeclination("40\u00b0 25' 12.000\""), 693, 1, ObserverElement.DST_RULE.N1);
        eph = new EphemerisElement(Target.TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                EphemerisElement.FRAME.ICRF);
        eph.correctForEOP = false;
    }

    /*
     ! ***
     ! 0 = Atlas test. Input values are RA (h), DEC (deg), output values are expected charts for
     ! millennium atlas, sky atlas 2000, uranometria, uranometria 2nd editions, and Rukl (lunar
     ! atlas). See (Millenium) http://www.rssd.esa.int/index.php?page=msa&project=HIPPARCOS,
     ! (Sky Atlas 2000) http://www.nightskyinfo.com/sky_atlas_2000/,
     ! (Uranometria 2000) http://www.fortunecity.com/roswell/borley/49/chartidx.htm,
     ! (Uranometria 2nd edition) http://www.willbell.com/u2k/index.htm,
     ! (Rukl) http://the-moon.wikispaces.com/Rukl+Index+Map.
     ! Use - for values that should not be tested.
     */
    @Test
    @DataProvider (name = "data_atlas_chart")
    private Object[][] data_atlas_chart() {
        return new Object[][] {
                { "0.5", "80.0", "VI-14", "1", "VI-3", "V1-2", "Rukl 4" },
                { "0.0", "-22", "-", "18", "VII-305", "-", "Rukl 55" },
                { "0.0", "11", "-", "17", "VII-170", "V1-81", "Rukl 33" },
                { "3.33", "-33", "-", "18", "VII-355", "-", "Rukl 69" },
                { "18.1", "-66", "-", "26", "VII-455", "-", "Rukl 71/VI" },
                { "18.1", "-88", "-", "26", "VII-472", "-", "Rukl 73/VI" },
                { "6.0", "-88", "-", "24", "VII-473", "-", "Rukl 73/V" },
                { "0.7", "41", "VI-105", "-", "-", "V1-30", "-" },
                { "5.6", "-5.5", "VI-278", "-", "-", "-", "-" },
                { "20.8", "30", "VIII-1169", "-", "-", "V1-47", "-" },
                { "8.7", "20", "VII-712", "-", "-", "-", "-" },
        };
    }

    @Test (dataProvider = "data_atlas_chart")
    public void testAtlasChart(
            final String testValue0,
            final String testValue1,
            final String expectedValue0,
            final String expectedValue1,
            final String expectedValue2,
            final String expectedValue3,
            final String expectedValue4)
            throws JPARSECException {
        double ra = DataSet.getDoubleValueWithoutLimit(testValue0) / Constant.RAD_TO_HOUR,
                dec = DataSet.getDoubleValueWithoutLimit(testValue1) * Constant.DEG_TO_RAD;
        LocationElement loc = new LocationElement(ra, dec, 1.0);

        assertUnequalDash(AtlasChart.atlasChart(loc, AtlasChart.ATLAS.MILLENIUM_STAR), expectedValue0);
        assertUnequalDash(AtlasChart.atlasChart(loc, AtlasChart.ATLAS.SKY_ATLAS_2000), expectedValue1);
        assertUnequalDash(AtlasChart.atlasChart(loc, AtlasChart.ATLAS.URANOMETRIA), expectedValue2);
        assertUnequalDash(AtlasChart.atlasChart(loc, AtlasChart.ATLAS.URANOMETRIA_2nd_EDITION), expectedValue3);
        assertUnequalDash(AtlasChart.atlasChart(loc, AtlasChart.ATLAS.RUKL), expectedValue4);
    }

    private void assertUnequalDash(final String actual, final String expected) {
        if (!"-".equals(expected)) {
            assertEquals(actual, expected);
        }
    }

    /*
     ! ***
     ! 1 = Constellation test. Input values are RA (h), DEC (deg). Output value is constellation
     ! name at that equatorial position
     */
    @Test
    @DataProvider (name = "data_constellation")
    private Object[][] data_constellation() {
        return new Object[][] {
                { "5.12", "9.12", "Orion" },
                { "9.0", "65.0", "Ursa Major" },
                { "23.5", "-20", "Aquarius" },
                { "9.46", "-19.9", "Hydra" },
                { "12.89", "22", "Coma Berenices" },
                { "15.67", "-12.12", "Libra" },
                { "19.0", "-40.0", "Corona Australis" },
                { "6.22", "-81.12", "Mensa" },
        };
    }


    @Test (dataProvider = "data_constellation")
    public void testAtlasChart(
            final String testValue0,
            final String testValue1,
            final String expectedValue)
            throws JPARSECException {
        double ra = DataSet.getDoubleValueWithoutLimit(testValue0) / Constant.RAD_TO_HOUR;
        double dec = DataSet.getDoubleValueWithoutLimit(testValue1) * Constant.DEG_TO_RAD;
        String constellation = Constellation.getConstellationName(ra, dec, Constant.J2000, eph);

        assertEquals(constellation, expectedValue);
    }

    /*
     ! ***
     ! 2 = Coordinate systems. Position (lon, lat), from one system (Galactical, Equatorial,
     ! Ecliptical, Horizontal) to another. In case of horizontal assumed time is J2000.0, location
     ! is Madrid, Spain, and reduction algorithms IAU2009. - is used for results that should not
     ! be tested. Transformations are done for J2000 epoch and FK5 frame, and with mean ecliptic.
     */
    @Test
    @DataProvider (name = "data_coordinate_system")
    private Object[][] data_coordinate_system() {
        return new Object[][] {
                { "0", "0.0", "Galactical", "Equatorial", "-93\u00b0 35' 42.014\"", "-28\u00b0 56' 10.221\"" },
                { "0", "90.0", "Galactical", "Equatorial", "192\u00b0 51' 34.132\"", "27\u00b0 7' 41.704\"" },
                { "192.8594812", "27.1282512", "Equatorial", "Galactical", "-", "90\u00b0 0' 0.000\"" },
                { "90", "0", "Equatorial", "Ecliptical", "90\u00b0 0' 0.000\"", "-23\u00b0 26' 21.406\"" },
                { "90", "-23.4392794", "Ecliptical", "Equatorial", "90\u00b0 0' 0.000\"", "0.000\"" },
                { "-90", "0", "Equatorial", "Ecliptical", "-90\u00b0 0' 0.000\"", "23\u00b0 26' 21.406\"" },
                { "-90", "23.4392794", "Ecliptical", "Equatorial", "-90\u00b0 0' 0.000\"", "0.000\"" }
        };
    }

    @Test (dataProvider = "data_coordinate_system")
    public void testCoordinateSystem(
            final String testValue0,
            final String testValue1,
            final String testValue2,
            final String testValue3,
            final String expectedValue0,
            final String expectedValue1)
        throws JPARSECException
    {
        eph.equinox = Constant.J2000;
        eph.ephemType = EphemerisElement.COORDINATES_TYPE.GEOMETRIC;
        eph.frame = EphemerisElement.FRAME.FK5;
        double lon = DataSet.getDoubleValueWithoutLimit(testValue0) * Constant.DEG_TO_RAD;
        double lat = DataSet.getDoubleValueWithoutLimit(testValue1) * Constant.DEG_TO_RAD;
        int s0 = DataSet.getIndex(CoordinateSystem.COORDINATE_SYSTEMS, testValue2);
        int s1 = DataSet.getIndex(CoordinateSystem.COORDINATE_SYSTEMS, testValue3);
        LocationElement out = CoordinateSystem.transform(CoordinateSystem.COORDINATE_SYSTEM.values()[s0], CoordinateSystem.COORDINATE_SYSTEM.values()[s1], new LocationElement(lon, lat, 1.0), time, observer, eph);

        assertUnequalDash(Functions.formatAngle(out.getLongitude(), 3), expectedValue0);
        assertUnequalDash(Functions.formatAngle(out.getLatitude(), 3), expectedValue1);
    }

    /*
     ! ***
     ! 3 = Ephemerides test.
     ! Values are year, month, day, hour, minute (UT), foreground body, background body, elongation (deg).
     ! The tests list occultations as appear in Wikipedia (see http://en.wikipedia.org/wiki/Occultation#Occultation_by_planets).
     ! This tests planetary and stellar ephemerides.
     ! Calculations are done relative to geocenter, considering the object radii and parallaxes con calculate
     ! if there is an occultation from a given point on Earth.
     ! Background body can be a planet or a star, identified by its name or catalog number.
     */
    @Test
    @DataProvider (name = "data_ephemerid")
    private Object[][] data_ephemerid() {
        return new Object[][] {
                { 1802, 12, 9, 7, 36, "Mercury", "Acrab" },
                { 1808, 12, 9, 20, 34, "Mercury", "Saturn" },
                { 1810, 12, 22, 6, 32, "Venus", "18570149" },
                { 1818, 1, 3, 21, 52, "Venus", "Jupiter" },
                { 1825, 7, 11, 9, 10, "Venus", "4220123" },
                { 1841, 5, 9, 19, 35, "Venus", "Electra" },
                { 1850, 12, 16, 11, 28, "Mercury", "lam Sgr" },
                { 1855, 5, 22, 5, 4, "Venus", "eps Gem" },
                { 1857, 6, 30, 0, 25, "Saturn", "del Gem" },
                { 1865, 12, 5, 14, 20, "Mercury", "lam Sgr" },
                { 1876, 2, 28, 5, 13, "Jupiter", "Acrab" },
                { 1881, 6, 7, 20, 54, "Mercury", "eps Gem" },
                { 1906, 12, 9, 17, 40, "Venus", "Acrab" },
                { 1910, 7, 27, 2, 53, "Venus", "eta Gem" },
                // 14500141 = Alpha-2 Lib = Zuben El Genubi, but Alpha-1 Lib has same name and is 3' off !!!
                { 1947, 10, 25, 1, 45, "Venus", "14500141" },
                { 1959, 7, 7, 14, 30, "Venus", "Regulus" },
                { 1965, 9, 27, 15, 30, "Mercury", "eta Vir" },
                { 1971, 5, 13, 20, 0, "Jupiter", "bet Sco" },
                { 1976, 4, 8, 1, 0, "Mars", "eps Gem" },
                // WIKIPEDIA WAS WRONG, IS 15:27, NOT 14:27
                { 1981, 11, 17, 15, 27, "Venus", "Nunki" },
                // WIKIPEDIA SAYS 1:32, but 1:33 eliminates the failure
                { 1984, 11, 19, 1, 33, "Venus", "lam Sgr" },
                { 2035, 2, 17, 15, 19, "Venus", "19090174" },
                { 2044, 10, 1, 22, 0, "Venus", "Regulus" },
                { 2046, 2, 23, 19, 24, "Venus", "19210133" },
                // 14500141 = Alpha-2 Lib = Zuben El Genubi, but Alpha-1 Lib has same name and is 3' off !!!
                { 2052, 11, 10, 7, 20, "Mercury", "14500141" },
                { 2065, 11, 22, 12, 45, "Venus", "Jupiter" },
                // WIKIPEDIA SAYS 11:56, but 11:55 eliminates the failure
                { 2067, 7, 15, 11, 55, "Mercury", "Neptune" },
                { 2078, 10, 3, 22, 0, "Mars", "the oph" },
                { 2079, 8, 11, 1, 30, "Mercury", "Mars" },
                { 2088, 10, 27, 13, 43, "Mercury", "Jupiter" },
                { 2094, 4, 7, 10, 48, "Mercury", "Jupiter" },

                // These ones all fail
                //{ 1837, 7, 11, 12, 50, "Mercury", "eta gem" },// FAILS
                //{ 1843, 9, 27, 18, 0, "Venus", "eta Vir " },// FAILS
                // WIKIPEDIA WAS WRONG HERE, 24-12, NOT 16-12. ELONGATION CORRECT
                //{ 1937, 12, 24, 18, 38, "Mercury", "omi Sgr" },// FAILS
                //{ 1940, 6, 10, 2, 21, "Mercury", "eps Gem" },// FAILS
                // 16:15.5 seems better, but still fails
                //{ 2015, 12, 4, 16, 14, "Mercury", "the oph" },// FAILS
                // 20:22 seems better, but still fails
                //{ 2069, 8, 11, 20, 25, "Venus", "Zavijava" },// FAILS
        };
    }

    @Test (dataProvider = "data_ephemerid")
    public void testEphemerid(
            final int yy,
            final int mm,
            final int dd,
            final int hh,
            final int mi,
            final String foregroundBody,
            final String backgroundBody)
            throws JPARSECException {
        Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH = null;
        eph.isTopocentric = false;

        astro = new AstroDate(yy, mm, dd, hh, mi, 0);
        time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);

        // Test with accurate ephemerides
        final boolean preferPrecision = true, topocentric = false;
        EphemElement foregroundEphem = getPlanetOrStarEphem(time, observer, eph, foregroundBody, preferPrecision, topocentric, false);
        EphemElement backgroundEphem = getPlanetOrStarEphem(time, observer, eph, backgroundBody, preferPrecision, topocentric, false);

        if (foregroundEphem == null) {
            fail("Unknown foreground body " + foregroundBody);
        }

        if (backgroundEphem == null) {
            fail("Unknown background body " + backgroundBody);
        }

        double dist = LocationElement.getAngularDistance(foregroundEphem.getEquatorialLocation(), backgroundEphem.getEquatorialLocation());
        double foregroundParallax = Math.atan2(Constant.EARTH_RADIUS / Constant.AU, foregroundEphem.distance);
        double backgroundParallax = Math.atan2(Constant.EARTH_RADIUS / Constant.AU, backgroundEphem.distance);
        double sizes = foregroundEphem.angularRadius + backgroundEphem.angularRadius + foregroundParallax + backgroundParallax;

        if (dist > sizes) {
            fail("(Accurate) The positions of " + foregroundBody + " and " + backgroundBody + " are separated by " + Functions.formatAngle(dist, 3) + ", which is more than the sum of their sizes and parallaxes (" + Functions.formatAngle(sizes, 3) + ')');
        }

        // Test with less accurate ephemerides
        foregroundEphem = getPlanetOrStarEphem(time, observer, eph, foregroundBody, false, topocentric, false);
        backgroundEphem = getPlanetOrStarEphem(time, observer, eph, backgroundBody, false, topocentric, false);

        if (foregroundEphem == null) {
            fail("Unknown foreground body " + foregroundBody);
        }

        if (backgroundEphem == null) {
            fail("Unknown background body " + backgroundBody);
        }

        dist = LocationElement.getAngularDistance(foregroundEphem.getEquatorialLocation(), backgroundEphem.getEquatorialLocation());
        foregroundParallax = Math.atan2(Constant.EARTH_RADIUS / Constant.AU, foregroundEphem.distance);
        backgroundParallax = Math.atan2(Constant.EARTH_RADIUS / Constant.AU, backgroundEphem.distance);
        sizes = foregroundEphem.angularRadius + backgroundEphem.angularRadius + foregroundParallax + backgroundParallax;

        if (dist > sizes) {
            fail("(Less accurate) The positions of " + foregroundBody + " and " + backgroundBody + " are separated by " + Functions.formatAngle(dist, 3) + ", which is more than the sum of their sizes and parallaxes (" + Functions.formatAngle(sizes, 3) + ')');
        }
    }

    private EphemElement getPlanetOrStarEphem(
            final TimeElement time,
            final ObserverElement observer,
            final EphemerisElement eph,
            final String body,
            final boolean preferPrecision,
            final boolean topocentric,
            final boolean full_ephem) {
        eph.isTopocentric = topocentric;
        eph.correctForEOP = eph.correctForPolarMotion = false;

        try {
            Target.TARGET index = Target.getIDFromEnglishName(body);

            if (index == Target.TARGET.NOT_A_PLANET) {
                throw new JPARSECException("it is a star");
            }

            // Is a planet
            eph.targetBody = index;
            eph.algorithm = EphemerisElement.ALGORITHM.MOSHIER; // Default value, overridden by Ephem.getEphemeris

            if (index.isNaturalSatellite()) {
                eph.algorithm = EphemerisElement.ALGORITHM.NATURAL_SATELLITE;
            }

            return Ephem.getEphemeris(time, observer, eph, full_ephem, preferPrecision);
        } catch (JPARSECException exc) {
            // Is a star
            try {
                int index = StarEphem.getStarTargetIndex(body);

                if (index < 0) {
                    index = StarEphem.getStarTargetIndex(StarEphem.getCatalogNameFromProperName(body));
                }

                if (index < 0) {
                    return null;
                }

                eph.targetBody = Target.TARGET.NOT_A_PLANET;
                eph.targetBody.setIndex(index);
                eph.algorithm = EphemerisElement.ALGORITHM.STAR;

                return Ephem.getEphemeris(time, observer, eph, false, preferPrecision);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /*
     ! ***
     ! 4 = Ephemerides for natural satellites.
     ! See (Jupiter) http://www.aanda.org/index.php?option=com_article&access=bibcode&Itemid=129&bibcode=2006A%2526A...451..733AFUL
     ! and (Saturn) http://www.aanda.org/index.php?option=com_article&access=standard&Itemid=129&url=/articles/aa/full/2001/19/aa1933/aa1933.html
     ! Parameters are year, month, day, hour, minute, second (TT), city, body in front, body behind, type.
     ! No expected values, the body behind should be occulted/eclipsed by the one in front.
     */
    @Test
    @DataProvider (name = "data_ephemerid_satellites")
    private Object[][] data_ephemerid_satellites() {
        return new Object[][] {
                // JUPITER
                { 1997, 4, 24, 3, 56, 21, "Madrid", "Callisto", "Europa", "Total occ" },
                { 1997, 4, 24, 3, 55, 59, "Bordeaux", "Callisto", "Europa", "Total occ" },
                { 1997, 5, 12, 2, 37, 34, "Munich", "Callisto", "Ganymede", "Partial occ" },
                { 1997, 5, 29, 1, 10, 4, "Munich", "Io", "Europa", "Partial ecl" },
                { 1997, 5, 30, 3, 5, 15, "Paris", "Ganymede", "Io", "Total occ" },
                { 1997, 5, 31, 0, 30, 6, "Stuttgart", "Ganymede", "Io", "Partial occ" },
                { 1997, 6, 7, 4, 32, 29, "Tenerife", "Ganymede", "Io", "Partial occ" },
                { 1997, 6, 18, 1, 4, 39, "Lisboa", "Europa", "Io", "Annular ecl" },
                { 1997, 6, 25, 3, 17, 9, "Tenerife", "Europa", "Io", "Annular ecl" },
                { 1997, 6, 30, 5, 39, 48, "New York", "Callisto", "Europa", "Partial ecl" },
                // There are a lot more!
                // SATURN
                { 1995, 8, 13, 22, 14, 42, "Bucharest", "Dione", "Titan", "Partial occ" },
                { 1995, 8, 15, 3, 25, 9, "Bordeaux", "Dione", "Titan", "Annular occ" },
                { 1995, 8, 16, 3, 45, 16, "Bordeaux", "Enceladus", "Mimas", "Total occ" },
                { 1995, 9, 24, 1, 15, 58, "Bordeaux", "Tethys", "Rhea", "Annular ecl" },
                { 1995, 11, 3, 19, 39, 36, "Bordeaux", "Dione", "Tethys", "Partial ecl" },
                { 1995, 11, 5, 18, 53, 3, "Bordeaux", "Rhea", "Tethys", "Partial ecl" }
        };
    }

    @Test (dataProvider = "data_ephemerid_satellites")
    public void testEphemeridSatellites(
            final int yy,
            final int mm,
            final int dd,
            final int hh,
            final int mi,
            final int ss,
            final String cityName,
            final String foregroundBody,
            final String backgroundBody,
            final String type)
            throws JPARSECException {
        astro = new AstroDate(yy, mm, dd, hh, mi, ss);
        time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);

        CityElement city = City.findCity(cityName);
        observer = ObserverElement.parseCity(city);
        boolean isEclipse = type.contains("ecl");

        // Test with accurate ephemerides
        final boolean preferPrecision = true, topocentric = true;
        EphemElement foregroundEphem = getPlanetOrStarEphem(time, observer, eph, foregroundBody, preferPrecision, topocentric, false);
        EphemElement backgroundEphem = getPlanetOrStarEphem(time, observer, eph, backgroundBody, preferPrecision, topocentric, false);

        if (foregroundEphem == null) {
            fail("Unknown foreground body " + foregroundBody);
        }

        if (backgroundEphem == null) {
            fail("Unknown background body " + backgroundBody);
        }

        double dist = LocationElement.getAngularDistance(foregroundEphem.getEquatorialLocation(), backgroundEphem.getEquatorialLocation());
        double sizes = foregroundEphem.angularRadius + backgroundEphem.angularRadius;

        if (!isEclipse) {
            if (dist > sizes) {
                fail("(Accurate) The positions of " + foregroundBody + " and " + backgroundBody + " are separated by " + Functions.formatAngle(dist, 3) + ", which is more than the sum of their sizes (" + Functions.formatAngle(sizes, 3) + ')');
            }

            if (backgroundEphem.status == null || !backgroundEphem.status.toLowerCase().contains("occulted by")) {
                fail("(Accurate) Mutual occultation not reported by MoonEphem class.");
            }
        } else {
            if (backgroundEphem.status == null || !backgroundEphem.status.toLowerCase().contains("eclipsed by")) {
                fail("(Accurate) Mutual eclipse not reported by MoonEphem class.");
            }
        }

        // Test with less accurate ephemerides
        foregroundEphem = getPlanetOrStarEphem(time, observer, eph, foregroundBody, false, topocentric, true);
        backgroundEphem = getPlanetOrStarEphem(time, observer, eph, backgroundBody, false, topocentric, true);

        if (foregroundEphem == null) {
            fail("Unknown foreground body " + foregroundBody);
        }

        if (backgroundEphem == null) {
            fail("Unknown background body " + backgroundBody);
        }

        dist = LocationElement.getAngularDistance(foregroundEphem.getEquatorialLocation(), backgroundEphem.getEquatorialLocation());
        sizes = foregroundEphem.angularRadius + backgroundEphem.angularRadius;

        if (!isEclipse) {
            if (dist > sizes) {
                fail("(Less accurate) The positions of " + foregroundBody + " and " + backgroundBody + " are separated by " + Functions.formatAngle(dist, 3) + ", which is more than the sum of their sizes (" + Functions.formatAngle(sizes, 3) + ')');
            }

            if (backgroundEphem.status == null || !backgroundEphem.status.toLowerCase().contains("occulted by")) {
                fail("(Less accurate) Mutual occultation not reported by MoonEphem class.");
            }
        } else {
            if (backgroundEphem.status == null || !backgroundEphem.status.toLowerCase().contains("eclipsed by")) {
                fail("(Less accurate) Mutual eclipse not reported by MoonEphem class.");
            }
        }
    }

    /*
     ! ***
     ! 5 = Julian days.
     ! Parameters are year, month, day, hour.
     ! Expected value of the Julian day.
     */
    @Test
    @DataProvider (name = "data_julian_date")
    private Object[][] data_julian_date() {
        return new Object[][] {
                { 2000, 1, 1, 12, 2451545.0 },
                { 1987, 6, 19, 12, 2446966.0 },
                { 1900, 1, 1, 0, 2415020.5 },
                { 1600, 12, 31, 0, 2305812.5 },
                { 837, 4, 10, 6, 2026871.75 },
                { -1001, 7, 12, 12, 1356001.0 },
                { -4713, 1, 1, 12, 0.0 },
        };
    }

    @Test (dataProvider = "data_julian_date")
    public void testJulianDate(
            final int yy,
            final int mm,
            final int dd,
            final int hh,
            final double expectedJulianDay)
            throws JPARSECException {
        astro = new AstroDate(yy, mm, dd, hh, 0, 0);
        double actualJulianDate = astro.jd();
        assertEquals(actualJulianDate, expectedJulianDay);
    }

    /*
     ! ***
     ! 6 = Time scales test.
     ! Input is Year, month, day, hour, minute, second, time scale (LT, UT1, UTC, TT, TDB),
     ! and maximum number of ms of error (>= 0, 0 for full precision).
     ! Expected values are the set of Julian days in LT, UT1, UTC, TT, and TDB.
     ! Input date cannot be given as a Julian day.
     ! In practice, for current dates double precision values allows a precision of 0.05 ms or better
     ! when working with a given instant represented as Julian days in different time scales.
     */
    @Test
    @DataProvider (name = "data_time_scale")
    private Object[][] data_time_scale() {
        return new Object[][] {
                //{ new AstroDate(2006, 1, 1, 0, 0, 0), "TT", 0.1 },
                { 2453736.5d, "TT", 0.1 },
                { 2453736.540912231, "LT", 0.1 },
                { 2453736.499249477, "UT1", 0.1207 },
                { 2453736.499245564, "UTC", 0.1207 },
                { 2453736.4999999995, "TDB", 0.1 },
        };
    }

    @Test (dataProvider = "data_time_scale")
    public void testTimeScale(
            final double julianDate,
            final String timeScale,
            final double ms)
            throws JPARSECException {
        eph.correctForEOP = true;
        astro = new AstroDate(julianDate);
        int ts = DataSet.getIndex(TimeElement.TIME_SCALES_ABBREVIATED, timeScale);
        time = new TimeElement(astro, TimeElement.SCALE.values()[ts]);

        double jd_lt = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.LOCAL_TIME);
        double jd_ut1 = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
        double jd_utc = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
        double jd_tt = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.TERRESTRIAL_TIME);
        double jd_tdb = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);

        double[] expectedValues = { 2453736.540912231d, 2453736.499249477d, 2453736.499245564d, 2453736.5d, 2453736.4999999995d };
        double[] foundValues = { jd_lt, jd_ut1, jd_utc, jd_tt, jd_tdb };

        for (int i = 0; i < foundValues.length; i++) {
            double e = expectedValues[i], f = foundValues[i];
            if (ms > 0) {
                double dms = Math.abs(e - f) * Constant.SECONDS_PER_DAY * 1000.0;
                assertTrue(dms <= ms);
            } else {
                assertEquals(f, e);
            }
        }
    }

    /*
     ! ***
     ! 7 = Measure representation and conversion test.
     ! Measure, error, unit, desired output unit.
     ! Expected values are the measure and error for the output unit, and measure representation by rounding errors.
     */
    @Test
    @DataProvider (name = "data_measure")
    private Object[][] data_measure() {
        return new Object[][] {
                { 1.0d, 1.0d, "JOHNSON I", "Jy", 967.4004244449982d, 891.0087185132663d, "1000 +/- 900 Jy" },
                { 968.4004244449982d, 891.0092796748264d, "Jy", "Jy", 968.4004244449982d, 891.0092796748264d, "1000 +/- 900 Jy" },
                { 0.998878256198762d, 0.9989679984628773d, "JOHNSON I", "JOHNSON I", 0.998878256198762d, 0.9989679984628773d, "1.0 +/- 1.0 JOHNSON I" },
                { 1.0d, 1.0, "JOHNSON I", "JOHNSON I", 1.0d, 1.0d, "1.0 +/- 1.0 JOHNSON I" },
                { 968.4004244449982d, 891.0092796748264d, "Jy", "JOHNSON I", 0.998878256198762d, 0.9989679984628772d, "1.0 +/- 1.0 JOHNSON I" },
                { 0.998878256198762d, 0.9989679984628773d, "JOHNSON I", "Jy", 968.4004244449982d, 891.0092796748264d, "1000 +/- 900 Jy" }
        };
    }

    @Test (dataProvider = "data_measure")
    public void testMeasure(
            final double x,
            final double dx,
            final String inputUnit,
            final String outputUnit,
            final double value,
            final double error,
            final String asString)
            throws JPARSECException {
        MeasureElement actual = new MeasureElement(x, dx, inputUnit);
        actual.convert(outputUnit);

        assertEquals(actual.getValue(), value);
        assertEquals(actual.error, error);
        assertEquals(actual.toString(), asString);
    }

    /*
     ! ***
     ! 8 = Tests for calendars.
     ! 3 test dates are applied for each of 8 calendars.
     ! Input are year, month, day, and calendar name (see CalendarGenericConversion class for the names).
     ! Output are the dates for the 8 calendars as yy/mm/dd, in the following order:
     !    Gregorian date
     !    Julian date
     !    Hebrew date
     !    Coptic date
     !    Ethiopic date
     !    Persian (astr)
     !    Islamic (astr)
     !    Hindu solar
     ! Test data comes from the following references:
     !    http://books.google.com/books?id=J90gKVAque4C&printsec=frontcover&hl=es#v=onepage&q&f=false
     !    http://www.cs.tau.ac.il/~nachum/calendar-book/Calendrica.html
     !    http://www.cs.tau.ac.il/~nachum/calendar-book/tables/CUP-2000.pdf
     ! Not all calendars are tested, neither transformations in the far past or future.
     */
    @Test
    @DataProvider (name = "data_calendar")
    private Object[][] data_calendar() {
        return new Object[][] {
                { new Gregorian(2000, 1, 2),
                        new Gregorian(2000, 1, 2),
                        new Julian(1999, 12, 20),
                        new Hebrew(5760, 10, 24),
                        new Coptic(1716, 4, 23),
                        new Ethiopic(1992, 4, 23),
                        new Persian(1378, 10, 12),
                        new Islamic(1420, 9, 25),
                        new HinduSolar(1921, 9, 18),
                },
                { new Gregorian(1901, 12, 29),
                        new Gregorian(1901, 12, 29),
                        new Julian(1901, 12, 16),
                        new Hebrew(5662, 10, 19),
                        new Coptic(1618, 4, 20),
                        new Ethiopic(1894, 4, 20),
                        new Persian(1280, 10, 8),
                        new Islamic(1319, 9, 18),
                        new HinduSolar(1823, 9, 15),
                },
                { new Gregorian(2101, 1, 1),
                        new Gregorian(2101, 1, 1),
                        new Julian(2100, 12, 18),
                        new Hebrew(5861, 9, 30),
                        new Coptic(1817, 4, 22),
                        new Ethiopic(2093, 4, 22),
                        new Persian(1479, 10, 11),
                        new Islamic(1524, 11, 1),
                        new HinduSolar(2022, 9, 16),
                },
                { new Julian(1999, 12, 20),
                        new Gregorian(2000, 1, 2),
                        new Julian(1999, 12, 20),
                        new Hebrew(5760, 10, 24),
                        new Coptic(1716, 4, 23),
                        new Ethiopic(1992, 4, 23),
                        new Persian(1378, 10, 12),
                        new Islamic(1420, 9, 25),
                        new HinduSolar(1921, 9, 18),
                },
                { new Julian(1901, 12, 16),
                        new Gregorian(1901, 12, 29),
                        new Julian(1901, 12, 16),
                        new Hebrew(5662, 10, 19),
                        new Coptic(1618, 4, 20),
                        new Ethiopic(1894, 4, 20),
                        new Persian(1280, 10, 8),
                        new Islamic(1319, 9, 18),
                        new HinduSolar(1823, 9, 15),
                },
                { new Julian(2100, 12, 18),
                        new Gregorian(2101, 1, 1),
                        new Julian(2100, 12, 18),
                        new Hebrew(5861, 9, 30),
                        new Coptic(1817, 4, 22),
                        new Ethiopic(2093, 4, 22),
                        new Persian(1479, 10, 11),
                        new Islamic(1524, 11, 1),
                        new HinduSolar(2022, 9, 16),
                },
                { new Hebrew(5760, 10, 24),
                        new Gregorian(2000, 1, 2),
                        new Julian(1999, 12, 20),
                        new Hebrew(5760, 10, 24),
                        new Coptic(1716, 4, 23),
                        new Ethiopic(1992, 4, 23),
                        new Persian(1378, 10, 12),
                        new Islamic(1420, 9, 25),
                        new HinduSolar(1921, 9, 18),
                },
                { new Hebrew(5662, 10, 19),
                        new Gregorian(1901, 12, 29),
                        new Julian(1901, 12, 16),
                        new Hebrew(5662, 10, 19),
                        new Coptic(1618, 4, 20),
                        new Ethiopic(1894, 4, 20),
                        new Persian(1280, 10, 8),
                        new Islamic(1319, 9, 18),
                        new HinduSolar(1823, 9, 15),
                },
                { new Hebrew(5861, 9, 30),
                        new Gregorian(2101, 1, 1),
                        new Julian(2100, 12, 18),
                        new Hebrew(5861, 9, 30),
                        new Coptic(1817, 4, 22),
                        new Ethiopic(2093, 4, 22),
                        new Persian(1479, 10, 11),
                        new Islamic(1524, 11, 1),
                        new HinduSolar(2022, 9, 16),
                },
                { new Coptic(1716, 4, 23),
                        new Gregorian(2000, 1, 2),
                        new Julian(1999, 12, 20),
                        new Hebrew(5760, 10, 24),
                        new Coptic(1716, 4, 23),
                        new Ethiopic(1992, 4, 23),
                        new Persian(1378, 10, 12),
                        new Islamic(1420, 9, 25),
                        new HinduSolar(1921, 9, 18),
                },
                { new Coptic(1618, 4, 20),
                        new Gregorian(1901, 12, 29),
                        new Julian(1901, 12, 16),
                        new Hebrew(5662, 10, 19),
                        new Coptic(1618, 4, 20),
                        new Ethiopic(1894, 4, 20),
                        new Persian(1280, 10, 8),
                        new Islamic(1319, 9, 18),
                        new HinduSolar(1823, 9, 15),
                },
                { new Coptic(1817, 4, 22),
                        new Gregorian(2101, 1, 1),
                        new Julian(2100, 12, 18),
                        new Hebrew(5861, 9, 30),
                        new Coptic(1817, 4, 22),
                        new Ethiopic(2093, 4, 22),
                        new Persian(1479, 10, 11),
                        new Islamic(1524, 11, 1),
                        new HinduSolar(2022, 9, 16),
                },
                { new Ethiopic(1992, 4, 23),
                        new Gregorian(2000, 1, 2),
                        new Julian(1999, 12, 20),
                        new Hebrew(5760, 10, 24),
                        new Coptic(1716, 4, 23),
                        new Ethiopic(1992, 4, 23),
                        new Persian(1378, 10, 12),
                        new Islamic(1420, 9, 25),
                        new HinduSolar(1921, 9, 18),
                },
                { new Ethiopic(1894, 4, 20),
                        new Gregorian(1901, 12, 29),
                        new Julian(1901, 12, 16),
                        new Hebrew(5662, 10, 19),
                        new Coptic(1618, 4, 20),
                        new Ethiopic(1894, 4, 20),
                        new Persian(1280, 10, 8),
                        new Islamic(1319, 9, 18),
                        new HinduSolar(1823, 9, 15),
                },
                { new Ethiopic(2093, 4, 22),
                        new Gregorian(2101, 1, 1),
                        new Julian(2100, 12, 18),
                        new Hebrew(5861, 9, 30),
                        new Coptic(1817, 4, 22),
                        new Ethiopic(2093, 4, 22),
                        new Persian(1479, 10, 11),
                        new Islamic(1524, 11, 1),
                        new HinduSolar(2022, 9, 16),
                },
                { new Persian(1378, 10, 12),
                        new Gregorian(2000, 1, 2),
                        new Julian(1999, 12, 20),
                        new Hebrew(5760, 10, 24),
                        new Coptic(1716, 4, 23),
                        new Ethiopic(1992, 4, 23),
                        new Persian(1378, 10, 12),
                        new Islamic(1420, 9, 25),
                        new HinduSolar(1921, 9, 18),
                },
                { new Persian(1280, 10, 8),
                        new Gregorian(1901, 12, 29),
                        new Julian(1901, 12, 16),
                        new Hebrew(5662, 10, 19),
                        new Coptic(1618, 4, 20),
                        new Ethiopic(1894, 4, 20),
                        new Persian(1280, 10, 8),
                        new Islamic(1319, 9, 18),
                        new HinduSolar(1823, 9, 15),
                },
                { new Persian(1479, 10, 11),
                        new Gregorian(2101, 1, 1),
                        new Julian(2100, 12, 18),
                        new Hebrew(5861, 9, 30),
                        new Coptic(1817, 4, 22),
                        new Ethiopic(2093, 4, 22),
                        new Persian(1479, 10, 11),
                        new Islamic(1524, 11, 1),
                        new HinduSolar(2022, 9, 16),
                },
                { new Islamic(1420, 9, 25),
                        new Gregorian(2000, 1, 2),
                        new Julian(1999, 12, 20),
                        new Hebrew(5760, 10, 24),
                        new Coptic(1716, 4, 23),
                        new Ethiopic(1992, 4, 23),
                        new Persian(1378, 10, 12),
                        new Islamic(1420, 9, 25),
                        new HinduSolar(1921, 9, 18),
                },
                { new Islamic(1319, 9, 18),
                        new Gregorian(1901, 12, 29),
                        new Julian(1901, 12, 16),
                        new Hebrew(5662, 10, 19),
                        new Coptic(1618, 4, 20),
                        new Ethiopic(1894, 4, 20),
                        new Persian(1280, 10, 8),
                        new Islamic(1319, 9, 18),
                        new HinduSolar(1823, 9, 15),
                },
                { new Islamic(1524, 11, 1),
                        new Gregorian(2101, 1, 1),
                        new Julian(2100, 12, 18),
                        new Hebrew(5861, 9, 30),
                        new Coptic(1817, 4, 22),
                        new Ethiopic(2093, 4, 22),
                        new Persian(1479, 10, 11),
                        new Islamic(1524, 11, 1),
                        new HinduSolar(2022, 9, 16),
                },
                { new HinduSolar(1921, 9, 18),
                        new Gregorian(2000, 1, 2),
                        new Julian(1999, 12, 20),
                        new Hebrew(5760, 10, 24),
                        new Coptic(1716, 4, 23),
                        new Ethiopic(1992, 4, 23),
                        new Persian(1378, 10, 12),
                        new Islamic(1420, 9, 25),
                        new HinduSolar(1921, 9, 18),
                },
                { new HinduSolar(1823, 9, 15),
                        new Gregorian(1901, 12, 29),
                        new Julian(1901, 12, 16),
                        new Hebrew(5662, 10, 19),
                        new Coptic(1618, 4, 20),
                        new Ethiopic(1894, 4, 20),
                        new Persian(1280, 10, 8),
                        new Islamic(1319, 9, 18),
                        new HinduSolar(1823, 9, 15),
                },
                { new HinduSolar(2022, 9, 16),
                        new Gregorian(2101, 1, 1),
                        new Julian(2100, 12, 18),
                        new Hebrew(5861, 9, 30),
                        new Coptic(1817, 4, 22),
                        new Ethiopic(2093, 4, 22),
                        new Persian(1479, 10, 11),
                        new Islamic(1524, 11, 1),
                        new HinduSolar(2022, 9, 16),
                },
        };
    }

    @Test (dataProvider = "data_calendar")
    public void testCalendar(
            final BaseCalendar base,
            final BaseCalendar gregorian,
            final BaseCalendar julian,
            final BaseCalendar hebrew,
            final BaseCalendar coptic,
            final BaseCalendar ethiopic,
            final BaseCalendar persian,
            final BaseCalendar islamic,
            final BaseCalendar hinduSolar)
    {
        assertEquals(gregorian.getJulianDate(), base.getJulianDate());
        assertEquals(julian.getJulianDate(), base.getJulianDate());
        assertEquals(hebrew.getJulianDate(), base.getJulianDate());
        assertEquals(coptic.getJulianDate(), base.getJulianDate());
        assertEquals(ethiopic.getJulianDate(), base.getJulianDate());
        assertEquals(persian.getJulianDate(), base.getJulianDate());
        assertEquals(islamic.getJulianDate(), base.getJulianDate());
        assertEquals(hinduSolar.getJulianDate(), base.getJulianDate());
    }

    /*
     ! ***
     ! 9 = Geodetic to geocentric latitudes.
     ! Input is longitude (irrelevant), latitude, height above sea level in m,
     ! coordinates type (geodetic or geocentric), and reference ellipsoid (WGS84 or IERS2003).
     ! Output is longitude, latitude for the other coordinate type, and elevation.
     ! Test cases taken from
     !     http://www.mathworks.es/help/toolbox/aerotbx/ug/geoc2geod.html
     */
    @Test
    @DataProvider (name = "data_geodetic")
    private Object[][] data_geodetic() {
        return new Object[][] {
                { 0.0d, 1000, "geodetic", ELLIPSOID.WGS84, 0.0d, 0.0d },
                { 45.0, 1000, "geodetic", ELLIPSOID.WGS84, 0.0d, 44.8076d },
                { 45.1921d, 1000, "geodetic", ELLIPSOID.WGS84, 0.0d, 44.9997d },
                { 90.0d, 1000, "geodetic", ELLIPSOID.WGS84, 0.0d, 90.0d },
                { 0.0d, 1000, "geocentric", ELLIPSOID.WGS84, 0.0d, 0.0d },
                { 44.8076d, 1000, "geocentric", ELLIPSOID.WGS84, 0.0d, 44.9997d },
                { 45.0d, 1000, "geocentric", ELLIPSOID.WGS84, 0.0d, 45.19207d },
                { 90.0d, 1000, "geocentric", ELLIPSOID.WGS84, 0.0d, 90.0d },
        };
    }

    @Test (dataProvider = "data_geodetic")
    public void testGeodetic(
            final double latitude,
            final int elevation,
            final String type,
            final ELLIPSOID elipsoid,
            final double expectedLongitude,
            final double expectedLatitude)
            throws JPARSECException {
        ELLIPSOID re = elipsoid;
        LocationElement location = null;

        if ("geodetic".equals(type)) {
            location = ObserverElement.geodeticToGeocentric(re, 0.0d, latitude * Constant.DEG_TO_RAD, elevation);
        } else if ("geocentric".equals(type)) {
            location = ObserverElement.geocentricToGeodetic(re, 0.0d, latitude * Constant.DEG_TO_RAD, 1.0d + elevation / (1000.0d * re.getEquatorialRadius()));
        } else {
            fail("Unknown coordinate type " + type);
        }

        assertEquals(location.getLongitude(), expectedLongitude);
        double difference = Math.abs(expectedLatitude - location.getLatitude() * Constant.RAD_TO_DEG);
        assertTrue(difference < 0.0001);
    }

    /*
     ! ***
     ! 10 = General tests of ephemerides for different theories, bodies, coordinates types (apparent, astrometric J2000,
     ! geometric). Input parameters are year, month, day (TT), body, maximum allowed difference in angles ("),
     ! and maximum difference in the distances (AU). Ephemerides tested for the methods by Moshier, VSOP87/ELP2000,
     ! Series96/Moshier, JPL DE200, 403, 405 (taken as reference to calculate differences), 413, 414. For VSOP87 and
     ! Pluto as target the calculations cannot be done (they are skipped). Tested fields are almost all in
     ! EphemElement class. Output expected values given here are the RA and DEC for apparent, astrometric, and
     ! geometric coordinates for the algorithm taken as reference (JPL DE405). These values are obtained using
     ! JPL Horizons for apparent and astrometric positions, and IMCCE for geometric ones. The rest of the expected
     ! values (if present) are the planetographic (geodetic) longitude and latitude, the subsolar longitude and
     ! latitude, the heliocentric apparent distance (not true/geometric one), apparent distance to the observer,
     ! apparent solar elongation, and almost-apparent phase angle (see Horizons documentation, expected
     ! difference < 20" with JPARSEC). Latest value is the constellation abbreviation. Tolerable errors for these
     ! parameters (that are compared directly with JPARSEC) are set automatically: 1.0E-5 AU for distances, 0.015\u00b0
     ! for elongation and phase angle, and 0.05\u00b0 for the rest. The exception is the planetogeodetic longitudes,
     ! where the error is 1 deg since JPL is probably giving wrong results in dates far from J2000. In addition,
     ! JPL and IMCCE implements different algorithms so here positions are tested to a precission of 0.1". In
     ! a later test positions are tested with more accuracy. Precision in elongation and phase angle is 0.4"
     ! since I only have 4 decimal places for those parameters from Horizons. In distance accuracy compared
     ! with Horizons and DE405 is excellent (all digits matches almost always), but here it is reduced to values
     ! around 1.0E-5 since other theories are not so accurate. The only exception is the distance to the Moon,
     ! which needs further investigation since Horizons GIVES DIFFERENT VALUES depending on the Ephemeris type.
     ! For example, in year 2000 the distance in OBSERVER mode is 0.00267983322055 AU, and in VECTOR mode is
     ! 0.002680056593 AU. The value obtained with JPARSEC is the later one (after correcting for light-time the
     ! value from Horizons, both are identical).
     */
    @Test
    @DataProvider (name = "data_detailed_ephemerides")
    private Object[][] data_detailed_ephemerides() {
        return new Object[][] {
            //{ new AstroDate (1950, 1, 1), TARGET.SUN, 0.075, 1.0E-7, "18h 43m 32.24s", "-23\u00b0 04' 14.8\"", "18h 46m 35.54s", "-23\u00b0 00' 48.9\"", "18h 43m 33.94s", "-23\u00b0 04' 04.8\"", "" },
            //{ new AstroDate (2000, 1, 1), TARGET.SUN, 0.275, 1.0E-7, "18h 42m 54.06s", "-23\u00b0 04' 16.2\"", "18h 42m 56.60s", "-23\u00b0 04' 19.3\"", "18h 42m 56.60s", "-23\u00b0 04' 19.3\"", "" },
            //{ new AstroDate (2049, 1, 1), TARGET.SUN, 0.075, 1.0E-7, "18h 47m 48.66s", "-22\u00b0 58' 39.0\"", "18h 44m 51.02s", "-23\u00b0 01' 58.4\"", "18h 47m 48.95s", "-22\u00b0 58' 41.2\"", "" },
            { new AstroDate(1950, 1, 1), TARGET.Moon, 0.5, 1.0E-7, "03h 53m 47.38s", "24\u00b0 09' 05.0\"", "03h 56m 46.04s", "24\u00b0 17' 33.3\"", "03h 53m 47.79s", "24\u00b0 08' 58.8\"", 356.94706, -4.96237, 35.52562, -1.57515, 0.985328111433, 0.00267134176532, 141.2508, 38.6518, "Tau" },
            { new AstroDate(2000, 1, 1), TARGET.Moon, 0.5, 1.0E-7, "14h 26m 38.63s", "-08\u00b0 59' 29.4\"", "14h 26m 40.12s", "-08\u00b0 59' 38.8\"", "14h 26m 39.51s", "-08\u00b0 59' 37.5\"", 5.56630, -6.79806, 248.27509, 0.63139, 0.982105696677, 0.00267981683743, 62.6979, 117.1631, "Lib" },
            { new AstroDate(2049, 1, 1), TARGET.Moon, 0.7, 1.0E-7, "15h 39m 19.06s", "-21\u00b0 26' 35.0\"", "15h 36m 27.34s", "-21\u00b0 17' 06.4\"", "15h 39m 17.88s", "-21\u00b0 26' 33.3\"", 354.74514, 2.41513, 218.21106, 0.59969, 0.981578201379, 0.00243629518339, 43.4658, 136.4362, "Lib" },
            { new AstroDate(1950, 1, 1), TARGET.MERCURY, 0.4, 1.0E-5, "20h 07m 31.46s", "-21\u00b0 28' 17.0\"", "20h 10m 28.81s", "-21\u00b0 19' 10.1\"", "20h 07m 33.09s", "-21\u00b0 28' 01.1\"", 75.16, -5.46, 358.29, -0.01, 0.336574125084, 1.00299133872360, 19.4794, 76.9406, "Cap" },
            { new AstroDate(2000, 1, 1), TARGET.MERCURY, 0.4, 1.0E-5, "18h 04m 52.62s", "-24\u00b0 22' 42.9\"", "18h 04m 55.15s", "-24\u00b0 22' 48.5\"", "18h 04m 56.98s", "-24\u00b0 22' 51.1\"", 245.55, -3.84, 264.00, -0.02, 0.466254982664, 1.41308874562055, 8.7984, 18.8213, "Sgr" },
            { new AstroDate(2049, 1, 1), TARGET.MERCURY, 0.4, 1.0E-5, "20h 05m 25.93s", "-20\u00b0 32' 49.0\"", "20h 02m 34.34s", "-20\u00b0 41' 16.3\"", "20h 05m 25.13s", "-20\u00b0 32' 48.8\"", 285.11, -6.39, 180.38, 0.01, 0.316967021309, 0.85415090116384, 18.1716, 104.6423, "Sgr" },
            { new AstroDate(1950, 1, 1), TARGET.VENUS, 0.4, 1.0E-5, "21h 16m 56.34s", "-15\u00b0 09' 04.7\"", "21h 19m 42.91s", "-14\u00b0 56' 10.3\"", "21h 16m 56.59s", "-15\u00b0 08' 56.0\"", 281.43, -1.00, 46.24, -1.09, 0.720099685276, 0.37468540492782, 36.9795, 124.7806, "Cap" },
            { new AstroDate(2000, 1, 1), TARGET.VENUS, 0.4, 1.0E-5, "15h 57m 05.14s", "-18\u00b0 18' 48.3\"", "15h 57m 07.15s", "-18\u00b0 18' 58.9\"", "15h 57m 07.98s", "-18\u00b0 19' 02.5\"", 257.46, -1.02, 198.32, -2.19, 0.720159644184, 1.13432878289948, 38.9447, 59.1280, "Lib" },
            { new AstroDate(2049, 1, 1), TARGET.VENUS, 0.4, 1.0E-5, "22h 03m 13.98s", "-13\u00b0 17' 18.9\"", "22h 00m 35.43s", "-13\u00b0 31' 35.3\"", "22h 03m 13.85s", "-13\u00b0 17' 19.8\"", 217.58, 0.70, 305.43, 0.11, 0.722151722747, 0.69495943163265, 47.2137, 87.8559, "Aqr" },
            { new AstroDate(1950, 1, 1), TARGET.MARS, 0.4, 1.0E-5, "12h 12m 06.67s", "01\u00b0 25' 32.3\"", "12h 14m 40.35s", "01\u00b0 08' 52.9\"", "12h 12m 07.44s", "01\u00b0 25' 28.2\"", 47.61, 24.03, 86.60, 22.28, 1.663835303003, 1.21552054413961, 97.7859, 35.8428, "Vir" },
            { new AstroDate(2000, 1, 1), TARGET.MARS, 0.4, 1.0E-5, "22h 00m 34.72s", "-13\u00b0 19' 21.3\"", "22h 00m 36.59s", "-13\u00b0 19' 14.2\"", "22h 00m 37.60s", "-13\u00b0 19' 08.6\"", 254.25, -23.17, 219.72, -25.39, 1.390945245952, 1.84697285467017, 47.7256, 31.5380, "Aqr" },
            { new AstroDate(2049, 1, 1), TARGET.MARS, 0.4, 1.0E-5, "23h 02m 28.92s", "-06\u00b0 59' 23.0\"", "22h 59m 55.68s", "-07\u00b0 15' 16.3\"", "23h 02m 29.36s", "-06\u00b0 59' 20.4\"", 302.38, -25.82, 260.55, -22.61, 1.417423762245, 1.55911965850753, 63.0732, 38.2062, "Aqr" },
            { new AstroDate(1950, 1, 1), TARGET.JUPITER, 0.5, 1.0E-5, "20h 36m 11.60s", "-19\u00b0 13' 08.9\"", "20h 39m 04.61s", "-19\u00b0 02' 20.5\"", "20h 36m 13.61s", "-19\u00b0 12' 54.7\"", 270.71, -0.62, 265.76, -0.31, 5.074481869144, 5.93536484491519, 26.5067, 4.9635, "Cap" }, //"[OK", L0 = 271.36 = value given by IMCCE in the graphical visualization]
            { new AstroDate(2000, 1, 1), TARGET.JUPITER, 0.5, 1.0E-5, "01h 35m 24.00s", "08\u00b0 35' 04.5\"", "01h 35m 24.47s", "08\u00b0 35' 10.3\"", "01h 35m 25.04s", "08\u00b0 35' 13.7\"", 339.54, 3.31, 328.52, 3.50, 4.965312354289, 4.61341107349985, 105.3708, 11.0049, "Psc" },
            { new AstroDate(2049, 1, 1), TARGET.JUPITER, 0.5, 1.0E-5, "05h 33m 55.87s", "22\u00b0 54' 55.8\"", "05h 30m 55.03s", "22\u00b0 52' 58.7\"", "05h 33m 53.84s", "22\u00b0 54' 57.3\"", 348.49, 2.88, 345.26, 2.76, 5.110803759687, 4.16236852715959, 163.0027, 3.2183, "Tau" },
            { new AstroDate(1950, 1, 1), TARGET.SATURN, 0.4, 1.0E-5, "11h 24m 20.02s", "06\u00b0 01' 40.8\"", "11h 26m 54.48s", "05\u00b0 45' 10.8\"", "11h 24m 20.15s", "06\u00b0 01' 39.3\"", 171.86, -1.83, 176.89, -5.00, 9.353240604830, 8.96249258574609, 110.5539, 5.6525, "Leo" },
            { new AstroDate(2000, 1, 1), TARGET.SATURN, 0.4, 1.0E-5, "02h 35m 06.26s", "12\u00b0 36' 56.5\"", "02h 35m 06.40s", "12\u00b0 37' 01.9\"", "02h 35m 06.84s", "12\u00b0 37' 04.1\"", 224.89, -23.03, 219.56, -25.01, 9.183967605493, 8.64561332170131, 120.5166, 5.2881, "Ari" },
            { new AstroDate(2049, 1, 1), TARGET.SATURN, 0.4, 1.0E-5, "19h 15m 43.46s", "-22\u00b0 03' 21.2\"", "19h 12m 47.84s", "-22\u00b0 08' 35.8\"", "19h 15m 44.18s", "-22\u00b0 03' 22.6\"", 311.03, 29.22, 310.36, 29.02, 10.024234935431, 11.0006165382543, 6.5112, 0.6414, "Sgr" },
            { new AstroDate(1950, 1, 1), TARGET.URANUS, 0.6, 1.0E-5, "06h 11m 43.14s", "23\u00b0 41' 23.0\"", "06h 14m 44.84s", "23\u00b0 40' 17.2\"", "06h 11m 42.21s", "23\u00b0 41' 15.0\"", 276.61, 73.15, 277.21, 72.83, 18.945469111788, 17.9698631366579, 172.6733, 0.3728, "Gem" },
            { new AstroDate(2000, 1, 1), TARGET.URANUS, 0.4, 3.0E-5, "21h 09m 47.94s", "-17\u00b0 01' 40.1\"", "21h 09m 50.09s", "-17\u00b0 01' 34.7\"", "21h 09m 50.39s", "-17\u00b0 01' 33.5\"", 14.84, -33.63, 15.13, -31.99, 19.923961518998, 20.7221895282437, 34.9310, 1.6228, "Cap" },
            { new AstroDate(2049, 1, 1), TARGET.URANUS, 1.5, 6.0E-5, "11h 09m 19.36s", "06\u00b0 17' 19.8\"", "11h 06m 45.42s", "06\u00b0 33' 27.5\"", "11h 09m 18.08s", "06\u00b0 17' 28.7\"", 211.11, 2.42, 210.75, 5.32, 18.289577172020, 17.8507922858825, 115.0892, 2.7946, "Leo" },
            { new AstroDate(1950, 1, 1), TARGET.NEPTUNE, 0.4, 2.0E-5, "13h 06m 06.48s", "-05\u00b0 18' 42.7\"", "13h 08m 42.48s", "-05\u00b0 34' 41.2\"", "13h 06m 07.14s", "-05\u00b0 18' 43.4\"", 269.84, 12.88, 271.56, 13.67, 30.295277061353, 30.4037158351984, 82.7413, 1.8453, "Vir" },
            { new AstroDate(2000, 1, 1), TARGET.NEPTUNE, 0.9, 7.0E-5, "20h 21m 39.48s", "-19\u00b0 13' 02.0\"", "20h 21m 41.83s", "-19\u00b0 12' 59.1\"", "20h 21m 42.09s", "-19\u00b0 12' 58.3\"", 152.46, -28.40, 151.64, -28.51, 30.120616388034, 31.0211175747855, 23.3179, 0.7447, "Cap" },
            // It seems here that the difference in the position of Neptune between DE200 and DE405 is almost 3" !
            { new AstroDate(2049, 1, 1), TARGET.NEPTUNE, 3.0, 11.0E-5, "03h 17m 28.13s", "16\u00b0 22' 08.3\"", "03h 14m 40.90s", "16\u00b0 11' 18.7\"", "03h 17m 26.29s", "16\u00b0 22' 03.8\"", 297.15, 2.31, 295.87, 2.99, 29.816680398689, 29.1714260682633, 130.2941, 1.4368, "Ari" },
            { new AstroDate(1950, 1, 1), TARGET.Pluto, 1, 0.0002, "09h 31m 51.78s", "23\u00b0 17' 35.9\"", "09h 34m 41.54s", "23\u00b0 04' 13.1\"", "09h 31m 50.98s", "23\u00b0 17' 36.7\"", 319.86, 58.96, 318.02, 59.15, 36.325399841190, 35.5510968781456, 141.4566, 0.9715, "Leo" },
            { new AstroDate(2000, 1, 1), TARGET.Pluto, 4.0, 0.0006, "16h 45m 36.38s", "-11\u00b0 23' 33.9\"", "16h 45m 38.53s", "-11\u00b0 23' 41.7\"", "16h 45m 38.79s", "-11\u00b0 23' 43.1\"", 100.36, -26.27, 99.69, -25.54, 30.223031944329, 31.0683011612493, 30.2612, 0.9360, "Oph" },
            // The tolerable error increased to 20" since there's a deviation from JPL DE200 to DE405 in the direction of the north pole of rotation
            { new AstroDate(2049, 1, 1), TARGET.Pluto, 20.0, 0.0011, "22h 51m 31.39s", "-20\u00b0 43' 33.4\"", "22h 48m 52.86s", "-20\u00b0 59' 09.4\"", "22h 51m 31.24s", "-20\u00b0 43' 32.0\"", 273.14, -50.50, 274.57, -49.83, 41.201795360105, 41.7410133883077, 56.1821, 1.1384, "Aqr" },
            // Natural satellites
            { new AstroDate(1950, 1, 1), TARGET.Phobos, 0.2, 0.00001, "12h 12m 06.22s", "01\u00b0 25' 38.5\"", "12h 14m 39.89s", "01\u00b0 08' 59.0\"", "12h 12m 07.05s", "01\u00b0 25' 34.3\"", 302.81, 41.72, 341.49, 39.14, 1.663890856745, 1.21555214644787, 97.7883, 35.8416, "Vir" },
            { new AstroDate(1950, 1, 1), TARGET.Io, 0.3, 0.00001, "20h 36m 16.93s", "-19\u00b0 12' 46.1\"", "20h 39m 09.92s", "-19\u00b0 01' 57.4\"", "20h 36m 18.41s", "-19\u00b0 12' 34.0\"", 127.40, -0.53, 122.44, -0.27, 5.072992182817, 5.93367287025952, 26.5286, 4.9705, "Cap" },
            { new AstroDate(1950, 1, 1), TARGET.Titan, 0.4, 0.00001, "11h 24m 12.63s", "06\u00b0 01' 35.9\"", "11h 26m 47.10s", "05\u00b0 45' 05.9\"", "11h 24m 12.54s", "06\u00b0 01' 34.0\"", 210.89, -1.49, 215.94, -4.04, 9.347150486647, 8.95594290443931, 110.5817, 5.6544, "Leo" },
            { new AstroDate(1950, 1, 1), TARGET.Oberon, 0.6, 0.00001, "06h 11m 40.01s", "23\u00b0 41' 28.0\"", "06h 14m 41.71s", "23\u00b0 40' 22.5\"", "06h 11m 39.07s", "23\u00b0 41' 17.9\"", 148.96, 72.60, 149.57, 72.27, 18.944444921185, 17.9688638943493, 172.6613, 0.3734, "Gem" },
            { new AstroDate(1950, 1, 1), TARGET.Triton, 0.4, 0.0001, "13h 06m 06.82s", "-05\u00b0 18' 50.5\"", "13h 08m 42.82s", "-05\u00b0 34' 49.0\"", "13h 06m 07.65s", "-05\u00b0 18' 51.7\"", 220.45, 2.63, 219.15, 3.94, 30.293372824130, 30.4018415829990, 82.7395, 1.8459, "Vir" }
        };
    }

    @Test (dataProvider = "data_detailed_ephemerides")
    public void testDetailedEphemerides(
            final AstroDate date,
            final TARGET body,
            final double precision1,
            final double precision2,
            final String raApparent,
            final String decApparent,
            final String raAstrometric,
            final String decAstrometric,
            final String raGeometric,
            final String decGeometric,
            final double meridianLongitude,
            final double poleAngle,
            final double subsolarLongitude,
            final double subsolarLatitude,
            final double distanceFromSun,
            final double distance,
            final double elongation,
            final double phaseAngle,
            final String constellationName)
        throws JPARSECException
    {
        Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH = null;
        eph.correctForPolarMotion = false;

        astro = date;
        time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);

        final EphemerisElement.COORDINATES_TYPE ephem[] = {
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.COORDINATES_TYPE.ASTROMETRIC,
                EphemerisElement.COORDINATES_TYPE.GEOMETRIC
        };

        final EphemerisElement.ALGORITHM algor[] = body.isNaturalSatellite() ?
                new EphemerisElement.ALGORITHM[] {
                        EphemerisElement.ALGORITHM.NATURAL_SATELLITE
                } :
                new EphemerisElement.ALGORITHM[] {
                        EphemerisElement.ALGORITHM.JPL_DE405, EphemerisElement.ALGORITHM.JPL_DE200,
                        EphemerisElement.ALGORITHM.JPL_DE403, EphemerisElement.ALGORITHM.JPL_DE413,
                        EphemerisElement.ALGORITHM.JPL_DE414, EphemerisElement.ALGORITHM.MOSHIER,
                        EphemerisElement.ALGORITHM.VSOP87_ELP2000ForMoon,
                        EphemerisElement.ALGORITHM.SERIES96_MOSHIERForMoon
                };

        eph.targetBody = body;
        eph.isTopocentric = false;
        eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_1976;
        String[] foundValues = new String[15];
        double[] expectedValues = { meridianLongitude, poleAngle, subsolarLongitude, subsolarLatitude, distanceFromSun, distance, elongation, phaseAngle };

        for (int i = 0; i < ephem.length; i++) {
            eph.ephemType = ephem[i];
            EphemElement ephems[] = new EphemElement[algor.length];
            eph.equinox = EphemerisElement.EQUINOX_OF_DATE;

            if (eph.ephemType == EphemerisElement.COORDINATES_TYPE.ASTROMETRIC) {
                eph.equinox = EphemerisElement.EQUINOX_J2000;
            }

            for (int j = 0; j < algor.length; j++) {
                if (algor[j] == EphemerisElement.ALGORITHM.VSOP87_ELP2000ForMoon || eph.targetBody == Target.TARGET.Pluto) {
                    continue;
                }

                eph.algorithm = algor[j];
                ephems[j] = Ephem.getEphemeris(time, observer, eph, false);
                foundValues[i * 2] = Functions.formatRA(ephems[j].rightAscension, 2);
                foundValues[i * 2 + 1] = Functions.formatDEC(ephems[j].declination, 1);

                if (eph.ephemType == EphemerisElement.COORDINATES_TYPE.APPARENT) {
                    foundValues[6] = Functions.formatAngleAsDegrees(ephems[j].longitudeOfCentralMeridian, 5);
                    foundValues[7] = Functions.formatAngleAsDegrees(ephems[j].positionAngleOfPole, 5);
                    foundValues[8] = Functions.formatAngleAsDegrees(ephems[j].subsolarLongitude, 5);
                    foundValues[9] = Functions.formatAngleAsDegrees(ephems[j].subsolarLatitude, 5);
                    foundValues[10] = Functions.formatAngleAsDegrees(ephems[j].distanceFromSun, 5);
                    foundValues[11] = Functions.formatAngleAsDegrees(ephems[j].distance, 5);
                    foundValues[12] = Functions.formatAngleAsDegrees(ephems[j].elongation, 5);
                    foundValues[13] = Functions.formatAngleAsDegrees(ephems[j].phaseAngle, 5);

                    int indexActual = DataSet.getIndex(Constellation.CONSTELLATION_NAMES, ephems[j].constellation + ' ');
                    int indexExpected = DataSet.getIndex(Constellation.CONSTELLATION_NAMES, constellationName + ' ');
                    assertEquals(indexActual, indexExpected);

                    final double diffs[] = {
                            Functions.normalizeRadians(ephems[j].longitudeOfCentralMeridian) - Functions.normalizeRadians(meridianLongitude * Constant.DEG_TO_RAD),
                            Functions.normalizeRadians(ephems[j].positionAngleOfPole) - Functions.normalizeRadians(poleAngle * Constant.DEG_TO_RAD),
                            Functions.normalizeRadians(ephems[j].subsolarLongitude) - Functions.normalizeRadians(subsolarLongitude * Constant.DEG_TO_RAD),
                            Functions.normalizeRadians(ephems[j].subsolarLatitude) - Functions.normalizeRadians(subsolarLatitude * Constant.DEG_TO_RAD),
                            ephems[j].distanceFromSun - distanceFromSun,
                            ephems[j].distance - distance,
                            Math.abs(ephems[j].elongation) - Math.abs(elongation) * Constant.DEG_TO_RAD,
                            Math.abs(ephems[j].phaseAngle) - Math.abs(phaseAngle) * Constant.DEG_TO_RAD
                    };

                    final String fields[] = {
                            "longitude of central meridian", "position angle of pole", "subsolar longitude", "subsolar latitude",
                            "distance from Sun", "distance to observer", "elongation", "phase angle", "constellation"
                    };

                    final double acceptableDiffs[] = {
                            1.0 * Constant.DEG_TO_RAD, // JPL is giving slightly wrong results ?
                            0.05 * Constant.DEG_TO_RAD,
                            1.0 * Constant.DEG_TO_RAD,
                            0.05 * Constant.DEG_TO_RAD,
                            1.0e-5,
                            1.0e-5,
                            0.015 * Constant.DEG_TO_RAD,
                            0.015 * Constant.DEG_TO_RAD
                    };

                    if (TARGET.SATURN == body || TARGET.Phobos == body || TARGET.Titan == body) {
                        acceptableDiffs[0] = 1.6338 * Constant.DEG_TO_RAD;
                        acceptableDiffs[2] = 1.6338 * Constant.DEG_TO_RAD;
                    }

                    if (TARGET.Phobos == body) {
                        acceptableDiffs[1] = 0.0192006;
                        acceptableDiffs[3] = 0.018925;
                    }

                    // Triton position using JPL elements is approximate
                    if (TARGET.NEPTUNE == body || TARGET.URANUS == body || TARGET.Triton == body) {
                        acceptableDiffs[4] = 1.0e-4;
                        acceptableDiffs[5] = 1.0e-4;
                    }

                    for (int k = 0; k < diffs.length; k++) {
                        double acceptableDiff = acceptableDiffs[k];
                        double diff = Math.abs(diffs[k]);

                        if (diff > Math.PI) {
                            diff -= Constant.TWO_PI;
                        }

                        //if (diff >= acceptableDiff) {
                        //    System.out.println("i: " + i + ", j: " + j + ", k: " + k + ", field: " + fields[k] + ", diff: " + diff + ", acceptable: " + acceptableDiff);
                        //}
                        assertTrue(diff < acceptableDiff);
                    }
                }
            }
        }
    }

    /*
     ! ***
     ! 11 = Sidereal time, true obliquity, and nutation.
     ! Input data is a date.
     ! Expected output for sidereal time test is the Greenwich apparent sidereal time, mean sidereal time, and equation of
     ! equinoxes (assuming input date is for 0h UT1).
     ! For obliquity the expected true obliquity for that date at 0h TT.
     ! For nutation the expected nutation at 0h TT without free-core nutation or pole movement.
     ! Data from AA. The IAU2006 algorithms are used.
     */
    @Test
    @DataProvider (name = "data_sideral_time")
    private Object[][] data_sideral_time() {
        return new Object[][] {
            { new AstroDate(2009, 1, 1), "siderealTime", "06h 43m 07.1394s", "06h 43m 06.3205s", "0.8189s" },
            { new AstroDate(2009, 11, 19), "siderealTime", "03h 52m 38.0203s", "03h 52m 37.1489s", "0.8714s" },
            { new AstroDate(2009, 7, 1), "obliquity", "23\u00b0 26' 21.2974\"", "21.2974\"", "" },
            { new AstroDate(2009, 7, 1), "nutation", "14.7823\"", "4.3391\"", "" },
        };
    }

    @Test (dataProvider = "data_sideral_time")
    public void testSideralTime(
            final AstroDate date,
            final String type,
            final String expectedValue0,
            final String expectedValue1,
            final String expectedValue2)
        throws JPARSECException
    {
        time = new TimeElement(date, TimeElement.SCALE.UNIVERSAL_TIME_UT1);

        if ("siderealtime".equals(type)) {
            double gast = SiderealTime.greenwichApparentSiderealTime(time, observer, eph);
            double gmst = SiderealTime.greenwichMeanSiderealTime(time, observer, eph);
            double eqeq = SiderealTime.equationOfEquinoxes(time, observer, eph);

            assertEquals(Functions.formatRA(gast, 4), expectedValue0);
            assertEquals(Functions.formatRA(gmst, 4), expectedValue1);
            assertEquals(Functions.formatValue(eqeq * Constant.RAD_TO_ARCSEC / 15.0d, 4) + 's', expectedValue2);
        } else if ("obliquity".equals(type)) {
            double obl = Obliquity.trueObliquity(Functions.toCenturies(astro.jd()), eph);
            assertEquals(Functions.formatAngle(obl, 4), expectedValue0);
        } else if ("nutation".equals(type)) {
            EarthOrientationParameters.clearEOP();
            Nutation.clearPreviousCalculation();
            Nutation.calcNutation(Functions.toCenturies(astro.jd()), eph);
            assertEquals(Functions.formatAngle(Nutation.getNutationInLongitude(), 4), expectedValue0);
            assertEquals(Functions.formatAngle(Nutation.getNutationInObliquity(), 4), expectedValue1);
        }
    }

    /*
     ! ***
     ! 12 = Tests for natural satellites theories and JPL ephemeris.
     ! Input data is JD (TT), theory name, satellite number or planet id constant for JPL ephem.
     ! Output is x, y, z, vx, vy, vz (in AU or AU/day, or km and km/day, km/yr, depending on the theory).
     ! From Horizons NATURAL_SATELLITES_AND_JPLDExxx_THEORIES
     */
    @Test
    @DataProvider (name = "data_natural_satellites_and_jpl_theory")
    private Object[][] data_natural_satellites_and_jpl_theory() {
        return new Object[][] {
            // From Horizons
            { 2442413.5, "de405", 0, -0.002778466806619606, -0.0001371481505240407, 0.00001598380242716914, 0.0000007121921412015217, -0.000006304236384065129, -0.000002754982127518105 },
            { 2460676.5, "de405", 0, -0.005729903581718697, -0.004576438644324111, -0.001788558413644218, 0.000007160785923387444, -0.000003317172921789437, -0.000001566444051546533 },
            { 2442413.5, "de405", 1, 0.2363743828365439, -0.3062866527086762, -0.1883244913305822, 0.01773958268732228, 0.01575571437706276, 0.006575020602516057 },
            { 2460676.5, "de405", 1, -0.3930329105340720, -0.1618290003542665, -0.04565180846134294, 0.005031591423528277, -0.02171734089259788, -0.01212198535208888 },
            { 2442413.5, "de405", 2, 0.4845626167198589, -0.4814490375328292, -0.2473299875827062, 0.01488229249232774, 0.01262844594009676, 0.004738248170656145 },
            { 2460676.5, "de405", 2, 0.4476888608388256, 0.5185822840206196, 0.2049285310188015, -0.01579911351233697, 0.01113451419284638, 0.006010096376850540 },
            // Earth unsupported in JPARSEC, position must be obtained indirectly
            //{ 2442413.5, "de405", 3, -0.1783293865616953, 0.8875024561409613, 0.3849174797326148, -0.01720209663873897, -0.002878905454813442, -0.001248178772238981 },
            //{ 2460676.5, "de405", 3, -0.1844133495183917, 0.8826332565543006, 0.3828082228185471, -0.01719757778068188, -0.002933328868506422, -0.001271875952440207 },
            { 2460676.5, "de405", 4, -0.5274157683992698, 1.376996176286727, 0.6459773632095778, -0.01270467413829678, -0.003162816692938147, -0.001107852767461495 },
            { 2469807.5, "de405", 4, -1.542430033907673, -0.4759482003289589, -0.1766849969944578, 0.004867166022962643, -0.01095577086977315, -0.005156268633839071 },
            { 2442413.5, "de405", 5, 4.934523854341761, -0.4442312880461308, -0.3106919066158428, 0.0007178788720506169, 0.007226513657206959, 0.003080300901256049 },
            { 2460676.5, "de405", 5, 1.050302344632133, 4.574254917002780, 1.935117479479192, -0.007468790877726857, 0.001699068362041206, 0.0009100993036339864 },
            { 2442413.5, "de405", 6, -2.426096721308273, 8.001136398692759, 3.408197939580888, -0.005675479727241492, -0.001487801739717524, -0.0003703820454423332 },
            { 2460676.5, "de405", 6, 9.455339917217261, -1.485995953092897, -1.021046562120735, 0.0007162730248081354, 0.005069753535508238, 0.002063210237583061 },
            { 2442413.5, "de405", 7, -16.08678481790292, -8.382709219779178, -3.443497635847714, 0.001900887811054284, -0.003297607858126338, -0.001471206529725114 },
            { 2460676.5, "de405", 7, 11.09788830709051, 14.79532470989420, 6.322982332846551, -0.003266569195965867, 0.001860844022546527, 0.0008611944247564096 },
            { 2442413.5, "de405", 8, -10.42862868588783, -26.42266013836331, -10.55546066473623, 0.002927296672225484, -0.0009571534427704233, -0.0004646226330431307 },
            { 2460676.5, "de405", 8, 29.87423311315728, -0.3177680028408320, -0.8738177584383171, 0.00004643143962398082, 0.002922804347922567, 0.001195163764607200 },
            // Horizons returns Pluto position for DE413, see ftp://naif.jpl.nasa.gov/pub/naif/generic_kernels/spk/planets/a_old_versions/de413_memo.ps
            // Difference is 300m
            { 2442413.5, "de413", 9, -29.25161272830501, -7.142391888761897, 6.584071526936301, 0.0009063986543760217, -0.003064256489386518, -0.001229243028582288 },
            { 2460676.5, "de413", 9, 18.22310042133425, -26.71691208124781, -13.82812035390832, 0.002767430794713086, 0.001216767238512264, -0.0004541040766725648 },
            { 2442413.5, "de405", 10, -0.001836555810927522, 0.001519678078544720, 0.0004346702822026698, -0.0004122203176888004, -0.0004236374236448662, -0.0002064885146158632 },
            { 2460676.5, "de405", 10, 0.001016407150940106, -0.002057673933390378, -0.001115523135172851, 0.0005386351640509153, 0.0002277848065203184, 0.0001228891083746145 },
            { 2442413.5, "de405", 11, -0.1783517017903028, 0.8875209211195537, 0.3849227612310906, -0.01720710535699908, -0.002884052897596879, -0.001250687728613225 },
            { 2460676.5, "de405", 11, -0.1844009995762899, 0.8826082546110162, 0.3827946685591936, -0.01719103304800872, -0.002930561149714225, -0.001270382777808411 },
            // Librations, see http://astro.ukho.gov.uk/data/tn/naotn74.pdf. Values are psi, theta, and psi
            //{ 2455713.5, "de403", 16, 0.0671434078218415, 0.4124126209708347, 3522.7808866515984000 },
            // From the different theories by the IMCCE
            { 2426855, "l1", 1, 0.002271527767, 0.001491984142, 0.000750111216, -0.005895653255, 0.007343188303, 0.003402858909 },
            { 2456483, "l1", 2, 0.003438637708, 0.002613676533, 0.001313674687, -0.005147284797, 0.005398552812, 0.002560664504 },
            { 2476235, "l1", 3, -0.006642067401, -0.002374459282, -0.001250108202, 0.002339135125, -0.005280921340, -0.002455264319 },
            { 2441669, "l1", 4, -0.012136434072, 0.003374929866, 0.001437454080, -0.001354205854, -0.004062009363, -0.001947041015 },
            { 2451605, "mars07", 1, 0.000033910062, -0.000038516526, -0.000037325256, 0.000870900873, 0.000850538844, -0.000068951380 },
            { 2451555, "mars07", 2, 0.000003554238, -0.000139120094, -0.000072183842, 0.000692893922, 0.000179388498, -0.000311490283 },
            { 2449909.125, "gust86", 1, 116776.465, -9655.903, -55704.358, -2.45651219, 2.65694226, -5.62224338 },
            { 2449909.125, "gust86", 2, 186368.403, -42290.739, -7012.500, 0.12909352, 1.43746647, -5.30747363 },
            { 2449909.125, "gust86", 3, -244947.486, 76695.011, -65719.487, -1.45138070, -0.87096610, 4.36898332 },
            { 2449909.125, "gust86", 4, -289642.933, 149245.644, -290221.835, -2.60666546, -0.07923656, 2.54490241 },
            { 2449909.125, "gust86", 5, 554001.114, -156422.593, 93294.441, 0.69497936, 0.66198498, -3.00317166 },
            { 2421677.4, "tass1.7", 1, -0.001198576888, -0.000209825461, 0.000186837157, 0.723936877, -2.640848972, 1.326181365 },
            { 2406147.5, "tass1.7", 2, -0.000317458744, 0.001387123315, -0.000695569370, -2.613864221, -0.369066453, 0.446037406 },
            { 2409977.4, "tass1.7", 3, 0.001169171755, -0.001455040893, 0.000629464516, 1.917976545, 1.207348532, -0.772706551 },
            { 2406477.5, "tass1.7", 4, -0.001139424157, 0.002032365984, -0.000955456204, -1.880203521, -0.779545669, 0.590410818 },
            { 2405824.5, "tass1.7", 5, 0.000255016488, -0.003124357779, 0.001600422912, 1.781208363, 0.049114113, -0.186597578 },
            { 2440512.6, "tass1.7", 6, -0.008312968003, 0.000590507681, 0.000496831020, -0.019590068, -1.020463815, 0.529324054 },
            { 2406327.6, "tass1.7", 7, 0.010080312859, -0.003940249379, 0.001120175589, 0.378238813, 0.783156263, -0.425574501 },
            { 2406216.6, "tass1.7", 8, 0.002447343471, 0.022675031476, -0.006665246397, -0.671393700, 0.121862318, 0.098725209 },
        };
    }

    @Test (dataProvider = "data_natural_satellites_and_jpl_theory")
    public void testNaturalSatellitesAndJplTheory(
            final double jd,
            final String theory,
            final int which,
            final double expectedValue0,
            final double expectedValue1,
            final double expectedValue2,
            final double expectedValue3,
            final double expectedValue4,
            final double expectedValue5)
        throws JPARSECException
    {
        double foundValues[] = null;
        double epsilon = 0;

        if ("l1".equals(theory)) {
            double e[] = L1.L1_theory(jd, which);
            foundValues = new double[] {
                    Functions.roundToPlace(e[0], -12),
                    Functions.roundToPlace(e[1], -12),
                    Functions.roundToPlace(e[2], -12),
                    Functions.roundToPlace(e[3], -12),
                    Functions.roundToPlace(e[4], -12),
                    Functions.roundToPlace(e[5], -12)
            };

            epsilon = 0.000000000001;
        }

        if ("mars07".equals(theory)) {
            double e[] = Mars07.getMoonPosition(jd, which, true);
            foundValues = new double[] {
                    Functions.roundToPlace(e[0], -12),
                    Functions.roundToPlace(e[1], -12),
                    Functions.roundToPlace(e[2], -12),
                    Functions.roundToPlace(e[3], -12),
                    Functions.roundToPlace(e[4], -12),
                    Functions.roundToPlace(e[5], -12)
            };
            epsilon = 3.4e-5;
        }

        if ("gust86".equals(theory)) {
            double e[] = GUST86.GUST86_theory(jd, which, 4); // B1950
            foundValues = new double[] {
                    Functions.roundToPlace(e[0] * Constant.AU, -3),
                    Functions.roundToPlace(e[1] * Constant.AU, -3),
                    Functions.roundToPlace(e[2] * Constant.AU, -3),
                    Functions.roundToPlace(e[3] * Constant.AU / Constant.SECONDS_PER_DAY, -8),
                    Functions.roundToPlace(e[4] * Constant.AU / Constant.SECONDS_PER_DAY, -8),
                    Functions.roundToPlace(e[5] * Constant.AU / Constant.SECONDS_PER_DAY, -8)
            };
            epsilon = 0.00000001;
        }

        if ("tass1.7".equals(theory)) {
            double e[] = TASS17.TASS17_theory(jd, which, false);
            foundValues = new double[] {
                    Functions.roundToPlace(e[0], -12),
                    Functions.roundToPlace(e[1], -12),
                    Functions.roundToPlace(e[2], -12),
                    Functions.roundToPlace(e[3] * 365.25, -9),
                    Functions.roundToPlace(e[4] * 365.25, -9),
                    Functions.roundToPlace(e[5] * 365.25, -9)
            };

            epsilon = 0.000000001;
        }

        if (theory.startsWith("de")) {
            JPLEphemeris jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE405);
            epsilon = 0.0000000000001;

            if (theory.endsWith("200")) {
                jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE200);
            }
            if (theory.endsWith("403")) {
                jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE403);
            }
            if (theory.endsWith("406")) {
                jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE406);
            }
            if (theory.endsWith("413")) {
                jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE413);
                epsilon = 0.00005;
            }
            if (theory.endsWith("414")) {
                jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE414);
            }
            if (theory.endsWith("422")) {
                jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE422);
            }
            double e[] = jpl.getPositionAndVelocity(jd, TARGET.values()[which]);

            foundValues = new double[] {
                    Functions.roundToPlace(e[0], -16),
                    Functions.roundToPlace(e[1], -16),
                    Functions.roundToPlace(e[2], -16),
                    Functions.roundToPlace(e[3], -16),
                    Functions.roundToPlace(e[4], -16),
                    Functions.roundToPlace(e[5], -16)
            };
        }

        assertEquals(foundValues[0], expectedValue0, epsilon);
        assertEquals(foundValues[1], expectedValue1, epsilon);
        assertEquals(foundValues[2], expectedValue2, epsilon);
        assertEquals(foundValues[3], expectedValue3, epsilon);
        assertEquals(foundValues[4], expectedValue4, epsilon);
        assertEquals(foundValues[5], expectedValue5, epsilon);
    }

    /*
     ! ***
     ! 13 = Tests for star ephemerides and Simbad.
     ! Input data is date (UT1) as year, month, day, hour, minute, second, object name (to be resolved by Simbad).
     ! Output is mean star position (J2000, as given by Simbad), apparent position in equinox of date, and velocities
     ! relative to LSR, geocentric, and topocentric.
     ! Default location is Madrid.
     ! Note Simbad/Vizier queries are sometimes not working, specially on weekends ...
     */
    @Test
    @DataProvider (name = "data_star_ephemerides_simbad")
    private Object[][] data_star_ephemerides_simbad() {
        return new Object[][] {
                { new AstroDate(2011, 7, 13, 10, 22, 46), "HD259431", "06h 33m 05.19s", "10\u00b0 19' 20.0\"", "06h 33m 43.28s", "10\u00b0 18' 45.8\"", 4.27, 12.97, 12.88 },
                { new AstroDate(2011, 7, 13, 10, 22, 46), "R Mon", "06h 39m 09.95s", "08\u00b0 44' 09.7\"", "06h 39m 47.58s", "08\u00b0 43' 29.5\"", -3.01, 6.80, 6.70 },
        };
    }

    @Test (dataProvider = "data_star_ephemerides_simbad")
    public void testStarEphemeridesSimbad(
            final AstroDate date,
            final String name,
            final String expectedValue0,
            final String expectedValue1,
            final String expectedValue2,
            final String expectedValue3,
            final double expectedValue4,
            final double expectedValue5,
            final double expectedValue6)
        throws JPARSECException
    {
        time = new TimeElement(date, TimeElement.SCALE.UNIVERSAL_TIME_UT1);

        SimbadElement simbad = SimbadQuery.query(name);
        StarElement star = StarElement.parseSimbadElement(simbad);
        StarEphemElement starEphem = StarEphem.starEphemeris(time, observer, eph, star, false);

        double vLSR = StarEphem.getLSRradialVelocity(time, observer, eph, star);
        double vTOP = StarEphem.getRadialVelocity(time, observer, eph, star);
        eph.isTopocentric = false;
        double vGEO = StarEphem.getRadialVelocity(time, observer, eph, star);

        assertEquals(Functions.formatRA(star.rightAscension, 2), expectedValue0);
        assertEquals(Functions.formatDEC(star.declination, 1), expectedValue1);
        assertEquals(Functions.formatRA(starEphem.rightAscension, 2), expectedValue2);
        assertEquals(Functions.formatDEC(starEphem.declination, 1), expectedValue3);
        assertEquals(vLSR, expectedValue4, 0.01);
        assertEquals(vGEO, expectedValue5, 0.01);
        assertEquals(vTOP, expectedValue6, 0.01);
    }

    /*
     ! ***
     ! 14 = Test for main astronomical events, like Venus or Mercury transits on the Sun disk, equinox and
     ! solstices, planetary phenomena like oppositions (maximum elongations), and transits of the GRS
     ! on Jupiter. Tests data taken from different sources in Internet, and GRS from Sky & Telescope,
     ! see http://www.skyandtelescope.com/skytel/beyondthepage/91731334.html. Location is Madrid, and
     ! output time are in different time scales depending on the event type.
     */
    @Test
    @DataProvider (name = "data_main_events")
    private Object[][] data_main_events() {
        return new Object[][] {
            { new AstroDate(2004, 1, 15), "apogeeperigee", 1, 0, "Moon's perigee: 2004-01-19 19:26 TDB (parallax = 1\u00b0 0' 25.622\", distance = 362876 km)" },
            { new AstroDate(2004, 1, 1), "apogeeperigee", 2, 0, "Moon's apogee: 2004-01-03 20:20 TDB (parallax = 54' 2.839\", distance = 405706 km)" },
            { new AstroDate(1988, 10, 1), "apogeeperigee", 2, 0, "Moon's apogee: 1988-10-07 20:30 TDB (parallax = 54' 0.679\", distance = 405976 km)" },
            { new AstroDate(2011, 1, 1), "equinoxsolstice", 8, -1, "21-jun-2011 19:16:30 LT" },
            { new AstroDate(2010, 11, 1), "grs", 0, 0, "01-nov-2010 03:31 UT1" }, // S&T says 03:30
            { new AstroDate(2010, 10, 1), "grs", 0, 0, "01-oct-2010 02:56 UT1" }, // S&T says 02:57
            { new AstroDate(2011, 1, 1), "grs", 0, 0, "01-jan-2011 09:08 UT1" }, // S&T says 09:01, but JPARSEC uses spline interpolation to get updated GRS longitude as it moves
            { new AstroDate(2011, 2, 20), "grs", 0, 0, "20-feb-2011 00:52 UT1" }, // S&T says 00:43, but JPARSEC uses spline interpolation to get updated GRS longitude as it moves
            { new AstroDate(2012, 3, 15), "transit", 2, 0, "06-jun-2012 03:30:32 LT" }, // NASA says 03:30, JPARSEC uses Meeus approximate method (only geocentric)
            { new AstroDate(2011, 7, 15), "planet", 4, 15, "03-mar-2012 21:48:07 LT" },
            { new AstroDate(2011, 11, 1), "planet", 4, 11, "24-jan-2013 10:13:02 LT" }, //"09-mar-2011 15:26:37 LT" },
            { new AstroDate(-1000, 3, 1), "moon", 4, 2, "14-mar-1000 05:38:14 B.C. TDB" },
            { new AstroDate(1992, 4, 12), "librations", 0, 0, "l = -1.23, b = 4.20, p = 15.08" }
        };
    }

    @Test (dataProvider = "data_main_events")
    public void testMainEvents(
            final AstroDate date,
            final String type,
            final int inputValue0,
            final int inputValue1,
            final String expectedValue)
        throws JPARSECException
    {
        double jd = date.jd();
        String foundValue = null;

        if ("apogeeperigee".equals(type)) {
            int w = inputValue0;
            SimpleEventElement s = null;
            if (w == 1) s = LunarEvent.getPerigee(jd, MainEvents.EVENT_TIME.NEXT);
            if (w == 2) s = LunarEvent.getApogee(jd, MainEvents.EVENT_TIME.NEXT);
            foundValue = s.toString();
        } else if ("equinoxsolstice".equals(type)) {
            SimpleEventElement see = MainEvents.EquinoxesAndSolstices(date.getYear(), SimpleEventElement.EVENT.values()[inputValue0]);
            foundValue = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, TimeElement.SCALE.LOCAL_TIME), TimeElement.SCALE.LOCAL_TIME), true, true);
        } else if ("grs".equals(type)) {
            SimpleEventElement see = MainEvents.getJupiterGRSNextTransitTime(TimeScale.getJD(new TimeElement(jd, TimeElement.SCALE.UNIVERSAL_TIME_UT1), observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME));
            foundValue = TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(new TimeElement(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1), TimeElement.SCALE.UNIVERSAL_TIME_UT1), true, true);
        } else if ("librations".equals(type)) {
            eph.isTopocentric = false;
            time = new TimeElement(date, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            double r[] = LunarEvent.getEckhardtMoonLibrations(time, observer, eph);
            foundValue = "l = " + Functions.formatAngleAsDegrees(r[0], 2) + ", b = " + Functions.formatAngleAsDegrees(r[1], 2) + ", p = " + Functions.formatAngleAsDegrees(r[2], 2);
        } else if ("moon".equals(type)) {
            SimpleEventElement see = MainEvents.MoonPhaseOrEclipse(jd, SimpleEventElement.EVENT.values()[inputValue0], MainEvents.EVENT_TIME.values()[inputValue1]);
            foundValue = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), true, true);
        } else if ("planet".equals(type)) {
            SimpleEventElement see = MainEvents.getPlanetaryEvent(TARGET.values()[inputValue0], jd, SimpleEventElement.EVENT.values()[inputValue1], MainEvents.EVENT_TIME.NEXT, true);
            foundValue = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, TimeElement.SCALE.LOCAL_TIME), TimeElement.SCALE.LOCAL_TIME), true, true);
        } else if ("transit".equals(type)) {
            SimpleEventElement see = MainEvents.getMercuryOrVenusTransit(TARGET.values()[inputValue0], jd, jd + 5 * 365.25, true);
            foundValue = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, TimeElement.SCALE.LOCAL_TIME), TimeElement.SCALE.LOCAL_TIME), true, true);
        }

        // Pass to English if default locale is Spanish
        foundValue = convertMonthsToEnglish(foundValue);
        assertEquals(foundValue, expectedValue);
    }

    /*
     ! ***
     ! 15 = Test for double and eclipsing variable stars.
     ! Input values are the date and time in local time for Madrid, the object type (double, variable),
     ! and the object name as given in the catalogs in JPARSEC.
     ! Output values are the phase and next minima for variable (eclipsing binary) stars, and the separation (") and
     ! PA (deg) for double stars.
     */
    @Test
    @DataProvider (name = "data_double_variable_stars")
    private Object[][] data_double_variable_stars() {
        return new Object[][] {
            { new AstroDate(2011, 7, 14, 14, 0, 0), "variable", "AN And", "0.81", "15-jul-2011 04:21" },
            { new AstroDate(2011, 7, 14, 14, 0, 0), "double", "STF  60AB", "13.2", "-36.3" },
        };
    }

    @Test (dataProvider = "data_double_variable_stars")
    public void testDoubleVariableStars(
            final AstroDate date,
            final String type,
            final String name,
            final String expectedValue0,
            final String expectedValue1)
        throws JPARSECException
    {
        time = new TimeElement(date, TimeElement.SCALE.LOCAL_TIME);
        String foundValue0 = null;
        String foundValue1 = null;

        if ("double".equals(type)) {
            ReadFile rf = new ReadFile();
            // Sixth Catalog of Orbits of Visual Binary Stars, Hartkopf 2006
            rf.setPath(DoubleStarElement.PATH_VISUAL_DOUBLE_STAR_CATALOG);
            rf.readFileOfDoubleStars();
            int i = rf.searchByName(name);
            DoubleStarElement dstar = rf.getDoubleStarElement(i);
            dstar.calcEphemeris(time, observer);
            foundValue0 = Functions.formatValue(dstar.getDistance(), 1);
            foundValue1 = Functions.formatAngleAsDegrees(dstar.getPositionAngle(), 1);
        }

        if ("variable".equals(type)) {
            ReadFile rf = new ReadFile();
            rf.setPath(VariableStarElement.PATH_VARIABLE_STAR_CATALOG);
            rf.readFileOfVariableStars();
            int i = rf.searchByName(name);
            VariableStarElement vstar = rf.getVariableStarElement(i);
            vstar.calcEphemeris(time, observer, false);
            foundValue0 = Functions.formatValue(vstar.getPhase(), 2);
            foundValue1 = TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(new TimeElement(vstar.getNextMinima(), null), true, true); //+secondaryFlag

            // Pass to English if default locale is Spanish
            foundValue1 = convertMonthsToEnglish(foundValue1);
        }

        assertEquals(foundValue0, expectedValue0);
        assertEquals(foundValue1, expectedValue1);
    }

    /*
     ! ***
     ! 16 = Test for eclipses.
     ! Input is date (eclipse search starts from this date in TDB), eclipse type (solar, lunar),
     ! and time scale for output times.
     ! Output values are the times of the different phases in the selected time scale, for Madrid.
     ! For lunar eclipses ELP2000 theory (fixed for moon secular acceleration) is used.
     ! For solar eclipses Moshier method is applied.
     */
    @Test
    @DataProvider (name = "data_eclipses")
    private Object[][] data_eclipses() {
        return new Object[][] {
            { new AstroDate(2005, 10, 1), "solar", TimeElement.SCALE.UNIVERSAL_TIME_UT1,
                    "03-oct-2005 07:40:13 (partial)",
                    "03-oct-2005 08:55:55 (annular)",
                    null, // "03-oct-2005 08:58:00 (Eclipse maximum)",
                    null, null
            }, // within 1s with Spenak's results" },
            { new AstroDate(-1000, 3, 10), "lunar", TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME,
                    "14-mar-1000 02:55:24 B.C. (penumbral)",
                    "14-mar-1000 03:55:28 B.C. (full penumbral)",
                    "14-mar-1000 03:54:53 B.C. (partial)",
                    "14-mar-1000 04:58:40 B.C. (total)",
                    "14-mar-1000 05:40:16 B.C. (Eclipse maximum)"
            }
        };
    }

    @Test (dataProvider = "data_eclipses")
    public void testEclipses(
            final AstroDate date,
            final String type,
            final TimeElement.SCALE scale,
            final String expectedValue0,
            final String expectedValue1,
            final String expectedValue2,
            final String expectedValue3,
            final String expectedValue4)
        throws JPARSECException
    {
        time = new TimeElement(date, TimeElement.SCALE.LOCAL_TIME);
        String foundValues[] = new String[6];
        double jd;

        // Set Madrid location according to Spenak's coordinates
        observer.setLongitudeDeg(-3.68333);
        observer.setLatitudeDeg(40.4);
        observer.setHeight(667, true);

        eph.targetBody = TARGET.Moon;

        if ("solar".equals(type)) {
            SolarEclipse se = new SolarEclipse(time, observer, eph);
            MoonEventElement[] events = se.getEvents();

            for (int i = 0; i < events.length; i++) {
                TimeElement t = new TimeElement(events[i].startTime, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                jd = TimeScale.getJD(t, observer, eph, scale);

                if (events[i].startTime != 0.0) {
                    foundValues[i] = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(jd, null), true, true) + " (" + events[i].details + ')';
                }
            }

            TimeElement t = new TimeElement(se.getEclipseMaximum(), TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            jd = TimeScale.getJD(t, observer, eph, scale);
            foundValues[foundValues.length - 1] = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(jd, null), true, true) + " (Eclipse maximum)";
        }

        if ("lunar".equals(type)) {
            eph.isTopocentric = false;
            eph.algorithm = EphemerisElement.ALGORITHM.VSOP87_ELP2000ForMoon;
            eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_2006;
            LunarEclipse le = new LunarEclipse(time, observer, eph);
            MoonEventElement[] events = le.getEvents();

            for (int i = 0; i < events.length; i++) {
                TimeElement t = new TimeElement(events[i].startTime, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                jd = TimeScale.getJD(t, observer, eph, scale);

                if (events[i].startTime != 0.0) {
                    foundValues[i] = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(jd, null), true, true) + " (" + events[i].details + ')';
                }
            }

            TimeElement t = new TimeElement(le.getEclipseMaximum(), TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            jd = TimeScale.getJD(t, observer, eph, scale);
            foundValues[foundValues.length - 1] = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(jd, null), true, true) + " (Eclipse maximum)";
        }

        // Pass to English if default locale is Spanish
        foundValues[0] = convertMonthsToEnglish(foundValues[0]);

        assertEquals(foundValues[0], expectedValue0);
        assertEquals(foundValues[1], expectedValue1);
        assertEquals(foundValues[2], expectedValue2);
        assertEquals(foundValues[3], expectedValue3);
        assertEquals(foundValues[4], expectedValue4);
    }

    private static String convertMonthsToEnglish(final String message) {
        return message
                .replaceAll("[Ee]ne", "jan")
                .replaceAll("[Aa]br", "apr")
                .replaceAll("[Aa]go", "aug")
                .replaceAll("[Dd]ic", "dec");
    }

    /*
     ! ***
     ! 17 = Test for very accurate ephemerides following the algorithms by IMCCE, Horizons, and AA.
     ! Input values are year, month, day (0h TT), target body, reduction type (IMCCE, AA, HORIZONS),
     ! the maximum allowed difference with JPARSEC results in milli-arcseconds and AU.
     ! Output is the RA, DEC, and distance obtained from the web queries at different ephemerides servers.
     ! Algorithm is JPL DE405, with files obtained from an external directory (downloaded from
     ! ftp://ssd.jpl.nasa.gov/pub/eph/planets/ascii/de405). In AA distance is not tested since it is
     ! the 'true' distance, not the apparent the planet had when it emitted the light that reaches us. The distance
     ! difference is also checked, the value used to throw a fail is 2.0E-12 AU for HORIZONS and 2.0E-9 for IMCCE.
     */
    @Test
    @DataProvider (name = "data_ephemerides_other_sources")
    private Object[][] data_ephemerides_other_sources() {
        return new Object[][] {
            { new AstroDate(1600, 1, 1), TARGET.NEPTUNE, "IMCCE", 1, 2e-9, "10h 00m 31.59378s", "12\u00b0 51' 13.3640\"", 29.466102577 },
            { new AstroDate(1900, 1, 1), TARGET.NEPTUNE, "IMCCE", 1, 2e-9, "05h 39m 21.87494s", "22\u00b0 03' 58.9701\"", 28.920271361 },
            { new AstroDate(2200, 1, 1), TARGET.NEPTUNE, "IMCCE", 1, 2e-9, "01h 24m 58.76536s", "07\u00b0 07' 44.9398\"", 29.615237432 },
            { new AstroDate(1600, 1, 1), TARGET.NEPTUNE, "HORIZONS", 70, 2e-12, "10h 00m 31.5983s", "12\u00b0 51' 13.356\"", 29.4661025774498 },
            { new AstroDate(1900, 1, 1), TARGET.NEPTUNE, "HORIZONS", 70, 2e-12, "05h 39m 21.8796s", "22\u00b0 03' 58.978\"", 28.9202713607579 },
            { new AstroDate(2200, 1, 1), TARGET.NEPTUNE, "HORIZONS", 80, 6e-12, "01h 24m 58.7607s", "07\u00b0 07' 44.908\"", 29.6152374317953 },
            /*
             ! Note the difference in RA in 2000 !!! It is clear from previous tests that HORIZONS and IMCCE uses APPLY_IAU1976
             ! algorithms, but IMCCE DOES NOT CORRECT FOR POLAR MOTION! In dates far
             ! from J2000 IMCCE results can be fitted to the milliarcsecond level, but that's not possible with HORIZONS
             ! since I don't know which set of algorithms they use. Comparing both servers it is clear that IMCCE has a
             ! better implementation of the ephemerides (but with old algorithms), and HORIZONS has a better implementation
             ! of the geometry of the Solar System (distances, elongation and phase angle, central meridian and PA axis/pole).
             */
            { new AstroDate(2000, 1, 1), TARGET.NEPTUNE, "IMCCE", 1, 2e-9, "20h 21m 39.47908s", "-19\u00b0 13' 01.9774\"", 31.021117421 },
            { new AstroDate(2000, 1, 1), TARGET.NEPTUNE, "HORIZONS", 50, 3e-12, "20h 21m 39.4756s", "-19\u00b0 13' 01.987\"", 31.0211174206981 },

            { new AstroDate(1600, 1, 1), TARGET.MARS, "IMCCE", 1, 2e-9, "09h 24m 41.58178s", "19\u00b0 15' 18.9756\"", 0.746598316 },
            { new AstroDate(1900, 1, 1), TARGET.MARS, "IMCCE", 8, 2e-9, "19h 00m 39.90359s", "-23\u00b0 38' 58.3076\"", 2.400963430 },
            { new AstroDate(2200, 1, 1), TARGET.MARS, "IMCCE", 1, 4e-9, "10h 25m 57.25433s", "13\u00b0 29' 37.0809\"", 0.842946359 },
            { new AstroDate(1600, 1, 1), TARGET.MARS, "HORIZONS", 70, 1e-12, "09h 24m 41.5865s", "19\u00b0 15' 18.970\"", 0.74659831738180 },
            { new AstroDate(1900, 1, 1), TARGET.MARS, "HORIZONS", 70, 1e-13, "19h 00m 39.9087s", "-23\u00b0 38' 58.308\"", 2.40096343001482 },
            { new AstroDate(2200, 1, 1), TARGET.MARS, "HORIZONS", 80, 4e-12, "10h 25m 57.2494s", "13\u00b0 29' 37.104\"", 0.84294636203948 },
            /*
             ! Well, Astronomical Almanac is easier to understand, just IAU2006 algorithms and J2000 frame. Due to lack of digits
             ! the results of JPARSEC are rounded to the same 3 decimal places in RA and 2 in DEC, and then the difference is computed.
             ! It should be zero.
             */
            { new AstroDate(2011, 7, 18), TARGET.NEPTUNE, "AA", 0, 0, "22h 10m 46.150s", "-11\u00b0 49' 46.06\"", 0 },
            { new AstroDate(2011, 7, 18), TARGET.MARS, "AA", 0, 0, "05h 11m 32.063s", "23\u00b0 05' 16.56\"", 0 },
            { new AstroDate(2011, 7, 18), TARGET.JUPITER, "AA", 0, 0, "02h 21m 54.955s", "12\u00b0 49' 28.91\"", 0 },
            /*
             ! Note here the bad coincidence JPARSEC-IMCCE in Deimos for dates far from J2000, and the excellent one
             ! in Oberon everytime. Maybe IMCCE has a better version of the theory for Martian satellites, since GUST86
             ! theory is not from IMCCE. Anyway, from Oberon it is clear that JPARSEC is extremely accurate. In Jupiter
             ! the difference varies from 40 mas in year 1600 to a few mas close to J2000. This variation with time
             ! and difference is high enough to worth further investigation. Obviously a bug in IMCCE ephemerides server
             ! cannot be discarded, mainly because I have found that to get the same output (mas level) for Titan it is
             ! necessary to rotate ecliptic position (from TASS) to equatorial using the ecliptic of date, not the J2000
             ! ecliptic as it is mentioned in the TASS theory. Maybe the same problem is happening in Mars and Jupiter
             ! and I need some king of strange rotation for their equatorial positions ? Horizons does not help here,
             ! since I cannot reproduce the results to the mas level.
             */
            { new AstroDate(1600, 1, 1), TARGET.Deimos, "IMCCE", 2000, 2e-5, "09h 24m 44.52675s", "19\u00b0 15' 19.6628\"", 0.746640479 },
            { new AstroDate(1900, 1, 1), TARGET.Deimos, "IMCCE", 20, 5e-8, "19h 00m 40.56578s", "-23\u00b0 38' 59.5043\"", 2.400848638 },
            { new AstroDate(2200, 1, 1), TARGET.Deimos, "IMCCE", 1200, 1e-5, "10h 25m 59.52983s", "13\u00b0 29' 25.4275\"", 0.842883773 },
            { new AstroDate(1600, 1, 1), TARGET.Titan, "IMCCE", 0.5, 5e-10, "13h 45m 15.35365s", "-08\u00b0 17' 57.0768\"", 9.998734380 }, // USING ECLIPTIC OF DATE, NOT J2000
            { new AstroDate(1900, 1, 1), TARGET.Titan, "IMCCE", 0.5, 2e-10, "17h 50m 10.06797s", "-22\u00b0 24' 27.4608\"", 11.031604402 },
            { new AstroDate(2200, 1, 1), TARGET.Titan, "IMCCE", 0.5, 2e-9, "22h 02m 43.88507s", "-13\u00b0 27' 15.7030\"", 10.450165195 },
            { new AstroDate(1600, 1, 1), TARGET.Callisto, "IMCCE", 40, 2e-6, "09h 33m 53.33724s", "15\u00b0 30' 35.5888\"", 4.558908178 },
            { new AstroDate(1900, 1, 1), TARGET.Callisto, "IMCCE", 20, 2e-6, "15h 56m 43.78523s", "-19\u00b0 36' 26.0122\"", 6.125545509 },
            { new AstroDate(2200, 1, 1), TARGET.Callisto, "IMCCE", 4, 1e-6, "22h 26m 48.97572s", "-10\u00b0 48' 53.3763\"", 5.494266711 },
            { new AstroDate(1600, 1, 1), TARGET.Oberon, "IMCCE", 0.5, 2e-9, "01h 41m 49.40287s", "10\u00b0 01' 47.6674\"", 19.470081383 }, // OK
            { new AstroDate(1900, 1, 1), TARGET.Oberon, "IMCCE", 0.5, 2e-8, "16h 34m 01.71001s", "-21\u00b0 54' 48.8151\"", 19.838041698 },
            { new AstroDate(2200, 1, 1), TARGET.Oberon, "IMCCE", 0.5, 1e-9, "05h 46m 52.47223s", "23\u00b0 32' 39.3707\"", 18.121971509 },
        };
    }

    @Test (dataProvider = "data_ephemerides_other_sources")
    public void testEphemeridesOtherSources(
            final AstroDate date,
            final TARGET body,
            final String method,
            final double maxErrorMilliarcseconds,
            final double maxErrorAU,
            final String expectedValue0,
            final String expectedValue1,
            final double distance)
        throws JPARSECException
    {
        if (distance == 0) {
            return;
        }

        eph.correctForEOP = true;
        eph.useVondrak2011PrecessionFormulaInsteadOfIAU2006 = false;
        eph.correctForPolarMotion = false;
        eph.correctEOPForDiurnalSubdiurnalTides = false;

        time = new TimeElement(date, TimeElement.SCALE.TERRESTRIAL_TIME);

        eph.isTopocentric = false;
        eph.algorithm =
                body.isNaturalSatellite() ?
                        EphemerisElement.ALGORITHM.NATURAL_SATELLITE :
                        Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH == null ?
                                EphemerisElement.ALGORITHM.MOSHIER :
                                EphemerisElement.ALGORITHM.JPL_DE405;

        eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_1976;

        if ("AA".equals(method)) {
            eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_2006;
            eph.frame = EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000;
            eph.correctForEOP = false;
        }

        eph.targetBody = body;
        EphemElement pephem = Ephem.getEphemeris(time, observer, eph, false);
        double expectedRA = Functions.parseRightAscension(expectedValue0);
        double expectedDEC = Functions.parseDeclination(expectedValue1);
        double exactRA = pephem.rightAscension, exactDEC = pephem.declination;

        // Compensate for the lack of digits in AA's results
        if ("AA".equals(method)) {
            pephem.rightAscension = Functions.parseRightAscension(Functions.formatRA(pephem.rightAscension, 3));
            pephem.declination = Functions.parseDeclination(Functions.formatDEC(pephem.declination, 2));
        }

        double difLoc = LocationElement.getAngularDistance(pephem.getEquatorialLocation(),
                new LocationElement(expectedRA, expectedDEC, 1.0)) * Constant.RAD_TO_ARCSEC * 1000.0;
        final double difDist = Math.abs(pephem.distance - distance);

        //assertTrue(difLoc > maxErrorMilliarcseconds || (difDist > maxErrorAU && difDist >= 0));
        assertTrue(difLoc <= maxErrorMilliarcseconds);
        assertTrue(difDist <= maxErrorAU);
    }

    /*
     ! ***
     ! 18 = Test for mutual events of natural satellites. Input values are year, month, day, planet (5 = Jupiter,
     ! 6 = Saturn, 7 = Uranus), event type (sat. #x eclipses/occultes sat #y, (P)artial, (T)otal, (A)nnular).
     ! Output values are start time (TT), time of maximum, and duration (s). Predictions below are from
     ! IMCCE (geocentric). All events with duration betweem 1000 and 3000s are considered, except for Saturn,
     ! where the lower limit is around 600s. Here what is tested is the logic of transits of satellites on top
     ! of each other as seen from Earth (occultations) or from Sun (eclipses). Note that the same logic is
     ! applied to events between satellites and their mother planets, which are not tested here in detail, it
     ! seems unnecessary. Tests will fail if the difference between the calculated and expected maximum is
     ! greater than 10s. Start/duration times are not tested since values agree perfectly for Uranus but not
     ! for Jupiter/Saturn satellites.
     ! Jupiter: I don't know which theory does IMCCE use for predictions, seems to be E2x3. Here I test
     ! against E2x3 for Jupiter and TASS for Saturn. GUST86 is used for Uranus.
     ! Data taken from:
     ! ftp://ftp.imcce.fr/pub/ephem/satel/phemu09/phemu09_132ts.txt
     ! ftp://ftp.imcce.fr/pub/ephem/satel/phesat09/phetri-calTASS-Xflux.txt
     ! ftp://ftp.imcce.fr/pub/ephem/satel/pheura07/pred_pheura07_GUST86_selected.txt
    */
    @Test
    @DataProvider (name = "data_mutual_events_natural_satellites")
    private Object[][] data_mutual_events_natural_satellites() {
        return new Object[][] {
            { new AstroDate(2009, 8, 16, 20, 31, 8), TARGET.JUPITER, "1 ECL 3 A", new AstroDate(2009, 8, 16, 20, 45, 57), 1662 },
            { new AstroDate(2009, 8, 18, 14, 26, 11), TARGET.JUPITER, "1 OCC 2 P", new AstroDate(2009, 8, 18, 14, 37, 59), 1358 },
            { new AstroDate(2009, 8, 19, 6, 4, 39), TARGET.JUPITER, "3 ECL 2 P", new AstroDate(2009, 8, 19, 6, 13, 48), 1110 },
            { new AstroDate(2009, 8, 22, 4, 0, 30), TARGET.JUPITER, "1 OCC 2 P", new AstroDate(2009, 8, 22, 4, 9, 32), 1058 },
            { new AstroDate(2009, 8, 26, 10, 59, 37), TARGET.JUPITER, "3 ECL 2 P", new AstroDate(2009, 8, 26, 11, 12, 29), 1577 },
            { new AstroDate(2009, 8, 26, 9, 16, 21), TARGET.JUPITER, "3 OCC 2 P", new AstroDate(2009, 8, 26, 9, 26, 55), 1283 },
            { new AstroDate(2009, 8, 28, 13, 50, 43), TARGET.JUPITER, "1 ECL 2 P", new AstroDate(2009, 8, 28, 13, 59, 32), 1095 },
            { new AstroDate(2009, 8, 28, 12, 42, 60), TARGET.JUPITER, "1 OCC 2 P", new AstroDate(2009, 8, 28, 12, 51, 55), 1096 },
            { new AstroDate(2009, 8, 30, 7, 47, 19), TARGET.JUPITER, "1 ECL 3 P", new AstroDate(2009, 8, 30, 7, 57, 59), 1348 },
            { new AstroDate(2009, 9, 1, 3, 55, 39), TARGET.JUPITER, "1 ECL 2 P", new AstroDate(2009, 9, 1, 4, 10, 39), 2069 },
            { new AstroDate(2009, 9, 1, 2, 4, 46), TARGET.JUPITER, "1 OCC 2 P", new AstroDate(2009, 9, 1, 2, 15, 23), 1320 },
            { new AstroDate(2009, 9, 2, 13, 37, 17), TARGET.JUPITER, "3 OCC 2 P", new AstroDate(2009, 9, 2, 13, 53, 38), 2020 },
            { new AstroDate(2009, 9, 4, 15, 37, 58), TARGET.JUPITER, "1 OCC 2 P", new AstroDate(2009, 9, 4, 15, 52, 8), 1815 },
            { new AstroDate(2010, 3, 17, 22, 40, 41), TARGET.JUPITER, "2 OCC 1 P", new AstroDate(2010, 3, 17, 23, 1, 43), 2727 },
            { new AstroDate(2010, 4, 4, 9, 13, 28), TARGET.JUPITER, "2 ECL 1 P", new AstroDate(2010, 4, 4, 9, 32, 9), 2147 },
            // Saturn (TASS)
            { new AstroDate(2008, 12, 19, 2, 4, 57), TARGET.SATURN, "4 OCC 5 P", new AstroDate(2008, 12, 19, 2, 12, 15), 875 },
            { new AstroDate(2008, 12, 31, 17, 53, 53), TARGET.SATURN, "4 OCC 1 T", new AstroDate(2008, 12, 31, 18, 5, 17), 1365 },
            { new AstroDate(2009, 1, 22, 5, 32, 45), TARGET.SATURN, "1 OCC 2 P", new AstroDate(2009, 1, 22, 5, 39, 9), 769 },
            { new AstroDate(2009, 1, 22, 7, 44, 34), TARGET.SATURN, "2 ECL 3 P", new AstroDate(2009, 1, 22, 7, 48, 50), 596 },
            { new AstroDate(2009, 4, 17, 7, 1, 23), TARGET.SATURN, "2 ECL 3 P", new AstroDate(2009, 4, 17, 7, 5, 47), 596 },
            { new AstroDate(2009, 6, 24, 13, 57, 6), TARGET.SATURN, "5 ECL 4 P", new AstroDate(2009, 6, 24, 14, 4, 46), 1116 },
            { new AstroDate(2009, 7, 1, 7, 31, 19), TARGET.SATURN, "4 ECL 5 P", new AstroDate(2009, 7, 1, 7, 35, 9), 601 },
            { new AstroDate(2009, 7, 10, 17, 48, 23), TARGET.SATURN, "2 OCC 3 A", new AstroDate(2009, 7, 10, 17, 53, 31), 617 },
            { new AstroDate(2009, 8, 2, 18, 54, 10), TARGET.SATURN, "6 ECL 5 T", new AstroDate(2009, 8, 2, 18, 57, 2), 597 },
            { new AstroDate(2009, 9, 1, 1, 44, 12), TARGET.SATURN, "5 ECL 3 P", new AstroDate(2009, 9, 1, 1, 51, 43), 1226 },
            { new AstroDate(2009, 12, 23, 18, 50, 13), TARGET.SATURN, "3 ECL 4 P", new AstroDate(2009, 12, 23, 18, 54, 42), 647 },
            { new AstroDate(2010, 4, 24, 15, 14, 55), TARGET.SATURN, "4 OCC 3 P", new AstroDate(2010, 4, 24, 15, 29, 59), 1805 },
            { new AstroDate(2010, 6, 18, 8, 10, 18), TARGET.SATURN, "4 OCC 3 P", new AstroDate(2010, 6, 18, 8, 19, 10), 1061 },
            // Uranus (GUST86)
            { new AstroDate(2007, 5, 3, 2, 12, 58), TARGET.URANUS, "1 OCC 3 P", new AstroDate(2007, 5, 3, 2, 22, 3), 1108 },
            { new AstroDate(2007, 5, 3, 12, 57, 18), TARGET.URANUS, "1 OCC 3 P", new AstroDate(2007, 5, 3, 13, 9, 45), 1493 },
            { new AstroDate(2007, 8, 5, 13, 42, 56), TARGET.URANUS, "4 OCC 2 P", new AstroDate(2007, 8, 5, 13, 52, 20), 1159 },
            { new AstroDate(2007, 8, 6, 0, 56, 35), TARGET.URANUS, "4 OCC 2 P", new AstroDate(2007, 8, 6, 1, 11, 39), 1807 },
            { new AstroDate(2007, 10, 21, 22, 53, 5), TARGET.URANUS, "1 ECL 2 P", new AstroDate(2007, 10, 21, 23, 11, 32), 2087 },
            { new AstroDate(2007, 11, 11, 3, 1, 53), TARGET.URANUS, "1 ECL 3 P", new AstroDate(2007, 11, 11, 3, 13, 30), 1237 },
            { new AstroDate(2007, 11, 30, 18, 32, 16), TARGET.URANUS, "3 ECL 4 P", new AstroDate(2007, 11, 30, 18, 46, 34), 1482 },
            { new AstroDate(2007, 12, 24, 18, 44, 52), TARGET.URANUS, "2 ECL 1 P", new AstroDate(2007, 12, 24, 18, 53, 54), 1010 },
            { new AstroDate(2007, 12, 25, 17, 0, 3), TARGET.URANUS, "2 ECL 1 P", new AstroDate(2007, 12, 25, 17, 24, 14), 2764 },
            { new AstroDate(2008, 2, 21, 19, 33, 41), TARGET.URANUS, "2 OCC 1 P", new AstroDate(2008, 2, 21, 19, 42, 33), 1064 },
            { new AstroDate(2008, 8, 12, 22, 12, 35), TARGET.URANUS, "1 ECL 5 T", new AstroDate(2008, 8, 12, 22, 24, 9), 1317 },
            { new AstroDate(2008, 8, 19, 9, 16, 57), TARGET.URANUS, "5 ECL 2 P", new AstroDate(2008, 8, 19, 9, 26, 44), 1024 },
            { new AstroDate(2008, 8, 23, 21, 16, 58), TARGET.URANUS, "5 OCC 2 A", new AstroDate(2008, 8, 23, 21, 27, 22), 1304 },
            { new AstroDate(2009, 6, 11, 18, 47, 57), TARGET.URANUS, "1 ECL 5 T", new AstroDate(2009, 6, 11, 19, 10, 52), 2636 },
            { new AstroDate(2009, 12, 2, 8, 44, 39), TARGET.URANUS, "1 ECL 5 T", new AstroDate(2009, 12, 2, 9, 6, 29), 2504 },
        };
    }

    @Test (dataProvider = "data_mutual_events_natural_satellites")
    public void testMutualEventsNaturalSatellites(
            final AstroDate startDate,
            final TARGET body,
            final String event,
            final AstroDate maxDate,
            final double duration)
        throws JPARSECException
    {
        time = new TimeElement(startDate, TimeElement.SCALE.TERRESTRIAL_TIME);

        int precision = 30, accuracy = 1;
        boolean all = false;

        eph.targetBody = body;
        eph.isTopocentric = false;
        eph.algorithm = EphemerisElement.ALGORITHM.JPL_DE405;
        eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_1976;
        eph.correctForEOP = false;

        MoonEvent me = new MoonEvent(time, observer, eph, new TimeElement(startDate.jd() + 1.0 / 24.0, time.timeScale),
                precision, accuracy, false, MoonEvent.JUPITER_THEORY.E2x3, MoonEvent.SATURN_THEORY.TASS);
        MoonEventElement ev[] = me.getMutualPhenomena(all);
        final TARGET first = (TARGET.SATURN == body) ? TARGET.Mimas : (TARGET.URANUS == body) ? TARGET.Ariel : TARGET.Io;

        boolean found = false;

        for (MoonEventElement mee : ev) {
            String e = "" + (mee.secondaryBody.ordinal() - first.ordinal() + 1);
            if (e.equals("-1")) e = "5"; // Miranda is 5 according to IMCCE
            if (mee.eventType == MoonEventElement.EVENT.ECLIPSED) e += " ECL ";
            if (mee.eventType == MoonEventElement.EVENT.OCCULTED) e += " OCC ";

            if ((mee.mainBody.ordinal() - first.ordinal() + 1) == -1) {
                e += "5"; // Miranda is 5 following IMCCE
            } else {
                e += "" + (mee.mainBody.ordinal() - first.ordinal() + 1);
            }

            if (event.startsWith(e)) {
                found = true;
                //System.out.println(mee.startTime + '/' + mee.details);
                AstroDate start = new AstroDate(mee.startTime);
                //AstroDate max = new AstroDate((mee.startTime + mee.endTime) / 2.0);
                AstroDate max = new AstroDate(Double.parseDouble(FileIO.getField(2, mee.details, ",", true)));
                assertEquals(max.jd(), maxDate.jd(), 100);

                if (mee.startTime != 0) {
                    double dur = (mee.endTime - mee.startTime) * Constant.SECONDS_PER_DAY;
                    assertEquals(dur, duration, 10);
                }

                break;
            }
        }

        assertTrue(found, "Could not find the event");
    }

    /*
     ! ***
     ! 19 - Test for apparent ephemerides as seen from other bodies.
     ! Input values are year, month, day, hour, minute, second, time scale, target body, origin body (lon, lat).
     ! Output values are topocentric RA and DEC, and azimuth and elevation.
     ! Apparent, topocentric calculations for equinox of date are done using dynamical equinox J2000 as frame,
     ! IAU_1976 as reduction algorithms, and Moshier as calculation algorithm.
     */
    @Test
    @DataProvider (name = "data_ephemerides_from_other_planets")
    private Object[][] data_ephemerides_from_other_planets() {
        return new Object[][] {
            { new AstroDate(2010, 1, 6, 0, 0, 0), TimeElement.SCALE.UNIVERSAL_TIME_UTC, TARGET.SUN, TARGET.MARS, 184.702, -14.64,
                "10h 58m 08.2425s", "13\u00b0 43' 57.384\"", "-75\u00b0 28' 38.838\"", "1\u00b0 10' 25.685\"" },
        };
    }

    @Test (dataProvider = "data_ephemerides_from_other_planets")
    public void testEphemeridesFromOtherPlanets (
            final AstroDate date,
            final TimeElement.SCALE scale,
            final TARGET body,
            final TARGET origin,
            final double longitude,
            final double latitude,
            final String expectedRA,
            final String expectedDEC,
            final String expectedAzimuth,
            final String expectedElevation)
        throws JPARSECException
    {
        time = new TimeElement(date, scale);

        EphemerisElement eph = new EphemerisElement(body, EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_1976,
                EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000, EphemerisElement.ALGORITHM.MOSHIER);

        observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement(origin.getEnglishName(), origin,
                new LocationElement(longitude * Constant.DEG_TO_RAD, latitude * Constant.DEG_TO_RAD, 0.0)));

        EphemElement pephem = Ephem.getEphemeris(time, observer, eph, false);

        String actualRA = Functions.formatRA(pephem.rightAscension);
        String actualDEC = Functions.formatDEC(pephem.declination);
        String actualAz = Functions.formatAngle(pephem.azimuth, 3);
        String actualEl = Functions.formatAngle(pephem.elevation, 3);

        assertEquals(actualRA, expectedRA);
        assertEquals(actualDEC, expectedDEC);
        assertEquals(actualAz, expectedAzimuth);
        assertEquals(actualEl, expectedElevation);
    }
}
