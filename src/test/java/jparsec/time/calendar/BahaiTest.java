package jparsec.time.calendar;

public class BahaiTest {
    private final static double[] julianDays = {
            2451544.5, // Gregorian 2000/01/01, is Bahai 1/9/4/16/2
            2457102.5, // Gregorian 2015/03/21 (Vernal Equinox), is Bahai 1/10/1/1/1
            2457101.5, // Gregorian 2015/03/20 (day before VE), is Bahai 1/9/19/19/19
            2457121.5  // Gregorian 2015/04/09 (VE + 19 days), is Bahai 1/10/1/2/1
    };

    private final static long[] fixedDates = {
            730119, // Gregorian 2000/01/01, is Bahai 1/9/4/16/2
            735677, // Gregorian 2015/03/21 (Vernal Equinox), is Bahai 1/10/1/1/1
            735676, // Gregorian 2015/03/20 (day before VE), is Bahai 1/9/19/19/19
            735696  // Gregorian 2015/04/09 (19 days after VE), is Bahai 1/10/1/2/1
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

            Bahai bf2 = new Bahai(bf.major, bf.cycle, (int) bf.year, bf.month, bf.day);
            System.out.println("JD " + bf2.julianDate + ", fixed " + bf2.fixed + "; " + bf2);
            Bahai bj2 = new Bahai(bj.major, bj.cycle, (int) bj.year, bj.month, bj.day);
            System.out.println("JD " + bj2.julianDate + ", fixed " + bj2.fixed + "; " + bj2);
        }
    }
}
