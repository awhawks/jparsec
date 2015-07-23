package jparsec.time.calendar;

public class MayanTzolkinTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Mayan Long Count Test");

        int jd = 2451545;
        MayanTzolkin h = new MayanTzolkin(jd);
        System.out.println("JD " + jd + " = " + h.number + '/' + h.name);

        MayanTzolkin h2 = new MayanTzolkin(h.number, h.name);
        System.out.println("JD " + " = " + h2.number + '/' + h2.name);
    }
}
