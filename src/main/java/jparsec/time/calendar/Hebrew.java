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

import java.io.Serializable;

/**
 * Implements the Hebrew calendar.
 * <P>
 * This lunar calendar was promulgated by the Patriarch, Hillel II, in the
 * med-fourth century. It is attributed to Mosaic revelation by Maimonides. Days
 * begin at sunset.
 * <P>
 * See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Hebrew implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * The year.
	 */
	public long year;

	/**
	 * Month.
	 */
	public int month;

	/**
	 * Day.
	 */
	public int day;

	/**
	 * Calendar epoch.
	 */
	public static final long EPOCH = Julian.toFixed(-3761, 10, 7);

	/**
	 * Day of week names.
	 */
	public static final String DAY_OF_WEEK_NAMES[] = { "yom rishon", "yom sheni", "yom shelishi", "yom revi`i", "yom hamishi", "yom shishi", "yom shabbat" };

	/**
	 * Month names.
	 */
	public static final String MONTH_NAMES[] = { "Nisan", "Iyyar", "Sivan", "Tammuz", "Av", "Elul", "Tishri", "Marheshvan", "Kislev", "Tevet", "Shevat", "Adar" };

	public static final String LEAPYEAR_MONTH_NAMES[] = { "Nisan", "Iyyar", "Sivan", "Tammuz", "Av", "Elul", "Tishri", "Marheshvan", "Kislev", "Tevet", "Shevat", "Adar I", "Adar II" };

	/**
	 * Default constructor.
	 */
	public Hebrew() { }

	/**
	 * Constructs a Hebrew date with a Julian day
	 *
	 * @param jd Julian day
	 */
	public Hebrew(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Explicit constructor.
	 *
	 * @param y Year.
	 * @param m Month.
	 * @param d Day.
	 */
	public Hebrew(long y, int m, int d)
	{
		year = y;
		month = m;
		day = d;
	}

	/**
	 * To fixed day..
	 *
	 * @param y Year.
	 * @param m Month.
	 * @param d Day.
	 * @return Fixed day.
	 */
	public static long toFixed(long y, int m, int d)
	{
		long l1 = (newYear(y) + (long) d) - 1L;
		if (m < 7)
		{
			for (int k = 7; k <= lastMonthOfYear(y); k++)
				l1 += lastDayOfMonth(k, y);

			for (int j1 = 1; j1 < m; j1++)
				l1 += lastDayOfMonth(j1, y);

		} else
		{
			for (int i1 = 7; i1 < m; i1++)
				l1 += lastDayOfMonth(i1, y);

		}
		return l1;
	}

	/**
	 * To fixed date.
	 *
	 * @return Fixed day.
	 */
	public long toFixed()
	{
		return toFixed(year, month, day);
	}

	/**
	 * Transforms a Hebrew date into a Julian day
	 *
	 * @param year Year.
	 * @param month Month.
	 * @param day Day.
	 * @return Julian day.
	 */
	public static int toJulianDay(int year, int month, int day)
	{
		return (int) (toFixed(year, month, day) + Gregorian.EPOCH);
	}

	/**
	 * Transforms a Hebrew date into a Julian day.
	 * @return The Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Hebrew date with a given Julian day
	 *
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}

	/**
	 * Sets the date from the fixed day.
	 *
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		long l1 = 1L + Calendar.quotient(l - EPOCH, 365.24682220597794D);
		for (year = l1 - 1L; newYear(year) <= l; year++)
			;
		year--;
		int i = l >= toFixed(year, 1, 1) ? 1 : 7;
		for (month = i; l > toFixed(year, month, lastDayOfMonth(month, year)); month++)
			;
		day = (int) ((1L + l) - toFixed(year, month, 1));
	}

	/**
	 * Is this a leap year?
	 *
	 * @param year Year.
	 * @return True if it is a leap year.
	 */
	public static boolean isLeapYear(long year)
	{
		return Calendar.mod(1L + 7L * year, 19L) < 7L;
	}

	/**
	 * Gets the last month in this year.
	 *
	 * @param year Year.
	 * @return Number of months.
	 */
	public static int lastMonthOfYear(long year)
	{
		return !isLeapYear(year) ? 12 : 13;
	}

	/**
	 * Gets the last day of month.
	 *
	 * @param month Month.
	 * @param year Year.
	 * @return Number of days in this month.
	 */
	public static int lastDayOfMonth(int month, long year)
	{
		return month != 2 && month != 4 && month != 6 && month != 10 && month != 13 && (month != 12 || isLeapYear(year)) && (month != 8 || hasLongMarheshvan(year)) && (month != 9 || !hasShortKislev(year))
				? 30 : 29;
	}

	/**
	 * Get elapsed days from fixed epoch.
	 *
	 * @param year Year.
	 * @return Elapsed days.
	 */
	public static long calendarElapsedDays(long year)
	{
		long l1 = Calendar.quotient(235L * year - 234L, 19D);
		double d = 12084D + 13753D * (double) l1;
		long l2 = 29L * l1 + Calendar.quotient(d, 25920D);
		if (Calendar.mod(3L * (l2 + 1L), 7L) < 3L)
			return l2 + 1L;
		else
			return l2;
	}

	/**
	 * Gets the new year.
	 *
	 * @param year Year.
	 * @return Next new year fixed day.
	 */
	public static long newYear(long year)
	{
		return EPOCH + calendarElapsedDays(year) + (long) newYearDelay(year);
	}

	/**
	 * Gets the delay until next new year.
	 *
	 * @param year Year.
	 * @return Delay.
	 */
	public static int newYearDelay(long year)
	{
		long l1 = calendarElapsedDays(year - 1L);
		long l2 = calendarElapsedDays(year);
		long l3 = calendarElapsedDays(year + 1L);
		if (l3 - l2 == 356L)
			return 2;
		return l2 - l1 != 382L ? 0 : 1;
	}

	/**
	 * Gets number of days in this year.
	 *
	 * @param year Year.
	 * @return Number of days.
	 */
	public static int daysInYear(long year)
	{
		return (int) (newYear(year + 1L) - newYear(year));
	}

	/**
	 * Returns true if this year has long Marheshvan.
	 *
	 * @param year Year.
	 * @return True or false.
	 */
	public static boolean hasLongMarheshvan(long year)
	{
		int i = daysInYear(year);
		return i == 355 || i == 385;
	}

	/**
	 * Returns true if this year has short Kislev.
	 *
	 * @param year Year.
	 * @return True or false.
	 */
	public static boolean hasShortKislev(long year)
	{
		int i = daysInYear(year);
		return i == 353 || i == 383;
	}

	/**
	 * Returns Yom Kippur feast.
	 *
	 * @param year Year.
	 * @return Such date.
	 */
	public static long yomKippur(long year)
	{
		long l1 = (1L + year) - Gregorian.yearFromFixed(EPOCH);
		return toFixed(l1, 7, 10);
	}

	/**
	 * Returns Passover feast.
	 *
	 * @param year Year.
	 * @return Such date.
	 */
	public static long passover(long year)
	{
		long l1 = year - Gregorian.yearFromFixed(EPOCH);
		return toFixed(l1, 1, 15);
	}
}
