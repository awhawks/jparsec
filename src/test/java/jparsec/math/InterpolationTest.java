package jparsec.math;

import java.awt.geom.Point2D;
import jparsec.ephem.Functions;
import jparsec.graph.CreateChart;
import jparsec.util.JPARSECException;

public class InterpolationTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Interpolation test");
        double x[] = new double[] { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
        double y[] = new double[] { 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3 };
        double z[] = new double[] { 1, 2, 3, 2, 3, 4, 3, 4, 5, 2, 3, 4 };

        try {
            Interpolation interp = new Interpolation(x, y, z, false);
            double px = 1.5, pz = 2.5;
            double py = interp.linearInterpolation3d(px, pz);
            System.out.println(px + " / " + pz + " / " + py);

            x = new double[] { 7.0, 8.0, 9.0 };
            y = new double[] { 0.884226, 0.877366, 0.870531 };
            interp = new Interpolation(x, y, false);
            System.out.println("Meeus  interpolation: y = " + interp.MeeusInterpolation(8.18125));
            System.out.println("Spline interpolation: y = " + interp.splineInterpolation(8.18125));
            System.out.println("Linear interpolation: y = " + interp.linearInterpolation(8.18125));
            x = new double[] { 12.0, 16.0, 20.0 };
            y = new double[] { 1.3814294, 1.3812213, 1.3812453 };
            interp = new Interpolation(x, y, false);
            Point2D p = interp.MeeusExtremum();
            System.out.println("Extremum: x = " + p.getX() + ", y = " + p.getY());
            CreateChart ch = interp.getChart(false, 2);
            ch.showChartInJFreeChartPanel();
            x = new double[] { -1.0, 0.0, 1.0 };
            y = new double[] { -2.0, 3.0, 2.0 };
            interp = new Interpolation(x, y, false);
            System.out.println("Zero at x = " + interp.MeeusZero());

            x = new double[] { 27.0, 27.5, 28.0, 28.5, 29.0 };
            y = new double[] { 54 * 60 + 36.125, 54 * 60 + 24.606, 54 * 60 + 15.486, 54 * 60.0 + 8.694, 54 * 60 + 4.133 };
            double y0 = 28.0 + (3.0 + 20.0 / 60.0) / 24.0;
            interp = new Interpolation(x, y, false);
            System.out.println("Meeus interpolation: y = " +
                    Functions.formatAngle(Constant.ARCSEC_TO_RAD * interp.MeeusInterpolation(y0), 3));

            x = new double[] { 25.0, 26.0, 27.0, 28.0, 29.0 };
            y = new double[] {
                Functions.parseDeclination("-01\u00b0 11' 21.23\""),
                Functions.parseDeclination("-00\u00b0 28' 12.31\""),
                Functions.parseDeclination("00\u00b0 16' 07.02\""),
                Functions.parseDeclination("01\u00b0 01' 00.13\""),
                Functions.parseDeclination("01\u00b0 45' 46.33\"")
            };

            interp = new Interpolation(x, y, false);
            System.out.println("Zero at x = " + interp.MeeusZero());

            // I must add 3 degrees here to 'help' convergence
            // otherwise the results are not close to those from
            // Meeus, chapter 3, page 31.
            double da = 3;

            x = new double[] {
                    29.0 + da,
                    30.0 + da,
                    31.0 + da,
                    32.0 + da,
                    33.0 + da
            };

            y = new double[] {
                    Math.sin((29.0 + da) * Constant.DEG_TO_RAD),
                    Math.sin((30.0 + da) * Constant.DEG_TO_RAD),
                    Math.sin((31.0 + da) * Constant.DEG_TO_RAD),
                    Math.sin((32.0 + da) * Constant.DEG_TO_RAD),
                    Math.sin((33.0 + da) * Constant.DEG_TO_RAD)
            };

            interp = new Interpolation(x, y, false);
            System.out.println("Zero at x = " + interp.MeeusZero());
            p = interp.MeeusExtremum();

            if (p != null) {
                System.out.println("Extremum: x = " + p.getX() + ", y = " + p.getY());
            } else {
                System.out.println("Could not find extremum (ok!)");
            }

            x = new double[] {
                    29.43, 30.97, 27.69, 28.11, 33.05
            };

            y = new double[] {
                    Math.sin(29.43 * Constant.DEG_TO_RAD),
                    Math.sin(30.97 * Constant.DEG_TO_RAD),
                    Math.sin(27.69 * Constant.DEG_TO_RAD),
                    Math.sin(28.11 * Constant.DEG_TO_RAD),
                    Math.sin(33.05 * Constant.DEG_TO_RAD)
            };

            interp = new Interpolation(x, y, false);
            System.out.println("Lagrange interpolation: y = " + interp.LagrangeInterpolation(30.0));
        } catch (JPARSECException e) {
            JPARSECException.showException(e);
        }
    }
}
