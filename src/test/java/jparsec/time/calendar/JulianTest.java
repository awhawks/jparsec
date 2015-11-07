package jparsec.time.calendar;

public class JulianTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("Julian test");

        double jd = 2086491;

        Julian h = new Julian(jd);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        Julian h2 = new Julian(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Gregorian.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Gregorian.DAY_OF_WEEK_NAMES));
    }
}
