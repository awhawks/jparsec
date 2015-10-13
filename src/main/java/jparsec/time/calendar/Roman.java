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
 * Implements the Roman calendar. See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Roman implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Year.
	 */
	public long year = 0;

	/**
	 * Month.
	 */
	public int month = 0;

	/**
	 * Event.
	 */
	public int event = 0;

	/**
	 * Count.
	 */
	public int count = 0;

	/**
	 * Leap day.
	 */
	public boolean leapDay = false;

	/**
	 * Calendar epoch.
	 */
	public static final long EPOCH = 0;

	/**
	 * Kalends ID.
	 */
	public static final int KALENDS = 1;

	/**
	 * Nones ID.
	 */
	public static final int NONES = 2;

	/**
	 * Ides ID.
	 */
	public static final int IDES = 3;

	/**
	 * Count names.
	 */
	public static final String COUNT_NAMES[] =
	{ "", "pridie ", "ante diem iii ", "ante diem iv ", "ante diem v ", "ante diem vi ", "ante diem vii ",
			"ante diem viii ", "ante diem ix ", "ante diem x ", "ante diem xi ", "ante diem xii ", "ante diem xiii ",
			"ante diem xiv ", "ante diem xv ", "ante diem xvi ", "ante diem xvii ", "ante diem xviii ",
			"ante diem xix " };

	/**
	 * Event names.
	 */
	public static final String EVENT_NAMES[] = { "Kalends", "Nones", "Ides" };

	/**
	 * Default constructor.
	 */
	public Roman() { }

	/**
	 * Julian day constructor.
	 *
	 * @param jd Julian day.
	 */
	public Roman(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Explicit constructor.
	 *
	 * @param ryear Year.
	 * @param rmonth Month.
	 * @param revent Event.
	 * @param rcount Count.
	 * @param rleapDay Leap day.
	 */
	public Roman(long ryear, int rmonth, int revent, int rcount, boolean rleapDay)
	{
		year = ryear;
		month = rmonth;
		event = revent;
		count = rcount;
		leapDay = rleapDay;
	}

	/**
	 * To fixed day.
	 *
	 * @param year Year.
	 * @param month Month.
	 * @param event Event.
	 * @param count Count.
	 * @param leapDay Leap day.
	 * @return Fixed date.
	 */
	public static long toFixed(long year, int month, int event, int count, boolean leapDay)
	{
		long l1 = 0L;
		if (event == 1)
			l1 = Julian.toFixed(year, month, 1);
		else if (event == 2)
			l1 = Julian.toFixed(year, month, nonesOfMonth(month));
		else if (event == 3)
			l1 = Julian.toFixed(year, month, idesOfMonth(month));
		return (l1 - (long) count) + (long) (!Julian.isLeapYear(year) || month != 3 || event != 1 || count > 16 || count < 6
				? 1 : 0) + (long) (leapDay ? 1 : 0);
	}

	/**
	 * To fixed day.
	 *
	 * @return Fixed date.
	 */
	public long toFixed()
	{
		return toFixed(year, month, event, count, leapDay);
	}

	/**
	 * Transforms a Roman date into a Julian day.
	 *
	 * @param year Year.
	 * @param month Month.
	 * @param event Event.
	 * @param count Count.
	 * @param leap Leap day.
	 * @return Julian day.
	 */
	public static int toJulianDay(int year, int month, int event, int count, boolean leap)
	{
		return (int) (toFixed(year, month, event, count, leap) + Gregorian.EPOCH);
	}

	/**
	 * Transforms a Roman date into a Julian day.
	 *
	 * @return Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Roman date with a given Julian day.
	 *
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}

	/**
	 * Sets the date from fixed date.
	 *
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		Julian julian = new Julian();
		julian.fromFixed(l);
		int i = julian.month;
		int j = julian.day;
		long l1 = julian.year;
		int k = Calendar.adjustedMod(i + 1, 12);
		long l2 = k != 1 ? l1 : l1 + 1L;
		long l3 = toFixed(l2, k, 1, 1, false);
		if (j == 1)
		{
			year = l1;
			month = i;
			event = 1;
			count = 1;
			leapDay = false;
			return;
		}
		if (j <= nonesOfMonth(i))
		{
			year = l1;
			month = i;
			event = 2;
			count = (nonesOfMonth(i) - j) + 1;
			leapDay = false;
			return;
		}
		if (j <= idesOfMonth(i))
		{
			year = l1;
			month = i;
			event = 3;
			count = (idesOfMonth(i) - j) + 1;
			leapDay = false;
			return;
		}
		if (i != 2 || !Julian.isLeapYear(l1))
		{
			year = l2;
			month = k;
			event = 1;
			count = (int) ((l3 - l) + 1L);
			leapDay = false;
			return;
		}
		if (j < 25)
		{
			year = l1;
			month = 3;
			event = 1;
			count = 30 - j;
			leapDay = false;
			return;
		} else
		{
			year = l1;
			month = 3;
			event = 1;
			count = 31 - j;
			leapDay = j == 25;
			return;
		}
	}

	/**
	 * Return ides.
	 *
	 * @param i Month.
	 * @return Ides, 13 if month = 3,5,7, or 10, and 15 otherwise.
	 */
	private static int idesOfMonth(int i)
	{
		return i != 3 && i != 5 && i != 7 && i != 10 ? 13 : 15;
	}

	/**
	 * Return nones of month.
	 *
	 * @param i Month.
	 * @return Nones = ides - 8.
	 */
	private static int nonesOfMonth(int i)
	{
		return idesOfMonth(i) - 8;
	}
}
