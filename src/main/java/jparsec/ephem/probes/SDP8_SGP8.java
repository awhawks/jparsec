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
package jparsec.ephem.probes;

import java.util.ArrayList;
import jparsec.astronomy.CoordinateSystem;
import jparsec.astronomy.Star;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Nutation;
import jparsec.ephem.Precession;
import jparsec.ephem.Target.TARGET;
import jparsec.ephem.event.Saros;
import jparsec.graph.DataSet;
import jparsec.math.Constant;
import jparsec.math.FastMath;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.observer.ReferenceEllipsoid.ELLIPSOID;
import jparsec.time.AstroDate;
import jparsec.time.SiderealTime;
import jparsec.time.TimeElement;
import jparsec.time.TimeElement.SCALE;
import jparsec.time.TimeScale;
import jparsec.util.DataBase;
import jparsec.util.JPARSECException;

/**
<p>The <code>SDP8_SGP8</code> class is a base class to calculate ephemeris for
an artificial Earth satellite, using the SGP8 and SDP8 models.</p>
<p>Based on JSGP by Matthew Funk.</p>

@author T. Alonso Albi - OAN (Spain)
 */
public class SDP8_SGP8
{
  private SatelliteOrbitalElement sat;
  /** Position vector [Gm] */
  private double[] itsR;
  /** Velocity vector [km/s] */
  private double[] itsV;
  /** The TLE epoch expressed in JD. */
  private double itsEpochJD;

  /** Whether period is >= 225 min */
  protected boolean isDeep;

  private double E1_EPOCH;
  final private double RHO = .15696615;

  // SDP8 only
  private double C0;
  private double C4;
  private double C5;
  private double SINI2;

  // SGP8
  private double A1;
  private double COSI;
  private double THETA2;
  private double TTHMUN;
  private double EOSQ;
  private double BETAO2;
  private double BETAO;
  private double DEL1;
  private double AO;
  private double DELO;
  private double AODP;
  private double XNODP;
  private double B;
  private int ISIMP;
  private double PO;
  private double POM2;
  private double SINI;
  private double SING;
  private double COSG;
  private double TEMP;
  private double SINIO2;
  private double COSIO2;
  private double THETA4;
  private double UNM5TH;
  private double UNMTH2;
  private double A3COF;
  private double PARDT1;
  private double PARDT2;
  private double PARDT4;
  private double XMDT1;
  private double XGDT1;
  private double XHDT1;
  private double XLLDOT;
  private double OMGDT;
  private double XNODOT;
  private double TSI;
  private double ETA;
  private double ETA2;
  private double PSIM2;
  private double ALPHA2;
  private double EETA;
  private double COS2G;
  private double D1;
  private double D2;
  private double D3;
  private double D4;
  private double D5;
  private double B1;
  private double B2;
  private double B3;
  private double c1;
  private double XNDT;
  private double XNDTN;
  private double D6;
  private double D7;
  private double D8;
  private double C8;
  private double C9;
  private double EDOT;
  private double D20;
  private double ALDTAL;
  private double TSDTTS;
  private double ETDT;
  private double PSDTPS;
  private double SIN2G;
  private double C0DTC0;
  private double C1DTC1;
  private double D9;
  private double D10;
  private double D11;
  private double D12;
  private double D13;
  private double D14;
  private double D15;
  private double D1DT;
  private double D2DT;
  private double D3DT;
  private double D4DT;
  private double D5DT;
  private double C4DT;
  private double C5DT;
  private double D16;
  private double XNDDT;
  private double EDDOT;
  private double D25;
  private double D17;
  private double TSDDTS;
  private double ETDDT;
  private double D18;
  private double D19;
  private double D23;
  private double D1DDT;
  private double XNTRDT;
  private double TMNDDT;
  private double PP;
  private double GAMMA;
  private double XND;
  private double QQ;
  private double ED;
  private double OVGPP;
  private DoubleRef XMAM = new DoubleRef(0.0);
  private DoubleRef OMGASM = new DoubleRef(0.0);
  private DoubleRef XNODES = new DoubleRef(0.0);
  private double TEMP1;
  private DoubleRef XN = new DoubleRef(0.0);
  private DoubleRef EM = new DoubleRef(0.0);
  private double Z1;
  private double Z7;
  private double ZC2;
  private double SINE;
  private double COSE;
  private double ZC5;
  private double CAPE;
  private double AM;
  private double BETA2M;
  private double SINOS;
  private double COSOS;
  private double AXNM;
  private double AYNM;
  private double PM;
  private double G1;
  private double G2;
  private double G3;
  private double BETA;
  private double G4;
  private double G5;
  private double SNF;
  private double CSF;
  private double FM;
  private double SNFG;
  private double CSFG;
  private double SN2F2G;
  private double CS2F2G;
  private double ECOSF;
  private double G10;
  private double RM;
  private double AOVR;
  private double G13;
  private double G14;
  private double DR;
  private double DIWC;
  private double DI;
  private double SNI2DU;
  private double XLAMB;
  private double Y4;
  private double Y5;
  private double R;
  private double RDOT;
  private double RVDOT;
  private double SNLAMB;
  private double CSLAMB;
  private double UX;
  private double UY;
  private double UZ;
  private double VX;
  private double VY;
  private double VZ;
  private DoubleRef XMAMDF = new DoubleRef(0.0f);
  private DoubleRef XINC = new DoubleRef(0.0f);

    public SatelliteOrbitalElement getSat() {
        return sat;
    }

  /**
   * The constructor to apply SDP8/SGP8 model.
   * @param sat The orbital elements.
   * @throws JPARSECException If the orbital elements cannot be
   * parsed.
   */
  public SDP8_SGP8(SatelliteOrbitalElement sat) throws JPARSECException {
	    this.sat = sat;
	    ReadNorad12(sat);
	    Init();
  }

  /**
   * Initialize the SDP8.
   *
   * <p>This initializes the SDP8 object.  Most state variables are set to
   * zero, some to constants necessary for the calculations.
 * @throws JPARSECException */

  private void Init() throws JPARSECException
  {
    itsR = new double[3];
    itsV = new double[3];
    itsR[0] = 0.01; itsR[1] = 0.; itsR[2] = 0.;
    itsV[0] = 0.;   itsV[1] = 0.; itsV[2] = 0.;
    //itsEpochJD    = 0.;


    // RECOVER ORIGINAL MEAN MOTION (XNODP) AND SEMIMAJOR AXIS (AODP)
    // FROM INPUT ELEMENTS --------- CALCULATE BALLISTIC COEFFICIENT
    // (B TERM) FROM INPUT B* DRAG TERM

    A1=Math.pow((C1.XKE/(sat.meanMotion/C1.XMNPDA)),C1.TOTHRD);
    COSI=Math.cos(sat.inclination);
    THETA2=COSI*COSI;
    TTHMUN=3.*THETA2-1.;
    EOSQ=sat.eccentricity*sat.eccentricity;
    BETAO2=1.-EOSQ;
    BETAO=Math.sqrt(BETAO2);
    DEL1=1.5*C1.CK2*TTHMUN/(A1*A1*BETAO*BETAO2);
    AO=A1*(1.-DEL1*(.5*C1.TOTHRD+DEL1*(1.+134./81.*DEL1)));
    DELO=1.5*C1.CK2*TTHMUN/(AO*AO*BETAO*BETAO2);
    AODP=AO/(1.-DELO);
    XNODP=(sat.meanMotion/C1.XMNPDA)/(1.+DELO);
    B=2.*(sat.drag/C1.AE)/RHO;

    // INITIALIZATION

    PO=AODP*BETAO2;
    POM2=1./(PO*PO);
    SINI=Math.sin(sat.inclination);
    SING=Math.sin(sat.argumentOfPerigee);
    COSG=Math.cos(sat.argumentOfPerigee);
    TEMP=.5*sat.inclination;
    SINIO2=Math.sin(TEMP);
    COSIO2=Math.cos(TEMP);
    THETA4=Math.pow(THETA2,2);
    UNM5TH=1.-5.*THETA2;
    UNMTH2=1.-THETA2;
    A3COF=-C1.XJ3/C1.CK2*Math.pow(C1.AE,3);
    PARDT1=3.*C1.CK2*POM2*XNODP;
    PARDT2=PARDT1*C1.CK2*POM2;
    PARDT4=1.25*C1.CK4*POM2*POM2*XNODP;
    XMDT1=.5*PARDT1*BETAO*TTHMUN;
    XGDT1=-.5*PARDT1*UNM5TH;
    XHDT1=-PARDT1*COSI;
    XLLDOT=XNODP+XMDT1+
        .0625*PARDT2*BETAO*(13.-78.*THETA2+137.*THETA4);
    OMGDT=XGDT1+
        .0625*PARDT2*(7.-114.*THETA2+395.*THETA4)+PARDT4*(3.-36.*
        THETA2+49.*THETA4);
    XNODOT=XHDT1+
        (.5*PARDT2*(4.-19.*THETA2)+2.*PARDT4*(3.-7.*THETA2))*COSI;
    TSI=1./(PO-C1.S);
    ETA=sat.eccentricity*C1.S*TSI;
    ETA2=Math.pow(ETA,2);
    PSIM2=Math.abs(1./(1.-ETA2));
    ALPHA2=1.+EOSQ;
    EETA=sat.eccentricity*ETA;
    COS2G=2.*Math.pow(COSG,2)-1.;
    D5=TSI*PSIM2;
    D1=D5/PO;
    D2=12.+ETA2*(36.+4.5*ETA2);
    D3=ETA2*(15.+2.5*ETA2);
    D4=ETA*(5.+3.75*ETA2);
    B1=C1.CK2*TTHMUN;
    B2=-C1.CK2*UNMTH2;
    B3=A3COF*SINI;


    C0=.5*B*RHO*C1.QOMS2T*XNODP*AODP*Math.pow(TSI,4)*Math.pow(PSIM2,3.5)/Math.sqrt(ALPHA2);
    c1=1.5*XNODP*Math.pow(ALPHA2,2)*C0;
    C4=D1*D3*B2;
    C5=D5*D4*B3;
    XNDT=c1*(
        (2.+ETA2*(3.+34.*EOSQ)+5.*EETA*(4.+ETA2)+8.5*EOSQ)+
        D1*D2*B1+   C4*COS2G+C5*SING);
    XNDTN=XNDT/XNODP;

    if (isDeep) {
	    EDOT=-C1.TOTHRD*XNDTN*(1.-sat.eccentricity);
	    DEEP.DPINIT(sat,EOSQ,SINI,COSI,BETAO,AODP,THETA2,SING,COSG,
	                BETAO2,XLLDOT,OMGDT,XNODOT,XNODP,E1_EPOCH);
	    return;
    }

    // INITIALIZATION SGP8

    ISIMP=0;

    // IF DRAG IS VERY SMALL, THE ISIMP FLAG IS SET AND THE
    // EQUATIONS ARE TRUNCATED TO LINEAR VARIATION IN MEAN
    // MOTION AND QUADRATIC VARIATION IN MEAN ANOMALY

    if (Math.abs(XNDTN*C1.XMNPDA) >= 2.16E-3)
    {
        D6=ETA*(30.+22.5*ETA2);
        D7=ETA*(5.+12.5*ETA2);
        D8=1.+ETA2*(6.75+ETA2);
        C8=D1*D7*B2;
        C9=D5*D8*B3;
        EDOT=-C0*(
            ETA*(4.+ETA2+EOSQ*(15.5+7.*ETA2))+sat.eccentricity*(5.+15.*ETA2)+
            D1*D6*B1 +
            C8*COS2G+C9*SING);
        D20=.5*C1.TOTHRD*XNDTN;
        ALDTAL=sat.eccentricity*EDOT/ALPHA2;
        TSDTTS=2.*AODP*TSI*(D20*BETAO2+sat.eccentricity*EDOT);
        ETDT=(EDOT+sat.eccentricity*TSDTTS)*TSI*C1.S;
        PSDTPS=-ETA*ETDT*PSIM2;
        SIN2G=2.*SING*COSG;
        C0DTC0=D20+4.*TSDTTS-ALDTAL-7.*PSDTPS;
        C1DTC1=XNDTN+4.*ALDTAL+C0DTC0;
        D9=ETA*(6.+68.*EOSQ)+sat.eccentricity*(20.+15.*ETA2);
        D10=5.*ETA*(4.+ETA2)+sat.eccentricity*(17.+68.*ETA2);
        D11=ETA*(72.+18.*ETA2);
        D12=ETA*(30.+10.*ETA2);
        D13=5.+11.25*ETA2;
        D14=TSDTTS-2.*PSDTPS;
        D15=2.*(D20+sat.eccentricity*EDOT/BETAO2);
        D1DT=D1*(D14+D15);
        D2DT=ETDT*D11;
        D3DT=ETDT*D12;
        D4DT=ETDT*D13;
        D5DT=D5*D14;
        C4DT=B2*(D1DT*D3+D1*D3DT);
        C5DT=B3*(D5DT*D4+D5*D4DT);
        D16=
            D9*ETDT+D10*EDOT +
            B1*(D1DT*D2+D1*D2DT) +
            C4DT*COS2G+C5DT*SING+XGDT1*(C5*COSG-2.*C4*SIN2G);
        XNDDT=C1DTC1*XNDT+c1*D16;
        EDDOT=C0DTC0*EDOT-C0*(
            (4.+3.*ETA2+30.*EETA+EOSQ*(15.5+21.*ETA2))*ETDT+(5.+15.*ETA2
                +EETA*(31.+14.*ETA2))*EDOT +
                B1*(D1DT*D6+D1*ETDT*(30.+67.5*ETA2))  +
                B2*(D1DT*D7+D1*ETDT*(5.+37.5*ETA2))*COS2G+
                B3*(D5DT*D8+D5*ETDT*ETA*(13.5+4.*ETA2))*SING+XGDT1*(C9*
                    COSG-2.*C8*SIN2G));
        D25=Math.pow(EDOT,2);
        D17=XNDDT/XNODP-Math.pow(XNDTN,2);
        TSDDTS=2.*TSDTTS*(TSDTTS-D20)+AODP*TSI*(C1.TOTHRD*BETAO2*D17-4.*D20*
            sat.eccentricity*EDOT+2.*(D25+sat.eccentricity*EDDOT));
        ETDDT =(EDDOT+2.*EDOT*TSDTTS)*TSI*C1.S+TSDDTS*ETA;
        D18=TSDDTS-Math.pow(TSDTTS,2);
        D19=-Math.pow(PSDTPS,2)/ETA2-ETA*ETDDT*PSIM2-Math.pow(PSDTPS,2);
        D23=ETDT*ETDT;
        D1DDT=D1DT*(D14+D15)+D1*(D18-2.*D19+C1.TOTHRD*D17+2.*(ALPHA2*D25
            /BETAO2+sat.eccentricity*EDDOT)/BETAO2);
        XNTRDT=XNDT*(2.*C1.TOTHRD*D17+3.*
            (D25+sat.eccentricity*EDDOT)/ALPHA2-6.*Math.pow(ALDTAL,2) +
            4.*D18-7.*D19 )   +
            C1DTC1*XNDDT+c1*(C1DTC1*D16+
                D9*ETDDT+D10*EDDOT+D23*(6.+30.*EETA+68.*EOSQ)+
                ETDT*EDOT*(40.+30.*
                    ETA2+272.*EETA)+D25*(17.+68.*ETA2) +
                    B1*(D1DDT*D2+2.*D1DT*D2DT+D1*(ETDDT*D11+D23*(72.+54.*ETA2))) +
                    B2*(D1DDT*D3+2.*D1DT*D3DT+D1*(ETDDT*D12+D23*(30.+30.*ETA2))) *
                    COS2G+
                    B3*((D5DT*D14+D5*(D18-2.*D19)) *
                        D4+2.*D4DT*D5DT+D5*(ETDDT*D13+22.5*ETA*D23)) *SING+XGDT1*
                        ((7.*D20+4.*sat.eccentricity*EDOT/BETAO2)*
                            (C5*COSG-2.*C4*SIN2G)
                            +((2.*C5DT*COSG-4.*C4DT*SIN2G)-XGDT1*(C5*SING+4.*
                                C4*COS2G))));
        TMNDDT=XNDDT*1.E9;
        TEMP=Math.pow(TMNDDT,2)-XNDT*1.E18*XNTRDT;
        PP=(TEMP+Math.pow(TMNDDT,2))/TEMP;
        GAMMA=-XNTRDT/(XNDDT*(PP-2.));
        XND=XNDT/(PP*GAMMA);
        QQ=1.-EDDOT/(EDOT*GAMMA);
        ED=EDOT/(QQ*GAMMA);
        OVGPP=1./(GAMMA*(PP+1.));
    }
    else
    {
        ISIMP=1;
        EDOT=-C1.TOTHRD*XNDTN*(1.-sat.eccentricity);
    }
    return;
  }

	private boolean FAST_MODE = false;

  /**
   * Calculate ephemeris for the satellite.
   * The ephemerisElement object is used when transforming to apparent
   * coordinates. In any other case output position is the same
   * (geometric = astrometric).
   *
   * @param time Time object.
   * @param obs Observer object.
   * @param eph Ephemeris object.
   * @return The ephemeris.
 * @throws JPARSECException  If an error occurs.*/
  public SatelliteEphemElement calcSatellite(TimeElement time, ObserverElement obs, EphemerisElement eph) throws JPARSECException
  {
	  return calcSatellite(time, obs, eph, true);
  }

  /**
   * Calculate ephemeris for the satellite.
   * The ephemerisElement object is used when transforming to apparent
   * coordinates. In any other case output position is the same
   * (geometric = astrometric).
   *
   * @param time Time object.
   * @param obs Observer object.
   * @param eph Ephemeris object.
   * @param magAndSize True to calculate magnitude and size, which is a
   * relatively slow operation.
   * @return The ephemeris.
 * @throws JPARSECException  If an error occurs.*/
  public SatelliteEphemElement calcSatellite(TimeElement time, ObserverElement obs, EphemerisElement eph,
		  boolean magAndSize) throws JPARSECException
  {
    int[] IFLAG = new int[1];

	double JD = TimeScale.getJD(time, obs, eph, SCALE.UNIVERSAL_TIME_UTC);
	boolean exactMode = FastMath.EXACT_MODE;
	FastMath.EXACT_MODE = false;
	if (!FAST_MODE) FastMath.EXACT_MODE = true;

    double TS = C1.XMNPDA * (JD - itsEpochJD);

    IFLAG[0] = 1;
    if (!isDeep) {RunSGP8(IFLAG, TS);}
    else                {RunSDP8(IFLAG, TS);}


    // Rest of calculations for topocentric results
	double LAT = obs.getLatitudeRad();
	double LON = obs.getLongitudeRad();
	double HT_km = obs.getHeight() / 1000.0;

	double cosLAT = FastMath.cos(LAT);
	double sinLAT = FastMath.sin(LAT);
	double cosLON = FastMath.cos(LON);
	double sinLON = FastMath.sin(LON);

	ELLIPSOID ellipsoid = obs.getEllipsoid();
	double equatorialRadius = ellipsoid.getEquatorialRadius();
	double flatenning = 1.0 / ellipsoid.getInverseOfFlatteningFactor();
	double polarRadius = equatorialRadius * (1.0 - flatenning);

	double D = FastMath.hypot(equatorialRadius * cosLAT, polarRadius * sinLAT); //Math.sqrt(equatorialRadius * equatorialRadius * cosLAT * cosLAT + polarRadius * polarRadius * sinLAT * sinLAT);
	double Rx = equatorialRadius * equatorialRadius / D + HT_km;
	double Rz = polarRadius * polarRadius / D + HT_km;

	// Observer's unit vectors UP EAST and NORTH in GEOCENTRIC coords.
	double Ux = cosLAT * cosLON;
	double Ex = -sinLON;
	double Nx = -sinLAT * cosLON;

	double Uy = cosLAT * sinLON;
	double Ey = cosLON;
	double Ny = -sinLAT * sinLON;

	double Uz = sinLAT;
	//Ez = 0.0;
	double Nz = cosLAT;

	// Observer's XYZ coords at Earth's surface
	double Ox = Rx * Ux;
	double Oy = Rx * Uy;
	double Oz = Rz * Uz;

	double YT = Constant.TROPICAL_YEAR; // Tropical year, days
	double earthTraslationRate = Constant.TWO_PI / YT; // Earth's traslation rate, rads/whole day
	double earthTraslationRate2 = Constant.TWO_PI + earthTraslationRate; // ditto radians/day
	double W0 = earthTraslationRate2 / Constant.SECONDS_PER_DAY; // ditto radians/sec

	double VOx = -Oy * W0;
	double VOy = Ox * W0; // Observer's velocity, GEOCENTRIC coords. (VOz=0)

    // Parse output from SDP
    double SATx = itsR[0] * 1.0E6;
    double SATy = itsR[1] * 1.0E6;
    double SATz = itsR[2] * 1.0E6;
    double VELx = itsV[0];
    double VELy = itsV[1];
    double VELz = itsV[2];

	double GHAA = jparsec.time.SiderealTime.greenwichMeanSiderealTime(time, obs, eph);
	if (!FAST_MODE) GHAA += jparsec.time.SiderealTime.equationOfEquinoxes(time, obs, eph);

	double C = FastMath.cos(GHAA);
	double S = -FastMath.sin(GHAA);
	double Sx = (SATx * C - SATy * S);
	double Vx = VELx * C - VELy * S;
	double Sy = (SATx * S + SATy * C);
	double Vy = VELx * S + VELy * C;
	double Sz = SATz;
	double Vz = VELz;

	double JD_TDB = 0;
	if (!FAST_MODE) {
		JD_TDB = TimeScale.getJD(time, obs, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
		DataBase.addData("GCRS", Precession.precessToJ2000(JD_TDB, new double[] {Sx / Constant.AU, Sy / Constant.AU, Sz / Constant.AU}, eph), true);
	}

	// Compute and manipulate range/velocity/antenna vectors
	Rx = Sx - Ox;
	double Ry = Sy - Oy;
	Rz = Sz - Oz; // Rangevec = Satvec - Obsvec
	double R = Math.sqrt(Rx * Rx + Ry * Ry + Rz * Rz); // Range magnitude
	Rx = Rx / R;
	Ry = Ry / R;
	Rz = Rz / R; // Normalise Range vector
	double U = Rx * Ux + Ry * Uy + Rz * Uz; // UP Component of unit range
	double E = Rx * Ex + Ry * Ey; // EAST do (Ez=0)
	double N = Rx * Nx + Ry * Ny + Rz * Nz; // NORTH do
	double AZI = FastMath.atan2_accurate(E, N); // Azimuth
	double EL = FAST_MODE ? FastMath.asin(U) : Math.asin(U); // Elevation

	// Calculate sub-satellite Lat/Lon
	double RS = Math.sqrt(Sx * Sx + Sy * Sy + Sz * Sz);
	double SLON = FastMath.atan2_accurate(Sy, Sx); // Lon, + East
	double SLAT = FAST_MODE ? FastMath.asin(Sz / RS) : Math.asin(Sz / RS); // Lat, + North
	double HGT = RS - equatorialRadius;

	// Resolve Sat-Obs velocity vector along unit range vector. (VOz=0)
	double RR = (Vx-VOx)*Rx + (Vy-VOy)*Ry + Vz*Rz; // Range rate, km/s

	// Sidereal and Solar data. NEVER needs changing. Valid to year 2000+
	double G0 = 98.9821; // GHAA, Year YG, Jan 0.0
	double MAS0 = 356.0507; // MA Sun and rate, deg, deg/day
	double MASD = 0.98560028; // MA Sun and rate, deg, deg/day
	double EQC1 = 0.03342;
	double EQC2 = 0.00035; // Sun's Equation of centre terms
	double EQC3 = 5.0E-6;
	double year = sat.year;
	double days = sat.day;
	// Convert satellite Epoch to Day No. and Fraction of day
	AstroDate astro = new AstroDate((int) year, 1, (int) Math.floor(days));
	double DE = astro.jd();
	days = days - Math.floor(days);
	double TN = 0.0;
	double DN = JD;
	double T = (DN - DE) + (TN - days); // Elapsed T since epoch, days

	// Revolution number
	double dragCoeff = -2.0 * sat.firstDerivative / (sat.meanMotion * 3.0); // Drag coeff. (Angular momentum rate)/(Ang mom) s^-1
	double DT = dragCoeff * T / 2.0;
	double KDP = 1.0 - 7.0 * DT; // Linear drag terms
	double M = sat.meanAnomaly + sat.meanMotion * T * (1.0 - 3.0 * DT); // Mean anomaly at YR,TN
	double DR = Math.floor(M / Constant.TWO_PI); // Strip out whole no of revs
	double RN = sat.revolutionNumber + DR; // Current Orbit number

	// Bring Sun data to Satellite Epoch
	double TEG = (DE - 2451543.5) + days; // Elapsed Time: Epoch - YG

	double sunMeanRA = Constant.DEG_TO_RAD * G0 + TEG * earthTraslationRate + Math.PI; // Mean RA Sun at Sat epoch
	double sunMeanAnomaly = Constant.DEG_TO_RAD * (MAS0 + MASD * TEG); // Mean MA Sun ..

	// Note other programs (XEphem among them) uses the following lines, which seems to be wrong
	// by 0.004 deg around year 2011. Algorithm at Saros class from Calendrical Calculations agree
	// with previous code up to 0.00001 deg.
	//double Tp = (itsEpochJD - 2415020.0) / 36525.0;
    //double sunMeanAnomaly2 = (358.475845 + 35999.04975 * Tp - 0.00015 * Tp * Tp - 0.00000333333 * Tp * Tp * Tp) * Constant.DEG_TO_RAD;

	double MAS = sunMeanAnomaly + Constant.DEG_TO_RAD * MASD * T; // MA of Sun round its orbit
	MAS = Functions.normalizeRadians(MAS);
	double TAS = sunMeanRA + earthTraslationRate * T + EQC1 * FastMath.sin(MAS) + EQC2 * FastMath.sin(2 * MAS) + EQC3 * FastMath.sin(3 * MAS);
	TAS = Functions.normalizeRadians(TAS);

	double INS = Constant.DEG_TO_RAD * 23.4393;
	double CNS = FastMath.cos(INS);
	double SNS = FastMath.sin(INS); // Sun's inclination
	C = FastMath.cos(TAS);
	S = FastMath.sin(TAS); // Sin/Cos Sun's true anomaly
	double SUNx = C;
	double SUNy = S * CNS;
	double SUNz = S * SNS; // Sun unit vector - CELESTIAL coords

	// Antenna location (for ILL)
	// Fixed (not real) values for the orientation of the satellite antenna
	double ALON = 180.0;
	double ALAT = 0;
	// Average Precession rates
	double GM = 3.98600433e14 * 1.0e-9; // Earth's Gravitational constant g' * R' * R' km^3/s^2, DE405
	double J2 = 0.00108263; // 2nd Zonal coeff, Earth's Gravity Field
	double n = sat.meanMotion / Constant.SECONDS_PER_DAY; // Mean motion rad/s
	double a = Math.pow((GM / (n * n)), 1.0 / 3.0); // Semi major axis km
	double b = a * Math.sqrt(1.0 - sat.eccentricity * sat.eccentricity); // Semi minor axis km. Note 'in astronomy' (comets/asteroids) b=a*(1-e)
	double sinIncl = FastMath.sin(sat.inclination);
	double cosIncl = FastMath.cos(sat.inclination);
	double PC = equatorialRadius * a / (b * b);
	PC = 1.5 * J2 * PC * PC * sat.meanMotion; // Precession const, rad/Day
	double nodePrecessionRate = -PC * cosIncl; // Node precession rate, rad/day
	double perigeePrecessionRate = PC * (5.0 * cosIncl * cosIncl - 1.0) / 2.0; // Perigee precession rate, rad/day
	// Antenna unit vector in orbit plane coordinates.
	cosLON = FastMath.cos(Constant.DEG_TO_RAD * ALON);
	sinLON = FastMath.sin(Constant.DEG_TO_RAD * ALON);
	cosLAT = FastMath.cos(Constant.DEG_TO_RAD * ALAT);
	sinLAT = FastMath.sin(Constant.DEG_TO_RAD * ALAT);
	double Ax = -cosLAT * cosLON;
	double Ay = -cosLAT * sinLON;
	double Az = -sinLAT;
	double correctedArgPerigee = sat.argumentOfPerigee + perigeePrecessionRate * T * KDP;
	double cosCorrectedArgPerigee = FastMath.cos(correctedArgPerigee);
	double sinCorrectedArgPerigee = FastMath.sin(correctedArgPerigee);
	double correctedNodeRA = sat.ascendingNodeRA + nodePrecessionRate * T * KDP;
	double cosCorrectedNodeRA = FastMath.cos(correctedNodeRA);
	double sinCorrectedNodeRA = FastMath.sin(correctedNodeRA);
	// Plane -> celestial coordinate transformation, [C] = [RAAN]*[IN]*[AP]
	double CXx = cosCorrectedArgPerigee * cosCorrectedNodeRA - sinCorrectedArgPerigee * cosIncl * sinCorrectedNodeRA;
	double CXy = -sinCorrectedArgPerigee * cosCorrectedNodeRA - cosCorrectedArgPerigee * cosIncl * sinCorrectedNodeRA;
	double CXz = sinIncl * sinCorrectedNodeRA;
	double CYx = cosCorrectedArgPerigee * sinCorrectedNodeRA + sinCorrectedArgPerigee * cosIncl * cosCorrectedNodeRA;
	double CYy = -sinCorrectedArgPerigee * sinCorrectedNodeRA + cosCorrectedArgPerigee * cosIncl * cosCorrectedNodeRA;
	double CYz = -sinIncl * cosCorrectedNodeRA;
	double CZx = sinCorrectedArgPerigee * sinIncl;
	double CZy = cosCorrectedArgPerigee * sinIncl;
	double CZz = cosIncl;
	double ANTx = Ax * CXx + Ay * CXy + Az * CXz;
	double ANTy = Ax * CYx + Ay * CYy + Az * CYz;
	double ANTz = Ax * CZx + Ay * CZy + Az * CZz;

	// Find Solar angle, elongation, illumination, and eclipse status.
	double SSA = -(ANTx * SUNx + ANTy * SUNy + ANTz * SUNz); // Sin of Sun angle -a.h
	double ILL = Math.sqrt(1.0 - SSA * SSA); // Illumination
//	double SATx = C * (Sx + S * Sy) / (S * S + C * C);
//	double SATy = S * (-Sx + C * Sy) / (S * S + C * C);
//	double SATz = Sz;
//	double ELO = Math.asin(Rx * SUNx + Ry * SUNy + Rz * SUNz); // Calculated later from az/el
	double CUA = -(SATx * SUNx + SATy * SUNy + SATz * SUNz) / RS; // Cos of umbral angle -h.s
	double UMD = RS * Math.sqrt(1.0 - CUA * CUA) / equatorialRadius; // Umbral dist, Earth radii

	String ECL = "Visible at sunset/sunrise";
	if (CUA <= 0.0)
		ECL = "Visible with the sun"; // + for shadow side
	if (UMD <= 1.0 && CUA >= 0.0)
		ECL = "Eclipsed"; // - for sunny side

	// Obtain SUN unit vector in GEOCENTRIC coordinates
	C = FastMath.cos(Constant.TWO_PI-GHAA);
	S = FastMath.sin(Constant.TWO_PI-GHAA);
	double Hx = SUNx * C - SUNy * S;
	double Hy = SUNx * S + SUNy * C; // If Sun more than 10 deg below horizon
	double Hz = SUNz; // satellite possibly visible

	U = Hx * Ux + Hy * Uy + Hz * Uz;
	E = Hx * Ex + Hy * Ey;
	N = Hx * Nx + Hy * Ny + Hz * Nz;
	double SAZ = FastMath.atan2_accurate(E, N); // Azimuth
	double SEL = FAST_MODE ? FastMath.asin(U) : Math.asin(U); // Elevation

	if ((SEL * Constant.RAD_TO_DEG < -10.0) && !(ECL.equals("Eclipsed")))
		ECL = "Possibly visible";

	double iridiumAngle = SatelliteEphem.iridiumAngle(new double[] {Sx, Sy, Sz}, new double[] {Vx, Vy, Vz},
			new double[] {Sx - Ox, Sy - Oy, Sz - Oz}, new double[] {Hx, Hy, Hz});

	// Obtain Moon iridium angle
	double jdTT = TimeScale.getJD(time, obs, eph, SCALE.TERRESTRIAL_TIME);
	double pos[] = Saros.getMoonPosition(jdTT);
	double iridiumAngleMoon = 100;
	if (pos[3] < 29.5306-3.0 && pos[3] > 3.0) {
		double toIlluminatedDiskCenter = 0.25 * Constant.DEG_TO_RAD * (pos[3] - 29.53 * 0.5) / 15.0;
		LocationElement locMoon = CoordinateSystem.eclipticToEquatorial(
				new LocationElement(pos[0] + toIlluminatedDiskCenter, pos[1], 1.0), INS, true);
		double locM[] = locMoon.getRectangularCoordinates();
		double MOONx = locM[0], MOONy = locM[1], MOONz = locM[2];

		double Mx = MOONx * C - MOONy * S;
		double My = MOONx * S + MOONy * C;
		double Mz = MOONz;
		iridiumAngleMoon = SatelliteEphem.iridiumAngle(new double[] {Sx, Sy, Sz}, new double[] {Vx, Vy, Vz},
				new double[] {Sx - Ox, Sy - Oy, Sz - Oz}, new double[] {Mx, My, Mz});
	}

	// Obtain Sun unit vector in EQ coordinates
//	Hx =  SUNx*CXx + SUNy*CYx + SUNz*CZx;
//	Hy =  SUNx*CXy + SUNy*CYy + SUNz*CZy;
//	Hz =  SUNx*CXz + SUNy*CYz + SUNz*CZz;

	boolean isEclipsed = false;
	if (ECL.equals("Eclipsed")) isEclipsed = true;

	FastMath.EXACT_MODE = exactMode;

	double ELO = 0;
	if (FAST_MODE) {
		ELO = LocationElement.getApproximateAngularDistance(new LocationElement(SAZ, SEL, 1.0), new LocationElement(AZI, EL, 1.0));
	} else {
		ELO = LocationElement.getAngularDistance(new LocationElement(SAZ, SEL, 1.0), new LocationElement(AZI, EL, 1.0));
	}

	LocationElement loc_horiz = new LocationElement(AZI, EL, R);
	double ast = FAST_MODE ? GHAA + obs.getLongitudeRad() : SiderealTime.apparentSiderealTime(time, obs, eph);
	LocationElement loc_eq = CoordinateSystem.horizontalToEquatorial(loc_horiz, ast, obs.getLatitudeRad(), true);

	if (FAST_MODE) {
		SatelliteEphemElement ephem = new SatelliteEphemElement(sat.getName(), loc_eq.getLongitude(), loc_eq.getLatitude(), R, AZI, EL,
				(float) SLON, (float) SLAT, (float) HGT, (float) RR, (float) ELO, (float) ILL,
				isEclipsed, (int) RN);

		ephem.iridiumAngle = (float) iridiumAngle;
		ephem.iridiumAngleForMoon = (float) iridiumAngleMoon;
		ephem.sunElevation = (float) SEL;
		return ephem;
	}

	// Mean equatorial to true equatorial
	if (eph.ephemType == EphemerisElement.COORDINATES_TYPE.APPARENT)
	{
		/* Correct nutation */
		double true_eq[] = loc_eq.getRectangularCoordinates();
		true_eq = Nutation.nutateInEquatorialCoordinates(JD_TDB, eph, true_eq, true);
		loc_eq = LocationElement.parseRectangularCoordinates(true_eq);

		/* To apparent elevation */
		loc_horiz = CoordinateSystem.equatorialToHorizontal(loc_eq, ast, obs, eph, true, true);
		//loc_horiz.setLatitude(Ephem.getApparentElevation(eph, obs, loc_horiz.getLatitude(), 5));
	}

	SatelliteEphemElement ephem = new SatelliteEphemElement(sat.getName(), loc_eq.getLongitude(), loc_eq.getLatitude(),
			loc_eq.getRadius(), loc_horiz.getLongitude(), loc_horiz.getLatitude(), (float) SLON, (float) SLAT, (float) HGT, (float) RR, (float) ELO, (float) ILL,
			isEclipsed, (int) RN);

	if (magAndSize) ephem = SatelliteEphem.getMagnitudeAndAngularSize(ephem, sat);
	ephem.iridiumAngle = (float) iridiumAngle;
	ephem.iridiumAngleForMoon = (float) iridiumAngleMoon;
	ephem.sunElevation = (float) SEL;

	// Correct apparent magnitude for extinction
	if (eph.ephemType == EphemerisElement.COORDINATES_TYPE.APPARENT && eph.correctForExtinction
			&& obs.getMotherBody() == TARGET.EARTH && ephem.magnitude != SatelliteEphemElement.UNKNOWN_MAGNITUDE)
		ephem.magnitude += Star.getExtinction(Constant.PI_OVER_TWO-ephem.elevation, obs.getHeight() / 1000.0, 5);

	return ephem;
  }

	/**
	 * Calculate the ephemeris of a satellite.
	 * <P>
	 * The ephemerisElement object is used when transforming to apparent
	 * coordinates. In any other case output position is the same
	 * (geometric = astrometric). Results are referred to mean equinox
	 * of date.
	 * <P>
	 * A pass is defined as the instant when the satellite is more then 15
	 * degrees above the horizon of the observer. A search for the next pass up
	 * to 7 days after calculation time will be done.
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object. The index of the satellite must be added to the index property.
	 * @param fullEphemeris True for full ephemeris, including next pass time and rise, set, transit.
	 * @return Satellite ephem.
	 * @throws JPARSECException If the method fails, for example because of an
	 *         invalid date.
	 */
	public static SatelliteEphemElement satEphemeris(TimeElement time, ObserverElement obs, EphemerisElement eph,
			boolean fullEphemeris) throws JPARSECException
	{
		// Check Ephemeris object
		if (!EphemerisElement.checkEphemeris(eph))
			throw new JPARSECException("invalid ephemeris object.");

		if (eph.targetBody.getIndex() < 0)
			throw new JPARSECException("invalid target body in ephemeris object.");

		// Obtain object
		SatelliteOrbitalElement sat = SatelliteEphem.getArtificialSatelliteOrbitalElement(eph.targetBody.getIndex());

		// Obtain ephemeris
		SDP8_SGP8 s = new SDP8_SGP8(sat);
		SatelliteEphemElement ephem = s.calcSatellite(time, obs, eph);

		// Obtain next pass time, when the satellite is at least 15 degrees
		// above horizon
		double min_elevation = 15.0 * Constant.DEG_TO_RAD;
		if (fullEphemeris) {
			ephem.nextPass = getNextPass(time, obs, eph, sat, min_elevation, 7, true);
			if (ephem.nextPass != 0.0)
				ephem = SDP8_SGP8.getCurrentOrNextRiseSetTransit(time, obs, eph, ephem, 34.0 * Constant.DEG_TO_RAD / 60.0);
		}

		return ephem;
	}

	/**
	 * Calculate the ephemeris of a satellite.
	 * <P>
	 * The ephemerisElement object is used when transforming to apparent
	 * coordinates. In any other case output position is the same
	 * (geometric = astrometric). Results are referred to mean equinox
	 * of date.
	 * <P>
	 * A pass is defined as the instant when the satellite is more then 15
	 * degrees above the horizon of the observer. A search for the next pass up
	 * to 7 days after calculation time will be done.
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object. The index of the satellite must be added to the index property.
	 * @param fullEphemeris True for full ephemeris, including next pass time and rise, set, transit.
	 * @param magAndSize True to calculate magnitude and size, which is a
	 * relatively slow operation.
	 * @return Satellite ephem.
	 * @throws JPARSECException If the method fails, for example because of an
	 *         invalid date.
	 */
	public static SatelliteEphemElement satEphemeris(TimeElement time, ObserverElement obs, EphemerisElement eph,
			boolean fullEphemeris, boolean magAndSize) throws JPARSECException
	{
		// Check Ephemeris object
		if (!EphemerisElement.checkEphemeris(eph))
			throw new JPARSECException("invalid ephemeris object.");

		if (eph.targetBody.getIndex() < 0)
			throw new JPARSECException("invalid target body in ephemeris object.");

		// Obtain object
		SatelliteOrbitalElement sat = SatelliteEphem.getArtificialSatelliteOrbitalElement(eph.targetBody.getIndex());

		// Obtain ephemeris
		SDP8_SGP8 s = new SDP8_SGP8(sat);
		SatelliteEphemElement ephem = s.calcSatellite(time, obs, eph, magAndSize);

		// Obtain next pass time, when the satellite is at least 15 degrees
		// above horizon
		double min_elevation = 15.0 * Constant.DEG_TO_RAD;
		if (fullEphemeris) {
			ephem.nextPass = getNextPass(time, obs, eph, sat, min_elevation, 7, true);
			if (ephem.nextPass != 0.0)
				ephem = SDP8_SGP8.getCurrentOrNextRiseSetTransit(time, obs, eph, ephem, 34.0 * Constant.DEG_TO_RAD / 60.0);
		}

		return ephem;
	}

	// Returns the approximate time in days required for a given satellite to move from
	// one side to the other side of the sky from a given observer.
	private static double getBestQuickSearch(SatelliteOrbitalElement sat, double minElev) {
		double GM = 3.98600433e14 * 1.0e-9; // Earth's Gravitational constant g' * R' * R'
		double n = sat.meanMotion / Constant.SECONDS_PER_DAY; // Mean motion rad/s
		double a = Math.pow((GM / (n * n)), 1.0 / 3.0); // Semi major axis km
		double ecc = sat.eccentricity;
		double b = a * Math.sqrt(1.0 - ecc * ecc); // Semi minor axis km

		double r = (a + b) / 2.0 - Constant.EARTH_RADIUS;
		double ang = Constant.PI_OVER_TWO - 2.0 * minElev;
		double dr = ang * r;
		double drDay = Constant.TWO_PI * (r + Constant.EARTH_RADIUS);
		double dt = dr * Constant.SECONDS_PER_DAY / drDay;
		return dt / Constant.SECONDS_PER_DAY; // days
	}

	/**
	 * Obtain the time of the next pass of the satellite above observer. It can be used
	 * as an starting point prior to obtain rise, set, transit times.
	 * <P>
	 * A pass is defined as an instant when the elevation of the satellite is
	 * greater than certain minimum value. If the observer has a perfect sight
	 * of the horizon, it is possible to set a value equal to zero, but the
	 * satellite will be probably too faint.
	 * <P>
	 * The pass is a search iteration with a precision of 1 minute of time. If
	 * the satellite appears too quickly or just below minimum elevation only
	 * for a few seconds, then the search could fail. Another possible cause
	 * of fail is for geostationary satellites.
	 * <P>
	 * The execution of this method is a low computer could last for quite a long
	 * time.
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @param sat Satellite orbital elements.
	 * @param min_elevation Minimum elevation in radians.
	 * @param maxDays Maximum number of days to search for a next pass.
	 * @param current True to return the input time if the satellite is above the minimum
	 * elevation, false to return next pass without considering the actual position of the satellite.
	 * @return Julian day of the next pass in local time, or 0.0 if the satellite
	 * has no next transit. If the day is negative that means that the satellite is
	 * eclipsed during the next pass.
	 * @throws JPARSECException If the method fails, for example because of an
	 *         invalid date.
	 */
	public static double getNextPass(TimeElement time, ObserverElement obs, EphemerisElement eph,
			SatelliteOrbitalElement sat, double min_elevation, double maxDays, boolean current) throws JPARSECException
	{
		// Check Ephemeris object
		if (!EphemerisElement.checkEphemeris(eph))
			throw new JPARSECException("invalid ephemeris object.");

		if (min_elevation < 0.0 || min_elevation >= Math.PI*0.5) throw new JPARSECException("invalid minimum elevation.");

		// Obtain ephemeris
		SDP8_SGP8 s = new SDP8_SGP8(sat);
		s.FAST_MODE = true;
		SatelliteEphemElement ephem = s.calcSatellite(time, obs, eph, false);

		// Obtain Julian day in reference scale
		SCALE refScale = SCALE.UNIVERSAL_TIME_UTC;
		double JD = TimeScale.getJD(time, obs, eph, refScale);
		double JD_LT = TimeScale.getJD(time, obs, eph, SCALE.LOCAL_TIME);

		// Set time step to 1 minute
		double time_step = 1.0 / (Constant.MINUTES_PER_HOUR * Constant.HOURS_PER_DAY);
		int nstep = 0;
		int max_step = (int) Math.floor(maxDays / time_step);
		double qs = getBestQuickSearch(sat, min_elevation) / time_step;
		int quickSearch = (int) (0.5 + qs / 2.0);
		if (quickSearch < 1) quickSearch = 1;
		if (quickSearch > 8) quickSearch = 8;

		// Obtain next pass. First we obtain the time when the satellite is
		// below the minimum elevation (necessary if it is currently above). Then, we
		// obtain the next pass
		while (ephem.elevation > min_elevation && nstep < max_step && !current)
		{
			nstep++;
			double new_JD = JD + (double) nstep * time_step;

			AstroDate astro = new AstroDate(new_JD);
			TimeElement new_time = new TimeElement(astro, refScale);

			ephem = s.calcSatellite(new_time, obs, eph, false);
		}

		if (nstep >= max_step) {
//			JPARSECException.addWarning("this satellite is permanently above the horizon and the minimum elevation.");
			return 0.0;
		}

		while (ephem.elevation < min_elevation && nstep < max_step)
		{
			if (ephem.elevation < -25.0 * Constant.DEG_TO_RAD) {
				nstep = nstep + quickSearch;
			} else {
				if (ephem.elevation < -15.0 * Constant.DEG_TO_RAD) {
					int bqs = quickSearch / 2;
					if (bqs < 1) bqs = 1;
					nstep = nstep + bqs;
				} else {
					int bqs = quickSearch / 4;
					if (bqs < 1) bqs = 1;
					nstep = nstep + bqs;
				}
			}
			double new_JD = JD + (double) nstep * time_step;

			AstroDate astro = new AstroDate(new_JD);
			TimeElement new_time = new TimeElement(astro, refScale);

			ephem = s.calcSatellite(new_time, obs, eph, false);
		}

		while (ephem.elevation > min_elevation && nstep < max_step)
		{
			nstep--;
			double new_JD = JD + (double) nstep * time_step;

			AstroDate astro = new AstroDate(new_JD);
			TimeElement new_time = new TimeElement(astro, refScale);

			ephem = s.calcSatellite(new_time, obs, eph, false);
		}

		double next_pass = TimeScale.getJD(time, obs, eph, SCALE.LOCAL_TIME) + nstep * time_step;

		if (next_pass >= JD_LT + maxDays) {
//			JPARSECException.addWarning("could not find next pass time during next "+maxDays+" days.");
			next_pass = 0.0;
		}

		if (ephem.isEclipsed) next_pass = -next_pass;

		return next_pass;
	}

	/**
	 * Obtain the time of the next flares of the satellite above observer. This
	 * method calls {@linkplain SDP8_SGP8#getNextPass(TimeElement, ObserverElement, EphemerisElement, SatelliteOrbitalElement, double, double, boolean)}
	 * and then checks for the flaring conditions. The returned array contains as the
	 * three latest objects the ephemerides for the satellite when the flare starts, ends, and
	 * reaches its maximum. In these objects the apparent magnitude expected for the flare is
	 * set to the magnitude field, and it is corrected for extinction.
	 *
	 * The field {@linkplain SatelliteEphem#MAXIMUM_IRIDIUM_ANGLE_FOR_FLARES} sets the sensitivty when
	 * searching for more or less bright flares.
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @param sat Satellite orbital elements. Should be obviosly an IRIDIUM satellite.
	 * @param min_elevation Minimum elevation of the satellite in radians.
	 * @param maxDays Maximum number of days to search for a next flare.
	 * @param current True to return the input time if the satellite is above the minimum
	 * elevation and flaring, false to return next flare without considering the actual
	 * position of the satellite.
	 * @param precision Precision in the search for events in seconds. The more the value you enter here,
	 * the faster the calculations will be, but some of the events could be skipped. A good value is
	 * 5, which means that flares that last for less than 5s could be missed. This value must be between
	 * 1 and 10. The output precision of the found flares will be always 1s.
	 * @return An array list with all the events for this satellite. The list will be null
	 * if the satellite has no next flare during the number of days given. Otherwise, it will
	 * contains arrays of double values with the Julian day of the beggining of the next flare
	 * in local time, the Julian day of the ending time of the flare, the Julian day of the
	 * maximum of the flare, and the minimum iridium angle as fourth value. The fifth, sixth,
	 * and seventh values will be respectivelly the {@linkplain SatelliteEphemElement} object
	 * for the start, end, and maximum times. No check is done
	 * for flares during day or night, although it is easy to provide a time object for sunset
	 * and a maximum number of days of 0.5 or the required value for sunrise. Precision in
	 * returned times is 1 second, and they consider the minimum elevation so that the start/end
	 * times could reflect the instants when the satellite reaches the minimum elevation (or
	 * is eclipsed) and not the start/end times of the flare.
	 * @throws JPARSECException If the method fails, for example because of an
	 *         invalid date.
	 */
	public static ArrayList<Object[]> getNextIridiumFlares(TimeElement time, ObserverElement obs, EphemerisElement eph,
			SatelliteOrbitalElement sat, double min_elevation, double maxDays, boolean current, int precision) throws JPARSECException
	{
		if (sat == null || sat.name.toLowerCase().indexOf("dummy")>=0) return null;
		if (precision < 1 || precision > 10) throw new JPARSECException("Precision parameters is "+precision+", which is outside range 1-10.");

		ArrayList<Object[]> events = null;
		SCALE refScale = SCALE.UNIVERSAL_TIME_UTC;
		double inputJD = TimeScale.getJD(time, obs, eph, refScale);
		double inputJD_LT = TimeScale.getJD(time, obs, eph, SCALE.LOCAL_TIME);
		double limitJD = inputJD + maxDays;
		double limitJD_LT = inputJD_LT + maxDays;
		double jd = inputJD, jdOut = 0.0;
		while (jd < limitJD && jd != 0.0) {
			TimeElement newTime = new TimeElement(jd, refScale);
			maxDays = limitJD - jd;
			jd = SDP8_SGP8.getNextPass(newTime, obs, eph, sat, min_elevation, maxDays, current);
			jd = Math.abs(jd); // <0 => eclipsed, but this limitation should be set at the end only if the sat is eclipsed
			if (jd > 0.0 && jd < limitJD_LT) {
	 			jd = TimeScale.getJD(new TimeElement(Math.abs(jd), SCALE.LOCAL_TIME), obs, eph, refScale);
				current = false;

				jdOut = jd;
				SDP8_SGP8 s = new SDP8_SGP8(sat);
				s.FAST_MODE = true;
				newTime = new TimeElement(jdOut, refScale);
				SatelliteEphemElement ephem = s.calcSatellite(newTime, obs, eph);
				//if (!ephem.isEclipsed) { // this limitation should be set at the end only if the sat is eclipsed
					// Check iridium angle second by second until flare ends, minimum elevation, or eclipse
					double startTime = 0.0, endTime = 0.0, maxTime = 0.0, minimumIA = 0.0;
					SatelliteEphemElement start = null, end = null, max = null;
					boolean above = false;
					if (ephem.iridiumAngle <= SatelliteEphem.MAXIMUM_IRIDIUM_ANGLE_FOR_FLARES)	{
						ephem.magnitude = (float) SatelliteEphem.getIridiumFlareMagnitude(ephem, obs);
						startTime = jdOut;
						minimumIA = ephem.iridiumAngle;
						maxTime = jdOut;
						start = ephem;
						max = ephem;
						end = ephem;
						endTime = jdOut;
					}

					boolean found = false;
					do {
						jdOut += precision / Constant.SECONDS_PER_DAY;
						newTime = new TimeElement(jdOut, refScale);
						ephem = s.calcSatellite(newTime, obs, eph);
						if (ephem.elevation > min_elevation) above = true;
						if (above && ephem.iridiumAngle <= SatelliteEphem.MAXIMUM_IRIDIUM_ANGLE_FOR_FLARES) {
							found = true;
							break;
						}
						if (ephem.elevation < min_elevation && above) break;
					} while (true);

					if (found) {
						jdOut -= precision / Constant.SECONDS_PER_DAY;
						above = false;
						do {
							jdOut += 1.0 / Constant.SECONDS_PER_DAY;
							newTime = new TimeElement(jdOut, refScale);
							ephem = s.calcSatellite(newTime, obs, eph);
							ephem.magnitude = (float) SatelliteEphem.getIridiumFlareMagnitude(ephem, obs);
							if (ephem.elevation > min_elevation) above = true;

							if (ephem.iridiumAngle <= SatelliteEphem.MAXIMUM_IRIDIUM_ANGLE_FOR_FLARES && startTime == 0.0) {
					 			if (ephem.elevation < min_elevation || ephem.isEclipsed) {
									startTime = 0.0;
									break;
								}
					 			startTime = TimeScale.getJD(newTime, obs, eph, SCALE.LOCAL_TIME);
								start = ephem;
								max = ephem;
								end = ephem;
								minimumIA = ephem.iridiumAngle;
								maxTime = startTime;
								endTime = startTime;
							}
							if ((ephem.iridiumAngle > SatelliteEphem.MAXIMUM_IRIDIUM_ANGLE_FOR_FLARES || ephem.elevation < min_elevation || ephem.isEclipsed) && startTime != 0.0) {
								endTime = TimeScale.getJD(newTime, obs, eph, SCALE.LOCAL_TIME);
								end = ephem;
								break;
							}
							if (startTime != 0.0 && ephem.iridiumAngle < minimumIA) {
								minimumIA = ephem.iridiumAngle;
								maxTime = TimeScale.getJD(newTime, obs, eph, SCALE.LOCAL_TIME);
								max = ephem;
							}
							if (ephem.elevation < min_elevation && above) {
								startTime = 0.0;
								break;
							}
						} while (true);
					}

					jd = jdOut;
					if (startTime != 0.0) {
						if (!max.isEclipsed) {
							if (events == null) events = new ArrayList<Object[]>();
							events.add(new Object[] {startTime, endTime, maxTime, minimumIA, start, end, max});
						}
					}
				//}
			}
			jd = Math.abs(jd);
			if (jd != 0.0) jd += 10.0 / 1440.0;
		}
		return events;
	}

	/**
	 * Obtain the time of the next lunar flares of the satellite above observer. This
	 * method calls {@linkplain SDP4_SGP4#getNextPass(TimeElement, ObserverElement, EphemerisElement, SatelliteOrbitalElement, double, double, boolean)}
	 * and then checks for the flaring conditions. The returned array contains as the
	 * three latest objects the ephemerides for the satellite when the flare starts, ends, and
	 * reaches its maximum. In these objects the apparent magnitude expected for the flare is
	 * set to the magnitude field, and it is corrected for extinction.
	 *
	 * The field {@linkplain SatelliteEphem#MAXIMUM_IRIDIUM_ANGLE_FOR_LUNAR_FLARES} sets the sensitivty when
	 * searching for more or less bright lunar flares.
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @param sat Satellite orbital elements. Should be obviosly an IRIDIUM satellite.
	 * @param min_elevation Minimum elevation of the satellite in radians.
	 * @param maxDays Maximum number of days to search for a next flare.
	 * @param current True to return the input time if the satellite is above the minimum
	 * elevation and flaring, false to return next flare without considering the actual
	 * position of the satellite.
	 * @param precision Precision in the search for events in seconds. The more the value you enter here,
	 * the faster the calculations will be, but some of the events could be skipped. A good value is
	 * 5, which means that flares that last for less than 5s could be missed. This value must be between
	 * 1 and 10. The output precision of the found flares will be always 1s.
	 * @return An array list with all the events for this satellite. The list will be null
	 * if the satellite has no next flare during the number of days given. Otherwise, it will
	 * contains arrays of double values with the Julian day of the beggining of the next flare
	 * in local time, the Julian day of the ending time of the flare, the Julian day of the
	 * maximum of the flare, and the minimum iridium angle as fourth value. The fifth, sixth,
	 * and seventh values will be respectivelly the {@linkplain SatelliteEphemElement} object
	 * for the start, end, and maximum times. No check is done
	 * for flares during day or night, although it is easy to provide a time object for sunset
	 * and a maximum number of days of 0.5 or the required value for sunrise. Precision in
	 * returned times is 1 second, and they consider the minimum elevation so that the start/end
	 * times could reflect the instants when the satellite reaches the minimum elevation (or
	 * is eclipsed) and not the start/end times of the flare.
	 * @throws JPARSECException If the method fails, for example because of an
	 *         invalid date.
	 */
	public static ArrayList<Object[]> getNextIridiumLunarFlares(TimeElement time, ObserverElement obs, EphemerisElement eph,
			SatelliteOrbitalElement sat, double min_elevation, double maxDays, boolean current, int precision) throws JPARSECException
	{
		if (sat == null || sat.name.toLowerCase().indexOf("dummy")>=0) return null;
		if (precision < 1 || precision > 10) throw new JPARSECException("Precision parameters is "+precision+", which is outside range 1-10.");

		ArrayList<Object[]> events = null;
		SCALE refScale = SCALE.UNIVERSAL_TIME_UTC;
		double inputJD = TimeScale.getJD(time, obs, eph, refScale);
		double inputJD_LT = TimeScale.getJD(time, obs, eph, SCALE.LOCAL_TIME);
		double limitJD = inputJD + maxDays;
		double limitJD_LT = inputJD_LT + maxDays;
		double jd = inputJD, jdOut = 0.0;
		while (jd < limitJD && jd != 0.0) {
			TimeElement newTime = new TimeElement(jd, refScale);
			maxDays = limitJD - jd;
			jd = SDP8_SGP8.getNextPass(newTime, obs, eph, sat, min_elevation, maxDays, current);
			jd = Math.abs(jd); // <0 => eclipsed, but this limitation should be set at the end only if the sat is eclipsed
			if (jd > 0.0 && jd < limitJD_LT) {
	 			jd = TimeScale.getJD(new TimeElement(Math.abs(jd), SCALE.LOCAL_TIME), obs, eph, refScale);
				current = false;

				jdOut = jd;
				SDP8_SGP8 s = new SDP8_SGP8(sat);
				s.FAST_MODE = true;
				newTime = new TimeElement(jdOut, refScale);
				SatelliteEphemElement ephem = s.calcSatellite(newTime, obs, eph);
				//if (!ephem.isEclipsed) { // this limitation should be set at the end only if the sat is eclipsed
					// Check iridium angle second by second until flare ends, minimum elevation, or eclipse
					double startTime = 0.0, endTime = 0.0, maxTime = 0.0, minimumIA = 0.0;
					SatelliteEphemElement start = null, end = null, max = null;
					boolean above = false;
					if (ephem.iridiumAngleForMoon <= SatelliteEphem.MAXIMUM_IRIDIUM_ANGLE_FOR_LUNAR_FLARES)	{
						ephem.magnitude = (float) SatelliteEphem.getIridiumLunarFlareMagnitude(newTime, obs, eph, ephem);
						startTime = jdOut;
						minimumIA = ephem.iridiumAngleForMoon;
						maxTime = jdOut;
						start = ephem;
						max = ephem;
						end = ephem;
						endTime = jdOut;
					}

					boolean found = false;
					do {
						jdOut += precision / Constant.SECONDS_PER_DAY;
						newTime = new TimeElement(jdOut, refScale);
						ephem = s.calcSatellite(newTime, obs, eph);
						if (ephem.elevation > min_elevation) above = true;
						if (above && ephem.iridiumAngleForMoon <= SatelliteEphem.MAXIMUM_IRIDIUM_ANGLE_FOR_LUNAR_FLARES) {
							found = true;
							break;
						}
						if (ephem.elevation < min_elevation && above) break;
					} while (true);

					if (found) {
						jdOut -= precision / Constant.SECONDS_PER_DAY;
						above = false;
						do {
							jdOut += 1.0 / Constant.SECONDS_PER_DAY;
							newTime = new TimeElement(jdOut, refScale);
							ephem = s.calcSatellite(newTime, obs, eph);
							ephem.magnitude = (float) SatelliteEphem.getIridiumLunarFlareMagnitude(newTime, obs, eph, ephem);
							if (ephem.elevation > min_elevation) above = true;

							if (ephem.iridiumAngleForMoon <= SatelliteEphem.MAXIMUM_IRIDIUM_ANGLE_FOR_LUNAR_FLARES && startTime == 0.0) {
					 			if (ephem.elevation < min_elevation || ephem.isEclipsed) {
									startTime = 0.0;
									break;
								}
					 			startTime = TimeScale.getJD(newTime, obs, eph, SCALE.LOCAL_TIME);
								start = ephem;
								max = ephem;
								end = ephem;
								minimumIA = ephem.iridiumAngleForMoon;
								maxTime = startTime;
								endTime = startTime;
							}
							if ((ephem.iridiumAngleForMoon > SatelliteEphem.MAXIMUM_IRIDIUM_ANGLE_FOR_LUNAR_FLARES || ephem.elevation < min_elevation || ephem.isEclipsed) && startTime != 0.0) {
								endTime = TimeScale.getJD(newTime, obs, eph, SCALE.LOCAL_TIME);
								end = ephem;
								break;
							}
							if (startTime != 0.0 && ephem.iridiumAngleForMoon < minimumIA) {
								minimumIA = ephem.iridiumAngleForMoon;
								maxTime = TimeScale.getJD(newTime, obs, eph, SCALE.LOCAL_TIME);
								max = ephem;
							}
							if (ephem.elevation < min_elevation && above) {
								startTime = 0.0;
								break;
							}
						} while (true);
					}

					jd = jdOut;
					if (startTime != 0.0) {
						if (!max.isEclipsed) {
							if (events == null) events = new ArrayList<Object[]>();
							events.add(new Object[] {startTime, endTime, maxTime, minimumIA, start, end, max});
						}
					}
				//}
			}
			jd = Math.abs(jd);
			if (jd != 0.0) jd += 10.0 / 1440.0;
		}
		return events;
	}

	/**
	 * Calculates current or next rise, set, transit for a satellite. It is recommended that the
	 * input ephem objects corresponds to a time when the satellite is above the horizon,
	 * obtained for the next pass, but it is not required.
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object. The index of the satellite must be added to the index property.
	 * @param satEphem Satellite ephem object where the data will be inserted.
	 * @param horizon Refraction at horizon. Standard value is 34 arcminutes.
	 * @return The ephem object with the data included. If the rise, set, transit cannot be
	 * obtained the output will be set to zero, thats means that the results are unknown.
	 * @throws JPARSECException If an error occurs.
	 */
	public static SatelliteEphemElement getCurrentOrNextRiseSetTransit(TimeElement time, ObserverElement obs, EphemerisElement eph,
			SatelliteEphemElement satEphem, double horizon)
	throws JPARSECException {
		SatelliteEphemElement sat = satEphem.clone();
		TimeElement timeEphem = time.clone();
		int index = 0;
		if (sat.rise == null) {
			sat.rise = new double[] {0.0};
			sat.set = new double[] {0.0};
			sat.transit = new double[] {0.0};
			sat.transitElevation = new float[] {0.0f};
		} else {
			index = sat.rise.length;
			sat.rise = DataSet.addDoubleArray(sat.rise, new double[] {0.0});
			sat.set = DataSet.addDoubleArray(sat.set, new double[] {0.0});
			sat.transit = DataSet.addDoubleArray(sat.transit, new double[] {0.0});
			sat.transitElevation = DataSet.addFloatArray(sat.transitElevation, new float[] {0.0f});
		}
		SatelliteEphemElement satOut = sat.clone();

		// Obtain next pass time, when the satellite is at least 15 degrees
		// above horizon
		if (sat.nextPass == 0.0) {
			// Check Ephemeris object
			if (!EphemerisElement.checkEphemeris(eph))
				throw new JPARSECException("invalid ephemeris object.");

			if (eph.targetBody.getIndex() < 0)
				throw new JPARSECException("invalid target body in ephemeris object.");

			// Obtain object
			SatelliteOrbitalElement satOrb = SatelliteEphem.getArtificialSatelliteOrbitalElement(eph.targetBody.getIndex());
			double min_elevation = 15.0 * Constant.DEG_TO_RAD;
			sat.nextPass = getNextPass(timeEphem, obs, eph, satOrb, min_elevation, 7, true);
		}

		if (sat.elevation < 0.0) {
			if (sat.nextPass == 0.0) {
				//throw new JPARSECException("satellite below horizon and no next pass could be obtained.");
				return sat;
			}
			timeEphem = new TimeElement(new AstroDate(Math.abs(sat.nextPass)), SCALE.LOCAL_TIME);
			sat = SDP8_SGP8.satEphemeris(timeEphem, obs, eph, false, false);
		}
		double jdref = TimeScale.getJD(timeEphem, obs, eph, SCALE.LOCAL_TIME);
		double jdMax = jdref, maxElev = sat.elevation;
		double precission = 1.0;
		double jd = jdref;
		int maxIter = 5000;
		int iter = 0;
		do {
			iter++;
			jd = jd - precission / Constant.SECONDS_PER_DAY;
			timeEphem = new TimeElement(new AstroDate(jd), SCALE.LOCAL_TIME);
			sat = SDP8_SGP8.satEphemeris(timeEphem, obs, eph, false, false);
			if (sat.elevation > maxElev) {
				maxElev = sat.elevation;
				jdMax = jd;
			}
		} while (sat.elevation > -horizon && iter < maxIter);
		double rise = jd;
		if (iter == maxIter) rise = 0.0;
		iter = 0;

		jd = jdref;
		do {
			iter ++;
			jd = jd + precission / Constant.SECONDS_PER_DAY;
			timeEphem = new TimeElement(new AstroDate(jd), SCALE.LOCAL_TIME);
			sat = SDP8_SGP8.satEphemeris(timeEphem, obs, eph, false, false);
			if (sat.elevation > maxElev) {
				maxElev = sat.elevation;
				jdMax = jd;
			}
		} while (sat.elevation > -horizon && iter < maxIter);
		double set = jd;
		double tra = jdMax;
		float traE = (float) maxElev;
		if (iter == maxIter) {
			set = 0.0;
			tra = 0.0;
			traE = 0.0f;
		}
		satOut.rise[index] = rise;
		satOut.set[index] = set;
		satOut.transit[index] = tra;
		satOut.transitElevation[index] = traE;
		return satOut;
	}

  /**
   * A helper routine to calculate the two-dimensional inverse tangens. */
  final static double ACTAN(double SINX, double COSX)
  {
    double value, TEMP;

    if (COSX == 0.) {
      if (SINX == 0.) {
        value = 0.;
      }
      else if (SINX > 0.) {
	value = C2.PIO2;
      }
      else {
	value = C2.X3PIO2;
      }
    }
    else if (COSX > 0.) {
      if (SINX == 0.) {
	value = 0.;
      }
      else if (SINX > 0.) {
	TEMP = SINX / COSX;
        value = Math.atan(TEMP);
      }
      else {
	value = C2.TWOPI;
	TEMP = SINX / COSX;
	value = value + Math.atan(TEMP);
      }
    }
    else {
      value = C2.PI;
      TEMP = SINX / COSX;
      value = value + Math.atan(TEMP);
    }

    return value;
  }



  /**
   * Run the SDP8 model.
   *
   * <p>This should be run for long-period satellites.  The criterion is
   * evaluated on reading the orbital data from the TLE and stored in the
   * state variable itsIsDeep (should be 1 for calling this routine).
   *
   * @param IFLAG
   *   IFLAG[0] must be given as 1 for the first call for any given satellite.
   *   It is then returned as 0 and can be given as 0 for further calls.
   * @param TSINCE
   *   TSINCE is the time difference between the time of interest and the
   *   epoch of the TLE.  It must be given in minutes.
 * @throws JPARSECException */
  private final void RunSDP8(int[] IFLAG, double TSINCE) throws JPARSECException
  {
	    // UPDATE FOR SECULAR GRAVITY AND ATMOSPHERIC DRAG

      Z1=.5*XNDT*TSINCE*TSINCE;
      Z7=3.5*C1.TOTHRD*Z1/XNODP;
      XMAMDF.value=sat.meanAnomaly+XLLDOT*TSINCE;
      OMGASM.value=sat.argumentOfPerigee+OMGDT*TSINCE+Z7*XGDT1;
      XNODES.value=sat.ascendingNodeRA+XNODOT*TSINCE+Z7*XHDT1;
      XN.value=XNODP;
      DEEP.DPSEC(sat,XMAMDF,OMGASM,XNODES,EM,XINC,XN,TSINCE);
      XN.value=XN.value+XNDT*TSINCE;
      EM.value=EM.value+EDOT*TSINCE;
      XMAM.value=XMAMDF.value+Z1+Z7*XMDT1;
      DEEP.DPPER(EM,XINC,OMGASM,XNODES,XMAM);
      //XMAM.value=Functions.normalizeRadians(XMAM.value);

      // SOLVE KEPLERS EQUATION

      ZC2=XMAM.value+EM.value*Math.sin(XMAM.value)*(1.+EM.value*Math.cos(XMAM.value));
      for ( int I=1 ; I <= 10 ; I++ )
      {
          SINE=Math.sin(ZC2);
          COSE=Math.cos(ZC2);
          ZC5=1./(1.-EM.value*COSE);
          CAPE=(XMAM.value+EM.value*SINE-ZC2)*
             ZC5+ZC2;
          if (Math.abs(CAPE-ZC2) <= C1.E6A) break;
          ZC2=CAPE;
      }

      // SHORT PERIOD PRELIMINARY QUANTITIES

      AM=Math.pow((C1.XKE/XN.value),C1.TOTHRD);
      BETA2M=1.-EM.value*EM.value;
      SINOS=Math.sin(OMGASM.value);
      COSOS=Math.cos(OMGASM.value);
      AXNM=EM.value*COSOS;
      AYNM=EM.value*SINOS;
      PM=AM*BETA2M;
      G1=1./PM;
      G2=.5*C1.CK2*G1;
      G3=G2*G1;
      BETA=Math.sqrt(BETA2M);
      G4=.25*A3COF*SINI;
      G5=.25*A3COF*G1;
      SNF=BETA*SINE*ZC5;
      CSF=(COSE-EM.value)*ZC5;
      FM=SDP8_SGP8.ACTAN(SNF,CSF);
      SNFG=SNF*COSOS+CSF*SINOS;
      CSFG=CSF*COSOS-SNF*SINOS;
      SN2F2G=2.*SNFG*CSFG;
      CS2F2G=2.*Math.pow(CSFG,2)-1.;
      ECOSF=EM.value*CSF;
      G10=FM-XMAM.value+EM.value*SNF;
      RM=PM/(1.+ECOSF);
      AOVR=AM/RM;
      G13=XN.value*AOVR;
      G14=-G13*AOVR;
      DR=G2*(UNMTH2*CS2F2G-3.*TTHMUN)-G4*SNFG;
      DIWC=3.*G3*SINI*CS2F2G-G5*AYNM;
      DI=DIWC*COSI;
      SINI2=Math.sin(.5*XINC.value);

      // UPDATE FOR SHORT PERIOD PERIODICS

      SNI2DU=SINIO2*(
         G3*(.5*(1.-7.*THETA2)*SN2F2G-3.*UNM5TH*G10)-G5*SINI*CSFG*(2.+
               ECOSF))-.5*G5*THETA2*AXNM/COSIO2;
      XLAMB=FM+OMGASM.value+XNODES.value+G3*(.5*(1.+6.*COSI-7.*THETA2)*SN2F2G-3.*
            (UNM5TH+2.*COSI)*G10)+G5*SINI*(COSI*AXNM/(1.+COSI)-(2.
            +ECOSF)*CSFG);
      Y4=SINI2*SNFG+CSFG*SNI2DU+.5*SNFG*COSIO2*DI;
      Y5=SINI2*CSFG-SNFG*SNI2DU+.5*CSFG*COSIO2*DI;
      R=RM+DR;
      RDOT=XN.value*AM*EM.value*SNF/BETA+G14*(2.*G2*UNMTH2*SN2F2G+G4*CSFG);
      RVDOT=XN.value*Math.pow(AM,2)*BETA/RM+
            G14*DR+AM*G13*SINI*DIWC;

      // ORIENTATION VECTORS

      SNLAMB=Math.sin(XLAMB);
      CSLAMB=Math.cos(XLAMB);
      TEMP=2.*(Y5*SNLAMB-Y4*CSLAMB);
      UX=Y4*TEMP+CSLAMB;
      VX=Y5*TEMP-SNLAMB;
      TEMP=2.*(Y5*CSLAMB+Y4*SNLAMB);
      UY=-Y4*TEMP+SNLAMB;
      VY=-Y5*TEMP+CSLAMB;
      TEMP=2.*Math.sqrt(1.-Y4*Y4-Y5*Y5);
      UZ=Y4*TEMP;
      VZ=Y5*TEMP;

    /* POSITION AND VELOCITY */

      double E1_X = R * UX;
      double E1_Y = R * UY;
      double E1_Z = R * UZ;
      double E1_XDOT = RDOT * UX + RVDOT * VX;
      double E1_YDOT = RDOT * UY + RVDOT * VY;
      double E1_ZDOT = RDOT * UZ + RVDOT * VZ;

    itsR[0] = E1_X    * C1.XKMPER / C1.AE / 1E6;
    itsR[1] = E1_Y    * C1.XKMPER / C1.AE / 1E6;
    itsR[2] = E1_Z    * C1.XKMPER / C1.AE / 1E6;
    itsV[0] = E1_XDOT * C1.XKMPER / C1.AE * C1.XMNPDA / 86400.;
    itsV[1] = E1_YDOT * C1.XKMPER / C1.AE * C1.XMNPDA / 86400.;
    itsV[2] = E1_ZDOT * C1.XKMPER / C1.AE * C1.XMNPDA / 86400.;
  }


  /**
   * Run the SGP8 model.
   *
   * <p>This should be run for short-period satellites.  The criterion is
   * evaluated on reading the orbital data from the TLE and stored in the
   * state variable itsIsDeep (should be 0 for calling this routine).
   *
   * @param IFLAG
   *   IFLAG[0] must be given as 1 for the first call for any given satellite.
   *   It is then returned as 0 and can be given as 0 for further calls.
   * @param TSINCE
   *   TSINCE[0] is the time difference between the time of interest and the
   *   epoch of the TLE.  It must be given in minutes. */

  private final void RunSGP8(int[] IFLAG, double TSINCE)
  {
      // UPDATE FOR SECULAR GRAVITY AND ATMOSPHERIC DRAG
      XMAM.value = sat.meanAnomaly+XLLDOT*TSINCE;
      OMGASM.value = sat.argumentOfPerigee+OMGDT*TSINCE;
      XNODES.value = sat.ascendingNodeRA+XNODOT*TSINCE;
      if (ISIMP != 1)
      {
          TEMP=1.-GAMMA*TSINCE;
          TEMP1=Math.pow(TEMP,PP);
          XN.value = XNODP+XND*(1.-TEMP1);
          EM.value = sat.eccentricity+ED*(1.-Math.pow(TEMP,QQ));
          Z1=XND*(TSINCE+OVGPP*(TEMP*TEMP1-1.));
      }
      else
      {
          XN.value = XNODP+XNDT*TSINCE;
          EM.value = sat.eccentricity+EDOT*TSINCE;
          Z1=.5*XNDT*TSINCE*TSINCE;
      }
      Z7=3.5*C1.TOTHRD*Z1/XNODP;
      XMAM.value=Functions.normalizeRadians(XMAM.value+Z1+Z7*XMDT1);
      OMGASM.value=OMGASM.value+Z7*XGDT1;
      XNODES.value=XNODES.value+Z7*XHDT1;

      // SOLVE KEPLERS EQUATION


      ZC2=XMAM.value+EM.value*Math.sin(XMAM.value)*(1.+EM.value*Math.cos(XMAM.value));
      for ( int I = 1 ; I <= 10 ; I++ )
      {
          SINE=Math.sin(ZC2);
          COSE=Math.cos(ZC2);
          ZC5=1./(1.-EM.value*COSE);
          CAPE=(XMAM.value+EM.value*SINE-ZC2)*
             ZC5+ZC2;
          if (Math.abs(CAPE-ZC2) <= C1.E6A) break;
          ZC2=CAPE;
      }

      // SHORT PERIOD PRELIMINARY QUANTITIES

      AM=Math.pow((C1.XKE/XN.value),C1.TOTHRD);
      BETA2M=1.-EM.value*EM.value;
      SINOS=Math.sin(OMGASM.value);
      COSOS=Math.cos(OMGASM.value);
      AXNM=EM.value*COSOS;
      AYNM=EM.value*SINOS;
      PM=AM*BETA2M;
      G1=1./PM;
      G2=.5*C1.CK2*G1;
      G3=G2*G1;
      BETA=Math.sqrt(BETA2M);
      G4=.25*A3COF*SINI;
      G5=.25*A3COF*G1;
      SNF=BETA*SINE*ZC5;
      CSF=(COSE-EM.value)*ZC5;
      FM=SDP8_SGP8.ACTAN(SNF,CSF);
      SNFG=SNF*COSOS+CSF*SINOS;
      CSFG=CSF*COSOS-SNF*SINOS;
      SN2F2G=2.*SNFG*CSFG;
      CS2F2G=2.*Math.pow(CSFG,2)-1.;
      ECOSF=EM.value*CSF;
      G10=FM-XMAM.value+EM.value*SNF;
      RM=PM/(1.+ECOSF);
      AOVR=AM/RM;
      G13=XN.value*AOVR;
      G14=-G13*AOVR;
      DR=G2*(UNMTH2*CS2F2G-3.*TTHMUN)-G4*SNFG;
      DIWC=3.*G3*SINI*CS2F2G-G5*AYNM;
      DI=DIWC*COSI;

      // UPDATE FOR SHORT PERIOD PERIODICS

      SNI2DU=SINIO2*(
         G3*(.5*(1.-7.*THETA2)*SN2F2G-3.*UNM5TH*G10)-G5*SINI*CSFG*(2.+
               ECOSF))-.5*G5*THETA2*AXNM/COSIO2;
      XLAMB=FM+OMGASM.value+XNODES.value+G3*(.5*(1.+6.*COSI-7.*THETA2)*SN2F2G-3.*
            (UNM5TH+2.*COSI)*G10)+G5*SINI*(COSI*AXNM/(1.+COSI)-(2.
            +ECOSF)*CSFG);
      Y4=SINIO2*SNFG+CSFG*SNI2DU+.5*SNFG*COSIO2*DI;
      Y5=SINIO2*CSFG-SNFG*SNI2DU+.5*CSFG*COSIO2*DI;
      R=RM+DR;
      RDOT=XN.value*AM*EM.value*SNF/BETA+G14*(2.*G2*UNMTH2*SN2F2G+G4*CSFG);
      RVDOT=XN.value*Math.pow(AM,2)*BETA/RM+
            G14*DR+AM*G13*SINI*DIWC;

      // ORIENTATION VECTORS

      SNLAMB=Math.sin(XLAMB);
      CSLAMB=Math.cos(XLAMB);
      TEMP=2.*(Y5*SNLAMB-Y4*CSLAMB);
      UX=Y4*TEMP+CSLAMB;
      VX=Y5*TEMP-SNLAMB;
      TEMP=2.*(Y5*CSLAMB+Y4*SNLAMB);
      UY=-Y4*TEMP+SNLAMB;
      VY=-Y5*TEMP+CSLAMB;
      TEMP=2.*Math.sqrt(1.-Y4*Y4-Y5*Y5);
      UZ=Y4*TEMP;
      VZ=Y5*TEMP;

    /* POSITION AND VELOCITY */

    double E1_X = R * UX;
    double E1_Y = R * UY;
    double E1_Z = R * UZ;
    double E1_XDOT = RDOT * UX + RVDOT * VX;
    double E1_YDOT = RDOT * UY + RVDOT * VY;
    double E1_ZDOT = RDOT * UZ + RVDOT * VZ;

    itsR[0] = E1_X    * C1.XKMPER / C1.AE / 1E6;
    itsR[1] = E1_Y    * C1.XKMPER / C1.AE / 1E6;
    itsR[2] = E1_Z    * C1.XKMPER / C1.AE / 1E6;
    itsV[0] = E1_XDOT * C1.XKMPER / C1.AE * C1.XMNPDA / 86400.;
    itsV[1] = E1_YDOT * C1.XKMPER / C1.AE * C1.XMNPDA / 86400.;
    itsV[2] = E1_ZDOT * C1.XKMPER / C1.AE * C1.XMNPDA / 86400.;
  }

  /**
   * Reads the orbital elements into internal variables.
   * @param sat The orbital elements for the satellite.
   * @throws JPARSECException If an error occurs.
   */

  private final void ReadNorad12(SatelliteOrbitalElement sat) throws JPARSECException
  {
    double t, A1, DEL1, AO, DELO, XNODP;

    AstroDate astro = new AstroDate(sat.year, AstroDate.JANUARY, sat.day);
    E1_EPOCH = astro.jd();
    double E1_XNDT2O = sat.firstDerivative / Constant.TWO_PI;
    double E1_XNDD6O = sat.secondDerivative;

    double E1_XINCL = sat.inclination;
    double E1_EO = sat.eccentricity;
    double E1_XNO = sat.meanMotion;

    E1_XNO    = E1_XNO  / C1.XMNPDA;
    E1_XNDT2O = E1_XNDT2O * C2.TWOPI / C1.XMNPDA / C1.XMNPDA;
    E1_XNDD6O = E1_XNDD6O * C2.TWOPI / C1.XMNPDA / C1.XMNPDA / C1.XMNPDA;

    /* Figure out which side of 225 minutes the period is. */

    A1 = Math.pow(C1.XKE / E1_XNO, C1.TOTHRD);
    t = 1.5 * C1.CK2 * (3. * Math.cos(E1_XINCL) * Math.cos(E1_XINCL) - 1.)
      / Math.pow(1.-E1_EO*E1_EO, 1.5);
    DEL1 = t / (A1 * A1);
    AO = A1 * (1. - DEL1 * (.5 * C1.TOTHRD + DEL1 * (1. + 134. / 81. * DEL1)));
    DELO = t / (AO * AO);
    XNODP = E1_XNO / (1. + DELO);
    if (C2.TWOPI / XNODP / C1.XMNPDA >= .15625) {isDeep = true;} else {isDeep = false;}

	itsEpochJD = astro.jd();
  }
}

