package jparsec.time.calendar;

public class GregorianTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("Gregorian test");

        double jd = 2299161.5;
        long fixed = 577736;

        Gregorian h = new Gregorian(fixed);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        Gregorian h2 = new Gregorian(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Gregorian.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Gregorian.DAY_OF_WEEK_NAMES));
    }
}
