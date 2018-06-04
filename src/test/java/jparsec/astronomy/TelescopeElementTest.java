package jparsec.astronomy;

import jparsec.ephem.Functions;
import jparsec.math.Constant;
import jparsec.util.Translate;

public class TelescopeElementTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("TelescopeElement test");
        TelescopeElement telescope = TelescopeElement.SCHMIDT_CASSEGRAIN_20cm;
        Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);
        System.out.println("Field of view: " + Functions.formatAngle(telescope.getField(), 1));
        System.out.println("Limiting magnitude: " + Functions.formatValue(telescope.getLimitingMagnitude(), 1));
        System.out.println("Resolution: " + Functions.formatAngle(telescope.getResolution(), 1));
        System.out.println("mm for 1 degree object: " + Functions.formatValue(telescope.getObjectSizeAtFilmPlane(0.5 * Constant.DEG_TO_RAD), 1));
        System.out.println("List of all telescopes");
        TelescopeElement elements[] = TelescopeElement.getAllAvailableTelescopes();

        for (TelescopeElement te : elements) {
            System.out.println(te.name + '/' + te.diameter + '/' + te.focalLength + '/' + te.centralObstruction + '/' + te.spidersSize + '/' + te.cromatismLevel);
            System.out.println("  Primary focus field: " + te.getPrimaryFocusField() * Constant.RAD_TO_DEG);
            System.out.println("  Field: " + te.getField() * Constant.RAD_TO_DEG);
            System.out.println("  Limiting magnitude: " + te.getLimitingMagnitude());
            System.out.println("  Resolution: " + te.getResolution() * Constant.RAD_TO_ARCSEC);
            te.attachCCDCamera(CCDElement.getCCD("ST-9E"));
            System.out.println("  Limiting magnitude for ST-9E and 100s exposure: " + te.getCCDLimitingMagnitude(100, 19, 7));
            System.out.println("  Field: " + te.getField() * Constant.RAD_TO_DEG);
            te.attachCCDCamera(null);
            System.out.println("  Field: " + te.getField() * Constant.RAD_TO_DEG);
            System.out.println("  Invert H/V: " + te.invertHorizontal + '/' + te.invertVertical);
        }
    }
}
