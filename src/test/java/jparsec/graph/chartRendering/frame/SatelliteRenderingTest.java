package jparsec.graph.chartRendering.frame;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.ephem.probes.SatelliteEphem;
import jparsec.graph.chartRendering.SatelliteRenderElement;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;

public class SatelliteRenderingTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("SatelliteRendering test");
        AstroDate astro = new AstroDate(); //2011, 10, 8, 22, 50, 50);
        TimeElement time = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);

        try {
            String name[] = new String[] { "ISS", "HST" };
            int index = SatelliteEphem.getArtificialSatelliteTargetIndex(name[0]);

            EphemerisElement eph = new EphemerisElement(Target.TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.ICRF);
            eph.targetBody.setIndex(index);
            eph.algorithm = EphemerisElement.ALGORITHM.ARTIFICIAL_SATELLITE;

            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);

            SatelliteRenderElement render = new SatelliteRenderElement(1000, 500); //(640*2, 320*2);
            //render.anaglyphMode = ANAGLYPH_COLOR_MODE.DUBOIS_RED_CYAN;
            render.showObserver = true;
            SatelliteRendering satRender = new SatelliteRendering(time, observer, eph, render, "Satellite rendering");
            satRender.addSatellite(name[1]);
            satRender.showRendering();

            // Get next pass (above 15 degrees of elevation)
            /*
            double min_elevation = 15 * Constant.DEG_TO_RAD;
            int max = 7; // 7 days of search
            int sources[] = new int[] {
                    index,
                    SatelliteEphem.getArtificialSatelliteTargetIndex(name[1])
            };
            long t0 = System.currentTimeMillis();
            if (satRender.getRenderSatelliteObject().getSatelliteEphem() != null && satRender.getRenderSatelliteObject().getSatelliteEphem().length > 0) {
                for (int i=0; i<satRender.getRenderSatelliteObject().getSatelliteEphem().length; i++) {
                    if (satRender.getRenderSatelliteObject().getSatelliteEphem()[i] != null) {
                        SatelliteOrbitalElement sat = SatelliteEphem.getArtificialSatelliteOrbitalElement(sources[i]);
                        satRender.getRenderSatelliteObject().getSatelliteEphem()[i].nextPass = SatelliteEphem.getNextPass(time, observer, eph, sat, min_elevation, max, true);
                        double dt = (System.currentTimeMillis()-t0)/1000.0;
                        if (satRender.getRenderSatelliteObject().getSatelliteEphem()[i].nextPass != 0.0) {
                            System.out.println(satRender.getRenderSatelliteObject().getSatelliteEphem()[i].name+": "+dt+"/"+TimeFormat.formatJulianDayAsDateAndTime(Math.abs(satRender.getRenderSatelliteObject().getSatelliteEphem()[i].nextPass), SCALE.LOCAL_TIME));
                        } else {
                            System.out.println(satRender.getRenderSatelliteObject().getSatelliteEphem()[i].name+": "+dt+"/"+"No pass in the next "+max+" days.");
                        }
                    }
                }
            }
            */
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
