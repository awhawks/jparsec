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
package jparsec.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import jparsec.graph.*;
import jparsec.io.FileIO;
import jparsec.io.ReadFile;
import jparsec.util.Logger.LEVEL;

/**
 * Translates library to other languages. This class uses direct
 * translation from/to English and Spanish.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Translate 
{
	// private constructor so that this class cannot be instantiated.
	private Translate() {}
		
	/**
	 * The set of languages available in JPARSEC.
	 */
	public static enum LANGUAGE {
		/** ID constant for English. */
		ENGLISH,
		/** ID Constant for Spanish. */
		SPANISH
	};
	
	/**
	 * Selects default language for output.
	 */
	private static LANGUAGE defaultLanguage = LANGUAGE.ENGLISH;

	/**
	 * Translates a given value from one language to another.
	 * @param value Value to search.
	 * @param from ID constant of the language of the value.
	 * @param to ID constant of the output language.
	 * @return Output value, or input String if not found.
	 */
	public static String translate(String value, LANGUAGE from, LANGUAGE to)
	{
		if (value == null) return null;
		if (value.equals("")) return "";
 		if (from == to) return DataSet.replaceAll(value, "\\n", FileIO.getLineSeparator(), true);
		String out = value;
		
		try
		{
			String pathToFile = "jparsec/util/"+from.name().toLowerCase()+".txt";
			String encoding = ReadFile.ENCODING_ISO_8859;
			String file_line = "";
			int index = -1, indexSW = -1, i = -1;
			int RindexSW = -1;
			
			InputStream is = Translate.class.getClassLoader().getResourceAsStream(pathToFile);
			BufferedReader dis = new BufferedReader(new InputStreamReader(is, encoding));

			while ((file_line = dis.readLine()) != null)
			{
				i ++;
				if (file_line.equals(value)) {
					index = i;
					break;
				}
				if (file_line.toLowerCase().startsWith(value.toLowerCase())) indexSW = i;
				if (value.toLowerCase().startsWith(file_line.toLowerCase())) RindexSW = i;
			}

			// Close file
			dis.close();
			
			if (index < 0) {
				index = indexSW;
				// FIXME: check if this is happening and maybe correct it
				if (index < 0) index = RindexSW;
				String s = "";
				if (index >= 0) {
					s = Translate.getEntry(index, from).trim().toLowerCase();
					if (!value.trim().toLowerCase().equals(s)) index = -1;
				}
			}
			if (index >= 0) out = Translate.getEntry(index, to);
		} catch (Exception e1)
		{
			Logger.log(LEVEL.ERROR, "Cannot read language file "+from+". This error should never happen!");
			return null;
		}

		out = DataSet.replaceAll(out, "\\n", FileIO.getLineSeparator(), true);
		return out;
	}

	/**
	 * Translates a given value from English into another.
	 * @param value Value to search.
	 * @param to ID constant of the output language.
	 * @return Output value, or empty String if not found.
	 */
	public static String translate(String value, LANGUAGE to)
	{
		if (LANGUAGE.ENGLISH == to) return value;
		return Translate.translate(value, LANGUAGE.ENGLISH, to);
	}

	/**
	 * Translates a given value from English into selected language.
	 * @param value Value to search.
	 * @return Output value, or empty String if not found.
	 */
	public static String translate(String value)
	{
		if (LANGUAGE.ENGLISH == defaultLanguage) return value;
		return Translate.translate(value, LANGUAGE.ENGLISH, defaultLanguage);
	}

	/**
	 * Translates a given value from English into selected language.
	 * @param stringID ID of the String to be translated. Constants defined in this class.
	 * @return Output value.
	 */
	public static String translate(int stringID)
	{
		try {
			return DataSet.replaceAll(ReadFile.readResourceSomeLines("jparsec/util/"+defaultLanguage.name().toLowerCase()+".txt", ReadFile.ENCODING_ISO_8859, stringID, stringID).get(0), "\\n", FileIO.getLineSeparator(), true);
		} catch (Exception exc) {
			Logger.log(LEVEL.ERROR, "Cannot read line "+stringID+" of language file "+defaultLanguage+". This error should never happen!");
			return null;
		}
	}

	/**
	 * Translates a given array from English into selected language.
	 * @param values Values to be translated.
	 * @return Output value, or empty String if not found.
	 */
	public static String[] translate(String[] values)
	{
		if (LANGUAGE.ENGLISH == defaultLanguage) return values;

		String out[] = null;
		if (values != null) {
			out = new String[values.length];
			for (int i=0; i<out.length; i++) {
				out[i] = Translate.translate(values[i], LANGUAGE.ENGLISH, defaultLanguage);
			}
		}
		return out;
	}

	/**
	 * Returns default language.
	 * @return ID constant of the language.
	 */
	public static LANGUAGE getDefaultLanguage()
	{
		return Translate.defaultLanguage;
	}
	/**
	 * Sets default language.
	 * @param language ID constant of the language.
	 */
	public static void setDefaultLanguage(LANGUAGE language)
	{
		Translate.defaultLanguage = language;
	}

	/**
	 * Returns the string entry for a given index.
	 * @param id The index, from 0 to the number of entries-1.
	 * @param language The language to retrieve the entry in, or null
	 * to use the current default language.
	 * @return The entry.
	 * @throws JPARSECException If the language is invalid, something
	 * that should never happen.
	 */
	public static String getEntry(int id, LANGUAGE language) throws JPARSECException {
		if (language == null) language = Translate.defaultLanguage;
		return ReadFile.readResourceSomeLines("jparsec/util/"+language.name().toLowerCase()+".txt", ReadFile.ENCODING_ISO_8859, id, id).get(0);
	}

	/**
	 * Returns the number of entries.
	 * @return Number of entries.
	 * @throws JPARSECException If the resource cannot be read.
	 */
	public static int getNumberOfEntries() throws JPARSECException {
		return ReadFile.readResourceGetNumberOfLines("jparsec/util/english.txt", ReadFile.ENCODING_ISO_8859);
	}
	
	/**
	 * Test program.
	 * @param args Unused.
	 */
	public static void main(String args[])
	{
		System.out.println("Translate test");

		try {
			String translate[] =  DataSet.arrayListToStringArray(ReadFile.readResource("jparsec/util/english.txt"));
			int index = DataSet.getIndex(translate, "Position angle");
			System.out.println(index);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_EQUATOR = 18;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ECLIPTIC = 19;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GALACTIC_PLANE = 20;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_RIGHT_ASCENSION = 21;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DECLINATION = 22;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_HORIZON = 23;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ECLIPTIC_LONGITUDE = 24;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ECLIPTIC_LATITUDE = 25;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GALACTIC_LONGITUDE = 26;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GALACTIC_LATITUDE = 27;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_AZIMUTH = 28;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ELEVATION = 29;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_STELLAR_MAGNITUDES = 30;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TYPES_OF_STARS = 31;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DOUBLE = 32;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_VARIABLE = 33;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OPEN_CLUSTER = 34;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GLOBULAR = 35;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_NEBULOSE = 36;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MILKY_WAY = 37;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DEEP_SKY_OBJECTS = 38;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PLAN_NEB = 39;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GALAXY = 40;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DEFAULT_OCULAR = 64;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PARTIAL_BEGINS = 147;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PARTIAL_ENDS = 152;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_VISIBLE_DISK_FRACTION = 155;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_JULIAN_DAY = 156;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MAGNITUDE = 157;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_COMBINED_MAGNITUDE = 158;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_LIGHT_CURVE = 159;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OF = 160;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_BY = 161;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_AND = 162;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ECLIPSED = 163;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OCCULTED = 164;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TRANSITING = 165;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SHADOW_TRANSITING = 166;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ANNULAR = 167;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TOTAL_ = 168;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PARTIAL = 169;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_LINE = 175;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SPLINE_INTERPOLATION = 213;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ERROR = 230;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INSERT_A_VALID_SOURCE_NAME = 231;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ERROR_WHILE_SOLVING_SOURCE__CHECK_YOUR_INTERNET_CONNECTION_IS_ACTIVE = 232;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DO_YOU_WANT_TO_OVERWRITE_THE_FILE = 238;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PLEASE_SELECT_A_VALID_FILE = 239;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_WARNING = 240;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DETAILS = 241;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CANNOT_PRINT_THE_DIALOG = 242;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_COULD_NOT_PRINT_THE_CHART = 243;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_COULD_NOT_SAVE_THE_DIALOG = 244;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_COULD_NOT_LOAD_THE_DIALOG = 245;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SOME_OF_THE_TRANSITIONS_COULD_NOT_BE_READ_DUE_TO_A_MEMORY_LIMIT = 256;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_AUTOMATICALLY_GENERATED = 257;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PACKAGE = 258;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ON = 259;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FILE = 260;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SAVE = 261;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PRINT = 262;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CLOSE = 263;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_UNKNOWN_DAYLIGHT_SAVING_TIME_RULE = 273;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_NO_INFORMATION_ABOUT_DAYLIGHT_SAVING_TIME_EXISTS_FOR_COUNTRY = 274;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_UT1_UTC_NOT_AVAILABLE = 275;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CANNOT_USE_RADEX_WITH_THIS_MOLECULE = 276;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ERROR_USING_RADEX_IN_MAP_POSITION = 277;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_STARTING_CALCULATIONS = 278;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FULL_SAMPLING_IS_NOT_REACHED__DECREASE_MAP_STEP_OR_INCREASE_THE_BEAM = 279;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_COMPLETED = 280;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_NO_BEAM_TO_CONVOLVE = 281;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INTEGRATION_INTERVAL_FROM = 282;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TO = 283;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FOR = 284;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_EXCEEDS_ACEPTABLE_INTERVAL = 285;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TOO_MUCH = 286;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_USING_VALUE = 287;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_NOT_VALID_IN_POSITION = 288;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SKIPPING_THIS_ONE = 289;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_VELOCITY = 292;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHANNEL_NUMBER = 293;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FREQUENCY = 294;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_RISE = 295;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET = 296;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TRANSIT = 297;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TRANSIT_ELEVATION = 298;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DISTANCE = 299;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_LIGHT_TIME = 300;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ELONGATION = 301;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PHASE_ANGLE = 302;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PHASE = 303;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_HELIOCENTRIC_ECLIPTIC_LONGITUDE = 304;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_HELIOCENTRIC_ECLIPTIC_LATITUDE = 305;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_HELIOCENTRIC_DISTANCE = 306;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DEFECT_OF_ILLUMINATION = 307;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ANGULAR_RADIUS = 308;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SURFACE_BRIGHTNESS = 309;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SUBSOLAR_LONGITUDE = 311;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SUBSOLAR_LATITUDE = 312;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PARALACTIC_ANGLE = 313;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_BRIGHT_LIMB_ANGLE = 314;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_POSITION_ANGLE_OF_AXIS = 315;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_POSITION_ANGLE_OF_POLE = 316;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SYSTEM = 317;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_LONGITUDE_OF_CENTRAL_MERIDIAN = 318;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_POLE_RIGHT_ASCENSION = 319;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_POLE_DECLINATION = 320;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CONSTELLATION = 321;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TYPE_OF_COORDINATES_ = 332;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_POSITION_ANGLE = 336;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OPACITY = 339;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PROJECTION_SYSTEM = 347;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_AM_MODEL_RESULTS = 395;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OBSERVED = 396;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DISK = 397;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FREE_FREE_EXTRAPOLATED = 398;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FREE_FREE = 399;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TOTAL = 400;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FLUX_AS_FUNCTION_OF_WAVELENGTH_FOR = 401;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PAH_CARBONACEUS_ION = 407;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MIXTURE = 409;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ENVELOPE_MODEL_FIGURE = 410;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_X_AXIS_ = 411;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_Y_AXIS_ = 412;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ROTATIONAL_DIAGRAM = 413;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TEMPERATURE = 414;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SIESS_TRACKS = 415;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_WAVELENGTH = 419;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_RADIUS_ = 420;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TEMPERATURE_AS_FUNCTION_OF_RADIUS = 421;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DEVIATION = 422;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_EFFECT_OF_VARYING = 430;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DUST_MASS = 431;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DISK_OUTER_RADIUS = 432;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_RIM_TEMPERATURE = 433;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DENSITY_SLOPE = 434;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INCLINATION = 435;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_IN_INTERIOR_LAYER = 436;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_IN_SURFACE_LAYER = 437;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ABUNDANCE_IN_INTERIOR_LAYER = 438;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ABUNDANCE_IN_SURFACE_LAYER = 439;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DUST_MASS_ = 440;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OUTER_RADIUS_ = 441;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_RIM_TEMPERATURE_ = 442;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DENSITY_SLOPE_ = 443;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DENSITY_SLOPE__ = 443;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DENSITY_SLOPE____ = 443;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INCLINATION_ = 444;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ABUNDANCE = 445;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GRAIN_MAXIMUM_SIZE = 446;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OPACITY_OF = 447;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MAXIMUM_SIZE_OF = 448;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SPECTRAL_INDEX_OF = 449;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SPECTRAL_INDEX = 450;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OPACITY_FOR_MAXIMUM_SIZE_OF = 451;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SPECTRAL_INDEX_FOR_MAXIMUM_SIZE_OF = 452;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_DATE_AND_TIME = 453;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_YEAR = 454;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_YEAR_HERE = 455;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_HOUR = 456;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_HOUR_HERE = 457;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MONTH = 458;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_MONTH_HERE = 459;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MINUTE = 460;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_MINUTE_HERE = 461;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DAY = 462;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_DAY_HERE = 463;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SECOND = 464;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_SECOND_HERE = 465;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CALENDAR = 466;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_CALENDAR = 467;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_TIME_SCALE = 468;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TIME_SCALE = 469;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TIME_DIALOG = 470;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_DATE = 471;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_INPUT_CALENDAR = 472;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INPUT_CALENDAR = 473;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_OUTPUT_CALENDAR = 474;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OUTPUT_CALENDAR = 475;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CALENDAR_CONVERSION_DIALOG = 476;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_TELESCOPE = 477;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TELESCOPE = 478;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_OCULAR = 479;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OCULAR = 480;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TELESCOPE_DIALOG = 481;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_TARGET_BODY = 482;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TARGET = 483;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TARGET_BODY = 484;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_EPHEMERIS_TYPE = 485;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TYPE = 486;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_EPHEMERIS_TYPE = 487;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_CALCULATION_METHOD_TO_APPLY = 488;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_METHOD = 489;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_METHOD_TO_REDUCE_THE_COORDINATES = 490;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_ALGORITHM_TO_USE = 491;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ALGORITHM = 492;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_GEOCENTRIC_OR_TOPOCENTRIC_RESULTS = 493;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GEOCENTRIC = 494;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TOPOCENTRIC = 495;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FOR_GEOCENTRIC_COORDINATES = 496;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FOR_TOPOCENTRIC_COORDINATES = 497;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_EQUINOX_AS_JULIAN_DAY = 498;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_EQUINOX = 499;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_EQUINOX__SET_TO__1 = 500;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_FRAME = 501;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FRAME = 502;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FRAME_OF_OUTPUT_COORDINATES = 503;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_EPHEMERIS_DIALOG = 504;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_LOCATION_NAME = 505;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_NAME = 506;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_PLACE_HERE = 507;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_LOCATION_TYPE = 508;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CITY = 509;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OBSERVATORY = 510;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THIS_FOR_A_CITY = 511;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THIS_FOR_AN_OBSERVATORY = 512;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OBSERVER_DIALOG = 513;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_INPUT_COORDINATES = 514;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_LONGITUDE = 515;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_LONGITUDE_ = 516;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_LATITUDE = 517;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_LATITUDE_ = 518;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_TYPE_OF_THE_INPUT_COORDINATES = 519;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INPUT_COORDINATES = 520;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TYPE_OF_COORDINATES = 521;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_TYPE_OF_THE_OUTPUT_COORDINATES = 522;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OUTPUT_COORDINATES = 523;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_COORDINATES_TRANSFORM_DIALOG = 524;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_LABELS = 525;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHART_TITLE = 526;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHART_X_LABEL = 527;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_LABEL_FOR_X_AXIS = 528;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHART_Y_LABEL = 529;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_LABEL_FOR_Y_AXIS = 530;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_CHART_TYPE_AND_SUBTYPE = 531;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHART_TYPE = 532;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SUBTYPE = 533;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_SERIES = 534;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_NEW = 535;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SERIES = 536;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SERIES_TO_BE_DRAWN = 537;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CREATE_EDIT = 538;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CLIC_TO_CREATE_EDIT_A_SERIES = 539;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DELETE = 540;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CLIC_TO_DELETE_A_SERIES = 541;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_OTHER_PARAMETERS = 542;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_WIDTH = 543;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_WIDTH_IN_PIXELS = 544;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_X_AXIS_IN_LOG_SCALE = 545;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_DRAW_X_AXIS_IN_LOG_SCALE = 546;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_HEIGHT = 547;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_HEIGHT_IN_PIXELS = 548;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_Y_AXIS_IN_LOG_SCALE = 549;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_DRAW_Y_AXIS_IN_LOG_SCALE = 550;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SHOW_ERROR_BARS = 551;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_DRAW_ERROR_BARS = 552;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHART_DIALOG = 553;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_DATA_POINTS = 554;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DATASET = 555;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_DATASET = 556;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_MAIN_PROPERTIES = 557;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHART_LEGEND = 558;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_LEGEND = 559;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_COLORS_FOR_THE_POINTS = 560;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_POINT_SHAPE = 561;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SHAPES_FOR_THE_POINTS = 563;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_REGRESSION = 564;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_REGRESSION_TYPE = 565;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_LINE_STROKE = 566;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_LINE_STROKE = 567;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_POINTER_ANGLE = 568;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_POINTERS_ANGLE = 569;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SHOW_POINTS = 570;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_DRAW_THE_POINTS = 571;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SHOW_POINTER_ARROWS = 572;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_DRAW_POINTER_ARROWS = 573;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SHOW_LEGEND = 574;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_SHOW_THE_LEGEND = 575;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SHAPE_SIZE = 576;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_SHAPE_SIZE = 577;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SERIES_DIALOG = 578;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PROJECTION = 579;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_COORDINATE_SYSTEM = 580;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CENTRAL_LONGITUDE = 581;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_CENTRAL_LONGITUDE_IN_DEGREES_OR_HOURS = 582;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CENTRAL_LATITUDE = 583;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_CENTRAL_LATITUDE_IN_DEGREES_OR_DEGREES = 584;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_IMAGE_WIDTH = 585;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_IMAGE_HEIGHT = 586;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SKY_RENDER_DIALOG = 589;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_STAR_PROPERTIES = 590;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_EFFECTIVE_TEMPERATURE_IN_K = 591;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_DISTANCE_IN_PC = 592;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MASS = 593;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_STAR_MASS_IN_SOLAR_UNITS = 594;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INCLINATION__ = 595;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INCLINATION___ = 595;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INCLINATION____ = 595;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_INCLINATION_IN_DEGREES_RESPECT_TO_A_FACE_ON_VIEW = 596;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_STAR_LUMINOSITY_IN_SOLAR_UNITS = 597;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_DISK_PROPERTIES = 598;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_DISK_MASS_ = 599;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OUTER_RADIUS = 600;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_OUTER_RADIUS_IN_AU = 601;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_RIM_TEMPERATURE_IN_K = 602;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DENSITY_SLOPE___ = 603;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_GRAIN_PROPERTIES = 604;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INTERIOR_LAYER = 605;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GRAINS_FOR_THE_INTERIOR_LAYER = 606;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ADD_EDIT = 607;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CLIC_TO_ADD_EDIT_A_GRAIN = 608;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CLIC_TO_DELETE_A_GRAIN = 609;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SURFACE_LAYER = 610;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GRAINS_FOR_THE_SURFACE_LAYER = 611;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_LAYERS = 612;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_NUMBER_OF_LAYERS = 613;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_WAVELENGTHS = 614;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_NUMBER_OF_WAVELENGTHS_TO_COMPUTE = 615;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_USE_KURUCZ_STELLAR_MODELS = 616;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_RIM_AS_SURFACE = 617;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_TREAT_THE_RIM_GRAINS_AS_THE_SURFACE_GRAINS = 618;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PURE_CG97 = 619;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_USE_A_PURE_CHIANG_GOLDREICH_MODEL = 620;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_LUMINOSITY_UNITS = 621;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_SHOW_THE_CHART_IN_UNITS_OF_LUMINOSITY_INSTEAD_OF_FLUX = 622;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SOLAR_UNITS = 623;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_SHOW_THE_CHART_IN_SOLAR_UNITS_INSTEAD_OF_PHYSICAL_UNITS = 624;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SED_FIT_DIALOG = 625;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_DUST_PROPERTIES = 626;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GRAIN_TYPE = 627;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THE_GRAIN = 628;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MAX_SIZE = 629;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THE_MAXIMUM_GRAIN_SIZE = 630;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SIZE_SLOPE = 631;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THE_GRAIN_DISTRIBUTION_SLOPE = 632;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ABUNDANCE_ = 633;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_RELATIVE_ABUNDANCE_OF_THIS_GRAIN = 634;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DUST_GRAIN_DIALOG = 635;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_OUPUT_FILE_NAME = 636;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FILE_NAME = 637;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_FILE_NAME = 638;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CLIC_TO_SELECT_A_FILE = 639;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_OUTPUT_FORMAT = 640;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_EXTENSION = 641;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OUTPUT_FORMATS_ALLOWED = 642;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SAVE_AS_DIALOG = 643;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_SOURCE_NAME = 644;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SOURCE = 645;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_SOURCE = 646;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SOLVE_WITH_SIMBAD = 647;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CLIC_TO_SOLVE_USING_SIMBAD = 648;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SIMBAD_SOLVER = 649;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_ENVELOPE_PROPERTIES = 650;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MODEL_FIGURE = 651;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THE_FIGURE = 652;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_ENVELOPE_MASS_IN_SOLAR_MASSES = 653;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_DENSITY_POWER_LAW_SLOPE_ = 654;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INITIAL_RADIUS = 655;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_INITIAL_RADIUS_OF_THE_ENVELOPE_IN_AU = 656;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FINAL_RADIUS = 657;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_FINAL_RADIUS_OF_THE_ENVELOPE_IN_AU = 658;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_BEAM_SIZE = 659;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_BEAM_TO_CONVOLVE_WITH_IN_ARCSECONDS = 660;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ENVELOPE_LAYER = 661;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GRAINS_FOR_THE_ENVELOPE = 662;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ICY_MANTLES = 663;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THE_ICY_MANTLES_FOR_OSSENKOPF__HENNING_1992_OPACITIES = 664;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_LONGITUDE_OF_THE_FIGURE_RELATIVE_TO_THE_OUTER_RADIUS = 665;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_ALTITUDE_OF_THE_FIGURE_RELATIVE_TO_THE_OUTER_RADIUS = 666;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_INCLINATION_OF_THE_FIGURE_IN_DEGREES = 667;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ENVELOPE_IN_SHADOW = 668;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_EXTRAPOLATE_DISK_TEMPERATURE_PROFILE_FOR_THE_ENVELOPE_ = 669;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ENABLE_ENVELOPE = 670;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_MAKE_THE_ENVELOPE_ACTIVE = 671;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_EXTINCTION_PROPERTIES = 672;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_THE_RATIO_OF_TOTAL_TO_SELECTIVE_ABSORPTION_AT_VISIBLE__TYPICALLY_3_1_TO_5_FOR_THE_MILKY_WAY = 673;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_THE_B_V_COLOR_OBSERVED_FOR_THIS_STAR = 674;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SPECTRAL_TYPE = 675;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_THE_SPECTRAL_TYPE_OF_THE_STAR_AS_OBAFGKM_FOLLOWED_BY_THE_SUBTYPE_NUMBER = 676;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MAIN_SEQUENCE = 677;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_IF_THE_STAR_BELONGS_TO_THE_MAIN_SEQUENCE = 678;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ENABLE_EXTINCTION = 679;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_ENABLE_EXTINCTION = 680;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ENVELOPE_DIALOG = 681;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_FIGURE_PROPERTIES = 682;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_INCLINATION_IN_DEGREES = 683;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_BEAM_TO_CONVOLVE_WITH_IN_ARCSECONDS_ = 684;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SHAPE_PARAM_1 = 685;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_SHAPE_PARAMETER_1_ = 686;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SHAPE_PARAM_2 = 687;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_SHAPE_PARAMETER_2_ = 688;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_LINE_PROPERTIES = 689;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CATALOG = 690;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TRANSITION = 691;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_CATALOG = 692;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_GAS_PROPERTIES = 693;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DENSITY_FUNCTION = 694;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_DENSITY_FUNCTION_IN_CM_3 = 695;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_TEMPERATURE_FUNCTION = 696;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_TEMPERATURE_FUNCTION_IN_K = 697;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ABUNDANCE_FUNCTION = 698;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_MAP_STEP_IN_AU = 699;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_VELOCITY_FUNCTION = 700;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_VELOCITY_WIDTH_FUNCTION = 701;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MAX__INTEGR = 702;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_MAXIMUM_INTEGRATION_STEP_IN_AU = 703;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_BACKGROUND_TEMPERATURE_IN_K = 704;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MAP_STEP = 705;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_VELOCITY_LIMITS = 706;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_SPECTRUM_WINDOW_VELOCITY_LIMITS_IN_KM_S = 707;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ENABLE_RADEX = 708;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_ENABLE_RADEX_IF_POSSIBLE = 709;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CONVOLVE_OUTSIDE_MAP = 710;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_CONSIDER_POINTS_THAT_LIES_OUTSIDE_THE_VISIBLE_MAP_WHEN_CONVOLVING_ = 711;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OPTICALLY_THIN = 712;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_FORCE_OPTICALLY_THIN_APPROXIMATION = 713;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_USE_SPLINE_INTERPOLATION_TO_OBTAIN_THE_VALUE_OF_THE_PARTITION_FUNCTION_INSTEAD_OF_LINEAR = 714;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_OUTPUT_FILE_NAME = 715;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_PATH_OF_THE_OUTPUT_ = 716;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT = 717;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SCRIPT_FILE_VARIABLE = 718;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_PATH_OF_THE_GILDAS_SCRIPT_TO_EXECUTE_AFTER_THE_MODEL_AND_THE_OUTPUT_VARIABLE_TO_RETRIEVE_FROM_IT = 719;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_K_TO_JY_COEFF = 720;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_K_TO_JY_TRANSFORM_COEFFICIENT = 721;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MODEL_PARAMETERS = 722;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_FREE_FREE_PROPERTIES = 723;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_AUTOMATIC_MODE = 724;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_USE_AUTOMATIC_MODE_TO_CALCULATE_FREE_FREE_EMISSION = 725;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FORCE_SLOPE = 726;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_VALUE_OF_THE_FREE_FREE_SLOPE = 727;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ENABLE_FREE_FREE = 728;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_MAKE_THE_FREE_FREE_EMISSION_ACTIVE = 729;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FREE_FREE_EMISSION_DIALOG = 730;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_CATALOG_AND_MOLECULE = 747;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MOLECULE = 748;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MIN = 749;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MAX = 750;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MAX_ = 751;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_MIN_ = 752;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CATALOG_SEARCH = 753;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_RESULTS = 754;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SEARCH = 755;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_MINIMUM_FREQUENCY_IN_MHZ = 756;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_MAXIMUM_FREQUENCY_IN_MHZ = 757;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_MAXIMUM_TEMPERATURE_OF_THE_TRANSITION_IN_K = 758;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_MINIMUM_INTENSITY_IN_LOG_SCALE_AS_GIVEN_IN_THE_CATALOGS = 759;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_RESULTS_OF_THE_SEARCH = 760;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CLIC_TO_EXECUTE = 761;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_INPUT_DATA = 762;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THE_INPUT_FILE = 763;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_DATA_FROM_THE_INPUT_FILES = 764;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CLIC_TO_LOAD_THE_SPECIFIED_FILE_AND_TO_SET_THE_CONTENTS_OF_THE_MAIN_TEXT_AREA = 765;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INPUT_DATA = 766;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_LOAD = 767;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ADD = 768;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CLIC_TO_ADD_THE_CONTENTS_OF_THE_MAIN_TEXT_AREA_AS_A_NEW_INTERPOLATION_FILE = 769;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_X_POSITION_TO_INTERPOLATE = 770;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_POSITION = 771;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_NUMBER_OF_DECIMALS = 772;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_NUMBER_OF_DECIMAL_PLACES_IN_THE_RESULTS = 773;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_INTERPOLATE_USING_SPLINE_METHOD = 774;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SPLINE = 775;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_RESULTS_OF_THE_INTERPOLATION = 776;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INTERPOLATION_DIALOG = 777;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_EXECUTE = 778;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ERROR_WHILE_PERFORMING_SEARCH__CHECK_YOUR_INPUT = 779;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ERROR_WHILE_LOADING_FILE = 780;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ERROR_WHILE_SAVING_FILE = 781;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INSERT_THE_NAME_OF_THE_NEW_FILE_IN_THE_LIST = 782;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INSERT_NAME = 783;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ERROR_WHILE_INTERPOLATING = 784;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SURVEY = 785;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FIELD = 786;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SOURCE_NAME = 787;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THE_SURVEY = 788;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FIELD_OF_VIEW_IN_DEGREES = 789;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_COORDINATES = 790;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THE_TYPE_OF_COORDINATES = 791;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_COLOR_TABLE = 792;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THE_COLOR_TABLE_ = 793;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SIZE_OF_THE_IMAGE_IN_PIXELS = 794;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SIZE = 795;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_INVERT = 796;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_INVERT_LEVELS = 797;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_SHOW_THE_GRID = 798;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_GRID = 799;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SELECT_THE_SCALING = 800;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SCALING = 801;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CONTOURS = 802;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SHOW_OVERLAY_OF_CONTOURS_OF_ANOTHER_SURVEY = 803;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SKYVIEW_DIALOG = 804;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_NAME_OF_ONE_OR_SEVERAL_VIZIER_CATALOGS_ = 805;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CLIC_TO_SAVE_IMAGE = 806;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ERROR_WHILE_EXECUTING = 807;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ERROR_WHILE_SAVING = 808;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_PAH_CARBONACEOUS_NEUTRAL = 809;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_FLUX = 810;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_POSITION_ANGLE = 811;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_THE_TITLE = 812;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_AUXILIAR_VALUES = 813;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_AUXILIAR_VALUES = 814;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ADD_EDIT_VALUE = 815;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_VEL = 816;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_SET_VEL_WIDTH = 817;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_ALL_MOLECULES = 856;
	/** ID value for the corresponding String constant. */
	public static final int JPARSEC_CHECK_TO_SEARCH_IN_ALL_MOLECULES = 857;
}
