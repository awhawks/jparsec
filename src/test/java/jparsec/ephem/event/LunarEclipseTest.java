package jparsec.ephem.event;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;

public class LunarEclipseTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("LunarEclipse test");

        try {
            AstroDate astro = new AstroDate(-1000, AstroDate.MARCH, 11, 0, 0, 0);
            //AstroDate astro = new AstroDate(1997, AstroDate.SEPTEMBER, 16, 0, 0, 0);
            // AstroDate astro = new AstroDate(7, AstroDate.september, 2006, 0, 0, 0);
            // AstroDate astro = new AstroDate(14, AstroDate.march, 2006, 0, 0, 0);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            EphemerisElement eph = new EphemerisElement(Target.TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000, EphemerisElement.ALGORITHM.MOSHIER);
            eph.correctForEOP = false;
            eph.correctForPolarMotion = false;
            eph.correctEOPForDiurnalSubdiurnalTides = false;
            eph.preferPrecisionInEphemerides = false;
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            TimeElement.SCALE output = TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME;
            LunarEclipse le = new LunarEclipse(time, observer, eph);
            double jdMax = le.getEclipseMaximum();
            System.out.println("TT-UT1: " + TimeScale.getTTminusUT1(astro));
            System.out.println(le.getEclipseType() + " lunar eclipse on " + TimeFormat
                    .formatJulianDayAsDateAndTime(jdMax, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME) + ". In " + output + ":");

            MoonEventElement[] events = le.getEvents();
            for (int i = 0; i < events.length; i++) {
                if (events[i].startTime != 0.0)
                    System.out.println("From " + TimeFormat.formatJulianDayAsDateAndTime(events[i].getEventTime(observer, eph, true, output), output) + " to " +
                            TimeFormat.formatJulianDayAsDateAndTime(events[i].getEventTime(observer, eph, false, output), output) + " (" + events[i].details + ")");
            }

            // Difference of only 30s at year 1000 B.C. with Spenak results in Five Millennium Canon of Solar/Lunar Eclipses (5:40:48),
            // using ELP2000 fixed. With Moshier 3 minutes, and with ELP2000 not fixed 30 min.
            // Only 30s, but why?

            /*
            ELP2000 Fixed
            TT-UT1: 25314.834625356198
            total lunar eclipse on 14-Mar-1000 05:40:15 (B.C.) TDB. In BARYCENTRIC_DYNAMICAL_TIME:
            From 14-Mar-1000 02:55:24 (B.C.) TDB to 14-Mar-1000 08:25:12 (B.C.) TDB (penumbral)
            From 14-Mar-1000 03:55:27 (B.C.) TDB to 14-Mar-1000 07:25:06 (B.C.) TDB (full penumbral)
            From 14-Mar-1000 03:54:52 (B.C.) TDB to 14-Mar-1000 07:25:39 (B.C.) TDB (partial)
            From 14-Mar-1000 04:58:40 (B.C.) TDB to 14-Mar-1000 06:21:51 (B.C.) TDB (total)

            ELP 2000 Fixed using Meeus expansion: Maximum at 05:38:14, just 2 minutes of error

            Moshier. Since it's based on DE404, it should be more accurate, gives max at 05:37:46
            total lunar eclipse on 14-Mar-1000 05:37:46 (B.C.) TDB. In BARYCENTRIC_DYNAMICAL_TIME:
            From 14-Mar-1000 02:52:55 (B.C.) TDB to 14-Mar-1000 08:22:42 (B.C.) TDB (penumbral)
            From 14-Mar-1000 03:52:59 (B.C.) TDB to 14-Mar-1000 07:22:37 (B.C.) TDB (full penumbral)
            From 14-Mar-1000 03:52:24 (B.C.) TDB to 14-Mar-1000 07:23:10 (B.C.) TDB (partial)
            From 14-Mar-1000 04:56:12 (B.C.) TDB to 14-Mar-1000 06:19:21 (B.C.) TDB (total)
             */

            JPARSECException.showWarnings();
            astro = new AstroDate(-1000, 1, 15);
            //astro = new AstroDate(1997, 9, 16);
            SimpleEventElement see = MainEvents.MoonPhaseOrEclipse(astro.jd(), SimpleEventElement.EVENT.MOON_LUNAR_ECLIPSE, MainEvents.EVENT_TIME.CLOSEST);
            if (see != null) {
                time = new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                astro = new AstroDate(see.getEventTime(observer, eph, output));
                // should be 23:36 LT or 05:38 TDB
                System.out.println("Meeus expansion: lunar eclipse on " + see.time + "/" + astro.getYear() + "/" + astro.getMonth() + "/" + astro.getDay() + "/" + astro.getHour() + ":" + astro.getMinute() + ":" + astro.getRoundedSecond());
            }
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
