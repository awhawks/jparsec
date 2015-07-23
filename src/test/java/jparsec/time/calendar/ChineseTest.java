package jparsec.time.calendar;

public class ChineseTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Chinese Test");

        int jd = 2451545;
        Chinese h = new Chinese(jd);
        System.out.println("JD " + jd + " = " + h.cycle + "/" + h.year + "/" + h.month + "/" + h.leapMonth + "/" + h.day);

        Chinese h2 = new Chinese(h.cycle, h.year, h.month, h.leapMonth, h.day);
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.cycle + "/" + h2.year + "/" + h2.month + "/" + h2.leapMonth + "/" + h2.day);

        ChineseName name = Chinese.nameOfYear(h2.year);
        String year = Calendar.nameFromMonth(name.stem, Chinese.YEAR_STEM_NAMES) + "-" + Calendar.nameFromMonth(name.branch, Chinese.YEAR_BRANCH_NAMES);
        System.out.println(year);

        name = Chinese.nameOfMonth(h2.year, h2.month);
        String month = Calendar.nameFromMonth(name.stem, Chinese.YEAR_STEM_NAMES);
        month += "-" + Calendar.nameFromMonth(name.branch, Chinese.YEAR_BRANCH_NAMES);
        System.out.println(month);

        name = Chinese.nameOfDay(h2.cycle);
        String day = Calendar.nameFromMonth(name.stem, Chinese.YEAR_STEM_NAMES);
        day += "-" + Calendar.nameFromMonth(name.branch, Chinese.YEAR_BRANCH_NAMES);
        System.out.println(day);

        // New year
        int yearN = 2012;
        Gregorian g = new Gregorian();
        g.fromFixed(Chinese.newYear(yearN));
        System.out.println("New Chinese year for Gregorian year " + yearN);
        System.out.println(g.year + "/" + g.month + "/" + g.day);
        h2 = new Chinese(Chinese.newYear(2012));
        System.out.println(h2.getYearNumber());
        System.out.println("JD " + h2.toJulianDay() + " = " + h2.cycle + "/" + h2.year + "/" + h2.month + "/" + h2.leapMonth + "/" + h2.day);
    }
}
