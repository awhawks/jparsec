package jparsec.graph.chartRendering.frame;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.graph.chartRendering.*;
import jparsec.graph.chartRendering.RenderSatellite;
import jparsec.io.image.Picture;
import jparsec.observer.City;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;
import jparsec.util.Translate;

public class EclipseRenderingTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("EclipseRendering test");

        try {
            // Translate.setDefaultLanguage(LANGUAGE.SPANISH);

            // Solar eclipse
            AstroDate astro = new AstroDate(2005, AstroDate.OCTOBER, 3, 9, 0, 0);
            // Lunar eclipse
            //AstroDate astro = new AstroDate(2011, AstroDate.JUNE, 15, 20, 0, 0);

            // Main objects
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            ObserverElement observer = ObserverElement.parseCity(City.findCity("Madrid"));
            EphemerisElement eph = new EphemerisElement(Target.TARGET.SUN, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2009,
                    EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000, EphemerisElement.ALGORITHM.MOSHIER);

            // Set some properties in other classes used by RenderEclipse
            jparsec.graph.chartRendering.RenderPlanet.MAXIMUM_TEXTURE_QUALITY_FACTOR = 2f; // Better quality
            RenderSatellite.ALLOW_SPLINE_RESIZING = false; // Improve performance
            // Following line is not required since it is used internally in RenderEclipse class,
            // but I put it here to show how to improve performance when calculating ephemerides
            eph.correctForEOP = eph.correctForPolarMotion = eph.preferPrecisionInEphemerides = false; // Improve performance

            // Set map and anaglyph properties
            SatelliteRenderElement.PLANET_MAP map = SatelliteRenderElement.PLANET_MAP.MAP_FLAT;
            // map.zoomFactor = 1.0f;
            map.EarthMapSource = SatelliteRenderElement.PLANET_MAP.EARTH_MAP_POLITICAL;
            Graphics.ANAGLYPH_COLOR_MODE colorMode = Graphics.ANAGLYPH_COLOR_MODE.NO_ANAGLYPH;
            boolean isSolarEclipse = true;
            // Following line sets title to "Solar eclipse" for a solar eclipse, and "Lunar eclipse" for
            // a lunar one. It also translates it in case Spanish is selected
            String title = Translate.translate(isSolarEclipse ? "Solar eclipse" : "Lunar eclipse");

            // Create the chart
            EclipseRendering eclRender = new EclipseRendering(time, observer, eph, isSolarEclipse, map, 600, 800, colorMode, title);
            Picture pic = new Picture(eclRender.createBufferedImage());
            pic.show(title);
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
