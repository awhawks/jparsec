/*
 * This file is part of JPARSEC library.
 * 
 * (C) Copyright 2006-2015 by T. Alonso Albi - OAN (Spain).
 *  
 * Project Info:  http://conga.oan.es/~alonso/jparsec/jparsec.html
 * 
 * JPARSEC library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JPARSEC library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */					
package jparsec.time.calendar;

import jparsec.util.JPARSECException;
import jparsec.util.Translate;
import jparsec.util.Translate.LANGUAGE;

/**
 * A class to transform the date between different calendars based on year, month, and day.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class CalendarGenericConversion
{
	// private constructor so that this class cannot be instantiated.
	private CalendarGenericConversion() {}
	
	// List ordered by calendars in use or old calendars
	
	/**
	 * The set of available calendars that have in common that
	 * a given date is represented with a year, a month, and a day.
	 * First 6 are old calendars, the next 9 ones are still used today.
	 */
	public static enum CALENDAR {
		// Old calendars
		
		/** ID constant for Julian calendar. */
		JULIAN,
		/** ID constant for Hindu (old solar) calendar. */
		OLD_HINDU_SOLAR,
		/** ID constant for Egyptian calendar. */
		EGYPTIAN,
		/** ID constant for French calendar. */
		FRENCH,
		/** ID constant for French (modified) calendar. */
		MODIFIED_FRENCH,
		/** ID constant for Persian arithmetic calendar. */
		ARITH_PERSIAN,

		// In use
		
		/** ID constant for Gregorian calendar. */
		GREGORIAN,
		/** ID constant for Islamic calendar. */
		ISLAMIC,
		/** ID constant for Islamic observational calendar. */
		OBSERVATIONAL_ISLAMIC,
		/** ID constant for Hebrew calendar. */
		HEBREW,
		/** ID constant for Hindu (solar) calendar. */
		HINDU_SOLAR,
		/** ID constant for Persian calendar. */
		PERSIAN,
		/** ID constant for Coptic calendar. */
		COPTIC,
		/** ID constant for Ethiopic calendar. */
		ETHIOPIC,
		/** ID constant for Armenian calendar. */
		ARMENIAN
	}
	
	/**
	 * Calendar names array.
	 */
	public static final String CALENDAR_NAMES[] =
	{ 
		"Julian", "Hindu solar (old)", "Egyptian", "French", "French (modified)", "Persian (old arithmetic)",
		"Gregorian", "Islamic (arithmetic)", "Islamic (observational)", "Hebrew", "Hindu solar",
		"Persian (astronomical)", "Coptic", "Ethiopic", "Armenian" 
	};

	/**
	 * Converts a given date from one calendar to another. Any of the 15
	 * different calendars with ID constants defined in this class are available
	 * for input or output.
	 * 
	 * @param input_calendar Input calendar.
	 * @param output_calendar Output calendar.
	 * @param year Year in input calendar. Year should be set in the astronomical convention, year 0 exists.
	 * @param month Month in input calendar.
	 * @param day Day in input calendar.
	 * @return Array with year, month, and day in output calendar.
	 * @throws JPARSECException If the input/output calendars are invalid.
	 */
	public static int[] GenericConversion(CALENDAR input_calendar, CALENDAR output_calendar, int year, int month, int day)
			throws JPARSECException
	{
		int out_year = 0, out_month = 0, out_day = 0;
		double jd = 0.0;

		switch (input_calendar)
		{
		case ARITH_PERSIAN:
			PersianArithmetic ap = new PersianArithmetic();
			ap.year = year;
			ap.month = month;
			ap.day = day;
			jd = ap.toJulianDay();
			break;
		case ARMENIAN:
			Armenian ar = new Armenian();
			ar.year = year;
			ar.month = month;
			ar.day = day;
			jd = ar.toJulianDay();
			break;
		case COPTIC:
			Coptic co = new Coptic();
			co.year = year;
			co.month = month;
			co.day = day;
			jd = co.toJulianDay();
			break;
		case EGYPTIAN:
			Egyptian eg = new Egyptian();
			eg.year = year;
			eg.month = month;
			eg.day = day;
			jd = eg.toJulianDay();
			break;
		case ETHIOPIC:
			Ethiopic et = new Ethiopic();
			et.year = year;
			et.month = month;
			et.day = day;
			jd = et.toJulianDay();
			break;
		case FRENCH:
			French fr = new French();
			fr.year = year;
			fr.month = month;
			fr.day = day;
			jd = fr.toJulianDay();
			break;
		case GREGORIAN:
			Gregorian gr = new Gregorian();
			gr.year = year;
			gr.month = month;
			gr.day = day;
			jd = gr.toJulianDay();
			break;
		case HEBREW:
			Hebrew he = new Hebrew();
			he.year = year;
			he.month = month;
			he.day = day;
			jd = he.toJulianDay();
			break;
		case HINDU_SOLAR:
			HinduSolar hs = new HinduSolar();
			hs.year = year;
			hs.month = month;
			hs.day = day;
			jd = hs.toJulianDay();
			break;
		case ISLAMIC:
			Islamic is = new Islamic();
			is.year = year;
			is.month = month;
			is.day = day;
			jd = is.toJulianDay();
			break;
		case JULIAN:
			Julian ju = new Julian();
			ju.year = year;
			ju.month = month;
			ju.day = day;
			jd = ju.toJulianDay();
			break;
		case MODIFIED_FRENCH:
			FrenchModified mf = new FrenchModified();
			mf.year = year;
			mf.month = month;
			mf.day = day;
			jd = mf.toJulianDay();
			break;
		case OBSERVATIONAL_ISLAMIC:
			IslamicObservational oi = new IslamicObservational();
			oi.year = year;
			oi.month = month;
			oi.day = day;
			jd = oi.toJulianDay();
			break;
		case OLD_HINDU_SOLAR:
			HinduOldSolar ohs = new HinduOldSolar();
			ohs.year = year;
			ohs.month = month;
			ohs.day = day;
			jd = ohs.toJulianDay();
			break;
		case PERSIAN:
			Persian pe = new Persian();
			pe.year = year;
			pe.month = month;
			pe.day = day;
			jd = pe.toJulianDay();
			break;
		default:
			throw new JPARSECException("input calendar is invalid.");
		}

		switch (output_calendar)
		{
		case ARITH_PERSIAN:
			PersianArithmetic ap = new PersianArithmetic();
			ap.fromJulianDay((int) jd);
			out_year = (int) ap.year;
			out_month = ap.month;
			out_day = ap.day;
			break;
		case ARMENIAN:
			Armenian ar = new Armenian();
			ar.fromJulianDay((int) jd);
			out_year = (int) ar.year;
			out_month = ar.month;
			out_day = ar.day;
			break;
		case COPTIC:
			Coptic co = new Coptic();
			co.fromJulianDay((int) jd);
			out_year = (int) co.year;
			out_month = co.month;
			out_day = co.day;
			break;
		case EGYPTIAN:
			Egyptian eg = new Egyptian();
			eg.fromJulianDay((int) jd);
			out_year = (int) eg.year;
			out_month = eg.month;
			out_day = eg.day;
			break;
		case ETHIOPIC:
			Ethiopic et = new Ethiopic();
			et.fromJulianDay((int) jd);
			out_year = (int) et.year;
			out_month = et.month;
			out_day = et.day;
			break;
		case FRENCH:
			French fr = new French();
			fr.fromJulianDay((int) jd);
			out_year = (int) fr.year;
			out_month = fr.month;
			out_day = fr.day;
			break;
		case GREGORIAN:
			Gregorian gr = new Gregorian();
			gr.fromJulianDay((int) jd);
			out_year = (int) gr.year;
			out_month = gr.month;
			out_day = gr.day;
			break;
		case HEBREW:
			Hebrew he = new Hebrew();
			he.fromJulianDay((int) jd);
			out_year = (int) he.year;
			out_month = he.month;
			out_day = he.day;
			break;
		case HINDU_SOLAR:
			HinduSolar hs = new HinduSolar();
			hs.fromJulianDay((int) jd);
			out_year = (int) hs.year;
			out_month = hs.month;
			out_day = hs.day;
			break;
		case ISLAMIC:
			Islamic is = new Islamic();
			is.fromJulianDay((int) jd);
			out_year = (int) is.year;
			out_month = is.month;
			out_day = is.day;
			break;
		case JULIAN:
			Julian ju = new Julian();
			ju.fromJulianDay((int) jd);
			out_year = (int) ju.year;
			out_month = ju.month;
			out_day = ju.day;
			break;
		case MODIFIED_FRENCH:
			FrenchModified mf = new FrenchModified();
			mf.fromJulianDay((int) jd);
			out_year = (int) mf.year;
			out_month = mf.month;
			out_day = mf.day;
			break;
		case OBSERVATIONAL_ISLAMIC:
			IslamicObservational oi = new IslamicObservational();
			oi.fromJulianDay((int) jd);
			out_year = (int) oi.year;
			out_month = oi.month;
			out_day = oi.day;
			break;
		case OLD_HINDU_SOLAR:
			HinduOldSolar ohs = new HinduOldSolar();
			ohs.fromJulianDay((int) jd);
			out_year = (int) ohs.year;
			out_month = ohs.month;
			out_day = ohs.day;
			break;
		case PERSIAN:
			Persian pe = new Persian();
			pe.fromJulianDay((int) jd);
			out_year = (int) pe.year;
			out_month = pe.month;
			out_day = pe.day;
			break;
		default:
			throw new JPARSECException("output calendar is invalid.");
		}

		return new int[] { out_year, out_month, out_day };
	}

	/**
	 * Returns the name of the month for a given calendar.
	 * @param month The month number, January is 1.
	 * @param calendar The calendar id value.
	 * @return The name of the month.
	 * @throws JPARSECException If the calendar or month is invalid.
	 */
	public static String getMonthName(int month, CALENDAR calendar) throws JPARSECException {
		try {
			String out = "";
			switch (calendar)
			{
			case ARITH_PERSIAN:
				out = Calendar.nameFromMonth(month, Persian.MONTH_NAMES);
				break;
			case ARMENIAN:
				out = Calendar.nameFromMonth(month, Armenian.MONTH_NAMES);
				break;
			case COPTIC:
				out = Calendar.nameFromMonth(month, Coptic.MONTH_NAMES);
				break;
			case EGYPTIAN:
				out = Calendar.nameFromMonth(month, Egyptian.MONTH_NAMES);
				break;
			case ETHIOPIC:
				out = Calendar.nameFromMonth(month, Ethiopic.MONTH_NAMES);
				break;
			case FRENCH:
				out = Calendar.nameFromMonth(month, French.MONTH_NAMES);
				break;
			case GREGORIAN:
				out = Calendar.nameFromMonth(month, Gregorian.MONTH_NAMES);
				break;
			case HEBREW:
				out = Calendar.nameFromMonth(month, Hebrew.MONTH_NAMES);
				break;
			case HINDU_SOLAR:
				out = Calendar.nameFromMonth(Calendar.adjustedMod(month + 1, 12), HinduOldLunar.MONTH_NAMES);
				break;
			case ISLAMIC:
				out = Calendar.nameFromMonth(month, Islamic.MONTH_NAMES);
				break;
			case JULIAN:
				out = Calendar.nameFromMonth(month, Gregorian.MONTH_NAMES);
				break;
			case MODIFIED_FRENCH:
				out = Calendar.nameFromMonth(month, French.MONTH_NAMES);
				break;
			case OBSERVATIONAL_ISLAMIC:
				out = Calendar.nameFromMonth(month, Islamic.MONTH_NAMES);
				break;
			case OLD_HINDU_SOLAR:
				out = Calendar.nameFromMonth(month, HinduOldSolar.MONTH_NAMES);
				break;
			case PERSIAN:
				out = Calendar.nameFromMonth(month, Persian.MONTH_NAMES);
				break;
			default:
				throw new JPARSECException("calendar is invalid.");
			}	
			out = Translate.translate(out);
			if (out.toLowerCase().equals("may") && Translate.getDefaultLanguage() == LANGUAGE.SPANISH) out += "o";
			return out;
		} catch (JPARSECException e) {
			throw e;
		} catch (Exception exc) {
			throw new JPARSECException("month is invalid.");
		}
	}

	/**
	 * Returns the index of the month for a given calendar.
	 * @param month The month name.
	 * @param calendar The calendar id value.
	 * @return The index of the month, or -1 if no match is found.
	 * @throws JPARSECException If the calendar or month is invalid.
	 */
	public static int getMonthNumber(String month, CALENDAR calendar) throws JPARSECException {
		try {
			int out = -1;
			switch (calendar)
			{
			case ARITH_PERSIAN:
				out = Calendar.indexFromName(month, Persian.MONTH_NAMES);
				break;
			case ARMENIAN:
				out = Calendar.indexFromName(month, Armenian.MONTH_NAMES);
				break;
			case COPTIC:
				out = Calendar.indexFromName(month, Coptic.MONTH_NAMES);
				break;
			case EGYPTIAN:
				out = Calendar.indexFromName(month, Egyptian.MONTH_NAMES);
				break;
			case ETHIOPIC:
				out = Calendar.indexFromName(month, Ethiopic.MONTH_NAMES);
				break;
			case FRENCH:
				out = Calendar.indexFromName(month, French.MONTH_NAMES);
				break;
			case GREGORIAN:
				out = Calendar.indexFromName(month, Gregorian.MONTH_NAMES);
				break;
			case HEBREW:
				out = Calendar.indexFromName(month, Hebrew.MONTH_NAMES);
				break;
			case HINDU_SOLAR:
				out = Calendar.indexFromName(month, HinduOldLunar.MONTH_NAMES);
				break;
			case ISLAMIC:
				out = Calendar.indexFromName(month, Islamic.MONTH_NAMES);
				break;
			case JULIAN:
				out = Calendar.indexFromName(month, Gregorian.MONTH_NAMES);
				break;
			case MODIFIED_FRENCH:
				out = Calendar.indexFromName(month, French.MONTH_NAMES);
				break;
			case OBSERVATIONAL_ISLAMIC:
				out = Calendar.indexFromName(month, Islamic.MONTH_NAMES);
				break;
			case OLD_HINDU_SOLAR:
				out = Calendar.indexFromName(month, HinduOldSolar.MONTH_NAMES);
				break;
			case PERSIAN:
				out = Calendar.indexFromName(month, Persian.MONTH_NAMES);
				break;
			default:
				throw new JPARSECException("calendar is invalid.");
			}	
			return out;
		} catch (JPARSECException e) {
			throw e;
		} catch (Exception exc) {
			throw new JPARSECException("month is invalid.");
		}
	}
	
	/**
	 * Returns the name of the day of the week for a given calendar.
	 * @param jd The Julian day.
	 * @param calendar The calendar id value.
	 * @return The name of the day.
	 * @throws JPARSECException If the calendar or day is invalid.
	 */
	public static String getDayOfWeekName(int jd, CALENDAR calendar) throws JPARSECException {
		String out = "";
		long day = jd - Gregorian.EPOCH;
		try {
			switch (calendar)
			{
			case ARITH_PERSIAN:
				out =  Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), Persian.DAY_OF_WEEK_NAMES);
				break;
			case ARMENIAN:
				out = Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), Armenian.DAY_OF_WEEK_NAMES);
				break;
			case COPTIC:
				out = Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), Coptic.DAY_OF_WEEK_NAMES);
				break;
			case ETHIOPIC:
				out = Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), Ethiopic.DAY_OF_WEEK_NAMES);
				break;
			case FRENCH:
				out = Calendar.nameFromNumber(French.getDayOfWeek(new French(jd)), French.DAY_OF_WEEK_NAMES);
				break;
			case GREGORIAN:
				out = Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), Gregorian.DAY_OF_WEEK_NAMES);
				break;
			case HEBREW:
				out = Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), Hebrew.DAY_OF_WEEK_NAMES);
				break;
			case HINDU_SOLAR:
				out = Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), HinduOldSolar.DAY_OF_WEEK_NAMES);
				break;
			case ISLAMIC:
				out = Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), Islamic.DAY_OF_WEEK_NAMES);
				break;
			case JULIAN:
				out = Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), Gregorian.DAY_OF_WEEK_NAMES);
				break;
			case MODIFIED_FRENCH:
				out = Calendar.nameFromNumber(French.getDayOfWeek(new French(jd)), French.DAY_OF_WEEK_NAMES);
				break;
			case OBSERVATIONAL_ISLAMIC:
				out = Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), Islamic.DAY_OF_WEEK_NAMES);
				break;
			case OLD_HINDU_SOLAR:
				out = Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), HinduOldSolar.DAY_OF_WEEK_NAMES);
				break;
			case PERSIAN:
				out = Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(day), Persian.DAY_OF_WEEK_NAMES);
				break;
			default:
				throw new JPARSECException("calendar is invalid.");
			}	
			out = Translate.translate(out);
			return out;
		} catch (JPARSECException e) {
			throw e;
		} catch (Exception exc) {
			throw new JPARSECException("month is invalid.");
		}
	}

	/**
	 * Returns the index of the name of the day of the week for a given calendar.
	 * @param day Day of week name.
	 * @param calendar The calendar id value.
	 * @return The index of the name of the day in the week, or -1 if no match is
	 * found.
	 * @throws JPARSECException If the calendar or day is invalid.
	 */
	public static int getDayOfWeekNumber(String day, CALENDAR calendar) throws JPARSECException {
		int out = -1;
		try {
			switch (calendar)
			{
			case ARITH_PERSIAN:
				out =  Calendar.indexFromName(day, Persian.DAY_OF_WEEK_NAMES);
				break;
			case ARMENIAN:
				out = Calendar.indexFromName(day, Armenian.DAY_OF_WEEK_NAMES);
				break;
			case COPTIC:
				out = Calendar.indexFromName(day, Coptic.DAY_OF_WEEK_NAMES);
				break;
			case ETHIOPIC:
				out = Calendar.indexFromName(day, Ethiopic.DAY_OF_WEEK_NAMES);
				break;
			case FRENCH:
				out = Calendar.indexFromName(day, French.DAY_OF_WEEK_NAMES);
				break;
			case GREGORIAN:
				out = Calendar.indexFromName(day, Gregorian.DAY_OF_WEEK_NAMES);
				break;
			case HEBREW:
				out = Calendar.indexFromName(day, Hebrew.DAY_OF_WEEK_NAMES);
				break;
			case HINDU_SOLAR:
				out = Calendar.indexFromName(day, HinduOldSolar.DAY_OF_WEEK_NAMES);
				break;
			case ISLAMIC:
				out = Calendar.indexFromName(day, Islamic.DAY_OF_WEEK_NAMES);
				break;
			case JULIAN:
				out = Calendar.indexFromName(day, Gregorian.DAY_OF_WEEK_NAMES);
				break;
			case MODIFIED_FRENCH:
				out = Calendar.indexFromName(day, French.DAY_OF_WEEK_NAMES);
				break;
			case OBSERVATIONAL_ISLAMIC:
				out = Calendar.indexFromName(day, Islamic.DAY_OF_WEEK_NAMES);
				break;
			case OLD_HINDU_SOLAR:
				out = Calendar.indexFromName(day, HinduOldSolar.DAY_OF_WEEK_NAMES);
				break;
			case PERSIAN:
				out = Calendar.indexFromName(day, Persian.DAY_OF_WEEK_NAMES);
				break;
			default:
				throw new JPARSECException("calendar is invalid.");
			}	
			return out;
		} catch (JPARSECException e) {
			throw e;
		} catch (Exception exc) {
			throw new JPARSECException("month is invalid.");
		}
	}
}
