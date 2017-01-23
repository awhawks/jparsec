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
package jparsec.time;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target.TARGET;
import jparsec.ephem.planets.imcce.Elp2000;
import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.io.ReadFile;
import jparsec.math.Constant;
import jparsec.math.Interpolation;
import jparsec.observer.EarthOrientationParameters;
import jparsec.observer.ObserverElement;
import jparsec.observer.ObserverElement.DST_RULE;
import jparsec.time.TimeElement.SCALE;
import jparsec.util.Configuration;
import jparsec.util.DataBase;
import jparsec.util.JPARSECException;

/**
 * Performs dynamical time calculations. Partially based on SOFA software
 * library. This class will be always provisional, as it should be
 * updated once a year and whenever a new leap second is introduced. To do that
 * it is necessary to edit the files leapSeconds.txt and TTminusUT1.txt in this
 * package, when necessary.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class TimeScale
{
	// private constructor so that this class cannot be instantiated.
	private TimeScale() {}

	/**
	 * Difference between TT and TAI in seconds, fixed value.
	 */
	public static final double TT_MINUS_TAI = 32.184;

	/** The first year when daylight saving time is considered. Currently set to 1970,
	 * when most countries already applied the rule. Much countries started using DST before. */
	public static final int DST_START_YEAR = 1970;

	/**
	 * Obtain Julian day number from a Time object in certain time scale.
	 * <P>
	 * This methods takes the date from the Time object and transforms
	 * the input time scale into the desired output time scale.
	 * <P>
	 * At epoch J2000.0, the 17 decimal precision of IEEE double precision
	 * numbers limits time resolution measured by Julian date to approximately
	 * 10 microseconds (1E-10 days). Uncertainty when transforming between different time
	 * scales could be as large as 50 microseconds, depending on the input
	 * and output time scales.
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @param type Desired output.
	 * @return Julian day.
	 * @throws JPARSECException If the date is invalid.
	 */
	public static double getJD(TimeElement time, ObserverElement obs, EphemerisElement eph, SCALE type)
			throws JPARSECException
	{
		double JD = time.astroDate.jd();

		if (!eph.correctForEOP) {
			if (time.timeScale == type) return JD;
			if (time.timeScale == SCALE.UNIVERSAL_TIME_UT1 && type == SCALE.UNIVERSAL_TIME_UTC) return JD;
			if (time.timeScale == SCALE.UNIVERSAL_TIME_UTC && type == SCALE.UNIVERSAL_TIME_UT1) return JD;
		}

		// Compute time scale transform values
		double TT2TDB = 0.0;
		double LT2UTC = -obs.getTimeZone() / Constant.HOURS_PER_DAY;
		double TTminusUT1 = 0.0;
		boolean computeTTmUT1 = false;
		if (time.timeScale == TimeElement.SCALE.TERRESTRIAL_TIME) computeTTmUT1 = true;
		if (time.timeScale == TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME && type != SCALE.TERRESTRIAL_TIME) computeTTmUT1 = true;
		if (time.timeScale == TimeElement.SCALE.LOCAL_TIME || time.timeScale == SCALE.UNIVERSAL_TIME_UT1
				|| time.timeScale == SCALE.UNIVERSAL_TIME_UTC) {
			if (type == SCALE.BARYCENTRIC_DYNAMICAL_TIME || type == SCALE.TERRESTRIAL_TIME)
				computeTTmUT1 = true;
		}

		if (computeTTmUT1) TTminusUT1 = TimeScale.getTTminusUT1(time, obs);
		double UT12TT = TTminusUT1 / Constant.SECONDS_PER_DAY;
		if (type == SCALE.BARYCENTRIC_DYNAMICAL_TIME)
		{
			TT2TDB = TimeScale.getTDBminusTT(time, obs, eph) / Constant.SECONDS_PER_DAY;
		} else {
			if (time.timeScale == TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME)
			{
				TT2TDB = TimeScale.getTDBminusTT(new TimeElement(time.astroDate, TimeElement.SCALE.TERRESTRIAL_TIME), obs, eph) / Constant.SECONDS_PER_DAY;
			}
		}
		double JD_UT = JD;
		if (time.timeScale == TimeElement.SCALE.LOCAL_TIME)
			JD_UT += LT2UTC;
		if (time.timeScale == TimeElement.SCALE.TERRESTRIAL_TIME || time.timeScale == TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME)
			JD_UT -= UT12TT;

		double DST = -(double) getDST(JD_UT, obs) / Constant.HOURS_PER_DAY;
		LT2UTC += DST;
		if (DST != 0 && time.timeScale == TimeElement.SCALE.LOCAL_TIME) {
			JD_UT += DST;
			double DST2 = -(double) getDST(JD_UT, obs) / Constant.HOURS_PER_DAY;
			if (DST2 == 0) {
				LT2UTC -= DST;
				JD_UT -= DST;
			}
		}

		double UTC2UT1 = 0.0;
		if (eph.correctForEOP && ((time.timeScale == SCALE.LOCAL_TIME && type != SCALE.UNIVERSAL_TIME_UTC) ||
				(time.timeScale == SCALE.TERRESTRIAL_TIME && type != SCALE.UNIVERSAL_TIME_UT1 && type != SCALE.BARYCENTRIC_DYNAMICAL_TIME) ||
				(time.timeScale == SCALE.BARYCENTRIC_DYNAMICAL_TIME && type != SCALE.UNIVERSAL_TIME_UT1 && type != SCALE.TERRESTRIAL_TIME) ||
				(time.timeScale == SCALE.UNIVERSAL_TIME_UT1 && type != SCALE.BARYCENTRIC_DYNAMICAL_TIME && type != SCALE.TERRESTRIAL_TIME) ||
				(time.timeScale == SCALE.UNIVERSAL_TIME_UTC && type != SCALE.LOCAL_TIME))) {
			double eop[] = EarthOrientationParameters.obtainEOP(JD_UT, eph); // UT approx to UTC
			UTC2UT1 = eop[4] / Constant.SECONDS_PER_DAY;
		}

		// Perform time scale transformation
		switch (time.timeScale)
		{
		case LOCAL_TIME:
			if (type == SCALE.TERRESTRIAL_TIME)
				JD = JD + (LT2UTC + UTC2UT1 + UT12TT);
			if (type == SCALE.UNIVERSAL_TIME_UT1)
				JD = JD + (LT2UTC + UTC2UT1);
			if (type == SCALE.UNIVERSAL_TIME_UTC)
				JD = JD + LT2UTC;
			if (type == SCALE.BARYCENTRIC_DYNAMICAL_TIME)
				JD = JD + (LT2UTC + UTC2UT1 + UT12TT) + TT2TDB;
			break;
		case TERRESTRIAL_TIME:
			if (type == SCALE.LOCAL_TIME)
				JD = JD - (LT2UTC + UTC2UT1 + UT12TT);
			if (type == SCALE.UNIVERSAL_TIME_UT1)
				JD = JD - UT12TT;
			if (type == SCALE.UNIVERSAL_TIME_UTC)
				JD = JD - (UT12TT + UTC2UT1);
			if (type == SCALE.BARYCENTRIC_DYNAMICAL_TIME)
				JD = JD + TT2TDB;
			break;
		case BARYCENTRIC_DYNAMICAL_TIME:
			if (type == SCALE.LOCAL_TIME)
				JD = JD - (TT2TDB + LT2UTC + UTC2UT1 + UT12TT);
			if (type == SCALE.UNIVERSAL_TIME_UT1)
				JD = JD - TT2TDB - UT12TT;
			if (type == SCALE.UNIVERSAL_TIME_UTC)
				JD = JD - (UT12TT + UTC2UT1) - TT2TDB;
			if (type == SCALE.TERRESTRIAL_TIME)
				JD = JD - TT2TDB;
			break;
		case UNIVERSAL_TIME_UT1:
			if (type == SCALE.TERRESTRIAL_TIME)
				JD = JD + UT12TT;
			if (type == SCALE.LOCAL_TIME)
				JD = JD - (LT2UTC + UTC2UT1);
			if (type == SCALE.BARYCENTRIC_DYNAMICAL_TIME)
				JD = JD + UT12TT + TT2TDB;
			if (type == SCALE.UNIVERSAL_TIME_UTC)
				JD = JD - UTC2UT1;
			break;
		case UNIVERSAL_TIME_UTC:
			if (type == SCALE.TERRESTRIAL_TIME)
				JD = JD + (UTC2UT1 + UT12TT);
			if (type == SCALE.UNIVERSAL_TIME_UT1)
				JD = JD + UTC2UT1;
			if (type == SCALE.BARYCENTRIC_DYNAMICAL_TIME)
				JD = JD + (UTC2UT1 + UT12TT) + TT2TDB;
			if (type == SCALE.LOCAL_TIME)
				JD = JD - LT2UTC;
			break;
		}

		return JD;
	}

	/**
	 * Obtain Julian day number from a Time object in certain time scale.
	 * <P>
	 * This methods takes the date from the Time object and transforms
	 * the input time scale into the desired output time scale.
	 * <P>
	 * The method returns a big decimal object with a very precise representation
	 * of the requested Julian day. Usual precision is around 2 fs (femptosecond)
	 * or better.
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @param type Desired output.
	 * @return Julian day.
	 * @throws JPARSECException If the date is invalid.
	 */
	public static BigDecimal getExactJD(TimeElement time, ObserverElement obs, EphemerisElement eph, SCALE type)
			throws JPARSECException
	{
		BigDecimal JD = time.astroDate.exactJD();

		if (!eph.correctForEOP) {
			if (time.timeScale == type) return JD;
			if (time.timeScale == SCALE.UNIVERSAL_TIME_UT1 && type == SCALE.UNIVERSAL_TIME_UTC) return JD;
			if (time.timeScale == SCALE.UNIVERSAL_TIME_UTC && type == SCALE.UNIVERSAL_TIME_UT1) return JD;
		}

		// Compute time scale transform values
		BigDecimal TT2TDB = new BigDecimal(0.0);
		BigDecimal LT2UTC = new BigDecimal(-obs.getTimeZone()).divide(new BigDecimal(Constant.HOURS_PER_DAY),
				Configuration.BIG_DECIMAL_PRECISION_DECIMAL_PLACES, Configuration.BIG_DECIMAL_PRECISION_ROUNDING_MODE);
		double TTminusUT1 = TimeScale.getTTminusUT1(time, obs);
		BigDecimal UT12TT = new BigDecimal(TTminusUT1).divide(new BigDecimal(Constant.SECONDS_PER_DAY)
			, Configuration.BIG_DECIMAL_PRECISION_DECIMAL_PLACES, Configuration.BIG_DECIMAL_PRECISION_ROUNDING_MODE);
		if (type == SCALE.BARYCENTRIC_DYNAMICAL_TIME)
		{
			TT2TDB = new BigDecimal(TimeScale.getTDBminusTT(time, obs, eph)).divide(new BigDecimal(Constant.SECONDS_PER_DAY),
					Configuration.BIG_DECIMAL_PRECISION_DECIMAL_PLACES, Configuration.BIG_DECIMAL_PRECISION_ROUNDING_MODE);
		} else {
			if (time.timeScale == TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME)
			{
				TT2TDB = new BigDecimal(TimeScale.getTDBminusTT(new TimeElement(time.astroDate, TimeElement.SCALE.TERRESTRIAL_TIME), obs, eph)).divide(new BigDecimal(Constant.SECONDS_PER_DAY),
						Configuration.BIG_DECIMAL_PRECISION_DECIMAL_PLACES, Configuration.BIG_DECIMAL_PRECISION_ROUNDING_MODE);
			}
		}
		BigDecimal JD_UT = JD;
		if (time.timeScale == TimeElement.SCALE.LOCAL_TIME)
			JD_UT = JD_UT.add(LT2UTC);
		if (time.timeScale == TimeElement.SCALE.TERRESTRIAL_TIME || time.timeScale == TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME)
			JD_UT = JD_UT.subtract(UT12TT); // Aprox. use of UT1 as UTC

		BigDecimal DST = new BigDecimal(-(double) getDST(JD_UT.doubleValue(), obs)).divide(new BigDecimal(Constant.HOURS_PER_DAY),
				Configuration.BIG_DECIMAL_PRECISION_DECIMAL_PLACES, Configuration.BIG_DECIMAL_PRECISION_ROUNDING_MODE);
		LT2UTC = LT2UTC.add(DST);
		if (time.timeScale == TimeElement.SCALE.LOCAL_TIME) {
			JD_UT = JD_UT.add(DST);
			double DST2 = -(double) getDST(JD_UT.doubleValue(), obs) / Constant.HOURS_PER_DAY;
			if (DST2 == 0) {
				LT2UTC = LT2UTC.subtract(DST);
				JD_UT = JD_UT.subtract(DST);
			}
		}

		BigDecimal UTC2UT1 = new BigDecimal(0.0);
		if (eph.correctForEOP && ((time.timeScale == SCALE.LOCAL_TIME && type != SCALE.UNIVERSAL_TIME_UTC) ||
				(time.timeScale == SCALE.TERRESTRIAL_TIME && type != SCALE.UNIVERSAL_TIME_UT1 && type != SCALE.BARYCENTRIC_DYNAMICAL_TIME) ||
				(time.timeScale == SCALE.BARYCENTRIC_DYNAMICAL_TIME && type != SCALE.UNIVERSAL_TIME_UT1 && type != SCALE.TERRESTRIAL_TIME) ||
				(time.timeScale == SCALE.UNIVERSAL_TIME_UT1 && type != SCALE.BARYCENTRIC_DYNAMICAL_TIME && type != SCALE.TERRESTRIAL_TIME) ||
				(time.timeScale == SCALE.UNIVERSAL_TIME_UTC && type != SCALE.LOCAL_TIME))) {
			double eop[] = EarthOrientationParameters.obtainEOP(JD_UT.doubleValue(), eph);
			UTC2UT1 = new BigDecimal(eop[4]).divide(new BigDecimal(Constant.SECONDS_PER_DAY),
					Configuration.BIG_DECIMAL_PRECISION_DECIMAL_PLACES, Configuration.BIG_DECIMAL_PRECISION_ROUNDING_MODE);
		}
		// Perform time scale transformation
		switch (time.timeScale)
		{
		case LOCAL_TIME:
			if (type == SCALE.TERRESTRIAL_TIME)
				JD = JD.add(LT2UTC.add(UTC2UT1.add(UT12TT)));
			if (type == SCALE.UNIVERSAL_TIME_UT1)
				JD = JD.add(LT2UTC.add(UTC2UT1));
			if (type == SCALE.UNIVERSAL_TIME_UTC)
				JD = JD.add(LT2UTC);
			if (type == SCALE.BARYCENTRIC_DYNAMICAL_TIME)
				JD = JD.add(LT2UTC.add(UTC2UT1.add(UT12TT.add(TT2TDB))));
			break;
		case TERRESTRIAL_TIME:
			if (type == SCALE.LOCAL_TIME)
				JD = JD.subtract(LT2UTC.add(UTC2UT1.add(UT12TT)));
			if (type == SCALE.UNIVERSAL_TIME_UT1)
				JD = JD.subtract(UT12TT);
			if (type == SCALE.UNIVERSAL_TIME_UTC)
				JD = JD.subtract(UT12TT.add(UTC2UT1));
			if (type == SCALE.BARYCENTRIC_DYNAMICAL_TIME)
				JD = JD.add(TT2TDB);
			break;
		case BARYCENTRIC_DYNAMICAL_TIME:
			if (type == SCALE.LOCAL_TIME)
				JD = JD.subtract(TT2TDB.add(LT2UTC.add(UTC2UT1.add(UT12TT))));
			if (type == SCALE.UNIVERSAL_TIME_UT1)
				JD = JD.subtract(TT2TDB.add(UT12TT));
			if (type == SCALE.UNIVERSAL_TIME_UTC)
				JD = JD.subtract(TT2TDB.add(UT12TT.add(UTC2UT1)));
			if (type == SCALE.TERRESTRIAL_TIME)
				JD = JD.subtract(TT2TDB);
			break;
		case UNIVERSAL_TIME_UT1:
			if (type == SCALE.TERRESTRIAL_TIME)
				JD = JD.add(UT12TT);
			if (type == SCALE.LOCAL_TIME)
				JD = JD.subtract(LT2UTC.add(UTC2UT1));
			if (type == SCALE.BARYCENTRIC_DYNAMICAL_TIME)
				JD = JD.add(UT12TT.add(TT2TDB));
			if (type == SCALE.UNIVERSAL_TIME_UTC)
				JD = JD.subtract(UTC2UT1);
			break;
		case UNIVERSAL_TIME_UTC:
			if (type == SCALE.TERRESTRIAL_TIME)
				JD = JD.add(UTC2UT1.add(UT12TT));
			if (type == SCALE.UNIVERSAL_TIME_UT1)
				JD = JD.add(UTC2UT1);
			if (type == SCALE.BARYCENTRIC_DYNAMICAL_TIME)
				JD = JD.add(UTC2UT1.add(UT12TT.add(TT2TDB)));
			if (type == SCALE.LOCAL_TIME)
				JD = JD.subtract(LT2UTC);
			break;
		}

		return JD;
	}

	/**
	 * Returns the date defined by the input objects in TCB (Barycentric Coordinate Time) scale.
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @return Julian day.
	 * @throws JPARSECException If the date is invalid.
	 */
	public static double getJDInTCB(TimeElement time, ObserverElement obs, EphemerisElement eph)
			throws JPARSECException
	{
		double jd = TimeScale.getJD(time, obs, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
		double TCBMinusTDB = TimeScale.getTCBminusTDB(jd);
		return jd + TCBMinusTDB / Constant.SECONDS_PER_DAY;
	}
	/**
	 * Returns the date defined by the input objects in TCG (Geocentric Coordinate Time) scale.
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @return Julian day.
	 * @throws JPARSECException If the date is invalid.
	 */
	public static double getJDInTCG(TimeElement time, ObserverElement obs, EphemerisElement eph)
			throws JPARSECException
	{
		double jd = TimeScale.getJD(time, obs, eph, SCALE.TERRESTRIAL_TIME);
		double TCGMinusTT = TimeScale.getTCGminusTT(jd);
		return jd + TCGMinusTT / Constant.SECONDS_PER_DAY;
	}
	/**
	 * Returns the date defined by the input objects in TAI (International Atomic Time) scale.
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @return Julian day.
	 * @throws JPARSECException If the date is invalid.
	 */
	public static double getJDInTAI(TimeElement time, ObserverElement obs, EphemerisElement eph)
			throws JPARSECException
	{
		double jd = TimeScale.getJD(time, obs, eph, SCALE.UNIVERSAL_TIME_UTC);
		double TAIMinusUTC = TimeScale.getTAIminusUTC(new AstroDate(jd));
		return jd + TAIMinusUTC / Constant.SECONDS_PER_DAY;
	}

	/**
	 * Correct Time Zone for the Daylight Saving Time.
	 * <P>
	 * The change is 1 hour and is set at the beginning of the last Sunday in
	 * the corresponding month. See DST method in Country for more information.
	 * The only exception to this rule is currently United States and Canada,
	 * where the change starts now on the second Monday of April, and ends on
	 * the first Sunday of November. The JPARSEC package applies this change in
	 * the same way in the whole USA (adopted in 2007), although some regions in
	 * USA have maintained the old rule. To select the correct rule it is
	 * possible to set DST information properly in the Observer object.
	 * <P>
	 * Daylight Saving Time is active since 1974. Previous dates will have 0 as
	 * result. This change was not adopted at the same time by all countries,
	 * but this behavior is established here to simplify and to adopt the same
	 * rule for every country.
	 *
	 * @param JD_UT Julian day in Universal Time.
	 * @param obs Observer object.
	 * @return 1 or 0 for Daylight Saving Time in hours.
	 * @throws JPARSECException If the method fails, for example because of an
	 *         invalid date.
	 */
	public static double getDST(double JD_UT, ObserverElement obs) throws JPARSECException
	{
		if (obs.getDSTCode() == DST_RULE.NONE)
			return 0;

		if (JD_UT < 2440587.5) return 0;

		ObserverElement DST_last_observer;
		double DST_last_jd = -1.0;
		int DST_last_value = 0;
		Object o = DataBase.getData("DST", true);
		if (o != null) {
			Object[] a = (Object[]) o;
			DST_last_observer = (ObserverElement) a[0];
			DST_last_jd = (Double) a[1];
			DST_last_value = (Integer) a[2];

			if (JD_UT == DST_last_jd && obs.equals(DST_last_observer))
				return DST_last_value;
		}

		double dst[] = getDSTStartEnd(JD_UT, obs);

		DST_last_value = 0;
		if (dst != null) {
			if (JD_UT > dst[0] && JD_UT < dst[1]) DST_last_value = 1;
		}
		DataBase.addData("DST", new Object[] {obs, JD_UT, DST_last_value}, true);

		return DST_last_value;
	}

	/**
	 * Returns the start and end times of the DST change in the year. For countries
	 * belonging to south hemisphere the values corresponding to the previous year
	 * will be returned for the start time in case the month is below the corresponding
	 * month when DST starts. The starting year to consider that daylight saving time is to be
	 * used is controlled by the constant {@linkplain #DST_START_YEAR}.
	 * <P>
	 * The change is 1 hour and is set at the beginning of the last Sunday in
	 * the corresponding month. See DST method in Country for more information.
	 * The only exception to this rule is currently United States and Canada,
	 * where the change starts now on the second Monday of April, and ends on
	 * the first Sunday of November. The JPARSEC package applies this change in
	 * the same way in the whole USA (adopted in 2007), although some regions in
	 * USA have maintained the old rule. To select the correct rule it is
	 * possible to set DST information properly in the Observer object.
	 * <P>
	 * Daylight Saving Time is active since 1974. Previous dates will have 0 as
	 * result. This change was not adopted at the same time by all countries,
	 * but this behavior is established here to simplify and to adopt the same
	 * rule for every country.
	 *
	 * @param JD_UT The Julian day in UT to return DST start/end times. For north
	 * hemisphere only the year is considered, for south also the month.
	 * @param obs Observer object.
	 * @return Start and end times of DST in UT scale, or null for dates when DST = 0.
	 * Returned times have an offset of 1 hr respect to 0h UT since, at least in Spain,
	 * time change is done at 1 UT.
	 * @throws JPARSECException If the method fails, for example because of an
	 *         invalid date.
	 */
	public static double[] getDSTStartEnd(double JD_UT, ObserverElement obs) throws JPARSECException
	{
		int year = (new AstroDate(JD_UT)).getYear();
		if (year >= DST_START_YEAR)
		{
			double JD_start = 0.0, JD_end = 0.0;

			if (obs.getDSTCode() == DST_RULE.N1)
			{
				JD_start = DateTimeOps.getLastSundayOfMarch(year);
				JD_end = DateTimeOps.getLastSundayOfOctober(year);
			}
			if (obs.getDSTCode() == DST_RULE.N2)
			{
				JD_start = DateTimeOps.getLastSundayOfApril(year);
				JD_end = DateTimeOps.getLastSundayOfNovember(year);
			}

			if (obs.getDSTCode() == DST_RULE.S1)
			{
				JD_start = DateTimeOps.getLastSundayOfOctober(year);
				JD_end = DateTimeOps.getLastSundayOfMarch(year + 1);
				if (JD_UT < JD_start)
				{
					JD_start = DateTimeOps.getLastSundayOfOctober(year - 1);
					JD_end = DateTimeOps.getLastSundayOfMarch(year);
				}
			}
			if (obs.getDSTCode() == DST_RULE.S2)
			{
				JD_start = DateTimeOps.getLastSundayOfNovember(year);
				JD_end = DateTimeOps.getLastSundayOfApril(year + 1);
				if (JD_UT < JD_start)
				{
					JD_start = DateTimeOps.getLastSundayOfNovember(year - 1);
					JD_end = DateTimeOps.getLastSundayOfApril(year);
				}
			}

			// Change start/end dates for custom selection of USA dst rule
			if (obs.getDSTCode() == DST_RULE.USA_NEW)
			{
				JD_start = DateTimeOps.getSecondSundayOfMarch(year);
				JD_end = DateTimeOps.getFirstSundayOfNovember(year);
			}
			if (obs.getDSTCode() == DST_RULE.USA_OLD)
			{
				JD_start = DateTimeOps.getFirstSundayOfApril(year);
				JD_end = DateTimeOps.getLastSundayOfOctober(year);
			}
			if (obs.getDSTCode() == DST_RULE.USA_AUTO)
			{
				if (JD_UT < 2454102.0)
				{
					JD_start = DateTimeOps.getFirstSundayOfApril(year);
					JD_end = DateTimeOps.getLastSundayOfOctober(year);
				} else
				{
					JD_start = DateTimeOps.getSecondSundayOfMarch(year);
					JD_end = DateTimeOps.getFirstSundayOfNovember(year);
				}
			}

			double hoffset = 1;
			if (obs.getDSTCode() == DST_RULE.CUSTOM)
			{
				int dst0 = obs.getDSTCode().start, dst1 = obs.getDSTCode().end;
				if (dst0 < 0 || dst0 > 9 || dst1 < 0 || dst1 > 9) throw new JPARSECException("Custom dst rule has invalid start/end fields.");

				if (dst0 == 0 || dst1 == 0) return null;
				hoffset = obs.getDSTCode().timeOffsetHours;
				switch (dst0) {
				case DST_RULE.FIRST_SUNDAY_APRIL:
					JD_start = DateTimeOps.getFirstSundayOfApril(year);
					break;
				case DST_RULE.FIRST_SUNDAY_NOVEMBER:
					JD_start = DateTimeOps.getFirstSundayOfNovember(year);
					break;
				case DST_RULE.LAST_SUNDAY_APRIL:
					JD_start = DateTimeOps.getLastSundayOfApril(year);
					break;
				case DST_RULE.LAST_SUNDAY_APRIL_NEXT_YEAR:
					JD_start = DateTimeOps.getLastSundayOfApril(year+1);
					break;
				case DST_RULE.LAST_SUNDAY_MARCH:
					JD_start = DateTimeOps.getLastSundayOfMarch(year);
					break;
				case DST_RULE.LAST_SUNDAY_MARCH_NEXT_YEAR:
					JD_start = DateTimeOps.getLastSundayOfMarch(year+1);
					break;
				case DST_RULE.LAST_SUNDAY_NOVEMBER:
					JD_start = DateTimeOps.getLastSundayOfNovember(year);
					break;
				case DST_RULE.LAST_SUNDAY_OCTOBER:
					JD_start = DateTimeOps.getLastSundayOfOctober(year);
					break;
				case DST_RULE.SECOND_SUNDAY_MARCH:
					JD_start = DateTimeOps.getFirstSundayOfApril(year);
					break;
				default:
					throw new JPARSECException("Custom dst rule has invalid start/end fields.");
				}
				switch (dst1) {
				case DST_RULE.FIRST_SUNDAY_APRIL:
					JD_end = DateTimeOps.getFirstSundayOfApril(year);
					break;
				case DST_RULE.FIRST_SUNDAY_NOVEMBER:
					JD_end = DateTimeOps.getFirstSundayOfNovember(year);
					break;
				case DST_RULE.LAST_SUNDAY_APRIL:
					JD_end = DateTimeOps.getLastSundayOfApril(year);
					break;
				case DST_RULE.LAST_SUNDAY_APRIL_NEXT_YEAR:
					JD_end = DateTimeOps.getLastSundayOfApril(year+1);
					break;
				case DST_RULE.LAST_SUNDAY_MARCH:
					JD_end = DateTimeOps.getLastSundayOfMarch(year);
					break;
				case DST_RULE.LAST_SUNDAY_MARCH_NEXT_YEAR:
					JD_end = DateTimeOps.getLastSundayOfMarch(year+1);
					break;
				case DST_RULE.LAST_SUNDAY_NOVEMBER:
					JD_end = DateTimeOps.getLastSundayOfNovember(year);
					break;
				case DST_RULE.LAST_SUNDAY_OCTOBER:
					JD_end = DateTimeOps.getLastSundayOfOctober(year);
					break;
				case DST_RULE.SECOND_SUNDAY_MARCH:
					JD_end = DateTimeOps.getFirstSundayOfApril(year);
					break;
				default:
					throw new JPARSECException("Custom dst rule has invalid start/end fields.");
				}
			}

			// Set an offset of 1 hour, due to the fact that the time changes at
			// 01:00 UT, not at 00:00. This is almost true for Europe ...
			// For USA DST really applies at 2 AM local time in March (-> 3 AM), and at
			// 2 AM local time in November (-> 1 AM), see
			// http://www.usno.navy.mil/USNO/astronomical-applications/astronomical-information-center/daylight-time
			double offset = hoffset / 24.0;

			return new double[] {JD_start + offset, JD_end + offset};
		}

		return null;
	}

	/**
	 * Returns the Daylight Saving Time offset from the system.
	 * This method uses Java native method to obtain the DST.
	 * Historical known changes in particular countries will
	 * be automatically considered.<P>
	 *
	 * @param year The year to obtain DST refered to.
	 * @param locale A Locale corresponding to certain country
	 * to override the current system's one. Can be null to use
	 * the default.
	 * @return The DST in hours.
	 * @throws JPARSECException If an error occurs.
	 */
	public static double getDSTfromSystem(int year, Locale locale)
	throws JPARSECException {
		double dstOffset = 0.0;
		for (int m = 1; m <= 12 && dstOffset == 0.0; ++m)
		{
			GregorianCalendar c = (new AstroDate(year, m, 1)).toGCalendar();
			if (locale != null) c = (GregorianCalendar) GregorianCalendar.getInstance(locale);
	        dstOffset = (int) (c.get(Calendar.DST_OFFSET) / 60000.0);
	    }
		return dstOffset / 60.0;
	}

	/**
	 * Returns calculation time adequate to use it in ephemeris calculations
	 * using TT (magnitudes related to Earth).
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @return Julian centuries from J2000 in TT.
	 * @throws JPARSECException If the date is invalid.
	 */
	public static double getCalcTime(TimeElement time, ObserverElement obs, EphemerisElement eph)
			throws JPARSECException
	{
		// Obtain Julian day in Barycentric Dynamical Time
		double JD = TimeScale.getJD(time, obs, eph, SCALE.TERRESTRIAL_TIME);

		// Obtain time in julian centuries from J2000
		double calc_time = Functions.toCenturies(JD);

		return calc_time;
	}

	/**
	 * True (default value) to extrapolate results after the last year
	 * available. Otherwise, the last value available will be used for
	 * any future time. The discontinuity between the tabulated data
	 * and the approximate extrapolation from formulae is around 1s,
	 * noticeable one year after the last date available in the tabulated
	 * data.
	 */
	public static boolean allowExtrapolationOfTTminusUT1ForFutureDates = true;

	/**
	 * Calculate difference between Terrestrial Time and Universal Time UT1 in
	 * seconds for a given date, using the last available references. For years
	 * before -500, -20 + 32 * t * t formula is used (t = (year - 1820) / 100).
	 * Uncertainty up to a few hours as said by JPL, but around 10 minutes near
	 * year -500.
	 * <P>
	 * For years between -500 and present spline interpolation around observational
	 * measurements is used. Uncertainty goes from 8 minutes at -500 to 0.3 minutes
	 * at 1600. See TTminusUT1.txt file.
	 * <P>
	 * After 365 days + the last date in the previous tabulated data the formula
	 * 62.92 + 0.32217 * (year - 2000.0) + 0.005589 * (year - 2000.0) * (year - 2000.0) is
	 * used until 2050, then -20.0 + 32.0 * t * t - 0.5628 * (2150.0 - year), with
	 * t = (yr - 1820) / 100, is used until 2150. After 2150 the formula
	 * -20 + 32 * t * t is used again. This is only applied if extrapolation is allowed.
	 * <P>
	 * After calculations a little correction due to an improved
	 * determination of Moon secular acceleration is applied to all expressions,
	 * except the tabulated values of The Astronomical Almanac between 1955 and present.
	 * <P>
	 * References:
	 * <P>
	 * Morrison and Stephenson, 2004. See http://adsabs.harvard.edu/abs/2004JHA....35..327M
	 * <P>
	 * Fred Spenak, Jean Meeus, Five Millenium Canon of Solar Eclipses,
	 * NASA/TP-2006-214141. See http://eclipse.gsfc.nasa.gov/SEcat5/deltatpoly.html
	 * <P>
	 * U.S. Naval Observatory file <A target="_blank" href =
	 * "ftp://maia.usno.navy.mil/ser7/tai-utc.dat">ftp://maia.usno.navy.mil/ser7/tai-utc.dat</A>.
	 * <P>
	 * Section 2.58.1 (p87) of the 1992 Explanatory Supplement of the
	 * Astronomical Almanac.
	 * <P>
	 * Stephenson, F. R., and L. V. Morrison, "Long-term changes in the rotation
	 * of the Earth: 700 B.C. to A.D. 1980," Philosophical Transactions of the
	 * Royal Society of London Series A 313, 47-70 (1984).
	 * <P>
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @return TT minus UT1 in seconds.
	 * @throws JPARSECException If an error occurs.
	 */
	public static double getTTminusUT1(TimeElement time, ObserverElement obs) throws JPARSECException
	{
		if (time.timeScale == SCALE.UNIVERSAL_TIME_UT1 || time.timeScale == SCALE.UNIVERSAL_TIME_UTC)
			return TimeScale.getTTminusUT1(time.astroDate);

		double TT_UT1_last_calc_T = -10000.0;
		double TTminusUT1 = 0.0;
		Object o = DataBase.getData("TTminusUT1", true);
		if (o != null) {
			double a[] = (double[]) o;
			TT_UT1_last_calc_T = a[0];
			TTminusUT1 = a[1];
		}

		double tt, t;

		// Pass to UT
		double JD_UT = time.astroDate.jd();
		if (time.timeScale == TimeElement.SCALE.LOCAL_TIME) {
			double LT2UTC = -obs.getTimeZone() / Constant.HOURS_PER_DAY;
			JD_UT += LT2UTC;
		}

		double UT12TT = TimeScale.getTTminusUT1(new AstroDate(JD_UT)) / Constant.SECONDS_PER_DAY;
		if (time.timeScale == TimeElement.SCALE.TERRESTRIAL_TIME || time.timeScale == TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME)
			JD_UT -= UT12TT;

		if (time.timeScale == TimeElement.SCALE.LOCAL_TIME) {
			double DST = -(double) getDST(JD_UT, obs) / Constant.HOURS_PER_DAY;
			//LT2UTC += DST;
			JD_UT += DST;
		}

		AstroDate astro = new AstroDate(JD_UT);

		tt = astro.getYear() + (astro.getMonth() - 1 + (astro.getDay() - 1.0) / 30.0) / 12.0;

		if (JD_UT != TT_UT1_last_calc_T)
		{
			TT_UT1_last_calc_T = JD_UT;

			// Uncertainty up to a few hours, but around 10 minutes in year -500
			if (astro.getYear() < -500)
			{
				t = (tt - 1820.0) / 100.0;
				TTminusUT1 = -20 + 32.0 * t * t;
			}

			// Uncertainty of a few minutes
/*			if (astro.getYear() >= -500 && astro.getYear() < 500)
			{
				t = tt / 100.0;
				TTminusUT1 = 10583.6 - 1014.41 * t + 33.78311 * t * t - 5.952053 * t * t * t - 0.1798452 * Math.pow(t, 4.0) + 0.022174192 * Math
						.pow(t, 5.0) + 0.0090316521 * Math.pow(t, 6.0);
			}

			// Uncertainty below 2 minutes
			if (astro.getYear() >= 500 && astro.getYear() < 1620) // 1600 following Spenak
			{
				t = (tt - 1000.0) / 100.0;
				TTminusUT1 = 1574.2 - 556.01 * t + 71.23472 * t * t + 0.319781 * t * t * t - 0.8503463 * Math.pow(t, 4.0) - 0.005050998 * Math
						.pow(t, 5.0) + 0.0083572073 * Math.pow(t, 6.0);
			}

			// Uncertainty below 2 minutes. I neglect this expansion from Spenak since the previous one
			// produces smoother results with tabulated values in TTminusUT1.txt
			if (astro.getYear() >= 1600 && astro.getYear() < 1620)
			{
				t = tt - 1600.0;
				TTminusUT1 = 120.0 - 0.9808 * t - 0.01532 * t * t + (t * t * t / 7129.0);
			}
*/
			/*
			 * Tabulated values from AA.
			 */
			if (DATA.interp == null) TimeScale.updateLeapSecondsAndDT(null, null);

			if (JD_UT >= DATA.dt_initJD && JD_UT < DATA.dt_endJD)
			{
				TTminusUT1 = DATA.interp.splineInterpolation(JD_UT);
			}
			if (JD_UT >= DATA.dt_endJD) {
				TTminusUT1 = DATA.interp.linearInterpolation(DATA.dt_endJD);
			}

			boolean approxFuture = false;
			if (JD_UT > DATA.dt_endJD + 365 && allowExtrapolationOfTTminusUT1ForFutureDates) approxFuture = true;
			if (approxFuture) {
				if (astro.getYear() < 2050)
				{
					t = tt - 2000.0;
					TTminusUT1 = 62.92 + 0.32217 * t + 0.005589 * t * t;
				} else
				{
					if (astro.getYear() < 2150)
					{
						t = (tt - 1820.0) / 100.0;
						TTminusUT1 = -20.0 + 32.0 * t * t - 0.5628 * (2150.0 - tt);
					} else
					{
						t = (tt - 1820.0) / 100.0;
						TTminusUT1 = -20.0 + 32.0 * t * t;
					}
				}
			}

			/* Correct TT-UT1 for difference between Moon secular acceleration
			 * used in Morrison and Stephenson 2004 (-26 "/cent^2) to the most recent
			 * estimation
			 */
			if (tt < 1955 || approxFuture)
			{
				double JD_TT = JD_UT + UT12TT;
				double FIXED_JD_TT = TimeScale.dynamicalTimeCorrectionForMoonSecularAcceleration(JD_TT);
				double dT = (FIXED_JD_TT - JD_TT) * Constant.SECONDS_PER_DAY;
				TTminusUT1 += dT;
			}

			DataBase.addData("TTminusUT1", new double[] {TT_UT1_last_calc_T, TTminusUT1}, true);
		}
		return TTminusUT1;
	}

	/**
	 * Returns TT minus UT1.
	 * @param astro_ut The date in UT.
	 * @return TT-UT1 (s).
	 * @throws JPARSECException If an error occurs.
	 */
	public static double getTTminusUT1(AstroDate astro_ut)
	throws JPARSECException {
		double TT_UT1_last_calc_T = -10000.0;
		double TTminusUT1 = 0;
		Object o = DataBase.getData("TTminusUT1_deprecated", true);
		if (o != null) {
			double a[] = (double[]) o;
			TT_UT1_last_calc_T = a[0];
			TTminusUT1 = a[1];
		}

		double tt, t;

		tt = astro_ut.getYear() + (astro_ut.getMonth() - 1 + (astro_ut.getDay() - 1.0) / 30.0) / 12.0;

		if (tt != TT_UT1_last_calc_T)
		{
			TT_UT1_last_calc_T = tt;

			if (astro_ut.getYear() < -500)
			{
				t = (tt - 1820.0) / 100.0;
				TTminusUT1 = -20 + 32.0 * t * t;
			}

			/*
			 * Tabulated values from AA.
			 */
			if (DATA.interp == null) TimeScale.updateLeapSecondsAndDT(null, null);

			double JD_UT = astro_ut.jd();
			if (JD_UT >= DATA.dt_initJD && JD_UT < DATA.dt_endJD)
			{
				TTminusUT1 = DATA.interp.splineInterpolation(JD_UT);
			}
			if (JD_UT >= DATA.dt_endJD)
				TTminusUT1 = DATA.interp.linearInterpolation(DATA.dt_endJD);

			boolean approxFuture = false;
			if (JD_UT > DATA.dt_endJD + 365 && allowExtrapolationOfTTminusUT1ForFutureDates) approxFuture = true;
			if (approxFuture) {
				if (astro_ut.getYear() < 2050)
				{
					t = tt - 2000.0;
					TTminusUT1 = 62.92 + 0.32217 * t + 0.005589 * t * t;
				} else
				{
					if (astro_ut.getYear() < 2150)
					{
						t = (tt - 1820.0) / 100.0;
						TTminusUT1 = -20.0 + 32.0 * t * t - 0.5628 * (2150.0 - tt);
					} else
					{
						t = (tt - 1820.0) / 100.0;
						TTminusUT1 = -20.0 + 32.0 * t * t;
					}
				}
			}

			/* Correct TT-UT1 for difference between Moon secular aceleration
			 * used in Morrison and Stephenson 2004 (-26 "/cent^2) to the most recent
			 * estimation
			 */
			if (tt < 1955 || approxFuture)
			{
				double JD_TT = astro_ut.jd(); // + UT12TT; // Approx. use of UT1 as TT
				double FIXED_JD_TT = TimeScale.dynamicalTimeCorrectionForMoonSecularAcceleration(JD_TT);
				double dT = (FIXED_JD_TT - JD_TT) * Constant.SECONDS_PER_DAY;
				TTminusUT1 += dT;
			}

			DataBase.addData("TTminusUT1_deprecated", new double[] {TT_UT1_last_calc_T, TTminusUT1}, true);
		}
		return TTminusUT1;
	}

	/**
	 * Forces the value of TT-UT1 for a given date.
	 * Note in case the TT-UT1 is requested for another date, the
	 * value forced here for the old date will be lost.
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param TTminusUT1 TT-UT1 (s).
	 * @throws JPARSECException If an error occurs.
	 */
	public static void forceTTminusUT1(TimeElement time, ObserverElement obs, double TTminusUT1) throws JPARSECException
	{
		// Pass to UT
		double JD_UT = time.astroDate.jd();
		double LT2UTC = -obs.getTimeZone() / Constant.HOURS_PER_DAY;
		if (time.timeScale == TimeElement.SCALE.LOCAL_TIME)
			JD_UT += LT2UTC;

		if (time.timeScale == TimeElement.SCALE.TERRESTRIAL_TIME || time.timeScale == TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME) {
			double UT12TT = TimeScale.getTTminusUT1(new AstroDate(JD_UT)) / Constant.SECONDS_PER_DAY;
			JD_UT -= UT12TT;
		}

		if (time.timeScale == TimeElement.SCALE.LOCAL_TIME) {
			double DST = -(double) getDST(JD_UT, obs) / Constant.HOURS_PER_DAY;
			LT2UTC += DST;
			JD_UT += DST;
		}

		DataBase.addData("TTminusUT1", new double[] {JD_UT, TTminusUT1}, true);
	}

	/**
	 * Returns the last Julian day when some information about TT-UT1 is available.
	 * The value can be a true value, or an extrapolated one, depending on the
	 * last time the package was updated.
	 *
	 * @return Last Julian day when TT-UT1 is known (exactly or extrapolated).
	 * @throws JPARSECException If an error occurs.
	 */
	public static double getTTminusUT1LastDateAvailable()
	throws JPARSECException {
		if (DATA.interp == null) TimeScale.updateLeapSecondsAndDT(null, null);
		return DATA.dt_endJD;
	}

	/**
	 * Calculate difference between International Atomic Time and Universal Time
	 * Coordinate in seconds for a given date. After 1960 a set of tabulated
	 * values adapted from the SOFA software library, based on values of the
	 * U.S. Naval Observatory, are used.
	 * <P>
	 * Before 1961 zero is returned due to a lack of information.
	 * <P>
	 * Updated deltaT predictions can be obtained from this network repository:
	 * <A target="_blank" href =
	 * "http://maia.usno.navy.mil">http://maia.usno.navy.mil</A>.
	 * <P>
	 * References:
	 * <P>
	 * U.S. Naval Observatory file <A target="_blank" href =
	 * "ftp://maia.usno.navy.mil/ser7/tai-utc.dat">ftp://maia.usno.navy.mil/ser7/tai-utc.dat</A>.
	 * <P>
	 * Section 2.58.1 (p87) of the 1992 Explanatory Supplement of the
	 * Astronomical Almanac.
	 * <P>
	 *
	 * @param astro AstroDate object defining the date in UTC.
	 * @return TAI minus UTC, in seconds.
	 * @deprecated Class {@linkplain EarthOrientationParameters} is recommended instead.
	 * @throws JPARSECException If the date is invalid.
	 */
	public static double getTAIminusUTC(AstroDate astro) throws JPARSECException
	{
		double tt, DAT = 0.0;

		tt = astro.getYear() + (astro.getMonth() - 1 + (astro.getDay() - 1.0) / 30.0) / 12.0;

		if (tt >= 1961.0)
		{
			if (DATA.leap_seconds == null) TimeScale.updateLeapSecondsAndDT(null, null);

			// Find the most recent table entry.
			int IS = 0;

			for (int i = 0; i < DATA.leap_seconds.length; i++)
			{
				// Months elapsed
				double date = DATA.leap_seconds[i][0] * 12.0 + DATA.leap_seconds[i][1];

				if (date < (12.0 * tt))
				{
					IS = i;
				}
			}

			// Get the Delta(AT).
			DAT = DATA.leap_seconds[IS][2];

			// Obtain JD
			double JD = astro.jd();

			// If pre-1972, adjust for drift.
			if (IS <= 13)
			{
				DAT += (JD - DATA.drift[IS][0]) * DATA.drift[IS][1];
			}
		}

		return DAT;
	}

	/**
	 * Return difference between Terrestrial Time and Universal Time Coordinate.
	 * Should only be used after 1960. Note TT-UT1 is the default operation
	 * prior to obtaining the dynamical time used for calculations.
	 *
	 * @param astro AstroDate object defining the date in UTC.
	 * @return TT - UTC, in seconds.
	 * @deprecated Class {@linkplain EarthOrientationParameters} is recommended instead.
	 * @throws JPARSECException If the date is invalid.
	 */
	public static double getTTminusUTC(AstroDate astro) throws JPARSECException
	{
		double TAIminusUTC = TimeScale.getTAIminusUTC(astro);

		double TT_UTC = TimeScale.TT_MINUS_TAI + TAIminusUTC;
		return TT_UTC;
	}

	/**
	 * Obtain difference between Barycentric Coordinate Time and Barycentric
	 * Dynamical Time. From SOFA library.
	 * <P>
	 * The 2006 IAU General Assembly introduced a conventional linear
	 * transformation between TDB and TCB.  This transformation
	 * compensates for the drift between TCB and terrestrial time TT,
	 * and keeps TDB approximately centered on TT.  Because the
	 * relationship between TT and TCB depends on the adopted solar
	 * system ephemeris, the degree of alignment between TDB and TT over
	 * long intervals will vary according to which ephemeris is used.
	 * Former definitions of TDB attempted to avoid this problem by
	 * stipulating that TDB and TT should differ only by periodic
	 * effects.  This is a good description of the nature of the
	 * relationship but eluded precise mathematical formulation.  The
	 * conventional linear relationship adopted in 2006 sidestepped
	 * these difficulties whilst delivering a TDB that in practice was
	 * consistent with values before that date.
	 * <P>
	 * TDB is essentially the same as Teph, the time argument for the
	 * JPL solar system ephemerides.
	 * @param JD Julian day in TDB.
	 * @return Difference TCB - TDB, in seconds.
	 */
	public static double getTCBminusTDB(double JD)
	{
		// Value of Lb according to the IAU.
		double Lb = 1.550519768e-8;

		// Obtain difference in seconds. Note by definition TCB = TDB in 1977.0
		double correction = (JD - 2443144.5003725 + 6.55E-5 / Constant.SECONDS_PER_DAY) * (Lb / (1.0 - Lb)) * Constant.SECONDS_PER_DAY
				+ 6.55E-5 / Constant.SECONDS_PER_DAY;

		return correction;
	}

	/**
	 * Obtain difference between Geocentric Coordinate Time and Terrestrial Time.
	 * From SOFA library.
	 *
	 * @param JD Julian day in TT.
	 * @return Difference TCG - TT, in seconds.
	 */
	public static double getTCGminusTT(double JD)
	{
		// Value of Lg according to the IAU.
		double Lg = 6.969290134e-10;

		// Obtain difference in seconds. Note by definition TCG = TT in 1977.0
		double correction = (JD - 2443144.5003725) * (Lg / (1.0 - Lg)) * Constant.SECONDS_PER_DAY;

		return correction;
	}

	/**
	 * Obtain difference between Geocentric Coordinate Time and Barycentric
	 * Coordinate Time. Precision better than 1 ms, see Seidelmann and Kovalevsky 2002.
	 *
	 * @param JD Julian day in TCB.
	 * @return Difference TCG - TCB, in seconds.
	 */
	public static double getTCGminusTCB(double JD)
	{
		// Value of Lb according to the IAU.
		double Lb = 1.4808268457e-8;

		// Obtain difference in seconds.
		double correction = (JD - 2443144.5003725) * (Lb / (1.0 - Lb)) * Constant.SECONDS_PER_DAY;

		// Apply corrections due to Earth, neglecting other terms well below the
		// millisecond. Note we are following Seidelmann and Kovalevsky 2002.
		double g = (357.53 + 0.985003 * (JD - Constant.J2000)) * Constant.DEG_TO_RAD;
		double periodic = 0.0015658 * Math.sin(g) + 0.000014 * Math.sin(2.0 * g);

		return correction + periodic;
	}

	/**
	 * Corrects Julian day of calculations in dynamical time to consider a different value for
	 * the Moon secular acceleration. The value used by Stephenson and Morrison is
	 * -26"/centuri^2, and the one considered as correct is slightly different (see
	 * {@linkplain Elp2000#MOON_SECULAR_ACCELERATION}). This correction is used
	 * in JPARSEC although its magnitude is below the uncertainty in TT-UT, you will only
	 * notice some difference when studying eclipses in ancient times. Correction
	 * for different years are as follows:<P>
	 * <pre>
	 * Year       Correction (seconds)
	 * -2000      -202
	 * -1000      -113
	 *     0      -49
	 *  1000      -12
	 *  1955       0.000
	 *  2000      -0.026
	 *  3000      -14
	 * </pre>
	 *
	 * @param jd Julian day in TDB/TT.
	 * @return Output (corrected) Julian day in the same time scale.
	 */
	public static double dynamicalTimeCorrectionForMoonSecularAcceleration(double jd)
	{
		double cent = (jd - 2435109.0) / Constant.JULIAN_DAYS_PER_CENTURY;
		double deltaT = -0.91072 * (Elp2000.MOON_SECULAR_ACCELERATION + 26.0) * cent * cent;

		return jd + deltaT / Constant.SECONDS_PER_DAY;
	}

	/**
	 * Calculate approximate difference between Barycentric Dynamical Time and Terrestrial
	 * Time in seconds for a given date. Sometimes ignored in ephemeris
	 * calculations because it it is a very little correction.
	 * <P>
	 * This method is based on SOFA (Standards of Astronomy) software library.
	 * <P>
	 * TT can be regarded as a coordinate time that is realized as an
	 * offset of 32.184s from International Atomic Time, TAI.  TT is a
	 * specific linear transformation of geocentric coordinate time TCG,
	 * which is the time scale for the Geocentric Celestial Reference
	 * System, GCRS.
	 * <P>
	 * TDB is a coordinate time, and is a specific linear transformation
	 * of barycentric coordinate time TCB, which is the time scale for
	 * the Barycentric Celestial Reference System, BCRS.
	 * <P>
	 * The difference TCG-TCB depends on the masses and positions of the
	 * bodies of the solar system and the velocity of the Earth.  It is
	 * dominated by a rate difference, the residual being of a periodic
	 * character.  The latter, which is modeled by the present routine,
	 * comprises a main (annual) sinusoidal term of amplitude
	 * approximately 0.00166 seconds, plus planetary terms up to about
	 * 20 microseconds, and lunar and diurnal terms up to 2 microseconds.
	 * These effects come from the changing transverse Doppler effect
	 * and gravitational red-shift as the observer (on the Earth's
	 * surface) experiences variations in speed (with respect to the
	 * BCRS) and gravitational potential.
	 * <P>
	 * TDB can be regarded as the same as TCB but with a rate adjustment
	 * to keep it close to TT, which is convenient for many applications.
	 * The history of successive attempts to define TDB is set out in
	 * Resolution 3 adopted by the IAU General Assembly in 2006, which
	 * defines a fixed TDB(TCB) transformation that is consistent with
	 * contemporary solar-system ephemerides.  Future ephemerides will
	 * imply slightly changed transformations between TCG and TCB, which
	 * could introduce a linear drift between TDB and TT;  however, any
	 * such drift is unlikely to exceed 1 nanosecond per century.
	 * <P>
	 * The geocentric TDB-TT model used in the present routine is that of
	 * Fairhead & Bretagnon (1990), in its full form.  It was originally
	 * supplied by Fairhead (private communications with P.T.Wallace,
	 * 1990) as a Fortran subroutine.  The present routine contains an
	 * adaptation of the Fairhead code.  The numerical results are
	 * essentially unaffected by the changes, the differences with
	 * respect to the Fairhead & Bretagnon original being at the 1D-20 s
	 * level.
	 * <P>
	 * The topocentric part of the model is from Moyer (1981) and
	 * Murray (1983), with fundamental arguments adapted from
	 * Simon et al. 1994.  It is an approximation to the expression
	 * ( v / c ) . ( r / c ), where v is the barycentric velocity of
	 * the Earth, r is the geocentric position of the observer and
	 * c is the speed of light.
	 * <P>
	 * During the interval 1950-2050, the absolute accuracy is better
	 * than +/- 3 nanoseconds relative to time ephemerides obtained by
	 * direct numerical integrations based on the JPL DE405 solar system
	 * ephemeris.
	 * <P>
	 * It must be stressed that the present routine is merely a model,
	 * and that numerical integration of solar-system ephemerides is the
	 * definitive method for predicting the relationship between TCG and
	 * TCB and hence between TT and TDB.
	 * <P>
	 * References:
	 * <P>
	 * Fairhead,L., & Bretagnon,P., Astron.Astrophys., 229, 240-247 (1990).
	 * <P>
	 * IAU 2006 Resolution 3.
	 * <P>
	 * McCarthy, D. D., Petit, G. (eds.), IERS Conventions (2003), IERS Technical Note No. 32, BKG (2004).
	 * <P>
	 * Moyer,T.D., Cel.Mech., 23, 33 (1981).
	 * <P>
	 * Murray,C.A., Vectorial Astrometry, Adam Hilger (1983).
	 * <P>
	 * Seidelmann,P.K. et al., Explanatory Supplement to the Astronomical
	 * Almanac, Chapter 2, University Science Books (1992).
	 * <P>
	 * Simon J.L., Bretagnon P., Chapront J., Chapront-Touze M., Francou G. &
	 * Laskar J., 1994, Astron.Astrophys., 282, 663-683.
	 * <P>
	 * This revision: 2005 August 26
	 * <P>
	 * Copyright (C) 2005 IAU SOFA Review Board.
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @return TDB-TT in seconds.
	 * @throws JPARSECException If the date is invalid.
	 */
	public static double getTDBminusTT(TimeElement time, ObserverElement obs, EphemerisElement eph)
			throws JPARSECException
	{
		double TDB_TT_last_value = 0.0;
		double TDB_TT_last_calc_jd = -1E100;
		Object o = DataBase.getData("TDBminusTT", true);
		if (o != null) {
			double a[] = (double[]) o;
			TDB_TT_last_calc_jd = a[0];
			TDB_TT_last_value = a[1];
		}

		// We use TT instead of TDB, but this has no effect in the prediction
		TimeElement newt = time.clone();
		double JD = 0.0;
		if (newt.timeScale == TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME ||
				newt.timeScale == TimeElement.SCALE.TERRESTRIAL_TIME) {
			JD = newt.astroDate.jd();
			newt.timeScale = SCALE.TERRESTRIAL_TIME;
		} else {
			JD = getJD(newt, obs, eph, SCALE.TERRESTRIAL_TIME);
		}

		if (Math.abs(JD - TDB_TT_last_calc_jd) < 1.0 / Constant.SECONDS_PER_DAY)
			return TDB_TT_last_value;

		TDB_TT_last_calc_jd = JD;

		// Time since J2000.0 in Julian millennia.
		double T = (JD - Constant.J2000) / Constant.JULIAN_DAYS_PER_MILLENIA;

		// This is the simple formula used by NOVAS. It is said to be accurate to 10 microseconds,
		// maybe true in current times, but degrades a lot with time
		if (!eph.preferPrecisionInEphemerides && Math.abs(T) < 0.1) {
			// USNO circular 179, eq. 2.6, accurate to 10 microseconds
			double secs = 0.001657 * Math.sin(628.3076 * T + 6.2401)
		            + 0.000022 * Math.sin(575.3385 * T + 4.2970)
		            + 0.000014 * Math.sin(1256.6152 * T + 6.1969)
		            + 0.000005 * Math.sin(606.9777 * T + 4.0212)
		            + 0.000005 * Math.sin(52.9691 * T + 0.4444)
		            + 0.000002 * Math.sin(21.3299 * T + 5.5431)
		            + 0.000010 * T * Math.sin(628.3076 * T + 4.2490);

			TDB_TT_last_value = secs;
			DataBase.addData("TDBminusTT", new double[] {TDB_TT_last_calc_jd, TDB_TT_last_value}, true);
			return secs;
		}

		// Obtain UT in fractions of days.
		double JD_UT = getJD(newt, obs, eph, SCALE.UNIVERSAL_TIME_UT1);
		double UT = (JD_UT - Math.floor(JD_UT)) + 0.5;
		if (UT > 1.0)
			UT = UT - 1.0;

		/*
		 * ================= Topocentric terms =================
		 */

		/*
		 * Reduction from geodetic latitude to geocentric latitude AA 1986 page
		 * K5
		 */
		double U = 0.0, V = 0.0, WT = 0.0;

		// Distance from Earth spin axis (km)
		if (obs.getMotherBody() == TARGET.EARTH) U = obs.getGeoRad() * obs.getEllipsoid().getEquatorialRadius() * Math.cos(obs.getGeoLat());

		// Distance north of equatorial plane (km)
		if (obs.getMotherBody() == TARGET.EARTH) V = obs.getGeoRad() * obs.getEllipsoid().getEquatorialRadius() * Math.sin(obs.getGeoLat());

		// Convert UT to local solar time in radians.
		double TSOL = 0.0;
		if (obs.getMotherBody() == TARGET.EARTH) TSOL = UT * 2.0 * Math.PI + obs.getLongitudeRad();

		// FUNDAMENTAL ARGUMENTS: Simon et al. 1994.

		// Combine time argument (millennia) with deg/arcsec factor.
		double W = T / 3600.0;

		// Sun Mean Longitude.
		double ELSUN = Constant.DEG_TO_RAD * Functions.normalizeDegrees(280.46645683 + 1296027711.03429 * W);

		// Sun Mean Anomaly.
		double EMSUN = Constant.DEG_TO_RAD * Functions.normalizeDegrees(357.52910918 + 1295965810.481 * W);

		// Mean Elongation of Moon from Sun.
		double D = Constant.DEG_TO_RAD * Functions.normalizeDegrees(297.85019547 + 16029616012.090 * W);

		// Mean Longitude of Jupiter.
		double ELJ = Constant.DEG_TO_RAD * Functions.normalizeDegrees(34.35151874 + 109306899.89453 * W);

		// Mean Longitude of Saturn.
		double ELS = Constant.DEG_TO_RAD * Functions.normalizeDegrees(50.07744430 + 44046398.47038 * W);

		// TOPOCENTRIC TERMS: Moyer 1981 and Murray 1983.
		if (obs.getMotherBody() == TARGET.EARTH) WT = +0.00029e-10 * U * Math.sin(TSOL + ELSUN - ELS) + 0.00100e-10 * U * Math.sin(TSOL - 2.0 * EMSUN) + 0.00133e-10 * U * Math
				.sin(TSOL - D) + 0.00133e-10 * U * Math.sin(TSOL + ELSUN - ELJ) - 0.00229e-10 * U * Math
				.sin(TSOL + 2.0 * ELSUN + EMSUN) - 0.02200e-10 * V * Math.cos(ELSUN + EMSUN) + 0.05312e-10 * U * Math
				.sin(TSOL - EMSUN) - 0.13677e-10 * U * Math.sin(TSOL + 2.0 * ELSUN) - 1.31840e-10 * V * Math.cos(ELSUN) + 3.17679e-10 * U * Math
				.sin(TSOL);

		/*
		 * ===================== Fairhead et al. model =====================
		 */

		// T**0
		double W0 = 0.0;
		for (int J = 473; J >= 0; J--)
		{
			int FAIRHD_index = J * 3 - 1;
			W0 += FAIRHD[FAIRHD_index + 1] * Math.sin(FAIRHD[FAIRHD_index + 2] * T + FAIRHD[FAIRHD_index + 3]);
		}

		// T**1
		double W1 = 0.0;
		for (int J = 678; J >= 474; J--)
		{
			int FAIRHD_index = J * 3 - 1;
			W1 += FAIRHD[FAIRHD_index + 1] * Math.sin(FAIRHD[FAIRHD_index + 2] * T + FAIRHD[FAIRHD_index + 3]);
		}

		// T**2
		double W2 = 0.0;
		for (int J = 763; J >= 679; J--)
		{
			int FAIRHD_index = J * 3 - 1;
			W2 += FAIRHD[FAIRHD_index + 1] * Math.sin(FAIRHD[FAIRHD_index + 2] * T + FAIRHD[FAIRHD_index + 3]);
		}

		// T**3
		double W3 = 0.0;
		for (int J = 783; J >= 764; J--)
		{
			int FAIRHD_index = J * 3 - 1;
			W3 += FAIRHD[FAIRHD_index + 1] * Math.sin(FAIRHD[FAIRHD_index + 2] * T + FAIRHD[FAIRHD_index + 3]);
		}

		// T**4
		double W4 = 0.0;
		for (int J = 786; J >= 784; J--)
		{
			int FAIRHD_index = J * 3 - 1;
			W4 += FAIRHD[FAIRHD_index + 1] * Math.sin(FAIRHD[FAIRHD_index + 2] * T + FAIRHD[FAIRHD_index + 3]);
		}

		// Multiply by powers of T and combine.
		double WF = T * (T * (T * (T * W4 + W3) + W2) + W1) + W0;

		// Adjustments to use JPL planetary masses instead of IAU.
		double WJ = 0.00065e-6 * Math.sin(6069.776754 * T + 4.021194) + 0.00033e-6 * Math
				.sin(213.299095 * T + 5.543132) + (-0.00196e-6 * Math.sin(6208.294251 * T + 5.696701)) + (-0.00173e-6 * Math
				.sin(74.781599 * T + 2.435900)) + 0.03638e-6 * T * T;

		/*
		 * ============ Final result ============
		 */

		// TDB-TT in seconds.
		double TDB = WT + WF + WJ;

		TDB_TT_last_value = TDB;
		DataBase.addData("TDBminusTT", new double[] {TDB_TT_last_calc_jd, TDB_TT_last_value}, true);

		return TDB;
	}

	/* Static data to calculate TDB-TT difference (from SOFA software library) */
	private static double[] FAIRHD =
	{
			// DATA ((FAIRHD(I,J) /I=1,3) /J= 1, 10) /
			1656.674564e-6, 6283.075849991, 6.240054195, 22.417471e-6, 5753.384884897, 4.296977442,
			13.839792e-6, 12566.151699983, 6.196904410, 4.770086e-6, 529.690965095, 0.444401603,
			4.676740e-6, 6069.776754553, 4.021195093, 2.256707e-6, 213.299095438, 5.543113262,
			1.694205e-6, -3.523118349, 5.025132748, 1.554905e-6, 77713.771467920, 5.198467090,
			1.276839e-6, 7860.419392439, 5.988822341, 1.193379e-6, 5223.693919802, 3.649823730,
			// DATA ((FAIRHD(I,J) /I=1,3) /J= 11, 20) /
			1.115322e-6, 3930.209696220, 1.422745069, 0.794185e-6, 11506.769769794, 2.322313077,
			0.447061e-6, 26.298319800, 3.615796498, 0.435206e-6, -398.149003408, 4.349338347,
			0.600309e-6, 1577.343542448, 2.678271909, 0.496817e-6, 6208.294251424, 5.696701824,
			0.486306e-6, 5884.926846583, 0.520007179, 0.432392e-6, 74.781598567, 2.435898309,
			0.468597e-6, 6244.942814354, 5.866398759, 0.375510e-6, 5507.553238667, 4.103476804,
			// DATA ((FAIRHD(I,J) /I=1,3) /J= 21, 30) /
			0.243085e-6, -775.522611324, 3.651837925, 0.173435e-6, 18849.227549974, 6.153743485,
			0.230685e-6, 5856.477659115, 4.773852582, 0.203747e-6, 12036.460734888, 4.333987818,
			0.143935e-6, -796.298006816, 5.957517795, 0.159080e-6, 10977.078804699, 1.890075226,
			0.119979e-6, 38.133035638, 4.551585768, 0.118971e-6, 5486.777843175, 1.914547226,
			0.116120e-6, 1059.381930189, 0.873504123, 0.137927e-6, 11790.629088659, 1.135934669,
			// DATA ((FAIRHD(I,J) /I=1,3) /J= 31, 40) /
			0.098358e-6, 2544.314419883, 0.092793886, 0.101868e-6, -5573.142801634, 5.984503847,
			0.080164e-6, 206.185548437, 2.095377709, 0.079645e-6, 4694.002954708, 2.949233637,
			0.062617e-6, 20.775395492, 2.654394814, 0.075019e-6, 2942.463423292, 4.980931759,
			0.064397e-6, 5746.271337896, 1.280308748, 0.063814e-6, 5760.498431898, 4.167901731,
			0.048042e-6, 2146.165416475, 1.495846011, 0.048373e-6, 155.420399434, 2.251573730,
			// DATA ((FAIRHD(I,J) /I=1,3) /J= 41, 50) /
			0.058844e-6, 426.598190876, 4.839650148, 0.046551e-6, -0.980321068, 0.921573539,
			0.054139e-6, 17260.154654690, 3.411091093, 0.042411e-6, 6275.962302991, 2.869567043,
			0.040184e-6, -7.113547001, 3.565975565, 0.036564e-6, 5088.628839767, 3.324679049,
			0.040759e-6, 12352.852604545, 3.981496998, 0.036507e-6, 801.820931124, 6.248866009,
			0.036955e-6, 3154.687084896, 5.071801441, 0.042732e-6, 632.783739313, 5.720622217,
			// DATA ((FAIRHD(I,J) /I=1,3) /J= 51, 60) /
			0.042560e-6, 161000.685737473, 1.270837679, 0.040480e-6, 15720.838784878, 2.546610123,
			0.028244e-6, -6286.598968340, 5.069663519, 0.033477e-6, 6062.663207553, 4.144987272,
			0.034867e-6, 522.577418094, 5.210064075, 0.032438e-6, 6076.890301554, 0.749317412,
			0.030215e-6, 7084.896781115, 3.389610345, 0.029247e-6, -71430.695617928, 4.183178762,
			0.033529e-6, 9437.762934887, 2.404714239, 0.032423e-6, 8827.390269875, 5.541473556,
			// DATA ((FAIRHD(I,J) /I=1,3) /J= 61, 70) /
			0.027567e-6, 6279.552731642, 5.040846034, 0.029862e-6, 12139.553509107, 1.770181024,
			0.022509e-6, 10447.387839604, 1.460726241, 0.020937e-6, 8429.241266467, 0.652303414,
			0.020322e-6, 419.484643875, 3.735430632, 0.024816e-6, -1194.447010225, 1.087136918,
			0.025196e-6, 1748.016413067, 2.901883301, 0.021691e-6, 14143.495242431, 5.952658009,
			0.017673e-6, 6812.766815086, 3.186129845, 0.022567e-6, 6133.512652857, 3.307984806,
			// DATA ((FAIRHD(I,J) /I=1,3) /J= 71, 80) /
			0.016155e-6, 10213.285546211, 1.331103168, 0.014751e-6, 1349.867409659, 4.308933301,
			0.015949e-6, -220.412642439, 4.005298270, 0.015974e-6, -2352.866153772, 6.145309371,
			0.014223e-6, 17789.845619785, 2.104551349, 0.017806e-6, 73.297125859, 3.475975097,
			0.013671e-6, -536.804512095, 5.971672571, 0.011942e-6, 8031.092263058, 2.053414715,
			0.014318e-6, 16730.463689596, 3.016058075, 0.012462e-6, 103.092774219, 1.737438797,
			// DATA ((FAIRHD(I,J) /I=1,3) /J= 81, 90) /
			0.010962e-6, 3.590428652, 2.196567739, 0.015078e-6, 19651.048481098, 3.969480770,
			0.010396e-6, 951.718406251, 5.717799605, 0.011707e-6, -4705.732307544, 2.654125618,
			0.010453e-6, 5863.591206116, 1.913704550, 0.012420e-6, 4690.479836359, 4.734090399,
			0.011847e-6, 5643.178563677, 5.489005403, 0.008610e-6, 3340.612426700, 3.661698944,
			0.011622e-6, 5120.601145584, 4.863931876, 0.010825e-6, 553.569402842, 0.842715011,
			// DATA ((FAIRHD(I,J) /I=1,3) /J= 91,100) /
			0.008666e-6, -135.065080035, 3.293406547, 0.009963e-6, 149.563197135, 4.870690598,
			0.009858e-6, 6309.374169791, 1.061816410, 0.007959e-6, 316.391869657, 2.465042647,
			0.010099e-6, 283.859318865, 1.942176992, 0.007147e-6, -242.728603974, 3.661486981,
			0.007505e-6, 5230.807466803, 4.920937029, 0.008323e-6, 11769.853693166, 1.229392026,
			0.007490e-6, -6256.777530192, 3.658444681, 0.009370e-6, 149854.400134205, 0.673880395,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=101,110) /
			0.007117e-6, 38.027672636, 5.294249518, 0.007857e-6, 12168.002696575, 0.525733528,
			0.007019e-6, 6206.809778716, 0.837688810, 0.006056e-6, 955.599741609, 4.194535082,
			0.008107e-6, 13367.972631107, 3.793235253, 0.006731e-6, 5650.292110678, 5.639906583,
			0.007332e-6, 36.648562930, 0.114858677, 0.006366e-6, 4164.311989613, 2.262081818,
			0.006858e-6, 5216.580372801, 0.642063318, 0.006919e-6, 6681.224853400, 6.018501522,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=111,120) /
			0.006826e-6, 7632.943259650, 3.458654112, 0.005308e-6, -1592.596013633, 2.500382359,
			0.005096e-6, 11371.704689758, 2.547107806, 0.004841e-6, 5333.900241022, 0.437078094,
			0.005582e-6, 5966.683980335, 2.246174308, 0.006304e-6, 11926.254413669, 2.512929171,
			0.006603e-6, 23581.258177318, 5.393136889, 0.005123e-6, -1.484472708, 2.999641028,
			0.004648e-6, 1589.072895284, 1.275847090, 0.005119e-6, 6438.496249426, 1.486539246,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=121,130) /
			0.004521e-6, 4292.330832950, 6.140635794, 0.005680e-6, 23013.539539587, 4.557814849,
			0.005488e-6, -3.455808046, 0.090675389, 0.004193e-6, 7234.794256242, 4.869091389,
			0.003742e-6, 7238.675591600, 4.691976180, 0.004148e-6, -110.206321219, 3.016173439,
			0.004553e-6, 11499.656222793, 5.554998314, 0.004892e-6, 5436.993015240, 1.475415597,
			0.004044e-6, 4732.030627343, 1.398784824, 0.004164e-6, 12491.370101415, 5.650931916,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=131,140) /
			0.004349e-6, 11513.883316794, 2.181745369, 0.003919e-6, 12528.018664345, 5.823319737,
			0.003129e-6, 6836.645252834, 0.003844094, 0.004080e-6, -7058.598461315, 3.690360123,
			0.003270e-6, 76.266071276, 1.517189902, 0.002954e-6, 6283.143160294, 4.447203799,
			0.002872e-6, 28.449187468, 1.158692983, 0.002881e-6, 735.876513532, 0.349250250,
			0.003279e-6, 5849.364112115, 4.893384368, 0.003625e-6, 6209.778724132, 1.473760578,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=141,150) /
			0.003074e-6, 949.175608970, 5.185878737, 0.002775e-6, 9917.696874510, 1.030026325,
			0.002646e-6, 10973.555686350, 3.918259169, 0.002575e-6, 25132.303399966, 6.109659023,
			0.003500e-6, 263.083923373, 1.892100742, 0.002740e-6, 18319.536584880, 4.320519510,
			0.002464e-6, 202.253395174, 4.698203059, 0.002409e-6, 2.542797281, 5.325009315,
			0.003354e-6, -90955.551694697, 1.942656623, 0.002296e-6, 6496.374945429, 5.061810696,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=151,160) /
			0.003002e-6, 6172.869528772, 2.797822767, 0.003202e-6, 27511.467873537, 0.531673101,
			0.002954e-6, -6283.008539689, 4.533471191, 0.002353e-6, 639.897286314, 3.734548088,
			0.002401e-6, 16200.772724501, 2.605547070, 0.003053e-6, 233141.314403759, 3.029030662,
			0.003024e-6, 83286.914269554, 2.355556099, 0.002863e-6, 17298.182327326, 5.240963796,
			0.002103e-6, -7079.373856808, 5.756641637, 0.002303e-6, 83996.847317911, 2.013686814,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=161,170) /
			0.002303e-6, 18073.704938650, 1.089100410, 0.002381e-6, 63.735898303, 0.759188178,
			0.002493e-6, 6386.168624210, 0.645026535, 0.002366e-6, 3.932153263, 6.215885448,
			0.002169e-6, 11015.106477335, 4.845297676, 0.002397e-6, 6243.458341645, 3.809290043,
			0.002183e-6, 1162.474704408, 6.179611691, 0.002353e-6, 6246.427287062, 4.781719760,
			0.002199e-6, -245.831646229, 5.956152284, 0.001729e-6, 3894.181829542, 1.264976635,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=171,180) /
			0.001896e-6, -3128.388765096, 4.914231596, 0.002085e-6, 35.164090221, 1.405158503,
			0.002024e-6, 14712.317116458, 2.752035928, 0.001737e-6, 6290.189396992, 5.280820144,
			0.002229e-6, 491.557929457, 1.571007057, 0.001602e-6, 14314.168113050, 4.203664806,
			0.002186e-6, 454.909366527, 1.402101526, 0.001897e-6, 22483.848574493, 4.167932508,
			0.001825e-6, -3738.761430108, 0.545828785, 0.001894e-6, 1052.268383188, 5.817167450,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=181,190) /
			0.001421e-6, 20.355319399, 2.419886601, 0.001408e-6, 10984.192351700, 2.732084787,
			0.001847e-6, 10873.986030480, 2.903477885, 0.001391e-6, -8635.942003763, 0.593891500,
			0.001388e-6, -7.046236698, 1.166145902, 0.001810e-6, -88860.057071188, 0.487355242,
			0.001288e-6, -1990.745017041, 3.913022880, 0.001297e-6, 23543.230504682, 3.063805171,
			0.001335e-6, -266.607041722, 3.995764039, 0.001376e-6, 10969.965257698, 5.152914309,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=191,200) /
			0.001745e-6, 244287.600007027, 3.626395673, 0.001649e-6, 31441.677569757, 1.952049260,
			0.001416e-6, 9225.539273283, 4.996408389, 0.001238e-6, 4804.209275927, 5.503379738,
			0.001472e-6, 4590.910180489, 4.164913291, 0.001169e-6, 6040.347246017, 5.841719038,
			0.001039e-6, 5540.085789459, 2.769753519, 0.001004e-6, -170.672870619, 0.755008103,
			0.001284e-6, 10575.406682942, 5.306538209, 0.001278e-6, 71.812653151, 4.713486491,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=201,210) /
			0.001321e-6, 18209.330263660, 2.624866359, 0.001297e-6, 21228.392023546, 0.382603541,
			0.000954e-6, 6282.095528923, 0.882213514, 0.001145e-6, 6058.731054289, 1.169483931,
			0.000979e-6, 5547.199336460, 5.448375984, 0.000987e-6, -6262.300454499, 2.656486959,
			0.001070e-6, -154717.609887482, 1.827624012, 0.000991e-6, 4701.116501708, 4.387001801,
			0.001155e-6, -14.227094002, 3.042700750, 0.001176e-6, 277.034993741, 3.335519004,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=211,220) /
			0.000890e-6, 13916.019109642, 5.601498297, 0.000884e-6, -1551.045222648, 1.088831705,
			0.000876e-6, 5017.508371365, 3.969902609, 0.000806e-6, 15110.466119866, 5.142876744,
			0.000773e-6, -4136.910433516, 0.022067765, 0.001077e-6, 175.166059800, 1.844913056,
			0.000954e-6, -6284.056171060, 0.968480906, 0.000737e-6, 5326.786694021, 4.923831588,
			0.000845e-6, -433.711737877, 4.749245231, 0.000819e-6, 8662.240323563, 5.991247817,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=221,230) /
			0.000852e-6, 199.072001436, 2.189604979, 0.000723e-6, 17256.631536341, 6.068719637,
			0.000940e-6, 6037.244203762, 6.197428148, 0.000885e-6, 11712.955318231, 3.280414875,
			0.000706e-6, 12559.038152982, 2.824848947, 0.000732e-6, 2379.164473572, 2.501813417,
			0.000764e-6, -6127.655450557, 2.236346329, 0.000908e-6, 131.541961686, 2.521257490,
			0.000907e-6, 35371.887265976, 3.370195967, 0.000673e-6, 1066.495477190, 3.876512374,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=231,240) /
			0.000814e-6, 17654.780539750, 4.627122566, 0.000630e-6, 36.027866677,0.156368499,
			0.000798e-6, 515.463871093, 5.151962502, 0.000798e-6, 148.078724426, 5.909225055,
			0.000806e-6, 309.278322656, 6.054064447, 0.000607e-6, -39.617508346, 2.839021623,
			0.000601e-6, 412.371096874, 3.984225404, 0.000646e-6, 11403.676995575, 3.852959484,
			0.000704e-6, 13521.751441591, 2.300991267, 0.000603e-6, -65147.619767937, 4.140083146,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=241,250) /
			0.000609e-6, 10177.257679534, 0.437122327, 0.000631e-6, 5767.611978898, 4.026532329,
			0.000576e-6, 11087.285125918, 4.760293101, 0.000674e-6, 14945.316173554, 6.270510511,
			0.000726e-6, 5429.879468239, 6.039606892, 0.000710e-6, 28766.924424484, 5.672617711,
			0.000647e-6, 11856.218651625, 3.397132627, 0.000678e-6, -5481.254918868, 6.249666675,
			0.000618e-6, 22003.914634870, 2.466427018, 0.000738e-6, 6134.997125565, 2.242668890,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=251,260) /
			0.000660e-6, 625.670192312, 5.864091907, 0.000694e-6, 3496.032826134, 2.668309141,
			0.000531e-6, 6489.261398429, 1.681888780, 0.000611e-6, -143571.324284214, 2.424978312,
			0.000575e-6, 12043.574281889, 4.216492400, 0.000553e-6, 12416.588502848, 4.772158039,
			0.000689e-6, 4686.889407707, 6.224271088, 0.000495e-6, 7342.457780181, 3.817285811,
			0.000567e-6, 3634.621024518, 1.649264690, 0.000515e-6, 18635.928454536, 3.945345892,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=261,270) /
			0.000486e-6, -323.505416657, 4.061673868, 0.000662e-6, 25158.601719765, 1.794058369,
			0.000509e-6, 846.082834751, 3.053874588, 0.000472e-6, -12569.674818332, 5.112133338,
			0.000461e-6, 6179.983075773, 0.513669325, 0.000641e-6, 83467.156352816, 3.210727723,
			0.000520e-6, 10344.295065386, 2.445597761, 0.000493e-6, 18422.629359098, 1.676939306,
			0.000478e-6, 1265.567478626, 5.487314569, 0.000472e-6, -18.159247265, 1.999707589,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=271,280) /
			0.000559e-6, 11190.377900137, 5.783236356, 0.000494e-6, 9623.688276691, 3.022645053,
			0.000463e-6, 5739.157790895, 1.411223013, 0.000432e-6, 16858.482532933, 1.179256434,
			0.000574e-6, 72140.628666286, 1.758191830, 0.000484e-6, 17267.268201691, 3.290589143,
			0.000550e-6, 4907.302050146, 0.864024298, 0.000399e-6, 14.977853527, 2.094441910,
			0.000491e-6, 224.344795702, 0.878372791, 0.000432e-6, 20426.571092422, 6.003829241,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=281,290) /
			0.000481e-6, 5749.452731634, 4.309591964, 0.000480e-6, 5757.317038160, 1.142348571,
			0.000485e-6, 6702.560493867, 0.210580917, 0.000426e-6, 6055.549660552, 4.274476529,
			0.000480e-6, 5959.570433334, 5.031351030, 0.000466e-6, 12562.628581634, 4.959581597,
			0.000520e-6, 39302.096962196, 4.788002889, 0.000458e-6, 12132.439962106, 1.880103788,
			0.000470e-6, 12029.347187887, 1.405611197, 0.000416e-6, -7477.522860216, 1.082356330,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=291,300) /
			0.000449e-6, 11609.862544012, 4.179989585, 0.000465e-6, 17253.041107690, 0.353496295,
			0.000362e-6, -4535.059436924, 1.583849576, 0.000383e-6, 21954.157609398, 3.747376371,
			0.000389e-6, 17.252277143, 1.395753179, 0.000331e-6, 18052.929543158, 0.566790582,
			0.000430e-6, 13517.870106233, 0.685827538, 0.000368e-6, -5756.908003246, 0.731374317,
			0.000330e-6, 10557.594160824, 3.710043680, 0.000332e-6, 20199.094959633, 1.652901407,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=301,310) /
			0.000384e-6, 11933.367960670, 5.827781531, 0.000387e-6, 10454.501386605, 2.541182564,
			0.000325e-6, 15671.081759407, 2.178850542, 0.000318e-6, 138.517496871, 2.253253037,
			0.000305e-6, 9388.005909415, 0.578340206, 0.000352e-6, 5749.861766548, 3.000297967,
			0.000311e-6, 6915.859589305, 1.693574249, 0.000297e-6, 24072.921469776, 1.997249392,
			0.000363e-6, -640.877607382, 5.071820966, 0.000323e-6, 12592.450019783, 1.072262823,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=311,320) /
			0.000341e-6, 12146.667056108, 4.700657997, 0.000290e-6, 9779.108676125, 1.812320441,
			0.000342e-6, 6132.028180148, 4.322238614, 0.000329e-6, 6268.848755990, 3.033827743,
			0.000374e-6, 17996.031168222, 3.388716544, 0.000285e-6, -533.214083444, 4.687313233,
			0.000338e-6, 6065.844601290, 0.877776108, 0.000276e-6, 24.298513841, 0.770299429,
			0.000336e-6, -2388.894020449, 5.353796034, 0.000290e-6, 3097.883822726, 4.075291557,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=321,330) /
			0.000318e-6, 709.933048357, 5.941207518, 0.000271e-6, 13095.842665077, 3.208912203,
			0.000331e-6, 6073.708907816, 4.007881169, 0.000292e-6, 742.990060533, 2.714333592,
			0.000362e-6, 29088.811415985, 3.215977013, 0.000280e-6, 12359.966151546, 0.710872502,
			0.000267e-6, 10440.274292604, 4.730108488, 0.000262e-6, 838.969287750, 1.327720272,
			0.000250e-6, 16496.361396202, 0.898769761, 0.000325e-6, 20597.243963041, 0.180044365,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=331,340) /
			0.000268e-6, 6148.010769956, 5.152666276, 0.000284e-6, 5636.065016677, 5.655385808,
			0.000301e-6, 6080.822454817, 2.135396205, 0.000294e-6, -377.373607916, 3.708784168,
			0.000236e-6, 2118.763860378, 1.733578756, 0.000234e-6, 5867.523359379, 5.575209112,
			0.000268e-6, -226858.238553767, 0.069432392, 0.000265e-6, 167283.761587465, 4.369302826,
			0.000280e-6, 28237.233459389, 5.304829118, 0.000292e-6, 12345.739057544, 4.096094132,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=341,350) /
			0.000223e-6, 19800.945956225, 3.069327406, 0.000301e-6, 43232.306658416, 6.205311188,
			0.000264e-6, 18875.525869774, 1.417263408, 0.000304e-6, -1823.175188677, 3.409035232,
			0.000301e-6, 109.945688789, 0.510922054, 0.000260e-6, 813.550283960, 2.389438934,
			0.000299e-6, 316428.228673312, 5.384595078, 0.000211e-6, 5756.566278634, 3.789392838,
			0.000209e-6, 5750.203491159, 1.661943545, 0.000240e-6, 12489.885628707, 5.684549045,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=351,360) /
			0.000216e-6, 6303.851245484, 3.862942261, 0.000203e-6, 1581.959348283, 5.549853589,
			0.000200e-6, 5642.198242609, 1.016115785, 0.000197e-6, -70.849445304, 4.690702525,
			0.000227e-6, 6287.008003254, 2.911891613, 0.000197e-6, 533.623118358, 1.048982898,
			0.000205e-6, -6279.485421340, 1.829362730, 0.000209e-6, -10988.808157535, 2.636140084,
			0.000208e-6, -227.526189440, 4.127883842, 0.000191e-6, 415.552490612, 4.401165650,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=361,370) /
			0.000190e-6, 29296.615389579, 4.175658539, 0.000264e-6, 66567.485864652, 4.601102551,
			0.000256e-6, -3646.350377354, 0.506364778, 0.000188e-6, 13119.721102825, 2.032195842,
			0.000185e-6, -209.366942175, 4.694756586, 0.000198e-6, 25934.124331089, 3.832703118,
			0.000195e-6, 4061.219215394, 3.308463427, 0.000234e-6, 5113.487598583, 1.716090661,
			0.000188e-6, 1478.866574064, 5.686865780, 0.000222e-6, 11823.161639450, 1.942386641,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=371,380) /
			0.000181e-6, 10770.893256262, 1.999482059, 0.000171e-6, 6546.159773364, 1.182807992,
			0.000206e-6, 70.328180442, 5.934076062, 0.000169e-6, 20995.392966449, 2.169080622,
			0.000191e-6, 10660.686935042, 5.405515999, 0.000228e-6, 33019.021112205, 4.656985514,
			0.000184e-6, -4933.208440333, 3.327476868, 0.000220e-6, -135.625325010, 1.765430262,
			0.000166e-6, 23141.558382925, 3.454132746, 0.000191e-6, 6144.558353121, 5.020393445,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=381,390) /
			0.000180e-6, 6084.003848555, 0.602182191, 0.000163e-6, 17782.732072784, 4.960593133,
			0.000225e-6, 16460.333529525, 2.596451817, 0.000222e-6, 5905.702242076, 3.731990323,
			0.000204e-6, 227.476132789, 5.636192701, 0.000159e-6, 16737.577236597, 3.600691544,
			0.000200e-6, 6805.653268085, 0.868220961, 0.000187e-6, 11919.140866668, 2.629456641,
			0.000161e-6, 127.471796607, 2.862574720, 0.000205e-6, 6286.666278643, 1.742882331,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=391,400) /
			0.000189e-6, 153.778810485, 4.812372643, 0.000168e-6, 16723.350142595, 0.027860588,
			0.000149e-6, 11720.068865232, 0.659721876, 0.000189e-6, 5237.921013804, 5.245313000,
			0.000143e-6, 6709.674040867, 4.317625647, 0.000146e-6, 4487.817406270, 4.815297007,
			0.000144e-6, -664.756045130, 5.381366880, 0.000175e-6, 5127.714692584, 4.728443327,
			0.000162e-6, 6254.626662524, 1.435132069, 0.000187e-6, 47162.516354635, 1.354371923,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=401,410) /
			0.000146e-6, 11080.171578918, 3.369695406, 0.000180e-6, -348.924420448, 2.490902145,
			0.000148e-6, 151.047669843, 3.799109588, 0.000157e-6, 6197.248551160, 1.284375887,
			0.000167e-6, 146.594251718, 0.759969109, 0.000133e-6, -5331.357443741, 5.409701889,
			0.000154e-6, 95.979227218, 3.366890614, 0.000148e-6, -6418.140930027, 3.384104996,
			0.000128e-6, -6525.804453965, 3.803419985, 0.000130e-6, 11293.470674356, 0.939039445,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=411,420) /
			0.000152e-6, -5729.506447149, 0.734117523, 0.000138e-6, 210.117701700, 2.564216078,
			0.000123e-6, 6066.595360816, 4.517099537, 0.000140e-6, 18451.078546566, 0.642049130,
			0.000126e-6, 11300.584221356, 3.485280663, 0.000119e-6, 10027.903195729, 3.217431161,
			0.000151e-6, 4274.518310832, 4.404359108, 0.000117e-6, 6072.958148291, 0.366324650,
			0.000165e-6, -7668.637425143, 4.298212528, 0.000117e-6, -6245.048177356, 5.379518958,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=421,430) /
			0.000130e-6, -5888.449964932, 4.527681115, 0.000121e-6, -543.918059096, 6.109429504,
			0.000162e-6, 9683.594581116, 5.720092446, 0.000141e-6, 6219.339951688, 0.679068671,
			0.000118e-6, 22743.409379516, 4.881123092, 0.000129e-6, 1692.165669502, 0.351407289,
			0.000126e-6, 5657.405657679, 5.146592349, 0.000114e-6, 728.762966531, 0.520791814,
			0.000120e-6, 52.596639600, 0.948516300, 0.000115e-6, 65.220371012, 3.504914846,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=431,440) /
			0.000126e-6, 5881.403728234, 5.577502482, 0.000158e-6, 163096.180360983, 2.957128968,
			0.000134e-6, 12341.806904281, 2.598576764, 0.000151e-6, 16627.370915377, 3.985702050,
			0.000109e-6, 1368.660252845, 0.014730471, 0.000131e-6, 6211.263196841, 0.085077024,
			0.000146e-6, 5792.741760812, 0.708426604, 0.000146e-6, -77.750543984, 3.121576600,
			0.000107e-6, 5341.013788022, 0.288231904, 0.000138e-6, 6281.591377283, 2.797450317,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=441,450) /
			0.000113e-6, -6277.552925684, 2.788904128, 0.000115e-6, -525.758811831, 5.895222200,
			0.000138e-6, 6016.468808270, 6.096188999, 0.000139e-6, 23539.707386333, 2.028195445,
			0.000146e-6, -4176.041342449, 4.660008502, 0.000107e-6, 16062.184526117, 4.066520001,
			0.000142e-6, 83783.548222473, 2.936315115, 0.000128e-6, 9380.959672717, 3.223844306,
			0.000135e-6, 6205.325306007, 1.638054048, 0.000101e-6, 2699.734819318, 5.481603249,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=451,460) /
			0.000104e-6, -568.821874027, 2.205734493, 0.000103e-6, 6321.103522627, 2.440421099,
			0.000119e-6, 6321.208885629, 2.547496264, 0.000138e-6, 1975.492545856, 2.314608466,
			0.000121e-6, 137.033024162, 4.539108237, 0.000123e-6, 19402.796952817, 4.538074405,
			0.000119e-6, 22805.735565994, 2.869040566, 0.000133e-6, 64471.991241142, 6.056405489,
			0.000129e-6, -85.827298831, 2.540635083, 0.000131e-6, 13613.804277336, 4.005732868,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=461,470) /
			0.000104e-6, 9814.604100291, 1.959967212, 0.000112e-6, 16097.679950283, 3.589026260,
			0.000123e-6, 2107.034507542, 1.728627253, 0.000121e-6, 36949.230808424, 6.072332087,
			0.000108e-6, -12539.853380183, 3.716133846, 0.000113e-6, -7875.671863624, 2.725771122,
			0.000109e-6, 4171.425536614, 4.033338079, 0.000101e-6, 6247.911759770, 3.441347021,
			0.000113e-6, 7330.728427345, 0.656372122, 0.000113e-6, 51092.726050855, 2.791483066,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=471,480) /
			0.000106e-6, 5621.842923210, 1.815323326, 0.000101e-6, 111.430161497, 5.711033677,
			0.000103e-6, 909.818733055, 2.812745443, 0.000101e-6, 1790.642637886, 1.965746028,
			102.156724e-6, 6283.075849991, 4.249032005, 1.706807e-6, 12566.151699983, 4.205904248,
			0.269668e-6, 213.299095438, 3.400290479, 0.265919e-6, 529.690965095, 5.836047367,
			0.210568e-6, -3.523118349, 6.262738348, 0.077996e-6, 5223.693919802, 4.670344204,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=481,490) /
			0.054764e-6, 1577.343542448, 4.534800170, 0.059146e-6, 26.298319800, 1.083044735,
			0.034420e-6, -398.149003408, 5.980077351, 0.032088e-6, 18849.227549974, 4.162913471,
			0.033595e-6, 5507.553238667, 5.980162321, 0.029198e-6, 5856.477659115, 0.623811863,
			0.027764e-6, 155.420399434, 3.745318113, 0.025190e-6, 5746.271337896, 2.980330535,
			0.022997e-6, -796.298006816, 1.174411803, 0.024976e-6, 5760.498431898, 2.467913690,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=491,500) /
			0.021774e-6, 206.185548437, 3.854787540, 0.017925e-6, -775.522611324, 1.092065955,
			0.013794e-6, 426.598190876, 2.699831988, 0.013276e-6, 6062.663207553, 5.845801920,
			0.011774e-6, 12036.460734888, 2.292832062, 0.012869e-6, 6076.890301554, 5.333425680,
			0.012152e-6, 1059.381930189, 6.222874454, 0.011081e-6, -7.113547001, 5.154724984,
			0.010143e-6, 4694.002954708, 4.044013795, 0.009357e-6, 5486.777843175, 3.416081409,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=501,510) /
			0.010084e-6, 522.577418094, 0.749320262, 0.008587e-6, 10977.078804699, 2.777152598,
			0.008628e-6, 6275.962302991, 4.562060226, 0.008158e-6, -220.412642439, 5.806891533,
			0.007746e-6, 2544.314419883, 1.603197066, 0.007670e-6, 2146.165416475, 3.000200440,
			0.007098e-6, 74.781598567, 0.443725817, 0.006180e-6, -536.804512095, 1.302642751,
			0.005818e-6, 5088.628839767, 4.827723531, 0.004945e-6, -6286.598968340, 0.268305170,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=511,520) /
			0.004774e-6, 1349.867409659, 5.808636673, 0.004687e-6, -242.728603974, 5.154890570,
			0.006089e-6, 1748.016413067, 4.403765209, 0.005975e-6, -1194.447010225, 2.583472591,
			0.004229e-6, 951.718406251, 0.931172179, 0.005264e-6, 553.569402842, 2.336107252,
			0.003049e-6, 5643.178563677, 1.362634430, 0.002974e-6, 6812.766815086, 1.583012668,
			0.003403e-6, -2352.866153772, 2.552189886, 0.003030e-6, 419.484643875, 5.286473844,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=521,530) /
			0.003210e-6, -7.046236698, 1.863796539, 0.003058e-6, 9437.762934887, 4.226420633,
			0.002589e-6, 12352.852604545, 1.991935820, 0.002927e-6, 5216.580372801, 2.319951253,
			0.002425e-6, 5230.807466803, 3.084752833, 0.002656e-6, 3154.687084896, 2.487447866,
			0.002445e-6, 10447.387839604, 2.347139160, 0.002990e-6, 4690.479836359, 6.235872050,
			0.002890e-6, 5863.591206116, 0.095197563, 0.002498e-6, 6438.496249426, 2.994779800,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=531,540) /
			0.001889e-6, 8031.092263058, 3.569003717, 0.002567e-6, 801.820931124, 3.425611498,
			0.001803e-6, -71430.695617928, 2.192295512, 0.001782e-6, 3.932153263, 5.180433689,
			0.001694e-6, -4705.732307544, 4.641779174, 0.001704e-6, -1592.596013633, 3.997097652,
			0.001735e-6, 5849.364112115, 0.417558428, 0.001643e-6, 8429.241266467, 2.180619584,
			0.001680e-6, 38.133035638, 4.164529426, 0.002045e-6, 7084.896781115, 0.526323854,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=541,550) /
			0.001458e-6, 4292.330832950, 1.356098141, 0.001437e-6, 20.355319399, 3.895439360,
			0.001738e-6, 6279.552731642, 0.087484036, 0.001367e-6, 14143.495242431, 3.987576591,
			0.001344e-6, 7234.794256242, 0.090454338, 0.001438e-6, 11499.656222793, 0.974387904,
			0.001257e-6, 6836.645252834, 1.509069366, 0.001358e-6, 11513.883316794, 0.495572260,
			0.001628e-6, 7632.943259650, 4.968445721, 0.001169e-6, 103.092774219, 2.838496795,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=551,560) /
			0.001162e-6, 4164.311989613, 3.408387778, 0.001092e-6, 6069.776754553, 3.617942651,
			0.001008e-6, 17789.845619785, 0.286350174, 0.001008e-6, 639.897286314, 1.610762073,
			0.000918e-6, 10213.285546211, 5.532798067, 0.001011e-6, -6256.777530192, 0.661826484,
			0.000753e-6, 16730.463689596, 3.905030235, 0.000737e-6, 11926.254413669, 4.641956361,
			0.000694e-6, 3340.612426700, 2.111120332, 0.000701e-6, 3894.181829542, 2.760823491,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=561,570) /
			0.000689e-6, -135.065080035, 4.768800780, 0.000700e-6, 13367.972631107, 5.760439898,
			0.000664e-6, 6040.347246017, 1.051215840, 0.000654e-6, 5650.292110678, 4.911332503,
			0.000788e-6, 6681.224853400, 4.699648011, 0.000628e-6, 5333.900241022, 5.024608847,
			0.000755e-6, -110.206321219, 4.370971253, 0.000628e-6, 6290.189396992, 3.660478857,
			0.000635e-6, 25132.303399966, 4.121051532, 0.000534e-6, 5966.683980335, 1.173284524,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=571,580) /
			0.000543e-6, -433.711737877, 0.345585464, 0.000517e-6, -1990.745017041, 5.414571768,
			0.000504e-6, 5767.611978898, 2.328281115, 0.000485e-6, 5753.384884897, 1.685874771,
			0.000463e-6, 7860.419392439, 5.297703006, 0.000604e-6, 515.463871093, 0.591998446,
			0.000443e-6, 12168.002696575, 4.830881244, 0.000570e-6, 199.072001436, 3.899190272,
			0.000465e-6, 10969.965257698, 0.476681802, 0.000424e-6, -7079.373856808, 1.112242763,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=581,590) /
			0.000427e-6, 735.876513532, 1.994214480, 0.000478e-6, -6127.655450557, 3.778025483,
			0.000414e-6, 10973.555686350, 5.441088327, 0.000512e-6, 1589.072895284, 0.107123853,
			0.000378e-6, 10984.192351700, 0.915087231, 0.000402e-6, 11371.704689758, 4.107281715,
			0.000453e-6, 9917.696874510, 1.917490952, 0.000395e-6, 149.563197135, 2.763124165,
			0.000371e-6, 5739.157790895, 3.112111866, 0.000350e-6, 11790.629088659, 0.440639857,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=591,600) /
			0.000356e-6, 6133.512652857, 5.444568842, 0.000344e-6, 412.371096874, 5.676832684,
			0.000383e-6, 955.599741609, 5.559734846, 0.000333e-6, 6496.374945429, 0.261537984,
			0.000340e-6, 6055.549660552, 5.975534987, 0.000334e-6, 1066.495477190, 2.335063907,
			0.000399e-6, 11506.769769794, 5.321230910, 0.000314e-6, 18319.536584880, 2.313312404,
			0.000424e-6, 1052.268383188, 1.211961766, 0.000307e-6, 63.735898303, 3.169551388,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=601,610) /
			0.000329e-6, 29.821438149, 6.106912080, 0.000357e-6, 6309.374169791, 4.223760346,
			0.000312e-6, -3738.761430108, 2.180556645, 0.000301e-6, 309.278322656, 1.499984572,
			0.000268e-6, 12043.574281889, 2.447520648, 0.000257e-6, 12491.370101415, 3.662331761,
			0.000290e-6, 625.670192312, 1.272834584, 0.000256e-6, 5429.879468239, 1.913426912,
			0.000339e-6, 3496.032826134, 4.165930011, 0.000283e-6, 3930.209696220, 4.325565754,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=611,620) /
			0.000241e-6, 12528.018664345, 3.832324536, 0.000304e-6, 4686.889407707, 1.612348468,
			0.000259e-6, 16200.772724501, 3.470173146, 0.000238e-6, 12139.553509107, 1.147977842,
			0.000236e-6, 6172.869528772, 3.776271728, 0.000296e-6, -7058.598461315, 0.460368852,
			0.000306e-6, 10575.406682942, 0.554749016, 0.000251e-6, 17298.182327326, 0.834332510,
			0.000290e-6, 4732.030627343, 4.759564091, 0.000261e-6, 5884.926846583, 0.298259862,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=621,630) /
			0.000249e-6, 5547.199336460, 3.749366406, 0.000213e-6, 11712.955318231, 5.415666119,
			0.000223e-6, 4701.116501708, 2.703203558, 0.000268e-6, -640.877607382, 0.283670793,
			0.000209e-6, 5636.065016677, 1.238477199, 0.000193e-6, 10177.257679534, 1.943251340,
			0.000182e-6, 6283.143160294, 2.456157599, 0.000184e-6, -227.526189440, 5.888038582,
			0.000182e-6, -6283.008539689, 0.241332086, 0.000228e-6, -6284.056171060, 2.657323816,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=631,640) /
			0.000166e-6, 7238.675591600, 5.930629110, 0.000167e-6, 3097.883822726, 5.570955333,
			0.000159e-6, -323.505416657, 5.786670700, 0.000154e-6, -4136.910433516, 1.517805532,
			0.000176e-6, 12029.347187887, 3.139266834, 0.000167e-6, 12132.439962106, 3.556352289,
			0.000153e-6, 202.253395174, 1.463313961, 0.000157e-6, 17267.268201691, 1.586837396,
			0.000142e-6, 83996.847317911, 0.022670115, 0.000152e-6, 17260.154654690, 0.708528947,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=641,650) /
			0.000144e-6, 6084.003848555, 5.187075177, 0.000135e-6, 5756.566278634, 1.993229262,
			0.000134e-6, 5750.203491159, 3.457197134, 0.000144e-6, 5326.786694021, 6.066193291,
			0.000160e-6, 11015.106477335, 1.710431974, 0.000133e-6, 3634.621024518, 2.836451652,
			0.000134e-6, 18073.704938650, 5.453106665, 0.000134e-6, 1162.474704408, 5.326898811,
			0.000128e-6, 5642.198242609, 2.511652591, 0.000160e-6, 632.783739313, 5.628785365,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=651,660) /
			0.000132e-6, 13916.019109642, 0.819294053, 0.000122e-6, 14314.168113050, 5.677408071,
			0.000125e-6, 12359.966151546, 5.251984735, 0.000121e-6, 5749.452731634, 2.210924603,
			0.000136e-6, -245.831646229, 1.646502367, 0.000120e-6, 5757.317038160, 3.240883049,
			0.000134e-6, 12146.667056108, 3.059480037, 0.000137e-6, 6206.809778716, 1.867105418,
			0.000141e-6, 17253.041107690, 2.069217456, 0.000129e-6, -7477.522860216, 2.781469314,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=661,670) /
			0.000116e-6, 5540.085789459, 4.281176991, 0.000116e-6, 9779.108676125, 3.320925381,
			0.000129e-6, 5237.921013804, 3.497704076, 0.000113e-6, 5959.570433334, 0.983210840,
			0.000122e-6, 6282.095528923, 2.674938860, 0.000140e-6, -11.045700264, 4.957936982,
			0.000108e-6, 23543.230504682, 1.390113589, 0.000106e-6, -12569.674818332, 0.429631317,
			0.000110e-6, -266.607041722, 5.501340197, 0.000115e-6, 12559.038152982, 4.691456618,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=671,680) /
			0.000134e-6, -2388.894020449, 0.577313584, 0.000109e-6, 10440.274292604, 6.218148717,
			0.000102e-6, -543.918059096, 1.477842615, 0.000108e-6, 21228.392023546, 2.237753948,
			0.000101e-6, -4535.059436924, 3.100492232, 0.000103e-6, 76.266071276, 5.594294322,
			0.000104e-6, 949.175608970, 5.674287810, 0.000101e-6, 13517.870106233, 2.196632348,
			0.000100e-6, 11933.367960670, 4.056084160, 4.322990e-6, 6283.075849991, 2.642893748,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=681,690) /
			0.406495e-6, 0.000000000, 4.712388980, 0.122605e-6, 12566.151699983, 2.438140634,
			0.019476e-6, 213.299095438, 1.642186981, 0.016916e-6, 529.690965095, 4.510959344,
			0.013374e-6, -3.523118349, 1.502210314, 0.008042e-6, 26.298319800, 0.478549024,
			0.007824e-6, 155.420399434, 5.254710405, 0.004894e-6, 5746.271337896, 4.683210850,
			0.004875e-6, 5760.498431898, 0.759507698, 0.004416e-6, 5223.693919802, 6.028853166,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=691,700) /
			0.004088e-6, -7.113547001, 0.060926389, 0.004433e-6, 77713.771467920, 3.627734103,
			0.003277e-6, 18849.227549974, 2.327912542, 0.002703e-6, 6062.663207553, 1.271941729,
			0.003435e-6, -775.522611324, 0.747446224, 0.002618e-6, 6076.890301554, 3.633715689,
			0.003146e-6, 206.185548437, 5.647874613, 0.002544e-6, 1577.343542448, 6.232904270,
			0.002218e-6, -220.412642439, 1.309509946, 0.002197e-6, 5856.477659115, 2.407212349,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=701,710) /
			0.002897e-6, 5753.384884897, 5.863842246, 0.001766e-6, 426.598190876, 0.754113147,
			0.001738e-6, -796.298006816, 2.714942671, 0.001695e-6, 522.577418094, 2.629369842,
			0.001584e-6, 5507.553238667, 1.341138229, 0.001503e-6, -242.728603974, 0.377699736,
			0.001552e-6, -536.804512095, 2.904684667, 0.001370e-6, -398.149003408, 1.265599125,
			0.001889e-6, -5573.142801634, 4.413514859, 0.001722e-6, 6069.776754553, 2.445966339,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=711,720) /
			0.001124e-6, 1059.381930189, 5.041799657, 0.001258e-6, 553.569402842, 3.849557278,
			0.000831e-6, 951.718406251, 2.471094709, 0.000767e-6, 4694.002954708, 5.363125422,
			0.000756e-6, 1349.867409659, 1.046195744, 0.000775e-6, -11.045700264, 0.245548001,
			0.000597e-6, 2146.165416475, 4.543268798, 0.000568e-6, 5216.580372801, 4.178853144,
			0.000711e-6, 1748.016413067, 5.934271972, 0.000499e-6, 12036.460734888, 0.624434410,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=721,730) /
			0.000671e-6, -1194.447010225, 4.136047594, 0.000488e-6, 5849.364112115, 2.209679987,
			0.000621e-6, 6438.496249426, 4.518860804, 0.000495e-6, -6286.598968340, 1.868201275,
			0.000456e-6, 5230.807466803, 1.271231591, 0.000451e-6, 5088.628839767, 0.084060889,
			0.000435e-6, 5643.178563677, 3.324456609, 0.000387e-6, 10977.078804699, 4.052488477,
			0.000547e-6, 161000.685737473, 2.841633844, 0.000522e-6, 3154.687084896, 2.171979966,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=731,740) /
			0.000375e-6, 5486.777843175, 4.983027306, 0.000421e-6, 5863.591206116, 4.546432249,
			0.000439e-6, 7084.896781115, 0.522967921, 0.000309e-6, 2544.314419883, 3.172606705,
			0.000347e-6, 4690.479836359, 1.479586566, 0.000317e-6, 801.820931124, 3.553088096,
			0.000262e-6, 419.484643875, 0.606635550, 0.000248e-6, 6836.645252834, 3.014082064,
			0.000245e-6, -1592.596013633, 5.519526220, 0.000225e-6, 4292.330832950, 2.877956536,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=741,750) /
			0.000214e-6, 7234.794256242, 1.605227587, 0.000205e-6, 5767.611978898, 0.625804796,
			0.000180e-6, 10447.387839604, 3.499954526, 0.000229e-6, 199.072001436, 5.632304604,
			0.000214e-6, 639.897286314, 5.960227667, 0.000175e-6, -433.711737877, 2.162417992,
			0.000209e-6, 515.463871093, 2.322150893, 0.000173e-6, 6040.347246017, 2.556183691,
			0.000184e-6, 6309.374169791, 4.732296790, 0.000227e-6, 149854.400134205, 5.385812217,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=751,760) /
			0.000154e-6, 8031.092263058, 5.120720920, 0.000151e-6, 5739.157790895, 4.815000443,
			0.000197e-6, 7632.943259650, 0.222827271, 0.000197e-6, 74.781598567, 3.910456770,
			0.000138e-6, 6055.549660552, 1.397484253, 0.000149e-6, -6127.655450557, 5.333727496,
			0.000137e-6, 3894.181829542, 4.281749907, 0.000135e-6, 9437.762934887, 5.979971885,
			0.000139e-6, -2352.866153772, 4.715630782, 0.000142e-6, 6812.766815086, 0.513330157,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=761,770) /
			0.000120e-6, -4705.732307544, 0.194160689, 0.000131e-6, -71430.695617928, 0.000379226,
			0.000124e-6, 6279.552731642, 2.122264908, 0.000108e-6, -6256.777530192, 0.883445696,
			0.143388e-6, 6283.075849991, 1.131453581, 0.006671e-6, 12566.151699983, 0.775148887,
			0.001480e-6, 155.420399434, 0.480016880, 0.000934e-6, 213.299095438, 6.144453084,
			0.000795e-6, 529.690965095, 2.941595619, 0.000673e-6, 5746.271337896, 0.120415406,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=771,780) /
			0.000672e-6, 5760.498431898, 5.317009738, 0.000389e-6, -220.412642439, 3.090323467,
			0.000373e-6, 6062.663207553, 3.003551964, 0.000360e-6, 6076.890301554, 1.918913041,
			0.000316e-6, -21.340641002, 5.545798121, 0.000315e-6, -242.728603974, 1.884932563,
			0.000278e-6, 206.185548437, 1.266254859, 0.000238e-6, -536.804512095, 4.532664830,
			0.000185e-6, 522.577418094, 4.578313856, 0.000245e-6, 18849.227549974, 0.587467082,
			// DATA ((FAIRHD(I,J) /I=1,3) /J=781,787) /
			0.000180e-6, 426.598190876, 5.151178553, 0.000200e-6, 553.569402842, 5.355983739,
			0.000141e-6, 5223.693919802, 1.336556009, 0.000104e-6, 5856.477659115, 4.239842759,
			0.003826e-6, 6283.075849991, 5.705257275, 0.000303e-6, 12566.151699983, 5.407132842,
			0.000209e-6, 155.420399434, 1.989815753, };


	/**
	 * Updates the leap seconds and TT-UT1 from the data contained in external
	 * or JPARSEC internal files. This method is automatically called by
	 * JPARSEC when an attemp is made to perform some time scale transformation.
	 * External applications do not need to call this method unless a change
	 * in the parameters occur during execution time.
	 * @param leapSeconds The contents of an external leap seconds file in the exactly
	 * same format of the leapSeconds.txt file used by JPARSEC. Set to null to use
	 * the JPARSEC one.
	 * @param tt_ut1 The contents of an external TTminusUT1 file in the exactly
	 * same format (or compatible) of the TTminusUT1.txt file used by JPARSEC. Set to null
	 * to use the JPARSEC one.
	 * @throws JPARSECException If an error occurs.
	 */
	public static void updateLeapSecondsAndDT(String leapSeconds[], String tt_ut1[])
	throws JPARSECException {
		try {
			if (leapSeconds == null) {
				String jarpath = "jparsec/time/leapSeconds.txt";
				leapSeconds = DataSet.arrayListToStringArray(ReadFile.readResource(jarpath));
			}
			DATA.leap_seconds = new double[leapSeconds.length-1][3];
			for (int i=1; i<leapSeconds.length; i++)
			{
				DATA.leap_seconds[i-1][0] = DataSet.parseDouble(FileIO.getField(1, leapSeconds[i], " ", true));
				DATA.leap_seconds[i-1][1] = DataSet.parseDouble(FileIO.getField(2, leapSeconds[i], " ", true));
				DATA.leap_seconds[i-1][2] = DataSet.parseDouble(FileIO.getField(3, leapSeconds[i], " ", true));
			}
			if (tt_ut1 == null) {
				String jarpath = "jparsec/time/TTminusUT1.txt";
				tt_ut1 = DataSet.arrayListToStringArray(ReadFile.readResource(jarpath));
			}
			ArrayList<double[]> dt = new ArrayList<double[]>();
			DATA.dt_initJD = DATA.dt_endJD = -1;
			int year = 0, month = 0, day = 0;
			double val = 0.0;
			for (int i=0; i<tt_ut1.length; i++)
			{
				if (!tt_ut1[i].startsWith("!")) {
					int n = FileIO.getNumberOfFields(tt_ut1[i], " ", true);
					if (n == 4) {
						year = Integer.parseInt(FileIO.getField(1, tt_ut1[i], " ", true));
						month = Integer.parseInt(FileIO.getField(2, tt_ut1[i], " ", true));
						day = Integer.parseInt(FileIO.getField(3, tt_ut1[i], " ", true));
						val = DataSet.parseDouble(FileIO.getField(4, tt_ut1[i], " ", true));
					} else {
						double yr = DataSet.parseDouble(FileIO.getField(1, tt_ut1[i], " ", true));
						year = (int) yr;
						month = day = 1;
						if (year != yr) month = 1 + (int) ((yr - year) * 12.0 + 0.5);
						val = DataSet.parseDouble(FileIO.getField(2, tt_ut1[i], " ", true));
					}
					if (year <= 0) year --;
					AstroDate astro = new AstroDate(year, month, day);
					double jd = astro.jd();
					if (jd < DATA.dt_initJD || DATA.dt_initJD == -1) DATA.dt_initJD = jd;
					if (jd > DATA.dt_endJD || DATA.dt_endJD == -1) DATA.dt_endJD = jd;
					dt.add(new double[] {jd, val});
				}
			}
			double[] jd = new double[dt.size()];
			double[] v = new double[dt.size()];
			for (int i=0; i<dt.size(); i++) {
				double d[] = dt.get(i);
				jd[i] = d[0];
				v[i] = d[1];
			}
			DATA.interp = new Interpolation(jd, v, true);

		} catch (Exception e)
		{
			if (e instanceof JPARSECException) throw (JPARSECException) e;
			throw new JPARSECException("cannot update/read leap seconds file.", e);
		}
	}
}

final class DATA
{
	// Reference dates (JD) and drift rates (s/day), pre leap seconds
	// No update necessary. From SOFA library.
	static double[][] drift = {
		new double[] { 2437300.0, 0.001296 }, new double[] { 2437300.0, 0.001296 },
		new double[] { 2437300.0, 0.001296 }, new double[] { 2437665.0, 0.0011232 },
		new double[] { 2437665.0, 0.0011232 }, new double[] { 2438761.0, 0.001296 },
		new double[] { 2438761.0, 0.001296 }, new double[] { 2438761.0, 0.001296 },
		new double[] { 2438761.0, 0.001296 }, new double[] { 2438761.0, 0.001296 },
		new double[] { 2438761.0, 0.001296 }, new double[] { 2438761.0, 0.001296 },
		new double[] { 2439126.0, 0.002592 }, new double[] { 2439126.0, 0.002592 } };

	// Dates of leap seconds (year, month, seconds TAI-UTC)
	// To be updated whenever a new leap seconds is introduced.
	// From SOFA library.
	static double[][] leap_seconds = null;

	/*
	 * Table of difference TT-UT1 for a given set of dates.
	 * First value is JD (UT), second one is TT-UT1 in seconds.
	 * Values contained in TTminusUT1.txt, to be updated
	 * regularly (see Astronomical Almanac, page K8).
	 */
	static Interpolation interp = null; //ArrayList<double[]> dt = null;

	static double dt_initJD = -1, dt_endJD = -1;
}
