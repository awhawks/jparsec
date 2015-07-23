package jparsec.math;

import jparsec.util.JPARSECException;

public class RootTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Root test");

        try {
            Root root = new Root("x*x-2*Math.cos(x)", "2*x+2*Math.sin(x)");
            double E = 1E-10;
            double x0 = 1.0;
            double y0 = 0.0;
            int limit_of_iterations = 1000;
            double result[] = root.getRootByNewtonRaphson(E, x0, y0, limit_of_iterations);

            if (result != null) {
                int iter = (int) result[2];
                System.out.println("Root for " + root.functionRealTerm + " found around x = " + result[0] + " (in " + iter + " iterations).");
                System.out.println("Residual error is " + Math.abs(root.evaluateFunction(root.functionRealTerm,
                        result[0], result[1])));
                System.out.println("String representation of the root is: " + root.toString(result[0], Math.abs(root.evaluateFunction(root.functionRealTerm,
                        result[0], result[1]))));
            } else {
                System.out.println("Root could not be found in " + limit_of_iterations + " iterations.");
            }

            // Now test real function roots using bisection
            root = new Root("Math.sin(x)");
            result = root.getRootsRealFunction(E, 0, 10, 1);
            System.out.println("");
            System.out.println("Using bisection for function " + root.functionRealTerm);

            for (int i = 0; i < result.length; i++) {
                System.out.println("Root around x = " + result[i]);
            }
        } catch (JPARSECException exc) {
            exc.showException();
        }
    }
}
