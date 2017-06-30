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
package jparsec.test;

import java.io.*;

import jparsec.astronomy.AtlasChart;
import jparsec.astronomy.Constellation;
import jparsec.astronomy.CoordinateSystem;
import jparsec.astrophysics.MeasureElement;
import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Nutation;
import jparsec.ephem.Obliquity;
import jparsec.ephem.Target;
import jparsec.ephem.Target.TARGET;
import jparsec.ephem.event.LunarEclipse;
import jparsec.ephem.event.LunarEvent;
import jparsec.ephem.event.MainEvents;
import jparsec.ephem.event.MoonEvent;
import jparsec.ephem.event.MoonEventElement;
import jparsec.ephem.event.SimpleEventElement;
import jparsec.ephem.event.SolarEclipse;
import jparsec.ephem.moons.GUST86;
import jparsec.ephem.moons.L1;
import jparsec.ephem.moons.Mars07;
import jparsec.ephem.moons.TASS17;
import jparsec.ephem.planets.EphemElement;
import jparsec.ephem.planets.JPLEphemeris;
import jparsec.ephem.stars.DoubleStarElement;
import jparsec.ephem.stars.StarElement;
import jparsec.ephem.stars.StarEphem;
import jparsec.ephem.stars.StarEphemElement;
import jparsec.ephem.stars.VariableStarElement;
import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.io.ReadFile;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.EarthOrientationParameters;
import jparsec.observer.ExtraterrestrialObserverElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.observer.ObserverElement.DST_RULE;
import jparsec.observer.ReferenceEllipsoid.ELLIPSOID;
import jparsec.time.AstroDate;
import jparsec.time.SiderealTime;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;
import jparsec.time.TimeScale;
import jparsec.time.TimeElement.SCALE;
import jparsec.time.calendar.CalendarGenericConversion;
import jparsec.time.calendar.CalendarGenericConversion.CALENDAR;
import jparsec.util.Configuration;
import jparsec.util.JPARSECException;
import jparsec.util.Logger;
import jparsec.util.Logger.LEVEL;
import jparsec.vo.SimbadElement;
import jparsec.vo.SimbadQuery;

/**
 * Stores information about tests and implements them.
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class TestElement implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Event id code. Constants defined in the enumeration in this class.
	 */
	public TEST testID;
	/**
	 * Holds the testing values (required input values to compute something).
	 */
	public String[] testValues;
	/**
	 * Holds the expected values that should be obtained when executing the test
	 * with a given set of input values.
	 */
	public String[] expectedValues;
	/**
	 * Holds the values found after executing the test. They are not always available.
	 */
	private String[] foundValues;

	/**
	 * Output and error logs.
	 */
	private String out, err;

	/**
	 * Constructor for a simple test.
	 * @param id Event ID constant.
	 * @param input Input values.
	 * @param output Expected output.
	 */
	public TestElement(TEST id, String[] input, String[] output)
	{
		this.testValues = input;
		this.testID = id;
		this.expectedValues = output;
	}

	/**
	 * Clones this instance.
	 */
	@Override
	public TestElement clone()
	{
		TestElement e = new TestElement(testID, this.testValues, this.expectedValues);
		e.out = out;
		e.err = err;
		e.foundValues = foundValues.clone();
		return e;
	}
	/**
	 * Returns whether the input Object contains the same information
	 * as this instance.
	 */
	public boolean equals(Object e)
	{
		if (e == null) return false;
		TestElement ee = (TestElement) e;
		if (!DataSet.sameArrayValues(ee.testValues, this.testValues)) return false;
		if (!DataSet.sameArrayValues(ee.expectedValues, this.expectedValues)) return false;
		if (!DataSet.sameArrayValues(ee.foundValues, this.foundValues)) return false;
		if (ee.testID != this.testID) return false;
		if (!ee.err.equals(this.err)) return false;
		if (!ee.out.equals(this.out)) return false;
		return true;
	}

	/**
	 * Returns the output from the test, if it was executed.
	 * @return The output.
	 */
	public String getOutput() {
		return out;
	}

	/**
	 * Returns the error output from the test, if it was executed.
	 * @return The error output.
	 */
	public String getErrorOutput() {
		return err;
	}

	/**
	 * Returns the values found after executing the test.
	 * @return Values found.
	 */
	public String[] getValuesFound() {
		return foundValues;
	}

	private EphemElement getPlanetOrStarEphem(TimeElement time,
			ObserverElement observer, EphemerisElement eph,
			String body, boolean preferPrecision, boolean topocentric,
			boolean full_ephem) {
		eph.isTopocentric = topocentric;
		eph.correctForEOP = eph.correctForPolarMotion = false;
		try {
			TARGET index = Target.getIDFromEnglishName(body);
			if (index == TARGET.NOT_A_PLANET) throw new JPARSECException("it is a star");
			// Is a planet
			eph.targetBody = index;
			eph.algorithm = EphemerisElement.ALGORITHM.MOSHIER; // Default value, overriden by Ephem.getEphemeris
			if (index.isNaturalSatellite()) eph.algorithm = EphemerisElement.ALGORITHM.NATURAL_SATELLITE;
			return Ephem.getEphemeris(time, observer, eph, full_ephem, preferPrecision);
		} catch (JPARSECException exc) {
			// Is a star
			try {
				int index = StarEphem.getStarTargetIndex(body);
				if (index < 0) index = StarEphem.getStarTargetIndex(StarEphem.getCatalogNameFromProperName(body));
				if (index < 0) return null;
				eph.targetBody = TARGET.NOT_A_PLANET;
				eph.targetBody.setIndex(index);
				eph.algorithm = EphemerisElement.ALGORITHM.STAR;
				return Ephem.getEphemeris(time, observer, eph, false, preferPrecision);
			} catch (Exception e) {
				return null;
			}
		}
	}

	/**
	 * Executes the test.
	 * @throws JPARSECException If an error occurs.
	 */
	public void executeTest() throws JPARSECException {
		out = "";
		err = FileIO.getLineSeparator();

		AstroDate astro = new AstroDate(2000, AstroDate.JANUARY, 1, 12, 0, 0);
		TimeElement time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);
		//CityElement city = City.findCity("Madrid", COUNTRY.Spain, false);
		//ObserverElement observer = ObserverElement.parseCity(city);
		ObserverElement observer = new ObserverElement("Madrid", Functions.parseDeclination("-03\u00b0 42' 36.000\""),
				Functions.parseDeclination("40\u00b0 25' 12.000\""), 693, 1, DST_RULE.N1);
		EphemerisElement eph = new EphemerisElement(TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
				EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
				EphemerisElement.FRAME.ICRF);
		eph.correctForEOP = false;

		String externalPath = Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH;
		switch (testID) {
		case ATLAS_CHART:
			double ra = DataSet.getDoubleValueWithoutLimit(testValues[0]) / Constant.RAD_TO_HOUR,
				dec = DataSet.getDoubleValueWithoutLimit(testValues[1]) * Constant.DEG_TO_RAD;
			LocationElement loc = new LocationElement(ra, dec, 1.0);
			String ou1 = AtlasChart.atlasChart(loc, AtlasChart.ATLAS.MILLENIUM_STAR);
			String ou2 = AtlasChart.atlasChart(loc, AtlasChart.ATLAS.SKY_ATLAS_2000);
			String ou3 = AtlasChart.atlasChart(loc, AtlasChart.ATLAS.URANOMETRIA);
			String ou4 = AtlasChart.atlasChart(loc, AtlasChart.ATLAS.URANOMETRIA_2nd_EDITION);
			String ou5 = AtlasChart.atlasChart(loc, AtlasChart.ATLAS.RUKL);

			foundValues = new String[] {ou1, ou2, ou3, ou4, ou5};
			for (int i=0; i<expectedValues.length; i++) {
				if (!expectedValues[i].trim().equals("-") && !foundValues[i].trim().equals(expectedValues[i].trim())) err += "Output value number "+(i+1)+" is "+foundValues[i].trim()+" and should be "+expectedValues[i].trim()+FileIO.getLineSeparator();
			}
			break;
		case CONSTELLATION:
			ra = DataSet.getDoubleValueWithoutLimit(testValues[0]) / Constant.RAD_TO_HOUR;
			dec = DataSet.getDoubleValueWithoutLimit(testValues[1]) * Constant.DEG_TO_RAD;
			String c = Constellation.getConstellationName(ra, dec, Constant.J2000, eph);
			foundValues = new String[] {c};
			if (!c.equals(expectedValues[0].trim())) err += "Found constellation name "+c+" and it should be (start with) "+expectedValues[0];
			break;
		case COORDINATE_SYSTEMS:
			eph.equinox = Constant.J2000;
			eph.ephemType = EphemerisElement.COORDINATES_TYPE.GEOMETRIC;
			eph.frame = EphemerisElement.FRAME.FK5;
			double lon = DataSet.getDoubleValueWithoutLimit(testValues[0]) * Constant.DEG_TO_RAD;
			double lat = DataSet.getDoubleValueWithoutLimit(testValues[1]) * Constant.DEG_TO_RAD;
			int s0 = DataSet.getIndex(CoordinateSystem.COORDINATE_SYSTEMS, testValues[2].trim());
			int s1 = DataSet.getIndex(CoordinateSystem.COORDINATE_SYSTEMS, testValues[3].trim());
			LocationElement out = CoordinateSystem.transform(CoordinateSystem.COORDINATE_SYSTEM.values()[s0], CoordinateSystem.COORDINATE_SYSTEM.values()[s1], new LocationElement(lon, lat, 1.0), time, observer, eph);
			foundValues = new String[] {
					Functions.formatAngle(out.getLongitude(), 3),
					Functions.formatAngle(out.getLatitude(), 3)};
			if (!expectedValues[0].trim().equals("-") && !foundValues[0].startsWith(expectedValues[0].trim())) err += "Found position "+foundValues[0]+" and it should be "+expectedValues[0];
			if (!expectedValues[1].trim().equals("-") && !foundValues[1].startsWith(expectedValues[1].trim())) err += "Found position "+foundValues[1]+" and it should be "+expectedValues[1];
			break;
		case EPHEMERIDES:
			Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH = null;
			eph.isTopocentric = false;

			double in[] = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 4));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2], (int) in[3], (int) in[4], (in[4]-(int) in[4])*60.0);

			time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
			String foregroundBody = testValues[5].trim(), backgroundBody = testValues[6].trim();

			// Test with accurate ephemerides
			boolean preferPrecision = true, topocentric = false;
			EphemElement foregroundEphem = getPlanetOrStarEphem(time, observer, eph, foregroundBody, preferPrecision, topocentric, false);
			EphemElement backgroundEphem = getPlanetOrStarEphem(time, observer, eph, backgroundBody, preferPrecision, topocentric, false);
			if (foregroundEphem == null) {
				err += "could not identify "+foregroundBody+FileIO.getLineSeparator();
				break;
			}
			if (backgroundEphem == null) {
				err += "could not identify "+backgroundBody+FileIO.getLineSeparator();
				break;
			}
			double dist = LocationElement.getAngularDistance(foregroundEphem.getEquatorialLocation(), backgroundEphem.getEquatorialLocation());
			double foregroundParallax = Math.atan2(Constant.EARTH_RADIUS / Constant.AU, foregroundEphem.distance);
			double backgroundParallax = Math.atan2(Constant.EARTH_RADIUS / Constant.AU, backgroundEphem.distance);
			double sizes = foregroundEphem.angularRadius + backgroundEphem.angularRadius + foregroundParallax + backgroundParallax;
			if (dist > sizes) err += "(Accurate) The positions of "+foregroundBody+" and "+backgroundBody+" are separated by "+Functions.formatAngle(dist, 3)+", which is more than the sum of their sizes and parallaxes ("+Functions.formatAngle(sizes, 3)+")"+FileIO.getLineSeparator();

			// Test with less accurate ephemerides
			preferPrecision = false;
			foregroundEphem = getPlanetOrStarEphem(time, observer, eph, foregroundBody, preferPrecision, topocentric, false);
			backgroundEphem = getPlanetOrStarEphem(time, observer, eph, backgroundBody, preferPrecision, topocentric, false);
			if (foregroundEphem == null) {
				err += "could not identify "+foregroundBody+FileIO.getLineSeparator();
				break;
			}
			if (backgroundEphem == null) {
				err += "could not identify "+backgroundBody+FileIO.getLineSeparator();
				break;
			}
			dist = LocationElement.getAngularDistance(foregroundEphem.getEquatorialLocation(), backgroundEphem.getEquatorialLocation());
			foregroundParallax = Math.atan2(Constant.EARTH_RADIUS / Constant.AU, foregroundEphem.distance);
			backgroundParallax = Math.atan2(Constant.EARTH_RADIUS / Constant.AU, backgroundEphem.distance);
			sizes = foregroundEphem.angularRadius + backgroundEphem.angularRadius + foregroundParallax + backgroundParallax;
			if (dist > sizes) err += "(Less accurate) The positions of "+foregroundBody+" and "+backgroundBody+" are separated by "+Functions.formatAngle(dist, 3)+", which is more than the sum of their sizes and parallaxes ("+Functions.formatAngle(sizes, 3)+")"+FileIO.getLineSeparator();

			break;
		case EPHEMERIDES_NATURAL_SATELLITES:
			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 5));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2], (int) in[3], (int) in[4], (int) in[5]);
			time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
			String cityName = testValues[6].trim();
			CityElement city = City.findCity(cityName);
			observer = ObserverElement.parseCity(city);
			foregroundBody = testValues[7].trim();
			backgroundBody = testValues[8].trim();
			String type = testValues[9].toLowerCase().trim();
			boolean isEclipse = false;
			if (type.indexOf("ecl") >= 0) isEclipse = true;

			// Test with accurate ephemerides
			preferPrecision = true;
			topocentric = true;
			foregroundEphem = getPlanetOrStarEphem(time, observer, eph, foregroundBody, preferPrecision, topocentric, false);
			backgroundEphem = getPlanetOrStarEphem(time, observer, eph, backgroundBody, preferPrecision, topocentric, false);
			if (foregroundEphem == null) {
				err += "could not identify "+foregroundBody+FileIO.getLineSeparator();
				break;
			}
			if (backgroundEphem == null) {
				err += "could not identify "+backgroundBody+FileIO.getLineSeparator();
				break;
			}
			dist = LocationElement.getAngularDistance(foregroundEphem.getEquatorialLocation(), backgroundEphem.getEquatorialLocation());
			sizes = foregroundEphem.angularRadius + backgroundEphem.angularRadius;
			if (!isEclipse) {
				if (dist > sizes) err += "(Accurate) The positions of "+foregroundBody+" and "+backgroundBody+" are separated by "+Functions.formatAngle(dist, 3)+", which is more than the sum of their sizes ("+Functions.formatAngle(sizes, 3)+")"+FileIO.getLineSeparator();
				if (backgroundEphem.status == null || backgroundEphem.status.toLowerCase().indexOf("occulted by") < 0)
					err += "(Accurate) Mutual occultation not reported by MoonEphem class."+FileIO.getLineSeparator();
			} else {
				if (backgroundEphem.status == null || backgroundEphem.status.toLowerCase().indexOf("eclipsed by") < 0)
					err += "(Accurate) Mutual eclipse not reported by MoonEphem class."+FileIO.getLineSeparator();
			}

			// Test with less accurate ephemerides
			preferPrecision = false;
			foregroundEphem = getPlanetOrStarEphem(time, observer, eph, foregroundBody, preferPrecision, topocentric, true);
			backgroundEphem = getPlanetOrStarEphem(time, observer, eph, backgroundBody, preferPrecision, topocentric, true);
			if (foregroundEphem == null) {
				err += "could not identify "+foregroundBody+FileIO.getLineSeparator();
				break;
			}
			if (backgroundEphem == null) {
				err += "could not identify "+backgroundBody+FileIO.getLineSeparator();
				break;
			}
			dist = LocationElement.getAngularDistance(foregroundEphem.getEquatorialLocation(), backgroundEphem.getEquatorialLocation());
			sizes = foregroundEphem.angularRadius + backgroundEphem.angularRadius;
			if (!isEclipse) {
				if (dist > sizes) this.out += "(Less accurate) The positions of "+foregroundBody+" and "+backgroundBody+" are separated by "+Functions.formatAngle(dist, 3)+", which is more than the sum of their sizes ("+Functions.formatAngle(sizes, 3)+")"+FileIO.getLineSeparator();
				if (backgroundEphem.status == null || backgroundEphem.status.toLowerCase().indexOf("occulted by") < 0) {
					if (dist > sizes) {
						this.out += "(Less accurate) Mutual occultation not reported by MoonEphem class.";
					} else {
						this.err += "(Less accurate) Mutual occultation occurs (dist < sizes => "+Functions.formatAngle(dist, 3)+" < "+Functions.formatAngle(sizes, 3)+") but it is not reported by MoonEphem class.";
					}
				}
			} else {
				if (backgroundEphem.status == null || backgroundEphem.status.toLowerCase().indexOf("eclipsed by") < 0)
					this.out += "(Less accurate) Mutual eclipse not reported by MoonEphem class.";
			}

			break;
		case JULIAN_DAY:
			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 3));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2], (int) in[3], 0, 0);
			double jd = astro.jd();
			foundValues = new String[] {""+jd};
			if (!expectedValues[0].trim().equals(foundValues[0].trim())) err += "Found Julian day number "+foundValues[0]+" and it should be "+expectedValues[0];
			break;
		case TIME_SCALES:
			eph.correctForEOP = true;
			String timeScale = "";
			double ms = 0;
			if (testValues.length > 6) {
				in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 5));
				astro = new AstroDate((int) in[0], (int) in[1], (int) in[2], (int) in[3], (int) in[4], (int) in[5]);
				timeScale = testValues[6].trim();
				ms = Double.parseDouble(testValues[7].trim());
			} else {
				astro = new AstroDate(Double.parseDouble(testValues[0]));
				timeScale = testValues[1].trim();
				ms = Double.parseDouble(testValues[2].trim());
			}
			int ts = DataSet.getIndex(TimeElement.TIME_SCALES_ABBREVIATED, timeScale);
			time = new TimeElement(astro, SCALE.values()[ts]);

			double jd_lt = TimeScale.getJD(time, observer, eph, SCALE.LOCAL_TIME);
			double jd_ut1 = TimeScale.getJD(time, observer, eph, SCALE.UNIVERSAL_TIME_UT1);
			double jd_utc = TimeScale.getJD(time, observer, eph, SCALE.UNIVERSAL_TIME_UTC);
			double jd_tt = TimeScale.getJD(time, observer, eph, SCALE.TERRESTRIAL_TIME);
			double jd_tdb = TimeScale.getJD(time, observer, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);

			foundValues = new String[] {""+jd_lt, ""+jd_ut1, ""+jd_utc, ""+jd_tt, ""+jd_tdb};
			for (int i=0; i<foundValues.length; i++) {
				String expected = expectedValues[i].trim(), found = foundValues[i].trim();
				if (ms > 0) {
					double e = Double.parseDouble(expected), f = Double.parseDouble(found);
					double dms = Math.abs(e-f) * Constant.SECONDS_PER_DAY * 1000.0;
					if (dms > ms) err += "Found Julian day number "+found+" ("+TimeElement.TIME_SCALES_ABBREVIATED[i]+") and it should be "+expected+" (difference greater than "+ms+" ms)" + FileIO.getLineSeparator();
//					expected = Functions.formatValue(new Double(expected), places);
//					found = Functions.formatValue(new Double(found), places);
//					if (!expected.equals(found)) err += "Found Julian day number "+found+" ("+TimeElement.TIME_SCALES_ABBREVIATED[i]+") and it should be "+expected+ FileIO.getLineSeparator();
				} else {
					if (!expected.equals(found)) err += "Found Julian day number "+found+" ("+TimeElement.TIME_SCALES_ABBREVIATED[i]+") and it should be "+expected+ FileIO.getLineSeparator();
				}
			}
			break;
		case MEASURES:
			MeasureElement me1 = new MeasureElement(Double.parseDouble(testValues[0]), Double.parseDouble(testValues[1]), testValues[2].trim());
			me1.convert(testValues[3].trim());
			foundValues = new String[] {(""+me1.value).trim(), (""+me1.error).trim(), me1.toString().trim()};
			for (int i=0; i<3; i++) {
				if (!foundValues[i].equals(expectedValues[i].trim())) {
					err += "Found values "+DataSet.toString(foundValues, ", ")+" and they should be "+DataSet.toString(expectedValues, ",").trim() + FileIO.getLineSeparator();
					break;
				}
			}
			break;
		case CALENDARS:
			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 2));
			int cal = DataSet.getIndex(CalendarGenericConversion.CALENDAR_NAMES, testValues[3].trim());

			if (cal < 0) {
				err += "Cannot find calendar '"+testValues[3].trim()+"'";
			} else {
				int[] outCal = new int[] {CALENDAR.GREGORIAN.ordinal(), CALENDAR.JULIAN.ordinal(),
						CALENDAR.HEBREW.ordinal(), CALENDAR.COPTIC.ordinal(), CALENDAR.ETHIOPIC.ordinal(),
						CALENDAR.PERSIAN.ordinal(), CALENDAR.ISLAMIC.ordinal(), CALENDAR.HINDU_SOLAR.ordinal()};
				foundValues = new String[outCal.length];
				for (int i=0; i<outCal.length; i++) {
					if (outCal[i] != -1) {
						int outDate[] = CalendarGenericConversion.GenericConversion(CALENDAR.values()[cal], CALENDAR.values()[outCal[i]], (int) in[0], (int) in[1], (int) in[2]);
						String o = ""+outDate[0]+" "+outDate[1]+" "+outDate[2];
						//if (outCal[i] == CalendarGenericConversion.JULIAN) {
						//	Julian jul = new Julian(outDate[0], outDate[1], outDate[2]);
						//	o = ""+jul.toJulianDay();
						//}
						foundValues[i] = o;
						if (!o.equals(expectedValues[i].trim())) err += "Expected output from "+CalendarGenericConversion.CALENDAR_NAMES[outCal[i]]+" calendar was "+expectedValues[i].trim()+", but "+o+" was found instead."+FileIO.getLineSeparator();
					}
				}
			}
			break;
		case GEODETIC_AND_GEOCENTRIC_COORDINATES:
			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 2));
			ELLIPSOID re = ELLIPSOID.WGS84;
			if (testValues[4].toLowerCase().trim().equals("iers2003")) {
				re = ELLIPSOID.IERS2003;
			} else {
				if (!testValues[4].toLowerCase().trim().equals("wgs84")) {
					err += "Cannot understand ellipsoid "+testValues[4];
					break;
				}
			}
			if (testValues[3].toLowerCase().trim().equals("geodetic")) {
				LocationElement geoloc = ObserverElement.geodeticToGeocentric(re, in[0] * Constant.DEG_TO_RAD, in[1] * Constant.DEG_TO_RAD, (int) in[2]);
				foundValues = new String[] {
						""+(geoloc.getLongitude() * Constant.RAD_TO_DEG),
						""+(geoloc.getLatitude() * Constant.RAD_TO_DEG),
						""+(geoloc.getRadius())
						};
				for (int i=0; i<foundValues.length; i++) {
					if (!expectedValues[i].trim().equals("-") && !foundValues[i].trim().startsWith(expectedValues[i].trim()))
						err += "Expected "+expectedValues[i].trim()+", but found "+foundValues[i].trim()+" instead."+FileIO.getLineSeparator();
				}
			} else {
				if (testValues[3].toLowerCase().trim().equals("geocentric")) {
					LocationElement geoloc = ObserverElement.geocentricToGeodetic(re, in[0] * Constant.DEG_TO_RAD, in[1] * Constant.DEG_TO_RAD, 1.0 + in[2] / (1000.0 * re.getEquatorialRadius()));
					foundValues = new String[] {
							""+(geoloc.getLongitude() * Constant.RAD_TO_DEG),
							""+(geoloc.getLatitude() * Constant.RAD_TO_DEG),
							""+(geoloc.getRadius())
							};
					for (int i=0; i<foundValues.length; i++) {
						if (!expectedValues[i].trim().equals("-") && !foundValues[i].trim().startsWith(expectedValues[i].trim()))
							err += "Expected "+expectedValues[i].trim()+", but found "+foundValues[i].trim()+" instead."+FileIO.getLineSeparator();
					}
				} else {
					err += "Cannot understand coordinates type "+testValues[3];
					break;
				}
			}
			break;
		case DETAILED_EPHEMERIDES:
			Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH = null;
			eph.correctForPolarMotion = false;

			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 2));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2]);
			time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);
			TARGET body = Target.getIDFromEnglishName(testValues[3].trim());
			double precision1 = Double.parseDouble(testValues[4].trim()) * Constant.ARCSEC_TO_RAD;
			double precision2 = Double.parseDouble(testValues[5].trim());

			if (body == TARGET.NOT_A_PLANET) {
				err += "Target body "+testValues[3].trim()+" cannot be identified.";
				break;
			}
			EphemerisElement.COORDINATES_TYPE ephem[] = new EphemerisElement.COORDINATES_TYPE[] {
					EphemerisElement.COORDINATES_TYPE.APPARENT, EphemerisElement.COORDINATES_TYPE.ASTROMETRIC, EphemerisElement.COORDINATES_TYPE.GEOMETRIC
			};
			EphemerisElement.ALGORITHM algor[] = new EphemerisElement.ALGORITHM[] {
					EphemerisElement.ALGORITHM.JPL_DE405, EphemerisElement.ALGORITHM.JPL_DE200,
					EphemerisElement.ALGORITHM.JPL_DE403, EphemerisElement.ALGORITHM.JPL_DE413,
					EphemerisElement.ALGORITHM.JPL_DE414, EphemerisElement.ALGORITHM.MOSHIER,
					EphemerisElement.ALGORITHM.VSOP87_ELP2000ForMoon, EphemerisElement.ALGORITHM.SERIES96_MOSHIERForMoon
			};
			eph.targetBody = body;
			eph.isTopocentric = false;
			eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_1976;

			boolean isNaturalSatellite = body.isNaturalSatellite();
			if (isNaturalSatellite) algor = new EphemerisElement.ALGORITHM[] {EphemerisElement.ALGORITHM.NATURAL_SATELLITE};

			for (int i=0; i<ephem.length; i++) {
				eph.ephemType = ephem[i];
				EphemElement ephems[] = new EphemElement[algor.length];
				eph.equinox = EphemerisElement.EQUINOX_OF_DATE;
				if (eph.ephemType == EphemerisElement.COORDINATES_TYPE.ASTROMETRIC) eph.equinox = EphemerisElement.EQUINOX_J2000;
				for (int j=0; j<algor.length; j++) {
					if (algor[j] != EphemerisElement.ALGORITHM.VSOP87_ELP2000ForMoon || eph.targetBody != TARGET.Pluto) {
						eph.algorithm = algor[j];
						ephems[j] = Ephem.getEphemeris(time, observer, eph, false);
						if (j > 0) {
							double diffs[] = new double[] {
									ephems[j].rightAscension - ephems[0].rightAscension,
									ephems[j].declination - ephems[0].declination,
									ephems[j].angularRadius - ephems[0].angularRadius,
									ephems[j].distance - ephems[0].distance,
									ephems[j].distanceFromSun - ephems[0].distanceFromSun,
									ephems[j].elongation - ephems[0].elongation,
									ephems[j].heliocentricEclipticLatitude - ephems[0].heliocentricEclipticLatitude,
									ephems[j].heliocentricEclipticLongitude - ephems[0].heliocentricEclipticLongitude,
									ephems[j].lightTime - ephems[0].lightTime,
									ephems[j].longitudeOfCentralMeridian - ephems[0].longitudeOfCentralMeridian,
									ephems[j].longitudeOfCentralMeridianSystemI - ephems[0].longitudeOfCentralMeridianSystemI,
									ephems[j].longitudeOfCentralMeridianSystemII - ephems[0].longitudeOfCentralMeridianSystemII,
									ephems[j].longitudeOfCentralMeridianSystemIII - ephems[0].longitudeOfCentralMeridianSystemIII,
									ephems[j].magnitude - ephems[0].magnitude,
									ephems[j].phase - ephems[0].phase,
									ephems[j].phaseAngle - ephems[0].phaseAngle,
									ephems[j].positionAngleOfAxis - ephems[0].positionAngleOfAxis,
									ephems[j].positionAngleOfPole - ephems[0].positionAngleOfPole,
									ephems[j].surfaceBrightness - ephems[0].surfaceBrightness,
									ephems[j].subsolarLongitude - ephems[0].subsolarLongitude,
									ephems[j].subsolarLatitude - ephems[0].subsolarLatitude,
							};
							String fields[] = new String[] {
									"RA", "DEC", "Angular radius", "Distance", "Distance from Sun", "Elongation",
									"Heliocentric ecliptic latitude", "Heliocentric ecliptic longitude", "Light time",
									"Longitude central meridian", "Longitude central meridian I", "Longitude central meridian II",
									"Longitude central meridian III", "Magnitude", "Phase", "Phase angle", "PA axis", "PA pole",
									"Surface brightness", "Subsolar longitude", "Subsolar latitude"
							};
							double acceptableDiffs[] = new double[] {precision1, precision1, precision1,
									precision2, precision2, precision1, precision1, precision1,
									precision2 * Constant.LIGHT_TIME_DAYS_PER_AU, precision1, precision1,
									precision1, precision1,
									0.01, 0.001, precision1, precision1, precision1, 0.01,
									precision1, precision1};
							String alg = algor[j].name();
							String ctype = ephem[i].name();
							for (int k=0; k<diffs.length; k++) {
								String f = fields[k];
								if (Math.abs(diffs[k]) > Math.PI) diffs[k] = Math.abs(diffs[k]) - Constant.TWO_PI;
								if (Math.abs(diffs[k]) > acceptableDiffs[k]) {
									if (acceptableDiffs[k] == precision1) {
										err += "Difference in field "+f+" between "+alg+" and "+algor[0].name()+" for "+ctype+" coordinates is "+Functions.formatAngle(Math.abs(diffs[k]), 1)+" > "+Functions.formatAngle(acceptableDiffs[k], 1)+"." + FileIO.getLineSeparator();
									} else {
										err += "Difference in field "+f+" between "+alg+" and "+algor[0].name()+" for "+ctype+" coordinates is "+Functions.formatValue(Math.abs(diffs[k]), 6)+" > "+Functions.formatValue(acceptableDiffs[k], 6)+"." + FileIO.getLineSeparator();
									}
								}
							}
						} else {
							if (i == 0) foundValues = new String[expectedValues.length];
							foundValues[i*2] = Functions.formatRA(ephems[j].rightAscension, 2);
							foundValues[i*2+1] = Functions.formatDEC(ephems[j].declination, 1);
							if (!expectedValues[i*2].trim().equals("-") && !foundValues[i*2].equals(expectedValues[i*2].trim()))
								err += "Expected right ascension of "+expectedValues[i*2].trim()+" ("+algor[0].name()+", type "+ephem[i].name()+"), but found "+foundValues[i*2]+" instead."+FileIO.getLineSeparator();
							if (!expectedValues[i*2+1].trim().equals("-") && !foundValues[i*2+1].equals(expectedValues[i*2+1].trim()))
								err += "Expected declination of "+expectedValues[i*2+1].trim()+" ("+algor[0].name()+", type "+ephem[i].name()+"), but found "+foundValues[i*2+1]+" instead."+FileIO.getLineSeparator();

							if (expectedValues.length > 6 && eph.ephemType == EphemerisElement.COORDINATES_TYPE.APPARENT) {
								foundValues = new String[] {
										foundValues[0], foundValues[1], foundValues[2], foundValues[3], foundValues[4], foundValues[5],
										Functions.formatAngleAsDegrees(ephems[j].longitudeOfCentralMeridian, 5),
										Functions.formatAngleAsDegrees(ephems[j].positionAngleOfPole, 5),
										Functions.formatAngleAsDegrees(ephems[j].subsolarLongitude, 5),
										Functions.formatAngleAsDegrees(ephems[j].subsolarLatitude, 5),
										Functions.formatValue(ephems[j].distanceFromSun, 5),
										Functions.formatValue(ephems[j].distance, 5),
										Functions.formatAngleAsDegrees(ephems[j].elongation, 5),
										Functions.formatAngleAsDegrees(ephems[j].phaseAngle, 5),
										ephems[j].constellation
								};
								double diffs[] = new double[] {
										Functions.normalizeRadians(ephems[j].longitudeOfCentralMeridian) - Functions.normalizeRadians(Double.parseDouble(expectedValues[6].trim()) * Constant.DEG_TO_RAD),
										Functions.normalizeRadians(ephems[j].positionAngleOfPole) - Functions.normalizeRadians(Double.parseDouble(expectedValues[7].trim()) * Constant.DEG_TO_RAD),
										Functions.normalizeRadians(ephems[j].subsolarLongitude) - Functions.normalizeRadians(Double.parseDouble(expectedValues[8].trim()) * Constant.DEG_TO_RAD),
										Functions.normalizeRadians(ephems[j].subsolarLatitude) - Functions.normalizeRadians(Double.parseDouble(expectedValues[9].trim()) * Constant.DEG_TO_RAD),
										ephems[j].distanceFromSun - Double.parseDouble(expectedValues[10].trim()),
										ephems[j].distance - Double.parseDouble(expectedValues[11].trim()),
										Math.abs(ephems[j].elongation) - Math.abs(Double.parseDouble(expectedValues[12].trim())) * Constant.DEG_TO_RAD,
										Math.abs(ephems[j].phaseAngle) - Math.abs(Double.parseDouble(expectedValues[13].trim())) * Constant.DEG_TO_RAD,
										DataSet.getIndex(Constellation.CONSTELLATION_NAMES, ephems[j].constellation.trim()+" ") - DataSet.getIndex(Constellation.CONSTELLATION_NAMES, expectedValues[14].trim()+" ")
								};
								String fields[] = new String[] {
										"longitude of central meridian", "position angle of pole",
										"subsolar longitude", "subsolar latitude",
										"distance from Sun", "distance to observer",
										"elongation", "phase angle", "constellation"
								};
								double acceptableDiffs[] = new double[] {
										1.0 * Constant.DEG_TO_RAD, // JPL is giving slightly wrong results ?
										0.05 * Constant.DEG_TO_RAD,
										1.0 * Constant.DEG_TO_RAD, 0.05 * Constant.DEG_TO_RAD,
										1.0E-5, 1.0E-5, 0.015 * Constant.DEG_TO_RAD, 0.015 * Constant.DEG_TO_RAD, 0};
								if (eph.targetBody == TARGET.Triton) // Triton position using JPL elements is approximate
									acceptableDiffs[4] = acceptableDiffs[5] = 1.0E-4;

								String ctype = ephem[i].name();
								for (int k=0; k<diffs.length; k++) {
									String f = fields[k];
									if (Math.abs(diffs[k]) > Math.PI) diffs[k] = Math.abs(diffs[k]) - Constant.TWO_PI;
									if (Math.abs(diffs[k]) > acceptableDiffs[k]) {
										if (k < 4 || k == 6 || k == 7) {
												err += "Difference in field "+f+" between Horizons and "+algor[0].name()+" for "+ctype+" coordinates is "+Functions.formatAngle(Math.abs(diffs[k]), 1)+" > "+Functions.formatAngle(acceptableDiffs[k], 1)+"." + FileIO.getLineSeparator();
										} else {
											if (k == diffs.length - 1) {
												err += "Difference in field "+f+" between Horizons and "+algor[0].name()+" for "+ctype+" coordinates. Found "+ephems[j].constellation+", expected "+expectedValues[12].trim()+"." + FileIO.getLineSeparator();
											} else {
												err += "Difference in field "+f+" between Horizons and "+algor[0].name()+" for "+ctype+" coordinates is "+Functions.formatValue(Math.abs(diffs[k]), 6)+" > "+Functions.formatValue(acceptableDiffs[k], 6)+"." + FileIO.getLineSeparator();
											}
										}
									}
								}
							}
						}
					}
				}
			}

			String isComment = expectedValues[expectedValues.length-1].trim();
			if (isComment.startsWith("[")) err = isComment + FileIO.getLineSeparator() + err;
			break;
		case SIDEREAL_TIME_OBLIQUITY_AND_NUTATION:
			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 2));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2]);
			time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);

			String what = testValues[3].trim().toLowerCase();
			if (what.equals("siderealtime")) {
				double gast = SiderealTime.greenwichApparentSiderealTime(time, observer, eph);
				double gmst = SiderealTime.greenwichMeanSiderealTime(time, observer, eph);
				double eceq = SiderealTime.equationOfEquinoxes(time, observer, eph);

				foundValues = new String[] {
						Functions.formatRA(gast, 4),
						Functions.formatRA(gmst, 4),
						Functions.formatValue(eceq * Constant.RAD_TO_ARCSEC / 15.0, 4)+"s",
				};

				if (!(expectedValues[0].trim()).equals(foundValues[0])) err += "Expected "+expectedValues[0].trim()+", but found "+foundValues[0]+" instead.";
				if (!(expectedValues[1].trim()).equals(foundValues[1])) err += "Expected "+expectedValues[1].trim()+", but found "+foundValues[1]+" instead.";
				if (!(expectedValues[2].trim()).equals(foundValues[2])) err += "Expected "+expectedValues[2].trim()+", but found "+foundValues[2]+" instead.";
			}
			if (what.equals("obliquity")) {
				double obl = Obliquity.trueObliquity(Functions.toCenturies(astro.jd()), eph);
				foundValues = new String[] { Functions.formatAngle(obl, 4) };
				if (!(expectedValues[0].trim()).equals(foundValues[0])) err += "Expected "+expectedValues[0].trim()+", but found "+foundValues[0]+" instead.";
			}
			if (what.equals("nutation")) {
				EarthOrientationParameters.clearEOP();
				Nutation.clearPreviousCalculation();
				Nutation.calcNutation(Functions.toCenturies(astro.jd()), eph);
				foundValues = new String[] { Functions.formatAngle(Nutation.getNutationInLongitude(), 4), Functions.formatAngle(Nutation.getNutationInObliquity(), 4) };
				if (!(expectedValues[0].trim()).equals(foundValues[0])) err += "Expected "+expectedValues[0].trim()+", but found "+foundValues[0]+" instead.";
				if (!(expectedValues[1].trim()).equals(foundValues[1])) err += "Expected "+expectedValues[1].trim()+", but found "+foundValues[1]+" instead.";
			}
			break;
		case NATURAL_SATELLITES_AND_JPLDExxx_THEORIES:
			astro = new AstroDate(Double.parseDouble(testValues[0]));
			time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);

			what = testValues[1].trim().toLowerCase();
			int which = Integer.parseInt(testValues[2].trim());
			if (what.equals("l1")) {
				double e[] = L1.L1_theory(astro.jd(), which);
				foundValues = new String[] {
						Functions.formatValue(e[0], 12),
						Functions.formatValue(e[1], 12),
						Functions.formatValue(e[2], 12),
						Functions.formatValue(e[3], 12),
						Functions.formatValue(e[4], 12),
						Functions.formatValue(e[5], 12)
				};
			}
			if (what.equals("mars07")) {
				double e[] = Mars07.getMoonPosition(astro.jd(), which, true);
				foundValues = new String[] {
						Functions.formatValue(e[0], 12),
						Functions.formatValue(e[1], 12),
						Functions.formatValue(e[2], 12),
						Functions.formatValue(e[3], 12),
						Functions.formatValue(e[4], 12),
						Functions.formatValue(e[5], 12)
				};
			}
			if (what.equals("gust86")) {
				double e[] = GUST86.GUST86_theory(astro.jd(), which, 4); // B1950
				foundValues = new String[] {
						Functions.formatValue(e[0] * Constant.AU, 3),
						Functions.formatValue(e[1] * Constant.AU, 3),
						Functions.formatValue(e[2] * Constant.AU, 3),
						Functions.formatValue(e[3] * Constant.AU / Constant.SECONDS_PER_DAY, 8),
						Functions.formatValue(e[4] * Constant.AU / Constant.SECONDS_PER_DAY, 8),
						Functions.formatValue(e[5] * Constant.AU / Constant.SECONDS_PER_DAY, 8)
				};
			}
			if (what.equals("tass1.7")) {
				double e[] = TASS17.TASS17_theory(astro.jd(), which, false);
				foundValues = new String[] {
						Functions.formatValue(e[0], 12),
						Functions.formatValue(e[1], 12),
						Functions.formatValue(e[2], 12),
						Functions.formatValue(e[3] * 365.25, 9),
						Functions.formatValue(e[4] * 365.25, 9),
						Functions.formatValue(e[5] * 365.25, 9)
				};
			}
			if (what.startsWith("de")) {
				JPLEphemeris jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE405);
				if (what.endsWith("200")) jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE200);
				if (what.endsWith("403")) jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE403);
				if (what.endsWith("406")) jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE406);
				if (what.endsWith("413")) jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE413);
				if (what.endsWith("414")) jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE414);
				if (what.endsWith("422")) jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE422);
				double e[] = jpl.getPositionAndVelocity(astro.jd(), TARGET.values()[which]);

				foundValues = new String[] {
						Functions.formatValue(e[0], 16),
						Functions.formatValue(e[1], 16),
						Functions.formatValue(e[2], 16),
						Functions.formatValue(e[3], 16),
						Functions.formatValue(e[4], 16),
						Functions.formatValue(e[5], 16)
				};
			}

			if (what.startsWith("de")) {
				for (int i=0; i<expectedValues.length; i++) {
					if (!(expectedValues[i].trim()).startsWith(foundValues[i].substring(0, 15))) err += "Expected "+expectedValues[i].trim()+", but found "+foundValues[i]+" instead."+FileIO.getLineSeparator();
				}
			} else {
				for (int i=0; i<foundValues.length; i++) {
					if (!(expectedValues[i].trim()).equals(foundValues[i])) err += "Expected "+expectedValues[i].trim()+", but found "+foundValues[i]+" instead."+FileIO.getLineSeparator();
				}
			}
			break;
		case STARS:
			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 5));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2], (int) in[3], (int) in[4], in[5]);
			time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);

			String name = testValues[6].trim();
			SimbadElement simbad = SimbadQuery.query(name);
			StarElement star = StarElement.parseSimbadElement(simbad);
			StarEphemElement starEphem = StarEphem.starEphemeris(time, observer, eph, star, false);

			double vLSR = StarEphem.getLSRradialVelocity(time, observer, eph, star);
			double vTOP = StarEphem.getRadialVelocity(time, observer, eph, star);
			eph.isTopocentric = false;
			double vGEO = StarEphem.getRadialVelocity(time, observer, eph, star);
			foundValues = new String[] {
					Functions.formatRA(star.rightAscension, 2),
					Functions.formatDEC(star.declination, 1),
					Functions.formatRA(starEphem.rightAscension, 2),
					Functions.formatDEC(starEphem.declination, 1),
					Functions.formatValue(vLSR, 2),
					Functions.formatValue(vGEO, 2),
					Functions.formatValue(vTOP, 2)
			};
			for (int i=0; i<foundValues.length; i++) {
				if (!(expectedValues[i].trim()).equals(foundValues[i])) err += "Expected "+expectedValues[i].trim()+", but found "+foundValues[i]+" instead."+FileIO.getLineSeparator();
			}

			break;
		case MAIN_EVENTS:
			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 2));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2]);
			time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);

			type = testValues[3].toLowerCase().trim();

			if (type.equals("grs")) {
				astro = new AstroDate((int) in[0], (int) in[1], in[2]);
				SimpleEventElement see = MainEvents.getJupiterGRSNextTransitTime(TimeScale.getJD(new TimeElement(astro.jd(), TimeElement.SCALE.UNIVERSAL_TIME_UT1), observer, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME));
				foundValues = new String[] {TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(new TimeElement(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, SCALE.UNIVERSAL_TIME_UT1), SCALE.UNIVERSAL_TIME_UT1), true, true) };
			}
			if (type.equals("moon")) {
				SimpleEventElement see = MainEvents.MoonPhaseOrEclipse(astro.jd(), SimpleEventElement.EVENT.values()[Integer.parseInt(testValues[4].trim())], MainEvents.EVENT_TIME.values()[Integer.parseInt(testValues[5].trim())]);
				foundValues = new String[] {TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(see.time, SCALE.BARYCENTRIC_DYNAMICAL_TIME), true, true)};
			}
			if (type.equals("planet")) {
				SimpleEventElement see = MainEvents.getPlanetaryEvent(TARGET.values()[Integer.parseInt(testValues[4].trim())], astro.jd(), SimpleEventElement.EVENT.values()[Integer.parseInt(testValues[5].trim())], MainEvents.EVENT_TIME.NEXT, true);
				foundValues = new String[] {TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, SCALE.LOCAL_TIME), SCALE.LOCAL_TIME), true, true)};
			}
			if (type.equals("equinoxsolstice")) {
				SimpleEventElement see = MainEvents.EquinoxesAndSolstices(astro.getYear(), SimpleEventElement.EVENT.values()[Integer.parseInt(testValues[4].trim())]);
				foundValues = new String[] {TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, SCALE.LOCAL_TIME), SCALE.LOCAL_TIME), true, true)};
			}
			if (type.equals("transit")) {
				SimpleEventElement see = MainEvents.getMercuryOrVenusTransit(TARGET.values()[Integer.parseInt(testValues[4].trim())], astro.jd(), astro.jd()+5*365.25, true);
				foundValues = new String[] {TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, SCALE.LOCAL_TIME), SCALE.LOCAL_TIME), true, true)};
			}
			if (type.equals("apogeeperigee")) {
				int w = Integer.parseInt(testValues[4].trim());
				SimpleEventElement s = null;
				if (w == 1) s = LunarEvent.getPerigee(astro.jd(), MainEvents.EVENT_TIME.NEXT);
				if (w == 2) s = LunarEvent.getApogee(astro.jd(), MainEvents.EVENT_TIME.NEXT);
				foundValues = new String[] {s.toString()};
				foundValues = DataSet.toStringArray(foundValues[0], ",", true);
			}
			if (type.equals("librations")) {
				eph.isTopocentric = false;
				time = new TimeElement(astro, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
				double r[] = LunarEvent.getEckhardtMoonLibrations(time, observer, eph);
				foundValues = new String[] {"l = "+Functions.formatAngleAsDegrees(r[0], 2) + ", b = "+Functions.formatAngleAsDegrees(r[1], 2) + ", p = "+Functions.formatAngleAsDegrees(r[2], 2)};
				foundValues = DataSet.toStringArray(foundValues[0], ",", true);
			}

			// Pass to English if default locale is Spanish
			foundValues[0] = DataSet.replaceAll(foundValues[0], "ene", "jan", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "abr", "apr", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "ago", "aug", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "dic", "dec", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Ene", "Jan", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Abr", "Apr", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Ago", "Aug", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Dic", "Dec", false);

			for (int i=0; i<foundValues.length; i++) {
				if (!(expectedValues[i].trim().toLowerCase()).equals(foundValues[i].trim().toLowerCase())) err += "Expected "+expectedValues[i].trim()+", but found "+foundValues[i]+" instead."+FileIO.getLineSeparator();
			}
			break;
		case DOUBLE_VARIABLE_STARS:
			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 5));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2], (int) in[3], (int) in[4], in[5]);
			time = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);

			type = testValues[6].toLowerCase().trim();
			name = testValues[7].trim();
			if (type.equals("double")) {
				try {
					boolean oldCatalog = false;
					ReadFile rf = new ReadFile();
					if (oldCatalog) {
						// Fourth Catalog of Orbits of Visual Binary Stars, Worley 1983
						rf.setPath(DoubleStarElement.PATH_OLD_VISUAL_DOUBLE_STAR_CATALOG);
						rf.readOldFileOfDoubleStars();
					} else {
						// Sixth Catalog of Orbits of Visual Binary Stars, Hartkopf 2006
						rf.setPath(DoubleStarElement.PATH_VISUAL_DOUBLE_STAR_CATALOG);
						rf.readFileOfDoubleStars();
					}
					int i = rf.searchByName(name);
					DoubleStarElement dstar = rf.getDoubleStarElement(i);
					dstar.calcEphemeris(time, observer);
					foundValues = new String[] {
						Functions.formatValue(dstar.getDistance(), 1),
						Functions.formatAngleAsDegrees(dstar.getPositionAngle(), 1)
					};
				} catch (Exception exc) {
					Logger.log(LEVEL.ERROR, "Error reading orbits of double stars. Message was: "+exc.getLocalizedMessage());
				}
			}
			if (type.equals("variable")) {
				try {
					ReadFile rf = new ReadFile();
					rf.setPath(VariableStarElement.PATH_VARIABLE_STAR_CATALOG);
					rf.readFileOfVariableStars();
					int i = rf.searchByName(name);
					VariableStarElement vstar = rf.getVariableStarElement(i);
					vstar.calcEphemeris(time, observer, false);
					foundValues = new String[] {
							Functions.formatValue(vstar.getPhase(), 2),
							TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(new TimeElement(vstar.getNextMinima(), null), true, true) //+secondaryFlag
					};
				} catch (Exception exc) {
					Logger.log(LEVEL.ERROR, "Error reading variable stars. Message was: "+exc.getLocalizedMessage());
				}
			}

			// Pass to English if default locale is Spanish
			foundValues[0] = DataSet.replaceAll(foundValues[0], "ene", "jan", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "abr", "apr", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "ago", "aug", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "dic", "dec", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Ene", "Jan", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Abr", "Apr", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Ago", "Aug", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Dic", "Dec", false);

			for (int i=0; i<foundValues.length; i++) {
				if (!(expectedValues[i].trim().toLowerCase()).equals(foundValues[i].toLowerCase())) err += "Expected "+expectedValues[i].trim()+", but found "+foundValues[i]+" instead."+FileIO.getLineSeparator();
			}
			break;
		case ECLIPSES:
			// Set Madrid location according to Spenak's coordinates
			observer.setLongitudeDeg(-3.68333);
			observer.setLatitudeDeg(40.4);
			observer.setHeight(667, true);

			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 2));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2]);
			time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);

			type = testValues[3].toLowerCase().trim();
			timeScale = testValues[4].trim();
			ts = DataSet.getIndex(TimeElement.TIME_SCALES_ABBREVIATED, timeScale);

			eph.targetBody = TARGET.Moon;
			if (type.equals("solar")) {
				SolarEclipse se = new SolarEclipse(time, observer, eph);
				MoonEventElement[] events = se.getEvents();
				foundValues = new String[events.length+1];
				for (int i = 0; i < events.length; i++)
				{
					TimeElement t = new TimeElement(events[i].startTime, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
					jd = TimeScale.getJD(t, observer, eph, SCALE.values()[ts]);
					if (events[i].startTime != 0.0)
						foundValues[i] = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(jd, null), true, true) + " (" + events[i].details + ")";
				}
				TimeElement t = new TimeElement(se.getEclipseMaximum(), TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
				jd = TimeScale.getJD(t, observer, eph, SCALE.values()[ts]);
				foundValues[foundValues.length-1] = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(jd, null), true, true) + " (Eclipse maximum)";
			}
			if (type.equals("lunar")) {
				eph.isTopocentric = false;
				eph.algorithm = EphemerisElement.ALGORITHM.VSOP87_ELP2000ForMoon;
				eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_2006;
				LunarEclipse le = new LunarEclipse(time, observer, eph);
				MoonEventElement[] events = le.getEvents();
				foundValues = new String[events.length+1];
				for (int i = 0; i < events.length; i++)
				{
					TimeElement t = new TimeElement(events[i].startTime, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
					jd = TimeScale.getJD(t, observer, eph, SCALE.values()[ts]);
					if (events[i].startTime != 0.0)
						foundValues[i] = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(jd, null), true, true) + " (" + events[i].details + ")";
				}
				TimeElement t = new TimeElement(le.getEclipseMaximum(), TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
				jd = TimeScale.getJD(t, observer, eph, SCALE.values()[ts]);
				foundValues[foundValues.length-1] = TimeFormat.formatJulianDayAsDateAndTime(new TimeElement(jd, null), true, true) + " (Eclipse maximum)";
			}

			// Pass to English if default locale is Spanish
			foundValues[0] = DataSet.replaceAll(foundValues[0], "ene", "jan", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "abr", "apr", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "ago", "aug", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "dic", "dec", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Ene", "Jan", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Abr", "Apr", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Ago", "Aug", false);
			foundValues[0] = DataSet.replaceAll(foundValues[0], "Dic", "Dec", false);

			for (int i=0; i<foundValues.length; i++) {
				if (!(expectedValues[i].trim().toLowerCase()).equals(foundValues[i].toLowerCase())) err += "Expected "+expectedValues[i].trim()+", but found "+foundValues[i]+" instead."+FileIO.getLineSeparator();
			}
			break;
		case EPHEMERIDES_OTHER_SOURCES:
			eph.correctForEOP = true;
			eph.useVondrak2011PrecessionFormulaInsteadOfIAU2006 = false;
			eph.correctForPolarMotion = false;
			eph.correctEOPForDiurnalSubdiurnalTides = false;

			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 2));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2]);
			time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);

			String b = testValues[3].trim();
			String method = testValues[4].toUpperCase().trim();
			double maxErrorMilliarcseconds = Double.parseDouble(testValues[5].trim());
			double maxErrorAU = Double.parseDouble(testValues[6].trim());

			eph.isTopocentric = false;
			eph.algorithm = EphemerisElement.ALGORITHM.JPL_DE405;
			if (Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH == null) eph.algorithm = EphemerisElement.ALGORITHM.MOSHIER;
			eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_1976;
			if (method.equals("AA")) {
				eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_2006;
				eph.frame = EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000;
				eph.correctForEOP = false;
			}

			try {
				body = TARGET.values()[Integer.parseInt(b)];
			} catch (Exception exc) {
				body = Target.getID(b);
				eph.algorithm = EphemerisElement.ALGORITHM.NATURAL_SATELLITE;
			}

			eph.targetBody = body;

			EphemElement pephem = Ephem.getEphemeris(time, observer, eph, false);

			double expectedRA = Functions.parseRightAscension(expectedValues[0].trim());
			double expectedDEC = Functions.parseDeclination(expectedValues[1].trim());

			double exactRA = pephem.rightAscension, exactDEC = pephem.declination;
			// Compensate for the lack of digits in AA's results
			if (method.equals("AA")) {
				pephem.rightAscension = Functions.parseRightAscension(Functions.formatRA(pephem.rightAscension, 3));
				pephem.declination = Functions.parseDeclination(Functions.formatDEC(pephem.declination, 2));
			}

			double difLoc = LocationElement.getAngularDistance(pephem.getEquatorialLocation(),
					new LocationElement(expectedRA, expectedDEC, 1.0)) * Constant.RAD_TO_ARCSEC * 1000.0;
			double difDist = -1.0;
			if (expectedValues.length >= 3 && !expectedValues[2].trim().equals("-"))
				difDist = Math.abs(pephem.distance - Double.parseDouble(expectedValues[2].trim()));
			if (difLoc > maxErrorMilliarcseconds || (difDist > maxErrorAU && difDist >= 0)) {
				difLoc = LocationElement.getAngularDistance(new LocationElement(exactRA, exactDEC, 1.0),
						new LocationElement(expectedRA, expectedDEC, 1.0)) * Constant.RAD_TO_ARCSEC * 1000.0;
				String fRA = Functions.formatRA(exactRA, 5);
				String fDEC = Functions.formatDEC(exactDEC, 4);
				String fDist = Functions.formatValue(pephem.distance, 13);
				err += "Found a separation of "+(float) difLoc+" mas and "+(float) difDist+" AU. JPARSEC position is "+fRA+", "+fDEC+", "+fDist+" AU." + FileIO.getLineSeparator();
			}
			break;
		case MUTUAL_EVENTS_NATURAL_SATELLITES:
			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 2));
			String eo1 = expectedValues[0].trim(), eo2 = expectedValues[1].trim(), eo3 = expectedValues[2].trim();
			int hour = Integer.parseInt(FileIO.getField(1, eo1, " ", true));
			int minute = Integer.parseInt(FileIO.getField(2, eo1, " ", true));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2], hour, minute - 8, 0.0);
			time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);

			body = TARGET.values()[Integer.parseInt(testValues[3].trim())];
			String event = testValues[4].toUpperCase().trim();

			int precision = 30, accuracy = 1;
			boolean all = false;
			eph.targetBody = body;
			eph.isTopocentric = false;
			eph.algorithm = EphemerisElement.ALGORITHM.JPL_DE405;
			eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_1976;
			eph.correctForEOP = false;
			MoonEvent me = new MoonEvent(time, observer, eph, new TimeElement(astro.jd()+1.0/24.0, time.timeScale),
					precision, accuracy, false, MoonEvent.JUPITER_THEORY.E2x3, MoonEvent.SATURN_THEORY.TASS);
			MoonEventElement ev[] = me.getMutualPhenomena(all);
			TARGET first = TARGET.Io;
			if (body == TARGET.SATURN) first = TARGET.Mimas;
			if (body == TARGET.URANUS) first = TARGET.Ariel;
			boolean found = false;
			for (int i=0; i<ev.length; i++) {
				String e = ""+(ev[i].secondaryBody.ordinal()-first.ordinal()+1);
				if (e.equals("-1")) e = "5"; // Miranda is 5 following IMCCE
				if (ev[i].eventType == MoonEventElement.EVENT.ECLIPSED) e+= " ECL ";
				if (ev[i].eventType == MoonEventElement.EVENT.OCCULTED) e+= " OCC ";
				if ((ev[i].mainBody.ordinal()-first.ordinal()+1) == -1) {
					e += "5"; // Miranda is 5 following IMCCE
				} else {
					e += ""+(ev[i].mainBody.ordinal()-first.ordinal()+1);
				}

				if (event.startsWith(e)) {
					found = true;
					//System.out.println(ev[i].startTime+"/"+ev[i].details);
					AstroDate start = new AstroDate(ev[i].startTime);
					//AstroDate max = new AstroDate((ev[i].startTime+ev[i].endTime) / 2.0);
					AstroDate max = new AstroDate(Double.parseDouble(FileIO.getField(2, ev[i].details, ",", true)));
					int duration = (int) (0.5 + (ev[i].endTime - ev[i].startTime) * Constant.SECONDS_PER_DAY);

					String o1 = prepareTime(start), o2 = prepareTime(max), o3 = ""+duration;
					foundValues = new String[] {o1, o2, o3};
//					if (getTimeDif(o1, eo1) > 30 || getTimeDif(o2, eo2) > 30 || Math.abs(duration-Integer.parseInt(eo3)) > 30)
					if (getTimeDif(o2, eo2) > 10)
						err += "Expected "+eo1+"/"+eo2+"/"+eo3+", but found "+o1+"/"+o2+"/"+o3+FileIO.getLineSeparator();
					break;
				}
			}
			if (!found) err += "Could not find the event!"+FileIO.getLineSeparator();
			break;
		case EPHEMERIDES_FROM_OTHER_PLANETS:
			in = DataSet.toDoubleValues(DataSet.getSubArray(testValues, 0, 5));
			astro = new AstroDate((int) in[0], (int) in[1], (int) in[2], (int) in[3], (int) in[4], (int) in[5]);
			time = new TimeElement(astro, TimeElement.fromTimeScaleAbbreviation(testValues[6].trim()));

			body = Target.getIDFromEnglishName(testValues[7].trim());
			eph = new EphemerisElement(body, EphemerisElement.COORDINATES_TYPE.APPARENT,
					EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_1976,
					EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000, EphemerisElement.ALGORITHM.MOSHIER);

			String obs = testValues[8].trim();
			String planet = obs.substring(0, obs.indexOf(" "));
			System.out.println(obs);
			String coord = obs.substring(obs.indexOf("(")+1, obs.indexOf(")"));
			observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement(planet, Target.getIDFromEnglishName(planet),
					new LocationElement(Double.parseDouble(FileIO.getField(1, coord, " ", false).trim()) * Constant.DEG_TO_RAD,
							Double.parseDouble(FileIO.getField(2, coord, " ", false).trim())* Constant.DEG_TO_RAD, 0.0)));

			pephem = Ephem.getEphemeris(time, observer, eph, false);

			String sRA = Functions.formatRA(pephem.rightAscension);
			String sDEC = Functions.formatDEC(pephem.declination);
			String sAz = Functions.formatAngle(pephem.azimuth, 3);
			String sEl = Functions.formatAngle(pephem.elevation, 3);

			String exp = DataSet.toString(expectedValues, ",").trim();
			String fou = sRA+", "+sDEC+", "+sAz+", "+sEl;
			if (!exp.equals(fou)) {
				err += "Expected '"+exp+"', but found '"+fou+"'." + FileIO.getLineSeparator();
			}
			break;
		default:
			this.out += "Found unsupported test: "+TEST_DESCRIPTION[testID.ordinal()]+FileIO.getLineSeparator();
			break;
		}

		if (err.equals(FileIO.getLineSeparator())) err = "";
		Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH = externalPath;
	}

	private String prepareTime(AstroDate a) {
		String h = ""+a.getHour(), m = ""+a.getMinute(), s = ""+(int) a.getSeconds();
		h = FileIO.addSpacesBeforeAString(h, 2);
		m = FileIO.addSpacesBeforeAString(m, 2);
		s = FileIO.addSpacesBeforeAString(s, 2);
		return h + " " + m + " " + s;
	}
	private int getTimeDif(String time1, String time2) {
		int h1 = Integer.parseInt(FileIO.getField(1, time1, " ", true));
		int m1 = Integer.parseInt(FileIO.getField(2, time1, " ", true));
		int s1 = Integer.parseInt(FileIO.getField(3, time1, " ", true));
		int h2 = Integer.parseInt(FileIO.getField(1, time2, " ", true));
		int m2 = Integer.parseInt(FileIO.getField(2, time2, " ", true));
		int s2 = Integer.parseInt(FileIO.getField(3, time2, " ", true));
		double t1 = h1 + m1 / 60.0 + s1 / 3600.0;
		double t2 = h2 + m2 / 60.0 + s2 / 3600.0;
		return (int) (0.5 + Math.abs((t2 - t1)*3600.0));
	}

	/**
	 * The set of available tests.
	 */
	public enum TEST {
		/** ID constant for testing the atlas charts. */
		ATLAS_CHART,
		/** ID constant for testing the constellations. */
		CONSTELLATION,
		/** ID constant for testing coordinate systems. */
		COORDINATE_SYSTEMS,
		/** ID constant for testing ephemerides. */
		EPHEMERIDES,
		/** ID constant for testing ephemerides. */
		EPHEMERIDES_NATURAL_SATELLITES,
		/** ID constant for testing Julian day. */
		JULIAN_DAY,
		/** ID constant for testing time scales. */
		TIME_SCALES,
		/** ID constant for testing measures. */
		MEASURES,
		/** ID constant for testing calendars. */
		CALENDARS,
		/** ID constant for testing geodetic and geocentric locations. */
		GEODETIC_AND_GEOCENTRIC_COORDINATES,
		/** ID constant for testing detailed ephemerides with different algorithms and coordinates. */
		DETAILED_EPHEMERIDES,
		/** ID constant for testing sidereal time, obliquity, and nutation. */
		SIDEREAL_TIME_OBLIQUITY_AND_NUTATION,
		/** ID constant for testing natural satellites and JPL theories. */
		NATURAL_SATELLITES_AND_JPLDExxx_THEORIES,
		/** ID constant for testing star ephemerides. */
		STARS,
		/** ID constant for testing general astronomical events. */
		MAIN_EVENTS,
		/** ID constant for testing double and variable stars. */
		DOUBLE_VARIABLE_STARS,
		/** ID constant for testing eclipses. */
		ECLIPSES,
		/** ID constant for testing ephemerides from IMCCE, HORIZONS, and AA. */
		EPHEMERIDES_OTHER_SOURCES,
		/** ID constant for testing mutual events of natural satellites from IMCCE. */
		MUTUAL_EVENTS_NATURAL_SATELLITES,
		/** ID constant for testing ephemerides from other planets. */
		EPHEMERIDES_FROM_OTHER_PLANETS,
		/** ID constant for selecting all tests. */
		ALL,
	};

	/**
	 * Holds descriptions for the tests.
	 */
	public static final String[] TEST_DESCRIPTION = new String[] {
		"atlas chart numbers for Millenium star, Sky atlas 2000, Uranometria (original and 2nd editions), and Rukl lunar atlases",
		"constellation names from equatorial positions",
		"coordinate systems",
		"ephemerides for planets and stars (tests of occultations)",
		"ephemerides for natural satellites (tests of mutual occultations/eclipses)",
		"Julian day",
		"time scales",
		"measure representation and unit conversion",
		"calendars",
		"geodetic and geocentric coordinates and ellipsoids",
		"detailed ephemerides for different bodies, theories, and coordinate types",
		"Greenwich apparent and mean sidereal times",
		"natural satellites and JPL DExxx theories",
		"stellar ephemerides",
		"general astronomical events",
		"double and variable stars",
		"eclipses",
		"accurate ephemerides from IMCCE, Horizons, and Astronomical Almanac",
		"mutual events of natural satellites",
		"ephemerides from other planets",
		"ALL TESTS",
	};
}
