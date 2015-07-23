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

import jparsec.time.*;

/**
 * Implements the Ecclesiastical calendar. See Calendrical Calculations for
 * reference.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Ecclesiastical implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Gets the Easter.
	 * 
	 * @param l Year.
	 * @return Easter fixed day.
	 */
	public static long orthodoxEaster(long l)
	{
		long l1 = Calendar.mod(14L + 11L * Calendar.mod(l, 19L), 30L);
		long l2 = l <= 0L ? l - 1L : l;
		long l3 = Julian.toFixed(l2, 4, 19) - l1;
		return Calendar.kDayAfter(l3, 0);
	}

	/**
	 * Gets the Easter.
	 * 
	 * @param l Year.
	 * @return Easter fixed day.
	 */
	public static long altOrthodoxEaster(long l)
	{
		long l1 = (354L * l + 30L * Calendar.quotient(7L * l - 8L, 19D) + Calendar.quotient(l, 4D)) - Calendar
				.quotient(l, 19D) - 272L;
		return Calendar.kDayAfter(l1, 0);
	}

	/**
	 * Gets the Easter.
	 * 
	 * @param l Year.
	 * @return Easter fixed day.
	 */
	public static long easter(long l)
	{
		long l1 = 1L + Calendar.quotient(l, 100D);
		long l2 = Calendar.mod(((14L + 11L * Calendar.mod(l, 19L)) - Calendar.quotient(3L * l1, 4D)) + Calendar
				.quotient(5L + 8L * l1, 25D), 30L);
		long l3 = l2 != 0L && (l2 != 1L || Calendar.mod(l, 19L) <= 10L) ? l2 : l2 + 1L;
		long l4 = Gregorian.toFixed(l, 4, 19) - l3;
		return Calendar.kDayAfter(l4, 0);
	}

	/**
	 * Gets the date of Pentecost.
	 * 
	 * @param l Year.
	 * @return Pentecost fixed day.
	 */
	public static long pentecost(long l)
	{
		return easter(l) + 49L;
	}

	/**
	 * Gets the astronomical Easter.
	 * 
	 * @param l Year.
	 * @return Easter fixed day.
	 */
	public static long astronomicalEaster(long l)
	{
		long l1 = Gregorian.toFixed(l, 1, 1);
		double d = Calendar.solarLongitudeAfter(l1, Calendar.SPRING);
		long l2 = (long) Math.floor(Calendar.apparentFromLocal(Calendar.localFromUniversal(Calendar.lunarPhaseAfter(d,
				180.0), Calendar.JERUSALEM))); // 180 = Moon lon - Sun lon at full moon phase
		return Calendar.kDayAfter(l2, 0);
	}

	/**
	 * Test program.
	 * @param args Unused.
	 */
	public static void main (String args[])
	{
		System.out.println("Ecclesiastical test");
		
		try {
			long fixed = 2009; //Calendar.fixedFromJD(2454986.0);
			System.out.println("Easter "+TimeFormat.formatJulianDayAsDate(Calendar.jdFromFixed(Ecclesiastical.easter(fixed))));
			System.out.println("Pentecost "+TimeFormat.formatJulianDayAsDate(Calendar.jdFromFixed(Ecclesiastical.pentecost(fixed))));
		} catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
}
