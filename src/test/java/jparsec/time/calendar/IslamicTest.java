package jparsec.time.calendar;

public class IslamicTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     * @throws Exception If an error occurs.
     */
    public static void main(final String args[]) throws Exception {
        System.out.println("Islamic test");

        double jd = new Gregorian(2000, 1, 1).julianDate;
        //long fixed = new Gregorian(2000, 1, 1).fixed;

        Islamic h = new Islamic(jd);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        Islamic h2 = new Islamic(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Islamic.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Islamic.DAY_OF_WEEK_NAMES));
        //System.out.println("(until sunset)");
    }
}
