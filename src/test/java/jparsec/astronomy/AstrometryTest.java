package jparsec.astronomy;

import jparsec.ephem.Functions;
import jparsec.io.ConsoleReport;
import jparsec.io.FileIO;
import jparsec.io.image.WCS;
import jparsec.math.Constant;
import jparsec.observer.LocationElement;

import java.awt.geom.Point2D;

public class AstrometryTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        LocationElement loc0 = new LocationElement(Functions.parseRightAscension("20 02 00.00"), Functions.parseDeclination("13 50 00.00"), 1.0);
        LocationElement loc[] = new LocationElement[] {
            new LocationElement(Functions.parseRightAscension("20 02 49.691"), Functions.parseDeclination("14 09 38.11"), 1.0),
            new LocationElement(Functions.parseRightAscension("20 03 58.936"), Functions.parseDeclination("13 39 13.57"), 1.0),
            new LocationElement(Functions.parseRightAscension("20 00 44.093"), Functions.parseDeclination("13 32 00.31"), 1.0),
            new LocationElement(Functions.parseRightAscension("20 02 45.502"), Functions.parseDeclination("13 37 15.97"), 1.0)
        };
        Point2D p[] = new Point2D[] {
            new Point2D.Double(279.23, 877.94),
            new Point2D.Double(14.59, 373.46),
            new Point2D.Double(794.31, 272.06),
            new Point2D.Double(307.11, 346.88)
        };

        Astrometry astrometry = new Astrometry(loc0, loc, p);
        double c[] = astrometry.getPlateConstants();
        System.out.println("a = " + c[0] + "   b = " + c[1] + "   c = " + c[2]);
        System.out.println("d = " + c[3] + "   e = " + c[4] + "   f = " + c[5]);

        double res[] = astrometry.getPlatePositionResidual();
        System.out.println("RA residual (\"):  " + res[0] * Constant.RAD_TO_ARCSEC);
        System.out.println("DEC residual (\"): " + res[1] * Constant.RAD_TO_ARCSEC);
        ConsoleReport.doubleArrayReport(astrometry.getResiduals(), "f3.6");

        // Plate center position is 484.3941, 560.1524 px
        LocationElement l = astrometry.getPlatePosition(382.41, 521.57);
        System.out.println("RA:  " + Functions.formatRA(l.getLongitude()));
        System.out.println("DEC: " + Functions.formatDEC(l.getLatitude()));

        Point2D po = astrometry.getPlatePosition(l);
        System.out.println("X:  " + Functions.formatValue(po.getX(), 4));
        System.out.println("Y:  " + Functions.formatValue(po.getY(), 4));

        po = astrometry.getPlatePosition(loc0);
        System.out.println("X0: " + Functions.formatValue(po.getX(), 4));
        System.out.println("Y0: " + Functions.formatValue(po.getY(), 4));

/*
            Results should be similar to:
            -1.0000177125018768 -4.100803280460952e-7 0.008809538547811933
            -3.97785696261592e-7 -0.9999822781957907 -0.009734226574819366
            0.20248154488594983 1.5620885347581153
            20 2 25.80162647956456 13 47 47.25003827978071
 */

        // Example from http://stsdas.stsci.edu/cgi-bin/gethelp.cgi?ccmap
        String data[] = new String[] {
            "327.5  410.4  13h 29m 47.30s  47° 13' 37.5\"  13:29:47.28  47:13:37.9  0.128 -0.370",
            "465.5   62.1  13h 29m 37.41s  47° 09' 09.2\"  13:29:37.42  47:09:09.2 -0.191 -0.062",
            "442.0  409.6  13h 29m 38.70s  47° 13' 36.2\"  13:29:38.70  47:13:35.9  0.040  0.282",
            "224.3  131.2  13h 29m 55.42s  47° 10' 05.2\"  13:29:55.40  47:10:05.1  0.289  0.059",
            "134.4  356.3  13h 30m 01.82s  47° 12' 58.8\"  13:30:01.84  47:12:58.7 -0.267  0.091",
            "250.256  266.309  13h 29m 53.273s  47° 11' 48.36\""
        };

        loc = new LocationElement[data.length];
        p = new Point2D[data.length];

        for (int i = 0; i < data.length; i++) {
            loc[i] = new LocationElement(
                Functions.parseRightAscension(FileIO.getField(3, data[i], "  ", true)),
                Functions.parseDeclination(FileIO.getField(4, data[i], "  ", true)),
                1.0
            );

            p[i] = new Point2D.Double(Double.parseDouble(FileIO.getField(1, data[i], " ", true)), Double.parseDouble(FileIO.getField(2, data[i], " ", true)));
        }

        astrometry = new Astrometry(loc[loc.length - 1], loc, p);
        c = astrometry.getPlateConstants();
        System.out.println("a = " + c[0] + "   b = " + c[1] + "   c = " + c[2]);
        System.out.println("d = " + c[3] + "   e = " + c[4] + "   f = " + c[5]);

        res = astrometry.getPlatePositionResidual();
        System.out.println("RA residual (\"):  " + res[0] * Constant.RAD_TO_ARCSEC);
        System.out.println("DEC residual (\"): " + res[1] * Constant.RAD_TO_ARCSEC);
        ConsoleReport.doubleArrayReport(astrometry.getResiduals(), "f3.6");

        po = astrometry.getPlatePosition(loc[loc.length - 1]);
        System.out.println("X:  " + Functions.formatValue(po.getX(), 4));
        System.out.println("Y:  " + Functions.formatValue(po.getY(), 4));
        l = astrometry.getPlatePosition(po.getX(), po.getY());
        System.out.println("RA:  " + Functions.formatRA(l.getLongitude()));
        System.out.println("DEC: " + Functions.formatDEC(l.getLatitude()));
        WCS wcs = astrometry.getAsWCS(true);
        System.out.println(wcs.toString());

            /*
#     Sky projection geometry: tan
#     Reference point: 13:29:53.273  47:11:48.36  (hours  degrees)
#     Reference point: 250.256  266.309  (pixels  pixels)
#     X and Y scale: 0.764  0.767  (arcsec/pixel  arcsec/pixel)
#     X and Y axis rotation: 179.126  358.974  (degrees  degrees)
EPOCH   = 1987.26 / EPOCH OF RA AND DEC
RADECSYS= 'FK5     '
EQUINOX = 2000.
MJD-WCS = 51544.5
CTYPE1  = 'RA---TAN'
CTYPE2  = 'DEC--TAN'
CRVAL1  = 202.471969550729
CRVAL2  =  47.1967667056819
CRPIX1  = 250.255619786203
CRPIX2  = 266.308757328719
CD1_1   =  -2.1224568721716E-4
CD1_2   =  -3.8136850875221E-6
CD2_1   =  -3.2384199624421E-6
CD2_2   =   2.12935798198448E-4
             */
    }
}
