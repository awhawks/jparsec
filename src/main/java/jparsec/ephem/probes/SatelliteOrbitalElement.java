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
package jparsec.ephem.probes;

import java.io.Serializable;

/**
 * An adequate class for storing orbital elements of artificial satellites.
 * 
 * @see SatelliteEphem
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class SatelliteOrbitalElement implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Explicit constructor of a satellite orbit object.
	 * 
	 * @param nom Name of the satellite.
	 * @param sat_n Satellite number.
	 * @param y Reference year of the elements.
	 * @param d Reference day and fraction of days.
	 * @param m2term First derivative of the mean motion.
	 * @param incl Inclination of orbit.
	 * @param ra Right ascension of the ascending node
	 * @param ecc Eccentricity.
	 * @param anomaly Mean anomaly.
	 * @param wp Argument of perigee.
	 * @param motion Mean motion.
	 * @param rev Number of revolutions completed at epoch.
	 * @param m3 Second derivative. Set to 0 if no available.
	 * @param drag Drag term. Set to 0 if no available.
	 */
	public SatelliteOrbitalElement(String nom, int sat_n, int y, double d, double m2term, double incl, double ra,
			double ecc, double anomaly, double wp, double motion, int rev, double m3, double drag)
	{
		name = nom;
		year = y;
		day = d;
		firstDerivative = m2term;
		secondDerivative = m3;
		this.drag = drag;
		inclination = incl;
		ascendingNodeRA = ra;
		eccentricity = ecc;
		meanAnomaly = anomaly;
		argumentOfPerigee = wp;
		meanMotion = motion;
		revolutionNumber = rev;
		satelliteNumber = sat_n;
	}

	/**
	 * Constructor of an empty satellite orbit object.
	 */
	public SatelliteOrbitalElement()
	{
		name = "";
		year = 0;
		day = 0.0;
		firstDerivative = 0.0;
		secondDerivative = 0.0;
		inclination = 0.0;
		ascendingNodeRA = 0.0;
		eccentricity = 0.0;
		meanAnomaly = 0.0;
		argumentOfPerigee = 0.0;
		meanMotion = 0.0;
		revolutionNumber = 0;
	}

	/**
	 * Name of the satellite.
	 */
	public String name;

	/**
	 * Satellite designation.
	 */
	public int satelliteNumber;

	/**
	 * Year of reference of the elements.
	 */
	public int year;

	/**
	 * Days and fractions of day of reference of the elements.
	 */
	public double day;

	/**
	 * First derivative of the mean motion in radians per day.
	 */
	public double firstDerivative;

	/**
	 * Second derivative of the mean motion.
	 */
	public double secondDerivative;

	/**
	 * Drag term.
	 */
	public double drag;

	/**
	 * Inclination of orbit in radians.
	 */
	public double inclination;

	/**
	 * Right ascension of the ascending node in radians.
	 */
	public double ascendingNodeRA;

	/**
	 * Eccentricity.
	 */
	public double eccentricity;

	/**
	 * Mean anomaly in radians.
	 */
	public double meanAnomaly;

	/**
	 * Argument of perigee in radians.
	 */
	public double argumentOfPerigee;

	/**
	 * Mean motion in radians per day.
	 */
	public double meanMotion;

	/**
	 * Revolution number at the epoch of reference of the elements.
	 */
	public int revolutionNumber;

	/**
	 * To clone the object.
	 */
	public SatelliteOrbitalElement clone()
	{
		if (this == null) return null;
		SatelliteOrbitalElement s = new SatelliteOrbitalElement(this.name, this.satelliteNumber, this.year, this.day,
				this.firstDerivative, this.inclination, this.ascendingNodeRA, this.eccentricity, this.meanAnomaly,
				this.argumentOfPerigee, this.meanMotion, this.revolutionNumber, this.secondDerivative, this.drag);
		return s;
	}
	/**
	 * Returns if a given object is equals to this satellite
	 * ephemeris object.
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
		SatelliteOrbitalElement soe = (SatelliteOrbitalElement) s;
		boolean equals = true;
		if (soe.argumentOfPerigee != this.argumentOfPerigee) equals = false;
		if (soe.ascendingNodeRA != this.ascendingNodeRA) equals = false;
		if (soe.day != this.day) equals = false;
		if (soe.eccentricity != this.eccentricity) equals = false;
		if (soe.firstDerivative != this.firstDerivative) equals = false;
		if (soe.secondDerivative != this.secondDerivative) equals = false;
		if (soe.drag != this.drag) equals = false;
		if (soe.inclination != this.inclination) equals = false;
		if (soe.meanAnomaly != this.meanAnomaly) equals = false;
		if (soe.meanMotion != this.meanMotion) equals = false;
		if (!soe.name.equals(this.name)) equals = false;
		if (soe.revolutionNumber != this.revolutionNumber) equals = false;
		if (soe.satelliteNumber != this.satelliteNumber) equals = false;
		if (soe.year != this.year) equals = false;
		return equals;
	}

	/**
	 * Holds the different status an Iridium satellite can have.
	 */
	public static enum STATUS {
		/** Unknown status. */
		UNKNOWN,
		/** In service, the Iridium satellite can flare. */
		IN_SERVICE,
		/** Spare status, the Iridium satellite may or may not flare. */
		SPARE,
		/** Failed, the Iridium satellite cannot flare. */
		FAILED
	};

	/**
	 * Returns the status of an Iridium satellite.
	 * @return The satellite's status.
	 */
	public STATUS getStatus() {
		int status = name.indexOf("[");
		if (status > 0) {
			String s = name.substring(status+1, status+2);
			if (s.equals("+")) return STATUS.IN_SERVICE;
			if (s.equals("-")) return STATUS.FAILED;
			if (s.equals("S")) return STATUS.SPARE;
		}
		return STATUS.UNKNOWN;
	}
	
	/**
	 * Returns the name of this satellite.
	 * @return Satellite's name.
	 */
	public String getName() {
		int status = name.indexOf("[");
		if (status > 0) return name.substring(0, status).trim();
		return name;
	}
}
