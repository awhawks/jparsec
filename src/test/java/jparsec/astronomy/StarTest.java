package jparsec.astronomy;

import jparsec.graph.DataSet;
import jparsec.io.ConsoleReport;
import jparsec.math.Constant;

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
        System.out.println("B-V for Mv = 4.93 = Mv (Sun): " + Star.getStarBminusV(4.93, lclass));

        System.out.println("Extinction at z = 40 deg: " + (float) Star.getExtinction(40 * Constant.DEG_TO_RAD, 0, 10) + " mag");
        System.out.println();
        System.out.println("---");
        System.out.println();
        double H0 = 71, WM = 0.27, WV = 0.73, z = 3;
        String values[] = new String[] {
            "Cosmology model for H0 = xx.x, WM = xx.xx, WV = xx.xx, and z = xx.",
            "",
            "It is now xx.xxx Gyr since the Big Bang.",
            "The age at redshift z was xx.xxx Gyr.",
            "The light travel time was xx.xxx Gyr.",
            "The comoving radial distance, which goes into Hubble's law, is xxxxx.x Mpc or xxx.xxx Gly.",
            "The comoving volume within redshift z is xxxx.xxx Gpc3.",
            "The angular size distance DA is xxxx.x Mpc or xx.xxxx Gly.",
            "This gives a scale of xx.xxx kpc/\".",
            "The luminosity distance DL is xxxxx.x Mpc or xx.xxx Gly."
        };

        System.out.println(ConsoleReport.doubleArrayReport(values, DataSet.addDoubleArray(new double[] { H0, WM, WV, z }, Star.cosmology(H0, WM, WV, z))));
        System.out.println("Input z was " + Star.getRedshiftFromLightTravelTime(H0, WM, WV, Star.cosmology(H0, WM, WV, z)[2]));

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
