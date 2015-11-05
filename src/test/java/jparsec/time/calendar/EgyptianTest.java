package jparsec.time.calendar;

import jparsec.time.AstroDate;

public class EgyptianTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Egyptian Test");
        double jd = new AstroDate(2010, 1, 12).jd();
        Egyptian h = new Egyptian(jd);
        System.out.println("JD " + jd + " = " + h);
        Egyptian h2 = new Egyptian(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + " = " + h2);
        System.out.println(Calendar.nameFromMonth(h2.month, Egyptian.MONTH_NAMES));
        //System.out.println(CalendarGenericConversion.getDayOfWeekName(jd, CalendarGenericConversion.EGYPTIAN));
    }
}
