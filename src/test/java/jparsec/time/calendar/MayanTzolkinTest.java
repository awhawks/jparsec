package jparsec.time.calendar;

public class MayanTzolkinTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        double jd = 2457359.5;
        
        System.out.println("MayanHaab test");
        MayanHaab h = new MayanHaab(jd);
        System.out.println("JD " + jd + " = " + h.month + '/' + h.day);
        MayanHaab h2 = new MayanHaab(h.month, h.day);
        System.out.println("JD " + jd + " = " + h2.month + '/' + h2.day);
        
        System.out.println("MayanTzolkin test");
        MayanTzolkin t = new MayanTzolkin(jd);
        System.out.println("JD " + jd + " = " + t.month + '/' + t.day);
        MayanTzolkin t2 = new MayanTzolkin(t.month, t.day);
        System.out.println("JD " + jd + " = " + t2.month + '/' + t2.day);
        
        System.out.println("");
        System.out.println("" + h2.day + '/' + Calendar.nameFromMonth(h2.month, MayanHaab.MONTH_NAMES));
        System.out.println("" + t2.day + '/' + Calendar.nameFromMonth(t2.month, MayanTzolkin.MONTH_NAMES));
    }
}
