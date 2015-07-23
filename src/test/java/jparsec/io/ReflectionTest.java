package jparsec.io;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;

public class ReflectionTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws Exception {
        AstroDate astro = new AstroDate(2013, AstroDate.JULY, 23, 10, 0, 0);
        TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
        CityElement city = City.findCity("New York");
        final ObserverElement observer = ObserverElement.parseCity(city);
        EphemerisElement eph = new EphemerisElement(Target.TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2009,
                EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000, EphemerisElement.ALGORITHM.MOSHIER);
        observer.setTimeZone(0.0);
        observer.setLongitudeDeg(-73.56);
        observer.setLatitudeDeg(40.40);
        observer.setDSTCode(ObserverElement.DST_RULE.NONE);

        TimeElement time2 = new TimeElement();
        ObserverElement observer2 = new ObserverElement();
        EphemerisElement eph2 = new EphemerisElement();

        System.out.println("BEFORE");
        System.out.println("time = time2: " + time.equals(time2));
        System.out.println("observer = observer2: " + observer.equals(observer2));
        System.out.println("eph = eph2: " + eph.equals(eph2));
        System.out.println();

        Reflection.copyFields(time, time2);
        Reflection.copyFields(observer, observer2);
        Reflection.copyFields(eph, eph2);

        System.out.println("AFTER");
        System.out.println("time = time2: " + time.equals(time2));
        System.out.println("observer = observer2: " + observer.equals(observer2));
        System.out.println("eph = eph2: " + eph.equals(eph2));
    }
}
