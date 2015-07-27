package jparsec.ephem.moons;

import jparsec.util.JPARSECException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class Mars07Test {
    /**
     * For unit testing only.
     *
     * //@param args Not used.
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

    @DataProvider (name = "Mars07_MoonPosition_1")
    Object[][] data_test_Mars07_MoonPosition_1() {
        return new Object[][] {
            { 3.391006237566556E-5, -3.851652620273116E-5, -3.7325256225233724E-5, 8.709008728560487E-4, 8.50538844319611E-4, -6.895138014801465E-5 },
        };
    }

    @Test(dataProvider = "Mars07_MoonPosition_1")
    public void test_Mars07_MoonPosition_1(double e0, double e1, double e2, double e3, double e4, double e5) throws JPARSECException {
        double jd = 2451605.00;
        double actual[] = Mars07.getMoonPosition(jd, 1, true);

        assertEquals(actual[0], e0);
        assertEquals(actual[1], e1);
        assertEquals(actual[2], e2);
        assertEquals(actual[3], e3);
        assertEquals(actual[4], e4);
        assertEquals(actual[5], e5);
    }

    @DataProvider (name = "Mars07_MoonPosition_2")
    Object[][] data_test_Mars07_MoonPosition_2() {
        return new Object[][] {
            { -8.86364950412834E-5, 8.725294013740733E-5, 9.554029153719937E-5, -5.341970935406236E-4, -5.682249168849269E-4, 2.3172688954810232E-5 },
        };
    }

    @Test(dataProvider = "Mars07_MoonPosition_2")
    public void test_Mars07_MoonPosition_2(double e0, double e1, double e2, double e3, double e4, double e5) throws JPARSECException {
        double jd = 2451605.00;
        double actual[] = Mars07.getMoonPosition(jd, 2, true);

        assertEquals(actual[0], e0);
        assertEquals(actual[1], e1);
        assertEquals(actual[2], e2);
        assertEquals(actual[3], e3);
        assertEquals(actual[4], e4);
        assertEquals(actual[5], e5);
    }
}
