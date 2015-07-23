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

import jparsec.time.AstroDate;
import jparsec.util.JPARSECException;

/**
 * Implements the Egyptian calendar. See Calendrical Calculations for reference.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Egyptian implements Serializable
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
	public static final long EPOCH = Calendar.fixedFromJD(1448638D);

	/**
	 * Month names.
	 */
	public static final String MONTH_NAMES[] =
	{ "Thoth", "Phaophi", "Athyr", "Choiak", "Tybi", "Mechir", "Phamenoth", "Pharmuthi", "Pachon", "Payni", "Epiphi",
			"Mesori", "Epagomenai" };

	/**
	 * Default constructor.
	 */
	public Egyptian() {}

	/**
	 * Julian day constructor.
	 * 
	 * @param jd Julian day.
	 */
	public Egyptian(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Constructor with the date.
	 * 
	 * @param y Year.
	 * @param m Month.
	 * @param d Day.
	 */
	public Egyptian(long y, int m, int d)
	{
		year = y;
		month = m;
		day = d;
	}

	/**
	 * To fixed day..
	 * 
	 * @param year Year.
	 * @param month Month.
	 * @param day Day.
	 * @return Fixed day.
	 */
	public static long toFixed(long year, int month, int day)
	{
		return (EPOCH + 365L * (year - 1L) + (long) (30 * (month - 1)) + (long) day) - 1L;
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
		long l1 = l - EPOCH;
		year = 1L + Calendar.quotient(l1, 365D);
		month = (int) (1L + Calendar.quotient(Calendar.mod(l1, 365L), 30D));
		day = (int) ((l1 - 365L * (year - 1L) - (long) (30 * (month - 1))) + 1L);
	}

	/**
	 * Transforms a Egyptian date into a Julian day
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
	 * Transforms a Egyptian date into a Julian day.
	 * @return The Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Egyptian date with a given Julian day.
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
		System.out.println("Egyptian Test");

		try {
			int jd = (int) (new AstroDate(2010, 1, 12)).jd();
			Egyptian h = new Egyptian(jd);
			System.out.println("JD " + jd + " = " + h.year + "/" + h.month + "/" + h.day);
	
			Egyptian h2 = new Egyptian(h.year, h.month, h.day);
			System.out.println("JD " + h2.toJulianDay() + " = " + h2.year + "/" + h2.month + "/" + h2.day);
	
			System.out.println(Calendar.nameFromMonth(h2.month, Egyptian.MONTH_NAMES));
			//System.out.println(CalendarGenericConversion.getDayOfWeekName(jd, CalendarGenericConversion.EGYPTIAN));
		} catch (JPARSECException e1) {
			e1.printStackTrace();
		}
	}
}
