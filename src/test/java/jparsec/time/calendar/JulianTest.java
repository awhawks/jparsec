package jparsec.time.calendar;

public class JulianTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("Julian test");

        Julian h = new Julian(-3102, 2, 18);
        //Julian h = new Julian (1999, 12, 19);
        double julian = h.julianDate;
        long fixed = h.fixed;
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        h = new Julian(julian);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        Julian h2 = new Julian(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Julian.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Julian.DAY_OF_WEEK_NAMES));
    }
}
