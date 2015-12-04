package jparsec.time.calendar;

import jparsec.time.AstroDate;
import jparsec.time.calendar.CalendarGenericConversion.CALENDAR;
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
            
            Julian jul = new Julian(jd);
            System.out.println(jul.toString());
            System.out.println(jul.getDayOfWeek());
            System.out.println(jul.getFixed());
            
            Gregorian gEpoch = new Gregorian(Gregorian.EPOCH + 1.5);
            System.out.println(gEpoch.toString());
            System.out.println(gEpoch.getDayOfWeek());
            System.out.println(gEpoch.getFixed());
            
            System.out.println("*** TESTING ERRORS IN CROSS CONVERSIONS ***");
            BaseCalendar b = new FrenchModified(2015, 1, 1);
            g = new Gregorian(b.getJulianDate());
            double refJD = b.getJulianDate();
            long refFixed = b.getFixed();
            for (int j=0; j<3; j++) {
	            for (int i=0; i<18; i++) {
	            	switch (i) {
	            	case 0:
	            		if (j == 0) b = new Armenian(g.julianDate);
	            		if (j == 1) b = new Armenian(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.ARMENIAN, (int)g.year, g.month, g.day);
	            			b = new Armenian(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 1:
	            		if (j == 0) b = new Bahai(g.julianDate);
	            		if (j == 1) b = new Bahai(g.fixed);
	            		if (j == 2) b = null;
	            		break;
	            	case 2:
	            		if (j == 0) b = new Chinese(g.julianDate);
	            		if (j == 1) b = new Chinese(g.fixed);
	            		if (j == 2) b = null;
	            		break;
	            	case 3:
	            		if (j == 0) b = new Coptic(g.julianDate);
	            		if (j == 1) b = new Coptic(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.COPTIC, (int)g.year, g.month, g.day);
	            			b = new Coptic(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 4:
	            		if (j == 0) b = new Egyptian(g.julianDate);
	            		if (j == 1) b = new Egyptian(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.EGYPTIAN, (int)g.year, g.month, g.day);
	            			b = new Egyptian(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 5:
	            		if (j == 0) b = new Ethiopic(g.julianDate);
	            		if (j == 1) b = new Ethiopic(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.ETHIOPIC, (int)g.year, g.month, g.day);
	            			b = new Ethiopic(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 6:
	            		if (j == 0) b = new French(g.julianDate);
	            		if (j == 1) b = new French(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.FRENCH, (int)g.year, g.month, g.day);
	            			b = new French(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 7:
	            		if (j == 0) b = new FrenchModified(g.julianDate);
	            		if (j == 1) b = new FrenchModified(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.FRENCH_MODIFIED, (int)g.year, g.month, g.day);
	            			b = new FrenchModified(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 8:
	            		if (j == 0) b = new Gregorian(g.julianDate);
	            		if (j == 1) b = new Gregorian(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.GREGORIAN, (int)g.year, g.month, g.day);
	            			b = new Gregorian(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 9:
	            		if (j == 0) b = new Hebrew(g.julianDate);
	            		if (j == 1) b = new Hebrew(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.HEBREW, (int)g.year, g.month, g.day);
	            			b = new Hebrew(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 10:
	            		if (j == 0) b = new HinduOldSolar(g.julianDate);
	            		if (j == 1) b = new HinduOldSolar(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.HINDU_OLD_SOLAR, (int)g.year, g.month, g.day);
	            			b = new HinduOldSolar(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 11:
	            		if (j == 0) b = new HinduSolar(g.julianDate);
	            		if (j == 1) b = new HinduSolar(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.HINDU_SOLAR, (int)g.year, g.month, g.day);
	            			b = new HinduSolar(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 12:
	            		if (j == 0) b = new Islamic(g.julianDate);
	            		if (j == 1) b = new Islamic(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.ISLAMIC, (int)g.year, g.month, g.day);
	            			b = new Islamic(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 13:
	            		if (j == 0) b = new IslamicObservational(g.julianDate);
	            		if (j == 1) b = new IslamicObservational(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.ISLAMIC_OBSERVATIONAL, (int)g.year, g.month, g.day);
	            			b = new IslamicObservational(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 14:
	            		if (j == 0) b = new ISO(g.julianDate);
	            		if (j == 1) b = new ISO(g.fixed);
	            		if (j == 2) b = null;
	            		break;
	            	case 15:
	            		if (j == 0) b = new Julian(g.julianDate);
	            		if (j == 1) b = new Julian(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.JULIAN, (int)g.year, g.month, g.day);
	            			b = new Julian(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 16:
	            		if (j == 0) b = new Persian(g.julianDate);
	            		if (j == 1) b = new Persian(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.PERSIAN, (int)g.year, g.month, g.day);
	            			b = new Persian(out[0], out[1], out[2]);
	            		}
	            		break;
	            	case 17:
	            		if (j == 0) b = new PersianArithmetic(g.julianDate);
	            		if (j == 1) b = new PersianArithmetic(g.fixed);
	            		if (j == 2) {
	            			int out[] = CalendarGenericConversion.GenericConversion(CALENDAR.GREGORIAN, CALENDAR.PERSIAN_ARITHMETIC, (int)g.year, g.month, g.day);
	            			b = new PersianArithmetic(out[0], out[1], out[2]);
	            		}
	            		break;
	            	}
	            	
	            	if (b != null) {
	            		if (b.getJulianDate() != refJD) 
	            			System.out.println("*** WRONG JD FOR "+b.getClass().getCanonicalName()+": is "+b.getJulianDate()+", and it should be "+refJD);
	            		if (b.getFixed() != refFixed) 
	            			System.out.println("*** WRONG FIXED FOR "+b.getClass().getCanonicalName()+": is "+b.getFixed()+", and it should be "+refFixed);
	            	}
	            }
            }
            
        } catch (JPARSECException e) {
            JPARSECException.showException(e);
        }
    }
}
