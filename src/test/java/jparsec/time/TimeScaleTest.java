package jparsec.time;

import jparsec.ephem.EphemerisElement;
import jparsec.graph.ChartSeriesElement;
import jparsec.graph.CreateChart;
import jparsec.graph.DataSet;
import jparsec.graph.SimpleChartElement;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.util.JPARSECException;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TimeScaleTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("TimeScale Test");

        try {
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            EphemerisElement eph = new EphemerisElement();
            System.out.println(observer.getDSTCode());
/*
            //AstroDate astro = new AstroDate(2013, 3, 31);
            AstroDate astro = new AstroDate(2013, 10, 27);
            double jd = astro.jd();
            for (int i=0; i<60*2.2*60; i++) {
                astro = new AstroDate(jd + i / 86400.0);
                AstroDate ut = new AstroDate(TimeScale.getJD(new TimeElement(astro.jd(), SCALE.LOCAL_TIME), observer, eph, SCALE.UNIVERSAL_TIME_UT1));
                System.out.println(astro.toString()+", DST = "+TimeScale.getDST(ut.jd(), observer)+". UT = "+ut.toString());
            }
            // REMARKS
            // 1. In case of changing 2h LT to 3h LT in spring (DST changes from 0 to 1), 2h LT to 3h LT does not exists, but you can access this range in JPARSEC and resulting UT is correct (DST still 0).
            // 2. In case of changing 2h LT to 1h LT in autumn (DST changes from 1 to 0), 1h LT to 2h LT for DST 0 cannot be accessed. >=2h LT will be available for DST 0.
*/
            System.out.println("dst offset = " + DateTimeOps.dstOffset() + ", time zone offset = " + DateTimeOps.tzOffset());
            AstroDate astro = new AstroDate(2006, AstroDate.JANUARY, 1, 0, 0, 0);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);

            double TTminusUT1 = TimeScale.getTTminusUT1(
                    new TimeElement(new AstroDate(2009, AstroDate.DECEMBER, 1), TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME), observer);
            System.out.println("TT-UT1 for december 2009 = " + TTminusUT1);

            System.out.println("Using TT as input");
            BigDecimal JD = TimeScale.getExactJD(time, observer, eph, TimeElement.SCALE.LOCAL_TIME);
            System.out.println("JD  LT: " + JD);
            JD = TimeScale.getExactJD(time, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            System.out.println("JD UT1: " + JD);
            JD = TimeScale.getExactJD(time, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            System.out.println("JD UTC: " + JD);
            JD = TimeScale.getExactJD(time, observer, eph, TimeElement.SCALE.TERRESTRIAL_TIME);
            System.out.println("JD  TT: " + JD);
            JD = TimeScale.getExactJD(time, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            System.out.println("JD TDB: " + JD);

            System.out.println("Using TDB as input");
            JD = TimeScale.getExactJD(time, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            astro = new AstroDate(JD);
            TimeElement time2 = new TimeElement(astro, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.LOCAL_TIME);
            System.out.println("JD  LT: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            System.out.println("JD UT1: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            System.out.println("JD UTC: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.TERRESTRIAL_TIME);
            System.out.println("JD  TT: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            System.out.println("JD TDB: " + JD);

            System.out.println("Using LT as input");
            JD = TimeScale.getExactJD(time, observer, eph, TimeElement.SCALE.LOCAL_TIME);
            astro = new AstroDate(JD);
            time2 = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.LOCAL_TIME);
            System.out.println("JD  LT: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            System.out.println("JD UT1: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            System.out.println("JD UTC: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.TERRESTRIAL_TIME);
            System.out.println("JD  TT: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            System.out.println("JD TDB: " + JD);

            System.out.println("Using UT1 as input");
            JD = TimeScale.getExactJD(time, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            astro = new AstroDate(JD);
            time2 = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.LOCAL_TIME);
            System.out.println("JD  LT: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            System.out.println("JD UT1: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            System.out.println("JD UTC: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.TERRESTRIAL_TIME);
            System.out.println("JD  TT: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            System.out.println("JD TDB: " + JD);

            System.out.println("Using UTC as input");
            JD = TimeScale.getExactJD(time, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            astro = new AstroDate(JD);
            time2 = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.LOCAL_TIME);
            System.out.println("JD  LT: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UT1);
            System.out.println("JD UT1: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            System.out.println("JD UTC: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.TERRESTRIAL_TIME);
            System.out.println("JD  TT: " + JD);
            JD = TimeScale.getExactJD(time2, observer, eph, TimeElement.SCALE.BARYCENTRIC_DYNAMICAL_TIME);
            System.out.println("JD TDB: " + JD);
            // So there is a numerical error in the millisecond level

            System.out.println("TABLE dT (s) as year");
            ArrayList<Double> x = new ArrayList<Double>();
            ArrayList<Double> y = new ArrayList<Double>();
            // Fit resulting for ancient times: - 140 (9) + 5.0 (1.4) x + 32.19 (0.05) x^{2}, with x = (year - 1820) * 0.01
            // JPL said -20 + 32*x*X
            for (int yr = 1590; yr <= 2200; yr = yr + 2) {
                int year = yr;
                if (year <= 0) year--;
                astro = new AstroDate(year, 1, 1);
                time = new TimeElement(astro.jd(), TimeElement.SCALE.UNIVERSAL_TIME_UT1);
                TTminusUT1 = TimeScale.getTTminusUT1(time, observer);
                System.out.println(yr + "  " + TTminusUT1);
                x.add((double) yr);
                y.add(TTminusUT1);
            }
            SimpleChartElement sce = new SimpleChartElement(DataSet.arrayListToDoubleArray(x),
                    DataSet.arrayListToDoubleArray(y), "TT-UT1", "Year", "TT-UT1", "TT-UT1");
            CreateChart chart = new CreateChart(sce);
            chart.showChartInJFreeChartPanel();

            //chart.getChartElement().series[0].showShapes = false;
            chart.getChartElement().series[0].regressionType = ChartSeriesElement.REGRESSION.POLYNOMIAL;
            chart.getChartElement().series[0].regressionType.setPolynomialDegree(8);
            chart.getChartElement().series[0].regressionType.setShowEquation(true);
            chart.updateChart();

            System.out.println(chart.getChartElement().series[0].regressionType.getEquation());
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
