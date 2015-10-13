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
 * Implements the Future Bahai calendar. See Calendrical Calculations for
 * reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class BahaiFuture implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Major.
	 */
	public long major;

	/**
	 * Cycle.
	 */
	public int cycle;

	/**
	 * Year.
	 */
	public int year;

	/**
	 * Month.
	 */
	public int month;

	/**
	 * Day.
	 */
	public int day;

	/**
	 * Haifa location.
	 */
	public static final CityElement HAIFA = new CityElement("Haifa, Israel", 35D, 32.82, 2D, 0);

	/**
	 * Default constructor.
	 */
	public BahaiFuture()
	{
	}

	/**
	 * Julian day constructor.
	 *
	 * @param jd Julian day.
	 */
	public BahaiFuture(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Explicit constructor.
	 *
	 * @param bmajor Major.
	 * @param bcycle Cycle.
	 * @param byear Year.
	 * @param bmonth Month.
	 * @param bday Day.
	 */
	public BahaiFuture(long bmajor, int bcycle, int byear, int bmonth, int bday)
	{
		major = bmajor;
		cycle = bcycle;
		year = byear;
		month = bmonth;
		day = bday;
	}

	/**
	 * To fixed date.
	 *
	 * @param major Major
	 * @param cycle Cycle.
	 * @param year Year.
	 * @param month Month.
	 * @param day Day.
	 * @return Fixed day.
	 */
	public static long toFixed(long major, int cycle, int year, int month, int day)
	{
		long l1 = 361L * (major - 1L) + (long) (19 * (cycle - 1)) + (long) year;
		if (month == 19)
			return ((newYearOnOrBefore(Bahai.EPOCH + (long) Math.floor(365.242189D * ((double) l1 + 0.5D))) - 19L) + (long) day) - 1L;
		if (month == 0)
			return (newYearOnOrBefore(Bahai.EPOCH + (long) Math.floor(365.242189D * ((double) l1 - 0.5D))) + 342L + (long) day) - 1L;
		else
			return (newYearOnOrBefore(Bahai.EPOCH + (long) Math.floor(365.242189D * ((double) l1 - 0.5D))) + (long) ((month - 1) * 19) + (long) day) - 1L;
	}

	/**
	 * To fixed date.
	 * @return The fixed date.
	 */
	public long toFixed()
	{
		return toFixed(major, cycle, year, month, day);
	}

	/**
	 * Sets the date from the fixed day.
	 *
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		long l1 = newYearOnOrBefore(l);
		long l2 = Math.round((double) (l1 - Bahai.EPOCH) / 365.242189D);
		major = Calendar.quotient(l2, 361D) + 1L;
		cycle = (int) Calendar.quotient(Calendar.mod(l2, 361L), 19D) + 1;
		year = (int) Calendar.mod(l2, 19L) + 1;
		long l3 = l - l1;
		if (l >= toFixed(major, cycle, year, 19, 1))
			month = 19;
		else if (l >= toFixed(major, cycle, year, 0, 1))
			month = 0;
		else
			month = (int) Calendar.quotient(l3, 19D) + 1;
		day = (int) ((l + 1L) - toFixed(major, cycle, year, month, 1));
	}

	/**
	 * Sunset in Haifa.
	 *
	 * @param l Fixed day.
	 * @return Sunset time.
	 */
	public static double sunsetInHaifa(long l)
	{
		try
		{
			return Calendar.universalFromStandard(Calendar.sunset(l, HAIFA), HAIFA);
		} catch (Exception ex)
		{
			return 0.0D;
		}
	}

	/**
	 * New year before certain date.
	 *
	 * @param l Fixed day.
	 * @return Previous new year.
	 */
	public static long newYearOnOrBefore(long l)
	{
		double d = Calendar.estimatePriorSolarLongitude(sunsetInHaifa(l), Calendar.SPRING);
		long l1;
		for (l1 = (long) Math.floor(d) - 1L; Calendar.solarLongitude(sunsetInHaifa(l1)) > Calendar.SPRING + 2.0; l1++)
			;
		return l1;
	}

	/**
	 * Ridvan feast.
	 *
	 * @param l Fixed date.
	 * @return Ridvan date.
	 */
	public static long feastOfRidvan(long l)
	{
		long l1 = l - Gregorian.yearFromFixed(Bahai.EPOCH);
		long l2 = 1L + Calendar.quotient(l1, 361D);
		int i = 1 + (int) Calendar.quotient(Calendar.mod(l1, 361L), 19D);
		int j = 1 + (int) Calendar.mod(l1, 19L);
		return toFixed(l2, i, j, 2, 13);
	}

	/**
	 * Transforms a Bahai date into a Julian day
	 *
	 * @param major Major
	 * @param cycle Cycle.
	 * @param year Year.
	 * @param month Month.
	 * @param day Day.
	 * @return Julian day.
	 */
	public static int toJulianDay(long major, int cycle, int year, int month, int day)
	{
		return (int) (toFixed(major, cycle, year, month, day) + Gregorian.EPOCH);
	}

	/**
	 * Transforms a Bahai date into a Julian day.
	 * @return The Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Bahai date with a given Julian day
	 *
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}
}
