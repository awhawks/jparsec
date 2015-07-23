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

import java.awt.Color;
import java.awt.Graphics2D;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target.TARGET;
import jparsec.ephem.planets.OrbitEphem;
import jparsec.ephem.planets.OrbitalElement;
import jparsec.graph.TextLabel;
import jparsec.graph.chartRendering.AWTGraphics;
import jparsec.io.FileIO;
import jparsec.io.ReadFile;
import jparsec.io.image.Picture;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeElement.SCALE;
import jparsec.util.JPARSECException;
import jparsec.vo.GeneralQuery;

/**
 * Convenient class for double stars ephemerides. Example:<P>
 * <pre>
 * try {
 * 		ReadFile re = new ReadFile();
 * 		re.setPath(DoubleStarElement.PATH_OLD_VISUAL_DOUBLE_STAR_CATALOG);
 * 		re.readOldFileOfDoubleStars();
 * 		System.out.println(re.getNumberOfObjects());
 * 		int index = re.searchByName("alpha centauri");
 * 		DoubleStarElement dstar = re.getDoubleStarElement(index);
 * 
 * 		AstroDate astro = new AstroDate(2010, 1, 1);
 * 		TimeElement time = new TimeElement(astro.jd(), SCALE.UNIVERSAL_TIME_UTC);
 * 		CityElement city = City.findCity("Madrid");
 * 		ObserverElement observer = ObserverElement.parseCity(city);
 * 
 * 		dstar.calcEphemeris(time, observer);
 * } catch (Exception e)
 * {
 *		e.printStackTrace();
 * } 
 * </pre> 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class DoubleStarElement {

	static final long serialVersionUID = 1L;

	/**
	 * Constructs an star object providing the values of the fields.
	 * 
	 * @param nom Name.
	 * @param ra Right Ascension in radians.
	 * @param dec Declination in radians.
	 * @param wds WDS designation.
	 * @param ads ADS designation.
	 * @param hd HP designation.
	 * @param hipp HipparcosS designation.
	 * @param magP Magnitude of primary.
	 * @param magS Magnitude of secondary.
	 * @param orbit The orbit object.
	 * @param orbitG Orbit grade.
	 * @param notes Contain a link to a notes file.
	 * @param ref Contain a link to a file of references.
	 * @param png Contain a link to a png file showing the orbit.
	 */
	public DoubleStarElement(String nom, double ra, double dec, String wds, String ads, String hd, String hipp, 
			String magP, String magS, OrbitalElement orbit, int orbitG, String notes, String ref, String png)
	{
		rightAscension = ra;
		declination = dec;
		name = nom;
		this.wds = wds;
		this.ads = ads;
		this.hd = hd;
		hipparcos = hipp;
		magPrimary = magP;
		magSecondary = magS;
		this.orbit = orbit;
		orbitGrade = orbitG;
		this.notes = notes;
		reference = ref;
		orbitPNG = png;
	}

	/**
	 * Constructs an empty star object.
	 */
	public DoubleStarElement()
	{
		rightAscension = 0.0;
		declination = 0.0;
		name = "";
		wds = "";
		ads = "";
		hd = "";
		hipparcos = "";
		magPrimary = "";
		magSecondary = "";
		orbit = null;
		orbitGrade = 0;
		notes = "";
		reference = "";
		orbitPNG = "";
	}

	/**
	 * Name of the star.
	 */
	public String name;
	/**
	 * WDS designation.
	 */
	public String wds;
	/**
	 * ADS designation.
	 */
	public String ads;
	/**
	 * HD designation.
	 */
	public String hd;
	/**
	 * Hipparcos designation.
	 */
	public String hipparcos;
	/**
	 * Magnitude of primary.
	 */
	public String magPrimary;
	/**
	 * Magnitude of secondary.
	 */
	public String magSecondary;
	/**
	 * Orbital elements for the star.
	 */
	public OrbitalElement orbit;
	/**
	 * Orbit grade, from 1 (reliable) to 5 (uncertain)
	 */
	public int orbitGrade;
	/**
	 * Link to notes.
	 */
	public String notes;
	/**
	 * Link to reference.
	 */
	public String reference;
	/**
	 * Link to online png file showing the orbit.
	 */
	public String orbitPNG;
	
	/**
	 * Right Ascension in radians from the catalogue.
	 */
	public double rightAscension;

	/**
	 * Declination in radians from the catalogue.
	 */
	public double declination;

	/**
	 * To clone the object.
	 */
	public DoubleStarElement clone()
	{
		if (this == null) return null;
		DoubleStarElement out = new DoubleStarElement(this.name, this.rightAscension, this.declination, wds, ads,
				hd, hipparcos, magPrimary, magSecondary, orbit.clone(), orbitGrade, notes, reference, orbitPNG);
		out.pa = this.pa;
		out.rho = this.rho;
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
		DoubleStarElement se = (DoubleStarElement) s;
		if (!se.name.equals(this.name)) equals = false;
		if (se.declination != this.declination) equals = false;
		if (se.rightAscension != this.rightAscension) equals = false;
		if (!se.wds.equals(this.wds)) equals = false;
		if (!se.ads.equals(this.ads)) equals = false;
		if (!se.hd.equals(this.hd)) equals = false;
		if (!se.hipparcos.equals(this.hipparcos)) equals = false;
		if (!se.magPrimary.equals(this.magPrimary)) equals = false;
		if (!se.magSecondary.equals(this.magSecondary)) equals = false;
		if (se.orbitGrade != this.orbitGrade) equals = false;
		if (!se.notes.equals(this.notes)) equals = false;
		if (!se.reference.equals(this.reference)) equals = false;
		if (!se.orbitPNG.equals(this.orbitPNG)) equals = false;
		if (!se.orbit.equals(this.orbit)) equals = false;
		if (se.pa != this.pa) equals = false;
		if (se.rho != this.rho) equals = false;
		return equals;
	}

	private double pa, rho;
	/**
	 * Return position angle.
	 * @return Position angle in radians, measured from North to East (North = 0 degrees, East = 90 degrees).
	 */
	public double getPositionAngle() {
		return pa;
	}

	/**
	 * Return distance.
	 * @return Distance in arcseconds.
	 */
	public double getDistance() {
		return rho;
	}

	/**
	 * The path to the sixth catalog of orbits of visual binary stars,
	 * Hartkopf 2006.
	 */
	public static final String PATH_VISUAL_DOUBLE_STAR_CATALOG =  FileIO.DATA_ORBITAL_ELEMENTS_DIRECTORY + "orb6orbits.txt";
	/**
	 * The path to the old fourth catalog of orbits of visual binary stars,
	 * Worley 1983.
	 */
	public static final String PATH_OLD_VISUAL_DOUBLE_STAR_CATALOG =  FileIO.DATA_ORBITAL_ELEMENTS_DIRECTORY + "worley.txt";

	/**
	 * Calculates ephemeris for this double star.
	 * @param time Time object.
	 * @param observer Observer object.
	 * @throws JPARSECException If an error occurs.
	 */
	public void calcEphemeris(TimeElement time, ObserverElement observer) throws JPARSECException {
		if (this.orbit == null) throw new JPARSECException("this double star has no set of orbital elements!");
		EphemerisElement eph = new EphemerisElement(TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
				EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
				EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.ORBIT, this.orbit);
		double pos[] = OrbitEphem.obtainPosition(time, observer, eph);
		
		rho = Math.sqrt(pos[0]*pos[0]+pos[1]*pos[1]);
		pa = Math.atan2(pos[1], pos[0]);		
	}
	
	/**
	 * Returns the equatorial position of this star.
	 * @return Equatorial position.
	 */
	public LocationElement getEquatorialPosition() {
		return new LocationElement(rightAscension, declination, 1);
	}
	
	/**
	 * Testing program.
	 * @param args Not used.
	 */
	public static void main(String args[]) {
		System.out.println("Double star ephemeris test");
		
		try {
			//String name = "alpha centauri";
			String name = "Eps Aur";
			
			ReadFile re = new ReadFile();
			re.setPath(DoubleStarElement.PATH_VISUAL_DOUBLE_STAR_CATALOG); //.PATH_OLD_VISUAL_DOUBLE_STAR_CATALOG);
			re.readFileOfDoubleStars();
			System.out.println(re.getNumberOfObjects());
			int index = re.searchByName(name);
			DoubleStarElement dstar = re.getDoubleStarElement(index);

			AstroDate astro = new AstroDate(2010, 1, 1);
			TimeElement time = new TimeElement(astro.jd(), SCALE.UNIVERSAL_TIME_UTC);
			CityElement city = City.findCity("Madrid");
			ObserverElement observer = ObserverElement.parseCity(city);

			dstar.calcEphemeris(time, observer);
			
			System.out.println(dstar.name+" RHO "+dstar.getDistance());
			System.out.println(dstar.name+" PA  "+Functions.formatAngleAsDegrees(dstar.getPositionAngle(), 3));
			
			if (!dstar.orbitPNG.equals("")) {
				try {
					Picture p = new Picture(GeneralQuery.queryImage(dstar.orbitPNG));
					p.show(dstar.name);
				} catch (Exception exc) {}
			}
			
			// Orbit sketch from JPARSEC, note image is inverted respect to previous one
			Picture pp = new Picture(dstar.orbit.getOrbitImage(dstar.orbit.name, 600, 600, 1.0, astro.jd(), false, true));
			Graphics2D g = pp.getImage().createGraphics();
			AWTGraphics.enableAntialiasing(g);
			g.setColor(Color.BLACK);
			String label1 = "@rho = "+Functions.formatValue(dstar.getDistance(), 3)+"\"";
			String label2 = "PA = "+Functions.formatAngleAsDegrees(dstar.getPositionAngle(), 3)+"º";
			TextLabel tl1 = new TextLabel(label1);
			tl1.draw(g, 10, 560);
			TextLabel tl2 = new TextLabel(label2);
			tl2.draw(g, 10, 580);
			pp.show("");
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
