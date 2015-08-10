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
 * Implements the Armenian calendar. See Calendrical Calculations for reference.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Armenian implements Serializable
{
	static final long serialVersionUID = 1L;

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
	 * Calendar epoch.
	 */
	public static final long EPOCH = 0x312e3L;

	/**
	 * Name of days of the week.
	 */
	public static final String DAY_OF_WEEK_NAMES[] =
	{ "Miashabathi", "Erkoushabathi", "Erekhshabathi", "Chorekhshabathi", "Hingshabathi", "Urbath", "Shabath" };

	/**
	 * Name of months.
	 */
	public static final String MONTH_NAMES[] =
	{ "Nawasardi", "Hori", "Sahmi", "Tre", "Kaloch", "Arach", "Mehekani", "Areg", "Ahekani", "Mareri", "Margach",
			"Hrotich", "Aweleach" };

	/**
	 * Empty constructor.
	 */
	public Armenian() { }

	/**
	 * Constructor using a Julian day.
	 * 
	 * @param jd Julian day.
	 */
	public Armenian(int jd)
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
	public Armenian(long y, int m, int d)
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
		return (0x312e3L + Egyptian.toFixed(year, month, day)) - Egyptian.EPOCH;
	}

	/**
	 * Pass to fixed date.
	 * @return Fixed day.
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
		Egyptian egyptian = new Egyptian();
		egyptian.fromFixed((l + Egyptian.EPOCH) - 0x312e3L);
		year = egyptian.year;
		month = egyptian.month;
		day = egyptian.day;
	}

	/**
	 * Transforms an Armenian date into a Julian day.
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
	 * Transforms an Armenian date into a Julian day.
	 * @return Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets an Armenian date with a given Julian day.
	 * @param jd The Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}
}
