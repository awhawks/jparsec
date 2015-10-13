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
 * Implements the Coptic calendar. See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Coptic implements Serializable
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
	public static final long EPOCH = Julian.toFixed(284, 8, 29);

	/**
	 * Week day names.
	 */
	public static final String DAY_OF_WEEK_NAMES[] =
	{ "Tkyriaka", "Pesnau", "Pshoment", "Peftoou", "Ptiou", "Psoou", "Psabbaton" };

	/**
	 * Month names.
	 */
	public static final String MONTH_NAMES[] =
	{ "Tut", "Babah", "Hatur", "Kiyahk", "Tubah", "Amshir", "Baramhat", "Baramundah", "Bashans", "Ba'unah", "Abib",
			"Misra", "al-Nasi" };

	/**
	 * Empty constructor.
	 */
	public Coptic() {}

	/**
	 * Julian day constructor.
	 *
	 * @param jd Julian day.
	 */
	public Coptic(int jd)
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
	public Coptic(long y, int m, int d)
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
		return (EPOCH - 1L) + 365L * (y - 1L) + Calendar.quotient(y, 4D) + (long) (30 * (m - 1)) + (long) d;
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
	 * Sets the date from the fixed day.
	 *
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		year = Calendar.quotient(4L * (l - EPOCH) + 1463L, 1461D);
		month = 1 + (int) Calendar.quotient(l - toFixed(year, 1, 1), 30D);
		day = (int) ((l + 1L) - toFixed(year, month, 1));
	}

	/**
	 * Returns if the year is a leap one.
	 *
	 * @param year Year.
	 * @return True if it is a leap year.
	 */
	public static boolean isLeapYear(long year)
	{
		return Calendar.mod(year, 4L) == 3L;
	}

	/**
	 * Transforms a Coptic date into a Julian day
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
	 * Transforms a Coptic date into a Julian day
	 * @return The Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Coptic date with a given Julian day
	 * @param jd The Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}
}
