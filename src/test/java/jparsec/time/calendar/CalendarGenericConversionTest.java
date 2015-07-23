package jparsec.time.calendar;

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
            CalendarGenericConversion.CALENDAR input_calendar = CalendarGenericConversion.CALENDAR.GREGORIAN;
            CalendarGenericConversion.CALENDAR output_calendar = CalendarGenericConversion.CALENDAR.ISLAMIC;

            int date[] = CalendarGenericConversion.GenericConversion(input_calendar, output_calendar, year, month, day);

            System.out.println(year + "/" + month + "/" + day + " in " + CalendarGenericConversion.CALENDAR_NAMES[input_calendar.ordinal()] + " is ...");
            System.out.println(date[0] + "/" + date[1] + "/" + date[2] + " in " + CalendarGenericConversion.CALENDAR_NAMES[output_calendar.ordinal()]);

            // Now get the next new year in the output calendar
            date[0]++;
            date[1] = date[2] = 1;
            System.out.println("Next new year (" + date[0] + "/" + date[1] + "/" + date[2] + ") in " + CalendarGenericConversion.CALENDAR_NAMES[output_calendar.ordinal()] + " calendar is ...");

            // And convert it back to the input one
            int date_back[] = CalendarGenericConversion.GenericConversion(output_calendar, input_calendar, date[0], date[1], date[2]);
            System.out.println(date_back[0] + "/" + date_back[1] + "/" + date_back[2] + " in " + CalendarGenericConversion.CALENDAR_NAMES[input_calendar.ordinal()] + ".");
        } catch (JPARSECException e) {
            JPARSECException.showException(e);
        }
    }
}
