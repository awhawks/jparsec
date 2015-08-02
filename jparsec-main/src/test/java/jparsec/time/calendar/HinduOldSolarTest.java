package jparsec.time.calendar;

public class HinduOldSolarTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("HinduOldSolar Test");
        int jd = 2451545;
        HinduOldSolar h = new HinduOldSolar(jd);
        System.out.println("JD " + jd + " = " + h.year + '/' + h.month + '/' + h.day);
        HinduOldSolar h2 = new HinduOldSolar(h.year, h.month, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + '/' + h2.month + '/' + h2.day);
        System.out.println(Calendar.nameFromMonth(h2.month, HinduOldSolar.MONTH_NAMES));
        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), HinduOldSolar.DAY_OF_WEEK_NAMES));
        System.out.println("(from sunrise)");
    }
}
