/*
 * This file is part of JPARSEC library.
 *
 * (C) Copyright 2006-2017 by T. Alonso Albi - OAN (Spain).
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
package jparsec.astrophysics.gildas;

import java.io.*;

import jparsec.graph.DataSet;
import jparsec.io.image.*;

/**
 * A simple class to hold parameters that contains a value, a
 * description, and a data type.
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Parameter implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Value.
	 */
	public String value;
	/**
	 * Description.
	 */
	public String description;
	/**
	 * The data type.
	 */
	public DATA_TYPE dataType;

	/**
	 * The set of data types for the parameters.
	 */
	public enum DATA_TYPE {
		/** ID constant for a double datatype. */
		DOUBLE,
		/** ID constant for an integer datatype. */
		INT,
		/** ID constant for a string datatype. */
		STRING
	};

	/**
	 * Constructor for a float parameter.
	 * @param f Value.
	 * @param d Description.
	 */
	public Parameter(float f, String d)
	{
		value = ""+f;
		description = d;
		dataType = DATA_TYPE.DOUBLE;
	}
	/**
	 * Constructor for a double parameter.
	 * @param f Value.
	 * @param d Description.
	 */
	public Parameter(double f, String d)
	{
		value = ""+f;
		description = d;
		dataType = DATA_TYPE.DOUBLE;
	}
	/**
	 * Constructor for a string parameter.
	 * @param f Value.
	 * @param d Description.
	 */
	public Parameter(String f, String d)
	{
		value = ""+f;
		description = d;
		dataType = DATA_TYPE.STRING;
	}
	/**
	 * Constructor for an integer parameter.
	 * @param f Value.
	 * @param d Description.
	 */
	public Parameter(int f, String d)
	{
		value = ""+f;
		description = d;
		dataType = DATA_TYPE.INT;
	}

	/**
	 * Checks if two parameters are the same.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Parameter)) return false;

		Parameter parameter = (Parameter) o;

		if (value != null ? !value.equals(parameter.value) : parameter.value != null) return false;
		if (description != null ? !description.equals(parameter.description) : parameter.description != null)
			return false;

		return dataType == parameter.dataType;
	}

	@Override
	public int hashCode() {
		int result = value != null ? value.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
		return result;
	}

	/**
	 * Clones this instance.
	 */
	@Override
	public Parameter clone()
	{
		Parameter p = new Parameter(this.value, this.description);
		p.dataType = this.dataType;
		return p;
	}

	/**
	 * Searches for a given parameter by its description.
	 * @param param The array of parameters.
	 * @param d The description to search for.
	 * @return The parameter found, or null if there's no any.
	 */
	public static Parameter getByDescription(Parameter param[], String d)
	{
		Parameter p = null;
		for (int i=0; i<param.length; i++)
		{
			if (param[i].description.equals(d)) {
				p = param[i].clone();
				break;
			}
		}
		return p;
	}

	/**
	 * Searches for a given parameter by its key.
	 * @param param The array of parameters.
	 * @param d The key to search for.
	 * @return The parameter found, or null if there's no any.
	 */
	public static Parameter getByKey(Parameter param[], String d)
	{
		Parameter p = null;
		for (int i=0; i<param.length; i++)
		{
			if (param[i].getKey().equals(d)) {
				p = param[i].clone();
				break;
			}
		}
		return p;
	}

	/**
	 * Returns the corresponding key of a given parameter description. The math
	 * is performed using the current set of parameters defined in {@linkplain Gildas30m}.
	 * @return The parameter key, or null if no match is found.
	 */
	public String getKey()
	{
		String key = null;
		if (this.description.equals(Gildas30m.ALTITUDE_DESC)) key = Gildas30m.ALTITUDE;
		if (this.description.equals(Gildas30m.AZIMUTH_DESC)) key = Gildas30m.AZIMUTH;
		if (this.description.equals(Gildas30m.BAD_DESC)) key = Gildas30m.BAD;
		if (this.description.equals(Gildas30m.BEAM_EFF_DESC)) key = Gildas30m.BEAM_EFF;
		if (this.description.equals(Gildas30m.BETA_DESC)) key = Gildas30m.BETA;
		if (this.description.equals(Gildas30m.BETA_OFF_DESC)) key = Gildas30m.BETA_OFF;
		if (this.description.equals(Gildas30m.LATOFF_DESC)) key = Gildas30m.LATOFF;
		if (this.description.equals(Gildas30m.BLOCK_DESC)) key = Gildas30m.BLOCK;
		if (this.description.equals(Gildas30m.COUNT1_DESC)) key = Gildas30m.COUNT1;
		if (this.description.equals(Gildas30m.COUNT2_DESC)) key = Gildas30m.COUNT2;
		if (this.description.equals(Gildas30m.COUNT3_DESC)) key = Gildas30m.COUNT3;
		if (this.description.equals(Gildas30m.ELEVATION_DESC)) key = Gildas30m.ELEVATION;
		if (this.description.equals(Gildas30m.EPOCH_DESC)) key = Gildas30m.EPOCH;
		if (this.description.equals(Gildas30m.FACTOR_DESC)) key = Gildas30m.FACTOR;
		if (this.description.equals(Gildas30m.FORW_EFF_DESC)) key = Gildas30m.FORW_EFF;
		if (this.description.equals(Gildas30m.FREQ_OFF_DESC)) key = Gildas30m.FREQ_OFF;
		if (this.description.equals(Gildas30m.FREQ_RESOL_DESC)) key = Gildas30m.FREQ_RESOL;
		if (this.description.equals(Gildas30m.GAIN_IM_DESC)) key = Gildas30m.GAIN_IM;
		if (this.description.equals(Gildas30m.GAUSS_DESC)) key = Gildas30m.GAUSS;
		if (this.description.equals(Gildas30m.H2OMM_DESC)) key = Gildas30m.H2OMM;
		if (this.description.equals(Gildas30m.IMAGE_DESC)) key = Gildas30m.IMAGE;
		if (this.description.equals(Gildas30m.INTEG_DESC)) key = Gildas30m.INTEG;
		if (this.description.equals(Gildas30m.KIND_DESC)) key = Gildas30m.KIND;
		if (this.description.equals(Gildas30m.LAMBDA_DESC)) key = Gildas30m.LAMBDA;
		if (this.description.equals(Gildas30m.LAMBDA_OFF_DESC)) key = Gildas30m.LAMBDA_OFF;
		if (this.description.equals(Gildas30m.LONOFF_DESC)) key = Gildas30m.LONOFF;
		if (this.description.equals(Gildas30m.LON_DESC)) key = Gildas30m.LON;
		if (this.description.equals(Gildas30m.LAT_DESC)) key = Gildas30m.LAT;
		if (this.description.equals(Gildas30m.LDOBS_DESC)) key = Gildas30m.LDOBS;
		if (this.description.equals(Gildas30m.LDRED_DESC)) key = Gildas30m.LDRED;
		if (this.description.equals(Gildas30m.LINE_DESC)) key = Gildas30m.LINE;
		if (this.description.equals(Gildas30m.LST_TIME_DESC)) key = Gildas30m.LST_TIME;
		if (this.description.equals(Gildas30m.MODE_DESC)) key = Gildas30m.MODE;
		if (this.description.equals(Gildas30m.MYVERSION_DESC)) key = Gildas30m.MYVERSION;
		if (this.description.equals(Gildas30m.NCHAN_DESC)) key = Gildas30m.NCHAN;
		if (this.description.equals(Gildas30m.NPHASE_DESC)) key = Gildas30m.NPHASE;
		if (this.description.equals(Gildas30m.NUM_DESC)) key = Gildas30m.NUM;
		if (this.description.equals(Gildas30m.OFF1_DESC)) key = Gildas30m.OFF1;
		if (this.description.equals(Gildas30m.OFF2_DESC)) key = Gildas30m.OFF2;
		if (this.description.equals(Gildas30m.OTF_LEN_DATA_DESC)) key = Gildas30m.OTF_LEN_DATA;
		if (this.description.equals(Gildas30m.OTF_LEN_DUMP_DESC)) key = Gildas30m.OTF_LEN_DUMP;
		if (this.description.equals(Gildas30m.OTF_LEN_HEADER_DESC)) key = Gildas30m.OTF_LEN_HEADER;
		if (this.description.equals(Gildas30m.OTF_NDUMPS_DESC)) key = Gildas30m.OTF_NDUMPS;
		if (this.description.equals(Gildas30m.PAMB_DESC)) key = Gildas30m.PAMB;
		if (this.description.equals(Gildas30m.POSA_DESC)) key = Gildas30m.POSA;
		if (this.description.equals(Gildas30m.PROJECTION_DESC)) key = Gildas30m.PROJECTION;
		if (this.description.equals(Gildas30m.QUALITY_DESC)) key = Gildas30m.QUALITY;
		if (this.description.equals(Gildas30m.REF_CHAN_DESC)) key = Gildas30m.REF_CHAN;
		if (this.description.equals(Gildas30m.REF_FREQ_DESC)) key = Gildas30m.REF_FREQ;
		if (this.description.equals(Gildas30m.SCAN_DESC)) key = Gildas30m.SCAN;
		if (this.description.equals(Gildas30m.SIGMA_DESC)) key = Gildas30m.SIGMA;
		if (this.description.equals(Gildas30m.SOURCE_DESC)) key = Gildas30m.SOURCE;
		if (this.description.equals(Gildas30m.TAMB_DESC)) key = Gildas30m.TAMB;
		if (this.description.equals(Gildas30m.TATMSIG_DESC)) key = Gildas30m.TATMSIG;
		if (this.description.equals(Gildas30m.TAU_DESC)) key = Gildas30m.TAU;
		if (this.description.equals(Gildas30m.TAUIMA_DESC)) key = Gildas30m.TAUIMA;
		if (this.description.equals(Gildas30m.TAUSIG_DESC)) key = Gildas30m.TAUSIG;
		if (this.description.equals(Gildas30m.TCHOP_DESC)) key = Gildas30m.TCHOP;
		if (this.description.equals(Gildas30m.TCOLD_DESC)) key = Gildas30m.TCOLD;
		if (this.description.equals(Gildas30m.TELES_DESC)) key = Gildas30m.TELES;
		if (this.description.equals(Gildas30m.TREC_DESC)) key = Gildas30m.TREC;
		if (this.description.equals(Gildas30m.TSYS_DESC)) key = Gildas30m.TSYS;
		if (this.description.equals(Gildas30m.TYPEC_DESC)) key = Gildas30m.TYPEC;
		if (this.description.equals(Gildas30m.UT_TIME_DESC)) key = Gildas30m.UT_TIME;
		if (this.description.equals(Gildas30m.VEL_RESOL_DESC)) key = Gildas30m.VEL_RESOL;
		if (this.description.equals(Gildas30m.VEL_TYPE_DESC)) key = Gildas30m.VEL_TYPE;
		if (this.description.equals(Gildas30m.VERSION_DESC)) key = Gildas30m.VERSION;
		if (this.description.equals(Gildas30m.REF_VEL_DESC)) key = Gildas30m.REF_VEL;
		return key;
	}
	/**
	 * Transforms a given set of parameters into an image header instance.
	 * @param param The parameters.
	 * @return The image header.
	 */
	public static ImageHeaderElement[] toImageHeader(Parameter param[])
	{
		ImageHeaderElement img[] = new ImageHeaderElement[param.length];
		for (int i=0; i<img.length; i++)
		{
			String key = param[i].getKey();
			if (key.length() > 8) key = key.substring(0, 8);
			img[i] = new ImageHeaderElement(key, param[i].value, param[i].description);
		}
		return img;
	}

	/**
	 * Transforms a given header set to a set of parameters.
	 * Key and format fields are ignored, parameters will contain string values.
	 * @param header The header.
	 * @return The output parameters.
	 */
	public static Parameter[] fromImageHeader(ImageHeaderElement header[])
	{
		Parameter img[] = new Parameter[header.length];
		for (int i=0; i<img.length; i++)
		{
			img[i] = new Parameter(header[i].value, header[i].comment);
		}
		return img;
	}

	/**
	 * Transforms a given set of parameters into an image header instance.
	 * @return The image header.
	 */
	public ImageHeaderElement toImageHeader()
	{
		String key = this.getKey();
		if (key.length() > 8) key = key.substring(0, 8);
		ImageHeaderElement img = new ImageHeaderElement(key, this.value, this.description);
		return img;
	}

	/**
	 * Returns the value as an integer.
	 * 0 is returned if the value is null or empty.
	 * @return The value.
	 */
	public int toInt()
	{
		if (this.value == null || this.value.equals("")) return 0;
		return Integer.parseInt(this.value);
	}
	/**
	 * Returns the value as a double.
	 * 0 is returned if the value is null or empty.
	 * @return The value.
	 */
	public double toDouble()
	{
		if (this.value == null || this.value.equals("")) return 0;
		return DataSet.parseDouble(this.value);
	}
	/**
	 * Returns the value as a float.
	 * 0 is returned if the value is null or empty.
	 * @return The value.
	 */
	public float toFloat()
	{
		if (this.value == null || this.value.equals("")) return 0;
		return DataSet.parseFloat(this.value);
	}
}
