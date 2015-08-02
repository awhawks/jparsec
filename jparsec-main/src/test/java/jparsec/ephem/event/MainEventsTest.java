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
import jparsec.util.Translate;

public class MainEventsTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);

        CityElement city = City.findCity("Madrid");
        ObserverElement observer = ObserverElement.parseCity(city);
        EphemerisElement eph = new EphemerisElement(Target.TARGET.SUN, EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.MOSHIER);
        eph.preferPrecisionInEphemerides = true;
        double jd = new AstroDate().jd();
        SimpleEventElement see = MainEvents.getPlanetaryEvent(Target.TARGET.EARTH, jd, SimpleEventElement.EVENT.PLANET_MAXIMUM_DISTANCE_FROM_SUN, MainEvents.EVENT_TIME.CLOSEST, true);
        AstroDate astro = new AstroDate(see.getEventTime(observer, eph, TimeElement.SCALE.LOCAL_TIME));
        System.out.println(astro.toString());

        astro = new AstroDate(2015, 5, 15);

        /*
        SimpleEventElement s[] = MainEvents.meteorShowers(2010);
        for (int i = 0; i < s.length; i++) {
            TimeElement time = new TimeElement(s[i].time, TimeElement.SCALE.TERRESTRIAL_TIME);
            double lt = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.LOCAL_TIME);
            AstroDate astro2 = new AstroDate(lt);
            String date = "" + astro2.getDay() + " de " +
                    Translate.translate(Gregorian.MONTH_NAMES[astro2.getMonth() - 1]) +
                    " de " + astro2.getYear() + ", " +
                    Functions.formatValue(astro2.getHour(), 0, 2, true) + ':' +
                    Functions.formatValue(astro2.getMinute(), 0, 2, true) + " LT";

            String n = FileIO.getField(1, s[i].details, "|", false) + '|' + date + '|' + FileIO.getRestAfterField(2, s[i].details, "|", false);
            n = DataSet.replaceAll(n, "|", " & ", true) + " \\\\";
            System.out.println(n);
        }
        */

        see = MainEvents.getPlanetaryEvent(Target.TARGET.MERCURY, astro.jd(), SimpleEventElement.EVENT.PLANET_MAXIMUM_DISTANCE, MainEvents.EVENT_TIME.CLOSEST, false);
        if (see != null)
            System.out.println("Mercury max dist on jd " + see.time + '/' + TimeFormat.formatJulianDayAsDateAndTime(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, TimeElement.SCALE.LOCAL_TIME), TimeElement.SCALE.LOCAL_TIME));

        see = MainEvents.getPlanetaryEvent(Target.TARGET.MERCURY, astro.jd(), SimpleEventElement.EVENT.PLANET_MINIMUM_DISTANCE, MainEvents.EVENT_TIME.CLOSEST, false);
        if (see != null)
            System.out.println("Mercury min dist on jd " + see.time + '/' + TimeFormat.formatJulianDayAsDateAndTime(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, TimeElement.SCALE.LOCAL_TIME), TimeElement.SCALE.LOCAL_TIME));

        see = MainEvents.getPlanetaryEvent(Target.TARGET.VENUS, astro.jd(), SimpleEventElement.EVENT.PLANET_MAXIMUM_DISTANCE, MainEvents.EVENT_TIME.CLOSEST, false);
        if (see != null)
            System.out.println("Venus max dist on jd " + see.time + '/' + TimeFormat.formatJulianDayAsDateAndTime(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, TimeElement.SCALE.LOCAL_TIME), TimeElement.SCALE.LOCAL_TIME));

        see = MainEvents.getPlanetaryEvent(Target.TARGET.VENUS, astro.jd(), SimpleEventElement.EVENT.PLANET_MINIMUM_DISTANCE, MainEvents.EVENT_TIME.CLOSEST, false);
        if (see != null)
            System.out.println("Venus min dist on jd " + see.time + '/' + TimeFormat.formatJulianDayAsDateAndTime(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, TimeElement.SCALE.LOCAL_TIME), TimeElement.SCALE.LOCAL_TIME));

        if (see != null) System.exit(0);

        see = MainEvents.getMercuryOrVenusTransit(Target.TARGET.VENUS, astro.jd(), astro.jd() + 2 * 365.25, true); //.MoonPhaseOrEclipse(astro.jd(), SimpleEventElement.MOON_LAST_QUARTER, MainEvents.NEXT_EVENT);
        if (see != null)
            System.out.println("Venus transit on jd " + see.time + '/' + TimeFormat.formatJulianDayAsDateAndTime(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer, eph, TimeElement.SCALE.LOCAL_TIME), TimeElement.SCALE.LOCAL_TIME));
/*
        astro = new AstroDate(-1000, 3, 1);
        see = MainEvents.MoonPhaseOrEclipse(astro.jd(), SimpleEventElement.EVENT.MOON_LUNAR_ECLIPSE, MainEvents.EVENT_TIME.CLOSEST);
        if (see != null) System.out.println("Lunar eclipse jd "+ see.time+'/'+TimeFormat.formatJulianDayAsDateAndTime(see.time) + " TT. Details: "+see.details);

        see = MainEvents.EquinoxesAndSolstices(2011, SimpleEventElement.EVENT.SUN_SUMMER_SOLSTICE);
        if (see != null) System.out.println("2011 Summer solstice on jd "+ see.time+'/'+TimeFormat.formatJulianDayAsDateAndTime(TimeScale.getJD(new TimeElement(see.time, SCALE.TERRESTRIAL_TIME), observer, eph, SCALE.LOCAL_TIME)));

        astro = new AstroDate(2011, 7, 15);
        see = MainEvents.getPlanetaryEvent(TARGET.MARS, astro.jd(), SimpleEventElement.EVENT.PLANET_MAXIMUM_ELONGATION, EVENT_TIME.NEXT);
        if (see != null) System.out.println("Next Mars opposition on jd "+ see.time+'/'+TimeFormat.formatJulianDayAsDateAndTime(TimeScale.getJD(new TimeElement(see.time, SCALE.TERRESTRIAL_TIME), observer, eph, SCALE.LOCAL_TIME)));

        // See http://www.skyandtelescope.com/skytel/beyondthepage/91731334.html
        astro = new AstroDate(2010, 11, 1);
        see = MainEvents.getJupiterGRSNextTransitTime(TimeScale.getJD(new TimeElement(astro.jd(), SCALE.UNIVERSAL_TIME_UT1), observer, eph, SCALE.TERRESTRIAL_TIME));
        if (see != null) System.out.println("Next GRS transit in Jupiter on jd "+ see.time+" TT / "+TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(TimeScale.getJD(new TimeElement(see.time, SCALE.TERRESTRIAL_TIME), observer, eph, SCALE.UNIVERSAL_TIME_UT1)) + " UT1");
*/

        boolean maximumAccuracy = true;
        long t0 = System.currentTimeMillis();
        see = MainEvents.SaturnRingsEdgeOn(astro.jd(), MainEvents.EVENT_TIME.NEXT);
        if (see != null)
            System.out.println("Next time Saturn rings in edge-on view on jd " + see.time + " TT / " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.TERRESTRIAL_TIME), observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1), TimeElement.SCALE.UNIVERSAL_TIME_UT1) + ". Details: " + see.details);
        long t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");

        t0 = System.currentTimeMillis();
        see = MainEvents.SaturnRingsMaximumAperture(astro.jd(), MainEvents.EVENT_TIME.NEXT);
        if (see != null)
            System.out.println("Next time Saturn rings in maximum aperture on jd " + see.time + " TT / " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.TERRESTRIAL_TIME), observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1), TimeElement.SCALE.UNIVERSAL_TIME_UT1) + ". Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");

        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.MARS, astro.jd(), SimpleEventElement.EVENT.PLANET_MAXIMUM_ELONGATION, MainEvents.EVENT_TIME.NEXT, maximumAccuracy);
        if (see != null)
            System.out.println("Next Mars oposition on jd " + see.time + " TT / " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.TERRESTRIAL_TIME), observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1), TimeElement.SCALE.UNIVERSAL_TIME_UT1) + ". Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");

        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.MARS, astro.jd(), SimpleEventElement.EVENT.PLANET_MINIMUM_DISTANCE_FROM_SUN, MainEvents.EVENT_TIME.NEXT, maximumAccuracy);
        if (see != null)
            System.out.println("Next Mars perihelion on jd " + see.time + " TT / " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.TERRESTRIAL_TIME), observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1), TimeElement.SCALE.UNIVERSAL_TIME_UT1) + ". Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");

        see = MainEvents.PerihelionAndAphelion(Target.TARGET.MARS, astro.jd(), SimpleEventElement.EVENT.PLANET_MINIMUM_DISTANCE_FROM_SUN, MainEvents.EVENT_TIME.CLOSEST);
        if (see != null)
            System.out.println("Closest Mars perihelion on jd " + see.time + " TT / " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(TimeScale.getJD(new TimeElement(see.time, TimeElement.SCALE.TERRESTRIAL_TIME), observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1), TimeElement.SCALE.UNIVERSAL_TIME_UT1) + " (following Meeus). Details: " + see.details);

        astro = new AstroDate(1993, 11, 1);
        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.MERCURY, astro.jd(), SimpleEventElement.EVENT.PLANET_MINIMUM_ELONGATION, MainEvents.EVENT_TIME.CLOSEST, maximumAccuracy);
        if (see != null)
            System.out.println(see.body + " conjunction on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (JPARSEC). Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");
        see = MainEvents.Conjunction(Target.TARGET.MERCURY, astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
        if (see != null)
            System.out.println(see.body + " conjunction on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (following Meeus). Details: " + see.details);

        astro = new AstroDate(2125, 6, 1);
        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.SATURN, astro.jd(), SimpleEventElement.EVENT.PLANET_MINIMUM_ELONGATION, MainEvents.EVENT_TIME.CLOSEST, maximumAccuracy);
        if (see != null)
            System.out.println(see.body + " conjunction on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (JPARSEC). Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");
        see = MainEvents.Conjunction(Target.TARGET.SATURN, astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
        if (see != null)
            System.out.println(see.body + " conjunction on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (following Meeus). Details: " + see.details);

        astro = new AstroDate(1993, 11, 15);
        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.MERCURY, astro.jd(), SimpleEventElement.EVENT.PLANET_MAXIMUM_ELONGATION, MainEvents.EVENT_TIME.CLOSEST, maximumAccuracy);
        if (see != null)
            System.out.println(see.body + " max. elongation on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (JPARSEC). Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");
        see = MainEvents.OppositionOrMaxElongation(Target.TARGET.MERCURY, astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
        if (see != null)
            System.out.println(see.body + " max. elongation on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (following Meeus). Details: " + see.details);

        astro = new AstroDate(1631, 11, 1);
        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.MERCURY, astro.jd(), SimpleEventElement.EVENT.PLANET_MINIMUM_ELONGATION, MainEvents.EVENT_TIME.CLOSEST, maximumAccuracy);
        if (see != null)
            System.out.println(see.body + " conjunction on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (JPARSEC). Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");
        see = MainEvents.Conjunction(Target.TARGET.MERCURY, astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
        if (see != null)
            System.out.println(see.body + " conjunction on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (following Meeus). Details: " + see.details);

        astro = new AstroDate(1882, 12, 1);
        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.VENUS, astro.jd(), SimpleEventElement.EVENT.PLANET_MINIMUM_ELONGATION, MainEvents.EVENT_TIME.CLOSEST, maximumAccuracy);
        if (see != null)
            System.out.println(see.body + " conjunction on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (JPARSEC). Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");
        see = MainEvents.Conjunction(Target.TARGET.VENUS, astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
        if (see != null)
            System.out.println(see.body + " conjunction on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (following Meeus). Details: " + see.details);

        astro = new AstroDate(2729, 9, 15);
        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.MARS, astro.jd(), SimpleEventElement.EVENT.PLANET_MAXIMUM_ELONGATION, MainEvents.EVENT_TIME.CLOSEST, maximumAccuracy);
        if (see != null)
            System.out.println(see.body + " opposition on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (JPARSEC). Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");
        see = MainEvents.OppositionOrMaxElongation(Target.TARGET.MARS, astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
        if (see != null)
            System.out.println(see.body + " opposition on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (following Meeus). Details: " + see.details);

        astro = new AstroDate(-7, 9, 1);
        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.JUPITER, astro.jd(), SimpleEventElement.EVENT.PLANET_MAXIMUM_ELONGATION, MainEvents.EVENT_TIME.CLOSEST, maximumAccuracy);
        if (see != null)
            System.out.println(see.body + " opposition on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (JPARSEC). Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");
        see = MainEvents.OppositionOrMaxElongation(Target.TARGET.JUPITER, astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
        if (see != null)
            System.out.println(see.body + " opposition on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (following Meeus). Details: " + see.details);

        astro = new AstroDate(-7, 9, 1);
        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.SATURN, astro.jd(), SimpleEventElement.EVENT.PLANET_MAXIMUM_ELONGATION, MainEvents.EVENT_TIME.CLOSEST, maximumAccuracy);
        if (see != null)
            System.out.println(see.body + " opposition on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (JPARSEC). Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");
        see = MainEvents.OppositionOrMaxElongation(Target.TARGET.SATURN, astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
        if (see != null)
            System.out.println(see.body + " opposition on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (following Meeus). Details: " + see.details);

        astro = new AstroDate(1780, 12, 15);
        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.URANUS, astro.jd(), SimpleEventElement.EVENT.PLANET_MAXIMUM_ELONGATION, MainEvents.EVENT_TIME.CLOSEST, maximumAccuracy);
        if (see != null)
            System.out.println(see.body + " opposition on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (JPARSEC). Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");
        see = MainEvents.OppositionOrMaxElongation(Target.TARGET.URANUS, astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
        if (see != null)
            System.out.println(see.body + " opposition on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (following Meeus). Details: " + see.details);

        astro = new AstroDate(1846, 8, 15);
        t0 = System.currentTimeMillis();
        see = MainEvents.getPlanetaryEvent(Target.TARGET.NEPTUNE, astro.jd(), SimpleEventElement.EVENT.PLANET_MAXIMUM_ELONGATION, MainEvents.EVENT_TIME.CLOSEST, maximumAccuracy);
        if (see != null)
            System.out.println(see.body + " opposition on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (JPARSEC). Details: " + see.details);
        t1 = System.currentTimeMillis();
        System.out.println("Calculated in " + (float) ((t1 - t0) / 1000.0) + " seconds");
        see = MainEvents.OppositionOrMaxElongation(Target.TARGET.NEPTUNE, astro.jd(), MainEvents.EVENT_TIME.CLOSEST);
        if (see != null)
            System.out.println(see.body + " opposition on jd " + see.time + " = " + TimeFormat.formatJulianDayAsDateAndTimeOnlyMinutes(see.time, TimeElement.SCALE.TERRESTRIAL_TIME) + " (following Meeus). Details: " + see.details);
    }
}
