package jparsec.graph.chartRendering;

import jparsec.astronomy.TelescopeElement;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.io.ConsoleReport;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;

public class RenderPlanetTest {
    /**
     * For unit testing only.
     */
    public static void main(String arg[]) throws Exception {
        System.out.println("RenderPlanet Test");

        // Triple eclipse in Jupiter
        AstroDate astro = new AstroDate(2004, AstroDate.MARCH, 28, 8, 2, 30);
        // Test of drawing partially a satellite while it is being occulted: Io and Ganymede simultaneosly being occulted, while Europa transiting
        //AstroDate astro = new AstroDate(2012, AstroDate.JANUARY, 21, 22, 20, 0); // Test also minute 27

        TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
        EphemerisElement eph = new EphemerisElement(Target.TARGET.JUPITER, EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                EphemerisElement.FRAME.ICRF);

        PlanetRenderElement render = new PlanetRenderElement(800, 600, true, true, true, false, false, false);

        TelescopeElement telescope = TelescopeElement.SCHMIDT_CASSEGRAIN_20cm;
        telescope.ocular.focalLength = 1f;
        render.telescope = telescope;

        CityElement city = City.findCity("Madrid");
        ObserverElement observer = ObserverElement.parseCity(city);
        System.out.println("JD " + TimeScale.getJD(time, observer, eph, TimeElement.SCALE.TERRESTRIAL_TIME));

        RenderPlanet renderPlanet = new RenderPlanet(time, observer, eph, render);
        Graphics g = new AWTGraphics(800, 600, false, false);
        renderPlanet.renderize(g);

        jparsec.io.image.Picture pic = new jparsec.io.image.Picture((java.awt.image.BufferedImage) g.getRendering());
        pic.show("");

        ConsoleReport.fullEphemReportToConsole(render.ephem);
    }
}
