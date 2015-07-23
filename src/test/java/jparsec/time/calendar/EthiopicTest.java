package jparsec.time.calendar;

public class EthiopicTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Ethiopic Test");

        int jd = 2451545;
        Ethiopic h = new Ethiopic(jd);
        System.out.println("JD " + jd + " = " + h.year + '/' + h.month + '/' + h.day);

        Ethiopic h2 = new Ethiopic(h.year, h.month, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + '/' + h2.month + '/' + h2.day);

        System.out.println(Calendar.nameFromMonth(h2.month, Ethiopic.MONTH_NAMES));
        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Ethiopic.DAY_OF_WEEK_NAMES));
    }
}
