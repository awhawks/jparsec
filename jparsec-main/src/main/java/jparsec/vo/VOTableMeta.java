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
package jparsec.vo;

import java.io.Serializable;

/**
 * A class to set metadata for VO tables.<P>
 * 
 * Metadata are atributes to resources, tables, or fields in the VO Table.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class VOTableMeta implements Serializable {
	static final long serialVersionUID = 1L;

	/**
	 * ID constant for float datatype.
	 */
	public static final String DATATYPE_FLOAT = "float";
	/**
	 * ID constant for short datatype.
	 */
	public static final String DATATYPE_SHORT = "short";
	/**
	 * ID constant for integer datatype.
	 */
	public static final String DATATYPE_INT = "int";
	/**
	 * ID constant for character datatype.
	 */
	public static final String DATATYPE_CHAR = "char";
	/**
	 * ID constant for double datatype.
	 */
	public static final String DATATYPE_DOUBLE = "double";
	
	/**
	 * ID constant for magnitude unit.
	 */
	public static final String UNIT_MAG = "mag";
	/**
	 * ID constant for degree unit.
	 */
	public static final String UNIT_DEG = "deg";
	/**
	 * ID constant for milliarcsecond unit.
	 */
	public static final String UNIT_MAS = "mas";
	/**
	 * ID constant for milliarcsecond per year unit.
	 */
	public static final String UNIT_MAS_BY_YEAR = "mas/yr";
	/**
	 * ID constant for pixel unit.
	 */
	public static final String UNIT_PIX = "pix";
	/**
	 * ID constant for Jansky unit.
	 */
	public static final String UNIT_JY = "Jy";
	/**
	 * ID constant for kilometer per second unit.
	 */
	public static final String UNIT_KMS = "km/s";
	/**
	 * ID constant for Kelvin unit.
	 */
	public static final String UNIT_K = "K";
	/**
	 * ID constant for second unit.
	 */
	public static final String UNIT_S = "s";

	/**
	 * Name. 
	 */
	public String name;
	/**
	 * UCD code.
	 */
	public String ucd;
	/**
	 * Unit.
	 */
	public String unit;
	/**
	 * Data type.
	 */
	public String datatype;
	/**
	 * Array size.
	 */
	public String arraysize;
	/**
	 * Description.
	 */
	public String description;
	/**
	 * ID.
	 */
	public String id;
	/**
	 * Precission in decimal places (as an int value).
	 */
	public String precision;
	/**
	 * Width or field length in characters.
	 */
	public String width;
	/**
	 * Reference field, for example J2000 for equatorial coordinates.
	 */
	public String ref;
	/**
	 * Simple constructor adequate for tables and resources.
	 * @param name Name.
	 * @param id ID.
	 * @param description Description.
	 */
	public VOTableMeta(String name, String id, String description)
	{
		this.name = name;
		this.id = id;
		this.description = description;
	}
	private VOTableMeta() {}
	/**
	 * Constructor adequate for fields. 
	 * @param name Name.
	 * @param id ID.
	 * @param description Description.
	 * @param datatype Data type. Some constants defined in this class.
	 * @param precision Precision.
	 * @param width Width.
	 * @param ucd UCD code.
	 * @param unit Unit. Some constants defined in this class.
	 */
	public VOTableMeta(String name, String id, String description, String datatype, String precision,
			String width, String ucd, String unit)
	{
		this.name = name;
		this.id = id;
		this.description = description;
		this.datatype = datatype;
		this.precision = precision;
		this.width = width;
		this.ucd = ucd;
		this.unit = unit;
	}
	
	/**
	 * Returns true if the input object is equals to this instance.
	 */
	public boolean equals (Object o)
	{
		if (o == null) {
			if (this == null) return true;
			return false;
		}
		if (this == null) {
			return false;
		}
		VOTableMeta v = (VOTableMeta) o;
		boolean equals = true;
		if (!v.arraysize.equals(this.arraysize)) equals = false;
		if (!v.datatype.equals(this.datatype)) equals = false;
		if (!v.description.equals(this.description)) equals = false;
		if (!v.id.equals(this.id)) equals = false;
		if (!v.name.equals(this.name)) equals = false;
		if (!v.precision.equals(this.precision)) equals = false;
		if (!v.ref.equals(this.ref)) equals = false;
		if (!v.ucd.equals(this.ucd)) equals = false;
		if (!v.unit.equals(this.unit)) equals = false;
		if (!v.width.equals(this.width)) equals = false;
		return equals;
	}
	/**
	 * Clones this instance.
	 */
	public Object clone()
	{
		if (this == null) return null;
		VOTableMeta v = new VOTableMeta();

		v.arraysize = this.arraysize;
		v.datatype = this.datatype;
		v.description = this.description;
		v.id = this.id;
		v.name = this.name;
		v.precision = this.precision;
		v.ref = this.ref;
		v.ucd = this.ucd;
		v.unit = this.unit;
		v.width = this.width;
		return v;
	}
}
