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
        int jd = (int) (new AstroDate(2010, 1, 12)).jd();
        Egyptian h = new Egyptian(jd);
        System.out.println("JD " + jd + " = " + h.year + '/' + h.month + '/' + h.day);
        Egyptian h2 = new Egyptian(h.year, h.month, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + '/' + h2.month + '/' + h2.day);
        System.out.println(Calendar.nameFromMonth(h2.month, Egyptian.MONTH_NAMES));
        //System.out.println(CalendarGenericConversion.getDayOfWeekName(jd, CalendarGenericConversion.EGYPTIAN));
    }
}
