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
package jparsec.ephem.planets.imcce;

// One ELP2000 term

/**
 * All the static data of the ELP2000 theory (part 1), in a
 * separate file for managability. The whole theory is applied. This is part 1
 * (from 2) of the data.
 * <p>
 * Library users can ignore this class.
 * <p>
 * <I><B>Reference</B></I>
 * <p>
 * ELP 2000-85: a semi-analytical lunar ephemeris adequate for historical times.
 * Chapront-Touze M., Chapront J. Astron. and Astrophys. 190, 342 (1988).
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @since version 1.0
 */

final class elp_plan {
    static final Elp2000Set2 Lon[] = {
            new Elp2000Set2(new int[] { 0, 0, 1, -2, 0 }, new double[] { 0.00000, 0.00007, 0.039 }),
            new Elp2000Set2(new int[] { 0, 0, 1, -1, 0 }, new double[] { 0.00000, 0.00108, 0.082 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 0, 0 }, new double[] { 0.00000, 0.00487, 1.000 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 1, 0 }, new double[] { 0.00000, 0.00080, 0.070 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 2, 0 }, new double[] { 0.00000, 0.00006, 0.036 }),
            new Elp2000Set2(new int[] { 0, 0, 2, -1, 0 }, new double[] { 0.00000, 0.00004, 0.089 }),
            new Elp2000Set2(new int[] { 0, 0, 2, 0, 0 }, new double[] { 0.00000, 0.00011, 0.500 }),
            new Elp2000Set2(new int[] { 0, 0, 2, 1, 0 }, new double[] { 0.00000, 0.00002, 0.066 }),
            new Elp2000Set2(new int[] { 0, 1, 1, 0, 0 }, new double[] { 180.00000, 0.00013, 0.075 }),
            new Elp2000Set2(new int[] { 0, 2, -2, -1, 0 }, new double[] { 180.00000, 0.00011, 0.105 }),
            new Elp2000Set2(new int[] { 0, 2, -2, 0, 0 }, new double[] { 180.00000, 0.00012, 0.044 }),
            new Elp2000Set2(new int[] { 0, 2, -2, 1, 0 }, new double[] { 180.00000, 0.00001, 0.028 }),
            new Elp2000Set2(new int[] { 0, 2, -1, -2, 0 }, new double[] { 180.00000, 0.00006, 0.360 }),
            new Elp2000Set2(new int[] { 0, 2, -1, -1, 0 }, new double[] { 180.00000, 0.00150, 0.095 }),
            new Elp2000Set2(new int[] { 0, 2, -1, 0, -2 }, new double[] { 180.00000, 0.00002, 0.322 }),
            new Elp2000Set2(new int[] { 0, 2, -1, 0, 0 }, new double[] { 180.00000, 0.00120, 0.042 }),
            new Elp2000Set2(new int[] { 0, 2, -1, 1, 0 }, new double[] { 180.00000, 0.00011, 0.027 }),
            new Elp2000Set2(new int[] { 0, 2, 0, -1, 0 }, new double[] { 0.00000, 0.00002, 0.087 }),
            new Elp2000Set2(new int[] { 0, 2, 0, 0, 0 }, new double[] { 0.00000, 0.00003, 0.040 }),
            new Elp2000Set2(new int[] { 0, 2, 1, -2, 0 }, new double[] { 180.00000, 0.00002, 1.292 }),
            new Elp2000Set2(new int[] { 0, 2, 1, -1, 0 }, new double[] { 0.00000, 0.00021, 0.080 }),
            new Elp2000Set2(new int[] { 0, 2, 1, 0, -2 }, new double[] { 0.00000, 0.00001, 0.903 }),
            new Elp2000Set2(new int[] { 0, 2, 1, 0, 0 }, new double[] { 0.00000, 0.00018, 0.039 }),
            new Elp2000Set2(new int[] { 0, 2, 1, 1, 0 }, new double[] { 0.00000, 0.00002, 0.026 }),
            new Elp2000Set2(new int[] { 0, 2, 2, -1, 0 }, new double[] { 0.00000, 0.00004, 0.074 }),
            new Elp2000Set2(new int[] { 0, 4, -1, -2, 0 }, new double[] { 180.00000, 0.00002, 0.046 }),
            new Elp2000Set2(new int[] { 0, 4, -1, -1, 0 }, new double[] { 180.00000, 0.00003, 0.028 }),
            new Elp2000Set2(new int[] { 0, 4, -1, 0, 0 }, new double[] { 180.00000, 0.00001, 0.021 })
    };
    static final Elp2000Set2 Lat[] = {
            new Elp2000Set2(new int[] { 0, 0, 1, -1, -1 }, new double[] { 0.00000, 0.00005, 0.039 }),
            new Elp2000Set2(new int[] { 0, 0, 1, -1, 1 }, new double[] { 0.00000, 0.00004, 0.857 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 0, -1 }, new double[] { 0.00000, 0.00004, 0.081 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 0, 1 }, new double[] { 0.00000, 0.00005, 0.069 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 1, -1 }, new double[] { 0.00000, 0.00004, 1.200 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 1, 1 }, new double[] { 0.00000, 0.00004, 0.036 }),
            new Elp2000Set2(new int[] { 0, 2, -2, 0, -1 }, new double[] { 180.00000, 0.00002, 0.107 }),
            new Elp2000Set2(new int[] { 0, 2, -1, -1, -1 }, new double[] { 180.00000, 0.00005, 0.340 }),
            new Elp2000Set2(new int[] { 0, 2, -1, -1, 1 }, new double[] { 180.00000, 0.00006, 0.042 }),
            new Elp2000Set2(new int[] { 0, 2, -1, 0, -1 }, new double[] { 180.00000, 0.00022, 0.097 }),
            new Elp2000Set2(new int[] { 0, 2, -1, 0, 1 }, new double[] { 180.00000, 0.00006, 0.027 }),
            new Elp2000Set2(new int[] { 0, 2, -1, 1, -1 }, new double[] { 180.00000, 0.00001, 0.042 }),
            new Elp2000Set2(new int[] { 0, 2, 1, 0, -1 }, new double[] { 0.00000, 0.00009, 0.081 })
    };
    static final Elp2000Set2 Rad[] = {
            new Elp2000Set2(new int[] { 0, 0, 1, -2, 0 }, new double[] { 90.00000, 0.00005, 0.039 }),
            new Elp2000Set2(new int[] { 0, 0, 1, -1, 0 }, new double[] { 90.00000, 0.00095, 0.082 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 0, 0 }, new double[] { 270.00000, 0.00036, 1.000 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 1, 0 }, new double[] { 270.00000, 0.00077, 0.070 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 2, 0 }, new double[] { 270.00000, 0.00004, 0.036 }),
            new Elp2000Set2(new int[] { 0, 0, 2, -1, 0 }, new double[] { 90.00000, 0.00003, 0.089 }),
            new Elp2000Set2(new int[] { 0, 1, 1, 0, 0 }, new double[] { 90.00000, 0.00012, 0.075 }),
            new Elp2000Set2(new int[] { 0, 2, -2, -1, 0 }, new double[] { 90.00000, 0.00007, 0.105 }),
            new Elp2000Set2(new int[] { 0, 2, -2, 0, 0 }, new double[] { 90.00000, 0.00014, 0.044 }),
            new Elp2000Set2(new int[] { 0, 2, -1, -2, 0 }, new double[] { 270.00000, 0.00007, 0.360 }),
            new Elp2000Set2(new int[] { 0, 2, -1, -1, 0 }, new double[] { 90.00000, 0.00111, 0.095 }),
            new Elp2000Set2(new int[] { 0, 2, -1, 0, 0 }, new double[] { 90.00000, 0.00149, 0.042 }),
            new Elp2000Set2(new int[] { 0, 2, -1, 1, 0 }, new double[] { 90.00000, 0.00009, 0.027 }),
            new Elp2000Set2(new int[] { 0, 2, 0, 0, 0 }, new double[] { 270.00000, 0.00004, 0.040 }),
            new Elp2000Set2(new int[] { 0, 2, 1, -1, 0 }, new double[] { 270.00000, 0.00018, 0.080 }),
            new Elp2000Set2(new int[] { 0, 2, 1, 0, 0 }, new double[] { 270.00000, 0.00023, 0.039 }),
            new Elp2000Set2(new int[] { 0, 2, 1, 1, 0 }, new double[] { 270.00000, 0.00002, 0.026 }),
            new Elp2000Set2(new int[] { 0, 2, 2, -1, 0 }, new double[] { 270.00000, 0.00003, 0.074 }),
            new Elp2000Set2(new int[] { 0, 4, -1, -1, 0 }, new double[] { 90.00000, 0.00003, 0.028 })
    };
}
