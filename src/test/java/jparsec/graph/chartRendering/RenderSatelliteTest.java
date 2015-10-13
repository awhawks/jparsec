package jparsec.graph.chartRendering;

import java.awt.image.BufferedImage;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.ephem.probes.SatelliteEphem;
import jparsec.ephem.probes.SatelliteOrbitalElement;
import jparsec.io.image.Picture;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;
import jparsec.util.JPARSECException;

public class RenderSatelliteTest {

    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("RenderSatellite test");
        AstroDate astro = new AstroDate(); //2015, 3, 20, 10, 15, 0); //2011, 10, 8, 22, 50, 50);
        TimeElement time = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);

        try {
            String name[] = new String[] { "ISS", "HST", "TIANGONG 1" };
            int index = SatelliteEphem.getArtificialSatelliteTargetIndex(name[0]);

            EphemerisElement eph = new EphemerisElement(
                Target.TARGET.NOT_A_PLANET,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.GEOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.IAU_2006,
                EphemerisElement.FRAME.ICRF);
            eph.targetBody.setIndex(index);
            eph.algorithm = EphemerisElement.ALGORITHM.ARTIFICIAL_SATELLITE;

            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);

            RenderSatellite.ALLOW_SPLINE_RESIZING = false;
            SatelliteRenderElement render = new SatelliteRenderElement(640 * 2, 320 * 4);
            render.planetMap = SatelliteRenderElement.PLANET_MAP.MAP_FLAT;
            render.showOrbits = true;
            render.planetMap.centralPosition = new LocationElement(0, 0, 1); // To center equator instead of observer
            //render.anaglyphMode = ANAGLYPH_COLOR_MODE.DUBOIS_RED_CYAN;
            //render.showDayAndNight = false;
            render.planetMap.showGrid = true;
            render.planetMap.zoomFactor = 0.95f;
            // render.planetMap.EarthMapSource = PLANET_MAP.EARTH_MAP_POLITICAL;
            RenderSatellite satRender = new RenderSatellite(time, observer, eph, render);
            //satRender.highlightMoon = true;
            satRender.addSatellite(name[1]);
            satRender.addSatellite(name[2]);
            Graphics g = new AWTGraphics(render.width, render.height, render.anaglyphMode, false, false);
            satRender.renderize(g);

            Picture pic = new Picture((BufferedImage) g.getRendering());
            pic.show("");

            // Get next pass (above 15 degrees of elevation)
            double min_elevation = 15 * Constant.DEG_TO_RAD;
            int max = 7; // 7 days of search
            int sources[] = new int[] {
                    index,
                    SatelliteEphem.getArtificialSatelliteTargetIndex(name[1])
            };
            long t0 = System.currentTimeMillis();
            if (satRender.getSatelliteEphem() != null && satRender.getSatelliteEphem().length > 0) {
                for (int i = 0; i < satRender.getSatelliteEphem().length; i++) {
                    if (satRender.getSatelliteEphem()[i] != null) {
                        SatelliteOrbitalElement sat = SatelliteEphem.getArtificialSatelliteOrbitalElement(sources[i]);
                        satRender.getSatelliteEphem()[i].nextPass = SatelliteEphem.getNextPass(time, observer, eph, sat, min_elevation, max, true);
                        double dt = (System.currentTimeMillis() - t0) / 1000.0;
                        if (satRender.getSatelliteEphem()[i].nextPass != 0.0) {
                            System.out.println(satRender.getSatelliteEphem()[i].name + ": " + dt + "/" + TimeFormat.formatJulianDayAsDateAndTime(Math.abs(satRender.getSatelliteEphem()[i].nextPass), TimeElement.SCALE.LOCAL_TIME));
                        } else {
                            System.out.println(satRender.getSatelliteEphem()[i].name + ": " + dt + "/" + "No pass in the next " + max + " days.");
                        }
                    }
                }
            }

            // Test ephemerides of the Earth from a point close to Earth on Earth's equator
            // and from the Sun, for an instant close to the culmination of the Sun from
            // Greenwich meridian. Both should be similar, including longitude of central
            // meridian.

            /*
            astro = new AstroDate(2013, 4, 2, 12, 3, 30);
            time = new TimeElement(astro, SCALE.UNIVERSAL_TIME_UT1);
            EphemerisElement eph1 = eph.clone();
            eph1.isTopocentric = true;
            eph1.equinox = EphemerisElement.EQUINOX_OF_DATE;
            eph1.targetBody = TARGET.EARTH;
            ObserverElement obs1 = observer.clone();
            obs1.setLatitudeRad(0);
            obs1.setLongitudeRad(0);
            obs1.setHeight(0, true);
            double jd_TDB = TimeScale.getJD(time, observer, eph1, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            double delta[] = obs1.topocentricObserverICRF(time, eph1);
            double p[] = Ephem.eclipticToEquatorial(PlanetEphem.getHeliocentricEclipticPositionJ2000(jd_TDB, TARGET.EARTH), Constant.J2000, eph1);
            delta = Functions.scalarProduct(delta, 1000);
            p = Functions.sumVectors(p, delta);
            p = new double[] {p[0], p[1], p[2], 0.0, 0.0, 0.0};
            obs1 = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("", p));
            EphemerisElement sun_eph = new EphemerisElement(TARGET.EARTH, eph.ephemType,
                    EphemerisElement.EQUINOX_OF_DATE, eph.isTopocentric, eph.ephemMethod, eph.frame);
            sun_eph.ephemType = COORDINATES_TYPE.GEOMETRIC;
            sun_eph.algorithm = EphemerisElement.ALGORITHM.MOSHIER;
            sun_eph.correctForEOP = false;
            sun_eph.correctForPolarMotion = false;
            sun_eph.preferPrecisionInEphemerides = false;
            EphemElement ephemEarth1 = PlanetEphem.MoshierEphemeris(time, obs1, sun_eph);

            //time.add(498.85834 / Constant.SECONDS_PER_DAY);
            obs1 = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("", TARGET.SUN));
            EphemElement ephemEarth2 = PlanetEphem.MoshierEphemeris(time, obs1, sun_eph);
            ephemEarth2.setEquatorialLocation(Ephem.getPositionFromEarth(ephemEarth2.getEquatorialLocation(), time, obs1, sun_eph));

            ConsoleReport.fullEphemReportToConsole(ephemEarth1);
            ConsoleReport.fullEphemReportToConsole(ephemEarth2);
        */
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
