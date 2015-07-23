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
package jparsec.time.calendar;

import java.io.Serializable;

import jparsec.util.JPARSECException;

/**
 * Support class for Chinese.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class ChineseName implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Stem.
	 */
	public int stem = 0;

	/**
	 * Branch.
	 */
	public int branch = 0;

	/**
	 * Empty constructor.
	 */
	public ChineseName()
	{
		stem = 1;
		branch = 1;
	}

	/**
	 * Full constructor.
	 * 
	 * @param i Stem.
	 * @param j Branch.
	 * @throws JPARSECException If input is invalid.
	 */
	public ChineseName(int i, int j) throws JPARSECException
	{
		if (Calendar.mod(i, 2) == Calendar.mod(j, 2))
		{
			stem = i;
			branch = j;
			return;
		} else
		{
			throw new JPARSECException("invalid input.");
		}
	}
}
