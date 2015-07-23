package jparsec.math;

import jparsec.util.JPARSECException;

public class MeanValueTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("MeanValue test");

        try {
            double x[] = new double[] { 0.007225092328677015, 0.006792142191533583,
                    0.00721567921149969743, 0.0066994947683284005, 0.0072940980534992655 };

            double dx[] = new double[] { 0.1471154725454545, 0.1471154725454545,
                    0.052060499327602934, 0.0, 0.002431366018643544 };

            MeanValue mv = new MeanValue(x, dx);
            mv.ponderate();

            System.out.println(mv.getMeasuredMeanValue().toString() + " (" + mv.getMeanValue() + " +/- " + mv.getMeanError() + ")");
            System.out.println(x.length + " measures done, but you should do up to " + mv.getMinimumNumberOfMeasuresRecommended() + " to get a correct value.");

            double sigmas = 3;
            int iter = 0;
            System.out.println("--------------");
            System.out.println("Mean value:      " + mv.getMeanValue());
            System.out.println("Arithmetic mean: " + mv.getMeanArithmeticValue());
            System.out.println("Median:          " + mv.getMedian());
            System.out.println("Average:         " + mv.getAverageValue());
            System.out.println("Average median:  " + mv.getAverageUsingMeanAndMedian(sigmas));
            System.out.println("Average sigma-cl:" + mv.getAverageUsingKappaSigmaClipping(sigmas, iter));
            JPARSECException.showWarnings();
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
