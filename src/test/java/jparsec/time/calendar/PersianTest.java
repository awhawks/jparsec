package jparsec.time.calendar;

public class PersianTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Persian test");

        int jd = 2451545;
        Persian h = new Persian(jd);
        System.out.println("JD " + jd + " = " + h.year + '/' + h.month + '/' + h.day);

        Persian h2 = new Persian(h.year, h.month, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + '/' + h2.month + '/' + h2.day);

        System.out.println(Calendar.nameFromMonth(h2.month, Persian.MONTH_NAMES));
        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Persian.DAY_OF_WEEK_NAMES));
    }
}
