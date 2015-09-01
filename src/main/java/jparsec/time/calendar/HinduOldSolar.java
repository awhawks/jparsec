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
 * Implements the Old Hindu Solar calendar. See Calendrical Calculations for
 * reference.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class HinduOldSolar implements Serializable
{
	static final long serialVersionUID = 1L;

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
	public static final long EPOCH = Julian.toFixed(-3102L, 2, 18);

	/**
	 * Arya solar year.
	 */
	public static final double ARYA_SOLAR_YEAR = 365.25868055555554D;

	/**
	 * Arya solar month.
	 */
	public static final double ARYA_SOLAR_MONTH = 30.43822337962963D;

	/**
	 * Arya Jupiter period.
	 */
	public static final double ARYA_JOVIAN_PERIOD = 4332.2721731681604D;

	/**
	 * Day of week names.
	 */
	public static final String DAY_OF_WEEK_NAMES[] =
	{ "Ravivara", "Chandravara", "Mangalavara", "Buddhavara", "Brihaspatvara", "Sukravara", "Sanivara" };

	/**
	 * Month names.
	 */
	public static final String MONTH_NAMES[] =
	{ "Mesha", "Vrishabha", "Mithuna", "Karka", "Simha", "Kanya", "Tula", "Vrischika", "Dhanu", "Makara", "Kumbha",
			"Mina" };

	/**
	 * Default constructor.
	 */
	public HinduOldSolar() {}

	/**
	 * Julian day constructor.
	 * 
	 * @param jd Julian day.
	 */
	public HinduOldSolar(int jd)
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
	public HinduOldSolar(long y, int m, int d)
	{
		year = y;
		month = m;
		day = d;
	}

	/**
	 * To fixed day.
	 * 
	 * @param year Year.
	 * @param month Month.
	 * @param day Day.
	 * @return Fixed day.
	 */
	public static long toFixed(long year, int month, int day)
	{
		return (long) Math
				.ceil(((double) EPOCH + (double) year * 365.25868055555554D + (double) (month - 1) * 30.43822337962963D + (double) day) - 1.25D);
	}

	/**
	 * To fixed day.
	 * 
	 * @return Fixed date.
	 */
	public long toFixed()
	{
		return toFixed(year, month, day);
	}

	/**
	 * Sets the date from fixed date.
	 * 
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		double d = (double) dayCount(l) + 0.25D;
		year = Calendar.quotient(d, 365.25868055555554D);
		month = 1 + (int) Calendar.mod(Calendar.quotient(d, 30.43822337962963D), 12L);
		day = 1 + (int) Math.floor(Calendar.mod(d, 30.43822337962963D));
	}

	/**
	 * Gets days elapsed since epoch.
	 * 
	 * @param l Fixed day.
	 * @return Elapsed days.
	 */
	public static long dayCount(long l)
	{
		return l - EPOCH;
	}

	/**
	 * Gets days elapsed since epoch.
	 * 
	 * @param d Fixed day.
	 * @return Elapsed days.
	 */
	public static double dayCount(double d)
	{
		return d - (double) EPOCH;
	}

	/**
	 * Gets the jovian year.
	 * 
	 * @param l Fixed day.
	 * @return Jovian year.
	 */
	public static int jovianYear(long l)
	{
		return 1 + (int) Calendar.mod(Calendar.quotient(dayCount(l), 361.02268109734672D), 60L);
	}

	/**
	 * Transforms a Hindu date into a Julian day.
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
	 * Transforms a Hindu date into a Julian day.
	 * 
	 * @return Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Hindu date with a given Julian day
	 * 
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}
}
