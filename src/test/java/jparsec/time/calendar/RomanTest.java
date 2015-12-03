package jparsec.time.calendar;

public class RomanTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Roman test");
        double jd = 2457359.5;
        Roman h = new Roman(jd);
        System.out.println("JD " + h.toJulianDay() + " / " + h.toFixed() + " = " + h.year + '/' + h.month + '/' + h.event + '/' + h.count);
        Roman h2 = new Roman(h.year, h.month, h.event, h.count, h.leapDay);
        System.out.println("JD " + h2.toJulianDay() + " / " + h.toFixed() + " = " + h2.year + '/' + h2.month + '/' + h2.event + '/' + h2.count);
        
        System.out.println("Count name: "+Calendar.nameFromNumber(h.count, Roman.COUNT_NAMES));
        System.out.println("Kalends name: "+Calendar.nameFromNumber(h.event, Roman.EVENT_NAMES));
    }
}
