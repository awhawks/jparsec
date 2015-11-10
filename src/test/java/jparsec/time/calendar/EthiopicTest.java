package jparsec.time.calendar;

public class EthiopicTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("Ethiopic test");

        double jd = 2451545.5;
        long fixed = 730120;

        Ethiopic h = new Ethiopic(jd);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        Ethiopic h2 = new Ethiopic(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Ethiopic.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Ethiopic.DAY_OF_WEEK_NAMES));
    }
}
