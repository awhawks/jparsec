package jparsec.time.calendar;

public class HinduOldSolarTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("HinduOldSolar Test");

        Gregorian g = new Gregorian(2000, 1, 1);
        System.out.println("julian " + g.julianDate + ", fixed " + g.fixed + " = " + g);

        HinduOldSolar h = new HinduOldSolar (1921, 9, 17);
        double julian = h.julianDate;
        long fixed = h.fixed;
        System.out.println("julian " + h.julianDate + ", fixed " + h.fixed + " = " + h);

        h = new HinduOldSolar(julian);
        System.out.println("julian " + h.julianDate + ", fixed " + h.fixed + " = " + h);

        h = new HinduOldSolar(fixed);
        System.out.println("julian " + h.julianDate + ", fixed " + h.fixed + " = " + h);

        HinduOldSolar h2 = new HinduOldSolar(h.year, h.month, h.day);
        System.out.println("julian " + h2.julianDate + ", fixed " + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(h2.month, HinduOldSolar.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), HinduOldSolar.DAY_OF_WEEK_NAMES));
        //System.out.println("(from sunrise)");
    }
}
