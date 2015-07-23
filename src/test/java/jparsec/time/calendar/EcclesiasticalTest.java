package jparsec.time.calendar;

import jparsec.time.TimeFormat;

public class EcclesiasticalTest {
    /**
     * Test program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) {
        System.out.println("Ecclesiastical test");

        try {
            long fixed = 2009; //Calendar.fixedFromJD(2454986.0);
            System.out.println("Easter " + TimeFormat.formatJulianDayAsDate(Calendar.jdFromFixed(Ecclesiastical.easter(fixed))));
            System.out.println("Pentecost " + TimeFormat.formatJulianDayAsDate(Calendar.jdFromFixed(Ecclesiastical.pentecost(fixed))));
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
