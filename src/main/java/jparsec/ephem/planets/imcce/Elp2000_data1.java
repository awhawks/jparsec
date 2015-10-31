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

import java.io.Serializable;

/**
 * All the static data of the ELP2000 theory (part 1), in a
 * separate file for managability. The whole theory is applied. This is part 1
 * (from 2) of the data.
 * <p/>
 * Library users can ignore this class.
 * <p/>
 * <I><B>Reference</B></I>
 * <p/>
 * ELP 2000-85: a semi-analytical lunar ephemeris adequate for historical times.
 * Chapront-Touze M., Chapront J. Astron. and Astrophys. 190, 342 (1988).
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @since version 1.0
 */

public final class Elp2000_data1 implements Serializable {
    public Elp2000Set1[] elp_lon_sine_0_LonSine0; // Serialise.deserialiseXML("elp_lon_sine_0.xml");
    public Elp2000Set1[] elp_lon_sine_1_LonSine1; // Serialise.deserialiseXML("elp_lon_sine_1.xml");
    public Elp2000Set1[] elp_lon_sine_2_LonSine2; // Serialise.deserialiseXML("elp_lon_sine_2.xml");
    public Elp2000Set1[] elp_lat_sine_0_LatSine0; // Serialise.deserialiseXML("elp_lat_sine_0.xml");
    public Elp2000Set1[] elp_lat_sine_1_LatSine1; // Serialise.deserialiseXML("elp_lat_sine_1.xml");
    public Elp2000Set1[] elp_lat_sine_2_LatSine2; // Serialise.deserialiseXML("elp_lon_sine_2.xml");
    public Elp2000Set1[] elp_rad_cose_0_RadCose0; // Serialise.deserialiseXML("elp_rad_cose_0.xml");
    public Elp2000Set1[] elp_rad_cose_1_RadCose1; // Serialise.deserialiseXML("elp_rad_cose_1.xml");
    public Elp2000Set2[] elp_lon_earth_perturb_Lon; // Serialise.deserialiseXML("elp_lon_earth_perturb.xml");
    public Elp2000Set2[] elp_lat_earth_perturb_Lat; // Serialise.deserialiseXML("elp_lat_earth_perturb.xml");
    public Elp2000Set2[] elp_rad_earth_perturb_Rad; // Serialise.deserialiseXML("elp_rad_earth_perturb.xml");
    public Elp2000Set2[] elp_earth_perturb_t_Lon; // Serialise.deserialiseXML("elp_earth_perturb_t.xml");
    public Elp2000Set2[] elp_earth_perturb_t_Lat; // Serialise.deserialiseXML("elp_earth_perturb_t.xml");
    public Elp2000Set2[] elp_earth_perturb_t_Rad; // Serialise.deserialiseXML("elp_earth_perturb_t.xml");
    public Elp2000Set2[] elp_tidal_Lon; // Serialise.deserialiseXML("elp_tidal.xml");
    public Elp2000Set2[] elp_tidal_Lat; // Serialise.deserialiseXML("elp_tidal.xml");
    public Elp2000Set2[] elp_tidal_Rad; // Serialise.deserialiseXML("elp_tidal.xml");
    public Elp2000Set2[] elp_tidal_Lon_t; // Serialise.deserialiseXML("elp_tidal.xml");
    public Elp2000Set2[] elp_tidal_Lat_t; // Serialise.deserialiseXML("elp_tidal.xml");
    public Elp2000Set2[] elp_tidal_Rad_t; // Serialise.deserialiseXML("elp_tidal.xml");
    public Elp2000Set2[] elp_moon_Lon; // Serialise.deserialiseXML("elp_moon.xml");
    public Elp2000Set2[] elp_moon_Lat; // Serialise.deserialiseXML("elp_moon.xml");
    public Elp2000Set2[] elp_moon_Rad; // Serialise.deserialiseXML("elp_moon.xml");
    public Elp2000Set2[] elp_rel_Lon; // Serialise.deserialiseXML("elp_rel.xml");
    public Elp2000Set2[] elp_rel_Lat; // Serialise.deserialiseXML("elp_rel.xml");
    public Elp2000Set2[] elp_rel_Rad; // Serialise.deserialiseXML("elp_rel.xml");
    public Elp2000Set2[] elp_plan_Lon; // Serialise.deserialiseXML("elp_plan.xml");
    public Elp2000Set2[] elp_plan_Lat; // Serialise.deserialiseXML("elp_plan.xml");
    public Elp2000Set2[] elp_plan_Rad; // Serialise.deserialiseXML("elp_plan.xml");
    public Elp2000Set3[] elp_plan_perturb2_Lon; // Serialise.deserialiseXML("elp_plan_perturb2.xml");
    public Elp2000Set3[] elp_plan_perturb2_Lat; // Serialise.deserialiseXML("elp_plan_perturb2.xml");
    public Elp2000Set3[] elp_plan_perturb2_Rad; // Serialise.deserialiseXML("elp_plan_perturb2.xml");
    public Elp2000Set3[] elp_plan_perturb2_Lon_t; // Serialise.deserialiseXML("elp_plan_perturb2.xml");
    public Elp2000Set3[] elp_plan_perturb2_Lat_t; // Serialise.deserialiseXML("elp_plan_perturb2.xml");
    public Elp2000Set3[] elp_plan_perturb2_Rad_t; // Serialise.deserialiseXML("elp_plan_perturb2.xml");
    public Elp2000Set3[] elp_plan_perturb10_0_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_0.xml");
    public Elp2000Set3[] elp_plan_perturb10_1_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_1.xml");
    public Elp2000Set3[] elp_plan_perturb10_2_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_2.xml");
    public Elp2000Set3[] elp_plan_perturb10_3_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_3.xml");
    public Elp2000Set3[] elp_plan_perturb10_4_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_4.xml");
    public Elp2000Set3[] elp_plan_perturb10_5_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_5.xml");
    public Elp2000Set3[] elp_plan_perturb10_6_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_6.xml");
    public Elp2000Set3[] elp_plan_perturb10_7_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_7.xml");
    public Elp2000Set3[] elp_plan_perturb10_8_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_8.xml");
    public Elp2000Set3[] elp_plan_perturb10_9_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_9.xml");
    public Elp2000Set3[] elp_plan_perturb10_10_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_10.xml");
    public Elp2000Set3[] elp_plan_perturb10_11_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_11.xml");
    public Elp2000Set3[] elp_plan_perturb10_12_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_12.xml");
    public Elp2000Set3[] elp_plan_perturb10_13_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_13.xml");
    public Elp2000Set3[] elp_plan_perturb10_14_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_14.xml");
    public Elp2000Set3[] elp_plan_perturb10_15_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_15.xml");
    public Elp2000Set3[] elp_plan_perturb10_16_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_16.xml");
    public Elp2000Set3[] elp_plan_perturb10_17_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_17.xml");
    public Elp2000Set3[] elp_plan_perturb10_18_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_18.xml");
    public Elp2000Set3[] elp_plan_perturb10_19_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_19.xml");
    public Elp2000Set3[] elp_plan_perturb10_20_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_20.xml");
    public Elp2000Set3[] elp_plan_perturb10_21_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_21.xml");
    public Elp2000Set3[] elp_plan_perturb10_22_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_22.xml");
    public Elp2000Set3[] elp_plan_perturb10_23_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_23.xml");
    public Elp2000Set3[] elp_plan_perturb10_24_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_24.xml");
    public Elp2000Set3[] elp_plan_perturb10_25_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_25.xml");
    public Elp2000Set3[] elp_plan_perturb10_26_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_26.xml");
    public Elp2000Set3[] elp_plan_perturb10_27_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_27.xml");
    public Elp2000Set3[] elp_plan_perturb10_28_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_28.xml");
    public Elp2000Set3[] elp_plan_perturb10_29_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_29.xml");
    public Elp2000Set3[] elp_plan_perturb10_30_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_30.xml");
    public Elp2000Set3[] elp_plan_perturb10_31_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_31.xml");
    public Elp2000Set3[] elp_plan_perturb10_32_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_32.xml");
    public Elp2000Set3[] elp_plan_perturb10_33_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_33.xml");
    public Elp2000Set3[] elp_plan_perturb10_34_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_34.xml");
    public Elp2000Set3[] elp_plan_perturb10_35_Lon; // Serialise.deserialiseXML("elp_plan_perturb10_35.xml");
}
