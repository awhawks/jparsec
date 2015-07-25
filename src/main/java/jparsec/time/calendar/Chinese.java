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
import jparsec.util.JPARSECException;

/**
 * Implements the Chinese calendar.
 * <P>
 * A lunisolar calendar based on astronomical events. Days begin at civil
 * midnight. Months begin and end with the new Moon. There are about 15 versions
 * of the Chinese calendar, the one implemented here is the latest, being used
 * since Gregorian year 1645 (Qing dinasty).
 * <P>
 * See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Chinese implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Cycle.
	 */
	public long cycle = 0;

	/**
	 * Year.
	 */
	public int year = 0;

	/**
	 * Month.
	 */
	public int month = 0;

	/**
	 * Leap month flag.
	 */
	public boolean leapMonth = false;

	/**
	 * Day.
	 */
	public int day = 0;

	/**
	 * Calendar epoch.
	 */
	public static final long EPOCH = Gregorian.toFixed(-2636L, 2, 15);

	/**
	 * Calendar day number at epoch.
	 */
	public static final int DAY_NAME_EPOCH = 15;

	/**
	 * Calendar month number at epoch.
	 */
	public static final int MONTH_NAME_EPOCH = 3;

	/**
	 * Stem year names.
	 */
	public static final String YEAR_STEM_NAMES[] = { "Jia", "Yi", "Bing", "Ding", "Wu", "Ji", "Geng", "Xin", "Ren", "Gui" };

	/**
	 * Branch year names.
	 */
	public static final String YEAR_BRANCH_NAMES[] = { "Zi", "Chou", "Yin", "Mao", "Chen", "Si", "Wu", "Wei", "Shen", "You", "Xu", "Hai" };

	/**
	 * Empty constructor.
	 */
	public Chinese() { }

	/**
	 * Constructor from a fixed epoch.
	 *
	 * @param l Fixed day.
	 */
	public Chinese(long l)
	{
		fromFixed(l);
	}

	/**
	 * Constructor from a Julian day.
	 *
	 * @param jd Julian day.
	 */
	public Chinese(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Full constructor.
	 *
	 * @param ccycle Cycle.
	 * @param cyear Year.
	 * @param cmonth Month.
	 * @param cleapMonth Leap month flag.
	 * @param cday Day.
	 */
	public Chinese(long ccycle, int cyear, int cmonth, boolean cleapMonth, int cday)
	{
		cycle = ccycle;
		year = cyear;
		month = cmonth;
		leapMonth = cleapMonth;
		day = cday;
	}

	/**
	 * To fixed day.
	 *
	 * @param cycle Cycle.
	 * @param year Year.
	 * @param month Month.
	 * @param leapMonth Leap month flag.
	 * @param day Day.
	 * @return Fixed day.
	 */
	public static long toFixed(long cycle, int year, int month, boolean leapMonth, int day)
	{
		long l1 = (long) Math
				.floor((double) EPOCH + ((double) ((cycle - 1L) * 60L + (long) (year - 1)) + 0.5D) * 365.242189D);
		long l2 = newYearOnOrBefore(l1);
		long l3 = newMoonOnOrAfter(l2 + (long) (29 * (month - 1)));
		Chinese chinese = new Chinese(l3);
		long l4 = month != chinese.month || leapMonth != chinese.leapMonth ? newMoonOnOrAfter(l3 + 1L) : l3;
		return (l4 + (long) day) - 1L;
	}

	/**
	 * To fixed day.
	 *
	 * @return Fixed day.
	 */
	public long toFixed()
	{
		return toFixed(cycle, year, month, leapMonth, day);
	}

	/**
	 * Sets the date from a fixed day.
	 *
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		long l1 = winterSolsticeOnOrBefore(l);
		long l2 = winterSolsticeOnOrBefore(l1 + 370L);
		long l3 = newMoonOnOrAfter(l1 + 1L);
		long l4 = newMoonBefore(l2 + 1L);
		long l5 = newMoonBefore(l + 1L);
		boolean flag = Math.round((double) (l4 - l3) / 29.530588853000001D) == 12L;
		month = (int) Calendar.adjustedMod(
				Math.round((double) (l5 - l3) / 29.530588853000001D) - (long) (!flag || !hasPriorLeapMonth(l3, l5) ? 0
						: 1), 12L);
		leapMonth = flag && hasNoMajorSolarTerm(l5) && !hasPriorLeapMonth(l3, newMoonBefore(l5));
		long l6 = (long) Math.floor((1.5D - (double) month / 12D) + (double) (l - EPOCH) / 365.242189D);
		cycle = Calendar.quotient(l6 - 1L, 60D) + 1L;
		year = (int) Calendar.adjustedMod(l6, 60L);
		day = (int) ((l - l5) + 1L);
	}

	/**
	 * Gets the time when the solar longitude is equal to some value.
	 *
	 * @param l Fixed day.
	 * @param d Desired longitude of the Sun to search for.
	 * @return Time when the Sun has this longitude in UT.
	 */
	private static double solarLongitudeOnOrAfter(long l, double d)
	{
		CityElement location = beijing(l);
		double d1 = Calendar.solarLongitudeAfter(Calendar.universalFromStandard(l, location), d);
		return Calendar.standardFromUniversal(d1, location);
	}

	private static double midnightInChina(long l)
	{
		return Calendar.universalFromStandard(l, beijing(l));
	}

	private static long winterSolsticeOnOrBefore(long l)
	{
		double d = Calendar.estimatePriorSolarLongitude(midnightInChina(l + 1L), Calendar.WINTER);
		long l1;
		for (l1 = (long) (Math.floor(d) - 1.0D); Calendar.WINTER > Calendar.solarLongitude(midnightInChina(l1 + 1L)); l1++)
			;
		return l1;
	}

	private static long newYearInSui(long l)
	{
		long l1 = winterSolsticeOnOrBefore(l);
		long l2 = winterSolsticeOnOrBefore(l1 + 370L);
		long l3 = newMoonOnOrAfter(l1 + 1L);
		long l4 = newMoonOnOrAfter(l3 + 1L);
		long l5 = newMoonBefore(l2 + 1L);
		if (Math.round((double) (l5 - l3) / 29.530588853000001D) == 12L && (hasNoMajorSolarTerm(l3) || hasNoMajorSolarTerm(l4)))
			return newMoonOnOrAfter(l4 + 1L);
		else
			return l4;
	}

	private static long newYearOnOrBefore(long l)
	{
		long l1 = newYearInSui(l);
		if (l >= l1)
			return l1;
		else
			return newYearInSui(l - 180L);
	}

	private static int currentMajorSolarTerm(long l)
	{
		double d = Calendar.solarLongitude(Calendar.universalFromStandard(l, beijing(l)));
		return (int) Calendar.adjustedMod(2L + Calendar.quotient(d, 30.0), 12L);
	}

	private static double minorSolarTermOnOrAfter(long l)
	{
		double d = Calendar.mod(30D * Math
				.ceil((Calendar.solarLongitude(midnightInChina(l)) - 15.0) / 30D) + 15.0, 360.0);
		return solarLongitudeOnOrAfter(l, d);
	}

	/**
	 * Returns previous new moon.
	 *
	 * @param l Fixed day.
	 * @return New Moon.
	 */
	public static long newMoonBefore(long l)
	{
		double d = Calendar.newMoonBefore(midnightInChina(l));
		return (long) Math.floor(Calendar.standardFromUniversal(d, beijing(d)));
	}

	/**
	 * Returns next new moon.
	 *
	 * @param l Fixed day.
	 * @return New Moon.
	 */
	public static long newMoonOnOrAfter(long l)
	{
		double d = Calendar.newMoonAfter(midnightInChina(l));
		return (long) Math.floor(Calendar.standardFromUniversal(d, beijing(d)));
	}

	private static boolean hasNoMajorSolarTerm(long l)
	{
		return currentMajorSolarTerm(l) == currentMajorSolarTerm(newMoonOnOrAfter(l + 1L));
	}

	private static boolean hasPriorLeapMonth(long l, long l1)
	{
		return l1 >= l && (hasNoMajorSolarTerm(l1) || hasPriorLeapMonth(l, newMoonBefore(l1)));
	}

	private static ChineseName sexagesimalName(long l)
	{
		try
		{
			return new ChineseName((int) Calendar.adjustedMod(l, 10L), (int) Calendar.adjustedMod(l, 12L));
		} catch (Exception _ex)
		{
			return new ChineseName();
		}
	}

	/**
	 * Returns name of year.
	 *
	 * @param i Fixed day.
	 * @return Year name.
	 */
	public static ChineseName nameOfYear(int i)
	{
		return sexagesimalName(i);
	}

	/**
	 * Returns name of month.
	 *
	 * @param i year in chinese calendar.
	 * @param j month in chinese calendar.
	 * @return Month name.
	 */
	public static ChineseName nameOfMonth(int i, int j)
	{
		long l = 12L * (long) (i - 1) + (long) (j - 1);
		return sexagesimalName(l + 15L);
	}

	/**
	 * Returns name of day.
	 *
	 * @param l Fixed day.
	 * @return Day name.
	 */
	public static ChineseName nameOfDay(long l)
	{
		return sexagesimalName(l + 15L);
	}

	/**
	 * Returns Beijing's city object. Note that in 1929 the official time
	 * zone changed.
	 *
	 * @param d Fixed day.
	 * @return The adequate object.
	 */
	public static final CityElement beijing(double d)
	{
		long l = Gregorian.yearFromFixed((long) Math.floor(d));
		return new CityElement("Beijing, China", Calendar.angle(116.0, 25.0, 0.0), 39.55, l >= 1929L ? 8.0: 7.7611111111111111, 44);
	}

	/**
	 * Returns Tokyo's city object. Note that in 1888 the official
	 * location for Tokyo changed.
	 *
	 * @param d Fixed day.
	 * @return The adequate object.
	 */
	public static final CityElement tokyo(double d)
	{
		long l = Gregorian.yearFromFixed((long) Math.floor(d));
		if (l < 1888L)
			return new CityElement("Tokyo, Japan", Calendar.angle(139D, 46D, 0.0D), 35.7, 9.3177777777777777, 24);
		else
			return new CityElement("Tokyo, Japan", 135.0, 35.0, 9.0, 0);
	}

	/**
	 * Returns new year.
	 *
	 * @param l Gregorian year.
	 * @return Fixed date of the new year for that Gregorian year.
	 */
	public static long newYear(long l)
	{
		return newYearOnOrBefore(Gregorian.toFixed(l, 7, 1));
	}

	/**
	 * Returns dragon festival date.
	 *
	 * @param l Fixed day.
	 * @return Dragon festival fixed date.
	 */
	public static long dragonFestival(long l)
	{
		long l1 = (l - Gregorian.yearFromFixed(EPOCH)) + 1L;
		long l2 = Calendar.quotient(l1 - 1L, 60D) + 1L;
		int i = (int) Calendar.adjustedMod(l1, 60L);
		return toFixed(l2, i, 5, false, 5);
	}

	/**
	 * Returns quingMing.
	 *
	 * @param l Fixed day.
	 * @return QuingMing.
	 */
	public static long qingMing(long l)
	{
		return (long) Math.floor(minorSolarTermOnOrAfter(Gregorian.toFixed(l, 3, 30)));
	}

	/**
	 * Returns chinese age.
	 *
	 * @param chinese Chinese instance.
	 * @param l Fixed day.
	 * @return Age.
	 * @throws JPARSECException If chinese is not larger than l.
	 */
	public static long age(Chinese chinese, long l) throws JPARSECException
	{
		Chinese chinese1 = new Chinese(l);
		if (l >= chinese.toFixed())
			return 60L * (chinese1.cycle - chinese.cycle) + (long) (chinese1.year - chinese.year) + 1L;
		else
			throw new JPARSECException("chinese chinese date, must be lower than second parameter "+l+".");
	}

	/**
	 * Transforms a Chinese date into a Julian day.
	 * @param cycle Cycle.
	 * @param year Year.
	 * @param month Month.
	 * @param leapMonth Leap month flag.
	 * @param day Day.
	 * @return Julian day.
	 */
	public static int toJulianDay(long cycle, int year, int month, boolean leapMonth, int day)
	{
		return (int) (toFixed(cycle, year, month, leapMonth, day) + Gregorian.EPOCH);
	}

	/**
	 * Transforms a Chinese date into a Julian day.
	 * @return The Julian day.
	 */
	public int toJulianDay()
	{
		return (int) (toFixed() + Gregorian.EPOCH);
	}

	/**
	 * Sets a Chinese date with a given Julian day.
	 * @param jd The Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}

	/**
	 * Returns the year number.
	 * @return The year.
	 */
	public int getYearNumber() {
		return 1 + (int) this.cycle * 60 + this.year;
	}
}
