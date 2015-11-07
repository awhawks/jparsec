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
 * Implements the Islamic arithmetic calendar.
 * <P>
 * The lunar calendar used by Moslems. Days begin at sunset.
 * <P>
 * See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Islamic implements Serializable
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
	public static final long EPOCH = new Julian(622, 7, 16).fixed;

	/**
	 * Mecca location.
	 */
	public static final CityElement MECCA = new CityElement("Mecca, Saudi Arabia", Calendar.angle(39, 49, 24),
			Calendar.angle(21, 25, 24), 2, 1000);

	/**
	 * Day of week names.
	 */
	public static final String DAY_OF_WEEK_NAMES[] = {
		"yaum al-ahad", "yaum al-ithnayna", "yaum ath-thalatha'", "yaum al-arba`a'", "yaum al-hamis", "yaum al-jum`a",
		"yaum as-sabt"
	};

	/**
	 * Month names.
	 */
	public static final String MONTH_NAMES[] = {
		"Muharram", "Safar", "Rabi I", "Rabi II", "Jumada I", "Jumada II", "Rajab", "Sha`ban", "Ramadan", "Shawwal",
		"Dhu al-Qa`da", "Dhu al-Hijja"
	};

	/**
	 * Default constructor.
	 */
	public Islamic() {}

	/**
	 * Julian day constructor.
	 *
	 * @param jd Julian day.
	 */
	public Islamic(int jd)
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
	public Islamic(long y, int m, int d)
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
	 * @return Fixed day.
	 */
	public static long toFixed(long y, int m, int d)
	{
		return ((long) (d + 29 * (m - 1)) + Calendar.quotient(6 * m - 1, 11D) + (y - 1L) * 354L + Calendar.quotient(
				3L + 11L * y, 30D) + EPOCH) - 1L;
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
	 * Sets the date from a fixed day.
	 *
	 * @param l Fixed date.
	 */
	public void fromFixed(long l)
	{
		year = Calendar.quotient(30L * (l - EPOCH) + 10646L, 10631D);
		long l1 = l - toFixed(year, 1, 1);
		month = (int) Calendar.quotient(11L * l1 + 330L, 325D);
		day = (int) ((1L + l) - toFixed(year, month, 1));
	}

	/**
	 * To know if the year is a leap one.
	 *
	 * @param l Fixed day.
	 * @return True if it is a leap year.
	 */
	public static boolean isLeapYear(long l)
	{
		return Calendar.mod(11L * l + 14L, 30L) < 11L;
	}

	/**
	 * Transforms an Islamic date into a Julian day
	 *
	 * @return Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets an Islamic date with a given Julian day
	 *
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}
}
