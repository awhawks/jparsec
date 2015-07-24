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

final class elp_plan_perturb14_2 {
    static final Elp2000Set3 Lat[] = {
            new Elp2000Set3(new int[] { 0, 18, -16, 0, 0, 0, 0, 0, 0, -1, 1 }, new double[] { 114.62374, 0.01123, 0.074 }),
            new Elp2000Set3(new int[] { 0, 18, -16, 0, 0, 0, 0, 0, 0, -1, 3 }, new double[] { 294.56550, 0.00001, 0.025 }),
            new Elp2000Set3(new int[] { 0, 18, -16, 0, 0, 0, 0, 0, 0, 0, -1 }, new double[] { 290.17387, 0.00003, 6.132 }),
            new Elp2000Set3(new int[] { 0, 18, -16, 0, 0, 0, 0, 0, 0, 0, 1 }, new double[] { 114.67748, 0.00121, 0.037 }),
            new Elp2000Set3(new int[] { 0, 18, -16, 0, 0, 0, 0, 0, 0, 1, -1 }, new double[] { 114.56550, 0.00002, 0.076 }),
            new Elp2000Set3(new int[] { 0, 18, -16, 0, 0, 0, 0, 0, 0, 1, 1 }, new double[] { 114.56550, 0.00011, 0.025 }),
            new Elp2000Set3(new int[] { 0, 18, -16, 0, 0, 0, 0, 0, 2, -2, 1 }, new double[] { 114.56550, 0.00025, 0.040 }),
            new Elp2000Set3(new int[] { 0, 18, -16, 0, 0, 0, 0, 0, 2, -1, -1 }, new double[] { 114.56550, 0.00038, 0.088 }),
            new Elp2000Set3(new int[] { 0, 18, -16, 0, 0, 0, 0, 0, 2, -1, 1 }, new double[] { 114.56550, 0.00022, 0.026 }),
            new Elp2000Set3(new int[] { 0, 18, -16, 0, 0, 0, 0, 0, 2, 0, -1 }, new double[] { 114.56550, 0.00004, 0.041 }),
            new Elp2000Set3(new int[] { 0, 18, -16, 0, 0, 0, 0, 0, 2, 0, 1 }, new double[] { 114.56550, 0.00004, 0.019 }),
            new Elp2000Set3(new int[] { 0, 18, -15, 0, 0, 0, 0, 0, -2, -1, -1 }, new double[] { 11.62857, 0.00001, 0.027 }),
            new Elp2000Set3(new int[] { 0, 18, -15, 0, 0, 0, 0, 0, -2, -1, 1 }, new double[] { 11.62857, 0.00002, 0.097 }),
            new Elp2000Set3(new int[] { 0, 18, -15, 0, 0, 0, 0, 0, -2, 0, -1 }, new double[] { 11.62857, 0.00001, 0.042 }),
            new Elp2000Set3(new int[] { 0, 19, -20, 0, 0, 0, 0, 0, -1, 1, 0 }, new double[] { 201.64074, 0.00002, 0.085 }),
            new Elp2000Set3(new int[] { 0, 20, -21, 0, 0, 0, 0, 0, -2, 1, -1 }, new double[] { 18.18419, 0.00005, 0.075 }),
            new Elp2000Set3(new int[] { 0, 20, -21, 0, 0, 0, 0, 0, -2, 1, 1 }, new double[] { 18.18419, 0.00005, 0.074 }),
            new Elp2000Set3(new int[] { 0, 20, -21, 0, 0, 0, 0, 0, -1, 1, 0 }, new double[] { 201.60861, 0.00003, 0.081 }),
            new Elp2000Set3(new int[] { 0, 20, -20, 0, 0, 0, 0, 0, -1, 1, -2 }, new double[] { 296.75407, 0.00003, 0.074 }),
            new Elp2000Set3(new int[] { 0, 20, -20, 0, 0, 0, 0, 0, -1, 1, 0 }, new double[] { 317.44238, 0.00005, 0.075 }),
            new Elp2000Set3(new int[] { 0, 20, -19, 0, 0, 0, 0, 0, -1, 1, -2 }, new double[] { 201.05224, 0.00002, 0.080 }),
            new Elp2000Set3(new int[] { 0, 20, -19, 0, 0, 0, 0, 0, -1, 1, 0 }, new double[] { 201.05224, 0.00002, 0.069 }),
            new Elp2000Set3(new int[] { 0, 21, -22, 0, 0, 0, 0, 0, -1, -1, 0 }, new double[] { 48.15158, 0.00002, 0.074 }),
            new Elp2000Set3(new int[] { 0, 21, -22, 0, 0, 0, 0, 0, -1, -1, 2 }, new double[] { 228.15158, 0.00002, 0.075 }),
            new Elp2000Set3(new int[] { 0, 21, -20, 0, 0, 0, 0, 0, 1, -1, -2 }, new double[] { 288.90331, 0.00002, 0.074 }),
            new Elp2000Set3(new int[] { 0, 21, -20, 0, 0, 0, 0, 0, 1, -1, 0 }, new double[] { 288.90331, 0.00002, 0.075 }),
            new Elp2000Set3(new int[] { 0, 22, -23, 0, 0, 0, 0, 0, -1, 1, 0 }, new double[] { 83.05036, 0.00002, 0.073 }),
            new Elp2000Set3(new int[] { 0, 23, -23, 0, 0, 0, 0, 0, 1, -1, -2 }, new double[] { 267.56110, 0.00002, 0.075 }),
            new Elp2000Set3(new int[] { 0, 23, -23, 0, 0, 0, 0, 0, 1, -1, 0 }, new double[] { 87.56110, 0.00002, 0.074 }),
            new Elp2000Set3(new int[] { 0, 26, -29, 0, 0, 0, 0, 0, 0, -1, -1 }, new double[] { 238.39622, 0.00004, 0.075 }),
            new Elp2000Set3(new int[] { 0, 26, -29, 0, 0, 0, 0, 0, 0, -1, 1 }, new double[] { 238.39622, 0.00004, 0.074 }),
            new Elp2000Set3(new int[] { 3, 0, -1, 0, 0, 0, 0, 0, -2, 1, -1 }, new double[] { 218.71494, 0.00001, 0.074 }),
            new Elp2000Set3(new int[] { 3, 0, -1, 0, 0, 0, 0, 0, -2, 1, 1 }, new double[] { 218.71494, 0.00001, 0.075 })
    };
}
