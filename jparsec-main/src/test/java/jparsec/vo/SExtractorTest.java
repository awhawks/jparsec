package jparsec.vo;

import java.awt.Color;
import java.awt.Graphics2D;
import jparsec.io.image.FitsIO;
import jparsec.io.image.Picture;
import jparsec.util.Logger;

public class SExtractorTest {
    /**
     * Test program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("SExtractor test");
        String dir = "/home/alonso/java/librerias/masymas/tres-3/";
        String file = "TRES-3-025-070725-.fit"; // Input fits file at 'dir'
        String config = "machine.config"; // Default configuration file of SExtractor, should be at 'dir'
        Logger.setLoggerLevel(Logger.LEVEL.TRACE_LEVEL1);

        SExtractor sex = new SExtractor(dir, config);
        sex.execute(file);

        // Show image and the brightest detected sources
        FitsIO f = new FitsIO(dir + file);
        Picture p = f.getPicture(0, FitsIO.PICTURE_LEVEL.LINEAR_INTERPOLATION, true);
        Graphics2D g = (Graphics2D) p.getImage().getGraphics();
        g.setColor(Color.RED);
        for (int i = 0; i < sex.getNumberOfSources(); i++) {
            int r = sex.getDetectionWidth(i);
            int x = (int) (sex.getX(i).getValue() - r / 2.0 + 0.5) - 1;
            int y = (int) (sex.getY(i).getValue() - r / 2.0 + 0.5) - 1;
            g.drawOval(x, y, r, r);
        }

        System.out.println(sex.toString());
        p.show(file);
        //p.write("/home/alonso/test.png");
    }
}
