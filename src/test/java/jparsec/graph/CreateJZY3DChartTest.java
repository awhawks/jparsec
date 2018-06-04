package jparsec.graph;

import java.awt.Color;

import jparsec.astronomy.Difraction;
import jparsec.astronomy.TelescopeElement;

public class CreateJZY3DChartTest {
    /**
     * Test program.
     *
     * @param args Not used.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("CreateJZY3DChart test");
        TelescopeElement telescope = TelescopeElement.NEWTON_20cm;
        int field = 3;
        double data[][] = Difraction.pattern(telescope, field);

        GridChartElement gridChart = new GridChartElement("Difraction pattern",
            "offsetX", "offsetY", "RelativeIntensity", GridChartElement.COLOR_MODEL.RED_TO_BLUE,
            new double[] { -field, field, -field, field }, data,
            new double[] { 0, 0.2, 0.4, 0.6, 0.8, 1.0 }, 400);

        ChartSeriesElement3D series = new ChartSeriesElement3D(gridChart);
        series.color = Color.RED;
        ChartSeriesElement3D series2 = new ChartSeriesElement3D(
            new double[] { 0, 1, 2, -1, -2 },
            new double[] { 0, 1, 1, -1, -1 },
            new double[] { 2, 1, 1, 1, 1 }, "3d Points");

        ChartElement3D chart = new ChartElement3D(new ChartSeriesElement3D[] { series, series2 },
            "Difraction pattern", "@DELTAx (\")", "@DELTAy (\")", "@SIZE20I_{relative} (|@PSI|^{2})");
        chart.showToolbar = false;
        chart.showLegend = true;
        chart.showTitle = true;
        CreateJZY3DChart c = new CreateJZY3DChart(gridChart);
        c.showChart(500, 500);
        CreateJZY3DChart c2 = new CreateJZY3DChart("x * Math.sin(x * y)", -3, 3, 40);
        c2.showChart(500, 500);
    }
}
