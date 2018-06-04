package jparsec.io.device;

import java.awt.image.BufferedImage;

public class ObservationManagerTest {
    /**
     * Test program.
     *
     * @param args Unused.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("ObservationManager test");

        String file = "/home/alonso/java/librerias/astroFoto/crw_2785.crw";
        ObservationManager.executeDCRaw(file);

        //file = "/home/alonso/java/librerias/astroFoto/crw_2785.pgm";
        file = "/home/alonso/eclipse/workspace/jparsec/capt0000.pgm";
        BufferedImage img = ObservationManager.readPGM(file, false);
        //pic.getScaledInstanceUsingSplines(1600, 1200, true);
        //pic.show(1600, 1200, "PGM file", true, false, true);

        int x = 2000, y = 800;
        System.out.println(ObservationManager.getPixelCount(x, y, img));
        System.out.println(ObservationManager.getPixelCount(x + 1, y, img));
        System.out.println(ObservationManager.getPixelCount(x + 2, y, img));
        System.out.println(ObservationManager.getPixelCount(x + 3, y, img));
        System.out.println(ObservationManager.getPixelCount(x + 4, y, img));
        System.out.println(ObservationManager.getPixelCount(x + 5, y, img));
        System.out.println(ObservationManager.getPixelCount(x + 6, y, img));
        System.out.println(ObservationManager.getPixelCount(x + 7, y, img));
        System.out.println(ObservationManager.getPixelCount(x + 8, y, img));
    }
}
