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
 * Implements the Hindu Solar calendar. See Calendrical Calculations for
 * reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class HinduSolar implements Serializable
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
	 * Sidereal year lenght.
	 */
	public static final double SIDEREAL_YEAR = 365.2587564814815D;

	/**
	 * Creation date.
	 */
	public static final double CREATION = (double) HinduOldSolar.EPOCH - 714402296627D;

	/**
	 * Anomalistic year length.
	 */
	public static final double ANOMALISTIC_YEAR = 365.25878920258134D;

	/**
	 * Solar era.
	 */
	public static final int SOLAR_ERA = 3179;

	/**
	 * Ujjain location.
	 */
	public static final CityElement UJJAIN = new CityElement("Ujjain, India", Calendar.angle(75D, 46D, 0.0D), Calendar
			.angle(23D, 9D, 0.0D), 5.0512222222222221D, 0);

	/**
	 * Hindu locale site, currently equal to Ujjain.
	 */
	public static final CityElement HINDU_LOCALE = UJJAIN;

	private static final double RS[] =
	{ 0.92777777777777781D, 0.99722222222222223D, 1.075D, 1.075D, 0.99722222222222223D, 0.92777777777777781D };

	/**
	 * Default constructor.
	 */
	public HinduSolar()	{}

	/**
	 * Fixed day constructor.
	 *
	 * @param l Fixed day.
	 */
	public HinduSolar(long l)
	{
		fromFixed(l);
	}

	/**
	 * Julian day constructor.
	 *
	 * @param jd Julian day.
	 */
	public HinduSolar(int jd)
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
	public HinduSolar(long y, int m, int d)
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
		return (new HinduSolar(year, month, day)).toFixed();
	}

	/**
	 * To fixed day.
	 *
	 * @return Fixed day.
	 */
	public long toFixed()
	{
		long l = ((long) Math.floor(((double) (year + 3179L) + (double) (month - 1) / 12D) * 365.2587564814815D) + HinduOldSolar.EPOCH + (long) day) - 1L;
		double d = 360.0 / 365.2587564814815D;
		double d1 = (double) (month - 1) * 30.0 + (double) (day - 1) * d;
		double d2 = Calendar.mod((solarLongitude((double) l + 0.25D) - d1) + 180.0, 360.0) - 180.0;
		long l1 = l - (long) Math.ceil(d2 / d);
		long l2;
		for (l2 = l1 - 2L; !onOrBefore(this, new HinduSolar(l2)); l2++)
			;
		return l2;
	}

	/**
	 * Sets the date from the fixed day.
	 *
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		double d = sunrise(l + 1L);
		month = zodiac(d);
		year = calendarYear(d) - 3179L;
		long l1 = l - 3L - (long) Calendar.mod(Math.floor(solarLongitude(d)), 30.0);
		long l2;
		for (l2 = l1; zodiac(sunrise(1L + l2)) != month; l2++)
			;
		day = (int) ((l - l2) + 1L);
	}

	private static double hinduSineTable(int i)
	{
		double d = 3438D * Calendar.sinDegrees(((double) i * 225D) / 60D);
		double d1 = 0.215D * (double) Calendar.signum(d) * (double) Calendar.signum(Math.abs(d) - 1716D);
		return (double) Math.round(d + d1) / 3438D;
	}

	private static double hinduSine(double d)
	{
		double d1 = (d * 60D) / 225D;
		double d2 = Calendar.mod(d1, 1.0D);
		return d2 * hinduSineTable((int) Math.ceil(d1)) + (1.0D - d2) * hinduSineTable((int) Math.floor(d1));
	}

	private static double hinduArcsin(double d)
	{
		boolean flag = d < 0.0D;
		if (flag)
			d = -d;
		int i;
		for (i = 0; d > hinduSineTable(i); i++)
			;
		double d1 = hinduSineTable(i - 1);
		double d2 = 3.75D * ((double) (i - 1) + (d - d1) / (hinduSineTable(i) - d1));
		if (flag)
			d2 = -d2;
		return d2;
	}

	private static double meanPosition(double d, double d1)
	{
		return 360.0 * Calendar.mod((d - CREATION) / d1, 1.0D);
	}

	/**
	 * Returns solar true position.
	 *
	 * @param d
	 * @param d1
	 * @param d2
	 * @param d3
	 * @param d4
	 * @return Sun's true position.
	 */
	static double truePosition(double d, double d1, double d2, double d3, double d4)
	{
		double d5 = meanPosition(d, d1);
		double d6 = hinduSine(meanPosition(d, d3));
		double d7 = Math.abs(d6) * d4 * d2;
		double d8 = hinduArcsin(d6 * (d2 - d7));
		return Calendar.mod(d5 - d8, 360D);
	}

	/**
	 * Gets the solar longitude.
	 *
	 * @param d Fixed day.
	 * @return Solar longitude.
	 */
	public static double solarLongitude(double d)
	{
		return truePosition(d, 365.2587564814815D, 0.03888888888888889D, 365.25878920258134D, 0.023809523809523808D);
	}

	/**
	 * Returns zodiac sign.
	 *
	 * @param d Fixed day.
	 * @return Zodiac sign integer.
	 */
	public static int zodiac(double d)
	{
		return (int) Calendar.quotient(solarLongitude(d), 30.0) + 1;
	}

	private static boolean onOrBefore(HinduSolar hindusolar, HinduSolar hindusolar1)
	{
		return hindusolar.year < hindusolar1.year || hindusolar.year == hindusolar1.year && (hindusolar.month < hindusolar1.month || hindusolar.month == hindusolar1.month && hindusolar.day <= hindusolar1.day);
	}

	/**
	 * Gets calendar day.
	 *
	 * @param d Fixed date.
	 * @return Calendar day.
	 */
	public static long calendarYear(double d)
	{
		return Math.round((d - (double) HinduOldSolar.EPOCH) / 365.2587564814815D - solarLongitude(d) / 360.0);
	}

	private static double equationOfTime(long l)
	{
		double d = hinduSine(meanPosition(l, 365.25878920258134D));
		double d1 = ((d * 3438D) / 60D) * (Math.abs(d) / 1080D - 0.03888888888888889D);
		return (((dailyMotion(l) / 360D) * d1) / 360D) * 365.2587564814815D;
	}

	private static double ascensionalDifference(long l, CityElement location)
	{
		double d = 0.40634089586969169D * hinduSine(tropicalLongitude(l));
		double d1 = location.latitude;
		double d2 = hinduSine(90.0 + hinduArcsin(d));
		double d3 = hinduSine(d1) / hinduSine(90.0 + d1);
		double d4 = d * d3;
		return hinduArcsin(-(d4 / d2));
	}

	private static double tropicalLongitude(long l)
	{
		long l1 = (long) Math.floor(l - HinduOldSolar.EPOCH);
		double d = 27.0 - Math.abs(54.0 - Calendar.mod(
				27.0 + 108.0 * 3.8024793772721099E-007D * (double) l1, 108D));
		return Calendar.mod(solarLongitude(l) - d, 360D);
	}

	private static double risingSign(long l)
	{
		int i = (int) Calendar.mod(Calendar.quotient(tropicalLongitude(l), 30.0), 6L);
		return RS[i];
	}

	private static double dailyMotion(long l)
	{
		double d = 360.0 / 365.2587564814815;
		double d1 = meanPosition(l, 365.25878920258134D);
		double d2 = 0.03888888888888889D - Math.abs(hinduSine(d1)) / 1080D;
		int i = (int) Calendar.quotient(d1, 225.0 / 60.0);
		double d3 = hinduSineTable(i + 1) - hinduSineTable(i);
		double d4 = d3 * -15D * d2;
		return d * (d4 + 1.0D);
	}

	private static double solarSiderealDifference(long l)
	{
		return dailyMotion(l) * risingSign(l);
	}

	/**
	 * Gets the sunrise time.
	 *
	 * @param l Fixed day.
	 * @return Sunrise time.
	 */
	public static double sunrise(long l)
	{
		return (double) l + 0.25D + (UJJAIN.longitude - HINDU_LOCALE.longitude) / 360.0 + equationOfTime(l) + (0.99726968985094955 / 360.0) * (ascensionalDifference(l, HINDU_LOCALE) + 0.25D * solarSiderealDifference(l));
	}

	/**
	 * Gets the solar longitude after some time from certain instant.
	 *
	 * @param d Fixed date.
	 * @param d1 Time to add.
	 * @return Solar longitude.
	 */
	public static double solarLongitudeAfter(double d, double d1)
	{
		double d2 = 9.9999999999999995E-007D;
		double d3 = d + 1.0146076568930043D * Calendar.mod(d1 - solarLongitude(d), 360.0);
		double d4 = Math.max(d, d3 - 5D);
		double d5 = d3 + 5D;
		double d6 = d4;
		double d7 = d5;
		double d8;
		for (d8 = (d7 + d6) / 2D; d7 - d6 >= d2; d8 = (d7 + d6) / 2D)
			if (Calendar.mod(solarLongitude(d8) - d1, 360D) < 180.0)
				d7 = d8;
			else
				d6 = d8;

		return d8;
	}

	/**
	 * Gets Mesha Samkranti.
	 *
	 * @param l Fixed day.
	 * @return Such date.
	 */
	public static double meshaSamkranti(long l)
	{
		long l1 = new Gregorian(l, 1, 1).fixed;
		return solarLongitudeAfter(l1, 0.0);
	}

	/**
	 * Transforms a Hindu date into a Julian day
	 *
	 * @param year Year.
	 * @param month Month.
	 * @param leap Leap.
	 * @param day Day.
	 * @return Julian day.
	 */
	public static int toJulianDay(int year, int month, boolean leap, int day)
	{
		return (int) (toFixed(year, month, day) + Gregorian.EPOCH);
	}

	/**
	 * Transforms a Hindu date into a Julian day
	 *
	 * @return Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Hindu date with a given Julian day
	 *
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}
}
