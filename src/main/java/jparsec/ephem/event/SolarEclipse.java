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
package jparsec.ephem.event;

import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.EphemerisElement.ALGORITHM;
import jparsec.ephem.Target.TARGET;
import jparsec.ephem.planets.EphemElement;
import jparsec.graph.chartRendering.RenderEclipse;
import jparsec.math.Constant;
import jparsec.math.FastMath;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeScale;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;
import jparsec.time.TimeElement.SCALE;
import jparsec.util.JPARSECException;
import jparsec.util.Translate;

/**
 * Obtain circumstances of solar eclipses. This class implements a
 * 'brute-force' method that does not require Bessel elements and is
 * independent from the ephemerides theory. It is quite fast if used
 * correctly. The effect of the mountains on lunar limb is ignored.
 * <P>
 * One advantage of this pure geometric approach is the possibility 
 * of calculating solar eclipses from other satellites (not the Moon).
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class SolarEclipse
{
	private TARGET targetBody = null;
	
	/**
	 * Checks for events.
	 * 
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris properties.
	 * @return A set of true or false values for the events.
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	private boolean[] checkEclipse(TimeElement time, ObserverElement obs, EphemerisElement new_eph)
			throws JPARSECException
	{
		//new_eph.isTopocentric = true;
		new_eph.targetBody = targetBody;
		EphemerisElement.ALGORITHM alg = new_eph.algorithm;
		if (alg == ALGORITHM.NATURAL_SATELLITE) alg = ALGORITHM.MOSHIER;
		if (targetBody != TARGET.Moon) new_eph.algorithm = ALGORITHM.NATURAL_SATELLITE;
		EphemElement ephem_moon = Ephem.getEphemeris(time, obs, new_eph, false);
		
		new_eph.targetBody = TARGET.SUN;
		new_eph.algorithm = alg;
		EphemElement ephem = Ephem.getEphemeris(time, obs, new_eph, false);
		LocationElement sun_loc = new LocationElement(ephem.rightAscension, ephem.declination, 1.0);

		LocationElement moon_loc = new LocationElement(ephem_moon.rightAscension, ephem_moon.declination, 1.0);
		double dist = LocationElement.getAngularDistance(moon_loc, sun_loc);
		double pa = 3.0 * Constant.PI_OVER_TWO - LocationElement.getPositionAngle(sun_loc, moon_loc) - ephem_moon.positionAngleOfAxis;

		double m_x = Math.sin(pa) / ephem_moon.angularRadius;
		double m_y = Math.cos(pa) / ephem_moon.angularRadius;
		double m_r = 1.0 / FastMath.hypot(m_x, m_y);
		double s_r = ephem.angularRadius;

		boolean inside_shadow = false, totality = false;
		if (dist <= (s_r + m_r))
		{
			inside_shadow = true;
		} else
		{
			if (targetBody == TARGET.Moon) {
				recommendedTimeOffsetToNextEvent = Math.abs(dist - (s_r + ephem_moon.angularRadius)) / SolarEclipse.moonMeanOrbitalRate;
				recommendedTimeOffsetToNextEventAfterTotality = recommendedTimeOffsetToNextEvent;
			}
		}

		if (((dist + m_r) <= s_r && (m_r < s_r)) || ((dist + s_r) <= m_r && (m_r > s_r)))
		{
			totality = true;
			if (type == ECLIPSE_TYPE.NO_ECLIPSE || type == ECLIPSE_TYPE.PARTIAL)
			{
				if (ephem_moon.angularRadius > s_r)
				{
					type = ECLIPSE_TYPE.TOTAL;
				} else
				{
					type = ECLIPSE_TYPE.ANNULAR;
				}
			}
		} else
		{
			if (inside_shadow)
			{
				if (type == ECLIPSE_TYPE.NO_ECLIPSE)
					type = ECLIPSE_TYPE.PARTIAL;
				if (targetBody == TARGET.Moon) {
					if (m_r > s_r)
					{
						recommendedTimeOffsetToNextEvent = Math.abs(dist + (s_r - m_r)) / SolarEclipse.moonMeanOrbitalRate;
						recommendedTimeOffsetToNextEventAfterTotality = Math.abs(dist - (s_r + m_r)) / SolarEclipse.moonMeanOrbitalRate;
					} else
					{
						recommendedTimeOffsetToNextEvent = Math.abs(dist - (s_r - m_r)) / SolarEclipse.moonMeanOrbitalRate;
						recommendedTimeOffsetToNextEventAfterTotality = Math.abs(dist - (s_r + m_r)) / SolarEclipse.moonMeanOrbitalRate;
					}
				}
			}
		}

		boolean out[] = new boolean[] { inside_shadow, totality };

		return out;
	}

	/**
	 * Sets the desired accuracy of the iterative method in seconds for
	 * eclipses produced by any moon besides the Moon. In case of Lunar
	 * eclipses or input values <= 0 this method will have no effect.
	 * @param s Accuracy in seconds, must be > 0.
	 */
	private void setAccuracy(double s)  {
		if (s > 0 && targetBody != TARGET.Moon) {
			recommendedTimeOffsetToNextEvent = s / Constant.SECONDS_PER_DAY;
			recommendedTimeOffsetToNextEventAfterTotality = s / Constant.SECONDS_PER_DAY;
		}
	}
	private double recommendedTimeOffsetToNextEvent = 1.0 / Constant.SECONDS_PER_DAY;
	private double recommendedTimeOffsetToNextEventAfterTotality = 1.0 / Constant.SECONDS_PER_DAY;
	private static double moonMeanOrbitalRate = 2.0 * Math.PI / 29.5;

	private double jdMax;
	private double[] events;
	
	/**
	 * The set of eclipse types for both solar and lunar eclipses.
	 */
	public static enum ECLIPSE_TYPE {
		/** Constant ID for a total solar/lunar eclipse. */
		TOTAL,
		/** Constant ID for a partial solar/lunar eclipse. */
		PARTIAL,
		/** Constant ID for an annular solar eclipse. */
		ANNULAR,
		/**
		 * Constant ID for an inexistent solar eclipse. In practice, the algorithm
		 * finds the next eclipse, so this type will never exist as output. This value
		 * is used only internally.
		 */
		NO_ECLIPSE
	};
	
	/**
	 * Eclipse type.
	 */
	public ECLIPSE_TYPE type = ECLIPSE_TYPE.NO_ECLIPSE;

	/**
	 * Obtain events for the next solar eclipse in TDB. Input time should be
	 * Immediately before, or the day before a certain solar eclipse starts
	 * (shadow ingress). Precision in the results is up to 0.5 seconds.
	 * Comparisons with NASA published values (calculations by Fred Spenak) show
	 * a mean difference of 1 second. The origin of this minimum discrepancy is 
	 * probably due to the fact that here the difference between the Moon center 
	 * of mass and its geometric center is not corrected. Otherwise, the irregular 
	 * limb profile of the Moon (not taken into account here nor in Spenak's 
	 * calculations) produces more effects than this negligible discrepancy.
	 * 
	 * Events are:<P>
	 * - Shadow ingress.<P>
	 * - Shadow total/annular ingress.<P>
	 * - Shadow total/annular egress.<P>
	 * - Shadow egress.
	 * 
	 * Eclipse type (total, partial, annular) is set in static variable type.
	 * 
	 * If you are calculation eclipses for current dates the Moshier algorithm will give a
	 * good performance. For better precision in ancient times use ELP2000, although internal
	 * precision of the algorithm will be reduced to 1 second instead of 0.5s to improve
	 * performance.
	 * 
	 * Moon/Sun elevation above local horizon is not considered in this method, so output
	 * events can be not visible by the input observer.
	 * 
	 * @param time Time object with the date of the eclipse before it starts, but as close
	 * as possible.
	 * @param obs Observer object.
	 * @param eph Ephemeris properties.
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	public SolarEclipse (TimeElement time, ObserverElement obs, EphemerisElement eph)
			throws JPARSECException
	{
		init(time, obs, eph);
	}

	/**
	 * Obtain events for the next solar eclipse in TDB. Input time should be
	 * Immediately before, or the day before a certain solar eclipse starts
	 * (shadow ingress). This constructor is similar to the other one, but
	 * it is intended to be used to calculate eclipses produced by other 
	 * satellites, not the Moon. So it adds an accuracy parameter to control
	 * the sensitivity of the search. 
	 * 
	 * Events are:<P>
	 * - Shadow ingress.<P>
	 * - Shadow total/annular ingress.<P>
	 * - Shadow total/annular egress.<P>
	 * - Shadow egress.
	 * 
	 * Eclipse type (total, partial, annular) is set in static variable type.
	 * 
	 * Moon/Sun elevation above local horizon is not considered in this method, so output
	 * events can be not visible by the input observer.
	 * 
	 * @param time Time object with the date of the eclipse before it starts, but as close
	 * as possible.
	 * @param obs Observer object.
	 * @param eph Ephemeris properties.
	 * @param accuracy Accuracy of the iterative search in seconds for eclipses produced by 
	 * any moon besides the Moon. In case of solar eclipses from Earth or input values <= 0 this value 
	 * will have no effect. Default value is 1s (for solar eclipses from Earth is irrelevant, it is set 
	 * automatically).
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	public SolarEclipse (TimeElement time, ObserverElement obs, EphemerisElement eph,
			double accuracy)
			throws JPARSECException
	{
		this.setAccuracy(accuracy);
		init(time, obs, eph);
	}
	
	private void init(TimeElement time, ObserverElement obs, EphemerisElement eph) throws JPARSECException {
		if (eph.targetBody != TARGET.Moon && !eph.targetBody.isNaturalSatellite()) throw new JPARSECException("Target body must be the Moon or any other natural satellite.");
		if (eph.targetBody.getCentralBody() != obs.getMotherBody()) throw new JPARSECException("Target body must orbit around the observer.");
		targetBody = eph.targetBody;
		
		double jd = TimeScale.getJD(time, obs, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
		double precission = 0.5 / Constant.SECONDS_PER_DAY;
		if (eph.algorithm == ALGORITHM.VSOP87_ELP2000ForMoon) precission = 1.0 / Constant.SECONDS_PER_DAY;
		double out[] = new double[] { 0.0, 0.0, 0.0, 0.0 };

		EphemerisElement newEph = eph.clone();
		double jd0 = jd;
		type = ECLIPSE_TYPE.NO_ECLIPSE;
		do
		{
			if (targetBody == TARGET.Moon) {
				recommendedTimeOffsetToNextEvent = 0.0;
				recommendedTimeOffsetToNextEventAfterTotality = 0.0;
			}

			jd += precission;
			TimeElement new_time = new TimeElement(jd, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
			boolean event[] = checkEclipse(new_time, obs, newEph);

			if (event[0] && out[0] == 0.0)
				out[0] = jd;
			if (event[1] && out[1] == 0.0)
				out[1] = jd;

			if (!event[1] && out[2] == 0.0 && out[1] != 0.0)
				out[2] = jd;
			if (!event[0] && out[3] == 0.0 && out[0] != 0.0)
				out[3] = jd;

			if (out[1] == 0.0 || type == ECLIPSE_TYPE.PARTIAL)
			{
				jd += 0.75 * recommendedTimeOffsetToNextEvent;
			} else
			{
				jd += 0.75 * recommendedTimeOffsetToNextEventAfterTotality;
			}
		} while (out[3] == 0.0);

		// It is necessary to repeat calculations to properly account for
		// partial eclipses
		if (type == ECLIPSE_TYPE.PARTIAL)
		{
			out[0] = out[1] = out[2] = out[3] = 0.0;
			jd = jd0;
			do
			{
				if (targetBody == TARGET.Moon) {
					recommendedTimeOffsetToNextEvent = 0.0;
					recommendedTimeOffsetToNextEventAfterTotality = 0.0;
				}

				jd += precission;
				TimeElement new_time = new TimeElement(jd, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
				boolean event[] = checkEclipse(new_time, obs, newEph);

				if (event[0] && out[0] == 0.0)
					out[0] = jd;
				if (event[1] && out[1] == 0.0)
					out[1] = jd;

				if (!event[1] && out[2] == 0.0 && out[1] != 0.0)
					out[2] = jd;
				if (!event[0] && out[3] == 0.0 && out[0] != 0.0)
					out[3] = jd;

				if (out[0] == 0.0)
				{
					jd += 0.75 * recommendedTimeOffsetToNextEvent;
				} else
				{
					jd += 0.75 * recommendedTimeOffsetToNextEventAfterTotality;
				}
			} while (out[3] == 0.0);
		}
		events = out;
		
		RenderEclipse re = null;
		jdMax = (events[0] + events[3]) * 0.5;
		if (events[1] != 0.0) jdMax = (events[1] + events[2]) * 0.5;
		if (obs.getMotherBody() == TARGET.EARTH) {
			try {
				re = new RenderEclipse(new AstroDate(events[0] - 0.25));
			} catch (Exception exc) {
				re = new RenderEclipse(new AstroDate(events[0] + 0.25));			
			}
			jdMax = TimeScale.getJD(re.solarEclipseMaximum(obs, eph), obs, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
		}
	}
	
	/**
	 * Returns type of the current calculated eclipse.
	 * 
	 * @return Eclipse type, such as annular, partial, or total.
	 */
	public String getEclipseType()
	{
		String type = "";
		switch (this.type)
		{
		case ANNULAR:
			type = "annular";
			break;
		case PARTIAL:
			type = "partial";
			break;
		case TOTAL:
			type = "total";
			break;
		case NO_ECLIPSE:
			type = "no eclipse";
			break;
		}

		return Translate.translate(type);
	}

	/**
	 * Returns the set of events. Times are given in TDB.
	 * @return Events as an object array. Two events defining
	 * the start/end of the partial phase, and start/end of the
	 * total/annular phase. In case of a partial eclipse only
	 * one event is returned.
	 */
	public MoonEventElement[] getEvents()
	{
		String e[] = new String[] {Translate.translate(Translate.JPARSEC_PARTIAL), Translate.translate(Translate.JPARSEC_TOTAL)};
		if (this.type == ECLIPSE_TYPE.ANNULAR) e[1] = Translate.translate(Translate.JPARSEC_ANNULAR);
		int count = 0;
		MoonEventElement event[] = new MoonEventElement[2];
		for (int i=0; i<2; i++)
		{
			event[i] = new MoonEventElement(events[i], events[3-i], TARGET.SUN, targetBody, MoonEventElement.EVENT.ECLIPSED, e[i]);
			if (events[i] > 0.0) count ++;
		}

		MoonEventElement out[] = new MoonEventElement[count];
		int index = -1;
		for (int i=0; i<2; i++)
		{
			if (event[i].startTime > 0.0) {
				index ++;
				out[index] = event[i].clone();
			}
		}
		
		return out;
	}
	
	/**
	 * Return eclipse maximum as Julian day.
	 * 
	 * @return Eclipse maximum in TDB.
	 * @throws JPARSECException If an error occurs.
	 */
	public double getEclipseMaximum() throws JPARSECException
	{
		return jdMax;
/*		double jd_max = (events[0] + events[3]) * 0.5;
		if (events[1] != 0.0)
			jd_max = (events[1] + events[2]) * 0.5;

		return jd_max;
*/	}

	/**
	 * For unit testing only.
	 * @param args Not used.
	 */
	public static void main(String args[])
	{
		System.out.println("SolarEclipse test");

		/*
		 * 3-10-05, Madrid: 7:40:12, 8:55:55, 8:57:58, 9:00:02, 10:23:36 (ROA)
		 * 3-10-05, Madrid: 7:40:11.7, 8:55:53.7, 8:57:59.1, 9:00:04.4, 10:23:38.3 (Spenak) Madrid at 40°24'N  003°41'W   667
		 * 3-10-05, Madrid: 7:40:12.7, 8:55:54.5, 8:57:59.0, 9:00:03.5, 10:23:36.8 (JPARSEC) Default Madrid city, Moshier algorithms, 0.1s precision
		 * 3-10-05, Madrid: 7:40:12.8, 8:55:54.9, 8:57:59.3, 9:00:03.7, 10:23:37.6 (JPARSEC) same Madrid city as Spenak, Moshier algorithms, 0.1s precision
		 * 3-10-05, Madrid: 7:40:12.9, 8:55:55.1, 8:57:59.4, 9:00:03.8, 10:23:37.7 (JPARSEC) same Madrid city as Spenak, DE405, 0.1s precision
		 */

		try
		{
			AstroDate astro = new AstroDate(2005, AstroDate.OCTOBER, 3, 0, 0, 0);
			TimeElement time = new TimeElement(astro, SCALE.UNIVERSAL_TIME_UTC);
			EphemerisElement eph = new EphemerisElement(TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT,
					EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
					EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000, EphemerisElement.ALGORITHM.MOSHIER);
			eph.correctForEOP = false;
			eph.correctForPolarMotion = false;
			eph.correctEOPForDiurnalSubdiurnalTides = false;
			eph.preferPrecisionInEphemerides = false;			
			CityElement city = City.findCity("Madrid");
			ObserverElement observer = ObserverElement.parseCity(city);
			// For same location as Spenak's Madrid
			observer.setLongitudeDeg(-3.68333);
			observer.setLatitudeDeg(40.4);
			observer.setHeight(667, true);
			SolarEclipse se = new SolarEclipse(time, observer, eph);

			double jdMax  = se.getEclipseMaximum();
			TimeElement t_max = new TimeElement(jdMax, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
			double jdUT_max = TimeScale.getJD(t_max, observer, eph, SCALE.UNIVERSAL_TIME_UT1);

			System.out.println(se.getEclipseType() + " solar eclipse on " + TimeFormat
					.formatJulianDayAsDateAndTime(jdUT_max, SCALE.UNIVERSAL_TIME_UT1) + ". In UT1:");
			MoonEventElement[] events = se.getEvents();
			for (int i = 0; i < events.length; i++)
			{
				if (events[i].startTime != 0.0) {
					TimeElement t_init = new TimeElement(events[i].startTime, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
					double jdUT_init = TimeScale.getJD(t_init, observer, eph, SCALE.UNIVERSAL_TIME_UT1);
					TimeElement t_end = new TimeElement(events[i].endTime, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
					double jdUT_end = TimeScale.getJD(t_end, observer, eph, SCALE.UNIVERSAL_TIME_UT1);
					
					System.out.println("From "+TimeFormat.formatJulianDayAsDateAndTime(jdUT_init, SCALE.UNIVERSAL_TIME_UT1) + " to "+
							TimeFormat.formatJulianDayAsDateAndTime(jdUT_end, SCALE.UNIVERSAL_TIME_UT1)+" (" + events[i].details + ")");
					
					// Show decimals in seconds
/*					AstroDate ini = new AstroDate(jdUT_init);
					AstroDate end = new AstroDate(jdUT_end);
					AstroDate max = new AstroDate(jdUT_max);
					System.out.println(ini.getSeconds()+"/"+end.getSeconds()+"/"+max.getSeconds());
*/				}
			}
			JPARSECException.showWarnings();
		} catch (JPARSECException ve)
		{
			JPARSECException.showException(ve);
		}
	}
}
