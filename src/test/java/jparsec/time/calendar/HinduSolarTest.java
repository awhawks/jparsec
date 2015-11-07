package jparsec.time.calendar;

public class HinduSolarTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("HinduSolar test");

        Gregorian g = new Gregorian(2000, 1, 1);
        System.out.println("julian " + g.julianDate + ", fixed " + g.fixed + " = " + g);

        HinduSolar h = new HinduSolar (1921, 9, 17);
        double julian = h.julianDate;
        long fixed = h.fixed;
        System.out.println("julian " + h.julianDate + ", fixed " + h.fixed + " = " + h);

        h = new HinduSolar(julian);
        System.out.println("julian " + h.julianDate + ", fixed " + h.fixed + " = " + h);

        h = new HinduSolar(fixed);
        System.out.println("julian " + h.julianDate + ", fixed " + h.fixed + " = " + h);

        HinduSolar h2 = new HinduSolar(h.year, h.month, h.day);
        System.out.println("julian " + h2.julianDate + ", fixed " + h2.fixed + " = " + h2);

        //System.out.println(Calendar.nameFromMonth(Calendar.adjustedMod(h2.month + 1, 12), HinduLunar.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), HinduSolar.DAY_OF_WEEK_NAMES));
        //System.out.println("(from sunrise)");
    }
}
