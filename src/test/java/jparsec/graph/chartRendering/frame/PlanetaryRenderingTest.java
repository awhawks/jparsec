package jparsec.graph.chartRendering.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import jparsec.astronomy.TelescopeElement;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.graph.DataSet;
import jparsec.graph.chartRendering.Graphics;
import jparsec.graph.chartRendering.PlanetRenderElement;
import jparsec.io.ConsoleReport;
import jparsec.io.FileIO;
import jparsec.io.image.Picture;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;

public class PlanetaryRenderingTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("PlanetaryRendering test");

        Target.TARGET target = Target.TARGET.JUPITER;
        AstroDate astro = new AstroDate(2004, AstroDate.MARCH, 27, 23, 0, 0);
        // Triple eclipse in Jupiter: start 23h, duration 13h
        //AstroDate astro = new AstroDate(2004, AstroDate.MARCH, 28, 8, 2, 30);
        // Test of drawing partially a satellite while it is being occulted: Io and Ganymede simultaneously being occulted, while Europa transiting
        //AstroDate astro = new AstroDate(2012, AstroDate.JANUARY, 21, 22, 20, 0); // Test also minute 27

        //TARGET target = TARGET.SATURN;
        //AstroDate astro = new AstroDate(2011, AstroDate.JUNE, 15, 0, 0, 0); // Test proyeccion sombras
        //AstroDate astro = new AstroDate(2010, AstroDate.JUNE, 15, 0, 0, 0); // Test opacidad anillos
        //AstroDate astro = new AstroDate(2013, AstroDate.JUNE, 15, 0, 0, 0); // Test anillos media apertura
        //AstroDate astro = new AstroDate(2009, AstroDate.FEBRUARY, 24, 12, 9, 30); // Test Titan transit

        TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
        EphemerisElement eph = new EphemerisElement(target, EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                EphemerisElement.FRAME.ICRF);
        PlanetRenderElement render = new PlanetRenderElement(1920, 1080, false, true, true, false, true, false);
        render.highQuality = true;
        TelescopeElement telescope = TelescopeElement.SCHMIDT_CASSEGRAIN_20cm;
        telescope.ocular.focalLength = 0.9f;
        if (target == Target.TARGET.SATURN)
            telescope.ocular.focalLength = 0.5f;
        telescope.invertHorizontal = telescope.invertVertical = false;

        render.anaglyphMode = Graphics.ANAGLYPH_COLOR_MODE.DUBOIS_RED_CYAN;
        render.anaglyphMode.setEyeSeparation(1f); // > 0.5 To exagerate the 3d effect in Dubois method

        render.telescope = telescope;
        try {
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            System.out.println("JD " + TimeScale.getJD(time, observer, eph, TimeElement.SCALE.TERRESTRIAL_TIME));
            //System.out.println("Field (deg) "+(float)(telescope.getField()*Constant.RAD_TO_DEG));

            int n0 = 0;
            int nmax = 13 * 24;
            int n = ("" + nmax).length();

            for (int i = 0; i < nmax; i++) {
                if (i >= n0) {
                    PlanetaryRendering renderPlanet = new PlanetaryRendering(time, observer, eph, render, "Planet rendering");
                    //renderPlanet.showRendering();
                    BufferedImage img = renderPlanet.createBufferedImage();
                    Graphics2D g = img.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
                    String date = time.toString();
                    g.drawString(date, 10, 30);
                    Picture pic = new Picture(img);

                    String ni = "" + i;
                    if (ni.length() < n) {
                        ni = FileIO.addSpacesBeforeAString(ni, n);
                        ni = DataSet.replaceAll(ni, " ", "0", true);
                    }
                    pic.write("/mnt/sdc2/" + ni + ".png");
                    if (i == n0) pic.show("My little rendering");
                }

                time.add(2.5 / 1440.0);
            }

            ConsoleReport.fullEphemReportToConsole(render.ephem);
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
