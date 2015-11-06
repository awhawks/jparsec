package jparsec.time.calendar;

public class FrenchTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("French test");

        double jd = Gregorian.toJulianDay(1792, 9, 22);
        long fixed = Gregorian.toFixed(1792, 9, 22);

        French h = new French(fixed);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        French h2 = new French(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        int decadi = h2.getDecadi();

        if (decadi != -1) {
            System.out.println("Decadi " + Calendar.nameFromNumber(decadi, French.DECADE_NAMES));
        }

        System.out.println(Calendar.nameFromMonth(h2.month, French.MONTH_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.getDayOfWeek(), French.DAY_OF_WEEK_NAMES));
    }
}
