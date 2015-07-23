package jparsec.time.calendar;

public class FrenchModifiedTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Modified French Test");

        int jd = 1757754;
        FrenchModified h = new FrenchModified(jd);
        System.out.println("JD " + jd + " = " + h.year + "/" + h.month + "/" + h.day);

        FrenchModified h2 = new FrenchModified(h.year, h.month, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + "/" + h2.month + "/" + h2.day);
        int decadi = h2.getDecadi();

        if (decadi != -1) {
            System.out.println("Decadi " + Calendar.nameFromNumber(decadi, French.DECADE_NAMES));
        }

        System.out.println(Calendar.nameFromMonth(h2.month, French.MONTH_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.getDayOfWeek(), French.DAY_OF_WEEK_NAMES));
    }
}
