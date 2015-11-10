package jparsec.time.calendar;

import jparsec.time.AstroDate;
import jparsec.util.JPARSECException;

public class CalendarGenericConversionTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("CalendarGenericConversion test");

        try {
            int year = 2000, month = 1, day = 1;
            CalendarGenericConversion.CALENDAR inCal = CalendarGenericConversion.CALENDAR.GREGORIAN;
            CalendarGenericConversion.CALENDAR outCal = CalendarGenericConversion.CALENDAR.ISLAMIC;

            int date[] = CalendarGenericConversion.GenericConversion(inCal, outCal, year, month, day);

            System.out.println(year + "/" + month + "/" + day + " in " +
                    CalendarGenericConversion.CALENDAR_NAMES[inCal.ordinal()] + " is ...");
            System.out.println(date[0] + "/" + date[1] + "/" + date[2] + " in " +
                    CalendarGenericConversion.CALENDAR_NAMES[outCal.ordinal()]);

            // Now get the next new year in the output calendar
            date[0]++;
            date[1] = date[2] = 1;
            System.out.println("Next new year (" + date[0] + "/" + date[1] + "/" + date[2] + ") in " +
                    CalendarGenericConversion.CALENDAR_NAMES[outCal.ordinal()] + " calendar is ...");

            // And convert it back to the input one
            int date_back[] = CalendarGenericConversion.GenericConversion(outCal, inCal, date[0], date[1], date[2]);
            System.out.println(date_back[0] + "/" + date_back[1] + "/" + date_back[2] + " in " +
                    CalendarGenericConversion.CALENDAR_NAMES[inCal.ordinal()] + ".");

            // Check Julian day conversion and day of week
            AstroDate astro = new AstroDate();
            Gregorian g = new Gregorian(astro.getYear(), astro.getMonth(), astro.getDay());
            double jd = astro.jd();
            String dayOfWeek = CalendarGenericConversion.getDayOfWeekName(jd, inCal);
            System.out.println("Today is "+dayOfWeek+" ("+g.getDayOfWeek()+")");
            System.out.println("Julian day from AstroDate (now): "+jd);
            System.out.println("Julian day from Gregorian (previous midnight): "+g.getJulianDate());
            System.out.println("Fixed Gregorian date: "+g.getFixed());
            
            // Check ISO and Julian dates
            ISO iso = new ISO(jd);
            System.out.println(iso.toString());
            System.out.println(iso.getDayOfWeek());
            System.out.println(iso.getFixed());
            
            Julian j = new Julian(jd);
            System.out.println(j.toString());
            System.out.println(j.getDayOfWeek());
            System.out.println(j.getFixed());
            
            Gregorian gEpoch = new Gregorian(Gregorian.EPOCH + 1.5);
            System.out.println(gEpoch.toString());
            System.out.println(gEpoch.getDayOfWeek());
            System.out.println(gEpoch.getFixed());
            
        } catch (JPARSECException e) {
            JPARSECException.showException(e);
        }
    }
}
