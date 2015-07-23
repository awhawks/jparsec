package jparsec.astrophysics.gildas;

import jparsec.graph.ChartSeriesElement;
import jparsec.graph.CreateChart;
import jparsec.io.ConsoleReport;

public class ProcessSpectrumTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("ProcessSpectrum test");
        String file = "/home/alonso/colaboraciones/Rafael/2013/largeProgram/out.30m";
        Gildas30m g30m = new Gildas30m(file);
        int list[] = g30m.getListOfSpectrums(true);
        Spectrum30m sp = g30m.getSpectrum(list[0]);
        ProcessSpectrum.TIMES_SIGMA = 5.0;
        SpectrumLine[] lines = ProcessSpectrum.reduceSpectrum(sp, -1);
        Spectrum30m.XUNIT xUnit = Spectrum30m.XUNIT.VELOCITY_KMS;
        CreateChart ch = sp.getChart(500, 500, xUnit);

        if (lines != null) {
            System.out.println("Found " + lines.length + " lines");
            ProcessSpectrum ps = new ProcessSpectrum(sp);
            for (int i = 0; i < lines.length; i++) {
                System.out.println(i + " (" + lines[i].minChannel + "-" + lines[i].maxChannel + "): vel = " + lines[i].vel + " km/s, peak = " + lines[i].peakT + " K, width = " + lines[i].width + " km/s, area = " + lines[i].area + " K km/s");
                ChartSeriesElement series = ps.getGaussianFit(lines[i].getGaussianParameters(), xUnit);
                series.legend = "Fit to line " + (i + 1);
                ch.addSeries(series);
            }
        }

        ch.showChartInJFreeChartPanel();
        double freq = 256329.5, width = 1, maxT = 1000, maxrint = -7;
        boolean jpl = false, splatalogue = true, onlyAtm = false, onlyLessProbable = false;
        String out[] = ProcessSpectrum.identifyLine(freq, width, maxT, maxrint, jpl, splatalogue, onlyAtm, onlyLessProbable);
        ConsoleReport.stringArrayReport(out);
    }
}
