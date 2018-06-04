package jparsec.ephem.moons;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class L1Test {
    /**
     * Testing program.
     * @param args Not used.
     */
    public static void main(String args[]) {
        for (int nsat = 1; nsat <= 4; nsat++) {
            /*
            double ELEM[] = L1.L1_theory_old(2451545, nsat, 1, 1);
            System.out.println(
                    Double.toString(ELEM[0]) + ',' + Double.toString(ELEM[1]) + ',' + Double.toString(ELEM[2]) + ',' +
                            Double.toString(ELEM[3]) + ',' + Double.toString(ELEM[4]) + ',' + Double.toString(ELEM[5]));
// Should be
// Io
//			  2.671999370920431E-003  7.644018403387422E-004  4.087344808808269E-004
//			  -3.116203340625001E-003  8.645679572984422E-003  4.066210333795641E-003
// Europa
//			  -3.751373844521062E-003 -2.136179970327756E-003 -1.056765216826830E-003
//			   4.310591732986133E-003 -6.143199976514738E-003 -2.800434328620005E-003
// Ganymede
//			  -5.490036250442612E-003 -4.112229247907583E-003 -2.033821277493470E-003
//			   4.036147912130572E-003 -4.364866691392988E-003 -2.037111499364415E-003
// Callisto
//			   2.172082907229073E-003  1.118792302205555E-002  5.322275059416266E-003
//			  -4.662583658656747E-003  7.976685330152526E-004  3.092058747362411E-004
            */
            double ELEM[] = L1.L1_theory(2441669, nsat);
            System.out.println(
                    Double.toString(ELEM[0]) + ',' + Double.toString(ELEM[1]) + ',' + Double.toString(ELEM[2]) + ',' +
                            Double.toString(ELEM[3]) + ',' + Double.toString(ELEM[4]) + ',' + Double.toString(ELEM[5]));
// Should be
//-0.012136434071684304/0.003374929866115463/0.0014374540796792653/-0.0013542058539217942/-0.0040620093629751275/-0.0019470410148563256
            System.out.print('\n');
        }
    }

    @Test
    @DataProvider (name = "data_L1_theory_old")
    Object[][] data_L1_theory_old() {
        return new Object[][] {
            { "Io",       1, 0.0026719993709204337, 7.64401840338735E-4, 4.087344808808235E-4, -0.003116203340624985, 0.008645679572984427, 0.004066210333795643 },
            { "Europa",   2, -0.0037513738445210487, -0.00213617997032777, -0.0010567652168268359, 0.00431059173298615, -0.006143199976514734, -0.0028004343286200033 },
            { "Ganymede", 3, -0.005490036250442614, -0.004112229247907584, -0.002033821277493471, 0.004036147912130571, -0.004364866691392988, -0.0020371114993644147 },
            { "Callisto", 4, 0.002172082907229078, 0.011187923022055558, 0.0053222750594162675, -0.004662583658656746, 7.976685330152533E-4, 3.092058747362414E-4 },
        };
    }

    @Test (dataProvider = "data_L1_theory_old")
    public void test_L1_theory_old(String moon, int nsat, double e0, double e1, double e2, double e3, double e4, double e5) {
        double actual[] = L1.L1_theory_old(2451545, nsat, 1, 1);
        assertEquals(actual[0], e0);
        assertEquals(actual[1], e1);
        assertEquals(actual[2], e2);
        assertEquals(actual[3], e3);
        assertEquals(actual[4], e4);
        assertEquals(actual[5], e5);
    }

    @Test
    @DataProvider (name = "data_L1_theory")
    Object[][] data_L1_theory() {
        return new Object[][] {
            /* // the first 2 lines do not work:
            { "Io",       1, -0.0013352855985781999, -0.002234056442768123, -0.0010840182414343725, 0.008797690289295283, -0.004367393709211122, -0.0019441988167382877 },
            { "Europa",   2, -0.004354707944938242, -0.0010601058761054315, -5.414009223862813E-4, 0.0021283082642296197, -0.006885963328631246, -0.0032018881378558326 },
            { "Ganymede", 3, 8.471345317342002E-4, 0.006419261227550793, 0.0030517649213347973, -0.006237247865084319, 6.950416261286821E-4, 2.501269045950717E-4 },
            { "Callisto", 4, -0.012136434071684304, 0.003374929866115463, 0.0014374540796792653, -0.0013542058539217942, -0.0040620093629751275, -0.0019470410148563256 },
            */
            { "Io",       1, 0.002671998719532807, 7.644016540366141E-4, 4.087343812603128E-4, -0.003116203720619223, 0.008645680626748566, 0.004066210829396273 },
            { "Europa",   2, -0.0037513763632646088, -0.002136181404545611, -0.0010567659263345758, 0.004310590285813511, -0.006143197914255824, -0.002800433388521186 },
            { "Ganymede", 3, -0.005490036250390494, -0.004112229247967984, -0.00203382127752096, 0.004036147912185888, -0.004364866691350376, -0.0020371114993420546 },
            { "Callisto", 4, 0.002172082907249288, 0.01118792302205131, 0.0053222750594190725, -0.004662583658652817, 7.976685330204668E-4, 3.092058747414288E-4 },
        };
    }

    @Test (dataProvider = "data_L1_theory")
    public void test_L1_theory(String moon, int nsat, double e0, double e1, double e2, double e3, double e4, double e5) {
        double ELEM[] = L1.L1_theory(2451545, nsat);

        assertEquals(ELEM[0], e0);
        assertEquals(ELEM[1], e1);
        assertEquals(ELEM[2], e2);
        assertEquals(ELEM[3], e3);
        assertEquals(ELEM[4], e4);
        assertEquals(ELEM[5], e5);
    }
}
