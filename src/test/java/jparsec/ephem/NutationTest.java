package jparsec.ephem;

import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.EarthOrientationParameters;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;

public class NutationTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Nutation test");

        AstroDate astro = new AstroDate(2009, AstroDate.JULY, 1, 0, 0, 0);

        try {
            double d = astro.jd();

            System.out.println(d + " TT");
            System.out.println("IAU1980");

            TimeElement time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            EphemerisElement eph = new EphemerisElement(Target.TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC,
                    EphemerisElement.REDUCTION_METHOD.IAU_1976, // Same results as those given by Horizons
                    EphemerisElement.FRAME.ICRF);
            double JD_UTC = TimeScale.getJD(time, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UTC);

            // Force initial EOP to 0
            EarthOrientationParameters.clearEOP();
            double dPsi = 0, dEpsilon = 0;

            double n[] = Nutation.calcNutation(Functions.toCenturies(d), eph);
            double nutLon = n[0] * Constant.RAD_TO_ARCSEC;
            double nutLat = n[1] * Constant.RAD_TO_ARCSEC;

            System.out.println("dPhi=" + nutLon + ", dEpsilon=" + nutLat + ", coreDpsi=" + dPsi + ", coreDeps=" + dEpsilon);
            // Results should be 14.7774  4.3386

            Nutation.clearPreviousCalculation();

            double[] eop = EarthOrientationParameters.obtainEOP(JD_UTC, eph);
            dPsi = eop[0];
            dEpsilon = eop[1];
            n = Nutation.calcNutation(Functions.toCenturies(d), eph);
            nutLon = n[0] * Constant.RAD_TO_ARCSEC;
            nutLat = n[1] * Constant.RAD_TO_ARCSEC;
            System.out.println("dPhi=" + nutLon + ", dEpsilon=" + nutLat + ", coreDpsi=" + dPsi + ", coreDeps=" + dEpsilon);

            EarthOrientationParameters.clearEOP();
            dPsi = dEpsilon = 0;
            Nutation.clearPreviousCalculation();

            System.out.println("IAU2000");
            eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_2000;
            n = Nutation.calcNutation(Functions.toCenturies(d), eph);
            nutLon = n[0] * Constant.RAD_TO_ARCSEC;
            nutLat = n[1] * Constant.RAD_TO_ARCSEC;
            System.out.println("dPhi=" + nutLon + ", dEpsilon=" + nutLat + ", coreDpsi=" + dPsi + ", coreDeps=" + dEpsilon);
            // Results should be 14.7823  4.3391 according to AA

            Nutation.clearPreviousCalculation();

            eop = EarthOrientationParameters.obtainEOP(JD_UTC, eph);
            dPsi = eop[0];
            dEpsilon = eop[1];
            n = Nutation.calcNutation(Functions.toCenturies(d), eph);
            nutLon = n[0] * Constant.RAD_TO_ARCSEC;
            nutLat = n[1] * Constant.RAD_TO_ARCSEC;
            System.out.println("dPhi=" + nutLon + ", dEpsilon=" + nutLat + ", coreDpsi=" + dPsi + ", coreDeps=" + dEpsilon);

/*            JPLEphemeris jpl = new JPLEphemeris(JPLEphemeris.DE405);
            double nut[] = jpl.getPositionAndVelocity(d, 12); //JPLEphemeris.TARGET_LIBRATIONS);
            System.out.println("JPL DE"+jpl.getJPLVersion()+": "+nut[4]);
            System.out.println("JPL DE"+jpl.getJPLVersion()+": "+nut[0]);
*/

/*            double JD = Constant.J1900;
            double T = Functions.toCenturies(JD);
            double eq[] = new double[] {1.0, 1.0, 1.0};
            double ecl[] = Ephem.equatorialToEcliptic(eq, JD, eph.ephemMethod);
            LocationElement lecl = LocationElement.parseRectangularCoordinates(ecl);
            double nut[] = Nutation.calcNutation(T, eph.ephemMethod);
            lecl.setLongitude(lecl.getLongitude() + nut[0]);
            lecl.setLatitude(lecl.getLatitude() + nut[1]);
            double eq1[] = Ephem.eclipticToEquatorial(lecl.getRectangularCoordinates(), JD, eph.ephemMethod);
            double eq2[] = Nutation.calcNutation(JD, eq, eph.ephemMethod);
            double eq3[] = Nutation.nutateInEquatorialCoordinates(JD, eph.ephemMethod, eq, true);

            System.out.println(eq1[0]+"/"+eq1[1]+"/"+eq1[2]);
            System.out.println(eq2[0]+"/"+eq2[1]+"/"+eq2[2]);
            System.out.println(eq3[0]+"/"+eq3[1]+"/"+eq3[2]);
*/
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
