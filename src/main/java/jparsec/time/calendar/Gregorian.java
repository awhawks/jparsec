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
 * Implements the Gregorian calendar.
 * <P>
 * This solar calendar was designed by a commission assembled by Pope Gregory
 * XII in the 16th century. The main author is the astronomer Aloysius Lillius,
 * who changed the rules for century leap years from the old Julian calendar.
 * <P>
 * See Calendrical Calculations for reference.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Gregorian implements Serializable
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
	 * Gregorian epoch.
	 */
	public static final long EPOCH = 1721425;

	/**
	 * Names of the days of the week.
	 */
	public static final String DAY_OF_WEEK_NAMES[] =
	{ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	/**
	 * Names of the months.
	 */
	public static final String MONTH_NAMES[] =
	{ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November",
			"December" };

	/**
	 * Default constructor.
	 */
	public Gregorian() { }

	/**
	 * Create a Gregorian object from a Julian day.
	 * 
	 * @param jd Julian day.
	 */
	public Gregorian(int jd)
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
	public Gregorian(long y, int m, int d)
	{
		year = y;
		month = m;
		day = d;
	}

	/**
	 * To fixed date.
	 * 
	 * @param l Year.
	 * @param i Month.
	 * @param j Day.
	 * @return Fixed day.
	 */
	public static long toFixed(long l, int i, int j)
	{
		return ((365L * (l - 1L) + Calendar.quotient(l - 1L, 4D)) - Calendar.quotient(l - 1L, 100D)) + Calendar
				.quotient(l - 1L, 400D) + Calendar.quotient(367 * i - 362, 12D) + (long) (i > 2 ? isLeapYear(l) ? -1
				: -2 : 0) + (long) j;
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
	 * Transforms a Gregorian date into a Julian day
	 * 
	 * @param year Year.
	 * @param month Month.
	 * @param day Day.
	 * @return Julian day.
	 */
	public static int toJulianDay(int year, int month, int day)
	{
		return (int) (toFixed(year, month, day) + EPOCH);
	}

	/**
	 * Transforms a Gregorian date into a Julian day.
	 * @return The Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + EPOCH);
	}

	/**
	 * Sets a Gregorian date with a given Julian day
	 * 
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - EPOCH);
	}

	/**
	 * Sets the date from a fixed date.
	 * 
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		year = yearFromFixed(l);
		long l1 = l - toFixed(year, 1, 1);
		int i = l >= toFixed(year, 3, 1) ? ((int) (isLeapYear(year) ? 1 : 2)) : 0;
		month = (int) Calendar.quotient(12L * (l1 + (long) i) + 373L, 367D);
		day = (int) ((l - toFixed(year, month, 1)) + 1L);
	}

	/**
	 * Is this a leap year?
	 * 
	 * @param l Year.
	 * @return True if it is a leap year.
	 */
	public static boolean isLeapYear(long l)
	{
		boolean flag = false;
		if (Calendar.mod(l, 4L) == 0L)
		{
			long l1 = Calendar.mod(l, 400L);
			if (l1 != 100L && l1 != 200L && l1 != 300L)
				flag = true;
		}
		return flag;
	}

	/**
	 * Gets the last day of the month.
	 * 
	 * @return Number of days in this month.
	 */
	public int lastDayOfMonth()
	{
		switch (month)
		{
		case 2: // '\002'
			return !isLeapYear(year) ? 28 : 29;

		case 4: // '\004'
		case 6: // '\006'
		case 9: // '\t'
		case 11: // '\013'
			return 30;

		}
		return 31;
	}

	/**
	 * Gets day number in current year..
	 * 
	 * @return Day number.
	 */
	public long dayNumber()
	{
		return Calendar.difference(toFixed(year - 1L, 12, 31), toFixed());
	}

	/**
	 * Get days remaining until end of year.
	 * 
	 * @return Number of days.
	 */
	public long daysRemaining()
	{
		return Calendar.difference(toFixed(), toFixed(year, 12, 31));
	}

	/**
	 * Gets independence day.
	 * 
	 * @param l Fixed day.
	 * @return Such date.
	 */
	public static long independenceDay(long l)
	{
		return toFixed(l, 7, 4);
	}

	/**
	 * Gets labor day.
	 * 
	 * @param l Fixed day.
	 * @return Such date.
	 */
	public static long laborDay(long l)
	{
		return Calendar.firstKDay(1, toFixed(l, 9, 1));
	}

	/**
	 * Gets memorial day.
	 * 
	 * @param l Fixed day.
	 * @return Such date.
	 */
	public static long memorialDay(long l)
	{
		return Calendar.lastKDay(1, toFixed(l, 5, 31));
	}

	/**
	 * Gets election day.
	 * 
	 * @param l Fixed day.
	 * @return Such date.
	 */
	public static long electionDay(long l)
	{
		return Calendar.firstKDay(2, toFixed(l, 11, 2));
	}

	/**
	 * Gets Christmas day.
	 * 
	 * @param l Fixed day.
	 * @return Such date.
	 */
	public static long christmas(long l)
	{
		return toFixed(l, 12, 25);
	}

	/**
	 * Gets advent day.
	 * 
	 * @param l Fixed day.
	 * @return Such date.
	 */
	public static long advent(long l)
	{
		return Calendar.kDayNearest(toFixed(l, 11, 30), 0);
	}

	/**
	 * Gets epiphany day.
	 * 
	 * @param l Fixed day.
	 * @return Such date.
	 */
	public static long epiphany(long l)
	{
		return Calendar.firstKDay(0, toFixed(l, 1, 2));
	}

	/**
	 * Gets the year from a fixed date.
	 * 
	 * @param l Fixed date.
	 * @return Year.
	 */
	public static long yearFromFixed(long l)
	{
		long l1 = l - 1L;
		long l2 = Calendar.quotient(l1, 146097D);
		long l3 = Calendar.mod(l1, 0x23ab1L);
		long l4 = Calendar.quotient(l3, 36524D);
		long l5 = Calendar.mod(l3, 36524L);
		long l6 = Calendar.quotient(l5, 1461D);
		long l7 = Calendar.mod(l5, 1461L);
		long l8 = Calendar.quotient(l7, 365D);
		long l9 = 400L * l2 + 100L * l4 + 4L * l6 + l8;

		if (l4 == 4L || l8 == 4L)
			return l9;

		return l9 + 1L;
	}
}
