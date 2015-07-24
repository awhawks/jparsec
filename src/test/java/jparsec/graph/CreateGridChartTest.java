package jparsec.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import jparsec.astronomy.Difraction;
import jparsec.astronomy.TelescopeElement;
import jparsec.graph.chartRendering.AWTGraphics;
import jparsec.io.image.Picture;

public class CreateGridChartTest {

    /**
     * For unit testing only.
     * @param args Not used.
     */
    public static void main (String args[]) throws Exception
    {
        System.out.println("CreateGridChart test");

        float x0 = -2f, y0 = -Math.abs(x0), xf = -x0, yf = -y0;
        TelescopeElement telescope = TelescopeElement.NEWTON_20cm;
        double data[][] = Difraction.pattern(telescope, (int) Math.abs(xf)*2);
        double values[] = DataSet.getSetOfValues(0, 1, 11, false);

        CreateGridChart.setLabelFontScalingFactor(1.2);
        GridChartElement chart = new GridChartElement("Difraction pattern",
                "offset x (\")", "offset y (\")",
                "Relative Intensity (|@PSI|)",
                GridChartElement.COLOR_MODEL.RED_TO_BLUE,
                new double[] {x0, xf, y0, yf}, data,
                values, 800);
        chart.subTitle = "Newton 20cm";
        chart.levelsOrientation = GridChartElement.WEDGE_ORIENTATION.VERTICAL_RIGHT;
        chart.levelsBorderStyle = GridChartElement.WEDGE_BORDER.NO_BORDER;
        chart.type = GridChartElement.TYPE.RASTER_CONTOUR;

        chart.pointers = new String[] {"0 0 1 1 @BLUEArrow from (1, 1) to (0, 0)@GREEN"};
        CreateGridChart c = new CreateGridChart(chart);
        //c.showChartInSGTpanel(true);
        Picture p = new Picture(c.chartAsBufferedImage());
        Graphics2D g = p.getImage().createGraphics();
        c.prepareGraphics2D(g, true);
        g.setColor(new Color(0, 255, 0, 128));
        int x = 0, y = 0, r = 1;
        g.setStroke(AWTGraphics.getStroke(new JPARSECStroke(JPARSECStroke.STROKE_DEFAULT_LINE, 0.1f)));
        g.drawOval(x-r, y-r, 2*r, 2*r);
        p.show("");

        //Serialization.writeObject(c, "/home/alonso/gridChartTest");
        //chart.resample(data.length*10, data[0].length*10);
        //CreateGridChart c2 = new CreateGridChart(chart);
        //c2.showChartInSGTpanel(false);
    }
}
