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

final class elp_plan_perturb2_rad_t {
    static final Elp2000Set3 Rad_t[] = {
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, new double[] { 270.00000, 0.00149, 99999.999 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -2 }, new double[] { 90.00000, 0.00010, 0.074 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, new double[] { 270.00000, 0.00174, 0.075 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 }, new double[] { 270.00000, 0.00029, 0.038 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0 }, new double[] { 270.00000, 0.00003, 0.025 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, -4, 0 }, new double[] { 90.00000, 0.00007, 0.019 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, -3, 0 }, new double[] { 90.00000, 0.00106, 0.026 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, -2, 0 }, new double[] { 90.00000, 0.01764, 0.039 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, -2, 2 }, new double[] { 90.00000, 0.00013, 0.750 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, -2 }, new double[] { 90.00000, 0.00002, 0.026 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0 }, new double[] { 90.00000, 0.32654, 0.082 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 2 }, new double[] { 270.00000, 0.00084, 0.069 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -2 }, new double[] { 90.00000, 0.00047, 0.039 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 }, new double[] { 270.00000, 0.12302, 1.000 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2 }, new double[] { 90.00000, 0.00040, 0.036 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, -2 }, new double[] { 90.00000, 0.00062, 0.079 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 }, new double[] { 270.00000, 0.26396, 0.070 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, -2 }, new double[] { 270.00000, 0.00008, 1.500 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0 }, new double[] { 270.00000, 0.01449, 0.036 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 0 }, new double[] { 270.00000, 0.00089, 0.025 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 4, 0 }, new double[] { 270.00000, 0.00006, 0.019 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 2, -3, 0 }, new double[] { 90.00000, 0.00005, 0.026 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 2, -2, 0 }, new double[] { 90.00000, 0.00069, 0.041 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 2, -1, 0 }, new double[] { 90.00000, 0.01066, 0.089 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 2, -1, 2 }, new double[] { 270.00000, 0.00002, 0.064 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, -2 }, new double[] { 90.00000, 0.00003, 0.040 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0 }, new double[] { 270.00000, 0.00536, 0.500 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0 }, new double[] { 270.00000, 0.00587, 0.066 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0 }, new double[] { 270.00000, 0.00026, 0.035 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 3, -2, 0 }, new double[] { 90.00000, 0.00002, 0.043 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, 0 }, new double[] { 90.00000, 0.00029, 0.098 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0 }, new double[] { 270.00000, 0.00016, 0.333 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 0 }, new double[] { 270.00000, 0.00014, 0.062 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, -2, -1, 0 }, new double[] { 270.00000, 0.00004, 0.346 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, -2, 0, 0 }, new double[] { 90.00000, 0.00015, 0.096 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, -1, -1, 0 }, new double[] { 270.00000, 0.00029, 0.530 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, -2 }, new double[] { 90.00000, 0.00003, 0.065 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0 }, new double[] { 270.00000, 0.00126, 0.088 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0 }, new double[] { 270.00000, 0.00028, 0.041 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, -1, 2, 0 }, new double[] { 270.00000, 0.00003, 0.026 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 0, -2, 0 }, new double[] { 90.00000, 0.00002, 0.071 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0 }, new double[] { 90.00000, 0.00004, 1.127 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, new double[] { 270.00000, 0.00008, 0.081 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0 }, new double[] { 90.00000, 0.00002, 0.039 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 1, -3, 0 }, new double[] { 270.00000, 0.00004, 0.038 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 1, -2, 0 }, new double[] { 270.00000, 0.00084, 0.076 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 1, -1, 0 }, new double[] { 270.00000, 0.00214, 8.850 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, -2 }, new double[] { 270.00000, 0.00008, 0.074 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0 }, new double[] { 90.00000, 0.04194, 0.075 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0 }, new double[] { 90.00000, 0.00235, 0.038 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 0 }, new double[] { 90.00000, 0.00015, 0.025 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 2, -1, 0 }, new double[] { 270.00000, 0.00002, 0.898 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0 }, new double[] { 270.00000, 0.00019, 0.070 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0 }, new double[] { 270.00000, 0.00003, 0.036 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -4, -1, 0 }, new double[] { 90.00000, 0.00004, 0.134 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -4, 0, 0 }, new double[] { 90.00000, 0.00016, 0.048 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -3, -2, 0 }, new double[] { 270.00000, 0.00008, 0.209 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -3, -1, 0 }, new double[] { 90.00000, 0.00112, 0.118 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -3, 0, 0 }, new double[] { 90.00000, 0.00310, 0.046 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -3, 1, 0 }, new double[] { 90.00000, 0.00022, 0.029 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -2, -3, 0 }, new double[] { 270.00000, 0.00008, 0.059 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -2, -2, 0 }, new double[] { 270.00000, 0.00173, 0.265 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -2, -1, -2 }, new double[] { 270.00000, 0.00005, 0.058 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -2, -1, 0 }, new double[] { 90.00000, 0.02490, 0.105 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -2, 0, -2 }, new double[] { 270.00000, 0.00014, 0.243 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -2, 0, 0 }, new double[] { 90.00000, 0.04970, 0.044 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -2, 1, -2 }, new double[] { 270.00000, 0.00004, 0.109 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -2, 1, 0 }, new double[] { 90.00000, 0.00331, 0.028 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -2, 2, 0 }, new double[] { 90.00000, 0.00023, 0.020 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, -4, 0 }, new double[] { 270.00000, 0.00006, 0.034 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, -3, 0 }, new double[] { 270.00000, 0.00125, 0.062 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, -2, -2 }, new double[] { 270.00000, 0.00004, 0.034 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, -2, 0 }, new double[] { 270.00000, 0.02529, 0.360 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, -2, 2 }, new double[] { 270.00000, 0.00008, 0.042 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, -1, -2 }, new double[] { 270.00000, 0.00081, 0.061 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, -1, 0 }, new double[] { 90.00000, 0.38245, 0.095 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, -1, 2 }, new double[] { 270.00000, 0.00009, 0.027 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, 0, -2 }, new double[] { 270.00000, 0.00165, 0.322 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, 0, 0 }, new double[] { 90.00000, 0.51395, 0.042 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, 1, -2 }, new double[] { 270.00000, 0.00053, 0.099 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, 1, 0 }, new double[] { 90.00000, 0.03222, 0.027 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, 2, -2 }, new double[] { 270.00000, 0.00005, 0.043 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, 2, 0 }, new double[] { 90.00000, 0.00213, 0.020 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, -1, 3, 0 }, new double[] { 90.00000, 0.00015, 0.016 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 0, -3, 0 }, new double[] { 270.00000, 0.00002, 0.067 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 0, -2, 0 }, new double[] { 90.00000, 0.00004, 0.564 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 0, -1, 0 }, new double[] { 270.00000, 0.00633, 0.087 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, -2 }, new double[] { 90.00000, 0.00005, 0.474 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0 }, new double[] { 270.00000, 0.01356, 0.040 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, -2 }, new double[] { 90.00000, 0.00003, 0.090 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 0 }, new double[] { 270.00000, 0.00114, 0.026 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0 }, new double[] { 270.00000, 0.00009, 0.020 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 1, -4, 0 }, new double[] { 270.00000, 0.00004, 0.037 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 1, -3, 0 }, new double[] { 270.00000, 0.00043, 0.071 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 1, -2, 0 }, new double[] { 270.00000, 0.00039, 1.292 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 1, -1, -2 }, new double[] { 90.00000, 0.00012, 0.070 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 1, -1, 0 }, new double[] { 270.00000, 0.06068, 0.080 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, -2 }, new double[] { 90.00000, 0.00034, 0.903 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0 }, new double[] { 270.00000, 0.07754, 0.039 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, -2 }, new double[] { 90.00000, 0.00014, 0.082 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 0 }, new double[] { 270.00000, 0.00658, 0.026 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0 }, new double[] { 270.00000, 0.00053, 0.019 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 1, 3, 0 }, new double[] { 270.00000, 0.00004, 0.015 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 2, -3, 0 }, new double[] { 90.00000, 0.00005, 0.077 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 2, -2, 0 }, new double[] { 90.00000, 0.00055, 4.425 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 2, -1, -2 }, new double[] { 90.00000, 0.00005, 0.075 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 2, -1, 0 }, new double[] { 270.00000, 0.01186, 0.074 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0 }, new double[] { 270.00000, 0.00074, 0.037 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 0 }, new double[] { 270.00000, 0.00005, 0.025 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, -2, -1, 0 }, new double[] { 270.00000, 0.00007, 0.046 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, -2, 0, 0 }, new double[] { 90.00000, 0.00005, 0.028 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, -1, -2, 0 }, new double[] { 270.00000, 0.00013, 0.104 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, -1, -1, 0 }, new double[] { 270.00000, 0.00064, 0.044 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, -1, 0, -2 }, new double[] { 270.00000, 0.00004, 0.108 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, -1, 0, 0 }, new double[] { 90.00000, 0.00040, 0.028 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, -1, 1, 0 }, new double[] { 90.00000, 0.00002, 0.020 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, 0, -1, 0 }, new double[] { 90.00000, 0.00006, 0.042 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0 }, new double[] { 90.00000, 0.00003, 0.027 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, 1, -2, 0 }, new double[] { 270.00000, 0.00010, 0.086 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, 1, -1, 0 }, new double[] { 90.00000, 0.00053, 0.040 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, 1, 0, -2 }, new double[] { 90.00000, 0.00002, 0.089 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, 1, 0, 0 }, new double[] { 90.00000, 0.00027, 0.026 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 3, 1, 1, 0 }, new double[] { 90.00000, 0.00004, 0.019 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -3, -2, 0 }, new double[] { 90.00000, 0.00004, 0.050 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -3, -1, 0 }, new double[] { 90.00000, 0.00012, 0.030 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -3, 0, 0 }, new double[] { 90.00000, 0.00006, 0.022 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -2, -2, 0 }, new double[] { 90.00000, 0.00054, 0.048 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -2, -1, 0 }, new double[] { 90.00000, 0.00140, 0.029 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -2, 0, -2 }, new double[] { 90.00000, 0.00002, 0.048 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -2, 0, 0 }, new double[] { 90.00000, 0.00064, 0.021 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -2, 1, 0 }, new double[] { 90.00000, 0.00009, 0.016 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -1, -3, 0 }, new double[] { 90.00000, 0.00011, 0.115 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -1, -2, 0 }, new double[] { 90.00000, 0.00476, 0.046 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -1, -1, -2 }, new double[] { 90.00000, 0.00007, 0.119 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -1, -1, 0 }, new double[] { 90.00000, 0.00993, 0.028 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -1, 0, -2 }, new double[] { 90.00000, 0.00014, 0.046 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -1, 0, 0 }, new double[] { 90.00000, 0.00394, 0.021 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -1, 1, 0 }, new double[] { 90.00000, 0.00051, 0.016 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, -1, 2, 0 }, new double[] { 90.00000, 0.00005, 0.013 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 0, -2, 0 }, new double[] { 270.00000, 0.00012, 0.044 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 0, -1, 0 }, new double[] { 270.00000, 0.00038, 0.028 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0 }, new double[] { 270.00000, 0.00019, 0.020 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 0, 1, 0 }, new double[] { 270.00000, 0.00003, 0.016 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 1, -3, 0 }, new double[] { 90.00000, 0.00006, 0.093 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 1, -2, 0 }, new double[] { 270.00000, 0.00060, 0.042 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 1, -1, -2 }, new double[] { 270.00000, 0.00004, 0.096 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 1, -1, 0 }, new double[] { 270.00000, 0.00146, 0.027 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 1, 0, -2 }, new double[] { 270.00000, 0.00005, 0.042 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 1, 0, 0 }, new double[] { 270.00000, 0.00061, 0.020 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 1, 1, 0 }, new double[] { 270.00000, 0.00009, 0.016 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 2, -2, 0 }, new double[] { 270.00000, 0.00013, 0.040 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 2, -1, 0 }, new double[] { 270.00000, 0.00009, 0.026 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 6, -2, -2, 0 }, new double[] { 90.00000, 0.00003, 0.022 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 6, -2, -1, 0 }, new double[] { 90.00000, 0.00003, 0.017 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 6, -1, -3, 0 }, new double[] { 90.00000, 0.00006, 0.030 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 6, -1, -2, 0 }, new double[] { 90.00000, 0.00017, 0.021 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 6, -1, -1, 0 }, new double[] { 90.00000, 0.00013, 0.017 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 6, -1, 0, 0 }, new double[] { 90.00000, 0.00004, 0.014 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 6, 1, -2, 0 }, new double[] { 270.00000, 0.00002, 0.021 }),
            new Elp2000Set3(new int[] { 0, 0, 0, 0, 0, 0, 0, 6, 1, -1, 0 }, new double[] { 270.00000, 0.00002, 0.016 }),
            new Elp2000Set3(new int[] { 0, 0, 1, 0, 0, 0, 0, -1, 0, 0, -1 }, new double[] { 284.81311, 0.00003, 0.040 }),
            new Elp2000Set3(new int[] { 0, 0, 1, 0, 0, 0, 0, -1, 0, 1, -1 }, new double[] { 104.81311, 0.00005, 0.087 }),
            new Elp2000Set3(new int[] { 0, 0, 1, 0, 0, 0, 0, 1, 0, -2, -1 }, new double[] { 284.81311, 0.00003, 0.038 }),
            new Elp2000Set3(new int[] { 0, 0, 1, 0, 0, 0, 0, 1, 0, -1, -1 }, new double[] { 284.81311, 0.00049, 0.075 }),
            new Elp2000Set3(new int[] { 0, 0, 1, 0, 0, 0, 0, 1, 0, -1, 1 }, new double[] { 104.81311, 0.00012, 0.074 }),
            new Elp2000Set3(new int[] { 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, -1 }, new double[] { 104.81311, 0.00049, 0.076 }),
            new Elp2000Set3(new int[] { 0, 0, 1, 0, 0, 0, 0, 1, 0, 2, -1 }, new double[] { 104.81311, 0.00003, 0.038 }),
            new Elp2000Set3(new int[] { 0, 0, 1, 0, 0, 0, 0, 3, 0, -1, -1 }, new double[] { 284.81311, 0.00003, 0.088 }),
            new Elp2000Set3(new int[] { 0, 0, 1, 0, 0, 0, 0, 3, 0, 0, -1 }, new double[] { 104.81311, 0.00004, 0.041 })
    };
}
