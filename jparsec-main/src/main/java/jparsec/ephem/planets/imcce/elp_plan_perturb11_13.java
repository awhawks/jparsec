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
 * All the static data of the ELP2000 theory (part 1), in a separate file for manageability.
 * The whole theory is applied. This is part 1 (from 2) of the data.
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

final class elp_plan_perturb11_13 {
    static final Elp2000Set3 Lat[] = {
            new Elp2000Set3(new int[] { 3, 0, -1, 0, 0, 0, 0, 0, 0, 0, 1 }, new double[] { 96.56536, 0.00003, 0.040 }),
            new Elp2000Set3(new int[] { 3, 0, -1, 0, 0, 0, 0, 0, 0, 1, -1 }, new double[] { 96.56536, 0.00004, 0.089 }),
            new Elp2000Set3(new int[] { 3, 0, -1, 0, 0, 0, 0, 0, 0, 1, 1 }, new double[] { 96.56536, 0.00002, 0.026 }),
            new Elp2000Set3(new int[] { 3, 0, 7, -15, 0, 0, 0, 0, -2, 1, -1 }, new double[] { 353.96206, 0.00007, 0.074 }),
            new Elp2000Set3(new int[] { 3, 0, 7, -15, 0, 0, 0, 0, -2, 1, 1 }, new double[] { 353.96206, 0.00007, 0.075 }),
            new Elp2000Set3(new int[] { 3, 2, -6, 0, 0, 0, 0, 0, -4, 3, -1 }, new double[] { 239.57624, 0.00001, 0.075 }),
            new Elp2000Set3(new int[] { 3, 2, -6, 0, 0, 0, 0, 0, -4, 3, 1 }, new double[] { 239.57624, 0.00001, 0.075 }),
            new Elp2000Set3(new int[] { 4, -15, 6, 0, 0, 0, 0, 0, -2, 2, -1 }, new double[] { 19.56685, 0.00003, 0.075 }),
            new Elp2000Set3(new int[] { 4, -15, 6, 0, 0, 0, 0, 0, -2, 2, 1 }, new double[] { 19.56685, 0.00003, 0.075 }),
            new Elp2000Set3(new int[] { 4, -5, 3, 0, 0, 0, 0, 0, -2, 1, -1 }, new double[] { 30.68945, 0.00007, 0.074 }),
            new Elp2000Set3(new int[] { 4, -5, 3, 0, 0, 0, 0, 0, -2, 1, 1 }, new double[] { 30.68945, 0.00007, 0.075 }),
            new Elp2000Set3(new int[] { 4, 0, -5, 0, 0, 0, 0, 0, -2, 0, -1 }, new double[] { 73.47726, 0.00001, 0.038 }),
            new Elp2000Set3(new int[] { 4, 0, -5, 0, 0, 0, 0, 0, -2, 1, -1 }, new double[] { 73.47726, 0.00013, 0.075 }),
            new Elp2000Set3(new int[] { 4, 0, -5, 0, 0, 0, 0, 0, -2, 1, 1 }, new double[] { 73.47726, 0.00013, 0.074 }),
            new Elp2000Set3(new int[] { 4, 0, -5, 0, 0, 0, 0, 0, -2, 2, 1 }, new double[] { 73.47726, 0.00001, 0.037 }),
            new Elp2000Set3(new int[] { 4, 0, -4, 0, 0, 0, 0, 0, -1, 1, -2 }, new double[] { 24.76162, 0.00005, 0.075 }),
            new Elp2000Set3(new int[] { 4, 0, -4, 0, 0, 0, 0, 0, -1, 1, 0 }, new double[] { 24.57741, 0.00003, 0.074 }),
            new Elp2000Set3(new int[] { 4, 0, -4, 0, 0, 0, 0, 0, 0, -1, -1 }, new double[] { 183.02988, 0.00002, 0.071 }),
            new Elp2000Set3(new int[] { 4, 0, -4, 0, 0, 0, 0, 0, 0, -1, 1 }, new double[] { 183.02988, 0.00002, 0.078 }),
            new Elp2000Set3(new int[] { 4, 0, -3, 0, 0, 0, 0, 0, 0, -1, -1 }, new double[] { 99.36455, 0.00003, 0.077 }),
            new Elp2000Set3(new int[] { 4, 0, -3, 0, 0, 0, 0, 0, 0, -1, 1 }, new double[] { 99.36455, 0.00003, 0.073 }),
            new Elp2000Set3(new int[] { 4, 0, -3, 0, 0, 0, 0, 0, 0, 0, -3 }, new double[] { 100.63105, 0.00002, 0.038 }),
            new Elp2000Set3(new int[] { 4, 0, -3, 0, 0, 0, 0, 0, 0, 1, -3 }, new double[] { 100.63014, 0.00019, 0.075 }),
            new Elp2000Set3(new int[] { 4, 0, -3, 0, 0, 0, 0, 0, 0, 1, -1 }, new double[] { 100.68226, 0.00016, 0.074 }),
            new Elp2000Set3(new int[] { 4, 0, -3, 0, 0, 0, 0, 0, 0, 2, -1 }, new double[] { 100.63105, 0.00002, 0.037 }),
            new Elp2000Set3(new int[] { 5, 0, -13, 7, 0, 0, 0, 0, -2, 1, -1 }, new double[] { 303.08124, 0.00002, 0.075 }),
            new Elp2000Set3(new int[] { 5, 0, -13, 7, 0, 0, 0, 0, -2, 1, 1 }, new double[] { 303.08124, 0.00002, 0.075 }),
            new Elp2000Set3(new int[] { 6, 0, 0, 0, -2, 0, 0, 0, -2, 0, -1 }, new double[] { 231.51527, 0.00001, 0.075 }),
            new Elp2000Set3(new int[] { 6, 0, 0, 0, -2, 0, 0, 0, -2, 0, 1 }, new double[] { 231.51527, 0.00001, 0.074 }),
            new Elp2000Set3(new int[] { 6, 0, 0, 0, 0, 0, 0, 0, -2, 0, -1 }, new double[] { 235.02094, 0.00001, 0.075 }),
            new Elp2000Set3(new int[] { 6, 0, 0, 0, 0, 0, 0, 0, -2, 0, 1 }, new double[] { 235.02094, 0.00001, 0.074 }),
            new Elp2000Set3(new int[] { 7, 0, -4, 0, 0, 0, 0, 0, -2, 0, -1 }, new double[] { 138.00381, 0.00001, 0.076 }),
            new Elp2000Set3(new int[] { 7, 0, -4, 0, 0, 0, 0, 0, -2, 0, 1 }, new double[] { 138.00381, 0.00001, 0.073 })
    };
}
