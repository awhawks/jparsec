package jparsec.time.calendar;

public class HinduOldSolarTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("HinduOldSolar Test");

        Gregorian g = new Gregorian(2015, 12, 2);
        System.out.println("julian " + g.getJulianDate() + ", fixed " + g.getFixed() + " = " + g);

        HinduOldSolar h = new HinduOldSolar (g.getFixed());
        double julian = h.getJulianDate();
        long fixed = h.getFixed();
        System.out.println("julian " + h.getJulianDate() + ", fixed " + h.getFixed() + " = " + h);

        h = new HinduOldSolar(julian);
        System.out.println("julian " + h.getJulianDate() + ", fixed " + h.getFixed() + " = " + h);

        h = new HinduOldSolar(fixed);
        System.out.println("julian " + h.getJulianDate() + ", fixed " + h.getFixed() + " = " + h);

        HinduOldSolar h2 = new HinduOldSolar(h.year, h.month, h.day);
        System.out.println("julian " + h2.getJulianDate() + ", fixed " + h2.getFixed() + " = " + h2);

        h2 = new HinduOldSolar(h.year+1, 1, 1);
        g = new Gregorian(h2.getJulianDate());
        System.out.println("NEXT NEW YEAR: julian " + g.getJulianDate() + ", fixed " + g.getFixed() + " = " + g);

        //System.out.println(Calendar.nameFromMonth(h2.month, HinduOldSolar.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), HinduOldSolar.DAY_OF_WEEK_NAMES));
        //System.out.println("(from sunrise)");
    }
}
