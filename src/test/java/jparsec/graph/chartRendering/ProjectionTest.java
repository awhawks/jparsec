package jparsec.graph.chartRendering;

import jparsec.astronomy.Constellation;
import jparsec.astronomy.CoordinateSystem;
import jparsec.astronomy.TelescopeElement;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;

public class ProjectionTest {
    /**
     * Test program.
     *
     * @param args Unused.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        // Define date
        AstroDate astro = new AstroDate(2000, 1, 1, 0, 0, 0);
        // Set this date as local time in our TimeElement object
        TimeElement time = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);
        // Define a telescope object as it is selected by the user
        TelescopeElement telescope = TelescopeElement.OBJECTIVE_300mm_f2_8;
        Target.TARGET targetID = Target.TARGET.NOT_A_PLANET;
        EphemerisElement eph = new EphemerisElement(
            targetID,
            EphemerisElement.COORDINATES_TYPE.APPARENT,
            EphemerisElement.EQUINOX_OF_DATE,
            EphemerisElement.TOPOCENTRIC,
            EphemerisElement.REDUCTION_METHOD.IAU_2006,
            EphemerisElement.FRAME.ICRF);

        ObserverElement observer = null;
        // Establish the selected city as a CityElement object
        CityElement city = City.findCity("Madrid");
        // Set this city as the location for our ObserverElement object
        observer = ObserverElement.parseCity(city);

        // Set main render properties
        SkyRenderElement sky = new SkyRenderElement();
        sky.coordinateSystem = CoordinateSystem.COORDINATE_SYSTEM.EQUATORIAL;
        sky.projection = Projection.PROJECTION.SPHERICAL;
        sky.width = 1200;
        sky.height = 700;
        sky.planetRender = null;
        sky.telescope = telescope;
        sky.trajectory = null;

        sky.drawSkyCorrectingLocalHorizon = true; // To show properly objects close to horizon -> slower
        sky.drawSkyBelowHorizon = true;

        // Now we set the center position to draw: constellation or a planet
        LocationElement loc = Constellation.getConstellationPosition("ANDROMEDA", astro.jd(), Constellation.CONSTELLATION_NAME.LATIN);

        if (loc != null) {
            loc = RenderSky.getPositionInSelectedCoordinateSystem(loc, time, observer, eph, sky, true);
            sky.centralLongitude = loc.getLongitude();
            sky.centralLatitude = loc.getLatitude();
        }

        // Test
        int x0 = 600, y0 = 350;
        double field = sky.telescope.getField();
        //loc = new LocationElement(44.222 * Constant.DEG_TO_RAD, -5.31111 * Constant.DEG_TO_RAD, 1.0);
        loc = new LocationElement(sky.centralLongitude - field * 0.4, sky.centralLatitude - field * 0.4, 1.0);
        Projection projection = new Projection(time, observer, eph, sky, field, x0, y0);
        projection.configure(sky);
        float pos[] = projection.projectPosition(loc, 0, false);
        System.out.println(loc.getLongitude() * Constant.RAD_TO_DEG + "/" + loc.getLatitude() * Constant.RAD_TO_DEG);
        System.out.println(pos[0] + "/" + pos[1]);

        double resolution = field * 2.0 * Constant.RAD_TO_DEG / sky.width;
        System.out.println("inverting with max. error " + resolution);
        long t0 = System.currentTimeMillis();
        LocationElement loc1 = projection.invertSpheric(pos[0], pos[1]);

        if (loc1 != null) {
            System.out.println(loc1.getLongitude() * Constant.RAD_TO_DEG + "/" + loc1.getLatitude() * Constant.RAD_TO_DEG);
            long t1 = System.currentTimeMillis();
            System.out.println((t1 - t0) / 1000.0);
        } else {
            System.out.println("Cannot invert position");
        }
    }
}