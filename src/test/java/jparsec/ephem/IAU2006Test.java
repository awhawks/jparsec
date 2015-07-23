package jparsec.ephem;

import jparsec.io.ConsoleReport;
import jparsec.math.matrix.Matrix;
import jparsec.observer.CityElement;
import jparsec.observer.EarthOrientationParameters;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;

import java.math.BigDecimal;

public class IAU2006Test {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("IAU2006 test");

        try {
            AstroDate astro = new AstroDate(2006, AstroDate.JANUARY, 15, 21, 24, 37.5000);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            ObserverElement obs = new ObserverElement(new CityElement("Madrid"));
            EphemerisElement eph = new EphemerisElement(Target.TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.MOSHIER);
            eph.useVondrak2011PrecessionFormulaInsteadOfIAU2006 = false;

            // Force example values
            double jd_UTC = TimeScale.getJD(time, obs, eph, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            double eop[] = new double[] { 0.0, 0.0, 0.0, 0.0, 0.3341 };
            EarthOrientationParameters.forceEOP(jd_UTC, eph, eop);
            double TTminusUT1 = 64.8499; // Value needed to match TT given by Capitaine
            TimeScale.forceTTminusUT1(time, obs, TTminusUT1);

            BigDecimal exactJD_UT1 = TimeScale.getExactJD(time, obs, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            BigDecimal exactJD_TT = TimeScale.getExactJD(time, obs, eph, TimeElement.SCALE.TERRESTRIAL_TIME);
            System.out.println("UT1 " + exactJD_UT1); // JDexact gives 2453751.39210456134259...
            System.out.println("TT  " + exactJD_TT); // Should be 2453751.392855138888889
            System.out.println(TimeScale.getTTminusUT1(time, obs));
            ConsoleReport.doubleArrayReport(EarthOrientationParameters.obtainEOP(jd_UTC, eph), "f2.4");

            Matrix NPB = IAU2006.getNPB(time, obs, eph);
            NPB.print(19, 17);
            // From Capitaine 2006 (ok):
            //+0.99999892304984813 -0.00134606989019584 -0.00058480338117601
            //+0.00134604536886632 +0.99999909318492607 -0.00004232245847787
            //+0.00058485981985452 +0.00004153524101576 +0.99999982810689266

            Matrix GCRS_to_CIRS = IAU2006.getGCRS_to_CIRS(time, obs, eph);
            GCRS_to_CIRS.print(19, 17);
            // NPB - CIO from Capitaine 2006 (ok)
            //+0.99999982896948063 +0.00000000032319161 -0.00058485982037244
            //-0.00000002461548515 +0.99999999913741188 -0.00004153523372294
            //+0.00058485981985452 +0.00004153524101576 +0.99999982810689266

            Matrix GCRS_to_TIRS = IAU2006.getGCRS_to_TIRS(time, obs, eph);
            GCRS_to_TIRS.print(19, 17);
            // Difference of <5E-15 (<0.1 microarcsecond) with Wallace and Capitaine 2006
            // (http://syrte.obspm.fr/iau2006/aa06_459.IAU06prec.pdf) when using the
            // same values for TT-UT1 (64.8499s) and UTC-UT1 (0.3341s):
            //+0.23742421473054043 +0.97140604802742436 -0.00017920749858993
            //-0.97140588849284692 +0.23742427873021975 +0.00055827489427310
            //+0.00058485981985452 +0.00004153524101576 +0.99999982810689266

/*            // DE200 to FK5 (eq)
            Matrix rx = Matrix.getR1(0.0361 * Constant.ARCSEC_TO_RAD);
            Matrix rz = Matrix.getR3(0.0059 * Constant.ARCSEC_TO_RAD);
            Matrix r = rx.times(rz);
            r.print(17, 15);

            // VSOP87 to FK5 (ecl to eq)
            rx = Matrix.getR1(Functions.parseDeclination("23 26 21.4091"));
            rz = Matrix.getR3(-0.0995 * Constant.ARCSEC_TO_RAD);
            // Vsop class implements algorithms given by IMCCE,
            // seems they use -0.099 instead of -0.0995. See
            // http://www.imcce.fr/en/ephemerides/generateur/ephepos/ephemcc_doc.ps.gz
            r = rx.times(rz);
            r.print(17, 15);
*/
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
