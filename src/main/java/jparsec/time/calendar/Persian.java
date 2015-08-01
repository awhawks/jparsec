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
 * Implements the Persian calendar.
 * <P>
 * The modern Persian solar calendar, adopted in 1925, and based on the old
 * arithmetic calendar.
 * 
 * See Calendrical Calculations for reference.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Persian implements Serializable
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
	public static final long EPOCH = Julian.toFixed(622, 3, 19);

	/**
	 * Tehran location.
	 */
	public static final CityElement TEHRAN = new CityElement("Tehran, Iran", 51.42, 35.68, 3.5D, 1100);

	/**
	 * Day of week names.
	 */
	public static final String DAY_OF_WEEK_NAMES[] =
	{ "Yek-shanbeh", "Do-shanbeh", "Se-shanbeh", "Char-shanbeh", "Panj-shanbeh", "Jom`eh", "Shanbeh" };

	/**
	 * Month names.
	 */
	public static final String MONTH_NAMES[] =
	{ "Farvardin", "Ordibehesht", "Xordad", "Tir", "Mordad", "Shahrivar", "Mehr", "Aban", "Azar", "Dey", "Bahman",
			"Esfand" };

	/**
	 * Default constructor.
	 */
	public Persian() {}

	/**
	 * Julian day constructor.
	 * 
	 * @param jd Julian day.
	 */
	public Persian(int jd)
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
	public Persian(long y, int m, int d)
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
	 * @return Fixed date.
	 */
	public static long toFixed(long y, int m, int d)
	{
		long l1 = newYearOnOrBefore(EPOCH + 180L + (long) Math.floor(365.242189D * (double) (y <= 0L ? y : y - 1L)));
		return (l1 - 1L) + (long) (m > 7 ? 30 * (m - 1) + 6 : 31 * (m - 1)) + (long) d;
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
	 * Transforms a Persian date into a Julian day
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
	 * 
	 * @return Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Persian date with a given Julian day.
	 * 
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}

	/**
	 * Sets the date from a fixed day.
	 * 
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		long l1 = newYearOnOrBefore(l);
		long l2 = 1L + Math.round((double) (l1 - EPOCH) / 365.242189D);
		year = l2 <= 0L ? l2 - 1L : l2;
		long l3 = (1L + l) - toFixed(year, 1, 1);
		month = l3 >= 186L ? (int) Math.ceil((double) (l3 - 6L) / 30D) : (int) Math.ceil((double) l3 / 31D);
		day = (int) (l - (toFixed(year, month, 1) - 1L));
	}

	/**
	 * Gets the midday in Tehran.
	 * 
	 * @param l Fixed day.
	 * @return Midday time.
	 */
	public static double middayInTehran(long l)
	{
		return Calendar.universalFromStandard(Calendar.midday(l, TEHRAN), TEHRAN);
	}

	/**
	 * Gets previous new year.
	 * 
	 * @param l Fixed day.
	 * @return Last new year.
	 */
	public static long newYearOnOrBefore(long l)
	{
		double d = Calendar.estimatePriorSolarLongitude(middayInTehran(l), Calendar.SPRING);
		long l1;
		for (l1 = (long) Math.floor(d) - 1L; Calendar.solarLongitude(middayInTehran(l1)) > Calendar.SPRING + 2.0; l1++)
			;
		return l1;
	}

	/**
	 * Gets new year.
	 * 
	 * @param l Fixed day.
	 * @return New Year.
	 */
	public static long nawRuz(long l)
	{
		long l1 = (1L + l) - Gregorian.yearFromFixed(EPOCH);
		return toFixed(l1 > 0L ? l1 : l1 - 1L, 1, 1);
	}

	/**
	 * For unit testing only.
	 * @param args Not used.
	 */
	public static void main(String args[])
	{
		System.out.println("Persian Test");

		int jd = 2451545;
		Persian h = new Persian(jd);
		System.out.println("JD " + jd + " = " + h.year + "/" + h.month + "/" + h.day);

		Persian h2 = new Persian(h.year, h.month, h.day);
		System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + "/" + h2.month + "/" + h2.day);

		System.out.println(Calendar.nameFromMonth(h2.month, Persian.MONTH_NAMES));
		System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Persian.DAY_OF_WEEK_NAMES));
	}
}
