package jparsec.astronomy;

import jparsec.math.Constant;
import jparsec.time.AstroDate;

public class StarTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Star test");

        double sunAbsMag = Star.absoluteMagnitude(-26.71, Constant.AU * 1000.0 / Constant.PARSEC);

        System.out.println("Sun absolute magnitude: " + (float) sunAbsMag);
        System.out.println("Solar wind mass lose ratio (Msun/yr): " + (float) Star.stellarWindMass(7, 500, 1));

        double r = Star.getStarLuminosity(1, 5777); // Value for L = 1: 5770.376 K
        System.out.println("Solar luminosity (W): " + (float) (r * Constant.SUN_LUMINOSITY) + " (" + (float) r + " Lsun)");
        Star.LUMINOSITY_CLASS lclass = Star.LUMINOSITY_CLASS.MAIN_SEQUENCE_V;

        System.out.println("Luminosity using bolometric correction: " + Star.getStarLuminosityUsingBolometricCorrection(5770, lclass));
        System.out.println("B-V for Mv = 4.93 = Mv (Sun): " + Star.getStarBminusV(5777, lclass));

        System.out.println("Extinction at z = 40 deg: " + (float) Star.getExtinction(40 * Constant.DEG_TO_RAD, 0, 10) + " mag");

        System.out.println("");

        double jd = new AstroDate().jd();
        System.out.println("Carrington rotation number: " + Star.getCarringtonRotationNumber(jd));
        System.out.println("Carrington rotation number started: " + Star.getCarringtonRotationNumberLastStart(jd));
        System.out.println("Browns lunation number: " + Star.getBrownLunationNumber(jd));

        System.out.println("");

        double t = 8000;
        String sep = "   ";
        lclass = Star.LUMINOSITY_CLASS.MAIN_SEQUENCE_V;
        double bv = Star.getStarBminusV(t, lclass), Mv = Star.getStarAbsoluteMagnitude(bv, lclass), bc = Star.getStarBolometricCorrection(t, lclass);
        double l = Star.getStarLuminosityUsingBolometricCorrection(Mv, t, lclass), m = Star.getMassFromMassLuminosityRelation(l);
        //t = getStarTeff(bv);
        //t = Star.getEffectiveTemperature(Star.getSpectralType(t, 3));
        System.out.println(Star.getSpectralType(t, 0) + sep + Math.log10(t) + sep + t + sep + bv + sep + Mv + sep + bc + sep + (Mv + bc) + sep + l);
        // See also http://www.world-builders.org/lessons/less/les1/StarTables_Z.html
        System.out.println(Star.luminosityRatio(Mv, Constant.SUN_ABSOLUTE_MAGNITUDE) + sep + Star.getStarRadius(l, t) + sep + m + sep + Star.getStarLifeTime(m));
    }
}
