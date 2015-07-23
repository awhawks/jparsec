package jparsec.astronomy;

import jparsec.math.Constant;

public class CCDElementTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("CCDElement test");
        CCDElement ccd[] = CCDElement.getAllAvailableCCDs();
        System.out.println("List of all CCDs");

        for (int i = 0; i < ccd.length; i++) {
            System.out.println(ccd[i].name + "/" + ccd[i].chipSizeX + "/" + ccd[i].chipSizeY + "/" + ccd[i].pixelSizeX + "/" + ccd[i].pixelSizeY);
        }

        CCDElement toucam = CCDElement.getCCD("TouCam");
        TelescopeElement telescope = TelescopeElement.SCHMIDT_CASSEGRAIN_20cm;
        telescope.ocular = null;
        System.out.println("Scale (\"/pixel): " + (float) (toucam.getScale(telescope) * Constant.RAD_TO_ARCSEC));
        System.out.println("Field (arcmin): " + (float) (toucam.getFieldX(telescope) * Constant.RAD_TO_DEG * 60.0) + " * " + (float) (toucam.getFieldY(telescope) * Constant.RAD_TO_DEG * 60.0));
    }
}
