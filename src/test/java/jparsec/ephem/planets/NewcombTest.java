package jparsec.ephem.planets;

import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;

public class NewcombTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Newcomb test");

        try {
            AstroDate astro = new AstroDate(Constant.B1950); // 1, AstroDate.january, 2006);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            EphemerisElement eph = new EphemerisElement(
                Target.TARGET.SUN,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.TOPOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.WILLIAMS_1994,
                EphemerisElement.FRAME.ICRF);

            EphemElement ephem = Ephem.getEphemeris(time, observer, eph, false, true);
            String name = eph.targetBody.getName();
            System.out.println("" + name + " RA: " + Functions.formatRA(ephem.rightAscension));
            System.out.println("" + name + " DEC: " + Functions.formatDEC(ephem.declination));
            System.out.println("" + name + " dist: " + ephem.distance);

            ephem = Newcomb.newcombSunEphemeris(time, observer, eph);
            System.out.println("NEWCOMB");

            double JD = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            System.out.println("lon " + observer.getLongitudeDeg());
            System.out.println("lat " + observer.getLatitudeDeg());
            System.out.println("alt " + observer.getHeight());
            System.out.println("JD " + JD);
            System.out.println("" + name + " RA: " + Functions.formatRA(ephem.rightAscension));
            System.out.println("" + name + " DEC: " + Functions.formatDEC(ephem.declination));
            System.out.println("" + name + " dist: " + ephem.distance);
            System.out.println("" + name + " ecl.lon: " + ephem.heliocentricEclipticLongitude * Constant.RAD_TO_DEG);
            System.out.println("" + name + " ecl.lat: " + ephem.heliocentricEclipticLatitude * Constant.RAD_TO_DEG);
            System.out.println("" + name + " ecl.r: " + ephem.distanceFromSun);
            System.out.println("" + name + " azi: " + ephem.azimuth * Constant.RAD_TO_DEG);
            System.out.println("" + name + " alt: " + ephem.elevation * Constant.RAD_TO_DEG);
            System.out.println("" + name + " ang. rad: " + ephem.angularRadius * Constant.RAD_TO_ARCSEC);
            System.out.println("" + name + " mag: " + ephem.magnitude);
            System.out.println("" + name + " axis: " + ephem.positionAngleOfAxis * Constant.RAD_TO_DEG);
            System.out.println("" + name + " pole: " + ephem.positionAngleOfPole * Constant.RAD_TO_DEG);
            System.out.println("" + name + " meridian: " + ephem.longitudeOfCentralMeridian * Constant.RAD_TO_DEG);

            JPARSECException.showWarnings();
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
