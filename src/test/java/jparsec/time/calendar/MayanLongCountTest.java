package jparsec.time.calendar;

public class MayanLongCountTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("MayanLongCount test");
        int jd = 2451545;
        MayanLongCount h = new MayanLongCount(jd);
        System.out.println("JD " + jd + " = " + h.baktun + '/' + h.katun + '/' + h.tun + '/' + h.uinal + '/' + h.kin);
        MayanLongCount h2 = new MayanLongCount(h.baktun, h.katun, h.tun, h.uinal, h.kin);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.baktun + '/' + h2.katun + '/' + h2.tun + '/' + h2.uinal + '/' + h2.kin);
    }
}
