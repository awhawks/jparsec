package jparsec.time.calendar;

public class FrenchModifiedTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("FrenchModified test");

        double jd = new Gregorian(1792, 9, 22).julianDate;
        long fixed = new Gregorian(1792, 9, 22).fixed;

        FrenchModified h = new FrenchModified(fixed);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        FrenchModified h2 = new FrenchModified(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        int decadi = h2.getDecadi();

        if (decadi != -1) {
            System.out.println("Decadi " + Calendar.nameFromNumber(decadi, French.DECADE_NAMES));
        }

        System.out.println(Calendar.nameFromMonth(h2.month, French.MONTH_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.getDayOfWeek(), French.DAY_OF_WEEK_NAMES));
    }
}
