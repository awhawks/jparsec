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
package jparsec.util;

import jparsec.graph.DataSet;
import jparsec.io.FileIO;

/**
 * Version of the library.
 * <P>
 * This class provides information about the current version of the library, by generating
 * a {@linkplain Module} object with the dependencies and their versions. This information
 * is used by the JPARSEC Manager tool to automatically update the library and dependencies
 * when the version of a given dependency changes.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Version
{
	// private constructor so that this class cannot be instantiated.
	private Version() {}
	
	/**
	 * Name of the package.
	 */
	public static final String PACKAGE_NAME = "JPARSEC";
	
	/**
	 * Current version of the library.
	 */
	public static final String VERSION_ID = getVersionNumber();

	/**
	 * Author of the library.
	 */
	public static final String AUTHOR = "Tom\u00e1s Alonso Albi, OAN (Spain)";

	/**
	 * Returns the JPARSEC library release data.
	 * @return The release data.
	 */
	public static Module getVersion()
	{
		String header = Version.PACKAGE_NAME+" "+Version.VERSION_ID+" Release Notes";
		String header2 = "";
		for (int i=0;i<header.length();i++)
		{
			header2 += "-";
		}
		String release[] = new String[] {
				header,
				header2,
				"",
				"1.60: Added SpectraChart, SkyChart, and VISADChart in jparsec.graphic.chart. Removed FreeHEP as dependency. New image resize functions in Picture. Serialization of Spectrum30m.\n"+
				"1.70: Labels can now be added in physical coordinates into a JFreeChart and SGT objects. TextLabel allows latex formulas, Greek capital characters, and text rotation. Gildas30m can now read spectra faster, including OTF maps and WILMA data. Spectra can be processed. Moshier and cds.astro packages are no longer required. Minor fixes and code cleanup.\n"+
				"1.80: New development cycle and bug fix release. Code cleanup, etc.",
				"      1.84: Implementation of SDP4/SGP4 models for artificial satellites\n"+
				       "(including Iridium flares), and lunar librations for Moshier and\n"+
						"JPL ephemerides (Nov 7, 2011).",
				"      1.841: Corrected the frequency/velocity output in Spectrum30m class\n"+
						"to always match Gildas.",
				"      1.842: Spectrum30m class now supports image frequency (Nov 20, 2011).\n",
				"      1.843: Spectrum30m class now writes any number of spectra per 30m file\n"+
						"and with any number of channels (Nov 28, 2011).\n",
				"      1.844: Some details fixed with the border of the wedge in grid charts.\n"+
						"LMVCube can now resample cubes also in velocity axis (Dec 9, 2011).",
				"      1.845: SpectraChart and SkyChart improved (Dec 15, 2011).\n"+
						"Method MeasureElement.toString() improved.",
				"      1.846: CreateVISADIsoSurfaceChart, SpectraChart, and other components improved\n"+
						"(Dec 22, 2011).",
				"      1.847: CreateVISADIsoSurfaceChart, SpectraChart, and other components improved,\n"+
						"with translation finished. Documentation revised (Dec 31, 2011).",
				"      1.85: Some bug fixes in bar charts, and more functions in DataSet class (Jan 30, 2012).\n"+
						"Fixed a bug with the number of channels when exporting spectra to 30m format.\n"+
						"New classes Logger and Feed for logging and rss feed operations.",
				"      1.86: Better rendering of natural satellites and planetary rings (Mar 2, 2012).\n"+
						"Improved ephemerides for stars, including barycentric light-time calculations.\n"+
						"Code cleanup to use different logging levels.\n"+
						"Updated time algorithms using latest version of SOFA.\n"+
						"A problem with spline interpolation was solved.",
				"      1.861: Prediction of variables stars maxima/minima from AAVSO for 2012-3 (Mar 15, 2012).",
				"      1.87: New packages with basic models and Gaussian/polynomial fitting to keep common code in one place (Apr 17, 2012).\n"+
						"The representation of measures (MeasureElement.toString()) has been fixed.\n"+
						"New better logic for the representation of regressions in JFreeCharts.",
				"      1.88: New features (Apr 27, 2012):\n"+
						"Graphics2D calls completely abstracted, first step towards Android platform support.\n"+
						"Class Regression extended with polynomial fitting.\n"+
						"Some minor bug fixes in sky rendering.",
				"      1.89: New features (Jun 23, 2012):\n"+
						"New planetary rendering algorithm: faster, very accurate, sub-pixel precision, Dubois 3d anaglyph, new textures, ....\n"+
						"Complete revision of sky and planetary rendering: sky rotation, inversion of projections, bug fixes, ...",
				"1.9: New major version (August 31, 2012) featuring:\n"+
						"Android platform officially supported for sky and planetary rendering.\n"+
						"JPARSEC is now completely thread-safe.\n"+
						"New class DataBase to hold data with flexibility and optimize memory consumption.\n"+
						"Output RA/DEC can now be obtained using matrices with IAU2006 class. Polar motion implemented and EOP parameters sections improved.\n"+
						"Refraction correction in RA/DEC implemented, with different refraction models for optical and radio wavelengths.\n"+
						"Precision of ephemerides validated to better than 0.1 mas (milliarcsecond).\n"+
						"Ephemerides and renderings can be calculated for an observed at any position in the Solar System.\n"+
						"New EventReport class to calculate lots of different astronomical events in RSS feed format.\n"+
						"Lots of fixes/improvements in sky and planetary rendering",
				"      1.91: New features (Oct 8, 2012):\n"+
						"Realistic sky rendering using textures for stars and deep sky objects. Better performance and bug fixes.\n"+
						"Android performance improved when using DataBase class, still too slow.",
				"      1.92: New features and fixes (Nov 20, 2012):\n"+
						"Much better quality, 3x performance, and 3x less memory consumption in sky rendering.\n"+
						"Android port fully operational, all bugs fixed.\n"+
						"Sky rendering optimized for output to vector graphics formats of any size (posters, sky charts).\n"+
						"The calculation of planetary events were extremely slow.",
				"      1.93: New features and bug fixes (Jan 21, 2013):\n"+
						"Ephemerides and sky rendering supported for observers in the surface of other planets or natural satellites, comets, and asteroids.\n"+
						"Satellite rendering also supports other planets.\n"+
						"Little improvements in visual quality and performance in sky rendering, with some bug fixes.\n"+
						"Some errors corrected in calculations of planetary events. Added the event of Saturn rings in edge-on view, Lunar-X, NEOs close approachs, and comets/asteroids visible to naked eye.\n"+
						"Profiling applied to ephemerides calculations (maths). Many calculations are now 4 times faster.\n"+
						"Update of orbital elements and other dependencies.",
				"      1.94: New features and bug fixes (Feb 14, 2013):\n"+
						"Less memory consumption when reading files, noticeable in Android.\n"+
						"Some bug fixes in sky/planetary rendering. 5% better performance, seems impossible to improve it more.\n"+
						"More realistic rendering of Saturn/Uranus rings (unlit region) and shadows.\n"+
						"Non-critical bug solved when reading very large .30m files. Automatic reduction (gaussian decomposition) improved.\n"+
						"Update of orbital elements and other dependencies.",
				"      1.95: New features (Mar 22, 2013):\n"+
						"20% Better performance and less memory consumption in sky rendering. JPARSEC is now Android-ready.\n"+
						"Constellation figures can be shown for a variety of cultures, with complete translations of names into/from English.\n"+
						"Bug fixed when showing trajectories of comets/asteroids.\n"+
						"Bug fixed in RiseSetTransit class. For some bodies and critical dates the times were wrong or referred to incorrect dates.\n"+
						"Virtual observatory tools now uses CDS libraries as dependency, with less files. AstroRuntime removed, so Plastic is now unsupported.\n"+
						"Lunar and solar eclipse maps supported (see RenderEclipse class). Bessel elements included for all solar eclipses from 3000 B.C. to 3000 A.D. Bug corrected in LunarEclipse class.",
				"      1.96: Bug fixes (Apr 17, 2013):\n"+
						"pyGildas extension in JPARSEC was not working for Gildas version > dec10c. This is now fixed.\n"+
						"Bug fixes and better quality in solar/lunar eclipse maps, and sky rendering (Earth's meridian wrongly calculated in sky rendering from other planets).\n"+
						"Sky rendering 10% faster ('fast lines' are now faster, star images optimized).",
				"      1.97: Bug fixes and new features (May 30, 2013):\n"+
						"Physical constants updated to CODATA 2010.\n"+
						"MeasureElement improved, supporting unit conversion for non-flux like magnitudes, and including main physical constants with uncertainties.\n"+
						"cds.astro replaced to use CODATA 2010 physical constants, so that unit conversions are now accurate.\n"+
						"Some classes useful for astrophysics has been extended (MeasureElement, FitsIO and binary tables, ...), with the inclusion of new classes (Table, PCAElement) to operate with tables/images.\n"+
						"Main time formatting methods in TimeFormat class no longer depends on Java's DateFormat classes.\n"+
						"Sky rendering 20% faster, with bug fixes: better icons for moon phases, symbols for plan.neb. and glob.clust. corrected, corrected positions for sun spots when image is inverted, screen coord. of planetographic positions now fully accurate, and many other minor bugs. \n"+
						"SDP8/SGP8 models for artificial satellites implemented.\n"+
						"IAU2006 contained some bugs, DataSet.isSorted was wrong, and CatalogRead had some deficiencies to help reading JPL/COLOGNE catalogs.\n"+
						"SolarEclipse and LunarEclipse classes can now predict eclipses of any other satellite orbiting its mother planet, not only the Earth-Moon-Sun combination.",
				"      1.98: New features, and some performance improvementes and some fixes (Jul 26, 2013):\n"+
						"Nomenclature of planetary features and SkyView list of surveys updated.\n"+
						"Conversion to/from galactic coordinates not completely accurate in some cases.\n"+
						"Some fixes (some bright asteroids missed, star names, pdf export), memory improvements to sky rendering.\n"+
						"Sky Rendering 10% faster in desktop thanks to the use of BufferStrategy and Canvas, although performance is similar since star textures are drawn with greater sizes.\n"+
						"Android version 40-100% faster after profiling, with bug fix when showing the leyend. Implementation of JPARSEC seems completed, Android example shows some options.\n"+
						"Some fixes in the solar eclipse charts.\n"+
						"Fixed a bug in rise/set/transit times referred to current date for certain critical situations.\n"+
						"Ephemerides for Pluto's satellite Charon supported. Pluto's position corrected from barycenter to Pluto's body center.\n"+
						"New catalog of extrasolar planets directly available in the SkyChart component.\n"+
						"Bug fixes in FitsIO class.\n"+
						"Complete telescope control for a variety of Meade and Celestron models and digital SLR cameras (among other hardware currently supported as 'virtual' devices) is provided in the new jparsec.io.device and jparsec.io.device.implementation packages.\n"+
						"A complete wiki page is available at http://conga.oan.es/~alonso/doku.php?id=jparsec_wiki to help users getting started with JPARSEC code.",
				"      1.99: Mainly a bug fix release (Sep 26, 2013):\n"+
						"Sky rendering >10% faster in PC mode (60% faster when filling Milky Way with a color or using the accurate label positioning algorithm) thanks to the use of WritableRaster instead of get/set RGB methods in class AWTGraphics.\n"+
						"Fixed a bug in rise/set/transit times referred to current date for certain critical situations.\n"+
						"Fixed a bug in OrbitEphem class related to the use of a wrong model for the magnitudes of comets.\n"+
						"Several bug fixes/improvements in SkyChart, Draw, DataSet, CreateChart (axis inversion), Difraction, Interpolation, Precession, ADSQuery, ReadFile, FitsIO, and FitsBinaryTable classes.\n"+
						"Some new useful methods in class jparsec.astronomy.Star.\n"+
						"Picture class can now create vectorial SVG/EPS/PDF files providing a generic instance implementing a draw(Graphics2D) - like method.\n"+
						"Diagrams from ditaa can now be rendered easily using the class jparsec.graph.CreateDitaaChart\n"+
						"WCS implementation from JSky replaced with that of SkyView, to take into account image distortions. nom.tam.fits also updated.\n"+
						"The telescope control component can now take and reduce astronomical images to .fits, with WCS fitting and calibration of photometry. Shots from webcams supported using mplayer.\n"+
						"Wiki page http://conga.oan.es/~alonso/doku.php?id=jparsec_wiki updated and completed.",
				"      1.100: Added a lot more (minor) features (Nov 11, 2013):\n"+
						"Implemented a direct draw approach to sky rendering, resulting in 15% better performance in desktop and 60% faster in Android, when anaglyph mode is not used.\n"+
						"Some bug fixes when exporting sky and planetary renderings to PDF.\n"+
						"Corrected many memory leaks when creating consecutive renderings.\n"+
						"Sky rendering can now show the radiants of meteor showers.\n"+
						"Explicit support for NEOs in OrbitEphem class.\n"+
						"Lots of changes to many classes to add new methods to interoperate between objects: Table, Spectrum, Spectrum30m, charts in GenericFit, Interpolation, ...\n"+
						"LATEXReport can now create complex documents and beamer presentations.\n"+
						"JSky implementation of WCS is used again to support projections unsupported by SkyView (but without image distortions).\n"+
						"SExtractor can now cross-match catalogs of sources.\n"+
						"New class jparsec.io.Reflection for useful and powerful reflection operations.\n"+
						"New class jparsec.astrophysics.HRDiagram to work with HR diagrams.\n"+
						"CreateChart and CreateGridChart support converting a Graphics2D object using Java2D coordinates to another one using physical coordinates. AWTGraphics contains methods to rotate Shapes in both systems.\n"+
						"Wiki page http://conga.oan.es/~alonso/doku.php?id=jparsec_wiki updated and completed.",
				"      1.101: Bug fix release (Jan 29, 2014):\n"+
						"Deep sky textures are now better handled in desktop mode, and rendered very fast.\n"+
						"UCAC4 is now used for faint stars instead of UCAC3.\n"+
						"Some bug fixes in classes RenderSky (deep sky objects), RenderEclipse and EventReport/MoonEvent (mutual events of satellites and lunar maximum/minimum declination).\n"+
						"ImageSplineTransnform now allows to use 2d images with int or short arrays for less memory consumption.\n"+
						"The basic implementation of image reduction in ObservationManager class is completed.\n"+
						"New functions to reduce/process cubes in LMVCube class and spectra in Spectrum30m class.\n"+
						"Orbital elements updated.",
				"      1.102: New features (April 14, 2014):\n"+
						"Radex molecular files updated, and support for RADEX improved.\n"+
						"Minor bug fixes related to Latex export.\n"+
						"Better labels for sky coordinates in little fields of view.",
				"      1.103: Bug fixes and new features (April 25, 2014):\n"+
						"Some bug fixes in the labels for sky coordinates in little fields of view.\n"+
						"Catalog of deep sky objects (DSO) corrected using the revised NGC.\n"+
						"Images from Stellarium replaced with a new automatically generated catalog of 700 DSO images using DSS, perfectly aligned when rendered.\n"+
						"Added support for latest JPL integrations DE 424 and DE 430.\n"+
						"Orbital elements updated.",
				"      1.104: Bug fixes (July 1, 2014):\n"+
						"There was a bug when enabling to show the field of view of an ocular in the sky rendering.\n"+
						"A CCDElement object can now be attached to a TelescopeElement instead of an OcularElement.\n"+
						"Labels in little fields of view and DSO rendering from DSS were wrong in inverted view mode.\n"+
						"The SkyChart component now uses a menu to allow to select which additional catalogs should be shown. Among them, DSS image overlay is now available.\n"+
						"There was a bug when recovering the correct positions of stars and planets (rendering in galactic coord) and DSO (in horizontal coord) when searching in a RenderSky instance.\n"+
						"The magnitudes of some old comets were too bright since they have mabs = g = 0.0 when no magnitude data is available.\n"+
						"The Ephem class had a bug, sometimes not correctly detecting that JPL ephemerides are not available.\n"+
						"Most methods in LunarEvent and MainEvents didn't return the inmediately next/previous event for the NEXT and PREVIOUS calculation types. Now the behavior is much better.\n"+
						"Spectra reduction in ProcessSpectrum class improved.\n"+
						"Catalog of deep sky objects (DSO) updated using the final 2014 version of the revised NGC.\n"+
						"Some code reorganization in RenderSky and VirtualCamera (return of catalogs of sources in a given field).",
				"      1.105: Bug fixes (September 26, 2014):\n"+
						"Gildas spectra are now exported with better support for image frequency and offset parameters.\n"+
						"There was a little bug in the English names for \"Southern Fish\" and \"Southern Triangle\" constellations.\n"+
						"City class has now much better performance when searching for cities, so that the search limited by countries is now removed.\n"+
						"There was an old bug in planetary rendering (without textures) and with texture quality < 1 (reduced quality), showing wrong positions for the planet and satellites.\n"+
						"The icons used for realistic stars are now stored as images instead of calculated, so that startup time for sky rendering is much better.\n"+
						"There were a bug rendering the horizon in dates very far from present.\n"+
						"Sky textures from DSS of M42, M27, and M57 replaced with images by Stephane Dumont (the ones in Stellarium). M31 texture from DSS fixed.\n"+
						"Some other details fixed in RenderSky class for proper support in Android (cardinal points rendered in horizon (not axes), precalculation of star/dso ephemerides, among others).",
				"      1.106: Bug fixes and some new features (March 20, 2015):\n"+
						"LMVCube and Spectrum30m classes now writes cubes and spectra much faster.\n"+
						"LMVCube has new methods to correct for primary beam and to reproject cubes in space and/or velocity.\n"+
						"Spectrum30m has new methods to resample spectra and to modify the rest frequency.\n"+
						"Spectrum30m can read fits files from Gildas in INDEX and SPECTRUM modes (best effort, Gildas changes the 'standard' with releases).\n"+
						"LMVCube had an important bug that produced wrong WCS coordinates when the reference pixel was not the center of the map.\n"+
						"New photometric surveys AKARI and WISE, and bug fix in VizierElement#getFluxAndError.\n"+
						"SkyChart component did not show all images in the details window.\n"+
						"Surface brightness is now returned as 0 when the object size is less than 1 arcsecond.\n"+
						"StarElement now uses a parallax field instead of distance.\n"+
						"RenderSky can now show the positions of the current (updated) solar spots, not only the old ones in the database.\n"+
						"Updated orbital elements and other internal files, with few corrections to the scopes file.\n"+
						"RenderSky did not center M1 correctly, often confused with a double star.\n"+
						"Added the Caldwell catalog at the same level of importance as the Messier catalog.\n"+
						"SDSS image overlay and UCAC4 star fields are now stored when downloaded, so they can be rendered again and again.\n"+
						"RenderSky showed wrong orientation for Moon icon during solar eclipses.\n"+
						"RenderSky now shows images of deep sky objects in ancient times, correctly rotated due to precesion (but with stars displaced due to proper motions).\n"+
						"Night mode improved.\n"+
						"RenderPlanet did not show natural satellites at very high zoom.\n"+
						"RenderEclipse and SolarEclipse classes showed wrong values for the time of the maximum in solar eclipses.\n"+
						"RenderEclipse now shows the eclipses using, optionally, textures for the Moon.\n"+
						"SkyChart component did not correctly identify several times the same object with inverted vision enabled.",
				"      1.107: Bug fixes, code clean up, new features related to artificial satellites and stars (July 23, 2015):\n"+
						"Code reorganizacion and clean up. Lots of new examples many bug fixes.\n"+
						"OrbitEphem now implements the method by Montenbruck to solve orbits from observations.\n"+
						"Great improvement in 30m file reading, OTFs of > 32 GB are no problem.\n"+
						"pyGildas instances are no longer 'closed' after executing the first script, allowing to execute more later.\n"+
						"New class FFT for Fast Fourier Transforms, still not used in the library.\n"+
						"Fixed a bug in the apparent magnitudes of asteroids and artificial satellites.\n"+
						"Computation of Iridium flares much faster. Some events were skipped previously and others wrongly shown with the satellite eclipsed.\n"+
						"Lunar Iridium flares are now computed, besides solar ones.\n"+
						"Added apparent magnitudes for Iridium flares.\n"+
						"Optimized rendering of comets/asteroids, 5% better performance overall and less memory consumption.\n"+
						"Fixed some bugs when showing the trajectory of comets with parabolic/hyperbolic orbits.\n"+
						"Trajectories for comets/asteroids used sometimes different elements respect sky rendering, resulting in shifted positions.\n"+
						"There was a bug when rendering lunar eclipses using a telescope that inverts the image.\n"+
						"Complete control of telescope field overlay in SkyChart.\n"+
						"There was a bug when rendering the current daily solar spots from NOAA.\n"+
						"New options in sky rendering: to show magnitudes labels for objects, and a crux at the center of the field of view.\n"+
						"There was an encoding issue when reading the files of cities and observatories in certain computers.\n"+
						"25% better performance in ephemerides computations in PlanetEphem and OrbitEphem.\n"+
						"The method to return a chart showing the orbit of a body in OrbitalElement class has been extended and improved.\n"+
						"Integration of recent galactic novae in SkyChart. Supernova events are also automatically updated in sky rendering.\n"+
						"Integration of orbits of visual binary stars in the details of stars in SkyChart.\n"+
						"Integration of orbits of comets/asteroids and light curves in the details of the objects in SkyChart.\n"+
						"SkyChart shows the Carring rotation number and Brown lunation number in the details of the Sun and Moon.\n"+
						"SkyChart can show trajectories of artificial satellites and the current Iridium flares."
												
				+FileIO.getLineSeparator(),
		};
		String jre = "1.6u12";

		// Dependencies for JPARSEC programs and models
		String library[] = new String[] {
			// Dependency name                             Version
			"jparsec.jar                                   1.107",
			
			"sky.jar                                       1.36",
			"orbital_elements.jar                          1.23",
			"sunspot.jar                                   1.14",
			"eop.jar                                       1.13",
			
			"sgt_v30.jar                                   1.06",
			"jcommon-1.0.16.jar                            1.03",
			"sky2000.jar                                   1.06",
			"radex.jar                                     1.03",
			"jfreechart-1.0.13.jar                         1.00",
			"series96.jar                                  1.02",
			"textures.jar                                  1.10",
			"surfacePlotter2.0.jar                         1.00",
			"miglayout-4.0-swing.jar                       1.00",
			"nrjavaserial-3.8.8.jar                        1.00",
			"ditaa.jar                                     1.00",

			"skyViewWCS.jar                                1.00",
			"jsky-coords.jar                               1.00",
			"jmathplot.jar                                 1.02",
			"visad.jar                                     1.00",
			"JMathTeX-0.7pre.jar                           1.00",
			"jdom-1.0.jar                                  1.00",
			"jhall.jar                                     1.00",
			"iue.jar                                       1.00",
			"vsop87.jar                                    1.01",
			"images.jar                                    1.12",
			"telescopes.jar                                1.01",
			"dust.jar                                      1.00",
			"fits.jar                                      1.01",
			"siess.jar                                     1.00",
			"kurucz.jar                                    1.00",

			"cds/cds.savot.pull.jar                        1.01",
			"cds/cds.savot.common.jar                      1.01",
			"cds/cds.savot.model.jar                       1.01",
			"cds/cds.savot.writer.jar                      1.01",
			"cds/kxml2-min.jar                             1.01",
			"cds/cds.savot.sax.jar                         1.01",
			"cds/cds.astro.jar                             1.02",
			
			"cologne.jar                                   1.01",
			"jpl.jar                                       1.01",
			
//			"elp2000.jar                                   1.01",
//			"jsch-0.1.41.jar                               1.00",						
//			"dsn.jar                                       1.00",
//			"imap.jar                                      1.00",
//			"pop3.jar                                      1.00",
//			"smtp.jar                                      1.00",
//			"mailapi.jar                                   1.00",

			"freehep-graphics2d-2.1.1.jar                  1.00",
			"freehep-xml-2.1.1.jar                         1.00",
			"freehep-swing-2.0.3.jar                       1.00",
			"freehep-graphicsio-ps-2.1.1.jar               1.00",
			"freehep-io-2.0.2.jar                          1.00",
			"freehep-export-2.1.1.jar                      1.00",
			"freehep-graphicsio-2.1.1.jar                  1.00",
			"freehep-util-2.0.2.jar                        1.00",
			"freehep-graphicsio-pdf-2.1.1.jar              1.00",
			"freehep-graphicsio-svg-2.1.1.jar              1.00",

//			"bsc5.jar                                      1.00",
//			"landscapes.jar                                1.00",

//			"jpl_ephem.jar                                 1.02",
		};
		
		Module module = null;
        try {
			module = new Module(
				"JPARSEC",
				DataSet.extractColumnFromTable(library, " ", 0),
				DataSet.toFloatArray(DataSet.toDoubleValues(DataSet.extractColumnFromTable(library, " ", 1))),				
				"http://conga.oan.es/~alonso/jparsec/lib/jparsec",
				"",
				release,
				jre,
				null,
				"Tom\u00e1s Alonso Albi"
			);
        } catch (Exception exc) {  }

        return module;
	}

	private static String getVersionNumber() {
		Module m = Version.getVersion();
		return ""+m.libVersion[0];
	}
}
