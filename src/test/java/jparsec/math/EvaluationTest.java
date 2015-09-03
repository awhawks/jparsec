package jparsec.math;

import jparsec.graph.CreateChart;
import jparsec.io.ConsoleReport;

public class EvaluationTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Evaluation test");

        //String f = "2*Math.sqrt(16)*(5==5)+Math.pow(x,3)*(3>4)";
        String f = "2*Math.sqrt(16)*(Math.sqrt(x)<2)+Math.pow(x,3)*(Math.abs(x-2)<1)";
        String variables[] = new String[] { "x 2" };
        Evaluation eval = new Evaluation(f, variables);
        double yy = eval.evaluate();
        System.out.println("Expression: " + f);
        System.out.println("(x=2) => " + yy);
        System.out.println();

        CreateChart ch = eval.getChart("x", 0, 5, 100);
        ch.showChartInJFreeChartPanel();

        double x = 1.0, y = 2.0, z = 3.0;
        String function = "x*x+y*y+z*z";
        eval = new Evaluation();
        double val = eval.evaluateMathFunction(function, x, y, z);
        System.out.println("f(x,y,z) = " + function);
        System.out.println("f(" + x + ", " + y + ", " + z + ") = " + val);
        System.out.println();

        String expression = "var u=1.0\nvar v=1.0\n";
        expression += "var ir=.3+.1*Math.sin(4*Math.PI*u)\n";
        expression += "var r=ir*Math.sin(2*Math.PI*v)+.5\n";
        expression += "var x=r*Math.sin(2*Math.PI*u)\n";
        expression += "var y=r*Math.cos(2*Math.PI*u)\n";
        expression += "var z=1.5*ir*Math.cos(Math.PI*v)\n";
        expression += "var xx = new Array (x, y, z)\n";
        expression += "xx"; // To get the array
        System.out.println("Expression: \n" + expression);
        double out[] = Evaluation.nativeArrayToDoubleArray(eval.evaluatePureJavaExpression(expression));
        ConsoleReport.doubleArrayReport(out, "f2.5");
    }
}
