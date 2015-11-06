package jparsec.time.calendar;

public class BahaiFutureTest {
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
        System.out.println("BahaiFuture test");

        for (int i = 0; i < fixedDates.length; i++) {
            long fixed = fixedDates[i];
            double julian = julianDays[i];

            System.out.print('\n');
            BahaiFuture bf = new BahaiFuture(fixed);
            System.out.println("JD " + bf.julianDate + ", fixed " + bf.fixed + "; " + bf);
            BahaiFuture bj = new BahaiFuture(julian);
            System.out.println("JD " + bj.julianDate + ", fixed " + bj.fixed + "; " + bj);

            BahaiFuture bf2 = new BahaiFuture(bf.major, bf.cycle, (int) bf.year, bf.month, bf.day);
            System.out.println("JD " + bf2.julianDate + ", fixed " + bf2.fixed + "; " + bf2);
            BahaiFuture bj2 = new BahaiFuture(bj.major, bj.cycle, (int) bj.year, bj.month, bj.day);
            System.out.println("JD " + bj2.julianDate + ", fixed " + bj2.fixed + "; " + bj2);
        }

//        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.fixed), Bahai.DAY_OF_WEEK_NAMES));
//        String month = Calendar.nameFromMonth(h2.day, Bahai.DAY_OF_MONTH_NAMES);
//        month += " " + Calendar.nameFromNumber(h2.month, Bahai.DAY_OF_MONTH_NAMES);
//        System.out.println(month);
//        System.out.println(Calendar.nameFromMonth(h2.year, Bahai.YEAR_NAMES));
    }
}
