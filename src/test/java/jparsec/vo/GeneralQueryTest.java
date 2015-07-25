package jparsec.vo;

import jparsec.ephem.Target;
import jparsec.io.image.Picture;
import jparsec.util.JPARSECException;

public class GeneralQueryTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        try {
            /*
            double radius = 2.0, temperature = 25000;
            double luminosity = jparsec.astronomy.Star.getStarLuminosity(radius, temperature);
            double radius2 = jparsec.astronomy.Star.getStarRadius(luminosity, temperature);
            System.out.println(radius+" / "+radius2);
            String out = GeneralQuery.query(GeneralQuery.getQueryToSiessModels(0.02, temperature, luminosity, 0));
            System.out.println(out);
            */

            int width = 800;
            String cat = null; //"uhuru4";
            String con = null; //"408mhz:Log:6:1:1000";
            String query = GeneralQuery.getQueryToSkyView("M31", GeneralQuery.SKYVIEW_SURVEY.IRAS100, 2, width, false, true,
                    GeneralQuery.SKYVIEW_COORDINATE.EQUATORIAL_J2000,
                    GeneralQuery.SKYVIEW_PROJECTION.ORTHOGRAPHIC,
                    GeneralQuery.SKYVIEW_LUT_TABLE.FIRE, null, cat, con);
            System.out.println(query);
            Picture p = new Picture(GeneralQuery.queryImage(query));
            p.show("SkyView Image server - M31");

            SimbadElement simbad = SimbadElement.searchDeepSkyObject("M31");
            //SimbadElement simbad = SimbadQuery.query("M31");
            System.out.println(simbad.name);
            //simbad.rightAscension = 180.0 * Constant.DEG_TO_RAD;
            //simbad.declination = 0.0 * Constant.DEG_TO_RAD;
            query = GeneralQuery.getQueryToSDSS(simbad.getLocation(), GeneralQuery.SDSS_PLATE.VISIBLE, 60.0, false);
            System.out.println(query);
            //GeneralQuery.queryFile(query, "myimage.gif");

            Picture pp = new Picture(GeneralQuery.queryImage(query));
            pp.show(800, 600, "DSS Image - M31", true, true, true);

            // Planetary maps are not working always
            width = 500;
            int height = 500;

            query = GeneralQuery.getQueryToUSGSAstroGeologyMapServer(Target.TARGET.MARS, width, height,
                    new double[] { 60, -30, 160, 70 }, GeneralQuery.USGS_MARS_VIKING_COLOR);

            System.out.println(query);
            // http://planetarynames.wr.usgs.gov/specifics.html
            p = new Picture(GeneralQuery.queryImage(query, 60000));
            p.show("USGS Image server - Tharsis");
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
