package jparsec.ephem.planets;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.io.image.Picture;
import jparsec.math.Constant;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class OrbitalElementTest {
    /**
     * Test program.
     *
     * @param args Not used.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        AstroDate astro = new AstroDate(1991, 10, 5.0);
        OrbitalElement orbit = new OrbitalElement("Encke", 2.2091404, 186.24444 * Constant.DEG_TO_RAD, 0.8502196, 0.0,
                334.04096 * Constant.DEG_TO_RAD, 11.93911 * Constant.DEG_TO_RAD,
                new AstroDate(1990, 10, 5.0).jd(), 0.0, Constant.B1950, 0.0, 0.0);
        //orbit = OrbitEphem.getOrbitalElementsOfComet(OrbitEphem.getIndexOfComet("2014 Q1"));
        Picture pp = new Picture(orbit.getOrbitImage(orbit.name, 600, 600, 0.65, astro.jd(), true, true));
        pp.show("");

        OrbitalElement orbit1 = orbit.clone();
        orbit1.changeToEquinox(Constant.J2000);
        System.out.println(Functions.formatAngleAsDegrees(orbit1.inclination, 5));
        System.out.println(Functions.formatAngleAsDegrees(orbit1.ascendingNodeLongitude, 5));
        System.out.println(Functions.formatAngleAsDegrees(orbit1.argumentOfPerihelion, 5));
        // Should be ... 11.94524  -25.24994  186.23352

        OrbitalElement orbit2 = orbit.clone();
        orbit2.referenceFrame = EphemerisElement.FRAME.FK4;
        orbit2.referenceTime = Constant.B1950;
        //orbit2.FK4_to_FK5();
        System.out.println(Functions.formatAngleAsDegrees(orbit2.inclination, 5));
        System.out.println(Functions.formatAngleAsDegrees(orbit2.ascendingNodeLongitude, 5));
        System.out.println(Functions.formatAngleAsDegrees(orbit2.argumentOfPerihelion, 5));
        // Should be ... 11.94521  -25.24957  186.23327

        astro = new AstroDate(1986, 2, 9.45891);
        OrbitalElement orbit3 = new OrbitalElement("Halley", 17.9400782, 111.84644 * Constant.DEG_TO_RAD, 0.96727426, 0.0,
                0 * Constant.DEG_TO_RAD, 0 * Constant.DEG_TO_RAD, astro.jd(), 0.01297082 * Constant.DEG_TO_RAD, Constant.B1950, 0.0, 0.0);
        System.out.println(TimeFormat.formatJulianDayAsDateAndTime(orbit3.getNextPassThroughMeanAscendingNode(), TimeElement.SCALE.TERRESTRIAL_TIME));
        // Nov 9 1985, 3:49
        System.out.println(TimeFormat.formatJulianDayAsDateAndTime(orbit3.getNextPassThroughMeanDescendingNode(), TimeElement.SCALE.TERRESTRIAL_TIME));
        // Mar 10 1986, 8:52

        /*
        Translate.setDefaultLanguage(LANGUAGE.SPANISH);
        AstroDate init = new AstroDate();
        AstroDate end = init.clone();
        end.add(365.25 * 40);
        orbit3.magnitudeModel = MAGNITUDE_MODEL.COMET_gk;
        CreateChart ch = orbit3.getLightCurveChart(
                new TimeElement(init, SCALE.BARYCENTRIC_DYNAMICAL_TIME),
                new TimeElement(end, SCALE.BARYCENTRIC_DYNAMICAL_TIME),
                new ObserverElement(),
                new EphemerisElement(), 200);
        ch.showChartInJFreeChartPanel();
        CreateChart ch2 = orbit3.getDistanceChart(
                new TimeElement(init, SCALE.BARYCENTRIC_DYNAMICAL_TIME),
                new TimeElement(end, SCALE.BARYCENTRIC_DYNAMICAL_TIME),
                new ObserverElement(),
                new EphemerisElement(), 200);
        ch2.showChartInJFreeChartPanel();

        Picture pic1 = new Picture(ChartElement.getSimpleChart(ch.getChartElement()));
        Picture pic2 = new Picture(ChartElement.getSimpleChart(ch2.getChartElement()));
        pic1.show("1");
        pic2.show("2");
        */
    }

    private OrbitalElement orbit;

    @BeforeMethod
    public void beforeMethod () throws Exception {
        AstroDate astro = new AstroDate(1990, 10, 5.0);
        orbit = new OrbitalElement("Encke", 2.2091404, 186.24444 * Constant.DEG_TO_RAD, 0.8502196, 0.0,
            334.04096 * Constant.DEG_TO_RAD, 11.93911 * Constant.DEG_TO_RAD, astro.jd(), 0.0, Constant.B1950, 0.0, 0.0);
    }

    @Test
    public void testOrbit_changeToEquinox () throws Exception {
        orbit.changeToEquinox(Constant.J2000);

        assertEquals(Functions.formatAngleAsDegrees(orbit.inclination,            5), "11.94524");
        assertEquals(Functions.formatAngleAsDegrees(orbit.ascendingNodeLongitude, 5), "-25.24994");
        assertEquals(Functions.formatAngleAsDegrees(orbit.argumentOfPerihelion,   5), "186.23352");
    }

    @Test
    public void testOrbit_changeReference () throws Exception {
        orbit.referenceFrame = EphemerisElement.FRAME.FK4;
        orbit.referenceTime = Constant.B1950;
        orbit.FK4_to_FK5();

        assertEquals(Functions.formatAngleAsDegrees(orbit.inclination,            5),  "11.94521");
        assertEquals(Functions.formatAngleAsDegrees(orbit.ascendingNodeLongitude, 5), "-25.24957");
        assertEquals(Functions.formatAngleAsDegrees(orbit.argumentOfPerihelion,   5), "186.23327");
    }
}
