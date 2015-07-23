package jparsec.time;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.Observatory;
import jparsec.observer.ObservatoryElement;
import jparsec.observer.ObserverElement;
import jparsec.util.JPARSECException;

public class SideralTimeTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Ephem Test");

        try {
            AstroDate astro = new AstroDate(2015, AstroDate.MARCH, 17, 13, 51, 30);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            CityElement city = City.findCity("Madrid");
            ObservatoryElement obs = Observatory.findObservatorybyName("Yebes");
            //ObserverElement observer = ObserverElement.parseCity(city);
            ObserverElement observer = ObserverElement.parseObservatory(obs);
            EphemerisElement eph = new EphemerisElement(Target.TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.JPL_DE405);

            eph.correctForEOP = false;

            double gmst = SiderealTime.greenwichMeanSiderealTime(time, observer, eph);
            double eceq = SiderealTime.equationOfEquinoxes(time, observer, eph);
            double ast = SiderealTime.apparentSiderealTime(time, observer, eph);

            System.out.println("jd " + astro.jd() + " / dst " + TimeScale.getDST(astro.jd(), observer));
            System.out.println("GMST: " + Functions.formatRA(gmst, 8));
            System.out.println("AST: " + Functions.formatRA(ast, 8));
            System.out.println("ECEQ: " + Functions.formatRA(eceq, 8));

            double eqTime = SiderealTime.equationOfTime(time, observer, eph);
            System.out.println(Functions.formatRAWithNegativeTime(eqTime, 8));
            JPARSECException.showWarnings();
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
