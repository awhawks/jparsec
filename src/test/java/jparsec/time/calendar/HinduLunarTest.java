package jparsec.time.calendar;

public class HinduLunarTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("HinduLunar test");
        double jd = 2457359.5;
        HinduLunar h = new HinduLunar(jd);
        System.out.println("JD " + h.toJulianDay() + " / " + h.toFixed() + " = " + h.year + '/' + h.month + '/' + h.leapMonth + '/' + h.day + '/' + h.leapDay);
        HinduLunar h2 = new HinduLunar(h.year, h.month, h.leapMonth, h.day, h.leapDay);
        System.out.println("JD " + h2.toJulianDay() + " / " + h.toFixed() + " = " + h2.year + '/' + h2.month + '/' + h2.leapMonth + '/' + h2.day + '/' + h2.leapDay);
        System.out.println(Calendar.nameFromMonth(h2.month, HinduOldLunar.MONTH_NAMES));
        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), HinduOldLunar.DAY_OF_WEEK_NAMES));
        System.out.println("(from sunrise)");
    }
}
