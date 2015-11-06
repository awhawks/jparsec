package jparsec.time.calendar;

public class ISOTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("ISO test");

        double jd = 2451545.5;
        long fixed = 730120;

        ISO h = new ISO(fixed);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        ISO h2 = new ISO(h.year, h.week, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);
    }
}
