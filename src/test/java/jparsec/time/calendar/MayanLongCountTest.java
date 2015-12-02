package jparsec.time.calendar;

import jparsec.time.AstroDate;

public class MayanLongCountTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("MayanLongCount test");
        double jd = 2457358.5;
        MayanLongCount h = new MayanLongCount(jd);
        System.out.println("JD " + h.toJulianDay() + " = " + h.baktun + '/' + h.katun + '/' + h.tun + '/' + h.uinal + '/' + h.kin);
        MayanLongCount h2 = new MayanLongCount(h.baktun, h.katun, h.tun, h.uinal, h.kin);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.baktun + '/' + h2.katun + '/' + h2.tun + '/' + h2.uinal + '/' + h2.kin);

        try {
			MayanLongCount hNew = new MayanLongCount(h.baktun+1, 0, 0, 0, 0);
			AstroDate astro = new AstroDate(hNew.toJulianDay()-1);
			System.out.println("Last day of current baktun: "+astro.toString());
        } catch (Exception exc) {
        	exc.printStackTrace();
        }
    }
}
