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

final class elp_lat_sine_2 {
    static final Elp2000Set1 LatSine2[] = {
            new Elp2000Set1(new int[] { 6, -1, 3, -1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, -1, 3, 1 }, new double[] { 0.00001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -4, -1 }, new double[] { 0.00014, 0.00, 0.00, 0.01, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -4, 1 }, new double[] { 0.00072, 0.05, 0.02, 0.05, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -4, 3 }, new double[] { -0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -3, -1 }, new double[] { 0.00702, 0.40, 0.15, 0.38, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -3, 1 }, new double[] { 0.03118, 1.77, 0.68, 1.71, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -3, 3 }, new double[] { -0.00014, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -2, -3 }, new double[] { 0.00007, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -2, -1 }, new double[] { 0.08096, 4.28, 1.79, 3.00, -0.04, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -2, 1 }, new double[] { 0.05963, 3.97, 1.28, 2.46, -0.03, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -2, 3 }, new double[] { -0.00021, -0.01, -0.01, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -1, -3 }, new double[] { 0.00107, 0.04, 0.07, 0.02, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -1, -1 }, new double[] { 0.09403, 5.84, 2.08, 2.28, -0.07, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -1, 1 }, new double[] { 0.04217, 3.17, 0.89, 1.37, -0.03, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, -1, 3 }, new double[] { -0.00014, -0.01, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 0, -3 }, new double[] { 0.00060, 0.04, 0.04, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 0, -1 }, new double[] { 0.03674, 2.54, 0.80, 0.73, -0.03, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 0, 1 }, new double[] { 0.01465, 1.19, 0.31, 0.47, -0.01, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 0, 3 }, new double[] { -0.00005, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 1, -3 }, new double[] { -0.00005, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 1, -1 }, new double[] { 0.00654, 0.46, 0.14, 0.22, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 1, 1 }, new double[] { 0.00305, 0.25, 0.06, 0.13, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 1, 3 }, new double[] { -0.00001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 2, -3 }, new double[] { -0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 2, -1 }, new double[] { 0.00088, 0.06, 0.02, 0.04, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 2, 1 }, new double[] { 0.00048, 0.04, 0.01, 0.03, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 3, -1 }, new double[] { 0.00010, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 3, 1 }, new double[] { 0.00006, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 0, 4, -1 }, new double[] { 0.00001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, -4, 1 }, new double[] { 0.00003, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, -3, -1 }, new double[] { -0.00003, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, -3, 1 }, new double[] { -0.00054, 0.00, -0.01, -0.03, -0.03, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, -2, -1 }, new double[] { -0.00256, -0.08, -0.06, -0.09, -0.15, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, -2, 1 }, new double[] { -0.00137, -0.05, -0.03, -0.06, -0.08, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, -1, -3 }, new double[] { -0.00007, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, -1, -1 }, new double[] { -0.00350, -0.19, -0.08, -0.08, -0.21, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, -1, 1 }, new double[] { -0.00116, -0.07, -0.02, -0.04, -0.07, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, 0, -3 }, new double[] { -0.00004, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, 0, -1 }, new double[] { -0.00153, -0.10, -0.03, -0.03, -0.09, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, 0, 1 }, new double[] { -0.00047, -0.04, 0.00, -0.02, -0.03, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, 1, -1 }, new double[] { -0.00031, -0.02, 0.00, 0.00, -0.02, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, 1, 1 }, new double[] { -0.00011, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, 2, -1 }, new double[] { -0.00005, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 1, 2, 1 }, new double[] { -0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 2, -3, -1 }, new double[] { -0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 2, -3, 1 }, new double[] { -0.00005, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 2, -2, -1 }, new double[] { -0.00009, 0.00, 0.00, 0.00, -0.01, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 2, -2, 1 }, new double[] { -0.00006, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 2, -1, -1 }, new double[] { -0.00005, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 6, 2, -1, 1 }, new double[] { -0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 7, -1, -3, -1 }, new double[] { -0.00001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 7, -1, -3, 1 }, new double[] { -0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 7, -1, -2, -1 }, new double[] { -0.00004, 0.00, 0.00, 0.00, 0.00, -0.02, 0.00 }),
            new Elp2000Set1(new int[] { 7, -1, -2, 1 }, new double[] { -0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 7, -1, -1, -1 }, new double[] { -0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 7, 0, -4, 1 }, new double[] { -0.00003, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 7, 0, -3, -1 }, new double[] { -0.00009, 0.00, 0.00, 0.00, 0.00, -0.03, 0.00 }),
            new Elp2000Set1(new int[] { 7, 0, -3, 1 }, new double[] { -0.00011, 0.00, 0.00, 0.00, 0.00, -0.04, 0.00 }),
            new Elp2000Set1(new int[] { 7, 0, -2, -1 }, new double[] { -0.00024, -0.02, 0.00, 0.00, 0.00, -0.09, 0.00 }),
            new Elp2000Set1(new int[] { 7, 0, -2, 1 }, new double[] { -0.00010, 0.00, 0.00, 0.00, 0.00, -0.04, 0.00 }),
            new Elp2000Set1(new int[] { 7, 0, -1, -3 }, new double[] { -0.00001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 7, 0, -1, -1 }, new double[] { -0.00013, -0.01, 0.00, 0.00, 0.00, -0.05, 0.00 }),
            new Elp2000Set1(new int[] { 7, 0, -1, 1 }, new double[] { -0.00003, 0.00, 0.00, 0.00, 0.00, -0.01, 0.00 }),
            new Elp2000Set1(new int[] { 7, 0, 0, -1 }, new double[] { -0.00003, 0.00, 0.00, 0.00, 0.00, -0.01, 0.00 }),
            new Elp2000Set1(new int[] { 7, 1, -2, -1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 7, 1, -2, 1 }, new double[] { 0.00001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 7, 1, -1, -1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 7, 1, -1, 1 }, new double[] { 0.00001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -2, -3, -1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -2, -3, 1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -2, -2, -1 }, new double[] { 0.00004, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -2, -2, 1 }, new double[] { 0.00003, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -2, -1, -1 }, new double[] { 0.00004, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -2, -1, 1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -2, 0, -1 }, new double[] { 0.00001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, -4, -1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, -4, 1 }, new double[] { 0.00007, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, -3, -1 }, new double[] { 0.00019, 0.01, 0.00, 0.01, 0.01, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, -3, 1 }, new double[] { 0.00022, 0.02, 0.00, 0.01, 0.01, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, -2, -1 }, new double[] { 0.00038, 0.03, 0.00, 0.02, 0.02, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, -2, 1 }, new double[] { 0.00025, 0.02, 0.00, 0.01, 0.01, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, -1, -1 }, new double[] { 0.00029, 0.03, 0.00, 0.00, 0.02, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, -1, 1 }, new double[] { 0.00015, 0.02, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, 0, -1 }, new double[] { 0.00011, 0.01, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, 0, 1 }, new double[] { 0.00005, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, 1, -1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, -1, 1, 1 }, new double[] { 0.00001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, -5, 1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, -4, -1 }, new double[] { 0.00009, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, -4, 1 }, new double[] { 0.00042, 0.03, 0.00, 0.03, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, -3, -1 }, new double[] { 0.00107, 0.08, 0.02, 0.06, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, -3, 1 }, new double[] { 0.00108, 0.09, 0.02, 0.06, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, -2, -3 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, -2, -1 }, new double[] { 0.00189, 0.15, 0.04, 0.08, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, -2, 1 }, new double[] { 0.00111, 0.11, 0.02, 0.05, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, -1, -3 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, -1, -1 }, new double[] { 0.00130, 0.12, 0.03, 0.04, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, -1, 1 }, new double[] { 0.00060, 0.06, 0.01, 0.03, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, 0, -1 }, new double[] { 0.00045, 0.04, 0.00, 0.01, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, 0, 1 }, new double[] { 0.00020, 0.02, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, 1, -1 }, new double[] { 0.00009, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, 1, 1 }, new double[] { 0.00004, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 0, 2, -1 }, new double[] { 0.00001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 1, -3, -1 }, new double[] { -0.00004, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 1, -3, 1 }, new double[] { -0.00003, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 1, -2, -1 }, new double[] { -0.00008, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 1, -2, 1 }, new double[] { -0.00004, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 1, -1, -1 }, new double[] { -0.00006, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 1, -1, 1 }, new double[] { -0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 8, 1, 0, -1 }, new double[] { -0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 10, 0, -4, -1 }, new double[] { 0.00001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 10, 0, -4, 1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 10, 0, -3, -1 }, new double[] { 0.00003, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 10, 0, -3, 1 }, new double[] { 0.00003, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 10, 0, -2, -1 }, new double[] { 0.00003, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 10, 0, -2, 1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }),
            new Elp2000Set1(new int[] { 10, 0, -1, -1 }, new double[] { 0.00002, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 })
    };
}
