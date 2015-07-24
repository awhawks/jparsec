package jparsec.ephem.probes;

import java.util.ArrayList;
import jparsec.astronomy.Constellation;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.graph.DataSet;
import jparsec.io.ReadFile;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.Observatory;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.SiderealTime;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;
import jparsec.time.TimeScale;
import jparsec.util.Configuration;
import jparsec.util.JPARSECException;

public class SatelliteEphemTest {

    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("SatelliteEphem test");

        try {
            AstroDate astro = new AstroDate(2005, 10, 3, 11, 0, 0); //2011, AstroDate.JULY, 4, 13, 32, 18.732);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);
            //ObservatoryElement obs = Observatory.findObservatorybyName("Yebes");
            //ObserverElement observer = ObserverElement.parseObservatory(obs);
            ObserverElement observer = ObserverElement.parseCity(City.findCity("Madrid"));
            //observer.dstCode = observer2.dstCode;
            //observer.timeZone = observer2.timeZone;

            //System.out.println("LON "+Functions.formatAngle(observer.longitude, 3));
            //System.out.println("LAT "+Functions.formatAngle(observer.latitude, 3));

            //observer.latitude = 40.524661111111108 * Constant.DEG_TO_RAD;
            //observer.longitude = -3.086863888888888 * Constant.DEG_TO_RAD;
            //observer.height = 990;

            if (!Configuration.isAcceptableDateForArtificialSatellites(astro))
                Configuration.updateArtificialSatellitesInTempDir(astro);

            String name = "ISS";
            int index = SatelliteEphem.getArtificialSatelliteTargetIndex(name);

            EphemerisElement eph = new EphemerisElement(Target.TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.ICRF);
            eph.algorithm = EphemerisElement.ALGORITHM.ARTIFICIAL_SATELLITE;

            //for (int i=0; i<readFile.getNumberOfObjects(); i++) {
            //    index = i;

            eph.targetBody.setIndex(index);
            //EphemElement ephem = jparsec.ephem.Ephem.getEphemeris(time, observer, eph, false);
            SatelliteEphemElement ephem = SatelliteEphem.satEphemeris(time, observer, eph, false);

            ephem.nextPass = SDP4_SGP4.getNextPass(time, observer, eph, SatelliteEphem.getArtificialSatelliteOrbitalElement(index), 15 * Constant.DEG_TO_RAD, 7, true);

            /*
            // To calculate the next rise, set, transit after the next one
            TimeElement newTime = new TimeElement(ephem.set[0] + 5.0 / 1440.0, SCALE.LOCAL_TIME);
            SatelliteEphemElement ephem2 = SatelliteEphem.satEphemeris(newTime, observer, eph, true);
            double nextPass = ephem2.nextPass;
            if (nextPass != 0.0) {
                ephem.rise = DataSet.addDoubleArray(ephem.rise, ephem2.rise);
                ephem.set = DataSet.addDoubleArray(ephem.set, ephem2.set);
                ephem.transit = DataSet.addDoubleArray(ephem.transit, ephem2.transit);
                ephem.transitElevation = DataSet.addFloatArray(ephem.transitElevation, ephem2.transitElevation);
            }
            */

            name = SatelliteEphem.readFile.getObjectName(index);
            double JD = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.TERRESTRIAL_TIME);
            System.out.println("JD " + JD + " / index " + index);
            double st = SiderealTime.apparentSiderealTime(time, observer, eph);
            //ConsoleReport.fullEphemReportToConsole(ephem, name);
            //if (ephem.elevation > 0.0) {
            System.out.println("" + name + " RA: " + Functions.formatRA(ephem.rightAscension));
            System.out.println("" + name + " DEC: " + Functions.formatDEC(ephem.declination));
            System.out.println("" + name + " dist: " + ephem.distance);
            System.out.println("" + name + " elong: " + ephem.elongation * Constant.RAD_TO_DEG);
            System.out.println("" + name + " lst: " + (st / 15.0) * Constant.RAD_TO_DEG);
            System.out.println("" + name + " h:   " + ((st - ephem.rightAscension) * Constant.RAD_TO_DEG / 15.0 + 24));
            System.out.println("" + name + " azi: " + ephem.azimuth * Constant.RAD_TO_DEG);
            System.out.println("" + name + " alt: " + ephem.elevation * Constant.RAD_TO_DEG);
            System.out.println("" + name + " ilum: " + ephem.illumination);
            System.out.println("" + name + " mag: " + ephem.magnitude);
            System.out.println("" + name + " sub. E. lon:  " + Functions.formatAngle(ephem.subEarthLongitude, 3));
            System.out.println("" + name + " sub. E. lat:  " + Functions.formatAngle(ephem.subEarthLatitude, 3));
            System.out.println("" + name + " sub. E. dist: " + ephem.subEarthDistance);
            System.out.println("" + name + " speed: " + ephem.topocentricSpeed);
            System.out.println("" + name + " revolution: " + ephem.revolutionsCompleted);
            System.out.println("" + name + " eclipsed: " + ephem.isEclipsed);
            System.out.println("" + name + " next pass: " + TimeFormat.formatJulianDayAsDateAndTime(Math.abs(ephem.nextPass), TimeElement.SCALE.LOCAL_TIME));
            //System.out.println("" + name + " next pass: " + TimeFormat.formatJulianDayAsDateAndTime(Math.abs(nextPass)));

            if (ephem.rise != null) {
                for (int i = 0; i < ephem.rise.length; i++) {
                    System.out.println("RISE:      " + TimeFormat.formatJulianDayAsDateAndTime(ephem.rise[i], TimeElement.SCALE.LOCAL_TIME));
                    System.out.println("TRANSIT:   " + TimeFormat.formatJulianDayAsDateAndTime(ephem.transit[i], TimeElement.SCALE.LOCAL_TIME));
                    System.out.println("MAX_ELEV:  " + Functions.formatAngle(ephem.transitElevation[i], 3));
                    System.out.println("SET:       " + TimeFormat.formatJulianDayAsDateAndTime(ephem.set[i], TimeElement.SCALE.LOCAL_TIME));
                }
            }
            //    }
            //}

            //astro = new AstroDate(2000, AstroDate.JANUARY, 0, 0, 0, 0);
            //obs = Observatory.findObservatorybyName("Greenwich");
            //observer = ObserverElement.parseObservatory(obs);
            /*
            time = new TimeElement(astro, SCALE.UNIVERSAL_TIME_UTC);
            eph.targetBody = jparsec.ephem.Target.SUN;
            eph.algorithm = EphemerisElement.ALGORITHM.ALGORITHM_MOSHIER;
            EphemElement sunEphem = jparsec.ephem.Ephem.getEphemeris(time, observer, eph, false);
            ConsoleReport.fullEphemReportToConsole(sunEphem, "Sun");
            double dist = LocationElement.getAngularDistance(
                    new LocationElement(sunEphem.azimuth, sunEphem.elevation, 1.0),
                    new LocationElement(ephem.azimuth, ephem.elevation, 1.0)
                    );
            System.out.println("Elong = "+dist*Constant.RAD_TO_DEG);
            double ast = jparsec.time.SiderealTime.greenwichMeanSiderealTime(time, observer, eph);
            System.out.println("GHA SUN = "+Functions.formatRA(ast));
            */

            /*
            String file = "";
            for (int i=0; i<1353-8;i++) {
                astro = new AstroDate(2009, AstroDate.MAY, 22, 12, 15*i, 0);
                double jd = astro.jd();
                astro = new AstroDate(jd);
                time = new TimeElement(astro, SCALE.UNIVERSAL_TIME_UTC);
                ephem = SatelliteEphem.satEphemeris(time, observer, eph, false);
                file +=" "+astro.getYear()+"/"+astro.getMonth()+"/"+astro.getDay()+"-"+astro.getHour()+":"+astro.getMinute()+":"+(int) (astro.getSecond()+0.5)+"   "+(ephem.azimuth*Constant.RAD_TO_DEG+180.0)+"   "+ephem.elevation*Constant.RAD_TO_DEG+"   "+ephem.distance;
                file += FileIO.getLineSeparator();
            }
            WriteFile.writeAnyExternalFile("/home/alonso/o.txt", file);
            */

            //double time_step = 1.0 / (Constant.MINUTES_PER_HOUR * Constant.HOURS_PER_DAY);
            //System.out.println("quickSearch "+ SatelliteEphem.getBestQuickSearch(SatelliteEphem.getArtificialSatelliteOrbitalElement(index), 15.0 * Constant.DEG_TO_RAD)/time_step);
            JPARSECException.showWarnings();
            System.out.println(Constellation.getConstellationName(ephem.rightAscension, ephem.declination, astro.jd(), eph));

            // TEST IRIDIUM FLARES
            // Download from http://www.tle.info/data/iridium.txt
            SatelliteEphem.setSatellitesFromExternalFile(DataSet.arrayListToStringArray(ReadFile.readAnyExternalFile("/home/alonso/eclipse/libreria_jparsec/ephem/test/iridium.txt")));

            //name = "IRIDIUM 31";
            //astro = new AstroDate(2011, AstroDate.OCTOBER, 26, 14, 51, 21);
            //name = "IRIDIUM 5";
            //astro = new AstroDate(2011, AstroDate.OCTOBER, 26, 16, 47, 42);
            name = "IRIDIUM 62";
            astro = new AstroDate(2011, AstroDate.OCTOBER, 27, 9, 24, 38);
            time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            observer = ObserverElement.parseObservatory(Observatory.findObservatorybyCode(1169));

            System.out.println();
            System.out.println("Testing iridium flares");
            index = SatelliteEphem.getArtificialSatelliteTargetIndex(name);
            eph.targetBody.setIndex(index);
            ephem = SatelliteEphem.satEphemeris(time, observer, eph, false);
            System.out.println("" + name + " RA: " + Functions.formatRA(ephem.rightAscension));
            System.out.println("" + name + " DEC: " + Functions.formatDEC(ephem.declination));
            System.out.println("" + name + " dist: " + ephem.distance);
            System.out.println("" + name + " elong: " + ephem.elongation * Constant.RAD_TO_DEG);
            System.out.println("" + name + " azi: " + ephem.azimuth * Constant.RAD_TO_DEG);
            System.out.println("" + name + " alt: " + ephem.elevation * Constant.RAD_TO_DEG);
            System.out.println("" + name + " ilum: " + ephem.illumination);
            System.out.println("" + name + " mag: " + ephem.magnitude);
            System.out.println("" + name + " sub. E. lon:  " + Functions.formatAngle(ephem.subEarthLongitude, 3));
            System.out.println("" + name + " sub. E. lat:  " + Functions.formatAngle(ephem.subEarthLatitude, 3));
            System.out.println("" + name + " sub. E. dist: " + ephem.subEarthDistance);
            System.out.println("" + name + " speed: " + ephem.topocentricSpeed);
            System.out.println("" + name + " revolution: " + ephem.revolutionsCompleted);
            System.out.println("" + name + " eclipsed: " + ephem.isEclipsed);
            System.out.println("" + name + " iridium angle: " + ephem.iridiumAngle);
            System.out.println("" + name + " next pass: " + TimeFormat.formatJulianDayAsDateAndTime(Math.abs(ephem.nextPass), TimeElement.SCALE.LOCAL_TIME));

            SatelliteEphem.MAXIMUM_IRIDIUM_ANGLE_FOR_LUNAR_FLARES = 0.5;
            astro = new AstroDate(2015, 6, 26); //2011, AstroDate.OCTOBER, 26, 12, 0, 0);
            time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            observer = ObserverElement.parseCity(City.findCity("Madrid"));
            //observer.setLongitudeDeg(-3.9872049);
            //observer.setLatitudeDeg(39.0337318);
            SatelliteEphem.USE_IRIDIUM_SATELLITES = true;
            SatelliteEphem.setSatellitesFromExternalFile(null);
            //if (!Configuration.isAcceptableDateForArtificialSatellites(time.astroDate))
            //    Configuration.updateArtificialSatellitesInTempDir(time.astroDate);
            double min_elevation = 0.0, maxDays = 2.0;
            int precision = 5;
            boolean current = true;
            long t0 = System.currentTimeMillis();
            int nmax = SatelliteEphem.getArtificialSatelliteCount();
            eph.correctForEOP = false;

            for (int n = 0; n < nmax; n++) {
                eph.targetBody.setIndex(n);
                SatelliteOrbitalElement soe = SatelliteEphem.getArtificialSatelliteOrbitalElement(n);
                if (soe.getStatus() == SatelliteOrbitalElement.STATUS.FAILED || soe.getStatus() == SatelliteOrbitalElement.STATUS.UNKNOWN)
                    continue;

                ArrayList<Object[]> flares = SDP4_SGP4.getNextIridiumFlares(time, observer, eph,
                        soe, min_elevation, maxDays, current, precision);
                if (flares != null) {
                    for (int i = 0; i < flares.size(); i++) {
                        Object o[] = flares.get(i);
                        SatelliteEphemElement start = (SatelliteEphemElement) o[4];
                        SatelliteEphemElement end = (SatelliteEphemElement) o[5];
                        SatelliteEphemElement max = (SatelliteEphemElement) o[6];
                        String fs = " (" + Functions.formatAngleAsDegrees(start.azimuth, 1) + ", " + Functions.formatAngleAsDegrees(start.elevation, 1) + ", " + Functions.formatValue(start.magnitude, 1) + ")";
                        String fe = " (" + Functions.formatAngleAsDegrees(end.azimuth, 1) + ", " + Functions.formatAngleAsDegrees(end.elevation, 1) + ", " + Functions.formatValue(end.magnitude, 1) + ")";
                        String fm = " (" + Functions.formatAngleAsDegrees(max.azimuth, 1) + ", " + Functions.formatAngleAsDegrees(max.elevation, 1) + ", " + Functions.formatValue(max.magnitude, 1) + ")";
                        if (end.isEclipsed || start.isEclipsed || max.isEclipsed) {
                            System.out.println("*** " + SatelliteEphem.getArtificialSatelliteName(n) + ": " + TimeFormat.formatJulianDayAsDateAndTime((Double) o[0], null) + fs + "/" + TimeFormat.formatJulianDayAsDateAndTime((Double) o[1], null) + fe + "/" + TimeFormat.formatJulianDayAsDateAndTime((Double) o[2], null) + fm + "/" + (Double) o[3]);
                        } else {
                            System.out.println(SatelliteEphem.getArtificialSatelliteName(n) + ": " + TimeFormat.formatJulianDayAsDateAndTime((Double) o[0], null) + fs + "/" + TimeFormat.formatJulianDayAsDateAndTime((Double) o[1], null) + fe + "/" + TimeFormat.formatJulianDayAsDateAndTime((Double) o[2], null) + fm + "/" + (Double) o[3]);
                        }
                    }
                }
            }

            long t1 = System.currentTimeMillis();
            System.out.println("Done in " + (float) ((t1 - t0) / 1000.0) + "s");

            /*
IRIDIUM 5: 26-oct-2011 17:47:33 (32.1, 31.4)/26-oct-2011 17:47:53 (28.8, 27.9)/26-oct-2011 17:47:42 (30.5, 29.8)/0.21889741718769073
IRIDIUM 4: 27-oct-2011 05:49:01 (187.8, 27.5)/27-oct-2011 05:49:26 (187.4, 23.4)/27-oct-2011 05:49:12 (187.6, 25.6)/1.4930144548416138
IRIDIUM 17: 27-oct-2011 10:02:33 (7.2, 77.3)/27-oct-2011 10:02:38 (8.0, 79.9)/27-oct-2011 10:02:35 (7.5, 78.4)/1.6581141948699951
IRIDIUM 31: 26-oct-2011 15:51:18 (78.4, 42.6)/26-oct-2011 15:51:25 (74.9, 41.9)/26-oct-2011 15:51:21 (76.9, 42.3)/1.6713924407958984
IRIDIUM 56: 27-oct-2011 07:59:40 (182.6, 68.3)/27-oct-2011 07:59:49 (183.0, 64.1)/27-oct-2011 07:59:44 (182.8, 66.4)/0.8697885274887085
IRIDIUM 62: 27-oct-2011 10:24:32 (-88.2, 60.2)/27-oct-2011 10:24:43 (-78.3, 58.9)/27-oct-2011 10:24:37 (-83.6, 59.7)/0.4163323938846588
Done in 467.015s

             Test data from http://www.chiandh.me.uk/ephem/iriday.shtml (2011, 10, 26)
              name			start	(hour, azimut 0=N, elevation)		peak			end
              IRIDIUM 31 [+]  14:51:18  258.5�  42.6�  14:51:21  257.0�  42.3�  1.7�  14:51:25  255.0�  41.9�
              IRIDIUM 5 [+]  16:47:33  212.1�  31.4�  16:47:42  210.5�  29.8�  0.2�  16:47:52  209.0�  28.1�
              IRIDIUM 4 [+]  04:49:01   7.8�  27.5�  04:49:12   7.6�  25.6�  1.5�  04:49:25   7.4�  23.5�
              IRIDIUM 56 [+]  06:59:41   2.6�  68.2�  06:59:45   2.7�  66.3�  0.9�  06:59:49   2.9�  64.5�
              IRIDIUM 17 [-]  09:02:33  187.2�  77.2�  09:02:35  187.5�  78.3�  1.7�  09:02:37  187.8�  79.3�
              IRIDIUM 62 [+] 09:24:33   92.4�  60.2�  09:24:38   97.0�  59.7�  0.4�  09:24:43  101.5�  59.0�
             */
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
