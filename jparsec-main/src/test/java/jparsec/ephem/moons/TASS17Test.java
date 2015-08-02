package jparsec.ephem.moons;

import jparsec.io.ConsoleReport;

public class TASS17Test {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        double jd = 2421677.4;
        double e[] = TASS17.TASS17_theory(jd, 1, false);

        ConsoleReport.doubleArrayReport(e, "f2.12");
    }
}
