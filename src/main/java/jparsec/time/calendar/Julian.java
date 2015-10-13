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
 * Implements the Julian calendar.
 * <P>
 * This solar calendar was instituted on January, 1, 709 A.U.C. (45 B.C.E.) by
 * Julius Caesar, with the help of the astronomer Sosigenes.
 * <P>
 * See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Julian implements Serializable
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
	public static final long EPOCH = Gregorian.toFixed(0L, 12, 30);

	/**
	 * Default constructor.
	 */
	public Julian()
	{
	}

	/**
	 * Julian day constructor.
	 *
	 * @param jd Julian day.
	 */
	public Julian(int jd)
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
	public Julian(long y, int m, int d)
	{
		year = y;
		month = m;
		day = d;
	}

	/**
	 * To fixed date.
	 *
	 * @param y Year.
	 * @param m Month.
	 * @param d Day.
	 * @return Fixed date.
	 */
	public static long toFixed(long y, int m, int d)
	{
		long l1 = y >= 0L ? y : y + 1L;
		return (EPOCH - 1L) + 365L * (l1 - 1L) + Calendar.quotient(l1 - 1L, 4D) + Calendar.quotient(367 * m - 362, 12D) + (long) (m > 2
				? isLeapYear(y) ? -1 : -2 : 0) + (long) d;
	}

	/**
	 * To fixed date.
	 *
	 * @return Fixed date.
	 */
	public long toFixed()
	{
		return toFixed(year, month, day);
	}

	/**
	 * Transforms a Julian date into a Julian day
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
	 * Transforms a Julian date into a Julian day
	 *
	 * @return Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Julian date with a given Julian day
	 *
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}

	/**
	 * Sets the date from a fixed date.
	 *
	 * @param l Fixed date.
	 */
	public void fromFixed(long l)
	{
		long l1 = Calendar.quotient(4L * (l - EPOCH) + 1464L, 1461D);
		year = l1 > 0L ? l1 : l1 - 1L;
		long l2 = l - toFixed(year, 1, 1);
		int i = l >= toFixed(year, 3, 1) ? ((int) (isLeapYear(year) ? 1 : 2)) : 0;
		month = (int) Calendar.quotient(12L * (l2 + (long) i) + 373L, 367D);
		day = (int) ((l - toFixed(year, month, 1)) + 1L);
	}

	/**
	 * Gives true if the year is a leap one.
	 *
	 * @param l Fixed date.
	 * @return True or false.
	 */
	public static boolean isLeapYear(long l)
	{
		return Calendar.mod(l, 4L) == (long) (l <= 0L ? 3 : 0);
	}
}
