package jparsec.ephem.event;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;

public class LunarEventTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("LunarEvent test");

        try {
            AstroDate astro = new AstroDate(1992, AstroDate.APRIL, 12, 0, 0, 0);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);
            String city = "Madrid";
            ObserverElement obs = new ObserverElement(new CityElement(city));
            EphemerisElement eph = new EphemerisElement(Target.TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT, EphemerisElement.EQUINOX_OF_DATE,
                    EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_1976, EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.MOSHIER);

            eph.correctForEOP = false;

            System.out.println("Lunar librations for jd = " + astro.jd());
            double r[] = LunarEvent.getEckhardtMoonLibrations(time, obs, eph);
            System.out.println("Using Eckhardt's theory");
            System.out.println("l = " + Functions.formatAngleAsDegrees(r[0], 2) + ", b = " + Functions.formatAngleAsDegrees(r[1], 2) + ", p = " + Functions.formatAngleAsDegrees(r[2], 2));
            // Meeus (Eckhardt): l = -1.23, b = 4.20, p = 15.08 (geocentric)
            // Meeus (Eckhardt): l = -1.74, b = 4.90, p = 14.88 (topocentric)
            // IAU:   l = -1.33, b = 4.17, p = 15.086 (geocentric)
            // IAU:   l = -1.84, b = 4.87, p = 14.88 (Madrid)
            // IAU has better b!

            //double jd = 2455713.5;
            //time = new TimeElement(jd, SCALE.TERRESTRIAL_TIME);
            System.out.println("Now using " + eph.algorithm.toString() + " algorithm");
            double angles[] = LunarEvent.getJPLMoonLibrations(time, obs, eph);
            System.out.println("l = " + Functions.formatAngleAsDegrees(angles[0], 5)); // -4.067 (for 2455713.5)
            System.out.println("b = " + Functions.formatAngleAsDegrees(angles[1], 5)); // -2.765
            System.out.println("p = " + Functions.formatAngleAsDegrees(angles[2], 5)); // -13.800
            astro = new AstroDate(2004, 1, 1);
            SimpleEventElement s = LunarEvent.getPerigee(astro.jd(), MainEvents.EVENT_TIME.NEXT);
            System.out.println(s.toString()); // 19:26 and 362 767 km, according to http://www.saao.ac.za/public-info/sun-moon-stars/moon-index/lunar-perigee-apogee-2004-2020/
            s = LunarEvent.getApogee(astro.jd(), MainEvents.EVENT_TIME.NEXT);
            System.out.println(s.toString()); // 20:20 and 405 706 km, according to http://www.saao.ac.za/public-info/sun-moon-stars/moon-index/lunar-perigee-apogee-2004-2020/
            astro = new AstroDate(1988, 10, 1);
            s = LunarEvent.getApogee(astro.jd(), MainEvents.EVENT_TIME.NEXT);
            System.out.println(s.toString()); // 20:29 TD according to Meeus (I obtain 20:30), parallax = 54' 0.679
            astro = new AstroDate(2052, 12, 1);
            s = LunarEvent.getPerigee(astro.jd(), MainEvents.EVENT_TIME.NEXT);
            System.out.println(s.toString()); // Distance should be 356 421 according to Meeus, but I obtain 356 321. An obvious typo error.
            astro = new AstroDate(2424, 12, 15);
            s = LunarEvent.getApogee(astro.jd(), MainEvents.EVENT_TIME.NEXT);
            System.out.println(s.toString()); // Distance = 406 712

            s = LunarEvent.getPassThroughAscendingNode((new AstroDate(1987, 5, 15)).jd(), MainEvents.EVENT_TIME.CLOSEST);
            System.out.println(s.toString());

            astro = new AstroDate(1988, 12, 21);
            s = LunarEvent.MoonMaximumDeclination(astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
            System.out.println(s.toString());
            // Meeus: 22 dic 1988 20h 02m, 28.1562

            astro = new AstroDate(2049, 4, 15);
            s = LunarEvent.MoonMinimumDeclination(astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
            System.out.println(s.toString());
            // Meeus: 21 apr 2049 14h, 22.1384

            astro = new AstroDate(-5, 3, 10);
            s = LunarEvent.MoonMaximumDeclination(astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
            System.out.println(s.toString());
            // Meeus: 16 mar -5 15h, 28.9739
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
