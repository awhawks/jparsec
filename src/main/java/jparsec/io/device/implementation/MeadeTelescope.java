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
package jparsec.io.device.implementation;

import javax.swing.JOptionPane;

import jparsec.astronomy.CoordinateSystem;
import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target.TARGET;
import jparsec.io.FileIO;
import jparsec.io.device.GenericCamera;
import jparsec.io.device.GenericTelescope;
import jparsec.io.device.SerialConnection;
import jparsec.io.device.VirtualCamera;
import jparsec.io.image.ImageHeaderElement;
import jparsec.math.Constant;
import jparsec.math.Evaluation;
import jparsec.math.FastMath;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.observer.ObserverElement.DST_RULE;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;
import jparsec.time.TimeElement.SCALE;
import jparsec.util.JPARSECException;
import jparsec.util.Translate;
import jparsec.util.Version;

/**
 * An implementation of a Meade telescope.
 * @author T. Alonso Albi - OAN (Spain)
 */
public class MeadeTelescope implements GenericTelescope {

	private SerialConnection sc;

	private TELESCOPE_MODEL telescopeModel = null;
	private boolean isMoving = false, isMovingS = false, isMovingN = false, isMovingE = false, isMovingW = false;
	private FOCUS_SPEED fs = FOCUS_SPEED.SLOW;
	private FOCUS_DIRECTION fd = FOCUS_DIRECTION.IN;
	private MOVE_SPEED ms = MOVE_SPEED.GUIDE;
	private MOVE_DIRECTION md = MOVE_DIRECTION.NORTH_UP;
	private double[] field = new double[] {-1, -1, -1, -1, -1};
	private TimeElement time0;
	private ObserverElement obs;
	private double timeOffset = 0;
	private long updateTime = 5000, lastRADEC = -1, lastAZEL = -1;
	private LocationElement lastEq = new LocationElement(), lastHz = new LocationElement();
	private LocationElement objLoc = null, parkPos;
	private boolean noGW = false;
	private String object;
	private static final double MOVE_TOLERANCE_1s = 30 * Constant.ARCSEC_TO_RAD;

	@Override
	public boolean hasGPS() {
		if (telescopeModel != TELESCOPE_MODEL.MEADE_AUTOSTAR_II) return false;
		String o = this.sendCmdAndReceiveResponse("#:gps#");
		if (o != null && o.length() > 0) return true;
		return false;
	}

	@Override
	public boolean hasFocuser() {
		if (telescopeModel != TELESCOPE_MODEL.MEADE_AUTOSTAR_II) return false;
		String o = this.sendCmdAndReceiveResponse("#:FB#");
		if (o != null && o.length() > 0) return true;
		return false;
	}

	@Override
	public boolean isMoving() {
		return isMoving;
	}

	@Override
    public synchronized boolean setFocusSpeed(FOCUS_SPEED rate) {
		boolean out = false;
		switch(rate) {
		case FAST:
			out = sendCmd("#:FF#");
			break;
		case SLOW:
			out = sendCmd("#:FS#");
			break;
		}
		if (out) fs = rate;
		return out;
    }
	@Override
	public FOCUS_SPEED getFocusSpeed() { return fs; }
	@Override
    public synchronized boolean startFocus(FOCUS_DIRECTION direction) {
		boolean out = false;
		switch(direction) {
		case IN:
			out = sendCmd("#:F+#");
			break;
		case OUT:
			out = sendCmd("#:F-#");
			break;
		}
		if (out) fd = direction;
		return out;
    }
	@Override
	public FOCUS_DIRECTION getFocusDirection() { return fd; }
	@Override
    public synchronized boolean stopFocus() {
		if (sendCmd("#:FQ#")) {
			fd = null;
			return true;
		}
		return false;
    }
	@Override
    public synchronized boolean setMoveSpeed(MOVE_SPEED rate) {
		boolean out = false;
		switch(rate) {
		case SLEW:
			out = sendCmd("#:RS#");
			break;
		case FIND:
			out = sendCmd("#:RM#");
			break;
		case CENTER:
			out = sendCmd("#:RC#");
			break;
		case GUIDE:
			out = sendCmd("#:RG#");
			break;
		}
		if (out) ms = rate;
		return out;
    }
	@Override
	public MOVE_SPEED getMoveSpeed() { return ms; }
	@Override
    public synchronized boolean startMove(MOVE_DIRECTION direction) {
		boolean out = false;
		switch(direction) {
		case NORTH_UP:
			out = sendCmd("#:Mn#");
			if (out) isMovingN = true;
			break;
		case EAST_LEFT:
			out = sendCmd("#:Me#");
			if (out) isMovingE = true;
			break;
		case SOUTH_DOWN:
			out = sendCmd("#:Ms#");
			if (out) isMovingS = true;
			break;
		case WEST_RIGHT:
			out = sendCmd("#:Mw#");
			if (out) isMovingW = true;
			break;
		}
		if (out) {
			md = direction;
			isMoving = true;
		}
		return out;
    }
	@Override
    public synchronized boolean move(MOVE_DIRECTION direction, float seconds) {
		boolean out = false;
		final int time = (int) (seconds * 1000);
		if (time > 9999) return false;
		switch(direction) {
		case NORTH_UP:
			out = sendCmd("#:Mgn"+time+"#");
			if (out) isMovingN = true;
			break;
		case EAST_LEFT:
			out = sendCmd("#:Mge"+time+"#");
			if (out) isMovingE = true;
			break;
		case SOUTH_DOWN:
			out = sendCmd("#:Mgs"+time+"#");
			if (out) isMovingS = true;
			break;
		case WEST_RIGHT:
			out = sendCmd("#:Mgw"+time+"#");
			if (out) isMovingW = true;
			break;
		}
		if (out) {
			md = direction;
			isMoving = true;
			final Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(time+500);
						isMoving = checkMove();
						if (md == MOVE_DIRECTION.NORTH_UP) isMovingN = false;
						if (md == MOVE_DIRECTION.EAST_LEFT) isMovingE = false;
						if (md == MOVE_DIRECTION.WEST_RIGHT) isMovingW = false;
						if (md == MOVE_DIRECTION.SOUTH_DOWN) isMovingS = false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
		return out;
    }
	@Override
    public synchronized boolean stopMove(MOVE_DIRECTION direction) {
		boolean out = false;
		switch(direction) {
		case NORTH_UP:
			out = sendCmd("#:Qn#");
			if (out) isMovingN = false;
			break;
		case EAST_LEFT:
			out = sendCmd("#:Qe#");
			if (out) isMovingE = false;
			break;
		case SOUTH_DOWN:
			out = sendCmd("#:Qs#");
			if (out) isMovingS = false;
			break;
		case WEST_RIGHT:
			out = sendCmd("#:Qw#");
			if (out) isMovingW = false;
			break;
		}
		if (out) isMoving = checkMove();
		return out;
    }
	@Override
	public MOVE_DIRECTION getLastMoveDirection() {return md;}
	@Override
	public synchronized LocationElement getEquatorialPosition() {
		long t = System.currentTimeMillis();
		if (lastRADEC == -1 || t - lastRADEC > updateTime) {
			lastRADEC = t;
			String ra = "", dec = "";
			ra = sendCmdAndReceiveResponse("#:GR#");
			if (ra == null) return lastEq;
			if (!ra.equals("")) dec = sendCmdAndReceiveResponse("#:GD#");
            LocationElement lastEq = new LocationElement(raToFloat(ra) * Constant.DEG_TO_RAD, decToFloat(dec) * Constant.DEG_TO_RAD, 1.0);
            if (lastEq.getLatitude() != 0.0 && lastEq.getLongitude() != 0) {
	            if (this.lastEq != null) isMoving = LocationElement.getAngularDistance(lastEq, this.lastEq) > MOVE_TOLERANCE_1s;
	    		this.lastEq = lastEq;
	            return lastEq;
            }
		}
		return lastEq;
	}
	@Override
	public synchronized LocationElement getApparentEquatorialPosition() {
		LocationElement loc = this.getEquatorialPosition();
		EphemerisElement eph = new EphemerisElement(TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
				EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
				EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000,
				EphemerisElement.ALGORITHM.MOSHIER);
		eph.preferPrecisionInEphemerides = false;
		eph.correctForEOP = false;
		eph.correctForPolarMotion = false;
		TimeElement time = getTime();
		try {
			if (!this.telescopeModel.isJ2000()) return loc;
			loc = Ephem.fromJ2000ToApparentGeocentricEquatorial(loc, time, obs, eph);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return loc;
	}

	@Override
	public synchronized LocationElement getJ2000EquatorialPosition() {
		LocationElement loc = this.getEquatorialPosition();
		if (this.telescopeModel.isJ2000()) return loc;
		EphemerisElement eph = new EphemerisElement(TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
				EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
				EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000,
				EphemerisElement.ALGORITHM.MOSHIER);
		eph.preferPrecisionInEphemerides = false;
		eph.correctForEOP = false;
		eph.correctForPolarMotion = false;
		TimeElement time = getTime();
		try {
			loc.setRadius(2062650);
			loc = Ephem.toMeanEquatorialJ2000(loc, time, obs, eph);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return loc;
	}

	@Override
	public synchronized LocationElement getHorizontalPosition() {
		long t = System.currentTimeMillis();
		if (lastAZEL == -1 || t - lastAZEL > updateTime) {
			lastAZEL = t;
			String az = "", el = "";
			az = sendCmdAndReceiveResponse("#:GZ#");
			if (az == null) return lastHz;
			if (!az.equals("")) el = sendCmdAndReceiveResponse("#:GA#");
			LocationElement lastHz = new LocationElement(azToFloat(az) * Constant.DEG_TO_RAD, altToFloat(el) * Constant.DEG_TO_RAD, 1.0);
            if (lastHz.getLatitude() != 0.0 && lastHz.getLongitude() != 0) {
            	this.lastHz = lastHz;
            	return lastHz;
            }
		}
		return lastHz;
	}
	@Override
    public synchronized boolean setObjectCoordinates(LocationElement loc0, String name) {
		this.objLoc = loc0.clone();

		LocationElement loc = objLoc.clone();
		if (this.telescopeModel.isJ2000()) {
			try {
				EphemerisElement eph = new EphemerisElement(TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
						EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
						EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000,
						EphemerisElement.ALGORITHM.MOSHIER);
				eph.preferPrecisionInEphemerides = false;
				eph.correctForEOP = false;
				eph.correctForPolarMotion = false;
				TimeElement time = getTime();
				loc = Ephem.toMeanEquatorialJ2000(loc, time, obs, eph);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}

        double hms[] = Functions.getHMS(loc.getLongitude());
        double dms[] = Functions.getDMSs(loc.getLatitude());
        String ra = Functions.fmt((int) hms[0], 2, ':') + Functions.fmt((int) hms[1], 2, ':') + Functions.fmt((int) hms[2], 2);
        String dec = Functions.fmt((int) dms[0], 2, ':') + Functions.fmt((int) dms[1], 2, ':') + Functions.fmt((int) dms[2], 2);
        if (dms[3] == -1) {
        	dec = "-"+dec;
        } else {
        	dec = "+"+dec;
        }

        boolean rc = true;;
        sendCmd("#:Sr"+ra+"#");
        //rc = readBoolean();
        sendCmd("#:Sd"+dec+"#");
        rc = readBoolean();
        if (rc) object = name;
        return rc;
    }
	@Override
	public String getObjectName() {
		return object;
	}
	@Override
    public synchronized LocationElement getObjectCoordinates() {
		if (objLoc == null) return null;
		return objLoc.clone();
/*
		String ra = this.sendCmdAndReceiveResponse("#:Gr#");
		String dec = this.sendCmdAndReceiveResponse("#:Gd#");
        return new LocationElement(raToFloat(ra) * Constant.DEG_TO_RAD, decToFloat(dec) * Constant.DEG_TO_RAD, 1.0);
*/
    }

	@Override
    public synchronized boolean gotoObject() {
		String rc = this.sendCmdAndReceiveResponse("#:MS#");
		if (rc == null) return false;
		if (rc.startsWith("0")) return true;
		return false;
    }
	@Override
    public synchronized double distanceToPosition(LocationElement loc, boolean isEquatorial) {
    	if (isEquatorial) {
    		return LocationElement.getAngularDistance(loc, getEquatorialPosition());
    	} else {
    		return LocationElement.getAngularDistance(loc, getHorizontalPosition());
    	}
    }
	@Override
    public synchronized boolean isMoving(float seconds, double tolerance) {
    	LocationElement pos = this.getEquatorialPosition();
        try {
        	Thread.sleep((int) (seconds*1000));
        } catch(InterruptedException e) {}
    	return this.distanceToPosition(pos, true) > tolerance;
    }
	@Override
    public synchronized double getLocalTime() {
		String o = this.sendCmdAndReceiveResponse("#:GL#");
    	return raToFloat(o)/15.0;
    }
	@Override
    public synchronized boolean setLocalTime(double hours) {
        double hms[] = Functions.getHMS(hours / Constant.RAD_TO_HOUR);
        String time = Functions.fmt((int) hms[0], 2, ':') + Functions.fmt((int) hms[1], 2, ':') + Functions.fmt((int) hms[2], 2);

    	if (sendCmd("#:SL"+time+"#")) return readBoolean();
    	return false;
    }
	@Override
    public synchronized boolean sync() {
		if (objLoc == null) return false;
		this.sendCmdAndReceiveResponse("#:CM#");
		return true;
    }
	@Override
    public synchronized boolean stopMoving() {
    	if (sendCmd("#:Q#")) {
    		isMoving = false;
    		isMovingE = isMovingN = isMovingW = isMovingS = false;
    		return true;
    	}
    	return false;
    }
	@Override
    public synchronized boolean park() {
		if (parkPos == null) {
		   	//sendCmd("#:hS#");
		   	return sendCmd("#:hP#");
		}

		try {
			TimeElement time = getTime();
			EphemerisElement eph = new EphemerisElement(TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
					EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
					EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000,
					EphemerisElement.ALGORITHM.MOSHIER);
			eph.preferPrecisionInEphemerides = false;
			eph.correctForEOP = false;
			eph.correctForPolarMotion = false;
			LocationElement loc;
			LocationElement loc0 = null;
			if (objLoc != null) loc0 = objLoc.clone();
			if (parkPos != null) {
				loc = CoordinateSystem.horizontalToEquatorial(parkPos, time, obs, eph);
			} else {
				loc = CoordinateSystem.horizontalToEquatorial(new LocationElement(0, Constant.PI_OVER_TWO, 1), time, obs, eph);
			}
			this.setObjectCoordinates(loc, "Park position");
			this.gotoObject();
			objLoc = loc0;
			disconnect();
			// TODO: disable tracking
			return true;
		} catch (JPARSECException e) {
			return false;
		}    }
	@Override
    public synchronized boolean unpark() {
		if (parkPos == null) {
			// TODO: recover control from park (sleep) mode
			return false;
		}

		try {
			connect();
			// TODO: enable tracking
			return true;
		} catch (JPARSECException e) {
			return false;
		}
    }
	@Override
    public synchronized boolean setParkPosition(LocationElement loc) {
		parkPos = loc;
		return true;
	}
	@Override
    public synchronized String getTelescopeName() {
		String n = this.sendCmdAndReceiveResponse("#:GVP#");
    	return "Meade "+n;
    }
	@Override
    public synchronized String getTelescopePort() {
    	return sc.getPortName();
    }
	@Override
    public synchronized ObserverElement getObserver() {
		if (obs != null) return obs;
		String o = this.sendCmdAndReceiveResponse("#:GG#");
		if (o == null || o.equals("")) return new ObserverElement();
    		double localToUTC = Double.parseDouble(o);
    		o = this.sendCmdAndReceiveResponse("#:Gg#");
    		if (o.equals("")) return new ObserverElement();

        		if (!o.startsWith("-") && !o.startsWith("+")) o = " "+o;
        		if (o.startsWith("+")) o = " "+o.substring(1);
        		int ld = Integer.parseInt(o.substring(0, 4).trim());
        		int lm = Integer.parseInt(o.substring(5, 7));
        		int ls = 0;
        		if (o.length() > 7) ls = Integer.parseInt(o.substring(8));
        		double lon = -(Math.abs(ld) + lm / 60.0 + ls / 3600.0);
        		if (o.startsWith("-")) lon = -lon;
        		o = this.sendCmdAndReceiveResponse("#:Gt#");
        		if (o.equals("")) return new ObserverElement();
            		if (!o.startsWith("-") && !o.startsWith("+")) o = " "+o;
            		if (o.startsWith("+")) o = " "+o.substring(1);
            		ld = Integer.parseInt(o.substring(0, 3).trim());
            		lm = Integer.parseInt(o.substring(4, 6));
            		if (o.length() > 7) ls = Integer.parseInt(o.substring(7));
            		double lat = (Math.abs(ld) + lm / 60.0 + ls / 3600.0);
            		if (o.startsWith("-")) lat = -lat;

            		ObserverElement observer = new ObserverElement(getTelescopeName(), lon * Constant.DEG_TO_RAD, lat * Constant.DEG_TO_RAD, 0, -localToUTC, DST_RULE.NONE);
            		obs = observer;
            		return observer;
    }
	@Override
    public synchronized TimeElement getTime() {
		// Avoid calling too much since telescope response is slow
		if (time0 == null) {
    		try {
	    		String date = this.sendCmdAndReceiveResponse("#:GC#");
	    		int month = Integer.parseInt(FileIO.getField(1, date, "/", false).trim());
	    		int day = Integer.parseInt(FileIO.getField(2, date, "/", false).trim());
	    		int year = 2000 + Integer.parseInt(FileIO.getField(3, date, "/", false).trim());
	    		String time = this.sendCmdAndReceiveResponse("#:GL#");
	    		double hours = raToFloat(time) / 15.0;
	    		AstroDate astro = new AstroDate(year, month, day + hours / 24.0);
	    		TimeElement t = new TimeElement(astro, SCALE.LOCAL_TIME);

	    		time0 = new TimeElement();
	    		timeOffset = astro.jd() - time0.astroDate.jd();
	    		return t;
    		} catch (Exception exc) { }
    		return new TimeElement();
		} else {
    		TimeElement time = new TimeElement();
    		try { time.add(timeOffset); } catch (Exception exc) { exc.printStackTrace(); }
    		return time;
		}
    }
	@Override
    public synchronized boolean isTracking() {
		if (noGW || this.telescopeModel == TELESCOPE_MODEL.MEADE_AUTOSTAR) return true;
    	if (sendCmd("#:GW#")) {
    		String o = this.readString();
    		if (o.equals("")) noGW = true;
    		if (o != null && o.length() > 0 && o.substring(1, 2).equals("T")) return true;
    	}
    	return false;
    }
	@Override
    public synchronized boolean isAligned() {
		if (noGW || this.telescopeModel == TELESCOPE_MODEL.MEADE_AUTOSTAR) return true;
    	if (sendCmd("#:GW#")) {
    		String o = this.readString();
    		if (o.equals("")) noGW = true;
    		if (o != null && o.length() > 0 && !o.substring(2, 3).equals("0")) return true;
    	}
    	return false;
    }
	@Override
    public synchronized MOUNT getMount() {
		if (noGW || this.telescopeModel == TELESCOPE_MODEL.MEADE_AUTOSTAR) return MOUNT.AZIMUTHAL;
    	if (sendCmd("#:GW#")) {
    		String o = this.readString();
    		if (o.equals("")) noGW = true;
    		if (o != null && o.length() > 0 && !o.substring(0, 1).equals("A")) return MOUNT.AZIMUTHAL;
    		return MOUNT.EQUATORIAL;
    	}
    	return null;
    }
	@Override
    public synchronized boolean disconnect() {
		sc.closeConnection();
    	return true;
    }
	@Override
    public synchronized boolean connect() throws JPARSECException {
		sc.openConnection();
    	return true;
    }
	@Override
    public synchronized boolean isConnected() {
		return sc.isOpen();
    }

    /**
    * Convert RA from a string to a number.
    */
    private static double raToFloat(String ra) {
    	if (ra == null || ra.equals("")) return 0;
    	try {
	    	int tp = FileIO.getNumberOfFields(ra, ":", false);
	    	if (tp == 3) {
	            final float hrs=Integer.parseInt(ra.substring(0,2));
	            final float mins=Integer.parseInt(ra.substring(3,5));
	            final float secs=Integer.parseInt(ra.substring(6,8));
	            return (hrs+mins/60.0f+secs/3600.0f)*15.0f;
	    	} else {
	            final float hrs=Integer.parseInt(ra.substring(0,2));
	            final double mins=Double.parseDouble(ra.substring(3));
	            return (hrs+mins/60.0f)*15.0f;
	    	}
    	} catch (Exception exc) {
    		return 0;
    	}
    }
    /**
    * Convert dec from a string to a number.
    */
    private static double decToFloat(String dec) {
    	return altToFloat(dec);
    }
    /**
    * Convert alt from a string to a number.
    */
    private static double altToFloat(String alt) {
    	if (alt == null || alt.equals("")) return 0;
    	try {
	    	int tp = alt.indexOf(":");
	    	if (alt.startsWith("+")) alt = " "+alt.substring(1);
	    	if (tp > 0) {
	            final float degs=Integer.parseInt(alt.substring(0,3).trim());
	            final float mins=Integer.parseInt(alt.substring(4,6));
	            final float secs=Integer.parseInt(alt.substring(7,9));
	            if (!alt.startsWith("-"))
	                    return degs+mins/60.0f+secs/3600.0f;
	            else
	                    return degs-mins/60.0f-secs/3600.0f;
	    	} else {
	            final float degs=Integer.parseInt(alt.substring(0,3).trim());
	            final float mins=Integer.parseInt(alt.substring(4,6));
	            if (!alt.startsWith("-"))
	                    return degs+mins/60.0f;
	            else
	                    return degs-mins/60.0f;
	    	}
    	} catch (Exception exc) {
    		return 0;
    	}
    }
    /**
    * Convert az from a string to a number.
    */
    private static double azToFloat(String az) {
    	return altToFloat(az);
    }

    /**
    * Sets high precision.
    */
	private synchronized void setHighPrecision(boolean setHigh) {
            final boolean isHigh=toggleHighPrecision();
            if (setHigh!=isHigh) toggleHighPrecision();
    }
    private boolean toggleHighPrecision() {
		String s = this.sendCmdAndReceiveResponse("#:P#");
		return (s.startsWith("H") || s.startsWith("A")); // HIGH PRECISION / ALTO PRECISION (eng/spa). TODO: More langs ?!!!
    }
    /**
    * Sets long format.
    */
    private synchronized void setLongFormat(boolean setLong) {
            final boolean isLong=isLongFormatEnabled();
            if(setLong!=isLong) sendCmd("#:U#");
    }
    private boolean isLongFormatEnabled() {
		String reply = this.sendCmdAndReceiveResponse("#:GR#");
		return (reply.length()>7);
    }


    /**
    * Sends a command to the scope.
    */
    private synchronized boolean sendCmd(String cmd) {
    	try {
    		if (!sc.isOpen()) return false;
            sc.sendString(cmd.substring(1));
            return true;
    	} catch (Exception exc) {
    		exc.printStackTrace();
    		return false;
    	}
    }
    /**
    * Sends a command to the scope and returns a response.
    */
    private synchronized String sendCmdAndReceiveResponse(String cmd) {
    	try {
    		if (!sc.isOpen()) return null;

    		sc.sendString(cmd.substring(1));

            return this.readString();
    	} catch (Exception exc) {
    		exc.printStackTrace();
    		return null;
    	}
    }

    /**
    * Reads a boolean from the scope.
    */
    private boolean readBoolean() {
            String s = readString();
            if (s == null) return false;
            return s.equals("1");
    }
    /**
    * Reads a string from the scope, dropping the terminating #.
    */
    private synchronized String readString() {
		if (!sc.isOpen()) return "";

		String s = sc.receiveString();
    	if (s == null) return "";
    	if (s.endsWith("#")) s = s.substring(0, s.length()-1);
    	return s;
    }

    private boolean checkMove() {
    	if (isMovingN || isMovingE || isMovingW || isMovingS) return true;
		return isMoving; //this.isMoving(1, MOVE_TOLERANCE_1s);
    }

    @Override
    public TELESCOPE_MODEL getTelescopeModel() {
    	return telescopeModel;
    }

    @Override
    public double getFieldOfView(int camera) {
    	return field[camera];
    }

    @Override
    public void setFieldOfView(double field, int camera) {
    	this.field[camera] = field;
    }

	private GenericCamera[] cameras;
	@Override
	public GenericCamera[] getCameras() {
		return cameras;
	}

	@Override
	public void setCameras(GenericCamera[] cameras) throws JPARSECException {
		this.cameras = cameras;
	}

	@Override
	public boolean invertHorizontally() {
		return type.invertH();
	}

	@Override
	public boolean invertVertically() {
		return type.invertV();
	}

	private TELESCOPE_TYPE type = TELESCOPE_TYPE.SCHMIDT_CASSEGRAIN;
	@Override
	public void setTelescopeType(TELESCOPE_TYPE type) {
		this.type = type;
	}

	@Override
	public ImageHeaderElement[] getFitsHeader(int cameraIndex) {
		try {
			TimeElement time = getTime();
			ObserverElement obs = this.getObserver();
			EphemerisElement eph = new EphemerisElement(TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
					EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
					EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000,
					EphemerisElement.ALGORITHM.MOSHIER);
			eph.preferPrecisionInEphemerides = false;
			eph.correctForEOP = false;
			eph.correctForPolarMotion = false;

			double jd = TimeScale.getJD(time, obs, eph, SCALE.UNIVERSAL_TIME_UT1);
			LocationElement eq = this.getEquatorialPosition(), hz = this.getHorizontalPosition();
			LocationElement eqapp = this.getApparentEquatorialPosition(), eq2000 = this.getJ2000EquatorialPosition();

			ImageHeaderElement header0[] = new ImageHeaderElement[] {
					new ImageHeaderElement("BITPIX", "32", "Bits per data value"),
					new ImageHeaderElement("NAXIS", "2", "Dimensionality"),
					new ImageHeaderElement("NAXIS1", "0", "Width"),
					new ImageHeaderElement("NAXIS2", "0", "Height"),
					new ImageHeaderElement("EXTEND", "T", "Extension permitted"),
					new ImageHeaderElement("AUTHOR", Version.PACKAGE_NAME+" "+Version.VERSION_ID+", "+Version.AUTHOR, "Data author"),
					new ImageHeaderElement("BUNIT", "counts", "Physical unit"),
					new ImageHeaderElement("BSCALE", "1.0", "Data scaling factor"),
					new ImageHeaderElement("BZERO", ""+FastMath.multiplyBy2ToTheX(1, 31), "(minus) data zero value"),
					new ImageHeaderElement("DATE-OBS", ""+getTime().toString(), "Date and time, usually in LT"),
					new ImageHeaderElement("TIME_JD", ""+jd, "Date and time as JD, in UT1"),
					new ImageHeaderElement("OBS_LON", ""+obs.getLongitudeDeg(), "Longitude in deg, west negative"),
					new ImageHeaderElement("OBS_LAT", ""+obs.getLatitudeDeg(), "Latitude in deg, south negative"),
					new ImageHeaderElement("OBS_NAME", obs.getName(), "Observer name"),
					new ImageHeaderElement("OBS_TZ", ""+obs.getTimeZone(), "Time zone"),
					new ImageHeaderElement("OBS_DST", obs.getDSTCode().name(), "DST code"),
					new ImageHeaderElement("TEL_MODEL", this.telescopeModel.name(), "Telescope model (driver)"),
					new ImageHeaderElement("TEL_TYPE", this.type.name(), "Telescope type (S/C, refractor, ...)"),
					new ImageHeaderElement("TELESCOP", this.getTelescopeName(), "Telescope name"),
					new ImageHeaderElement("MOUNT", this.getMount().name(), "Telescope mount"),
					new ImageHeaderElement("CONNECTED", ""+this.isConnected(), "Telescope connected ?"),
					new ImageHeaderElement("TRACKING", ""+this.isTracking(), "Telescope tracking ?"),
					new ImageHeaderElement("ALIGNED", ""+this.isAligned(), "Telescope aligned ?"),
					new ImageHeaderElement("MOVING", ""+this.isMoving(), "Telescope moving ?"),
					new ImageHeaderElement("OBJECT", object, "Object name"),
					new ImageHeaderElement("RA", ""+eqapp.getLongitude(), "Telescope apparent, unrefracted RA"),
					new ImageHeaderElement("DEC", ""+eqapp.getLatitude(), "Telescope apparent, unrefracted DEC"),
					new ImageHeaderElement("RAJ2000", ""+eq2000.getLongitude(), "Telescope J2000 RA"),
					new ImageHeaderElement("DECJ2000", ""+eq2000.getLatitude(), "Telescope J2000 DEC"),
					new ImageHeaderElement("AZ", ""+hz.getLongitude(), "Telescope AZ"),
					new ImageHeaderElement("EL", ""+hz.getLatitude(), "Telescope EL")
			};

			if (cameraIndex < 0) return header0;

			GenericCamera camera = this.getCameras()[cameraIndex];
			if (isTracking()) {
				try {
					double timeExp = camera.getCCDorBulbModeTime();
					if (!camera.isBulb()) timeExp = Evaluation.evaluate(camera.getExpositionTime(), null);
					if (camera instanceof VirtualCamera) {
						time = ((VirtualCamera) camera).startTimeOfLastShot;
					} else {
						time.add(-timeExp / Constant.SECONDS_PER_DAY);
					}
					LocationElement hz0 = CoordinateSystem.equatorialToHorizontal(eq, time, obs, eph);
					header0 = ImageHeaderElement.addHeaderEntry(header0, new ImageHeaderElement("DATE0", time.toString(), "Date and time for the beginning of the observation"));
					header0 = ImageHeaderElement.addHeaderEntry(header0, new ImageHeaderElement("AZ0", ""+hz0.getLongitude(), "Telescope AZ for the beginning of the observation"));
					header0 = ImageHeaderElement.addHeaderEntry(header0, new ImageHeaderElement("EL0", ""+hz0.getLatitude(), "Telescope EL for the beginning of the observation"));
					time.add(0.5 * timeExp / Constant.SECONDS_PER_DAY);
					hz0 = CoordinateSystem.equatorialToHorizontal(eq, time, obs, eph);
					header0 = ImageHeaderElement.addHeaderEntry(header0, new ImageHeaderElement("DATE-EFF", time.toString(), "Date and time for the middle of the observation"));
					header0 = ImageHeaderElement.addHeaderEntry(header0, new ImageHeaderElement("AZ-EFF", ""+hz0.getLongitude(), "Telescope AZ for the middle of the observation"));
					header0 = ImageHeaderElement.addHeaderEntry(header0, new ImageHeaderElement("EL-EFF", ""+hz0.getLatitude(), "Telescope EL for the middle of the observation"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			ImageHeaderElement cameraHeader[] = camera.getFitsHeaderOfLastImage();
			cameraHeader = ImageHeaderElement.addHeaderEntry(cameraHeader, new ImageHeaderElement("FIELD", Functions.formatAngleAsDegrees(this.getFieldOfView(cameraIndex), 3), "Camera field of view (deg)"));
			cameraHeader = ImageHeaderElement.addHeaderEntry(cameraHeader, new ImageHeaderElement("CAM_INDEX", ""+cameraIndex, "Camera index id value"));

			return ImageHeaderElement.addHeaderEntry(header0, cameraHeader);
		} catch (Exception exc) {
			exc.printStackTrace();
			return null;
		}
	}

    /**
     * Constructor for a Meade telescope.
     * @param model The telescope model, must be a Meade one.
     * @param port The port to use, COMx in Windows and /dev/tty... in Linux.
     * Set to null to scan for all possible ports and launch a window to select
     * in case there's more than 1.
     * @throws JPARSECException If no serial ports are available.
     */
	public MeadeTelescope(TELESCOPE_MODEL model, String port) throws JPARSECException {
		if (!model.isMeade()) throw new JPARSECException("Telescope must be a Meade one!");
		telescopeModel = model;
		String ports[] = SerialConnection.getAvailablePorts();
		if (ports.length == 0) throw new JPARSECException("No serial ports available!");
		if (port == null) {
			if (ports.length == 1) {
				port = ports[0];
			} else {
				for (int i=0; i<ports.length; i++) {
			        sc = new SerialConnection();
			        sc.setPortName(ports[i]);
			        sc.openConnection();

			        String name = this.getTelescopeName();
			        if (name != null && !name.equals("")) {
			        	port = ports[i];
			        	sc.closeConnection();
			        	break;
			        }
			        sc.closeConnection();
				}
				if (port == null) {
					int s = JOptionPane.showOptionDialog(null,
							Translate.translate(1126),
							Translate.translate(1125), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, ports, ports[0]);
					if (s >= 0) port = ports[s];
				}
			}
		}
		if (port == null) throw new JPARSECException("No serial ports selected/available!");

        sc = new SerialConnection();
        sc.setPortName(port);
        sc.openConnection();
       	setLongFormat(true); // Long format interact with high precision, never call long format after high precision!
       	setHighPrecision(true);
       	setFocusSpeed(fs);
       	setMoveSpeed(ms);
	}

	@Override
	public boolean hasGOTO() {
		return true;
	}
}
