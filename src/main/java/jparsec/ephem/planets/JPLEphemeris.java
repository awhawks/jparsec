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
package jparsec.ephem.planets;

import jparsec.ephem.*;
import jparsec.ephem.EphemerisElement.ALGORITHM;
import jparsec.ephem.EphemerisElement.COORDINATES_TYPE;
import jparsec.ephem.EphemerisElement.FRAME;
import jparsec.ephem.EphemerisElement.REDUCTION_METHOD;
import jparsec.ephem.Target.TARGET;
import jparsec.ephem.event.LunarEvent;
import jparsec.ephem.moons.MoonEphem;
import jparsec.ephem.planets.imcce.Elp2000;
import jparsec.graph.*;
import jparsec.io.*;
import jparsec.util.*;
import jparsec.time.*;
import jparsec.time.TimeElement.SCALE;
import jparsec.observer.*;
import jparsec.math.*;
import jparsec.math.matrix.Matrix;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLConnection;

/**
 * A class to perform ephemeris calculations using JPL numerical integration
 * theories. Available integrations are DE200, DE403, DE405, DE406, DE413,
 * DE414, DE422, and DE430. The necessary files in ASCII format are provided to cover 
 * the time span from 1950 to 2050, or greater in some cases.
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class JPLEphemeris {
	
	/**
	 * ID for the ephemeris version.
	 */
	private EphemerisElement.ALGORITHM jplID;
	/**
	 * Interval between records.
	 */
	private double jds;
	/**
	 * ncoeff parameter.
	 */
	private int ncoeff;
	/**
	 * Dates for the different ascii files.
	 */
	private double dates[];
	/**
	 * Years for the different ascii files.
	 */
	private int years[];
	/**
	 * Names of constants.
	 */
	private String cnam[];
	/**
	 * Constants values.
	 */
	private double cval[];
	/**
	 * Number of coefficients for planets.
	 */
	private int lpt[][];
	
	private int yearsPerFile;

	private String externalPath;

	/**
	 * Default constructor for DE405 JPL ephemerides,
	 * @throws JPARSECException If the integration version is not available
	 * (the header could not be read).
	 */
	public JPLEphemeris()
	throws JPARSECException {
		this(EphemerisElement.ALGORITHM.JPL_DE405, Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH);
	}

	/**
	 * Constructor.
	 * @param jplID ID for the JPL ephemeris version.
	 * @throws JPARSECException If the integration version is not available
	 * (the header could not be read).
	 */
	public JPLEphemeris(EphemerisElement.ALGORITHM jplID)
	throws JPARSECException {
		this(jplID, Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH);
	}

	/**
	 * Constructor.
	 * @param jplID ID for the JPL ephemeris version.
	 * @param externalPath External path for the external directory 
	 * of the JPL files to read. They must be ASCII files named like 
	 * ascpYEAR.xxx, where YEAR is the starting year of validity and
	 * xxx is the JPL ephemeris version. Only JPL DE405 (years 1600
	 * to 2200), DE406/422/424 (3000 B.C. to 3100), and DE430 (1550 to 2650) are 
	 * supported when reading external files. Files can be downloaded
	 * from ftp://ssd.jpl.nasa.gov/pub/eph/planets/ascii/. Set to null to
	 * use JPARSEC file if it is available. 
	 * @throws JPARSECException If the integration version is not available
	 * (the header could not be read).
	 */
	public JPLEphemeris(EphemerisElement.ALGORITHM jplID, String externalPath)
	throws JPARSECException {
		if (jplID.name().indexOf("JPL") < 0) throw new JPARSECException("This is not a JPL algorithm.");
		if (externalPath != null && !externalPath.endsWith(FileIO.getFileSeparator())) externalPath += FileIO.getFileSeparator();
		this.externalPath = externalPath;
		this.jplID = jplID;
		
		switch (jplID)
		{
		case JPL_DE430:
			dates = new double[] { 2287184.5, 2323696.5, 2360208.5,
					2396752.5, 2433264.5, 2469776.5, 2506320.5, 2542832.5, 2579376.5,
					2615888.5, 2652400.5, 2688976.5 };
			years = new int[] {
					1550, 1650, 1750, 1850, 1950, 2050, 2150, 2250, 2350, 2450, 2550
			};
			yearsPerFile = 100;
			break;
		case JPL_DE424:
			dates = new double[] { 625296.5, 661808.5, 698352.5, 734864.5,
					771376.5, 807920.5, 844432.5, 880976.5, 917488.5, 954032.5,
					990544.5, 1027056.5, 1063600.5, 1100112.5, 1136656.5, 1173168.5,
					1209680.5, 1246224.5, 1282736.5, 1319280.5, 1355792.5, 1392304.5,
					1428848.5, 1465360.5, 1501904.5, 1538416.5, 1574928.5, 1611472.5,
					1647984.5, 1684528.5, 1721040.5, 1757552.5, 1794096.5, 1830608.5,
					1867152.5, 1903664.5, 1940176.5, 1976720.5, 2013232.5, 2049776.5,
					2086288.5, 2122832.5, 2159344.5, 2195856.5, 2232400.5, 2268912.5,
					2305424.5, 2341968.5, 2378480.5, 2414992.5, 2451536.5, 2488048.5,
					2524592.5, 2561104.5, 2597616.5, 2634160.5, 2670672.5, 2707184.5,
					2743728.5, 2780240.5, 2816816.5 };
			years = new int[] {
					-3000, -2900, -2800, -2700, -2600, -2500, -2400, -2300, -2200, -2100,
					-2000, -1900, -1800, -1700, -1600, -1500, -1400, -1300, -1200, -1100, 
					-1000, -900, -800, -700, -600, -500, -400, -300, -200,  -100, 0, 100,
					200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300,
					1400, 1500, 1600, 1700, 1800, 1900, 2000, 2100, 2200, 2300, 2400,
					2500, 2600, 2700, 2800, 2900, 3000
			};
			yearsPerFile = 100;
			break;
		case JPL_DE422:
			// Values comes from JDEread project by Peter Hristozov
			dates = new double[] {
				625648.5, 661808.5, 698352.5, 734864.5, 771376.5, 807920.5, 844432.5,
				880976.5, 917488.5, 954032.5, 990544.5, 1027056.5, 1063600.5, 1100112.5, 1136656.5, 1173168.5,
	            1209680.5, 1246224.5, 1282736.5, 1319280.5, 1355792.5, 1392304.5, 1428848.5, 1465360.5,
	            1501904.5, 1538416.5, 1574928.5, 1611472.5, 1647984.5, 1684528.5, 1721040.5, 1757552.5,
	            1794096.5, 1830608.5, 1867152.5, 1903664.5, 1940176.5, 1976720.5, 2013232.5, 2049776.5,
	            2086288.5, 2122832.5, 2159344.5, 2195856.5, 2232400.5, 2268912.5, 2305424.5, 2341968.5,
	            2378480.5, 2414992.5, 2451536.5, 2488048.5, 2524592.5, 2561104.5, 2597616.5, 2634160.5,
	            2670672.5, 2707184.5, 2743728.5, 2780240.5, 2816816.5
			};
			years = new int[] {
					-3000, -2900, -2800, -2700, -2600, -2500, -2400, -2300, -2200, -2100,
					-2000, -1900, -1800, -1700, -1600, -1500, -1400, -1300, -1200, -1100, 
					-1000, -900, -800, -700, -600, -500, -400, -300, -200,  -100, 0, 100,
					200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300,
					1400, 1500, 1600, 1700, 1800, 1900, 2000, 2100, 2200, 2300, 2400,
					2500, 2600, 2700, 2800, 2900, 3000
			};
			yearsPerFile = 100;
			break;
		case JPL_DE406:
			dates = new double[] {
					// Values comes from JDEread project by Peter Hristozov
                	625360.5, 661776.5, 698320.5, 734864.5, 771344.5, 807888.5, 844432.5, 880976.5, 917456.5, 954000.5,
                	990544.5, 1027024.5, 1063568.5, 1100112.5, 1136656.5, 1173136.5, 1209680.5, 1246224.5, 1282704.5, 1319248.5, 
                	1355792.5, 1392272.5, 1428816.5, 1465360.5, 1501904.5, 1538384.5, 1574928.5, 1611472.5, 1647952.5,  1684496.5,
					1721040.5, 1757520.5, 1794064.5, 1830608.5, 1867152.5, 1903632.5, 1940176.5, 1976720.5, 2013200.5,
					2049744.5, 2086288.5, 2122832.5, 2159312.5, 2195856.5, 2232400.5, 2268880.5, 2305424.5, 2341968.5,
					2378448.5, 2414992.5, 2451536.5, 2488080.5, 2524560.5, 2561104.5, 2597584.5, 2634128.5, 2670672.5,
					2707152.5, 2743696.5, 2780240.5, 2816848.5
			};
			years = new int[] {
					-3000, -2900, -2800, -2700, -2600, -2500, -2400, -2300, -2200, -2100,
					-2000, -1900, -1800, -1700, -1600, -1500, -1400, -1300, -1200, -1100, 
					-1000, -900, -800, -700, -600, -500, -400, -300, -200,  -100, 0, 100,
					200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400,
					1500, 1600, 1700, 1800, 1900, 2000, 2100, 2200, 2300, 2400, 2500,
					2600, 2700, 2800, 2900, 3000
			};
			yearsPerFile = 100;
			break;
		case JPL_DE200:
			dates = new double[] {
					2429616.5, 2436912.5, 2444208.5, 2451536.5, 2458832.5, 2466128.5, 2473424.5
			};
			years = new int[] {
					1940, 1960, 1980, 2000, 2020, 2040, 2060
			};
			yearsPerFile = 20;
			break;
		case JPL_DE403:
		case JPL_DE413:
			dates = new double[] {
					2433264.5, 2442384.5, 2451536.5, 2460656.5, 2469776.5
			};
			years = new int[] {
					1950, 1975, 2000, 2025, 2050
			};
			yearsPerFile = 25;
			break;
		case JPL_DE405:
			dates = new double[] {
					2305424.5, 2312720.5, 2320048.5, 2327344.5, 2334640.5, 2341968.5, 2349264.5, 2356560.5, 2363856.5,
					2371184.5, 2378480.5, 2385776.5, 2393104.5, 2400400.5, 2407696.5, 2414992.5,  2422320.5, 2429616.5,
					2436912.5, 2444208.5, 2451536.5, 2458832.5, 2466128.5, 2473456.5, 2480752.5, 2488048.5, 2495344.5,
					2502672.5, 2509968.5, 2517264.5, 2524624.5
			};
			years = new int[] {
					1600, 1620, 1640, 1660, 1680, 1700, 1720, 1740, 1760, 1780, 1800, 1820, 1840, 1860, 1880,
					1900, 1920, 1940, 1960, 1980, 2000, 2020, 2040, 2060, 2080, 2100, 2120, 2140, 2160, 2180
			};
			yearsPerFile = 20;
			break;
		case JPL_DE414:
			dates = new double[] {
					2433264.5, 2451536.5, 2469808.5
			};
			years = new int[] {
					1950, 2000, 2050
			};
			yearsPerFile = 50;
			break;
		default:
			throw new JPARSECException("invalid jpl ephemeris version.");
		}
		this.readHeader();
	}
	
	/**
	 * Returns JPL ephemeris version of this instance.
	 * The returned value is not equal to {@linkplain JPLEphemeris#jplID}.
	 * For example, this method returns 200 for DE200.
	 * @return JPL version.
	 */
	public int getJPLVersion()
	{
		return EphemerisElement.JPL_EPHEMERIS_VERSIONS[this.jplID.ordinal()];
	}

	/**
	 * Returns JPL ephemeris id version of this instance.
	 * The returned value is equal to {@linkplain JPLEphemeris#jplID}.
	 * For example, this method returns 0 for the last supported version, 
	 * DE430.
	 * @return JPL version id.
	 */
	public EphemerisElement.ALGORITHM getJPLVersionID()
	{
		return this.jplID;
	}

	/**
	 * Returns if the ephemerides file is available or not.
	 * @param jd Julian day of the calculations, file name depends on this.
	 * @return True or false.
	 */
	public boolean isAvailable(double jd) {
		int i = 0;
		String filename = " ";
		double ephemerisDates[] = new double[3];
		try
		{
			int index = -1;
			for (i=0; i<this.dates.length-1; i++)
			{
				if (jd >= this.dates[i] && jd < this.dates[i+1]) {
					index = i;
					break;
				}
			}
			if (index < 0) throw new JPARSECException("cannot calculate ephemeris for this date.");

			int year = this.years[index];
			ephemerisDates[1] = dates[index];
			ephemerisDates[2] = dates[index+1];
			filename = "asc";
			if (year >= 0) {
				filename +="p";
			} else {
				filename +="m";				
			}
			filename += ""+year+"."+this.getJPLVersion();

			String filePath = FileIO.DATA_JPL_EPHEM_DIRECTORY+"de"+this.getJPLVersion()+Zip.ZIP_SEPARATOR;
			if (externalPath != null) filePath = externalPath;
			filename = filePath + filename;
			
			if (externalPath == null) {
				return ReadFile.resourceAvailable(filename);
			} else {
				return (new File(filename)).exists();
			}
		} catch (Exception exc) {
			return false;
		}
	}
	private void readHeader()
	throws JPARSECException {
		int version = this.getJPLVersion();
		String filePath = FileIO.DATA_JPL_EPHEM_DIRECTORY+"de" + version + Zip.ZIP_SEPARATOR;
		// Header file provided by JPARSEC. Since the dates and years should be typed in this class
		// it is not allowed to read an external header file, so only JPL integrations listed in this class
		// are supported.
		
		filePath += "header." + version;
		
		String file[] = null;
		// Read header from dependencies or from external directory if dependencies are not available
		try {
			file = DataSet.arrayListToStringArray(ReadFile.readResource(filePath));
		} catch (Exception exc) {
			if (externalPath != null) {
				filePath = externalPath + "header." + version;
				file = DataSet.arrayListToStringArray(ReadFile.readAnyExternalFile(filePath));
			}
		}
		
		for (int i=0; i<file.length; i++)
		{
			if (file[i].startsWith("KSIZE=")) {
				//this.ksize = Integer.parseInt(FileIO.getField(2, file[i], " "));
				this.ncoeff = Integer.parseInt(FileIO.getField(4, file[i], " ", true));
			}
			if (file[i].startsWith("GROUP   1030")) {
				String data = file[i+2];
				//this.jdi = Double.parseDouble(FileIO.getField(1, data, " "));
				//this.jdf = Double.parseDouble(FileIO.getField(2, data, " "));
				this.jds = Double.parseDouble(FileIO.getField(3, data, " ", true));
				i = i + 4;
			}
			if (file[i].startsWith("GROUP   1040")) {
				String data = file[i+2].trim();
				int n = Integer.parseInt(data);
				i = i + 3;
				int columns = FileIO.getNumberOfFields(file[i], " ", true);
				double frac = (double) n / (double) columns;
				int rows = (int) frac;
				if (rows != frac) rows ++;
				int index = -1;
				cnam = new String[n];
				for (int j=0; j<rows; j++)
				{
					data = file[i+j];
					for (int k=0; k<columns; k++)
					{
						index ++;
						if (index < n) cnam[index] = FileIO.getField(k+1, data, " ", true);
					}
				}
				i = i + rows + 1;
			}
			if (file[i].startsWith("GROUP   1041")) {
				String data = file[i+2].trim();
				int n = Integer.parseInt(data);
				i = i + 3;
				int columns = FileIO.getNumberOfFields(file[i], " ", true);
				double frac = (double) n / (double) columns;
				int rows = (int) frac;
				if (rows != frac) rows ++;
				int index = -1;
				cval = new double[n];
				for (int j=0; j<rows; j++)
				{
					data = file[i+j];
					for (int k=0; k<columns; k++)
					{
						index ++;
						if (index < n) cval[index] = Double.parseDouble(
								DataSet.replaceAll(FileIO.getField(k+1, data, " ", true), "D", "E", false));
					}
				}
				i = i + rows + 1;
			}
			if (file[i].startsWith("GROUP   1050")) {
				i = i + 2;
				String data = file[i];
				int columns = FileIO.getNumberOfFields(file[i], " ", true);
				int n = columns*3;
				double frac = (double) n / (double) columns;
				int rows = (int) frac;
				if (rows != frac) rows ++;
				lpt = new int[3][columns];
				for (int j=0; j<rows; j++)
				{
					data = file[i+j];
					int index = -1;
					for (int k=0; k<columns; k++)
					{
						index ++;
						if (index < n) lpt[j][index] = Integer.parseInt(FileIO.getField(k+1, data, " ", true));
					}
				}
				i = i + rows + 1;
			}			
		}
		
		int index = DataSet.getIndex(this.cnam, "EMRAT");
		this.emrat = this.cval[index];
		index = DataSet.getIndex(this.cnam, "AU");
		this.au = this.cval[index];
		interval_duration = this.jds;
		numbers_per_interval = this.ncoeff-2;
	}

	private static double[] ephemerisDates = new double[3];
	private static double[] ephemerisCoefficients;
	private static int jplVersion = -1;
	
	/** The Earth-Moon mass ratio. */
	public double emrat;
	/** The value assumed for the Astronomical Unit. */
	public double au;
	private double interval_duration;
	private int numbers_per_interval;
	
	private static TARGET targets[] = new TARGET[] {TARGET.NOT_A_PLANET, TARGET.MERCURY, TARGET.VENUS,
		TARGET.Earth_Moon_Barycenter, TARGET.MARS, TARGET.JUPITER, TARGET.SATURN, TARGET.URANUS,
		TARGET.NEPTUNE, TARGET.Pluto, TARGET.Moon, TARGET.SUN, TARGET.Nutation, TARGET.Libration, TARGET.Solar_System_Barycenter};
	
	/**
	 * Calculate ephemeris, providing full data. This method uses JPL
	 * ephemeris. The position of the Moon will contains the librations
	 * computed using JPL ephemerides in case the integration is
	 * one of the supported in {@linkplain LunarEvent#getJPLMoonLibrations(TimeElement, ObserverElement, EphemerisElement, LocationElement)},
	 * otherwise Eckhardt's theory will be used. In case of lunar ephemerides you may also want
	 * to correct it from center of mass to geometric center by means of
	 * {@linkplain Elp2000#fromMoonBarycenterToGeometricCenter(TimeElement, ObserverElement, EphemerisElement, EphemElement)}.
	 * 
	 * @param time Time object containing the date.
	 * @param obs Observer object containing the observer position.
	 * @param eph Ephemeris object with the target and ephemeris
	 *        properties.
	 * @return Ephem object containing ephemeris data. Rise, set, transit
	 * times and maximum elevation fields are not computed in this method, use
	 * {@linkplain Ephem} class for that.
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	public EphemElement getJPLEphemeris(TimeElement time, // Time Element
			ObserverElement obs, // Observer Element
			EphemerisElement eph) // Ephemeris Element
			throws JPARSECException
	{
		// Obtain julian day in Barycentric Dynamical Time
		double JD_TDB = TimeScale.getJD(time, obs, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
		EphemElement ephem_elem = this.JPLEphem(time, obs, eph, JD_TDB, true);

		/* Physical ephemeris */
		EphemerisElement new_eph = eph.clone();
		new_eph.ephemType = COORDINATES_TYPE.APPARENT;
		new_eph.equinox = EphemerisElement.EQUINOX_OF_DATE;
		EphemElement ephem_elem2 = ephem_elem;
		if (eph.ephemType != EphemerisElement.COORDINATES_TYPE.APPARENT || eph.equinox != EphemerisElement.EQUINOX_OF_DATE) 
			ephem_elem2 = this.JPLEphem(time, obs, new_eph, JD_TDB, true);
		new_eph.targetBody = TARGET.SUN;
		EphemElement ephemSun = this.JPLEphem(time, obs, new_eph, JD_TDB, false);
		
		ephem_elem2 = PhysicalParameters.physicalParameters(JD_TDB, ephemSun, ephem_elem2, obs, eph);
		PhysicalParameters.setPhysicalParameters(ephem_elem, ephem_elem2, time, obs, eph);
		
		/* Horizontal coordinates */
		if (eph.isTopocentric)
			ephem_elem = Ephem.horizontalCoordinates(time, obs, eph, ephem_elem);

		/* Set coordinates to the output equinox */
		if (EphemerisElement.EQUINOX_OF_DATE != eph.equinox)
			ephem_elem = Ephem.toOutputEquinox(ephem_elem, eph, JD_TDB);

		ephem_elem.name = eph.targetBody.getName();

		/* Obtain accurate lunar orientation, if possible */
		if (obs.getMotherBody() == TARGET.EARTH && eph.targetBody == TARGET.Moon && 
				(eph.algorithm == ALGORITHM.JPL_DE403 ||
				eph.algorithm == ALGORITHM.JPL_DE405 || eph.algorithm == ALGORITHM.JPL_DE413 ||
				eph.algorithm == ALGORITHM.JPL_DE414 || eph.algorithm == ALGORITHM.JPL_DE422 ||
				eph.algorithm == ALGORITHM.JPL_DE424 || eph.algorithm == ALGORITHM.JPL_DE430)
						) {
			double lib[] = LunarEvent.getJPLMoonLibrations(time, obs, eph, ephem_elem.getEquatorialLocation());
			ephem_elem.longitudeOfCentralMeridian = lib[0];
			ephem_elem.positionAngleOfPole = lib[1];
			ephem_elem.positionAngleOfAxis = lib[2];
		}

		return ephem_elem;
	}

	private EphemElement JPLEphem(TimeElement time, // Time Element
			ObserverElement obs, // Observer Element
			EphemerisElement eph, // Ephemeris Element
			double JD_TDB, boolean addGCRS)
	throws JPARSECException {
		if ((!eph.targetBody.isPlanet() && eph.targetBody != TARGET.Moon && 
				eph.targetBody != TARGET.SUN && eph.targetBody != TARGET.Pluto) || 
				((eph.targetBody == TARGET.EARTH || eph.targetBody == TARGET.Earth_Moon_Barycenter) && obs.getMotherBody() == TARGET.EARTH))
			throw new JPARSECException("target object is invalid.");

		// Check Ephemeris object
		if (!EphemerisElement.checkEphemeris(eph) || eph.algorithm.name().indexOf("JPL") < 0)
			throw new JPARSECException("invalid ephemeris object. "+eph.algorithm);

		// Obtain geocentric position
		double geo_eq[] = this.getGeocentricPosition(JD_TDB, eph.targetBody, 0.0, true, obs);
		if (Functions.equalVectors(geo_eq, JPLEphemeris.INVALID_VECTOR))
			throw new JPARSECException(
					"error during calculations. Resulting vector invalid.");

		// Obtain topocentric light_time
		LocationElement loc = LocationElement.parseRectangularCoordinates(geo_eq);
		double light_time = loc.getRadius() * Constant.LIGHT_TIME_DAYS_PER_AU;

		if (eph.ephemType == EphemerisElement.COORDINATES_TYPE.GEOMETRIC)
			light_time = 0.0;
		if (eph.ephemType != EphemerisElement.COORDINATES_TYPE.GEOMETRIC) // && eph.targetBody != TARGET.SUN)
		{
			double topo[] = obs.topocentricObserverICRF(time, eph);
			geo_eq = this.getGeocentricPosition(JD_TDB, eph.targetBody, light_time, true, obs);
			double light_time_corrected = Ephem.getTopocentricLightTime(geo_eq, topo, eph);
			// Iterate to obtain correct light time and geocentric position.
			// Typical difference in light time is 0.1 seconds. Iterate to
			// a precision up to 1E-6 seconds.

			do
			{
				light_time = light_time_corrected;
				geo_eq = this.getGeocentricPosition(JD_TDB, eph.targetBody, light_time_corrected, true, obs);
				light_time_corrected = Ephem.getTopocentricLightTime(geo_eq, topo, eph);
				// (A relativistic effect of about 1E-10 AU is neglected in the light time calculation)
			} while (Math.abs(light_time - light_time_corrected) > (1.0E-6 / Constant.SECONDS_PER_DAY));
			light_time = light_time_corrected;
		}

		// Obtain light time to Sun (first order approx)
		double geo_sun_0[] = this.getGeocentricPosition(JD_TDB, TARGET.SUN, 0.0, false, obs);
		double lightTimeS = 0.0;
		if (eph.ephemType != EphemerisElement.COORDINATES_TYPE.GEOMETRIC) {
			lightTimeS = light_time;
			if (eph.targetBody != TARGET.SUN) {
				LocationElement locS = LocationElement.parseRectangularCoordinates(geo_sun_0);
				lightTimeS = locS.getRadius() * Constant.LIGHT_TIME_DAYS_PER_AU;
			}
		}
		double baryc[] = this.getGeocentricPosition(JD_TDB, TARGET.Solar_System_Barycenter, 0.0, false, obs);

		// Obtain heliocentric equatorial coordinates, J2000. Note JPL ephems are respect to
		// Solar system barycenter, we will need to subtract the position of the Sun here
		double helio_object[] = this.getPositionAndVelocity(JD_TDB - light_time, eph.targetBody);
		Object o = DataBase.getData("offsetPosition", true);
		if (o != null) {
			double[] planetocentricPositionOfTargetSatellite = (double[]) o;
			helio_object = Functions.sumVectors(helio_object, planetocentricPositionOfTargetSatellite);
		}
		
		if (eph.targetBody != TARGET.Moon)
		{
			LocationElement locP = LocationElement.parseRectangularCoordinates(helio_object);
			double lightTimeP = locP.getRadius() * Constant.LIGHT_TIME_DAYS_PER_AU;
			double helio_object_sun[] = this.getPositionAndVelocity(JD_TDB - light_time - lightTimeP, TARGET.SUN);
			helio_object = Functions.substract(helio_object, helio_object_sun);
		} else {
			double[] geo_sun_ltS = this.getGeocentricPosition(JD_TDB, TARGET.SUN, lightTimeS, false, obs);
			helio_object = Functions.substract(helio_object, geo_sun_ltS);
			if (eph.ephemType == EphemerisElement.COORDINATES_TYPE.ASTROMETRIC) {
				geo_eq = Ephem.aberration(new double[] {-geo_eq[0], -geo_eq[1], -geo_eq[2], 0, 0, 0}, baryc, light_time);
				geo_eq = Functions.scalarProduct(geo_eq, -1.0);
			}
		}

		// Correct for solar deflection and aberration
		if (eph.ephemType == EphemerisElement.COORDINATES_TYPE.APPARENT)
		{
			if (//eph.preferPrecisionInEphemerides && 
					(obs.getMotherBody() != TARGET.EARTH || (eph.targetBody != TARGET.SUN && eph.targetBody != TARGET.Moon))) {
				double sun[] = this.getPositionAndVelocity(JD_TDB - lightTimeS, TARGET.SUN);
				//geo_eq = Ephem.solarDeflection(geo_eq, geo_sun_0, Functions.substract(helio_object, sun));
				geo_eq = Ephem.solarAndPlanetaryDeflection(geo_eq, geo_sun_0, Functions.substract(helio_object, sun),
						new TARGET[] {TARGET.JUPITER, TARGET.SATURN, TARGET.EARTH}, JD_TDB, false, obs);
			}
			if (obs.getMotherBody() != TARGET.EARTH || eph.targetBody != TARGET.Moon)
				geo_eq = Ephem.aberration(geo_eq, baryc, light_time);

			if (addGCRS) DataBase.addData("GCRS", geo_eq, true);
		} else {
			if (addGCRS) DataBase.addData("GCRS", null, true);
		}

		/* Correction to output frame. */
		if (this.getJPLVersion() >= 403)
		{
			if (this.getJPLVersion() == 403) {
				// Rotate DE403 into ICRF following Folkner 1994 and Chernetenko 2007
				double ang1 = -0.1 * 0.001 * Constant.ARCSEC_TO_RAD;
				double ang2 = 3 * 0.001 * Constant.ARCSEC_TO_RAD;
				double ang3 = -5.2 * 0.001 * Constant.ARCSEC_TO_RAD;
				Matrix m = Matrix.getR1(ang1).times(Matrix.getR2(ang2).times(Matrix.getR3(ang3)));
				geo_eq = m.times(new Matrix(DataSet.getSubArray(geo_eq, 0, 2))).getColumn(0);
				helio_object = m.times(new Matrix(DataSet.getSubArray(helio_object, 0, 2))).getColumn(0);
			}
			
			geo_eq = Ephem.toOutputFrame(geo_eq, FRAME.ICRF, eph.frame);
			helio_object = Ephem.toOutputFrame(helio_object, FRAME.ICRF, eph.frame);
		} else {
			// FIXME: Not sure if the following rotation to FK5 is required. It seems so, but results
			// from DE200 agree better with VSOP87 and ELP2000 if this is commented. 
			geo_eq = meanEquatorialDE200ToFK5(geo_eq);
			helio_object = meanEquatorialDE200ToFK5(helio_object);
			
			geo_eq = Ephem.toOutputFrame(geo_eq, FRAME.FK5, eph.frame);
			helio_object = Ephem.toOutputFrame(helio_object, FRAME.FK5, eph.frame);
		}

		double geo_date[];
		if (eph.frame == FRAME.FK4) {
			// Transform from B1950 to mean equinox of date
			 geo_date = Precession.precess(Constant.B1950, JD_TDB, geo_eq, eph);			
			 helio_object = Precession.precess(Constant.B1950, JD_TDB, helio_object, eph);	
		} else {
			// Transform from ICRS/J2000 to mean equinox of date
			geo_date = Precession.precessFromJ2000(JD_TDB, geo_eq, eph);
			helio_object = Precession.precessFromJ2000(JD_TDB, helio_object, eph);
		}

		// Get heliocentric ecliptic position
		LocationElement loc_elem = LocationElement.parseRectangularCoordinates(Ephem.equatorialToEcliptic(helio_object, JD_TDB, eph));

		// Mean equatorial to true equatorial: corret for nutation
		double true_eq[] = geo_date;
		if (obs.getMotherBody() == TARGET.EARTH) {
			if (eph.ephemType == EphemerisElement.COORDINATES_TYPE.APPARENT)
				true_eq = Nutation.nutateInEquatorialCoordinates(JD_TDB, eph, geo_date, true);
	
			// Correct for polar motion
			if (eph.ephemType == EphemerisElement.COORDINATES_TYPE.APPARENT &&
					eph.correctForPolarMotion)
			{
				double gast = SiderealTime.greenwichApparentSiderealTime(time, obs, eph);
				true_eq = Functions.rotateZ(true_eq, -gast);
				Matrix mat = IAU2006.getPolarMotionCorrectionMatrix(time, obs, eph);
				true_eq = mat.times(new Matrix(true_eq)).getColumn(0);
				true_eq = Functions.rotateZ(true_eq, gast);
			}
		}
		
		// Pass to coordinates as seen from another body, if necessary
		if (obs.getMotherBody() != TARGET.NOT_A_PLANET)
			true_eq = Ephem.getPositionFromBody(LocationElement.parseRectangularCoordinates(true_eq), time, obs, eph).getRectangularCoordinates();

		// Get equatorial coordinates
		LocationElement ephem_loc = LocationElement.parseRectangularCoordinates(true_eq);

		// Set preliminary results
		EphemElement ephem_elem = new EphemElement();
		ephem_elem.rightAscension = ephem_loc.getLongitude();
		ephem_elem.declination = ephem_loc.getLatitude();
		ephem_elem.distance = ephem_loc.getRadius();
		ephem_elem.heliocentricEclipticLongitude = loc_elem.getLongitude();
		ephem_elem.heliocentricEclipticLatitude = loc_elem.getLatitude();
		ephem_elem.lightTime = (float) light_time;
		// Note distances are apparent, not true
		ephem_elem.distanceFromSun = loc_elem.getRadius();

		if (eph.targetBody == TARGET.SUN) ephem_elem.heliocentricEclipticLatitude = ephem_elem.heliocentricEclipticLongitude = 
			ephem_elem.distanceFromSun = 0;

		/* Topocentric correction */
		if (eph.isTopocentric)
			ephem_elem = Ephem.topocentricCorrection(time, obs, eph, ephem_elem);
		
		return ephem_elem;
	}
	
	/**
	 * Transforms J2000 mean equatorial (dynamical equinox) coordinates into FK5.
	 * Specific to this theory (class) only for DE200. Rotation is performed
	 * according to IMCCE documentation, see 
	 * http://www.imcce.fr/en/ephemerides/generateur/ephepos/ephemcc_doc.ps.gz.
	 * 
	 * @param position Equatorial coordinates (x, y, z) or (x, y, z, vx, vy, vz)
	 *        from DE200.
	 * @return Equatorial FK5 coordinates.
	 */
	private static double[] meanEquatorialDE200ToFK5(double position[])
	{
		double RotM[][] = new double[4][4];
		double out_pos[] = new double[3];
		double out_vel[] = new double[3];

		// http://www.imcce.fr/en/ephemerides/generateur/ephepos/ephemcc_doc.ps.gz
		RotM[1][1] = 1.000000000000;
		RotM[1][2] = -0.000000028604007;
		RotM[1][3] = 0.000000000000005;
		RotM[2][1] = 0.000000028604007;
		RotM[2][2] = 0.999999999999984;
		RotM[2][3] = -0.000000175017739;
		RotM[3][1] = 0.000000000000;
		RotM[3][2] = 0.000000175017739;
		RotM[3][3] = 0.999999999999985;

		// Apply rotation
		out_pos[0] = RotM[1][1] * position[0] + RotM[1][2] * position[1] + RotM[1][3] * position[2]; // x
		out_pos[1] = RotM[2][1] * position[0] + RotM[2][2] * position[1] + RotM[2][3] * position[2]; // y
		out_pos[2] = RotM[3][1] * position[0] + RotM[3][2] * position[1] + RotM[3][3] * position[2]; // z
		if (position.length > 3)
		{
			out_vel[0] = RotM[1][1] * position[3] + RotM[1][2] * position[4] + RotM[1][3] * position[5]; // vx
			out_vel[1] = RotM[2][1] * position[3] + RotM[2][2] * position[4] + RotM[2][3] * position[5]; // vy
			out_vel[2] = RotM[3][1] * position[3] + RotM[3][2] * position[4] + RotM[3][3] * position[5]; // vz

			return new double[]
			{ out_pos[0], out_pos[1], out_pos[2], out_vel[0], out_vel[1], out_vel[2] };
		}

		return out_pos;
	}

	/**
	 * Get rectangular equatorial geocentric position of a planet in epoch
	 * J2000.
	 * 
	 * @param JD Julian day in TDB.
	 * @param planet Target ID.
	 * @param light_time Light time in days.
	 * @param addSat True to add the planetocentric position of the satellite to the position
	 * of the planet.
	 * @param obs The observer object. Can be null for the Earth's center.
	 * @return Array with x, y, z, (geocentric position) vx, vy, vz (earth barycentric
	 * velocity) coordinates. Note velocity components are those
	 * for the Earth (used for aberration correction) not those for the planet relative to the 
	 * geocenter.
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	public double[] getGeocentricPosition(double JD, TARGET planet, double light_time, boolean addSat, ObserverElement obs) throws JPARSECException
	{
		// Heliocentric position corrected for light time
		double helio_object[] = this.getPositionAndVelocity(JD - light_time, planet);
		if (Functions.equalVectors(helio_object, JPLEphemeris.INVALID_VECTOR) && planet != TARGET.SUN 
				&& planet != TARGET.Solar_System_Barycenter)
			return JPLEphemeris.INVALID_VECTOR;
		
		if (addSat) {
			Object o = DataBase.getData("offsetPosition", true);
			if (o != null) {
				double[] planetocentricPositionOfTargetSatellite = (double[]) o;
				helio_object = Functions.sumVectors(helio_object, planetocentricPositionOfTargetSatellite);
			}
		}

		if (planet == TARGET.Moon && (obs == null || obs.getMotherBody() == TARGET.EARTH)) return helio_object;

		// Compute apparent position of barycenter from Earth
		double earth[] = null;
		if (obs == null || obs.getMotherBody() == TARGET.EARTH || planet == TARGET.Moon) {
			double helio_barycenter[] = this.getPositionAndVelocity(JD, TARGET.Earth_Moon_Barycenter);
			if (Functions.equalVectors(helio_barycenter, JPLEphemeris.INVALID_VECTOR))
				return JPLEphemeris.INVALID_VECTOR;
			double moon[] = this.getPositionAndVelocity(JD, TARGET.Moon);
			earth = Functions.substract(helio_barycenter, Functions.scalarProduct(moon, 1.0 / (1.0 + emrat)));
		}

		/*
		// The call to the Moon can be replaced by the Series96 calculation of geocentric barycenter, which gives
		// also good results.
		double dt = 0.001;
		double[] geoEMB = Series96.getBarycenter(JD);
		double[] geoEMB2 = Series96.getBarycenter(JD+dt);
		geoEMB = new double[] {geoEMB[0], geoEMB[1], geoEMB[2], (geoEMB2[0]-geoEMB[0])/dt, (geoEMB2[1]-geoEMB[1])/dt, (geoEMB2[2]-geoEMB[2])/dt};
		earth = Functions.substract(helio_barycenter, geoEMB);
		 */

		if (obs != null && obs.getMotherBody() != TARGET.EARTH) {
			if (planet == TARGET.Moon) helio_object = Functions.sumVectors(earth, helio_object);
			EphemerisElement eph = new EphemerisElement();
			eph.ephemMethod = REDUCTION_METHOD.IAU_2006;
			eph.algorithm = this.jplID;
			earth = obs.heliocentricPositionOfObserver(JD, eph);			
		}
		
		double geo_pos[] = Functions.substract(helio_object, earth);

		geo_pos[3] = earth[3];
		geo_pos[4] = earth[4];
		geo_pos[5] = earth[5];
		return geo_pos;
	}

	/**
	 * Get rectangular equatorial geocentric position of a planet in epoch
	 * J2000, using extended precision. Not actively used in JPARSEC.
	 * 
	 * @param JD Julian day in TDB.
	 * @param planet Target ID.
	 * @param light_time Light time in days.
	 * @param addSat True to add the planetocentric position of the satellite to the position
	 * of the planet.
	 * @param obs The observer object. Can be null for the Earth's center.
	 * @return Array with x, y, z, (geocentric position) vx, vy, vz (earth barycentric
	 * velocity) coordinates. Note velocity components are those
	 * for the Earth (used for aberration correction) not those for the planet relative to the 
	 * geocenter.
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	public double[] getGeocentricPosition(BigDecimal JD, TARGET planet, double light_time, boolean addSat, ObserverElement obs) throws JPARSECException
	{
		// Heliocentric position corrected for light time
		double helio_object[] = this.getPositionAndVelocity(JD.subtract(new BigDecimal(light_time)), planet);
		if (Functions.equalVectors(helio_object, JPLEphemeris.INVALID_VECTOR) && planet != TARGET.SUN 
				&& planet != TARGET.Solar_System_Barycenter)
			return JPLEphemeris.INVALID_VECTOR;
		
		if (addSat) {
			Object o = DataBase.getData("offsetPosition", true);
			if (o != null) {
				double[] planetocentricPositionOfTargetSatellite = (double[]) o;
				helio_object = Functions.sumVectors(helio_object, planetocentricPositionOfTargetSatellite);
			}
		}

		if (planet == TARGET.Moon) return helio_object;

		// Compute apparent position of barycenter from Earth
		double earth[] = null;
		if (obs == null || obs.getMotherBody() == TARGET.EARTH || planet == TARGET.Moon) {
			double helio_barycenter[] = this.getPositionAndVelocity(JD, TARGET.Earth_Moon_Barycenter);
			if (Functions.equalVectors(helio_barycenter, JPLEphemeris.INVALID_VECTOR))
				return JPLEphemeris.INVALID_VECTOR;
			double moon[] = this.getPositionAndVelocity(JD, TARGET.Moon);
			earth = Functions.substract(helio_barycenter, Functions.scalarProduct(moon, 1.0 / (1.0 + emrat)));
		}

		/*
		// The call to the Moon can be replaced by the Series96 calculation of geocentric barycenter, which gives
		// also good results.
		double dt = 0.001;
		double[] geoEMB = Series96.getBarycenter(JD);
		double[] geoEMB2 = Series96.getBarycenter(JD+dt);
		geoEMB = new double[] {geoEMB[0], geoEMB[1], geoEMB[2], (geoEMB2[0]-geoEMB[0])/dt, (geoEMB2[1]-geoEMB[1])/dt, (geoEMB2[2]-geoEMB[2])/dt};
		earth = Functions.substract(helio_barycenter, geoEMB);
		 */

		if (obs != null && obs.getMotherBody() != TARGET.EARTH) {
			if (planet == TARGET.Moon) helio_object = Functions.sumVectors(earth, helio_object);
			EphemerisElement eph = new EphemerisElement();
			eph.ephemMethod = REDUCTION_METHOD.IAU_2006;
			eph.algorithm = this.jplID;
			earth = obs.heliocentricPositionOfObserver(JD.doubleValue(), eph);			
		}

		double geo_pos[] = Functions.substract(helio_object, earth);

		geo_pos[3] = earth[3];
		geo_pos[4] = earth[4];
		geo_pos[5] = earth[5];
		return geo_pos;
	}

	/**
	 * Invalid vector representing an invalid JPL ephemeris result (out of time span)
	 * for {@linkplain #getGeocentricPosition(double, TARGET, double, boolean, ObserverElement)}.
	 */
	public static final double[] INVALID_VECTOR = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

	private double[] getEarthPositionAndVelocity(double JD) throws JPARSECException {
		double helio_barycenter[] = this.getPositionAndVelocity(JD, TARGET.Earth_Moon_Barycenter);
		if (Functions.equalVectors(helio_barycenter, JPLEphemeris.INVALID_VECTOR))
			return JPLEphemeris.INVALID_VECTOR;
		double moon[] = this.getPositionAndVelocity(JD, TARGET.Moon);
		return Functions.substract(helio_barycenter, Functions.scalarProduct(moon, 1.0 / (1.0 + emrat)));
	}
	
	/**
	 * Obtains position and velocity of certain object using the selected
	 * JPL ephemeris version.
	 * @param jd Julian day, TDB.
	 * @param target Target. Can be a planet, Pluto, or the Sun, Moon, Earth-Moon
	 * barycenter, or can be also nutation and libration.
	 * @return A vector with equatorial position and velocity from Solar System
	 * Barycenter, refered to ICRS (or dynamical equinox and equator for DE200) and J2000 equinox.
	 * For the Moon the geocentric position is returned. 
	 * @throws JPARSECException If an error occurs.
	 */
	public double[] getPositionAndVelocity(double jd, TARGET target)
	throws JPARSECException {
		if (target == TARGET.Solar_System_Barycenter) return new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		if (target == TARGET.EARTH) return this.getEarthPositionAndVelocity(jd);
		int object = DataSet.getIndex(targets, target);
		if (object <= 0) throw new JPARSECException("invalid target "+target+".");

		int interval = 0, numbers_to_skip = 0, pointer = 0, j = 0, k = 0, subinterval = 0;

		double interval_start_time = 0, subinterval_duration = 0, chebyshev_time = 0;

		double[] position_poly = new double[20];
		double[][] coef = new double[4][20];
		double[] velocity_poly = new double[20];

		int[] number_of_coef_sets = new int[14];
		int[] number_of_coefs = new int[14];

		/* Initialize arrays */
		for (j=1; j<=13; j++)
		{
			number_of_coefs[j] = this.lpt[1][j-1];
			number_of_coef_sets[j] = this.lpt[2][j-1];
		}

		/*
		 * Begin by determining whether the current ephemeris coefficients are
		 * appropriate for jultime, or if we need to load a new set.
		 */
		if (jplVersion == -1 || jplVersion != this.getJPLVersion() || (jd < ephemerisDates[1]) || (jd > ephemerisDates[2]))
			getEphemerisCoefficients(jd);

		interval = (int) (Math.floor((jd - ephemerisDates[1]) / interval_duration) + 1);
		interval_start_time = (interval - 1.0) * interval_duration + ephemerisDates[1];
		subinterval_duration = interval_duration / number_of_coef_sets[object];
		subinterval = (int) (Math.floor((jd - interval_start_time) / subinterval_duration) + 1);
		numbers_to_skip = (interval - 1) * numbers_per_interval;

		/*
		 * Starting at the beginning of the coefficient array, skip the first
		 * "numbers_to_skip" coefficients. This puts the pointer on the first
		 * piece of data in the correct interval.
		 */
		pointer = numbers_to_skip + 1;

		/* Skip the coefficients for the first (i-1) planets */
		for (j = 1; j <= (object - 1); j++) {
			int n = 3;
			if (j == 12) n = 2;
			pointer = pointer + n * number_of_coef_sets[j] * number_of_coefs[j];
		}

		/* Skip the next (subinterval - 1)*3*number_of_coefs(i) coefficients */
		int n = 3;
		if (object == 12) n = 2;
		pointer = pointer + (subinterval - 1) * n * number_of_coefs[object];

		for (j = 1; j <= n; j++)
		{
			for (k = 1; k <= number_of_coefs[object]; k++)
			{
				/* Read the pointer'th coefficient as the array entry coef[j][k] */
				coef[j][k] = ephemerisCoefficients[pointer];
				pointer = pointer + 1;
			}
		}

		/*
		 * Calculate the chebyshev time within the subinterval, between -1 and +1.
		 * jd is a double value. I have tested that with BigDecimal the difference
		 * is around 20 microarcseconds for the Moon (0.04 m), and also below 1 m
		 * for Mars and other planets. 
		 */
		chebyshev_time = 2.0 * (jd - ((subinterval - 1.0) * subinterval_duration + interval_start_time)) / subinterval_duration - 1.0;

		/* Calculate the Chebyshev position polynomials */
		position_poly[1] = 1.0;
		position_poly[2] = chebyshev_time;
		for (j = 3; j <= number_of_coefs[object]; j++)
			position_poly[j] = 2.0 * chebyshev_time * position_poly[j - 1] - position_poly[j - 2];

		/* Calculate the position of the i'th planet at jultime */
		double[] ephemeris_r = new double[7];
		for (j = 1; j <= n; j++)
		{
			ephemeris_r[j] = 0;
			for (k = 1; k <= number_of_coefs[object]; k++)
				ephemeris_r[j] = ephemeris_r[j] + coef[j][k] * position_poly[k];

			/* Convert from km to A.U. */
			if (target != TARGET.Libration && target != TARGET.Nutation) ephemeris_r[j] = ephemeris_r[j] / au;
		}

		/* Calculate the Chebyshev velocity polynomials */
		velocity_poly[1] = 0.0;
		velocity_poly[2] = 1.0;
		velocity_poly[3] = 4.0 * chebyshev_time;
		for (j = 4; j <= number_of_coefs[object]; j++)
			velocity_poly[j] = 2.0 * chebyshev_time * velocity_poly[j - 1] + 2.0 * position_poly[j - 1] - velocity_poly[j - 2];

		/* Calculate the velocity of the i'th planet */
		for (j = n+1; j <= 2*n; j++)
		{
			ephemeris_r[j] = 0;
			for (k = 1; k <= number_of_coefs[object]; k++)
				ephemeris_r[j] = ephemeris_r[j] + coef[j-n][k] * velocity_poly[k];
			/*
			 * The next line accounts for differentiation of the iterative
			 * formula with respect to chebyshev time. Essentially, if dx/dt =
			 * (dx/dct) times (dct/dt), the next line includes the factor
			 * (dct/dt) so that the units are km/day
			 */
			ephemeris_r[j] = ephemeris_r[j] * (2.0 * number_of_coef_sets[object] / interval_duration);

			/* Convert from km to A.U. */
			if (target != TARGET.Libration && target != TARGET.Nutation) ephemeris_r[j] = ephemeris_r[j] / au;
		}

		double array[] = new double[] {
			ephemeris_r[1], ephemeris_r[2], ephemeris_r[3], 
			ephemeris_r[4], ephemeris_r[5], ephemeris_r[6]
		}; 
		
		if (target == TARGET.Nutation) array = DataSet.getSubArray(array, 0, 3); 

		// Return position of Pluto's body center
		if (target == TARGET.Pluto) {
			double newPos[] = MoonEphem.fromPlutoBarycenterToPlutoCenter(array.clone(), jd, EphemerisElement.REDUCTION_METHOD.IAU_2009, true);
			array[0] = newPos[0];
			array[1] = newPos[1];
			array[2] = newPos[2];
		}
		
		return array;
	}

	/**
	 * Obtains position and velocity of certain object using the selected
	 * JPL ephemeris version, and an input date with arbitrary precision. The use
	 * of high precision double values is generally innecesary and not actively used
	 * in JPARSEC.
	 * @param bigjd Julian day, TDB.
	 * @param target Target. Can be a planet, Pluto, or the Sun, Moon, Earth-Moon
	 * barycenter, or can be also nutation and libration.
	 * @return A vector with equatorial position and velocity from Solar System
	 * Barycenter, refered to ICRS (or dynamical equinox and equator for DE200) and J2000 equinox. 
	 * @throws JPARSECException If an error occurs.
	 */
	public double[] getPositionAndVelocity(BigDecimal bigjd, TARGET target)
	throws JPARSECException {
		double jd = bigjd.doubleValue();
		
		if (target == TARGET.Solar_System_Barycenter) return new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		int object = DataSet.getIndex(targets, target);
		if (object <= 0) throw new JPARSECException("invalid target.");

		int interval = 0, numbers_to_skip = 0, pointer = 0, j = 0, k = 0, subinterval = 0;

		double interval_start_time = 0, subinterval_duration = 0, chebyshev_time = 0;

		double[] position_poly = new double[20];
		double[][] coef = new double[4][20];
		double[] velocity_poly = new double[20];

		int[] number_of_coef_sets = new int[14];
		int[] number_of_coefs = new int[14];

		/* Initialize arrays */
		for (j=1; j<=13; j++)
		{
			number_of_coefs[j] = this.lpt[1][j-1];
			number_of_coef_sets[j] = this.lpt[2][j-1];
		}

		/*
		 * Begin by determining whether the current ephemeris coefficients are
		 * appropriate for jultime, or if we need to load a new set.
		 */
		if (jplVersion == -1 || jplVersion != this.getJPLVersion() || (jd < ephemerisDates[1]) || (jd > ephemerisDates[2]))
			getEphemerisCoefficients(jd);

		interval = (int) (Math.floor((jd - ephemerisDates[1]) / interval_duration) + 1);
		interval_start_time = (interval - 1.0) * interval_duration + ephemerisDates[1];
		subinterval_duration = interval_duration / number_of_coef_sets[object];
		subinterval = (int) (Math.floor((jd - interval_start_time) / subinterval_duration) + 1);
		numbers_to_skip = (interval - 1) * numbers_per_interval;

		/*
		 * Starting at the beginning of the coefficient array, skip the first
		 * "numbers_to_skip" coefficients. This puts the pointer on the first
		 * piece of data in the correct interval.
		 */
		pointer = numbers_to_skip + 1;

		/* Skip the coefficients for the first (i-1) planets */
		for (j = 1; j <= (object - 1); j++) {
			int n = 3;
			if (j == 12) n = 2;
			pointer = pointer + n * number_of_coef_sets[j] * number_of_coefs[j];
		}

		/* Skip the next (subinterval - 1)*3*number_of_coefs(i) coefficients */
		int n = 3;
		if (object == 12) n = 2;
		pointer = pointer + (subinterval - 1) * n * number_of_coefs[object];

		for (j = 1; j <= n; j++)
		{
			for (k = 1; k <= number_of_coefs[object]; k++)
			{
				/* Read the pointer'th coefficient as the array entry coef[j][k] */
				coef[j][k] = ephemerisCoefficients[pointer];
				pointer = pointer + 1;
			}
		}

		/*
		 * Calculate the chebyshev time within the subinterval, between -1 and +1.
		 * I have tested that with BigDecimal the difference
		 * is around 20 microarcseconds for the Moon (0.04 m), and also below 1 m
		 * for Mars and other planets. 
		 */
		//chebyshev_time = 2.0 * (jd - ((subinterval - 1.0) * subinterval_duration + interval_start_time)) / subinterval_duration - 1.0;
		BigDecimal big_chebyshev_time = (bigjd.subtract(new BigDecimal((subinterval - 1.0) * subinterval_duration + interval_start_time)));
		big_chebyshev_time = big_chebyshev_time.multiply(new BigDecimal(2.0 / subinterval_duration));
		big_chebyshev_time = big_chebyshev_time.subtract(new BigDecimal(1.0));
		chebyshev_time = big_chebyshev_time.doubleValue();
		
		
		/* Calculate the Chebyshev position polynomials */
		position_poly[1] = 1.0;
		position_poly[2] = chebyshev_time;
		for (j = 3; j <= number_of_coefs[object]; j++)
			position_poly[j] = 2.0 * chebyshev_time * position_poly[j - 1] - position_poly[j - 2];

		/* Calculate the position of the i'th planet at jultime */
		double[] ephemeris_r = new double[7];
		for (j = 1; j <= n; j++)
		{
			ephemeris_r[j] = 0;
			for (k = 1; k <= number_of_coefs[object]; k++)
				ephemeris_r[j] = ephemeris_r[j] + coef[j][k] * position_poly[k];

			/* Convert from km to A.U. */
			if (target != TARGET.Libration && target != TARGET.Nutation) ephemeris_r[j] = ephemeris_r[j] / au;
		}

		/* Calculate the Chebyshev velocity polynomials */
		velocity_poly[1] = 0.0;
		velocity_poly[2] = 1.0;
		velocity_poly[3] = 4.0 * chebyshev_time;
		for (j = 4; j <= number_of_coefs[object]; j++)
			velocity_poly[j] = 2.0 * chebyshev_time * velocity_poly[j - 1] + 2.0 * position_poly[j - 1] - velocity_poly[j - 2];

		/* Calculate the velocity of the i'th planet */
		for (j = n+1; j <= 2*n; j++)
		{
			ephemeris_r[j] = 0;
			for (k = 1; k <= number_of_coefs[object]; k++)
				ephemeris_r[j] = ephemeris_r[j] + coef[j-n][k] * velocity_poly[k];
			/*
			 * The next line accounts for differentiation of the iterative
			 * formula with respect to chebyshev time. Essentially, if dx/dt =
			 * (dx/dct) times (dct/dt), the next line includes the factor
			 * (dct/dt) so that the units are km/day
			 */
			ephemeris_r[j] = ephemeris_r[j] * (2.0 * number_of_coef_sets[object] / interval_duration);

			/* Convert from km to A.U. */
			if (target != TARGET.Libration && target != TARGET.Nutation) ephemeris_r[j] = ephemeris_r[j] / au;
		}

		double array[] = new double[] {
			ephemeris_r[1], ephemeris_r[2], ephemeris_r[3], 
			ephemeris_r[4], ephemeris_r[5], ephemeris_r[6]
		}; 
		
		if (target == TARGET.Nutation) array = DataSet.getSubArray(array, 0, 3); 
		
		return array;
	}

	/**
	 * Procedure to read the DExxx ephemeris file corresponding to jultime. The
	 * start and end dates of the ephemeris file are returned, as are the
	 * Chebyshev coefficients for Mercury, Venus, Earth-Moon, Mars, Jupiter,
	 * Saturn, Uranus, Neptune, Pluto, Geocentric Moon, and Sun.
	 */
	private void getEphemerisCoefficients(double jultime)
	throws JPARSECException {

		int i = 0, j = 0;
		String filename = " ", line = " ";

		try
		{
			int index = -1;
			for (i=0; i<this.dates.length-1; i++)
			{
				if (jultime >= this.dates[i] && jultime < this.dates[i+1]) {
					index = i;
					break;
				}
			}
			if (index < 0) throw new JPARSECException("cannot calculate ephemeris for this date.");

			int year = this.years[index];
			ephemerisDates[1] = dates[index];
			ephemerisDates[2] = dates[index+1];
			filename = "asc";
			if (year >= 0) {
				filename +="p";
			} else {
				filename +="m";				
			}
			filename += ""+Math.abs(year)+"."+this.getJPLVersion();
			String JPLfilename = filename;
			
			String filePath = FileIO.DATA_JPL_EPHEM_DIRECTORY+"de"+this.getJPLVersion()+Zip.ZIP_SEPARATOR;
			if (externalPath != null) filePath = externalPath;
			filename = filePath + filename;

			int seriesApprox = (int) (2.0 + 367.0 * (double) this.yearsPerFile / this.jds);
			ephemerisCoefficients = new double[numbers_per_interval*seriesApprox+1];
			jplVersion = this.getJPLVersion();
						
			InputStream is = null;
			if (externalPath != null && new File(filename).exists()) {
				URLConnection Connection = (URLConnection) ((new File(filename)).toURI().toURL()).openConnection();
				is = Connection.getInputStream();
			} else {
				filePath = FileIO.DATA_JPL_EPHEM_DIRECTORY+"de"+this.getJPLVersion()+Zip.ZIP_SEPARATOR;
				filename = filePath + JPLfilename;
				is = getClass().getClassLoader().getResourceAsStream(filename);
			}
			BufferedReader buff = new BufferedReader(new InputStreamReader(is));

			/* Read each record in the file */
			int imax = 1 + (this.ncoeff + 2) / 3;
			int rest = (this.ncoeff + 2) % 3;
			j = 0;
			while ((line = buff.readLine()) != null)
			{
				j ++;
				for (i = 2; i <= imax; i++)
				{
					line = DataSet.replaceAll(buff.readLine(), "D", "E", true);

					if (i > 2) {
						ephemerisCoefficients[(j - 1) * numbers_per_interval + (3 * (i - 2) - 1)] = 
							Double.parseDouble(FileIO.getField(1, line, " ", true));
						if (i < imax || (rest == 0 || rest == 2)) ephemerisCoefficients[(j - 1) * numbers_per_interval + (3 * (i - 2))] = 
								Double.parseDouble(FileIO.getField(2, line, " ", true));
					}
					if (i < imax || rest == 0) ephemerisCoefficients[(j - 1) * numbers_per_interval + (3 * (i - 2) + 1)] = 
							Double.parseDouble(FileIO.getField(3, line, " ", true));
				}
			}
			buff.close();
		} catch (Exception e)
		{
			throw new JPARSECException("a problem was found when trying to read from the file "+filename+".", e);
		} 
	}

	/**
	 * Value of the Moon secular acceleration ("/cy^2) for DE403 and DE404.
	 * The value is -25.8 as it appears in the IOM of JPL for DE403, but
	 * from the information of the IOM of JPL DE421 it seems that this 
	 * value could be -26.06. See page 7 of 
	 * http://naif.jpl.nasa.gov/pub/naif/generic_kernels/spk/planets/de421_lunar_ephemeris_and_orientation.pdf.
	 */
	public static final double MOON_SECULAR_ACCELERATION_DE403 = -25.8;
	/**
	 * Value of the Moon secular acceleration ("/cy^2) for DE405 and DE406.
	 */
	public static final double MOON_SECULAR_ACCELERATION_DE405 = -25.7376;
	/**
	 * Value of the Moon secular acceleration ("/cy^2) for DE200.
	 */
	public static final double MOON_SECULAR_ACCELERATION_DE200 = -23.8946;
	/**
	 * Value of the Moon secular acceleration ("/cy^2) for DE413 and DE414.
	 * It requires confirmation.
	 */
	public static final double MOON_SECULAR_ACCELERATION_DE413 = -25.85;
	/**
	 * Value of the Moon secular acceleration ("/cy^2) for DE422.
	 * It requires confirmation. The value is for DE421 according to
	 * http://naif.jpl.nasa.gov/pub/naif/generic_kernels/spk/planets/de421_lunar_ephemeris_and_orientation.pdf.
	 */
	public static final double MOON_SECULAR_ACCELERATION_DE422 = -25.85;
	/**
	 * Value of the Moon secular acceleration ("/cy^2) for DE430.
	 * From http://ipnpr.jpl.nasa.gov/progress_report/42-196/196C.pdf.
	 */
	public static final double MOON_SECULAR_ACCELERATION_DE430 = -25.82;
	
	/**
	 * Corrects Julian day of calculations of JPL Ephemeris for a different value
	 * of the secular acceleration of the Moon. This method uses the current value of static
	 * variable {@linkplain Elp2000#MOON_SECULAR_ACCELERATION}, which is the (usually) adopted 
	 * value. This correction is not applied automatically in JPARSEC, and should only be applied
	 * to lunar ephemerides (mainly for DE200).
	 * 
	 * @param jd Julian day in dynamical time.
	 * @param jplID The algorithm.
	 * @return Correction to non-dynamical time in days.
	 */
	public static double timeCorrectionForSecularAcceleration(double jd, EphemerisElement.ALGORITHM jplID)
	{
		double moonSecularAcceleration = 0.0;
		switch (jplID) {
		case VSOP87_ELP2000ForMoon:
		case JPL_DE200:
			moonSecularAcceleration = JPLEphemeris.MOON_SECULAR_ACCELERATION_DE200;
			break;
		case MOSHIER:
		case SERIES96_MOSHIERForMoon:
		case JPL_DE403:
			moonSecularAcceleration = JPLEphemeris.MOON_SECULAR_ACCELERATION_DE403;
			break;
		case JPL_DE405:
		case JPL_DE406:
			moonSecularAcceleration = JPLEphemeris.MOON_SECULAR_ACCELERATION_DE405;
			break;
		case JPL_DE413:
		case JPL_DE414:
			moonSecularAcceleration = JPLEphemeris.MOON_SECULAR_ACCELERATION_DE413;
			break;
		case JPL_DE422:
			moonSecularAcceleration = JPLEphemeris.MOON_SECULAR_ACCELERATION_DE422;
			break;
		case JPL_DE424:
			moonSecularAcceleration = JPLEphemeris.MOON_SECULAR_ACCELERATION_DE422;
			break;
		case JPL_DE430:
			moonSecularAcceleration = JPLEphemeris.MOON_SECULAR_ACCELERATION_DE430;
			break;
		}
		double cent = (jd - 2435109.0) / Constant.JULIAN_DAYS_PER_CENTURY;
		double deltaT = -0.91072 * (moonSecularAcceleration - Elp2000.MOON_SECULAR_ACCELERATION) * cent * cent;

		return deltaT / Constant.SECONDS_PER_DAY;
	}
	
	/**
	 * For unit testing only.
	 * @param args Not used.
	 */
	public static void main(String args[])
	{
		System.out.println("JPLEphemeris test");
		
		try {
			JPLEphemeris jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE430);

			// Full calculations
			AstroDate astro = new AstroDate(1992, AstroDate.APRIL, 12, 0, 0, 0);
			TimeElement time = new TimeElement(astro.jd(), SCALE.TERRESTRIAL_TIME);
			CityElement city = City.findCity("Madrid");
			EphemerisElement eph = new EphemerisElement(TARGET.JUPITER, EphemerisElement.COORDINATES_TYPE.APPARENT,
					EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_1976,
					EphemerisElement.FRAME.ICRF);
			eph.algorithm = jpl.jplID;
			ObserverElement observer = ObserverElement.parseCity(city);

			EphemElement ephem = jpl.getJPLEphemeris(time, observer, eph);

			String name = ephem.name;
			String out = "", sep = FileIO.getLineSeparator();
			out += name + " "+Translate.translate(Translate.JPARSEC_RIGHT_ASCENSION)+": " + Functions.formatRA(ephem.rightAscension, 5) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_DECLINATION)+": " + Functions.formatDEC(ephem.declination, 4) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_DISTANCE)+": " + Functions.formatValue(ephem.distance, 12) + sep;

			jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE405);
			ephem = jpl.getJPLEphemeris(time, observer, eph);

			out += name + " "+Translate.translate(Translate.JPARSEC_RIGHT_ASCENSION)+": " + Functions.formatRA(ephem.rightAscension, 5) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_DECLINATION)+": " + Functions.formatDEC(ephem.declination, 4) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_DISTANCE)+": " + Functions.formatValue(ephem.distance, 12) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_ELONGATION)+": " + Functions.formatAngleAsDegrees(ephem.elongation, 8) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_PHASE_ANGLE)+": " + Functions.formatAngleAsDegrees(ephem.phaseAngle, 8) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_PHASE)+": " + Functions.formatValue(ephem.phase, 8) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_HELIOCENTRIC_ECLIPTIC_LONGITUDE)+": " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLongitude, 8) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_HELIOCENTRIC_ECLIPTIC_LATITUDE)+": " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLatitude, 8) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_HELIOCENTRIC_DISTANCE)+": " + Functions.formatValue(ephem.distanceFromSun, 8) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_SUBSOLAR_LONGITUDE)+": " + Functions.formatAngleAsDegrees(ephem.subsolarLongitude, 6) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_SUBSOLAR_LATITUDE)+": " + Functions.formatAngleAsDegrees(ephem.subsolarLatitude, 6) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_POSITION_ANGLE_OF_AXIS)+": " + Functions.formatAngleAsDegrees(ephem.positionAngleOfAxis, 6) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_POSITION_ANGLE_OF_POLE)+": " + Functions.formatAngleAsDegrees(ephem.positionAngleOfPole, 6) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_LONGITUDE_OF_CENTRAL_MERIDIAN)+": " + Functions.formatAngleAsDegrees(ephem.longitudeOfCentralMeridian, 6) + sep;
			out += name + " "+Translate.translate(Translate.JPARSEC_LIGHT_TIME)+": " + Functions.formatValue(ephem.lightTime, 18) + sep;

			System.out.println(ephem.distance * Constant.AU);
			System.out.println(out+"*********"+sep+"Series96");
			ephem = jparsec.ephem.planets.imcce.Series96.series96Ephemeris(time, observer, eph);
			jparsec.io.ConsoleReport.fullEphemReportToConsole(ephem);

			double lib[] = jpl.getPositionAndVelocity(2455713.5, TARGET.Libration);
			double nut[] = jpl.getPositionAndVelocity(2455713.5, TARGET.Nutation);
			System.out.println("Librations");
			ConsoleReport.stringArrayReport(DataSet.toStringValues(lib));
			// Should be
			// 0,067141829176838490   0,412413988874723900 3522,780878808184800000
		    // -0,000121984430648748  -0,000007337186520484   0,230087432221497220
			
			System.out.println("Nutations");
			ConsoleReport.stringArrayReport(DataSet.toStringValues(nut));
			// Should be
			// 0,000078496970210652  -0,000006384222943097   
			// 0,000000404335979328  -0,000000247269507293

			double ang1 = -2 * 0.001 * Constant.ARCSEC_TO_RAD;
			double ang2 = -12 * 0.001 * Constant.ARCSEC_TO_RAD;
			double ang3 = -2 * 0.001 * Constant.ARCSEC_TO_RAD;
			Matrix m = Matrix.getR1(ang1).times(Matrix.getR2(ang2).times(Matrix.getR3(ang3)));
			m.print(19, 16);			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
