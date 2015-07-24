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

final class elp_moon {
    static final Elp2000Set2 Lon[] = {
            new Elp2000Set2(new int[] { 0, 0, 0, 0, 1 }, new double[] { 303.96185, 0.00004, 0.075 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 1, -1 }, new double[] { 259.88393, 0.00016, 5.997 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 2, -2 }, new double[] { 0.43020, 0.00040, 2.998 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 3, -2 }, new double[] { 0.43379, 0.00002, 0.077 }),
            new Elp2000Set2(new int[] { 0, 0, 1, -1, 0 }, new double[] { 359.99858, 0.00014, 0.082 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 0, 0 }, new double[] { 359.99982, 0.00223, 1.000 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 1, 0 }, new double[] { 359.99961, 0.00014, 0.070 }),
            new Elp2000Set2(new int[] { 0, 1, 0, -1, 0 }, new double[] { 359.99331, 0.00009, 1.127 }),
            new Elp2000Set2(new int[] { 0, 1, 0, 0, 0 }, new double[] { 359.99537, 0.00001, 0.081 }),
            new Elp2000Set2(new int[] { 0, 1, 1, -1, 0 }, new double[] { 0.06418, 0.00003, 8.850 }),
            new Elp2000Set2(new int[] { 0, 2, -1, -1, 0 }, new double[] { 180.00095, 0.00004, 0.095 }),
            new Elp2000Set2(new int[] { 0, 2, -1, 0, 0 }, new double[] { 180.00014, 0.00003, 0.042 }),
            new Elp2000Set2(new int[] { 0, 2, 0, -3, 0 }, new double[] { 179.98126, 0.00001, 0.067 }),
            new Elp2000Set2(new int[] { 0, 2, 0, -2, 0 }, new double[] { 179.98366, 0.00025, 0.564 }),
            new Elp2000Set2(new int[] { 0, 2, 0, -1, 0 }, new double[] { 179.99638, 0.00014, 0.087 }),
            new Elp2000Set2(new int[] { 0, 2, 0, 0, -2 }, new double[] { 179.95864, 0.00003, 0.474 }),
            new Elp2000Set2(new int[] { 0, 2, 0, 0, 0 }, new double[] { 179.99904, 0.00002, 0.040 }),
            new Elp2000Set2(new int[] { 0, 2, 1, -2, 0 }, new double[] { 179.99184, 0.00002, 1.292 }),
            new Elp2000Set2(new int[] { 0, 2, 1, -1, 0 }, new double[] { 0.00313, 0.00002, 0.080 }),
            new Elp2000Set2(new int[] { 0, 2, 1, 0, 0 }, new double[] { 359.99965, 0.00002, 0.039 })
    };
    static final Elp2000Set2 Lat[] = {
            new Elp2000Set2(new int[] { 0, 0, 0, 1, -1 }, new double[] { 0.02199, 0.00003, 5.997 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 1, 0 }, new double[] { 245.99067, 0.00001, 0.075 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 1, 1 }, new double[] { 0.00530, 0.00001, 0.037 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 2, -3 }, new double[] { 0.42283, 0.00002, 0.073 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 2, -1 }, new double[] { 0.74505, 0.00001, 0.076 }),
            new Elp2000Set2(new int[] { 0, 0, 1, -1, -1 }, new double[] { 359.99982, 0.00001, 0.039 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 0, -1 }, new double[] { 359.99982, 0.00010, 0.081 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 0, 1 }, new double[] { 359.99982, 0.00010, 0.069 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 1, 1 }, new double[] { 359.99982, 0.00001, 0.036 }),
            new Elp2000Set2(new int[] { 0, 2, 0, -2, -1 }, new double[] { 179.98356, 0.00001, 0.066 }),
            new Elp2000Set2(new int[] { 0, 2, 0, -2, 1 }, new double[] { 179.98353, 0.00001, 0.086 }),
            new Elp2000Set2(new int[] { 0, 2, 0, 0, -1 }, new double[] { 179.99478, 0.00005, 0.088 })
    };
    static final Elp2000Set2 Rad[] = {
            new Elp2000Set2(new int[] { 0, 0, 0, 0, 0 }, new double[] { 90.00000, 0.00130, 99999.999 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 0, 1 }, new double[] { 213.95720, 0.00003, 0.075 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 0, 2 }, new double[] { 270.03745, 0.00002, 0.037 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 1, 0 }, new double[] { 90.07597, 0.00004, 0.075 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 3, -2 }, new double[] { 270.43429, 0.00002, 0.077 }),
            new Elp2000Set2(new int[] { 0, 0, 1, -1, 0 }, new double[] { 89.99919, 0.00013, 0.082 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 0, 0 }, new double[] { 270.00007, 0.00022, 1.000 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 1, 0 }, new double[] { 269.99903, 0.00011, 0.070 }),
            new Elp2000Set2(new int[] { 0, 2, -1, -1, 0 }, new double[] { 89.99815, 0.00002, 0.095 }),
            new Elp2000Set2(new int[] { 0, 2, -1, 0, 0 }, new double[] { 90.00052, 0.00003, 0.042 }),
            new Elp2000Set2(new int[] { 0, 2, 0, -2, 0 }, new double[] { 269.98585, 0.00005, 0.564 }),
            new Elp2000Set2(new int[] { 0, 2, 0, -1, 0 }, new double[] { 89.99863, 0.00013, 0.087 }),
            new Elp2000Set2(new int[] { 0, 2, 1, -1, 0 }, new double[] { 269.99982, 0.00002, 0.080 }),
            new Elp2000Set2(new int[] { 0, 2, 1, 0, 0 }, new double[] { 269.99982, 0.00003, 0.039 })
    };
}
