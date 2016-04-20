package jparsec.graph;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import jparsec.astronomy.Constellation;
import jparsec.astronomy.CoordinateSystem;
import jparsec.astronomy.TelescopeElement;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.graph.chartRendering.Graphics;
import jparsec.graph.chartRendering.PlanetRenderElement;
import jparsec.graph.chartRendering.Projection;
import jparsec.graph.chartRendering.RenderPlanet;
import jparsec.graph.chartRendering.SkyRenderElement;
import jparsec.graph.chartRendering.frame.SkyRendering;
import jparsec.io.FileFormatElement;
import jparsec.io.FileIO;
import jparsec.io.ReadFile;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.Translate;

public class SkyChartTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("SkyChart test");

        //Configuration.USE_DISK_FOR_DATABASE = true;
        Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);
        //Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH = "/home/alonso/eclipse/libreria_jparsec/ephem/test";
        //FastMath.ACCURATE_MODE = false;
        //FastMath.setMaximumNumberOfAngles(FastMath.getMaximumNumberOfAngles()*10);
        //TextLabel.setRenderUnicodeAsImages(true);

        double values[] = new double[] {
            //2000, AstroDate.JANUARY, 1, 6, 45, 800, 600, 17, 0
            //2011, AstroDate.JUNE, 15, 21, 21, 1200, 800, 12, 30 // Lunar eclipse (Santa Cruz das Flores) 21:40, reykjavik +2.5h
            // 2009, AstroDate.APRIL, 13, 8, 30, 1200, 800, 12, 90 // Titan shadow transit
            //2004, AstroDate.MARCH, 28, 8, 3, 1200, 800, 12, 90 // Jupiter triple eclipse
            //2012, 7, 15, 1, 35, 800, 600, 17, 0 // Occultation of Jupiter by Moon
            //2012, 9, 13, 5, 15.5, 800, 600, 17, 0 // Partial occultation of Sun by Phobos on Mars
            //2005, 11, 27, 2, 14, 800, 600, 17, 0 // Eclipse of Phobos on Mars
            2013, AstroDate.AUGUST, 15, 23, 0, 1200, 800, 12, 90 //
        };

        int yMargin = 0;

        AstroDate astro = new AstroDate(); //(int) values[0], (int) values[1], (int) values[2], (int) values[3], (int) values[4], 60.0*(values[4]-(int) values[4]));
        TimeElement time = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);
        //TimeElement time = new TimeElement();
        EphemerisElement eph = new EphemerisElement(Target.TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000,
                EphemerisElement.ALGORITHM.MOSHIER);
        eph.preferPrecisionInEphemerides = false;
        eph.correctForEOP = false;
        eph.correctForPolarMotion = false;

        PlanetRenderElement render = new PlanetRenderElement(true, true, true, false, false);
        //render.highQuality = true;
        TelescopeElement telescope = TelescopeElement.HUMAN_EYE;
        //telescope.ocular.focalLength = 300f;
        /*
        try {
            telescope.attachCCDCamera(CCDElement.getCCD("TouCam"));
        } catch (JPARSECException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        telescope.invertHorizontal = true;
        telescope.invertVertical = true;
        */

        SkyRenderElement sky = new SkyRenderElement(CoordinateSystem.COORDINATE_SYSTEM.HORIZONTAL,
                Projection.PROJECTION.STEREOGRAPHICAL, 0, 0.0, (int) values[5], (int) values[6], render, telescope);

        //sky.anaglyphMode = Graphics.ANAGLYPH_COLOR_MODE.TRUE_3D_MODE_LEFT_RIGHT_HALF_WIDTH;
        sky.setColorMode(SkyRenderElement.COLOR_MODE.BLACK_BACKGROUND); //.WHITE_BACKGROUND_SIMPLE_GREEN_RED_OR_RED_CYAN_ANAGLYPH);

//		sky.drawOcularFieldOfView = false;
//		sky.drawCoordinateGrid = true;
//		sky.drawLeyend = true;
//		sky.drawConstellationContours = true;
//		sky.drawConstellationContoursColor = Color.BLUE;
//		sky.drawConstellationNames = true;
//		sky.drawConstellationNamesColor = Color.BLACK;
        sky.drawConstellationNamesType = Constellation.CONSTELLATION_NAME.SPANISH;
//		sky.drawCoordinateGridColor = Color.BLACK;
//		sky.drawCoordinateGridEclipticColor = Color.RED;
//		sky.drawCoordinateGridEcliptic = true;
//		sky.drawDeepSkyObjects = true;
        //sky.drawDeepSkyObjectsColor = Color.GREEN.getRGB();
//		sky.drawDeepSkyObjectsLabels = true;
//		sky.drawDeepSkyObjectsOnlyMessier = false;
//		sky.drawNebulaeContours = true;
//		sky.drawMilkyWayContoursColor = Color.LIGHT_GRAY;
//		sky.drawNebulaeContoursColor = Color.GRAY;
        sky.drawObjectsLimitingMagnitude = 12.5f;
        sky.drawMinorObjectsLimitingMagnitude = sky.drawObjectsLimitingMagnitude;
        sky.drawPlanetsMoonSun = true;
        sky.drawSpaceProbes = false;
//		sky.drawStars = true;
//		sky.drawStarsColor = Color.BLACK;
//		sky.drawStarsColors = false;
        sky.drawStarsLabels = SkyRenderElement.STAR_LABELS.ONLY_PROPER_NAME_SPANISH;
//		sky.drawStarsSymbols = true;
        sky.drawStarsGreekSymbols = true;
        sky.drawStarsGreekSymbolsOnlyIfHasProperName = false;
        sky.drawTransNeptunianObjects = false;
        sky.drawStarsLimitingMagnitude = 16f;
        sky.drawStarsLabelsLimitingMagnitude = sky.drawStarsLimitingMagnitude - 2;
        sky.drawArtificialSatellites = false;
        sky.drawAsteroids = false;
        sky.drawComets = false;
        sky.drawStarsSymbols = true;

        sky.drawConstellationLimits = true;
        sky.drawDeepSkyObjects = true;
        sky.drawSkyBelowHorizon = false;
        sky.drawSkyCorrectingLocalHorizon = true;
        sky.drawFastLabels = SkyRenderElement.SUPERIMPOSED_LABELS.AVOID_SUPERIMPOSING_SOFT;
        sky.drawFastLabelsInWideFields = false;
        sky.fillMilkyWay = true;
        sky.drawSuperNovaAndNovaEvents = true;
        sky.drawMilkyWayContoursWithTextures = SkyRenderElement.MILKY_WAY_TEXTURE.NO_TEXTURE;
        //sky.drawMilkyWayContours = false;
        //sky.drawFaintStarsTimeOut = 50;

        sky.drawClever = true;
        //sky.drawOcularFieldOfView = true;
        sky.drawStarsPositionAngleInDoubles = true;

        // Fast mode
        //sky.drawDeepSkyObjects = false;
        //sky.drawStars = false;
        sky.drawFastLinesMode = SkyRenderElement.FAST_LINES.GRID_AND_MILKY_WAY_AND_CONSTELLATIONS;
        sky.drawFastLinesMode.setFastOvals(true);
        sky.drawConstellationContoursMarginBetweenLineAndStar = 30;
        RenderPlanet.ALLOW_SPLINE_RESIZING = false;

        sky.drawStarsLimitingMagnitude = 8.5f;
        sky.drawObjectsLimitingMagnitude = 9f;
        sky.drawMinorObjectsLimitingMagnitude = sky.drawObjectsLimitingMagnitude;
        sky.fillMilkyWay = true;
        sky.drawConstellationLimits = false;
        sky.planetRender.textures = true;
        sky.drawFaintStars = true;
        sky.drawStarsRealistic = SkyRenderElement.REALISTIC_STARS.STARRED;
        sky.drawLeyend = SkyRenderElement.LEYEND_POSITION.TOP;
        sky.drawDeepSkyObjectsAllMessierAndCaldwell = true;
        //sky.drawHorizonTexture = HORIZON_TEXTURE.VELETA_30m;
        sky.drawOcularFieldOfView = false;

        // SUPER FAST MODE TEST *3.5
        SkyRenderElement skyFast = sky.clone();
        skyFast.drawStarsLimitingMagnitude = 6.5f;
        skyFast.drawStarsLabelsLimitingMagnitude = sky.drawStarsLimitingMagnitude - 2;
        skyFast.drawObjectsLimitingMagnitude = 5.5f;
        skyFast.drawMinorObjectsLimitingMagnitude = skyFast.drawObjectsLimitingMagnitude;
        //skyFast.drawDeepSkyObjectsAllMessier = false;
        skyFast.fillMilkyWay = false;
        skyFast.planetRender.textures = false;
        skyFast.drawFastLinesMode = SkyRenderElement.FAST_LINES.GRID_AND_MILKY_WAY_AND_CONSTELLATIONS;
        skyFast.drawFastLinesMode.setFastOvals(true);
        skyFast.drawFastLabels = SkyRenderElement.SUPERIMPOSED_LABELS.FAST;
        skyFast.drawFastLabelsInWideFields = true;
        skyFast.drawSuperNovaAndNovaEvents = false;
        skyFast.drawMilkyWayContoursWithTextures = SkyRenderElement.MILKY_WAY_TEXTURE.NO_TEXTURE;
        skyFast.drawDeepSkyObjectsTextures = false;
        skyFast.drawCoordinateGrid = false;
        skyFast.drawNebulaeContours = false;
        skyFast.drawMilkyWayContours = false;

        CityElement city = City.findCity("Madrid");
        ObserverElement observer = ObserverElement.parseCity(city);

        String obj = "SATURN";
/*			// Move observer towards Curiosity landing site on Mars
            double jd = TimeScale.getJD(time, observer, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
			Translate.setDefaultLanguage(Translate.LANGUAGE.ENGLISH);
			double[] pos = Ephem.eclipticToEquatorial(PlanetEphem.getHeliocentricEclipticPositionJ2000(jd, TARGET.valueOf(obj)), Constant.J2000, eph);
			LocationElement loc = LocationElement.parseRectangularCoordinates(pos);
			loc.setRadius(loc.getRadius()-0.01*0);
			pos = loc.getRectangularCoordinates();
			pos[0] += 0.009;
			pos[1] += 0.003;
			pos[2] += 0.25;
			pos = new double[] {pos[0], pos[1], pos[2], 0, 0, 0};
			observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("Close to "+obj, pos));
			observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("Mars (Gale crater)", TARGET.MARS,
					new LocationElement(-137.4417 * Constant.DEG_TO_RAD, -4.5895*Constant.DEG_TO_RAD, 1.0)));
			observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("Mars (Gusev crater)", TARGET.MARS,
					new LocationElement(-175.4785 * Constant.DEG_TO_RAD, -14.5718*Constant.DEG_TO_RAD, 1.0)));
			observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("Sol", TARGET.SUN,
					new LocationElement(94.3 * Constant.DEG_TO_RAD, -6.7*Constant.DEG_TO_RAD, 1.0)));
			observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("Mare Serenitatis", TARGET.Moon,
					new LocationElement(18 * Constant.DEG_TO_RAD, 26*Constant.DEG_TO_RAD, 1.0)));
*/

        // Move observer towards obj, at 0.3 AU of distance
/*			LocationElement loc0 = RenderSky.searchStar("Arcturus", time, observer, eph);
//			LocationElement loc0 = new LocationElement(SiderealTime.apparentSiderealTime(time, observer, eph), 0, 1.0); //RenderSky.searchStar("Arcturus", time, observer, eph);
			System.out.println("RA "+Functions.formatRA(loc0.getLongitude()));
			System.out.println("DEC "+Functions.formatDEC(loc0.getLatitude()));
			loc0 = CoordinateSystem.equatorialToHorizontal(loc0, time, observer, eph);
			System.out.println("Az "+Functions.formatAngle(loc0.getLongitude(), 3));
			System.out.println("Ele "+Functions.formatAngle(loc0.getLatitude(), 3));
*/
        sky.centralLongitude = values[7] * 15.0 * Constant.DEG_TO_RAD;
        sky.centralLatitude = values[8] * Constant.DEG_TO_RAD; //Math.PI * 0.25;

/*			double jd_bdt = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
			TrajectoryElement path = new TrajectoryElement(OBJECT.PLANET, TARGET.JUPITER.getName(), jd_bdt,
					jd_bdt + 20, 1, true, TrajectoryElement.LABELS.DAY_MONTH_ABBREVIATION, 2, true, true);
			path.apparentObjectName = ""; //targets[(int) values[1]];
			path.drawPathFont = FONT.DIALOG_ITALIC_13;
			path.drawPathColor1 = Color.WHITE.getRGB(); //Color.BLACK;
			path.drawPathColor2 = Color.RED.getRGB();

			sky.trajectory = new TrajectoryElement[] {path};
			//sky.poleAngle = (float) Constant.PI_OVER_FOUR;
*/

        try {
        String contents[] = DataSet.arrayListToStringArray(ReadFile.readResource(FileIO.DATA_SKY_DIRECTORY + "iram-J2000.sou"));
        sky.addExternalCatalog(Translate.translate("IRAM catalog"), Translate.translate("Radiosource"), Color.RED.getRGB(), contents, FileFormatElement.IRAM_SOU_FORMAT);

        contents = DataSet.arrayListToStringArray(ReadFile.readResource(FileIO.DATA_ORBITAL_ELEMENTS_DIRECTORY + "extrasolarPlanets.txt"));
        sky.addExternalCatalog(Translate.translate("Extrasolar planets"), Translate.translate("Extrasolar planets"), Color.CYAN.getRGB(), contents, "  ", true);

        for (int i = 0; i < sky.getNumberOfExternalCatalogs(); i++) {
            sky.drawExternalCatalogs[i] = false;
        }
        } catch (Exception exc) {
        	exc.printStackTrace();
        }

/*
			skyFast.drawExternalCatalogs = true;
			skyFast.addExternalCatalog("IRAM catalog", "Radiosource", Color.RED.getRGB(), "/home/alonso/colaboraciones/Pablo/2007/wp5000/catalogs/iram-J2000.sou",
					FileFormatElement.IRAM_SOU_FORMAT);
*/

        SkyRendering skyRender = new SkyRendering(time, observer, eph, sky, "Sky render", yMargin);
        int w = (int) values[5], h = (int) values[6];
/*
			// For full screen mode
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			w = d.width;
			h = d.height;
*/
        final SkyChart sc = new SkyChart(w, h, skyRender, true, false, 5, false); //skyFast);

/*			sc.chartForDragging = sky.clone();
			sc.chartForDragging.drawMilkyWayContoursWithTextures = false;
			sc.chartForDragging.drawConstellationLimits = false;
*/

/*			sc.addTelescopeMark("IRAM 30m", new ExternalTelescope(
					"IRAM", new double[] {
							30 * Constant.ARCSEC_TO_RAD,
							10 * Constant.ARCSEC_TO_RAD
					}, new String[] {"3mm", "1mm"}, "/home/alonso/pos.txt",
					POSITION_TYPE.HORIZONTAL, observer
					));
*/
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setUndecorated(true);
        f.setPreferredSize(new Dimension(w, h));
        f.add(sc.getComponent());
        f.pack();
        f.setVisible(true);
        f.addKeyListener(sc);

        //sc.now = true;
        //sc.timer.start();

        //Picture pic = new Picture("/home/alonso/star.png");
        //pic.makeTransparent(128, null);
        //pic.write("/home/alonso/star2.png");
        //Serialization.writeObject(sc, "/home/alonso/sky");
    }
}
