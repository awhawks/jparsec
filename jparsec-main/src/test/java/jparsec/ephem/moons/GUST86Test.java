package jparsec.ephem.moons;

import jparsec.graph.DataSet;
import jparsec.math.Constant;
import jparsec.math.DoubleVector;
import jparsec.time.AstroDate;

public class GUST86Test {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        AstroDate astro = new AstroDate(1995, 7, 10, 15, 0, 0);
/*
        EphemerisElement eph = new EphemerisElement(TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000,
                EphemerisElement.ALGORITHM.MOSHIER);
*/
        double jd = astro.jd();

        for (int i = 1; i <= 5; i++) {
            double e[] = GUST86.GUST86_theory(jd, i, 4);
            //e = Precession.precess(Constant.J1950, Constant.J2000, e, eph);

            // From AU to km
            DoubleVector dv = new DoubleVector(DataSet.applyFunction("x*" + Constant.AU, e));

            // From km/day to km/s
            //dv.set(3, dv.get(3) / Constant.SECONDS_PER_DAY);
            //dv.set(4, dv.get(4) / Constant.SECONDS_PER_DAY);
            //dv.set(5, dv.get(5) / Constant.SECONDS_PER_DAY);

            System.out.println(dv.toString());
        }

/* km, km/s
12; 2449909.125, GUST86, 1; 116776.465 ,  -9655.903 , -55704.358 ,-2.45651219 , 2.65694226 ,-5.62224338
12; 2449909.125, GUST86, 2; 186368.403 , -42290.739 ,  -7012.500 ,  .12909352 , 1.43746647 ,-5.30747363
12; 2449909.125, GUST86, 3;-244947.486 ,  76695.011 , -65719.487 ,-1.45138070 , -.87096610 , 4.36898332
12; 2449909.125, GUST86, 4;-289642.933 , 149245.644 ,-290221.835 ,-2.60666546 , -.07923656 , 2.54490241
12; 2449909.125, GUST86, 5; 554001.114 ,-156422.593 ,  93294.441 ,  .69497936 ,  .66198498 ,-3.00317166
 */
    }
}
