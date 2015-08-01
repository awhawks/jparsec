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
package jparsec.astrophysics;

import java.io.Serializable;

import jparsec.astrophysics.photometry.PhotometricBandElement;
import jparsec.astrophysics.photometry.Photometry;
import jparsec.graph.*;
import jparsec.util.*;

/**
 * This class holds each point in a spectra or SEDs.
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class FluxElement implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Wavelength/velocity value.
	 */
	public MeasureElement x;

	/**
	 * Flux value.
	 */
	public MeasureElement y;

	/**
	 * Holds the photometric band for this point.
	 */
	public PhotometricBandElement photometricBand;

	/**
	 * Simple constructor.
	 * @param x X value.
	 * @param y Y value.
	 */
	public FluxElement(MeasureElement x, MeasureElement y)
	{
		this.x = x;
		this.y = y;
	}
	/**
	 * Full constructor.
	 * @param x X value.
	 * @param y Y value.
	 * @param b The photometric band.
	 */
	public FluxElement(MeasureElement x, MeasureElement y, PhotometricBandElement b)
	{
		this.x = x;
		this.y = y;
		if (b != null) this.photometricBand = b.clone();
	}
	
	/**
	 * Clones this instance.
	 */
	public FluxElement clone()
	{
		if (this == null) return null;
		if (photometricBand == null) return new FluxElement(this.x.clone(), this.y.clone(), null);
		FluxElement p = new FluxElement(this.x.clone(), this.y.clone(), this.photometricBand.clone());
		return p;
	}
	
	/**
	 * Returns if this instance is equals to another.
	 */
	public boolean equals(Object o)
	{
		if (o == null) {
			if (this == null) return true;
			return false;
		}
		if (this == null) {
			return false;
		}
		FluxElement p = (FluxElement) o;
		boolean equals = true;
		if (!this.x.equals(p.x)) equals = false;
		if (!this.y.equals(p.y)) equals = false;
		if (!this.photometricBand.equals(p.photometricBand)) equals = false;
		return equals;
	}	
	
	private MeasureElement transformX(String newUnit)
	throws JPARSECException {
		if (newUnit == null || this.x.unit == null || this.x.unit.equals(newUnit)) return this.x;
		return this.x.get(newUnit);
	}

	private MeasureElement transformY(String newUnit)
	throws JPARSECException {
		if (newUnit == null || this.y.unit == null || this.y.unit.equals(newUnit)) return this.y;
		
		if (this.y.unit.equals(MeasureElement.UNIT_Y_MAG))
		{
			MeasureElement out = Photometry.getFluxFromMagnitude(DataSet.getDoubleValueWithoutLimit(this.y.value), this.y.error, this.photometricBand);
			String limit = DataSet.getLimit(this.y.value);
			MeasureElement p = new MeasureElement(limit+out.getValue(), out.error, MeasureElement.UNIT_Y_JY);
			return p.get(newUnit);
		}

		if (newUnit.equals(MeasureElement.UNIT_Y_MAG))
		{
			MeasureElement out = Photometry.getMagnitudeFromFlux(DataSet.getDoubleValueWithoutLimit(this.y.value), this.y.error, this.photometricBand);
			String limit = DataSet.getLimit(this.y.value);
			MeasureElement p = new MeasureElement(limit+out.getValue(), out.error, MeasureElement.UNIT_Y_MAG);
			return p;
		}

		return this.y.get(newUnit);
	}

	/**
	 * Converts the current x value to a new Unit.
	 * @param newUnit New unit in standard conventions.
	 * @throws JPARSECException If an error occurs.
	 */
	public void convertX(String newUnit)
	throws JPARSECException {
		this.x = this.transformX(newUnit);
	}
	
	/**
	 * Obtains the current x value in a new unit.
	 * @param newUnit The new unit.
	 * @return The converted value.
	 * @throws JPARSECException If an error occurs.
	 */
	public MeasureElement getX(String newUnit)
	throws JPARSECException {
		return this.transformX(newUnit);
	}

	/**
	 * Converts the current y value to a new Unit.
	 * @param newUnit New unit in standard conventions.
	 * @throws JPARSECException If an error occurs.
	 */
	public void convertY(String newUnit)
	throws JPARSECException {
		this.y = this.transformY(newUnit);
	}
	
	/**
	 * Obtains the current y value in a new unit.
	 * @param newUnit The new unit.
	 * @return The converted value.
	 * @throws JPARSECException If an error occurs.
	 */
	public MeasureElement getY(String newUnit)
	throws JPARSECException {
		return this.transformY(newUnit);
	}
	
	/**
	 * Returns a String representation of this object.
	 */
	public String toString() {
		String s = x.toString()+", "+y.toString();
		if (photometricBand != null) s += " ("+photometricBand.name+")";
		return s;
	}
}
