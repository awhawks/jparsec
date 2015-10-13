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
 * Implements the Arithmetic Persian calendar.
 * <P>
 * This is the old Persian calendar from the 11st century, designed by a
 * committee of astronomers including Omar Khayyam.
 *
 * See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class PersianArithmetic implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Year.
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
	 * Empty constructor.
	 */
	public PersianArithmetic() { }

	/**
	 * Constructor using a Julian day.
	 *
	 * @param jd Julian day.
	 */
	public PersianArithmetic(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Constructor using year, month, day.
	 *
	 * @param y Year.
	 * @param m Month.
	 * @param d Day.
	 */
	public PersianArithmetic(long y, int m, int d)
	{
		year = y;
		month = m;
		day = d;
	}

	/**
	 * Pass to fixed date.
	 *
	 * @param year Year.
	 * @param month Month.
	 * @param day Day.
	 * @return Fixed day number.
	 */
	public static long toFixed(long year, int month, int day)
	{
		long l1 = year <= 0L ? year - 473L : year - 474L;
		long l2 = Calendar.mod(l1, 2820L) + 474L;
		return (Persian.EPOCH - 1L) + 0xfb75fL * Calendar.quotient(l1, 2820D) + 365L * (l2 - 1L) + Calendar.quotient(
				682L * l2 - 110L, 2816D) + (long) (month > 7 ? 30 * (month - 1) + 6 : 31 * (month - 1)) + (long) day;
	}

	/**
	 * Pass to fixed date.
	 * @return Fixed date.
	 */
	public long toFixed()
	{
		return toFixed(year, month, day);
	}

	/**
	 * Gets the year, month, day of the instance from the fixed day.
	 *
	 * @param l Fixed day number.
	 */
	public void fromFixed(long l)
	{
		year = yearFromFixed(l);
		long l1 = (1L + l) - toFixed(year, 1, 1);
		month = (int) (l1 >= 186L ? Math.ceil((double) (l1 - 6L) / 30D) : Math.ceil((double) l1 / 31D));
		day = (int) (l - (toFixed(year, month, 1) - 1L));
	}

	/**
	 * True if the year is a leap one, false otherwise.
	 *
	 * @param year Year.
	 * @return True or false.
	 */
	public static boolean isLeapYear(long year)
	{
		long l1 = year <= 0L ? year - 473L : year - 474L;
		long l2 = Calendar.mod(l1, 2820L) + 474L;
		return Calendar.mod((l2 + 38L) * 682L, 2816L) < 682L;
	}

	/**
	 * Gets the year from a fixed date.
	 *
	 * @param l Fixed day number.
	 * @return Current year.
	 */
	public static long yearFromFixed(long l)
	{
		long l1 = l - toFixed(475L, 1, 1);
		long l2 = Calendar.quotient(l1, 1029983D);
		long l3 = Calendar.mod(l1, 0xfb75fL);
		long l4 = l3 != 0xfb75eL ? Calendar.quotient(2816D * (double) l3 + 1031337D, 1028522D) : 2820L;
		long l5 = 474L + 2820L * l2 + l4;
		if (l5 > 0L)
			return l5;
		else
			return l5 - 1L;
	}

	/**
	 * Transforms a Persian date into a Julian day.
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
	 * Transforms a Persian date into a Julian day.
	 * @return The Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Persian date with a given Julian day.
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}
}
