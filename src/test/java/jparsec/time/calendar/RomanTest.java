package jparsec.time.calendar;

public class RomanTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Roman test");

        int jd = 2451545;
        Roman h = new Roman(jd);
        System.out.println("JD " + jd + " = " + h.year + "/" + h.month + "/" + h.event);

        Roman h2 = new Roman(h.year, h.month, h.event, h.count, h.leapDay);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + "/" + h2.month + "/" + h2.event);
    }
}
