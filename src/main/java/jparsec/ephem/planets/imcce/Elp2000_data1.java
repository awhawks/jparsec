/**
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

import java.io.Serializable;

/**
 * <p>
 * All the static data of the ELP2000 theory (part 1), in a
 * separate file for manageability. The whole theory is applied.
 * This is part 1 (of 2) of the data.</p>
 * <p>
 * Library users can ignore this class.</p>
 * <p>
 * <i><b>Reference</b></i><p/>
 * <p>
 * ELP 2000-85: a semi-analytical lunar ephemeris adequate for historical times.
 * Chapront-Touze M., Chapront J. Astron. and Astrophys. 190, 342 (1988).
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @since version 1.0
 */
public final class Elp2000_data1 implements Serializable {
    public Elp2000Set1[] elp_lon_sine_0_LonSine0;
    public Elp2000Set1[] elp_lon_sine_1_LonSine1;
    public Elp2000Set1[] elp_lon_sine_2_LonSine2;
    public Elp2000Set1[] elp_lat_sine_0_LatSine0;
    public Elp2000Set1[] elp_lat_sine_1_LatSine1;
    public Elp2000Set1[] elp_lat_sine_2_LatSine2;
    public Elp2000Set1[] elp_rad_cose_0_RadCose0;
    public Elp2000Set1[] elp_rad_cose_1_RadCose1;
    public Elp2000Set2[] elp_lon_earth_perturb_Lon;
    public Elp2000Set2[] elp_lat_earth_perturb_Lat;
    public Elp2000Set2[] elp_rad_earth_perturb_Rad;
    public Elp2000Set2[] elp_earth_perturb_t_Lon;
    public Elp2000Set2[] elp_earth_perturb_t_Lat;
    public Elp2000Set2[] elp_earth_perturb_t_Rad;
    public Elp2000Set2[] elp_tidal_Lon;
    public Elp2000Set2[] elp_tidal_Lat;
    public Elp2000Set2[] elp_tidal_Rad;
    public Elp2000Set2[] elp_tidal_Lon_t;
    public Elp2000Set2[] elp_tidal_Lat_t;
    public Elp2000Set2[] elp_tidal_Rad_t;
    public Elp2000Set2[] elp_moon_Lon;
    public Elp2000Set2[] elp_moon_Lat;
    public Elp2000Set2[] elp_moon_Rad;
    public Elp2000Set2[] elp_rel_Lon;
    public Elp2000Set2[] elp_rel_Lat;
    public Elp2000Set2[] elp_rel_Rad;
    public Elp2000Set2[] elp_plan_Lon;
    public Elp2000Set2[] elp_plan_Lat;
    public Elp2000Set2[] elp_plan_Rad;
    public Elp2000Set3[] elp_plan_perturb2_Lon;
    public Elp2000Set3[] elp_plan_perturb2_Lat;
    public Elp2000Set3[] elp_plan_perturb2_Rad;
    public Elp2000Set3[] elp_plan_perturb2_Lon_t;
    public Elp2000Set3[] elp_plan_perturb2_Lat_t;
    public Elp2000Set3[] elp_plan_perturb2_Rad_t;
    public Elp2000Set3[] elp_plan_perturb10_0_Lon;
    public Elp2000Set3[] elp_plan_perturb10_1_Lon;
    public Elp2000Set3[] elp_plan_perturb10_2_Lon;
    public Elp2000Set3[] elp_plan_perturb10_3_Lon;
    public Elp2000Set3[] elp_plan_perturb10_4_Lon;
    public Elp2000Set3[] elp_plan_perturb10_5_Lon;
    public Elp2000Set3[] elp_plan_perturb10_6_Lon;
    public Elp2000Set3[] elp_plan_perturb10_7_Lon;
    public Elp2000Set3[] elp_plan_perturb10_8_Lon;
    public Elp2000Set3[] elp_plan_perturb10_9_Lon;
    public Elp2000Set3[] elp_plan_perturb10_10_Lon;
    public Elp2000Set3[] elp_plan_perturb10_11_Lon;
    public Elp2000Set3[] elp_plan_perturb10_12_Lon;
    public Elp2000Set3[] elp_plan_perturb10_13_Lon;
    public Elp2000Set3[] elp_plan_perturb10_14_Lon;
    public Elp2000Set3[] elp_plan_perturb10_15_Lon;
    public Elp2000Set3[] elp_plan_perturb10_16_Lon;
    public Elp2000Set3[] elp_plan_perturb10_17_Lon;
    public Elp2000Set3[] elp_plan_perturb10_18_Lon;
    public Elp2000Set3[] elp_plan_perturb10_19_Lon;
    public Elp2000Set3[] elp_plan_perturb10_20_Lon;
    public Elp2000Set3[] elp_plan_perturb10_21_Lon;
    public Elp2000Set3[] elp_plan_perturb10_22_Lon;
    public Elp2000Set3[] elp_plan_perturb10_23_Lon;
    public Elp2000Set3[] elp_plan_perturb10_24_Lon;
    public Elp2000Set3[] elp_plan_perturb10_25_Lon;
    public Elp2000Set3[] elp_plan_perturb10_26_Lon;
    public Elp2000Set3[] elp_plan_perturb10_27_Lon;
    public Elp2000Set3[] elp_plan_perturb10_28_Lon;
    public Elp2000Set3[] elp_plan_perturb10_29_Lon;
    public Elp2000Set3[] elp_plan_perturb10_30_Lon;
    public Elp2000Set3[] elp_plan_perturb10_31_Lon;
    public Elp2000Set3[] elp_plan_perturb10_32_Lon;
    public Elp2000Set3[] elp_plan_perturb10_33_Lon;
    public Elp2000Set3[] elp_plan_perturb10_34_Lon;
    public Elp2000Set3[] elp_plan_perturb10_35_Lon;
}
