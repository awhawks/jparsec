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
 * Implements the French revolution calendar.
 * <P>
 * This calendar was instituted by the National Convention of the French
 * Republic in 1793. It was used until the end of Gregorian year 1805.
 * <P>
 * See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class French implements Serializable
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
	public static final long EPOCH = Gregorian.toFixed(1792L, 9, 22);

	/**
	 * Paris location.
	 */
	public static final CityElement PARIS = new CityElement("Paris, France", Calendar.angle(2D, 20D, 15D), Calendar.angle(48D, 50D, 11D), 1.0, 27);

	/**
	 * Month poetic names coined by Fabre d'Eglantine.
	 */
	public static final String MONTH_NAMES[] = { "Vendemiaire", "Brumaire", "Frimaire", "Nivose", "Pluviose", "Ventose", "Germinal", "Floreal", "Prairial", "Messidor", "Thermidor", "Fructidor", "Sansculottides" };

	/**
	 * Day of week names.
	 */
	public static final String DAY_OF_WEEK_NAMES[] = { "Primidi", "Duodi", "Tridi", "Quartidi", "Quintidi", "Sextidi", "Septidi", "Octidi", "Nonidi", "Decadi" };

	/**
	 * Special day names.
	 */
	public static final String SPECIAL_DAY_NAMES[] = { "Jour de la Vertu", "Jour du Genie", "Jour du Labour", "Jour de la Raison", "Jour de la Recompense", "Jour de la Revolution" };

	/**
	 * Decade names.
	 */
	public static final String DECADE_NAMES[] = { "I", "II", "III" };

	/**
	 * Default constructor.
	 */
	public French() {}

	/**
	 * Constructs a Hebrew date with a Julian day
	 *
	 * @param jd Julian day
	 */
	public French(int jd)
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
	public French(long y, int m, int d)
	{
		year = y;
		month = m;
		day = d;
	}

	/**
	 * To fixed day..
	 *
	 * @param y Year.
	 * @param m Month.
	 * @param d Day.
	 * @return Fixed day.
	 */
	public static long toFixed(long y, int m, int d)
	{
		long l1 = newYearOnOrBefore((long) Math.floor((double) (EPOCH + 180L) + 365.242189D * (double) (y - 1L)));
		return (l1 - 1L) + (long) (30 * (m - 1)) + (long) d;
	}

	/**
	 * To fixed day.
	 * @return The fixed day.
	 */
	public long toFixed()
	{
		return toFixed(year, month, day);
	}

	/**
	 * Sets the date from the fixed day.
	 *
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		long l1 = newYearOnOrBefore(l);
		year = Math.round((double) (l1 - EPOCH) / 365.242189D) + 1L;
		month = 1 + (int) Calendar.quotient(l - l1, 30D);
		day = 1 + (int) Calendar.mod(l - l1, 30L);
	}

	/**
	 * Transforms a French date into a Julian day
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
	 * Transforms a French date into a Julian day.
	 * @return The Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a French date with a given Julian day
	 *
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}

	/**
	 * Midnight in Paris.
	 *
	 * @param l Fixed day.
	 * @return Midnight time.
	 */
	public static double midnightInParis(long l)
	{
		return Calendar.universalFromStandard(Calendar.midnight(l + 1L, PARIS), PARIS);
	}

	/**
	 * New year before certain fixed date.
	 *
	 * @param l Fixed date.
	 * @return New year fixed date.
	 */
	public static long newYearOnOrBefore(long l)
	{
		double d = Calendar.estimatePriorSolarLongitude(midnightInParis(l), Calendar.AUTUMN);
		long l1;
		for (l1 = (long) (Math.floor(d) - 1.0D); Calendar.AUTUMN > Calendar.solarLongitude(midnightInParis(l1)); l1++)
			;
		return l1;
	}

	/**
	 * Gets the day of the week.
	 *
	 * @return Day of week.
	 */
	public int getDayOfWeek()
	{
		int d = this.day + 9;
        if (this.month < 13) day = (this.day - 1) % 10;
        d++;
		if (d > French.DAY_OF_WEEK_NAMES.length) d -= French.DAY_OF_WEEK_NAMES.length;
		if (d < 0) d += French.DAY_OF_WEEK_NAMES.length;
		return d;
	}

	/**
	 * Gets the decadi.
	 *
	 * @return Decadi index (0, 1, 2), or -1 for no decadi.
	 */
	public int getDecadi()
	{
		int week = -1;
        if (this.month < 13) week = (this.day - 1) / 10 + 1;
        if (week > 2) week = week - 3;
		return week;
	}
}
