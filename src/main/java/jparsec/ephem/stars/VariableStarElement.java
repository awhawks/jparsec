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
package jparsec.ephem.stars;

import jparsec.astronomy.CoordinateSystem;
import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target.TARGET;
import jparsec.ephem.planets.EphemElement;
import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.math.Constant;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.TimeElement;
import jparsec.time.TimeElement.SCALE;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;

/**
 * Convenient class for variable stars ephemerides, calculated according to 
 * <i>Up-to-date Linear Elements of Close Binaries</i>, J.M. Kreiner, 2004, 
 * Acta Astronomica, vol. 54, pp 207-210. See http://www.as.up.krakow.pl/o-c/cont.html
 * for more information on the variable stars. 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class VariableStarElement {

	static final long serialVersionUID = 1L;

	/**
	 * Constructs a variable star object providing the values of the fields.
	 * 
	 * @param nom Name.
	 * @param ra Right Ascension in radians.
	 * @param dec Declination in radians.
	 * @param mag Magnitude range.
	 * @param t Type.
	 * @param p Period.
	 * @param t0 Minima time.
	 */
	public VariableStarElement(String nom, double ra, double dec, String mag, String t, double p, double t0)
	{
		rightAscension = ra;
		declination = dec;
		name = nom;
		magRange = mag;
		type = t;
		period = p;
		minimaTime = t0;
	}

	/**
	 * Constructs an empty star object.
	 */
	public VariableStarElement()
	{
		rightAscension = 0.0;
		declination = 0.0;
		name = "";
		magRange = "";
		period = 0;
		minimaTime = 0;
		minimaDuration = 0;
		type = "";
		spectralType = "";
		eclipsingType = "";
		maximaDates = "";
		minimaDates= "";
		isEclipsing = false;
		phase = 0;
		nextMinima = 0;
		onlySecondaryMinima = false;
	}

	/**
	 * Name of the star.
	 */
	public String name;
	/**
	 * Period in days.
	 */
	public double period;
	/**
	 * Magnitude range for the variable.
	 */
	public String magRange;
	
	/**
	 * J2000 Right ascension in radians from the catalog.
	 */
	public double rightAscension;

	/**
	 * Declination in radians from the catalog.
	 */
	public double declination;

	/**
	 * True if this star is an eclipsing binary star.
	 */
	public boolean isEclipsing;

	/* ECLIPSING STARS PARAMETERS */
	
	/**
	 * Spectral type for eclipsing stars.
	 */
	public String spectralType;
	/**
	 * type of eclipsing binary for eclipsing stars.
	 */
	public String eclipsingType;
	/**
	 * Heliocentric Julian day of the minima in UTC, for eclipsing stars.
	 */
	public double minimaTime;
	/**
	 * Minima duration in days for an eclipsing star.
	 */
	public double minimaDuration;
	/**
	 * Minima type for eclipsing stars.
	 */
	public String type;

	/* LONG-PERIOD PARAMETERS */
	
	/**
	 * Dates of maxima for long-period variable stars, given as Julian days
	 * separated by comma.
	 */
	public String maximaDates;
	/**
	 * Dates of minima for long-period variable stars, given as Julian days
	 * separated by comma.
	 */
	public String minimaDates;
		
	/**
	 * To clone the object.
	 */
	public VariableStarElement clone()
	{
		if (this == null) return null;
		VariableStarElement out = new VariableStarElement(this.name, this.rightAscension, this.declination, 
				magRange, type, period, minimaTime);
		out.spectralType = this.spectralType;
		out.eclipsingType = this.eclipsingType;
		out.maximaDates = this.maximaDates;
		out.minimaDates = this.minimaDates;
		out.isEclipsing = this.isEclipsing;
		out.onlySecondaryMinima = this.onlySecondaryMinima;
		out.phase = this.phase;
		out.nextMinima = this.nextMinima;
		out.minimaDuration = this.minimaDuration;
		return out;
	}
	/**
	 * Returns true if the input object is equals to this
	 * instance.
	 */
	public boolean equals(Object s)
	{
		if (s == null) {
			if (this == null) return true;
			return false;
		}
		if (this == null) {
			return false;
		}
		boolean equals = true;
		VariableStarElement se = (VariableStarElement) s;
		if (!se.name.equals(this.name)) equals = false;
		if (se.declination != this.declination) equals = false;
		if (se.rightAscension != this.rightAscension) equals = false;
		if (!se.type.equals(this.type)) equals = false;
		if (!se.magRange.equals(this.magRange)) equals = false;
		if (se.period != this.period) equals = false;
		if (se.minimaTime != this.minimaTime) equals = false;

		if (se.eclipsingType == null && eclipsingType != null) equals = false;
		if (se.eclipsingType != null && eclipsingType == null) equals = false;
		if (se.eclipsingType != null && eclipsingType != null && !se.eclipsingType.equals(eclipsingType)) equals = false;

		if (se.spectralType == null && spectralType != null) equals = false;
		if (se.spectralType != null && spectralType == null) equals = false;
		if (se.spectralType != null && spectralType != null && !se.spectralType.equals(spectralType)) equals = false;

		if (se.maximaDates == null && maximaDates != null) equals = false;
		if (se.maximaDates != null && maximaDates == null) equals = false;
		if (se.maximaDates != null && maximaDates != null && !se.maximaDates.equals(maximaDates)) equals = false;

		if (se.minimaDates == null && minimaDates != null) equals = false;
		if (se.minimaDates != null && minimaDates == null) equals = false;
		if (se.minimaDates != null && minimaDates != null && !se.minimaDates.equals(minimaDates)) equals = false;

		if (se.phase != this.phase) equals = false;
		if (se.minimaDuration != this.minimaDuration) equals = false;
		if (se.nextMinima != this.nextMinima) equals = false;
		if (se.onlySecondaryMinima != this.onlySecondaryMinima) equals = false;
		if (se.isEclipsing != this.isEclipsing) equals = false;
		return equals;
	}

	private double phase, nextMinima;
	private boolean onlySecondaryMinima;
	
	/**
	 * Return phase for an eclipsing star, from 0 to 1. 
	 * 0 or 1 is primary minima (in case of circular orbit), 0.5 
	 * is approximatelly the secondary minima. 
	 * @return Phase.
	 */
	public double getPhase() {
		return phase;
	}

	/**
	 * Return next minima for an eclipsing star.
	 * @return Julian day of the next minima in LT.
	 */
	public double getNextMinima() {
		return nextMinima;
	}

	/**
	 * Return next minima for a long-period variable star.
	 * @param time Time object.
	 * @param observer Observer object.
	 * @return Julian day of the next minima, or 0 
	 * if cannot be calculated.
	 * @throws JPARSECException If an error occurs.
	 */
	public double getNextMinima(TimeElement time, ObserverElement observer) throws JPARSECException {
		if (minimaDates.equals("") || isEclipsing) return 0;
		
		EphemerisElement eph = new EphemerisElement(TARGET.SUN, EphemerisElement.COORDINATES_TYPE.APPARENT,
				EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
				EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.MOSHIER);
		
		double jd = TimeScale.getJD(time, observer, eph, SCALE.UNIVERSAL_TIME_UTC);

		double out = 0;
		double val[] = DataSet.toDoubleValues(DataSet.toStringArray(minimaDates, ","));
		for (int i=0; i<val.length; i++) {
			if (val[i] >= jd && (out == 0 || val[i] < out)) out = val[i];
		}
		return out;
	}

	/**
	 * Return next maxima for a long-period variable star.
	 * @param time Time object.
	 * @param observer Observer object.
	 * @return Julian day of the next maxima, or 0 
	 * if cannot be calculated.
	 * @throws JPARSECException If an error occurs.
	 */
	public double getNextMaxima(TimeElement time, ObserverElement observer) throws JPARSECException {
		if (maximaDates.equals("") || isEclipsing) return 0;
		
		EphemerisElement eph = new EphemerisElement(TARGET.SUN, EphemerisElement.COORDINATES_TYPE.APPARENT,
				EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
				EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.MOSHIER);
		
		double jd = TimeScale.getJD(time, observer, eph, SCALE.UNIVERSAL_TIME_UTC);

		double out = 0;
		double val[] = DataSet.toDoubleValues(DataSet.toStringArray(maximaDates, ","));
		for (int i=0; i<val.length; i++) {
			if (val[i] >= jd && (out == 0 || val[i] < out)) out = val[i];
		}
		return out;
	}

	/**
	 * Return if the observations of this star were done only for secondary minimas, 
	 * for an eclipsing star.
	 * @return True or false.
	 */
	public boolean onlySecondaryMinima() {
		return onlySecondaryMinima;
	}

	/**
	 * The path to the catalog of variable stars.
	 */
	public static final String PATH_VARIABLE_STAR_CATALOG =  FileIO.DATA_SKY_DIRECTORY + "allstars-cat.txt";
	/**
	 * The path to the catalog of variable stars.
	 */
	public static final String PATH_VARIABLE_STAR_AAVSO_BULLETIN_2011 =  FileIO.DATA_SKY_DIRECTORY + "bulletin2011.csv";

	/**
	 * Calculates ephemeris for this variable star if it is an eclipsing star.
	 * @param time Time object.
	 * @param observer Observer object.
	 * @param preferPrecision True to prefer accuracy over speed (heliocentric Julian day
	 * will be corrected to geocentric, resulting in, at most, 8 minutes of difference in
	 * the time of the minima).
	 * @throws JPARSECException If an error occurs.
	 */
	public void calcEphemeris(TimeElement time, ObserverElement observer, boolean preferPrecision) throws JPARSECException {
		if (!isEclipsing) return;
		
		EphemerisElement eph = new EphemerisElement(TARGET.SUN, EphemerisElement.COORDINATES_TYPE.APPARENT,
				EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
				EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.MOSHIER);

		double jul = TimeScale.getJD(time, observer, eph, SCALE.UNIVERSAL_TIME_UTC);

		double helioToGeo = 0.0;
		
		// Correct minima time from heliocentric Julian day to (geocentric) Julian day.
		// Too slow, and only 8 minutes of correction at most.
		if (preferPrecision) {
			EphemElement ephem = Ephem.getEphemeris(time, observer, eph, false, false);
			LocationElement loc = new LocationElement(rightAscension, declination, 1.0);
			LocationElement locEcl = CoordinateSystem.equatorialToEcliptic(loc, time, observer, eph);
			LocationElement locSunEcl = CoordinateSystem.equatorialToEcliptic(ephem.getEquatorialLocation(), time, observer, eph);
			helioToGeo = -ephem.distance * Constant.LIGHT_TIME_DAYS_PER_AU * Math.cos(locEcl.getLatitude()) * Math.cos(locEcl.getLongitude()-locSunEcl.getLongitude());
			jul = jul - helioToGeo;
		}

		// phase: current phase, <0.1 or >0.9 primary, >0.4 <0.6 secondary
		double f1 = (jul - minimaTime) / period;
		phase = f1 - Math.floor(f1);
		onlySecondaryMinima = false;
		if (type != null && type.toLowerCase().indexOf("sec") >= 0) onlySecondaryMinima = true;
		
		double nextPhase = 1.0 - phase;
		// Following line disabled to return always primary minima
		//if (onlySecondaryMinima) nextPhase = 0.5 - phase; 
		if (nextPhase <= 0.0) nextPhase += 1.0;

		nextMinima = jul + nextPhase * period;
		double UTC_TO_LT = (observer.getTimeZone() + (double) TimeScale.getDST(nextMinima, observer)) / Constant.HOURS_PER_DAY;
		nextMinima += UTC_TO_LT + helioToGeo;
	}
	
	/**
	 * Returns the path in the classpath to the AAVSO bulletin for a given year.
	 * @param year The year.
	 * @return The path.
	 */
	public static String getPathBulletinAAVSO(int year) {
		return DataSet.replaceAll(PATH_VARIABLE_STAR_AAVSO_BULLETIN_2011, "2011", ""+year, true);
	}
	
	/**
	 * Returns the equatorial position of this star.
	 * @return Equatorial position.
	 */
	public LocationElement getEquatorialPosition() {
		return new LocationElement(rightAscension, declination, 1);
	}
}
