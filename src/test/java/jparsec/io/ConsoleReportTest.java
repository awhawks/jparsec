package jparsec.io;

import jparsec.graph.DataSet;

public class ConsoleReportTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("ConsoleReport test");

        String val[] = DataSet.toStringValues(new double[] {
                -123.123, -12, 12, 321.1234567
        });
        String format = "1x, f5.3, 3x, 2I3, 1x, A5";
        String out = ConsoleReport.formatAsFortran(val, format, true);
        System.out.println(out);
    }
}
