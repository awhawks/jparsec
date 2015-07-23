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
package jparsec.graph.chartRendering;

import java.io.Serializable;

import jparsec.astronomy.TelescopeElement;
import jparsec.ephem.Target.TARGET;
import jparsec.ephem.moons.MoonEphemElement;
import jparsec.ephem.planets.EphemElement;
import jparsec.graph.chartRendering.Graphics.ANAGLYPH_COLOR_MODE;

/**
 * A class to instantiate an adequate object for rendering operations.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class PlanetRenderElement implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Empty constructor.
	 */
	public PlanetRenderElement() {	}

	/**
	 * Explicit full constructor.
	 * 
	 * @param w Width of the window in pixels.
	 * @param h Height of the window in pixels.
	 * @param a True for viewing rotation axis.
	 * @param anose True for viewing N-O-S-E axis.
	 * @param t True for viewing textures.
	 * @param s True for viewing main natural satellites.
	 * @param sa True for viewing all available natural satellites.
	 * @param nu True for viewing North direction upwards.
	 * @param d True for simulating the object as seen through the
	 *        telescope.
	 */
	public PlanetRenderElement(int w, int h, boolean a, boolean anose, boolean t, boolean s,
			boolean sa, boolean nu, boolean d)
	{
		axes = a;
		axesNOSE = anose;
		textures = t;
		height = h;
		width = w;
		northUp = nu;
		satellitesMain = s;
		satellitesAll = sa;
		difraction = d;
	}

	/**
	 * Explicit constructor.
	 * 
	 * @param w Width of the window in pixels.
	 * @param h Height of the window in pixels.
	 * @param a True for viewing axes.
	 * @param t True for viewing textures.
	 * @param s True for viewing main natural satellites.
	 * @param sa True for viewing all available natural satellites.
	 * @param nu True for viewing North direction upwards.
	 * @param d True for simulating the object as seen through the
	 *        telescope.
	 */
	public PlanetRenderElement(int w, int h, boolean a, boolean t, boolean s,
			boolean sa, boolean nu, boolean d)
	{
		axes = a;
		axesNOSE = a;
		textures = t;
		height = h;
		width = w;
		northUp = nu;
		satellitesMain = s;
		satellitesAll = sa;
		difraction = d;
	}

	/**
	 * Explicit constructor.
	 * 
	 * @param w Width of the window in pixels.
	 * @param h Height of the window in pixels.
	 * @param a True for viewing axes.
	 * @param t True for viewing textures.
	 * @param s True for viewing main natural satellites.
	 * @param nu True for viewing North direction upwards.
	 * @param d True for simulating the object as seen through the
	 *        telescope.
	 */
	public PlanetRenderElement(int w, int h, boolean a, boolean t, boolean s,
			boolean nu, boolean d)
	{
		axes = a;
		axesNOSE = a;
		textures = t;
		height = h;
		width = w;
		northUp = nu;
		satellitesMain = s;
		difraction = d;
	}

	/**
	 * Constructor to be used with an sky render object.
	 * 
	 * @param a True for viewing axes.
	 * @param t True for viewing textures.
	 * @param s True for viewing main natural satellites.
	 * @param d True for simulating the object as seen through the
	 *        telescope.
	 */
	public PlanetRenderElement(boolean a, boolean t, boolean s, boolean d)
	{
		axes = a;
		axesNOSE = a;
		textures = t;
		satellitesMain = s;
		difraction = d;
	}

	/**
	 * Constructor to be used with an sky render object.
	 * 
	 * @param a True for viewing axes.
	 * @param t True for viewing textures.
	 * @param s True for viewing main natural satellites.
	 * @param sa True for viewing all available natural satellites.
	 * @param d True for simulating the object as seen through the
	 *        telescope.
	 */
	public PlanetRenderElement(boolean a, boolean t, boolean s, boolean sa,
			boolean d)
	{
		axes = a;
		axesNOSE = a;
		textures = t;
		satellitesMain = s;
		satellitesAll = sa;
		difraction = d;
	}

	/**
	 * Constructor only for the size of the window.
	 * 
	 * @param w Width of the window in pixels.
	 * @param h Height of the window in pixels.
	 */
	public PlanetRenderElement(int w, int h)
	{
		height = h;
		width = w;
	}

	/**
	 * An ephem object defining the ephemeris of the object.
	 */
	public EphemElement ephem;

	/**
	 * A telescope object with the telescope characteristics.
	 */
	public TelescopeElement telescope;

	/**
	 * A moon ephem object defining the ephemeris of the satellites around
	 * target. Used internally.
	 */
	public MoonEphemElement[] moonephem;

	/**
	 * An ephem object defining the ephemeris of the Sun. Used internally.
	 */
	public EphemElement ephemSun;

	/**
	 * The width of the window.
	 */
	public int width = 700;

	/**
	 * The height of the window.
	 */
	public int height = 600;

	/**
	 * Sets whether to paint textures or not.
	 */
	public boolean textures = true;

	/**
	 * Sets whether to paint axes or not.
	 */
	public boolean axes = true;

	/**
	 * Sets whether to paint N-O-S-E axes or not.
	 */
	public boolean axesNOSE = true;

	/**
	 * Sets whether to put North up or not.
	 */
	public boolean northUp = false;

	/**
	 * Sets whether to render satellites or not (only main ones unless you
	 * select also to show all satellites).
	 */
	public boolean satellitesMain = true;

	/**
	 * Sets the high quality flag for planetary rendering. In this mode the rendering
	 * is done for twice the size of the planet in pixels, and later the image is
	 * resampled to half of its resolution. The result is an extremely realistic rendering,
	 * but requires much more time. Default value is false.
	 */
	public boolean highQuality = false;
	
	/**
	 * Sets whether to render also all available satellites or not (instead of
	 * only main ones). Only when both main satellites and all ones are enabled,
	 * then all satellites will be displayed. Note that the algorithm used is
	 * different for all satellites, providing in this case slightly less
	 * accurate results. If you want to show any satellite (even the main ones)
	 * for Neptune and Pluto this is the only way to do that, since no
	 * particular theory exists for the movement of these satellites. So for
	 * these two bodies no satellite will be displayed if you don't set main and
	 * all satellites to true.
	 */
	public boolean satellitesAll = false;

	/**
	 * Sets whether to render the difraction effects of the telescope, if
	 * textures are set to true. Not supported in Android.
	 */
	public boolean difraction = false;

	/**
	 * Sets the target body.
	 */
	public TARGET target = TARGET.SUN;
	
	/**
	 * Set to true as default value to show labels below the satellites. 
	 */
	public boolean showLabels = true;

	/**
	 * Holds background color, black as default.
	 */
	public int background = 255<<24 | 0<<16 | 0<<8 | 0;
	/**
	 * Holds foreground color (satellites), white as default.
	 */
	public int foreground = 255<<24 | 255<<16 | 255<<8 | 255;
	
	/**
	 * The anaglyph mode, disabled by default.
	 */
	public ANAGLYPH_COLOR_MODE anaglyphMode = ANAGLYPH_COLOR_MODE.NO_ANAGLYPH;

	/**
	 * To clone the object.
	 */
	public PlanetRenderElement clone()
	{
		if (this == null) return null;
		PlanetRenderElement out = new PlanetRenderElement(this.width, this.height, this.axes, this.textures,
				this.satellitesMain, this.satellitesAll, this.northUp, this.difraction);

		if (ephem != null) out.ephem = this.ephem.clone();
		if (ephemSun != null) out.ephemSun = this.ephemSun.clone();
		if (moonephem != null) {
			out.moonephem = new MoonEphemElement[moonephem.length];
			for (int i=0; i<moonephem.length; i++) {
				if (moonephem[i] != null) out.moonephem[i] = this.moonephem[i].clone();
			}
		}
		out.target = this.target;
		if (telescope != null) out.telescope = this.telescope.clone();
		out.showLabels = this.showLabels;
		out.background = this.background;
		out.foreground = this.foreground;
		out.axesNOSE = this.axesNOSE;
		out.anaglyphMode = this.anaglyphMode;
		out.highQuality = this.highQuality;
		return out;
	}
	/**
	 * Returns true if the input object is equal to this instance.
	 */
	public boolean equals(Object e)
	{
		if (e == null) {
			if (this == null) return true;
			return false;
		}
		if (this == null) {
			return false;
		}
		boolean equals = true;
		PlanetRenderElement ere = (PlanetRenderElement) e;
		if (ere.showLabels != this.showLabels) equals = false;
		if (ere.axes != this.axes) equals = false;
		if (ere.axesNOSE != this.axesNOSE) equals = false;
		if (ere.textures != this.textures) equals = false;
		if (ere.height != this.height) equals = false;
		if (ere.width != this.width) equals = false;
		if (ere.satellitesMain != this.satellitesMain) equals = false;
		if (ere.satellitesAll != this.satellitesAll) equals = false;
		if (ere.difraction != this.difraction) equals = false;
		if (ere.northUp != this.northUp) equals = false;
		if (ere.target != this.target) equals = false;
		if (!ere.telescope.equals(this.telescope)) equals = false;
		if (!ere.ephemSun.equals(this.ephemSun)) equals = false;
		if (!ere.ephem.equals(this.ephem)) equals = false;
		if (ere.background != this.background) equals = false;
		if (ere.foreground != this.foreground) equals = false;
		if (ere.anaglyphMode != this.anaglyphMode) equals = false;
		if (ere.highQuality != this.highQuality) equals = false;

		if (moonephem == null || ere.moonephem == null) {
			if (ere.moonephem != null || moonephem != null) equals = false; 
		} else {
			if (this.moonephem.length == ere.moonephem.length)
			{
				for (int i=0; i<this.moonephem.length; i++)
				{
					if (this.moonephem[i] == null) {
						if (ere.moonephem[i] != null) equals = false;
					} else {
						if (!this.moonephem[i].equals(ere.moonephem[i])) equals = false;
					}
				}
			} else {
				equals = false;
			}
		}

		return equals;
	}
}
