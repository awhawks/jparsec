package jparsec.ephem.stars;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.io.ConsoleReport;
import jparsec.io.ReadFile;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;
import jparsec.util.JPARSECException;

public class StarEphemTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("StarEphem test");

        try {
            AstroDate astro = new AstroDate(2050, AstroDate.JANUARY, 1, 6, 10, 0);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);
            EphemerisElement eph = new EphemerisElement(Target.TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.WILLIAMS_1994,
                    EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.JPL_DE405);

            // BSC5
/*
            ReadFile re = new ReadFile();
            re.setPath(PATH_TO_BSC5_FILE);
            re.setFormat(ReadFile.FORMAT.FORMAT_BSC5);
            re.readFileOfStars();
            String myStar = "Alpheratz";
            int my_star = re.searchByName(getCatalogNameFromProperName(myStar));
            StarElement star = (StarElement) re.readElements.get(my_star);

            ObservatoryElement obs = Observatory.findObservatorybyName("Greenwich");
            ObserverElement observer = ObserverElement.parseObservatory(obs);
*/
            // Moshier test, seems he lives near Boston
            CityElement city = City.findCity("Boston");
            ObserverElement observer = ObserverElement.parseCity(city);
            observer.setLongitudeDeg(-71.13);
            observer.setLatitudeDeg(42.27); // he says geocentic lat = 42.0785 => lat 42.27 deg

            StarElement star = new StarElement("HD 119288", Functions.parseRightAscension(13, 39, 44.526), Functions
                    .parseDeclination("8", 38, 28.63), 1, 2.06f, (float) (-0.0259 * 15.0 * Constant.ARCSEC_TO_RAD), (float) (-0.093 * Constant.ARCSEC_TO_RAD), 0, Constant.B1950,
                    EphemerisElement.FRAME.FK4);

            // AA Supplement (2006) page 181: difference of 6 mas in RA and 1 mas in DEC if correction of
            // proper motions from "/tropical year to "/Julian year is applied, else difference is < 1 mas.
            // After inverting the transformation the inconsistency is close to 1 mas, due to non-zero 
            // radial v, which is supposed constant between 1950 and 2000 ...
            // In previos example (HD 119288) inconsistency is obviously 0.
/*
            star = new StarElement("Fictitious", Functions.parseRightAscension(14, 36, 11.25), Functions
                    .parseDeclination("-60", 37, 48.85), 751, 2.06f, (float) (-49.042 * 0.01 * 15.0 * Constant.ARCSEC_TO_RAD), (float) (71.2 * 0.01 * Constant.ARCSEC_TO_RAD), -22.2f, Constant.B1950,
                    EphemerisElement.FRAME.FK4); // Should be 14 39 36.1869 -60 50 7.393 -49.5060 69.934 0.7516 -22.18
*/
            star = StarEphem.transform_FK4_B1950_to_FK5_J2000(star);
            System.out.println("RA: " + Functions.formatRA(star.rightAscension, 6));
            System.out.println("DEC: " + Functions.formatDEC(star.declination, 5));
            System.out.println("dRA: " + Functions.formatDEC(star.properMotionRA / 15.0, 6));
            System.out.println("dDEC: " + Functions.formatDEC(star.properMotionDEC, 5));
            System.out.println("Parallax: " + Functions.formatDEC(Constant.ARCSEC_TO_RAD * 0.001 * star.parallax, 5));
            System.out.println("Radial V: " + Functions.formatValue(star.properMotionRadialV, 5));
            // JPrecess.pro program: 13 42 12.740, 8 23 17.69, ok
            EphemerisElement eph_iau1976 = new EphemerisElement();
            eph_iau1976.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_1976;
            star = StarEphem.transform_FK5_J2000_to_FK4_B1950(star, eph_iau1976);
            System.out.println("RA: " + Functions.formatRA(star.rightAscension, 6));
            System.out.println("DEC: " + Functions.formatDEC(star.declination, 5));
            System.out.println("dRA: " + Functions.formatDEC(star.properMotionRA / 15.0, 6));
            System.out.println("dDEC: " + Functions.formatDEC(star.properMotionDEC, 5));

/*            double m[][] = new double[6][6];
            int in = -1;
            for (int i=0; i<6; i++) {
                for (int j=0; j<6; j++) {
                    in ++;
                    m[i][j] = MAT[in];
                }                
            }
            Matrix ma = new Matrix(m);
            ma.print(17, 15);
            ma = ma.inverse();
            ma.print(17, 15);
*/
            // Use his catalog. To reproduce results of aa56 of Moshier I use
            // Williams formulae, for the recent aa200 IAU1976 should be used.
            star = new StarElement("Alpheratz", Functions.parseRightAscension(0, 8, 23.265), Functions
                    .parseDeclination("29", 05, 25.58), 24, 2.06f, (float) (1.039 * 0.01 * 15.0 * Constant.ARCSEC_TO_RAD / Math
                    .cos(0 * 29.1 * Constant.DEG_TO_RAD)), (float) (-16.33 * 0.01 * Constant.ARCSEC_TO_RAD), -12, Constant.J2000,
                    EphemerisElement.FRAME.ICRF);
            
/*            // AA 2004 test, B31
            astro = new AstroDate(2004, AstroDate.JANUARY, 1, 0, 0, 0);
            time = new TimeElement(astro, SCALE.TERRESTRIAL_TIME);
            eph.algorithm = EphemerisElement.ALGORITHM.JPL_DE405;
            star = new StarElement("Ficticious", Functions.parseRightAscension(14, 39, 36.087), Functions
                    .parseDeclination("-60", 50, 7.14), 752, 2.06f, (float) (-49.486 * 0.01 * 15.0 * Constant.ARCSEC_TO_RAD), (float) (69.60 * 0.01 * Constant.ARCSEC_TO_RAD), -22.2f, Constant.J2000,
                    EphemerisElement.FRAME.ICRF); // eph object above in ICRF to avoid frame conversion
            // AA says 14 39 49.532, -60 50 50.75 (OK, using old WILLIAMS formulae)
*/

            // For Barnard star
/*            star = new StarElement("Barnard", Functions.parseRightAscension(17, 55, 23.0), Functions
                    .parseDeclination("4", 33, 18.00), 548, 9.54f, (float) (-5.0 * 15.0 * Constant.ARCSEC_TO_RAD / 100.0),
                    (float) (1031.0 * Constant.ARCSEC_TO_RAD / 100.0), -107.8f, Constant.B1950, EphemerisElement.FRAME.FK4);
            astro = new AstroDate(1986, AstroDate.JANUARY, 1, 0, 0, 0);
            time = new TimeElement(astro, SCALE.TERRESTRIAL_TIME);
*/
            
/*            astro = new AstroDate(1989, AstroDate.JULY, 3, 10, 0, 0);
            time = new TimeElement(astro, SCALE.UNIVERSAL_TIME_UTC);
            //Configuration.PREFER_PRECISION_IN_EPHEMERIDES = false;
            eph = new EphemerisElement(TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.STAR);
            star = StarEphem.getStarElement(StarEphem.getStarTargetIndex("18460079"));
            city = City.findCity("Madrid");
            observer = ObserverElement.parseCity(city);
*/

            StarEphemElement star_ephem = StarEphem.starEphemeris(time, observer, eph, star, true);

            System.out.println("CALCULATION TIME " + TimeFormat.formatJulianDayAsDateAndTime(astro.jd(), time.timeScale));
            System.out.println("Using catalog by Moshier " + (star.name));
            System.out.println("RA: " + Functions.formatRA(star_ephem.rightAscension));
            System.out.println("DEC: " + Functions.formatDEC(star_ephem.declination));

            eph.isTopocentric = true;
            star_ephem = StarEphem.starEphemeris(time, observer, eph, star, true);

            System.out.println("MAG: " + Functions.formatValue(star_ephem.magnitude, 3));
            System.out.println("R: " + Functions.formatValue(star_ephem.distance, 3));
            System.out.println("AZI: " + Functions.formatAngle(star_ephem.azimuth, 3));
            System.out.println("ELE: " + Functions.formatAngle(star_ephem.elevation, 3));
            System.out.println("PARA. ANGLE: " + Functions.formatAngle(star_ephem.paralacticAngle, 3));
            System.out.println("CONSTEL: " + star_ephem.constellation);
            System.out.println("RISE:      " + TimeFormat.formatJulianDayAsDateAndTime(star_ephem.rise, TimeElement.SCALE.LOCAL_TIME));
            System.out.println("TRANSIT:   " + TimeFormat.formatJulianDayAsDateAndTime(star_ephem.transit, TimeElement.SCALE.LOCAL_TIME));
            System.out.println("MAX_ELEV:  " + Functions.formatAngle(star_ephem.transitElevation, 3));
            System.out.println("SET:       " + TimeFormat.formatJulianDayAsDateAndTime(star_ephem.set, TimeElement.SCALE.LOCAL_TIME));

            // JPARSEC Sky2000
            ReadFile re = new ReadFile();
            re.setPath(StarEphem.PATH_TO_SkyMaster2000_JPARSEC_FILE);
            re.setFormat(ReadFile.FORMAT.JPARSEC_SKY2000);
            re.readFileOfStars();
            int my_star = re.searchByName(StarEphem.getCatalogNameFromProperName("Alpheratz"));
            star = (StarElement) re.getReadElements()[my_star];

/*
            star = new StarElement("Polar", Functions.parseRightAscension(2, 31, 47.0753), Functions
                    .parseDeclination("89", 15, 50.090), 7.56, 1.97f, (float) (0.04422 * Constant.ARCSEC_TO_RAD / Math
                    .cos(89.25 * Constant.DEG_TO_RAD)), (float) (-0.01175 * Constant.ARCSEC_TO_RAD), star.properMotionRadialV, Constant.J2000,
                    EphemerisElement.FRAME.FRAME_FK5);
*/
            eph.isTopocentric = true;
            star_ephem = StarEphem.starEphemeris(time, observer, eph, star, true);

            System.out.println("");
            System.out.println("USING JPARSEC Sky2000");
            System.out.println("RA: " + Functions.formatRA(star_ephem.rightAscension));
            System.out.println("DEC: " + Functions.formatDEC(star_ephem.declination));

            //eph.isTopocentric = true;
            //star_ephem = StarEphem.starEphemeris(time, observer, eph, star, true);

            System.out.println("MAG: " + Functions.formatValue(star_ephem.magnitude, 3));
            System.out.println("R: " + Functions.formatValue(star_ephem.distance, 3));
            System.out.println("AZI: " + Functions.formatAngle(star_ephem.azimuth, 3));
            System.out.println("ELE: " + Functions.formatAngle(star_ephem.elevation, 3));
            System.out.println("PARA. ANGLE: " + Functions.formatAngle(star_ephem.paralacticAngle, 3));
            System.out.println("CONSTEL: " + star_ephem.constellation);
            System.out.println("RISE:      " + TimeFormat.formatJulianDayAsDateAndTime(star_ephem.rise, TimeElement.SCALE.LOCAL_TIME));
            System.out.println("TRANSIT:   " + TimeFormat.formatJulianDayAsDateAndTime(star_ephem.transit, TimeElement.SCALE.LOCAL_TIME));
            System.out.println("MAX_ELEV:  " + Functions.formatAngle(star_ephem.transitElevation, 3));
            System.out.println("SET:       " + TimeFormat.formatJulianDayAsDateAndTime(star_ephem.set, TimeElement.SCALE.LOCAL_TIME));

            // Test data from Moshier
/*
            StarElement star2 = new StarElement("Barnard", Functions.parseRightAscension(17, 55, 23.0), Functions
                    .parseDeclination(4, 33, 18.00), 548, 9.54, -5.0 * 15.0 * Constant.ARCSEC_TO_RAD / 100.0,
                    1031.0 * Constant.ARCSEC_TO_RAD / 100.0, -107.8, Constant.B1950, EphemerisElement.FRAME.FRAME_FK4);

            StarEphemElement star_ephem2 = StarEphem.starEphemeris(time, observer, eph, star2, true);

            System.out.println("");
            System.out.println("RA: " + Functions.formatRA(star_ephem2.rightAscension));
            System.out.println("DEC: " + Functions.formatDEC(star_ephem2.declination));
            System.out.println("MAG: " + Functions.formatValue(star_ephem2.magnitude, 3));
            System.out.println("R: " + Functions.formatValue(star_ephem2.distance, 3));
            System.out.println("AZI: " + Functions.formatAngle(star_ephem2.azimuth, 3));
            System.out.println("ELE: " + Functions.formatAngle(star_ephem2.elevation, 3));
            System.out.println("PARA. ANGLE: " + Functions.formatAngle(star_ephem2.paralacticAngle, 3));
            System.out.println("CONSTEL: " + star_ephem2.constellation);
            System.out.println("RISE:      " + TimeFormat.formatJulianDayAsDateAndTime(star_ephem2.rise));
            System.out.println("TRANSIT:   " + TimeFormat.formatJulianDayAsDateAndTime(star_ephem2.transit));
            System.out.println("MAX_ELEV:  " + Functions.formatAngle(star_ephem2.transitElevation, 3));
            System.out.println("SET:       " + TimeFormat.formatJulianDayAsDateAndTime(star_ephem2.set));

            StarElement star3 = new StarElement("MWC1080", Functions.parseRightAscension(23, 17, 25.5744), Functions
                    .parseDeclination(60, 50, 43.34), 1000.0 / 1600, 10, -0.01863 * Constant.ARCSEC_TO_RAD / Math
                    .cos(60.83 * Constant.DEG_TO_RAD), -0.01484 * Constant.ARCSEC_TO_RAD, -4.0, Constant.J2000,
                    EphemerisElement.FRAME.FRAME_ICRS);
            StarElement star4 = new StarElement("VVSer", Functions.parseRightAscension(18, 28, 47.863), Functions
                    .parseDeclination(0, 8, 39.99), 1000.0 / 330, 10, 0.002 * Constant.ARCSEC_TO_RAD / Math
                    .cos(0.13 * Constant.DEG_TO_RAD), -0.008 * Constant.ARCSEC_TO_RAD, -4.0, Constant.J2000,
                    EphemerisElement.FRAME.FRAME_FK5);
*/
/*            star = new StarElement("Polar", 2.530301028 / Constant.RAD_TO_HOUR, 89.264109444 * Constant.DEG_TO_RAD,
                7.56, 1.97f, (float) (0.04422 * Constant.ARCSEC_TO_RAD / Math
                .cos(89.26411 * Constant.DEG_TO_RAD)), (float) (-0.01175 * Constant.ARCSEC_TO_RAD), -17.4f, Constant.J2000,
                EphemerisElement.FRAME.FRAME_FK5);
            time = new TimeElement(2450300.5, SCALE.TERRESTRIAL_TIME);
            eph.isTopocentric = false;
            eph.frame = EphemerisElement.FRAME.FRAME_FK5;
            eph.ephemMethod = EphemerisElement.APPLY_IAU2006;
            star_ephem = StarEphem.starEphemeris(time, observer, eph, star, true);

            System.out.println("");
            System.out.println("POLARIS POSITION AT JD = 2450203.5 TT");
            System.out.println("RA: " + (star_ephem.rightAscension * Constant.RAD_TO_HOUR));
            System.out.println("DEC: " + (star_ephem.declination * Constant.RAD_TO_DEG));
*/
            System.out.println("");
            StarElement star5 = new StarElement("RMon", Functions.parseRightAscension(6, 39, 9.947), Functions
                    .parseDeclination("8", 44, 10.75), 1000.0 / 800, 10, (float) (-0.003 * Constant.ARCSEC_TO_RAD / Math
                    .cos(8.75 * Constant.DEG_TO_RAD)), (float) (-0.004 * Constant.ARCSEC_TO_RAD), 12.0f, Constant.J2000,
                    EphemerisElement.FRAME.FK5);

            StarElement star7 = new StarElement("ZCMa", Functions.parseRightAscension(7, 3, 43.1619), Functions
                    .parseDeclination("-11", 33, 6.209), 1000.0 / 930, 10, (float) (-0.00877 * Constant.ARCSEC_TO_RAD / Math
                    .cos(11.55 * Constant.DEG_TO_RAD)), (float) (-0.00342 * Constant.ARCSEC_TO_RAD), 28.0f, Constant.J2000,
                    EphemerisElement.FRAME.FK5);

            StarElement star6 = new StarElement("HD259431", Functions.parseRightAscension(6, 33, 5.19), Functions
                    .parseDeclination("+10", 19, 19.99), 5.78, 8.8f, (float) (-0.00237 * Constant.ARCSEC_TO_RAD / Math
                    .cos(25.35 * Constant.DEG_TO_RAD)), (float) (-0.00272 * Constant.ARCSEC_TO_RAD), 17f, Constant.J2000,
                    EphemerisElement.FRAME.FK5);

            //StarElement star7 = new StarElement("Check", StarEphem.lsrRA, StarEphem.lsrDEC, 930, 0, 0, 0, 0,
            //    Constant.J2000, EphemerisElement.FRAME.FRAME_FK5);

            astro = new AstroDate(2011, AstroDate.JULY, 13, 10, 22, 46);
            time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            city = City.findCity("Madrid");
            observer = ObserverElement.parseCity(city);
            double LSR = StarEphem.getLSRradialVelocity(time, observer, eph, star6);
            System.out.println("LSR of " + star6.name + ": " + LSR);
            eph.isTopocentric = true;
            double vRel = StarEphem.getRadialVelocity(time, observer, eph, star6);
            System.out.println("Vtop of " + star6.name + ": " + vRel);
            eph.isTopocentric = false;
            vRel = StarEphem.getRadialVelocity(time, observer, eph, star6);
            System.out.println("Vgeo of " + star6.name + ": " + vRel);

            // Sirius = HIP 32349
            StarElement star8 = new StarElement("Sirius", Functions.parseRightAscension(6, 45, 9.25), Functions
                    .parseDeclination("-16", 42, 47.3), 379.21, -1.44f, (float) (-0.54601 * Constant.ARCSEC_TO_RAD / Math
                    .cos(-16.7131388889 * Constant.DEG_TO_RAD)), (float) (-1.22308 * Constant.ARCSEC_TO_RAD),
                    -7.6f, Constant.J1991_25, // Radial velocity from Simbad
                    EphemerisElement.FRAME.ICRF);
            // Hipparcos -> FK5 J2000
            star8 = StarEphem.transformStarElementsToOutputEquinoxAndFrame(star8, EphemerisElement.FRAME.FK5, Constant.J2000, Constant.J1991_25);
            System.out.println("J2000 RA FK5:     " + Functions.formatRA(star8.rightAscension, 4));
            System.out.println("J2000 DEC FK5:   " + Functions.formatDEC(star8.declination, 3));
            System.out.println("J2000 DRA FK5:   " + Functions.formatValue(star8.properMotionRA * Constant.RAD_TO_ARCSEC, 3));
            System.out.println("J2000 DDEC FK5: " + Functions.formatValue(star8.properMotionDEC * Constant.RAD_TO_ARCSEC, 3));
            System.out.println("J2000 DRV FK5:   " + Functions.formatValue(star8.properMotionRadialV, 3));

            // ICRS J2000 (Simbad): 06 45 08.91728 -16 42 58.0171         8.9174      58.002     (JPARSEC)
            // FK5 J2000 (Simbad): 06 45 08.917 -16 42 58.02                  8.9189      58.024
            // FK4 B1950 (Simbad): 06 42 56.72 -16 38 45.4                    56.7247    45.391

            // Now to B1950. Note in the previous step precession is not applied since Hipparcos data is referred to ICRS.
            // Here we first force equinox to be already J2000 to avoid precession also. That's why previous method don't
            // support direct transformation to B1950, due to peculiarity of Hipparcos reference epoch/equinox.
            star8.equinox = Constant.J2000;
            star8 = StarEphem.transform_FK5_J2000_to_FK4_B1950(star8, eph_iau1976);
            System.out.println("B1950 RA FK4:     " + Functions.formatRA(star8.rightAscension, 4));
            System.out.println("B1950 DEC FK4:   " + Functions.formatDEC(star8.declination, 3));
            System.out.println("B1950 DRA FK4:   " + Functions.formatValue(star8.properMotionRA * Constant.RAD_TO_ARCSEC, 3));
            System.out.println("B1950 DDEC FK4: " + Functions.formatValue(star8.properMotionDEC * Constant.RAD_TO_ARCSEC, 3));
            System.out.println("B1950 DRV FK4:   " + Functions.formatValue(star8.properMotionRadialV, 3));

            // Cross check
            star8 = StarEphem.transform_FK4_B1950_to_FK5_J2000(star8);
            System.out.println("J2000 RA FK5:     " + Functions.formatRA(star8.rightAscension, 4));
            System.out.println("J2000 DEC FK5:   " + Functions.formatDEC(star8.declination, 3));
            System.out.println("J2000 DRA FK5:   " + Functions.formatValue(star8.properMotionRA * Constant.RAD_TO_ARCSEC, 3));
            System.out.println("J2000 DDEC FK5: " + Functions.formatValue(star8.properMotionDEC * Constant.RAD_TO_ARCSEC, 3));
            System.out.println("J2000 DRV FK5:   " + Functions.formatValue(star8.properMotionRadialV, 3));

            star8 = new StarElement("HD 6755", Functions.parseRightAscension(1, 9, 42.3),
                    Functions.parseDeclination("61", 32, 49.5), 1000.0 / 139, 0, (float) (628.42 * 1.0E-3 * Constant.ARCSEC_TO_RAD),
                    (float) (76.65 * 1.0E-3 * Constant.ARCSEC_TO_RAD), -321.4f, Constant.J2000, EphemerisElement.FRAME.FK5);
            double uvw[] = StarEphem.getGalacticMotionUVW(star8, true);
            System.out.println(ConsoleReport.doubleArrayReport(new String[] { "u = xxxx.x km/s", "v = xxxx.x km/s", "w = xxxx.x km/s" }, uvw));
            JPARSECException.showWarnings();
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
