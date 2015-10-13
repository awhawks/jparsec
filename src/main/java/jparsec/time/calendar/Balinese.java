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
 * Implements the Balinese calendar. See Calendrical Calculations for reference.
 * <P>
 * Note that it is not possible to pass from a given date to a Julian day, since
 * no year exists in this calendar.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Balinese implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Luang flag.
	 */
	public boolean luang = false;

	/**
	 * Dwiwara.
	 */
	public int dwiwara = 0;

	/**
	 * Triwara.
	 */
	public int triwara = 0;

	/**
	 * Caturwara.
	 */
	public int caturwara = 0;

	/**
	 * Pancawara.
	 */
	public int pancawara = 0;

	/**
	 * Sadwara.
	 */
	public int sadwara = 0;

	/**
	 * Saptawara.
	 */
	public int saptawara = 0;

	/**
	 * Asatawara.
	 */
	public int asatawara = 0;

	/**
	 * Sangawara.
	 */
	public int sangawara = 0;

	/**
	 * Dasawara.
	 */
	public int dasawara = 0;

	/**
	 * Epoch
	 */
	public static final long EPOCH = Calendar.fixedFromJD(146D);

	private static final int PANCAWARA_I[] =
	{ 5, 9, 7, 4, 8 };

	private static final int SAPTAWARA_J[] =
	{ 5, 4, 3, 7, 8, 6, 9 };

	/**
	 * Dwiwara names.
	 */
	public static final String DWIWARA_NAMES[] =
	{ "Menga", "Pepet" };

	/**
	 * Triwara names.
	 */
	public static final String TRIWARA_NAMES[] =
	{ "Pasah", "Beteng", "Kajeng" };

	/**
	 * Caturwara names.
	 */
	public static final String CATURWARA_NAMES[] =
	{ "Sri", "Laba", "Jaya", "Menala" };

	/**
	 * Pancawara names.
	 */
	public static final String PANCAWARA_NAMES[] =
	{ "Umanis", "Paing", "Pon", "Wage", "Keliwon" };

	/**
	 * Sadwara names.
	 */
	public static final String SADWARA_NAMES[] =
	{ "Tungleh", "Aryang", "Urukung", "Paniron", "Was", "Maulu" };

	/**
	 * Saptawara names.
	 */
	public static final String SAPTAWARA_NAMES[] =
	{ "Redite", "Coma", "Anggara", "Buda", "Wraspati", "Sukra", "Saniscara" };

	/**
	 * Asatawara names.
	 */
	public static final String ASATAWARA_NAMES[] =
	{ "Sri", "Indra", "Guru", "Yama", "Ludra", "Brahma", "Kala", "Uma" };

	/**
	 * Sangawara names.
	 */
	public static final String SANGAWARA_NAMES[] =
	{ "Dangu", "Jangur", "Gigis", "Nohan", "Ogan", "Erangan", "Urungan", "Tulus", "Dadi" };

	/**
	 * Dasawara names.
	 */
	public static final String DASAWARA_NAMES[] =
	{ "Pandita", "Pati", "Suka", "Duka", "Sri", "Manuh", "Manusa", "Raja", "Dewa", "Raksasa" };

	/**
	 * Week names.
	 */
	public static final String WEEK_NAMES[] =
	{ "Sinta", "Landep", "Ukir", "Kulantir", "Taulu", "Gumbreg", "Wariga", "Warigadian", "Jukungwangi", "Sungsang",
			"Dunggulan", "Kuningan", "Langkir", "Medangsia", "Pujut", "Pahang", "Krulut", "Merakih", "Tambir",
			"Medangkungan", "Matal", "Uye", "Menail", "Parangbakat", "Bala", "Ugu", "Wayang", "Kelawu", "Dukut",
			"Watugunung" };

	/**
	 * Empty constructor.
	 */
	public Balinese() {}

	/**
	 * Constructor from a Julian day.
	 *
	 * @param jd Julian day.
	 */
	public Balinese(int jd)
	{
		fromJulianDay(jd);
	}

	/**
	 * Constructor using all fields.
	 *
	 * @param bluang Luang.
	 * @param bdwiwara Dwiwara.
	 * @param btriwara Triwara.
	 * @param bcaturwara Caturwara.
	 * @param bpancawara Pancawara.
	 * @param bsadwara Sadwara.
	 * @param bsaptawara Saptawara.
	 * @param basatawara Asatawara.
	 * @param bsangawara Sangawara.
	 * @param bdasawara Dasawara.
	 */
	public Balinese(boolean bluang, int bdwiwara, int btriwara, int bcaturwara, int bpancawara, int bsadwara,
			int bsaptawara,	int basatawara, int bsangawara, int bdasawara)
	{
		luang = bluang;
		dwiwara = bdwiwara;
		triwara = btriwara;
		caturwara = bcaturwara;
		pancawara = bpancawara;
		sadwara = bsadwara;
		saptawara = bsaptawara;
		asatawara = basatawara;
		sangawara = bsangawara;
		dasawara = bdasawara;
	}

	/**
	 * Gets the Balinese calendar fields from the fixed day.
	 *
	 * @param l Fixed day.
	 */
	public void fromFixed(long l)
	{
		luang = luangFromFixed(l);
		dwiwara = dwiwaraFromFixed(l);
		triwara = triwaraFromFixed(l);
		caturwara = caturwaraFromFixed(l);
		pancawara = pancawaraFromFixed(l);
		sadwara = sadwaraFromFixed(l);
		saptawara = saptawaraFromFixed(l);
		asatawara = asatawaraFromFixed(l);
		sangawara = sangawaraFromFixed(l);
		dasawara = dasawaraFromFixed(l);
	}

	/**
	 * Gets the day from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Day.
	 */
	public static int dayFromFixed(long l)
	{
		return (int) Calendar.mod(l - EPOCH, 210L);
	}

	/**
	 * Gets the luang from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Luang.
	 */
	public static boolean luangFromFixed(long l)
	{
		return Calendar.mod(dasawaraFromFixed(l), 2) == 0;
	}

	/**
	 * Gets the dwiwara from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Dwiwara.
	 */
	public static int dwiwaraFromFixed(long l)
	{
		return Calendar.mod(dasawaraFromFixed(l) + 1, 2) + 1;
	}

	/**
	 * Gets the triwara from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Triwara.
	 */
	public static int triwaraFromFixed(long l)
	{
		return Calendar.mod(dayFromFixed(l), 3) + 1;
	}

	/**
	 * Gets the caturwara from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Caturwara.
	 */
	public static int caturwaraFromFixed(long l)
	{
		return Calendar.adjustedMod(asatawaraFromFixed(l), 4);
	}

	/**
	 * Gets the pancawara from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Pancawara.
	 */
	public static int pancawaraFromFixed(long l)
	{
		return Calendar.mod(dayFromFixed(l) + 1, 5) + 1;
	}

	/**
	 * Gets the sadwara from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Sadwara.
	 */
	public static int sadwaraFromFixed(long l)
	{
		return Calendar.mod(dayFromFixed(l), 6) + 1;
	}

	/**
	 * Gets the saptawara from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Saptawara.
	 */
	public static int saptawaraFromFixed(long l)
	{
		return Calendar.mod(dayFromFixed(l), 7) + 1;
	}

	/**
	 * Gets the asatawara from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Asatawara.
	 */
	public static int asatawaraFromFixed(long l)
	{
		int i = dayFromFixed(l);
		return Calendar.mod(Math.max(6, 4 + Calendar.mod(i - 70, 210)), 8) + 1;
	}

	/**
	 * Gets the sangawara from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Sangawara.
	 */
	public static int sangawaraFromFixed(long l)
	{
		return Calendar.mod(Math.max(0, dayFromFixed(l) - 3), 9) + 1;
	}

	/**
	 * Gets the dasawara from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Dasawara.
	 */
	public static int dasawaraFromFixed(long l)
	{
		int i = pancawaraFromFixed(l);
		int j = saptawaraFromFixed(l);
		return Calendar.mod(PANCAWARA_I[i - 1] + SAPTAWARA_J[j - 1] + 1, 10);
	}

	/**
	 * Gets the week from the fixed date.
	 *
	 * @param l Fixed date.
	 * @return Week.
	 */
	public static int weekFromFixed(long l)
	{
		return (int) Calendar.quotient(dayFromFixed(l), 7D) + 1;
	}

	/**
	 * Gets the interval between two dates.
	 *
	 * @param balinese Balinese instance.
	 * @param l Fixed date.
	 * @return Interval in days.
	 */
	public static long onOrBefore(Balinese balinese, long l)
	{
		int i = balinese.pancawara - 1;
		int j = balinese.sadwara - 1;
		int k = balinese.saptawara - 1;
		int i1 = Calendar.mod(i + 14 + 15 * (k - i), 35);
		int j1 = j + 36 * (i1 - j);
		int k1 = dayFromFixed(0L);
		return l - Calendar.mod((l + (long) k1) - (long) j1, 210L);
	}

	/**
	 * Gets the day from a Balinese date.
	 *
	 * @return Day.
	 */
	public int day()
	{
		return (int) ((onOrBefore(this, EPOCH + 209L) - EPOCH) + 1L);
	}

	/**
	 * Gets the week from a Balinese date.
	 *
	 * @return Week.
	 */
	public int week()
	{
		return (int) (Calendar.quotient(day() - 1, 7D) + 1L);
	}

	/**
	 * Sets a Balinese date with a given Julian day.
	 *
	 * @param jd Julian day.
	 */
	public void fromJulianDay(int jd)
	{
		fromFixed(jd - Gregorian.EPOCH);
	}
}
