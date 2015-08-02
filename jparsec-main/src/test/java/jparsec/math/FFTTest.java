package jparsec.math;

import jparsec.astrophysics.Spectrum;
import jparsec.astrophysics.gildas.Gildas30m;
import jparsec.astrophysics.gildas.Spectrum30m;
import jparsec.graph.CreateChart;
import jparsec.graph.DataSet;

public class FFTTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws Exception {
        String path = "/home/alonso/reduccion/2006/hot_cores/n7129/n7129.30m";
        Gildas30m g30m = new Gildas30m(path);
        int list[] = g30m.getListOfSpectrums(true);
        System.out.println("List of scans (index position, scan number)");

        for (int i = 0; i < list.length; i++) {
            System.out.println(i + " = " + list[i]);
        }

        int index = 11; //list.length-1;
        Spectrum30m s = g30m.getSpectrum(list[index]);

        // Uncomment this to use a gaussian profile
        double v = 0, t = 1, w = 10, nu0 = 100E3, totalW = 250, noiseLevel = 0.1;
        int np = 1024;
        Spectrum sp = Spectrum.getGaussianSpectrum(v, t, w, np, nu0, totalW, noiseLevel);
        s = new Spectrum30m(sp);

        // Get a chart of this spectrum
        CreateChart ch = s.getChart(400, 400, Spectrum30m.XUNIT.CHANNEL_NUMBER);
        ch.showChartInJFreeChartPanel();

        // Write number of channels
        double data[] = DataSet.toDoubleArray(s.getSpectrumData());

        if (data.length % 2 != 0) {
            int n = FFT.getClosestPowerOf2(data.length);
            data = DataSet.getSubArray(data, data.length / 2 - n / 2, data.length / 2 + n / 2 - 1);
            s.setSpectrumData(DataSet.toFloatArray(data));
        }

        System.out.println("number of channels (should be a power of 2): " + data.length);

        // FFT  filtering and chart generation
        s.setSpectrumData(FFT.filter(s.getSpectrumData(), 10));
        CreateChart ch2 = s.getChart(400, 400, Spectrum30m.XUNIT.CHANNEL_NUMBER);
        ch2.showChartInJFreeChartPanel();

        /*
        // Compute FFT and a chart of frequencies vs amplitudes
        Complex cc[] = Complex.createSetOfComplex(data);
        Complex[] ccy = fft(cc);
        CreateChart ch2 = s.getChart(400, 400, Spectrum30m.XUNIT.CHANNEL_NUMBER);
        double filtered[] = Complex.getSetOfReals(ccy);
        ChartElement chart = ch2.getChartElement();
        chart.series[0].yValues = DataSet.toStringValues(filtered);
        ch2 = new CreateChart(chart);
        ch2.showChartInJFreeChartPanel();

        // Process the spectrum by eliminating high and low frequencies,
        // and show another chart
        double r[] = Complex.getSetOfReals(ccy);
        double max = DataSet.getMaximumValue(r), min = DataSet.getMinimumValue(r);
        for (int i=0; i<ccy.length; i++) {
            if (Math.abs(ccy[i].real) < max/20.0) ccy[i] = new Complex(0, 0);
        }
        filtered = Complex.getSetOfImaginary(ccy);
        chart.series[0].yValues = DataSet.toStringValues(filtered);
        ch2 = new CreateChart(chart);
        ch2.showChartInJFreeChartPanel();

        // Compute inverse FFT to recover the new spectrum without the high and low
        // frequencies (likely noise)
        Complex cccy[] = ifft(ccy);
        filtered = Complex.getSetOfReals(cccy);
        chart.series[0].yValues = DataSet.toStringValues(filtered);
        ch2 = new CreateChart(chart);
        ch2.showChartInJFreeChartPanel();
        */
    }
}
