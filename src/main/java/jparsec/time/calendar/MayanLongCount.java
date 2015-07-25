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
 * Implements the Mayan Long Count calendar.
 * <P>
 * A strictly counting days calendar, based on a cycle of 2 880 000 days.
 *
 * See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class MayanLongCount implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * 144 000 days.
	 */
	public long baktun;

	/**
	 * 7200 days.
	 */
	public int katun;

	/**
	 * 360 days.
	 */
	public int tun;

	/**
	 * 20 days.
	 */
	public int uinal;

	/**
	 * 1 day.
	 */
	public int kin;

	/**
	 * Calendar epoch.
	 */
	public static final long EPOCH = Calendar.fixedFromJD(584283D);

	/**
	 * Default constructor.
	 */
	public MayanLongCount() { }

	/**
	 * Julian day constructor.
	 *
	 * @param jd Julian day.
	 */
	public MayanLongCount(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Explicit constructor.
	 *
	 * @param mbaktun Baktun.
	 * @param mkatun Katun.
	 * @param mtun Tun.
	 * @param muinal Uinal.
	 * @param mkin Kin.
	 */
	public MayanLongCount(long mbaktun, int mkatun, int mtun, int muinal, int mkin)
	{
		baktun = mbaktun;
		katun = mkatun;
		tun = mtun;
		uinal = muinal;
		kin = mkin;
	}

	/**
	 * To fixed day.
	 *
	 * @param baktun Baktun.
	 * @param katun Katun.
	 * @param tun Tun.
	 * @param uinal Uinal.
	 * @param kin Kin.
	 * @return Fixed day.
	 */
	public static long toFixed(long baktun, int katun, int tun, int uinal, int kin)
	{
		return EPOCH + baktun * 0x23280L + (long) (katun * 7200) + (long) (tun * 360) + (long) (uinal * 20) + (long) kin;
	}

	/**
	 * To fixed day.
	 *
	 * @return Fixed day.
	 */
	public long toFixed()
	{
		return toFixed(baktun, katun, tun, uinal, kin);
	}

	/**
	 * Sets the date from fixed epoch.
	 *
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		long l1 = l - EPOCH;
		baktun = Calendar.quotient(l1, 144000D);
		int i = (int) Calendar.mod(l1, 0x23280L);
		katun = (int) Calendar.quotient(i, 7200D);
		int j = Calendar.mod(i, 7200);
		tun = (int) Calendar.quotient(j, 360D);
		int k = Calendar.mod(j, 360);
		uinal = (int) Calendar.quotient(k, 20D);
		kin = Calendar.mod(k, 20);
	}

	/**
	 * Transforms a Mayan date into a Julian day
	 *
	 * @param baktun Baktun.
	 * @param katun Katun.
	 * @param tun Tun.
	 * @param uinal Uinal.
	 * @param kin Kin.
	 * @return Julian day.
	 */
	public static int toJulianDay(long baktun, int katun, int tun, int uinal, int kin)
	{
		return (int) (toFixed(baktun, katun, tun, uinal, kin) + Gregorian.EPOCH);
	}

	/**
	 * Transforms a Mayan date into a Julian day
	 *
	 * @return Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
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
