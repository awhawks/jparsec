package jparsec.ephem.planets;

import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;

public class PlanetEphemTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("PlanetEphem Test");

        try {
            AstroDate astro = new AstroDate(1990, AstroDate.JANUARY, 3, 12, 7, 38.002);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);
            CityElement city = City.findCity("Madrid");
            EphemerisElement eph = new EphemerisElement(
                    Target.TARGET.JUPITER,
                    EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE,
                    EphemerisElement.TOPOCENTRIC,
                    EphemerisElement.REDUCTION_METHOD.IAU_1976, // Same results as those given by Horizons
                    EphemerisElement.FRAME.ICRF);
            eph.algorithm = EphemerisElement.ALGORITHM.MOSHIER; //.JPL_DE405;
            ObserverElement observer = ObserverElement.parseCity(city);
            //ObserverElement observer = ObserverElement.parseObservatory(observatory);

            eph.correctForEOP = false;
            eph.correctForPolarMotion = false;

            EphemElement ephem = PlanetEphem.MoshierEphemeris(time, observer, eph);
            System.out.println(ephem.getEquatorialLocation().toStringAsEquatorialLocation());

            LocationElement ephem_loc0 = Ephem.toMeanEquatorial(ephem.getEquatorialLocation(), time, observer, eph);
            System.out.println("Mean: " + Functions.formatRA(ephem_loc0.getLongitude()) + "/" + Functions.formatDEC(ephem_loc0.getLatitude()) + "/" + ephem_loc0.getRadius());
            // Should be: RA: 06h 21m 14.0671s/23� 14' 18.920"/4.175983896906823. Exact is: RA: 06h 21m 14.0665s, DEC: 23� 14' 18.920", DIST: 4.175983897448329

            astro = new AstroDate(2011, AstroDate.JUNE, 15, 21, 21, 0);
            time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            eph = new EphemerisElement(Target.TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC,
                    EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000);
            eph.algorithm = EphemerisElement.ALGORITHM.MOSHIER;

            observer = ObserverElement.parseCity(city);
            ephem = Ephem.getEphemeris(time, observer, eph, false);
            System.out.println(ephem.getEquatorialLocation().toStringAsEquatorialLocation());

            city = City.findCity("Kabul");
            observer = ObserverElement.parseCity(city);
            ephem = Ephem.getEphemeris(time, observer, eph, false);
            System.out.println(ephem.getEquatorialLocation().toStringAsEquatorialLocation());

            city = City.findCity("Madrid");
            observer = ObserverElement.parseCity(city);
            ephem = Ephem.getEphemeris(time, observer, eph, false);
            System.out.println(ephem.getEquatorialLocation().toStringAsEquatorialLocation());
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
