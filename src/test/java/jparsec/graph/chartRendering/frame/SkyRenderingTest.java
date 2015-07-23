package jparsec.graph.chartRendering.frame;

import java.awt.Color;
import jparsec.astronomy.Constellation;
import jparsec.astronomy.TelescopeElement;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.graph.chartRendering.PlanetRenderElement;
import jparsec.graph.chartRendering.Projection;
import jparsec.graph.chartRendering.SkyRenderElement;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;
import jparsec.util.Translate;

public class SkyRenderingTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);

        double values[] = new double[] {
            //2011, AstroDate.AUGUST, 20, 8, 45, 1200, 1000, 0.5, -20
            1989, AstroDate.MARCH, 3, 0, 0, 3072, 8192, 18.855, -22.12
        };

        int yMargin = 0;

        AstroDate astro = new AstroDate((int) values[0], (int) values[1], (int) values[2], (int) values[3], (int) values[4], 0);
        TimeElement time = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);
        EphemerisElement eph = new EphemerisElement(Target.TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
            EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
            EphemerisElement.FRAME.ICRF);
        eph.preferPrecisionInEphemerides = false;

        PlanetRenderElement render = new PlanetRenderElement(false, true, true, false, false);
        TelescopeElement telescope = TelescopeElement.OBJECTIVE_50mm_f1_4;
        telescope.ocular.focalLength = 0.07f;

        SkyRenderElement sky = new SkyRenderElement(jparsec.astronomy.CoordinateSystem.COORDINATE_SYSTEM.EQUATORIAL,
                Projection.PROJECTION.STEREOGRAPHICAL, 0, 0.0, (int) values[5], (int) values[6], render, telescope);

//        sky.drawOcularFieldOfView = false;
//        sky.drawCoordinateGrid = true;
//        sky.drawLeyend = true;
//        sky.drawConstellationContours = true;
//        sky.drawConstellationContoursColor = Color.BLUE;
//        sky.drawConstellationNames = true;
//        sky.drawConstellationNamesColor = Color.BLACK;
        sky.drawConstellationNamesType = Constellation.CONSTELLATION_NAME.SPANISH;
//        sky.drawCoordinateGridColor = Color.BLACK;
//        sky.drawCoordinateGridEclipticColor = Color.RED;
//        sky.drawCoordinateGridEcliptic = true;
//        sky.drawDeepSkyObjects = true;
        sky.drawDeepSkyObjectsColor = Color.GREEN.getRGB();
//        sky.drawDeepSkyObjectsLabels = true;
//        sky.drawDeepSkyObjectsOnlyMessier = false;
//        sky.drawNebulaeContours = true;
//        sky.drawMilkyWayContoursColor = Color.LIGHT_GRAY;
//        sky.drawNebulaeContoursColor = Color.GRAY;
        sky.drawObjectsLimitingMagnitude = 12.5f;
        sky.drawPlanetsMoonSun = true;
        sky.drawSpaceProbes = false;
//        sky.drawStars = true;
//        sky.drawStarsColor = Color.BLACK;
//        sky.drawStarsColors = false;
        sky.drawStarsLabels = SkyRenderElement.STAR_LABELS.ONLY_PROPER_NAME_SPANISH;
//        sky.drawStarsSymbols = true;
        sky.drawStarsGreekSymbols = true;
        sky.drawStarsGreekSymbolsOnlyIfHasProperName = true;
        sky.drawTransNeptunianObjects = true;
        sky.drawStarsLimitingMagnitude = 12.5f;
        sky.drawStarsLabelsLimitingMagnitude = sky.drawStarsLimitingMagnitude;
        sky.drawArtificialSatellites = false;
        sky.drawAsteroids = false;
        sky.drawComets = false;

        sky.drawConstellationLimits = true;
        sky.drawDeepSkyObjects = true;
        sky.drawSkyCorrectingLocalHorizon = false;
        sky.drawSkyBelowHorizon = true;
        sky.drawFastLabels = SkyRenderElement.SUPERIMPOSED_LABELS.AVOID_SUPERIMPOSING_SOFT;
        sky.drawFastLabelsInWideFields = false;
        sky.setColorMode(SkyRenderElement.COLOR_MODE.BLACK_BACKGROUND);

        CityElement city = City.findCity("Madrid");
        ObserverElement observer = ObserverElement.parseCity(city);
        /*LocationElement loc0 = RenderSky.searchStar("Arcturus", time, observer, eph);
        //LocationElement loc0 = new LocationElement(SiderealTime.apparentSiderealTime(time, observer, eph), 0, 1.0);
        //RenderSky.searchStar("Arcturus", time, observer, eph);
        System.out.println("RA "+Functions.formatRA(loc0.getLongitude()));
        System.out.println("DEC "+Functions.formatDEC(loc0.getLatitude()));
        loc0 = CoordinateSystem.equatorialToHorizontal(loc0, time, observer, eph);
        System.out.println("Az "+Functions.formatAngle(loc0.getLongitude(), 3));
        System.out.println("Ele "+Functions.formatAngle(loc0.getLatitude(), 3));
        */
        sky.centralLongitude = values[7] * 15.0 * Constant.DEG_TO_RAD;
        sky.centralLatitude = values[8] * Constant.DEG_TO_RAD; //Math.PI * 0.25;

        SkyRendering skyRender = new SkyRendering(time, observer, eph, sky, "Sky render", yMargin);
        skyRender.showRendering();
        //skyRender.exportAsEPSFile("/home/alonso/prueba.eps");
        //Picture p = new Picture(skyRender.createBufferedImage());
        //p.show((int) values[5], (int) values[6], "Sky render", true, true, true);
        JPARSECException.showWarnings();

        //Serialization.writeObject(skyRender, "/home/alonso/sky");
        System.out.println("Sky Render Test");
        System.out.println("FIELD: " + (telescope.getField() * Constant.RAD_TO_DEG));
    }
}
