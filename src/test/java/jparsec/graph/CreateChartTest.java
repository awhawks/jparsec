package jparsec.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import jparsec.graph.chartRendering.AWTGraphics;
import jparsec.io.image.Picture;
import jparsec.math.Constant;
import jparsec.util.JPARSECException;

public class CreateChartTest {
    /**
     * For unit testing only.
     * @param args Not used.
     * @throws JPARSECException If an error occurs.
     */
    public static void main(String[] args) throws JPARSECException
    {
        System.out.println("CreateChart test");

        // Charts can be created using a ChartElement or a SimpleChartElement

        // SimpleChartElement example
        SimpleChartElement chart1 = new SimpleChartElement(ChartElement.TYPE.XY_CHART,
                ChartElement.SUBTYPE.XY_SCATTER, new double[]
                { 1, 2, 3 }, new double[]
                { 2, 5, 8 }, "CHART 1", "X_LABEL", "Y_LABEL", "LEGEND", true, false, 400, 300);
        chart1.xAxisInLogScale = false;
        CreateChart ch1 = new CreateChart(chart1);
        Picture p1 = ch1.showChart(false); // false => not draw it now (later in real time)
        double p[] = ch1.getJava2DUnits(1, 2);
        System.out.println(p[0]+"/"+p[1]);
        Graphics2D g = p1.getImage().createGraphics();
        g.setColor(Color.BLACK);
        int x = (int) p[0], y = (int) p[1], rx = 5, ry = 3;
        g.drawOval(x-rx, y-ry, 2*rx, 2*ry);
        p = ch1.getPhysicalUnits(p[0], p[1]);
        System.out.println(p[0]+"/"+p[1]);
        ch1.prepareGraphics2D(g, true);
        x = (int) p[0];
        y = (int) p[1];
        rx = 1;
        ry = 2;
        g.setColor(new Color(255, 0, 0, 128));
        g.setStroke(AWTGraphics.getStroke(new JPARSECStroke(JPARSECStroke.STROKE_DEFAULT_LINE, 0.1f)));
        g.fillOval(x-rx, y-ry, 2*rx, 2*ry);
        g.draw(AWTGraphics.rotateShape(new Ellipse2D.Double(2-rx*0.5, 5-ry*0.5, rx, ry), -45 * Constant.DEG_TO_RAD)); // Rotate 45� using the Graphics2D transform
        g.fill(AWTGraphics.rotateShape(g, new Ellipse2D.Double(2-rx*0.5, 5-ry*0.5, rx, ry), -45 * Constant.DEG_TO_RAD)); // Rotate 45� using the original transform
        g.drawLine(2, 5, 1, 2);
        p1.show("");

/*
		// Quick simple chart example
//		CreateChart ch1bis =
//			CreateChart.createSimpleChart(new double[] {1, 2, 3}, new double [] {2, 4, 8}, "TITLE", "X_LABEL", "Y_LABEL", "LEGEND", false);
//		ch1bis.showChart(true);

		// Another example for a category chart
		SimpleChartElement chart2 = new SimpleChartElement(ChartElement.TYPE.CATEGORY_CHART,
				ChartElement.SUBTYPE.CATEGORY_BAR_3D, new double[]
				{ 1, 2, 3 }, new double[]
				{ 2, 4, 8 }, "CHART 2", "X_LABEL", "Y_LABEL", "LEGEND", true, false, 400, 300);
		CreateChart ch2 = new CreateChart(chart2);
		ch2.showChart(true);

		// Example of a simple pie chart
		SimpleChartElement chart3 = new SimpleChartElement(ChartElement.TYPE.PIE_CHART,
				ChartElement.SUBTYPE.PIE_3D, new double[]
				{ 1, 2, 3 }, new double[]
				{ 2, 4, 8 }, "CHART 3", "X_LABEL", "Y_LABEL", "LEGEND", true, false, 400, 300);
		chart3.xValuesForPieAndCategoryCharts = new String[] { "2006", "2007", "2008" };
		CreateChart ch3 = new CreateChart(chart3);
		ch3.showChart(true);


		// Full ChartElement simple example. This allows to show several series
		String vx[] = DataSet.toStringValues(DataSet.getSetOfValues(1.0, 100, 100, true));
		String y1[] = DataSet.toStringValues(DataSet.getSetOfValues(1000.0, 100000, 100, true));
		String y2[] = DataSet.toStringValues(DataSet.getSetOfValues(10000.0, 1000000, 100, true));
		String y3[] = DataSet.toStringValues(DataSet.getSetOfValues(100000.0, 10000000, 100, true));
		String vy[] = new String[100];
		for (int i=0; i<100; i++) {
			double y = 1.82E7/Math.pow(Double.parseDouble(vx[i]), 2.3);
			vy[i] = ""+y;
		}
		ChartSeriesElement chartSeries1 = new ChartSeriesElement(vx,
				vy, null, null,
				"t_{kh}", true, Color.BLACK, ChartSeriesElement.SHAPE_EMPTY,
				ChartSeriesElement.REGRESSION.NONE);
		ChartSeriesElement chartSeries2 = new ChartSeriesElement(vx,
				y1, null, null,
				"dM/dt = 10^{-3} M_{@SUN}/a�o", true, Color.BLACK, ChartSeriesElement.SHAPE_EMPTY,
				ChartSeriesElement.REGRESSION.NONE);
		ChartSeriesElement chartSeries3 = new ChartSeriesElement(vx,
				y2, null, null,
				"dM/dt = 10^{-4}", true, Color.BLACK, ChartSeriesElement.SHAPE_EMPTY,
				ChartSeriesElement.REGRESSION.NONE);
		ChartSeriesElement chartSeries4 = new ChartSeriesElement(vx,
				y3, null, null,
				"dM/dt = 10^{-5}", true, Color.BLACK, ChartSeriesElement.SHAPE_EMPTY,
				ChartSeriesElement.REGRESSION.NONE);
		chartSeries1.showShapes = false;
		chartSeries2.showShapes = false;
		chartSeries3.showShapes = false;
		chartSeries4.showShapes = false;

		ChartSeriesElement series[] = new ChartSeriesElement[] {chartSeries1, chartSeries2,
				chartSeries3, chartSeries4};
		ChartElement chart4 = new ChartElement(series, ChartElement.TYPE.XY_CHART,
				ChartElement.SUBTYPE.XY_SCATTER,
				"Acreci�n y tiempo de Kelvin-Helmholtz",
				"Masa de la estrella (M_{@SUN})", "Tiempo (a�os)", false, 802, 482);
		chart4.xAxisInLogScale = true;
		chart4.yAxisInLogScale = true;
		// Modifiers (test lot more...)
		chart4.series[0].stroke = new JPARSECStroke(JPARSECStroke.STROKE_DEFAULT_LINE, 3f);
		chart4.series[1].stroke = new JPARSECStroke(JPARSECStroke.STROKE_DEFAULT_LINE, 3f);
		chart4.series[2].stroke = new JPARSECStroke(JPARSECStroke.STROKE_DEFAULT_LINE, 3f);
		chart4.series[3].stroke = new JPARSECStroke(JPARSECStroke.STROKE_DEFAULT_LINE, 3f);
		chart4.series[0].showLines = true;
		chart4.series[1].showLines = true;
		chart4.series[2].showLines = true;
		chart4.series[3].showLines = true;
		chart4.series[0].color = Color.BLACK;
		chart4.series[1].color = Color.BLUE;
		chart4.series[2].color = Color.GREEN;
		chart4.series[3].color = Color.RED;
		chart4.series[0].pointers = new String[] {"10 t_{kh}"};
		chart4.series[1].pointers = new String[] {"10 dM/dt = 10^{-3} M_{@SUN}/a�o"};
		chart4.series[2].pointers = new String[] {"10 dM/dt = 10^{-4} M_{@SUN}/a�o"};
		chart4.series[3].pointers = new String[] {"10 dM/dt = 10^{-5} M_{@SUN}/a�o"};
		chart4.series[1].pointersAngle = ChartSeriesElement.POINTER_ANGLE.TO_OUTSIDE;
		chart4.series[2].pointersAngle = ChartSeriesElement.POINTER_ANGLE.TO_OUTSIDE;
		chart4.series[3].pointersAngle = ChartSeriesElement.POINTER_ANGLE.TO_OUTSIDE;
//		chart4.series[0].showErrorBars = false;
		chart4.yTickLabels = ChartElement.TICK_LABELS.LOGARITHM_VALUES;

		CreateChart ch4 = new CreateChart(chart4);
		ch4.showChartInJFreeChartPanelWithAdvancedControls();
//		Serialization.writeObject(chart4, "/home/alonso/chartTest");
//		Serialization.writeObject(ch4, "/home/alonso/createChartTest");

		CreateChart.showChartsInJFreeChartPanel(new CreateChart[] {ch1, ch2, ch3, ch4}, "My group of charts", false);
*/
/*
        // Export possibilities
		ch4.exportAsScriptForGILDAS("myChart", CreateChart.GILDAS_SHOW_LEYEND_AT_BOTTOM);
		CreateChart.CreateChartAsPNGFile(chart1, "chart.png");
		CreateChart.CreateChartAsSVGFile(chart2, "chart.svg");
		CreateChart.CreateChartAsPDFFile(chart3, "chart.pdf");
		CreateChart.CreateChartAsEPSFile(chart4, "chart.eps");
		ch4.chartAsHTMLFile("chart");
*/

/*
		// Real time charting with X-Y chart (ch1), series 0
		int i = 0;
		double oldTime = new Date().getTime() / 1000.0;
		p1.show("REAL TIME CHART");
		do {
			Date date = new Date();
			double actualTime = date.getTime() / 1000.0;
			double elapsed = actualTime - oldTime;
			if (elapsed >= 1.0) {
				oldTime = actualTime;
				i ++;
				double x[] = DataSet.toDoubleValues(ch1.getChartElement().series[0].xValues);
				double y[] = DataSet.toDoubleValues(ch1.getChartElement().series[0].yValues);
				x = DataSet.addDoubleArray(x, new double[] {i + 3});
				y = DataSet.addDoubleArray(y, new double[] {(i + 3)*3-1});
				ch1.getChartElement().series[0].xValues = DataSet.toStringValues(x);
				ch1.getChartElement().series[0].yValues = DataSet.toStringValues(y);

				// Add a linear fit to the chart
				ch1.getChartElement().series[0].regressionType = REGRESSION.LINEAR;

				// Update the chart
				ch1.updateChart();
			}
			try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
		} while (i<20);
*/	}
}
