package jparsec.time.calendar;

public class ISOTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("ISO test");

        double jd = 2457359.4;
        long fixed = 735934;

        ISO h = new ISO(fixed);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        ISO h2 = new ISO(h.year, h.week, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);
        
        ISO h3 = new ISO(jd);
        System.out.println("JD " + h3.julianDate + ' ' + h3.fixed + " = " + h3);
    }
}
