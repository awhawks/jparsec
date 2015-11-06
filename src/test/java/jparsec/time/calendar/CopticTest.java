package jparsec.time.calendar;

public class CopticTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("Coptic test");

        double jd = 2451545.5;
        long fixed = 730120;
        Coptic h = new Coptic(jd);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        Coptic h2 = new Coptic(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, Coptic.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.fixed), Coptic.DAY_OF_WEEK_NAMES));
    }
}
