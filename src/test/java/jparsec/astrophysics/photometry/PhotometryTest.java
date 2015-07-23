package jparsec.astrophysics.photometry;

import jparsec.astrophysics.MeasureElement;
import jparsec.astrophysics.gildas.Spectrum30m;
import jparsec.graph.ChartElement;
import jparsec.graph.ChartSeriesElement;
import jparsec.graph.CreateChart;
import jparsec.graph.DataSet;
import jparsec.math.Constant;
import jparsec.util.JPARSECException;

import java.awt.*;

public class PhotometryTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Photometry test");

        try {
            double mag = 7.461, dmag = 0.03;
            MeasureElement flux = Photometry.getFluxFromMagnitude(mag, dmag, PhotometricBandElement.BAND_J_2MASS);
            System.out.println("Flux " + flux.toString());
            MeasureElement m = Photometry.getMagnitudeFromFlux(flux.getValue(), flux.error, PhotometricBandElement.BAND_J_2MASS);
            System.out.println("mag " + m.toString());

            // Another easier way, just
            m.convert(MeasureElement.UNIT_Y_JY);
            System.out.println("Flux " + m.toString());

            // Testing case for Vega: color indexes should be zero if vega flag is true, or very close at least if vega flag is false
            double Teff = 9700;

            Photometry.FILTER filter1 = Photometry.FILTER.B_JOHNSON;
            Photometry.FILTER filter2 = Photometry.FILTER.V_JOHNSON;
            String name = filter1.name() + "-" + filter2.name();
            double color = Photometry.getBlackBodyColorIndex(Teff, filter1, filter2, true);
            System.out.println(name + " (Tef = " + Teff + ", using black body = incorrect) = " + color);
            double mass = 2.2, r = 2.5;
            color = Photometry.getColorIndexUsingKuruczModels(mass, r, Teff, filter1, filter2, true);
            System.out.println(name + " (Tef = " + Teff + ") = " + color);

            // Now we do some color index calculations for the SUN, using black bodies and Kurucz stellar models,
            // and Vega as reference. The last 'true' means that we will use Vega spectrum as reference for
            // magnitude 0. If set to false, a synthetic star (black body) will be used.
            Teff = 5770;
            mass = 1.0;
            r = 1.0;
            color = Photometry.getBlackBodyColorIndex(Teff, filter1, filter2, true);
            System.out.println(name + " (Tef = " + Teff + ", using black body = incorrect) = " + color); // quite unaccurate
            color = Photometry.getColorIndexUsingKuruczModels(mass, r, Teff, filter1, filter2, true);
            System.out.println(name + " (Tef = " + Teff + ") = " + color); // better
            // Should be close to 0.656 or 0.67 depending on authors, so Kurucz models are more accurate, as expected

            // Show a chart with the filters
            ChartSeriesElement series[] = new ChartSeriesElement[5];
            Color[] col = new Color[] { Color.MAGENTA, Color.BLUE, Color.YELLOW, Color.RED, Color.ORANGE };

            for (int i = 0; i < 5; i++) {
                series[i] = new ChartSeriesElement(Photometry.getFilterWavelengths(Photometry.FILTER.values()[i]),
                        Photometry.getFilterTransmitancy(Photometry.FILTER.values()[i])
                        , null, null, Photometry.FILTER.values()[i].name(), true, col[i],
                        ChartSeriesElement.SHAPE_EMPTY, ChartSeriesElement.REGRESSION.NONE);
                series[i].showLines = true;
                series[i].showShapes = false;
            }

            ChartElement chart = new ChartElement(series, ChartElement.TYPE.XY_CHART,
                    ChartElement.SUBTYPE.XY_SCATTER,
                    "Johnson UBVRI filter transmitancies",
                    "Wavelength (@mum)", "Transmitancy", false, 800, 480);
            CreateChart ch = new CreateChart(chart);
            ch.showChartInJFreeChartPanelWithAdvancedControls();

            // Cross test
            CreateChart ch2 = ((Photometry.getFilterSpectrum(Photometry.FILTER.values()[0], 100))).getChart(800, 600, Spectrum30m.XUNIT.FREQUENCY_MHZ);
            //CreateChart ch2 = (((new Kurucz(1, 1, 8000)).getSpectrum(1000))).getChart(800, 600, Spectrum30m.XUNIT.FREQUENCY_MHZ);
            ch2.getChartElement().series[0].xValues = DataSet.toStringValues(DataSet.applyFunction(Constant.SPEED_OF_LIGHT + "/x", DataSet.toDoubleValues(ch2.getChartElement().series[0].xValues)));
            ch2.getChartElement().yAxisInLogScale = true;
            ch2.updateChart();
            ch2.showChartInJFreeChartPanel();
        } catch (JPARSECException exc) {
            exc.showException();
        }
    }
}
