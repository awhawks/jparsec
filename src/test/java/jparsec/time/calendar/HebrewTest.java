package jparsec.time.calendar;

public class HebrewTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Hebrew Test");

        try {
            int jd = (int) (new jparsec.time.AstroDate(2011, 9, 29, 12, 0, 0)).jd();
            Hebrew h = new Hebrew(jd);
            System.out.println("JD " + jd + " = " + h.year + "/" + h.month + "/" + h.day);

            Hebrew h2 = new Hebrew(h.year, h.month, h.day);
            System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + "/" + h2.month + "/" + h2.day);

            System.out.println(Calendar.nameFromMonth(h2.month, Hebrew.MONTH_NAMES));
            System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Hebrew.DAY_OF_WEEK_NAMES));
            System.out.println("(until sunset)");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
