package jparsec.ephem;

import jparsec.graph.ChartElement;
import jparsec.graph.ChartSeriesElement;
import jparsec.graph.CreateChart;
import jparsec.graph.DataSet;
import jparsec.math.Constant;
import jparsec.time.AstroDate;

import java.awt.Color;

public class ObliquityTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Obliquity test");
        EphemerisElement eph = new EphemerisElement();
        eph.ephemMethod = EphemerisElement.REDUCTION_METHOD.IAU_2006;
        AstroDate astro = new AstroDate(2009, AstroDate.JULY, 1, 0, 0, 0);
        double obl = Obliquity.trueObliquity(Functions.toCenturies(astro.jd()), eph);
        System.out.println(Functions.formatAngle(obl, 5));
        EphemerisElement.REDUCTION_METHOD comparisonModel = EphemerisElement.REDUCTION_METHOD.IAU_2006;
        double t0 = -2000, t1 = 2000, step = 10;
        int np = 1 + (int) ((t1 - t0) / step + 0.5);
        String cm = comparisonModel.name();
        cm = DataSet.replaceAll(cm, "_", " ", true);
        double x1[] = new double[np];
        double x2[] = new double[np];
        double y1[] = new double[np];
        double y2[] = new double[np];
        int index = -1;

        for (double t = t0; t <= t1; t = t + step) {
            double jd = Constant.J2000 + t * Constant.JULIAN_DAYS_PER_CENTURY;
            double cen = Functions.toCenturies(jd);
            System.out.println("JD = " + jd + " / t = " + cen);

            index++;
            x1[index] = cen;
            x2[index] = cen;

            eph.useVondrak2011PrecessionFormulaInsteadOfIAU2006 = false;
            eph.ephemMethod = comparisonModel;
            obl = Obliquity.meanObliquity(cen, eph);
            System.out.println("obl " + cm + " =  " + Functions.formatAngle(obl, 3));
            y1[index] = obl * Constant.RAD_TO_ARCSEC;

            if (Math.abs(cen) > 200) {
                x1[index] = 0;
                y1[index] = 0;

                // Clip IAU 2006 to see models clearly
                if (t == t1) {
                    int imin = DataSet.getIndexOfMinimum(x1);
                    int imax = DataSet.getIndexOfMaximum(x1);
                    x1 = DataSet.getSubArray(x1, imin, imax);
                    y1 = DataSet.getSubArray(y1, imin, imax);
                }
            }

            eph.useVondrak2011PrecessionFormulaInsteadOfIAU2006 = true;
            obl = Obliquity.meanObliquity(cen, eph);
            System.out.println("obl Vondrak   =  " + Functions.formatAngle(obl, 3));
            y2[index] = obl * Constant.RAD_TO_ARCSEC;
        }

        ChartSeriesElement chartSeries1 = new ChartSeriesElement(x1, y1, null, null, cm, true, Color.RED, ChartSeriesElement.SHAPE_EMPTY, ChartSeriesElement.REGRESSION.NONE);
        ChartSeriesElement chartSeries2 = new ChartSeriesElement(x2, y2, null, null, "Vondrak 2011", true, Color.BLUE, ChartSeriesElement.SHAPE_EMPTY, ChartSeriesElement.REGRESSION.NONE);
        chartSeries1.showLines = true;
        chartSeries2.showLines = true;
        ChartSeriesElement series[] = new ChartSeriesElement[] { chartSeries1, chartSeries2 };
        ChartElement chart = new ChartElement(series, ChartElement.TYPE.XY_CHART, ChartElement.SUBTYPE.XY_SCATTER,
                "Comparison of the obliquity of the ecliptic for " + cm + " and Vondrak 2011 models",
                "Julian centuries from J2000", "Obliquity (\")", false, 800, 300);
        CreateChart ch = new CreateChart(chart);
        ch.showChartInJFreeChartPanel();

        // See Vondrak et al. 2011, A&A 534, A22, Figure 4
    }
}
