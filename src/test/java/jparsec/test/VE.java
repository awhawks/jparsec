package jparsec.test;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.ephem.event.MainEvents;
import jparsec.ephem.event.SimpleEventElement;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;
import jparsec.time.TimeScale;

/**
 * Created by carlo on 17.07.15.
 */
public class VE {
    public static void main(final String args[]) {
        try {
            // Basic time, observer, and ephemeris objects
            CityElement city = City.findCity("Tehran");
            ObserverElement observer = ObserverElement.parseCity(city);
            EphemerisElement eph = new EphemerisElement(Target.TARGET.SUN, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000, EphemerisElement.ALGORITHM.MOSHIER);
            eph.optimizeForSpeed();

            // Get equinox for 2015 in TT and LT
            SimpleEventElement see = MainEvents.EquinoxesAndSolstices(2015, SimpleEventElement.EVENT.SUN_SPRING_EQUINOX, city);
            TimeElement.SCALE outputTimeScale = TimeElement.SCALE.LOCAL_TIME;
            double jdLT = TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.TERRESTRIAL_TIME), observer, eph, outputTimeScale);

            // Get time offset on that date in hours for the observer
            double jdUTC = TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.TERRESTRIAL_TIME), observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            double tzone = observer.getTimeZone() + TimeScale.getDST(jdUTC, observer);
            String timeOffset = Functions.formatValue(tzone, 1);

            if (tzone > 0) {
                timeOffset = "+" + timeOffset;
            }

            // Print
            if (see != null) {
                System.out.println("Vernal equinox for 2015 occurs on " + TimeFormat.formatJulianDayAsDateAndTime(jdLT, outputTimeScale) + " of " + observer.getName() + " (UTC " + timeOffset + ")");
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
