package jparsec.astronomy;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.math.Constant;
import jparsec.observer.LocationElement;
import jparsec.util.JPARSECException;

public class ConstellationTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Constellation test");

        EphemerisElement eph = new EphemerisElement(Target.TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
            EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.WILLIAMS_1994,
            EphemerisElement.FRAME.ICRF);

        try {
            double ra = 15.47 / Constant.RAD_TO_HOUR;
            double dec = 4.9 / Constant.RAD_TO_DEG;
            double epoch = Constant.J2000;

            LocationElement loc = new LocationElement(ra, dec, 1.0);
            String constel = Constellation.getConstellationName(ra, dec, epoch, eph);
            System.out.println("RA = " + Functions.formatRA(loc.getLongitude()) + " DEC = " + Functions.formatDEC(loc.getLatitude()) + " is in " + constel);
            // Serpens Caput
            System.out.println("   = " + Constellation.getConstellation(constel, Constellation.CONSTELLATION_NAME.ENGLISH));

            ra = 17.49 / Constant.RAD_TO_HOUR;
            dec = -12.14 / Constant.RAD_TO_DEG;

            loc = new LocationElement(ra, dec, 1.0);
            constel = Constellation.getConstellationName(ra, dec, epoch, eph);
            System.out.println("RA = " + Functions.formatRA(loc.getLongitude()) + " DEC = " + Functions.formatDEC(loc
                    .getLatitude()) + " is in " + constel);
            // Serpens Cauda

            System.out.println("   = " + Constellation.getConstellation(constel, Constellation.CONSTELLATION_NAME.ENGLISH));

            // Complete list
            for (int i = 0; i < Constellation.CONSTELLATION_NAMES.length; i++) {
                String eng = Constellation.getConstellation(Constellation.CONSTELLATION_NAMES[i], Constellation.CONSTELLATION_NAME.ENGLISH);
                System.out.println(Constellation.CONSTELLATION_NAMES[i] + " = " + eng + " = " + Constellation.getConstellation(eng, Constellation.CONSTELLATION_NAME.SPANISH) + " = " + Constellation.getConstellation(eng, Constellation.CONSTELLATION_NAME.ABREVIATED));
            }

            String c = Constellation.getConstellation(Constellation.getConstellationName(ra, dec, Constant.J2000, eph), Constellation.CONSTELLATION_NAME.ABREVIATED);
            System.out.println(c);
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
