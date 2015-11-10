package jparsec.time.calendar;

public class ArmenianTest {
    /**
     * For unit testing only.
     * @param args Not used.
     */
    public static void main(String args[])
    {
        System.out.println("Armenian test");

        double jd = 2451545.5;
        Armenian h = new Armenian(jd);
        System.out.println("JD " + jd + " = " + h);

        Armenian h2 = new Armenian(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Armenian.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.fixed), Armenian.DAY_OF_WEEK_NAMES));
    }
}
