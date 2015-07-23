package jparsec.io.image;

import jparsec.util.JPARSECException;

public class ImageSplineTransformTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("ImageSplineTransform test");

        double image[][] = new double[][] {
                new double[] { 1, 2, 3 },
                new double[] { 3, 4, 5 },
                new double[] { 5, 6, 7 },
                new double[] { 7, 8, 9 },
        };

        System.out.println(image.length + '/' + image[0].length);
        System.out.println(image[0][0] + '/' + image[1][0] + '/' + image[2][0]); //+'/'+image[2][0]);
        System.out.println(image[0][1] + '/' + image[1][1] + '/' + image[2][1]); //+'/'+image[2][1]);
        System.out.println(image[0][2] + '/' + image[1][2] + '/' + image[2][2]); //+'/'+image[2][2]);
        //System.out.println(image[0][3]+'/'+image[1][3]+'/'+image[2][3]); //+'/'+image[3][3]);

        try {
            ImageSplineTransform interp = new ImageSplineTransform(3, image);
            double px = 1.5, py = 1.5;
            double pz = interp.interpolate(px, py);

            System.out.println(px + " / " + py + " / " + pz);
            pz = interp.fastBilinearInterpolation(px, py);
            System.out.println(px + " / " + py + " / " + pz);

            //interp.recenter(3, 3);
            interp.resize(3, 3);
            interp.rotate(Math.PI * 0.5);
            image = interp.getImage();

            System.out.println(image.length + '/' + image[0].length);
            System.out.println(image[0][0] + '/' + image[1][0] + '/' + image[2][0]); //+'/'+image[2][0]);
            System.out.println(image[0][1] + '/' + image[1][1] + '/' + image[2][1]); //+'/'+image[2][1]);
            System.out.println(image[0][2] + '/' + image[1][2] + '/' + image[2][2]); //+'/'+image[2][2]);
            //System.out.println(image[0][3]+'/'+image[1][3]+'/'+image[2][3]); //+'/'+image[3][3]);
            //System.out.println(image.length+'/'+image[0].length);
            //System.out.println(image[0][0]+'/'+image[1][0]+'/'+image[2][0]+'/'+image[3][0]+'/'+image[4][0]+'/'+image[5][0]); //+'/'+image[6][0]+'/'+image[7][0]+'/'+image[8][0]);
            //System.out.println(image[0][1]+'/'+image[1][1]+'/'+image[2][1]+'/'+image[3][1]+'/'+image[4][1]+'/'+image[5][1]); //+'/'+image[6][1]+'/'+image[7][1]+'/'+image[8][1]);
            //System.out.println(image[0][2]+'/'+image[1][2]+'/'+image[2][2]+'/'+image[3][2]+'/'+image[4][2]+'/'+image[5][2]); //+'/'+image[6][2]+'/'+image[7][2]+'/'+image[8][2]);
            //System.out.println(image[0][3]+'/'+image[1][3]+'/'+image[2][3]+'/'+image[3][3]+'/'+image[4][3]+'/'+image[5][3]); //+'/'+image[6][3]+'/'+image[7][3]+'/'+image[8][3]);
            //System.out.println(image[0][4]+'/'+image[1][4]+'/'+image[2][4]+'/'+image[3][4]+'/'+image[4][4]+'/'+image[5][4]); //+'/'+image[6][4]+'/'+image[7][4]+'/'+image[8][4]);
            //System.out.println(image[0][5]+'/'+image[1][5]+'/'+image[2][5]+'/'+image[3][5]+'/'+image[4][5]+'/'+image[5][5]); //+'/'+image[6][5]+'/'+image[7][5]+'/'+image[8][5]);
            //System.out.println(image[0][6]+'/'+image[1][6]+'/'+image[2][6]+'/'+image[3][6]+'/'+image[4][6]+'/'+image[5][6]); //+'/'+image[6][6]+'/'+image[7][6]+'/'+image[8][6]);
            //System.out.println(image[0][7]+'/'+image[1][7]+'/'+image[2][7]+'/'+image[3][7]+'/'+image[4][7]+'/'+image[5][7]); //+'/'+image[6][7]+'/'+image[7][7]+'/'+image[8][7]);
            //System.out.println(image[0][8]+'/'+image[1][8]+'/'+image[2][8]+'/'+image[3][8]+'/'+image[4][8]+'/'+image[5][8]); //+'/'+image[6][8]+'/'+image[7][8]+'/'+image[8][8]);

            px = 1.333;
            py = 1.333;
            pz = interp.interpolate(px, py);
            System.out.println(px + " / " + py + " / " + pz);
            pz = interp.fastBilinearInterpolation(px, py);
            System.out.println(px + " / " + py + " / " + pz);
        } catch (JPARSECException e) {
            JPARSECException.showException(e);
        }
    }
}
