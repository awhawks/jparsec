package jparsec.time.calendar;

public class PersianTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("Persian test");

        double jd = 2451545.5;

        Persian h = new Persian(jd);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        Persian h2 = new Persian(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Persian.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Persian.DAY_OF_WEEK_NAMES));
    }
}
