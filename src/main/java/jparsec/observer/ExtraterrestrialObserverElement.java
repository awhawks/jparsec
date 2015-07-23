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
package jparsec.observer;

import java.io.Serializable;

import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target.TARGET;
import jparsec.ephem.planets.EphemElement;
import jparsec.ephem.planets.PlanetEphem;
import jparsec.graph.chartRendering.RenderPlanet;
import jparsec.io.ConsoleReport;
import jparsec.math.Constant;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;
import jparsec.time.TimeElement.SCALE;
import jparsec.util.*;

/**
 * A class to instantiate an observer outside the Earth, as a previous step
 * to calculate ephemerides. In the ephemerides properties the algorithm to
 * use must support this observer (Moshier, Series96, or any of the JPL 
 * integration methods, but not ELP2000 for the Moon). When using other
 * bodies take into account that local time = UTC on Earth, since no DST
 * rule can be applied. In practice, NEVER use local time when obtaining
 * ephemerides for an observer out from the Earth. 
 * 
 * @see EphemerisElement
 * @see ObserverElement
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class ExtraterrestrialObserverElement implements Serializable {
	static final long serialVersionUID = 1L;

	/**
	 * Basic constructor.
	 * @param n A name for the place.
	 * @param pos The mean barycentric equatorial vector (position and velocity) of the observer, J2000 equinox.
	 * @throws JPARSECException If the length or the position vector is not 6.
	 */
	public ExtraterrestrialObserverElement(String n, double pos[]) throws JPARSECException {
		if (pos.length != 6) throw new JPARSECException("Position vector must be of length 6 (position+velocity).");
		this.name = n;
		this.barycentricVector = pos;
	}

	/**
	 * Basic constructor.
	 * @param feature The name of a planetary feature.
	 * @param target The body where the feature is located.
	 * @throws JPARSECException If the feature is not found or the body is unsupported.
	 */
	public ExtraterrestrialObserverElement(String feature, TARGET target) throws JPARSECException {
		obsLoc = RenderPlanet.identifyFeature(feature, target);
		if (obsLoc != null) obsLoc.setRadius(0.0);
		name = feature;
		motherPlanet = target;
		if (target.isNaturalSatellite() && (target.ordinal() < TARGET.Phobos.ordinal() || target.ordinal() > TARGET.Oberon.ordinal()))
			throw new JPARSECException("unsupported body. Use one of the main satellites of Mars, Jupiter, Saturn, or Uranus.");
	}

	/**
	 * Full constructor for an observer located on the surface of another non-Earth planet/satellite.
	 * @param feature The name of a planetary feature. Just informative, not used internally.
	 * @param target The body where the feature is located.
	 * @param loc The location of the observer. Here you must be careful to put the correct 
	 * longitude on a given planet, since the longitude to use here is the one calculated using
	 * the IAU recomendations for the prime meridian of the planets, with has opposite sign in
	 * some cases respect planetary nomenclature. The cases are Mars and the rest of outer
	 * planets/satellites from Earth. For instance, Curiosity landed at 137.4º E of longitude
	 * (and 4.5º S of latitude), here you should put -137.4 degrees (in radians). The radius
	 * of the location object should be the desired elevation above sea level in m. You can use 0.
	 * @throws JPARSECException If the body is not supported.
	 */
	public ExtraterrestrialObserverElement(String feature, TARGET target, LocationElement loc) throws JPARSECException {
		obsLoc = loc;
		name = feature;
		motherPlanet = target;
		if (target.isNaturalSatellite() && (target.ordinal() < TARGET.Phobos.ordinal() || target.ordinal() > TARGET.Oberon.ordinal()))
				throw new JPARSECException("unsupported body. Use one of the main satellites of Mars, Jupiter, Saturn, or Uranus.");
	}

	/** The mean equatorial J2000 barycentric vector of the observer (AU, AU/d). This vector is only
	 * used when the observer is located in the space, not in the surface of another body. */
	public double barycentricVector[];
	/** A name for the place. */
	public String name;
	/** The mother planet where the observer is located. */
	public TARGET motherPlanet = null;
	/** The observer location in the mother planet. */
	public LocationElement obsLoc = null;
	
	/**
	 * Test program.
	 * @param args Not used.
	 */
	public static void main(String args[]) {
		System.out.println("ET test");
		try {
			AstroDate astro = new AstroDate(2012, 1, 1, 0, 0, 0);
			TimeElement time = new TimeElement(astro, SCALE.UNIVERSAL_TIME_UTC);
			EphemerisElement eph = new EphemerisElement(TARGET.JUPITER, EphemerisElement.COORDINATES_TYPE.APPARENT,
					EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
					EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000);
			
			CityElement city = City.findCity("Madrid");
			ObserverElement observer = ObserverElement.parseCity(city);
			
			// Move observer towards Jupiter, at 0.1 AU of distance
			double pos[] = Ephem.eclipticToEquatorial(PlanetEphem.getHeliocentricEclipticPositionJ2000(TimeScale.getJD(time, observer, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME), TARGET.JUPITER), Constant.J2000, eph);
			LocationElement loc = LocationElement.parseRectangularCoordinates(pos);
			loc.setRadius(loc.getRadius()-0.1);
			pos = loc.getRectangularCoordinates();
			pos = new double[] {pos[0], pos[1], pos[2], 0, 0, 0};
			observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("Close to Jupiter", pos));

			EphemElement ephem = Ephem.getEphemeris(time, observer, eph, false);
			ConsoleReport.basicEphemReportToConsole(ephem);

	
			// Mars, loc: lon = 184.702,  lat = -14.64;
			// Az/El Sun                Horizons             JPARSEC ('simple mode')Mars24
			// 2010-Jan-06 00:00 UT1    284.5225   1.1738    284.5379    1.2618     284.6525    1.1303
			// 2004-Jan-03 13:46:31 UT1 179.9952 -62.0741    180.0387  -62.1659     179.9890  -61.9392
			// (lon=lat=0)2000-Jan-06 00:00 UT1    191.1564 -64.5049    191.1364  -64.5079     191.0398  -64.2616
			astro = new AstroDate(2010, 1, 6, 0, 0, 0);
			//astro = new AstroDate(2004, 1, 3, 13, 46, 31);
			time = new TimeElement(astro, SCALE.UNIVERSAL_TIME_UT1);
			eph = new EphemerisElement(TARGET.SUN, EphemerisElement.COORDINATES_TYPE.APPARENT,
					EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
					EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000);
			
			observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("Marte", TARGET.MARS, 
					new LocationElement(184.702 * Constant.DEG_TO_RAD, -14.64*Constant.DEG_TO_RAD, 1.0)));

			ephem = Ephem.getEphemeris(time, observer, eph, false);
			ConsoleReport.basicEphemReportToConsole(ephem);

/*			
			// Check TSL Earth
			eph.targetBody = TARGET.EARTH;
			observer.forceObserverOnEarth();
			System.out.println(SiderealTime.apparentSiderealTime(time, observer, eph)*Constant.RAD_TO_DEG);
			double JD_TDB = TimeScale.getJD(time, observer, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
			System.out.println((PhysicalParameters.getBodySiderealTimeAt0Lon(JD_TDB, eph)+observer.getLongitudeRad())*Constant.RAD_TO_DEG);
*/
/*			
			// Check coord. rotation body <-> Earth
			LocationElement loc1 = new LocationElement(184.702 * Constant.DEG_TO_RAD, 89*Constant.DEG_TO_RAD, 1.0);
			LocationElement loc2 = Ephem.getPositionFromBody(loc1, time, observer, eph);
			LocationElement loc3 = Ephem.getPositionFromEarth(loc2, time, observer, eph);
			System.out.println(loc1.toStringAsEquatorialLocation());
			System.out.println(loc2.toStringAsEquatorialLocation());
			System.out.println(loc3.toStringAsEquatorialLocation());
*/			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
