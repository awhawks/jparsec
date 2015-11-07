package jparsec.time.calendar;

public class IslamicObservationalTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("IslamicObservational test");

        double jd = new Gregorian(2000, 1, 1).julianDate;
        long fixed = new Gregorian(2000, 1, 1).fixed;

        IslamicObservational h = new IslamicObservational(jd);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        IslamicObservational h2 = new IslamicObservational(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Islamic.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Islamic.DAY_OF_WEEK_NAMES));
    }
}
