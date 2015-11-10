package jparsec.time.calendar;

public class PersianArithmeticTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("PersianArithmetic test");

        double jd = 2451545.5;

        PersianArithmetic h = new PersianArithmetic(jd);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        PersianArithmetic h2 = new PersianArithmetic(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Persian.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Persian.DAY_OF_WEEK_NAMES));
    }
}
