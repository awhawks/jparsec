package jparsec.ephem.event;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ExtraterrestrialObserverElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;

public class SolarEclipseTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("SolarEclipse test");

        /*
         * 3-10-05, Madrid: 7:40:12, 8:55:55, 8:57:58, 9:00:02, 10:23:36 (ROA)
         * 3-10-05, Madrid: 7:40:11.7, 8:55:53.7, 8:57:59.1, 9:00:04.4, 10:23:38.3 (Spenak) Madrid at 40°24'N  003°41'W   667
         * 3-10-05, Madrid: 7:40:12.7, 8:55:54.5, 8:57:59.0, 9:00:03.5, 10:23:36.8 (JPARSEC) Default Madrid city, Moshier algorithms, 0.1s precision
         * 3-10-05, Madrid: 7:40:12.8, 8:55:54.9, 8:57:59.3, 9:00:03.7, 10:23:37.6 (JPARSEC) same Madrid city as Spenak, Moshier algorithms, 0.1s precision
         * 3-10-05, Madrid: 7:40:12.9, 8:55:55.1, 8:57:59.4, 9:00:03.8, 10:23:37.7 (JPARSEC) same Madrid city as Spenak, DE405, 0.1s precision
         */

        try {
            AstroDate astro = new AstroDate(2005, AstroDate.OCTOBER, 3, 0, 0, 0);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            EphemerisElement eph = new EphemerisElement(Target.TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000, EphemerisElement.ALGORITHM.MOSHIER);
            eph.correctForEOP = false;
            eph.correctForPolarMotion = false;
            eph.correctEOPForDiurnalSubdiurnalTides = false;
            eph.preferPrecisionInEphemerides = false;
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            // For same location as Spenak's Madrid
            observer.setLongitudeDeg(-3.68333);
            observer.setLatitudeDeg(40.4);
            observer.setHeight(667, true);
            SolarEclipse se = new SolarEclipse(time, observer, eph);

            double jdMax = se.getEclipseMaximum();
            TimeElement t_max = new TimeElement(jdMax, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            double jdUT_max = TimeScale.getJD(t_max, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);

            System.out.println(se.getEclipseType() + " solar eclipse on " + TimeFormat
                    .formatJulianDayAsDateAndTime(jdUT_max, TimeElement.SCALE.UNIVERSAL_TIME_UT1) + ". In UT1:");
            MoonEventElement[] events = se.getEvents();

            for (int i = 0; i < events.length; i++) {
                if (events[i].startTime != 0.0) {
                    TimeElement t_init = new TimeElement(events[i].startTime, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                    double jdUT_init = TimeScale.getJD(t_init, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
                    TimeElement t_end = new TimeElement(events[i].endTime, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                    double jdUT_end = TimeScale.getJD(t_end, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);

                    System.out.println("From " + TimeFormat.formatJulianDayAsDateAndTime(jdUT_init, TimeElement.SCALE.UNIVERSAL_TIME_UT1) + " to " +
                            TimeFormat.formatJulianDayAsDateAndTime(jdUT_end, TimeElement.SCALE.UNIVERSAL_TIME_UT1) + " (" + events[i].details + ")");

                    // Show decimals in seconds
                    // AstroDate ini = new AstroDate(jdUT_init);
                    // AstroDate end = new AstroDate(jdUT_end);
                    // AstroDate max = new AstroDate(jdUT_max);
                    // System.out.println(ini.getSeconds() + '/' + end.getSeconds() + '/' + max.getSeconds());
                }
            }

            // Partial occultation of Sun by Phobos on Mars
            System.out.println("");
            System.out.println("Testing the partial solar occultation by Phobos on Mars, from Curiosity landing site");
            astro = new AstroDate(2012, 9, 13, 5, 15, 0);
            time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            observer = ObserverElement.parseExtraterrestrialObserver(new ExtraterrestrialObserverElement("Mars (Gale crater)", Target.TARGET.MARS,
                    new LocationElement(-137.4417 * Constant.DEG_TO_RAD, -4.5895 * Constant.DEG_TO_RAD, 1.0)));
            eph.targetBody = Target.TARGET.Phobos;
            se = new SolarEclipse(time, observer, eph, 0.2);
            jdMax = se.getEclipseMaximum();
            t_max = new TimeElement(jdMax, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            jdUT_max = TimeScale.getJD(t_max, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);

            System.out.println(se.getEclipseType() + " solar eclipse on " + TimeFormat
                    .formatJulianDayAsDateAndTime(jdUT_max, TimeElement.SCALE.UNIVERSAL_TIME_UT1) + ". In UT1:");
            events = se.getEvents();
            for (int i = 0; i < events.length; i++) {
                if (events[i].startTime != 0.0) {
                    TimeElement t_init = new TimeElement(events[i].startTime, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                    double jdUT_init = TimeScale.getJD(t_init, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
                    TimeElement t_end = new TimeElement(events[i].endTime, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                    double jdUT_end = TimeScale.getJD(t_end, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);

                    System.out.println("From " + TimeFormat.formatJulianDayAsDateAndTime(jdUT_init, TimeElement.SCALE.UNIVERSAL_TIME_UT1) + " to " +
                            TimeFormat.formatJulianDayAsDateAndTime(jdUT_end, TimeElement.SCALE.UNIVERSAL_TIME_UT1) + " (" + events[i].details + ")");

                    // Show decimals in seconds
                    AstroDate ini = new AstroDate(jdUT_init);
                    AstroDate end = new AstroDate(jdUT_end);
                    AstroDate max = new AstroDate(jdUT_max);
                    System.out.println(ini.getSeconds() + "/" + end.getSeconds() + "/" + max.getSeconds());
                }
            }

            JPARSECException.showWarnings();
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
