package jparsec.observer;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.math.Constant;
import jparsec.time.AstroDate;
import jparsec.util.JPARSECException;

public class EarthOrientationParametersTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("EarthOrientationParameters Test");
        AstroDate astro = new AstroDate(2000, AstroDate.JANUARY, 1, 0, 0, 0);

        try {
            double d = astro.jd();

            EphemerisElement eph = new EphemerisElement(
                Target.TARGET.NOT_A_PLANET,
                EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE,
                EphemerisElement.GEOCENTRIC,
                EphemerisElement.REDUCTION_METHOD.IAU_1976,
                EphemerisElement.FRAME.ICRF);

            System.out.println("IAU1980");
            double eop[] = EarthOrientationParameters.obtainEOP(d, eph);
            double dPsi = eop[0], dEpsilon = eop[1];
            System.out.println("dPsi=" + dPsi + ", dEpsilon=" + dEpsilon);
            System.out.println("First date: " + EarthOrientationParameters.firstEOPRecordDate(eph).toString());
            System.out.println("Last  date: " + EarthOrientationParameters.lastEOPRecordDate(eph).toString());

            eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_2006;
            System.out.println("IAU2000");
            eop = EarthOrientationParameters.obtainEOP(d, eph);
            dPsi = eop[0];
            dEpsilon = eop[1];
            System.out.println("dPsi=" + dPsi + ", dEpsilon=" + dEpsilon);
            System.out.println("First date: " + EarthOrientationParameters.firstEOPRecordDate(eph).toString());
            System.out.println("Last  date: " + EarthOrientationParameters.lastEOPRecordDate(eph).toString());
            System.out.println("LOD=" + EarthOrientationParameters.getLOD(d, eph));

            // EOP prediction test
            double jd = new AstroDate().jd() + 9.5;
            System.out.println(jd + "/" + (jd - Constant.JD_MINUS_MJD));
            eop = EarthOrientationParameters.getEOPPrediction(jd, false, false, eph.frame);

            if (eop != null) {
                System.out.println("EOP predictions: " + eop[0] + "/" + eop[1] + "/" + eop[2] + "/" + eop[3] + "/" + eop[4] + "/" + eop[5]);
            } else {
                System.out.println("EOP prediction not available");
            }

            // RAY model
            double c[] = EarthOrientationParameters.RAYmodelForDiurnalSubdiurnalTides(47100.0 + Constant.JD_MINUS_MJD);
            System.out.println(c[0] * 1.0E6); // should be  162.8386373279636530 muas
            System.out.println(c[1] * 1.0E6); // should be  117.7907525842668974 muas
            System.out.println(c[2] * 1.0E6); // should be -23.39092370609808214 mus
        } catch (JPARSECException ve) {
            ve.printStackTrace();
            JPARSECException.showException(ve);
        }
    }
}
