package jparsec.ephem.event;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.io.FileIO;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;
import jparsec.util.Translate;

public class MoonEventTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("MoonEvent test - mutual phenomena");

        try {
            Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);

            // Test on Jupiter
            AstroDate astroi = new AstroDate(2011, AstroDate.OCTOBER, 2);
            Target.TARGET t = Target.TARGET.JUPITER;

            // Test for Charon
            astroi = new AstroDate(1987, AstroDate.MARCH, 12, 20, 0, 0);
            t = Target.TARGET.Pluto;

            AstroDate astrof = new AstroDate(astroi.jd() + 1.0);

            TimeElement timei = new TimeElement(astroi, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            TimeElement timef = new TimeElement(astrof, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            EphemerisElement eph = new EphemerisElement(t, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_1976,
                    EphemerisElement.FRAME.ICRF);
            eph.algorithm = EphemerisElement.ALGORITHM.JPL_DE406;

            long t0 = System.currentTimeMillis();
            MoonEvent me = new MoonEvent(timei, observer, eph, timef, 30, 10, true);
            MoonEventElement ev[] = me.getMutualPhenomena(false);
            System.out.println("FOUND EVENTS");
            for (int i = 0; i < ev.length; i++) {
                AstroDate astroI = new AstroDate(ev[i].startTime);
                TimeElement timeI = new TimeElement(astroI, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                double jdI = TimeScale.getJD(timeI, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                AstroDate astroI2 = new AstroDate(jdI);
                String eDateI = astroI2.getYear() + "-" + astroI2.getMonth() + "-" + astroI2.getDay();
                String eTimeI = astroI2.getHour() + ":" + astroI2.getMinute() + ":" + astroI2.getRoundedSecond();

                AstroDate astroF = new AstroDate(ev[i].endTime);
                TimeElement timeF = new TimeElement(astroF, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                double jdF = TimeScale.getJD(timeF, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                AstroDate astro2F = new AstroDate(jdF);
                String eTimeF = astro2F.getHour() + ":" + astro2F.getMinute() + ":" + astro2F.getRoundedSecond();

                String visible = "YES";
                if ((ev[i].elevation * Constant.RAD_TO_DEG) < 20) visible = "DIFFICULT";
                if ((ev[i].elevation * Constant.RAD_TO_DEG) < 5) visible = "NO";
                if (!ev[i].visibleFromEarth) visible = "NO*";
                AstroDate astroMax = new AstroDate(Double.parseDouble(FileIO.getField(2, ev[i].details, ",", true)));
                String eTimeMax = astroMax.getHour() + ":" + astroMax.getMinute() + ":" + astroMax.getRoundedSecond();
                String details = eTimeMax + ", ";
                details += Functions.formatValue(Double.parseDouble(FileIO.getField(1, ev[i].details, ",", true)), 1) + "%";
                System.out.println(ev[i].mainBody.getName() + " & " + Translate.translate(MoonEventElement.EVENTS[ev[i].eventType.ordinal()]) + " & " + ev[i].secondaryBody.getName() + " & " + eDateI + " & " + eTimeI + " & " + eTimeF + " & " + details + " & " + visible);
                //CreateChart ch = MoonEvent.lightCurve(ev[i], SCALE.UNIVERSAL_TIME_UTC, true);
                //ch.showChartInJFreeChartPanel();
            }

            JPARSECException.showWarnings();
            long t1 = System.currentTimeMillis();
            System.out.println("Done in " + (float) ((t1 - t0) / 1000.0) + " seconds.");
            // DE406 is 2 times faster than Moshier

            System.out.println();
            System.out.println("MoonEvent test - phenomena");

            t0 = System.currentTimeMillis();
            me = new MoonEvent(timei, observer, eph, timef, 200, 30, true);
            ev = me.getPhenomena();
            System.out.println("FOUND EVENTS");
            for (int i = 0; i < ev.length; i++) {
                AstroDate astroI = new AstroDate(ev[i].startTime);
                TimeElement timeI = new TimeElement(astroI, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                double jdI = TimeScale.getJD(timeI, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                AstroDate astroI2 = new AstroDate(jdI);
                String eDateI = TimeFormat.formatJulianDayAsDate(jdI); //astroI2.getYear()+"-"+astroI2.getMonth()+"-"+astroI2.getDay();
                String eTimeI = astroI2.getHour() + ":" + astroI2.getMinute() + ":" + astroI2.getRoundedSecond();

                AstroDate astroF = new AstroDate(ev[i].endTime);
                TimeElement timeF = new TimeElement(astroF, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                double jdF = TimeScale.getJD(timeF, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
                AstroDate astro2F = new AstroDate(jdF);
                String eDateF = astro2F.getYear() + "-" + astro2F.getMonth() + "-" + astro2F.getDay();
                String eTimeF = astro2F.getHour() + ":" + astro2F.getMinute() + ":" + astro2F.getRoundedSecond();

                if (ev[i].startTime <= 0) {
                    eTimeI = "-";
                    eDateI = eDateF;
                }

                String visible = "YES";
                if ((ev[i].elevation * Constant.RAD_TO_DEG) < 20) visible = "DIFFICULT";
                if ((ev[i].elevation * Constant.RAD_TO_DEG) < 5) visible = "NO";
                if (!ev[i].visibleFromEarth) visible = "NO*";
                System.out.println(ev[i].mainBody.getName() + " & " + Translate.translate(MoonEventElement.EVENTS[ev[i].eventType.ordinal()]) + " & " + ev[i].secondaryBody.getName() + " & " + eDateI + " & " + eTimeI + " & " + eTimeF + " & " + ev[i].details + " & " + visible);

                if (visible.equals("YES"))
                    System.out.println(ev[i].mainBody.getName() + " & " + Translate.translate(MoonEventElement.EVENTS[ev[i].eventType.ordinal()]) + " & " + eDateI + " & " + eTimeI + " & " + eTimeF + " & " + ev[i].details);

                //CreateChart ch = MoonEvent.lightCurve(ev[i], SCALE.UNIVERSAL_TIME_UTC, true);
                //ch.showChartInJFreeChartPanel();
            }

            //CreateChart ch = me.getPathChart(1.0 / 24.0, true, true);
            //ch.showChartInJFreeChartPanel();
            JPARSECException.showWarnings();
            t1 = System.currentTimeMillis();
            System.out.println("Done in " + (float) ((t1 - t0) / 1000.0) + " seconds.");
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
