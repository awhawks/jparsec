package jparsec.model;

import java.awt.Color;
import jparsec.graph.ChartElement;
import jparsec.graph.ChartSeriesElement;
import jparsec.graph.CreateChart;
import jparsec.util.JPARSECException;
import jparsec.util.Translate;

public class DustOpacityTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("DustOpacity test");
        //Translate.setDefaultLanguage(Translate.LANGUAGE_SPANISH);

        try {
            // Grain size distributions
            double size[] = new double[] { 1, 100, 10000 };
            Color col[] = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.BLACK };
            // The two grain types for the mixture
            int grain1 = DustOpacity.GRAIN_GRAPHITE;
            int grain2 = DustOpacity.GRAIN_ASTRONOMICAL_SILICATE;
            double p = 3.5;
            double max = size[0];

            // 0.14 and 0.86 and the % of each grain type
            DustOpacity dust1 = new DustOpacity(grain1, p, max, 0.14);
            DustOpacity dust2 = new DustOpacity(grain2, p, max, 0.86);
            ChartSeriesElement series[] = new ChartSeriesElement[size.length];

            for (int i = 0; i < size.length; i++) {
                dust1.sizeMax = size[i];
                dust2.sizeMax = size[i];
                CreateChart charts[] = DustOpacity.getOpacityAndBetaCharts(dust1, dust2, 0.1, 10000.0, true, true);
                ChartElement chartElem = charts[0].getChartElement(); // Return only the opacity chart here
                ChartSeriesElement s[] = chartElem.series;

                for (int j = 0; j < s.length; j++) {
                    s[j].showLines = true;
                    s[j].showShapes = false;
                    s[j].legend = "" + dust1.sizeMax;
                    s[j].color = col[i];
                }

                series[i] = s[2];
            }

            series[0].legend += " (@mum)";
            series[2].legend = "10^{4}";

            // Construct the new chart
            ChartElement chart = new ChartElement(series, ChartElement.TYPE.XY_CHART, ChartElement.SUBTYPE.XY_SCATTER,
                    "Opacity of a mixture of grains for different grain size distributions",
                    Translate.translate(419) + " (@mum)", Translate.translate(339) + " (cm^{2} g^{-1})", false,
                    600, 600);
            chart.showErrorBars = false;
            chart.xTickLabels = ChartElement.TICK_LABELS.LOGARITHM_VALUES;
            chart.yTickLabels = ChartElement.TICK_LABELS.LOGARITHM_VALUES;
            chart.xAxisInLogScale = true;
            chart.yAxisInLogScale = true;
            //Serialization.writeObject(chart, "/home/alonso/grainOpacity");
            CreateChart c = new CreateChart(chart);
            c.showChartInJFreeChartPanel();
        } catch (JPARSECException e) {
            JPARSECException.showException(e);
        }
    }
}
