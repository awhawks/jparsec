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

final class elp_rel {
    static final Elp2000Set2 Lon[] = {
            new Elp2000Set2(new int[] { 0, 0, 1, -1, 0 }, new double[] { 179.93473, 0.00006, 0.082 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 0, 0 }, new double[] { 179.98532, 0.00081, 1.000 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 1, 0 }, new double[] { 179.96323, 0.00005, 0.070 }),
            new Elp2000Set2(new int[] { 0, 1, 0, 0, 0 }, new double[] { 0.00001, 0.00013, 0.081 }),
            new Elp2000Set2(new int[] { 0, 1, 1, 0, 0 }, new double[] { 180.02282, 0.00001, 0.075 }),
            new Elp2000Set2(new int[] { 0, 2, -1, -1, 0 }, new double[] { 0.02264, 0.00002, 0.095 }),
            new Elp2000Set2(new int[] { 0, 2, 0, -1, 0 }, new double[] { 359.98826, 0.00002, 0.087 }),
            new Elp2000Set2(new int[] { 0, 2, 0, 0, 0 }, new double[] { 180.00019, 0.00055, 0.040 }),
            new Elp2000Set2(new int[] { 0, 2, 0, 1, 0 }, new double[] { 180.00017, 0.00006, 0.026 }),
            new Elp2000Set2(new int[] { 0, 2, 1, -1, 0 }, new double[] { 180.74954, 0.00001, 0.080 }),
            new Elp2000Set2(new int[] { 0, 4, 0, -1, 0 }, new double[] { 180.00035, 0.00001, 0.028 })
    };
    static final Elp2000Set2 Lat[] = {
            new Elp2000Set2(new int[] { 0, 0, 1, 0, -1 }, new double[] { 179.99803, 0.00004, 0.081 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 0, 1 }, new double[] { 179.99798, 0.00004, 0.069 }),
            new Elp2000Set2(new int[] { 0, 2, 0, 0, -1 }, new double[] { 359.99810, 0.00002, 0.088 }),
            new Elp2000Set2(new int[] { 0, 2, 0, 0, 1 }, new double[] { 180.00026, 0.00002, 0.026 })
    };
    static final Elp2000Set2 Rad[] = {
            new Elp2000Set2(new int[] { 0, 0, 0, 0, 0 }, new double[] { 270.00000, 0.00828, 99999.999 }),
            new Elp2000Set2(new int[] { 0, 0, 0, 1, 0 }, new double[] { 89.99994, 0.00043, 0.075 }),
            new Elp2000Set2(new int[] { 0, 0, 1, -1, 0 }, new double[] { 269.93292, 0.00005, 0.082 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 0, 0 }, new double[] { 270.00908, 0.00009, 1.000 }),
            new Elp2000Set2(new int[] { 0, 0, 1, 1, 0 }, new double[] { 89.95765, 0.00005, 0.070 }),
            new Elp2000Set2(new int[] { 0, 1, 0, 0, 0 }, new double[] { 270.00002, 0.00006, 0.081 }),
            new Elp2000Set2(new int[] { 0, 2, -1, 0, 0 }, new double[] { 89.97071, 0.00002, 0.042 }),
            new Elp2000Set2(new int[] { 0, 2, 0, -1, 0 }, new double[] { 269.99367, 0.00003, 0.087 }),
            new Elp2000Set2(new int[] { 0, 2, 0, 0, 0 }, new double[] { 90.00014, 0.00106, 0.040 }),
            new Elp2000Set2(new int[] { 0, 2, 0, 1, 0 }, new double[] { 90.00010, 0.00008, 0.026 })
    };
}
