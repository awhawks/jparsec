package jparsec.time.calendar;

public class HinduOldSolarTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("HinduOldSolar Test");

        double jd = 2451545.5;

        HinduOldSolar h = new HinduOldSolar(jd);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        HinduOldSolar h2 = new HinduOldSolar(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, HinduOldSolar.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), HinduOldSolar.DAY_OF_WEEK_NAMES));
        //System.out.println("(from sunrise)");
    }
}
