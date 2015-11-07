package jparsec.time.calendar;

public class HebrewTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("Hebrew test");

        double jd = new Gregorian(2011, 9, 29).julianDate;

        Hebrew h = new Hebrew(jd);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        Hebrew h2 = new Hebrew(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Hebrew.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Hebrew.DAY_OF_WEEK_NAMES));
        //System.out.println("(until sunset)");
    }
}
