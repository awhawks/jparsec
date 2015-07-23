package jparsec.io.image;

public class PictureTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception{
        System.out.println("Picture test");
        //Picture pio = new Picture(Picture.createDesktopImage());
        //pio.toGrayScale();
        //Graphics g = pio.getImage().getGraphics();
        //g.setColor(Color.RED);
        //g.drawOval(100, 100, 20, 20);
        //pio.show("IMAGE");

        //int w = 450, h = 0;
        //Picture p = new Picture("/home/alonso/documentos/presentaciones/2011/tesis/img/atmosferat.png");
        //p.getScaledInstanceUsingLanczos(w, h, true);
        //p.show("Original image resized with Picture class");
        //p.write("/home/alonso/test.png");

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

        // A useful example that creates thumbnails and quality reduced versions
        // of all the images in certain directory.
/*
        String path = "/home/alonso/otros/fotos/LesHouches2007/";
        String files[] = FileIO.getFiles(path);
        // Scale to 150 pixels of width.
        int widthThumbnail = 150, heightThumbnail = 0;
        String pathThumbnail = path + "thumbnails/";
        double qualityThumbnail = 0.7;
        // int widthPhoto = 3072, heightPhoto = 2048;
        String pathPhoto = path + "photos/";
        double qualityPhoto = 0.7;
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
            Picture pbig = new Picture(files[i]);
            Picture psmall = new Picture(files[i]);
            psmall.getScaledInstance(widthThumbnail, heightThumbnail, true);

            String fileName = files[i].substring(files[i].lastIndexOf("/") + 1);
            psmall.writeAsJPEG(pathThumbnail + fileName, qualityThumbnail);
            pbig.writeAsJPEG(pathPhoto + fileName, qualityPhoto);
        }
*/
    }
}
