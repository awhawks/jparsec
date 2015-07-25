package jparsec.time.calendar;

public class ISOTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("ISO test");
        int jd = 2451545;
        ISO h = new ISO(jd);
        System.out.println("JD " + jd + " = " + h.year + '/' + h.week + '/' + h.day);
        ISO h2 = new ISO(h.year, h.week, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + '/' + h2.week + '/' + h2.day);
    }
}
