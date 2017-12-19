package jparsec.graph.chartRendering;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.ephem.event.MainEvents;
import jparsec.ephem.event.SimpleEventElement;
import jparsec.io.image.Picture;
import jparsec.observer.City;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;
import jparsec.time.TimeElement.SCALE;
import jparsec.util.Translate;

public class RenderEclipseTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("RenderEclipse test");
        Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);
        //AstroDate astro = new AstroDate(2003, AstroDate.MAY, 31, 9, 0, 0);
        //AstroDate astro = new AstroDate(1994, AstroDate.MAY, 10, 9, 0, 0);
        //AstroDate astro = new AstroDate(2015, AstroDate.MARCH, 20, 9, 0, 0);
        //AstroDate astro = new AstroDate(2005, AstroDate.OCTOBER, 3, 9, 0, 0); // Correct values: max 2005-10-03 10:57:58 LT, partial from 09:40:12 to 12:23:35 LT, annular from 10:55:53 to 11:00:02 LT
        //AstroDate astro = new AstroDate(2013, AstroDate.MAY, 10, 9, 0, 0);
        AstroDate astro = new AstroDate(2013, AstroDate.NOVEMBER, 3, 9, 0, 0); // Correct values: max 2013-11-03 13:35:27 LT, partial from 13:00:27 to 14:10:26 LT
        //AstroDate astro = new AstroDate(2001, AstroDate.JUNE, 21, 9, 0, 0);
        //AstroDate astro = new AstroDate(2011, AstroDate.JANUARY, 4, 0, 0, 0);
        RenderEclipse b = new RenderEclipse(astro);
        // ApplicationLauncher.launchURL(b.getGoogleMapLink());

        TimeElement time = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);
        ObserverElement observer = ObserverElement.parseCity(City.findCity("Madrid"));
        
        EphemerisElement eph = new EphemerisElement(
            Target.TARGET.SUN,
            EphemerisElement.COORDINATES_TYPE.APPARENT,
            EphemerisElement.EQUINOX_OF_DATE,
            EphemerisElement.TOPOCENTRIC,
            EphemerisElement.REDUCTION_METHOD.IAU_2009,
            EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000,
            EphemerisElement.ALGORITHM.MOSHIER);

        RenderPlanet.MAXIMUM_TEXTURE_QUALITY_FACTOR = 2f;
        RenderPlanet.FORCE_HIGHT_QUALITY = true;
        RenderSatellite.ALLOW_SPLINE_RESIZING = false; // Improve performance
        SatelliteRenderElement.PLANET_MAP map = SatelliteRenderElement.PLANET_MAP.MAP_SPHERICAL;
        map.zoomFactor = 1.5f;
        map.EarthMapSource = SatelliteRenderElement.PLANET_MAP.EARTH_MAP_POLITICAL;
        Graphics g;
        Picture pic;

        Graphics.ANAGLYPH_COLOR_MODE colorMode = Graphics.ANAGLYPH_COLOR_MODE.NO_ANAGLYPH; //.DUBOIS_RED_CYAN;
        //colorMode.setEyeSeparation(0.2f);
        if (map == SatelliteRenderElement.PLANET_MAP.MAP_FLAT) {
            g = new AWTGraphics(600, 800, colorMode, false, false);
        } else {
            g = new AWTGraphics(600, 800, colorMode, false, false);
            map.zoomFactor = 0.9f;
        }

        /*
        b.renderSolarEclipse(time.timeScale, observer, eph, g, true, map);
        pic = new Picture((java.awt.image.BufferedImage) g.getRendering());
        pic.show(Translate.translate("Solar eclipse"));
        //pic.write("/home/alonso/solarEclipse_Madrid_2013-11-03.png");

        Graphics g2 = g.getGraphics(600, 800);
        map = PLANET_MAP.MAP_FLAT;
        map.zoomFactor = 1f;
        map.EarthMapSource = null;
        astro = new AstroDate(2015, AstroDate.SEPTEMBER, 28, 19, 0, 0);
        // astro = new AstroDate(1997, AstroDate.SEPTEMBER, 16, 19, 0, 0);
        time = new TimeElement(astro, SCALE.UNIVERSAL_TIME_UT1);
        RenderEclipse.renderLunarEclipse(time, observer, eph, g2, true, map);
        pic = new Picture((java.awt.image.BufferedImage) g2.getRendering());
        pic.show(Translate.translate("Lunar eclipse"));
        */

        /*
        // Test of high quality output
        map = PLANET_MAP.MAP_SPHERICAL;
        map.clear();
        map.zoomFactor = 0.90f;
        //map.EarthMapSource = PLANET_MAP.EARTH_MAP_POLITICAL;
        map.showGrid = true;
        RenderSatellite.ALLOW_SPLINE_RESIZING = true;
        astro = new AstroDate(2011, AstroDate.JANUARY, 4, 0, 0, 0);
        b = new RenderEclipse(astro);
        g = g.getGraphics(800, 800);
        g.setColor(255, 255, 255, 255); // Color for the grid in the eclipse map
        if (map.EarthMapSource == PLANET_MAP.EARTH_MAP_POLITICAL) g.setColor(0, 0, 0, 255); // Color for the grid in the eclipse map
        g.setFont(FONT.getDerivedFont(g.getFont(), (g.getFont().getSize()*g.getWidth())/500)); // duplicate font size
        observer.setName(""); // Don't show observer
        //RenderPlanet.FORCE_WHITE_BACKGROUND = true; // Done automatically, but only for non default map (political, etc)
        b.solarEclipseMap(time.timeScale, observer, eph, g, map);
        Picture pic2 = new Picture((java.awt.image.BufferedImage) g.getRendering());
        pic2.show(Translate.translate("Solar eclipse"));
        */

        // Chart all solar/lunar eclipses for a given year
        RenderSatellite.ALLOW_SPLINE_RESIZING = false; // Improve performance
        int year = 2018;
        String locName = "Madrid";
        
        //observer.setName("Burgeo");
        //observer.setLongitudeDeg(-57.61);
        //observer.setLatitudeDeg(47.65);
        //observer.setHeight(0, true);
        observer = ObserverElement.parseCity(City.findCity(locName));
        
        boolean onlyVisibleFromSpain = false;
        boolean horiz = true;
        int w = 600, h = 800;
        String path = "/home/alonso/";
        map = SatelliteRenderElement.PLANET_MAP.MAP_FLAT;
        map.clear();
        //map.EarthMapSource = PLANET_MAP.EARTH_MAP_POLITICAL;

        // Lunar eclipses
        String addName = "";
        if (!horiz) addName = "_eq";
        astro = new AstroDate(year, 1, 1);
        SCALE sc = SCALE.LOCAL_TIME;
        TimeElement newTime = new TimeElement(astro, sc);
        w = 600;
        h = 800;
        RenderEclipse.ShowWithoutLT = true;
        RenderEclipse.ShowMoonTexture = true;

        do {
            boolean found = true;
            do {
                SimpleEventElement s = MainEvents.MoonPhaseOrEclipse(newTime.astroDate.jd(), SimpleEventElement.EVENT.MOON_LUNAR_ECLIPSE, MainEvents.EVENT_TIME.NEXT);
                newTime = new TimeElement(s.time, TimeElement.SCALE.TERRESTRIAL_TIME);
                if (onlyVisibleFromSpain) found = RenderEclipse.lunarEclipseVisible(newTime, observer, eph, false);
                if (!found) newTime.add(20);
            } while (!found);

            if (newTime.astroDate.getYear() > year) break;
            newTime = new TimeElement(TimeScale.getJD(newTime, observer, eph, sc), sc);
            g = new AWTGraphics(w, h, false, false);
            RenderEclipse.renderLunarEclipse(newTime, observer, eph, g, horiz, map);
            pic = new Picture((java.awt.image.BufferedImage) g.getRendering());
            String date = newTime.astroDate.toString();
            date = date.substring(0, date.indexOf(" "));
            pic.write(path + "lunarEclipse_" + locName + "_" + date + addName + ".png");
            newTime.add(20);
        } while (true);

        // Solar eclipses
        //h = w = 1200;
        map = SatelliteRenderElement.PLANET_MAP.MAP_FLAT;
        map.clear();
        map.zoomFactor = 0.9f;
        map.EarthMapSource = SatelliteRenderElement.PLANET_MAP.EARTH_MAP_POLITICAL;
        newTime = new TimeElement(astro, sc);

        do {
            boolean found = true;
            do {
                SimpleEventElement s = MainEvents.MoonPhaseOrEclipse(newTime.astroDate.jd(), SimpleEventElement.EVENT.MOON_SOLAR_ECLIPSE, MainEvents.EVENT_TIME.NEXT);
                newTime = new TimeElement(s.time, TimeElement.SCALE.TERRESTRIAL_TIME);

                if (onlyVisibleFromSpain) {
                    RenderEclipse re = new RenderEclipse(newTime.astroDate);
                    found = re.isVisible(observer);
                }

                if (!found) newTime.add(20);
            } while (!found);

            if (newTime.astroDate.getYear() > year) break;
            g = new AWTGraphics(w, h, false, false);
            try {
            	RenderEclipse re = new RenderEclipse(newTime.astroDate);
                re.renderSolarEclipse(sc, observer, eph, g, horiz, map);
            } catch (Exception exc) {
            	System.out.println("No eclipse on "+newTime.astroDate.toString()+" ?");
            	newTime.add(20);
            	continue;
            }
            pic = new Picture((java.awt.image.BufferedImage) g.getRendering());
            String date = newTime.astroDate.toString();
            date = date.substring(0, date.indexOf(" "));
            pic.write(path + "solarEclipse_" + locName + "_" + date + addName + ".png");
            newTime.add(20);
        } while (true);
    }
}
