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
 * Implements the Mayan Tzolkin calendar. See Calendrical Calculations for
 * reference.
 * <P>
 * Note that it is not possible to pass from a given date to a Julian day, since
 * no year exists in this calendar.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class MayanTzolkin implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Number.
	 */
	public int number = 0;

	/**
	 * Name.
	 */
	public int name = 0;

	/**
	 * Calendar epoch.
	 */
	public static final long EPOCH = MayanLongCount.EPOCH - (long) ordinal(new MayanTzolkin(4, 20));

	/**
	 * Month names.
	 */
	public static final String NAMES[] =
	{ "Imix", "Ik", "Akbal", "Kan", "Chicchan", "Cimi", "Manik", "Lamat", "Muluc", "Oc", "Chuen", "Eb", "Ben", "Ix",
			"Men", "Cib", "Caban", "Etznab", "Cauac", "Ahau" };

	/**
	 * Default constructor.
	 */
	public MayanTzolkin() {}

	/**
	 * Julian day constructor.
	 * 
	 * @param jd Julian day.
	 */
	public MayanTzolkin(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Explicit constructor.
	 * 
	 * @param mnumber Number.
	 * @param mname Name.
	 */
	public MayanTzolkin(int mnumber, int mname)
	{
		number = mnumber;
		name = mname;
	}

	/**
	 * Sets the date from fixed day.
	 * 
	 * @param l Fixed date.
	 */
	public void fromFixed(long l)
	{
		long l1 = (l - EPOCH) + 1L;
		number = (int) Calendar.adjustedMod(l1, 13L);
		name = (int) Calendar.adjustedMod(l1, 20L);
	}

	/**
	 * Gets the ordinal.
	 * 
	 * @param mayantzolkin MayanTzolkin instance.
	 * @return Days elapsed since new year.
	 */
	public static int ordinal(MayanTzolkin mayantzolkin)
	{
		return Calendar.mod((mayantzolkin.number - 1) + 39 * (mayantzolkin.number - mayantzolkin.name), 260);
	}

	/**
	 * Gets last new year.
	 * 
	 * @param mayantzolkin MayanTzolkin instance.
	 * @param l Fixed day.
	 * @return Such date.
	 */
	public static long onOrBefore(MayanTzolkin mayantzolkin, long l)
	{
		return l - Calendar.mod(l - EPOCH - (long) ordinal(mayantzolkin), 260L);
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
}
