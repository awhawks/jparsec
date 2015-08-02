package jparsec.time.calendar;

import jparsec.time.TimeFormat;

public class EcclesiasticalTest {
    /**
     * Test program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Ecclesiastical test");
        long fixed = 2009; //Calendar.fixedFromJD(2454986.0);
        System.out.println("Easter " + TimeFormat.formatJulianDayAsDate(Calendar.jdFromFixed(Ecclesiastical.easter(fixed))));
        System.out.println("Pentecost " + TimeFormat.formatJulianDayAsDate(Calendar.jdFromFixed(Ecclesiastical.pentecost(fixed))));
    }
}
