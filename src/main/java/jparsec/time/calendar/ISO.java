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
 * Implements the ISO calendar. See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class ISO implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Year.
	 */
	public long year = 0;

	/**
	 * Week.
	 */
	public int week = 0;

	/**
	 * Day.
	 */
	public int day = 0;

	/**
	 * Calendar epoch.
	 */
	public static final long EPOCH = 1721425;

	/**
	 * Default constructor.
	 */
	public ISO() { }

	/**
	 * Julian day constructor.
	 *
	 * @param jd Julian day.
	 */
	public ISO(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Explicit constructor.
	 *
	 * @param y Year.
	 * @param w Week.
	 * @param d Day.
	 */
	public ISO(long y, int w, int d)
	{
		year = y;
		week = w;
		day = d;
	}

	/**
	 * To fixed date.
	 *
	 * @param year Year.
	 * @param week Week.
	 * @param day Day.
	 * @return Fixed day.
	 */
	public static long toFixed(long year, int week, int day)
	{
		return Calendar.nthKDay(week, 0, Gregorian.toFixed(year - 1L, 12, 28)) + (long) day;
	}

	/**
	 * To fixed day.
	 *
	 * @return Fixed date.
	 */
	public long toFixed()
	{
		return toFixed(year, week, day);
	}

	/**
	 * Sets the date from a fixed day.
	 *
	 * @param l Fixed date.
	 */
	public void fromFixed(long l)
	{
		long l1 = Gregorian.yearFromFixed(l - 3L);
		year = l < toFixed(l1 + 1L, 1, 1) ? l1 : l1 + 1L;
		week = (int) Calendar.quotient(l - toFixed(year, 1, 1), 7D) + 1;
		day = (int) Calendar.adjustedMod(l, 7L);
	}

	/**
	 * Transforms an ISO date into a Julian day
	 *
	 * @param year Year.
	 * @param week Week.
	 * @param day Day.
	 * @return Julian day.
	 */
	public static int toJulianDay(int year, int week, int day)
	{
		return (int) (toFixed(year, week, day) + EPOCH);
	}

	/**
	 * Transforms an ISO date into a Julian day.
	 *
	 * @return Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + EPOCH);
	}

	/**
	 * Sets an ISO date with a given Julian day.
	 *
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - EPOCH);
	}
}
