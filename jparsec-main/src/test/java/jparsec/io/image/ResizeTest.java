package jparsec.io.image;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ResizeTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        //String path = "/home/alonso/000000109.png";
        String path = "/home/alonso/documentos/presentaciones/2011/tesis/img/escalasTiempo.png";
        //String path = "/home/alonso/documentos/presentaciones/2011/tesis/img/atmosfera.png";
        //String path = "/home/alonso/documentos/presentaciones/2011/tesis/img/datacube1a.png";
        int w = 1920, h = 0;

        Resize.ALLOW_RESIZE_TO_GREATER_SIZES = true;
        BufferedImage in = ImageIO.read(new File(path));
        /*
        Picture pic = new Picture(in);
        pic.getScaledInstance(w*2, 0, true);
        in = pic.getImage();
        */
        long t0 = System.currentTimeMillis();
        BufferedImage out = Resize.resize(in, w, h, true);
        long t1 = System.currentTimeMillis();
        Picture p2 = new Picture(out);
        p2.show("Resize class image");
        //pic.show("Original");
        System.out.println("Spline interpolation shown in " + (t1 - t0) / 1000.0 + " seconds.");
    }
}
