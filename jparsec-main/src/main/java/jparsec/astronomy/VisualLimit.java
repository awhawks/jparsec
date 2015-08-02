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
package jparsec.astronomy;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target.TARGET;
import jparsec.ephem.planets.EphemElement;
import jparsec.ephem.planets.PlanetEphem;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;

/**
 * Calculates sky brightness and limiting magnitude. <BR>
 * Based on C code by Bill Gray (www.projectpluto.com), which was in turn based
 * on Brad Schaefer's article and code on pages 57-60, May 1998 <I>Sky &
 * Telescope</I>, "To the Visual Limits".
 * <P>
 * The computations for sky brightness and limiting magnitude can be broken up
 * into several pieces. Some computations depend on things that are constant for
 * a given observing site and time: the lunar and solar zenith distances, the
 * air masses to those objects, the temperature and relative humidity, and so
 * forth.
 * 
 * @author M. Huss
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class VisualLimit
{
	/**
	 * Explicit (all values) constructor.
	 * 
	 * @param bandMask A logical mask which represents one or more of the five
	 *        possible bands to calculate. Bands are U (1), B (2), V (4), R (8),
	 *        and I (16), so to calculate all set this value to 31, the sum of all them.
	 * @param fbd The fixed brightness data.
	 * @param abd The angular brightness data.
	 */
	protected VisualLimit(int bandMask, VisualLimitFixedBrightnessData fbd, VisualLimitAngularBrightnessData abd)
	{
		this.mask = bandMask;
		setBrightnessParams(fbd);
		computeSkyBrightness(abd);
		computeExtinction();
	}

	/**
	 * Constructor to calculate limiting magnitudes.
	 * 
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param ephem_sun Ephem object containing Sun's ephemeris.
	 * @param ephem_moon Ephem object containing Moon's ephemeris.
	 * @param azimuth Azimuth of observing point in radians.
	 * @param elevation Elevation of observing point in radians.
	 * @param bandMask A logical mask which represents one or more of the five
	 *        possible bands to calculate. Bands are U (1), B (2), V (4), R (8),
	 *        and I (16), so to calculate all set this value to 31, the sum of all them.
	 * @throws JPARSECException In case the observer is not located on Earth.
	 */
	public VisualLimit(TimeElement time, ObserverElement obs, EphemElement ephem_sun,
			EphemElement ephem_moon, double azimuth, double elevation, int bandMask) throws JPARSECException
	{
		VisualLimit v = getInstance(time, obs, ephem_sun, ephem_moon, azimuth, elevation, bandMask);
		this.mask = bandMask;
		setBrightnessParams(v.fixed);
		computeSkyBrightness(v.angular);
		computeExtinction();
	}

	/**
	 * Set the fixed brightness parameters.
	 * 
	 * @param fbd The fixed brightness data.
	 */
	private void setBrightnessParams(VisualLimitFixedBrightnessData fbd)
	{
		fixed = fbd;
		double monthAngle = (fixed.month - 3.) * Math.PI / 6.;
		double kaCoeff, krCoeff, koCoeff, kwCoeff, moonElong;
		int i;

		krCoeff = .1066 * Math.exp(-fixed.heightAboveSeaLevel / 8200.);
		kaCoeff = .1 * Math.exp(-fixed.heightAboveSeaLevel / 1500.);
		if (fixed.relativeHumidity > 0.)
		{
			double humidityParam;

			if (fixed.relativeHumidity >= 100.)
				humidityParam = 1000000.;
			else
				humidityParam = 1. - .32 / Math.log(fixed.relativeHumidity / 100.);

			kaCoeff *= Math.exp(1.33 * Math.log(humidityParam));
		}
		if (fixed.latitude < 0D)
			kaCoeff *= 1D - Math.sin(monthAngle);
		else
			kaCoeff *= 1D + Math.sin(monthAngle);

		koCoeff = (3. + .4 * (fixed.latitude * Math.cos(monthAngle) - Math.cos(3. * fixed.latitude))) / 3.;

		kwCoeff = .94 * (fixed.relativeHumidity / 100.) * Math.exp(fixed.temperature / 15.) * Math
				.exp(-fixed.heightAboveSeaLevel / 8200.);

		yearTerm = 1. + .3 * Math.cos(2. * Math.PI * (fixed.year - 1992) / 11.);
		airMassMoon = computeAirMass(fixed.moonZenithAngle);
		airMassSun = computeAirMass(fixed.sunZenithAngle);
		moonElong = fixed.moonElongation * 180. / Math.PI;
		lunarMag = -12.73 + moonElong * (.026 + 4.e-9 * (moonElong * moonElong * moonElong));
		/* line 2180 in B Schaefer code */

		for (i = 0; i < 5; i++)
		{

			kr[i] = krCoeff * fourthPowerTerms[i];
			ka[i] = kaCoeff * onePointThreePowerTerms[i];
			ko[i] = koCoeff * oz[i];
			kw[i] = kwCoeff * wt[i];

			k[i] = kr[i] + ka[i] + ko[i] + kw[i];
			c3[i] = magToBrightness(k[i] * airMassMoon);
			/* compute dropoff in lunar brightness from extinction: 2200 */
			c4[i] = magToBrightness(k[i] * airMassSun);
		}
	}

	/**
	 * Compute the sky brightness.
	 * 
	 * @param abd The angular brightness data.
	 */
	private void computeSkyBrightness(VisualLimitAngularBrightnessData abd)
	{
		angular = abd;

		double sinZenith;
		double brightnessDrop2150, fs, fm;
		int i;

		double airMass = computeAirMass(angular.zenithAngle);
		sinZenith = Math.sin(angular.zenithAngle);
		brightnessDrop2150 = .4 + .6 / Math.sqrt(1.0 - .96 * sinZenith * sinZenith);
		fm = computeFFactor(angular.moonAngularDistance);
		fs = computeFFactor(angular.sunAngularDistance);

		for (i = 0; i < 5; i++)
			if (0 != ((mask >> i) & 1))
			{
				double bn = bo[i] * yearTerm, directLoss;
				/* accounts for a 30% variation due to sunspots? */

				double brightnessMoon, twilightBrightness;
				double brightnessDaylight;

				directLoss = magToBrightness(k[i] * airMass);
				bn *= brightnessDrop2150;
				/* Not sure what this is.. line 2150 in B Schaefer code */
				bn *= directLoss;
				/* drop brightness to account for extinction: 2160 */

				if (fixed.moonZenithAngle < Math.PI / 2.) /* moon is above horizon */
				{
					brightnessMoon = magToBrightness(lunarMag + cm[i] - mo[i] + 43.27);
					brightnessMoon *= (1. - directLoss);
					/* Maybe computing how much of the lunar light gets */
					/* scattered? 2240 */
					brightnessMoon *= (fm * c3[i] + 440000. * (1. - c3[i]));
				} else
					brightnessMoon = 0.;

				twilightBrightness = ms[i] - mo[i] + 32.5 - (90. - fixed.sunZenithAngle * 180. / Math.PI) - angular.zenithAngle / (2.0 * Math.PI * k[i]);
				/* above is in magnitudes, so gotta do this: */
				twilightBrightness = magToBrightness(twilightBrightness);
				/* above is line 2280, B Schaefer code */
				twilightBrightness *= 100. / (angular.sunAngularDistance * 180. / Math.PI);
				twilightBrightness *= 1. - magToBrightness(k[i]);
				/* preceding line looks suspicious to me... line 2290 */
				brightnessDaylight = magToBrightness(ms[i] - mo[i] + 43.27);
				/* line 2340 */
				brightnessDaylight *= (1. - directLoss);
				/* line 2350 */
				brightnessDaylight *= fs * c4[i] + 440000. * (1. - c4[i]);
				if (brightnessDaylight > twilightBrightness)
					brightness[i] = bn + twilightBrightness + brightnessMoon;
				else
					brightness[i] = bn + brightnessDaylight + brightnessMoon;
			}
	}

	/**
	 * Calculate the limiting magnitude.
	 * 
	 * @return The limiting magnitude.
	 */
	public double limitingMagnitude()
	{
		double c1, c2, bl = brightness[2] / 1.11e-15;
		double th, tval;

		if (bl > 1500.)
		{
			c1 = 4.4668e-9;
			c2 = 1.2589e-6;
		} else
		{
			c1 = 1.5849e-10;
			c2 = 1.2589e-2;
		}
		tval = 1. + Math.sqrt(c2 * bl);
		th = c1 * tval * tval; // brightness in foot-candles?
		return -16.57 + brightnessToMag(th) - extinction[2];
	}

	/**
	 * Compute the extinction value.
	 */
	public void computeExtinction()
	{
		double cosZenithAng = Math.cos(angular.zenithAngle);
		double tval;
		int i;

		airMassGas = 1. / (cosZenithAng + .0286 * Math.exp(-10.5 * cosZenithAng));
		airMassAerosol = 1. / (cosZenithAng + .0123 * Math.exp(-24.5 * cosZenithAng));
		tval = Math.sin(angular.zenithAngle) / (1. + 20. / 6378.);
		airMassOzone = 1. / Math.sqrt(1. - tval * tval);
		for (i = 0; i < 5; i++)
		{
			if (0 != ((mask >> i) & 1))
			{
				extinction[i] = (kr[i] + kw[i]) * airMassGas + ka[i] * airMassAerosol + ko[i] * airMassOzone;
			}
		}
	}

	/**
	 * Set the band mask value.
	 * @param m Mask value.
	 */
	public void setMask(int m)
	{
		mask = m;
	}

	/**
	 * Get the K band value.
	 * 
	 * @param i Index into the band data (0 for U, to 4 for I).
	 * @return k[i].
	 * @throws JPARSECException For an invalid band.
	 */
	public double getK(int i) throws JPARSECException
	{
		if (i < 0 || i > BANDS)
			throw new JPARSECException("invalid band data index " + i + ".");

		return k[i];
	}

	/**
	 * Get the brightness value.
	 * 
	 * @param i Index into the band data (0 for U, to 4 for I).
	 * @return brightness[i].
	 * @throws JPARSECException For an invalid band.
	 */
	public double getBrightness(int i) throws JPARSECException
	{
		if (i < 0 || i > BANDS)
			throw new JPARSECException("invalid band data index " + i + ".");

		return brightness[i];
	}

	/**
	 * Get the extinction value.
	 * 
	 * @param i Index into the band data (0 for U, to 4 for I).
	 * @return extinction[i].
	 * @throws JPARSECException For an invalid band.
	 */
	public double getExtinction(int i) throws JPARSECException
	{
		if (i < 0 || i > BANDS)
			throw new JPARSECException("invalid band data index " + i + ".");

		return extinction[i];
	}

	protected static final int BANDS = 5;

	private static double magToBrightness(double m)
	{
		return Math.exp(-.4 * m * Math.log(10.0));
	}

	private static double brightnessToMag(double b)
	{
		return (-2.5 * Math.log(b) / Math.log(10.0));
	}

	// constants for a given time:
	private VisualLimitFixedBrightnessData fixed;

	// values varying across the sky:
	private VisualLimitAngularBrightnessData angular;

	private int mask; // indicates which of the 5 photometric bands we want

	private static final double fourthPowerTerms[] =
	{ 5.155601, 2.441406, 1., 0.381117, 0.139470 };

	private static final double onePointThreePowerTerms[] =
	{ 1.704083, 1.336543, 1., 0.730877, 0.527177 };

	private static final double oz[] =
	{ 0., 0., .031, .008, 0. };

	private static final double wt[] =
	{ .074, .045, .031, .02, .015 };

	private static final double bo[] =
	{ 8.0e-14, 7.e-14, 1.e-13, 1.e-13, 3.e-13 };

	/* Base sky brightness in each band */
	private static final double cm[] =
	{ 1.36, 0.91, 0.00, -0.76, -1.17 };

	/* Correction to moon's magnitude */
	/* Solar magnitude? */
	private static final double ms[] =
	{ -25.96, -26.09, -26.74, -27.26, -27.55 };

	/* Lunar magnitude? */
	private static final double mo[] =
	{ -10.93, -10.45, -11.05, -11.90, -12.70 };

	// Items computed in setBrightnessParams:
	private double airMassSun, airMassMoon, lunarMag;

	private double k[] =
	{ 0D, 0D, 0D, 0D, 0D }, c3[] =
	{ 0D, 0D, 0D, 0D, 0D }, c4[] =
	{ 0D, 0D, 0D, 0D, 0D }, ka[] =
	{ 0D, 0D, 0D, 0D, 0D }, kr[] =
	{ 0D, 0D, 0D, 0D, 0D }, ko[] =
	{ 0D, 0D, 0D, 0D, 0D }, kw[] =
	{ 0D, 0D, 0D, 0D, 0D };

	private double yearTerm;

	// Items computed in computeLimitingMag:
	private double airMassGas, airMassAerosol, airMassOzone;

	private double extinction[] =
	{ 0D, 0D, 0D, 0D, 0D };;

	// Internal parameters from computeSkyBrightness:
	private double brightness[] =
	{ 0D, 0D, 0D, 0D, 0D };

	private static double computeAirMass(double zenithAngle)
	{
		double rval = 40D, cosAng = Math.cos(zenithAngle);

		if (cosAng > 0.)
			rval = 1. / (cosAng + .025 * Math.exp(-11. * cosAng));

		return rval;
	}

	private static double computeFFactor(double objDist)
	{
		double objDistDegrees = objDist * 180D / Math.PI;
		double rval, cosDist = Math.cos(objDist);

		rval = 6.2e+7 / (objDistDegrees * objDistDegrees) + Math.exp(Math.log(10.0) * (6.15 - objDistDegrees / 40.));
		rval += 229086. * (1.06 + cosDist * cosDist); /* polarization term? */

		return rval;

		/*
		 * Seen on lines 2210 & 2200 for the moon, and on lines 2320 & 2330 for
		 * the moon. I've only foggy ideas what it means; I think it attempts to
		 * compute the falloff in scattered light from an object as a function
		 * of distance.
		 */
	}

	/**
	 * Computes limiting magnitude for V band.
	 * 
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param ephem_sun Ephem object containing Sun's ephemeris.
	 * @param ephem_moon Ephem object containing Moon's ephemeris.
	 * @param azimuth Azimuth of observing point in radians.
	 * @param elevation Elevation of observing point in radians.
	 * @return Limiting magnitude.
	 * @throws JPARSECException In case the observer is not located on Earth.
	 */
	public static double getLimitingMagnitude(TimeElement time, ObserverElement obs, EphemElement ephem_sun,
			EphemElement ephem_moon, double azimuth, double elevation) throws JPARSECException
	{	
		int bandMask = 0x1F; // all five bands
		VisualLimit v = getInstance(time, obs, ephem_sun, ephem_moon, azimuth, elevation, bandMask);
		double mag_limit = v.limitingMagnitude();
		return mag_limit;
	}

	private static VisualLimit getInstance(TimeElement time, ObserverElement obs, EphemElement ephem_sun,
			EphemElement ephem_moon, double azimuth, double elevation, int bandMask) throws JPARSECException {
		if (obs.getMotherBody() != TARGET.EARTH) throw new JPARSECException("Observer must be located on Earth.");
		
		VisualLimitFixedBrightnessData f = new VisualLimitFixedBrightnessData(Math.PI * 0.5 - ephem_moon.elevation, // zenithAngleMoon
				Math.PI * 0.5 - ephem_sun.elevation, // zenithAngSun
				ephem_moon.elongation, // moonElongation // 180g = full moon
				obs.getHeight(), // htAboveSeaInMeters
				obs.getLatitudeRad(), // latitude
				obs.getTemperature(), // temperatureInC
				obs.getHumidity(), // relativeHumidity
				time.astroDate.getYear(), // year
				time.astroDate.getMonth() // month
		);

		LocationElement loc_obs = new LocationElement(azimuth, elevation, 1.0);
		LocationElement loc_sun = new LocationElement(ephem_sun.azimuth, ephem_sun.elevation, 1.0);
		LocationElement loc_moon = new LocationElement(ephem_moon.azimuth, ephem_moon.elevation, 1.0);

		// Values varying across the sky:
		VisualLimitAngularBrightnessData a = new VisualLimitAngularBrightnessData(Math.PI * 0.5 - elevation, // zenithAngle
				LocationElement.getApproximateAngularDistance(loc_obs, loc_moon), // distMoon
				LocationElement.getApproximateAngularDistance(loc_obs, loc_sun)); // distSun

		VisualLimit v = new VisualLimit(bandMask, f, a);
		return v;
	}

	/**
	 * Computes sky brightness for U, B, V, R, and I bands.
	 * 
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param ephem_sun Ephem object containing Sun's ephemeris.
	 * @param ephem_moon Ephem object containing Moon's ephemeris.
	 * @param azimuth Azimuth of observing point.
	 * @param elevation Elevation of observing point.
	 * @return Sky brightness values in nanolamberts (nL).
	 * @throws JPARSECException If the band index is not valid or the observer is not on Earth.
	 */
	public static double[] getSkyBrightness(TimeElement time, ObserverElement obs, EphemElement ephem_sun,
			EphemElement ephem_moon, double azimuth, double elevation) throws JPARSECException
	{
		if (obs.getMotherBody() != TARGET.EARTH) throw new JPARSECException("Observer must be located on Earth.");
		
		VisualLimitFixedBrightnessData f = new VisualLimitFixedBrightnessData(Math.PI * 0.5 - ephem_moon.elevation, // zenithAngleMoon
				Math.PI * 0.5 - ephem_sun.elevation, // zenithAngSun
				ephem_moon.elongation, // moonElongation // 180g = full moon
				obs.getHeight(), // htAboveSeaInMeters
				obs.getLatitudeRad(), // latitude
				obs.getTemperature(), // temperatureInC
				obs.getHumidity(), // relativeHumidity
				time.astroDate.getYear(), // year
				time.astroDate.getMonth() // month
		);

		LocationElement loc_obs = new LocationElement(azimuth, elevation, 1.0);
		LocationElement loc_sun = new LocationElement(ephem_sun.azimuth, ephem_sun.elevation, 1.0);
		LocationElement loc_moon = new LocationElement(ephem_moon.azimuth, ephem_moon.elevation, 1.0);

		// Values varying across the sky:
		VisualLimitAngularBrightnessData a = new VisualLimitAngularBrightnessData(Math.PI * 0.5 - elevation, // zenithAngle
				LocationElement.getAngularDistance(loc_obs, loc_moon), // distMoon
				LocationElement.getAngularDistance(loc_obs, loc_sun)); // distSun

		int bandMask = 0x1F; // all five bands

		VisualLimit v = new VisualLimit(bandMask, f, a);
		double[] bright = new double[5];
		try
		{
			bright = new double[]
			{ v.getBrightness(0) / 1.11E-15, v.getBrightness(1) / 1.11E-15, v.getBrightness(2) / 1.11E-15,
					v.getBrightness(3) / 1.11E-15, v.getBrightness(4) / 1.11E-15 };
		} catch (JPARSECException ve)
		{
			throw ve;
		}

		return bright;
	}

	/**
	 * Computes if the object is visible to the naked eye or not. Position of
	 * the object is calculated using Moshier ephemeris.
	 * 
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @param ephem Ephem object with the ephemeris calculations.
	 * @return True if it is visible, false otherwise.
	 * @throws JPARSECException Thrown if the observer is not on Earth or the calculation fails.
	 */
	public static boolean isVisibleToNakedEye(TimeElement time, ObserverElement obs, EphemerisElement eph,
			EphemElement ephem) throws JPARSECException
	{
		if (obs.getMotherBody() != TARGET.EARTH) throw new JPARSECException("Observer must be located on Earth.");

		EphemerisElement sun_eph = (EphemerisElement) eph.clone();
		sun_eph.targetBody = TARGET.SUN;
		sun_eph.algorithm = EphemerisElement.ALGORITHM.MOSHIER;
		EphemElement ephem_sun = PlanetEphem.MoshierEphemeris(time, obs, sun_eph);

		EphemerisElement moon_eph = (EphemerisElement) eph.clone();
		moon_eph.targetBody = TARGET.Moon;
		moon_eph.algorithm = EphemerisElement.ALGORITHM.MOSHIER;
		EphemElement ephem_moon = PlanetEphem.MoshierEphemeris(time, obs, moon_eph);

		double limiting_magnitude = VisualLimit.getLimitingMagnitude(time, obs, ephem_sun, ephem_moon, ephem.azimuth, ephem.elevation);
		boolean isVisible = ephem.magnitude <= limiting_magnitude;

		return isVisible;
	}

	/**
	 * Computes extinction coefficient for U, B, V, R, and I bands.
	 * 
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param ephem_sun Ephem object containing Sun's ephemeris.
	 * @param ephem_moon Ephem object containing Moon's ephemeris.
	 * @param azimuth Azimuth of observing point.
	 * @param elevation Elevation of observing point.
	 * @return Extinction coefficients.
	 * @throws JPARSECException If the band index is not valid or the observer is not on Earth.
	 */
	public static double[] getExtinctionCoefficient(TimeElement time, ObserverElement obs, EphemElement ephem_sun,
			EphemElement ephem_moon, double azimuth, double elevation) throws JPARSECException
	{
		if (obs.getMotherBody() != TARGET.EARTH) throw new JPARSECException("Observer must be located on Earth.");
		VisualLimitFixedBrightnessData f = new VisualLimitFixedBrightnessData(Math.PI * 0.5 - ephem_moon.elevation, // zenithAngleMoon
				Math.PI * 0.5 - ephem_sun.elevation, // zenithAngSun
				ephem_moon.elongation, // moonElongation // 180g = full moon
				obs.getHeight(), // htAboveSeaInMeters
				obs.getLatitudeRad(), // latitude
				obs.getTemperature(), // temperatureInC
				obs.getHumidity(), // relativeHumidity
				time.astroDate.getYear(), // year
				time.astroDate.getMonth() // month
		);

		LocationElement loc_obs = new LocationElement(azimuth, elevation, 1.0);
		LocationElement loc_sun = new LocationElement(ephem_sun.azimuth, ephem_sun.elevation, 1.0);
		LocationElement loc_moon = new LocationElement(ephem_moon.azimuth, ephem_moon.elevation, 1.0);

		// Values varying across the sky:
		VisualLimitAngularBrightnessData a = new VisualLimitAngularBrightnessData(Math.PI * 0.5 - elevation, // zenithAngle
				LocationElement.getAngularDistance(loc_obs, loc_moon), // distMoon
				LocationElement.getAngularDistance(loc_obs, loc_sun)); // distSun

		int bandMask = 0x1F; // all five bands

		VisualLimit v = new VisualLimit(bandMask, f, a);
		double[] bright = new double[5];

		bright = new double[] { v.getExtinction(0), v.getExtinction(1), v.getExtinction(2), v.getExtinction(3), v.getExtinction(4) };

		return bright;
	}
}
