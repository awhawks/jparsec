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
 * Implements the Bahai calendar.
 * <P>
 * A calendar based on the 19 year cycle 1844-1863 of the B&aacute;b, the martyred
 * forerunner of Bah&aacute;'u'l&aacute;lh and co-founder of the Bah&aacute;'&iacute; faith.
 * Days begin at sunset.
 * <P>
 * See Calendrical Calculations for reference.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Bahai implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Major.
	 */
	public long major = 0;

	/**
	 * Cycle number.
	 */
	public int cycle = 0;

	/**
	 * Year.
	 */
	public int year = 0;

	/**
	 * Month.
	 */
	public int month = 0;

	/**
	 * Day.
	 */
	public int day = 0;

	/**
	 * Calendar epoch.
	 */
	public static final long EPOCH = Gregorian.toFixed(1844L, 3, 21);

	/**
	 * Ayyam I ha.
	 */
	public static final int AYYAM_I_HA = 0;

	/**
	 * Name of the days of the week.
	 */
	public static final String DAY_OF_WEEK_NAMES[] =
	{ "Jamal", "Kamal", "Fidal", "`Idal", "Istijlal", "Istiqlal", "Jalal" };

	/**
	 * Name of the days of the month.
	 */
	public static final String DAY_OF_MONTH_NAMES[] =
	{ "Baha'", "Jalal", "Jamal", "`Azamat", "Nur", "Rahmat", "Kalimat", "Kamal", "Asma'", "`Izzat", "Mashiyyat",
			"`Ilm", "Qudrat", "Qawl", "Masa'il", "Sharaf", "Sultan", "Mulk", "`Ala'" };

	/**
	 * Name of the months.
	 */
	public static final String MONTH_NAMES[] =
	{ "Ayyam-i-Ha", "Baha'", "Jalal", "Jamal", "`Azamat", "Nur", "Rahmat", "Kalimat", "Kamal", "Asma'", "`Izzat",
			"Mashiyyat", "`Ilm", "Qudrat", "Qawl", "Masa'il", "Sharaf", "Sultan", "Mulk", "`Ala'" };

	/**
	 * Name of the years.
	 */
	public static final String YEAR_NAMES[] =
	{ "Alif", "Ba'", "Ab", "Dal", "Bab", "Vav", "Abad", "Jad", "Baha'", "Hubb", "Bahhaj", "Javab", "Ahad", "Vahhab",
			"Vidad", "Badi'", "Bahi", "Abha", "Vahid" };

	/**
	 * Empty constructor.
	 */
	public Bahai() {}

	/**
	 * Constructs a Bahai date with a Julian day.
	 * 
	 * @param jd Julian day.
	 */
	public Bahai(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Constructor using major, cycle, year, month, day.
	 * 
	 * @param mj Major.
	 * @param c Cycle.
	 * @param y Year.
	 * @param m Month.
	 * @param d Day.
	 */
	public Bahai(long mj, int c, int y, int m, int d)
	{
		major = mj;
		cycle = c;
		year = y;
		month = m;
		day = d;
	}

	/**
	 * Pass to fixed date.
	 * 
	 * @param major Major.
	 * @param cycle Cycle.
	 * @param year Year.
	 * @param month Month.
	 * @param day Day.
	 * @return The fixed date.
	 */
	public static long toFixed(long major, int cycle, int year, int month, int day)
	{
		long l1 = ((361L * (major - 1L) + (long) (19 * (cycle - 1)) + (long) year) - 1L) + Gregorian
				.yearFromFixed(EPOCH);
		long l2;
		if (month == 0)
			l2 = 342L;
		else if (month == 19)
			l2 = Gregorian.isLeapYear(l1 + 1L) ? 347 : '\u015A';
		else
			l2 = 19 * (month - 1);
		return Gregorian.toFixed(l1, 3, 20) + l2 + (long) day;
	}

	/**
	 * Pass to fixed date.
	 * @return The fixed date.
	 */
	public long toFixed()
	{
		return toFixed(major, cycle, year, month, day);
	}

	/**
	 * Gets the major, cycle, year, month, day of the instance from the fixed
	 * day.
	 * 
	 * @param fixed Fixed day number.
	 */
	public void fromFixed(long fixed)
	{
		long l1 = Gregorian.yearFromFixed(fixed);
		long l2 = Gregorian.yearFromFixed(EPOCH);
		long l3 = l1 - l2 - (long) (fixed > Gregorian.toFixed(l1, 3, 20) ? 0 : 1);
		major = 1L + Calendar.quotient(l3, 361D);
		cycle = 1 + (int) Calendar.quotient(Calendar.mod(l3, 361L), 19D);
		year = 1 + (int) Calendar.mod(l3, 19L);
		long l4 = fixed - toFixed(major, cycle, year, 1, 1);
		if (fixed >= toFixed(major, cycle, year, 19, 1))
			month = 19;
		else if (fixed >= toFixed(major, cycle, year, 0, 1))
			month = 0;
		else
			month = (int) (1L + Calendar.quotient(l4, 19D));
		day = (int) ((fixed + 1L) - toFixed(major, cycle, year, month, 1));
	}

	/**
	 * Gets the new year.
	 * 
	 * @param year Year to get its first day.
	 * @return The fixed day of the beggining of the year.
	 */
	public static long newYear(long year)
	{
		return Gregorian.toFixed(year, 3, 21);
	}

	/**
	 * Transforms a Bahai date into a Julian day.
	 * 
	 * @param major Major.
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
	 * Sets a Bahai date with a given Julian day.
	 * @param jd The Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}

	/**
	 * For unit testing only.
	 * @param args Not used.
	 */
	public static void main(String args[])
	{
		System.out.println("Bahai Test");

		int jd = 2451545;
		Bahai h = new Bahai(jd);
		System.out.println("JD " + jd + " = " + h.major + "/" + h.cycle + "/" + h.year + "/" + h.month + "/" + h.day);

		Bahai h2 = new Bahai(h.major, h.cycle, h.year, h.month, h.day);
		System.out
				.println("JD " + h2.toJulianDay() + " = " + h2.major + "/" + h2.cycle + "/" + h2.year + "/" + h2.month + "/" + h2.day);

		System.out.println(Calendar.nameFromDayOfWeek(Calendar.dayOfWeekFromFixed(h2.toFixed()), Bahai.DAY_OF_WEEK_NAMES));
		String month = Calendar.nameFromMonth(h2.day, Bahai.DAY_OF_MONTH_NAMES);
		month += " " + Calendar.nameFromNumber(h2.month, Bahai.DAY_OF_MONTH_NAMES);
		System.out.println(month);
		System.out.println(Calendar.nameFromMonth(h2.year, Bahai.YEAR_NAMES));

		System.out.println("(until sunset)");
	}
}
