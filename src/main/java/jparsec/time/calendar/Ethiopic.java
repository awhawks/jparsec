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
 * Implements the Ethiopic calendar. See Calendrical Calculations for reference.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Ethiopic implements Serializable
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
	public static final long EPOCH = Julian.toFixed(8, 8, 29);

	/**
	 * Day of week names.
	 */
	public static final String DAY_OF_WEEK_NAMES[] =
	{ "Ihud", "Sanyo", "Maksanyo", "Rob", "Hamus", "Arb", "Kidamme" };

	/**
	 * Month names.
	 */
	public static final String MONTH_NAMES[] =
	{ "Maskaram", "Teqemt", "Kehdar", "Takhsas", "Ter", "Yakatit", "Magabit", "Miyazya", "Genbot", "Sane", "Hamle",
			"Nahase", "Paguemen" };

	/**
	 * Empty constructor.
	 */
	public Ethiopic() {}

	/**
	 * Julian day constructor.
	 * 
	 * @param jd Julian day.
	 */
	public Ethiopic(int jd)
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
	public Ethiopic(long y, int m, int d)
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
		return (Coptic.toFixed(y, m, d) - Coptic.EPOCH) + EPOCH;
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
		Coptic coptic = new Coptic();
		coptic.fromFixed((l + Coptic.EPOCH) - EPOCH);
		month = coptic.month;
		day = coptic.day;
		year = coptic.year;
	}

	/**
	 * Transforms a Ethiopic date into a Julian day
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
	 * Transforms a Ethiopic date into a Julian day.
	 * @return The Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Ethiopic date with a given Julian day.
	 * @param jd The Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}
}
