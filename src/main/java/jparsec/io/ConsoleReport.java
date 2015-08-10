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
package jparsec.io;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Set;

import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.ephem.Target.TARGET;
import jparsec.ephem.planets.EphemElement;
import jparsec.graph.DataSet;
import jparsec.math.Constant;
import jparsec.time.TimeElement.SCALE;
import jparsec.time.TimeFormat;
import jparsec.util.JPARSECException;
import jparsec.util.Translate;

/**
 * A class with static methods to report results of calculations in an adequate format.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class ConsoleReport
{
	// private constructor so that this class cannot be instantiated.
	private ConsoleReport() {}
	
	/**
	 * Report the results of an ephemeris calculation to the console. Useful for
	 * getting the values quickly or for testing purposes.
	 * 
	 * @param ephem Ephem object for a Solar System body.
	 * @throws JPARSECException Thrown for invalid rise, set, or transit times.
	 */
	public static void fullEphemReportToConsole(EphemElement ephem) throws JPARSECException
	{
		System.out.println(getFullEphemReport(ephem, false));
	}
	
	/**
	 * Report the results of an ephemeris calculation to a string
	 * 
	 * @param ephem Ephem object.
	 * @param isStar True if the object is a star, and distance should be
	 * shown in pc instead of AU.
	 * @return The report.
	 * @throws JPARSECException Thrown for invalid rise, set, or transit times.
	 */
	public static String getFullEphemReport(EphemElement ephem, boolean isStar) throws JPARSECException
	{
		return getFullEphemReport(ephem, isStar, true);
	}
	
	/**
	 * Report the results of an ephemeris calculation to a string
	 * 
	 * @param ephem Ephem object.
	 * @param isStar True if the object is a star, and distance should be
	 * shown in pc instead of AU.
	 * @param showName True to show the name of the body in each line of the output text.
	 * @return The report.
	 * @throws JPARSECException Thrown for invalid rise, set, or transit times.
	 */
	public static String getFullEphemReport(EphemElement ephem, boolean isStar, boolean showName) throws JPARSECException
	{
		return getFullEphemReport(ephem, isStar, showName, 3);
	}

	/**
	 * Report the results of an ephemeris calculation to a string
	 * 
	 * @param ephem Ephem object.
	 * @param isStar True if the object is a star, and distance should be
	 * shown in pc instead of AU.
	 * @param showName True to show the name of the body in each line of the output text.
	 * @param decimalArcsec Number of decimal places to show in the arcseconds for the equatorial
	 * and horizontal positions.
	 * @return The report.
	 * @throws JPARSECException Thrown for invalid rise, set, or transit times.
	 */
	public static String getFullEphemReport(EphemElement ephem, boolean isStar, boolean showName, int decimalArcsec) throws JPARSECException
	{
		double factor1 = Constant.AU * 1000.0 / Constant.PARSEC;
		double factor2 = 1000.0 / (Constant.LIGHT_YEAR * Constant.LIGHT_TIME_DAYS_PER_AU);
		String unit1 = "pc", unit2 = "light-years";
		if (Translate.getDefaultLanguage() == Translate.LANGUAGE.SPANISH) unit2 = "años-luz";
		int n = 1;
		if (!isStar || ephem.distance < 100000) {
			unit1 = "AU";
			if (Translate.getDefaultLanguage() == Translate.LANGUAGE.SPANISH) unit1 = "UA";
			unit2 = "s";
			factor1 = 1.0;
			factor2 = Constant.SECONDS_PER_DAY;
			n = 6;
		}
		
		String name = ephem.name + " ";
		if (!showName) name = "";
		String out = "", sep = FileIO.getLineSeparator();
		out += name + Translate.translate(Translate.JPARSEC_RIGHT_ASCENSION)+": " + Functions.formatRA(ephem.rightAscension, 1+decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_DECLINATION)+": " + Functions.formatDEC(ephem.declination, decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_DISTANCE)+": " + Functions.formatValue(ephem.distance*factor1, n) + " " + unit1 + sep;
		if (ephem.rise != null || ephem.transit != null || ephem.set != null) {
			int l = 0;
			if (ephem.rise != null) l = ephem.rise.length;
			if (ephem.set != null && ephem.set.length > l) l = ephem.set.length;
			if (ephem.transit != null && ephem.transit.length > l) l = ephem.transit.length;
			for (int i=0; i<l; i++) {
				if (ephem.rise != null && ephem.rise.length > i) out += name + Translate.translate(Translate.JPARSEC_RISE)+": " + TimeFormat.formatJulianDayAsDateAndTime(ephem.rise[i], SCALE.LOCAL_TIME) + sep;
				if (ephem.transit != null && ephem.transit.length > i) out += name + Translate.translate(Translate.JPARSEC_TRANSIT)+": " + TimeFormat.formatJulianDayAsDateAndTime(ephem.transit[i], SCALE.LOCAL_TIME) + sep;
				if (ephem.transitElevation != null && ephem.transitElevation.length > i) out += name + Translate.translate(Translate.JPARSEC_TRANSIT_ELEVATION)+": " + Functions.formatAngle(ephem.transitElevation[i], 3) + sep;
				if (ephem.set != null && ephem.set.length > i) out += name + Translate.translate(Translate.JPARSEC_SET)+": " + TimeFormat.formatJulianDayAsDateAndTime(ephem.set[i], SCALE.LOCAL_TIME) + sep;
			}
		}
		out += name + Translate.translate(Translate.JPARSEC_LIGHT_TIME)+": " + (float) (ephem.lightTime * factor2) + " " + unit2 + sep;
		out += name + Translate.translate(Translate.JPARSEC_CONSTELLATION)+": " + ephem.constellation + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_ELONGATION)+": " + Functions.formatAngle(ephem.elongation, decimalArcsec) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_PHASE_ANGLE)+": " + Functions.formatAngle(ephem.phaseAngle, decimalArcsec) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_PHASE)+": " + Functions.formatValue(ephem.phase, 3) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_HELIOCENTRIC_ECLIPTIC_LONGITUDE)+": " + Functions.formatAngle(ephem.heliocentricEclipticLongitude, decimalArcsec) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_HELIOCENTRIC_ECLIPTIC_LATITUDE)+": " + Functions.formatAngle(ephem.heliocentricEclipticLatitude, decimalArcsec) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_HELIOCENTRIC_DISTANCE)+": " + Functions.formatValue(ephem.distanceFromSun, 5) + sep;
		out += name + Translate.translate(Translate.JPARSEC_AZIMUTH)+": " + Functions.formatAngle(ephem.azimuth, decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_ELEVATION)+": " + Functions.formatAngle(ephem.elevation, decimalArcsec) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_DEFECT_OF_ILLUMINATION)+": " + Functions.formatAngle(ephem.defectOfIllumination, 3) + sep;
		out += name + Translate.translate(Translate.JPARSEC_ANGULAR_RADIUS)+": " + Functions.formatAngle(ephem.angularRadius, 3) + sep;
		out += name + Translate.translate(Translate.JPARSEC_MAGNITUDE)+": " + (ephem.magnitude > 99 ? "-" : Functions.formatValue(ephem.magnitude, 3)) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_SURFACE_BRIGHTNESS)+": " + Functions.formatValue(ephem.surfaceBrightness, 3) + sep;
		out += name + Translate.translate(Translate.JPARSEC_POLE_RIGHT_ASCENSION)+": " + Functions.formatAngle(ephem.northPoleRA, decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_POLE_DECLINATION)+": " + Functions.formatAngle(ephem.northPoleDEC, decimalArcsec) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_SUBSOLAR_LONGITUDE)+": " + Functions.formatAngle(ephem.subsolarLongitude, decimalArcsec) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_SUBSOLAR_LATITUDE)+": " + Functions.formatAngle(ephem.subsolarLatitude, decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_PARALACTIC_ANGLE)+": " + Functions.formatAngle(ephem.paralacticAngle, decimalArcsec) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_BRIGHT_LIMB_ANGLE)+": " + Functions.formatAngle(ephem.brightLimbAngle, decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_POSITION_ANGLE_OF_AXIS)+": " + Functions.formatAngle(ephem.positionAngleOfAxis, decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_POSITION_ANGLE_OF_POLE)+": " + Functions.formatAngle(ephem.positionAngleOfPole, decimalArcsec) + sep;
		
		TARGET t = Target.getID(ephem.name);
		if (t == TARGET.JUPITER || t == TARGET.SATURN || t == TARGET.URANUS || t == TARGET.NEPTUNE) {
			if (t == TARGET.JUPITER || t == TARGET.SATURN) {
				out += name + Translate.translate(Translate.JPARSEC_LONGITUDE_OF_CENTRAL_MERIDIAN)+" "+Translate.translate(Translate.JPARSEC_SYSTEM).toLowerCase()+" I: " + Functions.formatAngle(
					ephem.longitudeOfCentralMeridianSystemI, decimalArcsec) + sep;
			}
			if (t == TARGET.JUPITER) {
				out += name + Translate.translate(Translate.JPARSEC_LONGITUDE_OF_CENTRAL_MERIDIAN)+" "+Translate.translate(Translate.JPARSEC_SYSTEM).toLowerCase()+" II: " + Functions.formatAngle(
					ephem.longitudeOfCentralMeridianSystemII, decimalArcsec) + sep;
			}
			out += name + Translate.translate(Translate.JPARSEC_LONGITUDE_OF_CENTRAL_MERIDIAN)+" "+Translate.translate(Translate.JPARSEC_SYSTEM).toLowerCase()+" III: " + Functions.formatAngle(
					ephem.longitudeOfCentralMeridianSystemIII, decimalArcsec) + sep;
		} else {
			out += name + Translate.translate(Translate.JPARSEC_LONGITUDE_OF_CENTRAL_MERIDIAN)+": " + Functions.formatAngle(ephem.longitudeOfCentralMeridian, decimalArcsec) + sep;			
		}
		return out;
	}
	
	/**
	 * Report main results of an ephemeris calculation to the console. Useful
	 * for getting the values quickly or for testing purposes.
	 * 
	 * @param ephem Ephem object for a Solar System body.
	 * @throws JPARSECException Thrown for invalid rise, set, or transit times.
	 */
	public static void basicEphemReportToConsole(EphemElement ephem) throws JPARSECException
	{
		System.out.println(getBasicEphemReport(ephem, false));
	}
	
	/**
	 * Report main results of an ephemeris calculation to a string.
	 * 
	 * @param ephem Ephem object.
	 * @param isStar True if the object is a star, and distance should be
	 * shown in pc instead of AU.
	 * @return The report.
	 * @throws JPARSECException Thrown for invalid rise, set, or transit times.
	 */
	public static String getBasicEphemReport(EphemElement ephem, boolean isStar) throws JPARSECException
	{
		return getBasicEphemReport(ephem, isStar, true);
	}
	
	/**
	 * Report main results of an ephemeris calculation to a string.
	 * 
	 * @param ephem Ephem object.
	 * @param isStar True if the object is a star, and distance should be
	 * shown in pc instead of AU.
	 * @param showName True to show the name of the body in each line of the output text.
	 * @return The report.
	 * @throws JPARSECException Thrown for invalid rise, set, or transit times.
	 */
	public static String getBasicEphemReport(EphemElement ephem, boolean isStar, boolean showName) throws JPARSECException
	{
		return getBasicEphemReport(ephem, isStar, showName, 3);
	}
	
	/**
	 * Report main results of an ephemeris calculation to a string.
	 * 
	 * @param ephem Ephem object.
	 * @param isStar True if the object is a star, and distance should be
	 * shown in pc instead of AU.
	 * @param showName True to show the name of the body in each line of the output text.
	 * @param decimalArcsec Number of decimal places to show in the arcseconds for the equatorial
	 * and horizontal positions.
	 * @return The report.
	 * @throws JPARSECException Thrown for invalid rise, set, or transit times.
	 */
	public static String getBasicEphemReport(EphemElement ephem, boolean isStar, boolean showName, int decimalArcsec) throws JPARSECException
	{
		double factor = Constant.AU * 1000.0 / Constant.PARSEC;
		String unit = "pc";
		int n = 1;
		if (!isStar || ephem.distance < 100000) {
			unit = "AU";
			if (Translate.getDefaultLanguage() == Translate.LANGUAGE.SPANISH) unit = "UA";
			factor = 1.0;
			n = 6;
		}
		
		String name = ephem.name + " ";
		if (!showName) name = "";
		String out = "", sep = FileIO.getLineSeparator();
		out += name + Translate.translate(Translate.JPARSEC_RIGHT_ASCENSION)+": " + Functions.formatRA(ephem.rightAscension, 1+decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_DECLINATION)+": " + Functions.formatDEC(ephem.declination, decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_DISTANCE)+": " + Functions.formatValue(ephem.distance * factor, n) + " " + unit + sep;
		if (ephem.rise != null) out += name + Translate.translate(Translate.JPARSEC_RISE)+": " + TimeFormat.formatJulianDayAsDateAndTime(ephem.rise[0], SCALE.LOCAL_TIME) + sep;
		if (ephem.transit != null) out += name + Translate.translate(Translate.JPARSEC_TRANSIT)+": " + TimeFormat.formatJulianDayAsDateAndTime(ephem.transit[0], SCALE.LOCAL_TIME) + sep;
		if (ephem.set != null) out += name + Translate.translate(Translate.JPARSEC_SET)+": " + TimeFormat.formatJulianDayAsDateAndTime(ephem.set[0], SCALE.LOCAL_TIME) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_ELONGATION)+": " + Functions.formatAngle(ephem.elongation, decimalArcsec) + sep;
		if (!isStar) out += name + Translate.translate(Translate.JPARSEC_PHASE_ANGLE)+": " + Functions.formatAngle(ephem.phaseAngle, decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_CONSTELLATION)+": " + ephem.constellation + sep;
		out += name + Translate.translate(Translate.JPARSEC_AZIMUTH)+": " + Functions.formatAngle(ephem.azimuth, decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_ELEVATION)+": " + Functions.formatAngle(ephem.elevation, decimalArcsec) + sep;
		out += name + Translate.translate(Translate.JPARSEC_MAGNITUDE)+": " + (ephem.magnitude > 99 ? "-" : Functions.formatValue(ephem.magnitude, 3)) + sep;
	
		return out;
	}

	/**
	 * Reports an array directly to the console.
	 * @param array The array to print.
	 */
	public static void stringArrayReport(String array[])
	{
		for (int i=0; i<array.length; i++)
		{
			System.out.println(array[i]);
		}
	}
	/**
	 * Reports an array directly to the console.
	 * @param array The array to print.
	 * @param format Format for printing in a Fortran-style way.
	 */
	public static void stringArrayReport(String array[][], String format)
	{
		for (int i=0; i<array.length; i++)
		{
			System.out.println(ConsoleReport.formatAsFortran(array[i], format, true));
		}
	}
	/**
	 * Reports an array directly to the console.
	 * @param array The array to print.
	 * @param format Format for printing in a Fortran-style way.
	 */
	public static void doubleArrayReport(double array[][], String format)
	{
		for (int i=0; i<array.length; i++)
		{
			System.out.println(ConsoleReport.formatAsFortran(
					DataSet.toStringValues(array[i]), format, true));
		}
	}
	/**
	 * Reports an array directly to the console.
	 * @param array The array to print.
	 * @param format Format for printing in a Fortran-style way each number.
	 */
	public static void doubleArrayReport(double array[], String format)
	{
		for (int i=0; i<array.length; i++)
		{
			System.out.println(ConsoleReport.formatAsFortran(new String[] {""+array[i]}, format, true));
		}
	}

	/**
	 * Reports an array of doubles to the console within a supplied text.
	 * In the text use 'xx.xxx' to set the desired decimal places for each of
	 * the values in the array. The will be written in order. Decimal place
	 * in 'xx.xx' is not required, although to identify a number to write
	 * use at least two x ('xx').
	 * @param values The text with generic formmated numbers xxxx.xxx...
	 * @param array The array with the values.
	 * @return The formatted text.
	 */
	public static String doubleArrayReport(String values[], double array[])
	{
		String sep = FileIO.getLineSeparator();
		StringBuffer s = new StringBuffer("");
		int index = 0;
		for (int i=0; i<values.length; i++)
		{
			int x = values[i].indexOf("xx");
			String line = values[i];
			if (x >= 0) {
				while (x >= 0) {
					int end = x;
					int p1 = 1, pp = 0, p2 = 0;
					while (true) {
						end ++;
						if (end == line.length()) break;
						if (line.substring(end, end+1).equals("x")) {
							if (pp > 0) {
								p2 ++;
							} else {
								p1 ++;
							}
							continue;
						}
						if (line.substring(end, end+1).equals(".") && end < line.length()-1 && line.substring(end+1, end+2).equals("x")) {
							pp = 1;
							continue;
						}
						break;
					}
					if (p2 == 0) {
						line = DataSet.replaceOne(line, line.substring(x, end), 
								FileIO.eliminateLeadingZeros(ConsoleReport.formatAsFortran(new String[] {""+array[index]}, "I"+p1, true).trim(), true), 1);
					} else {
						line = DataSet.replaceOne(line, line.substring(x, end), 
							FileIO.eliminateLeadingZeros(ConsoleReport.formatAsFortran(new String[] {""+array[index]}, "f"+p1+"."+p2, true).trim(), true), 1);
					}
					index ++;
					x = line.indexOf("xx");
				};
			}
			s.append(line);
			if (i < values.length-1) s.append(sep);
		}
		return s.toString();
	}
	/**
	 * Formats a set of values in a Fortran style way.
	 * @param val The set of values.
	 * @param format The format to apply, for example '1x, i2, 2f5.4, a3'.
	 * @param fillBefore True to fill with blank spaces before the values in case the
	 * field is longer than the length of the value. False will fill with blanks after 
	 * the field value. True is used commonly so that values will be aligned to the right,
	 * and decimal point always at the same place.
	 * @return The formatted output.
	 */
	public static String formatAsFortran(String val[], String format, boolean fillBefore)
	{
		String out = "";
		DecimalFormat formatter = new DecimalFormat("00.0000");
		
		format = format.toUpperCase();
		int n = FileIO.getNumberOfFields(format, ",", true);
		int index = -1;
		for (int i=0; i<n; i++)
		{
			String f = FileIO.getField(i+1, format, ",", true);
			
			int x = f.indexOf("X");
			if (x >= 0) {
				int pre = ConsoleReport.getPreviousInt(f, "X");
				for (int rep=0; rep<pre; rep++)
				{
					int v = Integer.parseInt(ConsoleReport.removeText(f, "X"));
					for (int j=0; j<v;j++)
					{
						out += " ";
					}
				}
			} else {
				x = f.indexOf("I");
				if (x >= 0) {
					int pre = ConsoleReport.getPreviousInt(f, "I");
					for (int rep=0; rep<pre; rep++)
					{
						int v = Integer.parseInt(ConsoleReport.removeText(f, "I"));
						index ++;
						int w = (int) DataSet.parseDouble(val[index]);
						String z = ""+w;
						if (v < z.length()) {
							out += z.substring(0, v);
						} else {
							if (v > z.length()) {
								if (fillBefore) z = FileIO.addSpacesBeforeAString(z, v);
								if (!fillBefore) z = FileIO.addSpacesAfterAString(z, v);
							}
							out += z;
						}
					}
				} else {
					x = f.indexOf("F");
					if (x >= 0) {
						int pre = ConsoleReport.getPreviousInt(f, "F");
						for (int rep=0; rep<pre; rep++)
						{
							f = ConsoleReport.removeText(f, "F");
							int p = f.indexOf(".");
							int v = Integer.parseInt(f.substring(0,p));
							int w = Integer.parseInt(f.substring(p+1));
							index ++;
							String vv = "", ww = "";
							if (DataSet.parseDouble(val[index]) < 0.0) v--;
							for (int j=0;j <v; j++)
							{
								vv += "0";
							}
							for (int j=0;j <w; j++)
							{
								ww += "0";
							}
							formatter = new DecimalFormat(vv+"."+ww);
							String flo = formatter.format(DataSet.parseDouble(val[index]));
							int po = flo.indexOf(".");
							int s = 0;
							if (flo.startsWith("-")) s = 1;
							if (po > s && flo.substring(s, s+1).equals("0")) {
								int nsp = 0;
								while (true) {
									if (flo.substring(s+1, s+2).equals(".")) break;
									if (s == 0) {
										flo = flo.substring(1);
									} else {
										flo = flo.substring(0, s) + flo.substring(2);
									}
									nsp ++;
									if (!flo.substring(s, s+1).equals("0")) break;
								}
								if (fillBefore) flo = DataSet.repeatString(" ", nsp) + flo;
								if (!fillBefore) flo = flo + DataSet.repeatString(" ", nsp);
							}
							out += flo;
						}
					} else {
						x = f.indexOf("A");
						if (x >= 0) {
							int pre = ConsoleReport.getPreviousInt(f, "A");
							for (int rep=0; rep<pre; rep++)
							{
								int v = Integer.parseInt(ConsoleReport.removeText(f, "A"));
								index ++;
								if (v >= val[index].length()) {
									if (fillBefore) out += FileIO.addSpacesBeforeAString(val[index], v);
									if (!fillBefore) out += FileIO.addSpacesAfterAString(val[index], v);
								} else {
									out += val[index].substring(0, v);
								}
							}
						}
					}				
				}				
			}
		}
		
		out = DataSet.replaceAll(out, ",", ".", true);
		return out;
	}
	
	private static int getPreviousInt(String text, String remove)
	{
		int val = 1;
		String v = "";
		int x = text.indexOf(remove);
		if (x > 0) {
			v = text.substring(0, x).trim();
			if (!v.equals("")) val = Integer.parseInt(v);
		}
		return val;
	}
	private static String removeText(String text, String remove)
	{
		String v = "";
		int x = text.indexOf(remove);
		if (x < (text.length()-1)) v += text.substring(x+1);
		if (v.equals("")) v = "1";
		return v;
	}

	/**
	 * Reports system properties.
	 */
	public static void reportSystemProperties() {
		Properties properties = System.getProperties(); 
		Set<Object> keys = properties.keySet(); 
		for(Object key : keys){ 
			System.out.println(key + ": " + properties.get(key)); 
		} 
	}

	/**
	 * Reports the values of the fields for a given object.
	 * @param o The object.
	 * @throws JPARSECException If an error occurs.
	 */
	public static void reportObjectFields(Object o) throws JPARSECException {
		try
		{
			if (o == null) {
				System.out.println("Null object");
				return;
			}
			
			String className = o.getClass().getCanonicalName();
			Class<?> c = Class.forName(className);
			Field f[] = c.getFields();
			String out = className+": ";
			for (int i=0; i<f.length; i++) {
				Object fv = f[i].get(o);
				String val = "null";
				if (fv != null) val = fv.toString();
				out += f[i].getName()+" = "+val;
				if (i < f.length-1) out += ", ";
			}
			System.out.println(out);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new JPARSECException("cannot report fields.", e);
		}
	}
}
