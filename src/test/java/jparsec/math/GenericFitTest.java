package jparsec.math;

import jparsec.ephem.Functions;
import jparsec.graph.CreateChart;
import jparsec.graph.DataSet;
import jparsec.io.ConsoleReport;

public class GenericFitTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Let's fit a set of data to the function p1 * Math.sin(x) + p2 * Math.sin(2.0*x) + p3 * Math.sin(3.0*x)");

        double x[] = new double[] { 3, 20, 34, 50, 75, 88, 111, 129, 143, 160, 183, 200, 218, 230, 248, 269, 290, 303, 320, 344 };
        double y[] = new double[] { 0.0433, 0.2532, 0.3386, 0.3560, 0.4983, 0.7577, 1.4585, 1.8628,
                1.8264, 1.2431, -0.2043, -1.2431, -1.8422, -1.8726, -1.4889, -0.8372, -0.4377, -0.3640, -0.3508, -0.2126 };
        x = Functions.scalarProduct(x, Constant.DEG_TO_RAD);

        GenericFit g = new GenericFit(x, y, "Math.sin(x)", "Math.sin(2.0*x)", "Math.sin(3.0*x)");
        ConsoleReport.doubleArrayReport(g.fit(), "f2.2"); // 1.2, -0.77, 0.39

        System.out.println();

        // Now the same using Regression
        System.out.println("Now using Regression class");
        Regression regression = new Regression(x, y, DataSet.getSetOfValues(0.0, 0.0, x.length, false));
        String function = "p1*Math.sin(x)+p2*Math.sin(2.0*x)+p3*Math.sin(3.0*x)";
        double initialValues[] = DataSet.getSetOfValues(1.0, 1.0, 3, false); // Suppose we have no idea of the results
        initialValues[1] = -1; // But we know second value is negative (Help required with correct sign!)
        regression.customFunction(function, initialValues);
        ConsoleReport.doubleArrayReport(regression.getBestEstimates(), "f2.4");
        System.out.println("Converge ? " + regression.convergence());

        System.out.println("");
        double out[] = regression.getBestEstimates();
        double out2[] = g.fit();
        String[] param = new String[] { "p1 " + out[0], "p2 " + out[1], "p3 " + out[2] };
        String[] param2 = new String[] { "p1 " + out2[0], "p2 " + out2[1], "p3 " + out2[2] };
        System.out.println("x, y, y estimate (GenericFit), y estimate (Regression)");
        for (int i = 0; i < x.length; i++) {
            Evaluation eval = new Evaluation(function, DataSet.addStringArray(param, new String[] { "x " + x[i] }));
            Evaluation eval2 = new Evaluation(function, DataSet.addStringArray(param2, new String[] { "x " + x[i] }));
            System.out.println(x[i] + " " + y[i] + " " + eval.evaluate() + " " + eval2.evaluate());
        }

        System.out.println("");
        System.out.println("Now more simple a*sqrt(x)+b*x+c");
        x = new double[] { 0, 1, 2, 3, 4, 5 };
        y = new double[] { 0, 1.2, 1.4, 1.7, 2.1, 2.2 };
        g = new GenericFit(x, y, "Math.sqrt(x)", "x", "1");
        ConsoleReport.doubleArrayReport(g.fit(), "f2.4"); // 1.016

        System.out.println("Now using Regression class");
        regression = new Regression(x, y, DataSet.getSetOfValues(0.0, 0.0, x.length, false));
        function = "p1*Math.sqrt(x)+p2*x+p3";
        initialValues = DataSet.getSetOfValues(1.0, 1.0, 3, false); // Suppose we have no idea of the results
        regression.customFunction(function, initialValues);
        ConsoleReport.doubleArrayReport(regression.getBestEstimates(), "f2.4"); // Second value is negative, but little enough

        CreateChart ch = g.getChart(2);
        ch.showChartInJFreeChartPanel();
    }
}
