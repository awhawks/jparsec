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
	/**
	 * Number.
	 */
	public final int number;

	/**
	 * Name.
	 */
	public final int name;

	/**
	 * Calendar epoch.
	 */
	public static final long EPOCH = -1137301; // MayanLongCount.EPOCH - ordinal(new MayanTzolkin(4, 20));

	/**
	 * Month names.
	 */
	public static final String NAMES[] = {
		"Imix", "Ik", "Akbal", "Kan", "Chicchan", "Cimi", "Manik", "Lamat", "Muluc", "Oc", "Chuen", "Eb", "Ben", "Ix",
		"Men", "Cib", "Caban", "Etznab", "Cauac", "Ahau"
	};

	private static final long serialVersionUID = 8836908879580577439L;

	/**
	 * Fixed day constructor.
	 *
	 * @param fixed fixed day.
	 */
	public MayanTzolkin(final long fixed) {
		long l1 = (fixed - EPOCH) + 1;
		this.number = (int) Calendar.adjustedMod(l1, 13);
		this.name = (int) Calendar.adjustedMod(l1, 20);
	}

	/**
	 * Julian day constructor.
	 *
	 * @param julianDay Julian day.
	 */
	public MayanTzolkin(final double julianDay) {
		this((long) julianDay - Gregorian.EPOCH);
	}

	/**
	 * Explicit constructor.
	 *
	 * @param mnumber Number.
	 * @param mname Name.
	 */
	public MayanTzolkin(final int mnumber, final int mname) {
		this.number = mnumber;
		this.name = mname;
	}

	/**
	 * Gets the ordinal.
	 *
	 * @param mayantzolkin MayanTzolkin instance.
	 * @return Days elapsed since new year.
	 */
//	private static int ordinal(final MayanTzolkin mayantzolkin) {
//		return ((mayantzolkin.number - 1) + 39 * (mayantzolkin.number - mayantzolkin.name)) % 260;
//	}

	/**
	 * Gets last new year.
	 *
	 * @param mayantzolkin MayanTzolkin instance.
	 * @param fixed Fixed day.
	 * @return Such date.
	 */
//	private static long onOrBefore(final MayanTzolkin mayantzolkin, final long fixed) {
//		return fixed - (fixed - EPOCH - ordinal(mayantzolkin)) % 260;
//	}
}
