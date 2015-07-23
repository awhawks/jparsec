package jparsec.time.calendar;

public class BahaiFutureTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("FutureBahai Test");

        int jd = 2451545;
        BahaiFuture h = new BahaiFuture(jd);
        System.out.println("JD " + jd + " = " + h.major + "/" + h.cycle + "/" + h.year + "/" + h.month + "/" + h.day);

        BahaiFuture h2 = new BahaiFuture(h.major, h.cycle, h.year, h.month, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.major + "/" + h2.cycle + "/" + h2.year + "/" + h2.month + "/" + h2.day);

        System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Bahai.DAY_OF_WEEK_NAMES));
        String month = Calendar.nameFromMonth(h2.day, Bahai.DAY_OF_MONTH_NAMES);
        month += " " + Calendar.nameFromNumber(h2.month, Bahai.DAY_OF_MONTH_NAMES);
        System.out.println(month);
        System.out.println(Calendar.nameFromMonth(h2.year, Bahai.YEAR_NAMES));

        System.out.println("(until sunset)");
    }
}
