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
package jparsec.ephem.event;

import jparsec.time.*;
import jparsec.time.TimeElement.SCALE;
import jparsec.util.*;
import jparsec.observer.*;
import jparsec.ephem.*;
import jparsec.ephem.Target.TARGET;

import java.io.*;

/**
 * Manages data related to satellite events.
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class MoonEventElement implements Serializable {
	static final long serialVersionUID = 1L;

	/**
	 * Holds the start time of the events as a Julian day in TDB.
	 */
	public double startTime;
	/**
	 * Holds the end time of the events as a Julian day in TDB.
	 */
	public double endTime;
	/**
	 * Main body id constant.
	 */
	public TARGET mainBody;
	/**
	 * Secondary body id constant, if anyone exists.
	 */
	public TARGET secondaryBody;
	/**
	 * Event id code. Constants defined in this class.
	 */
	public MoonEventElement.EVENT eventType;
	/**
	 * Holds details on the event. For mutual events it contains a number 
	 * with the percentage of eclipse/occultation, the date of that maximum,
	 * and the distance between both bodies in degrees.
	 */
	public String details;
	/**
	 * Subevent id constant. Only takes sense on mutual events with the
	 * mother planet, while a satellite is partially eclipsed or occulted. In this case
	 * the event marks the beggining or the ending of the period when the
	 * satellite is fully eclipsed or occulted. Constants defined in this class.
	 */
	public MoonEventElement.SUBEVENT subType;
	/**
	 * True for a visible event, false otherwise. Note a given event
	 * must be visible from Earth and with positive elevation to be
	 * observable.
	 */
	public boolean visibleFromEarth;
	/**
	 * Elevation of the source in radians above the observer, calculated
	 * even if requested ephemerides are geocentric.
	 */
	public double elevation;
	/**
	 * This flag is false always except when calculating natural satellites events
	 * (mutual and non-mutual). In this case this flag will be true for those
	 * events that are simultaneous (several events going on at the same time).
	 * In practice, this flag is true always for non-mutual events involving two or 
	 * more satellites, and also true always for mutual events (since they involve
	 * two satellites).
	 */
	public boolean severalSimultaneousEvents = false;
	
	/**
	 * The set of moon events.
	 */
	public static enum EVENT {
		/** ID code for an eclipse event. */
		ECLIPSED,
		/** Id code for an occultation event. */
		OCCULTED,
		/** Id code for a transit event. */
		TRANSIT,
		/** Id code for a shadow transit event. */
		SHADOW_TRANSIT,
	};

	/**
	 * The set of moon subevents (start and end times).
	 */
	public static enum SUBEVENT {
		/** ID code for an event that begins. */
		START,
		/** Id code for an event that ends. */
		END,
		/** Id code for an event that begins and ends. */
		TIME
	};
	
	/**
	 * Holds a description of the events.
	 */
	public static final String[] EVENTS = new String[] {"Eclipsed", "Occulted", "Transiting",
		"Shadow transiting"};
	
	/**
	 * Empty constructor.
	 */
	public MoonEventElement() {}
	
	/**
	 * Constructor for a simple event.
	 * @param jd Event time.
	 * @param main Main object involved.
	 * @param event Event id constant.
	 * @param details Details.
	 */
	public MoonEventElement(double jd, TARGET main, MoonEventElement.EVENT event, String details)
	{
		this.startTime = jd;
		this.endTime = jd;
		this.mainBody = main;
		this.secondaryBody = main;
		this.eventType = event;
		this.details = details;
		this.visibleFromEarth = true;
		this.subType = MoonEventElement.SUBEVENT.TIME;
	}
	/**
	 * Full constructor.
	 * @param jdi initial time.
	 * @param jdf Final time.
	 * @param main Main object involved.
	 * @param secondary Secondary object involved.
	 * @param event Event id constant.
	 * @param details Details.
	 */
	public MoonEventElement(double jdi, double jdf, TARGET main, TARGET secondary, MoonEventElement.EVENT event, String details)
	{
		this.startTime = jdi;
		this.endTime = jdf;
		this.mainBody = main;
		this.secondaryBody = secondary;
		this.eventType = event;
		this.details = details;
		this.visibleFromEarth = true;
		this.subType = MoonEventElement.SUBEVENT.TIME;
	}
	
	/**
	 * Transforms the event time into another time scale.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @param start True to transform start time, false for end time.
	 * @param timeScale Ouput time scale id constant.
	 * @return The output time.
	 * @throws JPARSECException If an error occurs.
	 */
	public double getEventTime(ObserverElement obs, EphemerisElement eph, boolean start, SCALE timeScale)
	throws JPARSECException {
		TimeElement time = new TimeElement(this.startTime, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
		if (!start) time = new TimeElement(this.endTime, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
		
		double out = TimeScale.getJD(time, obs, eph, timeScale);
		return out;
	}
	
	/**
	 * Reports the current event to the console.
	 * @throws JPARSECException If an error occurs.
	 */
	public void report()
	throws JPARSECException {
		System.out.println(this.mainBody.getName()+" "+MoonEventElement.EVENTS[this.eventType.ordinal()]+" by "+this.secondaryBody.getName()+" between "+this.startTime+" and "+this.endTime+" (TDB). Details: "+this.details+".");
	}
	/**
	 * Clones this instance.
	 */
	public MoonEventElement clone()
	{
		if (this == null) return null;
		MoonEventElement e = new MoonEventElement(this.startTime, this.endTime, this.mainBody, this.secondaryBody,
				this.eventType, this.details);
		e.elevation = this.elevation;
		e.visibleFromEarth = this.visibleFromEarth;
		e.subType = this.subType;
		e.severalSimultaneousEvents = this.severalSimultaneousEvents;
		return e;
	}
	/**
	 * Returns wether the input Object contains the same information
	 * as this instance.
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
		MoonEventElement ee = (MoonEventElement) e;
		if (!ee.details.equals(this.details)) equals = false;
		if (ee.endTime != this.endTime) equals = false;
		if (ee.elevation != this.elevation) equals = false;
		if (ee.startTime != this.startTime) equals = false;
		if (ee.mainBody != this.mainBody) equals = false;
		if (ee.eventType != this.eventType) equals = false;
		if (ee.secondaryBody != this.secondaryBody) equals = false;
		if (ee.visibleFromEarth != this.visibleFromEarth) equals = false;
		if (ee.subType != this.subType) equals = false;
		if (ee.severalSimultaneousEvents != this.severalSimultaneousEvents) equals = false;
		return equals;
	}
}
