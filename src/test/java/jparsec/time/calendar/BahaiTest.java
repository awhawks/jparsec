package jparsec.time.calendar;

public class BahaiTest {
    private final static double[] julianDays = {
        new Gregorian(1844,  3, 21).julianDate, // Julian day 2394647.5 is Bahai 1/1/1/1/1
        new Gregorian(2000,  1,  1).julianDate, // Julian day 2451544.5 is Bahai 1/9/4/16/2
        new Gregorian(2015,  3, 20).julianDate, // Julian day 2457101.5 is Bahai 1/9/19/19/19 (VE - 1 day)
        new Gregorian(2015,  3, 21).julianDate, // Julian day 2457102.5 is Bahai 1/10/1/1/1 (Vernal Equinox)
        new Gregorian(2015,  4,  9).julianDate, // Julian day 2457121.5 is Bahai 1/10/1/2/1 (VE + 1 Vahid)
        new Gregorian(2205,  3, 21).julianDate, // Julian day 2526498.5 is Bahai 2/1/1/1/1
    };

    private final static long[] fixedDates = {
        new Gregorian(1844,  3, 21).fixed, // Fixed day 673222 is Bahai 1/1/1/1/2
        new Gregorian(2000,  1,  1).fixed, // Fixed day 730120 is Bahai 1/9/4/16/2
        new Gregorian(2015,  3, 20).fixed, // Fixed day 735677 is Bahai 1/9/19/19/19 (VE - 1 day)
        new Gregorian(2015,  3, 21).fixed, // Fixed day 735678 is Bahai 1/10/1/1/1 (Vernal Equinox)
        new Gregorian(2015,  4,  9).fixed, // Fixed day 735697 is Bahai 1/10/1/2/1 (VE + 1 Vahid)
        new Gregorian(2205,  3, 21).fixed, // Fixed day 805074 is Bahai 2/1/1/1/1
    };

    /**
     * For unit testing only.
     * @param args Not used.
     */
    public static void main(final String args[])
    {
        System.out.println("Bahai test");

        for (int i = 0; i < fixedDates.length; i++) {
            long fixed = fixedDates[i];
            double julian = julianDays[i];

            System.out.print('\n');
            Bahai bf = new Bahai(fixed);
            System.out.println("JD " + bf.julianDate + ", fixed " + bf.fixed + "; " + bf);
            Bahai bj = new Bahai(julian);
            System.out.println("JD " + bj.julianDate + ", fixed " + bj.fixed + "; " + bj);

//            Bahai bf2 = new Bahai(bf.major, bf.cycle, (int) bf.year, bf.month, bf.day);
//            System.out.println("JD " + bf2.julianDate + ", fixed " + bf2.fixed + "; " + bf2);
//            Bahai bj2 = new Bahai(bj.major, bj.cycle, (int) bj.year, bj.month, bj.day);
//            System.out.println("JD " + bj2.julianDate + ", fixed " + bj2.fixed + "; " + bj2);
        }
    }
}
