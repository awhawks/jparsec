package jparsec.ephem.planets.imcce;

import java.io.Serializable;
import jparsec.ephem.Ephem;

/**
 * This class implements the Lunar Solution ELP2000 from the IMCCE. The entire
 * theory is applied. <P>
 * Reference frame is mean dynamical ecliptic and inertial equinox of J2000
 * epoch.<P>
 * References: <P>
 * 1. <I>ELP 2000-85: a semi-analytical lunar ephemeris adequate for historical
 * times</I>, Chapront-Touze M., Chapront J., Astron. & Astrophys. 190, 342
 * (1988).
 * <P>
 * 2. <I>The Lunar Ephemeris ELP 2000</I>, Chapront-Touze M., Chapront J.,
 * Astron. & Astrophys. 124, 50 (1983).
 *
 * @see Ephem
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
class Elp2000Set1 implements Serializable {
    final int[] ILU;
    final double[] COEF;

    Elp2000Set1(final int[] ilu, final double[] coef) {
        this.ILU = ilu;
        this.COEF = coef;
    }
}
