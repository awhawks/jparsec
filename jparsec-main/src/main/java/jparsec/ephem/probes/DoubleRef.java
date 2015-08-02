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
package jparsec.ephem.probes;

/**
 * <p>The <code>SDP8_SGP8</code> class is a base class to calculate ephemeris for
 * an artificial Earth satellite, using the SGP8 and SDP8 models.</p>
 * <p>Based on JSGP by Matthew Funk.</p>
 *
 * @author T. Alonso Albi - OAN (Spain)
 */
class DoubleRef
{
    double value;

    /**
     * Constructs an instance of this class which has the given value.
     */
    DoubleRef(double value)
    {
        this.value = value;
    }
}
