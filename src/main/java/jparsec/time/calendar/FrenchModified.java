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
 * Implements the Modified French calendar.
 * <P>
 * Used between May, 6-23, 1871.
 * <P>
 * See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class FrenchModified implements Serializable
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
	 * Default constructor.
	 */
	public FrenchModified() {}

	/**
	 * Julian day constructor.
	 *
	 * @param jd Julian day.
	 */
	public FrenchModified(int jd)
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
	public FrenchModified(long y, int m, int d)
	{
		year = y;
		month = m;
		day = d;
	}

	/**
	 * To fixed day.
	 *
	 * @param y Year.
	 * @param m Month.
	 * @param d Day.
	 * @return Fixed day.
	 */
	public static long toFixed(long y, int m, int d)
	{
		return (((((French.EPOCH - 1L) + 365L * (y - 1L) + Calendar.quotient(y - 1L, 4D)) - Calendar.quotient(y - 1L,
				100D)) + Calendar.quotient(y - 1L, 400D)) - Calendar.quotient(y - 1L, 4000D)) + (long) (30 * (m - 1)) + (long) d;
	}

	/**
	 * To fixed day.
	 *
	 * @return Fixed day.
	 */
	public long toFixed()
	{
		return toFixed(year, month, day);
	}

	/**
	 * Sets the date from fixed day.
	 *
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		long l1 = 1L + Calendar.quotient((l - French.EPOCH) + 2L, 365.24225000000001D);
		year = l >= toFixed(l1, 1, 1) ? l1 : l1 - 1L;
		month = 1 + (int) Calendar.quotient(l - toFixed(year, 1, 1), 30D);
		day = (int) ((l - toFixed(year, month, 1)) + 1L);
	}

	/**
	 * Gets if the current year is a leap one.
	 *
	 * @param l Fixed day.
	 * @return True or false.
	 */
	public static boolean isLeapYear(long l)
	{
		boolean flag = false;
		if (Calendar.mod(l, 4L) == 0L)
		{
			long l1 = Calendar.mod(l, 400L);
			if (l1 != 100L && l1 != 200L && l1 != 300L && Calendar.mod(l, 4000L) != 0L)
				flag = true;
		}
		return flag;
	}

	/**
	 * Transforms a French date into a Julian day
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
	 * Transforms a French date into a Julian day.
	 *
	 * @return Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a French date with a given Julian day.
	 *
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}

	/**
	 * Gets the day of the week.
	 *
	 * @return Day of week.
	 */
	public int getDayOfWeek()
	{
		int day = this.day + 9;
        if (month < 13) day = (this.day - 1) % 10;
        day++;
		if (day > French.DAY_OF_WEEK_NAMES.length) day -= French.DAY_OF_WEEK_NAMES.length;
		if (day < 0) day += French.DAY_OF_WEEK_NAMES.length;
		return day;
	}

	/**
	 * Gets the decadi.
	 *
	 * @return Decadi index (0, 1, 2), or -1 for no decadi.
	 */
	public int getDecadi()
	{
		int week = -1;
        if (month < 13) week = (day - 1) / 10 + 1;
        if (week > 2) week = week - 3;
		return week;
	}
}
