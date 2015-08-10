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

import java.io.Serializable;

import jparsec.ephem.EphemerisElement;
import jparsec.math.Constant;
import jparsec.observer.LocationElement;
import jparsec.vo.SimbadElement;

/**
 * Convenient class for stars data access.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class StarElement implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Value returned for the distance to the star when parallax is 0.
	 * Current value is 1000 pc.
	 */
	public static final int DISTANCE_UNKNOWN = 1000;
	
	/**
	 * Constructs an star object providing the values of the fields.
	 * 
	 * @param nom Name.
	 * @param ra Right Ascension in radians.
	 * @param dec Declination in radians.
	 * @param p Parallax in mas.
	 * @param mag Apparent magnitude.
	 * @param pm_ra Proper motion in RA.
	 * @param pm_dec Proper motion in DEC.
	 * @param pm_rv Radial velocity.
	 * @param eq Reference equinox.
	 * @param fr Reference frame.
	 */
	public StarElement(String nom, double ra, double dec, double p, float mag, float pm_ra, float pm_dec,
			float pm_rv, double eq, EphemerisElement.FRAME fr)
	{
		rightAscension = ra;
		declination = dec;
		parallax = p;
		magnitude = mag;
		properMotionRA = pm_ra;
		properMotionDEC = pm_dec;
		properMotionRadialV = pm_rv;
		equinox = eq;
		frame = fr;
		name = nom;
		spectrum = "";
		type = "";
	}

	/**
	 * Constructs an empty star object.
	 */
	public StarElement()
	{
		rightAscension = 0.0;
		declination = 0.0;
		parallax = 0.0;
		properMotionRA = 0.0f;
		properMotionDEC = 0.0f;
		properMotionRadialV = 0.0f;
		equinox = 0.0;
		frame = EphemerisElement.FRAME.ICRF;
		name = "";
		spectrum = "";
		type = "";
		magnitude = 0f;
	}

	/**
	 * Name of the star.
	 */
	public String name;

	/**
	 * Right Ascension in radians from the catalogue.
	 */
	public double rightAscension;

	/**
	 * Declination in radians from the catalogue.
	 */
	public double declination;

	/**
	 * Parallax in milliarcseconds.
	 */
	public double parallax;

	/**
	 * Apparent visual magnitude.
	 */
	public float magnitude;

	/**
	 * Displacement in Right Ascension in radians per Julian year, referred to
	 * the Solar System Barycenter. This movement is in terms of coordinate
	 * angle on the celestial sphere. In the catalogs (specially the old
	 * ones), it is sometimes measured in arcseconds per Julian year as a true
	 * angle. If it is the case, as in the Bright Star Catalogue, you will have
	 * to divide it by COS(DECLINATION). If you are in doubts, check Alp UMi
	 * (Polaris) in the catalog. It's annual proper motion in RA is 3
	 * [coordinate "/year] = 0.04 [apparent "/year] / COS(89.2).
	 */
	public float properMotionRA;

	/**
	 * Displacement in Declination in radians per Julian year, referred to the
	 * Solar System Barycenter.
	 */
	public float properMotionDEC;

	/**
	 * Displacement in Radial velocity in km/s, referred to the Solar System
	 * Barycenter.
	 */
	public float properMotionRadialV;

	/**
	 * Reference equinox (and epoch) for the catalogue coordinates as a Julian day.
	 */
	public double equinox;

	/**
	 * Reference frame for the catalogue coordinates.
	 */
	public EphemerisElement.FRAME frame;

	/**
	 * Spectral type. Currently available only for JPARSEC file format.
	 */
	public String spectrum;

	/**
	 * Type of star: N for Normal, D for double or multiple, V for variable, and
	 * B for both double and variable. Only available for BSC5 and JPARSEC file
	 * formats. For JPARSEC file format additional information is available as three
	 * fields separated by ;. First field is one of the previous values N, D, V, B.
	 * Second is double star data (only if it is double or multiple). Third is variability
	 * data (if it is variable). Double data includes four fields separated by a comma
	 * (separation of main components in arcseconds, magnitude difference in components A-B,
	 * orbit period in years, position angle in degrees), while variability data includes 
	 * another four fields separated by a comma (maximum magnitude, minimum magnitude,
	 * period of variability in days, variable type).
	 */
	public String type;

	/**
	 * To clone the object.
	 */
	public StarElement clone()
	{
		if (this == null) return null;
		StarElement out = new StarElement(this.name, this.rightAscension, this.declination, this.parallax,
				this.magnitude, this.properMotionRA, this.properMotionDEC, this.properMotionRadialV,
				this.equinox, this.frame);
		out.spectrum = this.spectrum;
		out.type = this.type;
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
		StarElement se = (StarElement) s;
		if (se.properMotionRA != this.properMotionRA) equals = false;
		if (!se.name.equals(this.name)) equals = false;
		if (se.declination != this.declination) equals = false;
		if (se.parallax != this.parallax) equals = false;
		if (se.properMotionDEC != this.properMotionDEC) equals = false;
		if (se.magnitude != this.magnitude) equals = false;
		if (se.properMotionRadialV != this.properMotionRadialV) equals = false;
		if (se.rightAscension != this.rightAscension) equals = false;
		if (se.equinox != this.equinox) equals = false;
		if (se.frame != this.frame) equals = false;
		if (!se.spectrum.equals(this.spectrum)) equals = false;
		if (!se.type.equals(this.type)) equals = false;
		return equals;
	}

	/**
	 * Creates a star object from a Simbad one.
	 * 
	 * @param s Simbad object.
	 * @return Star object, with J2000 equinox and FK5 frame. In case
	 * parallax is 0 distance is arbitrarily set to 1000 pc.
	 */
	public static StarElement parseSimbadElement(SimbadElement s)
	{
		StarElement star = new StarElement(s.name, s.rightAscension, s.declination, s.parallax,
				0, (float) (s.properMotionRA), (float) s.properMotionDEC, (float) s.properMotionRadialV,
				Constant.J2000, EphemerisElement.FRAME.FK5);
		return star;
	}
	
	/**
	 * Returns the distance to the star in pc.
	 * @return Distance in pc, or 0 if parallax is 0.
	 */
	public double getDistance() {
		if (parallax == 0) return DISTANCE_UNKNOWN;
		return (1000.0 / parallax);
	}
	
	/**
	 * Returns if the distance to the star is unknown.
	 * @return True if parallax is 0, false otherwise.
	 */
	public boolean isDistanceUnknown() {
		return (parallax == 0);
	}
	
	/**
	 * Returns the equatorial position of this star.
	 * @return Equatorial position.
	 */
	public LocationElement getEquatorialPosition() {
		return new LocationElement(rightAscension, declination, getDistance());
	}
}
