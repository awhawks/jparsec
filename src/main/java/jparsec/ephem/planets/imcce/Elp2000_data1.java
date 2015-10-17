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

final class elp_lon_sine_0 {
    // First 400 terms
    static final Elp2000Set1[] LonSine0 = (Elp2000Set1[]) Serialise.deserialise("elp_lon_sine_0.LonSine0.xml");
}

final class elp_lon_sine_1 {
    // Next 400 terms
    static final Elp2000Set1[] LonSine1 = (Elp2000Set1[]) Serialise.deserialise("elp_lon_sine_1.LonSine1.xml");
}

final class elp_lon_sine_2 {
    // Last 223 terms
    static final Elp2000Set1[] LonSine2 = (Elp2000Set1[]) Serialise.deserialise("elp_lon_sine_2.LonSine2.xml");
}

final class elp_lat_sine_0 {
    static final Elp2000Set1[] LatSine0 = (Elp2000Set1[]) Serialise.deserialise("elp_lat_sine_0.LatSine0.xml");
}

final class elp_lat_sine_1 {
    static final Elp2000Set1[] LatSine1 = (Elp2000Set1[]) Serialise.deserialise("elp_lat_sine_1.LatSine1.xml");
}

final class elp_lat_sine_2 {
    static final Elp2000Set1[] LatSine2 = (Elp2000Set1[]) Serialise.deserialise("elp_lon_sine_2.LonSine2.xml");
}

final class elp_rad_cose_0 {
    // First 400 terms
    static final Elp2000Set1[] RadCose0 = (Elp2000Set1[]) Serialise.deserialise("elp_rad_cose_0.RadCose0.xml");
}

final class elp_rad_cose_1 {
    // Last terms
    static final Elp2000Set1[] RadCose1 = (Elp2000Set1[]) Serialise.deserialise("elp_rad_cose_1.RadCose1.xml");
}

final class elp_lon_earth_perturb {
    static final Elp2000Set2[] Lon = (Elp2000Set2[]) Serialise.deserialise("elp_lon_earth_perturb.Lon.xml");
}

final class elp_lat_earth_perturb {
    static final Elp2000Set2[] Lat = (Elp2000Set2[]) Serialise.deserialise("elp_lat_earth_perturb.Lat.xml");
}

final class elp_rad_earth_perturb {
    static final Elp2000Set2[] Rad = (Elp2000Set2[]) Serialise.deserialise("elp_rad_earth_perturb.Rad.xml");
}

final class elp_earth_perturb_t {
    static final Elp2000Set2[] Lon = (Elp2000Set2[]) Serialise.deserialise("elp_earth_perturb_t.Lon.xml");
    static final Elp2000Set2[] Lat = (Elp2000Set2[]) Serialise.deserialise("elp_earth_perturb_t.Lat.xml");
    static final Elp2000Set2[] Rad = (Elp2000Set2[]) Serialise.deserialise("elp_earth_perturb_t.Rad.xml");
}

final class elp_tidal {
    static final Elp2000Set2[] Lon = (Elp2000Set2[]) Serialise.deserialise("elp_tidal.Lon.xml");
    static final Elp2000Set2[] Lat = (Elp2000Set2[]) Serialise.deserialise("elp_tidal.Lat.xml");
    static final Elp2000Set2[] Rad = (Elp2000Set2[]) Serialise.deserialise("elp_tidal.Rad.xml");
    static final Elp2000Set2[] Lon_t = (Elp2000Set2[]) Serialise.deserialise("elp_tidal.Lon_t.xml");
    static final Elp2000Set2[] Lat_t = (Elp2000Set2[]) Serialise.deserialise("elp_tidal.Lat_t.xml");
    static final Elp2000Set2[] Rad_t = (Elp2000Set2[]) Serialise.deserialise("elp_tidal.Rad_t.xml");
}

final class elp_moon {
    static final Elp2000Set2[] Lon = (Elp2000Set2[]) Serialise.deserialise("elp_moon.Lon.xml");
    static final Elp2000Set2[] Lat = (Elp2000Set2[]) Serialise.deserialise("elp_moon.Lat.xml");
    static final Elp2000Set2[] Rad = (Elp2000Set2[]) Serialise.deserialise("elp_moon.Rad.xml");
}

final class elp_rel {
    static final Elp2000Set2[] Lon = (Elp2000Set2[]) Serialise.deserialise("elp_rel.Lon.xml");
    static final Elp2000Set2[] Lat = (Elp2000Set2[]) Serialise.deserialise("elp_rel.Lat.xml");
    static final Elp2000Set2[] Rad = (Elp2000Set2[]) Serialise.deserialise("elp_rel.Rad.xml");
}

final class elp_plan {
    static final Elp2000Set2[] Lon = (Elp2000Set2[]) Serialise.deserialise("elp_plan.Lon.xml");
    static final Elp2000Set2[] Lat = (Elp2000Set2[]) Serialise.deserialise("elp_plan.Lat.xml");
    static final Elp2000Set2[] Rad = (Elp2000Set2[]) Serialise.deserialise("elp_plan.Rad.xml");
}

final class elp_plan_perturb2 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb2.Lon.xml");
    static final Elp2000Set3[] Lat = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb2.Lat.xml");
    static final Elp2000Set3[] Rad = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb2.Rad.xml");
    static final Elp2000Set3[] Lon_t = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb2.Lon_t.xml");
    static final Elp2000Set3[] Lat_t = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb2.Lat_t.xml");
    static final Elp2000Set3[] Rad_t = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb2.Rad_t.xml");
}

final class elp_plan_perturb10_0 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_0.Lon.xml");
}

final class elp_plan_perturb10_1 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_1.Lon.xml");
}

final class elp_plan_perturb10_2 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_2.Lon.xml");
}

final class elp_plan_perturb10_3 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_3.Lon.xml");
}

final class elp_plan_perturb10_4 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_4.Lon.xml");
}

final class elp_plan_perturb10_5 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_5.Lon.xml");
}

final class elp_plan_perturb10_6 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_6.Lon.xml");
}

final class elp_plan_perturb10_7 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_7.Lon.xml");
}

final class elp_plan_perturb10_8 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_8.Lon.xml");
}

final class elp_plan_perturb10_9 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_9.Lon.xml");
}

final class elp_plan_perturb10_10 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_10.Lon.xml");
}

final class elp_plan_perturb10_11 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_11.Lon.xml");
}

final class elp_plan_perturb10_12 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_12.Lon.xml");
}

final class elp_plan_perturb10_13 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_13.Lon.xml");
}

final class elp_plan_perturb10_14 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_14.Lon.xml");
}

final class elp_plan_perturb10_15 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_15.Lon.xml");
}

final class elp_plan_perturb10_16 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_16.Lon.xml");
}

final class elp_plan_perturb10_17 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_17.Lon.xml");
}

final class elp_plan_perturb10_18 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_18.Lon.xml");
}

final class elp_plan_perturb10_19 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_19.Lon.xml");
}

final class elp_plan_perturb10_20 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_20.Lon.xml");
}

final class elp_plan_perturb10_21 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_21.Lon.xml");
}

final class elp_plan_perturb10_22 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_22.Lon.xml");
}

final class elp_plan_perturb10_23 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_23.Lon.xml");
}

final class elp_plan_perturb10_24 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_24.Lon.xml");
}

final class elp_plan_perturb10_25 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_25.Lon.xml");
}

final class elp_plan_perturb10_26 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_26.Lon.xml");
}

final class elp_plan_perturb10_27 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_27.Lon.xml");
}

final class elp_plan_perturb10_28 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_28.Lon.xml");
}

final class elp_plan_perturb10_29 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_29.Lon.xml");
}

final class elp_plan_perturb10_30 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_30.Lon.xml");
}

final class elp_plan_perturb10_31 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_31.Lon.xml");
}

final class elp_plan_perturb10_32 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_32.Lon.xml");
}

final class elp_plan_perturb10_33 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_33.Lon.xml");
}

final class elp_plan_perturb10_34 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_34.Lon.xml");
}

final class elp_plan_perturb10_35 {
    static final Elp2000Set3[] Lon = (Elp2000Set3[]) Serialise.deserialise("elp_plan_perturb10_35.Lon.xml");
}
