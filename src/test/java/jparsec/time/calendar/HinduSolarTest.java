package jparsec.time.calendar;

import jparsec.time.calendar.CalendarGenericConversion.CALENDAR;
import jparsec.util.JPARSECException;

public class HinduSolarTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("HinduSolar test");

        Gregorian g = new Gregorian(2015, 12, 3);
        System.out.println("julian " + g.getJulianDate() + ", fixed " + g.getFixed() + " = " + g);

        HinduSolar h = new HinduSolar (g.getFixed());
        double julian = h.getJulianDate();
        long fixed = h.getFixed();
        System.out.println("julian " + h.getJulianDate() + ", fixed " + h.getFixed() + " = " + h);

        h = new HinduSolar(julian);
        System.out.println("julian " + h.getJulianDate() + ", fixed " + h.getFixed() + " = " + h);

        h = new HinduSolar(fixed);
        System.out.println("julian " + h.getJulianDate() + ", fixed " + h.getFixed() + " = " + h);

        HinduSolar h2 = new HinduSolar(h.year, h.month, h.day);
        System.out.println("julian " + h2.getJulianDate() + ", fixed " + h2.getFixed() + " = " + h2);

        try {
        	int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.HINDU_SOLAR, 2015, 12, 3);
        	System.out.println(out[0]+"/"+out[1]+"/"+out[2]);
		} catch (JPARSECException e) {
			e.printStackTrace();
		}
        //System.out.println(Calendar.nameFromMonth(Calendar.adjustedMod(h2.month + 1, 12), HinduLunar.MONTH_NAMES));
        //System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), HinduSolar.DAY_OF_WEEK_NAMES));
        //System.out.println("(from sunrise)");
        
        h = new HinduSolar(h.year+1, 1, 1);
        g = new Gregorian(h.julianDate);
        h = new HinduSolar(g.julianDate);
        System.out.println(h.julianDate+"/"+h.fixed+"/"+g.toString()+"/"+h.toString());
    }
}
