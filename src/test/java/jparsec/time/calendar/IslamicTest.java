package jparsec.time.calendar;

import jparsec.time.AstroDate;
import jparsec.util.JPARSECException;

public class IslamicTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws JPARSECException {
        System.out.println("Islamic test");

        int jd = (int) (new AstroDate(2009, 6, 24, 22, 0, 0)).jd();
        Islamic h = new Islamic(jd);
        System.out.println("JD " + jd + " = " + h.year + '/' + h.month + '/' + h.day);

        Islamic h2 = new Islamic(h.year, h.month, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + '/' + h2.month + '/' + h2.day);

        System.out.println(Calendar.nameFromMonth(h2.month, Islamic.MONTH_NAMES));
        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Islamic.DAY_OF_WEEK_NAMES));
        System.out.println("(until sunset)");
    }
}
