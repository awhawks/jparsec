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
package jparsec.ephem;

import java.util.*;

import jparsec.ephem.moons.MoonEphem;
import jparsec.graph.DataSet;
import jparsec.util.DataBase;
import jparsec.util.JPARSECException;
import jparsec.util.Logger;
import jparsec.util.Translate;
import jparsec.util.Logger.LEVEL;
import jparsec.util.Translate.LANGUAGE;

/**
 * A common place to store target body IDs. This class is updated following new
 * IAU resolutions about Pluto.
 * <P>
 * Size parameters comes from the IAU2009 recommended values. Note that not all
 * known natural objects are listed here, but only those whose size or
 * rotational parameters are known. Otherwise, ephemeris can also be obtained
 * for objects not listed here (see {@linkplain MoonEphem#calcAllJPLSatellites(jparsec.time.TimeElement, jparsec.observer.ObserverElement, EphemerisElement)}).
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public final class Target
{
	// private constructor so that this class cannot be instantiated.
	private Target() {}
	
	/**
	 * The set of targets for JPARSEC ephemerides.
	 */
	public static enum TARGET {
		// Object name, equatorial radius (km), polar radius (km), relative mass to the Sun (1, >= 1)
		// Main Solar System bodies, Sun must be the first
		/** The Sun */
		SUN (696000.0, 696000.0, 1.0),
		/** Mercury */
		MERCURY (2439.7, 2439.7, 6023600.0),
		/** Venus */
		VENUS (6051.8, 6051.8, 408523.71),
		/** Earth */
		EARTH (6378.1366, 6356.7519, 332946.050895),
		/** Mars */
		MARS (3396.19, 3376.2, 3098708.0),
		/** Jupiter */
		JUPITER (71492.0, 66854.0, 1047.3486),
		/** Saturn */
		SATURN (60268.0, 54364.0, 3497.898),
		/** Uranus */
		URANUS (25559.0, 24973.0, 22902.98),
		/** Neptune */
		NEPTUNE (24764.0, 24341.0, 19412.24),
		/** Dwarf planet Pluto */
		Pluto (1195.0, 1195.0, 1.352E8),
		/** The Moon */
		Moon (1737.4, 1737.4, 2.7068700387534E7),
		/** Special target for Earth-Moon barycenter */
		Earth_Moon_Barycenter (0.0, 0.0, 328900.5614),
		// Generic comet/asteroid
		/** Generic comet */
		Comet (0.0, 0.0, 0.0),
		/** Generic asteroid */
		Asteroid (0.0, 0.0, 0.0),
		/** Generic NEO */
		NEO (0.0, 0.0, 0.0),
		// Special values available in some theories (JPL, Moshier)
		/**
		 * Special target for nutation, only for JPL ephemerides.
		 */
		Nutation (0, 0, 0),
		/**
		 * Special target for lunar librations, only for Moshier and JPL ephemerides.
		 */
		Libration (0, 0, 0),
		/**
		 * Special target for Solar System barycenter, only for Moshier and JPL ephemerides.
		 */
		Solar_System_Barycenter (0, 0, 0),
		// Natural satellites, Phobos must be the first and Proteus the last
		/** A satellite of Mars */
		Phobos (13.0, 9.1, 0.0),
		/** A satellite of Mars */
		Deimos (7.8, 5.1, 0.0),
		/** A satellite of Jupiter */
		Io (1829.4, 1815.7, 0.0),
		/** A satellite of Jupiter */
		Europa (1562.6, 1559.5, 0.0),
		/** A satellite of Jupiter */
		Ganymede (2631.2, 2631.2, 0.0),
		/** A satellite of Jupiter */
		Callisto (2410.3, 2410.3, 0.0),
		/** A satellite of Saturn */
		Mimas (207.8, 190.6, 0.0),
		/** A satellite of Saturn */
		Enceladus (256.6, 248.3, 0.0),
		/** A satellite of Saturn */
		Tethys (538.4, 526.3, 0.0),
		/** A satellite of Saturn */
		Dione (563.4, 559.6, 0.0),
		/** A satellite of Saturn */
		Rhea (765.0, 762.4, 0.0),
		/** A satellite of Saturn */
		Titan (2575.15, 2574.47, 0.0),
		/** A satellite of Saturn */
		Hyperion (180.1, 102.7, 0.0),
		/** A satellite of Saturn */
		Iapetus (745.7, 712.1, 0.0),
		/** A satellite of Uranus */
		Miranda (240.4, 232.9, 0.0),
		/** A satellite of Uranus */
		Ariel (581.1, 577.7, 0.0),
		/** A satellite of Uranus */
		Umbriel (584.7, 584.7, 0.0),
		/** A satellite of Uranus */
		Titania (788.9, 788.9, 0.0),
		/** A satellite of Uranus */
		Oberon (761.4, 761.4, 0.0),
		/** A satellite of Neptune */
		Triton (1352.6, 1352.6, 0.0),
		/** A satellite of Neptune */
		Nereid (170.0, 170.0, 0.0),
		/** A satellite of Pluto */
		Charon (605.0, 605.0, 0.0),
		/** A satellite of Jupiter */
		Amalthea (125.0, 64.0, 0.0),
		/** A satellite of Jupiter */
		Thebe (58.0, 42.0, 0.0),
		/** A satellite of Jupiter */
		Adrastea (10.0, 7.0, 0.0),
		/** A satellite of Jupiter */
		Metis (30.0, 20.0, 0.0),
		/** A satellite of Jupiter */
		Himalia (85.0, 85.0, 0.0),
		/** A satellite of Jupiter */
		Elara (40.0, 40.0, 0.0),
		/** A satellite of Jupiter */
		Pasiphae (18.0, 18.0, 0.0),
		/** A satellite of Jupiter */
		Sinope (14.0, 14.0, 0.0),
		/** A satellite of Jupiter */
		Lysithea (12.0, 12.0, 0.0),
		/** A satellite of Jupiter */
		Carme (15.0, 15.0, 0.0),
		/** A satellite of Jupiter */
		Ananke (10.0, 10.0, 0.0),
		/** A satellite of Jupiter */
		Leda (5.0, 5.0, 0.0),
		/** A satellite of Saturn */
		Atlas (18.5, 13.5, 0.0),
		/** A satellite of Saturn */
		Prometheus (74.0, 34.0, 0.0),
		/** A satellite of Saturn */
		Pandora (55.0, 31.0, 0.0),
		/** A satellite of Saturn */
		Pan (10.0, 10.0, 0.0),
		/** A satellite of Saturn */
		Epimetheus (64.9, 53.1, 0.0),
		/** A satellite of Saturn */
		Janus (101.5, 76.3, 0.0),
		/** A satellite of Saturn */
		Telesto (16.3, 10.0, 0.0),
		/** A satellite of Saturn */
		Calypso (15.1, 7.0, 0.0),
		/** A satellite of Saturn */
		Helene (21.7, 13.0, 0.0),
		/** A satellite of Saturn */
		Phoebe (109.4, 101.8, 0.0),
		/** A satellite of Uranus */
		Cordelia (13.0, 13.0, 0.0),
		/** A satellite of Uranus */
		Ophelia (15.0, 15.0, 0.0),
		/** A satellite of Uranus */
		Cressida (31.0, 31.0, 0.0),
		/** A satellite of Uranus */
		Bianca (21.0, 21.0, 0.0),
		/** A satellite of Uranus */
		Desdemona (27.0, 27.0, 0.0),
		/** A satellite of Uranus */
		Juliet (42.0, 42.0, 0.0),
		/** A satellite of Uranus */
		Portia (54.0, 54.0, 0.0),
		/** A satellite of Uranus */
		Rosalind (27.0, 27.0, 0.0),
		/** A satellite of Uranus */
		Puck (77.0, 77.0, 0.0),
		/** A satellite of Uranus */
		Belinda (33.0, 33.0, 0.0),
		/** A satellite of Neptune */
		Naiad (29.0, 29.0, 0.0),
		/** A satellite of Neptune */
		Thalassa (40.0, 40.0, 0.0),
		/** A satellite of Neptune */
		Despina (74.0, 74.0, 0.0),
		/** A satellite of Neptune */
		Galatea (79.0, 79.0, 0.0),
		/** A satellite of Neptune */
		Larissa (104.0, 89.0, 0.0),
		/** A satellite of Neptune */
		Proteus (218.0, 201.0, 0.0),
		// Asteroids. Ceres must be the first
		/** Dwarf planet */
		Ceres (487.3, 454.7, 0.0),
		/** An asteroid */
		Pallas (582.0, 500.0, 0.0),
		/** An asteroid */
		Vesta (289.0, 229.0, 0.0),
		/** An asteroid */
		Lutetia (62.0, 46.5, 0.0),
		/** An asteroid */
		Ida (26.8, 7.6, 0.0),
		/** An asteroid */
		Eros (17.0, 5.5, 0.0),
		/** An asteroid */
		Davida (180.0, 127.0, 0.0),
		/** An asteroid */
		Gaspra (9.1, 4.4, 0.0),
		/** An asteroid */
		Steins (3.24, 2.04, 0.0),
		/** An asteroid */
		Itokawa (0.535, 0.209, 0.0),
		// Comets
		/** A comet */
		P9_Tempel_1 (3.7, 2.5, 0.0),
		/** A comet */
		P19_Borrelly (3.5, 3.5, 0.0),
		// Generic unknown body, must be the last item
		/**
		 * ID value for a generic 'not a planet' body, for instance
		 * a space probe.
		 */
		NOT_A_PLANET (0, 0, 0);
		
		/**
		 * The equatorial radius in km.
		 */
		public final double equatorialRadius;
		/**
		 * The polar radius in km.
		 */
		public final double polarRadius;
		/**
		 * The relative mass to the Sun (1), greater or equal to 1. Values
		 * come from JPL DE405 ephemerides, and are used for correting for
		 * light deflection.
		 */
		public final double relativeMass;

		private String name = null;
		private int lang = -1;
		
		/**
		 * The id number (index) for a comet or asteroid.
		 */
		private int id = -1;
		
		private TARGET(double eqR, double poR, double rM) {
			this.equatorialRadius = eqR;
			this.polarRadius = poR;
			this.relativeMass = rM;
		}
		/**
		 * Sets the index of a body, used to select a minor body 
		 * when reading asteroids/comets. Default initial value
		 * for index is -1.
		 * @param i The index of the body. Should be >= 0.
		 */
		public void setIndex(int i) {
			id = i;
		}
		/**
		 * Returns the index, or -1 if it is not set.
		 * @return The index.
		 */
		public int getIndex() {
			return id;
		}
		
		/**
		 * Check if the index is indeed a valid index of an object. The check is
		 * made for main objects suitable for ephemeris calculations, such as the
		 * Sun, the Moon, any planet except Earth, and Pluto. If the index
		 * is a valid one but is not an index of a given asteroid or a natural satellite
		 * (neither the Earth or its barycenter), true will be returned. If the index
		 * is equal to a generic Comet/Asteroid true will be returned since this is
		 * valid for that specific kind of calculations.
		 * 
		 * @return True if it is a valid index, false otherwise.
		 */
		public boolean isValidObjectButNotASatelliteNeitherAnAsteroid()
		{
			boolean it_is = false;

			if (this.ordinal() >= TARGET.SUN.ordinal() && this.ordinal() <= TARGET.Asteroid.ordinal() && this != TARGET.Earth_Moon_Barycenter && this != TARGET.EARTH)
				it_is = true;

			return it_is;
		}

		/**
		 * Check if the index is indeed a valid index of a natural satellite.
		 * 
		 * @return True if it is a valid satellite, false otherwise. Note this method
		 * returns false for the Moon.
		 */
		public boolean isNaturalSatellite()
		{
			boolean it_is = false;

			if (this.ordinal() >= TARGET.Phobos.ordinal() && this.ordinal() <= TARGET.Proteus.ordinal())
				it_is = true;

			return it_is;
		}

		/**
		 * Check if the index is indeed a planet.
		 * 
		 * @return True if it is a planet or the Sun, false otherwise.
		 */
		public boolean isPlanet()
		{
			boolean it_is = false;

			if (this.ordinal() >= TARGET.SUN.ordinal() && this.ordinal() <= TARGET.NEPTUNE.ordinal())
				it_is = true;

			return it_is;
		}

		/**
		 * Check if the index is indeed an asteroid (or comet).
		 * 
		 * @return True if it is an asteroid, false otherwise.
		 */
		public boolean isAsteroid()
		{
			boolean it_is = false;
			// Asteroid or comet !
			if (this.ordinal() >= TARGET.Ceres.ordinal() && this.ordinal() <= TARGET.NOT_A_PLANET.ordinal())
				it_is = true;

			return it_is;
		}

		/**
		 * Obtain the name of an object.
		 * 
		 * @return A string with the name of the object.
		 */
		public String getName()
		{
			if (name != null && lang == Translate.getDefaultLanguage().ordinal()) return name;
			lang = Translate.getDefaultLanguage().ordinal();
			name = null;
			
			if (this.ordinal() <= EARTH.ordinal()) {
				try {
					name = Translate.getEntry(this.ordinal(), Translate.getDefaultLanguage());
					return name;
				} catch (Exception exc) {
					Logger.log(LEVEL.ERROR, "Cannot translate body name "+this+" to language "+Translate.getDefaultLanguage()+". This error should never happen!");
					return null;
				}
			}
			int i = -1;
			if (this == TARGET.Moon) i = 4;
			if (this == TARGET.Earth_Moon_Barycenter) i = 80;
			if (this == TARGET.NOT_A_PLANET) i = 81;
			if (this == TARGET.Comet) i = 74;
			if (this == TARGET.NEO) i = 1275;
			if (this == TARGET.Asteroid) i = 73;
			if (this == TARGET.Belinda) i = 821;
			if (this == TARGET.Naiad) i = 126;
			if (this == TARGET.Thalassa) i = 125;
			if (this == TARGET.Despina) i = 123;
			if (this == TARGET.Galatea) i = 124;
			if (this == TARGET.Larissa) i = 122;
			if (this == TARGET.Proteus) i = 121;
			if (this == TARGET.Ceres) return "Ceres";
			if (this == TARGET.Pallas) return "Pallas";
			if (this == TARGET.Vesta) i = 143;
			if (this == TARGET.Eros) i = 144;
			if (this == TARGET.Ida) i = 141;
			if (this == TARGET.Gaspra) i = 142;
			if (this.isNaturalSatellite() && i < 0) {
				if (this.ordinal() <= Metis.ordinal()) i = 82 + this.ordinal() - Phobos.ordinal();
				if (this.ordinal() >= Himalia.ordinal() && this.ordinal() <= Leda.ordinal()) i = 127 + this.ordinal() - Himalia.ordinal();
				if (this.ordinal() >= Atlas.ordinal() && this.ordinal() <= Pan.ordinal()) i = 108 + this.ordinal() - Atlas.ordinal();
				if (this.ordinal() >= Epimetheus.ordinal() && this.ordinal() <= Phoebe.ordinal()) i = 135 + this.ordinal() - Epimetheus.ordinal();
				if (this.ordinal() >= Cordelia.ordinal() && this.ordinal() <= Puck.ordinal()) i = 112 + this.ordinal() - Cordelia.ordinal();
			}
			if (i >= 0) {
				try {
					name = Translate.getEntry(i, Translate.getDefaultLanguage());
					return name;
				} catch (Exception exc) {
					Logger.log(LEVEL.ERROR, "Cannot translate body name "+this+" to language "+Translate.getDefaultLanguage()+". This error should never happen!");
					return null;
				}
			}
			if (this.ordinal() <= Pluto.ordinal()) {
				try {
					name = Translate.getEntry(this.ordinal()+1, Translate.getDefaultLanguage());
					return name;
				} catch (Exception exc) {
					Logger.log(LEVEL.ERROR, "Cannot translate body name "+this+" to language "+Translate.getDefaultLanguage()+". This error should never happen!");
					return null;
				}
			}

			name = this.getEnglishName(); //Translate.translate(this.getEnglishName());
			return name;
		}
		
		/**
		 * Obtain the name of an object.
		 * 
		 * @return A string with the name of the object.
		 */
		public String getEnglishName()
		{
			String name = "";
			
			switch (this)
			{
			case Earth_Moon_Barycenter:
				name = "Earth-Moon barycenter";
				break;
			case NOT_A_PLANET:
				name = "Not a planet";
				break;
			case P9_Tempel_1:
				name = "9P/Tempel 1";
				break;
			case P19_Borrelly:
				name = "19P/Borrelly";
				break;
			default:
				name = this.name().toLowerCase();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				break;
			};
			return name;
		}
		
		/**
		 * Returns flatenning factor = (equatorial radius - polar radius ) /
		 * equatorial radius.
		 * 
		 * @return Flatenning factor. Set to 0 if the object size is unknown.
		 */
		public double getFlatenningFactor()
		{
			if (this.equatorialRadius == 0.0) return 0.0;

			return (this.equatorialRadius - this.polarRadius) / this.equatorialRadius;
		}
		
		/**
		 * Obtains the central body ID of certain object (natural satellite).
		 * 
		 * @return The central body or SUN if it is not found.
		 */
		public TARGET getCentralBody()
		{
			if (this == TARGET.Moon)
				return TARGET.EARTH;
			if (this == TARGET.Charon)
				return TARGET.Pluto;
			if (this == TARGET.Phobos)
				return TARGET.MARS;
			if (this == TARGET.Deimos)
				return TARGET.MARS;
			if (this == TARGET.Io)
				return TARGET.JUPITER;
			if (this == TARGET.Europa)
				return TARGET.JUPITER;
			if (this == TARGET.Ganymede)
				return TARGET.JUPITER;
			if (this == TARGET.Callisto)
				return TARGET.JUPITER;
			if (this == TARGET.Amalthea)
				return TARGET.JUPITER;
			if (this == TARGET.Thebe)
				return TARGET.JUPITER;
			if (this == TARGET.Adrastea)
				return TARGET.JUPITER;
			if (this == TARGET.Metis)
				return TARGET.JUPITER;
			if (this == TARGET.Mimas)
				return TARGET.SATURN;
			if (this == TARGET.Enceladus)
				return TARGET.SATURN;
			if (this == TARGET.Tethys)
				return TARGET.SATURN;
			if (this == TARGET.Dione)
				return TARGET.SATURN;
			if (this == TARGET.Rhea)
				return TARGET.SATURN;
			if (this == TARGET.Titan)
				return TARGET.SATURN;
			if (this == TARGET.Hyperion)
				return TARGET.SATURN;
			if (this == TARGET.Iapetus)
				return TARGET.SATURN;
			if (this == TARGET.Atlas)
				return TARGET.SATURN;
			if (this == TARGET.Prometheus)
				return TARGET.SATURN;
			if (this == TARGET.Pandora)
				return TARGET.SATURN;
			if (this == TARGET.Pan)
				return TARGET.SATURN;
			if (this == TARGET.Miranda)
				return TARGET.URANUS;
			if (this == TARGET.Ariel)
				return TARGET.URANUS;
			if (this == TARGET.Umbriel)
				return TARGET.URANUS;
			if (this == TARGET.Titania)
				return TARGET.URANUS;
			if (this == TARGET.Oberon)
				return TARGET.URANUS;
			if (this == TARGET.Cordelia)
				return TARGET.URANUS;
			if (this == TARGET.Ophelia)
				return TARGET.URANUS;
			if (this == TARGET.Bianca)
				return TARGET.URANUS;
			if (this == TARGET.Cressida)
				return TARGET.URANUS;
			if (this == TARGET.Desdemona)
				return TARGET.URANUS;
			if (this == TARGET.Juliet)
				return TARGET.URANUS;
			if (this == TARGET.Portia)
				return TARGET.URANUS;
			if (this == TARGET.Rosalind)
				return TARGET.URANUS;
			if (this == TARGET.Belinda)
				return TARGET.URANUS;
			if (this == TARGET.Puck)
				return TARGET.URANUS;
			if (this == TARGET.Triton)
				return TARGET.NEPTUNE;
			if (this == TARGET.Nereid)
				return TARGET.NEPTUNE;
			if (this == TARGET.Proteus)
				return TARGET.NEPTUNE;
			if (this == TARGET.Larissa)
				return TARGET.NEPTUNE;
			if (this == TARGET.Despina)
				return TARGET.NEPTUNE;
			if (this == TARGET.Galatea)
				return TARGET.NEPTUNE;
			if (this == TARGET.Thalassa)
				return TARGET.NEPTUNE;
			if (this == TARGET.Naiad)
				return TARGET.NEPTUNE;

			if (this == TARGET.Himalia)
				return TARGET.JUPITER;
			if (this == TARGET.Elara)
				return TARGET.JUPITER;
			if (this == TARGET.Pasiphae)
				return TARGET.JUPITER;
			if (this == TARGET.Sinope)
				return TARGET.JUPITER;
			if (this == TARGET.Lysithea)
				return TARGET.JUPITER;
			if (this == TARGET.Carme)
				return TARGET.JUPITER;
			if (this == TARGET.Ananke)
				return TARGET.JUPITER;
			if (this == TARGET.Leda)
				return TARGET.JUPITER;

			if (this == TARGET.Epimetheus)
				return TARGET.SATURN;
			if (this == TARGET.Janus)
				return TARGET.SATURN;
			if (this == TARGET.Telesto)
				return TARGET.SATURN;
			if (this == TARGET.Calypso)
				return TARGET.SATURN;
			if (this == TARGET.Helene)
				return TARGET.SATURN;
			if (this == TARGET.Phoebe)
				return TARGET.SATURN;

			return TARGET.SUN;
		}		
	};

	
	private static String[] populateStringArrayList()
	{
		ArrayList<String> vS = new ArrayList<String>();
		for (int i=TARGET.SUN.ordinal(); i<=TARGET.NOT_A_PLANET.ordinal(); i++)
		{
				vS.add(TARGET.values()[i].getName());
		}
		
		String data[] = DataSet.arrayListToStringArray(vS);
		String lang = Translate.getDefaultLanguage().toString();
		DataBase.addData("targetNames"+lang, null, data, true);
		return data;
	}
	/**
	 * Returns the names of all available targets.
	 * @return All names.
	 */
	public static String[] getNames()
	{
		String lang = Translate.getDefaultLanguage().toString();
		String vS[] = (String[]) DataBase.getData("targetNames"+lang, null, true);
		if (vS == null) vS = Target.populateStringArrayList();
		return vS;
	}
	
	private static int[] populateIntegerArrayList()
	{
		ArrayList<Integer> vI = new ArrayList<Integer>();
		for (int i=TARGET.SUN.ordinal(); i<=TARGET.NOT_A_PLANET.ordinal(); i++)
		{
			vI.add(new Integer(i));
		}
		int[] data = DataSet.arrayListToIntegerArray(vI);
		DataBase.addData("targetIDs", null, data, true);
		return data;
	}
	/**
	 * Returns the IDs of all available targets.
	 * @return All IDs.
	 */
	public static int[] getIDs()
	{
		int vI[] = (int[]) DataBase.getData("targetIDs", null, true);
		if (vI == null) vI = Target.populateIntegerArrayList();
		return vI;
	}


	/**
	 * Obtain the ID constant of an object.
	 * 
	 * @param name The name of the object.
	 * @return Object ID, or the 'not a planet' constant if it is not found.
	 * @throws JPARSECException If an error occurs, for instance when the object
	 *  is null.
	 */
	public static TARGET getID(String name) throws JPARSECException
	{
		if (name == null || name.equals("")) throw new JPARSECException("null/empty String as input planet!");
		
		if (Translate.getDefaultLanguage() == LANGUAGE.ENGLISH) {
			try {
				TARGET out = TARGET.valueOf(name);
				return out;
			} catch (Exception exc) {}
		}
		
		String names[] = Target.getNames();
		int index = DataSet.getIndex(names, name);
		if (index < 0) index = DataSet.getIndexStartingWith(names, name);
		if (index >= 0) return TARGET.values()[index];
		
		String others[] = new String[] {
				"Ceres", "Pallas", "Vesta", "Lutetia", "Ida", "Eros", "Davida", "Gaspra",
				"Steins", "Itokawa", "9P/Tempel 1", "19P/Borrely"
		};
		index = DataSet.getIndexContaining(others, name);
		if (index < 0) return TARGET.NOT_A_PLANET;
		return TARGET.values()[index];

/*		int ID = TARGET.NOT_A_PLANET.ordinal();
		String nameUpper = name.toUpperCase();
		TARGET t[] = TARGET.values();
		for (int i = TARGET.Ceres.ordinal(); i< TARGET.NOT_A_PLANET.ordinal(); i++) {
			String n = t[i].getName();
			n = n.toUpperCase();
			if (i <= TARGET.Asteroid.ordinal()) {
				if (n.indexOf(nameUpper) == 0) ID = i;
				if (n.equals(nameUpper)) break;				
			} else {
				if (i >= TARGET.Ceres.ordinal()) {
					// Search by indexof for asteroids. This is due to the fact that the
					// ussual name in the catalogs for Ida for example is not 'Ida', but '243
					// Ida' instead.
					if (nameUpper.indexOf(n) >= 0) ID = i;					
				} else {
					if (nameUpper.equals(n)) ID = i;
				}
			}
		}
		
		return t[ID];
*/		
	}

	/**
	 * Obtain the ID constant of an object.
	 * 
	 * @param name The name of the object in English.
	 * @return Object ID, or 'not a planet' constant if it is not found.
	 * @throws JPARSECException If an error occurs, for instance when the object
	 *  is null.
	 */
	public static TARGET getIDFromEnglishName(String name) throws JPARSECException
	{
		if (name == null || name.equals("")) throw new JPARSECException("null/empty String as input planet!");
		int ID = TARGET.NOT_A_PLANET.ordinal();
		String nameUpper = name.toUpperCase();
		for (int i = TARGET.SUN.ordinal(); i< TARGET.NOT_A_PLANET.ordinal(); i++) {
			String n = TARGET.values()[i].getEnglishName();
			n = n.toUpperCase();
			if (i <= TARGET.Asteroid.ordinal()) {
				if (n.indexOf(nameUpper) == 0) ID = i;
				if (n.equals(nameUpper)) break;				
			} else {
				if (i >= TARGET.Ceres.ordinal()) {
					// Search by indexof for asteroids. This is due to the fact that the
					// ussual name in the catalogs for Ida for example is not 'Ida', but '243
					// Ida' instead.
					if (nameUpper.indexOf(n) >= 0) ID = i;					
				} else {
					if (nameUpper.equals(n)) ID = i;
				}
			}
		}
		
		return TARGET.values()[ID];
	}
	
	/**
	 * Transforms a given latitude from planetocentric to planetogeodetic.
	 * @param lat Latitude in radians.
	 * @param target Target body.
	 * @return Planetogeodetic latitude.
	 */
	public static double planetocentricToPlanetogeodeticLatitude(double lat, TARGET target) {
		double shape = target.getFlatenningFactor(); // (equatorial - polar radius) / (equatorial radius)
		return Math.atan(Math.tan(lat) / Math.pow(1.0 - shape, 2.0));
	}
	
	/**
	 * Transforms a given latitude from planetogeodetic to planetocentric.
	 * @param lat Latitude in radians.
	 * @param target Target body.
	 * @return Planetocentric latitude.
	 */
	public static double planetogeodeticToPlanetocentricLatitude(double lat, TARGET target) {
		double shape = target.getFlatenningFactor(); // (equatorial - polar radius) / (equatorial radius)
		return Math.atan(Math.tan(lat) * Math.pow(1.0 - shape, 2.0));
	}
}
