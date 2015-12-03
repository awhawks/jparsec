package jparsec.time.calendar;

public class HinduOldLunarTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("HinduOldLunar test");
        double jd = 2457359.5;
        HinduOldLunar h = new HinduOldLunar(jd);
        System.out.println("JD " + h.toJulianDay() + " / " + h.toFixed() + " = " + h.year + '/' + h.month + '/' + h.leapMonth + '/' + h.day);
        HinduOldLunar h2 = new HinduOldLunar(h.year, h.month, h.leapMonth, h.day);
        System.out.println("JD " + h2.toJulianDay() + " / " + h.toFixed() + " = " + h2.year + '/' + h2.month + '/' + h2.leapMonth + '/' + h2.day);
        System.out.println(Calendar.nameFromMonth(h2.month, HinduOldLunar.MONTH_NAMES));
        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), HinduOldLunar.DAY_OF_WEEK_NAMES));
        System.out.println("(from sunrise)");
    }
}
