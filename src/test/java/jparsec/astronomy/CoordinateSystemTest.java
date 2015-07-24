package jparsec.astronomy;

import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.ephem.planets.EphemElement;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;

public class CoordinateSystemTest {
    /**
     * For unit testing only.
     * @param args Not used.
     */
    public static void main(String[] args) {
        System.out.println("CoordinateSystem test");

        try {
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            EphemerisElement eph = new EphemerisElement(
                    Target.TARGET.NOT_A_PLANET,
                    EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE,
                    EphemerisElement.TOPOCENTRIC,
                    EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.FK5);

            LocationElement loc = new LocationElement(0, 0, 1);
            AstroDate astrof = new AstroDate(2000, AstroDate.JANUARY, 1, 12, 0, 0);
            TimeElement timef = new TimeElement(astrof, TimeElement.SCALE.TERRESTRIAL_TIME);
            LocationElement loc_out = CoordinateSystem.galacticToEquatorial(loc, timef, observer, eph);
            System.out.println("RA:  " + Functions.formatRA(loc_out.getLongitude()));
            System.out.println("DEC: " + Functions.formatDEC(loc_out.getLatitude()));
            // Should be 17 45 37.19  -28 56 10.22
            loc_out = CoordinateSystem.equatorialToGalactic(loc_out, timef, observer, eph);
            System.out.println("l:   " + Functions.formatDEC(loc_out.getLongitude()));
            System.out.println("b:   " + Functions.formatDEC(loc_out.getLatitude()));
            // Should be 0 0

            // For more information see
            // Jia-Cheng Liu et al. 2010, http://arxiv.org/abs/1010.3773.
            // http://cxc.harvard.edu/ciao/ahelp/prop-coords.html and
            // http://bado-shanai.net/astrogation/astrogGalacticCoord.htm

            // CHECK FOR CONSISTENCY STARTING FROM B1950
            // Galactic center at B1950
            loc = new LocationElement(Functions.parseRightAscension("17 42 26.58"),
                    Functions.parseDeclination("-28 55 00.43"), 1);
            // We can transform this loc to FK5 J2000 ...
            //loc = LocationElement.parseRectangularCoordinates(Precession.FK4_B1950ToFK5_J2000(loc.getRectangularCoordinates()));
            // or let the function do that automatically
            // (providing the correct frame and equinox for that location)
            eph.frame = EphemerisElement.FRAME.FK4;
            eph.equinox = Constant.B1950;

            loc_out = CoordinateSystem.equatorialToGalactic(loc, timef, observer, eph);
            System.out.println("l:   " + Functions.formatDEC(loc_out.getLongitude()));
            System.out.println("b:  " + Functions.formatDEC(loc_out.getLatitude()));
            // Very nice since input position has an uncertainty above 20 mas.

/*			loc_out = CoordinateSystem.galacticToEquatorial(loc_out, timef, observer, eph);
			System.out.println("RA:  " + Functions.formatRA(loc_out.getLongitude()));
			System.out.println("DEC: " + Functions.formatDEC(loc_out.getLatitude()));
*/

/*			double obliquity = Obliquity.trueObliquity(Functions.toCenturies(Constant.J2000+12.5*365.25), eph.ephemMethod);
			System.out.println(obliquity*Constant.RAD_TO_DEG);
			loc_out = CoordinateSystem.eclipticToEquatorial(new LocationElement(
					28.02 * Constant.DEG_TO_RAD, 33.35 * Constant.DEG_TO_RAD, 1.0
					), obliquity, true);
			System.out.println("RA:  " + Functions.formatRA(loc_out.getLongitude()));
			System.out.println("DEC: " + Functions.formatDEC(loc_out.getLatitude()));

			loc_out = CoordinateSystem.equatorialToEcliptic(loc_out, obliquity, true);
			System.out.println("lon: " + Functions.formatAngleAsDegrees(loc_out.getLongitude(), 2));
			System.out.println("lat: " + Functions.formatAngleAsDegrees(loc_out.getLatitude(), 2));
*/

            eph.frame = EphemerisElement.FRAME.FK4;
            eph.equinox = EphemerisElement.EQUINOX_OF_DATE;
            TimeElement time = new TimeElement();
            eph.targetBody = Target.TARGET.JUPITER;
            EphemElement ephem1 = Ephem.getEphemeris(time, observer, eph, false);
            LocationElement loc1 = CoordinateSystem.equatorialToGalactic(ephem1.getEquatorialLocation(), time, observer, eph);
            System.out.println("Jupiter, equ: "+ephem1.getEquatorialLocation().toStringAsEquatorialLocation());
            System.out.println("Jupiter, gal: "+loc1.toString());
            System.out.println("Jupiter, equ: "+CoordinateSystem.galacticToEquatorial(loc1, time, observer, eph).toStringAsEquatorialLocation());
            eph.equinox = Constant.J2000;
            EphemElement ephem2 = Ephem.getEphemeris(time, observer, eph, false);
            LocationElement loc2 = CoordinateSystem.equatorialToGalactic(ephem2.getEquatorialLocation(), time, observer, eph);
            System.out.println("Jupiter, equ: "+ephem2.getEquatorialLocation().toStringAsEquatorialLocation());
            System.out.println("Jupiter, gal: "+loc2.toString());
            System.out.println("Jupiter, equ: "+CoordinateSystem.galacticToEquatorial(loc2, time, observer, eph).toStringAsEquatorialLocation());
        } catch (JPARSECException ve)
        {
            JPARSECException.showException(ve);
        }
    }
}
