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
import java.util.ArrayList;

import jparsec.astronomy.Constellation;
import jparsec.astronomy.CoordinateSystem;
import jparsec.astronomy.TelescopeElement;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Obliquity;
import jparsec.ephem.Precession;
import jparsec.ephem.EphemerisElement.REDUCTION_METHOD;
import jparsec.graph.DataSet;
import jparsec.graph.JPARSECStroke;
import jparsec.graph.chartRendering.Graphics.ANAGLYPH_COLOR_MODE;
import jparsec.io.FileFormatElement;
import jparsec.io.ReadFile;
import jparsec.io.ReadFormat;
import jparsec.math.Constant;
import jparsec.observer.LocationElement;
import jparsec.time.AstroDate;
import jparsec.util.DataBase;
import jparsec.util.JPARSECException;
import jparsec.util.Translate;
import jparsec.util.Translate.LANGUAGE;

/**
 * A class to instantiate an adequate object for sky rendering operations. The
 * constructor allows to instantiate an object selecting the values of the main
 * fields. There are much more variables (any variable name starting with
 * draw...) that can be set optionally.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class SkyRenderElement implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Default color model is normal
	 * with white background.
	 */
	public SkyRenderElement()
	{
		this.coordinateSystem = CoordinateSystem.COORDINATE_SYSTEM.EQUATORIAL;
		this.centralLongitude = 0.0;
		this.centralLatitude = 0.0;
		this.projection = Projection.PROJECTION.STEREOGRAPHICAL;
		this.width = height = 100;
		this.poleAngle = 0f;

		this.trajectory = null;
		telescope = new TelescopeElement();
		this.setColorMode(COLOR_MODE.BLACK_BACKGROUND);
	}

	/**
	 * Explicit constructor. Note that a lot of secondary options (enabled by
	 * default) like to draw constellations, nebula, and others like colors
	 * should be set after constructing this object. Default color model is normal
	 * with white background.
	 * 
	 * @param coord_system Coordinate system.
	 * @param proj Projection type.
	 * @param lon0 Central position in the selected system.
	 * @param lat0 Central position in the selected system.
	 * @param w Screen width.
	 * @param h Screen height.
	 * @param pr Planet render object.
	 * @param tel Telescope object.
	 */
	public SkyRenderElement(CoordinateSystem.COORDINATE_SYSTEM coord_system, Projection.PROJECTION proj, double lon0, double lat0, int w, int h,
			PlanetRenderElement pr, TelescopeElement tel)
	{
		this.coordinateSystem = coord_system;
		this.centralLongitude = lon0;
		this.centralLatitude = lat0;
		this.projection = proj;
		this.width = w;
		this.height = h;
		this.planetRender = pr;
		this.telescope = tel;
		this.poleAngle = 0f;

		this.trajectory = null;
		this.setColorMode(COLOR_MODE.WHITE_BACKGROUND);
	}

	/**
	 * Holds screen width in pixels.
	 */
	public int width;

	/**
	 * Holds screen height in pixels.
	 */
	public int height;

	/**
	 * Holds screen central position in coordinate longitude in radians. Can be
	 * set for either equatorial, horizontal, ecliptic, or galactic coordinates.
	 */
	public double centralLongitude;

	/**
	 * Holds screen central position in coordinate latitude in radians. Can be
	 * set for either equatorial, horizontal, ecliptic, or galactic coordinates.
	 */
	public double centralLatitude;

	/**
	 * Holds projection type.
	 */
	public Projection.PROJECTION projection;

	/**
	 * Holds the direction of the pole in the current projection, angle in radians. Rotation 
	 * is towards east if no vertical inversion is applied in the telescope object, which means
	 * that +PI/2 will show the west region upwards.
	 */
	public float poleAngle;

	/**
	 * Holds hour_angle.
	 */
	public float hourAngle;

	/**
	 * Holds coordinate system.
	 */
	public CoordinateSystem.COORDINATE_SYSTEM coordinateSystem;

	/**
	 * Holds planetary rendering properties.
	 */
	public PlanetRenderElement planetRender;

	/**
	 * Holds the selected telescope.
	 */
	public TelescopeElement telescope;

	/**
	 * Holds properties of a custom defined trajectory path to show.
	 */
	public TrajectoryElement[] trajectory;

	/**
	 * Set to false to disable transparent colors (for printing). Default value is true.
	 */
	public boolean enableTransparentColors = true;

	/**
	 * Set whether to draw or not constellations and which set of lines. Default
	 * value is the JPARSEC set of lines (for Western culture).
	 */
	public CONSTELLATION_CONTOUR drawConstellationContours = CONSTELLATION_CONTOUR.DEFAULT; 

	/**
	 * Set whether to draw or not constellations limits.
	 */
	public boolean drawConstellationLimits = true;

	/**
	 * Set whether to draw or not constellation names.
	 */
	public boolean drawConstellationNames = true;

	/**
	 * Set whether to draw or not constellation names in upper case.
	 */
	public boolean drawConstellationNamesUppercase = false;

	/**
	 * Set which constellation names to show. This only takes effect
	 * when the set of constellation lines is the default JPARSEC one,
	 * otherwise the names of the constellations for the different
	 * cultures are available only in Spanish or English.
	 */ 
	public Constellation.CONSTELLATION_NAME drawConstellationNamesType = Constellation.CONSTELLATION_NAME.LATIN;

	/**
	 * Set whether to draw or not coordinate grid.
	 */
	public boolean drawCoordinateGrid = true;

	/**
	 * Set whether to draw or not celestial points (NCP, SCP).
	 */
	public boolean drawCoordinateGridCelestialPoints = true;

	/**
	 * Set whether to draw or not detailed coordinate grid with high zoom.
	 */
	public boolean drawCoordinateGridHighZoom = true;

	/**
	 * Set whether to draw or not the square containing the rendering with the outer labels.
	 */
	public boolean drawExternalGrid = true;

	/**
	 * Set whether to draw or not the ecliptic when rendering sky in other
	 * coordinate systems, such as equatorial, galactic, or horizontal.
	 */
	public boolean drawCoordinateGridEcliptic = true;

	/**
	 * Set whether to draw or not the numeric labels for the ecliptic when 
	 * rendering sky in other coordinate systems, such as equatorial, galactic, 
	 * or horizontal.
	 */
	public boolean drawCoordinateGridEclipticLabels = true;

	/**
	 * Set whether to draw or not the stars.
	 */
	public boolean drawStars = true;

	/**
	 * Set whether to draw or not nebula.
	 */
	public boolean drawNebulaeContours = true;

	/**
	 * Set whether to draw or not the radians of meteor showers. Default is false.
	 */
	public boolean drawMeteorShowers = false;
	
	/** 
	 * Set to false to show all showers, true (default value) for those active currently. 
	 * In case this flag is flase, current active showers will be rendered without alpha 
	 * color component, and labels in bold face. In any case most intense showers will be 
	 * rendered using a stroke with greater width.
	 */
	public boolean drawMeteorShowersOnlyActive = true;
	
	/**
	 * Set whether to draw or not Milky Way.
	 */
	public boolean drawMilkyWayContours = true;

	/**
	 * Set if Milky Way should be filled.
	 */
	public boolean fillMilkyWay = true;

	/**
	 * Set if nebula should be filled. Note that sky rendering
	 * is usually faster when filling nebula.
	 */
	public boolean fillNebulae = true;

	/**
	 * Set whether to draw or not Milky Way with textures.
	 */
	public MILKY_WAY_TEXTURE drawMilkyWayContoursWithTextures = MILKY_WAY_TEXTURE.NO_TEXTURE;

	/**
	 * Set whether to draw or not deep sky objects.
	 */
	public boolean drawDeepSkyObjects = true;

	/**
	 * Set whether to draw or not the real images of the 
	 * deep sky objects on the sky for reduced fields of view.
	 */
	public boolean drawDeepSkyObjectsTextures = true;

	/**
	 * Set whether to draw or not only Messier deep sky objects.
	 */
	public boolean drawDeepSkyObjectsOnlyMessier = false;

	/**
	 * Set whether to draw or not all Messier deep sky objects, even
	 * in case their magnitudes are fainter that the limiting magnitude.
	 * Default value is true. This also includes all Caldwell objects.
	 */
	public boolean drawDeepSkyObjectsAllMessierAndCaldwell = true;

	/**
	 * Set whether to draw or not labels for deep sky objects.
	 */
	public boolean drawDeepSkyObjectsLabels = true;

	/**
	 * Set whether to draw or not the stars using different colors for spectral
	 * types. Only applied in case the background is not white.
	 */
	public boolean drawStarsColors = false;

	/**
	 * Select if stars should be rendered in a realistic way (using textures) or not. 
	 * Symbols for double and variable stars will not be drawn if something different
	 * from NONE is selected. Default value is NONE.
	 */
	public REALISTIC_STARS drawStarsRealistic = REALISTIC_STARS.NONE;
	
	/**
	 * Set the color for stars, only when {@linkplain SkyRenderElement#drawStarsColors}
	 * is false; This will be also the color for the planets in intermediate fields, when 
	 * they appear as little disks.
	 */
	public int drawStarsColor = 156<<24 | 0<<16 | 0<<8 | 0;

	/**
	 * Set whether to draw or not the stars using different symbols for variable
	 * and double types.
	 */
	public boolean drawStarsSymbols = true;

	/**
	 * Set whether to draw or not the orientation of the double star in its symbol. False as default,
	 * resulting in an horizontal line.
	 */
	public boolean drawStarsPositionAngleInDoubles = false;

	/**
	 * Set limiting magnitude for stars. Default value is 7.5, and the minimum value to
	 * show all constellation lines is 5.3. If it is set to a magnitude fainter than 10,
	 * and the field of view is lower than 1 degree, then the remaining stars will
	 * be drawn using USNO-B1 catalog, by calling Vizier database (only if Internet
	 * connection is available). If the limit is 12.0 or brighter, and fainter than 10,
	 * Karchenko's catalog will be used instead due to speed considerations. In this
	 * case, a field limit of 3 degrees will be used. 
	 */
	public float drawStarsLimitingMagnitude = 7.5f;

	/**
	 * Set limiting magnitude for deep sky objects, natural satellites, comets,
	 * and asteroids.
	 */
	public float drawObjectsLimitingMagnitude = 12.0f;

	/** Limiting magnitude for comets and asteroids. */
	public float drawMinorObjectsLimitingMagnitude = 12.0f;

	/**
	 * Set whether to draw or not Supernova and nova events.
	 */
	public boolean drawSuperNovaAndNovaEvents = true;

	/**
	 * Set during how many (Julian) years a SN event would still be visible in 
	 * the rendering. Default value is 3 years.
	 */
	public int drawSuperNovaEventsNumberOfYears = 3;

	/**
	 * Color attribute for SN.
	 */
	public int drawSuperNovaEventsColor = 255<<24 | 255<<16 | 0<<8 | 0;

	/**
	 * Color attribute for drawing deep sky objects.
	 */
	public int drawDeepSkyObjectsColor = 156<<24 | 0<<16 | 250<<8 | 0;

	/**
	 * Color attribute for drawing meteor showers.
	 */
	public int drawMeteorShowersColor = 156<<24 | 0<<16 | 250<<8 | 250;

	/**
	 * Color attribute for drawing constellations.
	 */
	public int drawConstellationContoursColor = 90<<24 | 128<<16 | 128<<8 | 128;

	/**
	 * Color attribute for drawing constellation limits.
	 */
	public int drawConstellationLimitsColor = 64<<24 | 192<<16 | 192<<8 | 192;

	/**
	 * Color attribute for drawing constellation names.
	 */
	public int drawConstellationNamesColor = 96<<24 | 0<<16 | 0<<8 | 0;

	/**
	 * Color attribute for drawing bright nebula.
	 */
	public int drawBrightNebulaeContoursColor = 128<<24 | 120<<16 | 120<<8 | 255;

	/**
	 * Color attribute for drawing dark nebula.
	 */
	public int drawDarkNebulaeContoursColor = 128<<24 | 120<<16 | 120<<8 | 255;

	/**
	 * Color attribute for drawing Milky Way. 
	 */
	public int drawMilkyWayContoursColor = 128<<24 | 180<<16 | 180<<8 | 255;

	/**
	 * Color attribute to fill Milky Way.
	 */
	public int fillMilkyWayColor = 128<<24 | 244<<16 | 244<<8 | 255;

	/**
	 * Color attribute to fill nebulae. Default value is -1 
	 * to use the same color as the nebulae contours.
	 */
	public int fillBrightNebulaeColor = -1;

	/**
	 * Sets coordinate grid color.
	 */
	public int drawCoordinateGridColor = 90<<24 | 0<<16 | 0<<8 | 0;

	/**
	 * Sets coordinate grid color for the ecliptic.
	 */
	public int drawCoordinateGridEclipticColor = 128<<24 | 255<<16 | 0<<8 | 0;

	/**
	 * Sets Sun spots color.
	 */
	public int drawSunSpotsColor = 255<<24 | 0<<16 | 0<<8 | 0;

	/**
	 * Set whether to draw or not the planets, the Moon, and the Sun.
	 */
	public boolean drawPlanetsMoonSun = true;

	/**
	 * Set whether to draw or not labels for solar spots.
	 */
	public boolean drawSunSpotsLabels = true;

	/**
	 * Set whether to draw or not the labels for planets, the Moon, and the Sun.
	 */
	public boolean drawPlanetsLabels = true;

	/**
	 * Set what kind of labels should be drawn for stars.
	 */
	public STAR_LABELS drawStarsLabels = STAR_LABELS.ONLY_PROPER_NAME;
	/**
	 * Set whether to show or not Greek symbols for the stars.
	 */
	public boolean drawStarsGreekSymbols = true;
	/**
	 * True to show Greek symbols only if the star has proper name.
	 */
	public boolean drawStarsGreekSymbolsOnlyIfHasProperName = false;
	
	/**
	 * Set limiting magnitude for stars labels;
	 */
	public float drawStarsLabelsLimitingMagnitude = 4.0f; 

	/**
	 * Minimum separation in arcseconds of a double star to draw it as a double.
	 */
	public float limitOfSeparationForDoubleStars = 0.5f;

	/**
	 * Minimum difference of magnitudes in a variable star to draw it as a variable.
	 */
	public float limitOfDifferenceOfMagnitudesForVariableStars = 0.1f;

	/**
	 * Set if fast lines mode will be used to render coordinate grid and constellations.
	 * This mode allows to draw lines very fast, but without support for strokes and
	 * antialiasing. In case of using a stroke with a line width greater than 1 for grid
	 * or constellations, this mode is automatically disabled. Default value is NONE.
	 */
	public FAST_LINES drawFastLinesMode = FAST_LINES.NONE;

	/**
	 * This sets the separation between constellation lines and stars. Default value is
	 * 20 px. Set to 0 to draw all lines from one star to the other.
	 */
	public int drawConstellationContoursMarginBetweenLineAndStar = 20;
	
	/**
	 * Selects the texture to draw at horizon, none by default.
	 */
	public HORIZON_TEXTURE drawHorizonTexture = HORIZON_TEXTURE.NONE;
	
	/**
	 * Set this flag to true to overlay an DSS image in the next rendering. This
	 * is only allowed when deep sky objects are shown and the field of view is
	 * lower than 3 degrees. After the overlay this flag is set to false in
	 * Android. Default value is false.
	 */
	public boolean overlayDSSimageInNextRendering = false;

	/**
	 * Selects if magnitudes should be shown for stars, asteroids, and comets.
	 * Labels are drawn only if limiting magnitude is equal or above 8.5.
	 */
	public boolean drawMagnitudeLabels = false;
	
	/**
	 * Selects if a central crux should be drawn to mark the screen center.
	 */
	public boolean drawCentralCrux = false;
	
	/**
	 * The set of options to draw fast lines for better performance.
	 */
	public static enum FAST_LINES {
		/** Never use fast mode. */
		NONE,
		/** Use fast line rendering for grid. */
		ONLY_GRID,
		/** Use fast line rendering for grid and constellations. */
		GRID_AND_CONSTELLATIONS,
		/** Use fast line rendering for grid and Milky Way outline. */
		GRID_AND_MILKY_WAY,
		/** Use fast line rendering for grid, Milky Way outline, and constellations. */ 
		GRID_AND_MILKY_WAY_AND_CONSTELLATIONS;
		
		/** Set to true to allow drawing fast ovals using images. */
		private boolean fastOvals = false;

		/**
		 * Sets the fast ovals flag. Little ovals for symbols like
		 * globular clusters or galaxies can be drawn as images to
		 * improve performance.
		 * @param fo True or false.
		 */
		public void setFastOvals(boolean fo) {
			fastOvals = fo;
		}
		/**
		 * Returns true if fast ovals are is enabled. Little ovals for symbols like
		 * globular clusters or galaxies can be drawn as images to
		 * improve performance.
		 * @return True or false.
		 */
		public boolean fastOvals() {
			return fastOvals;
		}
		/**
		 * Returns true if fast grid is enabled.
		 * @return True or false.
		 */
		public boolean fastGrid() {
			if (this == NONE) return false;
			return true;
		}
		
		/**
		 * Returns true if fast constellations are enabled.
		 * @return True or false.
		 */
		public boolean fastConstellations() {
			if (this == NONE || this == ONLY_GRID || this == GRID_AND_MILKY_WAY) return false;
			return true;
		}
		
		/**
		 * Returns true if fast Milky Way is enabled.
		 * @return True or false.
		 */
		public boolean fastMilkyWay() {
			if (this == NONE || this == ONLY_GRID || this == GRID_AND_CONSTELLATIONS) return false;
			return true;
		}
		
		/**
		 * Resets the fast ovals flag to its default value (false).
		 */
		public void clear() {
			fastOvals = false;
		}
	};
	
	/**
	 * The set of options to draw star labels.
	 */
	public static enum STAR_LABELS {
		/** No labels. */
		NONE,
		/** Normal labels, like Alp And. */
		NORMAL,
		/** Proper common name. */
		ONLY_PROPER_NAME,
		/** Proper common name in Spanish. */
		ONLY_PROPER_NAME_SPANISH
	};

	/**
	 * The set of options to draw texture at horizon.
	 */
	public static enum HORIZON_TEXTURE {
		/** No texture. */
		NONE,
		/** Veleta 30m telescope site, at Granada (Spain). */
		VELETA_30m,
		/** A generic village. */
		VILLAGE
	};

	/**
	 * The set of values to select the kind of realistic stars to render.
	 */
	public static enum REALISTIC_STARS { // DON'T CHANGE ORDERING
		/** Starred mode. */
		STARRED,
		/** Difuse star mode. */
		DIFFUSED,
		/** Stars with 4 spikes. */
		SPIKED,
		/** To avoid rendering realistic stars. Default value. This mode
		 * allows to show the symbols for double and variable stars. */
		NONE,
		/** Same as none, but faint stars located on top of brighter ones
		 * are rendered with an halo around to show them more clearly. */
		NONE_CUTE
	};

	/**
	 * The set of values to select how to draw labels.
	 */
	public static enum SUPERIMPOSED_LABELS {
		/** Fast labels without calculating positions to avoid superimposing them. */
		FAST,
		/** Avoid superimposing in a fasr way. */
		AVOID_SUPERIMPOSING_SOFT,
		/** Avoid superimposing with more quality and slower. */
		AVOID_SUPERIMPOSING_ACCURATE,
		/** Avoid superimposing with even more quality and slower. */
		AVOID_SUPERIMPOSING_VERY_ACCURATE
	}

	/**
	 * The set of textures for the milky way. All images except the optical
	 * one were taken from http://lambda.gsfc.nasa.gov/product/map/current/sos/7year/.
	 */
	public static enum MILKY_WAY_TEXTURE {
		/** Draw without texture. */
		NO_TEXTURE,
		/** Use the optical image by Nick Risinger. */
		OPTICAL,
		/** Use the h-alpha image by Finkbeiner 2003. */
		H_ALPHA,
		/** Use the 21cm (HI) line by Kalberla 2005. */
		HI_21cm,
		/** Use the CO (molecular clouds) map by Dame 2001. */
		CO,
		/** Use the IRAS map at 100 microns from Schlegel 1998. */
		IRAS_100,
		/** Use the background microwave map from WMAP. */
		WMAP
	}

	/**
	 * Holds the filenames for the different Milky Way textures.
	 */
	public static final String[] MILKY_WAY_TEXTURE_FILENAME = new String[] {
		null, "milkyway.jpg", "halpha_Finkbeiner_2003.jpg", "21cm_Kalberla_2005.jpg", "co_Dame_2001.jpg",
		"dust_Schlegel_1998.jpg", "wmap.jpg"
	};
	
	/**
	 * The set of values to select how to draw labels.
	 */
	public static enum LEYEND_POSITION {
		/** No leyend. */
		NO_LEYEND,
		/** Leyend in horizontal orientation on top. */
		TOP,
		/** Leyend in horizontal orientation on bottomp. */
		BOTTOM,
		/** Leyend in vertical orientation on the left edge. Not supported in Android. */
		LEFT,
		/** Leyend in vertical orientation on the right edge. Not supported in Android. */
		RIGHT
	}

	/**
	 * The set of options to draw constellation contours. All values here except
	 * the default set of lines are derived from the GPL work by Jason Harris and 
	 * Clemens ?, included in KStars. This selection also affects the labels for
	 * constellation names.
	 */
	public static enum CONSTELLATION_CONTOUR {
		/** No constellations. */
		NONE,
		/** Default JPARSEC lines. */
		DEFAULT,
		/** Western set of constellation lines. Similar to JPARSEC default set, 
		 * but not the same. */
		Western,
		/** Chinese set of constellation lines. */
		Chinese,
		/** Egyptian set of constellation lines. */
		Egyptian,
		/** Inuit set of constellation lines. */
		Inuit,
		/** Korean set of constellation lines. */
		Korean,
		/** Lakota set of constellation lines. */
		Lakota,
		/** Maori set of constellation lines. */
		Maori,
		/** Navaro set of constellation lines. */
		Navaro,
		/** Norse set of constellation lines. */
		Norse,
		/** Polynesian set of constellation lines. */
		Polynesian,
		/** Tupi-Guarani set of constellation lines. */
		Tupi_Guarani
	};

	/**
	 * Set whether to draw or not the labels for asteroids, comets, probes, and
	 * artificial satellites.
	 */
	public boolean drawMinorObjectsLabels = true;

	/**
	 * Set whether to draw or not asteroids.
	 */
	public boolean drawAsteroids = true;

	/**
	 * Set whether to draw or not comets.
	 */
	public boolean drawComets = true;

	/**
	 * Set whether to draw or not transNeptunian objects.
	 */
	public boolean drawTransNeptunianObjects = false;

	/**
	 * Set whether to draw or not artificial Earth satellites.
	 */
	public boolean drawArtificialSatellites = false;

	/**
	 * Select which satellites to draw (listed by names separated by comma),
	 * null to draw all if {@linkplain #drawArtificialSatellites} is true.
	 */
	public String drawArtificialSatellitesOnlyThese = null;

	/**
	 * Set whether to show or not flaring Iridium satellites. For this
	 * {@linkplain #drawArtificialSatellites} should be enabled.
	 */
	public boolean drawArtificialSatellitesIridiumFlares = false;

	/**
	 * Set whether to draw or not space probes.
	 */
	public boolean drawSpaceProbes = true;

	/**
	 * Set whether to draw or not the ocular field of view circle.
	 */
	public boolean drawOcularFieldOfView = false;

	/**
	 * Set whether to draw or not coordinate grid labels.
	 */
	public boolean drawCoordinateGridLabels = true;

	/**
	 * Holds the leyend position, top by default. Leyend should
	 * only be drawn with at least 800 px of space.
	 */
	public LEYEND_POSITION drawLeyend = LEYEND_POSITION.TOP;

	/**
	 * Sets the font to be used for constellation labels.
	 */
	public Graphics.FONT drawConstellationNamesFont = Graphics.FONT.SANS_SERIF_ITALIC_18;

	/**
	 * Sets the font to be used for constellation labels.
	 */
	public Graphics.FONT drawStarsNamesFont = Graphics.FONT.SANS_SERIF_PLAIN_11;

	/**
	 * Sets the font to be used for planets labels.
	 */
	public Graphics.FONT drawPlanetsNamesFont = Graphics.FONT.SANS_SERIF_BOLD_14;

	/**
	 * Sets the font to be used for deep sky objects labels.
	 */
	public Graphics.FONT drawDeepSkyObjectsNamesFont = Graphics.FONT.SANS_SERIF_ITALIC_10;

	/**
	 * Sets the font to be used for comet, asteroid, satellite labels, and meteor showers.
	 */
	public Graphics.FONT drawMinorObjectsNamesFont = Graphics.FONT.SANS_SERIF_BOLD_10;

	/**
	 * Sets the font to be used for grid labels.
	 */
	public Graphics.FONT drawCoordinateGridFont = Graphics.FONT.SANS_SERIF_PLAIN_14;
/*
	// PREVIOUS VALUES
	drawConstellationNamesFont = Graphics.FONT.SANS_SERIF_ITALIC_20;
	drawStarsNamesFont = Graphics.FONT.SANS_SERIF_PLAIN_13;
	drawPlanetsNamesFont = Graphics.FONT.SANS_SERIF_BOLD_16;
	drawDeepSkyObjectsNamesFont = Graphics.FONT.SANS_SERIF_ITALIC_12;
	drawMinorObjectsNamesFont = Graphics.FONT.SANS_SERIF_PLAIN_12;
	drawCoordinateGridFont = Graphics.FONT.SANS_SERIF_PLAIN_15;
*/	

	/**
	 * True (default value) to show N, S, E, and W points in the sky.
	 */
	public boolean drawCoordinateGridCardinalPoints = true;
	
	/**
	 * Sets whether to draw or not the sky below the observer's horizon.
	 * True as default.
	 */
	public boolean drawSkyBelowHorizon = true;

	/**
	 * Sets whether to correct for refraction in observer's horizon. This also 
	 * corrects local horizon for depression. Only considered
	 * if also {@linkplain SkyRenderElement#drawSkyBelowHorizon} is false.
	 * False as default.
	 */
	public boolean drawSkyCorrectingLocalHorizon = false;

	/**
	 * To draw or not the Sun spots if available for this date. If textures are
	 * enabled, solar spots will not be drawn.
	 */
	public boolean drawSunSpots = true;

	/**
	 * Sets background color. Also the color to fill dark nebula.
	 */
	public int background = 255<<24 | 255<<16 | 255<<8 | 255;
	
	/**
	 * The color for the grid used to show the field of view of a telescope.
	 */
	public int drawOcularFieldOfViewColor = 255<<24 | 0<<16 | 0<<8 | 0;
	
	/**
	 * Stroke to draw nebula.
	 */
	public JPARSECStroke drawNebulaeStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
	/**
	 * Stroke to draw Milky Way.
	 */
	public JPARSECStroke drawMilkyWayStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
	/**
	 * Stroke to draw deep sky objects.
	 */
	public JPARSECStroke drawDeepSkyObjectsStroke = new JPARSECStroke(JPARSECStroke.STROKE_DEFAULT_LINE_THIN, 1.0f);
	/**
	 * Stroke to draw constellations.
	 */
	public JPARSECStroke drawConstellationStroke = JPARSECStroke.STROKE_DEFAULT_LINE;
	/**
	 * Stroke to draw constellation limits.
	 */
	public JPARSECStroke drawConstellationLimitsStroke = new JPARSECStroke(JPARSECStroke.STROKE_LINES_LARGE, new float[] {8, 3}, 0);
	/**
	 * Stroke to draw coordinate grid.
	 */
	public JPARSECStroke drawCoordinateGridStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;

	/**
	 * True to draw stars beyond magnitude 10 (Internet connection required).
	 */
	public boolean drawFaintStars = true;

	/**
	 * Selects to draw labels in a fast way or trying to reduce label
	 * superimposing.
	 */
	public SUPERIMPOSED_LABELS drawFastLabels = SUPERIMPOSED_LABELS.FAST;

	/**
	 * True to draw labels in a fast way when the field of view is greater than 100 degrees.
	 * Only considered if {@linkplain #drawFastLabels} is not set to fast mode.
	 */
	public boolean drawFastLabelsInWideFields = true;

	/**
	 * True to draw icons for planets, probes, ...
	 */
	public boolean drawIcons = true;
	
	/**
	 * True to hide objects depending on the field of view, in a clever way. Setting
	 * this to false will show most objects (but not stars) independently of the field 
	 * of view, but the disks of planets and satellites will not appear. Note projection is forced
	 * to cylindrical for fields of view lower than 30 degrees to show correctly the
	 * relative positions of planets, satellites, and stars in case the clever flag is true.
	 * True is recommended, false can be useful to construct charts with a given set of data.
	 */
	public boolean drawClever = true;

	/**
	 * True to use antialiasing to increasing rendering quality.
	 */
	public boolean drawWithAntialiasing = true;
	
	/**
	 * The color to fill a galaxy. Set to -1 as default to avoid the fill.
	 */
	public int fillGalaxyColor = -1;
	/**
	 * The color to fill a globular cluster. Set to -1 as default to avoid the fill.
	 */
	public int fillGlobularColor = -1;
	/**
	 * The color to fill an open cluster. Set to -1 as default to avoid the fill.
	 */
	public int fillOpenColor = -1;
	/**
	 * Timeout to query UCAC3 catalog in seconds.
	 */
	public int drawFaintStarsTimeOut = 20;
	/**
	 * Set to false (default is true) to avoid using superscript for labeling
	 * right ascension, which is something only supported in desktop mode.
	 */
	public boolean useSuperScriptForRA = true;
	/**
	 * The anaglyph mode, disabled by default.
	 */
	public ANAGLYPH_COLOR_MODE anaglyphMode = ANAGLYPH_COLOR_MODE.NO_ANAGLYPH;
	/**
	 * The time interval to update the ephemerides of everything when rendering the sky in
	 * real-time mode. Default value is 30s. When time changes less than this value planets
	 * and satellites are still updated, but not comets or asteroids.
	 */
	public float updateTime = 30;
	/**
	 * The time interval to update everything except ephemerides when rendering the sky in
	 * real-time mode. Default value is 3600s. Will force the update of everything.
	 */
	public float updateTimeFullUpdate = 3600;
	
	/**
	 * Default value is null, which means that all external catalogs defined will be shown.
	 * Define the array and set each to true or false to enable/disable each of them.
	 */
	public boolean drawExternalCatalogs[] = null;
	
	private String[] externalCatalogs = new String[0];
	private String[] externalCatalogNames = new String[0];
	private static int externalCatalogCounter = 0;
	
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_NAME1 = "NAME1";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_MAG = "MAG";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_FLUX = "FLUX";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_POSITION_ANGLE_DEG = "PA";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_DETAILS = "COMMENT";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_MINSIZE_DEG = "MINSIZE";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_MAXSIZE_DEG = "MAXSIZE";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_SIZE_DEG = "SIZE";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_MINSIZE_ARCSEC = "MINSIZE";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_MAXSIZE_ARCSEC = "MAXSIZE";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_SIZE_ARCSEC = "SIZE";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_EQUINOX_YEAR = "EQUINOX";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_COORDINATES_TYPE = "COORD_TYPE";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_RA_H = "RAH";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_RA_M = "RAM";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_RA_S = "RAS";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_DEC_D = "DECD";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_DEC_M = "DECM";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_DEC_S = "DECS";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_RA_DEG_WITH_DECIMALS = "RA_DEG";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_RA_HOURS_WITH_DECIMALS = "RA_HOURS";
	/** Id constant for a field name to be used in a {@linkplain FileFormatElement} object for an external catalog. */
	public static final String EXTERNAL_CATALOG_FIELD_DEC_DEG_WITH_DECIMALS = "DEC_DEG";

	/**
	 * Adds an external catalog to render certain objects to memory. Since the sky rendering class already takes into account main
	 * astronomical objects, the catalog is added as a subset of deep sky objects.
	 * @param name An identifier or name for this catalog.
	 * @param objType The global name for the type of objects in this catalog.
	 * @param rgb The rgb color to show the objects in this catalog.
	 * @param path The path to the file.
	 * @param format The format. The identifiers for the fields should be set using the symbolic
	 * constants in this class. Flux field can be used instead of magnitude to use a logarithmic scale. The angular
	 * size can be set in degrees or arcsec, using major/minor values (and the position angle) or the same value 
	 * for both axes. The coordinates type is a field used to include objects with ecliptic/galactic coordinates
	 * instead of equatorial. Valid values in the files are those starting with 'EQ', 'EC', 'GA'. In this case
	 * the RA/DEC fields corresponds to ecliptic/galactic longitudes/latitudes. Default equinox is year 2000 if it 
	 * is not specified. RA/DEC should be mean coordinates referred to a given equinox.
	 * @throws JPARSECException If an error occurs.
	 */
	public void addExternalCatalog(String name, String objType, int rgb, String path, FileFormatElement[] format) throws JPARSECException {
		addExternalCatalog(name, objType, rgb, ReadFile.readAnyExternalFile(path), format, true);
	}
	/**
	 * Adds an external catalog to render certain objects to memory. Since the sky rendering class already takes into account main
	 * astronomical objects, the catalog is added as a subset of deep sky objects.
	 * @param name An identifier or name for this catalog.
	 * @param objType The global name for the type of objects in this catalog.
	 * @param rgb The rgb color to show the objects in this catalog.
	 * @param contents The contents of the file.
	 * @param format The format. The identifiers for the fields should be set using the symbolic
	 * constants in this class. Flux field can be used instead of magnitude to use a logarithmic scale. The angular
	 * size can be set in degrees or arcsec, using major/minor values (and the position angle) or the same value 
	 * for both axes. The coordinates type is a field used to include objects with ecliptic/galactic coordinates
	 * instead of equatorial. Valid values in the files are those starting with 'EQ', 'EC', 'GA'. In this case
	 * the RA/DEC fields corresponds to ecliptic/galactic longitudes/latitudes. Default equinox is year 2000 if it 
	 * is not specified. RA/DEC should be mean coordinates referred to a given equinox.
	 * @throws JPARSECException  If an error occurs.
	 */
	public void addExternalCatalog(String name, String objType, int rgb, String[] contents, FileFormatElement[] format) throws JPARSECException {
		addExternalCatalog(name, objType, rgb, contents, format, true);
	}
	
	/**
	 * Adds an external catalog to render certain objects to memory. Since the sky rendering class already takes into account main
	 * astronomical objects, the catalog is added as a subset of deep sky objects.
	 * @param name An identifier or name for this catalog.
	 * @param objType The global name for the type of objects in this catalog.
	 * @param rgb The rgb color to show the objects in this catalog.
	 * @param contents The contents of the file.
	 * @param format The format. The identifiers for the fields should be set using the symbolic
	 * constants in this class. Flux field can be used instead of magnitude to use a logarithmic scale. The angular
	 * size can be set in degrees or arcsec, using major/minor values (and the position angle) or the same value 
	 * for both axes. The coordinates type is a field used to include objects with ecliptic/galactic coordinates
	 * instead of equatorial. Valid values in the files are those starting with 'EQ', 'EC', 'GA'. In this case
	 * the RA/DEC fields corresponds to ecliptic/galactic longitudes/latitudes. Default equinox is year 2000 if it 
	 * is not specified. RA/DEC should be mean coordinates referred to a given equinox.
	 * @throws JPARSECException  If an error occurs.
	 */
	public void addExternalCatalog(String name, String objType, int rgb, ArrayList<String> contents, FileFormatElement[] format) throws JPARSECException {
		addExternalCatalog(name, objType, rgb, contents, format, true);
	}
	
	/**
	 * Adds an external catalog to render objects. Since the sky rendering class already takes into account main
	 * astronomical objects, the catalog is added as a subset of deep sky objects.
	 * @param name An identifier or name for this catalog.
	 * @param objType The global name for the type of objects in this catalog.
	 * @param rgb The rgb color to show the objects in this catalog.
	 * @param contents The contents of the file.
	 * @param format The format. The identifiers for the fields should be set using the symbolic
	 * constants in this class. Flux field can be used instead of magnitude to use a logarithmic scale. The angular
	 * size can be set in degrees or arcsec, using major/minor values (and the position angle) or the same value 
	 * for both axes. The coordinates type is a field used to include objects with ecliptic/galactic coordinates
	 * instead of equatorial. Valid values in the files are those starting with 'EQ', 'EC', 'GA'. In this case
	 * the RA/DEC fields corresponds to ecliptic/galactic longitudes/latitudes. Default equinox is year 2000 if it 
	 * is not specified. RA/DEC should be mean coordinates referred to a given equinox.
	 * @param inMemory True to hold the catalog in memory, false for disk. True is strongly recommended for better
	 * performance.
	 * @throws JPARSECException If an error occurs.
	 */
	public synchronized void addExternalCatalog(String name, String objType, int rgb, ArrayList<String> contents, FileFormatElement[] format,
			boolean inMemory) throws JPARSECException {
		ArrayList<Object> list = new ArrayList<Object>();
		
		ReadFormat rf = new ReadFormat(format);
		boolean fluxMode = false;
		ArrayList<Double> listFlux = new ArrayList<Double>();
		int line0 = 0;
		try { line0 = rf.getField("SKIP_LINES").endingPosition; } catch (Exception exc) {}
		double equinox = 2000, lastEquinox = equinox;
		double jd = Constant.J2000;
		for (int i=line0; i<contents.size(); i++) {
			String line = contents.get(i);
			if (!line.startsWith("!") && !line.startsWith("#") && !line.startsWith("*")) { // IRAM/JCMT comments
				float pa = -1, maxSize = 0, minSize = 0;
				String com = "", name1 = "", name2 = objType, mag = "0", ctype = "EQ";
				int tt = 100;

				String s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_NAME1);
				if (s != null) name1 = s;

				s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_DETAILS);
				if (s != null) com = s;

				s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_COORDINATES_TYPE);
				if (s != null) ctype = s.toUpperCase();

				s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_EQUINOX_YEAR);
				if (s != null) equinox = Double.parseDouble(s);

				s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_MAG);
				if (s != null) {
					mag = s;
				} else {
					s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_FLUX);
					if (s != null) {
						mag = s;
						fluxMode = true;
						listFlux.add(Double.parseDouble(mag));
					}
				}

				s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_POSITION_ANGLE_DEG);
				if (s != null) pa = (float) (Double.parseDouble(s) * Constant.DEG_TO_RAD);

				s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_SIZE_DEG);
				if (s != null) {
					maxSize = (float) Double.parseDouble(s);					
					minSize = maxSize;
				} else {
					s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_SIZE_ARCSEC);
					if (s != null) {
						maxSize = (float) (Double.parseDouble(s) / 3600.0);
						minSize = maxSize;
					} else {
						s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_MAXSIZE_DEG);
						if (s != null) {
							maxSize = (float) Double.parseDouble(s);
							tt = 1;
						} else {
							s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_MAXSIZE_ARCSEC);
							if (s != null) {
								maxSize = (float) (Double.parseDouble(s) / 3600.0);
								tt = 1;
							}					
						}

						s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_MINSIZE_DEG);
						if (s != null) {
							minSize = (float) Double.parseDouble(s);
							tt = 1;
						} else {
							s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_MINSIZE_ARCSEC);
							if (s != null) {
								minSize = (float) (Double.parseDouble(s) / 3600.0);
								tt = 1;
							}					
						}

					}
				}

				double ra = 0, dec = 0, r = 1.0;
				s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_RA_DEG_WITH_DECIMALS);
				if (s != null) {
					ra = Double.parseDouble(s) * Constant.DEG_TO_RAD;
				} else {
					s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_RA_HOURS_WITH_DECIMALS);
					if (s != null) {
						ra = Double.parseDouble(s) / Constant.RAD_TO_HOUR;
					} else {
						String s1 = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_RA_H);
						String s2 = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_RA_M);
						String s3 = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_RA_S);
						if (s1 != null && s2 != null && s3 != null) {
							ra = (Double.parseDouble(s1)+Double.parseDouble(s2)/60.0+Double.parseDouble(s3)/3600.0) / Constant.RAD_TO_HOUR;
						} else {
							continue;
						}
					}					
				}
				s = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_DEC_DEG_WITH_DECIMALS);
				if (s != null) {
					dec = Double.parseDouble(s) * Constant.DEG_TO_RAD;
				} else {
					String s1 = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_DEC_D);
					String s2 = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_DEC_M);
					String s3 = getField(line, rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_DEC_S);
					if (s1 != null && s2 != null && s3 != null) {
						dec = (Math.abs(Double.parseDouble(DataSet.replaceAll(s1, " ", "", true)))+Double.parseDouble(s2)/60.0+Double.parseDouble(s3)/3600.0) * Constant.DEG_TO_RAD;
						if (s1.startsWith("-")) dec = -dec;
					} else {
						continue;
					}
				}

				LocationElement loc = new LocationElement(ra, dec, r);

				if (lastEquinox != equinox) {
					jd = (new AstroDate((int)equinox, 1, 1, 12, 0, 0)).jd();
					lastEquinox = equinox;
				}
				if (!ctype.startsWith("EQ")) {
					boolean fast = false;
					double t = (equinox - 2000.0) / 100.0;
					if (ctype.startsWith("EC")) {
						EphemerisElement eph = new EphemerisElement();
						eph.ephemMethod = REDUCTION_METHOD.IAU_2009;
						double obliquity = Obliquity.meanObliquity(t, eph);
						loc = CoordinateSystem.eclipticToEquatorial(loc, obliquity, fast);
					}
					if (ctype.startsWith("GA")) {
						loc = CoordinateSystem.galacticToEquatorial(loc, jd, fast);
					}					
				}
				if (jd != Constant.J2000) {
					EphemerisElement eph = new EphemerisElement();
					eph.ephemMethod = REDUCTION_METHOD.IAU_2009;
					loc = LocationElement.parseRectangularCoordinates(Precession.precess(jd, Constant.J2000,
							LocationElement.parseLocationElement(loc), eph));
				}

				list.add(new Object[] {name1, name2, tt, loc, (float)Double.parseDouble(mag), 
						new float[] {maxSize, minSize}, pa, com, rgb});
			}
		}
		
		if (fluxMode) {
			double flux[] = DataSet.arrayListToDoubleArray(listFlux);
			double max = DataSet.getMaximumValue(flux), min = DataSet.getMinimumValue(flux);
			double minMag = 6.5, maxMag = 0;
			for (int i=0; i<list.size(); i++) {
				Object o[] = (Object[]) list.get(i);
				double mag = (Float) o[4];
				
				mag = 1.0 + (mag - min) / (max - min); // 1 to 2. Note log(1) = 0
				mag = Math.log(mag) / Math.log(2.0); // 0 to 1, in log scale
				mag = minMag - mag * (minMag - maxMag);
				
				o[4] = (float)mag;
				list.set(i, o);
			}
		}
		
		String id = "RenderSkyExternalCatalog"+externalCatalogCounter;
		DataBase.addData(id, list.toArray(), inMemory);
		if (!this.externalCatalogAvailable(id)) {
			externalCatalogs = DataSet.addStringArray(externalCatalogs, new String[] {id});
			externalCatalogNames = DataSet.addStringArray(externalCatalogNames, new String[] {name});
			externalCatalogCounter ++;
		} else {
			externalCatalogNames[DataSet.getIndex(externalCatalogs, id)] = name;
		}
		if (this.drawExternalCatalogs == null && externalCatalogs.length == 1) {
			drawExternalCatalogs = new boolean[externalCatalogs.length];
			drawExternalCatalogs[0] = true;
		} else {
			if (drawExternalCatalogs != null && externalCatalogs.length == 1+drawExternalCatalogs.length)
				drawExternalCatalogs = DataSet.addBooleanArray(drawExternalCatalogs, new boolean[] {true});
		}
		//return id;
	}
	
	/**
	 * Adds an external catalog to render objects. Since the sky rendering class already takes into account main
	 * astronomical objects, the catalog is added as a subset of deep sky objects.
	 * @param name An identifier or name for this catalog.
	 * @param objType The global name for the type of objects in this catalog.
	 * @param rgb The rgb color to show the objects in this catalog.
	 * @param contents The contents of the file.
	 * @param format The format. The identifiers for the fields should be set using the symbolic
	 * constants in this class. Flux field can be used instead of magnitude to use a logarithmic scale. The angular
	 * size can be set in degrees or arcsec, using major/minor values (and the position angle) or the same value 
	 * for both axes. The coordinates type is a field used to include objects with ecliptic/galactic coordinates
	 * instead of equatorial. Valid values in the files are those starting with 'EQ', 'EC', 'GA'. In this case
	 * the RA/DEC fields corresponds to ecliptic/galactic longitudes/latitudes. Default equinox is year 2000 if it 
	 * is not specified. RA/DEC should be mean coordinates referred to a given equinox.
	 * @param inMemory True to hold the catalog in memory, false for disk. True is strongly recommended for better
	 * performance.
	 * @throws JPARSECException If an error occurs.
	 */
	public synchronized void addExternalCatalog(String name, String objType, int rgb, String[] contents, FileFormatElement[] format,
			boolean inMemory) throws JPARSECException {
		ArrayList<Object> list = new ArrayList<Object>();
		
		ReadFormat rf = new ReadFormat(format);
		boolean fluxMode = false;
		ArrayList<Double> listFlux = new ArrayList<Double>();
		int line0 = 0;
		try { line0 = rf.getField("SKIP_LINES").endingPosition; } catch (Exception exc) {}
		double equinox = 2000, lastEquinox = equinox;
		double jd = Constant.J2000;
		for (int i=line0; i<contents.length; i++) {
			if (!contents[i].startsWith("!") && !contents[i].startsWith("#") && !contents[i].startsWith("*")) { // IRAM/JCMT comments
				float pa = -1, maxSize = 0, minSize = 0;
				String com = "", name1 = "", name2 = objType, mag = "0", ctype = "EQ";
				int tt = 100;

				String s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_NAME1);
				if (s != null) name1 = s;

				s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_DETAILS);
				if (s != null) com = s;

				s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_COORDINATES_TYPE);
				if (s != null) ctype = s.toUpperCase();

				s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_EQUINOX_YEAR);
				if (s != null) equinox = Double.parseDouble(s);

				s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_MAG);
				if (s != null) {
					mag = s;
				} else {
					s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_FLUX);
					if (s != null) {
						mag = s;
						fluxMode = true;
						listFlux.add(Double.parseDouble(mag));
					}
				}

				s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_POSITION_ANGLE_DEG);
				if (s != null) pa = (float) (Double.parseDouble(s) * Constant.DEG_TO_RAD);

				s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_SIZE_DEG);
				if (s != null) {
					maxSize = (float) Double.parseDouble(s);					
					minSize = maxSize;
				} else {
					s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_SIZE_ARCSEC);
					if (s != null) {
						maxSize = (float) (Double.parseDouble(s) / 3600.0);
						minSize = maxSize;
					} else {
						s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_MAXSIZE_DEG);
						if (s != null) {
							maxSize = (float) Double.parseDouble(s);
							tt = 1;
						} else {
							s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_MAXSIZE_ARCSEC);
							if (s != null) {
								maxSize = (float) (Double.parseDouble(s) / 3600.0);
								tt = 1;
							}					
						}

						s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_MINSIZE_DEG);
						if (s != null) {
							minSize = (float) Double.parseDouble(s);
							tt = 1;
						} else {
							s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_MINSIZE_ARCSEC);
							if (s != null) {
								minSize = (float) (Double.parseDouble(s) / 3600.0);
								tt = 1;
							}					
						}

					}
				}

				double ra = 0, dec = 0, r = 1.0;
				s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_RA_DEG_WITH_DECIMALS);
				if (s != null) {
					ra = Double.parseDouble(s) * Constant.DEG_TO_RAD;
				} else {
					s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_RA_HOURS_WITH_DECIMALS);
					if (s != null) {
						ra = Double.parseDouble(s) / Constant.RAD_TO_HOUR;
					} else {
						String s1 = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_RA_H);
						String s2 = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_RA_M);
						String s3 = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_RA_S);
						if (s1 != null && s2 != null && s3 != null) {
							ra = (Double.parseDouble(s1)+Double.parseDouble(s2)/60.0+Double.parseDouble(s3)/3600.0) / Constant.RAD_TO_HOUR;
						} else {
							continue;
						}
					}					
				}
				s = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_DEC_DEG_WITH_DECIMALS);
				if (s != null) {
					dec = Double.parseDouble(s) * Constant.DEG_TO_RAD;
				} else {
					String s1 = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_DEC_D);
					String s2 = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_DEC_M);
					String s3 = getField(contents[i], rf, SkyRenderElement.EXTERNAL_CATALOG_FIELD_DEC_S);
					if (s1 != null && s2 != null && s3 != null) {
						dec = (Math.abs(Double.parseDouble(DataSet.replaceAll(s1, " ", "", true)))+Double.parseDouble(s2)/60.0+Double.parseDouble(s3)/3600.0) * Constant.DEG_TO_RAD;
						if (s1.startsWith("-")) dec = -dec;
					} else {
						continue;
					}
				}

				LocationElement loc = new LocationElement(ra, dec, r);

				if (lastEquinox != equinox) {
					jd = (new AstroDate((int)equinox, 1, 1, 12, 0, 0)).jd();
					lastEquinox = equinox;
				}
				if (!ctype.startsWith("EQ")) {
					boolean fast = false;
					double t = (equinox - 2000.0) / 100.0;
					if (ctype.startsWith("EC")) {
						EphemerisElement eph = new EphemerisElement();
						eph.ephemMethod = REDUCTION_METHOD.IAU_2009;
						double obliquity = Obliquity.meanObliquity(t, eph);
						loc = CoordinateSystem.eclipticToEquatorial(loc, obliquity, fast);
					}
					if (ctype.startsWith("GA")) {
						loc = CoordinateSystem.galacticToEquatorial(loc, jd, fast);
					}					
				}
				if (jd != Constant.J2000) {
					EphemerisElement eph = new EphemerisElement();
					eph.ephemMethod = REDUCTION_METHOD.IAU_2009;
					loc = LocationElement.parseRectangularCoordinates(Precession.precess(jd, Constant.J2000,
							LocationElement.parseLocationElement(loc), eph));
				}

				list.add(new Object[] {name1, name2, tt, loc, (float)Double.parseDouble(mag), 
						new float[] {maxSize, minSize}, pa, com, rgb});
			}
		}
		
		if (fluxMode) {
			double flux[] = DataSet.arrayListToDoubleArray(listFlux);
			double max = DataSet.getMaximumValue(flux), min = DataSet.getMinimumValue(flux);
			double minMag = 6.5, maxMag = 0;
			for (int i=0; i<list.size(); i++) {
				Object o[] = (Object[]) list.get(i);
				double mag = (Float) o[4];
				
				mag = 1.0 + (mag - min) / (max - min); // 1 to 2. Note log(1) = 0
				mag = Math.log(mag) / Math.log(2.0); // 0 to 1, in log scale
				mag = minMag - mag * (minMag - maxMag);
				
				o[4] = (float)mag;
				list.set(i, o);
			}
		}
		
		String id = "RenderSkyExternalCatalog"+externalCatalogCounter;
		DataBase.addData(id, list.toArray(), inMemory);
		if (!this.externalCatalogAvailable(id)) {
			externalCatalogs = DataSet.addStringArray(externalCatalogs, new String[] {id});
			externalCatalogNames = DataSet.addStringArray(externalCatalogNames, new String[] {name});
			externalCatalogCounter ++;
		} else {
			externalCatalogNames[DataSet.getIndex(externalCatalogs, id)] = name;
		}
		if (this.drawExternalCatalogs == null && externalCatalogs.length == 1) {
			drawExternalCatalogs = new boolean[externalCatalogs.length];
			drawExternalCatalogs[0] = true;
		} else {
			if (drawExternalCatalogs != null && externalCatalogs.length == 1+drawExternalCatalogs.length)
				drawExternalCatalogs = DataSet.addBooleanArray(drawExternalCatalogs, new boolean[] {true});
		}
		//return id;
	}

	private String getField(String line, ReadFormat rf, String fieldName) {
		try {
			if (!rf.fieldExists(fieldName)) return null;
			FileFormatElement format = rf.getField(fieldName);
			String out = ReadFormat.readField(line, format).trim();
			if (out.equals("null")) out = null;
			return out;
		} catch (Exception exc) {
			exc.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns the number of external catalogs defined. Note this is a static value that 
	 * corresponds to the total number of external catalogs defined in all threads, for
	 * a given thread some of those catalogs could be null.
	 * @return The number of total external catalogs.
	 */
	public static int getTotalNumberOfExternalCatalogs() {
		return externalCatalogCounter;
	}

	/**
	 * Returns the number of external catalogs defined in this instance.
	 * @return The number of external catalogs for this instance.
	 */
	public int getNumberOfExternalCatalogs() {
		return externalCatalogs.length;
	}
	
	/**
	 * Returns the number of external catalogs currently enabled to be shown, and defined in this instance.
	 * @return The number of external catalogs currently enabled for this instance.
	 */
	public int getNumberOfEnabledExternalCatalogs() {
		if (drawExternalCatalogs == null) return getNumberOfExternalCatalogs();
		int n = 0;
		for (int i=0; i<externalCatalogs.length; i++) {
			if (drawExternalCatalogs[i]) n ++;
		}
		return n;
	}

	/**
	 * Returns the name of a given external catalog.
	 * @param index The index, from 0 to {@link #getNumberOfExternalCatalogs()}.
	 * @return The name of a given catalog, or null for an invalid index.
	 */
	public String getExternalCatalogName(int index) {
		if (externalCatalogNames == null || (index < 0 || index >= externalCatalogNames.length)) return null;
		return externalCatalogNames[index];
	}
	
	/**
	 * Returns if a given catalog id is available for this instance.
	 * @param id The id of the external catalog.
	 * @return True or false.
	 */
	public boolean externalCatalogAvailable(String id) {
		if (externalCatalogs == null || externalCatalogs.length == 0) return false;
		boolean out = false;
		int index = DataSet.getIndex(externalCatalogs, id);
		if (index >= 0) out = true;
		return out;
	}
	
	/**
	 * Clones this instance.
	 */
	public SkyRenderElement clone()
	{
		if (this == null) return null;
		SkyRenderElement s = new SkyRenderElement();
		s.centralLatitude = this.centralLatitude;
		s.centralLongitude = this.centralLongitude;
		s.coordinateSystem = this.coordinateSystem;
		s.drawArtificialSatellites = this.drawArtificialSatellites;
		s.drawArtificialSatellitesOnlyThese = this.drawArtificialSatellitesOnlyThese;
		s.drawArtificialSatellitesIridiumFlares = this.drawArtificialSatellitesIridiumFlares;
		s.drawAsteroids = this.drawAsteroids;
		s.drawComets = this.drawComets;
		s.enableTransparentColors = this.enableTransparentColors;
		s.drawConstellationContours = this.drawConstellationContours;
		s.drawConstellationContoursMarginBetweenLineAndStar = this.drawConstellationContoursMarginBetweenLineAndStar;
		s.drawConstellationContoursColor = this.drawConstellationContoursColor;
		s.drawConstellationLimits = this.drawConstellationLimits;
		s.drawConstellationLimitsColor = this.drawConstellationLimitsColor;
		s.drawConstellationNames = this.drawConstellationNames;
		s.drawConstellationNamesUppercase = this.drawConstellationNamesUppercase;
		s.drawConstellationNamesColor = this.drawConstellationNamesColor;
		s.drawConstellationNamesFont = this.drawConstellationNamesFont;
		s.drawConstellationNamesType = this.drawConstellationNamesType;
		s.drawCoordinateGrid = this.drawCoordinateGrid;
		s.drawCoordinateGridCelestialPoints = this.drawCoordinateGridCelestialPoints;
		s.drawCoordinateGridHighZoom = this.drawCoordinateGridHighZoom;
		s.drawExternalGrid = this.drawExternalGrid;
		s.drawCoordinateGridColor = this.drawCoordinateGridColor;
		s.drawCoordinateGridEcliptic = this.drawCoordinateGridEcliptic;
		s.drawCoordinateGridEclipticLabels = this.drawCoordinateGridEclipticLabels;
		s.drawCoordinateGridEclipticColor = this.drawCoordinateGridEclipticColor;
		s.drawCoordinateGridFont = this.drawCoordinateGridFont;
		s.drawCoordinateGridLabels = this.drawCoordinateGridLabels;
		s.drawDeepSkyObjects = this.drawDeepSkyObjects;
		s.drawDeepSkyObjectsTextures = this.drawDeepSkyObjectsTextures;
		s.drawDeepSkyObjectsColor = this.drawDeepSkyObjectsColor;
		s.drawMeteorShowersColor = this.drawMeteorShowersColor;
		s.drawDeepSkyObjectsLabels = this.drawDeepSkyObjectsLabels;
		s.drawDeepSkyObjectsNamesFont = this.drawDeepSkyObjectsNamesFont;
		s.drawDeepSkyObjectsOnlyMessier = this.drawDeepSkyObjectsOnlyMessier;
		s.drawDeepSkyObjectsAllMessierAndCaldwell = this.drawDeepSkyObjectsAllMessierAndCaldwell;
		s.drawMinorObjectsLabels = this.drawMinorObjectsLabels;
		s.drawMinorObjectsNamesFont = this.drawMinorObjectsNamesFont;
		s.drawNebulaeContours = this.drawNebulaeContours;
		s.drawMeteorShowers = this.drawMeteorShowers;
		s.drawMeteorShowersOnlyActive = this.drawMeteorShowersOnlyActive;
		s.drawMilkyWayContours = this.drawMilkyWayContours;
		s.fillMilkyWay = this.fillMilkyWay;
		s.drawMilkyWayContoursWithTextures = this.drawMilkyWayContoursWithTextures;
		s.drawBrightNebulaeContoursColor = this.drawBrightNebulaeContoursColor;
		s.drawDarkNebulaeContoursColor = this.drawDarkNebulaeContoursColor;
		s.drawMilkyWayContoursColor = this.drawMilkyWayContoursColor;
		s.fillMilkyWayColor = this.fillMilkyWayColor;
		s.fillBrightNebulaeColor = this.fillBrightNebulaeColor;
		s.fillNebulae = this.fillNebulae;
		s.drawObjectsLimitingMagnitude = this.drawObjectsLimitingMagnitude;
		s.drawMinorObjectsLimitingMagnitude = this.drawMinorObjectsLimitingMagnitude;
		s.drawOcularFieldOfView = this.drawOcularFieldOfView;
		s.drawPlanetsLabels = this.drawPlanetsLabels;
		s.drawLeyend = this.drawLeyend;
		s.drawPlanetsMoonSun = this.drawPlanetsMoonSun;
		s.drawSkyBelowHorizon = this.drawSkyBelowHorizon;
		s.drawCoordinateGridCardinalPoints = this.drawCoordinateGridCardinalPoints;
		s.drawSkyCorrectingLocalHorizon = this.drawSkyCorrectingLocalHorizon;
		s.drawSpaceProbes = this.drawSpaceProbes;
		s.drawStars = this.drawStars;
		s.drawStarsColor = this.drawStarsColor;
		s.drawStarsColors = this.drawStarsColors;
		s.drawStarsLabels = this.drawStarsLabels;
		s.drawStarsGreekSymbols = this.drawStarsGreekSymbols;
		s.drawStarsGreekSymbolsOnlyIfHasProperName = this.drawStarsGreekSymbolsOnlyIfHasProperName;
		s.drawStarsLimitingMagnitude = this.drawStarsLimitingMagnitude;
		s.drawStarsLabelsLimitingMagnitude = this.drawStarsLabelsLimitingMagnitude;
		s.drawStarsNamesFont = this.drawStarsNamesFont;
		s.drawPlanetsNamesFont = this.drawPlanetsNamesFont;
		s.drawStarsSymbols = this.drawStarsSymbols;
		s.drawStarsPositionAngleInDoubles = this.drawStarsPositionAngleInDoubles;
		s.drawSunSpots = this.drawSunSpots;
		s.drawSunSpotsColor = this.drawSunSpotsColor;
		s.drawSunSpotsLabels = this.drawSunSpotsLabels;
		s.drawSuperNovaAndNovaEvents = this.drawSuperNovaAndNovaEvents;
		s.drawSuperNovaEventsNumberOfYears = this.drawSuperNovaEventsNumberOfYears;
		s.drawSuperNovaEventsColor = this.drawSuperNovaEventsColor;
		s.drawTransNeptunianObjects = this.drawTransNeptunianObjects;
		s.drawConstellationLimitsStroke = this.drawConstellationLimitsStroke.clone();
		s.drawCoordinateGridStroke = this.drawCoordinateGridStroke.clone();
		s.drawConstellationStroke = this.drawConstellationStroke.clone();
		s.drawNebulaeStroke = this.drawNebulaeStroke.clone();
		s.drawMilkyWayStroke = this.drawMilkyWayStroke.clone();
		s.drawDeepSkyObjectsStroke = this.drawDeepSkyObjectsStroke.clone();
		s.background = this.background;
		s.drawOcularFieldOfViewColor = this.drawOcularFieldOfViewColor;
		s.anaglyphMode = this.anaglyphMode;
		s.updateTime = this.updateTime;
		s.updateTimeFullUpdate = this.updateTimeFullUpdate;
		s.externalCatalogs = this.externalCatalogs.clone();
		s.externalCatalogNames = this.externalCatalogNames.clone();
		s.drawExternalCatalogs = null;
		if (this.drawExternalCatalogs != null) s.drawExternalCatalogs = this.drawExternalCatalogs.clone();
		
		s.height = this.height;
		s.hourAngle = this.hourAngle;
		s.poleAngle = this.poleAngle;
		if (this.planetRender != null) s.planetRender = this.planetRender.clone();
		s.projection = this.projection;
		if (this.telescope != null) s.telescope = this.telescope.clone();
		if (this.trajectory != null) {
			s.trajectory = new TrajectoryElement[this.trajectory.length];
			for (int i=0; i<this.trajectory.length; i++) {
				s.trajectory[i] = this.trajectory[i].clone();
			}
		}
		s.width = this.width;
		
		s.drawFastLinesMode = this.drawFastLinesMode;
		s.drawHorizonTexture = this.drawHorizonTexture;
		s.limitOfDifferenceOfMagnitudesForVariableStars = this.limitOfDifferenceOfMagnitudesForVariableStars;
		s.limitOfSeparationForDoubleStars = this.limitOfSeparationForDoubleStars;
		s.colorModel = this.colorModel;
		s.drawFaintStars = this.drawFaintStars;
		s.drawFastLabels = this.drawFastLabels;
		s.drawFastLabelsInWideFields = this.drawFastLabelsInWideFields;
		s.drawIcons = this.drawIcons;
		s.drawClever = this.drawClever;
		s.fillGalaxyColor = this.fillGalaxyColor;
		s.fillGlobularColor = this.fillGlobularColor;
		s.fillOpenColor = this.fillOpenColor;
		s.drawWithAntialiasing = this.drawWithAntialiasing;
		s.useSuperScriptForRA = this.useSuperScriptForRA;
		s.drawStarsRealistic = this.drawStarsRealistic;
		s.overlayDSSimageInNextRendering = this.overlayDSSimageInNextRendering;
		s.drawMagnitudeLabels = this.drawMagnitudeLabels;
		s.drawCentralCrux = this.drawCentralCrux;
		return s;
	}
	/**
	 * Returns true if the input object is equals to this instance.
	 */
	public boolean equals(Object o)
	{
		if (o == null) {
			if (this == null) return true;
			return false;
		}
		if (this == null) {
			return false;
		}
		SkyRenderElement s = (SkyRenderElement) o;
		boolean equals = true;

		if (s.centralLatitude != this.centralLatitude) equals = false;
		if (s.centralLongitude != this.centralLongitude) equals = false;
		if (s.coordinateSystem != this.coordinateSystem) equals = false;
		if (s.drawArtificialSatellites != this.drawArtificialSatellites) equals = false;
		if (s.drawArtificialSatellitesIridiumFlares != this.drawArtificialSatellitesIridiumFlares) equals = false;
		if (s.drawArtificialSatellitesOnlyThese == null && this.drawArtificialSatellitesOnlyThese != null) equals = false;
		if (s.drawArtificialSatellitesOnlyThese != null && this.drawArtificialSatellitesOnlyThese == null) equals = false;
		if (s.drawArtificialSatellitesOnlyThese != null && this.drawArtificialSatellitesOnlyThese != null && 
				!s.drawArtificialSatellitesOnlyThese.equals(this.drawArtificialSatellitesOnlyThese)) equals = false;
		if (s.drawAsteroids != this.drawAsteroids) equals = false;
		if (s.drawComets != this.drawComets) equals = false;
		if (s.drawOcularFieldOfViewColor != this.drawOcularFieldOfViewColor) equals = false;
		if (s.enableTransparentColors != this.enableTransparentColors) equals = false;
		if (s.drawConstellationContours != this.drawConstellationContours) equals = false;
		if (s.drawConstellationContoursMarginBetweenLineAndStar != this.drawConstellationContoursMarginBetweenLineAndStar) equals = false;
		if (s.drawConstellationContoursColor != this.drawConstellationContoursColor) equals = false;
		if (s.drawConstellationLimits != this.drawConstellationLimits) equals = false;
		if (s.drawConstellationLimitsColor != this.drawConstellationLimitsColor) equals = false;
		if (s.drawConstellationNames != this.drawConstellationNames) equals = false;
		if (s.drawConstellationNamesUppercase != this.drawConstellationNamesUppercase) equals = false;
		if (s.drawConstellationNamesColor != this.drawConstellationNamesColor) equals = false;
		if (s.drawConstellationNamesFont != this.drawConstellationNamesFont) equals = false;
		if (s.drawConstellationNamesType != this.drawConstellationNamesType) equals = false;
		if (s.drawLeyend != this.drawLeyend) equals = false;
		if (s.drawCoordinateGrid != this.drawCoordinateGrid) equals = false;
		if (s.drawCoordinateGridCelestialPoints != this.drawCoordinateGridCelestialPoints) equals = false;
		if (s.drawCoordinateGridHighZoom != this.drawCoordinateGridHighZoom) equals = false;
		if (s.drawExternalGrid != this.drawExternalGrid) equals = false;
		if (s.drawCoordinateGridColor != this.drawCoordinateGridColor) equals = false;
		if (s.drawCoordinateGridEcliptic != this.drawCoordinateGridEcliptic) equals = false;
		if (s.drawCoordinateGridEclipticLabels != this.drawCoordinateGridEclipticLabels) equals = false;
		if (s.drawCoordinateGridEclipticColor != this.drawCoordinateGridEclipticColor) equals = false;
		if (s.drawCoordinateGridFont != this.drawCoordinateGridFont) equals = false;
		if (s.drawCoordinateGridLabels != this.drawCoordinateGridLabels) equals = false;
		if (s.drawDeepSkyObjects != this.drawDeepSkyObjects) equals = false;
		if (s.drawDeepSkyObjectsTextures != this.drawDeepSkyObjectsTextures) equals = false;
		if (s.drawDeepSkyObjectsColor != this.drawDeepSkyObjectsColor) equals = false;
		if (s.drawMeteorShowersColor != this.drawMeteorShowersColor) equals = false;
		if (s.drawDeepSkyObjectsLabels != this.drawDeepSkyObjectsLabels) equals = false;
		if (s.drawDeepSkyObjectsNamesFont != this.drawDeepSkyObjectsNamesFont) equals = false;
		if (s.drawDeepSkyObjectsOnlyMessier != this.drawDeepSkyObjectsOnlyMessier) equals = false;
		if (s.drawDeepSkyObjectsAllMessierAndCaldwell != this.drawDeepSkyObjectsAllMessierAndCaldwell) equals = false;
		if (s.drawMinorObjectsLabels != this.drawMinorObjectsLabels) equals = false;
		if (s.drawFaintStars != this.drawFaintStars) equals = false;
		if (s.drawMinorObjectsNamesFont != this.drawMinorObjectsNamesFont) equals = false;
		if (s.drawNebulaeContours != this.drawNebulaeContours) equals = false;
		if (s.drawMeteorShowers != this.drawMeteorShowers) equals = false;
		if (s.drawMeteorShowersOnlyActive != this.drawMeteorShowersOnlyActive) equals = false;
		if (s.drawMilkyWayContours != this.drawMilkyWayContours) equals = false;
		if (s.fillMilkyWay != this.fillMilkyWay) equals = false;
		if (s.drawMilkyWayContoursWithTextures != this.drawMilkyWayContoursWithTextures) equals = false;
		if (s.drawBrightNebulaeContoursColor != this.drawBrightNebulaeContoursColor) equals = false;
		if (s.drawDarkNebulaeContoursColor != this.drawDarkNebulaeContoursColor) equals = false;
		if (s.drawMilkyWayContoursColor != this.drawMilkyWayContoursColor) equals = false;
		if (s.fillMilkyWayColor != this.fillMilkyWayColor) equals = false;
		if (s.fillNebulae != this.fillNebulae) equals = false;
		if (s.fillBrightNebulaeColor != this.fillBrightNebulaeColor) equals = false;
		if (s.drawObjectsLimitingMagnitude != this.drawObjectsLimitingMagnitude) equals = false;
		if (s.drawMinorObjectsLimitingMagnitude != this.drawMinorObjectsLimitingMagnitude) equals = false;
		if (s.drawOcularFieldOfView != this.drawOcularFieldOfView) equals = false;
		if (s.drawPlanetsLabels != this.drawPlanetsLabels) equals = false;
		if (s.drawPlanetsMoonSun != this.drawPlanetsMoonSun) equals = false;
		if (s.drawSkyBelowHorizon != this.drawSkyBelowHorizon) equals = false;
		if (s.drawCoordinateGridCardinalPoints != this.drawCoordinateGridCardinalPoints) equals = false;
		if (s.drawSkyCorrectingLocalHorizon != this.drawSkyCorrectingLocalHorizon) equals = false;
		if (s.drawSpaceProbes != this.drawSpaceProbes) equals = false;
		if (s.drawStars != this.drawStars) equals = false;
		if (s.drawStarsColor != this.drawStarsColor) equals = false;
		if (s.background != this.background) equals = false;
		if (s.drawStarsColors != this.drawStarsColors) equals = false;
		if (s.drawStarsLabels != this.drawStarsLabels) equals = false;
		if (s.drawStarsGreekSymbols != this.drawStarsGreekSymbols) equals = false;
		if (s.drawStarsGreekSymbolsOnlyIfHasProperName != this.drawStarsGreekSymbolsOnlyIfHasProperName) equals = false;
		if (s.drawStarsLimitingMagnitude != this.drawStarsLimitingMagnitude) equals = false;
		if (s.drawStarsLabelsLimitingMagnitude != this.drawStarsLabelsLimitingMagnitude) equals = false;
		if (s.drawStarsNamesFont != this.drawStarsNamesFont) equals = false;
		if (s.drawPlanetsNamesFont != this.drawPlanetsNamesFont) equals = false;
		if (s.drawStarsSymbols != this.drawStarsSymbols) equals = false;
		if (s.drawStarsPositionAngleInDoubles != this.drawStarsPositionAngleInDoubles) equals = false;
		if (s.drawSunSpots != this.drawSunSpots) equals = false;
		if (s.drawSunSpotsColor != this.drawSunSpotsColor) equals = false;
		if (s.drawSunSpotsLabels != this.drawSunSpotsLabels) equals = false;
		if (s.drawSuperNovaAndNovaEvents != this.drawSuperNovaAndNovaEvents) equals = false;
		if (s.drawSuperNovaEventsNumberOfYears != this.drawSuperNovaEventsNumberOfYears) equals = false;
		if (s.drawSuperNovaEventsColor != this.drawSuperNovaEventsColor) equals = false;
		if (s.drawTransNeptunianObjects != this.drawTransNeptunianObjects) equals = false;
		if (s.drawFastLinesMode != this.drawFastLinesMode) equals = false;
		if (s.drawHorizonTexture != this.drawHorizonTexture) equals = false;
		
		if (s.colorModel != this.colorModel) equals = false;
		if (s.height != this.height) equals = false;
		if (s.hourAngle != this.hourAngle) equals = false;
		if (s.poleAngle != this.poleAngle) equals = false;
		if (s.planetRender.equals(this.planetRender)) equals = false;
		if (s.projection != this.projection) equals = false;
		if (s.telescope.equals(this.telescope)) equals = false;
		if (s.trajectory.equals(this.trajectory)) equals = false;
		if (s.width != this.width) equals = false;

		if (!s.drawNebulaeStroke.equals(this.drawNebulaeStroke)) equals = false;
		if (!s.drawMilkyWayStroke.equals(this.drawMilkyWayStroke)) equals = false;
		if (!s.drawDeepSkyObjectsStroke.equals(this.drawDeepSkyObjectsStroke)) equals = false;
		if (!s.drawConstellationLimitsStroke.equals(this.drawConstellationLimitsStroke)) equals = false;
		if (!s.drawCoordinateGridStroke.equals(this.drawCoordinateGridStroke)) equals = false;
		if (!s.drawConstellationStroke.equals(this.drawConstellationStroke)) equals = false;

		if (s.limitOfDifferenceOfMagnitudesForVariableStars != this.limitOfDifferenceOfMagnitudesForVariableStars) equals = false;
		if (s.limitOfSeparationForDoubleStars != this.limitOfSeparationForDoubleStars) equals = false;

		if (s.drawFastLabels != this.drawFastLabels) equals = false;
		if (s.drawFastLabelsInWideFields != this.drawFastLabelsInWideFields) equals = false;
		if (s.drawIcons != this.drawIcons) equals = false;
		if (s.drawClever != this.drawClever) equals = false;
		if (s.drawWithAntialiasing != this.drawWithAntialiasing) equals = false;

		if (s.fillGalaxyColor != this.fillGalaxyColor) equals = false;
		if (s.fillGlobularColor != this.fillGlobularColor) equals = false;
		if (s.fillOpenColor != this.fillOpenColor) equals = false;
		if (s.useSuperScriptForRA != this.useSuperScriptForRA) equals = false;
		if (s.anaglyphMode != this.anaglyphMode) equals = false;
		if (s.updateTime != this.updateTime) equals = false;
		if (s.updateTimeFullUpdate != this.updateTimeFullUpdate) equals = false;
		if (!DataSet.sameArrayValues(s.externalCatalogs, this.externalCatalogs)) equals = false;
		if (!DataSet.sameArrayValues(s.externalCatalogNames, this.externalCatalogNames)) equals = false;
		if (s.drawExternalCatalogs == null && this.drawExternalCatalogs != null) {
			equals = false;
		} else {
			if (s.drawExternalCatalogs != null && this.drawExternalCatalogs == null) {
				equals = false;
			} else {
				if (s.drawExternalCatalogs != null && s.drawExternalCatalogs.length == this.drawExternalCatalogs.length) {
					for (int i=0; i<s.drawExternalCatalogs.length; i++) {
						if (s.drawExternalCatalogs[i] != this.drawExternalCatalogs[i]) equals = false;						
					}
				} else {
					equals = false;					
				}
			}			
		}
		if (s.drawStarsRealistic != this.drawStarsRealistic) equals = false;
		if (s.overlayDSSimageInNextRendering != this.overlayDSSimageInNextRendering) equals = false;
		if (s.drawMagnitudeLabels != this.drawMagnitudeLabels) equals = false;
		if (s.drawCentralCrux != this.drawCentralCrux) equals = false;
		return equals;
	}

	/**
	 * The set of available color modes to render the sky.
	 * Each of them will produce a default set of recommended
	 * colors.
	 */
	public static enum COLOR_MODE {
		/** White background color mode. */
		WHITE_BACKGROUND,
		/** Black background color mode. */
		BLACK_BACKGROUND,
		/** Night mode color scheme. */
		NIGHT_MODE,
		/** White background and simple green-red or red_cyan color anaglyph mode.
		 * In case the anaglyph mode is selected to one of these two, color mode
		 * should be set to this value for an adequate anaglyph effect. */
		WHITE_BACKGROUND_SIMPLE_GREEN_RED_OR_RED_CYAN_ANAGLYPH,
		/** Mode optimized for printing. */
		PRINT_MODE};
	
	private COLOR_MODE colorModel;

	/**
	 * The set of color modes as Strings.
	 */
	public static final String[] COLOR_MODES = new String[] {
		"White background", "Black background", "Night mode", "White background simple anaglyph",
		"Print mode"
	};
	
	/**
	 * Returns the current color model.
	 * @return Color model id constant.
	 */
	public COLOR_MODE getColorMode() { return this.colorModel; }
	/**
	 * Sets the color model. The colors for the different objects are adjusted.
	 * Anaglyph mode should be selected before calling this method.
	 * @param id Color model id constant.
	 */
	public void setColorMode(COLOR_MODE id) {
		if (this.colorModel == COLOR_MODE.NIGHT_MODE)
			drawStarsRealistic = REALISTIC_STARS.STARRED;
		switch (id) {
		case PRINT_MODE:
			this.colorModel = id;
			drawStarsColor = 192<<24 | 0<<16 | 0<<8 | 0;
			if (this.drawStarsRealistic == REALISTIC_STARS.NONE_CUTE)
				drawStarsColor = 255<<24 | 0<<16 | 0<<8 | 0;
			drawDeepSkyObjectsColor = 255<<24 | 0<<16 | 0<<8 | 0;
			drawMeteorShowersColor = 255<<24 | 164<<16 | 64<<8 | 128;
			drawConstellationContoursColor = 128<<24 | 164<<16 | 164<<8 | 164;
			drawConstellationLimitsColor = 192<<24 | 128<<16 | 128<<8 | 128;
			drawConstellationNamesColor = 212<<24 | 0<<16 | 80<<8 | 0;
			drawBrightNebulaeContoursColor = 192<<24 | 92<<16 | 92<<8 | 255;
			drawDarkNebulaeContoursColor = 64<<24 | 64<<16 | 64<<8 | 64;
			drawMilkyWayContoursColor = 192<<24 | 80<<16 | 80<<8 | 80;
			fillMilkyWayColor = 255<<24 | 220<<16 | 220<<8 | 244;
//			fillMilkyWayColor = 255<<24 | 240<<16 | 240<<8 | 244;
			if (this.anaglyphMode == ANAGLYPH_COLOR_MODE.DUBOIS_AMBER_BLUE ||
					this.anaglyphMode == ANAGLYPH_COLOR_MODE.DUBOIS_GREEN_MAGENTA ||
					this.anaglyphMode == ANAGLYPH_COLOR_MODE.DUBOIS_RED_CYAN) {
				drawMilkyWayContoursColor = 255<<24 | 132<<16 | 132<<8 | 132;
				fillMilkyWayColor = 255<<24 | 184<<16 | 184<<8 | 170;				
			}
			drawCoordinateGridColor = 224<<24 | 0<<16 | 0<<8 | 0;
			drawOcularFieldOfViewColor = 255<<24 | 0<<16 | 0<<8 | 0;
			drawCoordinateGridEclipticColor = 224<<24 | 255<<16 | 0<<8 | 0;
			drawSunSpotsColor = 255<<24 | 0<<16 | 0<<8 | 0;
			fillGalaxyColor = 128<<24 | 220<<16 | 0<<8 | 0;
			fillGlobularColor = 128<<24 | 220<<16 | 220<<8 | 0;
			fillOpenColor = 128<<24 | 220<<16 | 220<<8 | 0;
			drawStarsColors = false;			
			this.drawMilkyWayContoursWithTextures = MILKY_WAY_TEXTURE.NO_TEXTURE;
			background = 255<<24 | 255<<16 | 255<<8 | 255;
			drawSuperNovaEventsColor = 255<<24 | 255<<16 | 0<<8 | 0;
			
			drawNebulaeStroke =  new JPARSECStroke(JPARSECStroke.STROKE_DEFAULT_LINE_THIN, 1.0f);
			drawMilkyWayStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			drawConstellationStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THICK;
			drawConstellationLimitsStroke = new JPARSECStroke(JPARSECStroke.STROKE_LINES_LARGE, new float[] {8, 3}, 0);
			drawCoordinateGridStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			break;
		case WHITE_BACKGROUND:
			this.colorModel = id;
			drawStarsColor = 192<<24 | 0<<16 | 0<<8 | 0;
			drawDeepSkyObjectsColor = 192<<24 | 0<<16 | 0<<8 | 0;
			drawMeteorShowersColor = 192<<24 | 96<<16 | 96<<8 | 128;
			drawConstellationContoursColor = 192<<24 | 48<<16 | 138<<8 | 138;
//			drawConstellationContoursColor = 255<<24 | 156<<16 | 156<<8 | 156;
			drawConstellationLimitsColor = 192<<24 | 128<<16 | 128<<8 | 128;
			drawConstellationNamesColor = 212<<24 | 0<<16 | 96<<8 | 0;
			drawBrightNebulaeContoursColor = 128<<24 | 96<<16 | 96<<8 | 240;
			drawDarkNebulaeContoursColor = 64<<24 | 64<<16 | 64<<8 | 64;
			drawMilkyWayContoursColor = 240<<24 | 128<<16 | 128<<8 | 192;
			fillMilkyWayColor = 255<<24 | 220<<16 | 220<<8 | 244;
//			fillMilkyWayColor = 128<<24 | 240<<16 | 240<<8 | 246;
			if (this.anaglyphMode.isDubois()) {
				//drawMilkyWayContoursColor = 240<<24 | 132<<16 | 132<<8 | 132;
				fillMilkyWayColor = 255<<24 | 190<<16 | 190<<8 | 220;				
			}
			drawCoordinateGridColor = 192<<24 | 0<<16 | 0<<8 | 0;
			drawOcularFieldOfViewColor = 255<<24 | 0<<16 | 0<<8 | 0;
			drawCoordinateGridEclipticColor = 192<<24 | 255<<16 | 0<<8 | 0;
			drawSunSpotsColor = 255<<24 | 0<<16 | 0<<8 | 0;
			fillGalaxyColor = 80<<24 | 0<<16 | 0<<8 | 0;
			fillGlobularColor = 56<<24 | 255<<16 | 255<<8 | 0;
			fillOpenColor =56<<24 | 255<<16 | 255<<8 | 0;
			drawStarsColors = false;			
			this.drawMilkyWayContoursWithTextures = MILKY_WAY_TEXTURE.NO_TEXTURE;
			background = 255<<24 | 255<<16 | 255<<8 | 255;
			drawSuperNovaEventsColor = 255<<24 | 255<<16 | 0<<8 | 0;

			drawNebulaeStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			drawMilkyWayStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			drawConstellationStroke = JPARSECStroke.STROKE_DEFAULT_LINE;
			drawConstellationLimitsStroke = new JPARSECStroke(JPARSECStroke.STROKE_LINES_LARGE, new float[] {8, 3}, 0);
			drawCoordinateGridStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN; 
			break;
		case BLACK_BACKGROUND:
			this.colorModel = id;
			drawStarsColor = 156<<24 | 255<<16 | 255<<8 | 255;
			drawStarsColors = true;
			drawDeepSkyObjectsColor = 192<<24 | 255<<16 | 255<<8 | 255;
			drawMeteorShowersColor = 164<<24 | 96<<16 | 96<<8 | 0;
			//drawConstellationContoursColor = 255<<24 | 92<<16 | 92<<8 | 92;
			drawConstellationContoursColor = 192<<24 | 32<<16 | 92<<8 | 92;
			drawConstellationLimitsColor = 64<<24 | 192<<16 | 192<<8 | 192;
			drawConstellationNamesColor = 128<<24 | 255<<16 | 255<<8 | 255;
			drawBrightNebulaeContoursColor = 128<<24 | 120<<16 | 120<<8 | 255;
			drawDarkNebulaeContoursColor = 64<<24 | 64<<16 | 64<<8 | 64;
			drawMilkyWayContoursColor = 128<<24 | 128<<16 | 128<<8 | 128;
			fillMilkyWayColor = 128<<24 | 32<<16 | 32<<8 | 48;
			drawCoordinateGridColor = 128<<24 | 170<<16 | 170<<8 | 170;
			drawCoordinateGridEclipticColor = 128<<24 | 255<<16 | 0<<8 | 0;
			fillGalaxyColor = 128<<24 | 255<<16 | 255<<8 | 255;
			fillGlobularColor = -1; //128<<24 | 0<<16 | 0<<8 | 0;
			fillOpenColor = -1; //128<<24 | 0<<16 | 0<<8 | 0;
			//background = 255<<24 | 5<<16 | 5<<8 | 5;
			background = 255<<24 | 5<<16 | 5<<8 | 12;
			drawOcularFieldOfViewColor = 255<<24 | 255<<16 | 255<<8 | 255;
			drawSunSpotsColor = 255<<24 | 72<<16 | 72<<8 | 72;
			drawSuperNovaEventsColor = 255<<24 | 255<<16 | 0<<8 | 0;

			drawNebulaeStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			drawMilkyWayStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			drawConstellationStroke = JPARSECStroke.STROKE_DEFAULT_LINE;
			drawConstellationLimitsStroke = new JPARSECStroke(JPARSECStroke.STROKE_LINES_LARGE, new float[] {8, 3}, 0);
			drawCoordinateGridStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			
			break;
		case NIGHT_MODE:
			this.colorModel = id;
			drawStarsColor = 128<<24 | 255<<16 | 0<<8 | 0;
			drawStarsColors = false;
			drawDeepSkyObjectsColor = 240<<24 | 128<<16 | 80<<8 | 32;
			drawMeteorShowersColor = 220<<24 | 96<<16 | 32<<8 | 64;
			drawConstellationContoursColor = 255<<24 | 112<<16 | 0<<8 | 0;
			drawConstellationLimitsColor = 64<<24 | 192<<16 | 0<<8 | 0;
			drawConstellationNamesColor = 128<<24 | 255<<16 | 0<<8 | 0;
			drawBrightNebulaeContoursColor = 128<<24 | 120<<16 | 0<<8 | 0;
			drawDarkNebulaeContoursColor = 32<<24 | 128<<16 | 64<<8 | 64;
			drawMilkyWayContoursColor = 128<<24 | 128<<16 | 0<<8 | 0;
			fillMilkyWayColor = 128<<24 | 64<<16 | 0<<8 | 0;
			if (this.anaglyphMode == ANAGLYPH_COLOR_MODE.DUBOIS_AMBER_BLUE ||
					this.anaglyphMode == ANAGLYPH_COLOR_MODE.DUBOIS_GREEN_MAGENTA ||
					this.anaglyphMode == ANAGLYPH_COLOR_MODE.DUBOIS_RED_CYAN) {
				drawMilkyWayContoursColor = 128<<24 | 188<<16 | 0<<8 | 0;
				fillMilkyWayColor = 128<<24 | 124<<16 | 0<<8 | 0;				
			}
			drawCoordinateGridColor = 112<<24 | 164<<16 | 0<<8 | 0;
			drawCoordinateGridEclipticColor = 128<<24 | 255<<16 | 0<<8 | 0;
			fillGalaxyColor = 80<<24 | 128<<16 | 0<<8 | 0;
			fillGlobularColor = -1; //50<<24 | 128<<16 | 0<<8 | 0;
			fillOpenColor = -1; //50<<24 | 128<<16 | 0<<8 | 0;
			background = 255<<24 | 0<<16 | 0<<8 | 0;
			drawOcularFieldOfViewColor = 255<<24 | 255<<16 | 128<<8 | 128;
			drawSunSpotsColor = 255<<24 | 96<<16 | 30<<8 | 30;
			drawSuperNovaEventsColor = 255<<24 | 255<<16 | 0<<8 | 0;

			drawNebulaeStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			drawMilkyWayStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			drawConstellationStroke = JPARSECStroke.STROKE_DEFAULT_LINE;
			drawConstellationLimitsStroke = new JPARSECStroke(JPARSECStroke.STROKE_LINES_LARGE, new float[] {8, 3}, 0);
			drawCoordinateGridStroke = JPARSECStroke.STROKE_DEFAULT_LINE;
			drawStarsRealistic = REALISTIC_STARS.SPIKED;
			drawDeepSkyObjectsTextures = false;
			break;
		case WHITE_BACKGROUND_SIMPLE_GREEN_RED_OR_RED_CYAN_ANAGLYPH:
			this.colorModel = id;
			drawStarsColor = 156<<24 | 0<<16 | 0<<8 | 0;
			drawDeepSkyObjectsColor = 156<<24 | 0<<16 | 187<<8 | 0;
			drawMeteorShowersColor = 192<<24 | 192<<16 | 164<<8 | 0;
			drawConstellationContoursColor = 90<<24 | 128<<16 | 128<<8 | 128;
			drawConstellationLimitsColor = 192<<24 | 128<<16 | 128<<8 | 128;
			drawConstellationNamesColor = 128<<24 | 0<<16 | 0<<8 | 0;
			drawBrightNebulaeContoursColor = 128<<24 | 120<<16 | 120<<8 | 255;
			drawDarkNebulaeContoursColor = 128<<24 | 120<<16 | 120<<8 | 120;
			drawMilkyWayContoursColor = 1<<24 | 180<<16 | 180<<8 | 255;
			fillMilkyWayColor = 255<<24 | 212<<16 | 212<<8 | 218;
			drawCoordinateGridColor = 156<<24 | 32<<16 | 32<<8 | 32;
			drawCoordinateGridEclipticColor = 128<<24 | 255<<16 | 0<<8 | 0;
			drawSunSpotsColor = 255<<24 | 0<<16 | 0<<8 | 0;
			fillGalaxyColor = 80<<24 | 0<<16 | 0<<8 | 0;
			fillGlobularColor = -1; //56<<24 | 255<<16 | 255<<8 | 0;
			fillOpenColor = -1; //56<<24 | 255<<16 | 255<<8 | 0;
			drawStarsColors = false;			
			background = 255<<24 | 222<<16 | 222<<8 | 222;
			drawOcularFieldOfViewColor = 255<<24 | 0<<16 | 0<<8 | 0;
			drawSuperNovaEventsColor = 255<<24 | 255<<16 | 0<<8 | 0;

			drawNebulaeStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			drawMilkyWayStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			drawConstellationStroke = JPARSECStroke.STROKE_DEFAULT_LINE;
			drawConstellationLimitsStroke = new JPARSECStroke(JPARSECStroke.STROKE_LINES_LARGE, new float[] {8, 3}, 0);
			drawCoordinateGridStroke = JPARSECStroke.STROKE_DEFAULT_LINE_THIN;
			break;
		}
	}

	/**
	 * Sets the star labels to the star names in a given language in case
	 * the selected labels are {@link STAR_LABELS#ONLY_PROPER_NAME} and the
	 * current selected language is different. Currently only change to
	 * Spanish proper names is supported.
	 */
	public void setStarLabelsAccordingtoCurrentLanguage() {
		if (Translate.getDefaultLanguage() == LANGUAGE.SPANISH && drawStarsLabels == STAR_LABELS.ONLY_PROPER_NAME)
			drawStarsLabels = STAR_LABELS.ONLY_PROPER_NAME_SPANISH;
	}
}
