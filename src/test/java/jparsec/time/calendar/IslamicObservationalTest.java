package jparsec.time.calendar;

public class IslamicObservationalTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("IslamicObservational test");
        int jd = 2451545;
        IslamicObservational h = new IslamicObservational(jd);
        System.out.println("JD " + jd + " = " + h.year + '/' + h.month + '/' + h.day);
        IslamicObservational h2 = new IslamicObservational(h.year, h.month, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + '/' + h2.month + '/' + h2.day);
        System.out.println(Calendar.nameFromMonth(h2.month, Islamic.MONTH_NAMES));
        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Islamic.DAY_OF_WEEK_NAMES));
    }
}
