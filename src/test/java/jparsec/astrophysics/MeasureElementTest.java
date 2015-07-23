package jparsec.astrophysics;

import jparsec.math.Constant;
import jparsec.math.FastMath;

public class MeasureElementTest {
    /**
     * Test program
     *
     * @param args unused.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("MeasureElement test");

        MeasureElement me0 = new MeasureElement(608.1E-69, 7.96E-69, MeasureElement.UNIT_Y_JY);
        System.out.println(me0.toString());

        me0 = new MeasureElement(18E-88, 27E-88, MeasureElement.UNIT_Y_JY);
        System.out.println(me0.toString());

        me0 = new MeasureElement(156.4E-83, 18.44E-83, MeasureElement.UNIT_Y_JY);
        System.out.println(me0.toString());

        MeasureElement me1 = new MeasureElement(1.0, 1.0, MeasureElement.UNIT_Y_JY);
        MeasureElement me2 = new MeasureElement(1.0, 1.0, MeasureElement.UNIT_Y_MAG_JOHNSON_I);

        System.out.println("(1) = " + me1.value + " +/- " + me1.error + " / " + me1.unit);
        System.out.println("(2) = " + me2.value + " +/- " + me2.error + " / " + me2.unit);

        me1.add(me2);
        System.out.println("(1) + (2) = " + me1.value + " +/- " + me1.error + " / " + me1.unit + " = " + me1.toString());

        me1.convert(MeasureElement.UNIT_Y_MAG_JOHNSON_I);
        System.out.println("(1) + (2) = " + me1.value + " +/- " + me1.error + " / " + me1.unit + " = " + me1.toString());

        me2 = me1.get(MeasureElement.UNIT_Y_JY);
        System.out.println("(1) + (2) = " + me2.value + " +/- " + me2.error + " / " + me2.unit + " = " + me2.toString());

        me1.value = "<125E-6";
        me1.error = 0.0001;
        System.out.println(me1.toString());

        me1.value = "<125E-16";
        me1.error = 10E-16;
        System.out.println(me1.toString());

        me1.value = "125E+16";
        me1.error = 10E16;
        System.out.println(me1.toString());

        me1.value = "70.7E44";
        me1.error = 46.2E44;
        System.out.println(me1.toString());

        me1.value = "760.7";
        me1.error = 416.2;
        System.out.println(me1.toString());

        me1.value = "659.4418422443596E-19";
        me1.error = 35.377975035955366E-19;
        System.out.println(me1.toString(true, 3));

        me1.value = "134.6045340655535E-3";
        me1.error = 44.50724765723468E-3;
        System.out.println(me1.toString());

        for (int i = 0; i < 100; i++) {
            double val = Math.random() * 1000.0;
            double err = Math.random() * 50.0;
            int mag = (int) (Math.random() * 50.0) - 25;
            me1.value = "" + val + "E" + mag;
            me1.error = FastMath.multiplyBy10ToTheX(err, mag);

            System.out.println(val + "/" + err + "/" + mag + "///" + me1.toString());
        }

        System.out.println();
        // c to pc/yr = 0.3065...
        me1 = new MeasureElement(Constant.SPEED_OF_LIGHT, 0, "m/s");
        System.out.println("c = " + me1.toString());
        me1.convert("pc/yr"); // or km/yr
        System.out.println("c = " + me1.toString());
        // Same value but using tropical year instead of Julian year
        // System.out.println(me1.getValue()*(Constant.TROPICAL_YEAR/365.25));
        // Same with JPARSEC
        // System.out.println(Constant.SPEED_OF_LIGHT*86400*Constant.TROPICAL_YEAR/Constant.PARSEC);

        // System.out.println();
        // Another example
        // me1 = new MeasureElement((Constant.GRAVITATIONAL_CONSTANT * 3. * Constant.SUN_MASS) / Math.pow(2.2 * Constant.AU * 1000.0, 2), 0, "kg*m/s2");
        // me1.convert("N");
        // System.out.println(me1.toString()+" "+me1.unit);
    }
}
