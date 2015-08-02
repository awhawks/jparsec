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
 <p>The <code>SDP8_SGP8</code> class is a base class to calculate ephemeris for
 an artificial Earth satellite, using the SGP8 and SDP8 models.</p>
 <p>Based on JSGP by Matthew Funk.</p>

 @author T. Alonso Albi - OAN (Spain)
 */
class C1
{
    final static double E6A = 1.E-6;
    final static double TOTHRD = .66666667;
    final static double XJ2 = 1.082616E-3;
    final static double XJ4 = -1.65597E-6;
    final static double XJ3 = -.253881E-5;
    final static double XKE = .743669161E-1;
    final static double XKMPER = 6378.135;
    final static double XMNPDA = 1440.;
    final static double AE = 1.;
    final static double QO = 120.0;
    final static double SO = 78.0;
    final static double CK2 =.5*XJ2*Math.pow(AE,2);
    final static double CK4 = -.375*XJ4*Math.pow(AE,4);
    final static double QOMS2T = Math.pow(((QO-SO)*AE/XKMPER),4);
    final static double S = AE*(1.+SO/XKMPER);
}
