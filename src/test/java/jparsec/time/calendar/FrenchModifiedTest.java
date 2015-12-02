package jparsec.time.calendar;

import jparsec.time.calendar.CalendarGenericConversion.CALENDAR;
import jparsec.util.JPARSECException;

public class FrenchModifiedTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("FrenchModified test");

        double jd = new Gregorian(2015, 12, 2).julianDate;
        long fixed = new Gregorian(1792, 9, 22).fixed;

        FrenchModified h = new FrenchModified(jd);
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        FrenchModified h2 = new FrenchModified(h.year, h.month, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);

        int decadi = h2.getDecadi();

        if (decadi != -1) {
            System.out.println("Decadi " + Calendar.nameFromNumber(decadi, French.DECADE_NAMES));
        }

        System.out.println(Calendar.nameFromMonth(h2.month, French.MONTH_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.getDayOfWeek(), French.DAY_OF_WEEK_NAMES));
        try {
			System.out.println(CalendarGenericConversion.getDayOfWeekName(jd, CALENDAR.FRENCH_MODIFIED));
		} catch (JPARSECException e) {
			e.printStackTrace();
		}
        
        h = new FrenchModified(h.year+1, 1, 1);
        Gregorian g = new Gregorian(h.getJulianDate());
        System.out.println(g.toString());
    }
}
