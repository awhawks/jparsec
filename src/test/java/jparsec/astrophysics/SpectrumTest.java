package jparsec.astrophysics;

import jparsec.astrophysics.gildas.ProcessSpectrum;
import jparsec.astrophysics.gildas.Spectrum30m;
import jparsec.astrophysics.gildas.SpectrumLine;
import jparsec.ephem.Functions;
import jparsec.graph.ChartElement;
import jparsec.graph.ChartSeriesElement;
import jparsec.graph.CreateChart;
import jparsec.graph.DataSet;
import jparsec.math.Polynomial;
import jparsec.math.Regression;

import java.awt.Color;

public class SpectrumTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        double v = 11.55, t = 4.05, w = 2.24;
        int np = 500;
        double nu0 = 558966.6656; // N2H+ 6-5
        Spectrum30m.XUNIT xUnit = Spectrum30m.XUNIT.VELOCITY_KMS;

        Spectrum sp = Spectrum.getGaussianSpectrum(v, t, w, np, nu0, 8.0 * w, t / 20.0); // 5 to get some baseline
        Spectrum30m s30m = new Spectrum30m(sp);

        // Add an absorption line at +20 channels well above 3 sigma level
        int offset = 20;
        Spectrum sp2 = Spectrum.getGaussianSpectrum(v, -t / 3, w / 8.0, np - offset, nu0, 5.0 * w, 0 * t / 20.0); // 5 to get some baseline
        Spectrum30m s30m2 = new Spectrum30m(sp2);
        double data[] = s30m.getSpectrumData();
        double data2[] = s30m2.getSpectrumData();

        for (int i = 0; i < data2.length; i++) {
            data[i + offset] += data2[i];
        }

        s30m.setSpectrumData(data);
        CreateChart ch = s30m.getChart(800, 600, xUnit);
        ChartElement chart = ch.getChartElement();
        chart.title = "N_{2}H^{+} 6-5";
        chart.series[0].legend = "Observed spectrum";

        // Now process the spectrum to check the input values of the Gaussian
        // TODO: no lines with 5 sigma
        ProcessSpectrum.TIMES_SIGMA = 5;
        SpectrumLine lines[] = ProcessSpectrum.reduceSpectrum(s30m);
        ProcessSpectrum ps = ProcessSpectrum.reduceSpectrumAndReturnProcessSpectrum(s30m);
        if (lines != null) {
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].enabled) {
                    System.out.println("Found line with " + lines[i].toString() + ". Frequency is " + Functions.formatValue(lines[i].freq, 4) + " MHz.");
                    ChartSeriesElement series = ps.getGaussianFit(lines[i].getGaussianParameters(), xUnit);
                    series.legend = "Fitting " + (i + 1);
                    chart.addSeries(series.clone());
                }
            }
            ChartSeriesElement series = ps.getGaussianFit(lines[0].getGaussianParameters(), xUnit);
            series.yValues = DataSet.toStringValues(ps.getResidualSpectrum());
            series.legend = "Residuals";
            series.color = Color.RED;
            chart.addSeries(series.clone());
        } else {
            System.out.println("No lines found!");
        }

        ch = new CreateChart(chart);
        ch.showChartInJFreeChartPanel();

        // Now let's fit the input spectrum to a polynomial
        double x[] = DataSet.toDoubleValues(sp.getXs(null));
        double y[] = DataSet.toDoubleValues(sp.getYs(null));
        Regression regression = new Regression(x, y);
        regression.polynomial(12);
        Polynomial pn = new Polynomial(regression.getBestEstimates());
        for (int j = 0; j < x.length; j++) {
            System.out.println("Point " + j + ": original = " + y[j] + " / fitted = " + pn.evaluate(x[j]).real);
        }

        // Beam Herschel fitted to 1/nu
/*            x = new double[] {480, 640, 800, 960, 1120, 1410, 1910};
            y = new double[] {44.3, 33.2, 26.6, 22.2, 19.0, 15.2, 11.2};
            for (int i=0; i<x.length; i++) {
                x[i] = 1.0 / x[i];
            }
            regression = new Regression(x, y);
            regression.polynomial(1);
            pn = new Polynomial(regression.getBestEstimates());
            System.out.println(pn.toString());
            double dp[] = regression.getBestEstimatesErrors();
            System.out.println(dp[0]+"/"+dp[1]);
*/
    }

}
