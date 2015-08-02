package jparsec.graph;

import jparsec.astronomy.Difraction;
import jparsec.astronomy.TelescopeElement;
import jparsec.util.JPARSECException;
import org.jzy3d.colors.Color;

public class CreateChart3DTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("CreateChart3D test");

        try {
            TelescopeElement telescope = TelescopeElement.NEWTON_20cm;
            int field = 3;
            double data[][] = Difraction.pattern(telescope, field);

            GridChartElement gridChart = new GridChartElement("Difraction pattern",
                    "offsetX", "offsetY", "RelativeIntensity", GridChartElement.COLOR_MODEL.RED_TO_BLUE,
                    new double[] { -field, field, -field, field }, data,
                    new double[] { 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0 }, 400);
            //gridChart.resample(data.length*5, data[0].length*5);

            ChartSeriesElement3D series = new ChartSeriesElement3D(gridChart);
            //GridChartElement.createData(-field, field, 1.0 / 5.0),
//					GridChartElement.createData(-field, field, 1.0 / 5.0), data, "Relative intensity (|@PSI|^{2})");
            series.color = Color.RED;

/*			// This is only to test GridChartElement.getDataFromDataSet
            DoubleVector stupidData[] = new DoubleVector[] {
					new DoubleVector(1-2, 1, 0E-10),
					new DoubleVector(2-2, 1, 1E-10),
					new DoubleVector(3-2, 1, 0E-10),
					new DoubleVector(1-2, 2, 1E-10),
					new DoubleVector(2-2, 2, 4E-10),
					new DoubleVector(3-2, 2, 1E-10),
					new DoubleVector(1-2, 3, 0E-10),
					new DoubleVector(2-2, 3, 1E-10),
					new DoubleVector(3-2, 3, 0E-10)
			};
			double x[] = DoubleVector.getAGivenComponent(stupidData, 0);
			double y[] = DoubleVector.getAGivenComponent(stupidData, 1);
			double z[] = DoubleVector.getAGivenComponent(stupidData, 2);
			gridChart.data = GridChartElement.getDataFromDataSet(x, y, z);
			gridChart.limits = GridChartElement.getLimitsFromDataSet(x, y);
			series = new ChartSeriesElement3D(gridChart);
			series.color = Color.RED;
*/

/*			series = ChartSeriesElement3D.getSurfaceFromPoints(new DoubleVector[] {
					new DoubleVector(1-2, 1, 0E-10),
					new DoubleVector(2-2, 1, 1E-10),
					new DoubleVector(3-2, 1, 0E-10),
					new DoubleVector(1-2, 2, 1E-10),
					new DoubleVector(2-2, 2, 4E-10),
					new DoubleVector(3-2, 2, 1E-10),
					new DoubleVector(1-2, 3, 0E-10),
					new DoubleVector(2-2, 3, 1E-10),
					new DoubleVector(3-2, 3, 0E-10)
			}, 20);
*/
/*			series = new ChartSeriesElement3D(
					GridChartElement.createData(-field, field, field / 2.0),
					GridChartElement.createData(-field, field, field / 2.0),
					GridChartElement.createData(-field*10, field*10, field*10 / 2.0), "Relative intensity (|@PSI|^{2})");
			series.zValues = new String[] {"<"+series.xValues[0], ">"+series.xValues[1],
					series.xValues[2],  ">"+series.xValues[3], "<"+series.xValues[4]};
			series.dxValues = new double[] {1, 2, 0, 2, 1};
			series.dyValues = new double[] {1, 2, 0, 2, 1};
			series.dzValues = new double[] {1, 2, 0, 2, 1};
*/

            ChartElement3D chart = new ChartElement3D(new ChartSeriesElement3D[] { series },
                    "Difraction pattern", "@DELTAx (\")", "@DELTAy (\")", "@SIZE20I_{relative} (|@PSI|^{2})");
            chart.showToolbar = false;
            chart.showLegend = false;
            chart.showTitle = false;
            //chart.showGridX = chart.showGridY = chart.showGridZ = false;
            CreateChart3D c = new CreateChart3D(chart);
            c.showChart(500, 500);

//			Serialization.writeObject(chart, "/home/alonso/chart3dTest");
//			Serialization.writeObject(c, "/home/alonso/createChart3dTest");
            System.out.println(c.getIntensityAt(2, 2, series.legend));
            System.out.println(c.getIntensityAt(0, 0, series.legend));
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
