package jparsec.model;

import jparsec.astrophysics.MeasureElement;
import jparsec.graph.CreateChart;
import jparsec.util.JPARSECException;

public class RotationalDiagramTest {

    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("RotationalDiagram test");

        try {
            final boolean jpl_cat = true;
            final boolean spline = true;
            String molecule_name = "41001 CH3CN";
            String transition_names[] = new String[] { "91987.0540", "239137.9312" };
            double area[] = new double[] { 11.5 * 0.5 * 0.3996, 73.5 * 0.5 * 0.3594 * 0.24 };
            double area_error[] = new double[] { 2.0 * 0.3996, 11 * 0.5 * 0.3594 * 0.14 };

            // Apply beam filling
            /*
            for (int i=0; i<area.length; i++) {
                area[i] *= Math.pow(DataSet.parseDouble(transition_names[1]) / DataSet.parseDouble(transition_names[i]), 2.0);
                area_error[i] *= Math.pow(DataSet.parseDouble(transition_names[1]) / DataSet.parseDouble(transition_names[i]), 2.0);
            }
            */

            RotationalDiagram dr = new RotationalDiagram(molecule_name, transition_names, jpl_cat, spline, area, area_error);
            MeasureElement trot = new MeasureElement(dr.getTrot(), dr.getTrotError(), "K");
            MeasureElement cden = new MeasureElement(dr.getColumnDensity(), dr.getColumnDensityError(), "(cm^-2)");
            System.out.println("TROT:           " + trot.toString());
            System.out.println("COLUMN DENSITY: " + cden.toString());

            for (int i = 0; i < dr.getX().length; i++) {
                MeasureElement x = new MeasureElement(dr.getX()[i], dr.getXErrors()[i], "K");
                MeasureElement y = new MeasureElement(dr.getY()[i], dr.getYErrors()[i], "(Log Nu/gu)");
                System.out.println("x = " + x.toString() + ", y = " + y.toString());
            }

            int width = 600, height = 600;
            final boolean logScaleX = false, logScaleY = false;
            CreateChart ch = dr.getChart("", width, height, logScaleX, logScaleY);
            ch.showChartInJFreeChartPanel();
            //ch.exportAsPostscriptWithGILDAS("rd", CreateChart.GILDAS_SHOW_LEYEND_AT_BOTTOM);
        } catch (JPARSECException e) {
            JPARSECException.showException(e);
        }
    }
}
