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

final class elp_plan_perturb2_rad {
	static final Elp2000Set3 Rad[] = {
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, new double[] { 270.00000,   0.02702, 99999.999}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  2}, new double[] { 270.00030,   0.00004,     0.037}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  0,  0,  1, -2}, new double[] {  89.99962,   0.00010,     0.074}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  0}, new double[] {  89.99752,   0.01035,     0.075}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  0,  0,  2,  0}, new double[] {  90.00007,   0.00053,     0.038}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  0,  0,  3,  0}, new double[] {  89.99979,   0.00003,     0.025}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  0,  1, -2,  0}, new double[] { 245.88710,   0.00007,     0.039}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  0,  1, -1,  0}, new double[] { 244.23379,   0.00126,     0.082}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  0,  1,  1,  0}, new double[] {  64.42940,   0.00112,     0.070}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  0,  1,  2,  0}, new double[] {  65.90169,   0.00006,     0.036}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  1, -1, -1,  0}, new double[] { 114.15507,   0.00003,     0.530}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  1, -1,  0,  0}, new double[] { 294.14183,   0.00025,     0.088}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  1,  0,  0,  0}, new double[] { 261.05217,   0.00002,     0.081}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  1,  1, -2,  0}, new double[] {  66.29236,   0.00003,     0.076}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  1,  1, -1,  0}, new double[] {  65.77739,   0.00008,     8.850}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  1,  1,  0,  0}, new double[] { 245.78631,   0.00147,     0.075}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  1,  1,  1,  0}, new double[] { 245.84495,   0.00008,     0.038}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2, -2,  0,  0}, new double[] { 114.06475,   0.00003,     0.044}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2, -1, -1,  0}, new double[] { 301.71251,   0.00029,     0.095}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2, -1,  0,  0}, new double[] { 105.06390,   0.00022,     0.042}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2,  0, -2,  0}, new double[] { 270.00021,   0.00022,     0.564}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2,  0, -1, -2}, new double[] {  89.99975,   0.00002,     0.065}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2,  0, -1,  0}, new double[] {  90.09472,   0.00166,     0.087}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2,  0,  0, -2}, new double[] {  89.99974,   0.00002,     0.474}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2,  0,  0,  0}, new double[] {  89.73046,   0.00057,     0.040}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2,  0,  1,  0}, new double[] {  90.00003,   0.00007,     0.026}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2,  1, -2,  0}, new double[] {  65.87675,   0.00004,     1.292}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2,  1, -1,  0}, new double[] { 246.39267,   0.00051,     0.080}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2,  1,  0,  0}, new double[] {  63.77987,   0.00010,     0.039}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  2,  2, -1,  0}, new double[] {  66.00391,   0.00023,     0.074}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  4,  0, -2,  0}, new double[] {  89.99926,   0.00002,     0.044}),
		new Elp2000Set3 (new int[] {  0,  0,  0,  0,  0,  0,  0,  4,  0, -1,  0}, new double[] {  90.00001,   0.00002,     0.028}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -3,  0,  0, -1}, new double[] { 185.13603,   0.00003,     0.020}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -3,  0,  0,  1}, new double[] { 185.13216,   0.00008,     0.044}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -3,  0,  1, -1}, new double[] {   5.13227,   0.00002,     0.028}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -3,  0,  1,  1}, new double[] { 185.13251,   0.00004,     0.106}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -3,  0,  2, -1}, new double[] {   5.13352,   0.00007,     0.043}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -2,  0,  0,  0}, new double[] { 166.99439,   0.00004,     0.042}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -2,  0,  0,  1}, new double[] {   5.13228,   0.00003,     0.097}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -2,  0,  1,  0}, new double[] { 167.07345,   0.00002,     0.095}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1, -1,  0, -1}, new double[] {   5.13666,   0.00003,     0.039}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1, -1,  0,  1}, new double[] { 185.13228,   0.00002,     0.949}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  0, -2, -1}, new double[] { 185.13470,   0.00007,     0.019}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  0, -2,  1}, new double[] {   5.13142,   0.00007,     0.041}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  0, -1, -1}, new double[] { 185.13498,   0.00066,     0.026}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  0, -1,  1}, new double[] {   5.13138,   0.00101,     0.089}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  0,  0, -1}, new double[] { 185.13603,   0.00328,     0.040}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  0,  0,  1}, new double[] {   5.13237,   0.00149,     0.487}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  0,  1, -1}, new double[] {   5.13343,   0.00648,     0.087}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  0,  1,  1}, new double[] {   5.13217,   0.00133,     0.065}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  0,  2, -1}, new double[] { 185.13414,   0.00110,     0.581}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  0,  2,  1}, new double[] {   5.13228,   0.00007,     0.035}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  0,  3, -1}, new double[] { 185.13434,   0.00010,     0.067}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  1, -1, -1}, new double[] { 185.13505,   0.00005,     0.027}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  1, -1,  1}, new double[] {   5.13142,   0.00005,     0.098}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  1,  0, -1}, new double[] { 185.13610,   0.00022,     0.042}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  1,  0,  1}, new double[] {   5.13228,   0.00010,     0.327}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  1,  1, -1}, new double[] {   5.13354,   0.00025,     0.095}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  1,  1,  1}, new double[] {   5.13228,   0.00005,     0.061}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0, -1,  1,  2, -1}, new double[] { 185.13433,   0.00004,     0.368}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  0,  0, -1,  0}, new double[] { 165.96905,   0.00002,     0.082}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  0,  0,  0, -1}, new double[] { 185.13043,   0.00014,     0.081}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  0,  0,  0,  1}, new double[] { 185.13228,   0.00012,     0.069}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  0,  0,  1, -1}, new double[] {   5.13334,   0.00003,     1.200}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  0,  0,  1,  0}, new double[] { 345.96905,   0.00002,     0.070}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1, -1, -2, -1}, new double[] {   5.13493,   0.00003,     0.036}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1, -1, -1, -1}, new double[] {   5.13544,   0.00021,     0.070}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1, -1, -1,  1}, new double[] { 185.13386,   0.00002,     0.080}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1, -1,  0, -1}, new double[] { 185.13227,   0.00004,     0.949}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1, -1,  0,  1}, new double[] { 185.13228,   0.00003,     0.039}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1, -1,  1, -1}, new double[] {   5.13404,   0.00046,     0.082}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1, -1,  2, -1}, new double[] {   5.13427,   0.00004,     0.039}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  0, -3, -1}, new double[] { 185.13456,   0.00019,     0.025}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  0, -2, -1}, new double[] { 185.13459,   0.00311,     0.038}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  0, -2,  1}, new double[] { 185.13193,   0.00082,     3.575}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  0, -1, -1}, new double[] { 185.13459,   0.05703,     0.075}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  0, -1,  1}, new double[] {   5.13190,   0.01447,     0.074}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  0,  0, -1}, new double[] { 185.12989,   0.00183,    18.600}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  0,  0,  1}, new double[] { 185.13309,   0.00035,     0.037}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  0,  1, -3}, new double[] { 185.13504,   0.00010,     0.073}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  0,  1, -1}, new double[] {   5.13453,   0.05765,     0.076}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  0,  2, -1}, new double[] {   5.13453,   0.00314,     0.038}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  0,  3, -1}, new double[] {   5.13456,   0.00019,     0.025}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  1, -2, -1}, new double[] { 185.13494,   0.00003,     0.039}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  1, -1, -1}, new double[] { 185.13559,   0.00025,     0.081}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  1, -1,  1}, new double[] {   5.13304,   0.00004,     0.069}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  1,  0, -1}, new double[] { 185.13227,   0.00004,     1.057}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  1,  0,  1}, new double[] { 185.13228,   0.00002,     0.036}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  1,  1, -1}, new double[] { 185.13402,   0.00037,     0.070}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  1,  1,  2, -1}, new double[] { 185.13428,   0.00004,     0.036}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  2,  0,  0, -1}, new double[] { 185.13319,   0.00027,     0.081}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  2,  0,  1, -1}, new double[] { 185.13387,   0.00003,     0.039}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  2,  1,  0, -1}, new double[] {   5.13229,   0.00004,     0.075}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3, -1, -2, -1}, new double[] {   5.13433,   0.00004,     0.354}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3, -1, -1, -1}, new double[] { 185.13438,   0.00015,     0.096}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3, -1,  0, -1}, new double[] {   5.13469,   0.00035,     0.042}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3, -1,  1, -1}, new double[] {   5.13457,   0.00006,     0.027}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3,  0, -3, -1}, new double[] {   5.13451,   0.00009,     0.066}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3,  0, -2, -1}, new double[] {   5.13456,   0.00091,     0.547}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3,  0, -2,  1}, new double[] {   5.13187,   0.00014,     0.040}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3,  0, -1, -1}, new double[] { 185.13456,   0.00351,     0.088}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3,  0, -1,  1}, new double[] {   5.13183,   0.00011,     0.026}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3,  0,  0, -1}, new double[] {   5.13469,   0.00503,     0.041}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3,  0,  1, -1}, new double[] {   5.13462,   0.00076,     0.026}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3,  0,  2, -1}, new double[] {   5.13470,   0.00007,     0.020}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3,  1, -1, -1}, new double[] {   5.13321,   0.00005,     0.080}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  3,  1,  0, -1}, new double[] { 185.13451,   0.00006,     0.039}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  5,  0, -2, -1}, new double[] { 185.13445,   0.00004,     0.044}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  5,  0, -1, -1}, new double[] {   5.13227,   0.00002,     0.028}),
		new Elp2000Set3 (new int[] {  0,  0,  1,  0,  0,  0,  0,  5,  0,  0, -1}, new double[] {   5.13472,   0.00004,     0.020}),
		new Elp2000Set3 (new int[] {  0,  8,-13,  0,  0,  0,  0, -2,  0,  0,  0}, new double[] { 324.68594,   0.00003,     0.040}),
		new Elp2000Set3 (new int[] {  0,  8,-13,  0,  0,  0,  0,  0,  0, -1,  0}, new double[] { 324.67709,   0.00003,     0.075}),
		new Elp2000Set3 (new int[] {  0,  8,-13,  0,  0,  0,  0,  0,  0,  1,  0}, new double[] { 144.67709,   0.00003,     0.075}),
		new Elp2000Set3 (new int[] {  0,  8,-13,  0,  0,  0,  0,  2,  0,  0,  0}, new double[] { 144.68611,   0.00003,     0.040})
	};
}
