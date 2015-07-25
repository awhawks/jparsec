package jparsec.math;

import jparsec.graph.CreateChart;
import jparsec.graph.DataSet;
import jparsec.util.JPARSECException;

public class IntegrationTest {

    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Integration test");

        int np = 3000;
        double x0 = 1.1, xf = 1000.0;
        double x[] = new double[np];
        double y[] = new double[x.length];

        // Prepare x values in log scale
        x = DataSet.getSetOfValues(x0, xf, np, true);

        try {
            // Use 1.0/x as function.
            for (int i = 0; i < x.length; i++) {
                y[i] = Evaluation.evaluate("1.0/x", new String[] { "x " + x[i] });
                //System.out.println(x[i] + " / " + y[i]);
            }

            double x1 = 2.0, x2 = xf - 1.0;
            double step = (x2 - x1) * 0.00001;

            double exact = Math.log(x2) - Math.log(x1);

            Integration intgr = new Integration(x, y, x1, x2);
            double integral = intgr.simpleIntegrationUsingSpline(step);
            System.out.println("simple integration spline I = " + integral);
            integral = intgr.simpleIntegration(step);
            System.out.println("simple integration        I = " + integral);
            System.out.println("Exact = " + exact);
            CreateChart ch = intgr.getChart();
            ch.showChartInJFreeChartPanel();
        } catch (JPARSECException e) {
            JPARSECException.showException(e);
        }
    }
}
