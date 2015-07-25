package jparsec.time;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.observer.Observatory;
import jparsec.observer.ObservatoryElement;
import jparsec.observer.ObserverElement;
import jparsec.util.JPARSECException;
import jparsec.util.Logger;

public class SideralTimeTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("SiderealTime test");

        try {
            Logger.disableLogging();
            AstroDate astro = new AstroDate(2015, AstroDate.MARCH, 17, 13, 51, 30);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            ObservatoryElement obs = Observatory.findObservatorybyName("Yebes");
            //CityElement city = City.findCity("Madrid");
            //ObserverElement observer = ObserverElement.parseCity(city);
            ObserverElement observer = ObserverElement.parseObservatory(obs);
            EphemerisElement eph = new EphemerisElement(
                Target.TARGET.Moon,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.GEOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.IAU_2006,
                EphemerisElement.FRAME.ICRF,
                EphemerisElement.ALGORITHM.JPL_DE405);

            //eph.optimizeForSpeed();
            double gmst = SiderealTime.greenwichMeanSiderealTime(time, observer, eph);
            double ast = SiderealTime.apparentSiderealTime(time, observer, eph);
            double eceq = SiderealTime.equationOfEquinoxes(time, observer, eph);
            double eqTime = SiderealTime.equationOfTime(time, observer, eph);
            System.out.println("jd " + astro.jd() + " / dst " + TimeScale.getDST(astro.jd(), observer));
            System.out.println("GMST: " + Functions.formatRA(gmst, 8));
            System.out.println("AST: " + Functions.formatRA(ast, 8));
            System.out.println("ECEQ: " + Functions.formatRA(eceq, 8));
            System.out.println("ECTime: " + Functions.formatRAWithNegativeTime(eqTime, 8));
            eph.preferPrecisionInEphemerides = false;
            eceq = SiderealTime.equationOfEquinoxes(time, observer, eph);
            System.out.println("ECEQ: " + Functions.formatRA(eceq, 8));
            JPARSECException.showWarnings();
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
