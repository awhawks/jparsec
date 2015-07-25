package jparsec.io.image;

import java.awt.geom.Point2D;
import jparsec.math.Constant;
import jparsec.observer.LocationElement;

public class WCSTest {
    /**
     * Testing program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("WCS test");

        String file = "/home/alonso/colaboraciones/Pablo/2008/fitsALMA/testImages/1904-66_TAN.fits";
        //file = "/home/alonso/today/reduced/1377258097873.fits";
        System.out.println("File " + file);
        WCS wcs = new WCS(file);

        int px = 0, py = 1011; // 96, 96
        wcs.useSkyViewImplementation = false;
        System.out.println("Width " + wcs.getWidth());
        System.out.println("Height " + wcs.getHeight());
        Point2D.Double p = new Point2D.Double(px, py);
        LocationElement loc = wcs.getSkyCoordinates(p);
        System.out.println(p.getX() + "/" + p.getY() + " = " + loc.getLongitude() * Constant.RAD_TO_DEG + "/" + loc.getLatitude() * Constant.RAD_TO_DEG);
        Point2D pp = wcs.getPixelCoordinates(loc);
        System.out.println(pp.getX() + "/" + pp.getY() + " = " + loc.getLongitude() * Constant.RAD_TO_DEG + "/" + loc.getLatitude() * Constant.RAD_TO_DEG);

        wcs.useSkyViewImplementation = true;
        System.out.println("Width " + wcs.getWidth());
        System.out.println("Height " + wcs.getHeight());
        p = new Point2D.Double(px, py);
        loc = wcs.getSkyCoordinates(p);
        System.out.println(p.getX() + "/" + p.getY() + " = " + loc.getLongitude() * Constant.RAD_TO_DEG + "/" + loc.getLatitude() * Constant.RAD_TO_DEG);
        pp = wcs.getPixelCoordinates(loc);
        System.out.println(pp.getX() + "/" + pp.getY() + " = " + loc.getLongitude() * Constant.RAD_TO_DEG + "/" + loc.getLatitude() * Constant.RAD_TO_DEG);

        wcs.setCrval1(322.9358838939052);
        wcs.setCrval2(48.48428727951623);
        wcs.setCrpix1(1883.4799194335938);
        wcs.setCrpix2(1093.2748413085938);
        wcs.setCdelt1(1.965398232347E-4);
        wcs.setCdelt2(-1.965398232347E-4);
        wcs.setEpoch(2013.9222222222224);
        wcs.setCrota2(360.0565970885837);

            /*
World Coordinate System
-----------------------
crval1     : 322.9358838939052
crval2     : 48.48428727951623
crpix1     : 1883.4799194335938
crpix2     : 1093.2748413085938
cdelt1     : 1.965398232347E-4
cdelt2     : -1.965533565914E-4
ctype1     : RA---TAN
ctype2     : DEC--TAN
equinox    : 2000
epoch      : 2013.9222222222224
crota2     : 360.0565970885837
projection : TAN
width : 3888
height : 2592
cd : -9.999999999999999E-5, -1.0000000000000002E-6, -1.0000000000000002E-6, 1.0E-4
           */

        wcs.setWidth(3888);
        wcs.setHeight(2592);
        wcs.setCD(new double[] { -9.999999999999999E-5, -1.0000000000000002E-6, -1.0000000000000002E-6, 1.0E-4 });
        System.out.println(wcs.toString());

        wcs.useSkyViewImplementation = false;
        System.out.println("Width " + wcs.getWidth());
        System.out.println("Height " + wcs.getHeight());
        p = new Point2D.Double(px, py);
        loc = wcs.getSkyCoordinates(p);
        System.out.println(p.getX() + "/" + p.getY() + " = " + loc.getLongitude() * Constant.RAD_TO_DEG + "/" + loc.getLatitude() * Constant.RAD_TO_DEG);
        pp = wcs.getPixelCoordinates(loc);
        System.out.println(pp.getX() + "/" + pp.getY() + " = " + loc.getLongitude() * Constant.RAD_TO_DEG + "/" + loc.getLatitude() * Constant.RAD_TO_DEG);

        wcs.useSkyViewImplementation = true;
        System.out.println("Width " + wcs.getWidth());
        System.out.println("Height " + wcs.getHeight());
        p = new Point2D.Double(px, py);
        loc = wcs.getSkyCoordinates(p);
        System.out.println(p.getX() + "/" + p.getY() + " = " + loc.getLongitude() * Constant.RAD_TO_DEG + "/" + loc.getLatitude() * Constant.RAD_TO_DEG);
        pp = wcs.getPixelCoordinates(loc);
        System.out.println(pp.getX() + "/" + pp.getY() + " = " + loc.getLongitude() * Constant.RAD_TO_DEG + "/" + loc.getLatitude() * Constant.RAD_TO_DEG);
    }
}
