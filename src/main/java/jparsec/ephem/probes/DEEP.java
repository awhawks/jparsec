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

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.SiderealTime;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;

/**
 * <p>The <code>SDP8_SGP8</code> class is a base class to calculate ephemeris for
 * an artificial Earth satellite, using the SGP8 and SDP8 models.</p>
 * <p>Based on JSGP by Matthew Funk.</p>
 *
 * @author T. Alonso Albi - OAN (Spain)
 */
class DEEP {
    static double DAY, PREEP, XNODCE, ATIME, DELT, SAVTSN, STEP2, STEPN, STEPP;
    static double ZNS;
    static double C1SS;
    static double ZES;
    static double ZNL;
    static double C1L;
    static double ZEL;
    static double ZCOSIS;
    static double ZSINIS;
    static double ZSINGS;
    static double ZCOSGS;
    static double ZCOSHS;
    static double ZSINHS;
    static double Q22;
    static double Q31;
    static double Q33;
    static double G22;
    static double G32;
    static double G44;
    static double G52;
    static double G54;
    static double ROOT22;
    static double ROOT32;
    static double ROOT44;
    static double ROOT52;
    static double ROOT54;
    static double THDT;
    static double THGR;
    static double EQ;
    static double XNQ;
    static double AQNV;
    static double XQNCL;
    static double XMAO;
    static double XPIDOT;
    static double SINQ;
    static double COSQ;
    static double OMEGAQ;
    static double STEM;
    static double CTEM;
    static double ZCOSIL;
    static double ZSINIL;
    static double ZSINHL;
    static double ZCOSHL;
    static double C;
    static double GAM;
    static double ZMOL;
    static double ZX;
    static double ZY;
    static double ZCOSGL;
    static double ZSINGL;
    static double ZMOS;
    static int LS;
    static double ZCOSG;
    static double ZSING;
    static double ZCOSI;
    static double ZSINI;
    static double ZCOSH;
    static double ZSINH;
    static double CC;
    static double ZN;
    static double ZE;
    static double ZMO;
    static double XNOI;
    static double A1;
    static double A3;
    static double A7;
    static double A8;
    static double A9;
    static double A10;
    static double A2;
    static double A4;
    static double A5;
    static double A6;
    static double X1;
    static double X2;
    static double X3;
    static double X4;
    static double X5;
    static double X6;
    static double X7;
    static double X8;
    static double Z31;
    static double Z32;
    static double Z33;
    static double Z1;
    static double Z2;
    static double Z3;
    static double Z11;
    static double Z12;
    static double Z13;
    static double Z21;
    static double Z22;
    static double Z23;
    static double S1;
    static double S2;
    static double S3;
    static double S4;
    static double S5;
    static double S6;
    static double S7;
    static double SE;
    static double SI;
    static double SL;
    static double SGH;
    static double SH;
    static double EE2;
    static double E3;
    static double XI2;
    static double XI3;
    static double XL2;
    static double XL3;
    static double XL4;
    static double XGH2;
    static double XGH3;
    static double XGH4;
    static double XH2;
    static double XH3;
    static double SSE;
    static double SSI;
    static double SSL;
    static double SSH;
    static double SSG;
    static double SE2;
    static double SI2;
    static double SL2;
    static double SGH2;
    static double SH2;
    static double SE3;
    static double SI3;
    static double SL3;
    static double SGH3;
    static double SH3;
    static double SL4;
    static double SGH4;
    static int IRESFL;
    static int ISYNFL;
    static double EOC;
    static double G201;
    static double G211;
    static double G310;
    static double G322;
    static double G410;
    static double G422;
    static double G520;
    static double G533;
    static double G521;
    static double G532;
    static double SINI2;
    static double F220;
    static double F221;
    static double F321;
    static double F322;
    static double F441;
    static double F442;
    static double F522;
    static double F523;
    static double F542;
    static double F543;
    static double XNO2;
    static double AINV2;
    static double TEMP1;
    static double TEMP;
    static double D2201;
    static double D2211;
    static double D3210;
    static double D3222;
    static double D4410;
    static double D4422;
    static double D5220;
    static double D5232;
    static double D5421;
    static double D5433;
    static double XLAMO;
    static double BFACT;
    static double G200;
    static double G300;
    static double F311;
    static double F330;
    static double DEL1;
    static double DEL2;
    static double DEL3;
    static double FASX2;
    static double FASX4;
    static double FASX6;
    static double XFACT;
    static double XLI;
    static double XNI;
    static int IRET;
    static int IRETN;
    static double FT;
    static double XNDOT;
    static double XNDDT;
    static double OMGDT;
    static double XOMI;
    static double X2OMI;
    static double X2LI;
    static double XLDOT;
    static double XL;
    static double T;
    static double ZM;
    static double ZF;
    static double SINZF;
    static double F2;
    static double F3;
    static double SES;
    static double SIS;
    static double SLS;
    static double SGHS;
    static double SHS;
    static double SEL;
    static double SIL;
    static double SLL;
    static double SGHL;
    static double SHL;
    static double PE;
    static double PINC;
    static double PL;
    static double PGH;
    static double PH;
    static double SINIQ;
    static double COSIQ;
    static double SINOK;
    static double COSOK;
    static double ALFDP;
    static double BETDP;
    static double DALF;
    static double DBET;
    static double XLS;
    static double DLS;
    static double SINIS;
    static double COSIS;

    static {
        ZNS = 1.19459E-5f;
        C1SS = 2.9864797E-6f;
        ZES = .01675f;
        ZNL = 1.5835218E-4f;
        C1L = 4.7968065E-7f;
        ZEL = .05490f;
        ZCOSIS = .91744867f;
        ZSINIS = .39785416f;
        ZSINGS = -.98088458f;
        ZCOSGS = .1945905f;
        ZCOSHS = 1.0f;
        ZSINHS = 0.0f;
        Q22 = 1.7891679E-6f;
        Q31 = 2.1460748E-6f;
        Q33 = 2.2123015E-7f;
        G22 = 5.7686396f;
        G32 = 0.95240898f;
        G44 = 1.8014998f;
        G52 = 1.0508330f;
        G54 = 4.4108898f;
        ROOT22 = 1.7891679E-6f;
        ROOT32 = 3.7393792E-7f;
        ROOT44 = 7.3636953E-9f;
        ROOT52 = 1.1428639E-7f;
        ROOT54 = 2.1765803E-9f;
        THDT = 4.3752691E-3f;
    }

    static void DPINIT(SatelliteOrbitalElement sat, double EQSQ, double SINIQ, double COSIQ, double RTEQSQ, double AO,
                       double COSQ2, double SINOMO, double COSOMO, double BSQ, double XLLDOT, double OMGDT, double XNODOT,
                       double XNODP, double E1_EPOCH
    ) throws JPARSECException {
        DEEP.OMGDT = OMGDT;
        DEEP.SINIQ = SINIQ;
        DEEP.COSIQ = COSIQ;

        double E1_DS50 = 0;
        try {
            AstroDate astro = new AstroDate(E1_EPOCH);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            EphemerisElement eph = new EphemerisElement(
                Target.TARGET.NOT_A_PLANET,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.GEOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.IAU_2006,
                EphemerisElement.FRAME.ICRF,
                EphemerisElement.ALGORITHM.JPL_DE405);
            eph.correctForEOP = false;
            E1_DS50 = astro.jd() - new AstroDate(1950, 1, 0).jd();
            THGR = SiderealTime.greenwichMeanSiderealTime(time, observer, eph);
        } catch (Exception exc) {
            throw new JPARSECException("Invalid epoch");
        }

        EQ = sat.eccentricity;
        XNQ = XNODP;
        AQNV = 1.f / AO;
        XQNCL = sat.inclination;
        XMAO = sat.meanAnomaly;
        XPIDOT = OMGDT + XNODOT;
        SINQ = Math.sin(sat.ascendingNodeRA);
        COSQ = Math.cos(sat.ascendingNodeRA);
        OMEGAQ = sat.argumentOfPerigee;

        // INITIALIZE LUNAR SOLAR TERMS

        DAY = E1_DS50 + 18261.5;
        if (DAY != PREEP) {
            PREEP = DAY;
            XNODCE = 4.5236020 - 9.2422029E-4 * DAY;
            STEM = Math.sin(XNODCE);
            CTEM = Math.cos(XNODCE);
            ZCOSIL = .91375164f - .03568096f * CTEM;
            ZSINIL = Math.sqrt(1. - ZCOSIL * ZCOSIL);
            ZSINHL = .089683511f * STEM / ZSINIL;
            ZCOSHL = Math.sqrt(1. - ZSINHL * ZSINHL);
            C = (4.7199672 + .22997150 * DAY);
            GAM = (5.8351514 + .0019443680 * DAY);
            ZMOL = Functions.normalizeRadians(C - GAM);
            ZX = .39785416f * STEM / ZSINIL;
            ZY = ZCOSHL * CTEM + 0.91744867f * ZSINHL * STEM;
            ZX = SDP8_SGP8.ACTAN(ZX, ZY);
            ZX = (GAM + ZX - XNODCE);
            ZCOSGL = Math.cos(ZX);
            ZSINGL = Math.sin(ZX);
            ZMOS = (6.2565837 + .017201977 * DAY);
            ZMOS = Functions.normalizeRadians(ZMOS);
        }

        // DO SOLAR TERMS
        LS = 0;
        SAVTSN = 1.e20;
        ZCOSG = ZCOSGS;
        ZSING = ZSINGS;
        ZCOSI = ZCOSIS;
        ZSINI = ZSINIS;
        ZCOSH = COSQ;
        ZSINH = SINQ;
        CC = C1SS;
        ZN = ZNS;
        ZE = ZES;
        ZMO = ZMOS;
        XNOI = 1.f / XNQ;
        LS = 30;

        while (true) {
            A1 = ZCOSG * ZCOSH + ZSING * ZCOSI * ZSINH;
            A3 = -ZSING * ZCOSH + ZCOSG * ZCOSI * ZSINH;
            A7 = -ZCOSG * ZSINH + ZSING * ZCOSI * ZCOSH;
            A8 = ZSING * ZSINI;
            A9 = ZSING * ZSINH + ZCOSG * ZCOSI * ZCOSH;
            A10 = ZCOSG * ZSINI;
            A2 = COSIQ * A7 + SINIQ * A8;
            A4 = COSIQ * A9 + SINIQ * A10;
            A5 = -SINIQ * A7 + COSIQ * A8;
            A6 = -SINIQ * A9 + COSIQ * A10;

            X1 = A1 * COSOMO + A2 * SINOMO;
            X2 = A3 * COSOMO + A4 * SINOMO;
            X3 = -A1 * SINOMO + A2 * COSOMO;
            X4 = -A3 * SINOMO + A4 * COSOMO;
            X5 = A5 * SINOMO;
            X6 = A6 * SINOMO;
            X7 = A5 * COSOMO;
            X8 = A6 * COSOMO;

            Z31 = 12.f * X1 * X1 - 3.f * X3 * X3;
            Z32 = 24.f * X1 * X2 - 6.f * X3 * X4;
            Z33 = 12.f * X2 * X2 - 3.f * X4 * X4;
            Z1 = 3.f * (A1 * A1 + A2 * A2) + Z31 * EQSQ;
            Z2 = 6.f * (A1 * A3 + A2 * A4) + Z32 * EQSQ;
            Z3 = 3.f * (A3 * A3 + A4 * A4) + Z33 * EQSQ;
            Z11 = -6.f * A1 * A5 + EQSQ * (-24.f * X1 * X7 - 6.f * X3 * X5);
            Z12 = -6.f * (A1 * A6 + A3 * A5) + EQSQ * (-24.f * (X2 * X7 + X1 * X8) - 6.f * (X3 * X6 + X4 * X5));
            Z13 = -6.f * A3 * A6 + EQSQ * (-24.f * X2 * X8 - 6.f * X4 * X6);
            Z21 = 6.f * A2 * A5 + EQSQ * (24.f * X1 * X5 - 6.f * X3 * X7);
            Z22 = 6.f * (A4 * A5 + A2 * A6) + EQSQ * (24.f * (X2 * X5 + X1 * X6) - 6.f * (X4 * X7 + X3 * X8));
            Z23 = 6.f * A4 * A6 + EQSQ * (24.f * X2 * X6 - 6.f * X4 * X8);
            Z1 = Z1 + Z1 + BSQ * Z31;
            Z2 = Z2 + Z2 + BSQ * Z32;
            Z3 = Z3 + Z3 + BSQ * Z33;
            S3 = CC * XNOI;
            S2 = -.5f * S3 / RTEQSQ;
            S4 = S3 * RTEQSQ;
            S1 = -15.f * EQ * S4;
            S5 = X1 * X3 + X2 * X4;
            S6 = X2 * X3 + X1 * X4;
            S7 = X2 * X4 - X1 * X3;
            SE = S1 * ZN * S5;
            SI = S2 * ZN * (Z11 + Z13);
            SL = -ZN * S3 * (Z1 + Z3 - 14.f - 6.f * EQSQ);
            SGH = S4 * ZN * (Z31 + Z33 - 6.f);
            SH = -ZN * S2 * (Z21 + Z23);
            if (XQNCL < 5.2359877E-2) SH = 0.0f;
            EE2 = 2.f * S1 * S6;
            E3 = 2.f * S1 * S7;
            XI2 = 2.f * S2 * Z12;
            XI3 = 2.f * S2 * (Z13 - Z11);
            XL2 = -2.f * S3 * Z2;
            XL3 = -2.f * S3 * (Z3 - Z1);
            XL4 = -2.f * S3 * (-21.f - 9.f * EQSQ) * ZE;
            XGH2 = 2.f * S4 * Z32;
            XGH3 = 2.f * S4 * (Z33 - Z31);
            XGH4 = -18.f * S4 * ZE;
            XH2 = -2.f * S2 * Z22;
            XH3 = -2.f * S2 * (Z23 - Z21);

            if (LS == 40) break;

            // DO LUNAR TERMS
            SSE = SE;
            SSI = SI;
            SSL = SL;
            SSH = SH / SINIQ;
            SSG = SGH - COSIQ * SSH;
            SE2 = EE2;
            SI2 = XI2;
            SL2 = XL2;
            SGH2 = XGH2;
            SH2 = XH2;
            SE3 = E3;
            SI3 = XI3;
            SL3 = XL3;
            SGH3 = XGH3;
            SH3 = XH3;
            SL4 = XL4;
            SGH4 = XGH4;
            LS = 1;
            ZCOSG = ZCOSGL;
            ZSING = ZSINGL;
            ZCOSI = ZCOSIL;
            ZSINI = ZSINIL;
            ZCOSH = ZCOSHL * COSQ + ZSINHL * SINQ;
            ZSINH = SINQ * ZCOSHL - COSQ * ZSINHL;
            ZN = ZNL;
            CC = C1L;
            ZE = ZEL;
            ZMO = ZMOL;
            LS = 40;
        }

        SSE = SSE + SE;
        SSI = SSI + SI;
        SSL = SSL + SL;
        SSG = SSG + SGH - COSIQ / SINIQ * SH;
        SSH = SSH + SH / SINIQ;

        // GEOPOTENTIAL RESONANCE INITIALIZATION FOR 12 HOUR ORBITS

        IRESFL = 0;
        ISYNFL = 0;
        if (XNQ >= .0052359877 || XNQ <= .0034906585) {
            if (XNQ < 8.26E-3 || XNQ > 9.24E-3) return;
            if (EQ < 0.5) return;
            IRESFL = 1;
            EOC = EQ * EQSQ;
            G201 = -.306f - (EQ - .64f) * .440f;
            if (EQ <= .65) {
                G211 = 3.616f - 13.247f * EQ + 16.290f * EQSQ;
                G310 = -19.302f + 117.390f * EQ - 228.419f * EQSQ + 156.591f * EOC;
                G322 = -18.9068f + 109.7927f * EQ - 214.6334f * EQSQ + 146.5816f * EOC;
                G410 = -41.122f + 242.694f * EQ - 471.094f * EQSQ + 313.953f * EOC;
                G422 = -146.407f + 841.880f * EQ - 1629.014f * EQSQ + 1083.435f * EOC;
                G520 = -532.114f + 3017.977f * EQ - 5740f * EQSQ + 3708.276f * EOC;
            } else {
                G211 = -72.099f + 331.819f * EQ - 508.738f * EQSQ + 266.724f * EOC;
                G310 = -346.844f + 1582.851f * EQ - 2415.925f * EQSQ + 1246.113f * EOC;
                G322 = -342.585f + 1554.908f * EQ - 2366.899f * EQSQ + 1215.972f * EOC;
                G410 = -1052.797f + 4758.686f * EQ - 7193.992f * EQSQ + 3651.957f * EOC;
                G422 = -3581.69f + 16178.11f * EQ - 24462.77f * EQSQ + 12422.52f * EOC;
                if (EQ <= .715) {
                    G520 = 1464.74f - 4664.75f * EQ + 3763.64f * EQSQ;
                } else {
                    G520 = -5149.66f + 29936.92f * EQ - 54087.36f * EQSQ + 31324.56f * EOC;
                }
            }
            if (EQ < .7) {
                G533 = -919.2277f + 4988.61f * EQ - 9064.77f * EQSQ + 5542.21f * EOC;
                G521 = -822.71072f + 4568.6173f * EQ - 8491.4146f * EQSQ + 5337.524f * EOC;
                G532 = -853.666f + 4690.25f * EQ - 8624.77f * EQSQ + 5341.4f * EOC;
            } else {
                G533 = -37995.78f + 161616.52f * EQ - 229838.2f * EQSQ + 109377.94f * EOC;
                G521 = -51752.104f + 218913.95f * EQ - 309468.16f * EQSQ + 146349.42f * EOC;
                G532 = -40023.88f + 170470.89f * EQ - 242699.48f * EQSQ + 115605.82f * EOC;
            }
            SINI2 = SINIQ * SINIQ;
            F220 = .75f * (1.f + 2.f * COSIQ + COSQ2);
            F221 = 1.5f * SINI2;
            F321 = 1.875f * SINIQ * (1.f - 2.f * COSIQ - 3.f * COSQ2);
            F322 = -1.875f * SINIQ * (1.f + 2.f * COSIQ - 3.f * COSQ2);
            F441 = 35.f * SINI2 * F220;
            F442 = 39.3750f * SINI2 * SINI2;
            F522 = 9.84375f * SINIQ * (SINI2 * (1.f - 2.f * COSIQ - 5.f * COSQ2)
                    + .33333333f * (-2.f + 4.f * COSIQ + 6.f * COSQ2));
            F523 = SINIQ * (4.92187512f * SINI2 * (-2.f - 4.f * COSIQ + 10.f * COSQ2)
                    + 6.56250012f * (1.f + 2.f * COSIQ - 3.f * COSQ2));
            F542 = 29.53125f * SINIQ * (2.f - 8.f * COSIQ + COSQ2 * (-12.f + 8.f * COSIQ
                    + 10.f * COSQ2));
            F543 = 29.53125f * SINIQ * (-2.f - 8.f * COSIQ + COSQ2 * (12.f + 8.f * COSIQ - 10.f * COSQ2));
            XNO2 = XNQ * XNQ;
            AINV2 = AQNV * AQNV;
            TEMP1 = 3.f * XNO2 * AINV2;
            TEMP = TEMP1 * ROOT22;
            D2201 = TEMP * F220 * G201;
            D2211 = TEMP * F221 * G211;
            TEMP1 = TEMP1 * AQNV;
            TEMP = TEMP1 * ROOT32;
            D3210 = TEMP * F321 * G310;
            D3222 = TEMP * F322 * G322;
            TEMP1 = TEMP1 * AQNV;
            TEMP = 2.f * TEMP1 * ROOT44;
            D4410 = TEMP * F441 * G410;
            D4422 = TEMP * F442 * G422;
            TEMP1 = TEMP1 * AQNV;
            TEMP = TEMP1 * ROOT52;
            D5220 = TEMP * F522 * G520;
            D5232 = TEMP * F523 * G532;
            TEMP = 2.f * TEMP1 * ROOT54;
            D5421 = TEMP * F542 * G521;
            D5433 = TEMP * F543 * G533;
            XLAMO = (XMAO + 2.0f * sat.ascendingNodeRA - 2.0 * THGR);
            BFACT = XLLDOT + XNODOT + XNODOT - THDT - THDT;
            BFACT = BFACT + SSL + SSH + SSH;
        } else {
            // SYNCHRONOUS RESONANCE TERMS INITIALIZATION

            IRESFL = 1;
            ISYNFL = 1;
            G200 = 1.0f + EQSQ * (-2.5f + .8125f * EQSQ);
            G310 = 1.0f + 2.0f * EQSQ;
            G300 = 1.0f + EQSQ * (-6.0f + 6.60937f * EQSQ);
            F220 = .75f * (1.f + COSIQ) * (1.f + COSIQ);
            F311 = .9375f * SINIQ * SINIQ * (1.f + 3.f * COSIQ) - .75f * (1.f + COSIQ);
            F330 = 1.f + COSIQ;
            F330 = 1.875f * F330 * F330 * F330;
            DEL1 = 3.f * XNQ * XNQ * AQNV * AQNV;
            DEL2 = 2.f * DEL1 * F220 * G200 * Q22;
            DEL3 = 3.f * DEL1 * F330 * G300 * Q33 * AQNV;
            DEL1 = DEL1 * F311 * G310 * Q31 * AQNV;
            FASX2 = .13130908f;
            FASX4 = 2.8843198f;
            FASX6 = .37448087f;
            XLAMO = (XMAO + sat.ascendingNodeRA + sat.argumentOfPerigee - THGR);
            BFACT = XLLDOT + XPIDOT - THDT;
            BFACT = BFACT + SSL + SSG + SSH;
        }

        XFACT = BFACT - XNQ;

        // INITIALIZE INTEGRATOR
        XLI = XLAMO;
        XNI = XNQ;
        ATIME = 0.0;
        STEPP = 720.0;
        STEPN = -720.0;
        STEP2 = 259200.0;
    }

    static void DPSEC(SatelliteOrbitalElement sat, DoubleRef XLL, DoubleRef OMGASM, DoubleRef XNODES, DoubleRef EM,
                      DoubleRef XINC, DoubleRef XN, double T) {
        DEEP.T = T;
        XLL.value = XLL.value + SSL * T;
        OMGASM.value = OMGASM.value + SSG * T;
        XNODES.value = XNODES.value + SSH * T;
        EM.value = sat.eccentricity + SSE * T;
        XINC.value = sat.inclination + SSI * T;
        if (XINC.value < 0.) {
            XINC.value = -XINC.value;
            XNODES.value = XNODES.value + C2.PI;
            OMGASM.value = OMGASM.value - C2.PI;
        }

        if (IRESFL == 0) return;

        while (true) {
            boolean checkIRETN = true;
            if ((ATIME == 0.0) ||
                    (T >= 0.0 && ATIME < 0.0) ||
                    (T < 0.0 && ATIME >= 0.0)) {
                // EPOCH RESTART

                if (T < 0.0) {
                    DELT = STEPN;
                } else {
                    DELT = STEPP;
                }
                ATIME = 0.0;
                XNI = XNQ;
                XLI = XLAMO;
            } else {
                if (Math.abs(T) < Math.abs(ATIME)) {
                    DELT = STEPP;
                    if (T >= 0.0) DELT = STEPN;
                    IRET = 100;
                    IRETN = 165;
                    checkIRETN = false;
                } else {
                    DELT = STEPN;
                    if (T > 0.0) DELT = STEPP;
                }
            }

            while (true) {
                if (checkIRETN) {
                    if (Math.abs(T - ATIME) >= STEPP) {
                        IRET = 125;
                        IRETN = 165;
                    } else {
                        FT = (T - ATIME);
                        IRETN = 140;
                    }
                }
                checkIRETN = true;

                // DOT TERMS CALCULATED

                if (ISYNFL != 0) {
                    XNDOT = DEL1 * Math.sin(XLI - FASX2) + DEL2 * Math.sin(2.f * (XLI - FASX4))
                            + DEL3 * Math.sin(3.f * (XLI - FASX6));
                    XNDDT = DEL1 * Math.cos(XLI - FASX2)
                            + 2.f * DEL2 * Math.cos(2.f * (XLI - FASX4))
                            + 3.f * DEL3 * Math.cos(3.f * (XLI - FASX6));
                } else {
                    XOMI = (OMEGAQ + OMGDT * ATIME);
                    X2OMI = XOMI + XOMI;
                    X2LI = XLI + XLI;
                    XNDOT = D2201 * Math.sin(X2OMI + XLI - G22)
                            + D2211 * Math.sin(XLI - G22)
                            + D3210 * Math.sin(XOMI + XLI - G32)
                            + D3222 * Math.sin(-XOMI + XLI - G32)
                            + D4410 * Math.sin(X2OMI + X2LI - G44)
                            + D4422 * Math.sin(X2LI - G44)
                            + D5220 * Math.sin(XOMI + XLI - G52)
                            + D5232 * Math.sin(-XOMI + XLI - G52)
                            + D5421 * Math.sin(XOMI + X2LI - G54)
                            + D5433 * Math.sin(-XOMI + X2LI - G54);
                    XNDDT = (D2201 * Math.cos(X2OMI + XLI - G22)
                            + D2211 * Math.cos(XLI - G22)
                            + D3210 * Math.cos(XOMI + XLI - G32)
                            + D3222 * Math.cos(-XOMI + XLI - G32)
                            + D5220 * Math.cos(XOMI + XLI - G52)
                            + D5232 * Math.cos(-XOMI + XLI - G52)
                            + 2. * (D4410 * Math.cos(X2OMI + X2LI - G44)
                            + D4422 * Math.cos(X2LI - G44)
                            + D5421 * Math.cos(XOMI + X2LI - G54)
                            + D5433 * Math.cos(-XOMI + X2LI - G54)));
                }
                XLDOT = XNI + XFACT;
                XNDDT = XNDDT * XLDOT;
                if (IRETN == 140) {
                    XN.value = XNI + XNDOT * FT + XNDDT * FT * FT * 0.5f;
                    XL = XLI + XLDOT * FT + XNDOT * FT * FT * 0.5f;
                    TEMP = (-XNODES.value + THGR + T * THDT);
                    XLL.value = XL - OMGASM.value + TEMP;
                    if (ISYNFL == 0) XLL.value = XL + TEMP + TEMP;
                    return;
                }

                // INTEGRATOR

                XLI = (XLI + XLDOT * DELT + XNDOT * STEP2);
                XNI = (XNI + XNDOT * DELT + XNDDT * STEP2);
                ATIME = ATIME + DELT;

                if (IRET == 100) {
                    break;
                }
            }
        }
    }

    // ENTRANCES FOR LUNAR-SOLAR PERIODICS
    static void DPPER(DoubleRef EM, DoubleRef XINC, DoubleRef OMGASM, DoubleRef XNODES, DoubleRef XLL) {
        SINIS = Math.sin(XINC.value);
        COSIS = Math.cos(XINC.value);
        if (Math.abs(SAVTSN - T) >= 30.0) {
            SAVTSN = T;
            ZM = ZMOS + ZNS * T;
            ZF = ZM + 2.f * ZES * Math.sin(ZM);
            SINZF = Math.sin(ZF);
            F2 = .5f * SINZF * SINZF - .25f;
            F3 = -.5f * SINZF * Math.cos(ZF);
            SES = SE2 * F2 + SE3 * F3;
            SIS = SI2 * F2 + SI3 * F3;
            SLS = SL2 * F2 + SL3 * F3 + SL4 * SINZF;
            SGHS = SGH2 * F2 + SGH3 * F3 + SGH4 * SINZF;
            SHS = SH2 * F2 + SH3 * F3;
            ZM = ZMOL + ZNL * T;
            ZF = ZM + 2.f * ZEL * Math.sin(ZM);
            SINZF = Math.sin(ZF);
            F2 = .5f * SINZF * SINZF - .25f;
            F3 = -.5f * SINZF * Math.cos(ZF);
            SEL = EE2 * F2 + E3 * F3;
            SIL = XI2 * F2 + XI3 * F3;
            SLL = XL2 * F2 + XL3 * F3 + XL4 * SINZF;
            SGHL = XGH2 * F2 + XGH3 * F3 + XGH4 * SINZF;
            SHL = XH2 * F2 + XH3 * F3;
            PE = SES + SEL;
            PINC = SIS + SIL;
            PL = SLS + SLL;
        }

        PGH = SGHS + SGHL;
        PH = SHS + SHL;
        XINC.value = XINC.value + PINC;
        EM.value = EM.value + PE;

        if (XQNCL >= .2) {
            // APPLY PERIODICS DIRECTLY
            PH = PH / SINIQ;
            PGH = PGH - COSIQ * PH;
            OMGASM.value = OMGASM.value + PGH;
            XNODES.value = XNODES.value + PH;
            XLL.value = XLL.value + PL;
        } else {
            // APPLY PERIODICS WITH LYDDANE MODIFICATION
            SINOK = Math.sin(XNODES.value);
            COSOK = Math.cos(XNODES.value);
            ALFDP = SINIS * SINOK;
            BETDP = SINIS * COSOK;
            DALF = PH * COSOK + PINC * COSIS * SINOK;
            DBET = -PH * SINOK + PINC * COSIS * COSOK;
            ALFDP = ALFDP + DALF;
            BETDP = BETDP + DBET;
            XLS = XLL.value + OMGASM.value + COSIS * XNODES.value;
            DLS = PL + PGH - PINC * XNODES.value * SINIS;
            XLS = XLS + DLS;
            XNODES.value = SDP8_SGP8.ACTAN(ALFDP, BETDP);
            XLL.value = XLL.value + PL;
            OMGASM.value = XLS - XLL.value - Math.cos(XINC.value) * XNODES.value;
        }
    }
}
