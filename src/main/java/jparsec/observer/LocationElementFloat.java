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
package jparsec.observer;

import java.io.Serializable;

import jparsec.ephem.Functions;
import jparsec.math.Constant;
import jparsec.math.FastMath;

/**
 * This is a convenience class used for passing around polar coordinates. Units
 * are radians for longitude and latitude, AU for distance. This class use
 * float maths for improved memory footprint in Android devices.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class LocationElementFloat implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public LocationElementFloat()
	{
		lonLatRad = new float[] {0.0f, 0.0f, 0.0f};
	}

	/**
	 * Explicit constructor.
	 *
	 * @param lon longitude.
	 * @param lat latitude.
	 * @param rad radius.
	 */
	public LocationElementFloat(float lon, float lat, float rad)
	{
		lonLatRad = new float[] {lon, lat, rad};
		if (lon < 0.0 || lon > Constant.TWO_PI) lonLatRad[0] = (float) Functions.normalizeRadians(lon);
	}

	/**
	 * Constructor to parse a {@linkplain LocationElement} object.
	 *
	 * @param loc The location object.
	 */
	public LocationElementFloat(LocationElement loc)
	{
		lonLatRad = new float[] {
			(float) loc.getLongitude(),
			(float) loc.getLatitude(),
			(float) loc.getRadius()
		};
	}
	
	/**
	 * Vector constructor.
	 *
	 * @param vector { lon, lat, rad }
	 */
	public LocationElementFloat(float vector[])
	{
		set(vector);
	}

	
	/**
	 * Gets the latitude.
	 *
	 * @return The latitude value of this instance.
	 */
	public float getLatitude()
	{
		return lonLatRad[1];
	}

	/**
	 * Gets the longitude.
	 *
	 * @return The longitude value of this instance.
	 */
	public float getLongitude()
	{
		return lonLatRad[0];
	}

	/**
	 * Gets the radius.
	 *
	 * @return The radius value of this instance.
	 */
	public float getRadius()
	{
		return lonLatRad[2];
	}

	/**
	 * Get all values in this instance as a vector. <P>
	 * The vector is an array of three floats, longitude, latitude, radius.
	 *
	 * @return v[0] = longitude, v[1] = latitude, v[2] = radius.
	 */
	public float[] get()
	{
		return lonLatRad;
	}

	/**
	 * Set the latitude.
	 *
	 * @param d The new latitude value.
	 */
	public void setLatitude(float d)
	{
		lonLatRad[1] = d;
	}

	/**
	 * Set the longitude.
	 *
	 * @param d The new longitude value.
	 */
	public void setLongitude(float d)
	{
		lonLatRad[0] = d;
	}

	/**
	 * Set the radius.
	 *
	 * @param d The new radius value.
	 */
	public void setRadius(float d)
	{
		lonLatRad[2] = d;
	}

	/**
	 * Set all members of this instance from a vector. <P>
	 * The vector is an array of three floats, longitude, latitude, radius, in
	 * that order.
	 *
	 * @param vector v[0] = longitude, v[1] = latitude, v[2] = radius.
	 */
	public void set(float vector[])
	{
		lonLatRad = vector.clone();
	}

	/**
	 * Set all members of this instance individually.
	 *
	 * @param lon The new longitude.
	 * @param lat The new latitude.
	 * @param rad The new radius.
	 */
	public void set(float lon, float lat, float rad)
	{
		lonLatRad = new float[] {lon, lat, rad};
	}

	private float lonLatRad[];

	/**
	 * Transforms rectangular coordinates x, y, z contained in an array to a
	 * {@linkplain LocationElementFloat}. Coordinate system is independent: equatorial, ecliptic,
	 * or any other.
	 *
	 * @param v (x, y, z) vector.
	 * @return Location object.
	 */
	public static LocationElementFloat parseRectangularCoordinates(float v[])
	{
		double lon = 0.0;
		double lat = Constant.PI_OVER_TWO;
		if (v[2] < 0.0)
			lat = -lat;
		if (v[1] != 0.0 || v[0] != 0.0)
		{
			lon = Math.atan2(v[1], v[0]);
			lat = Math.atan(v[2] / Math.sqrt(v[0] * v[0] + v[1] * v[1]));
			if (lon < 0.0) lon += Constant.TWO_PI;
		}
		double rad = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);

		LocationElementFloat loc = new LocationElementFloat((float) lon, (float) lat, (float) rad);
		return loc;
	}

	/**
	 * Transforms rectangular coordinates x, y, z contained in an array to a
	 * {@linkplain LocationElementFloat}. Coordinate system is independent: equatorial, ecliptic,
	 * or any other. This method uses {@linkplain FastMath#atan2_accurate(float, float)} method.
	 *
	 * @param v (x, y, z) vector.
	 * @return Location object.
	 */
	public static LocationElementFloat parseRectangularCoordinatesFast(float v[])
	{
		return parseRectangularCoordinatesFast(v[0], v[1], v[2]);
	}

	/**
	 * Transforms rectangular coordinates x, y, z contained in an array to a
	 * {@linkplain LocationElementFloat}. Coordinate system is independent: equatorial, ecliptic,
	 * or any other. This method uses {@linkplain FastMath#atan2_accurate(float, float)} method.
	 *
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @return Location object.
	 */
	public static LocationElementFloat parseRectangularCoordinatesFast(float x, float y, float z)
	{
		double lon = 0.0, h = 0.0;
		double lat = Constant.PI_OVER_TWO;
		if (z < 0.0)
			lat = -lat;

		if (y != 0.0 || x!= 0.0)
		{
			h = FastMath.hypot(x, y);
			lon = FastMath.atan2_accurate(y, x);
			lat = FastMath.atan2_accurate(z / h, 1.0);
		}
		double rad = Math.sqrt(h * h + z * z);

		LocationElementFloat loc = new LocationElementFloat((float) lon, (float) lat, (float) rad);
		return loc;
	}

	/**
	 * Transforms rectangular coordinates x, y, z to a {@linkplain LocationElementFloat}.
	 * Coordinate system is independent: equatorial, ecliptic, or any other.
	 *
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @return Location object.
	 */
	public static LocationElementFloat parseRectangularCoordinates(float x, float y, float z)
	{
		float v[] = new float[] { x, y, z };
		LocationElementFloat loc = LocationElementFloat.parseRectangularCoordinates(v);
		return loc;
	}

	/**
	 * Transforms a {@linkplain LocationElementFloat} into a set of rectangular coordinates x, y, z.
	 *
	 * @param loc Location object.
	 * @return Array with (x, y, z) vector.
	 */
	public static float[] parseLocationElement(LocationElementFloat loc)
	{
		double cl = Math.cos(loc.lonLatRad[1]);
		double x = loc.lonLatRad[2] * Math.cos(loc.lonLatRad[0]) * cl;
		double y = loc.lonLatRad[2] * Math.sin(loc.lonLatRad[0]) * cl;
		double z = loc.lonLatRad[2] * Math.sin(loc.lonLatRad[1]);

		return new float[] { (float) x, (float) y, (float) z };
	}

	/**
	 * Transforms a {@linkplain LocationElementFloat} into a set of rectangular coordinates x, y, z,
	 * using approximate trigonometric functions.
	 *
	 * @param loc Location object.
	 * @return Array with (x, y, z) vector.
	 */
	public static float[] parseLocationElementFast(LocationElementFloat loc)
	{
		float cl = FastMath.cosf(loc.lonLatRad[1]);
		float x = loc.lonLatRad[2] * FastMath.cosf(loc.lonLatRad[0]) * cl;
		float y = loc.lonLatRad[2] * FastMath.sinf(loc.lonLatRad[0]) * cl;
		float z = loc.lonLatRad[2] * FastMath.sinf(loc.lonLatRad[1]);

		return new float[] { x, y, z };
	}

	/**
	 * Transforms a {@linkplain LocationElementFloat} into a set of rectangular coordinates x, y, z.
	 *
	 * @return Array with (x, y, z) vector.
	 */
	public float[] getRectangularCoordinates()
	{
		return LocationElementFloat.parseLocationElement(this);
	}


	/**
	 * Obtain angular distance between two spherical coordinates.
	 *
	 * @param loc1 Location object.
	 * @param loc2 Location object.
	 * @return The distance in radians, from 0 to PI.
	 */
	public static double getAngularDistance(LocationElementFloat loc1, LocationElementFloat loc2)
	{
		LocationElementFloat cl1 = new LocationElementFloat(loc1.getLongitude(), loc1.getLatitude(), 1.0f);
		LocationElementFloat cl2 = new LocationElementFloat(loc2.getLongitude(), loc2.getLatitude(), 1.0f);

		float[] xyz1 = parseLocationElement(cl1);
		float[] xyz2 = parseLocationElement(cl2);

		double dx = xyz1[0] - xyz2[0];
		double dy = xyz1[1] - xyz2[1];
		double dz = xyz1[2] - xyz2[2];

		double r2 = dx * dx + dy * dy + dz * dz;

		return Math.acos(1.0 - r2 * 0.5);
/*
 		// Haversine formula
 		double dLat = loc1.lat - loc2.lat;
		double dLon = loc1.lon - loc2.lon;
		double a = FastMath.sin(dLat/2) * FastMath.sin(dLat/2) + FastMath.cos(loc1.lat) * FastMath.cos(loc2.lat) * FastMath.sin(dLon/2) * FastMath.sin(dLon/2);
		return 2.0 * FastMath.atan2(Math.sqrt(a), Math.sqrt(1.0-a));
*/
	}

	/**
	 * Obtain approximate angular distance between two spherical coordinates. Good performance,
	 * accuracy around 0.2 deg or better.
	 *
	 * @param loc1 Location object.
	 * @param loc2 Location object.
	 * @return The distance in radians, from 0 to PI.
	 */
	public static double getApproximateAngularDistance(LocationElementFloat loc1, LocationElementFloat loc2)
	{
		return FastMath.acos(FastMath.sin(loc2.lonLatRad[1]) * FastMath.sin(loc1.lonLatRad[1]) + FastMath.cos(loc2.lonLatRad[1]) * FastMath.cos(loc1.lonLatRad[1]) * FastMath.cos(loc2.lonLatRad[0]-loc1.lonLatRad[0]));
	}

	/**
	 * Obtain position angle between two spherical coordinates. Good performance.
	 *
	 * @param loc1 Location object.
	 * @param loc2 Location object.
	 * @return The position angle in radians.
	 */
	public static double getApproximatePositionAngle(LocationElementFloat loc1, LocationElementFloat loc2)
	{
		double al = loc1.getLongitude(), ap = loc1.getLatitude();
		double bl = loc2.getLongitude(), bp = loc2.getLatitude();
		double dl = bl - al;
		double cbp = FastMath.cos(bp);
	    double y = FastMath.sin(dl) * cbp;
	    double x = FastMath.sin(bp) * FastMath.cos(ap) - cbp * FastMath.sin(ap) * FastMath.cos(dl);
	    double pa = 0.0;
	    if (x != 0.0 || y != 0.0) pa = -FastMath.atan2_accurate(y, x);
		return pa;
	}

	/**
	 * Obtain exact position angle between two spherical coordinates. Performance will be poor.
	 *
	 * @param loc1 Location object.
	 * @param loc2 Location object.
	 * @return The position angle in radians.
	 */
	public static double getPositionAngle(LocationElementFloat loc1, LocationElementFloat loc2)
	{
		double al = loc1.getLongitude(), ap = loc1.getLatitude();
		double bl = loc2.getLongitude(), bp = loc2.getLatitude();
		double dl = bl - al;
		double cbp = Math.cos(bp);
	    double y = Math.sin(dl) * cbp;
	    double x = Math.sin(bp) * Math.cos(ap) - cbp * Math.sin(ap) * Math.cos(dl);
	    double pa = 0.0;
	    if (x != 0.0 || y != 0.0) pa = -Math.atan2(y, x);
		return pa;
	}

	/**
	 * To clone the object.
	 */
	@Override
	public LocationElementFloat clone()
	{
		LocationElementFloat loc = new LocationElementFloat(this.getLongitude(), this.getLatitude(),
				this.getRadius());
		return loc;
	}
	/**
	 * Returns true if the input object is equals to this instance.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LocationElementFloat)) return false;

		LocationElementFloat that = (LocationElementFloat) o;

		if (Double.compare(that.lonLatRad[1], lonLatRad[1]) != 0) return false;
		if (Double.compare(that.lonLatRad[0], lonLatRad[0]) != 0) return false;

		return Double.compare(that.lonLatRad[2], lonLatRad[2]) == 0;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(lonLatRad[1]);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lonLatRad[0]);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lonLatRad[2]);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 */
	@Override
	public String toString() {
		String out = "lon: "+Functions.formatDEC(this.lonLatRad[0])+", lat: "+Functions.formatDEC(this.lonLatRad[1])+", rad: "+this.lonLatRad[2];
		return out;
	}

	/**
	 * Returns a String representation of this object.
	 * @return The string.
	 */
	public String toStringAsEquatorialLocation() {
		String out = "RA: "+Functions.formatRA(this.lonLatRad[0])+", DEC: "+Functions.formatDEC(this.lonLatRad[1])+", DIST: "+this.lonLatRad[2];
		return out;
	}

	/**
	 * Moves this position a given amount in each axis.
	 * @param dlon Displacement in longitude, radians. It is a
	 * pure incremental modification.
	 * @param dlat Displacement in latitude, radians.
	 * @param dr Displacemente in distance, in units of the distance.
	 */
	public void move(double dlon, double dlat, double dr) {
		lonLatRad[0] += dlon;
		lonLatRad[1] += dlat;
		lonLatRad[2] += dr;
	}

	/**
	 * Moves this position a given amount in each axis, setting the location
	 * to a given point with some offset.
	 * @param dlon Displacement in longitude, radians. This increment is
	 * respect the sky plain, not a coordinate incremental offset.
	 * @param dlat Displacement in latitude, radians.
	 */
	public void toOffset(double dlon, double dlat) {
		double lonp = dlon / Math.cos(lonLatRad[1]);
		lonLatRad[0] += lonp;
		lonLatRad[1] += dlat;
	}
}
