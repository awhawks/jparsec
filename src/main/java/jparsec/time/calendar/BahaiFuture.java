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

import jparsec.observer.CityElement;

/**
 * Implements the Future Bahai calendar.
 * See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class BahaiFuture extends Bahai
{
	private static final long serialVersionUID = 1L;

	/**
	 * Haifa location.
	 */
	//public static final CityElement HAIFA = new CityElement("Haifa, Israel", 35D, 32.82, 2D, 0);

	/**
	 * Tehran location.
	 */
	public static final CityElement TEHRAN = new CityElement("Tehran, Iran", 51.43, 35.67, 3.5, 1353);

	/**
	 * Fixed date constructor.
	 *
	 * @param fixed fixed date.
	 */
	public BahaiFuture(final long fixed)
	{
		super(fixed);
	}

	/**
	 * Julian day constructor.
	 *
	 * @param julianDay Julian day.
	 */
	public BahaiFuture(final double julianDay)
	{
		super(julianDay);
	}

	/**
	 * Explicit constructor.
	 *
	 * @param major Major.
	 * @param cycle Cycle.
	 * @param year Year.
	 * @param month Month.
	 * @param day Day.
	 */
	public BahaiFuture(long major, int cycle, int year, int month, int day)
	{
		super (major, cycle, year, month, day);
	}

	/**
	 * To fixed day..
	 *
	 * @param yr Year.
	 * @param month Month.
	 * @param day Day.
	 * @return Fixed day.
	 */
	@Override
	long toFixed(final long yr, final int month, final int day) {
		long s0;
		double s1;

		if (month == 19) {
			s0 = -21L;
			s1 = (double) yr + 0.5D;
		}
		else {
			s0 = (month - 1) * 19L - 2L;
			s1 = (double) yr - 0.5D;
		}

		return newYearOnOrBefore(Bahai.EPOCH + (long) Math.floor(365.242189D * s1)) + s0 + (long) day;
	}

	/**
	 * Sunset in Haifa.
	 *
	 * @param fixed Fixed day.
	 * @return Sunset time.
	 */
	private double sunsetInTehran(final long fixed)
	{
		try {
			return Calendar.universalFromStandard(Calendar.sunset(fixed, TEHRAN), TEHRAN);
		} catch (Exception ex) {
			return 0.0D;
		}
	}

	/**
	 * New year before certain date.
	 *
	 * @param fixed Fixed day.
	 * @return Previous new year.
	 */
	private long newYearOnOrBefore(long fixed)
	{
		double sunsetTime = Calendar.estimatePriorSolarLongitude(sunsetInTehran(fixed), Calendar.SPRING);
		long l1 = (long) Math.floor(sunsetTime) - 1L;

		while (Calendar.solarLongitude(sunsetInTehran(l1)) > Calendar.SPRING + 2.0) {
			l1++;
		}

		return l1;
	}
}
