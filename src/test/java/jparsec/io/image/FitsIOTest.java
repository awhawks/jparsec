package jparsec.io.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import jparsec.graph.DataSet;
import jparsec.math.FastMath;
import jparsec.vo.SExtractor;

public class FitsIOTest {

    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("FitsIO test");

        try {
            String s = "/home/alonso/java/librerias/40m/fits/opt_Vega467.fits";
            //s = "/home/alonso/java/soporte/2013/testeoFits/SIRTF_I4_0012101376_0007_0000_03_levbflx.fits"; // Test of fits with NaN
            //s = "/home/alonso/colaboraciones/Pablo/2008/fitsALMA/testFitNew.fits"; // Test with only bintables
            s = "/home/alonso/java/librerias/masymas/tres-3/TRES-3-025-070725-.fit";

            FitsIO fio = new FitsIO(s);
            System.out.println(fio.toString());

            int imageNumber = 0;
            short data[][] = (short[][]) fio.getData(imageNumber);

            System.out.println(data[0].length);
            System.out.println(data.length);
            System.out.println(ImageHeaderElement.toString(fio.getHeader(imageNumber), 25, 10));

            FitsIO.PICTURE_LEVEL level = FitsIO.PICTURE_LEVEL.LINEAR_INTERPOLATION; // .CUSTOM;
            level.NaN = 255;
            level.formula = "25 + 200*(x-min)/(max-min)";
            Picture p = fio.getPicture(imageNumber, level, true);

            SExtractor sex = fio.solveSources(imageNumber, 5, 10);
            System.out.println(sex.toString());
            Graphics2D g = (Graphics2D) p.getImage().getGraphics();
            g.setColor(Color.RED);
            for (int i = 0; i < sex.getNumberOfSources(); i++) {
                int r = sex.getDetectionWidth(i);
                int x = (int) (sex.getX(i).getValue() - r / 2.0 + 0.5) - 1;
                int y = (int) (sex.getY(i).getValue() - r / 2.0 + 0.5) - 1;
                g.drawOval(x, y, r, r);
            }

            // Print the equatorial position of the first (brightest) source detected by SExtractor
            System.out.println(fio.getWCS(imageNumber).getSkyCoordinates(new Point2D.Double(sex.getX(0).getValue(), sex.getY(0).getValue())).toStringAsEquatorialLocation());

            p.show(fio.getHDU(imageNumber).getAuthor());


            // Separate an image into rgb hdus
            s = "/home/alonso/java/soporte/2013/testeoFits/Hs-2009-14-a-web.jpg";
            Picture pic = new Picture(s);
            int img[][][] = pic.getImageAsArray(); // as rgba, x, y

            // Construct a basic fits header and add some info to it. Note
            int offset = FastMath.multiplyBy2ToTheX(1, 16 - 1); // 16 bit range / 2
            ImageHeaderElement header[] = ImageHeaderElement.getFitsHeader(DataSet.toShortArray(img[0], 0));
            header = ImageHeaderElement.addHeaderEntry(header, new ImageHeaderElement("LATOBS", "32:11:56", "as a test"));
            header = ImageHeaderElement.addHeaderEntry(header, new ImageHeaderElement("LONGOBS", "110:56", "as a test"));

            // Insert data into the fits
            FitsIO rgb = new FitsIO();
            for (int i = 0; i < 3; i++) {
                // Substract 32768 to fit into short data type to reduce memory consumption a factor 2 respect
                // using int data type (this is optional but recommended). Note byte data is also possible using
                // an offset of 128 (2^(8-1)).
                rgb.addHDU(FitsIO.createHDU(DataSet.toShortArray(img[i], -offset), header));
            }

            // Write fits file
            // rgb.writeEntireFits("/home/alonso/test.fits");

            // Check header
            header = rgb.getHeader(0);
            System.out.println(ImageHeaderElement.toString(header));

            // Check to remove header card
            //System.out.println(header.length);
            //header = ImageHeaderElement.deleteHeaderEntry(header, "LATOBS");
            //System.out.println(ImageHeaderElement.toString(header));
            //System.out.println(header.length);

            // Reconstruct original image, adding the value 32768 to compensate. Another approach
            // would to add a BZERO value of 32768 to the header, so that to add 32768 here would
            // not be necessary
            int[][] cr = DataSet.toIntArray((short[][]) rgb.getData(0), offset);
            int[][] cg = DataSet.toIntArray((short[][]) rgb.getData(1), offset);
            int[][] cb = DataSet.toIntArray((short[][]) rgb.getData(2), offset);
            pic = new Picture(cr, cg, cb, null);
            pic.show("Original image");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
