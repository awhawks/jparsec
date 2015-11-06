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

/**
 * Implements the Bahai calendar.
 * <P>
 * A calendar based on the 19 year cycle 1844-1863 of the B&aacute;b, the martyred
 * forerunner of Bah&aacute;'u'l&aacute;lh and co-founder of the Bah&aacute;'&iacute; faith. Days begin at
 * sunset.
 * <P>
 * See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Bahai extends BaseCalendar
{
	private static final long serialVersionUID = 1L;

	/**
	 * Major.
	 */
	public long major;

	/**
	 * Cycle number.
	 */
	public int cycle;

	/**
	 * Calendar epoch.
	 */
	public static final long EPOCH = Gregorian.toFixed(1844L, 3, 21);

	/**
	 * Ayyam I ha.
	 */
	public static final int AYYAM_I_HA = 0;

	/**
	 * Name of the days of the week.
	 */
	public static final String DAY_OF_WEEK_NAMES[] =
	{ "Jamal", "Kamal", "Fidal", "`Idal", "Istijlal", "Istiqlal", "Jalal" };

	/**
	 * Name of the days of the month.
	 */
	public static final String DAY_OF_MONTH_NAMES[] =
	{ "Baha'", "Jalal", "Jamal", "`Azamat", "Nur", "Rahmat", "Kalimat", "Kamal", "Asma'", "`Izzat", "Mashiyyat",
			"`Ilm", "Qudrat", "Qawl", "Masa'il", "Sharaf", "Sultan", "Mulk", "`Ala'" };

	/**
	 * Name of the months.
	 */
	public static final String MONTH_NAMES[] =
	{ "Ayyam-i-Ha", "Baha'", "Jalal", "Jamal", "`Azamat", "Nur", "Rahmat", "Kalimat", "Kamal", "Asma'", "`Izzat",
			"Mashiyyat", "`Ilm", "Qudrat", "Qawl", "Masa'il", "Sharaf", "Sultan", "Mulk", "`Ala'" };

	/**
	 * Name of the years.
	 */
	public static final String YEAR_NAMES[] =
	{ "Alif", "Ba'", "Ab", "Dal", "Bab", "Vav", "Abad", "Jad", "Baha'", "Hubb", "Bahhaj", "Javab", "Ahad", "Vahhab",
			"Vidad", "Badi'", "Bahi", "Abha", "Vahid" };

	/**
	 * Constructs a Bahai date with a fixed date.
	 *
	 * @param fixedDate fixed date.
	 */
	public Bahai(final long fixedDate)
	{
		super(EPOCH, fixedDate);
	}

	/**
	 * Constructs a Bahai date with a Julian day.
	 *
	 * @param julianDay Julian day.
	 */
	public Bahai(final double julianDay)
	{
		super(EPOCH, julianDay);
	}

	/**
	 * Constructor using year, month, day.
	 *
	 * @param y Year.
	 * @param m Month.
	 * @param d Day.
	 */
	public Bahai(final int y, final int m, final int d)
	{
		super(EPOCH, y, m, d);

		yearFromFixed();
	}

	/**
	 * Constructor using major, cycle, year, month, day.
	 *
	 * @param mj Major.
	 * @param c Cycle.
	 * @param y Year.
	 * @param m Month.
	 * @param d Day.
	 */
	public Bahai(final long mj, final int c, final int y, final int m, final int d)
	{
		this((int) (((mj - 1) * 19) + c - 1) * 19 + y, m, d);
	}

	@Override
	long toYear(final long year) {
		return 1 + ((year - 1) % 19);
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
		long y = yr - 1L + Gregorian.yearFromFixed(EPOCH);
		long monthOffset;

		if (month == 19) {
			monthOffset = Gregorian.isLeapYear(y + 1L) ? 347 : 346;
		}
		else {
			monthOffset = 19 * (month - 1);
		}

		return Gregorian.toFixed(y, 3, 20) + monthOffset + (long) day - 1L;
	}

	@Override
	long yearFromFixed() {
		long f = Gregorian.yearFromFixed(fixed);
		long e = Gregorian.yearFromFixed(EPOCH);

		long y = f - e + (this.fixed >= Gregorian.toFixed(f, 3, 20) ? 1 : 0);

		this.major = 1L + y / 361;
		this.cycle = 1 + (int) ((y - 1) % 361L) / 19;

		return 1 + ((y - 1) % 19);
	}

	@Override
	int monthFromFixed(final long year) {
		long y = (((this.major - 1) * 19) + this.cycle - 1) * 19 + this.year;

		if (this.fixed >= toFixed(y, 19, 1)) {
			return 19;
		}
		else {
			int daysInYear = (int) (this.fixed - toFixed(y, 1, 1)) + 1;
			return 1 + (daysInYear / 19);
		}
	}

	@Override
	int dayFromFixed(final long year, final int month) {
		long y = (((this.major - 1) * 19) + this.cycle - 1) * 19 + this.year;
		return 1 + (int) ((fixed - toFixed(y, month, 1)) % 19);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName())
				.append(' ')
				.append(major)
				.append('/')
				.append(cycle)
				.append('/')
				.append(year)
				.append('/')
				.append(month)
				.append('/')
				.append(day);

		return builder.toString();
	}
}
