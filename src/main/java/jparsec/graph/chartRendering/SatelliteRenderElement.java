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

import jparsec.graph.chartRendering.Graphics.ANAGLYPH_COLOR_MODE;
import jparsec.io.FileIO;
import jparsec.observer.LocationElement;

/**
 * A class to instantiate an adequate object for rendering operations.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class SatelliteRenderElement implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public SatelliteRenderElement()	{	}

	/**
	 * Constructor only for the size of the window.
	 * 
	 * @param w Width of the window in pixels.
	 * @param h Height of the window in pixels.
	 */
	public SatelliteRenderElement(int w, int h)
	{
		height = h;
		width = w;
	}

	/**
	 * The width of the window.
	 */
	public int width = 700;

	/**
	 * The height of the window.
	 */
	public int height = 600;

	/**
	 * Set to true (default) to show the observer.
	 */
	public boolean showObserver = true;
	/**
	 * Set to true as default value to show the artificial satellite/s.
	 */
	public boolean showSatellite = true;
	/**
	 * Set to true as default value to show the Moon/natural satellites.
	 */
	public boolean showMoon = true;
	/**
	 * Set to true as default value to show the Sun.
	 */
	public boolean showSun = true;
	/**
	 * Set to true to show the orbits of the artificial satellites. Default
	 * value is false. The orbit shown will be the next orbit starting from
	 * the current position of the artificial satellite.
	 */
	public boolean showOrbits = false;
	/**
	 * Set to true as default value to show day and night sides.
	 */
	public boolean showDayAndNight = true;
	/**
	 * The color for the orbits.
	 */
	public int showOrbitsColor = 192<<24 | 192<<16 | 192<<8 | 192;
	/**
	 * The color for the natural satellites in case the planet is not the Earth.
	 */
	public int showSatellitesColor = 128<<24 | 255<<16 | 255<<8 | 255;
	/**
	 * The color for the natural satellites in case the planet is not the Earth.
	 */
	public int showObserverColor = 255<<24 | 255<<16 | 255<<8 | 255;
	/**
	 * True will show the observer in red during night. True is the default value.
	 */
	public boolean observerInRedAtNight = true;
	/**
	 * True will show Earth when rendering from other planets. True is the default value.
	 */
	public boolean showEarth = true;
	
	/**
	 * The anaglyph mode, disabled by default.
	 */
	public ANAGLYPH_COLOR_MODE anaglyphMode = ANAGLYPH_COLOR_MODE.NO_ANAGLYPH;

	/**
	 * Sets the Earth map to use. Default value is Earth's natural-like map
	 * included in JPARSEC with a zoom factor of 1.
	 */
	public PLANET_MAP planetMap = PLANET_MAP.MAP_FLAT;
	
	/** Set to true to highligh region where Moon is above horizon. Default is false. */
	public boolean highlightMoon = false;

	/**
	 * The set of options to show the Earth map.
	 */
	public static enum PLANET_MAP {
		/** Constant ID to show no map. */
		NO_MAP,
		/** Constant ID to show a flat map lat vs lon. */
		MAP_FLAT,
		/** Constant ID to show the Earth sphere in the eclipse map. */
		MAP_SPHERICAL;
		
		/** Zoom factor for the map. Default value is 1 to show the entire Earth. */
		public float zoomFactor = 1f;
		
		/**
		 * This string can be used to force a different Earth map located in another resource
		 * in the classpath, instead of the natural-like map used by JPARSEC. Default value
		 * is null to use the default map, in case you change it set this value to the
		 * path plus map name, including extension. Note this path is an url, so to use a
		 * local file you should add 'file://' at the beginning. Several other Earth maps are
		 * available in JPARSEC, you can set this variable to any of the static constants
		 * defined in this class for that.
		 */
		public String EarthMapSource = null;
		
		/**
		 * The location object controls the central position of the chart in the rendering, in
		 * Earth's geographical coordinates. Default value is null to set the center to the
		 * location of the observer. In case the map type selected is 'flat' and zoom factor
		 * is set to 1, the value set here will have no effect.
		 */
		public LocationElement centralPosition = null;
		
		/** The path to the physical Earth map in JPARSEC. */
		public static final String EARTH_MAP_PHYSICAL = FileIO.DATA_TEXTURES_DIRECTORY + "earthmap1.jpg";
		/** The path to the political Earth map in JPARSEC. */
		public static final String EARTH_MAP_POLITICAL = FileIO.DATA_TEXTURES_DIRECTORY + "earthmap2.jpg";
		/** A constant (null) to force the use of the realistic Earth map in JPARSEC. */
		public static final String EARTH_MAP_REALISTIC = null;
		
		/**
		 * True will show the grid of coordinates in intervals of 30 deg. False is the default value.
		 * For spherical maps the interval is 10 deg.
		 */
		public boolean showGrid = false;
		/**
		 * The color for the grid.
		 */
		public int showGridColor = 192<<24 | 32<<16 | 92<<8 | 92;
		
		/**
		 * Set the default values for all variables for this enum.
		 */
		public void clear() {
			showGridColor = 192<<24 | 32<<16 | 92<<8 | 92;
			showGrid = false;
			EarthMapSource = null;
			centralPosition = null;
			zoomFactor = 1.0f;
		}
	};
	
	/**
	 * To clone the object.
	 */
	public SatelliteRenderElement clone()
	{
		if (this == null) return null;
		SatelliteRenderElement out = new SatelliteRenderElement(this.width, this.height);

		out.showSatellite = this.showSatellite;
		out.showMoon = this.showMoon;
		out.showSun = this.showSun;
		out.anaglyphMode = this.anaglyphMode;
		out.showOrbitsColor = this.showOrbitsColor;
		out.showSatellitesColor = this.showSatellitesColor;
		out.showObserver = this.showObserver;
		out.showOrbits = this.showOrbits;
		out.showDayAndNight = this.showDayAndNight;
		out.showObserverColor = this.showObserverColor;
		out.observerInRedAtNight = this.observerInRedAtNight;
		out.showEarth = this.showEarth;
		out.planetMap = this.planetMap;
		out.highlightMoon = this.highlightMoon;
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
		SatelliteRenderElement ere = (SatelliteRenderElement) e;
		if (ere.showSatellite != this.showSatellite) equals = false;
		if (ere.showMoon != this.showMoon) equals = false;
		if (ere.showSun != this.showSun) equals = false;
		if (ere.height != this.height) equals = false;
		if (ere.width != this.width) equals = false;
		if (!ere.anaglyphMode.equals(this.anaglyphMode)) equals = false;
		if (ere.showObserver != this.showObserver) equals = false;
		if (ere.showOrbits != this.showOrbits) equals = false;
		if (ere.showDayAndNight != this.showDayAndNight) equals = false;
		if (ere.showOrbitsColor != this.showOrbitsColor) equals = false;
		if (ere.showSatellitesColor != this.showSatellitesColor) equals = false;
		if (ere.showObserverColor != this.showObserverColor) equals = false;
		if (ere.observerInRedAtNight != this.observerInRedAtNight) equals = false;
		if (ere.showEarth != this.showEarth) equals = false;
		if (ere.highlightMoon != this.highlightMoon) equals = false;
		if (ere.planetMap != this.planetMap) equals = false;
		
		return equals;
	}
}
