package jparsec.ephem.planets;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.io.image.Picture;
import jparsec.math.Constant;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;

public class OrbitalElementTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        AstroDate astro = new AstroDate(1990, 10, 5.0);
        OrbitalElement orbit = new OrbitalElement("Encke", 2.2091404, 186.24444 * Constant.DEG_TO_RAD, 0.8502196, 0.0,
                334.04096 * Constant.DEG_TO_RAD, 11.93911 * Constant.DEG_TO_RAD, astro.jd(), 0.0, Constant.B1950, 0.0, 0.0);

        Picture pp = new Picture(orbit.getOrbitImage(600, 600, 0.5, astro.jd()));
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
        orbit2.FK4_to_FK5();
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
    }
}
