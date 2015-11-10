package jparsec.time.calendar;

public class GregorianTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("Gregorian test");

        Gregorian h = new Gregorian (2000, 1, 1);
        double julian = h.julianDate;
        long fixed = h.fixed;
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        h = new Gregorian(julian);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        Gregorian h2 = new Gregorian(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Gregorian.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Gregorian.DAY_OF_WEEK_NAMES));
    }
}
