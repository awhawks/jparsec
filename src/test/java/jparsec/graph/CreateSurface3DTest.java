package jparsec.graph;

import jparsec.astronomy.Difraction;
import jparsec.astronomy.TelescopeElement;

public class CreateSurface3DTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws Exception {
        int w = 800, h = 600;
        float x0 = -2f, y0 = -Math.abs(x0), xf = -x0, yf = -y0;
        TelescopeElement telescope = TelescopeElement.NEWTON_20cm;
        double data[][] = Difraction.pattern(telescope, (int) Math.abs(xf) * 2);
        double values[] = DataSet.getSetOfValues(0, 1, 11, false);

        GridChartElement chart = new GridChartElement("Difraction pattern",
                "@DELTAx (\")", "@DELTAy (\")",
                "Relative Intensity (|@PSI|)",
                GridChartElement.COLOR_MODEL.RED_TO_BLUE,
                new double[] { x0, xf, y0, yf }, data,
                values, 800);

        //chart.subTitle = "Newton 20cm";
        //chart.levelsOrientation = GridChartElement.WEDGE_ORIENTATION.HORIZONTAL_BOTTOM;
        //chart.levelsBorderStyle = GridChartElement.WEDGE_BORDER.NO_BORDER;
        //chart.type = GridChartElement.TYPE.RASTER_CONTOUR;
        //chart.pointers = new String[] {"0 0 1 1 @BLUEArrow from (1, 1) to (0, 0)@BLACK"};

        CreateSurface3D cs1 = new CreateSurface3D(chart);
        cs1.show(w, h, "Surface test 1", true);

        CreateSurface3D cs2 = new CreateSurface3D("Math.sin(x)", new double[] { -5, 5, -5, 5 }, "x", "y", "Functions", GridChartElement.COLOR_MODEL.RED_TO_BLUE);
        /*
        cs2.getModel().setXMin(-2.5f);
        cs2.getModel().setXMax(2.5f);
        cs2.getModel().setYMin(-2.5f);
        cs2.getModel().setYMax(2.5f);
        */
        cs2.show(w, h, "Surface test 2", false);
        //Picture p1 = new Picture(cs1.chartAsBufferedImage());
        //p1.show("My surface chart");

        //Serialization.writeObject(cs1, "/home/alonso/surface3dTest");
    }
}
