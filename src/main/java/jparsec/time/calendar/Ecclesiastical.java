/*
 * This file is part of JPARSEC library.
 *
 * (C) Copyright 2006-2017 by T. Alonso Albi - OAN (Spain).
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
 * Implements the Ecclesiastical calendar. See Calendrical Calculations for
 * reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Ecclesiastical implements Serializable
{
	private static final long serialVersionUID = 1L;

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
		long l3 = new Julian(l2, 4, 19).fixed - l1;
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
	 * @deprecated This algorithm gives a wrong value for 2018, please use 
	 * the alternative one.
	 */
	public static long easterFromCalendricalCalculations(long l)
	{
		long l1 = 1L + Calendar.quotient(l, 100D);
		long l2 = Calendar.mod(((14L + 11L * Calendar.mod(l, 19L)) - Calendar.quotient(3L * l1, 4D)) + 
				Calendar.quotient(5L + 8L * l1, 25D), 30L);
		long l3 = l2 + 1L;
		if (l2 == 0L || (l2 == 1L && Calendar.mod(l, 19L) > 10L)) l3 = l2;
		long l4 = new Gregorian(l, 4, 19).fixed - l3;
		return Calendar.kDayAfter(l4, 0);
	}
	
	/**
	 * Gets the Easter from an alternative method not based on Calendrical Calculations.
	 *
	 * @param l y.
	 * @return Easter fixed day.
	 */
	public static long easter(long y) {
		int year = (int) y;
    	int centuria = (int) (year / 100) + 1;
    	int bb = (int) (year - (int) (year / 19) * 19);
    	int epacta = 14 + 11 * bb - (int) (3 * centuria / 4) + (int) ((5 + 8 * centuria) / 25);
    	epacta = epacta - (int) (epacta / 30) * 30;
    	if (epacta == 0 || epacta == 1 && bb > 10) epacta ++;
    	int pascal = FNFG(year, 4, 19) - epacta;
    	int diasem = pascal - (int) (pascal / 7) * 7;
    	if (diasem < 1) diasem += 7;
    	int delta = 7 - diasem;
    	if (delta == 0) delta = 7;
    	double jd = delta + pascal + 1721424.5;
    	return (new Gregorian(jd)).fixed;
	}
	private static int FNFG(int A, int b, int C) {
		int D = 1 - 1 + 365 * (A - 1) + (int) ((A - 1) / 4) - (int) ((A - 1) / 100) + (int) ((A - 1) / 400) + (int) ((367 * b - 362) / 12) + C;
		int dd = 0;
		if (b > 2) dd = -2;
		if (b > 2 && FNBIS(A) == 1) dd = -1;
		return dd + D;
	}
	private static int FNBIS(int year) {
		int p = year - (int) (year / 4) * 4;
		int pp = year - (int) (year / 400) * 400;
		int Z = 0;
		if (p == 0 && pp != 100 && pp != 200 && pp != 300) Z = 1;
		return Z;
	}

	/**
	 * Gets the date of Pentecost. This function uses the fixed easter method.
	 *
	 * @param l Year.
	 * @return Pentecost fixed day.
	 */
	public static long pentecost(long l)
	{
		return easter((int) l) + 49;
	}
	
	/**
	 * Gets the astronomical Easter.
	 *
	 * @param l Year.
	 * @return Easter fixed day.
	 */
	public static long astronomicalEaster(long l)
	{
		long l1 = new Gregorian(l, 1, 1).fixed;
		double d = Calendar.solarLongitudeAfter(l1, Calendar.SPRING);
		long l2 = (long) Math.floor(Calendar.apparentFromLocal(Calendar.localFromUniversal(Calendar.lunarPhaseAfter(d,
				180.0), Calendar.JERUSALEM))); // 180 = Moon lon - Sun lon at full moon phase
		return Calendar.kDayAfter(l2, 0);
	}
}
