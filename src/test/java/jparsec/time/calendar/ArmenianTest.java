package jparsec.time.calendar;

public class ArmenianTest {
    /**
     * For unit testing only.
     * @param args Not used.
     */
    public static void main(String args[])
    {
        System.out.println("Armenian test");

        double jd = 2457358.5;
        Armenian h = new Armenian(jd);
        System.out.println("JD " + h.getJulianDate() + " / " + h.getFixed() + " = " + h);

        Armenian h1 = new Armenian(h.getFixed());
        System.out.println("JD " + h1.getJulianDate() + " / " + h1.getFixed() + " = " + h1);

        Armenian h2 = new Armenian(h.year, h.month, h.day);
        System.out.println("JD " + h2.getJulianDate() + " / " + h2.getFixed() + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Armenian.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.fixed), Armenian.DAY_OF_WEEK_NAMES));
    }
}
