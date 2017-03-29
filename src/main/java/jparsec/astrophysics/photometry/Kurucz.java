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
package jparsec.astrophysics.photometry;

import jparsec.astronomy.*;
import jparsec.astrophysics.FluxElement;
import jparsec.astrophysics.MeasureElement;
import jparsec.astrophysics.Spectrum;
import jparsec.math.*;
import jparsec.graph.DataSet;
import jparsec.io.*;
import jparsec.util.*;

/**
 * A class to manipulate Kurucz models of star atmospheres. Only those
 * models with solar metallicity are supported. <BR>
 *
 * Reference: Buser, R., Kurucz, R. L., A&amp;A 264, 557-591 (1992).
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Kurucz {

	//private double temperature;
	private String fileName = "";
	private String fileContents[] = null;

	/**
	 * ID constant for twice the solar metallicity.
	 */
	//private static final String METALLICITY_TWICE_SOLAR = "p03";
	/**
	 * ID constant for solar metallicity.
	 */
	private static final String METALLICITY_SOLAR = "p00";
	/**
	 * ID constant for a quarter of solar metallicity.
	 */
	//private static final String METALLICITY_QUARTER_SOLAR = "m06";
	/**
	 * ID constant for one tenth of solar metallicity.
	 */
	//private static final String METALLICITY_TENTH_SOLAR = "m10";

	/**
	 * Constructor of a Kurucz model with solar metallicity.
	 * @param starMass Mass of the star, solar masses.
	 * @param starRadius Radius of the star, solar radii. Used to obtain the surface gravity, must be
	 * consistent with mass and temperature.
	 * @param starTemperature Star effective temperature, K. From 4000 to 60 000.
	 * @throws JPARSECException If an error occurs.
	 */
	public Kurucz(double starMass, double starRadius, double starTemperature)
	throws JPARSECException{
		this(starMass, starRadius, starTemperature, METALLICITY_SOLAR);
	}

	/**
	 * Constructor of a Kurucz model.
	 * @param starMass Mass of the star, solar masses.
	 * @param starRadius Radius of the star, solar radii. Used to obtain the surface gravity, must be
	 * consistent with mass and temperature.
	 * @param starTemperature Star effective temperature, K. From 4000 to 60 000.
	 * @param metallicity Metallicity, must be solar. Constants defined in this class, but note that only
	 * solar metallicity is supported (see available files inside the kurucz .jar file).
	 * @throws JPARSECException If an error occurs.
	 */
	private Kurucz(double starMass, double starRadius, double starTemperature, String metallicity)
	throws JPARSECException{
		if (!metallicity.equals(METALLICITY_SOLAR)) throw new JPARSECException("Only solar metallicity is supported.");

		double gravity = 10.0 * Math.log10(Star.getSurfaceGravity(starMass, starRadius) * 100.0);

		int grav = (int) (gravity + 0.5);
		if (grav < 0 && grav >= -5) {
			grav = 0;
		} else {
			if (grav > 50 && grav <= 55) {
				grav = 50;
			} else {
				if (grav < 0 || grav > 50) {
					throw new JPARSECException("gravity value "+grav+" outside acceptable range -5 to 55.");
				} else {
					grav = 5 * ((int) (gravity / 5.0 + 0.5));
				}
			}
		}

		if (starTemperature < 4000 || starTemperature > 60000)
			throw new JPARSECException("temperature value "+starTemperature+" outside acceptable range 4000 to 60000.");
		int temp = 500 * ((int) (starTemperature / 500.0 + 0.5));
		if (temp > 13000) temp = 1000 * ((int) (starTemperature / 1000.0 + 0.5));
		if (temp > 35000) temp = 2500 * ((int) (starTemperature / 2500.0 + 0.5));
		if (temp > 50000) temp = 10000 * ((int) (starTemperature / 10000.0 + 0.5));

		if (temp > 40000) grav = 50;

		fileName = "t"+temp+"g"+grav+metallicity+".dat";

		//this.temperature = starTemperature;
	}

	private void readFile()
	throws JPARSECException {
		try {
			fileContents = DataSet.arrayListToStringArray(ReadFile.readResource(FileIO.DATA_KURUCZ_DIRECTORY + fileName));
		} catch (Exception exc) {
			throw new JPARSECException("Cannot read kurucz model: "+fileName);
		}
	}

	/**
	 * Obtains star emission for the current instance.
	 * The flux is given as in the Kurucz model, which is already multiplied
	 * by PI. To account for the solid angle it is enough to multiply the flux
	 * by (r/d)^2, where r is the star radius and d its distance.
	 * @param lambda Wavelength in m. Between 47E-10 and 91000E-10 m.
	 * @return Emission in Jy. 0.0 will be returned if
	 * wavelength is out of range.
	 * @throws JPARSECException If an error occurs.
	 */
	public double getStarEmission (double lambda)
	throws JPARSECException {
		if (fileContents == null) readFile();

		try {
			double x[] = new double[fileContents.length];
			double y[] = new double[fileContents.length];
			for (int i=0; i<fileContents.length; i++)
			{
				x[i] = DataSet.parseDouble(jparsec.io.FileIO.getField(1, fileContents[i], " ", true));
				y[i] = DataSet.parseDouble(jparsec.io.FileIO.getField(2, fileContents[i], " ", true));

				y[i] = y[i] * x[i] * x[i] / (Constant.SPEED_OF_LIGHT * 1.0E10);
				y[i] = y[i] * Constant.ERG_S_CM2_HZ_TO_JY;
				x[i] = x[i] * 1.0E-10;
			}
			Interpolation interp = new Interpolation(x, y, false);
			double f = interp.linearInterpolationInLogScale(lambda);

			return f;

		} catch (JPARSECException e) {	return 0.0; }
	}

	/**
	 * Returns the Kurucz spectrum for the star.
	 * @param np Number of points in the spectrum. You may need a very high value
	 * here to sample properly the high frequency part of the spectrum.
	 * @return The specturm.
	 * @throws JPARSECException If an error occurs.
	 */
	public Spectrum getSpectrum(int np) throws JPARSECException {
		if (fileContents == null) readFile();

		double x[] = new double[fileContents.length];
		double y[] = new double[fileContents.length];
		for (int i=0; i<fileContents.length; i++)
		{
			x[i] = DataSet.parseDouble(jparsec.io.FileIO.getField(1, fileContents[i], " ", true));
			y[i] = DataSet.parseDouble(jparsec.io.FileIO.getField(2, fileContents[i], " ", true));

			y[i] = y[i] * x[i] * x[i] / (Constant.SPEED_OF_LIGHT * 1.0E10);
			y[i] = y[i] * Constant.ERG_S_CM2_HZ_TO_JY;
			x[i] = x[i] * 1.0E-4;
		}


		FluxElement f[] = new FluxElement[np];
		double maxNu = Constant.SPEED_OF_LIGHT / 47.0E-4; // MHz
		double minNu = Constant.SPEED_OF_LIGHT / 91100.0E-4; // MHz

		Interpolation interp = new Interpolation(x, y, false);
		double newNu[] = new double[f.length];
		for (int i=0; i<f.length; i++) {
			newNu[i] = minNu + (maxNu - minNu) * (i / (f.length - 1.0));
			double l = Constant.SPEED_OF_LIGHT / newNu[i];
			f[i] = new FluxElement(new MeasureElement(i+1, 0, null), new MeasureElement(interp.splineInterpolation(l), 0, MeasureElement.UNIT_Y_JY));
		}
		Spectrum sp = new Spectrum(f);
		int refCh = f.length/2;
		sp.referenceChannel = 1+refCh;
		sp.referenceVelocity = 0;
		sp.referenceFrequency = newNu[refCh];
		double dnu = newNu[refCh+1] - sp.referenceFrequency;
		sp.velocityResolution = -Constant.SPEED_OF_LIGHT * 0.001 * dnu / sp.referenceFrequency;
		sp.source = fileName;
		sp.line = fileName;
		return sp;
	}
}
