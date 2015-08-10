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

import jparsec.observer.CityElement;

/**
 * Implements the Observational Islamic calendar. See Calendrical Calculations
 * for reference.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class IslamicObservational implements Serializable
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
	 * Cairo location.
	 */
	public static final CityElement CAIRO = new CityElement("Cairo, Egypt", 31.3, 30.1, 2D, 200);

	/**
	 * Islamic location, currently set to Cairo.
	 */
	public static final CityElement ISLAMIC_LOCALE = CAIRO;

	/**
	 * Default constructor.
	 */
	public IslamicObservational()
	{
	}

	/**
	 * Julian day constructor.
	 * 
	 * @param jd Julian day.
	 */
	public IslamicObservational(int jd)
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
	public IslamicObservational(long y, int m, int d)
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
		long l1;
		try
		{
			long l2 = Islamic.EPOCH + (long) Math
					.floor(((double) ((y - 1L) * 12L + (long) m) - 0.5D) * 29.530588853000001D);
			l1 = (Calendar.phasisOnOrBefore(l2, ISLAMIC_LOCALE) + (long) d) - 1L;
		} catch (Exception ex)
		{
			l1 = 0L;
		}
		return l1;
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
	 * @param l Fixed date.
	 */
	public void fromFixed(long l)
	{
		try
		{
			long l1 = Calendar.phasisOnOrBefore(l, ISLAMIC_LOCALE);
			long l2 = Math.round((double) (l1 - Islamic.EPOCH) / 29.530588853);
			year = Calendar.quotient(l2, 12D) + 1L;
			month = (int) (Calendar.mod(l2, 12L) + 1L);
			day = (int) ((l - l1) + 1L);
			return;
		} catch (Exception ex)
		{
			return;
		}
	}

	/**
	 * Transforms an Islamic date into a Julian day.
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
	 * Transforms an Islamic date into a Julian day.
	 * 
	 * @return Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets an Islamic date with a given Julian day.
	 * 
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}
}
