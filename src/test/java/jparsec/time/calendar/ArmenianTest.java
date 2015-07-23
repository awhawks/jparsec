package jparsec.time.calendar;

public class ArmenianTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Armenian Test");

        int jd = 2451545;
        Armenian h = new Armenian(jd);
        System.out.println("JD " + jd + " = " + h.year + "/" + h.month + "/" + h.day);

        Armenian h2 = new Armenian(h.year, h.month, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + "/" + h2.month + "/" + h2.day);

        System.out.println(Calendar.nameFromMonth(h2.month, Armenian.MONTH_NAMES));
        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Armenian.DAY_OF_WEEK_NAMES));
    }
}
