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
 * Implements the Mayan Haab calendar. See Calendrical Calculations for
 * reference.
 * <P>
 * Note that it is not possible to pass from a given date to a Julian day, since
 * no year exists in this calendar.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class MayanHaab implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Month
	 */
	public int month = 0;

	/**
	 * Day.
	 */
	public int day = 0;

	/**
	 * Calendar epoch.
	 */
	public static final long EPOCH = MayanLongCount.EPOCH - (long) ordinal(new MayanHaab(18, 8));

	/**
	 * Month names.
	 */
	public static final String MONTH_NAMES[] =
	{ "Pop", "Uo", "Zip", "Zotz", "Tzec", "Xul", "Yaxkin", "Mol", "Chen", "Yax", "Zac", "Ceh", "Mac", "Kankin", "Muan",
			"Pax", "Kayab", "Cumku", "Uayeb" };

	/**
	 * Default constructor.
	 */
	public MayanHaab() {}

	/**
	 * Fixed day constructor.
	 * 
	 * @param l Fixed day.
	 */
	public MayanHaab(long l)
	{
		fromFixed(l);
	}

	/**
	 * Julian day constructor.
	 * 
	 * @param jd Julian day.
	 */
	public MayanHaab(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Explicit constructor.
	 * 
	 * @param m Month.
	 * @param d Day.
	 */
	public MayanHaab(int m, int d)
	{
		month = m;
		day = d;
	}

	/**
	 * Sets the date from fixed day.
	 * 
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		long l1 = Calendar.mod(l - EPOCH, 365L);
		day = (int) Calendar.mod(l1, 20L);
		month = 1 + (int) Calendar.quotient(l1, 20D);
	}

	/**
	 * Sets a Mayan date with a given Julian day
	 * 
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}

	/**
	 * Gets elapsed days since last new year.
	 * 
	 * @param mayanhaab MayanHaab instance.
	 * @return Ordinal.
	 */
	public static int ordinal(MayanHaab mayanhaab)
	{
		return (mayanhaab.month - 1) * 20 + mayanhaab.day;
	}

	/**
	 * Gets last new year.
	 * 
	 * @param mayanhaab MayanHaab instance.
	 * @param l Fixed day.
	 * @return Such date.
	 */
	public static long onOrBefore(MayanHaab mayanhaab, long l)
	{
		return l - Calendar.mod(l - EPOCH - (long) ordinal(mayanhaab), 365L);
	}
}
