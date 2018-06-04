package jparsec.astronomy;

import jparsec.astrophysics.photometry.Photometry;

public class ColorsTest {
    /**
     * Test program.
     * @param args Unused.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception
    {
        double ival0 = 1.0;
        double ival = ival0;

        System.out.println("(V-I) " +ival+ " = (B-V) " + Colors.viTobv(ival));
        System.out.println("(V-I) " +ival+ " = (V-R) " + Colors.viTovr(ival));
        ival = Colors.viTobv(ival0);

        System.out.println("(B-V) " +ival+ " = (V-I) " + Colors.bvTovi(ival));
        ival = Colors.viTovr(ival);

        System.out.println("(V-R) " +ival+ " = (V-I) " + Colors.vrTovi(ival));
        ival = ival0;

        System.out.println("(V-R) " +ival+ " = (B-V) " + Colors.vrTobv(ival));
        ival = Colors.vrTobv(ival);

        System.out.println("(B-V) " +ival+ " = (V-R) " + Colors.bvTovr(ival));
        double ovals[] = Colors.bvTychoToJohnson(ival);

        if (ovals != null) {
            System.out.println("V-VT =  " + ovals[0]);
            System.out.println("B-V  =  " + ovals[1]);
            System.out.println("B-V = (from original formula) " + Colors.bvTychoTobvJohnson(ival));
            double bAndv[] = Photometry.getApproximateJohnsonBVFromTycho(0, -ival, false);
            double bv = bAndv[0] - bAndv[1];
            System.out.println("B-V = (from Photometry class, ESA method) " + bv);
            bAndv = Photometry.getApproximateJohnsonBVFromTycho(0, -ival, true);
            bv = bAndv[0] - bAndv[1];
            System.out.println("B-V = (from Photometry class, Kidger's method) " + bv);
            System.out.println("V-Hp = " + ovals[2]);
        }
    }
}
