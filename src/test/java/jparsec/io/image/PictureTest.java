package jparsec.io.image;

public class PictureTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Picture test");
        String path = "/home/alonso/documentos/presentaciones/2011/tesis/img/datacube1a.png";
        int w = 440, h = 0;

        Picture p1 = new Picture(path);
        p1.getScaledInstance(w, h, true);
        p1.show("Default multi-step scaling");

        Picture p2 = new Picture(path);
        p2.getScaledInstanceUsingSplines(w, h, true);
        p2.show("Spline interpolated image");

        Picture p3 = new Picture("/home/alonso/java/librerias/bayesian/noiseExample.png");
        p3.show("with noise");
        Picture p4 = new Picture("/home/alonso/java/librerias/bayesian/noiseExample.png");
        p4.denoise(1);
        p4.show("without noise");
        Picture p5 = new Picture("/home/alonso/java/librerias/bayesian/noiseExample.png");

        for (int i = 0; i < 3; i++) {
            p5.denoise(1);
        }

        p5.show("even with less noise");
    }
}
