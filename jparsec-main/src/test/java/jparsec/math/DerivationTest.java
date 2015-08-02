package jparsec.math;

import jparsec.graph.CreateChart;
import jparsec.util.JPARSECException;

public class DerivationTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        System.out.println("Derivation test");
        double x[] = new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        double y[] = new double[x.length];

        try {
            for (int i = 0; i < x.length; i++) {
                y[i] = Evaluation.evaluate("x+Math.log(x+1)", new String[] { "x " + x[i] });
            }

            double px = 8.3335;
            int degree = 6;
            double exact = 1 + 1.0 / (1.0 + px);
            Derivation der = new Derivation(x, y);

            double i = der.Lagrange(px, degree);
            double ii = der.splineDerivative(px);
            System.out.println("I (Lagrange) = " + i);
            System.out.println("I (Spline) = " + ii);
            System.out.println("I (exact) = " + exact);

            CreateChart ch = der.getChart(true, 2);
            ch.showChartInJFreeChartPanel();
            // Note Lagrange's method is usually better
        } catch (JPARSECException e) {
            JPARSECException.showException(e);
        }
    }
}
