package jparsec.time.calendar;

public class CopticTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Coptic Test");

        int jd = 2451545;
        Coptic h = new Coptic(jd);
        System.out.println("JD " + jd + " = " + h.year + "/" + h.month + "/" + h.day);

        Coptic h2 = new Coptic(h.year, h.month, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + "/" + h2.month + "/" + h2.day);

        System.out.println(Calendar.nameFromMonth(h2.month, Coptic.MONTH_NAMES));
        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Coptic.DAY_OF_WEEK_NAMES));
    }
}
