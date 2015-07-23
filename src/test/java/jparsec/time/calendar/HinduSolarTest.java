package jparsec.time.calendar;

public class HinduSolarTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Hindu Solar Test");

        int jd = 2451545;
        HinduSolar h = new HinduSolar(jd);
        System.out.println("JD " + jd + " = " + h.year + "/" + h.month + "/" + h.day);

        HinduSolar h2 = new HinduSolar(h.year, h.month, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + "/" + h2.month + "/" + h2.day);

        System.out.println(Calendar.nameFromMonth(Calendar.adjustedMod(h2.month + 1, 12), HinduOldLunar.MONTH_NAMES));
        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), HinduOldSolar.DAY_OF_WEEK_NAMES));

        System.out.println("(from sunrise)");
    }
}
