package jparsec.ephem.moons;

public class Mars07Test {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Mars07 test");
        double jd = 2451605.00;
        double pos[] = Mars07.getMoonPosition(jd, 1, true);

        for (int i = 0; i < pos.length; i++) {
            System.out.println(pos[i]);
        }

// Should be
// 2451605.00   0.000033910062 -0.000038516526 -0.000037325256  0.000870900873  0.000850538844 -0.000068951380

        System.out.println("");
        pos = Mars07.getMoonPosition(jd, 2, true);
        for (int i = 0; i < pos.length; i++) {
            System.out.println(pos[i]);
        }

// Should be
// -0.000088636495  0.000087252940  0.000095540292 -0.000534197094 -0.000568224917  0.000023172689
    }
}
